package com.kore.network.api.responsemodels.branding

import com.google.gson.annotations.SerializedName

data class BrandingOverrideHistoryRecentModel(
    @SerializedName("batch_size")
    val batchSize: Int? = 0,
    @SerializedName("loading_label")
    val loadingLabel: String? = null,
    @SerializedName("isEnable")
    val isEnable: Boolean? = false,
)

fun BrandingOverrideHistoryRecentModel.updateWith(configModel: BrandingOverrideHistoryRecentModel?): BrandingOverrideHistoryRecentModel {
    return this.copy(
        isEnable = configModel?.isEnable ?: isEnable,
        batchSize = configModel?.batchSize ?: batchSize,
        loadingLabel = configModel?.loadingLabel?.ifEmpty { loadingLabel } ?: loadingLabel
    )
}
