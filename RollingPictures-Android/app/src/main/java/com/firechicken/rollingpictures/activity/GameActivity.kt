package com.firechicken.rollingpictures.activity

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.AnimationUtils
import com.firechicken.rollingpictures.R
import android.os.Handler
import android.os.Looper
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.firechicken.rollingpictures.config.ApplicationClass
import com.firechicken.rollingpictures.config.ApplicationClass.Companion.channelResDTO
import com.firechicken.rollingpictures.config.ApplicationClass.Companion.fragmentNum
import com.firechicken.rollingpictures.config.ApplicationClass.Companion.gameChannelResDTO
import com.firechicken.rollingpictures.config.ApplicationClass.Companion.playerList
import com.firechicken.rollingpictures.config.ApplicationClass.Companion.playerRecyclerViewAdapter
import com.firechicken.rollingpictures.config.ApplicationClass.Companion.prefs
import com.firechicken.rollingpictures.config.ApplicationClass.Companion.recyclerView
import com.firechicken.rollingpictures.config.ApplicationClass.Companion.roundNum
import com.firechicken.rollingpictures.databinding.ActivityGameBinding
import com.firechicken.rollingpictures.dialog.GameExitDialog
import com.firechicken.rollingpictures.dialog.PermissionsDialogFragment
import com.firechicken.rollingpictures.dto.*
import com.firechicken.rollingpictures.fragment.GameDrawingFragment
import com.firechicken.rollingpictures.fragment.GameFinishFragment
import com.firechicken.rollingpictures.fragment.GameWaitingFragment
import com.firechicken.rollingpictures.fragment.GameWritingFragment
import com.firechicken.rollingpictures.service.ChannelService
import com.firechicken.rollingpictures.util.RetrofitCallback
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
import com.firechicken.rollingpictures.webrtc.util.EarPhoneIntentListener
import com.google.gson.GsonBuilder
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_game.*
import kotlinx.android.synthetic.main.fragment_game_waiting.*
import kotlinx.android.synthetic.main.fragment_game_waiting.view.*
import ua.naiksoftware.stomp.Stomp
import ua.naiksoftware.stomp.StompClient
import ua.naiksoftware.stomp.dto.LifecycleEvent
import ua.naiksoftware.stomp.dto.StompHeader
import java.util.ArrayList

private const val TAG = "GameActivity_??????"

class GameActivity : AppCompatActivity() {

    // webRTC ?????? ??????
    private val MY_PERMISSIONS_REQUEST = 102
    private val SSESION_TAG = "Session"
    private var OPENVIDU_URL: String? = null
    private var OPENVIDU_SECRET: String? = null
    private var session: Session? = null
    private var httpClient: CustomHttpClient? = null
    private var isVoice: Boolean = false

    val USERID = "userId"
    val PASSCODE = "passcode"

    // ????????? ?????? ??????
    var mStompClient: StompClient? = null
    private var compositeDisposable: CompositeDisposable? = null
    private val mRestPingDisposable: Disposable? = null
    private val mGson = GsonBuilder().create()

    private lateinit var activityGameActivity: ActivityGameBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        activityGameActivity = ActivityGameBinding.inflate(layoutInflater)
        setContentView(activityGameActivity.root)

        gameChannelResDTO = SingleResult (GameChannelResDTO(0),"",0)

        // ?????? ????????? ?????? ??? ?????? ??????
        for(users in channelResDTO.data.users){
            playerList.add(users)
        }

        // ?????? ???????????? ?????? ?????? ??????????????? ??????
        val transaction =
            supportFragmentManager.beginTransaction().replace(R.id.frameLayout, GameWaitingFragment())
        transaction.commit()

