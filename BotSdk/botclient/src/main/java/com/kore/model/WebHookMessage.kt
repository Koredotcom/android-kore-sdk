package com.kore.model

import com.google.gson.annotations.SerializedName

data class WebHookMessage(
    @SerializedName("type")
    private val type: String,

    @SerializedName("val")
    private val value: String
)