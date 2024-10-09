package com.kore.network.api.responsemodels.branding

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class BrandingQuickStartButtonActionModel (
    @SerializedName("type")
    val type : String?,
    @SerializedName("value")
    val value : String?,
    @SerializedName("icon")
    val icon : String?,
    @SerializedName("title")
    val title : String?,
) : Serializable