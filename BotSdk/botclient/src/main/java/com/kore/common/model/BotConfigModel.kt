package com.kore.common.model

data class BotConfigModel(
    val botName: String,
    val botId: String,
    val clientId: String,
    val clientSecret: String,
    val botUrl: String,
    val identity: String,
    val isWebHook: Boolean,
    val jwtServerUrl: String,
    val jwtToken: String,
    val enablePanel: Boolean = false,
)
