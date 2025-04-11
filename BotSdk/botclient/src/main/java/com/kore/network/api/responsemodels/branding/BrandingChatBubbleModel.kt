package com.kore.network.api.responsemodels.branding

import com.google.gson.annotations.SerializedName

data class BrandingChatBubbleModel(
    @SerializedName("style")
    val style: String = "",
    @SerializedName("icon")
    val icon: BrandingIconModel?,
    @SerializedName("minimise")
    val minimise: BrandingMinimizeModel?,
    @SerializedName("sound")
    val sound: String = "",
    @SerializedName("alignment")
    val alignment: String = "",
    @SerializedName("animation")
    val animation: String = "",
    @SerializedName("expand_animation")
    val expandAnimation: String = "",
    @SerializedName("primary_color")
    val primaryColor: String = "",
    @SerializedName("secondary_color")
    val secondaryColor: String = "",
)

fun BrandingChatBubbleModel.updateWith(configModel: BrandingChatBubbleModel?): BrandingChatBubbleModel {
    return this.copy(
        style = configModel?.style?.ifEmpty { style } ?: style,
        icon = icon?.updateWith(configModel?.icon) ?: icon,
        minimise = minimise?.updateWith(configModel?.minimise) ?: minimise,
        sound = configModel?.sound?.ifEmpty { sound } ?: sound,
        alignment = configModel?.alignment?.ifEmpty { alignment } ?: alignment,
        animation = configModel?.animation?.ifEmpty { animation } ?: animation,
        expandAnimation = configModel?.expandAnimation?.ifEmpty { expandAnimation } ?: expandAnimation,
        primaryColor = configModel?.primaryColor?.ifEmpty { primaryColor } ?: primaryColor,
        secondaryColor = configModel?.secondaryColor?.ifEmpty { secondaryColor } ?: secondaryColor
    )
}