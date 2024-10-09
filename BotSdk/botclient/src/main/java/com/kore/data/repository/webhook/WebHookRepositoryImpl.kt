package com.kore.data.repository.webhook

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.kore.common.Result
import com.kore.common.SDKConfiguration
import com.kore.common.model.BotConfigModel
import com.kore.model.BotInfoModel
import com.kore.model.BotMetaResponse
import com.kore.model.BotResponse
import com.kore.model.BotResponseMessage
import com.kore.model.ComponentInfo
import com.kore.model.ComponentModel
import com.kore.model.PayloadOuter
import com.kore.model.WebHookMessageResponse
import com.kore.model.WebHookModel
import com.kore.model.constants.BotResponseConstants
import com.kore.model.constants.BotResponseConstants.COMPONENT_TYPE_TEXT
import com.kore.model.constants.BotResponseConstants.KEY_BOT_RESPONSE
import com.kore.model.constants.BotResponseConstants.KEY_TEXT
import com.kore.network.api.ApiClient
import com.kore.network.api.service.WebHookApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

class WebHookRepositoryImpl : WebHookRepository {
    private val botConfigModel: BotConfigModel? = SDKConfiguration.getBotConfigModel()

    override suspend fun getWebhookMeta(token: String, botId: String): Result<BotMetaResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val webHookApi = ApiClient.getInstance().createService(WebHookApi::class.java)
                val result = webHookApi.getWebHookBotMeta("bearer $token", botId)
                if (result.isSuccessful && result.body() != null) {
                    Result.Success(result.body() as BotMetaResponse)
                } else {
                    Result.Error(Exception(Gson().toJson(result.errorBody())))
                }
            } catch (ex: Exception) {
                Result.Error(ex)
            }
        }
    }

    override suspend fun sendMessage(
        botId: String,
        token: String,
        message: HashMap<String, Any?>
    ): Result<Pair<List<BotResponse>?, String>> {
        return withContext(Dispatchers.IO) {
            try {
                val webHookApi = ApiClient.getInstance().createService(WebHookApi::class.java)
                val result = webHookApi.sendWebHookMessage(botId, "bearer $token", message)
                processWebHookResponse(result)

            } catch (ex: Exception) {
                Result.Error(ex)
            }
        }
    }

    override suspend fun getPollData(token: String, botId: String, pollId: String): Result<Pair<List<BotResponse>?, String>> {
        return withContext(Dispatchers.IO) {
            try {
                val webHookApi = ApiClient.getInstance().createService(WebHookApi::class.java)
                val result = webHookApi.getPollIdData("bearer $token", botId, pollId)
                processWebHookResponse(result)

            } catch (ex: Exception) {
                Result.Error(ex)
            }
        }
    }

    private fun processWebHookResponse(result: Response<WebHookMessageResponse?>): Result<Pair<List<BotResponse>, String>> {
        return if (result.isSuccessful) {
            var exception: java.lang.Exception? = null
            var botResponses = emptyList<BotResponse>()

            val messages = result.body()?.data

            if (messages == null && result.body()?.pollId == null) Result.Error(Exception("Something went wrong!"))
            messages?.let {
                for (webHookModel in messages) {
                    if (webHookModel.value is String) {
                        botResponses = botResponses + getWebhookResponse(webHookModel.value.toString(), webHookModel.type, webHookModel)
                    } else if (webHookModel.value != null) {
                        val gson = Gson()
                        try {
                            val elementsAsString: String = gson.toJson(webHookModel.value)
                            val carouselType = object : TypeToken<PayloadOuter>() {}.type
                            val payloadOuter: PayloadOuter = gson.fromJson(elementsAsString, carouselType)
                            getWebhookResponse(payloadOuter, webHookModel)?.let { botResponses = botResponses + it }
                        } catch (e: java.lang.Exception) {
                            try {
                                val elementsAsString: String = gson.toJson(webHookModel.value)
                                val carouselType = object : TypeToken<PayloadOuter>() {}.type
                                val payloadOuter: PayloadOuter = gson.fromJson(elementsAsString, carouselType)
                                if (payloadOuter.payload != null) {
                                    botResponses = botResponses +
                                            getWebhookResponse(
                                                payloadOuter.payload?.get(BotResponseConstants.KEY_TEMPLATE_TYPE) as String,
                                                COMPONENT_TYPE_TEXT,
                                                webHookModel
                                            )
                                } else {
                                    if (result.body()?.pollId == null) exception = Exception("Something went wrong!")
                                }
                            } catch (ex: Exception) {
                                val elementsAsString: String = gson.toJson(webHookModel.value)
                                botResponses = botResponses + getWebhookResponse(elementsAsString, COMPONENT_TYPE_TEXT, webHookModel)
                            }
                        }
                    } else {
                        if (result.body()?.pollId == null) exception = Exception("No data found!")
                    }
                }
            }
            if (exception == null) {
                Result.Success(Pair(botResponses, result.body()?.pollId ?: ""))
            } else {
                Result.Error(exception!!)
            }
        } else {
            Result.Error(Exception(Gson().toJson(result.errorBody())))
        }
    }

    private fun getWebhookResponse(text: String, type: String, webHookModel: WebHookModel): BotResponse {
        val gson = Gson()
        try {
            if (botConfigModel == null) return getErrorBotResponse(text, webHookModel)
            val newText = text.replace("&quot;", "\"")
            val payloadOuter: PayloadOuter = gson.fromJson(newText, PayloadOuter::class.java)
            if (payloadOuter.type.isNullOrEmpty()) payloadOuter.type = type
            val componentModel = ComponentModel()
            componentModel.type = payloadOuter.type
            componentModel.payload = payloadOuter
            val componentInfo = ComponentInfo(if (payloadOuter.payload != null) Gson().toJson(payloadOuter.payload) else payloadOuter.text)
            val botResponseMessage = BotResponseMessage(componentModel.type, componentModel, componentInfo)
            val arrBotResponseMessages: ArrayList<BotResponseMessage> = ArrayList()
            arrBotResponseMessages.add(botResponseMessage)
            val info = BotInfoModel(botConfigModel.botName, botConfigModel.botId, null)
            return BotResponse(
                KEY_BOT_RESPONSE,
                message = arrBotResponseMessages,
                messageId = webHookModel.messageId,
                createdOn = webHookModel.createdOn
            ).apply {
                botInfo = Gson().fromJson(Gson().toJson(info), object : TypeToken<HashMap<String, Any>>() {}.type)
            }
        } catch (e: Exception) {
            return getErrorBotResponse(text, webHookModel)
        }
    }

    private fun getErrorBotResponse(text: String, webHookModel: WebHookModel): BotResponse {
        val payloadInner = HashMap<String, Any>()
        payloadInner[BotResponseConstants.KEY_TEMPLATE_TYPE] = KEY_TEXT
        payloadInner[KEY_TEXT] = text
        val payloadOuter = PayloadOuter()
        payloadOuter.text = text
        payloadOuter.type = KEY_TEXT
        payloadOuter.payload = payloadInner
        val componentModel = ComponentModel()
        componentModel.type = KEY_TEXT
        componentModel.payload = payloadOuter
        val componentInfo = ComponentInfo(Gson().toJson(payloadOuter.payload))
        val botResponseMessage = BotResponseMessage(KEY_TEXT, componentModel, componentInfo)
        val arrBotResponseMessages: ArrayList<BotResponseMessage> = ArrayList()
        arrBotResponseMessages.add(botResponseMessage)
        val info = BotInfoModel(botConfigModel?.botName ?: "", botConfigModel?.botId ?: "", null)
        return BotResponse(
            KEY_BOT_RESPONSE,
            message = arrBotResponseMessages,
            messageId = webHookModel.messageId,
            createdOn = webHookModel.createdOn
        ).apply {
            botInfo = Gson().fromJson(Gson().toJson(info), object : TypeToken<HashMap<String, Any>>() {}.type)
        }
    }

    private fun getWebhookResponse(payloadOuter: PayloadOuter, webHookModel: WebHookModel): BotResponse? {
        return try {
            if (botConfigModel == null) return null
            if (payloadOuter.payload != null) {
                val componentModel = ComponentModel()
                componentModel.type = payloadOuter.type
                componentModel.payload = payloadOuter
                val componentInfo = ComponentInfo(payloadOuter)
                val botResponseMessage = BotResponseMessage(componentModel.type, componentModel, componentInfo)
                val arrBotResponseMessages: ArrayList<BotResponseMessage> = ArrayList()
                arrBotResponseMessages.add(botResponseMessage)
                val info = BotInfoModel(botConfigModel.botName, botConfigModel.botId, null)
                BotResponse(
                    KEY_BOT_RESPONSE,
                    message = arrBotResponseMessages,
                    messageId = webHookModel.messageId,
                    createdOn = webHookModel.createdOn
                ).apply {
                    botInfo = Gson().fromJson(Gson().toJson(info), object : TypeToken<HashMap<String, Any>>() {}.type)
                }
            } else if (!payloadOuter.text.isNullOrEmpty()) {
                getWebhookResponse(payloadOuter.text!!, COMPONENT_TYPE_TEXT, webHookModel)
            } else {
                null
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            null
        }
    }
}