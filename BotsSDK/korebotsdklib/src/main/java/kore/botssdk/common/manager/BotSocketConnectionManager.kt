package kore.botssdk.common.manager

import android.content.Context
import android.os.Handler
import android.util.Log
import android.widget.Toast
import com.google.gson.Gson
import com.google.gson.JsonElement
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kore.botssdk.bot.BotClient
import kore.botssdk.common.botdb.BotDataPersister
import kore.botssdk.common.constants.BundleConstants
import kore.botssdk.common.eventbus.AuthTokenUpdateEvent
import kore.botssdk.common.eventbus.NetworkEvents
import kore.botssdk.common.eventbus.SocketDataTransferModel
import kore.botssdk.common.listener.SocketChatListener
import kore.botssdk.common.utils.NetworkUtility.Companion.isNetworkConnectionAvailable
import kore.botssdk.common.utils.TTSSynthesizer
import kore.botssdk.event.KoreEventCenter
import kore.botssdk.models.BotInfoModel
import kore.botssdk.models.BotRequest
import kore.botssdk.models.JWTTokenResponse
import kore.botssdk.models.TokenResponseModel
import kore.botssdk.models.UserNameModel
import kore.botssdk.net.RestAPIHelper
import kore.botssdk.net.RestBuilder
import kore.botssdk.net.RestResponse
import kore.botssdk.net.RestResponse.BotCustomData
import kore.botssdk.net.SDKConfiguration
import kore.botssdk.utils.DateUtils
import kore.botssdk.utils.Utils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.util.Date
import java.util.Random

class BotSocketConnectionManager private constructor() : BaseSocketConnectionManager() {
    companion object {
        private var botSocketConnectionManager: BotSocketConnectionManager? = null
        private val LOG_TAG = BotSocketConnectionManager::class.java.simpleName

        @JvmStatic
        val instance: BotSocketConnectionManager
            get() {
                if (botSocketConnectionManager == null) {
                    botSocketConnectionManager = BotSocketConnectionManager()
                }
                return botSocketConnectionManager!!
            }

        @JvmStatic
        fun killInstance() {
            if (botSocketConnectionManager != null) {
                botSocketConnectionManager?.stopDelayMsgTimer()
                botSocketConnectionManager?.stopAlertMsgTimer()
                botSocketConnectionManager?.shutDownConnection()
            }
        }

        private val alertHandler = Handler()
    }

    private var botClient: BotClient? = null
    private var ttsSynthesizer: TTSSynthesizer? = null
    private var accessToken: String? = null
    var chatListener: SocketChatListener? = null
    private var botCustomData: BotCustomData? = null
    private val gson = Gson()
    private var isWithAuth = false
    var connectionState: CONNECTION_STATE? = CONNECTION_STATE.DISCONNECTED
    var botAccessToken: String? = null
    var botUserId: String? = null
    private var botName: String? = null
    var streamId: String? = null
    var userId: String? = null
    override fun onOpen(isReconnection: Boolean) {
        if (botClient == null) return
        connectionState = CONNECTION_STATE.CONNECTED
        if (chatListener != null) {
            chatListener?.onConnectionStateChanged(connectionState, isReconnection)
        }
        botAccessToken = botClient?.accessToken
        botUserId = botClient?.userId
        botClient?.sendMessage(null)
    }

    override fun onClose(code: Int, reason: String) {
        connectionState = CONNECTION_STATE.CONNECTED_BUT_DISCONNECTED
        if (chatListener != null) {
            chatListener?.onConnectionStateChanged(connectionState, false)
        }
    }

    override fun onTextMessage(payload: String) {
        persistBotMessage(payload, false, null)
        if (chatListener != null) {
            chatListener?.onMessage(
                SocketDataTransferModel(
                    EVENT_TYPE.TYPE_TEXT_MESSAGE,
                    payload,
                    null,
                    false
                )
            )
        }
    }

    override fun refreshJwtToken() {
        if (isWithAuth) {
            makeJwtCallWithToken(true)
        } else {
            makeJwtCallWithConfig(true)
        }
    }

