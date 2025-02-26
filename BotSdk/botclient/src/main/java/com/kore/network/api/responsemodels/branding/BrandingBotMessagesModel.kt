package com.kore.network.api.responsemodels.branding

import com.google.gson.annotations.SerializedName

data class BrandingBotMessagesModel(
    @SerializedName("bubbleColor")
    val bubbleColor: String = "",
    @SerializedName("fontColor")
    val fontColor: String = "",
    @SerializedName("borderColor")
    val borderColor: String = ""
)
