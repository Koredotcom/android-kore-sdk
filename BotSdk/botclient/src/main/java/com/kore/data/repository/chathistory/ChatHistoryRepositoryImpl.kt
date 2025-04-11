package com.kore.data.repository.chathistory

import android.content.Context
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import com.kore.botclient.helper.BotClientHelper.Companion.getFormattedTime
import com.kore.common.Result
import com.kore.common.utils.DateUtils.formattedSentDateV6
import com.kore.common.utils.DateUtils.isoFormatter
import com.kore.model.BaseBotMessage
import com.kore.model.BotHistory
import com.kore.model.BotInfoModel
import com.kore.model.BotMessage
import com.kore.model.BotPayLoad
import com.kore.model.BotRequest
import com.kore.model.BotResponse
import com.kore.model.BotResponseMessage
import com.kore.model.ComponentInfo
import com.kore.model.ComponentModel
import com.kore.model.PayloadOuter
import com.kore.model.constants.BotResponseConstants
import com.kore.model.constants.BotResponseConstants.DATA
import com.kore.model.constants.BotResponseConstants.KEY_TEXT
import com.kore.network.api.ApiClient
import com.kore.network.api.service.ChatHistoryApi
import retrofit2.Response
import java.util.TimeZone

class ChatHistoryRepositoryImpl : ChatHistoryRepository {
    override suspend fun getChatHistory(
        context: Context,
        accessToken: String,
        botId: String,
        botName: String,
        offset: Int,
        pageLimit: Int,
        isWebhook: Boolean
    ): Result<Pair<List<BaseBotMessage>, Boolean>> {
        return try {
            val historyApi = ApiClient.getInstance().createService(ChatHistoryApi::class.java)
            val response =
                if (isWebhook) {
                    historyApi.getWebHookBotHistory("bearer $accessToken", botId, botId, offset, pageLimit)
                } else {
                    historyApi.getBotHistory("bearer $accessToken", botId, pageLimit, offset, true)
                }
            processChatHistory(response, botId, botName)
        } catch (e: Exception) {
            e.printStackTrace()
            Result.Error(Exception(e.message!!))
        }
    }

    private fun processChatHistory(
        response: Response<BotHistory?>,
        botId: String,
        botName: String
    ): Result<Pair<List<BaseBotMessage>, Boolean>> {
        var msgs: List<BaseBotMessage> = emptyList()
        var moreAvailable = false
        if (response.isSuccessful && response.body() != null) {
            val icon = response.body()?.icon
            moreAvailable = response.body()?.moreAvailable == true
            val messages = response.body()?.messages
            if (!messages.isNullOrEmpty()) {
                for (index in messages.indices) {
                    val msg = messages[index]
                    val components = msg.components
                    val altText: List<Map<String, Any>> = msg.tags[BotResponseConstants.ALT_TEXT] as List<Map<String, Any>>
                    val renderMsg: String? = altText.firstOrNull()?.get(BotResponseConstants.VALUE) as String?
                    if (msg.type == BotResponseConstants.MESSAGE_TYPE_OUTGOING) {
                        val data = ((components[0][DATA] as Map<String, String>)[KEY_TEXT] ?: "").replace("&quot;", "\"")
                        if (data.isEmpty()) continue
                        msgs = try {
                            val outer: PayloadOuter = Gson().fromJson(data, PayloadOuter::class.java)
                            val botResponse = createBotResponse(outer, icon, botId, botName, msg.createdOn!!, msg.id ?: "")
                            msgs + botResponse
                        } catch (ex: JsonSyntaxException) {
                            val botResponse = createBotResponse(data, icon, botId, botName, msg.createdOn!!, msg.id ?: "")
                            msgs + botResponse
                        }
                    } else {
                        try {
                            val message = ((components[0][DATA] as Map<String, String>)[KEY_TEXT] ?: "").replace("&quot;", "\"")
                            msgs = msgs + createBotRequest(msg.id ?: "", message, renderMsg, botId, botName, msg.createdOn!!)
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                }
            }
        }
        return Result.Success(Pair(msgs, moreAvailable))
    }

    private fun createBotRequest(msgId: String, message: String, renderMsg: String?, botId: String, botName: String, createdOn: String): BotRequest {
        val botMessage = BotMessage(if (!renderMsg.isNullOrEmpty()) renderMsg else message, renderMsg)
        val botInfo = BotInfoModel(botName, botId, null)
        val botPayLoad = BotPayLoad(message = botMessage, botInfo = botInfo)
        val gson = Gson()
        val jsonPayload = gson.toJson(botPayLoad)
        val timeStamp = (isoFormatter.parse(createdOn)?.time ?: 0L) + TimeZone.getDefault().rawOffset
        val formattedTime = getFormattedTime(timeStamp, botName, false)
        return gson.fromJson(jsonPayload, BotRequest::class.java)
            .copy(
                messageId = msgId,
                formattedTime = formattedTime,
                timeMillis = timeStamp,
                messageDate = formattedSentDateV6(timeStamp),
                createdOn = createdOn
            )
    }

    private fun createBotResponse(
        outer: Any?,
        icon: String?,
        botId: String,
        botName: String,
        createdOn: String,
        msgId: String
    ): BotResponse {
        val payloadOuter = if (outer is PayloadOuter) outer else PayloadOuter(text = outer.toString())
        val cModel = ComponentModel(BotResponseConstants.COMPONENT_TYPE_TEMPLATE, payloadOuter)
        val botResponseMessage = BotResponseMessage(BotResponseConstants.COMPONENT_TYPE_TEXT, cModel, ComponentInfo(outer))
        val message: java.util.ArrayList<BotResponseMessage> = java.util.ArrayList<BotResponseMessage>(1)
        message.add(botResponseMessage)
        val timeStamp = (isoFormatter.parse(createdOn)?.time ?: 0L) + TimeZone.getDefault().rawOffset
        val formattedTime = getFormattedTime(timeStamp, botName, true)
        return BotResponse(
            BotResponseConstants.KEY_BOT_RESPONSE,
            msgId,
            ArrayList(message),
            timeStamp,
            "bot",
            false,
            messageDate = formattedSentDateV6(timeStamp),
            formattedTime = formattedTime,
            icon = icon,
            createdOn = createdOn,
        ).apply {
            val gson = Gson()
            val json = gson.toJson(BotInfoModel(botName, botId, null))
            botInfo = gson.fromJson(json, object : TypeToken<Map<String, Any>>() {}.type)
        }
    }
}