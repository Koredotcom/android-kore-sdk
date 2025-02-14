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

fun BrandingBodyModel.updateWith(configModel: BrandingBodyModel): BrandingBodyModel {
    return this.copy(
        img = configModel.img.ifEmpty { this.img },
        secondaryHoverColor = configModel.secondaryHoverColor.ifEmpty { this.secondaryHoverColor },
        secondaryColor = configModel.secondaryColor.ifEmpty { this.secondaryColor },
        primaryHoverColor = configModel.primaryHoverColor.ifEmpty { this.primaryHoverColor },
        primaryColor = configModel.primaryColor.ifEmpty { this.primaryColor },
        bubbleStyle = configModel.bubbleStyle.ifEmpty { this.bubbleStyle },
        timeStamp = this.timeStamp?.updateWith(configModel.timeStamp ?: this.timeStamp),
        bodyIconModel = this.bodyIconModel?.updateWith(configModel.bodyIconModel ?: this.bodyIconModel),
        agentMessage = this.agentMessage?.updateWith(configModel.agentMessage ?: this.agentMessage),
        botMessage = this.botMessage?.updateWith(configModel.botMessage ?: this.botMessage),
        userMessage = this.userMessage?.updateWith(configModel.userMessage ?: this.userMessage),
        font = this.font?.updateWith(configModel.font ?: this.font),
        background = this.background?.updateWith(configModel.background ?: this.background),
    )
}