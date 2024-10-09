package com.kore.network.api.responsemodels.branding

import com.google.gson.annotations.SerializedName

data class BrandingFooterModel(
    @SerializedName("bg_color")
    val bgColor : String = "",
    @SerializedName("layout")
    val layout : String = "",
    @SerializedName("style")
    val style : String = "",
    @SerializedName("compose_bar")
    val composeBar : BrandingFooterComposeBarModel,
    @SerializedName("icons_color")
    var iconsColor : String = "",
    @SerializedName("buttons")
    val buttons : BrandingFooterButtonsModel
)