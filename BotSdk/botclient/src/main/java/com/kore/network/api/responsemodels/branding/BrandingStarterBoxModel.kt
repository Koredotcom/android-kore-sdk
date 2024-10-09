package com.kore.network.api.responsemodels.branding

import com.google.gson.annotations.SerializedName

data class BrandingStarterBoxModel (
    @SerializedName("show")
    val show : Boolean,
    @SerializedName("icon")
    val icon : BrandingIconModel,
    @SerializedName("title")
    val title : String = "",
    @SerializedName("sub_text")
    val subText : String = "",
    @SerializedName("start_conv_button")
    val startConvButton : BrandingTitleModel,
    @SerializedName("start_conv_text")
    val startConvText : BrandingTitleModel,
    @SerializedName("quick_start_buttons")
    val quickStartButtons : BrandingQuickStartButtons,
    @SerializedName("links")
    val links : BrandingStaticLinksModel

)