package com.kore.ui.botchat

import com.kore.botclient.ConnectionState
import com.kore.common.base.BaseView
import com.kore.common.event.UserActionEvent
import com.kore.model.BaseBotMessage
import com.kore.model.BotEventResponse
import com.kore.network.api.responsemodels.branding.BotBrandingModel

interface BotChatView : BaseView {
    fun init()
    fun addMessageToAdapter(baseBotMessage: BaseBotMessage)
    fun onActionEvent(event: UserActionEvent)
    fun onConnectionStateChanged(state: ConnectionState, isReconnection: Boolean)
    fun onBrandingDetails(header: BotBrandingModel?)
    fun onBotEventMessage(botResponse: BotEventResponse)
    fun showCalenderTemplate(payload: HashMap<String, Any>)
    fun showTypingIndicator(icon: String?)
    fun showQuickReplies(quickReplies: List<Map<String, *>>?, type: String?)
    fun getAdapterLastItems(): BaseBotMessage?
    fun hideQuickReplies()
    fun onChatHistory(list: List<BaseBotMessage>)
    fun onSwipeRefresh()
    fun onFileDownloadProgress(progress: Int, msgId: String)
}