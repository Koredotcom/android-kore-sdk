package com.kore.data.repository.converter

import com.kore.model.jwt.JwtTokenModel
import com.kore.network.api.response.JwtTokenResponse

fun JwtTokenResponse.toModel(): JwtTokenModel {
    val jwt = jwt
    val botId = botId
    val botName = botName
    val botsUrl = botsUrl
    val ip = ip
    val taskId = taskId
    val clientId = clientId
    val clientSecret = clientSecret
    return if (
        jwt != null &&
        botId != null &&
        botName != null &&
        botsUrl != null &&
        ip != null &&
        taskId != null &&
        clientId != null &&
        clientSecret != null
    ) {
        JwtTokenModel(
            jwt = jwt,
            botId = botId,
            botName = botName,
            botsUrl = botsUrl,
            ip = ip.toString(),
            taskId = taskId,
            clientId = clientId,
            clientSecret = clientSecret
        )
    } else {
        JwtTokenModel()
    }
}