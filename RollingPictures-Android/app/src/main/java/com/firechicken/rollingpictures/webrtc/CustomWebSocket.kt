package com.firechicken.rollingpictures.webrtc

import android.os.AsyncTask
import android.os.Build
import android.os.Handler
import android.util.Log
import android.util.Pair
import android.widget.Toast
import com.firechicken.rollingpictures.activity.GameActivity
import com.firechicken.rollingpictures.webrtc.observer.CustomSdpObserver
import com.firechicken.rollingpictures.webrtc.openvidu.RemoteParticipant
import com.firechicken.rollingpictures.webrtc.openvidu.Session
import com.firechicken.rollingpictures.webrtc.util.JsonConstants
import com.neovisionaries.ws.client.*
import org.json.JSONException
import org.json.JSONObject
import org.webrtc.*
import org.webrtc.PeerConnection.SignalingState
import java.io.IOException
import java.lang.Exception
import java.net.MalformedURLException
import java.net.URL
import java.security.KeyManagementException
import java.security.NoSuchAlgorithmException
import java.security.SecureRandom
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ScheduledThreadPoolExecutor
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

class CustomWebSocket(
    private val session: Session,
    private val openviduUrl: String,
    activity: GameActivity
) :
    AsyncTask<GameActivity?, Void?, Void?>(), WebSocketListener {
    private val TAG = "CustomWebSocketListener"
    private val PING_MESSAGE_INTERVAL = 5
    private val trustManagers = arrayOf<TrustManager>(object : X509TrustManager {
        override fun getAcceptedIssuers(): Array<X509Certificate> {
            return emptyArray()
        }

        @Throws(CertificateException::class)
        override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {
            Log.i(TAG, ": authType: $authType")
        }

        @Throws(CertificateException::class)
        override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {
            Log.i(TAG, ": authType: $authType")
        }
    })
    private val RPC_ID = AtomicInteger(0)
    private val ID_PING = AtomicInteger(-1)
    private val ID_JOINROOM = AtomicInteger(-1)
    private val ID_LEAVEROOM = AtomicInteger(-1)
    private val ID_PUBLISHVIDEO = AtomicInteger(-1)
    private val IDS_PREPARERECEIVEVIDEO: MutableMap<Int, Pair<String, String>> = ConcurrentHashMap()
    private val IDS_RECEIVEVIDEO: MutableMap<Int, String> = ConcurrentHashMap()
    private val IDS_ONICECANDIDATE = Collections.newSetFromMap(ConcurrentHashMap<Int, Boolean>())
    private var mediaServer: String? = null
    private val activity: GameActivity
    private lateinit var websocket: WebSocket
    private var websocketCancelled = false

    /* websocket에서 온 text가 결과를 포함하는지 아닌지에 따라 분기
       결과의 유무로 분기함
       handleServerResponse는 클라이언트에서 서버로
       handleServerEvent는 서버에서 클라이언트로 보내는 것
    * */
    @Throws(Exception::class)
    override fun onTextMessage(websocket: WebSocket, text: String) {
        Log.i(TAG, "Text Message $text")
        val json = JSONObject(text)
        if (json.has(JsonConstants.RESULT)) {
            handleServerResponse(json)
        } else {
            handleServerEvent(json)
        }
    }

    @Throws(JSONException::class)
    private fun handleServerResponse(json: JSONObject) {
        val rpcId = json.getInt(JsonConstants.ID)
        val result = JSONObject(json.getString(JsonConstants.RESULT))

        if (result.has("value") && result.getString("value") == "pong") {
            // 1. ping : 서버가 클라이언트의 연결을 인식, 수신이 되지 않을 경우 이뤄져야 하는 작업 명시
            Log.i(TAG, "pong")
        } else if (rpcId == ID_JOINROOM.get()) {
            // 2. join room : 초기화 된 방으로 클라이언트를 참가시킴
            val localParticipant = session.getLocalParticipant()
            val localConnectionId = result.getString(JsonConstants.ID)
            mediaServer = result.getString(JsonConstants.MEDIA_SERVER)
            localParticipant!!.connectionId = (localConnectionId)
            val localPeerConnection = session.createLocalPeerConnection()
            localPeerConnection!!.addTrack(localParticipant.audioTrack)
            for (transceiver in localPeerConnection.transceivers) {
                transceiver.direction = RtpTransceiver.RtpTransceiverDirection.SEND_ONLY
            }
            localParticipant.peerConnection = (localPeerConnection)
            val sdpConstraints = MediaConstraints()
            sdpConstraints.mandatory.add(
                MediaConstraints.KeyValuePair(
                    "offerToReceiveAudio",
                    "true"
                )
            )
            // 방에 들어갔기 때문에 내 오디오를 전송할 스트림을 publish
            session.createOfferForPublishing(sdpConstraints)

            // 들어간 세션에 이미 나말고 다른 사람들이 들어가 있어서 구독이 필요할 때
            if (result.getJSONArray(JsonConstants.VALUE).length() > 0) {
                // There were users already connected to the session
                addRemoteParticipantsAlreadyInRoom(result)
            }
        } else if (rpcId == ID_LEAVEROOM.get()) {
            // 3. leave room : 방을 떠남
            Log.d("tete", "!ID_LEAVEROOM : $rpcId")
            Log.d("tete", "!ID_LEAVEROOM : $ID_LEAVEROOM")
            // Response to leaveRoom
            if (websocket.isOpen) {
                websocket.disconnect()
            }
        } else if (rpcId == ID_PUBLISHVIDEO.get()) {
            // 4. publish video : 현재 사용자의 오디오 송출
            Log.d("tetete", "!ID_PUBLISHVIDEO--json : $json")
            Log.d("tetete", "!ID_PUBLISHVIDEO : $rpcId")
            Log.d("tetete", "!ID_PUBLISHVIDEO : $ID_PUBLISHVIDEO")
            // Response to publishVideo
            val localParticipant = session.getLocalParticipant()
            val remoteSdpAnswer =
                SessionDescription(SessionDescription.Type.ANSWER, result.getString("sdpAnswer"))
            localParticipant!!.peerConnection?.setRemoteDescription(
                CustomSdpObserver("publishVideo_setRemoteDescription"),
                remoteSdpAnswer
            )
        } else if (IDS_PREPARERECEIVEVIDEO.containsKey(rpcId)) {
            // 5. prepareReceiveVideo : ????????????????????????????????
            Log.d("tetete", "!IDS_PREPARERECEIVEVIDEO--json : $json")
            Log.d("tetete", "!IDS_PREPARERECEIVEVIDEO : $rpcId")
            Log.d("tetete", "!IDS_PREPARERECEIVEVIDEO : $IDS_PREPARERECEIVEVIDEO")
            // Response to prepareReceiveVideoFrom
            val participantAndStream = IDS_PREPARERECEIVEVIDEO.remove(rpcId)!!
            val remoteParticipant = session.getRemoteParticipant(participantAndStream.first)
            val streamId = participantAndStream.second
            val remoteSdpOffer =
                SessionDescription(SessionDescription.Type.OFFER, result.getString("sdpOffer"))
            remoteParticipant!!.peerConnection?.setRemoteDescription(object :
                CustomSdpObserver("prepareReceiveVideoFrom_setRemoteDescription") {
                override fun onSetSuccess() {
                    subscriptionInitiatedFromServer(remoteParticipant, streamId)
                }

                override fun onSetFailure(s: String) {
                    Log.i("setRemoteDescription ER", s)
                }
            }, remoteSdpOffer)
        } else if (IDS_RECEIVEVIDEO.containsKey(rpcId)) {
            // 6. receivedVideo : ????????????????????????????????
            Log.d("tetete", "!IDS_RECEIVEVIDEO--json : $json")
            Log.d("tetete", "!IDS_RECEIVEVIDEO : $rpcId")
            Log.d("tetete", "!IDS_RECEIVEVIDEO : $IDS_RECEIVEVIDEO")
            // Response to receiveVideoFrom
            val id = IDS_RECEIVEVIDEO.remove(rpcId)
            if ("kurento" == mediaServer) {
                val sessionDescription = SessionDescription(
                    SessionDescription.Type.ANSWER,
                    result.getString("sdpAnswer")
                )
                session.getRemoteParticipant(id!!)?.peerConnection?.setRemoteDescription(
                    CustomSdpObserver("remoteSetRemoteDesc"),
                    sessionDescription
                )
            }
        } else if (IDS_ONICECANDIDATE.contains(rpcId)) {
            // 7. onIceCandidate : ????????????????????????????????
            Log.d("tete", "!IDS_ONICECANDIDATE : $rpcId")
            Log.d("tete", "!IDS_ONICECANDIDATE : $IDS_ONICECANDIDATE")
            // Response to onIceCandidate
            IDS_ONICECANDIDATE.remove(rpcId)
        } else {
            Log.e(TAG, "Unrecognized server response: $result")
        }
    }

    fun joinRoom() {
        val joinRoomParams: MutableMap<String, String> = HashMap()
        joinRoomParams[JsonConstants.METADATA] =
            "{\"clientData\": \"" + session.getLocalParticipant()?.participantName
                .toString() + "\"}"
        joinRoomParams["secret"] = ""
        joinRoomParams["session"] = session.id
        joinRoomParams["platform"] = "Android " + Build.VERSION.SDK_INT
        joinRoomParams["token"] = session.token
        ID_JOINROOM.set(this.sendJson(JsonConstants.JOINROOM_METHOD, joinRoomParams))
    }

    // 방을 떠남
    fun leaveRoom() {
        ID_LEAVEROOM.set(this.sendJson(JsonConstants.LEAVEROOM_METHOD))
    }

    fun publishVideo(sessionDescription: SessionDescription) {
        val publishVideoParams: MutableMap<String, String> = HashMap()
        publishVideoParams["audioActive"] = "true"
        publishVideoParams["videoActive"] = "true"
        publishVideoParams["doLoopback"] = "false"
        publishVideoParams["frameRate"] = "30"
        publishVideoParams["hasAudio"] = "true"
        publishVideoParams["hasVideo"] = "true"
        publishVideoParams["typeOfVideo"] = "CAMERA"
        publishVideoParams["videoDimensions"] = "{\"width\":320, \"height\":240}"
        publishVideoParams["sdpOffer"] = sessionDescription.description
        ID_PUBLISHVIDEO.set(this.sendJson(JsonConstants.PUBLISHVIDEO_METHOD, publishVideoParams))
    }

    // ????????????????????????????????????????????????????????????????????????????????????
    fun prepareReceiveVideoFrom(remoteParticipant: RemoteParticipant?, streamId: String) {
        val prepareReceiveVideoFromParams: MutableMap<String, String> = HashMap()
        prepareReceiveVideoFromParams["sender"] = streamId
        prepareReceiveVideoFromParams["reconnect"] = "false"
        IDS_PREPARERECEIVEVIDEO[this.sendJson(
            JsonConstants.PREPARERECEIVEVIDEO_METHOD,
            prepareReceiveVideoFromParams
        )] =
            Pair(remoteParticipant?.connectionId, streamId)
    }

    fun receiveVideoFrom(
        sessionDescription: SessionDescription,
        remoteParticipant: RemoteParticipant?,
        streamId: String
    ) {
        val receiveVideoFromParams: MutableMap<String, String> = HashMap()
        receiveVideoFromParams["sender"] = streamId
        if ("kurento" == mediaServer) {
            Log.d("tete", "!44444")
            receiveVideoFromParams["sdpOffer"] = sessionDescription.description
        } else {
            Log.d("tete", "!6666")
            receiveVideoFromParams["sdpAnswer"] = sessionDescription.description
        }
        IDS_RECEIVEVIDEO.put(this.sendJson(JsonConstants.RECEIVEVIDEO_METHOD, receiveVideoFromParams),
            remoteParticipant?.connectionId!!
        )

    }

    fun onIceCandidate(iceCandidate: IceCandidate, endpointName: String?) {
        val onIceCandidateParams: MutableMap<String, String> = HashMap()
        if (endpointName != null) {
            onIceCandidateParams["endpointName"] = endpointName
        }
        onIceCandidateParams["candidate"] = iceCandidate.sdp
        onIceCandidateParams["sdpMid"] = iceCandidate.sdpMid
        onIceCandidateParams["sdpMLineIndex"] = Integer.toString(iceCandidate.sdpMLineIndex)
        IDS_ONICECANDIDATE.add(
            this.sendJson(
                JsonConstants.ONICECANDIDATE_METHOD,
                onIceCandidateParams
            )
        )
    }

    @Throws(JSONException::class)
    private fun handleServerEvent(json: JSONObject) {
        if (!json.has(JsonConstants.PARAMS)) {
            Log.e(TAG, "No params $json")
        } else {
            val params = JSONObject(json.getString(JsonConstants.PARAMS))
            val method = json.getString(JsonConstants.METHOD)
            when (method) {
                JsonConstants.ICE_CANDIDATE -> iceCandidateEvent(params)
                JsonConstants.PARTICIPANT_JOINED -> participantJoinedEvent(params)
                JsonConstants.PARTICIPANT_PUBLISHED -> participantPublishedEvent(params)
                JsonConstants.PARTICIPANT_LEFT -> participantLeftEvent(params)
                else -> throw JSONException("Unknown method: $method")
            }
        }
    }

    fun sendJson(method: String?): Int {
        return this.sendJson(method, HashMap())
    }

    @Synchronized
    fun sendJson(method: String?, params: Map<String, String>): Int {
        val id = RPC_ID.get()
        val jsonObject = JSONObject()
        try {
            val paramsJson = JSONObject()
            for ((key, value) in params) {
                paramsJson.put(key, value)
            }
            jsonObject.put("jsonrpc", JsonConstants.JSON_RPCVERSION)
            jsonObject.put("method", method)
            jsonObject.put("id", id)
            jsonObject.put("params", paramsJson)
        } catch (e: JSONException) {
            Log.e(TAG, "JSONException raised on sendJson", e)
            return -1
        }
        websocket.sendText(jsonObject.toString())
        RPC_ID.incrementAndGet()
        return id
    }

    // 방에 입장했을 때, 사람들이 있으면 명수만큼 for문 돌려서 구독 (클라이언트 > 서버)
    @Throws(JSONException::class)
    private fun addRemoteParticipantsAlreadyInRoom(result: JSONObject) {
        for (i in 0 until result.getJSONArray(JsonConstants.VALUE).length()) {
            val participantJson = result.getJSONArray(JsonConstants.VALUE).getJSONObject(i)
            val remoteParticipant = newRemoteParticipantAux(participantJson)
            try {
                val streams = participantJson.getJSONArray("streams")
                for (j in 0 until streams.length()) {
                    val stream = streams.getJSONObject(0)
                    val streamId = stream.getString("id")
                    subscribe(remoteParticipant, streamId)
                }
            } catch (e: Exception) {

                Log.e(TAG, "Error in addRemoteParticipantsAlreadyInRoom: " + e.localizedMessage)
            }
        }
    }

    // ???
    @Throws(JSONException::class)
    private fun iceCandidateEvent(params: JSONObject) {
        val iceCandidate = IceCandidate(
            params.getString("sdpMid"),
            params.getInt("sdpMLineIndex"),
            params.getString("candidate")
        )
        val connectionId = params.getString("senderConnectionId")
        val isRemote: Boolean =
            !session.getLocalParticipant()?.connectionId.equals(connectionId)
        val participant =
            if (isRemote) session.getRemoteParticipant(connectionId) else session.getLocalParticipant()
        val pc: PeerConnection = participant?.peerConnection!!
        when (pc.signalingState()) {
            SignalingState.CLOSED -> Log.e(
                "saveIceCandidate error",
                "PeerConnection object is closed"
            )
            SignalingState.STABLE -> if (pc.remoteDescription != null) {
                participant.peerConnection?.addIceCandidate(iceCandidate)
            } else {
                participant.iceCandidateList.add(iceCandidate)
            }
            else -> participant.iceCandidateList.add(iceCandidate)
        }
    }

    // ???
    @Throws(JSONException::class)
    private fun participantJoinedEvent(params: JSONObject) {
        newRemoteParticipantAux(params)
    }

    // 내가 아닌 참석자가 방에 들어왔을 때 ( 서버 > 클라이언트)
    @Throws(JSONException::class)
    private fun participantPublishedEvent(params: JSONObject) {
        val remoteParticipantId = params.getString(JsonConstants.ID)
        val remoteParticipant = session.getRemoteParticipant(remoteParticipantId)
        val streamId = params.getJSONArray("streams").getJSONObject(0).getString("id")
        subscribe(remoteParticipant, streamId)
    }

    // 내가 아닌 참석자가 떠났을 때 ( 서버 > 클라이언트)
    @Throws(JSONException::class)
    private fun participantLeftEvent(params: JSONObject) {
        val remoteParticipant = session.removeRemoteParticipant(params.getString("connectionId"))
        remoteParticipant!!.dispose()
        val mainHandler: Handler = Handler(activity.getMainLooper())
        val myRunnable = Runnable {
            session.removeView(
                remoteParticipant.view
            )
        }
        mainHandler.post(myRunnable)
    }

    // ??????????
    @Throws(JSONException::class)
    private fun newRemoteParticipantAux(participantJson: JSONObject): RemoteParticipant {
        val connectionId = participantJson.getString(JsonConstants.ID)
        var participantName: String? = ""
        if (participantJson.getString(JsonConstants.METADATA) != null) {
            val jsonStringified = participantJson.getString(JsonConstants.METADATA)
            try {
                val json = JSONObject(jsonStringified)
                val clientData = json.getString("clientData")
                if (clientData != null) {
                    participantName = clientData
                }
            } catch (e: JSONException) {
                participantName = jsonStringified
            }
        }
        val remoteParticipant = RemoteParticipant(connectionId, participantName, session)
        session.createRemotePeerConnection(remoteParticipant.connectionId!!)
        return remoteParticipant
    }

    /* "kurento" == mediaServer 일 때의 구독은 두 군데에서 이뤄지는데,
    * 하나는 내가 들어갔는데, 그 방에 이미 사람들이 있거나  (이때는 클라이언트 > 서버)
    * 난 이미 있고, 다른 사람이 추가로 방에 들어왔을 때 ( 이때는 서버 > 클라이언트)
    * */
    private fun subscribe(remoteParticipant: RemoteParticipant?, streamId: String) {
        if ("kurento" == mediaServer) {
            subscriptionInitiatedFromClient(remoteParticipant, streamId)
        } else {
            prepareReceiveVideoFrom(remoteParticipant, streamId)
        }
    }

    private fun subscriptionInitiatedFromClient(
        remoteParticipant: RemoteParticipant?,
        streamId: String
    ) {
        val sdpConstraints = MediaConstraints()
        Log.d("tete", "!3333")
        sdpConstraints.mandatory.add(MediaConstraints.KeyValuePair("offerToReceiveAudio", "true"))
        //////////////////////////////////////////////sdpConstraints.mandatory.add(MediaConstraints.KeyValuePair("offerToReceiveVideo", "true"))
        remoteParticipant!!.peerConnection
            ?.createOffer(object : CustomSdpObserver("remote offer sdp") {
                override fun onCreateSuccess(sessionDescription: SessionDescription) {
                    super.onCreateSuccess(sessionDescription)
                    remoteParticipant.peerConnection?.setLocalDescription(
                        CustomSdpObserver("remoteSetLocalDesc"),
                        sessionDescription
                    )
                    receiveVideoFrom(sessionDescription, remoteParticipant, streamId)
                }

                override fun onCreateFailure(s: String) {
                    Log.e("createOffer error", s)
                }
            }, sdpConstraints)
    }

    // ???????????
    private fun subscriptionInitiatedFromServer(
        remoteParticipant: RemoteParticipant?,
        streamId: String
    ) {
        val sdpConstraints = MediaConstraints()
        sdpConstraints.mandatory.add(MediaConstraints.KeyValuePair("offerToReceiveAudio", "true"))
        //////////////////////////////////////////////sdpConstraints.mandatory.add(MediaConstraints.KeyValuePair("offerToReceiveVideo", "true"))
        session.createAnswerForSubscribing(remoteParticipant!!, streamId, sdpConstraints)
    }

    fun setWebsocketCancelled(websocketCancelled: Boolean) {
        this.websocketCancelled = websocketCancelled
    }

    // 웹 소켓 연결 끊음
    fun disconnect() {
        websocket.disconnect()
    }

    @Throws(Exception::class)
    override fun onStateChanged(websocket: WebSocket, newState: WebSocketState) {
        Log.i(TAG, "State changed: " + newState.name)
    }

    @Throws(Exception::class)
    override fun onConnected(ws: WebSocket, headers: Map<String, List<String>>) {
        Log.i(TAG, "Connected")
        pingMessageHandler()
        joinRoom()
    }

    @Throws(Exception::class)
    override fun onConnectError(websocket: WebSocket, cause: WebSocketException) {
        Log.e(TAG, "Connect error: $cause")
    }

    @Throws(Exception::class)
    override fun onDisconnected(
        websocket: WebSocket,
        serverCloseFrame: WebSocketFrame,
        clientCloseFrame: WebSocketFrame,
        closedByServer: Boolean
    ) {
        Log.e(
            TAG,
            "Disconnected " + serverCloseFrame.closeReason + " " + clientCloseFrame.closeReason + " " + closedByServer
        )
    }

    @Throws(Exception::class)
    override fun onFrame(websocket: WebSocket, frame: WebSocketFrame) {
        Log.i(TAG, "Frame")
    }

    @Throws(Exception::class)
    override fun onContinuationFrame(websocket: WebSocket, frame: WebSocketFrame) {
        Log.i(TAG, "Continuation Frame")
    }

    @Throws(Exception::class)
    override fun onTextFrame(websocket: WebSocket, frame: WebSocketFrame) {
        Log.i(TAG, "Text Frame")
    }

    @Throws(Exception::class)
    override fun onBinaryFrame(websocket: WebSocket, frame: WebSocketFrame) {
        Log.i(TAG, "Binary Frame")
    }

    @Throws(Exception::class)
    override fun onCloseFrame(websocket: WebSocket, frame: WebSocketFrame) {
        Log.i(TAG, "Close Frame")
    }

    @Throws(Exception::class)
    override fun onPingFrame(websocket: WebSocket, frame: WebSocketFrame) {
        Log.i(TAG, "Ping Frame")
    }

    @Throws(Exception::class)
    override fun onPongFrame(websocket: WebSocket, frame: WebSocketFrame) {
        Log.i(TAG, "Pong Frame")
    }

    @Throws(Exception::class)
    override fun onTextMessage(websocket: WebSocket, data: ByteArray) {
    }

    @Throws(Exception::class)
    override fun onBinaryMessage(websocket: WebSocket, binary: ByteArray) {
        Log.i(TAG, "Binary Message")
    }

    @Throws(Exception::class)
    override fun onSendingFrame(websocket: WebSocket, frame: WebSocketFrame) {
        Log.i(TAG, "Sending Frame")
    }

    @Throws(Exception::class)
    override fun onFrameSent(websocket: WebSocket, frame: WebSocketFrame) {
        Log.i(TAG, "Frame sent")
    }

    @Throws(Exception::class)
    override fun onFrameUnsent(websocket: WebSocket, frame: WebSocketFrame) {
        Log.i(TAG, "Frame unsent")
    }

    @Throws(Exception::class)
    override fun onThreadCreated(websocket: WebSocket, threadType: ThreadType, thread: Thread) {
        Log.i(TAG, "Thread created")
    }

    @Throws(Exception::class)
    override fun onThreadStarted(websocket: WebSocket, threadType: ThreadType, thread: Thread) {
        Log.i(TAG, "Thread started")
    }

    @Throws(Exception::class)
    override fun onThreadStopping(websocket: WebSocket, threadType: ThreadType, thread: Thread) {
        Log.i(TAG, "Thread stopping")
    }

    @Throws(Exception::class)
    override fun onError(websocket: WebSocket, cause: WebSocketException) {
        Log.e(TAG, "Error!")
    }

    @Throws(Exception::class)
    override fun onFrameError(
        websocket: WebSocket,
        cause: WebSocketException,
        frame: WebSocketFrame
    ) {
        Log.e(TAG, "Frame error!")
    }

    @Throws(Exception::class)
    override fun onMessageError(
        websocket: WebSocket,
        cause: WebSocketException,
        frames: List<WebSocketFrame>
    ) {
        Log.e(TAG, "Message error! $cause")
    }

    @Throws(Exception::class)
    override fun onMessageDecompressionError(
        websocket: WebSocket, cause: WebSocketException,
        compressed: ByteArray
    ) {
        Log.e(TAG, "Message decompression error!")
    }

    @Throws(Exception::class)
    override fun onTextMessageError(
        websocket: WebSocket,
        cause: WebSocketException,
        data: ByteArray
    ) {
        Log.e(TAG, "Text message error! $cause")
    }

    @Throws(Exception::class)
    override fun onSendError(
        websocket: WebSocket,
        cause: WebSocketException,
        frame: WebSocketFrame
    ) {
        Log.e(TAG, "Send error! $cause")
    }

    @Throws(Exception::class)
    override fun onUnexpectedError(websocket: WebSocket, cause: WebSocketException) {
        Log.e(TAG, "Unexpected error! $cause")
    }

    @Throws(Exception::class)
    override fun handleCallbackError(websocket: WebSocket, cause: Throwable) {
        Log.e(TAG, "Handle callback error! $cause")
    }

    @Throws(Exception::class)
    override fun onSendingHandshake(
        websocket: WebSocket,
        requestLine: String,
        headers: List<Array<String>>
    ) {
        Log.i(TAG, "Sending Handshake! Hello!")
    }

    private fun pingMessageHandler() {
        val initialDelay = 0L
        val executor = ScheduledThreadPoolExecutor(1)
        executor.scheduleWithFixedDelay({
            val pingParams: MutableMap<String, String> =
                HashMap()
            if (ID_PING.get() == -1) {
                // First ping call
                pingParams["interval"] = "5000"
            }
            ID_PING.set(sendJson(JsonConstants.PING_METHOD, pingParams))
        }, initialDelay, PING_MESSAGE_INTERVAL.toLong(), TimeUnit.SECONDS)
    }

    private fun getWebSocketAddress(openviduUrl: String): String {
        return try {
            val url = URL(openviduUrl)
            if (url.port > -1) "wss://" + url.host + ":" + url.port + "/openvidu" else "wss://" + url.host + "/openvidu"
        } catch (e: MalformedURLException) {
            Log.e(TAG, "Wrong URL", e)
            e.printStackTrace()
            ""
        }
    }

    override fun doInBackground(vararg sessionActivities: GameActivity?): Void? {
        try {
            val factory = WebSocketFactory()
            val sslContext = SSLContext.getInstance("TLS")
            sslContext.init(null, trustManagers, SecureRandom())
            factory.sslContext = sslContext
            factory.verifyHostname = false
            websocket = factory.createSocket(getWebSocketAddress(openviduUrl))
            websocket.addListener(this)
            websocket.connect()
        } catch (e: KeyManagementException) {
            Log.e("WebSocket error", e.message!!)
            val mainHandler: Handler = Handler(activity.getMainLooper())
            val myRunnable = Runnable {
                val toast = Toast.makeText(activity, e.message, Toast.LENGTH_LONG)
                toast.show()
                activity.leaveSession()
            }
            mainHandler.post(myRunnable)
            websocketCancelled = true
        } catch (e: NoSuchAlgorithmException) {
            Log.e("WebSocket error", e.message!!)
            val mainHandler: Handler = Handler(activity.getMainLooper())
            val myRunnable = Runnable {
                val toast = Toast.makeText(activity, e.message, Toast.LENGTH_LONG)
                toast.show()
                activity.leaveSession()
            }
            mainHandler.post(myRunnable)
            websocketCancelled = true
        } catch (e: IOException) {
            Log.e("WebSocket error", e.message!!)
            val mainHandler: Handler = Handler(activity.getMainLooper())
            val myRunnable = Runnable {
                val toast = Toast.makeText(activity, e.message, Toast.LENGTH_LONG)
                toast.show()
                activity.leaveSession()
            }
            mainHandler.post(myRunnable)
            websocketCancelled = true
        } catch (e: WebSocketException) {
            Log.e("WebSocket error", e.message!!)
            val mainHandler: Handler = Handler(activity.getMainLooper())
            val myRunnable = Runnable {
                val toast = Toast.makeText(activity, e.message, Toast.LENGTH_LONG)
                toast.show()
                activity.leaveSession()
            }
            mainHandler.post(myRunnable)
            websocketCancelled = true
        }
        return null
    }

    init {
        this.activity = activity
    }

}