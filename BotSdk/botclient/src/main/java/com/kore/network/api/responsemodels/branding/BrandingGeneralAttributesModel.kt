package com.kore.network.api.responsemodels.branding

import com.google.gson.annotations.SerializedName

data class BrandingGeneralAttributesModel(
    @SerializedName("fontStyle")
    val fontStyle: String = "",
    @SerializedName("bubbleShape")
    val bubbleShape: String = "",
    @SerializedName("borderColor")
    val borderColor: String = ""
)
