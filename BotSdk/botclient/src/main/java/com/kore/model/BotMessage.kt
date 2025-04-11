package com.kore.model

data class BotMessage(
    var body: String,
    var renderMsg: String?,
    var attachments: List<Map<String, *>>?,
    val customData: HashMap<String, Any?>?,
    var params: HashMap<String, Any>? = null
) {
    constructor(body: String, renderMsg: String?) : this(body, renderMsg, null, null, null)
    constructor(body: String, renderMsg: String?, attachments: List<Map<String, *>>?) : this(body, renderMsg, attachments, null, null)

    fun checkAndUpdateRenderMessage() {
        if (!renderMsg.isNullOrEmpty()) {
            body = renderMsg!!
        }
    }
}