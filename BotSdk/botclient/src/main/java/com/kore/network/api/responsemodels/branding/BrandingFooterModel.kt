package com.kore.network.api.responsemodels.branding

import com.google.gson.annotations.SerializedName

data class BrandingFooterModel(
    @SerializedName("bg_color")
    val bgColor: String? = null,
    @SerializedName("layout")
    val layout: String? = null,
    @SerializedName("style")
    val style: String? = null,
    @SerializedName("compose_bar")
    val composeBar: BrandingFooterComposeBarModel? = null,
    @SerializedName("icons_color")
    var iconsColor: String? = null,
    @SerializedName("buttons")
    val buttons: BrandingFooterButtonsModel? = null
)

fun BrandingFooterModel.updateWith(configModel: BrandingFooterModel): BrandingFooterModel {
    return this.copy(
        bgColor = configModel.bgColor?.ifEmpty { this.bgColor } ?: bgColor,
        style = configModel.style?.ifEmpty { this.style } ?: style,
        iconsColor = configModel.iconsColor?.ifEmpty { this.iconsColor } ?: iconsColor,
        layout = configModel.layout?.ifEmpty { this.layout } ?: layout,
        buttons = this.buttons?.updateWith(configModel.buttons) ?: this.buttons,
    )
}