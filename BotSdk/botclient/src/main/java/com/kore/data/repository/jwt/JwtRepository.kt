package com.kore.data.repository.jwt

import com.kore.common.Result
import com.kore.network.api.response.BotAuthorizationResponse
import com.kore.network.api.response.JwtTokenResponse
import com.kore.network.api.response.RtmUrlResponse

interface JwtRepository {
    fun generateJwt(identity: String, clientSecret: String, clientId: String): String
    fun generateJwtForAPI(identity: String, clientSecret: String, clientId: String): String
    suspend fun getJwtToken(url: String, clientId: String, clientSecret: String, identity: String): Result<JwtTokenResponse?>
    suspend fun getJwtGrant(jwtToken: HashMap<String, Any>): Result<BotAuthorizationResponse?>
    suspend fun getRtmUrl(token: String, optParameterBotInfo: HashMap<String, Any>): Result<RtmUrlResponse?>
    suspend fun getRtmUrl(token: String, botInfo: HashMap<String, Any>, isReconnect: Boolean): Result<RtmUrlResponse?>
}