package com.kore.model

import com.google.gson.annotations.SerializedName

data class WebHookModel(
    @SerializedName("type")
    val type: String,
    @SerializedName("val")
    val value: Any?,
    @SerializedName("createdOn")
    val createdOn: String,
    @SerializedName("_v")
    val _v: String,
    @SerializedName("endOfTask")
    val endOfTask: Boolean = false,
    @SerializedName("messageId")
    val messageId: String
)