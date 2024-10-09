package com.kore.model

import com.google.gson.annotations.SerializedName

data class BotMetaResponse(
    @SerializedName("icon")
    var icon: String? = null
)