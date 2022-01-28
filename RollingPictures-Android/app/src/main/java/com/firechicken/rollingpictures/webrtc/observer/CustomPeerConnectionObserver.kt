package com.firechicken.rollingpictures.webrtc.observer

import android.util.Log
import org.webrtc.*
import org.webrtc.PeerConnection.*
import org.webrtc.PeerConnection.Observer
import java.util.*

open class CustomPeerConnectionObserver(id: String) : Observer {

    private var TAG = "PeerConnection"

    override fun onSignalingChange(signalingState: SignalingState) {
        Log.d(
            TAG,
            "onSignalingChange() called with: signalingState = [$signalingState]"
        )
    }

    override fun onIceConnectionChange(iceConnectionState: IceConnectionState) {
        Log.d(
            TAG,
            "onIceConnectionChange() called with: iceConnectionState = [$iceConnectionState]"
        )
    }

    override fun onIceConnectionReceivingChange(b: Boolean) {
        Log.d(TAG, "onIceConnectionReceivingChange() called with: b = [$b]")
    }

    override fun onIceGatheringChange(iceGatheringState: IceGatheringState) {
        Log.d(
            TAG,
            "onIceGatheringChange() called with: iceGatheringState = [$iceGatheringState]"
        )
    }

    override fun onIceCandidate(iceCandidate: IceCandidate) {
        Log.d(TAG, "onIceCandidate() called with: iceCandidate = [$iceCandidate]")
    }

    override fun onIceCandidatesRemoved(iceCandidates: Array<IceCandidate>) {
        Log.d(
            TAG,
            "onIceCandidatesRemoved() called with: iceCandidates = [" + Arrays.toString(
                iceCandidates
            ) + "]"
        )
    }

    override fun onAddStream(mediaStream: MediaStream) {
        Log.d(TAG, "onAddStream() called with: mediaStream = [$mediaStream]")
    }

    override fun onRemoveStream(mediaStream: MediaStream) {
        Log.d(TAG, "onRemoveStream() called with: mediaStream = [$mediaStream]")
    }

    override fun onDataChannel(dataChannel: DataChannel) {
        Log.d(TAG, "onDataChannel() called with: dataChannel = [$dataChannel]")
    }

    override fun onRenegotiationNeeded() {
        Log.d(TAG, "onRenegotiationNeeded() called")
    }

    override fun onAddTrack(rtpReceiver: RtpReceiver, mediaStreams: Array<MediaStream>) {
        Log.d(
            TAG,
            "onAddTrack() called with: mediaStreams = [" + Arrays.toString(mediaStreams) + "]"
        )
    }

    init {
        TAG = TAG + "-" + id
    }
}
