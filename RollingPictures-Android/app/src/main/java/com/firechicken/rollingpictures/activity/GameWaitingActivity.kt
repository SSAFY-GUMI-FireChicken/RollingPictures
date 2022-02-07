package com.firechicken.rollingpictures.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firechicken.rollingpictures.adapter.PlayerRecyclerViewAdapter
import com.firechicken.rollingpictures.config.ApplicationClass.Companion.channelResDTO
import com.firechicken.rollingpictures.config.ApplicationClass.Companion.gameChannelResDTO
import com.firechicken.rollingpictures.config.ApplicationClass.Companion.loginUserResDTO
import com.firechicken.rollingpictures.config.ApplicationClass.Companion.playerList
import com.firechicken.rollingpictures.config.ApplicationClass.Companion.sectionResDTO
import com.firechicken.rollingpictures.config.ApplicationClass.Companion.websocketURL
import com.firechicken.rollingpictures.dialog.GameExitDialog
import com.firechicken.rollingpictures.databinding.ActivityGameWaitingBinding
import com.firechicken.rollingpictures.dto.*
import com.firechicken.rollingpictures.service.GameChannelService
import com.firechicken.rollingpictures.service.SectionService
import com.firechicken.rollingpictures.util.RetrofitCallback

import com.google.gson.GsonBuilder
import io.reactivex.Completable
import io.reactivex.CompletableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import ua.naiksoftware.stomp.*
import ua.naiksoftware.stomp.dto.LifecycleEvent
import ua.naiksoftware.stomp.dto.StompHeader
import java.util.*

private const val TAG = "GameWaitingActivity_싸피"

class GameWaitingActivity : AppCompatActivity() {

    lateinit var recyclerView: RecyclerView
    lateinit var playerRecyclerViewAdapter: PlayerRecyclerViewAdapter
    var mStompClient: StompClient? = null
    private var compositeDisposable: CompositeDisposable? = null
    private val mRestPingDisposable: Disposable? = null
    private val mGson = GsonBuilder().create()

    val USERID = "USERID"
    val PASSCODE = "passcode"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val activityGameWaitingBinding = ActivityGameWaitingBinding.inflate(layoutInflater)
        setContentView(activityGameWaitingBinding.root)

        activityGameWaitingBinding.exitRoom.setOnClickListener {
            val dialog = GameExitDialog(this, intent.getStringExtra("code").toString())
            dialog.showDialog()
        }

        activityGameWaitingBinding.startGameButton.setOnClickListener {
            makeGameChannel()
        }

        activityGameWaitingBinding.roomCodeTextView.setText(channelResDTO.data.code)

        recyclerView = activityGameWaitingBinding.recyclerViewPlayer
        playerRecyclerViewAdapter = PlayerRecyclerViewAdapter(this, playerList)
        recyclerView.apply {
            adapter = playerRecyclerViewAdapter
            layoutManager = GridLayoutManager(context, 2)
        }

        for(users in channelResDTO.data.users){
            addPlayer(users)
        }

        activityGameWaitingBinding.playerCountTextView.setText("${playerRecyclerViewAdapter.itemCount}/10")

