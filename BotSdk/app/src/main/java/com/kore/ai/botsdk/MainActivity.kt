package com.kore.ai.botsdk

import android.content.res.Resources.NotFoundException
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import com.kore.ai.botsdk.databinding.ActivityMainBinding
import com.kore.botclient.BotClient
import com.kore.botclient.BotConnectionListener
import com.kore.botclient.BotRequestState
import com.kore.botclient.ConnectionState
import com.kore.common.SDKConfiguration
import com.kore.common.model.BotConfigModel
import com.kore.common.utils.ToastUtils
import com.kore.model.BotRequest
import java.io.IOException
import java.util.Locale
import java.util.TimeZone

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    companion object {
        private val LOG_TAG = MainActivity::class.java.simpleName
    }

    private val connectionListener = object : BotConnectionListener {
        override fun onBotResponse(response: String?) {
            response?.let {
                if (it.contains("bot_response")) {
                    binding.response.text = response
                }
            }
        }

        override fun onConnectionStateChanged(state: ConnectionState, isReconnection: Boolean) {
            binding.launchBotBtn.isClickable = state != ConnectionState.CONNECTING
            binding.launchBotBtn.isEnabled = state != ConnectionState.CONNECTING
            binding.launchBotBtn.text = if (state == ConnectionState.CONNECTED) "Disconnect bot" else "Connect to bot"
            binding.message.isVisible = state == ConnectionState.CONNECTED
            binding.sendMessage.isVisible = state == ConnectionState.CONNECTED
            binding.response.text = ""
            binding.status.text = when (state) {
                ConnectionState.CONNECTED -> "Connected"
                ConnectionState.CONNECTING -> "Connecting"
                else -> "Disconnected"
            }
        }

        override fun onBotRequest(code: BotRequestState, botRequest: BotRequest) {
        }

        override fun onAccessTokenReady() {
        }

        override suspend fun onAccessTokenGenerated(token: String) {
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (BotClient.isConnected()) BotClient.getInstance().disconnectBot()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.activity_main, null, false)
        setContentView(binding.root)

        getBotConfigModel()?.let { SDKConfiguration.initialize(it) }
        BotClient.getInstance().setListener(connectionListener)
//        SDKConfiguration.OverrideKoreConfig.isEmojiShortcutEnable = false
        // Adding user owned Query params
//        SDKConfiguration.setQueryParams(getQueryParams())
        // Adding user owned custom data
//        SDKConfiguration.setCustomData(getCustomData())
        // Set client required connection mode(Like: Start_New, Start_New_Resume_Agent or Default) for the socket connection
//        SDKConfiguration.setConnectionMode("Default")

        binding.launchBotBtn.setOnClickListener {
            if (BotClient.getInstance().getConnectionState() == ConnectionState.CONNECTED) {
                BotClient.getInstance().disconnectBot()
            } else if (BotClient.getInstance().getConnectionState() == ConnectionState.DISCONNECTED) {
                BotClient.getInstance().connectToBot(this, true)
            }
        }
        binding.sendMessage.setOnClickListener {
            if (binding.message.text.trim().isNotEmpty()) {
                BotClient.getInstance().sendMessage(binding.message.text.trim().toString(), null)
                binding.response.text = ""
            } else
                ToastUtils.showToast(this, "Enter a message to send to bot")
        }
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
}