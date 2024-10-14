package com.kore.ai.botsdk

import android.app.Application
import android.content.res.Resources.NotFoundException
import android.util.Log
import com.kore.ai.botsdk.row.DownloadLinkTemplateProvider
import com.kore.ai.botsdk.row.DownloadLinkTemplateRow
import com.kore.common.SDKConfiguration
import com.kore.common.model.BotConfigModel
import com.kore.ui.adapters.BotChatAdapter
import com.kore.ui.row.botchat.BotChatRowType
import java.io.IOException
import java.util.Properties

class BotApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        getBotConfigModel()?.let { SDKConfiguration.initialize(it) }
        BotChatRowType.prepareDefaultRowTypes()
//        BotChatAdapter.addCustomTemplate(BotChatRowType.ROW_BUTTON_PROVIDER, BotResponseConstants.TEMPLATE_TYPE_BUTTON, SampleTemplateProvider(), SampleTemplateRow::class)
        BotChatAdapter.addCustomTemplate("link", "link", DownloadLinkTemplateProvider(), DownloadLinkTemplateRow::class)
    }

    private fun getBotConfigModel(): BotConfigModel? {
        try {
            val rawResource = resources.openRawResource(R.raw.config)
            val properties = Properties()
            properties.load(rawResource)
            return BotConfigModel(
                botName = properties.getProperty("botName"),
                botId = properties.getProperty("botId"),
                clientId = properties.getProperty("clientId"),
                clientSecret = properties.getProperty("clientSecret"),
                botUrl = properties.getProperty("botUrl"),
                identity = properties.getProperty("identity"),
                isWebHook = false,
                jwtServerUrl = properties.getProperty("jwtServerUrl"),
                enablePanel = true,
                jwtToken = properties.getProperty("jwtToken")
            )
        } catch (e: NotFoundException) {
            Log.e(BotApplication::class.java.simpleName, "Unable to find the config file: " + e.message)
        } catch (e: IOException) {
            Log.e(BotApplication::class.java.simpleName, "Failed to open config file.")
        }
        return null
    }
}