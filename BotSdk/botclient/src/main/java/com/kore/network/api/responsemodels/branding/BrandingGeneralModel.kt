package com.kore.network.api.responsemodels.branding

import com.google.gson.annotations.SerializedName

data class BrandingGeneralModel(
    @SerializedName("bot_icon")
    val botIcon: String = "",
    @SerializedName("size")
    val size: String? = null,
    @SerializedName("themeType")
    val themeType: String = "",
    @SerializedName("widgetPanel")
    val widgetPanel: Boolean? = null,
    @SerializedName("colors")
    val colors: BrandingColorsModel
)

fun BrandingGeneralModel.updateWith(configModel: BrandingGeneralModel): BrandingGeneralModel {
    return this.copy(
        botIcon = configModel.botIcon.ifEmpty { this.botIcon },
        size = configModel.size?.ifEmpty { this.size },
        themeType = configModel.themeType.ifEmpty { this.themeType },
        widgetPanel = configModel.widgetPanel ?: this.widgetPanel,
        colors = colors.updateWith(configModel.colors),
    )
}