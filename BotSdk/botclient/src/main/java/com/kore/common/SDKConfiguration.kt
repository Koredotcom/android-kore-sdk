package com.kore.common

import com.kore.common.model.BotConfigModel

object SDKConfiguration {
    private var botConfigModel: BotConfigModel? = null
    private var loginToken: String? = null

    fun initialize(botConfigModel: BotConfigModel) {
        this.botConfigModel = botConfigModel
    }

    fun getBotConfigModel(): BotConfigModel? = botConfigModel

    fun setLoginToken(token: String) {
        loginToken = token
    }

    fun getLoginToken(): String? = loginToken
}