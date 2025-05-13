package com.kore.ui.botchat.fragment

import com.kore.model.BaseBotMessage
import com.kore.ui.base.BaseView

interface BotContentView : BaseView {
    fun onChatHistory(list: List<BaseBotMessage>, isReconnection: Boolean)
    fun onLoadingHistory()
}