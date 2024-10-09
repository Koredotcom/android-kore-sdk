package com.kore.network.api.responsemodels.branding

import com.google.gson.annotations.SerializedName

data class BrandingBodyTimeStampModel(
    @SerializedName("show") val show: Boolean,
    @SerializedName("show_type") val showType: String = "",
    @SerializedName("position") val position: String = "",
    @SerializedName("separator") val separator: String = "",
    @SerializedName("color") val color: String = "",
    @SerializedName("time_format") val timeFormat: Int = 12,
    @SerializedName("date_format") val dateFormat: String = "",
)