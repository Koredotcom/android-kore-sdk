package com.kore.ui.botchat

import android.annotation.SuppressLint
import android.content.Context
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.core.util.Pair
import androidx.lifecycle.viewModelScope
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
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
import com.kore.extensions.stringToDate
import com.kore.helper.SpeechSynthesizerHelper
import com.kore.model.BotEventResponse
import com.kore.model.BotRequest
import com.kore.model.BotResponse
import com.kore.model.PayloadOuter
import com.kore.model.constants.BotResponseConstants
import com.kore.model.constants.BotResponseConstants.KEY_TEMPLATE_TYPE
import com.kore.model.constants.BotResponseConstants.KEY_TEXT
import com.kore.model.constants.BotResponseConstants.TEMPLATE_TYPE_DATE
import com.kore.model.constants.BotResponseConstants.TEMPLATE_TYPE_DATE_RANGE
import com.kore.model.constants.BotResponseConstants.TEMPLATE_TYPE_QUICK_REPLIES
import com.kore.model.constants.BotResponseConstants.THEME_NAME
import com.kore.model.constants.BotResponseConstants.TYPE
import com.kore.network.api.responsemodels.branding.BotBrandingModel
import com.kore.ui.R
import com.kore.ui.base.BaseViewModel
import com.kore.validator.RangeValidator
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@SuppressLint("StaticFieldLeak")
class BotChatViewModel : BaseViewModel<BotChatView>() {
    companion object {
        private val LOG_TAG = BotChatViewModel::class.java.simpleName
    }

    private lateinit var context: Context
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

    private lateinit var token: String
    private var isFirstTime = true
    private var historyOffset = 0
    private var moreHistory = true
    private val handler = Handler(Looper.getMainLooper())

    init {
        botClient.setListener(object : BotConnectionListener {
            override fun onBotResponse(response: String?) {
                response?.let {
                    if (it.contains(BotResponseConstants.KEY_BOT_RESPONSE)) {
                        val botResponse = BotClientHelper.processBotResponse(Gson().fromJson(it, BotResponse::class.java))
                        if (botResponse.message.isEmpty()) return
                        getView()?.showTypingIndicator(botResponse.icon)
                        getView()?.addMessageToAdapter(botResponse)
                        historyOffset += 1
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
                                                    if (innerMap[TYPE] != null) innerMap[TYPE] as String else ""
                                                )
                                            }

                                            else -> {
                                                getView()?.hideQuickReplies()
                                            }
                                        }
                                    }
                                }
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
                        viewModelScope.launch {
                            getBrandingDetails(token)
                            if (isReconnection) {
                                historyOffset = 0
                                moreHistory = true
                                fetchChatHistory(true)
                            }
                        }
                    }

