package com.kore.model

import com.google.gson.annotations.SerializedName

data class BotWebHookRequest(
    @SerializedName("session")
    var session: Session? = null,

    @SerializedName("message")
    var message: WebHookMessage? = null,

    @SerializedName("from")
    var from: From? = null,

    @SerializedName("to")
    var to: To? = null,

    @SerializedName("attachments")
    var attachments: List<Map<String, *>>? = null
)