    private fun makeJwtCallWithConfig(isRefresh: Boolean) {
        try {
            if (botClient == null) return
            val jwt = botClient?.generateJWT(
                SDKConfiguration.Client.identity,
                SDKConfiguration.Client.client_secret,
                SDKConfiguration.Client.client_id,
                SDKConfiguration.Server.IS_ANONYMOUS_USER
            )
            botName = SDKConfiguration.Client.bot_name
            streamId = SDKConfiguration.Client.bot_id
            if (!isRefresh) {
                botClient?.connectAsAnonymousUser(jwt, botName, streamId, botSocketConnectionManager)
            } else {
                KoreEventCenter.post(jwt)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(context, "Something went wrong in fetching JWT", Toast.LENGTH_SHORT).show()
            connectionState = if (isRefresh) CONNECTION_STATE.CONNECTED_BUT_DISCONNECTED else CONNECTION_STATE.DISCONNECTED
            if (chatListener != null) chatListener?.onConnectionStateChanged(connectionState, false)
        }
    }

    private fun makeJwtCallwithConfig(): String {
        var jwt = ""
        try {
            jwt = botClient?.generateJWTForAPI(
                SDKConfiguration.Client.webHook_identity,
                SDKConfiguration.Client.webHook_client_secret,
                SDKConfiguration.Client.webHook_client_id,
                SDKConfiguration.Server.IS_ANONYMOUS_USER
            ).toString()
            KoreEventCenter.post(jwt)
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(context, "Something went wrong in fetching JWT", Toast.LENGTH_SHORT).show()
        }
        return jwt
    }

    private fun persistBotMessage(payload: String?, isSentMessage: Boolean, sentMsg: BotRequest?) {
        BotDataPersister(context, userId, payload, isSentMessage, sentMsg)
            .loadDataFromNetwork()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<Boolean?> {
                override fun onSubscribe(d: Disposable) {}
                override fun onNext(isSuccess: Boolean) {
                    Log.d(LOG_TAG, "Persistence success")
                }

                override fun onError(e: Throwable) {
                    Log.d(LOG_TAG, "Persistence fail")
                }

                override fun onComplete() {}
            })
    }

    private fun makeJwtCallWithToken(isRefresh: Boolean) {
        val jwtTokenCall = RestBuilder.getRestAPI().getJWTToken(Utils.accessTokenHeader(accessToken), HashMap())
        RestAPIHelper.enqueueWithRetry(jwtTokenCall, object : Callback<JWTTokenResponse?> {
            override fun onResponse(call: Call<JWTTokenResponse?>, response: Response<JWTTokenResponse?>) {
                if (response.isSuccessful) {
                    jwtKeyResponse = response.body()
                    botName = jwtKeyResponse?.botName
                    streamId = jwtKeyResponse?.streamId
                    if (isRefresh) {
                        KoreEventCenter.post(response.body()!!.jwt)
                    }
                } else {
                    connectionState = if (isRefresh) CONNECTION_STATE.CONNECTED_BUT_DISCONNECTED else CONNECTION_STATE.DISCONNECTED
                }
            }

            override fun onFailure(call: Call<JWTTokenResponse?>, t: Throwable) {
                Log.d("token refresh", t.message!!)
                connectionState = if (isRefresh) CONNECTION_STATE.CONNECTED_BUT_DISCONNECTED else CONNECTION_STATE.DISCONNECTED
            }
        })
    }

    private fun makeTokenCallForJwt(isRefresh: Boolean) {
        if (botCustomData == null) {
            botCustomData = BotCustomData()
        }
        botCustomData!!["tenantId"] = SDKConfiguration.Client.tenant_id
        val jwtTokenCall = RestBuilder.getTokenRestAPI().getTokenJWT(botCustomData)
        RestAPIHelper.enqueueWithRetry(jwtTokenCall, object : Callback<TokenResponseModel?> {
            override fun onResponse(call: Call<TokenResponseModel?>, response: Response<TokenResponseModel?>) {
                if (response.isSuccessful) {
                    tokenResponseModel = response.body()
                    botName = tokenResponseModel?.botInfo?.getName()
                    streamId = tokenResponseModel?.botInfo?.get_id()
                    SDKConfiguration.Server.setServerUrl(tokenResponseModel?.koreAPIUrl)
                    SDKConfiguration.Server.setKoreBotServerUrl(tokenResponseModel?.koreAPIUrl)
                    KoreEventCenter.post(tokenResponseModel?.branding)
                    if (!isRefresh) {
                        botClient?.connectAsAnonymousUser(tokenResponseModel?.jwt, botName, streamId, botSocketConnectionManager)
                    } else {
                        KoreEventCenter.post(tokenResponseModel?.jwt)
                    }
                } else {
                    connectionState = if (isRefresh) CONNECTION_STATE.CONNECTED_BUT_DISCONNECTED else CONNECTION_STATE.DISCONNECTED
                }
            }

            override fun onFailure(call: Call<TokenResponseModel?>, t: Throwable) {
                Log.d("token refresh", t.message!!)
                connectionState = if (isRefresh) CONNECTION_STATE.CONNECTED_BUT_DISCONNECTED else CONNECTION_STATE.DISCONNECTED
            }
        })
    }

