package com.kore.data.repository.branding

import com.kore.common.Result
import com.kore.common.SDKConfiguration
import com.kore.network.api.ApiClient
import com.kore.network.api.responsemodels.branding.BotActiveThemeModel
import com.kore.network.api.responsemodels.branding.BotBrandingModel
import com.kore.network.api.responsemodels.branding.BrandingBodyBackgroundModel
import com.kore.network.api.responsemodels.branding.BrandingBodyModel
import com.kore.network.api.responsemodels.branding.BrandingBodyUserMessageModel
import com.kore.network.api.responsemodels.branding.BrandingColorsModel
import com.kore.network.api.responsemodels.branding.BrandingFooterComposeBarModel
import com.kore.network.api.responsemodels.branding.BrandingFooterModel
import com.kore.network.api.responsemodels.branding.BrandingGeneralModel
import com.kore.network.api.responsemodels.branding.BrandingHeaderModel
import com.kore.network.api.responsemodels.branding.BrandingTitleModel
import com.kore.network.api.service.BrandingApi

class BrandingRepositoryImpl : BrandingRepository {
    override suspend fun getBranding(
        botId: String,
        token: String,
        state: String,
        version: String,
        language: String
    ): Result<BotActiveThemeModel?> {
        return try {
            val brandingApi = ApiClient.getInstance().createService(BrandingApi::class.java)
            val response = brandingApi.getBrandingNewDetails(botId, "bearer $token", state, version, language, botId)
            if (response.isSuccessful) {
                val model: BotActiveThemeModel? = response.body()
                if (model?.brandingModel?.overrideKoreConfig?.isEnable == true) {
                    val brandingModel = model.brandingModel
                    SDKConfiguration.OverrideKoreConfig.isEmojiShortcutEnable = brandingModel.overrideKoreConfig?.emojiShortCut == true
                    SDKConfiguration.OverrideKoreConfig.typingIndicatorTimeout = brandingModel.overrideKoreConfig?.typingIndicatorTimeout ?: 0
                    if (brandingModel.footer.buttons != null) {
                        SDKConfiguration.OverrideKoreConfig.showASRMicroPhone = brandingModel.footer.buttons.microphone.show
                        SDKConfiguration.OverrideKoreConfig.showAttachment = brandingModel.footer.buttons.attachment.show
                        SDKConfiguration.OverrideKoreConfig.showTextToSpeech = brandingModel.footer.buttons.speaker.show
                    }
                    if (brandingModel.overrideKoreConfig?.history != null) {
                        SDKConfiguration.OverrideKoreConfig.historyEnable = brandingModel.overrideKoreConfig.history.isEnable
                        if (brandingModel.overrideKoreConfig.history.recent != null) SDKConfiguration.OverrideKoreConfig.historyBatchSize =
                            brandingModel.overrideKoreConfig.history.recent.batchSize
                        if (brandingModel.overrideKoreConfig.history.paginatedScroll != null) {
                            SDKConfiguration.OverrideKoreConfig.paginatedScrollEnable = brandingModel.overrideKoreConfig.history.paginatedScroll.isEnable
                            SDKConfiguration.OverrideKoreConfig.paginatedScrollBatchSize = brandingModel.overrideKoreConfig.history.paginatedScroll.batchSize
                            SDKConfiguration.OverrideKoreConfig.paginatedScrollLoadingLabel =
                                brandingModel.overrideKoreConfig.history.paginatedScroll.loadingLabel
                        }
                    }
                }
                Result.Success(processResponse(model))
            } else {
                Result.Error(Exception(response.errorBody()?.string()))
            }
        } catch (ex: Exception) {
            Result.Error(Exception(ex.message.toString()))
        }
    }
}

internal fun processResponse(model: BotActiveThemeModel?): BotActiveThemeModel? {
    if (model == null || model.brandingModel != null) return model
    val brandingModel = BotBrandingModel(
        BrandingGeneralModel(
            colors = BrandingColorsModel(
                primary = model.buttons?.defaultButtonColor,
                primaryText = model.buttons?.defaultFontColor,
                secondary = model.buttons?.onHoverButtonColor,
                secondaryText = model.buttons?.onHoverFontColor
            )
        ),
        BrandingHeaderModel(
            bgColor = model.widgetHeader?.backgroundColor,
            title = BrandingTitleModel(
                name = SDKConfiguration.getBotConfigModel()?.botName,
                color = model.widgetHeader?.fontColor
            )
        ),
        BrandingFooterModel(
            bgColor = model.widgetFooter?.backgroundColor,
            composeBar = BrandingFooterComposeBarModel(
                outlineColor = model.widgetFooter?.borderColor,
                placeholder = model.widgetFooter?.placeHolder
            )
        ),
        BrandingBodyModel(
            botMessage = BrandingBodyUserMessageModel(
                bgColor = model.botMessage?.bubbleColor,
                color = model.botMessage?.fontColor
            ),
            userMessage = BrandingBodyUserMessageModel(
                bgColor = model.userMessage?.bubbleColor,
                color = model.userMessage?.fontColor
            ),
            background = BrandingBodyBackgroundModel(color = model.widgetBody?.backgroundColor)
        )
    )

    return model.copy(brandingModel = brandingModel)
}

