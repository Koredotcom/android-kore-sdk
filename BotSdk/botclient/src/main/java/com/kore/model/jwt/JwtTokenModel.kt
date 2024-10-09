package com.kore.model.jwt

data class JwtTokenModel (
    val jwt: String = "",
    val botId: String = "",
    val botName: String = "",
    val botsUrl: String = "",
    val ip: String = "",
    val taskId: String = "",
    val clientId: String = "",
    val clientSecret: String = ""
)