package com.kore.network.api.responsemodels.branding

import com.google.gson.annotations.SerializedName

data class BrandingOverrideConfigModel(
    @SerializedName("isEnable")
    val isEnable: Boolean? = false,
    @SerializedName("isEmoji_short_cut")
    val emojiShortCut: Boolean? = false,
    @SerializedName("typing_indicator_timeout")
    val typingIndicatorTimeout: Int? = 0,
    @SerializedName("history")
    val history: BrandingOverrideHistoryModel? = null
)

fun BrandingOverrideConfigModel.updateWith(configModel: BrandingOverrideConfigModel?): BrandingOverrideConfigModel {
    return this.copy(
        isEnable = configModel?.isEnable ?: isEnable,
        emojiShortCut = configModel?.emojiShortCut ?: emojiShortCut,
        typingIndicatorTimeout = configModel?.typingIndicatorTimeout ?: typingIndicatorTimeout,
        history = history?.updateWith(configModel?.history) ?: history
    )
}