    override fun onRawTextMessage(payload: ByteArray) {}
    override fun onBinaryMessage(payload: ByteArray) {}
    override fun startAndInitiateConnectionWithAuthToken(
        context: Context,
        userId: String,
        accessToken: String,
        botCustomData: BotCustomData
    ) {
        if (connectionState == null || connectionState === CONNECTION_STATE.DISCONNECTED) {
            this.context = context
            this.userId = userId
            this.accessToken = accessToken
            connectionState = CONNECTION_STATE.CONNECTING
            isWithAuth = true
            KoreEventCenter.post(connectionState)
            botCustomData["kmUId"] = userId
            botCustomData["kmToken"] = accessToken
            botClient = BotClient(context, botCustomData)
            ttsSynthesizer = TTSSynthesizer(context)
            initiateConnection()
        }
    }

    override fun startAndInitiateConnectionWithConfig(context: Context, botCustomData: BotCustomData?) {
        this.botCustomData = botCustomData
        if (connectionState == null || connectionState === CONNECTION_STATE.DISCONNECTED) {
            this.context = context
            connectionState = CONNECTION_STATE.CONNECTING
            if (chatListener != null) {
                chatListener?.onConnectionStateChanged(connectionState, false)
            }
            if (this.botCustomData == null) {
                this.botCustomData = BotCustomData()
            }
            this.botCustomData!!["kmUId"] = userId
            this.botCustomData!!["kmToken"] = accessToken
            isWithAuth = false
            botClient = BotClient(context, this.botCustomData)
            ttsSynthesizer = TTSSynthesizer(context)
            initiateConnection()
        }
    }

    override fun startAndInitiateConnection(
        mContext: Context,
        userId: String,
        accessToken: String,
        userNameModel: UserNameModel,
        orgId: String
    ) {
    }

    private fun initiateConnection() {
        if (!isNetworkConnectionAvailable(context!!)) {
            connectionState = CONNECTION_STATE.DISCONNECTED
            if (chatListener != null) {
                chatListener?.onConnectionStateChanged(connectionState, false)
            }
            return
        }
        if (isWithAuth) {
            makeJwtCallWithToken(false)
        } else {
            if (!SDKConfiguration.Client.isWebHook) makeJwtCallWithConfig(false) else makeJwtCallwithConfig()
        }
    }

    fun sendInitMessage(initialMessage: String?) {
        if (botClient != null) botClient?.sendMessage(initialMessage)
    }

    fun sendMessage(message: String?, payLoad: String?) {
        var message = message
        stopTextToSpeech()
        if (payLoad != null) botClient?.sendMessage(payLoad) else botClient?.sendMessage(message)
        try {
            Gson().getAdapter(JsonElement::class.java).fromJson(message)
            message = null
        } catch (ignored: IOException) {
        }
        //Update the bot content list with the send message
        val botMessage = RestResponse.BotMessage(message)
        val botPayLoad = RestResponse.BotPayLoad()
        botPayLoad.message = botMessage
        val botInfo = BotInfoModel(botName, streamId, null)
        botPayLoad.botInfo = botInfo
        val gson = Gson()
        val jsonPayload = gson.toJson(botPayLoad)
        val botRequest = gson.fromJson(jsonPayload, BotRequest::class.java)
        botRequest.createdOn = DateUtils.isoFormatter.format(Date())
        persistBotMessage(null, true, botRequest)
        if (chatListener != null) {
            chatListener?.onMessage(
                SocketDataTransferModel(
                    EVENT_TYPE.TYPE_MESSAGE_UPDATE,
                    message!!,
                    botRequest,
                    false
                )
            )
        }
    }

    fun sendAttachmentMessage(message: String, attachments: ArrayList<HashMap<String, String>>) {
        stopTextToSpeech()
        if (attachments.size > 0) {
            botClient?.sendMessage(message, attachments)
        } else {
            botClient?.sendMessage(message)
        }

        //Update the bot content list with the send message
        val botMessage = RestResponse.BotMessage(message)
        val botPayLoad = RestResponse.BotPayLoad()
        botPayLoad.message = botMessage
        val botInfo = BotInfoModel(botName, streamId, null)
        botPayLoad.botInfo = botInfo
        val gson = Gson()
        val jsonPayload = gson.toJson(botPayLoad)
        val botRequest = gson.fromJson(jsonPayload, BotRequest::class.java)
        botRequest.createdOn = DateUtils.isoFormatter.format(Date())
        persistBotMessage(null, true, botRequest)
        if (chatListener != null) {
            chatListener?.onMessage(
                SocketDataTransferModel(
                    EVENT_TYPE.TYPE_MESSAGE_UPDATE,
                    message!!,
                    botRequest,
                    false
                )
            )
        }
    }

