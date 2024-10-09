package com.kore.widgets.model

data class WidgetConfigModel(
    val botName: String,
    val botId: String,
    val clientId: String,
    val clientSecret: String,
    val botUrl: String,
    val identity: String,
    val isJwtWithUrl: Boolean,
    val jwtServerUrl: String,
)
