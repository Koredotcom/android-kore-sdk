package com.kore.botclient

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.kore.botclient.helper.BotClientHelper
import com.kore.common.Result
import com.kore.common.SDKConfiguration
import com.kore.common.model.BotConfigModel
import com.kore.common.utils.LogUtils
import com.kore.common.utils.NetworkUtils
import com.kore.common.utils.ToastUtils
import com.kore.data.repository.jwt.JwtRepository
import com.kore.data.repository.jwt.JwtRepositoryImpl
import com.kore.data.repository.webhook.WebHookRepository
import com.kore.data.repository.webhook.WebHookRepositoryImpl
import com.kore.model.BotInfoModel
import com.kore.model.BotMessage
import com.kore.model.BotPayLoad
import com.kore.model.BotRequest
import com.kore.model.BotSocketOptions
import com.kore.model.Meta
import io.crossbar.autobahn.websocket.WebSocketConnection
import io.crossbar.autobahn.websocket.WebSocketConnectionHandler
import io.crossbar.autobahn.websocket.exceptions.WebSocketException
import io.crossbar.autobahn.websocket.interfaces.IWebSocket
import io.crossbar.autobahn.websocket.types.WebSocketOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.URISyntaxException
import java.util.Locale
import java.util.TimeZone
import java.util.Timer

class BotClient private constructor() {
    private val jwtRepository: JwtRepository = JwtRepositoryImpl()
    private val webHookRepository: WebHookRepository = WebHookRepositoryImpl()

    companion object {
        private const val LOG_TAG = "BotClient"
        private const val DATA_IDENTITY = "identity"
        private const val DATA_USERNAME = "userName"
        private const val DATA_USER_AGENT = "userAgent"
        private const val SYS_PROP_HTTP_AGENT = "http.agent"
        private const val KEY_ASSERTION = "assertion"
        private const val KEY_BOT_INFO = "botInfo"
        private const val IS_RECONNECT_PARAM = "&isReconnect=true"
        private const val CONNECTION_MODE_PARAM = "&ConnectionMode=Reconnect"

        private const val RECONNECT_ATTEMPT_LIMIT = 16
        private const val POLL_DELAY_REPEAT = 5000L

        private const val BOT_TOKEN = "botToken"
        private const val HR_ACCESS_TOKEN = "hrAccessToken"
        private const val ITSM_ACCESS_TOKEN = "itsmAccessToken"
        private const val MSG_ON_CONNECT = "ON_CONNECT"
        private var accessToken = ""
        private var jwtToken = ""
        private var userJwtToken = ""
        private var botUserId = ""
        private var connectionState = ConnectionState.DISCONNECTED
        private val socketConnection: IWebSocket = WebSocketConnection()
        private var instance: BotClient? = null
        private var callEventMessage: HashMap<String, Any>? = null

        @JvmStatic
        fun getJwtToken(): String = userJwtToken.ifEmpty { jwtToken }

        @JvmStatic
        fun getAccessToken(): String = accessToken

        @JvmName("getBotUserId")
        @JvmStatic
        fun getUserId(): String = botUserId

        @JvmStatic
        fun isConnected(): Boolean = socketConnection.isConnected

        @JvmStatic
        fun getInstance(): BotClient {
            if (instance == null) instance = BotClient()
            return instance!!
        }

        @JvmStatic
        fun setCallEventMessage(callEventMessage: HashMap<String, Any>) {
            this.callEventMessage = callEventMessage
        }

        @JvmStatic
        fun getCallEventMessage(): HashMap<String, Any>? = callEventMessage
    }

    private lateinit var context: Context
    private var listener: BotConnectionListener? = null
    private val botCustomData: HashMap<String, Any?> = HashMap()
    private lateinit var botInfoModel: BotInfoModel
    private lateinit var botInfoMap: HashMap<String, Any>
    private var timer: Timer? = null
    private var options: BotSocketOptions? = null
    private var isConnecting = false
    private var isReconnectAttemptRequired = false
    private var reconnectAttemptCount = 1
    private val reconnectHandler = Handler(Looper.getMainLooper())

    fun setListener(listener: BotConnectionListener) {
        this.listener = listener
    }

    fun getConnectionState(): ConnectionState = connectionState

    fun isConnecting(): Boolean = isConnecting

    private val handler = Handler(Looper.getMainLooper())

