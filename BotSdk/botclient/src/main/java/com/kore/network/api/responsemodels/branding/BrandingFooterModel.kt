package com.kore.network.api.responsemodels.branding

import com.google.gson.annotations.SerializedName

data class BrandingFooterModel(
    @SerializedName("bg_color")
    val bgColor : String? = null,
    @SerializedName("layout")
    val layout : String? = null,
    @SerializedName("style")
    val style : String? = null,
    @SerializedName("compose_bar")
    val composeBar : BrandingFooterComposeBarModel? = null,
    @SerializedName("icons_color")
    var iconsColor : String? = null,
    @SerializedName("buttons")
    val buttons : BrandingFooterButtonsModel? = null
)