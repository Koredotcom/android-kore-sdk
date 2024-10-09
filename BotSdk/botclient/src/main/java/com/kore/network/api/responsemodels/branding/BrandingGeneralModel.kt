package com.kore.network.api.responsemodels.branding

import com.google.gson.annotations.SerializedName

data class BrandingGeneralModel (
    @SerializedName("bot_icon")
    val botIcon : String = "",
    @SerializedName("size")
    val size : String = "",
    @SerializedName("themeType")
    val themeType : String = "",
    @SerializedName("colors")
    val colors : BrandingColorsModel
)