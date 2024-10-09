package com.kore.network.api.responsemodels.branding

import com.google.gson.annotations.SerializedName

data class BrandingChatBubbleModel (
    @SerializedName("style")
    val style : String = "",
    @SerializedName("icon")
    val icon : BrandingIconModel,
    @SerializedName("minimise")
    val minimise : BrandingMinimizeModel,
    @SerializedName("sound")
    val sound : String = "",
    @SerializedName("alignment")
    val alignment : String = "",
    @SerializedName("animation")
    val animation : String = "",
    @SerializedName("expand_animation")
    val expandAnimation : String = "",
    @SerializedName("primary_color")
    val primaryColor : String = "",
    @SerializedName("secondary_color")
    val secondaryColor : String = "",
)