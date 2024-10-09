package com.kore.botclient.helper

import android.content.Context
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.kore.common.utils.LogUtils
import com.kore.model.BaseBotMessage
import com.kore.model.BotRequest
import com.kore.model.BotResponse
import com.kore.model.ComponentModel
import com.kore.model.constants.BotResponseConstants
import com.kore.model.constants.BotResponseConstants.TEMPLATE_TYPE_BOT_ACTIVE
import com.kore.model.constants.BotResponseConstants.TEMPLATE_TYPE_BOT_KIT_UNREACHABLE

class BotResponseHelper {
    companion object {
        private var LOG_TAG = BotResponseHelper::class.java.simpleName

        var lastMsgId = ""

        fun getPayloadOuter(botResponse: BotResponse): Any? =
            if (botResponse.message.isNotEmpty()) {
                val compModel = botResponse.message[0].component
                if (compModel != null) {
                    val compType = compModel.type
                    if (BotResponseConstants.COMPONENT_TYPE_TEMPLATE.equals(compType, ignoreCase = true)) {
                        compModel.payload
                    } else null
                } else null
            } else null

        fun processPayload(
            context: Context,
            payload: String,
            botLocalResponse: BotResponse?,
            addMessageToBotChatAdapter: (botResponse: BotResponse) -> Unit,
            updateContentListOnSend: (botRequest: BotRequest) -> Unit,
            setQuickReplies: (botResponse: BotResponse?) -> Unit = {},
            setBotTypingStatus: (botResponse: BotResponse) -> Unit = {}
        ) {
//            if (botLocalResponse == null) BotSocketConnectionManager.getInstance().stopDelayMsgTimer()
            val gson = Gson()
            try {
                LogUtils.d(LOG_TAG, payload)
                if (payload.contains(TEMPLATE_TYPE_BOT_ACTIVE)) {
//                    addMessageToBotChatAdapter(BotResponse().apply { type = TEMPLATE_TYPE_BOT_ACTIVE })
                    return
                } else if (payload.contains(TEMPLATE_TYPE_BOT_KIT_UNREACHABLE)) {
//                    addMessageToBotChatAdapter(BotResponse().apply { type = TEMPLATE_TYPE_BOT_KIT_UNREACHABLE })
                    return
                }
                val botResponse = botLocalResponse ?: gson.fromJson(payload, BotResponse::class.java)
                if (botResponse == null || botResponse.message.isNullOrEmpty()) return

//                if (!botResponse.message.isNullOrEmpty()) {
//                    val compModel = botResponse.message!![0]?.component
//                    if (compModel != null) {
//                        var payOuter = compModel.payload
//                        payOuter?.text?.let {
//                            payOuter = if (it.contains("&quot") || it.contains("*")) {
//                                val textNew = it.replace("&quot;", "\"")
//                                if (!compModel.type.equals(BotResponseConstants.COMPONENT_TYPE_TEXT)) {
//                                    gson.fromJson(textNew, PayloadOuter::class.java)
//                                } else {
//                                    payOuter?.apply { text = textNew }
//                                }
//                            } else {
//                                payOuter
//                            }
//                        }
////                        val payloadInner: PayloadInner? = payOuter.payload
////                        if (payloadInner?.templateType != null && "start_timer".equals(payloadInner.templateType, ignoreCase = true)) {
//////                            BotSocketConnectionManager.getInstance().startDelayMsgTimer()
////                        }
////                        setBotTypingStatus(botResponse)
////                        payloadInner?.convertElementToAppropriate()
////                        botResponse.messageId?.let { lastMsgId = it }
//                        addMessageToBotChatAdapter(botResponse)
//                        setQuickReplies(botResponse)
//                    }
//                }

            } catch (e: Exception) {
                e.printStackTrace()
                if (e is JsonSyntaxException) {
                    try {
                        //This is the case Bot returning user sent message from another channel
                        val botRequest: BotRequest = gson.fromJson(payload, BotRequest::class.java)
//                        botRequest.createdOn = DateUtils.isoFormatter.format(Date())
                        updateContentListOnSend(botRequest)
                    } catch (e1: Exception) {
                        e1.printStackTrace()
//                        try {
//                            val botResponse: BotResponsePayLoadText = gson.fromJson(payload, BotResponsePayLoadText::class.java)
//                            if (botResponse.message.isNullOrEmpty()) return
//                            Log.d(LOG_TAG, payload)
//                            if (botResponse.message.isNotEmpty()) {
//                                val compModel = botResponse.message[0].component
//                                if (!compModel?.payload.isNullOrEmpty()) {
//                                    displayMessage(
//                                        compModel?.payload!!,
//                                        BotResponseConstants.COMPONENT_TYPE_TEXT,
//                                        botResponse.messageId,
//                                    ) { localPayload, response ->
//                                        processPayload(
//                                            context,
//                                            localPayload,
//                                            response,
//                                            addMessageToBotChatAdapter,
//                                            updateContentListOnSend,
//                                            setQuickReplies,
//                                            setBotTypingStatus
//                                        )
//                                    }
//                                }
//                            }
//                        } catch (e2: Exception) {
//                            e2.printStackTrace()
//                        }
                    }
                }
            }
        }

//        private fun displayMessage(
//            text: String,
//            type: String?,
//            messageId: String?,
//            processPayload: (payload: String, botLocalResponse: BotResponse?) -> Unit
//        ) {
//            var isDeserializeSuccess = false
//            var payloadOuter: PayloadOuter? = null
//
//            if (!lastMsgId.equals(messageId, ignoreCase = true)) {
//                try {
//                    payloadOuter = Gson().fromJson(text, PayloadOuter::class.java)
//                    if (payloadOuter.type.isNullOrEmpty()) payloadOuter.type = type
//                    isDeserializeSuccess = true
//
//                } catch (e: java.lang.Exception) {
//                    e.printStackTrace()
//                } finally {
//                    payloadOuter = if (!isDeserializeSuccess) {
//                        val payloadInner = PayloadInner()
//                        payloadInner.templateType = "text"
//                        payloadOuter = PayloadOuter()
//                        payloadOuter.text = text
//                        payloadOuter.type = "text"
//                        payloadOuter.payload = payloadInner
//                        payloadOuter
//                    } else {
//                        payloadOuter
//                    }
//                    val componentModel = ComponentModel(
//                        if (isDeserializeSuccess) payloadOuter?.type.toString() else "text",
//                        payloadOuter!!
//                    )
//
//                    val botResponseMessage = BotResponseMessage()
//                    botResponseMessage.type = if (isDeserializeSuccess) componentModel.type else "text"
//                    botResponseMessage.component = componentModel
//
//                    val arrBotResponseMessages = java.util.ArrayList<BotResponseMessage>()
//                    arrBotResponseMessages.add(botResponseMessage)
//
//                    val botResponse = BotResponse()
//                    botResponse.type = if (isDeserializeSuccess) componentModel.type else "text"
//                    botResponse.message = arrBotResponseMessages
//                    botResponse.messageId = messageId
//                    processPayload("", botResponse)
//                }
//            }
//        }

//        fun textToSpeech(botResponse: BotResponse/*, speechSynthesizerHelper: SpeechSynthesizerHelper*/) {
//            if (botResponse.message.isNullOrEmpty()) return
//            var botResponseTextualFormat = ""
//            val componentModel = botResponse.message!![0].component
//            if (componentModel != null) {
//                val compType = componentModel.type
//                var payOuter = componentModel.payload
//
//                when {
//                    BotResponseConstants.COMPONENT_TYPE_TEXT.equals(compType, ignoreCase = true) || payOuter.type == null -> {
//                        botResponseTextualFormat = payOuter.speechHint ?: payOuter.text
//                    }
//
//                    BotResponseConstants.COMPONENT_TYPE_ERROR.equals(payOuter.type, ignoreCase = true) -> {
//                        botResponseTextualFormat = payOuter.payload?.text.toString()
//                    }
//
//                    BotResponseConstants.COMPONENT_TYPE_TEMPLATE.equals(payOuter.type, ignoreCase = true) ||
//                            BotResponseConstants.COMPONENT_TYPE_MESSAGE.equals(payOuter.type, ignoreCase = true) -> {
//                        if (payOuter.text.contains("&quot")) {
//                            val gson = Gson()
//                            payOuter = gson.fromJson(payOuter.text.replace("&quot;", "\""), PayloadOuter::class.java)
//                        }
//                        val payInner: PayloadInner? = payOuter.payload
//                        when {
//                            payInner?.speechHint != null -> {
//                                botResponseTextualFormat = payInner.speechHint
//                            }
//
//                            BotResponseConstants.TEMPLATE_TYPE_BUTTON.equals(payInner?.templateType, ignoreCase = true) ||
//                                    BotResponseConstants.TEMPLATE_TYPE_QUICK_REPLIES.equals(payInner?.templateType, true) ||
//                                    BotResponseConstants.TEMPLATE_TYPE_CAROUSEL.equals(payInner?.templateType, ignoreCase = true) ||
//                                    BotResponseConstants.TEMPLATE_TYPE_CAROUSEL_ADV.equals(payInner?.templateType, ignoreCase = true) ||
//                                    BotResponseConstants.TEMPLATE_TYPE_LIST.equals(payInner?.templateType, ignoreCase = true)
//                            -> {
//                                botResponseTextualFormat = payInner?.text ?: ""
//                            }
//                        }
//                    }
//                }
//            }
////            if (speechSynthesizerHelper.isTTSEnabled()) {
////                speechSynthesizerHelper.startSpeak(botResponseTextualFormat)
////            }
//        }

        fun getComponentModel(baseBotMessage: BaseBotMessage): ComponentModel? {
            var compModel: ComponentModel? = null
            if (baseBotMessage is BotResponse && !baseBotMessage.message.isNullOrEmpty()) {
                compModel = baseBotMessage.message!![0]?.component
            }
            return compModel
        }

//        fun getJsonRequest(new_session: Boolean, msg: String, attachments: ArrayList<HashMap<String, String>>?): HashMap<String, Any> {
//            val hsh = HashMap<String, Any>()
//            try {
//                val webHookRequestModel = WebHookRequestModel()
//                val session = WebHookRequestModel.Session()
//                session.newSession = new_session
//                webHookRequestModel.session = session
//                hsh["session"] = session
//                val message = WebHookRequestModel.Message()
//                message.value = msg
//                if (new_session) message.type = "event" else message.type = "text"
//                webHookRequestModel.message = message
//                hsh["message"] = message
//                val from = WebHookRequestModel.From()
////                from.id = SDKConfiguration.Client.webHook_identity
//                val userInfo = WebHookRequestModel.From.WebHookUserInfo("", "", "")
//                from.userInfo = userInfo
//                webHookRequestModel.from = from
//                hsh["from"] = from
//                val to = WebHookRequestModel.To("Kore.ai", WebHookRequestModel.To.GroupInfo("", ""))
//                webHookRequestModel.to = to
//                hsh["to"] = to
//                val token = WebHookRequestModel.Token()
//                hsh["token"] = token
//                if (!attachments.isNullOrEmpty()) hsh["attachments"] = attachments
//                Gson().toJson(webHookRequestModel)
//            } catch (e: Exception) {
//                Log.e("Exception", e.message!!)
//            }
//            return hsh
//        }

//        fun processWebHookResponse(
//            context: Context,
//            model: WebHookResponseDataModel,
//            addMessageToBotChatAdapter: (botResponse: BotResponse) -> Unit,
//            updateContentListOnSend: (botRequest: BotRequest) -> Unit,
//            setQuickReplies: (botResponse: BotResponse?) -> Unit = {},
//        ) {
//            if (!model.data.isNullOrEmpty()) {
//                for (data in model.data) {
//                    if (data.value is String) {
//                        displayMessage(data.value.toString(), data.type, data.messageId) { payload, response ->
//                            processPayload(context, payload, response, addMessageToBotChatAdapter, updateContentListOnSend, setQuickReplies)
//                        }
//                    } else {
//                        val gson = Gson()
//                        try {
//                            val elementsAsString: String = gson.toJson(data.value)
//                            val carouselType = object : TypeToken<PayloadOuter?>() {}.type
//                            val payloadOuter: PayloadOuter = gson.fromJson(elementsAsString, carouselType)
//                            displayMessage(context, payloadOuter, addMessageToBotChatAdapter, updateContentListOnSend)
//                        } catch (e: java.lang.Exception) {
//                            try {
//                                val elementsAsString: String = gson.toJson(data.value)
//                                val carouselType = object : TypeToken<PayloadHeaderModel?>() {}.type
//                                val payloadOuter: PayloadHeaderModel = gson.fromJson(elementsAsString, carouselType)
//                                if (payloadOuter.payload != null) {
//                                    displayMessage(
//                                        payloadOuter.payload?.templateType!!,
//                                        BotResponseConstants.COMPONENT_TYPE_TEXT,
//                                        data.messageId
//                                    ) { payload, botLocalResponse ->
//                                        processPayload(
//                                            context,
//                                            payload,
//                                            botLocalResponse,
//                                            addMessageToBotChatAdapter,
//                                            updateContentListOnSend,
//                                            setQuickReplies
//                                        )
//                                    }
//                                }
//                            } catch (ex: java.lang.Exception) {
//                                val elementsAsString: String = gson.toJson(data.value)
//                                displayMessage(
//                                    elementsAsString,
//                                    BotResponseConstants.COMPONENT_TYPE_TEXT,
//                                    data.messageId
//                                ) { payload, botLocalResponse ->
//                                    processPayload(
//                                        context,
//                                        payload,
//                                        botLocalResponse,
//                                        addMessageToBotChatAdapter,
//                                        updateContentListOnSend,
//                                        setQuickReplies
//                                    )
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//        }

//        private fun displayMessage(
//            context: Context,
//            payloadOuter: PayloadOuter?,
//            addMessageToBotChatAdapter: (botResponse: BotResponse) -> Unit,
//            updateContentListOnSend: (botRequest: BotRequest) -> Unit,
//            setQuickReplies: (botResponse: BotResponse?) -> Unit = {}
//        ) {
//            try {
//                if (payloadOuter?.payload != null) {
//                    val componentModel = ComponentModel(payloadOuter.type!!, payloadOuter)
//                    val botResponseMessage = BotResponseMessage()
//                    botResponseMessage.type = componentModel.type
//                    botResponseMessage.component = componentModel
//                    val arrBotResponseMessages = java.util.ArrayList<BotResponseMessage>()
//                    arrBotResponseMessages.add(botResponseMessage)
//                    val botResponse = BotResponse()
//                    botResponse.type = componentModel.type
//                    botResponse.message = arrBotResponseMessages
//                    processPayload(context, "", botResponse, addMessageToBotChatAdapter, updateContentListOnSend, setQuickReplies)
//                } else if (payloadOuter != null && !payloadOuter.text.isEmpty()) {
//                    displayMessage(payloadOuter.text, BotResponseConstants.COMPONENT_TYPE_TEXT, "") { payload, botLocalResponse ->
//                        processPayload(
//                            context,
//                            payload,
//                            botLocalResponse,
//                            addMessageToBotChatAdapter,
//                            updateContentListOnSend,
//                            setQuickReplies
//                        )
//                    }
//                }
//            } catch (e: java.lang.Exception) {
//                e.printStackTrace()
//            }
//        }
    }
}