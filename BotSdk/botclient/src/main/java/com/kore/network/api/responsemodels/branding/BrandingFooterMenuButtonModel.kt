package com.kore.network.api.responsemodels.branding

import com.google.gson.annotations.SerializedName

data class BrandingFooterMenuButtonModel(
    @SerializedName("show")
    val show: Boolean?,
    @SerializedName("icon")
    val icon: String? = null,
    @SerializedName("actions")
    val actions: ArrayList<BrandingQuickStartButtonActionModel> = ArrayList(),
)

fun BrandingFooterMenuButtonModel.updateWith(configModel: BrandingFooterMenuButtonModel?): BrandingFooterMenuButtonModel {
    return this.copy(
        show = configModel?.show ?: show,
        icon = configModel?.icon?.ifEmpty { icon } ?: icon,
    )
}