package com.kore.network.api.response

class BotAuthorizationResponse(
    val authorization: AuthorizationResponse,
    val userInfo: BotUserInfoResponse
)