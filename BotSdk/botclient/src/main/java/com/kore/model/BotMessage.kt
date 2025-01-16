package com.kore.model

data class BotMessage(
    var body: String,
    var attachments: List<Map<String, *>>?,
    val customData: HashMap<String, Any?>?,
    var params: HashMap<String, Any>? = null
) {
    constructor(body: String) : this(body, null, null, null)
    constructor(body: String, attachments: List<Map<String, *>>?) : this(body, attachments, null, null)
}