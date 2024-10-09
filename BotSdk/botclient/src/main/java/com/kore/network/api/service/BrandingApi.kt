package com.kore.network.api.service

import com.kore.network.api.responsemodels.branding.BotActiveThemeModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface BrandingApi {
    // Get JWT Token
    @GET("api/websdkthemes/{botId}/activetheme")
    suspend fun getBrandingNewDetails(
        @Path("botId") bot_Id: String?,
        @Header("Authorization") token: String?,
        @Header("state") state: String?,
        @Header("Accepts-version") version: String?,
        @Header("Accept-Language") language: String?,
        @Header("botid") botId: String?,
        @Header("Content-Type") content: String? = "application/json"
    ): Response<BotActiveThemeModel?>
}