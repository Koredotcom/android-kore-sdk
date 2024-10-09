package com.kore.model

import com.google.gson.annotations.SerializedName

data class BotPayLoad(
    @SerializedName("message")
    val message: BotMessage? = null,
    @SerializedName("resourceid")
    val resourceId: String = "/bot.message",
    @SerializedName("botInfo")
    val botInfo: BotInfoModel? = null,
    @SerializedName("clientMessageId")
    val clientMessageId: Int = System.currentTimeMillis().toInt(),
    @SerializedName("meta")
    val meta: Meta? = null,
    @SerializedName("id")
    val id: Int = clientMessageId,
    @SerializedName("client")
    val client: String = "Android",
    @SerializedName("event")
    val event: String? = null
)