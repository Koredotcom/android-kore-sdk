package com.kore.network.api.responsemodels.branding

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class BrandingTitleModel (
    @SerializedName("name")
    val name : String?,
    @SerializedName("color")
    var color : String?,
    @SerializedName("type")
    val type : String?,
    @SerializedName("img")
    val img : String?
) : Serializable