    fun sendPayload(message: String?, payLoad: String?) {
        stopTextToSpeech()
        if (payLoad != null) botClient?.sendFormData(payLoad, message) else botClient?.sendMessage(message)

        //Update the bot content list with the send message
        val botMessage = RestResponse.BotMessage(message)
        val botPayLoad = RestResponse.BotPayLoad()
        botPayLoad.message = botMessage
        val botInfo = BotInfoModel(botName, streamId, null)
        botPayLoad.botInfo = botInfo
        val gson = Gson()
        val jsonPayload = gson.toJson(botPayLoad)
        val botRequest = gson.fromJson(jsonPayload, BotRequest::class.java)
        botRequest.createdOn = DateUtils.isoFormatter.format(Date())
        persistBotMessage(null, true, botRequest)
        if (chatListener != null) {
            chatListener?.onMessage(
                SocketDataTransferModel(
                    EVENT_TYPE.TYPE_MESSAGE_UPDATE,
                    message!!,
                    botRequest,
                    false
                )
            )
        }
    }

    fun sendPayloadWithParams(message: String?, body: String?, payLoad: String?) {
        stopTextToSpeech()
        botClient?.sendFormData(payLoad, body)

        //Update the bot content list with the send message
        val botMessage = RestResponse.BotMessage(message)
        val botPayLoad = RestResponse.BotPayLoad()
        botPayLoad.message = botMessage
        botPayLoad.botInfo = botClient?.botInfoModel
        val gson = Gson()
        val jsonPayload = gson.toJson(botPayLoad)
        val botRequest = gson.fromJson(jsonPayload, BotRequest::class.java)
        botRequest.createdOn = DateUtils.isoFormatter.format(Date())
        persistBotMessage(null, true, botRequest)
        if (chatListener != null) {
            chatListener?.onMessage(
                SocketDataTransferModel(
                    EVENT_TYPE.TYPE_MESSAGE_UPDATE,
                    message!!,
                    botRequest,
                    false
                )
            )
        }
    }

    override fun shutDownConnection() {
        botSocketConnectionManager = null
        botClient?.disconnect()
        if (ttsSynthesizer != null) {
            ttsSynthesizer?.stopTextToSpeech()
        }
    }

    override fun subscribe() {
        isSubscribed = true
    }

    override fun subscribe(listener: SocketChatListener) {
        isSubscribed = true
        chatListener = listener
        chatListener?.onConnectionStateChanged(connectionState, false)
    }

    override fun unSubscribe() {
        isSubscribed = false
        stopTextToSpeech()
    }

    override fun ttsUpdateListener(isTTSEnabled: Boolean) {
        stopTextToSpeech()
    }

    override fun ttsOnStop() {
        stopTextToSpeech()
    }

    fun stopTextToSpeech() {
        try {
            if (ttsSynthesizer != null) ttsSynthesizer?.stopTextToSpeech()
        } catch (exception: IllegalArgumentException) {
            exception.printStackTrace()
        }
    }

    fun setTtsEnabled(ttsEnabled: Boolean) {
        if (ttsSynthesizer != null) ttsSynthesizer?.setTtsEnabled(ttsEnabled)
    }

    val isTTSEnabled: Boolean
        get() = ttsSynthesizer != null && ttsSynthesizer?.isTtsEnabled() == true

    fun startSpeak(text: String?) {
        if (botClient == null || botClient?.accessToken == null) return
        if (!text.isNullOrEmpty() && isSubscribed && ttsSynthesizer != null) {
            ttsSynthesizer?.speak(text.replace("\\<.*?>".toRegex(), ""), botClient?.accessToken!!)
        }
    }

    fun onEvent(event: NetworkEvents.NetworkConnectivityEvent) {
        if (event.networkInfo != null && event.networkInfo.isConnected) {
            if (botClient?.isConnected == true) return
            checkConnectionAndRetry(context, false)
        }
    }

    fun onEvent(ev: AuthTokenUpdateEvent) {
        if (ev.accessToken != null) {
            accessToken = ev.accessToken
            checkConnectionAndRetry(context, false)
        }
    }

