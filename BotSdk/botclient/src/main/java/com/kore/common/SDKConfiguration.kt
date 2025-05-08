package com.kore.common

import com.kore.common.model.BotConfigModel
import com.kore.network.api.responsemodels.branding.BotBrandingModel

object SDKConfiguration {
    private var botConfigModel: BotConfigModel? = null
    private var loginToken: String? = null
    private var queryParams: HashMap<String, Any>? = null
    private var customData: HashMap<String, Any>? = null
    private var botBrandingModel: BotBrandingModel? = null
    private var socketConnectionMode: String? = null
    private var isShowActionBar = true

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

    @JvmStatic
    fun initialize(botConfigModel: BotConfigModel) {
        this.botConfigModel = botConfigModel
    }

    @JvmStatic
    fun getBotConfigModel(): BotConfigModel? = botConfigModel

    @JvmStatic
    fun setLoginToken(token: String) {
        loginToken = token
    }

    @JvmStatic
    fun getLoginToken(): String? = loginToken

    @JvmStatic
    fun setQueryParams(queryParams: HashMap<String, Any>) {
        this.queryParams = queryParams
    }

    @JvmStatic
    fun getQueryParams(): HashMap<String, Any>? = queryParams

    @JvmStatic
    fun setCustomData(customData: HashMap<String, Any>) {
        this.customData = customData
    }

    @JvmStatic
    fun getCustomData(): HashMap<String, Any>? = customData

    @JvmStatic
    fun setBotBrandingConfig(botBrandingModel: BotBrandingModel?) {
        this.botBrandingModel = botBrandingModel
    }

    @JvmStatic
    fun getBotBrandingConfig(): BotBrandingModel? = botBrandingModel

    @JvmStatic
    fun setConnectionMode(connectionMode: String) {
        socketConnectionMode = "&ConnectionMode=$connectionMode"
    }

    @JvmStatic
    fun getConnectionMode(): String? = socketConnectionMode

    @JvmStatic
    fun isIsShowActionBar(): Boolean = isShowActionBar

    @JvmStatic
    fun setIsShowActionBar(isShow: Boolean) {
        isShowActionBar = isShow
    }
}