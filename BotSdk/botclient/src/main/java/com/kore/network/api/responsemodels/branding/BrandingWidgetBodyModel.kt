package com.kore.network.api.responsemodels.branding

import com.google.gson.annotations.SerializedName

data class BrandingWidgetBodyModel(
    @SerializedName("backgroundImage")
    val backgroundImage: String = "",
    @SerializedName("backgroundColor")
    val backgroundColor: String = "",
    @SerializedName("useBackgroundImage")
    val useBackgroundImage: String = "",
    @SerializedName("fontColor")
    val fontColor: String = "",
    @SerializedName("borderColor")
    val borderColor: String = "",
    @SerializedName("placeHolder")
    val placeHolder: String = "",
)
