package com.firechicken.rollingpictures_android

import android.util.Log
import org.webrtc.*
import java.lang.IllegalStateException
import java.util.ArrayList

abstract class Participant {
    var connectionId: String? = null
    var participantName: String
        protected set
    protected var session: Session
    var iceCandidateList: MutableList<IceCandidate> = ArrayList()
        protected set
    var peerConnection: PeerConnection? = null
    var audioTrack: AudioTrack? = null

    constructor(participantName: String, session: Session) {
        this.participantName = participantName
        this.session = session
    }

    constructor(connectionId: String?, participantName: String, session: Session) {
        this.connectionId = connectionId
        this.participantName = participantName
        this.session = session
    }

    open fun dispose() {
        if (peerConnection != null) {
            try {
                peerConnection!!.close()
            } catch (e: IllegalStateException) {
                Log.e("Dispose PeerConnection", e.message!!)
            }
        }
    }
}
