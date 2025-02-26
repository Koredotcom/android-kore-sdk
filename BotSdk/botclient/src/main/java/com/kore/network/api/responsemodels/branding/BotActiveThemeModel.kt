package com.kore.network.api.responsemodels.branding

import com.google.gson.annotations.SerializedName

data class BotActiveThemeModel(
    @SerializedName("_id")
    val id: String = "",
    @SerializedName("streamId")
    val streamId: String = "",
    @SerializedName("activeTheme")
    val activeTheme: Boolean,
    @SerializedName("createdBy")
    val createdBy: String = "",
    @SerializedName("createdOn")
    val createdOn: String = "",
    @SerializedName("defaultTheme")
    val defaultTheme: Boolean,
    @SerializedName("lastModifiedBy")
    val lastModifiedBy: String = "",
    @SerializedName("lastModifiedOn")
    val lastModifiedOn: String = "",
    @SerializedName("refId")
    val refId: String = "",
    @SerializedName("state")
    val state: String = "",
    @SerializedName("themeName")
    val themeName: String = "",
    @SerializedName("v3")
    val brandingModel: BotBrandingModel? = null,
    @SerializedName("botMessage")
    var botMessage: BrandingBotMessagesModel? = null,
    @SerializedName("userMessage")
    var userMessage: BrandingBotMessagesModel? = null,
    @SerializedName("buttons")
    var buttons: BrandingButtonsModel? = null,
    @SerializedName("generalAttributes")
    var generalAttributes: BrandingGeneralAttributesModel? = null,
    @SerializedName("widgetBody")
    var widgetBody: BrandingWidgetBodyModel? = null,
    @SerializedName("widgetFooter")
    var widgetFooter: BrandingWidgetBodyModel? = null,
    @SerializedName("widgetHeader")
    var widgetHeader: BrandingWidgetBodyModel? = null
)

fun BotActiveThemeModel.updateWithV3Model(configModel: BotBrandingModel): BotActiveThemeModel {
    return this.copy(
        brandingModel = brandingModel?.updateWith(configModel),
    )
}