    fun checkConnectionAndRetry(mContext: Context?, isFirstTime: Boolean) {
        ///here going to refresh jwt token from chat activity and it should not
        if (botClient == null) {
            this.context = mContext
            botClient = BotClient(mContext, botCustomData)
            if (isFirstTime) {
                if (chatListener != null) {
                    chatListener?.onConnectionStateChanged(CONNECTION_STATE.CONNECTING, false)
                }
                initiateConnection()
            } else {
                refreshJwtToken()
            }
            return
        }
        if (connectionState === CONNECTION_STATE.DISCONNECTED) initiateConnection() else if (connectionState === CONNECTION_STATE.CONNECTED_BUT_DISCONNECTED) {
            refreshJwtToken()
        }
    }

    fun checkConnectionAndRetryForSignify(context: Context, isFirstTime: Boolean) {
        ///here going to refresh jwt token from chat activity and it should not
        if (botClient == null) {
            this.context = context
            if (botCustomData == null) {
                val IDENTITY = "identity"
                val USERNAME = "userName"
                val preferences = context.getSharedPreferences("signify_preferences", Context.MODE_PRIVATE)
                val identity = preferences.getString(IDENTITY, "")
                val userName = preferences.getString(USERNAME, "")
                botCustomData = BotCustomData()
                botCustomData!![USERNAME] = userName
                botCustomData!![IDENTITY] = identity
                botCustomData!!["userAgent"] = System.getProperty("http.agent")
            }
            botClient = BotClient(context, botCustomData)
            if (!isFirstTime) {
                if (chatListener != null) {
                    chatListener?.onConnectionStateChanged(CONNECTION_STATE.CONNECTING, false)
                }
                initiateConnection()
            } else {
                refreshJwtToken()
            }
            return
        }
        if (connectionState === CONNECTION_STATE.DISCONNECTED) initiateConnection() else if (connectionState === CONNECTION_STATE.CONNECTED_BUT_DISCONNECTED) {
            refreshJwtToken()
        }
    }

    /**
     * initial reconnection count
     */
    private var attemptCount = -1
    private var alertAttemptCount = 0
    private var isAttemptNeeded = true
    private var alertIsAttemptNeeded = true

    /**
     * The reconnection attempt delay(incremental delay)
     *
     * @return
     */
    private val delay: Int
        get() {
            attemptCount++
            if (attemptCount > 6) {
                attemptCount = -1
                isAttemptNeeded = false
            }
            val randomInt = Random()
            var delay = (randomInt.nextInt(5) + 1) * attemptCount * 2500
            if (delay < 3000) delay = 5000
            return delay
        }

    private fun alertDelay(): Int {
        alertAttemptCount++
        if (alertAttemptCount > 2) {
            alertAttemptCount = 0
            alertIsAttemptNeeded = false
        }
        return 55 * 1000
    }

    private fun postDelayMessage() {
        val mDelay = delay
        try {
            val handler = Handler()
            val r = Runnable {
                if (isAttemptNeeded) {
                    if (chatListener != null) {
                        chatListener?.onMessage(Utils.buildBotMessage(BundleConstants.DELAY_MESSAGES[attemptCount], streamId, botName))
                    }
                    postDelayMessage()
                }
            }
            handler.postDelayed(r, mDelay.toLong())
        } catch (e: Exception) {
            Log.d("KoraSocketConnection", ":: The Exception is $e")
        }
    }

    private val alertRunnable = Runnable {
        if (alertIsAttemptNeeded) {
            if (chatListener != null) {
                try {
                    chatListener?.onMessage(
                        Utils.buildBotMessage(
                            BundleConstants.SESSION_END_ALERT_MESSAGES[alertAttemptCount - 1],
                            streamId,
                            botName
                        )
                    )
                } catch (_: ArrayIndexOutOfBoundsException) {
                }
            }
            postAlertDelayMessage()
        }
    }

    init {
        KoreEventCenter.register(this)
    }

    private fun postAlertDelayMessage() {
        val mDelay = alertDelay()
        try {
            alertHandler.postDelayed(alertRunnable, mDelay.toLong())
        } catch (e: Exception) {
            Log.d("KoraSocketConnection", ":: The Exception is $e")
        }
    }

    fun startDelayMsgTimer() {
        isAttemptNeeded = true
        postDelayMessage()
    }

    fun startAlertMsgTimer() {
        alertIsAttemptNeeded = true
        postAlertDelayMessage()
    }

    fun stopDelayMsgTimer() {
        attemptCount = -1
        isAttemptNeeded = false
    }

    fun stopAlertMsgTimer() {
        alertAttemptCount = 0
        alertHandler.removeCallbacks(alertRunnable)
        alertIsAttemptNeeded = false
    }

    fun resetAlertHandler() {
        alertAttemptCount = 0
        alertHandler.removeCallbacks(alertRunnable)
        startAlertMsgTimer()
    }
}