package com.kore.network.api.responsemodels.branding

import com.google.gson.annotations.SerializedName

data class BrandingStarterBoxModel(
    @SerializedName("show")
    val show: Boolean?,
    @SerializedName("icon")
    val icon: BrandingIconModel,
    @SerializedName("title")
    val title: String = "",
    @SerializedName("sub_text")
    val subText: String = "",
    @SerializedName("start_conv_button")
    val startConvButton: BrandingTitleModel?,
    @SerializedName("start_conv_text")
    val startConvText: BrandingTitleModel?,
    @SerializedName("quick_start_buttons")
    val quickStartButtons: BrandingQuickStartButtons?,
    @SerializedName("links")
    val links: BrandingStaticLinksModel? = null
)

fun BrandingStarterBoxModel.updateWith(configModel: BrandingStarterBoxModel?): BrandingStarterBoxModel {
    return this.copy(
        show = configModel?.show ?: show,
        icon = icon.updateWith(configModel?.icon),
        title = configModel?.title?.ifEmpty { title } ?: title,
        subText = configModel?.subText?.ifEmpty { subText } ?: subText,
        startConvButton = startConvButton?.updateWith(configModel?.startConvButton) ?: startConvButton,
        startConvText = startConvText?.updateWith(configModel?.startConvText) ?: startConvText,
        quickStartButtons = quickStartButtons?.updateWith(configModel?.quickStartButtons) ?: quickStartButtons,
        links = links?.updateWith(configModel?.links) ?: links,
    )
}