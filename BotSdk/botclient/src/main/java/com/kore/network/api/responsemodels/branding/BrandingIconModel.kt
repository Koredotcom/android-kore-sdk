package com.kore.network.api.responsemodels.branding

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class BrandingIconModel(
    @SerializedName("icon_url")
    val iconUrl: String = "",
    @SerializedName("size")
    val size: String = "",
    @SerializedName("shape")
    val shape: String = "",
    @SerializedName("type")
    val type: String = "",
    @SerializedName("show")
    val show: Boolean? = null,
    @SerializedName("color")
    val color: String = "",
    @SerializedName("icon")
    val icon: String = "",
    @SerializedName("action")
    val action: BrandingQuickStartButtonActionModel? = null
) : Serializable

fun BrandingIconModel.updateWith(configModel: BrandingIconModel): BrandingIconModel {
    return this.copy(
        iconUrl = configModel.iconUrl.ifEmpty { this.iconUrl },
        size = configModel.size.ifEmpty { this.size },
        shape = configModel.shape.ifEmpty { this.shape },
        type = configModel.type.ifEmpty { this.type },
        show = configModel.show ?: this.show,
        color = configModel.color.ifEmpty { this.color },
        icon = configModel.icon.ifEmpty { this.icon },
        action = this.action?.updateWith(configModel.action ?: this.action),
    )
}