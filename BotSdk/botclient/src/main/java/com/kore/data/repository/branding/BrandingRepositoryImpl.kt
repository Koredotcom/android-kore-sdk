package com.kore.data.repository.branding

import android.content.Context
import com.google.gson.Gson
import com.kore.botclient.R
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
import com.kore.network.api.responsemodels.branding.updateWithV3Model
import com.kore.network.api.service.BrandingApi
import java.io.InputStreamReader

class BrandingRepositoryImpl : BrandingRepository {
    override suspend fun getBranding(
        context: Context,
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
                val model: BotActiveThemeModel? = response.body() ?: getLocalBrandingDetails(context)
                Result.Success(processResponse(model))
            } else {
                Result.Success(processResponse(getLocalBrandingDetails(context)))
            }
        } catch (ex: Exception) {
            Result.Success(processResponse(getLocalBrandingDetails(context)))
        }
    }
}

internal fun getLocalBrandingDetails(context: Context): BotActiveThemeModel? {
    val inputStream = context.resources.openRawResource(R.raw.branding_details)
    val reader = InputStreamReader(inputStream)
    val jsonText = reader.readText().also { reader.close() }
    return Gson().fromJson(jsonText, BotActiveThemeModel::class.java)
}

internal fun processResponse(responseModel: BotActiveThemeModel?): BotActiveThemeModel? {
    val configBrandingModel = SDKConfiguration.getBotBrandingConfig()
    val model = if (configBrandingModel != null) responseModel?.updateWithV3Model(configBrandingModel) else responseModel

    if (model?.brandingModel?.overrideKoreConfig?.isEnable == true) {
        val brandingModel = model.brandingModel
        SDKConfiguration.OverrideKoreConfig.isEmojiShortcutEnable = brandingModel.overrideKoreConfig?.emojiShortCut == true
        SDKConfiguration.OverrideKoreConfig.typingIndicatorTimeout = brandingModel.overrideKoreConfig?.typingIndicatorTimeout ?: 0
        if (brandingModel.footer.buttons != null) {
            SDKConfiguration.OverrideKoreConfig.showASRMicroPhone = brandingModel.footer.buttons.microphone.show == true
            SDKConfiguration.OverrideKoreConfig.showAttachment = brandingModel.footer.buttons.attachment.show == true
            SDKConfiguration.OverrideKoreConfig.showTextToSpeech = brandingModel.footer.buttons.speaker.show == true
        }
        if (brandingModel.overrideKoreConfig?.history != null) {
            SDKConfiguration.OverrideKoreConfig.historyEnable = brandingModel.overrideKoreConfig.history.isEnable ?: false
            if (brandingModel.overrideKoreConfig.history.recent != null) SDKConfiguration.OverrideKoreConfig.historyBatchSize =
                brandingModel.overrideKoreConfig.history.recent.batchSize ?: 0
            if (brandingModel.overrideKoreConfig.history.paginatedScroll != null) {
                SDKConfiguration.OverrideKoreConfig.paginatedScrollEnable = brandingModel.overrideKoreConfig.history.paginatedScroll.isEnable == true
                SDKConfiguration.OverrideKoreConfig.paginatedScrollBatchSize = brandingModel.overrideKoreConfig.history.paginatedScroll.batchSize ?: 0
                SDKConfiguration.OverrideKoreConfig.paginatedScrollLoadingLabel =
                    brandingModel.overrideKoreConfig.history.paginatedScroll.loadingLabel
            }
        }
    }
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