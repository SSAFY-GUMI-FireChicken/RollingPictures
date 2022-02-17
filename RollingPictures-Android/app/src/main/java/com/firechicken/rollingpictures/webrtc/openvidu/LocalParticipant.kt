package com.firechicken.rollingpictures.webrtc.openvidu

import org.webrtc.*
import java.util.ArrayList

// 나
class LocalParticipant(participantName: String?, session: Session, ) : Participant(participantName!!, session) {

    private var surfaceTextureHelper: SurfaceTextureHelper? = null
    private val localIceCandidates: MutableCollection<IceCandidate>

    // session이 만들어졌으니, 클라이언트는 오디오 트랙을 만들어 관리한다.
    fun startAudio() {
        val eglBaseContext = EglBase.create().eglBaseContext
        val peerConnectionFactory: PeerConnectionFactory = session.peerConnectionFactory!!

        // create AudioSource
        val audioSource = peerConnectionFactory.createAudioSource(MediaConstraints())
        audioTrack = peerConnectionFactory.createAudioTrack("101", audioSource)
        surfaceTextureHelper = SurfaceTextureHelper.create("CaptureThread", eglBaseContext)
    }

    override fun dispose() {
        super.dispose()
        if (surfaceTextureHelper != null) {
            surfaceTextureHelper!!.dispose()
            surfaceTextureHelper = null
        }
    }

    init {
        localIceCandidates = ArrayList()
        session.setLocalParticipant(this)
    }
}
