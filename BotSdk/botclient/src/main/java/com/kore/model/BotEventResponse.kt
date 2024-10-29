package com.kore.model

data class BotEventResponse(
    var type: String? = null,
    val timestamp: String? = null,
    var message: HashMap<String, Any>? = null,
    override val from: String? = null,
    override val isSend: Boolean = false,
    override val createdOn: String = "",
    override val formattedTime: String = "",
    override val timeMillis: Long = 0,
    override val messageId: String = "",
    override val messageDate: String = ""
) : BaseBotMessage {
    var icon: String? = null
    val key: String? = null
}