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
    val bodyIconModel: BrandingBodyIconModel? = null,
    @SerializedName("time_stamp")
    val timeStamp: BrandingBodyTimeStampModel? = null,
    @SerializedName("agent_message")
    val agentMessage: BrandingBodyAgentMsgModel? = null,
    @SerializedName("bot_message")
    val botMessage: BrandingBodyUserMessageModel? = null,
    @SerializedName("user_message")
    val userMessage: BrandingBodyUserMessageModel? = null,
    @SerializedName("font")
    val font: BrandingBodyFontModel? = null,
    @SerializedName("background")
    val background: BrandingBodyBackgroundModel? = null
)