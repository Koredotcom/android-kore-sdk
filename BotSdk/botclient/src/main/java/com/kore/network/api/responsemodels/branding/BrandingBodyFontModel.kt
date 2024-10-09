package com.kore.network.api.responsemodels.branding

import com.google.gson.annotations.SerializedName

data class BrandingBodyFontModel(
    @SerializedName("family")
    val family : String = "",
    @SerializedName("size")
    val size : String = "",
    @SerializedName("style")
    val style : String = ""
)