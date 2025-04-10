package com.kore.network.api.responsemodels.branding

import com.google.gson.annotations.SerializedName

data class BrandingColorsModel(
    @SerializedName("primary")
    val primary: String? = null,
    @SerializedName("secondary")
    val secondary: String? = null,
    @SerializedName("primary_text")
    val primaryText: String? = null,
    @SerializedName("secondary_text")
    val secondaryText: String? = null,
    @SerializedName("useColorPaletteOnly")
    val useColorPaletteOnly: Boolean? = null
)

fun BrandingColorsModel.updateWith(configModel: BrandingColorsModel): BrandingColorsModel {
    return this.copy(
        primary = configModel.primary?.ifEmpty { this.primary } ?: primary,
        secondary = configModel.secondary?.ifEmpty { this.secondary } ?: secondary,
        primaryText = configModel.primaryText?.ifEmpty { this.primaryText } ?: primaryText,
        secondaryText = configModel.secondaryText?.ifEmpty { this.secondaryText } ?: secondaryText,
        useColorPaletteOnly = configModel.useColorPaletteOnly ?: this.useColorPaletteOnly,
    )
}