//    private fun processResponse(response: Response<String>): BrandingModel? {
//        if (response.isSuccessful && response.body() != null) {
//            try {
//                val resp = response.body().toString()
//                val activeThemeType = object : TypeToken<BotActiveThemeModel?>() {
//                }.type
//                val brandingNewDos: BotActiveThemeModel = Gson().fromJson(resp, activeThemeType)
//                if (brandingNewDos.v3 == null) {
//                    val brandingModel = BrandingModel(
//                        botChatBgColor = brandingNewDos.botMessage?.bubbleColor,
//                        botChatTextColor = brandingNewDos.botMessage?.fontColor,
//                        userChatBgColor = brandingNewDos.userMessage?.bubbleColor,
//                        userChatTextColor = brandingNewDos.userMessage?.fontColor,
//
//                        buttonActiveBgColor = brandingNewDos.buttons?.defaultButtonColor,
//                        buttonActiveTextColor = brandingNewDos.buttons?.defaultFontColor,
//
//                        buttonInactiveBgColor = brandingNewDos.buttons?.onHoverButtonColor,
//                        buttonInactiveTextColor = brandingNewDos.buttons?.onHoverFontColor,
//                        buttonBorderColor = brandingNewDos.buttons?.borderColor,
//
//                        botName = SDKConfiguration.getBotConfigModel()?.botName,
//                        widgetBodyColor = brandingNewDos.widgetBody?.backgroundColor,
//                        widgetTextColor = brandingNewDos.widgetHeader?.fontColor,
//                        widgetHeaderColor = brandingNewDos.widgetHeader?.backgroundColor,
//                        widgetFooterColor = brandingNewDos.widgetFooter?.backgroundColor,
//                        widgetFooterBorderColor = brandingNewDos.widgetFooter?.borderColor,
//                        widgetFooterHintColor = brandingNewDos.widgetFooter?.placeHolder
//                    )
//                    return brandingModel
//
//                } else {
//                    val botBrandingV3Model: BotBrandingV3Model = brandingNewDos.v3
//                    try {
//                        val brandingModel = BrandingModel(
//                            botChatBgColor = botBrandingV3Model.body.botMessage.bgColor,
//                            botChatTextColor = botBrandingV3Model.getBody().getBot_message().getColor(),
//
//                            userChatBgColor = botBrandingV3Model.getBody().getUser_message().getBg_color(),
//                            userChatTextColor = botBrandingV3Model.getBody().getUser_message().getColor(),
//
//                            botName = botBrandingV3Model.getHeader().getTitle().getName(),
//                            widgetBodyColor = botBrandingV3Model.getBody().getBackground().getColor(),
//                            widgetTextColor = botBrandingV3Model.getHeader().getTitle().getColor(),
//                            widgetHeaderColor = botBrandingV3Model.getHeader().getBg_color(),
//                            widgetFooterColor = botBrandingV3Model.getFooter().getBg_color(),
//                            widgetFooterBorderColor = botBrandingV3Model.getFooter().getCompose_bar().getOutline_color(),
//                            widgetFooterHintColor = botBrandingV3Model.getFooter().getCompose_bar().getOutline_color(),
//                            widgetFooterHintText = botBrandingV3Model.getFooter().getCompose_bar().getPlaceholder(),
//                            chatBubbleStyle = botBrandingV3Model.getChat_bubble().getStyle()
//                        )
//
//                        if (botBrandingV3Model.getFooter().getButtons().getMicrophone() != null) SDKConfiguration.OverrideKoreConfig.showASRMicroPhone =
//                            botBrandingV3Model.getFooter().getButtons().getMicrophone().isShow()
//                        if (botBrandingV3Model.getFooter().getButtons().getAttachment() != null) SDKConfiguration.OverrideKoreConfig.showAttachment =
//                            botBrandingV3Model.getFooter().getButtons().getAttachment().isShow()
//                        if (botBrandingV3Model.getFooter().getButtons().getSpeaker() != null) SDKConfiguration.OverrideKoreConfig.showTextToSpeech =
//                            botBrandingV3Model.getFooter().getButtons().getSpeaker().isShow()
//
//                        if (botBrandingV3Model.getGeneral() != null && botBrandingV3Model.getGeneral().getColors() != null && botBrandingV3Model.getGeneral()
//                                .getColors().isUseColorPaletteOnly()
//                        ) {
//                            brandingModel.setButtonActiveBgColor(botBrandingV3Model.getGeneral().getColors().getPrimary())
//                            brandingModel.setButtonActiveTextColor(botBrandingV3Model.getGeneral().getColors().getPrimary_text())
//                            brandingModel.setButtonInactiveBgColor(botBrandingV3Model.getGeneral().getColors().getSecondary())
//                            brandingModel.setButtonInactiveTextColor(botBrandingV3Model.getGeneral().getColors().getSecondary_text())
//                            SDKConfiguration.BubbleColors.quickReplyColor = botBrandingV3Model.getGeneral().getColors().getPrimary()
//                            SDKConfiguration.BubbleColors.quickReplyTextColor = botBrandingV3Model.getGeneral().getColors().getPrimary_text()
//                            brandingModel.setBotchatBgColor(botBrandingV3Model.getGeneral().getColors().getSecondary())
//                            brandingModel.setBotchatTextColor(botBrandingV3Model.getGeneral().getColors().getPrimary_text())
//                            brandingModel.setUserchatBgColor(botBrandingV3Model.getGeneral().getColors().getPrimary())
//                            brandingModel.setUserchatTextColor(botBrandingV3Model.getGeneral().getColors().getSecondary_text())
//                            brandingModel.setWidgetHeaderColor(botBrandingV3Model.getGeneral().getColors().getSecondary())
//                            brandingModel.setWidgetFooterColor(botBrandingV3Model.getGeneral().getColors().getSecondary())
//                            brandingModel.setWidgetTextColor((botBrandingV3Model.getGeneral().getColors().getPrimary_text()))
//                        }
//
//
//                        if (botBrandingV3Model.getOverride_kore_config() != null && botBrandingV3Model.getOverride_kore_config().isEnable()) {
//                            SDKConfiguration.OverrideKoreConfig.isEmojiShortcutEnable = botBrandingV3Model.getOverride_kore_config().isEmoji_short_cut()
//                            SDKConfiguration.OverrideKoreConfig.typing_indicator_timeout =
//                                botBrandingV3Model.getOverride_kore_config().getTyping_indicator_timeout()
//                            if (botBrandingV3Model.getOverride_kore_config().getHistory() != null) {
//                                SDKConfiguration.OverrideKoreConfig.history_enable = botBrandingV3Model.getOverride_kore_config().getHistory().isEnable()
//                                if (botBrandingV3Model.getOverride_kore_config().getHistory()
//                                        .getRecent() != null
//                                ) SDKConfiguration.OverrideKoreConfig.history_batch_size =
//                                    botBrandingV3Model.getOverride_kore_config().getHistory().getRecent().getBatch_size()
//                                if (botBrandingV3Model.getOverride_kore_config().getHistory().getPaginated_scroll() != null) {
//                                    SDKConfiguration.OverrideKoreConfig.paginated_scroll_enable =
//                                        botBrandingV3Model.getOverride_kore_config().getHistory().getPaginated_scroll().isEnable()
//                                    SDKConfiguration.OverrideKoreConfig.paginated_scroll_batch_size =
//                                        botBrandingV3Model.getOverride_kore_config().getHistory().getPaginated_scroll().getBatch_size()
//                                    SDKConfiguration.OverrideKoreConfig.paginated_scroll_loading_label =
//                                        botBrandingV3Model.getOverride_kore_config().getHistory().getPaginated_scroll().getLoading_label()
//                                }
//                            }
//                        }
//
//                        brandingModel
//                    } catch (ex: java.lang.Exception) {
//                        ex.printStackTrace()
//                    }
//                } else {
//                    getBrandingDataFromTxt()
//                }
//            } catch (ex: java.lang.Exception) {
//                getBrandingDataFromTxt()
//                throw RuntimeException(ex)
//            }
//        }
//    } else
//    {
//        getBrandingDataFromTxt()
//    }
//    return model
//}