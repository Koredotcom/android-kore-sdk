package com.kore.network.api.responsemodels.branding

import com.google.gson.annotations.SerializedName

data class BrandingStaticLinksModel(
    @SerializedName("show")
    val show: Boolean?,
    @SerializedName("layout")
    val layout: String = ""
)

fun BrandingStaticLinksModel.updateWith(configModel: BrandingStaticLinksModel): BrandingStaticLinksModel {
    return this.copy(
        show = configModel.show ?: show,
        layout = layout.ifEmpty { layout }
    )
}