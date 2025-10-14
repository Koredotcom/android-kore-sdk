package com.kore.network.api.responsemodels.branding

import com.google.gson.annotations.SerializedName

data class BrandingFooterComposeBarModel (
    @SerializedName("bg_color")
    var bgColor : String? = null,
    @SerializedName("outline-color")
    var outlineColor : String? = null,
    @SerializedName("inline-color")
    var inlineColor : String? = null,
    @SerializedName("placeholder")
    val placeholder : String? = null
)

fun BrandingFooterComposeBarModel.updateWith(configModel: BrandingFooterComposeBarModel?): BrandingFooterComposeBarModel {
    return this.copy(
        bgColor = configModel?.bgColor?.ifEmpty { this.bgColor } ?: bgColor,
        outlineColor = configModel?.outlineColor?.ifEmpty { this.outlineColor } ?: outlineColor,
        inlineColor = configModel?.inlineColor?.ifEmpty { this.inlineColor } ?: inlineColor,
        placeholder = configModel?.placeholder?.ifEmpty { this.placeholder } ?: placeholder,
    )
}