package com.kore.network.api.responsemodels.branding

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class BrandingHeaderButtonsModel(
    @SerializedName("close")
    val close : BrandingIconModel?,
    @SerializedName("minimise")
    val minimise : BrandingIconModel?,
    @SerializedName("expand")
    val expand : BrandingIconModel?,
    @SerializedName("reconnect")
    val reconnect : BrandingIconModel?,
    @SerializedName("help")
    val help : BrandingIconModel?,
    @SerializedName("live_agent")
    val liveAgent : BrandingIconModel?
) : Serializable