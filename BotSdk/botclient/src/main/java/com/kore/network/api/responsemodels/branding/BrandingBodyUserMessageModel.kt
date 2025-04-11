package com.kore.network.api.responsemodels.branding

import com.google.gson.annotations.SerializedName

data class BrandingBodyUserMessageModel(
    @SerializedName("bg_color")
    val bgColor: String? = null,
    @SerializedName("color")
    val color: String? = null
)

fun BrandingBodyUserMessageModel.updateWith(configModel: BrandingBodyUserMessageModel?): BrandingBodyUserMessageModel {
    return this.copy(
        bgColor = configModel?.bgColor?.ifEmpty { this.bgColor } ?: bgColor,
        color = configModel?.color?.ifEmpty { this.color } ?: color,
    )
}