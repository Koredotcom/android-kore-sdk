package com.kore.network.api.responsemodels.branding

import com.google.gson.annotations.SerializedName

data class PromotionsModel (
    @SerializedName("banner")
    val banner : String = "",
    @SerializedName("action")
    val action : BrandingQuickStartButtonActionModel
)