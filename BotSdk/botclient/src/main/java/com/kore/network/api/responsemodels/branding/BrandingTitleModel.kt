package com.kore.network.api.responsemodels.branding

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class BrandingTitleModel (
    @SerializedName("name")
    val name : String? = null,
    @SerializedName("color")
    var color : String? = null,
    @SerializedName("type")
    val type : String? = null,
    @SerializedName("img")
    val img : String? = null
) : Serializable