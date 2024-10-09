package com.kore.network.api.responsemodels.branding

import com.google.gson.annotations.SerializedName

data class PromotionalContentModel (
    @SerializedName("show")
    val show : Boolean,
    @SerializedName("promotions")
    val promotions : ArrayList<PromotionsModel>
)