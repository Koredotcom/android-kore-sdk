package com.kore.ui.botchat

import com.kore.botclient.ConnectionState
import com.kore.common.event.UserActionEvent
import com.kore.model.BaseBotMessage
import com.kore.model.BotEventResponse
import com.kore.model.BotResponse
import com.kore.network.api.responsemodels.branding.BotActiveThemeModel
import com.kore.ui.base.BaseView

interface BotChatView : BaseView {
    fun init()
    fun addMessageToAdapter(baseBotMessage: BaseBotMessage)
    fun onActionEvent(event: UserActionEvent)
    fun onConnectionStateChanged(state: ConnectionState, isReconnection: Boolean)
    fun onBrandingDetails(header: BotActiveThemeModel?)
    fun onBotEventMessage(botResponse: BotEventResponse)
    fun showCalenderTemplate(payload: HashMap<String, Any>)
    fun showTypingIndicator(icon: String?)
    fun stopTypingIndicator()
    abstract fun showQuickReplies(quickReplies: List<Map<String, *>>?, type: String?, isStacked: Boolean)
    fun getAdapterLastItems(): BaseBotMessage?
    fun hideQuickReplies()

    //    fun onLoadingHistory()
    fun onLoadHistory(isReconnect: Boolean)

    //    fun onChatHistory(list: List<BaseBotMessage>, isReconnection: Boolean)
    fun onFileDownloadProgress(msgId: String, progress: Int, downloadedBytes: Int)
    fun showTemplateBottomSheet(botResponse: BotResponse)
}