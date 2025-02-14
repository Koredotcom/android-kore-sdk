package com.kore.common

import com.kore.common.model.BotConfigModel
import com.kore.network.api.responsemodels.branding.BotBrandingModel

object SDKConfiguration {
    private var botConfigModel: BotConfigModel? = null
    private var loginToken: String? = null
    private var queryParams: HashMap<String, Any>? = null
    private var customData: HashMap<String, Any>? = null
    private var botBrandingModel: BotBrandingModel? = null

    object OverrideKoreConfig {
        var isEmojiShortcutEnable: Boolean = true
        var typingIndicatorTimeout: Int = 10000
        var historyEnable: Boolean = true
        var historyBatchSize: Int = 10
        var paginatedScrollEnable: Boolean = true
        var paginatedScrollBatchSize: Int = 10
        var paginatedScrollLoadingLabel: String? = "Loading old messages"
        var showIconTop: Boolean = true
        var showAttachment: Boolean = true
        var showASRMicroPhone: Boolean = true
        var showTextToSpeech: Boolean = true
        var showHamburgerMenu: Boolean = false
        var historyInitialCall: Boolean = false
    }

    fun initialize(botConfigModel: BotConfigModel) {
        this.botConfigModel = botConfigModel
    }

    fun getBotConfigModel(): BotConfigModel? = botConfigModel

    fun setLoginToken(token: String) {
        loginToken = token
    }

    fun getLoginToken(): String? = loginToken

    fun setQueryParams(queryParams: HashMap<String, Any>) {
        this.queryParams = queryParams
    }

    fun getQueryParams(): HashMap<String, Any>? = queryParams

    fun setCustomData(customData: HashMap<String, Any>) {
        this.customData = customData
    }

    fun getCustomData(): HashMap<String, Any>? = customData

    fun setBotBrandingConfig(botBrandingModel: BotBrandingModel?) {
        this.botBrandingModel = botBrandingModel
    }

    fun getBotBrandingConfig(): BotBrandingModel? = botBrandingModel
}