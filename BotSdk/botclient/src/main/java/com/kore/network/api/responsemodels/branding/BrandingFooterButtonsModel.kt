package com.kore.network.api.responsemodels.branding

import com.google.gson.annotations.SerializedName

data class BrandingFooterButtonsModel (
    @SerializedName("menu")
    val menu : BrandingFooterMenuButtonModel,
    @SerializedName("emoji")
    val emoji : BrandingIconModel,
    @SerializedName("microphone")
    val microphone : BrandingIconModel,
    @SerializedName("attachment")
    val attachment : BrandingIconModel,
    @SerializedName("speaker")
    val speaker : BrandingIconModel
)