package com.kore.network.api.responsemodels.branding

import com.google.gson.annotations.SerializedName

data class BrandingStaticLinksModel (
    @SerializedName("show")
    val show : Boolean,
    @SerializedName("layout")
    val layout : String = ""
)