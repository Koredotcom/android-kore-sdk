package com.kore.model

import com.google.gson.annotations.SerializedName

data class BotResponse(
    @SerializedName("type")
    var type: String? = null,
    @SerializedName("messageId")
    override var messageId: String,
    @SerializedName("message")
    var message: ArrayList<BotResponseMessage>,
    @SerializedName("timestamp")
    override val timeStamp: Long = 0,
    @SerializedName("from")
    override val from: String? = null,
    override val isSend: Boolean = false,
    override val createdOn: String = "",
    override val formattedTime: String = "",
    override val messageDate: String = "",
    var icon: String? = null,
    @SerializedName("fromAgent")
    val fromAgent: Boolean = false,
) : BaseBotMessage {
    val key: String? = null

    @SerializedName("botInfo")
    var botInfo: Map<String, Any>? = null
}