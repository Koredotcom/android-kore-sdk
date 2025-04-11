package com.kore.network.api.responsemodels.branding

import com.google.gson.annotations.SerializedName

data class BrandingFooterButtonsModel(
    @SerializedName("menu")
    val menu: BrandingFooterMenuButtonModel,
    @SerializedName("emoji")
    val emoji: BrandingIconModel,
    @SerializedName("microphone")
    val microphone: BrandingIconModel,
    @SerializedName("attachment")
    val attachment: BrandingIconModel,
    @SerializedName("speaker")
    val speaker: BrandingIconModel
)

fun BrandingFooterButtonsModel.updateWith(configModel: BrandingFooterButtonsModel?): BrandingFooterButtonsModel {
    return this.copy(
        menu = this.menu.updateWith(configModel?.menu),
        emoji = this.emoji.updateWith(configModel?.emoji),
        microphone = this.microphone.updateWith(configModel?.microphone),
        attachment = this.attachment.updateWith(configModel?.attachment),
        speaker = this.speaker.updateWith(configModel?.speaker),
    )
}