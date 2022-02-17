package com.firechicken.rollingpictures.webrtc.openvidu

import android.os.AsyncTask
import android.view.View
import com.firechicken.rollingpictures.activity.*
import com.firechicken.rollingpictures.webrtc.observer.CustomPeerConnectionObserver
import com.firechicken.rollingpictures.webrtc.observer.CustomSdpObserver
import com.firechicken.rollingpictures.webrtc.CustomWebSocket
import org.webrtc.*
import org.webrtc.PeerConnection.*
import org.webrtc.PeerConnectionFactory.InitializationOptions
import java.util.ArrayList
import java.util.HashMap

class Session(val id: String, val token: String, activity: GameActivity) {

    private var localParticipant: LocalParticipant? = null
    private val remoteParticipants: MutableMap<String, RemoteParticipant> =
        HashMap<String, RemoteParticipant>()
    var peerConnectionFactory: PeerConnectionFactory?
        private set
    private var websocket: CustomWebSocket? = null
    private val activity: GameActivity

    fun setWebSocket(websocket: CustomWebSocket?) {
        this.websocket = websocket
    }

    // 나라는 사람의 커넥션을 만듦
    fun createLocalPeerConnection(): PeerConnection? {
        val iceServers: MutableList<IceServer> = ArrayList()
        val iceServer =
            IceServer.builder("stun:stun.l.google.com:19302").createIceServer()
        iceServers.add(iceServer)
        val rtcConfig = RTCConfiguration(iceServers)
        rtcConfig.tcpCandidatePolicy = TcpCandidatePolicy.ENABLED
        rtcConfig.bundlePolicy = BundlePolicy.MAXBUNDLE
        rtcConfig.rtcpMuxPolicy = RtcpMuxPolicy.NEGOTIATE
        rtcConfig.continualGatheringPolicy =
            ContinualGatheringPolicy.GATHER_CONTINUALLY
        rtcConfig.keyType = KeyType.ECDSA
        rtcConfig.enableDtlsSrtp = true
        rtcConfig.sdpSemantics = SdpSemantics.UNIFIED_PLAN
        return peerConnectionFactory!!.createPeerConnection(
            rtcConfig,
            object : CustomPeerConnectionObserver("local") {
                override fun onIceCandidate(iceCandidate: IceCandidate) {
                    super.onIceCandidate(iceCandidate!!)
                    websocket?.onIceCandidate(iceCandidate, localParticipant?.connectionId)
                }
            })
    }

    // 다른 사람과의 커넥션을 만듦
    fun createRemotePeerConnection(connectionId: String) {
        val iceServers: MutableList<IceServer> = ArrayList()
        val iceServer = IceServer.builder("stun:stun.l.google.com:19302").createIceServer()
        iceServers.add(iceServer)
        val rtcConfig = RTCConfiguration(iceServers)
        rtcConfig.tcpCandidatePolicy = TcpCandidatePolicy.ENABLED
        rtcConfig.bundlePolicy = BundlePolicy.MAXBUNDLE
        rtcConfig.rtcpMuxPolicy = RtcpMuxPolicy.NEGOTIATE
        rtcConfig.continualGatheringPolicy = ContinualGatheringPolicy.GATHER_CONTINUALLY
        rtcConfig.keyType = KeyType.ECDSA
        rtcConfig.enableDtlsSrtp = true
        rtcConfig.sdpSemantics = SdpSemantics.UNIFIED_PLAN
        val peerConnection = peerConnectionFactory!!.createPeerConnection(
            rtcConfig,
            object : CustomPeerConnectionObserver("remotePeerCreation") {
                override fun onIceCandidate(iceCandidate: IceCandidate) {
                    super.onIceCandidate(iceCandidate!!)
                    websocket?.onIceCandidate(iceCandidate, connectionId)
                }

                override fun onAddTrack(rtpReceiver: RtpReceiver, mediaStreams: Array<MediaStream>) {
                    super.onAddTrack(rtpReceiver, mediaStreams)
                }

                override fun onSignalingChange(signalingState: SignalingState) {
                    if (SignalingState.STABLE == signalingState) {
                        val remoteParticipant: RemoteParticipant? = remoteParticipants[connectionId]
                        val it: MutableIterator<IceCandidate> =
                            remoteParticipant!!.iceCandidateList.iterator()
                        while (it.hasNext()) {
                            val candidate = it.next()
                            remoteParticipant.peerConnection?.addIceCandidate(candidate)
                            it.remove()
                        }
                    }
                }
            })

        peerConnection!!.addTrack(localParticipant?.audioTrack) //Add audio track to create transReceiver

        for (transceiver in peerConnection.transceivers) {
            //We set both audio and video in receive only mode
            transceiver.direction = RtpTransceiver.RtpTransceiverDirection.RECV_ONLY
        }

        remoteParticipants[connectionId]?.peerConnection = (peerConnection)
    }

