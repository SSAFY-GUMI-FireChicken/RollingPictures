package com.firechicken.rollingpictures_android

import android.view.View
import android.widget.TextView
import org.webrtc.SurfaceViewRenderer

// 나 말고 다
class RemoteParticipant(connectionId: String?, participantName: String?, session: Session?) :
    Participant(connectionId, participantName!!, session!!) {
    var view: View? = null

    override fun dispose() {
        super.dispose()
    }

    init {
        this.session.addRemoteParticipant(this)
    }
}
