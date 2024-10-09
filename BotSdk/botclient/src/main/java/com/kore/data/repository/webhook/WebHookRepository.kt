package com.kore.data.repository.webhook

import com.kore.common.Result
import com.kore.model.BotMetaResponse
import com.kore.model.BotResponse

interface WebHookRepository {
    suspend fun getWebhookMeta(token: String, botId: String): Result<BotMetaResponse>
    suspend fun sendMessage(botId: String, token: String, message: HashMap<String, Any?>): Result<Pair<List<BotResponse>?, String>>
    suspend fun getPollData(token: String, botId: String, pollId: String): Result<Pair<List<BotResponse>?, String>>
}