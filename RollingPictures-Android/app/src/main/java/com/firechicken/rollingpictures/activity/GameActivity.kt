package com.firechicken.rollingpictures.activity

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.ProgressBar
import com.firechicken.rollingpictures.fragment.GameDrawingFragment
import com.firechicken.rollingpictures.R
import com.firechicken.rollingpictures.databinding.ActivityGameBinding
import android.os.Handler
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.firechicken.rollingpictures.dialog.PermissionsDialogFragment
import com.firechicken.rollingpictures.webrtc.CustomHttpClient
import com.firechicken.rollingpictures.webrtc.CustomWebSocket
import com.firechicken.rollingpictures.webrtc.openvidu.LocalParticipant
import com.firechicken.rollingpictures.webrtc.openvidu.Session
import okhttp3.Call
import okhttp3.Callback
import okhttp3.RequestBody
import okhttp3.Response
import okhttp3.MediaType.Companion.toMediaTypeOrNull

import org.json.JSONException
import org.json.JSONObject
import java.io.IOException

class GameActivity : AppCompatActivity() {

    private val MY_PERMISSIONS_REQUEST_CAMERA = 100
    private val MY_PERMISSIONS_REQUEST_RECORD_AUDIO = 101
    private val MY_PERMISSIONS_REQUEST = 102
    private val TAG = "SessionActivity"
    private var OPENVIDU_URL: String? = null
    private var OPENVIDU_SECRET: String? = null
    private var session: Session? = null
    private var httpClient: CustomHttpClient? = null
    private var isVoice: Boolean = false
    private lateinit var activityGameActivity:ActivityGameBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        val activityGameBinding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(activityGameBinding.root)

        val progressGrow = AnimationUtils.loadAnimation(this, R.anim.grow)
        val timeProgressBar = findViewById<ProgressBar>(R.id.timeProgressBar)
        timeProgressBar.startAnimation(progressGrow)

//        val transaction = supportFragmentManager.beginTransaction().add(R.id.frameLayout, GameWritingFragment())
//        transaction.commit()

