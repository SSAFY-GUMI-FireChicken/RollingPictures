package com.firechicken.rollingpictures.webrtc.openvidu

import android.view.View

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