                    else -> {}
                }
            }

            override fun onBotRequest(code: BotRequestState, botRequest: BotRequest) {
                LogUtils.d(LOG_TAG, "$code")
                historyOffset += 1
                getView()?.addMessageToAdapter(botRequest)
            }

            override suspend fun onJwtTokenGenerated(token: String) {
                this@BotChatViewModel.token = token
            }
        })
    }

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
            when (val response = brandingRepository.getBranding(botConfigModel.botId, token)) {
                is Result.Success -> {
                    setButtonBranding(response.data?.v3)
                    getView()?.onBrandingDetails(response.data?.v3)
                }

                else -> {
                    getView()?.onBrandingDetails(null)
                    LogUtils.e(LOG_TAG, "BrandingDetails Response error: $response")
                }
            }
        }
    }

    fun fetchChatHistory(isReconnect: Boolean) {
        if (!moreHistory) {
            getView()?.onChatHistory(emptyList())
            return
        }
        viewModelScope.launch {
            val botConfigModel = getBotConfigModel()

            if (!NetworkUtils.isNetworkAvailable(context)) {
                getView()?.onChatHistory(emptyList())
                Toast.makeText(context, context.getString(R.string.no_network), Toast.LENGTH_SHORT).show()
                return@launch
            }
            if (botConfigModel == null) {
                getView()?.onChatHistory(emptyList())
                Toast.makeText(context, context.getString(R.string.error_config_bot), Toast.LENGTH_SHORT).show()
                return@launch
            }
            val historyCount = preferenceRepository.getIntValue(context, THEME_NAME, HISTORY_COUNT, 0)
            when (val response =
                chatHistoryRepository.getChatHistory(
                    context,
                    if (botConfigModel.isWebHook) BotClient.getJwtToken() else BotClient.getAccessToken(),
                    botConfigModel.botId,
                    botConfigModel.botName,
                    historyOffset,
                    if (historyCount > 10 || historyCount == 0) 10 else historyCount,
                    botConfigModel.isWebHook
                )) {
                is Result.Success -> {
                    var historyMessages = response.data.first
                    if (isReconnect && !isMinimized()) historyMessages = historyMessages.filterIsInstance<BotResponse>()
                    getView()?.onChatHistory(historyMessages)
                    historyOffset += response.data.first.size
                    moreHistory = response.data.second
                    SDKConfig.setIsMinimized(false)
                    if (historyCount > 0)
                        preferenceRepository.putIntValue(context, THEME_NAME, HISTORY_COUNT, 0)
                }

                else -> {
                    getView()?.onChatHistory(emptyList())
                    LogUtils.e(LOG_TAG, "RtmUrlResponse error: $response")
                    SDKConfig.setIsMinimized(false)
                }
            }
        }
    }

    fun limitRange(startDate: String, endDate: String, format: String): CalendarConstraints.Builder {
        val constraintsBuilderRange = CalendarConstraints.Builder()
        val calendarStart = Calendar.getInstance()
        val calendarEnd = Calendar.getInstance()

        val dateFormat = format.replace("DD", "dd").replace("YYYY", "yyyy")
        if (dateFormat.isNotEmpty() && (startDate.isNotEmpty() || endDate.isNotEmpty())) {
            if (startDate.isNotEmpty()) calendarStart.time = startDate.stringToDate(dateFormat)!!
            if (endDate.isNotEmpty()) calendarEnd.time = endDate.stringToDate(dateFormat)!!
            val minDate = calendarStart.timeInMillis
            val maxDate = calendarEnd.timeInMillis
            if (startDate.isNotEmpty() || endDate.isNotEmpty()) {
                constraintsBuilderRange.setStart(minDate)
                constraintsBuilderRange.setEnd(maxDate)
                constraintsBuilderRange.setValidator(RangeValidator(minDate, maxDate))
            }
        } else {
            constraintsBuilderRange.setValidator(RangeValidator(null, null))
        }
        return constraintsBuilderRange
    }

    fun minRange(date: Long): CalendarConstraints.Builder {
        val constraintsBuilderRange = CalendarConstraints.Builder()

        if (date > 0) constraintsBuilderRange.setStart(date)

        constraintsBuilderRange.setValidator(DateValidatorPointForward.now())
        return constraintsBuilderRange
    }

    fun onRangeDatePicked(selection: Pair<Long, Long>) {
        val startDate = selection.first
        val endDate = selection.second
        val cal = Calendar.getInstance()
        cal.timeInMillis = startDate
        val strYear = cal[Calendar.YEAR]
        val strMonth = cal[Calendar.MONTH] + 1
        val strDay = cal[Calendar.DAY_OF_MONTH]
        cal.timeInMillis = endDate
        val endYear = cal[Calendar.YEAR]
        val endMonth = cal[Calendar.MONTH] + 1
        val endDay = cal[Calendar.DAY_OF_MONTH]
        val formattedDate: StringBuilder = java.lang.StringBuilder()
        formattedDate.append((if (strMonth > 9) strMonth else "0$strMonth")).append("-")
            .append(if (strDay > 9) strDay else "0$strDay").append("-").append(strYear)
            .append(" to ").append((if (endMonth > 9) endMonth else "0$endMonth"))
            .append("-").append((if (endDay > 9) endDay else "0$endDay")).append("-")
            .append(endYear)

        sendMessage(formattedDate.toString(), formattedDate.toString())
    }

    fun onDatePicked(selection: Long) {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = selection
        val stYear = calendar[Calendar.YEAR]
        val stMonth = calendar[Calendar.MONTH] + 1
        val stDay = calendar[Calendar.DAY_OF_MONTH]
        val formattedDate =
            ((if (stMonth > 9) stMonth else "0$stMonth").toString() + "-" + (if (stDay > 9) stDay else "0$stDay") + "-" + stYear)
        sendMessage(formattedDate, formattedDate)
    }

    private fun setButtonBranding(brandingModel: BotBrandingModel?) {
        brandingModel?.let {
            if (it.general.colors.useColorPaletteOnly) {
                it.header.bgColor = it.general.colors.secondary
                it.header.avatarBgColor = it.general.colors.primary
                it.header.title?.color = it.general.colors.primary
                it.header.subTitle?.color = it.general.colors.primary

                it.footer.composeBar.outlineColor = it.general.colors.primary
                it.footer.composeBar.inlineColor = it.general.colors.secondaryText
            }

            if (it.body.timeStamp != null) {
                BotResponseConstants.DATE_FORMAT = it.body.timeStamp!!.dateFormat.replace("mm", "MM")
                BotResponseConstants.TIME_FORMAT = it.body.timeStamp!!.timeFormat
            }

            preferenceRepository.putStringValue(context, THEME_NAME, BotResponseConstants.BUBBLE_LEFT_BG_COLOR, it.body.botMessage.bgColor)
            preferenceRepository.putStringValue(context, THEME_NAME, BotResponseConstants.BUBBLE_LEFT_TEXT_COLOR, it.body.botMessage.color)
            preferenceRepository.putStringValue(context, THEME_NAME, BotResponseConstants.BUBBLE_RIGHT_BG_COLOR, it.body.userMessage.bgColor)
            preferenceRepository.putStringValue(context, THEME_NAME, BotResponseConstants.BUBBLE_RIGHT_TEXT_COLOR, it.body.userMessage.color)

            if (it.chatBubble.style.isNotEmpty()) {
                preferenceRepository.putStringValue(context, THEME_NAME, BotResponseConstants.BUBBLE_STYLE, it.chatBubble.style)
            }

            if (it.body.bubbleStyle.isNotEmpty()) {
                preferenceRepository.putStringValue(context, THEME_NAME, BotResponseConstants.BUBBLE_STYLE, it.body.bubbleStyle)
            }

            if (it.general.colors.useColorPaletteOnly) {
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
                preferenceRepository.putBooleanValue(context, THEME_NAME, BotResponseConstants.IS_TIME_STAMP_REQUIRED, timeStamp.show)
            }
        }
    }

    fun onTerminate(isAgentTransfer: Boolean) {
        if (isAgentTransfer && BotClient.isConnected()) {
            sendCloseOrMinimizeEvent(R.string.agent_bot_close_event)
        }
        preferenceRepository.putIntValue(context, THEME_NAME, HISTORY_COUNT, 0)
    }

    fun onMinimize(historyCount: Int) {
        preferenceRepository.putIntValue(context, THEME_NAME, HISTORY_COUNT, historyCount)
    }
}