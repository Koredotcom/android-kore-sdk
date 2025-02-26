package com.kore.network.api.responsemodels.branding

import com.google.gson.annotations.SerializedName

data class BrandingBodyBackgroundModel(
    @SerializedName("type")
    val type: String? = null,
    @SerializedName("color")
    val color: String? = null,
    @SerializedName("img")
    val img: String? = null
)

fun BrandingBodyBackgroundModel.updateWith(configModel: BrandingBodyBackgroundModel): BrandingBodyBackgroundModel {
    return this.copy(
        type = configModel.type?.ifEmpty { this.type },
        color = configModel.color?.ifEmpty { this.color },
        img = configModel.img?.ifEmpty { this.img }
    )
}