    fun disconnectBot() {
        isReconnectAttemptRequired = false
        try {
            if (socketConnection.isConnected) socketConnection.sendClose()
        } catch (e: Exception) {
            LogUtils.e(LOG_TAG, "Exception while bot disconnection...")
        }
        jwtToken = ""
        accessToken = ""
        botUserId = ""
        handler.removeCallbacksAndMessages(null)
    }

    fun getUserId(): String {
        return botUserId
    }

    fun connectToBot(context: Context, isFirstTime: Boolean, jwtToken: String) {
        this.context = context;
        if (!init(isFirstTime)) return
        userJwtToken = jwtToken
        initiateConnection(!isFirstTime)
    }

    fun connectToBot(context: Context, isFirstTime: Boolean) {
        this.context = context
        if (!init(isFirstTime)) return
        initiateConnection(!isFirstTime)
    }

    private fun init(isFirstTime: Boolean): Boolean {
        if (connectionState == ConnectionState.CONNECTED || isConnecting) return false
        if (!NetworkUtils.isNetworkAvailable(context)) {
            connectionState = ConnectionState.DISCONNECTED
            listener?.onConnectionStateChanged(connectionState, false)
            return false
        }
        if (SDKConfiguration.getBotConfigModel() == null) {
            LogUtils.e(LOG_TAG, "Bot details are not configured!")
            return false
        }
        val botConfigModel = SDKConfiguration.getBotConfigModel() ?: return false
        isReconnectAttemptRequired = true
        botCustomData.clear()
        SDKConfiguration.getCustomData()?.let {
            for ((key, value) in it.entries) {
                botCustomData[key] = value
            }
        }
        botCustomData[DATA_USERNAME] = ""
        botCustomData[DATA_IDENTITY] = ""
        botCustomData[DATA_USER_AGENT] = System.getProperty(SYS_PROP_HTTP_AGENT)
        botInfoModel = BotInfoModel(botConfigModel.botName, botConfigModel.botId, botCustomData)
        val gson = Gson()
        botInfoMap = gson.fromJson(gson.toJson(botInfoModel), object : TypeToken<HashMap<String, Any>>() {}.type)
        connectionState = ConnectionState.CONNECTING
        listener?.onConnectionStateChanged(connectionState, !isFirstTime)
        return true
    }

