package com.kore.network.api.responsemodels.branding

import com.google.gson.annotations.SerializedName

data class BrandingOverrideHistoryModel(
    @SerializedName("isEnable")
    val isEnable: Boolean? = false,
    @SerializedName("recent")
    val recent: BrandingOverrideHistoryRecentModel? = null,
    @SerializedName("paginated_scroll")
    val paginatedScroll: BrandingOverrideHistoryRecentModel? = null
)

fun BrandingOverrideHistoryModel.updateWith(configModel: BrandingOverrideHistoryModel?): BrandingOverrideHistoryModel {
    return this.copy(
        isEnable = configModel?.isEnable ?: isEnable,
        recent = recent?.updateWith(configModel?.recent) ?: recent,
        paginatedScroll = paginatedScroll?.updateWith(configModel?.paginatedScroll) ?: paginatedScroll
    )
}

