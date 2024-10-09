package com.kore.network.api.responsemodels.branding

import com.google.gson.annotations.SerializedName

data class StaticLinksModel (
    @SerializedName("show")
    val show : Boolean,
    @SerializedName("type")
    val type : String = "",
    @SerializedName("layout")
    val layout : String = "",
    @SerializedName("links")
    val links : ArrayList<BrandingQuickStartButtonButtonsModel>
)