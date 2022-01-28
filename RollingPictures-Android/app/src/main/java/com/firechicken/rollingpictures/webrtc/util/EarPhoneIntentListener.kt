package com.firechicken.rollingpictures.webrtc.util

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothHeadset
import android.bluetooth.BluetoothProfile
import android.bluetooth.BluetoothProfile.ServiceListener
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.AudioManager
import android.media.AudioManager.ADJUST_RAISE
import android.util.Log
import com.firechicken.rollingpictures.webrtc.openvidu.Session
import java.lang.IllegalArgumentException

class EarPhoneIntentListener private constructor(private val context: Context, val session: Session) {
    var mBluetoothAdapter: BluetoothAdapter? = null
    var mBluetoothHeadset: BluetoothHeadset? = null
    var mAudioManager: AudioManager? = null

    private var receiverFilter: IntentFilter? = null
    private var receiver: EarphoneReceiver? = null

    fun init() {

        // 초기 출력 세팅, 스피커 세팅
        enableSpeaker()

        // 브로드 캐스트 리시버에서 이어폰 포트(유선 이어폰 연결) 감지해서 오디오 매니저 관리
        receiverFilter = IntentFilter(Intent.ACTION_HEADSET_PLUG)
        receiver = EarphoneReceiver()
        context.registerReceiver(receiver, receiverFilter)

        // 블루투스 어댑터에서 이어폰 포트(무선 이어폰 연결) 감지해서 오디오 매니저 관리
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        if (mBluetoothAdapter != null) {
            mAudioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
            if (mAudioManager!!.isBluetoothScoAvailableOffCall) {
                mBluetoothAdapter!!.getProfileProxy(
                    context,
                    mHeadsetProfileListener,
                    BluetoothProfile.HEADSET
                )
            }
        }
    }

    fun destroy() {
        mHeadsetProfileListener.onServiceDisconnected(BluetoothProfile.HEADSET)
    }

    private var mHeadsetProfileListener: ServiceListener = object : ServiceListener {
        // 블루투스 연결이 끊겼을 때 : 유선 이어폰 관련 리시버를 등록하고, 무선 이어폰 관련 리시버는 해제, 스피커 O
        override fun onServiceDisconnected(profile: Int) {
            try {
                context.registerReceiver(receiver, receiverFilter)
                enableSpeaker()
                context.unregisterReceiver(mHeadsetBroadcastReceiver)
                mBluetoothHeadset = null
                Log.d("kiaa", " ================== 무선 이어폰 연결해제")
            } catch (il: IllegalArgumentException) {
                Log.i(TAG, "Headset broadcast receiver wasn't registered yet.")
            }
        }

        // 블루투스 연결 됐을 때 : 유선 이어폰 관련 리시버는 삭제하고, 무선 이어폰 관련 리시버를 등록, 스피커 X
        override fun onServiceConnected(profile: Int, proxy: BluetoothProfile) {
            context.unregisterReceiver(receiver)
            disableSpeaker()
            mBluetoothHeadset = proxy as BluetoothHeadset



            context.registerReceiver(
                mHeadsetBroadcastReceiver,
                IntentFilter(BluetoothHeadset.ACTION_CONNECTION_STATE_CHANGED)
            )
            val f = IntentFilter(BluetoothHeadset.ACTION_AUDIO_STATE_CHANGED)
            f.priority = Int.MAX_VALUE
            context.registerReceiver(mHeadsetBroadcastReceiver, f)
            Log.d("kiaa", " ================== 무선 이어폰 연결")
        }
    }

    var mHeadsetBroadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action
            val state: Int
            if (action == BluetoothHeadset.ACTION_AUDIO_STATE_CHANGED) {
                state = intent.getIntExtra(
                    BluetoothHeadset.EXTRA_STATE,
                    BluetoothHeadset.STATE_DISCONNECTED
                )
                if (state == BluetoothHeadset.STATE_AUDIO_CONNECTED) {
                } else if (state == BluetoothHeadset.STATE_AUDIO_DISCONNECTED) {
                    Log.d("kiaa", "disConnected")
                    // Hangup of bluetooth headset pressed.
                }
            }
        }
    }

    // 출력 장치 (스피커로)
    private fun enableSpeaker() {
        val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        if (!audioManager.isSpeakerphoneOn) {
            audioManager.isSpeakerphoneOn = true
            audioManager.mode = AudioManager.MODE_IN_COMMUNICATION
            audioManager.adjustVolume(ADJUST_RAISE, AudioManager.FLAG_SHOW_UI)
        }
    }

    // 출력 장치 (노말로: 유/무선 이어폰으로)
    private fun disableSpeaker() {
        val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        if (audioManager.isSpeakerphoneOn) {
            audioManager.isSpeakerphoneOn = false
            audioManager.mode = AudioManager.MODE_NORMAL
            session.getLocalParticipant()?.audioTrack?.setVolume(0.0)

        }
    }

    // 유선 이어폰 출력 포트 감지 브로드 캐스트 리시버
    inner class EarphoneReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.action.equals(Intent.ACTION_HEADSET_PLUG, ignoreCase = true)) {
                // if : 출력 장치 (스피커로) / 유선 이어폰 X
                // else : 출력 장치 (유선 이어폰으로) / 스피커 X
                if (intent.getIntExtra("state", 0) == 0) {
                    Log.d("kiaa", " ================== 유선 이어폰 연결해제")
                    enableSpeaker()
                } else if (intent.getIntExtra("state", 0) == 1) {
                    Log.d("kiaa", " ================== 유선 이어폰 연결")
                    disableSpeaker()
                }
            }
        }
    }

    companion object {
        private const val TAG = "BluetoothIntent"
        private var earPhoneIntentListener: EarPhoneIntentListener? = null
        fun getInstance(context: Context, session: Session): EarPhoneIntentListener? {
            if (earPhoneIntentListener == null) {
                earPhoneIntentListener = EarPhoneIntentListener(context, session)
            }
            return earPhoneIntentListener
        }
    }

}