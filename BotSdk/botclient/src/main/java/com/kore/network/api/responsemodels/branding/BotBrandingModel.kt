package com.kore.network.api.responsemodels.branding

import com.google.gson.annotations.SerializedName

data class BotBrandingModel (
    @SerializedName("general")
    val general : BrandingGeneralModel,
    @SerializedName("chat_bubble")
    val chatBubble : BrandingChatBubbleModel,
    @SerializedName("welcome_screen")
    val welcomeScreen : BrandingWelcomeModel,
    @SerializedName("header")
    val header : BrandingHeaderModel,
    @SerializedName("footer")
    val footer : BrandingFooterModel,
    @SerializedName("body")
    val body : BrandingBodyModel
)