package com.kore.ui.botchat

import android.annotation.SuppressLint
import android.content.Context
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.kore.SDKConfig
import com.kore.SDKConfig.isMinimized
import com.kore.botclient.BotClient
import com.kore.botclient.BotConnectionListener
import com.kore.botclient.BotRequestState
import com.kore.botclient.ConnectionState
import com.kore.botclient.helper.BotClientHelper
import com.kore.common.Result
import com.kore.common.SDKConfiguration.getBotConfigModel
import com.kore.common.utils.LogUtils
import com.kore.common.utils.NetworkUtils
import com.kore.common.utils.ToastUtils
import com.kore.constants.SharedPrefConstants.HISTORY_COUNT
import com.kore.data.repository.branding.BrandingRepository
import com.kore.data.repository.branding.BrandingRepositoryImpl
import com.kore.data.repository.chathistory.ChatHistoryRepository
import com.kore.data.repository.chathistory.ChatHistoryRepositoryImpl
import com.kore.data.repository.dynamicurl.DynamicUrlRepository
import com.kore.data.repository.dynamicurl.DynamicUrlRepositoryImpl
import com.kore.data.repository.preference.PreferenceRepository
import com.kore.data.repository.preference.PreferenceRepositoryImpl
import com.kore.helper.SpeechSynthesizerHelper
import com.kore.model.BotEventResponse
import com.kore.model.BotRequest
import com.kore.model.BotResponse
import com.kore.model.PayloadOuter
import com.kore.model.constants.BotResponseConstants
import com.kore.model.constants.BotResponseConstants.BUTTON_STACKED
import com.kore.model.constants.BotResponseConstants.KEY_TEMPLATE_TYPE
import com.kore.model.constants.BotResponseConstants.KEY_TEXT
import com.kore.model.constants.BotResponseConstants.TEMPLATE_TYPE_ADVANCED_MULTI_SELECT
import com.kore.model.constants.BotResponseConstants.TEMPLATE_TYPE_DATE
import com.kore.model.constants.BotResponseConstants.TEMPLATE_TYPE_DATE_RANGE
import com.kore.model.constants.BotResponseConstants.TEMPLATE_TYPE_OTP
import com.kore.model.constants.BotResponseConstants.TEMPLATE_TYPE_QUICK_REPLIES
import com.kore.model.constants.BotResponseConstants.TEMPLATE_TYPE_RESET_PIN
import com.kore.model.constants.BotResponseConstants.THEME_NAME
import com.kore.model.constants.BotResponseConstants.TYPE
import com.kore.network.api.responsemodels.branding.BotBrandingModel
import com.kore.ui.R
import com.kore.ui.base.BaseViewModel
import com.kore.ui.utils.BundleConstants
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@SuppressLint("StaticFieldLeak")
class BotChatViewModel : BaseViewModel<BotChatView>() {
    companion object {
        private val LOG_TAG = BotChatViewModel::class.java.simpleName
    }

    private var isFirstTime = true
//    private var historyOffset = 0
//    private var moreHistory = true
    private lateinit var token: String
    private var isAgentTransfer = false
    private var isActivityResumed = false
    private lateinit var context: Context
    private var arrMessageList = ArrayList<String>()
    private val handler = Handler(Looper.getMainLooper())
    private val botClient: BotClient = BotClient.getInstance()
    private val chatHistoryRepository: ChatHistoryRepository = ChatHistoryRepositoryImpl()
    private val brandingRepository: BrandingRepository = BrandingRepositoryImpl()
    private val dynamicUrlRepository: DynamicUrlRepository = DynamicUrlRepositoryImpl()
    private val preferenceRepository: PreferenceRepository = PreferenceRepositoryImpl()

    private val speechSynthesizerHelper by lazy {
        SpeechSynthesizerHelper(context).apply { setListener(speechSynthesizerListener) }
    }

//    private val headers by lazy {
//        HashMap<String, Any>().apply {
//            put("alg", "RS256")
//            put("typ", "JWT")
//            put("Content-Type", "application/json; charset=UTF-8")
//        }
//    }
//
//    private val body by lazy {
//        HashMap<String, Any>().apply {
//            put("isAnonymous", false)
//            put("clientId", SDKConfiguration.getBotConfigModel()?.clientId!!)
//            put("identity", SDKConfiguration.getBotConfigModel()?.identity!!)
//            put("aud", "https://idproxy.kore.com/authorize")
//            put("clientSecret", SDKConfiguration.getBotConfigModel()?.clientSecret!!)
//        }
//    }

