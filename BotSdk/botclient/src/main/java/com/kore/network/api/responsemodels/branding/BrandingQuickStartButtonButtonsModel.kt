package com.kore.network.api.responsemodels.branding

import com.google.gson.annotations.SerializedName

data class BrandingQuickStartButtonButtonsModel (
    @SerializedName("title")
    val title : String = "",
    @SerializedName("description")
    val description : String = "",
    @SerializedName("action")
    val action : BrandingQuickStartButtonActionModel
)