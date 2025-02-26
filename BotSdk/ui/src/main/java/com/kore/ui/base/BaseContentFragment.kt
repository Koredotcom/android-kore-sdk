package com.kore.ui.base

import androidx.fragment.app.Fragment
import com.kore.common.event.UserActionEvent
import com.kore.listeners.QuickRepliesClickListener
import com.kore.model.BaseBotMessage

abstract class BaseContentFragment : Fragment(), QuickRepliesClickListener {
    abstract fun setView(view: BaseView)

    abstract fun onFileDownloadProgress(msgId: String, progress: Int, downloadBytes: Int)

    abstract fun setActionEvent(actionEvent: (event: UserActionEvent) -> Unit)

    abstract fun getAdapterLastItem(): BaseBotMessage?

    abstract fun showTypingIndicator(icon: String?, enable: Boolean)

    abstract fun showQuickReplies(quickReplies: List<Map<String, *>>?, type: String?)

    abstract fun hideQuickReplies()

    abstract fun addMessagesToAdapter(messages: List<BaseBotMessage>, isHistory: Boolean, isReconnection: Boolean)

    abstract fun getAdapterCount(): Int

    abstract fun onBrandingDetails()
}