    // 커넥션에 offer를 만들고, 성공하면 미디어 송출
    fun createOfferForPublishing(constraints: MediaConstraints?) {
        localParticipant!!.peerConnection?.createOffer(object : CustomSdpObserver("createOffer") {
            override fun onCreateSuccess(sessionDescription: SessionDescription) {
                super.onCreateSuccess(sessionDescription)

                localParticipant!!.peerConnection?.setLocalDescription(
                    CustomSdpObserver("createOffer_setLocalDescription"),
                    sessionDescription
                )
                websocket?.publishVideo(sessionDescription)
            }

            override fun onCreateFailure(s: String) {}
        }, constraints)
    }

    // ((앱에서는 안 씀))
    fun createAnswerForSubscribing(
        remoteParticipant: RemoteParticipant,
        streamId: String?,
        constraints: MediaConstraints?
    ) {
        remoteParticipant.peerConnection
            ?.createAnswer(object : CustomSdpObserver("createAnswerSubscribing") {
                override fun onCreateSuccess(sessionDescription: SessionDescription) {
                    super.onCreateSuccess(sessionDescription)

                    remoteParticipant.peerConnection!!.setLocalDescription(object :
                        CustomSdpObserver("createAnswerSubscribing_setLocalDescription") {
                        override fun onSetSuccess() {
                            websocket?.receiveVideoFrom(
                                sessionDescription,
                                remoteParticipant,
                                streamId!!
                            )
                        }

                        override fun onSetFailure(s: String) {}
                    }, sessionDescription)
                }

                override fun onCreateFailure(s: String) {}
            }, constraints)
    }

    // '나' 를 반환
    fun getLocalParticipant(): LocalParticipant? {
        return localParticipant
    }

    // '나' 를 로컬 참가자로 설정
    fun setLocalParticipant(localParticipant: LocalParticipant?) {
        this.localParticipant = localParticipant
    }

    // '나' 말고 다른 참가자 중에 id에 해당하는 참가자 반환
    fun getRemoteParticipant(id: String): RemoteParticipant? {
        return remoteParticipants[id]
    }

    // '나' 말고 다른 참가자가 나타나면 선언과 함께 세션에 추가
    fun addRemoteParticipant(remoteParticipant: RemoteParticipant) {
        remoteParticipants.put(remoteParticipant.connectionId!!,remoteParticipant)
    }

    // '나' 말고 다른 참가자가 나가면 세션에서 삭제
    fun removeRemoteParticipant(id: String): RemoteParticipant? {
        return remoteParticipants.remove(id)
    }

    // 보이스를 끝낼 때 통신 관련 설정을 모두 close 함
    fun leaveSession() {
        AsyncTask.execute {
            websocket?.setWebsocketCancelled(true)
            if (websocket != null) {
                websocket!!.leaveRoom()
                websocket!!.disconnect()
            }
            localParticipant?.dispose()
        }
        activity.runOnUiThread {
            for (remoteParticipant in remoteParticipants.values) {
                if (remoteParticipant.peerConnection != null) {
                    remoteParticipant.peerConnection!!.close()
                }
            }
        }
        AsyncTask.execute {
            if (peerConnectionFactory != null) {
                peerConnectionFactory!!.dispose()
                peerConnectionFactory = null
            }
        }
    }

    // 보이스를 끝낼 때 Main스레드에서 이뤄져야 하는 작업
    fun removeView(view: View?) {}

    init {
        this.activity = activity
        val optionsBuilder = InitializationOptions.builder(activity.getApplicationContext())
        optionsBuilder.setEnableInternalTracer(true)
        val opt = optionsBuilder.createInitializationOptions()
        PeerConnectionFactory.initialize(opt)
        val options = PeerConnectionFactory.Options()
        val encoderFactory: VideoEncoderFactory
        val decoderFactory: VideoDecoderFactory
        encoderFactory = SoftwareVideoEncoderFactory()
        decoderFactory = SoftwareVideoDecoderFactory()
        peerConnectionFactory = PeerConnectionFactory.builder()
            .setVideoEncoderFactory(encoderFactory)
            .setVideoDecoderFactory(decoderFactory)
            .setOptions(options)
            .createPeerConnectionFactory()
    }
}
