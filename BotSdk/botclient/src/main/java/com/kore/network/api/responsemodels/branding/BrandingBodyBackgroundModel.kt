package com.kore.network.api.responsemodels.branding

import com.google.gson.annotations.SerializedName

data class BrandingBodyBackgroundModel(
    @SerializedName("type")
    val type : String = "",
    @SerializedName("color")
    val color : String = "",
    @SerializedName("img")
    val img : String = ""
)