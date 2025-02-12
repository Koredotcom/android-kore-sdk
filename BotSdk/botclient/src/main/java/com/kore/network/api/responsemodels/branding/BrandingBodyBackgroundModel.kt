package com.kore.network.api.responsemodels.branding

import com.google.gson.annotations.SerializedName

data class BrandingBodyBackgroundModel(
    @SerializedName("type")
    val type : String? = null,
    @SerializedName("color")
    val color : String? = null,
    @SerializedName("img")
    val img : String? = null
)