        //websocket URL ??????
        mStompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, ApplicationClass.websocketURL)

        connectStomp()

        if (!arePermissionGranted()) {
            val permissionsFragment: DialogFragment = PermissionsDialogFragment(this@GameActivity)
            permissionsFragment.show(supportFragmentManager, "Permissions Fragment")
        }
    }

    // ????????? ??????????????? boolean?????? return
    private fun arePermissionGranted(): Boolean {
        return (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_DENIED)
                && (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_DENIED)
                && (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_DENIED)
    }

    // ?????? ?????????????????? ????????? ?????? ?????? ????????????, ????????? ?????????
    fun askForPermissions() {
        if ((ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                    != PackageManager.PERMISSION_GRANTED)||
                (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED)||
                (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED)
        ) {
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.RECORD_AUDIO
                    , Manifest.permission.READ_EXTERNAL_STORAGE
                    , Manifest.permission.WRITE_EXTERNAL_STORAGE),
                MY_PERMISSIONS_REQUEST
            )
        }
    }

    // ????????? ???????????? ???????????? ??????????????? ???????????? ?????????
    // ?????? ???????????? ?????? ??????, ??????????????? ????????????.
    fun buttonPressed(view: View) {

        view.isEnabled = false
        Handler(Looper.getMainLooper()).postDelayed({
            view.isEnabled = true
        }, 2000)

        // ?????? ?????? ?????? ??? ????????? ????????? ?????? ????????? ?????????
        if(isVoice){
            activityGameActivity.headSetImageButton.setImageResource(R.drawable.ic_baseline_headset_off_24)
            leaveSession()
            isVoice = false
            return
        }
        /* ????????? ????????? ?????? ?????????
            OPENVIDU_URL : AWS url
            OPENVIDU_SECRET : OpenVidu??? ???????????? ?????? PW
            sessionId : ???
            participantName : ????????? ?????? ??????
         */
        if (arePermissionGranted()) {
            isVoice = true
            activityGameActivity.headSetImageButton.setImageResource(R.drawable.ic_baseline_headset_24)
            OPENVIDU_URL = "https://i6d208.p.ssafy.io:8011"
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

    // ????????????
    private fun getToken(sessionId: String) {
        try {
            // Session Request
            val sessionBody = RequestBody.create(
                "application/json; charset=utf-8".toMediaTypeOrNull(),
                "{\"customSessionId\": \"$sessionId\"}"
            )
            //http ??????
            httpClient!!.httpCall(
                "/openvidu/api/sessions",
                "POST",
                "application/json",
                sessionBody,
                object : Callback {
                    @Throws(IOException::class)
                    // ?????? openvidu??? ????????? ?????? > ?????? ?????? ?????????
                    override fun onResponse(call: Call, response: Response) {
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

                                    // response??? JSONOBJECT??? ??????
                                    try {
                                        responseString = response.body!!.string()
                                    } catch (e: IOException) {
                                        Log.e(SSESION_TAG, "Error getting body", e)
                                    }
                                    Log.d(SSESION_TAG, "responseString2: $responseString")

                                    var tokenJsonObject: JSONObject? = null
                                    var token: String? = null

                                    // JSONOBJECT?????? token ?????? ?????????
                                    try {
                                        tokenJsonObject = JSONObject(responseString)
                                        token = tokenJsonObject.getString("token")
                                    } catch (e: JSONException) {
                                        e.printStackTrace()
                                    }

                                    // ????????? token ????????? session??? ????????? ?????? ??????
                                    getTokenSuccess(token, sessionId)
                                }

                                override fun onFailure(call: Call, e: IOException) {
                                    connectionError()
                                }
                            })
                    }

                    override fun onFailure(call: Call, e: IOException) {
                        connectionError()
                    }
                })
        } catch (e: IOException) {
            e.printStackTrace()
            connectionError()
        }
    }

    // ????????? token ????????? session??? ????????? ?????? ??????
    private fun getTokenSuccess(token: String?, sessionId: String) {
        // Initialize our session
        session = Session(sessionId, token!!, this)

        EarPhoneIntentListener.getInstance(applicationContext, session!!)?.init()

        val participantName = "seokgyu"

        val localParticipant = LocalParticipant(participantName, session!!)
        localParticipant.startAudio()

        // Initialize and connect the websocket to OpenVidu Server
        startWebSocket()
    }

    // url??? ????????? session?????? websocket ??????
    private fun startWebSocket() {
        val webSocket = CustomWebSocket(session!!, OPENVIDU_URL!!, this)
        webSocket.execute()
        session!!.setWebSocket(webSocket)
    }

    // ?????? ??????
    private fun connectionError() {
        val myRunnable = Runnable {
            toast("Error connecting to $OPENVIDU_URL")
        }
        Handler(this.mainLooper).post(myRunnable)
    }

    // ????????? ?????? ????????? ??????
    fun leaveSession() {
        session?.leaveSession()
        httpClient?.dispose()
    }

    private fun resetSubscriptions() {
        if (compositeDisposable != null) {
            compositeDisposable!!.dispose()
        }
        compositeDisposable = CompositeDisposable()
    }

    //Stomp ??????
    private fun connectStomp() {
        val headers: MutableList<StompHeader> = ArrayList()

        headers.add(StompHeader(USERID, "${prefs.getId()}"))
        headers.add(StompHeader(PASSCODE, "guest"))
        mStompClient!!.withClientHeartbeat(1000).withServerHeartbeat(1000)
        resetSubscriptions()
        val dispLifecycle: Disposable = mStompClient!!.lifecycle()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { lifecycleEvent ->
                when (lifecycleEvent.getType()) {
                    LifecycleEvent.Type.OPENED -> toast("Stomp connection opened")
                    LifecycleEvent.Type.ERROR -> toast("Stomp connection error")
                    LifecycleEvent.Type.CLOSED -> {
                        toast("Stomp connection closed")
                        resetSubscriptions()
                    }
                    LifecycleEvent.Type.FAILED_SERVER_HEARTBEAT -> toast("Stomp failed server heartbeat")
                }
            }
        compositeDisposable!!.add(dispLifecycle)


        val dispTopic: Disposable = mStompClient!!.topic("/channel/in/${channelResDTO.data.code}") //????????? Stomp URI
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ topicMessage ->

                //channel/in ????????? ???????????? ??? ????????? ??????
                val user = mGson.fromJson(topicMessage.getPayload(), UserInfoResDTO::class.java)
                addPlayer(user)

                val transaction = supportFragmentManager.beginTransaction().replace(R.id.frameLayout, GameWaitingFragment())
                transaction.commit()

            }) { throwable -> Log.e(TAG, "Error on subscribe topic", throwable) }

        val dispTopic2: Disposable = mStompClient!!.topic("/channel/out/${channelResDTO.data.code}")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ topicMessage ->

                //channel/out ????????? ???????????? ??? ????????? ??????
                val user = mGson.fromJson(topicMessage.getPayload(), UserInfoResDTO::class.java)
                removePlayer(user)

                //GameWaitingFragment??? ????????????
                if(fragmentNum == 0){
                    val transaction = supportFragmentManager.beginTransaction().replace(R.id.frameLayout, GameWaitingFragment())
                    transaction.commit()
                }

            }) { throwable -> Log.e(TAG, "Error on subscribe topic", throwable) }

        val dispTopic3: Disposable = mStompClient!!.topic("/channel/start/${channelResDTO.data.code}")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ topicMessage ->

                roundNum=1
                var res = mGson.fromJson(topicMessage.getPayload(), Long::class.java)
                gameChannelResDTO.data.id = res
                gameChannelResDTO.msg = "??????"
                gameChannelResDTO.output = 1

                val transaction = supportFragmentManager.beginTransaction().replace(R.id.frameLayout, GameWritingFragment())
                transaction.commit()

            }) { throwable -> Log.e(TAG, "Error on subscribe topic", throwable) }

        val dispTopic4: Disposable = mStompClient!!.topic("/channel/leader/${channelResDTO.data.code}")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ topicMessage ->

                //channel/leader ????????? ???????????? ??? ????????? ??????
                val user = mGson.fromJson(topicMessage.getPayload(), UserInfoResDTO::class.java)
                updateLeader(user)

                if(fragmentNum == 0){
                    val transaction = supportFragmentManager.beginTransaction().replace(R.id.frameLayout, GameWaitingFragment())
                    transaction.commit()
                }

            }) { throwable -> Log.e(TAG, "Error on subscribe topic", throwable) }

        val dispTopic5: Disposable = mStompClient!!.topic("/channel/game/next/${channelResDTO.data.code}")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ topicMessage ->

                //channel/game/next ????????? ???????????? ??? ????????? ??????
                roundNum = mGson.fromJson(topicMessage.getPayload(), Int::class.java)
                if(roundNum%2==1){
                    val transaction = supportFragmentManager.beginTransaction().replace(R.id.frameLayout, GameWritingFragment())
                    transaction.commit()
                }else{
                    val transaction = supportFragmentManager.beginTransaction().replace(R.id.frameLayout, GameDrawingFragment())
                    transaction.commit()
                }

            }) { throwable -> Log.e(TAG, "Error on subscribe topic", throwable) }

        val dispTopic6: Disposable = mStompClient!!.topic("/channel/setting/${channelResDTO.data.code}")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ topicMessage ->

                val channelStmpResDto = mGson.fromJson(topicMessage.getPayload(), ChannelResDTO::class.java)
                channelResDTO.data = channelStmpResDto

                val transaction = supportFragmentManager.beginTransaction().replace(R.id.frameLayout, GameWaitingFragment())
                transaction.commit()

            }) { throwable -> Log.e(TAG, "Error on subscribe topic", throwable) }


        //?????? ???
        val dispTopic7: Disposable = mStompClient!!.topic("/channel/end/${channelResDTO.data.code}")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ topicMessage ->

                var res = mGson.fromJson(topicMessage.getPayload(), Long::class.java)

                val transaction = supportFragmentManager.beginTransaction().replace(R.id.frameLayout, GameFinishFragment())
                transaction.commit()

            }) { throwable -> Log.e(TAG, "Error on subscribe topic", throwable) }

        compositeDisposable!!.add(dispTopic)
        compositeDisposable!!.add(dispTopic2)
        compositeDisposable!!.add(dispTopic3)
        compositeDisposable!!.add(dispTopic4)
        compositeDisposable!!.add(dispTopic5)
        compositeDisposable!!.add(dispTopic6)
        compositeDisposable!!.add(dispTopic7)
        mStompClient!!.connect(headers) //?????? ??????
    }

    //player ??????
    private fun addPlayer(user: UserInfoResDTO) {
        channelResDTO.data.users.add(user)
        playerList = channelResDTO.data.users
        playerRecyclerViewAdapter.notifyDataSetChanged()
        recyclerView.smoothScrollToPosition(playerList.size - 1)
    }

    //player ??????
    private fun removePlayer(user: UserInfoResDTO) {
        channelResDTO.data.users.remove(user)
        playerList = channelResDTO.data.users
        playerRecyclerViewAdapter.notifyDataSetChanged()
        recyclerView.smoothScrollToPosition(playerList.size - 1)
    }

    //Leader ??????
    private fun updateLeader(user: UserInfoResDTO) {
        channelResDTO.data.apply{
            for(i: Int in 0..users.size-1){
                if(users[i].id==user.id){
                    users[i].isLeader="Y"
                }
            }
            playerList = users
        }
        playerRecyclerViewAdapter.notifyDataSetChanged()
    }

    // ????????? ?????? ??????
    fun disconnectStomp() {
        mStompClient!!.disconnect()
    }

    // ???????????????????????? BackPressed ?????? ???
    override fun onBackPressed() {
        val dialog = GameExitDialog(this)
        dialog.showDialog()
        dialog.setOnClickListener(object : GameExitDialog.OnDialogClickListener {
            override fun onDialogOkClick() {
                outChannel(channelResDTO.data.code, prefs.getId()!!)
                prefs.setEnteredChannel("none")
                dialog.dialog.dismiss()
                finish()
            }
        })
    }

    // ???????????? ???????????? ?????????
    fun outChannel(code: String, userId: Long) {
        val req = InOutChannelReqDTO(code, userId)
        ChannelService().outChannel(req, object : RetrofitCallback<SingleResult<Any>> {
            override fun onSuccess(code: Int, responseData: SingleResult<Any>) {
                if (responseData.output == 1) {
                    channelResDTO = SingleResult (
                        ChannelResDTO(-1,"","","N",0,0, mutableListOf())
                        ,"",0)
                    playerList = mutableListOf()
                } else {
                    toast("outChannel?????? ????????? ?????????????????????. ?????? ??????????????????.")
                }
            }

            override fun onFailure(code: Int) {
                toast("outChannel?????? ??????????????? ?????????????????????. ?????? ??????????????????.")
            }

            override fun onError(t: Throwable) {
                toast("outChannel?????? ??????????????? ?????????????????????. ?????? ??????????????????.")
            }
        })
    }

    // ????????? ?????? ????????? ???????????? ???
    fun updateChannel(changedChannel: MakeChannelReqDTO) {

        ChannelService().updateChannel(changedChannel, object : RetrofitCallback<SingleResult<ChannelResDTO>> {
            override fun onSuccess(code: Int, responseData: SingleResult<ChannelResDTO>) {
                if (responseData.data.id > 0) {
                    channelResDTO = responseData
                } else {
                    toast("makeChannel?????? ????????? ?????????????????????. ?????? ??????????????????.")
                }
            }

            override fun onFailure(code: Int) {
                toast("makeChannel?????? ??????????????? ?????????????????????. ?????? ??????????????????.")
            }

            override fun onError(t: Throwable) {
                toast("makeChannel?????? ??????????????? ?????????????????????. ?????? ??????????????????.")
            }
        })
    }

    // ????????????????????? Stop ?????? ???
    override fun onStop() {
        fragmentNum = -1
        leaveSession()
        disconnectStomp()
        activityGameActivity.headSetImageButton.setImageResource(R.drawable.ic_baseline_headset_off_24)
        isVoice = false
        if (mRestPingDisposable != null) mRestPingDisposable.dispose()
        if (compositeDisposable != null) compositeDisposable!!.dispose()
        super.onStop()
    }

    override fun onRestart() {
        connectStomp()
        super.onRestart()
    }

    private fun toast(text: String) {
        Toast.makeText(this@GameActivity, text, Toast.LENGTH_SHORT).show()
    }
}
