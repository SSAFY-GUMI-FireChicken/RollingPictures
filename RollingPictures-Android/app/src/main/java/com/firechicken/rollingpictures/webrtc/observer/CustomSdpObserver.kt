package com.firechicken.rollingpictures.webrtc.observer

import org.webrtc.SdpObserver
import org.webrtc.SessionDescription

open class CustomSdpObserver(tag: String) : SdpObserver {

    private fun log(s: String) {
    }

    override fun onCreateSuccess(sessionDescription: SessionDescription) {
        log("onCreateSuccess $sessionDescription")
    }

    override fun onSetSuccess() {
        log("onSetSuccess ")
    }

    override fun onCreateFailure(s: String) {
        log("onCreateFailure $s")
    }

    override fun onSetFailure(s: String) {
        log("onSetFailure $s")
    }
}