        val transaction =
            supportFragmentManager.beginTransaction().add(R.id.frameLayout, GameDrawingFragment())
        transaction.commit()

//        val transaction = supportFragmentManager.beginTransaction().add(R.id.frameLayout, GameFinishFragment())
//        transaction.commit()

    }

    // 권한을 요청함
    fun askForPermissions() {
        if ((ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                    != PackageManager.PERMISSION_GRANTED)
        ) {
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.RECORD_AUDIO),
                MY_PERMISSIONS_REQUEST
            )
        }
    }

    fun buttonPressed(view: View?) {
        // 스피커 통신 토글
        if(isVoice){
            activityGameActivity.headSetImageButton.setImageResource(R.drawable.ic_baseline_headset_off_24)
            leaveSession()
            isVoice = false
            return
        }
        /* 권한이 있다면 통신 시작함
            OPENVIDU_URL : AWS url
            OPENVIDU_SECRET : AWS로 들어가기 위한 PW?
            sessionId : 방
            participantName : 사용자 입장 이름
         */
        if (arePermissionGranted()) {
            isVoice = true
            activityGameActivity.headSetImageButton.setImageResource(R.drawable.ic_baseline_headset_24)
            OPENVIDU_URL = "https://jwsh.link:8011"
            OPENVIDU_SECRET = "ssafy0107"
            httpClient = CustomHttpClient(
                OPENVIDU_URL!!,
                "Basic " + Base64.encodeToString(
                    "OPENVIDUAPP:$OPENVIDU_SECRET".toByteArray(),
                    Base64.DEFAULT
                ).trim { it <= ' ' })
            val sessionId = "SessionA"
            getToken(sessionId)
        } else {
            val permissionsFragment: DialogFragment = PermissionsDialogFragment(this@GameActivity)
            permissionsFragment.show(supportFragmentManager, "Permissions Fragment")
        }
    }

    private fun getToken(sessionId: String) {
        try {
            // Session Request
            val sessionBody = RequestBody.create(
                "application/json; charset=utf-8".toMediaTypeOrNull(),
                "{\"customSessionId\": \"$sessionId\"}"
            )
            //http 통신
            httpClient!!.httpCall(
                "/openvidu/api/sessions",
                "POST",
                "application/json",
                sessionBody,
                object : Callback {
                    @Throws(IOException::class)
                    // 일단 openvidu에 연결이 되면 > 해당 방에 접속함
                    override fun onResponse(call: Call, response: Response) {
                        Log.d(TAG, "responseString: " + response.body!!.string())
                        // Token Request
                        val tokenBody =
                            RequestBody.create("application/json; charset=utf-8".toMediaTypeOrNull(), "{}")

                        httpClient!!.httpCall(
                            "/openvidu/api/sessions/$sessionId/connection",
                            "POST",
                            "application/json",
                            tokenBody,
                            object : Callback {
                                override fun onResponse(call: Call, response: Response) {

                                    var responseString: String? = null

                                    // response를 JSONOBJECT로 변환
                                    try {
                                        responseString = response.body!!.string()
                                    } catch (e: IOException) {
                                        Log.e(TAG, "Error getting body", e)
                                    }
                                    Log.d(TAG, "responseString2: $responseString")

                                    var tokenJsonObject: JSONObject? = null
                                    var token: String? = null

                                    // JSONOBJECT에서 token 값을 가져옴
                                    try {
                                        tokenJsonObject = JSONObject(responseString)
                                        token = tokenJsonObject.getString("token")
                                    } catch (e: JSONException) {
                                        e.printStackTrace()
                                    }

                                    // 가져온 token 값으로 session을 만들어 방에 입장
                                    getTokenSuccess(token, sessionId)
                                }

                                override fun onFailure(call: Call, e: IOException) {
                                    Log.e(TAG, "Error POST /api/tokens", e)
                                    connectionError()
                                }
                            })
                    }

                    override fun onFailure(call: Call, e: IOException) {
                        Log.e(TAG, "Error POST /api/sessions", e)
                        connectionError()
                    }
                })
        } catch (e: IOException) {
            Log.e(TAG, "Error getting token", e)
            e.printStackTrace()
            connectionError()
        }
    }

    // 가져온 token 값으로 session을 만들어 방에 입장
    private fun getTokenSuccess(token: String?, sessionId: String) {
        // Initialize our session
        session = Session(sessionId, token!!, this)

        val participantName = "seokgyu"

        val localParticipant = LocalParticipant(participantName, session!!)
        localParticipant.startAudio()

        // Initialize and connect the websocket to OpenVidu Server
        startWebSocket()
    }

    // url과 생성한 session으로 websocket 실행
    private fun startWebSocket() {
        val webSocket = CustomWebSocket(session!!, OPENVIDU_URL!!, this)
        webSocket.execute()
        session!!.setWebSocket(webSocket)
    }

    // 연결 에러
    private fun connectionError() {
        val myRunnable = Runnable {
            val toast =
                Toast.makeText(this, "Error connecting to $OPENVIDU_URL", Toast.LENGTH_LONG)
            toast.show()
        }
        Handler(this.mainLooper).post(myRunnable)
    }

    // 연결을 끊고 세션을 떠남
    fun leaveSession() {
        session?.leaveSession()
        httpClient?.dispose()
    }

    // 권한이 받아졌음을 boolean으로 return
    private fun arePermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.RECORD_AUDIO
        ) != PackageManager.PERMISSION_DENIED
    }

    // 어플리케이션이 Destroy 됐을 때
    override fun onDestroy() {
        leaveSession()
        super.onDestroy()
    }

    // 어플리케이션에서 BackPressed 됐을 때
    override fun onBackPressed() {
        leaveSession()
        super.onBackPressed()
    }

    // 어플리케이션이 Stop 됐을 때
    override fun onStop() {
        leaveSession()
        super.onStop()
    }
}