package com.kore.network.api.responsemodels.branding

import com.google.gson.annotations.SerializedName

data class BotBrandingModel(
    @SerializedName("general")
    val general: BrandingGeneralModel,
    @SerializedName("header")
    val header: BrandingHeaderModel,
    @SerializedName("footer")
    val footer: BrandingFooterModel,
    @SerializedName("body")
    val body: BrandingBodyModel,
    @SerializedName("chat_bubble")
    val chatBubble: BrandingChatBubbleModel? = null,
    @SerializedName("welcome_screen")
    val welcomeScreen: BrandingWelcomeModel? = null,
    @SerializedName("override_kore_config")
    val overrideKoreConfig: BrandingOverrideConfigModel? = null
)