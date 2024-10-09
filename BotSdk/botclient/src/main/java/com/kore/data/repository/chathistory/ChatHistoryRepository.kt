package com.kore.data.repository.chathistory

import android.content.Context
import com.kore.common.Result
import com.kore.model.BaseBotMessage

interface ChatHistoryRepository {
    suspend fun getChatHistory(
        context: Context,
        accessToken: String,
        botId: String,
        botName: String,
        offset: Int,
        pageLimit: Int,
        isWebhook: Boolean
    ): Result<Pair<List<BaseBotMessage>, Boolean>>
}