    private fun initiateConnection(isReconnectionAttempt: Boolean) {
        if (isConnecting) return
        isConnecting = true
        val botConfigModel = SDKConfiguration.getBotConfigModel() ?: return
        if (userJwtToken.isEmpty()) userJwtToken = botConfigModel.jwtToken
        if (userJwtToken.isNotEmpty()) {
            createAccessTokenAndRtmUrl(isReconnectionAttempt) { rtmUrl, isReconnection -> connectToSocket(rtmUrl, isReconnection) }
            return
        }
        try {
            MainScope().launch {
                withContext(Dispatchers.IO) {
                    when (val result = jwtRepository.getJwtToken(botConfigModel.jwtServerUrl, botConfigModel.clientId, botConfigModel.clientSecret, botConfigModel.identity)) {
                        is Result.Success -> {
                            if (result.data?.jwt.isNullOrEmpty()) {
                                ToastUtils.showToast(context, "Jwt token is not created!")
                                return@withContext
                            }
                            result.data?.jwt?.let {
                                jwtToken = it
                                createAccessTokenAndRtmUrl(isReconnectionAttempt) { rtmUrl, isReconnection ->
                                    connectToSocket(rtmUrl, isReconnection)
                                }
                            }
                        }

                        else -> {
                            onError(isReconnectionAttempt)
                            Log.e(LOG_TAG, "Jwt Token creation failed")
                        }
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(context, "Something went wrong in fetching JWT token", Toast.LENGTH_SHORT).show()
        }
    }

    private fun createAccessTokenAndRtmUrl(
        isReconnectionAttempt: Boolean,
        connect: (url: String?, isReconnectionAttempt: Boolean) -> Unit
    ) {
        if (getJwtToken().isEmpty()) {
            isConnecting = false
            LogUtils.e(LOG_TAG, "jwtToken is empty!. Please create jwtToken to continue!")
            return
        }
        accessToken = ""
        val request = HashMap<String, Any>()
        request[KEY_ASSERTION] = getJwtToken()
        request[KEY_BOT_INFO] = botInfoModel
        MainScope().launch {
            withContext(Dispatchers.IO) {
                try {
                    when (val result = jwtRepository.getJwtGrant(request)) {
                        is Result.Success -> {
                            val botAuthorizationResponse = result.data
                            botUserId = botAuthorizationResponse?.userInfo?.userId ?: ""
                            accessToken = botAuthorizationResponse?.authorization?.accessToken ?: ""
                            listener?.onAccessTokenGenerated(accessToken)
                            if (SDKConfiguration.getBotConfigModel()?.isWebHook == true) {
                                getWebHookMeta(getJwtToken(), isReconnectionAttempt)
                                return@withContext
                            }
                            request.remove(KEY_ASSERTION)
                            when (val rtmResult = jwtRepository.getRtmUrl(accessToken, request)) {
                                is Result.Success -> {
                                    try {
                                        connect(rtmResult.data?.url, isReconnectionAttempt)
                                    } catch (e: URISyntaxException) {
                                        e.printStackTrace()
                                        onError(isReconnectionAttempt)
                                    }
                                }

                                is Result.Error -> {
                                    LogUtils.e(LOG_TAG, "RtmUrlResponse error: ${rtmResult.exception.message}")
                                    onError(isReconnectionAttempt)
                                }
                            }
                        }

                        is Result.Error -> {
                            LogUtils.e(LOG_TAG, "getJwtGrant error: ${result.exception.message}")
                            onError(isReconnectionAttempt)
                        }
                    }
                } catch (e: Exception) {
                    LogUtils.e(LOG_TAG, "Something went wrong in Jwt grant. error: ${e.message}")
                    onError(isReconnectionAttempt)
                }
            }
        }
    }

    private fun onError(isReconnectionAttempt: Boolean) {
        isConnecting = false
        connectionState = ConnectionState.DISCONNECTED
        reconnectHandler.post {
            listener?.onConnectionStateChanged(connectionState, isReconnectionAttempt)
            reconnectToBot(isReconnectionAttempt)
        }
    }

    private fun connectToSocket(url: String?, isReconnectionAttempt: Boolean) {
        listener?.onAccessTokenReady()
        if (url != null) {
            var rtmUrl = url
            options?.let {
                rtmUrl = it.replaceOptions(url, it)
            }
            val connectOptions = WebSocketOptions()
            connectOptions.maxFramePayloadSize = 256 * 1024
            connectOptions.maxMessagePayloadSize = 256 * 1024
            connectOptions.reconnectInterval = 0
            try {
                val queryParams = StringBuilder()
                SDKConfiguration.getQueryParams()?.let {
                    for ((key, value) in it.entries) {
                        queryParams.append("&")
                        queryParams.append(key)
                        queryParams.append("=")
                        queryParams.append(value)
                    }
                }
                val connectionMode = SDKConfiguration.getConnectionMode();
                val socketUrl = if (isReconnectionAttempt) {
                    rtmUrl + if (connectionMode.isNullOrEmpty()) IS_RECONNECT_PARAM else IS_RECONNECT_PARAM + CONNECTION_MODE_PARAM + queryParams
                } else {
                    rtmUrl + if (!connectionMode.isNullOrEmpty()) connectionMode else "" + queryParams
                }
                LogUtils.d(LOG_TAG, "socketUrl $socketUrl")
                socketConnection.connect(
                    socketUrl,
                    object : WebSocketConnectionHandler() {
                        override fun onOpen() {
                            LogUtils.d(LOG_TAG, "Connection Open...")
                            reconnectHandler.removeCallbacksAndMessages(null)
                            connectionState = ConnectionState.CONNECTED
                            listener?.onConnectionStateChanged(connectionState, isReconnectionAttempt)
                            isConnecting = false
                            reconnectAttemptCount = 1
                        }

                        override fun onClose(code: Int, reason: String?) {
                            accessToken = ""
                            jwtToken = ""
                            botUserId = ""

                            LogUtils.d(LOG_TAG, "Connection Lost...")
                            connectionState = ConnectionState.DISCONNECTED
                            listener?.onConnectionStateChanged(connectionState, isReconnectionAttempt)
                            timer?.let {
                                it.cancel()
                                timer = null
                            }
                            isConnecting = false
                            reconnectToBot(true)
                        }

                        override fun onMessage(payload: String?) {
                            LogUtils.i(LOG_TAG, payload)
                            listener?.onBotResponse(payload)
                        }

                        override fun onPing() {
                            LogUtils.d(LOG_TAG, "Websocket Ping received")
                        }

                        override fun onPing(payload: ByteArray?) {
                            LogUtils.d(LOG_TAG, "Websocket Ping received")
                        }

                        override fun onPong() {
                            LogUtils.d(LOG_TAG, "Websocket Pong received")
                        }

                        override fun onPong(payload: ByteArray?) {
                            LogUtils.d(LOG_TAG, "Websocket Pong received")
                        }
                    }, connectOptions
                )
            } catch (e: WebSocketException) {
                isConnecting = false
                if (e.message != null && e.message.equals("already connected")) {
                    LogUtils.d(LOG_TAG, "Already connected to bot...")
                    connectionState = ConnectionState.CONNECTED
                    reconnectHandler.removeCallbacksAndMessages(null)
                    reconnectHandler.post {
                        listener?.onConnectionStateChanged(connectionState, isReconnectionAttempt)
                    }
                    reconnectAttemptCount = 1
                }
                e.printStackTrace()
            }
        }
    }

    internal fun reconnectToBot(isReconnectionAttempt: Boolean) {
        if (isReconnectAttemptRequired) {
            isConnecting = false
            reconnectHandler.postDelayed({ if (isReconnectAttemptRequired) initiateConnection(isReconnectionAttempt) }, getReconnectDelay())
        }
    }

    /**
     * The reconnection attempt delay(incremental delay)
     *
     * @return
     */
    private fun getReconnectDelay(): Long {
        reconnectAttemptCount += if (reconnectAttemptCount == 1) 1 else 2
        if (reconnectAttemptCount > RECONNECT_ATTEMPT_LIMIT) reconnectAttemptCount = 1

        LogUtils.d(LOG_TAG, "Reconnection count $reconnectAttemptCount")

        return (reconnectAttemptCount * 1000).toLong()
    }

    private fun setMoreCustomData() {
        botCustomData[BOT_TOKEN] = accessToken
        if (SDKConfiguration.getLoginToken() != null) {
            botCustomData[HR_ACCESS_TOKEN] = SDKConfiguration.getLoginToken()
            botCustomData[ITSM_ACCESS_TOKEN] = SDKConfiguration.getLoginToken()
        }
    }

    fun sendMessage(msg: String, payload: String?, attachments: List<Map<String, *>>? = null) {
        setMoreCustomData()
        val botConfigModel = SDKConfiguration.getBotConfigModel() ?: return
        BotClientHelper.createRequestPayload(
            msg,
            payload ?: msg,
            if (payload != null && msg.isNotEmpty()) msg else null,
            attachments,
            botCustomData,
            botInfoModel
        ) { jsonPayload, botRequest ->
            if (botConfigModel.isWebHook) {
                sendWebHookMessage(botRequest, false, msg, payload, attachments)
                return@createRequestPayload
            }
            LogUtils.d(LOG_TAG, "Payload: $jsonPayload")
            if (isConnected()) {
                socketConnection.sendMessage(jsonPayload)
                if (msg.isNotEmpty()) listener?.onBotRequest(BotRequestState.SENT_BOT_REQ_SUCCESS, botRequest)
            } else {
                if (msg.isNotEmpty()) listener?.onBotRequest(BotRequestState.SENT_BOT_REQ_FAIL, botRequest)
            }
        }
    }

    private fun sendWebHookMessage(
        botRequest: BotRequest?,
        isNewSession: Boolean,
        msg: String,
        payload: String?,
        attachments: List<Map<String, *>>?
    ) {
        if (!NetworkUtils.isNetworkAvailable(context)) {
            ToastUtils.showToast(context, context.getString(R.string.no_network), Toast.LENGTH_LONG)
            return
        }
        MainScope().launch {
            val botConfigModel = SDKConfiguration.getBotConfigModel() ?: return@launch
            botRequest?.let {
                if (msg.isNotEmpty() && msg != MSG_ON_CONNECT) listener?.onBotRequest(BotRequestState.SENT_BOT_REQ_SUCCESS, it)
            }
            setMoreCustomData()
            BotClientHelper.createWebHookRequestPayload(isNewSession, msg, payload, attachments, botConfigModel, botCustomData)
            { jsonPayload ->
                LogUtils.d(LOG_TAG, "Webhook Payload: $jsonPayload")
                MainScope().launch {
                    when (val result = webHookRepository.sendMessage(botConfigModel.botId, getJwtToken(), jsonPayload)) {
                        is Result.Success -> {
                            if (msg == MSG_ON_CONNECT) {
                                listener?.onConnectionStateChanged(ConnectionState.CONNECTED, false)
                            }
                            result.data.first?.let {
                                it.map { response -> listener?.onBotResponse(Gson().toJson(response)) }
                                if (result.data.second.isNotEmpty()) {
                                    startSendingPoll(result.data.second)
                                }
                            }
                        }

                        is Result.Error -> LogUtils.e(LOG_TAG, "Error ${result.exception.message}")
                    }
                    isConnecting = false
                }
            }
        }
    }

    private fun getWebHookMeta(token: String, isReconnectionAttempt: Boolean) {
        if (!NetworkUtils.isNetworkAvailable(context)) {
            isConnecting = false
            reconnectToBot(isReconnectionAttempt)
            ToastUtils.showToast(context, context.getString(R.string.no_network), Toast.LENGTH_LONG)
            return
        }
        val botConfigModel = SDKConfiguration.getBotConfigModel() ?: return
        MainScope().launch {
            when (val result = webHookRepository.getWebhookMeta(token, botConfigModel.botId)) {
                is Result.Success -> {
                    sendWebHookMessage(null, !isReconnectionAttempt, MSG_ON_CONNECT, null, null)
                }

                is Result.Error -> {
                    LogUtils.e(LOG_TAG, "Webhook meta Error ${result.exception.message}")
                    reconnectToBot(isReconnectionAttempt)
                    ToastUtils.showToast(context, "Something went wrong!")
                }
            }
        }
    }

    private fun startSendingPoll(pollId: String) {
        if (pollId.isEmpty()) return
        handler.postDelayed({ postPollingData(pollId) }, POLL_DELAY_REPEAT)
    }

    private fun postPollingData(pollId: String) {
        val botConfigModel = SDKConfiguration.getBotConfigModel() ?: return
        MainScope().launch {
            when (val result = webHookRepository.getPollData(getJwtToken(), botConfigModel.botId, pollId)) {
                is Result.Success -> result.data.first?.let {
                    it.map { response -> listener?.onBotResponse(Gson().toJson(response)) }
                    if (result.data.second.isNotEmpty()) {
                        startSendingPoll(result.data.second)
                    } else {
                        handler.removeCallbacksAndMessages(null)
                    }
                }

                is Result.Error -> {
                    LogUtils.e(LOG_TAG, "Error polling ${result.exception.message}")
                    handler.removeCallbacksAndMessages(null)
                }
            }
        }
    }

    fun sendCloseOrMinimizeEvent(event: String, botName: String, botId: String) {
        if (!isConnected()) return
        botCustomData[BOT_TOKEN] = getAccessToken()
        val botMessage = BotMessage("", null, null, botCustomData)
        botInfoModel = BotInfoModel(botName, botId, botCustomData)
        val meta = Meta(TimeZone.getDefault().id, Locale.getDefault().isO3Language)
        val botPayLoad = BotPayLoad(botMessage, meta = meta, event = event, botInfo = botInfoModel)
        val gson = Gson()
        val jsonPayload = gson.toJson(botPayLoad)
        LogUtils.d("BotClient", "Payload : $jsonPayload")
        socketConnection.sendMessage(jsonPayload)
    }

    /**
     * Method to send messages over socket.
     * It uses FIFO pattern to first send if any pending requests are present
     * following current request later onward.
     * pass 'msg' as NULL on reconnection of the socket to empty the pool
     * by sending messages from the pool.
     */
    fun sendReceipts(eventName: String?, msgId: String?) {
        val botMessage = BotMessage("", null, null, botCustomData)
        botCustomData["botToken"] = getAccessToken()
        val configModel: BotConfigModel = SDKConfiguration.getBotConfigModel() ?: return

        botInfoModel = BotInfoModel(configModel.botName, configModel.botId, botCustomData)
        val meta = Meta(TimeZone.getDefault().id, Locale.getDefault().isO3Language)
        val botPayLoad = BotPayLoad(botMessage, meta = meta, event = eventName, botInfo = botInfoModel)

        val jsonPayload = Gson().toJson(botPayLoad)
        LogUtils.d("BotClient", "Payload recipient : $jsonPayload")
        socketConnection.sendMessage(jsonPayload)
    }
}