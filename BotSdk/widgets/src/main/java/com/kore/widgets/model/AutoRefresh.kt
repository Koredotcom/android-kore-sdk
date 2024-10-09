package com.kore.widgets.model

import com.google.gson.annotations.SerializedName

data class AutoRefresh(
    @SerializedName("enabled")
    val enabled: Boolean = false,

    @SerializedName("interval")
    val interval: Int = 0
)