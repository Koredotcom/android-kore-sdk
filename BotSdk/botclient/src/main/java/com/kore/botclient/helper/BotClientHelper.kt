package com.kore.botclient.helper

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import com.kore.common.model.BotConfigModel
import com.kore.common.utils.DateUtils
import com.kore.common.utils.DateUtils.formattedSentDateV6
import com.kore.common.utils.DateUtils.isoFormatter
import com.kore.model.BotInfoModel
import com.kore.model.BotMessage
import com.kore.model.BotPayLoad
import com.kore.model.BotRequest
import com.kore.model.BotResponse
import com.kore.model.BotResponseMessage
import com.kore.model.From
import com.kore.model.GroupInfo
import com.kore.model.Meta
import com.kore.model.PayloadOuter
import com.kore.model.Session
import com.kore.model.To
import com.kore.model.WebHookMessage
import com.kore.model.WebHookUserInfo
import com.kore.model.constants.BotResponseConstants.CHAT_BOT
import java.lang.reflect.Type
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

class BotClientHelper {
    companion object {
        fun createRequestPayload(
            message: String,
            payload: String?,
            attachment: List<Map<String, *>>?,
            botCustomData: HashMap<String, Any?>,
            botInfoModel: BotInfoModel,
            onCreatePayload: (payload: String, botRequest: BotRequest) -> Unit
        ) {
            var botPayLoad = BotPayLoad()
            var botMessage = BotMessage(payload ?: message, attachment)
            botMessage = botMessage.copy(customData = botCustomData)
            botPayLoad = botPayLoad.copy(message = botMessage, botInfo = botInfoModel)

            val meta = Meta(TimeZone.getDefault().id, Locale.getDefault().isO3Language)
            botPayLoad = botPayLoad.copy(meta = meta)

            val jsonPayload = Gson().toJson(botPayLoad)

            var botRequest: BotRequest = Gson().fromJson(jsonPayload, BotRequest::class.java)
            val calendar = Calendar.getInstance()
            botRequest = botRequest.copy(
                message = botMessage,
                messageId = calendar.timeInMillis.toString(),
                resourceId = botPayLoad.resourceId,
                timeMillis = calendar.timeInMillis,
                createdOn = isoFormatter.format(calendar.time),
                messageDate = formattedSentDateV6(calendar.timeInMillis),
                formattedTime = getFormattedTime(calendar.timeInMillis, botInfoModel.botName, false)
            )
            onCreatePayload(jsonPayload, botRequest)
        }

        fun createWebHookRequestPayload(
            newSession: Boolean,
            msg: String,
            payload: String?,
            attachments: List<Map<String, *>>?,
            botConfigModel: BotConfigModel,
            botCustomData: HashMap<String, Any?>,
            onCreatePayload: (HashMap<String, Any?>) -> Unit
        ) {
            val hashMap = HashMap<String, Any?>()
            try {
                val session = Session(newSession)
                hashMap["session"] = session
                val message = WebHookMessage(if (newSession) "event" else "text", payload ?: msg)
                hashMap["message"] = message
                val userInfo = WebHookUserInfo("", "", "")
                val from = From(id = botConfigModel.identity, userInfo)
                hashMap["from"] = from
                val groupInfo = GroupInfo("", "")
                val to = To("Kore.ai", groupInfo)
                hashMap["to"] = to
                hashMap["attachments"] = attachments
                hashMap["customData"] = botCustomData

            } catch (e: Exception) {
                e.printStackTrace()
            }

            onCreatePayload(hashMap)
        }

        fun processBotResponse(response: BotResponse): BotResponse {
            val botInfo = response.botInfo
            val timeStamp = isoFormatter.parse(response.createdOn)?.time ?: 0L
            val botResponse = if (response.timeMillis == 0L) {
                response.copy(
                    createdOn = response.createdOn,
                    timeMillis = timeStamp,
                    messageDate = formattedSentDateV6(timeStamp),
                    formattedTime = getFormattedTime(timeStamp, if (botInfo != null) botInfo[CHAT_BOT].toString() else "")
                )
            } else {
                response.copy(
                    createdOn = response.createdOn,
                    messageDate = formattedSentDateV6(response.timeMillis),
                    formattedTime = getFormattedTime(response.timeMillis, if (botInfo != null) botInfo[CHAT_BOT].toString() else "")
                )
            }
            botResponse.botInfo = response.botInfo
            if (botResponse.message.isEmpty()) return botResponse
            var msg = botResponse.message[0]
            var outerPayload: PayloadOuter? = null
            try {
                outerPayload = if (msg.cInfo != null && msg.cInfo?.body != null && msg.cInfo?.body is String) {
                    parseBotResponse(msg.cInfo?.body.toString(), object : TypeToken<PayloadOuter>() {}.type)
                } else {
                    parseBotResponseToBotResponse(msg, object : TypeToken<PayloadOuter>() {}.type)
                }
            } catch (e: JsonSyntaxException) {
                e.printStackTrace()
            }

            if (outerPayload == null) return botResponse
            msg = msg.copy(cInfo = msg.cInfo?.copy(body = outerPayload))
            botResponse.message[0] = msg
            return botResponse
        }

        private fun parseBotResponse(body: String?, type: Type): PayloadOuter? {
            if (body != null) {
                val gson = GsonBuilder().create()
                return gson.fromJson(body, type)
            }

            return null
        }

        private fun parseBotResponseToBotResponse(msg: BotResponseMessage, type: Type): PayloadOuter {
            val gson = GsonBuilder().create()
            return gson.fromJson(gson.toJson(msg.component?.payload), type)
        }

        fun getFormattedTime(milliSecs: Long, botName: String, isBotResponse: Boolean = true): String {
            return if (isBotResponse) {
                "<big><b>" + botName + "</b></big>" + " " + DateUtils.getTimeInAmPm(milliSecs) + ", " +
                        DateUtils.formattedSentDate(milliSecs)
            } else {
                (DateUtils.getTimeInAmPm(milliSecs) + ", " + DateUtils.formattedSentDate(milliSecs)) + "<big><b>" + " You" + "</b></big>"
            }
        }
    }
}