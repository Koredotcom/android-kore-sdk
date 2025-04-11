package com.kore.network.api.service

import com.kore.network.api.response.BotAuthorizationResponse
import com.kore.network.api.response.JwtTokenResponse
import com.kore.network.api.response.RtmUrlResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query
import retrofit2.http.Url

/**
 * Copyright (c) 2023 Kore Inc. All rights reserved.
 */
interface JwtApi {
    @POST
    suspend fun getJwtToken(@Url url: String, @Body body: HashMap<String, Any>): Response<JwtTokenResponse?>

    //Getting jwt grant
    @POST("/api/oAuth/token/jwtgrant")
    suspend fun jwtGrant(@Body jwtToken: HashMap<String, Any>): Response<BotAuthorizationResponse>

    //Getting rtm URL
    @POST("/api/rtm/start")
    suspend fun getRtmUrl(@Header("Authorization") token: String, @Body optParameterBotInfo: HashMap<String, Any>): Response<RtmUrlResponse>

    //Getting rtm URL with reconnect
    @POST("/api/rtm/start")
    suspend fun getRtmUrl(
        @Header("Authorization") token: String,
        @Body optParameterBotInfo: HashMap<String, Any>,
        @Query("isReconnect") isReconnect: Boolean
    ): Response<RtmUrlResponse>
}