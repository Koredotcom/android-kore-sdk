package com.kore.ai.botsdk

import android.app.Application
import android.content.res.Resources.NotFoundException
import android.util.Log
import com.kore.SDKConfig
import com.kore.ai.botsdk.fragments.CustomContentFragment
import com.kore.ai.botsdk.fragments.CustomFooterFragment
import com.kore.ai.botsdk.fragments.CustomHeaderFragment
import com.kore.ai.botsdk.row.DownloadLinkTemplateProvider
import com.kore.ai.botsdk.row.DownloadLinkTemplateRow
import com.kore.ai.botsdk.row.SampleTemplateProvider
import com.kore.ai.botsdk.row.SampleTemplateRow
import com.kore.common.model.BotConfigModel
import com.kore.model.constants.BotResponseConstants
import com.kore.ui.row.botchat.BotChatRowType
import com.kore.widgets.model.WidgetConfigModel
import java.io.IOException
import java.util.Locale
import java.util.Properties
import java.util.TimeZone

class BotApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        getBotConfigModel()?.let { SDKConfig.initialize(it) }
        getWidgetBotConfigModel()?.let { SDKConfig.setWidgetConfig(it) }
//        SDKConfig.setQueryParams(getQueryParams())
//        SDKConfig.setCustomData(getCustomData())

//        SDKConfig.addCustomTemplate("link", "link", DownloadLinkTemplateProvider(), DownloadLinkTemplateRow::class)
//        SDKConfig.addCustomTemplate(BotChatRowType.ROW_BUTTON_PROVIDER, BotResponseConstants.TEMPLATE_TYPE_BUTTON, SampleTemplateProvider(), SampleTemplateRow::class)
//        SDKConfig.addCustomHeaderFragment(BotResponseConstants.HEADER_SIZE_COMPACT, CustomHeaderFragment())
//        SDKConfig.addCustomContentFragment(CustomContentFragment())
//        SDKConfig.addCustomFooterFragment(CustomFooterFragment())
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

    private fun getWidgetBotConfigModel(): WidgetConfigModel? {
        try {
            val rawResource = resources.openRawResource(R.raw.config)
            val properties = Properties()
            properties.load(rawResource)
            return WidgetConfigModel(
                botName = properties.getProperty("widgetBotName"),
                botId = properties.getProperty("widgetBotId"),
                clientId = properties.getProperty("widgetClientId"),
                clientSecret = properties.getProperty("widgetClientSecret"),
                botUrl = properties.getProperty("widgetBotUrl"),
                identity = properties.getProperty("widgetIdentity"),
                jwtServerUrl = properties.getProperty("widgetJwtServerUrl"),
            )
        } catch (e: NotFoundException) {
            Log.e(BotApplication::class.java.simpleName, "Unable to find the config file: " + e.message)
        } catch (e: IOException) {
            Log.e(BotApplication::class.java.simpleName, "Failed to open config file.")
        }
        return null
    }

    private fun getQueryParams(): HashMap<String, Any> {
        val queryParams = HashMap<String, Any>()
        queryParams["q1"] = true
        queryParams["q2"] = 4
        queryParams["q3"] = "connect"
        return queryParams
    }

    private fun getCustomData(): HashMap<String, Any> {
        val customData: HashMap<String, Any> = HashMap()
        customData["name"] = "Kore Bot"
        customData["emailId"] = "emailId"
        customData["mobile"] = "mobile"
        customData["accountId"] = "accountId"
        customData["timeZoneOffset"] = -330
        customData["UserTimeInGMT"] = TimeZone.getDefault().id + " " + Locale.getDefault().isO3Language
        return customData
    }
}