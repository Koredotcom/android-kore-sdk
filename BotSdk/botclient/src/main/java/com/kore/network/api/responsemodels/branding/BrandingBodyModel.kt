package com.kore.network.api.responsemodels.branding

import com.google.gson.annotations.SerializedName

data class BrandingBodyModel(
    @SerializedName("img")
    val img: String = "",
    @SerializedName("secondaryHoverColor")
    val secondaryHoverColor: String = "",
    @SerializedName("secondaryColor")
    val secondaryColor: String = "",
    @SerializedName("primaryHoverColor")
    val primaryHoverColor: String = "",
    @SerializedName("primaryColor")
    val primaryColor: String = "",
    @SerializedName("bubble_style")
    val bubbleStyle: String = "",
    @SerializedName("bodyIconModel")
    val bodyIconModel: BrandingBodyIconModel,
    @SerializedName("time_stamp")
    val timeStamp: BrandingBodyTimeStampModel?,
    @SerializedName("agent_message")
    val agentMessage: BrandingBodyAgentMsgModel,
    @SerializedName("bot_message")
    val botMessage: BrandingBodyUserMessageModel,
    @SerializedName("user_message")
    val userMessage: BrandingBodyUserMessageModel,
    @SerializedName("font")
    val font: BrandingBodyFontModel,
    @SerializedName("background")
    val background: BrandingBodyBackgroundModel
)