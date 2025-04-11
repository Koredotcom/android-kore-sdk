package com.kore.network.api.responsemodels.branding

import com.google.gson.annotations.SerializedName

data class StaticLinksModel(
    @SerializedName("show")
    val show: Boolean?,
    @SerializedName("type")
    val type: String? = null,
    @SerializedName("layout")
    val layout: String = "",
    @SerializedName("links")
    val links: ArrayList<BrandingQuickStartButtonButtonsModel> = ArrayList()
)

fun StaticLinksModel.updateWith(configModel: StaticLinksModel?): StaticLinksModel {
    return this.copy(
        show = configModel?.show ?: show,
        type = configModel?.type?.ifEmpty { type } ?: type,
        layout = configModel?.layout?.ifEmpty { layout } ?: layout,
    )
}