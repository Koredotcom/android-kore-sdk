package com.kore.common

import com.kore.common.model.BotConfigModel

object SDKConfiguration {
    private var botConfigModel: BotConfigModel? = null
    private var loginToken: String? = null
    private var queryParams: HashMap<String, Any>? = null
    private var customData: HashMap<String, Any>? = null

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
}