    init {
        botClient.setListener(object : BotConnectionListener {
            override fun onBotResponse(response: String?) {
                response?.let {
                    if (response.contains("\"from\":\"self\"")) {
                        val botRequest = BotClientHelper.processBotMessage(it)
                        getView()?.addMessageToAdapter(botRequest)
                        return
                    }
                    if (it.contains(BotResponseConstants.KEY_BOT_RESPONSE)) {
                        val botResponse = BotClientHelper.processBotMessage(it)
                        if (botResponse !is BotResponse || botResponse.message.isEmpty()) return

                        isAgentTransfer = botResponse.fromAgent
                        getView()?.showTypingIndicator(botResponse.icon)
                        getView()?.addMessageToAdapter(botResponse)
//                        historyOffset += 1
                        val lastItem = getView()?.getAdapterLastItems() as BotResponse

                        when (val body = lastItem.message[0].cInfo?.body) {
                            is PayloadOuter -> {
                                val innerMap = body.payload
                                if (innerMap != null) {
                                    if (innerMap.containsKey(KEY_TEMPLATE_TYPE)) {
                                        when (innerMap[KEY_TEMPLATE_TYPE]) {
                                            TEMPLATE_TYPE_DATE,
                                            TEMPLATE_TYPE_DATE_RANGE -> getView()?.showCalenderTemplate(innerMap)

                                            TEMPLATE_TYPE_QUICK_REPLIES -> {
                                                getView()?.showQuickReplies(
                                                    innerMap[TEMPLATE_TYPE_QUICK_REPLIES] as List<Map<String, *>>,
                                                    if (innerMap[TYPE] != null) innerMap[TYPE] as String else "",
                                                    innerMap[BUTTON_STACKED] as Boolean? == true
                                                )
                                            }

                                            TEMPLATE_TYPE_OTP -> {
                                                if (innerMap[BotResponseConstants.SLIDER_VIEW] as Boolean? == true) {
                                                    getView()?.showOtpBottomSheet(innerMap)
                                                }
                                            }

                                            TEMPLATE_TYPE_RESET_PIN -> {
                                                if (innerMap[BotResponseConstants.SLIDER_VIEW] as Boolean? == true) {
                                                    getView()?.showPinResetBottomSheet(innerMap)
                                                }
                                            }

                                            TEMPLATE_TYPE_ADVANCED_MULTI_SELECT -> {
                                                if (innerMap[BotResponseConstants.SLIDER_VIEW] as Boolean? == true) {
                                                    getView()?.showAdvancedMultiSelectBottomSheet(botResponse.messageId, innerMap)
                                                }
                                            }

                                            else -> {
                                                getView()?.hideQuickReplies()
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        if (isAgentTransfer && BotClient.isConnected()) {
                            botClient.sendReceipts(BundleConstants.MESSAGE_DELIVERED, botResponse.messageId)
                            if (isActivityResumed) {
                                botClient.sendReceipts(BundleConstants.MESSAGE_READ, botResponse.messageId)
                            } else {
                                arrMessageList.add(botResponse.messageId)
                            }
                        }
                        textToSpeech(botResponse)
                    } else if (it.contains(BotResponseConstants.KEY_EVENTS)) {
                        val botResponse = Gson().fromJson(it, BotEventResponse::class.java)
                        getView()?.onBotEventMessage(botResponse)
                    }
                }
            }

            override fun onConnectionStateChanged(state: ConnectionState, isReconnection: Boolean) {
                getView()?.onConnectionStateChanged(state, isReconnection)
                when (state) {
                    ConnectionState.CONNECTED -> {
                        isFirstTime = false
                        if (isReconnection) getView()?.onLoadHistory(true)
                    }

                    else -> {}
                }
            }

            override fun onBotRequest(code: BotRequestState, botRequest: BotRequest) {
                LogUtils.d(LOG_TAG, "$code")
//                historyOffset += 1
                getView()?.addMessageToAdapter(botRequest)
            }

            override suspend fun onAccessTokenGenerated(token: String) {
                this@BotChatViewModel.token = token
            }

            override fun onAccessTokenReady() {
                viewModelScope.launch {
                    getBrandingDetails(token)
                }
            }
        })
    }

    fun isAgentTransfer(): Boolean = isAgentTransfer

    fun init(context: Context) {
        this.context = context
        getView()?.init()
        if (NetworkUtils.isNetworkAvailable(context)) {
            isFirstTime = true
            connectToBot()
        } else {
            Toast.makeText(context, context.getString(R.string.no_network), Toast.LENGTH_SHORT).show()
        }
    }

    private fun connectToBot() {
        botClient.connectToBot(context, if (isMinimized()) false else isFirstTime)
    }

    fun disconnectBot() {
        botClient.disconnectBot()
    }

    fun sendMessage(message: String, payload: String?) {
        botClient.sendMessage(message, payload)
    }

    fun sendAttachment(message: String, attachments: List<Map<String, *>>?) {
        botClient.sendMessage(message, null, attachments)
    }

    fun downloadFile(msgId: String, url: String, fileName: String?) {
        if (NetworkUtils.isNetworkAvailable(context)) {
            val name = if (!fileName.isNullOrEmpty()) {
                fileName
            } else {
                SimpleDateFormat("yyyyMMdd_HHmmssSSS", Locale.getDefault()).format(Date()) + ".pdf"
            }
            val file = File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), name)
            ToastUtils.showToast(context, context.getString(R.string.download_started), Toast.LENGTH_LONG)

            viewModelScope.launch {
                var prevProgress = -1
                dynamicUrlRepository.downloadFile(url, file) { progress, downloadedBytes ->
                    handler.post {
                        when (progress) {
                            -1 -> ToastUtils.showToast(context, context.getString(R.string.download_fail))
                            100 -> ToastUtils.showToast(context, context.getString(R.string.download_success))
                        }
                        if (prevProgress != progress && (progress <= 1 || progress % 5 == 0)) {
                            prevProgress = progress
                            getView()?.onFileDownloadProgress(msgId, progress, downloadedBytes)
                        }
                    }
                }
            }
        } else {
            getView()?.onFileDownloadProgress(msgId, -1, 0)
            ToastUtils.showToast(context, context.getString(R.string.no_network), Toast.LENGTH_LONG)
        }
    }

    fun sendCloseOrMinimizeEvent(eventId: Int) {
        val botName = getBotConfigModel()?.botName ?: return
        val botId = getBotConfigModel()?.botId ?: return
        botClient.sendCloseOrMinimizeEvent(context.getString(eventId), botName, botId)
        SDKConfig.setIsMinimized(eventId == R.string.bot_minimize_event)
    }

    fun getUserId(): String = botClient.getUserId()

    private val speechSynthesizerListener = object : SpeechSynthesizerHelper.SynthesizerListener {
        override fun startOtherSynthesizer(message: String) {
        }

        override fun stopOtherSynthesizer() {
        }
    }

    fun onNetworkAvailable() {
        if (getBotConfigModel()?.isWebHook == true ||
            (botClient.getConnectionState() != ConnectionState.CONNECTED && !botClient.isConnecting())
        ) {
            botClient.connectToBot(context, isFirstTime)
        }
    }

    fun isWebhook(): Boolean = getBotConfigModel()?.isWebHook == true

    fun textToSpeech(botResponse: BotResponse) {
        if (speechSynthesizerHelper.isTTSEnabled()) {
            var botResponseTextualFormat = ""

            when (val body = botResponse.message[0].cInfo?.body) {
                is String -> botResponseTextualFormat = body

                is PayloadOuter -> {
                    if (body.type.equals(BotResponseConstants.KEY_TEMPLATE)) {
                        val innerMap = body.payload
                        if (innerMap != null && innerMap.containsKey(KEY_TEMPLATE_TYPE)) {
                            if (innerMap.contains(KEY_TEXT)) botResponseTextualFormat = innerMap[KEY_TEXT].toString()
                        } else {
                            innerMap?.let { botResponseTextualFormat = it[KEY_TEXT].toString() }
                        }
                    }
                }
            }

            if (botResponseTextualFormat.isNotEmpty()) speechSynthesizerHelper.startSpeak(botResponseTextualFormat)
        }
    }

    internal suspend fun getBrandingDetails(token: String) {
        val botConfigModel = getBotConfigModel()

        if (NetworkUtils.isNetworkAvailable(context)) {
            if (botConfigModel == null) {
                Toast.makeText(context, context.getString(R.string.error_config_bot), Toast.LENGTH_SHORT).show()
                return
            }
            when (val response = brandingRepository.getBranding(context, botConfigModel.botId, token)) {
                is Result.Success -> {
                    onBrandingDetails(response.data?.brandingModel)
                    getView()?.onBrandingDetails(response.data)
                    getView()?.onLoadHistory(false)
                }

                else -> {
                    getView()?.onBrandingDetails(null)
                    LogUtils.e(LOG_TAG, "BrandingDetails Response error: $response")
                    getView()?.onLoadHistory(false)
                }
            }
        }
    }

    private fun onBrandingDetails(brandingModel: BotBrandingModel?) {
        brandingModel?.let {
            if (it.body.timeStamp != null) {
                BotResponseConstants.DATE_FORMAT = it.body.timeStamp!!.dateFormat.replace("mm", "MM")
                BotResponseConstants.TIME_FORMAT = if (it.body.timeStamp!!.timeFormat.isNotEmpty()) it.body.timeStamp!!.timeFormat.toInt() else 12
            }

            preferenceRepository.putStringValue(context, THEME_NAME, BotResponseConstants.BUBBLE_LEFT_BG_COLOR, it.body.botMessage?.bgColor)
            preferenceRepository.putStringValue(context, THEME_NAME, BotResponseConstants.BUBBLE_LEFT_TEXT_COLOR, it.body.botMessage?.color)
            preferenceRepository.putStringValue(context, THEME_NAME, BotResponseConstants.BUBBLE_RIGHT_BG_COLOR, it.body.userMessage?.bgColor)
            preferenceRepository.putStringValue(context, THEME_NAME, BotResponseConstants.BUBBLE_RIGHT_TEXT_COLOR, it.body.userMessage?.color)

            if (!it.chatBubble?.style.isNullOrEmpty()) {
                preferenceRepository.putStringValue(context, THEME_NAME, BotResponseConstants.BUBBLE_STYLE, it.chatBubble?.style)
            }

            if (it.body.bubbleStyle.isNotEmpty()) {
                preferenceRepository.putStringValue(context, THEME_NAME, BotResponseConstants.BUBBLE_STYLE, it.body.bubbleStyle)
            }

            if (it.general.colors.useColorPaletteOnly == true) {
                preferenceRepository.putStringValue(context, THEME_NAME, BotResponseConstants.BUTTON_ACTIVE_BG_COLOR, it.general.colors.primary)
                preferenceRepository.putStringValue(context, THEME_NAME, BotResponseConstants.BUTTON_ACTIVE_TXT_COLOR, it.general.colors.primaryText)
                preferenceRepository.putStringValue(context, THEME_NAME, BotResponseConstants.BUTTON_INACTIVE_BG_COLOR, it.general.colors.secondary)
                preferenceRepository.putStringValue(context, THEME_NAME, BotResponseConstants.BUTTON_INACTIVE_TXT_COLOR, it.general.colors.secondaryText)
                preferenceRepository.putStringValue(context, THEME_NAME, BotResponseConstants.BUBBLE_LEFT_BG_COLOR, it.general.colors.secondary)
                preferenceRepository.putStringValue(context, THEME_NAME, BotResponseConstants.BUBBLE_LEFT_TEXT_COLOR, it.general.colors.primaryText)
                preferenceRepository.putStringValue(context, THEME_NAME, BotResponseConstants.BUBBLE_RIGHT_BG_COLOR, it.general.colors.primary)
                preferenceRepository.putStringValue(context, THEME_NAME, BotResponseConstants.BUBBLE_RIGHT_TEXT_COLOR, it.general.colors.secondaryText)
            }
            it.body.timeStamp?.let { timeStamp ->
                if (timeStamp.color.isNotEmpty()) preferenceRepository.putStringValue(
                    context,
                    THEME_NAME,
                    BotResponseConstants.TIME_STAMP_TXT_COLOR,
                    timeStamp.color
                )

                preferenceRepository.putBooleanValue(context, THEME_NAME, BotResponseConstants.TIME_STAMP_IS_BOTTOM, timeStamp.position == "Top")
                preferenceRepository.putBooleanValue(context, THEME_NAME, BotResponseConstants.IS_TIME_STAMP_REQUIRED, timeStamp.show ?: false)
            }
        }
    }

    fun onTerminate() {
        if (isAgentTransfer && BotClient.isConnected()) {
            sendCloseOrMinimizeEvent(R.string.agent_bot_close_event)
        }
        preferenceRepository.putIntValue(context, THEME_NAME, HISTORY_COUNT, 0)
    }

    fun onMinimize(historyCount: Int) {
        preferenceRepository.putIntValue(context, THEME_NAME, HISTORY_COUNT, historyCount)
    }

    fun sendReadReceipts() {
        //Added newly for send receipts
        if (BotClient.isConnected() && arrMessageList.isNotEmpty() && isAgentTransfer) {
            arrMessageList.map { botClient.sendReceipts(BundleConstants.MESSAGE_READ, it) }
            arrMessageList.clear()
        }
    }

    fun setIsActivityResumed(isResumed: Boolean) {
        isActivityResumed = isResumed
    }
}