package com.kore.ai.botsdk

import android.content.Context
import android.content.res.Resources.NotFoundException
import android.os.Bundle
import android.util.Log
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.gson.Gson
import com.kore.SDKConfig
import com.kore.ai.botsdk.databinding.FragmentActivityBinding
import com.kore.ai.botsdk.row.DownloadLinkTemplateProvider
import com.kore.ai.botsdk.row.DownloadLinkTemplateRow
import com.kore.common.SDKConfiguration
import com.kore.common.model.BotConfigModel
import com.kore.common.utils.NetworkUtils
import com.kore.listeners.BotChatCloseListener
import com.kore.network.api.responsemodels.branding.BotBrandingModel
import com.kore.ui.botchat.BotChatFragment
import java.io.IOException
import java.io.InputStreamReader
import java.util.Locale
import java.util.TimeZone

class SampleFragmentActivity : AppCompatActivity(), BotChatCloseListener {
    private lateinit var binding: FragmentActivityBinding

    companion object {
        private val LOG_TAG = SampleFragmentActivity::class.java.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_activity, null, false)
        setContentView(binding.root)

        getBotConfigModel()?.let { SDKConfig.initialize(it) }
        SDKConfiguration.OverrideKoreConfig.isEmojiShortcutEnable = false
        // Adding user owned Query params
//        SDKConfig.setQueryParams(getQueryParams())
        // Adding user owned custom data
//        SDKConfig.setCustomData(getCustomData())

        // New template adding here
        SDKConfig.addCustomTemplate("link", "link", DownloadLinkTemplateProvider(), DownloadLinkTemplateRow::class)
//        SDKConfig.setIsShowIconTop(false)
        // Replacing existing template(Button template) with user owned template
//        SDKConfig.addCustomTemplate(BotChatRowType.ROW_BUTTON_PROVIDER, BotResponseConstants.TEMPLATE_TYPE_BUTTON, SampleTemplateProvider(), SampleTemplateRow::class)
//        SDKConfig.addCustomTemplate(BotResponseConstants.COMPONENT_TYPE_VIDEO, BotResponseConstants.COMPONENT_TYPE_VIDEO, DownloadLinkTemplateProvider(), DownloadLinkTemplateRow::class)
//        SDKConfig.addCustomTemplate(BotResponseConstants.TABLE_VIEW_RESPONSIVE, BotResponseConstants.TABLE_VIEW_RESPONSIVE, DownloadLinkTemplateProvider(), DownloadLinkTemplateRow::class)
//        SDKConfig.addCustomTemplate(BotResponseConstants.PIE_TYPE_DONUT, BotResponseConstants.PIE_TYPE_DONUT, DownloadLinkTemplateProvider(), DownloadLinkTemplateRow::class)
//        SDKConfig.addCustomTemplate(BotResponseConstants.BAR_CHART_DIRECTION_VERTICAL, BotResponseConstants.BAR_CHART_DIRECTION_VERTICAL, DownloadLinkTemplateProvider(), DownloadLinkTemplateRow::class)
//        SDKConfig.addCustomTemplate(BotResponseConstants.CAROUSEL_STACKED, BotResponseConstants.CAROUSEL_STACKED, DownloadLinkTemplateProvider(), DownloadLinkTemplateRow::class)
        // Adding user custom Header fragment
//        SDKConfig.addCustomHeaderFragment(BotResponseConstants.HEADER_SIZE_COMPACT, CustomHeaderFragment())
        // Adding user custom Content fragment
//        SDKConfig.addCustomContentFragment(CustomContentFragment())
        // Adding user custom Footer fragment
//        SDKConfig.addCustomFooterFragment(CustomFooterFragment())
        // Set client defined V3 branding configuration
//        SDKConfig.setBotBrandingConfig(getDefaultBrandingModel(this))
        // Set client required connection mode(Like: Start_New, Start_New_Resume_Agent or Default) for the socket connection
//        SDKConfig.setConnectionMode("Default")
        // To disable loading history when socket reconnected in case of socket disconnect and reconnect
//        SDKConfig.setHistoryOnNetworkResume(false)

        val fragmentTransaction = this.supportFragmentManager.beginTransaction()

        //Add Bot Content Fragment
        val botChatFragment = BotChatFragment()
        fragmentTransaction.add(R.id.flChatBot, botChatFragment).commit()

        botChatFragment.setBotChatCloseListener(this)

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (NetworkUtils.isNetworkAvailable(this@SampleFragmentActivity)) {
                    botChatFragment.showCloseAlert()
                }
            }
        })
    }

    private fun getDefaultBrandingModel(context: Context): BotBrandingModel? {
        val inputStream = context.resources.openRawResource(R.raw.config_branding_details)
        val reader = InputStreamReader(inputStream)
        val jsonText = reader.readText().also { reader.close() }
        return Gson().fromJson(jsonText, BotBrandingModel::class.java)
    }

    private fun getBotConfigModel(): BotConfigModel? {
        try {
            return BotConfigModel(
                botName = "Please enter Bot name",
                botId = "Please enter botID",
                clientId = "Please enter  clientId",
                clientSecret = "Please enter clientSecret",
                botUrl = "Please enter botUrl",
                identity = "Please enter identity",
                isWebHook = false,
                jwtServerUrl = "Please enter jwtServerUrl",
                enablePanel = true,
                jwtToken = "Please enter jwtToken"
            )
        } catch (e: NotFoundException) {
            Log.e(LOG_TAG, "Unable to find the config file: " + e.message)
        } catch (e: IOException) {
            Log.e(LOG_TAG, "Failed to open config file.")
        }
        return null
    }

//    private fun getBotConfigModel(): BotConfigModel? {
//        try {
//            val rawResource = resources.openRawResource(R.raw.config)
//            val properties = Properties()
//            properties.load(rawResource)
//            return BotConfigModel(
//                botName = properties.getProperty("botName"),
//                botId = properties.getProperty("botId"),
//                clientId = properties.getProperty("clientId"),
//                clientSecret = properties.getProperty("clientSecret"),
//                botUrl = properties.getProperty("botUrl"),
//                identity = properties.getProperty("identity"),
//                isWebHook = false,
//                jwtServerUrl = properties.getProperty("jwtServerUrl"),
//                enablePanel = false,
//                jwtToken = properties.getProperty("jwtToken")
//            )
//        } catch (e: NotFoundException) {
//            Log.e(LOG_TAG, "Unable to find the config file: " + e.message)
//        } catch (e: IOException) {
//            Log.e(LOG_TAG, "Failed to open config file.")
//        }
//        return null
//    }
//
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

    override fun onBotChatClosed() {
        Log.d(SampleFragmentActivity::class.java.simpleName, "onBotChatClosed")
    }

    override fun onBotChatMinimized() {
        Log.d(SampleFragmentActivity::class.java.simpleName, "onBotChatMinimized")
    }
}