        //websocket URL 지정
        mStompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, websocketURL)

        Log.d(TAG, "onCreate1: ")
        connectStomp()
        Log.d(TAG, "onCreate2: ")
    }

    private fun resetSubscriptions() {
        if (compositeDisposable != null) {
            compositeDisposable!!.dispose()
        }
        compositeDisposable = CompositeDisposable()
    }

    //Stomp 연결
    fun connectStomp() {
        val headers: MutableList<StompHeader> = ArrayList()
        Log.d(TAG, "connectStomp: ${channelResDTO}")
        headers.add(StompHeader(USERID, "${channelResDTO.data.users[0].id}"))
        headers.add(StompHeader(PASSCODE, "guest"))
        mStompClient!!.withClientHeartbeat(1000).withServerHeartbeat(1000)
        resetSubscriptions()
        Log.d(TAG, "connectStomp1: ")
        val dispLifecycle: Disposable = mStompClient!!.lifecycle()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { lifecycleEvent ->
                when (lifecycleEvent.getType()) {
                    LifecycleEvent.Type.OPENED -> toast("Stomp connection opened")
                    LifecycleEvent.Type.ERROR -> {
                        Log.e(
                            TAG,
                            "Stomp connection error",
                            lifecycleEvent.getException()
                        )
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
        // Receive greetings


        val dispTopic: Disposable = mStompClient!!.topic("/channel/in/${channelResDTO.data.code}") //인식할 Stomp URI
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ topicMessage ->
                Log.d(TAG, "Received " + topicMessage.getPayload())
                //channel/in 신호가 들어왔을 때 실행할 함수
                addPlayer(mGson.fromJson(topicMessage.getPayload(), UserInfoResDTO::class.java))
            }) { throwable -> Log.e(TAG, "Error on subscribe topic", throwable) }

        val dispTopic2: Disposable = mStompClient!!.topic("/channel/out/${channelResDTO.data.code}")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ topicMessage ->
                Log.d(TAG, "Received " + topicMessage.getPayload())
                //channel/out 신호가 들어왔을 때 실행할 함수
                removePlayer(mGson.fromJson(topicMessage.getPayload(), UserInfoResDTO::class.java))
            }) { throwable -> Log.e(TAG, "Error on subscribe topic", throwable) }

        compositeDisposable!!.add(dispTopic)
        compositeDisposable!!.add(dispTopic2)
        mStompClient!!.connect(headers) //연결 시작
        Log.d(TAG, "conectStomp3: ")

    }

    private fun addPlayer(user: UserInfoResDTO) {
        playerList.add(user)

        playerRecyclerViewAdapter.notifyDataSetChanged()
        recyclerView.smoothScrollToPosition(playerList.size - 1)
        Log.d(TAG, "addPlayer: ")
    }
    private fun removePlayer(user: UserInfoResDTO) {
        playerList.remove(user)

        playerRecyclerViewAdapter.notifyDataSetChanged()
        recyclerView.smoothScrollToPosition(playerList.size - 1)
        Log.d(TAG, "removePlayer: ")
    }

    fun disconnectStomp() {
        mStompClient!!.disconnect()
    }


    private fun toast(text: String) {
        Log.i(TAG, text)
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }

    protected fun applySchedulers(): CompletableTransformer? {
        return CompletableTransformer { upstream: Completable ->
            upstream
                .unsubscribeOn(Schedulers.newThread())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
        }
    }

    override fun onDestroy() {
        disconnectStomp()

        mStompClient!!.disconnect()

        if (mRestPingDisposable != null) mRestPingDisposable.dispose()
        if (compositeDisposable != null) compositeDisposable!!.dispose()
        super.onDestroy()
    }

    private fun makeGameChannel() {
        val req = MakeGameChannelReqDTO(channelResDTO.data.id,loginUserResDTO.data.id)
        Log.d(TAG, "makeGameChannel: ")
        GameChannelService().makeGameChannel(req, object : RetrofitCallback<SingleResult<GameChannelResDTO>> {
            override fun onSuccess(code: Int, responseData: SingleResult<GameChannelResDTO>) {
                Log.d(TAG, "makeGameChannel : responseData: ${responseData}")
                if (responseData.data.id > 0L) {
                    gameChannelResDTO = responseData
                    Log.d(TAG, "onSuccess: ${responseData}")
                    makeSection()
                } else {
                    Log.d(TAG, "onSuccess: null")
                    Toast.makeText(
                        this@GameWaitingActivity,
                        "문제가 발생하였습니다. 다시 시도해주세요.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(code: Int) {
                Log.d(TAG, "onFailure: ")
                Toast.makeText(this@GameWaitingActivity, "문제가 발생하였습니다. 다시 시도해주세요.", Toast.LENGTH_SHORT)
                    .show()
            }

            override fun onError(t: Throwable) {
                Log.d(TAG, "onError: ")
                Toast.makeText(this@GameWaitingActivity, "문제가 발생하였습니다. 다시 시도해주세요.", Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }

    private fun makeSection() {
        val req = SectionReqDTO(gameChannelResDTO.data.id,loginUserResDTO.data.id)
        Log.d(TAG, "makeSection: ")
        SectionService().makeSection(req, object : RetrofitCallback<ListResult<SectionResDTO>> {
            override fun onSuccess(code: Int, responseData: ListResult<SectionResDTO>) {
                Log.d(TAG, "makeSection : responseData: ${responseData}")
                if (responseData.data.size > 0) {
                    sectionResDTO = responseData
                    Log.d(TAG, "onSuccess: ${responseData}")
                } else {
                    Log.d(TAG, "onSuccess: null")
                    Toast.makeText(
                        this@GameWaitingActivity,
                        "문제가 발생하였습니다. 다시 시도해주세요.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(code: Int) {
                Log.d(TAG, "onFailure: ")
                Toast.makeText(this@GameWaitingActivity, "문제가 발생하였습니다. 다시 시도해주세요.", Toast.LENGTH_SHORT)
                    .show()
            }

            override fun onError(t: Throwable) {
                Log.d(TAG, "onError: ")
                Toast.makeText(this@GameWaitingActivity, "문제가 발생하였습니다. 다시 시도해주세요.", Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }
}