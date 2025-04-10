package com.kore.network.api.responsemodels.branding

import com.google.gson.annotations.SerializedName

data class BrandingBodyIconModel(
    @SerializedName("show")
    val show: Boolean?,
    @SerializedName("user_icon")
    val userIcon: Boolean?,
    @SerializedName("bot_icon")
    val botIcon: Boolean?,
    @SerializedName("agent_icon")
    val agentIcon: Boolean?,
)

fun BrandingBodyIconModel.updateWith(configModel: BrandingBodyIconModel?): BrandingBodyIconModel {
    return this.copy(
        show = configModel?.show ?: show,
        userIcon = configModel?.userIcon ?: userIcon,
        botIcon = configModel?.botIcon ?: botIcon,
        agentIcon = configModel?.agentIcon ?: agentIcon,
    )
}