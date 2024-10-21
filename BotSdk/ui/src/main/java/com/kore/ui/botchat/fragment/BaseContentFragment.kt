package com.kore.ui.botchat.fragment

import androidx.fragment.app.Fragment
import com.kore.common.base.BaseView
import com.kore.common.event.UserActionEvent
import com.kore.listeners.QuickRepliesClickListener
import com.kore.model.BaseBotMessage

abstract class BaseContentFragment : Fragment(), QuickRepliesClickListener {
    abstract fun setView(view: BaseView)

    abstract fun onFileDownloadProgress(progress: Int, msgId: String)

    abstract fun setActionEvent(actionEvent: (event: UserActionEvent) -> Unit)

    abstract fun getAdapterLastItem(): BaseBotMessage?

    abstract fun showTypingIndicator(icon: String?, enable: Boolean)

    abstract fun showQuickReplies(quickReplies: List<Map<String, *>>?, type: String?)

    abstract fun hideQuickReplies()

    abstract fun addMessagesToAdapter(messages: List<BaseBotMessage>, isHistory: Boolean)

    abstract fun getAdapterCount(): Int

    abstract fun onBrandingDetails()
}