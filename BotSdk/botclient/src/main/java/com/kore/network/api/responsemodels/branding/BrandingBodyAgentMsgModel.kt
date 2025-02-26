package com.kore.network.api.responsemodels.branding

import com.google.gson.annotations.SerializedName

data class BrandingBodyAgentMsgModel(
    @SerializedName("bg_color")
    val bgColor: String = "",
    @SerializedName("color")
    val color: String = "",
    @SerializedName("separator")
    val separator: String = "",
    @SerializedName("icon")
    val icon: BrandingIconModel,
    @SerializedName("title")
    val title: BrandingTitleModel,
    @SerializedName("sub_title")
    val subTitle: BrandingTitleModel,
)

fun BrandingBodyAgentMsgModel.updateWith(configModel: BrandingBodyAgentMsgModel): BrandingBodyAgentMsgModel {
    return this.copy(
        bgColor = configModel.bgColor.ifEmpty { this.bgColor },
        separator = configModel.separator.ifEmpty { this.separator },
        color = configModel.color.ifEmpty { this.color },
        icon = icon.updateWith(configModel.icon),
        title = title.updateWith(configModel.title),
        subTitle = subTitle.updateWith(configModel.subTitle)
    )
}