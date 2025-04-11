package com.kore.network.api.responsemodels.branding

import com.google.gson.annotations.SerializedName

data class BrandingBodyTimeStampModel(
    @SerializedName("show") val show: Boolean?,
    @SerializedName("show_type") val showType: String = "",
    @SerializedName("position") val position: String = "",
    @SerializedName("separator") val separator: String = "",
    @SerializedName("color") val color: String = "",
    @SerializedName("time_format") val timeFormat: String = "12",
    @SerializedName("date_format") val dateFormat: String = "",
)

fun BrandingBodyTimeStampModel.updateWith(configModel: BrandingBodyTimeStampModel?): BrandingBodyTimeStampModel {
    return this.copy(
        show = configModel?.show ?: show,
        showType = configModel?.showType?.ifEmpty { this.showType } ?: showType,
        position = configModel?.position?.ifEmpty { this.position } ?: position,
        separator = configModel?.separator?.ifEmpty { this.separator } ?: separator,
        color = configModel?.color?.ifEmpty { this.color } ?: color,
        timeFormat = configModel?.timeFormat?.ifEmpty { timeFormat } ?: timeFormat,
        dateFormat = configModel?.dateFormat?.ifEmpty { this.dateFormat } ?: dateFormat,
    )
}