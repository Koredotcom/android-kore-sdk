package com.kore.network.api.responsemodels.branding

import com.google.gson.annotations.SerializedName

data class BrandingBodyUserMessageModel(
    @SerializedName("bg_color")
    val bgColor: String = "",
    @SerializedName("color")
    val color: String = ""
)