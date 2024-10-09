package com.kore.model

import com.google.gson.annotations.SerializedName

data class WebHookMessageResponse(
    @SerializedName("data")
    val data: List<WebHookModel>? = null,
    @SerializedName("_v")
    val _v: String? = null,
    @SerializedName("pollId")
    val pollId: String? = null
)