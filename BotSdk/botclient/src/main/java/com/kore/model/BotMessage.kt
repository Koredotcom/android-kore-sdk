package com.kore.model

data class BotMessage(
    var body: String,
    private var attachments: List<Map<String, *>>?,
    private val customData: HashMap<String, String?>?,
    var params: HashMap<String, Any>? = null
) {
    constructor(body: String) : this(body, null, null, null)
    constructor(body: String, attachments: List<Map<String, *>>?) : this(body, attachments, null, null)
}