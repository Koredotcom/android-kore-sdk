package com.kore.network.api.responsemodels.branding

import com.google.gson.annotations.SerializedName

data class BrandingQuickStartButtons (
    @SerializedName("show")
    val show : Boolean,
    @SerializedName("style")
    val style : String = "",
    @SerializedName("buttons")
    val buttons : ArrayList<BrandingQuickStartButtonButtonsModel>,
    @SerializedName("input")
    val input : String = "",
    @SerializedName("action")
    val action : BrandingQuickStartButtonActionModel
)