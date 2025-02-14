package com.kore.network.api.responsemodels.branding

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class BrandingTitleModel(
    @SerializedName("name")
    val name: String? = null,
    @SerializedName("color")
    var color: String? = null,
    @SerializedName("type")
    val type: String? = null,
    @SerializedName("img")
    val img: String? = null
) : Serializable

fun BrandingTitleModel.updateWith(configModel: BrandingTitleModel): BrandingTitleModel {
    return this.copy(
        name = configModel.name?.ifEmpty { this.name },
        color = configModel.color?.ifEmpty { this.color },
        type = configModel.type?.ifEmpty { this.type },
        img = configModel.img?.ifEmpty { this.img },
    )
}