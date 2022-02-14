package com.firechicken.rollingpictures.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.AnimationUtils
import com.firechicken.rollingpictures.R
import android.os.Handler
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import com.firechicken.rollingpictures.config.ApplicationClass
import com.firechicken.rollingpictures.config.ApplicationClass.Companion.channelResDTO
import com.firechicken.rollingpictures.config.ApplicationClass.Companion.gameChannelResDTO
import com.firechicken.rollingpictures.config.ApplicationClass.Companion.loginUserResDTO
import com.firechicken.rollingpictures.config.ApplicationClass.Companion.playerList
import com.firechicken.rollingpictures.config.ApplicationClass.Companion.playerRecyclerViewAdapter
import com.firechicken.rollingpictures.config.ApplicationClass.Companion.prefs
import com.firechicken.rollingpictures.config.ApplicationClass.Companion.recyclerView
import com.firechicken.rollingpictures.config.ApplicationClass.Companion.roundNum
import com.firechicken.rollingpictures.databinding.ActivityGameBinding
import com.firechicken.rollingpictures.dialog.GameExitDialog
import com.firechicken.rollingpictures.dialog.GameSettingDialog
import com.firechicken.rollingpictures.dialog.PermissionsDialogFragment
import com.firechicken.rollingpictures.dto.*
import com.firechicken.rollingpictures.fragment.GameDrawingFragment
import com.firechicken.rollingpictures.fragment.GameWaitingFragment
import com.firechicken.rollingpictures.fragment.GameWritingFragment
import com.firechicken.rollingpictures.service.ChannelService
import com.firechicken.rollingpictures.service.SectionService
import com.firechicken.rollingpictures.service.UserService
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

private const val TAG = "GameActivity_싸피"

class GameActivity : AppCompatActivity() {

    // webRTC 관련 변수
    private val MY_PERMISSIONS_REQUEST = 102
    private val SSESION_TAG = "Session"
    private var OPENVIDU_URL: String? = null
    private var OPENVIDU_SECRET: String? = null
    private var session: Session? = null
    private var httpClient: CustomHttpClient? = null
    private var isVoice: Boolean = false

    val USERID = "userId"
    val PASSCODE = "passcode"

    // 스텀프 관련 변수
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

        // 시간 표시 (게임진행 중에만 사용함)
        val progressGrow = AnimationUtils.loadAnimation(this, R.anim.grow)
        //activityGameActivity.timeProgressBar.startAnimation(progressGrow)
        activityGameActivity.timeProgressBar.visibility = View.GONE

        // 초기 게임방 대기 시 인원 설정
        for(users in channelResDTO.data.users){
            playerList.add(users)
        }

        // 처음 들어왔을 때는 게임 대기방으로 입장
        val transaction =
            supportFragmentManager.beginTransaction().replace(R.id.frameLayout, GameWaitingFragment())
        transaction.commit()

