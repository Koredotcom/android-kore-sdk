package com.kore.network.api.responsemodels.branding

import com.google.gson.annotations.SerializedName

data class BrandingBodyFontModel(
    @SerializedName("family")
    val family: String = "",
    @SerializedName("size")
    val size: String = "",
    @SerializedName("style")
    val style: String = ""
)

fun BrandingBodyFontModel.updateWith(configModel: BrandingBodyFontModel): BrandingBodyFontModel {
    return this.copy(
        family = configModel.family.ifEmpty { this.family },
        size = configModel.size.ifEmpty { this.size },
        style = configModel.style.ifEmpty { this.style }
    )
}