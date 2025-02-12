package com.kore.network.api.responsemodels.branding

import com.google.gson.annotations.SerializedName

data class BrandingOverrideHistoryModel(
    @SerializedName("isEnable")
    val isEnable: Boolean = false,
    @SerializedName("recent")
    val recent: BrandingOverrideHistoryRecentModel? = null,
    @SerializedName("paginated_scroll")
    val paginatedScroll: BrandingOverrideHistoryRecentModel? = null
)

