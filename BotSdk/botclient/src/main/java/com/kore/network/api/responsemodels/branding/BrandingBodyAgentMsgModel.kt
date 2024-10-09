package com.kore.network.api.responsemodels.branding

import com.google.gson.annotations.SerializedName

data class BrandingBodyAgentMsgModel(
    @SerializedName("bg_color")
    val bgColor : String = "",
    @SerializedName("color")
    val color : String = "",
    @SerializedName("separator")
    val separator : String = "",
    @SerializedName("icon")
    val icon : BrandingIconModel,
    @SerializedName("title")
    val title : BrandingTitleModel,
    @SerializedName("sub_title")
    val subTitle : BrandingTitleModel,
)