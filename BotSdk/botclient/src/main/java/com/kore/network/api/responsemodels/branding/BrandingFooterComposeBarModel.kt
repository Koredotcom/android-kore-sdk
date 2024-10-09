package com.kore.network.api.responsemodels.branding

import com.google.gson.annotations.SerializedName

data class BrandingFooterComposeBarModel (
    @SerializedName("bg_color")
    var bgColor : String = "",
    @SerializedName("outline-color")
    var outlineColor : String = "",
    @SerializedName("inline-color")
    var inlineColor : String = "",
    @SerializedName("placeholder")
    val placeholder : String = ""
)