        //websocket URL 지정
        mStompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, ApplicationClass.websocketURL)

        Log.d(SSESION_TAG, "onCreate1: ")
        connectStomp()
        Log.d(SSESION_TAG, "onCreate2: ")

    }

    // 권한이 받아졌음을 boolean으로 return
    private fun arePermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.RECORD_AUDIO
        ) != PackageManager.PERMISSION_DENIED
    }

    // 메인 액티비티에서 권한을 허가 받지 못했으면, 권한을 요청함
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


    // 헤드셋 아이콘을 클릭하여 음성채팅을 실시하는 메소드
    // 이미 음성채팅 중인 경우, 음성채팅을 해제한다.
    fun buttonPressed(view: View?) {

        // 음성 통신 토글 및 오디오 매니저 노말 모드로 초기화
        if(isVoice){
            activityGameActivity.headSetImageButton.setImageResource(R.drawable.ic_baseline_headset_off_24)
            leaveSession()
            isVoice = false
            return
        }
        /* 권한이 있다면 통신 시작함
            OPENVIDU_URL : AWS url
            OPENVIDU_SECRET : OpenVidu로 들어가기 위한 PW
            sessionId : 방
            participantName : 사용자 입장 이름
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

    // 음성채팅
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
                        Log.d(SSESION_TAG, "responseString: " + response.body!!.string())
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
                                        Log.e(SSESION_TAG, "Error getting body", e)
                                    }
                                    Log.d(SSESION_TAG, "responseString2: $responseString")

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
                                    Log.e(SSESION_TAG, "Error POST /api/tokens", e)
                                    connectionError()
                                }
                            })
                    }

                    override fun onFailure(call: Call, e: IOException) {
                        Log.e(SSESION_TAG, "Error POST /api/sessions", e)
                        connectionError()
                    }
                })
        } catch (e: IOException) {
            Log.e(SSESION_TAG, "Error getting token", e)
            e.printStackTrace()
            connectionError()
        }
    }

    // 가져온 token 값으로 session을 만들어 방에 입장
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

    // url과 생성한 session으로 websocket 실행
    private fun startWebSocket() {
        val webSocket = CustomWebSocket(session!!, OPENVIDU_URL!!, this)
        webSocket.execute()
        session!!.setWebSocket(webSocket)
    }

    // 연결 에러
    private fun connectionError() {
        val myRunnable = Runnable {
            toast("Error connecting to $OPENVIDU_URL")
        }
        Handler(this.mainLooper).post(myRunnable)
    }

    // 연결을 끊고 세션을 떠남
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

    //Stomp 연결
    private fun connectStomp() {
        val headers: MutableList<StompHeader> = ArrayList()
        Log.d(SSESION_TAG, "connectStomp: ${channelResDTO}")

        headers.add(StompHeader(USERID, "${prefs.getId()}"))
        headers.add(StompHeader(PASSCODE, "guest"))
        mStompClient!!.withClientHeartbeat(1000).withServerHeartbeat(1000)
        resetSubscriptions()
        Log.d(SSESION_TAG, "connectStomp1: ")
        val dispLifecycle: Disposable = mStompClient!!.lifecycle()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { lifecycleEvent ->
                when (lifecycleEvent.getType()) {
                    LifecycleEvent.Type.OPENED -> toast("Stomp connection opened")
                    LifecycleEvent.Type.ERROR -> {
                        Log.e(SSESION_TAG, "Stomp connection error", lifecycleEvent.getException())
                        toast("Stomp connection error")
                    }
                    LifecycleEvent.Type.CLOSED -> {
                        toast("Stomp connection closed")
                        resetSubscriptions()
                    }
                    LifecycleEvent.Type.FAILED_SERVER_HEARTBEAT -> toast("Stomp failed server heartbeat")
                }
            }
        compositeDisposable!!.add(dispLifecycle)


        val dispTopic: Disposable = mStompClient!!.topic("/channel/in/${channelResDTO.data.code}") //인식할 Stomp URI
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ topicMessage ->
                Log.d(TAG, "dispTopic Received " + topicMessage.getPayload())

                //channel/in 신호가 들어왔을 때 실행할 함수
                val user = mGson.fromJson(topicMessage.getPayload(), UserInfoResDTO::class.java)
                addPlayer(user)

                val transaction = supportFragmentManager.beginTransaction().replace(R.id.frameLayout, GameWaitingFragment())
                transaction.commit()

            }) { throwable -> Log.e(TAG, "Error on subscribe topic", throwable) }

        val dispTopic2: Disposable = mStompClient!!.topic("/channel/out/${channelResDTO.data.code}")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ topicMessage ->
                Log.d(TAG, "dispTopic2 Received " + topicMessage.getPayload())

                //channel/out 신호가 들어왔을 때 실행할 함수
                val user = mGson.fromJson(topicMessage.getPayload(), UserInfoResDTO::class.java)
                removePlayer(user)

                val transaction = supportFragmentManager.beginTransaction().replace(R.id.frameLayout, GameWaitingFragment())
                transaction.commit()

            }) { throwable -> Log.e(TAG, "Error on subscribe topic", throwable) }

        val dispTopic3: Disposable = mStompClient!!.topic("/channel/start/${channelResDTO.data.code}")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ topicMessage ->
                Log.d(TAG, "dispTopic3 Received " + topicMessage.getPayload())
                roundNum=1
                var res = mGson.fromJson(topicMessage.getPayload(), Long::class.java)
                Log.d(TAG, "connectStomp: ${res}")
                gameChannelResDTO.data.id = res
                gameChannelResDTO.msg = "성공"
                gameChannelResDTO.output = 1

//                getSection(res, loginUserResDTO.data.id)



                val transaction = supportFragmentManager.beginTransaction().replace(R.id.frameLayout, GameWritingFragment())
                transaction.commit()

            }) { throwable -> Log.e(TAG, "Error on subscribe topic", throwable) }

        val dispTopic4: Disposable = mStompClient!!.topic("/channel/leader/${channelResDTO.data.code}")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ topicMessage ->
                Log.d(TAG, "dispTopic4 Received " + topicMessage.getPayload())

                //channel/leader 신호가 들어왔을 때 실행할 함수
                val user = mGson.fromJson(topicMessage.getPayload(), UserInfoResDTO::class.java)
                updateLeader(user)

                val transaction = supportFragmentManager.beginTransaction().replace(R.id.frameLayout, GameWaitingFragment())
                transaction.commit()

            }) { throwable -> Log.e(TAG, "Error on subscribe topic", throwable) }

        val dispTopic5: Disposable = mStompClient!!.topic("/channel/game/next/${channelResDTO.data.code}")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ topicMessage ->
                Log.d(TAG, "dispTopic5 Received " + topicMessage.getPayload())

                //channel/game/next 신호가 들어왔을 때 실행할 함수
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
                Log.d(TAG, "dispTopic5 Received " + topicMessage.getPayload())
                val channelStmpResDto = mGson.fromJson(topicMessage.getPayload(), ChannelResDTO::class.java)
                channelResDTO.data = channelStmpResDto

                val transaction = supportFragmentManager.beginTransaction().replace(R.id.frameLayout, GameWaitingFragment())
                transaction.commit()

            }) { throwable -> Log.e(TAG, "Error on subscribe topic", throwable) }

        compositeDisposable!!.add(dispTopic)
        compositeDisposable!!.add(dispTopic2)
        compositeDisposable!!.add(dispTopic3)
        compositeDisposable!!.add(dispTopic4)
        compositeDisposable!!.add(dispTopic5)
        compositeDisposable!!.add(dispTopic6)
        mStompClient!!.connect(headers) //연결 시작
        Log.d(TAG, "conectStomp3: ")
    }

    //player 입장
    private fun addPlayer(user: UserInfoResDTO) {
        channelResDTO.data.users.add(user)
            playerList = channelResDTO.data.users
        playerRecyclerViewAdapter.notifyDataSetChanged()
        recyclerView.smoothScrollToPosition(playerList.size - 1)
    }

    //player 퇴장
    private fun removePlayer(user: UserInfoResDTO) {
        channelResDTO.data.users.remove(user)
        playerList = channelResDTO.data.users
        playerRecyclerViewAdapter.notifyDataSetChanged()
        recyclerView.smoothScrollToPosition(playerList.size - 1)

    }

    //Leader 변경
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


    //섹션조회
    private fun getSection(gameChannelId: Long, userId: Long) {
        Log.d(TAG, "getSection: ")
        SectionService().getSection(gameChannelId, userId, object : RetrofitCallback<ListResult<SectionRetrieveResDTO>> {
            override fun onSuccess(code: Int, responseData: ListResult<SectionRetrieveResDTO>) {
                if (responseData.output == 1) {
                    //companion object에 나의 섹션 순서 저장
                    Log.d(TAG, "onSuccess: ${responseData}")
                } else {
                    Log.d(TAG, "onSuccess: null")
                }
            }

            override fun onFailure(code: Int) {
                Log.d(TAG, "onFailure: ")
            }

            override fun onError(t: Throwable) {
                Log.d(TAG, "onError: ")
            }
        })
    }

    // 스텀프 통신 해제
    fun disconnectStomp() {
        mStompClient!!.disconnect()
    }

    // 어플리케이션에서 BackPressed 됐을 때
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

    // 사용자가 나갔음을 전달함
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
                    toast("outChannel에서 문제가 발생하였습니다. 다시 시도해주세요.")
                }
            }

            override fun onFailure(code: Int) {
                toast("outChannel에서 실패문제가 발생하였습니다. 다시 시도해주세요.")
            }

            override fun onError(t: Throwable) {
                toast("outChannel에서 에러문제가 발생하였습니다. 다시 시도해주세요.")

            }
        })
    }

    // 방장이 방의 설정을 수정했을 때
    fun updateChannel(changedChannel: MakeChannelReqDTO) {

        Log.d(TAG, "makeChannel: ")

        ChannelService().updateChannel(changedChannel, object : RetrofitCallback<SingleResult<ChannelResDTO>> {
            override fun onSuccess(code: Int, responseData: SingleResult<ChannelResDTO>) {
                Log.d(TAG, "updateChannel ChannelResDTO: ${responseData}")
                if (responseData.data.id > 0) {
                    channelResDTO = responseData
                    Log.d(TAG, "onSuccess: ${responseData}")
                } else {
                    Log.d(TAG, "onSuccess: null")
                    toast("makeChannel에서 문제가 발생하였습니다. 다시 시도해주세요.")
                }
            }

            override fun onFailure(code: Int) {
                Log.d(TAG, "onFailure: ")
                toast("makeChannel에서 실패문제가 발생하였습니다. 다시 시도해주세요.")
            }

            override fun onError(t: Throwable) {
                Log.d(TAG, "onError: ")
                toast("makeChannel에서 에러문제가 발생하였습니다. 다시 시도해주세요.")
            }
        })
    }

    // 어플리케이션이 Stop 됐을 때
    override fun onStop() {
        leaveSession()
        disconnectStomp()
        if (mRestPingDisposable != null) mRestPingDisposable.dispose()
        if (compositeDisposable != null) compositeDisposable!!.dispose()
        super.onStop()
    }

    private fun toast(text: String) {
        Log.i(TAG, text)
        Toast.makeText(this@GameActivity, text, Toast.LENGTH_SHORT).show()
    }
}