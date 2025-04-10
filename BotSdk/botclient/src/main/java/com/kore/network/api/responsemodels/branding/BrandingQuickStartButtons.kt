package com.kore.network.api.responsemodels.branding

import com.google.gson.annotations.SerializedName

data class BrandingQuickStartButtons(
    @SerializedName("show")
    val show: Boolean?,
    @SerializedName("style")
    val style: String = "",
    @SerializedName("buttons")
    val buttons: ArrayList<BrandingQuickStartButtonButtonsModel> = ArrayList(),
    @SerializedName("input")
    val input: String = "",
    @SerializedName("action")
    val action: BrandingQuickStartButtonActionModel?
)

fun BrandingQuickStartButtons.updateWith(configModel: BrandingQuickStartButtons?): BrandingQuickStartButtons {
    return this.copy(
        show = configModel?.show ?: show,
        style = style.ifEmpty { style },
        input = input.ifEmpty { input },
        action = action?.updateWith(configModel?.action) ?: action
    )
}