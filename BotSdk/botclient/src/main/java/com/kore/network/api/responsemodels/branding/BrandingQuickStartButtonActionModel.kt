package com.kore.network.api.responsemodels.branding

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class BrandingQuickStartButtonActionModel(
    @SerializedName("type")
    val type: String?,
    @SerializedName("value")
    val value: String?,
    @SerializedName("icon")
    val icon: String?,
    @SerializedName("title")
    val title: String?,
) : Serializable

fun BrandingQuickStartButtonActionModel.updateWith(configModel: BrandingQuickStartButtonActionModel): BrandingQuickStartButtonActionModel {
    return this.copy(
        type = configModel.type?.ifEmpty { this.type },
        value = configModel.value?.ifEmpty { this.value },
        icon = configModel.icon?.ifEmpty { this.icon },
        title = configModel.title?.ifEmpty { this.title },
    )
}