package com.kore.widgets.event

sealed class WidgetActionEvent : BaseActionEvent {
    class SendMessage(val message: String, val payload: String? = null) : WidgetActionEvent()
    class SendMessageFromPanel(val message: String, isRefresh: Boolean = false, val payload: String? = null) : WidgetActionEvent()
    class UrlClick(val url: String, val header: String) : WidgetActionEvent()
    class OnPanelState(val isOpen: Boolean) : WidgetActionEvent()
}
