package com.kore.network.api.responsemodels.branding

import com.google.gson.annotations.SerializedName

data class BrandingColorsModel(
    @SerializedName("primary")
    val primary: String = "",
    @SerializedName("secondary")
    val secondary: String = "",
    @SerializedName("primary_text")
    val primaryText: String = "",
    @SerializedName("secondary_text")
    val secondaryText: String = "",
    @SerializedName("useColorPaletteOnly")
    val useColorPaletteOnly: Boolean
)