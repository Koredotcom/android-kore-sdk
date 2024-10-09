package com.kore.network.api.responsemodels.branding

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class BrandingIconModel (
    @SerializedName("icon_url")
    val iconUrl : String?,
    @SerializedName("size")
    val size : String?,
    @SerializedName("shape")
    val shape : String?,
    @SerializedName("type")
    val type : String?,
    @SerializedName("show")
    val show : Boolean,
    @SerializedName("color")
    val color : String?,
    @SerializedName("icon")
    val icon : String?,
    @SerializedName("action")
    val action : BrandingQuickStartButtonActionModel?
): Serializable