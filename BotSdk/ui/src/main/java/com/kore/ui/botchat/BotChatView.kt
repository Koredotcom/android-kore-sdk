package com.kore.ui.botchat

import com.kore.botclient.ConnectionState
import com.kore.ui.base.BaseView
import com.kore.common.event.UserActionEvent
import com.kore.model.BaseBotMessage
import com.kore.model.BotEventResponse
import com.kore.network.api.responsemodels.branding.BotActiveThemeModel
import com.kore.network.api.responsemodels.branding.BotBrandingModel

interface BotChatView : BaseView {
    fun init()
    fun addMessageToAdapter(baseBotMessage: BaseBotMessage)
    fun onActionEvent(event: UserActionEvent)
    fun onConnectionStateChanged(state: ConnectionState, isReconnection: Boolean)
    fun onBrandingDetails(header: BotActiveThemeModel?)
    fun onBotEventMessage(botResponse: BotEventResponse)
    fun showCalenderTemplate(payload: HashMap<String, Any>)
    fun showTypingIndicator(icon: String?)
    fun showQuickReplies(quickReplies: List<Map<String, *>>?, type: String?)
    fun getAdapterLastItems(): BaseBotMessage?
    fun hideQuickReplies()
    fun onChatHistory(list: List<BaseBotMessage>, isReconnection: Boolean)
    fun onSwipeRefresh()
    fun onFileDownloadProgress(msgId: String, progress: Int, downloadedBytes: Int)
    fun showOtpBottomSheet(payload: HashMap<String, Any>)
    fun showPinResetBottomSheet(payload: HashMap<String, Any>)
    fun showAdvancedMultiSelectBottomSheet(msgId: String, payload: HashMap<String, Any>)
}