package com.kore.network.api.responsemodels.branding

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class BrandingHeaderButtonsModel(
    @SerializedName("close")
    val close: BrandingIconModel?,
    @SerializedName("minimise")
    val minimise: BrandingIconModel?,
    @SerializedName("expand")
    val expand: BrandingIconModel?,
    @SerializedName("reconnect")
    val reconnect: BrandingIconModel?,
    @SerializedName("help")
    val help: BrandingIconModel?,
    @SerializedName("live_agent")
    val liveAgent: BrandingIconModel?
) : Serializable

fun BrandingHeaderButtonsModel.updateWith(configModel: BrandingHeaderButtonsModel?): BrandingHeaderButtonsModel {
    return this.copy(
        close = this.close?.updateWith(configModel?.close) ?: this.close,
        minimise = this.minimise?.updateWith(configModel?.minimise) ?: this.minimise,
        expand = this.expand?.updateWith(configModel?.expand) ?: this.expand,
        reconnect = this.reconnect?.updateWith(configModel?.reconnect) ?: this.reconnect,
        help = this.help?.updateWith(configModel?.help) ?: this.help,
        liveAgent = this.liveAgent?.updateWith(configModel?.liveAgent) ?: this.liveAgent,
    )
}