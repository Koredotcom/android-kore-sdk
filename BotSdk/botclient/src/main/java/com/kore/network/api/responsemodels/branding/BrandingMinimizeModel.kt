package com.kore.network.api.responsemodels.branding

import com.google.gson.annotations.SerializedName

data class BrandingMinimizeModel(
    @SerializedName("icon")
    val icon: String?,
    @SerializedName("theme")
    val theme: String?
)

fun BrandingMinimizeModel.updateWith(configModel: BrandingMinimizeModel?): BrandingMinimizeModel {
    return this.copy(
        icon = configModel?.icon?.ifEmpty { icon } ?: icon,
        theme = configModel?.theme?.ifEmpty { theme } ?: theme
    )
}