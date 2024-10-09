package com.kore.network.api.responsemodels.branding

import com.google.gson.annotations.SerializedName

data class BrandingMinimizeModel (
    @SerializedName("icon")
    val icon : String = "",
    @SerializedName("theme")
    val theme : String = ""
)