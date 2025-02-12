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
    val useColorPaletteOnly: Boolean = false
)