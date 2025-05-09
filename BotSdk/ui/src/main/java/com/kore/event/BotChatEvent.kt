package com.kore.event

import com.kore.common.event.UserActionEvent

sealed class BotChatEvent : UserActionEvent {
    class SendMessage(val message: String, val payload: String? = null) : BotChatEvent()
    class SendAttachments(val message: String, val attachments: List<Map<String, *>>?) : BotChatEvent()
    class UrlClick(val url: String, val header: String = "") : BotChatEvent()
    class PhoneNumberClick(val phoneNumber: String) : BotChatEvent()
    class OnDropDownItemClicked(val selectedItem: String) : BotChatEvent()
    data object ShowAttachmentOptions : BotChatEvent()
    data object OnBackPressed : BotChatEvent()
    class DownloadLink(val msgId: String, val url: String, val fileName: String?) : BotChatEvent()
}
