package com.kore.network.api.responsemodels.branding

import com.google.gson.annotations.SerializedName

data class BrandingButtonsModel(
    @SerializedName("defaultButtonColor")
    val defaultButtonColor: String = "",
    @SerializedName("defaultFontColor")
    val defaultFontColor: String = "",
    @SerializedName("onHoverButtonColor")
    val onHoverButtonColor: String = "",
    @SerializedName("onHoverFontColor")
    val onHoverFontColor: String = "",
    @SerializedName("borderColor")
    val borderColor: String = ""
)
