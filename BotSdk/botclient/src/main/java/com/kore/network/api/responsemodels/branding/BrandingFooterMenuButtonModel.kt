package com.kore.network.api.responsemodels.branding

import com.google.gson.annotations.SerializedName

data class BrandingFooterMenuButtonModel(
    @SerializedName("show")
    val show: Boolean,
    @SerializedName("icon")
    val icon: String = "",
    @SerializedName("actions")
    val actions: ArrayList<BrandingQuickStartButtonActionModel>,
)