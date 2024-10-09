package com.kore.network.api.response

data class JwtTokenResponse (
    val jwt: String? = null,
    val botId: String? = null,
    val botName: String? = null,
    val botsUrl: String? = null,
    val ip: String? = null,
    val taskId: String? = null,
    val clientId: String? = null,
    val clientSecret: String? = null
)