package com.kore.network.api.service

import com.kore.model.BotMetaResponse
import com.kore.model.WebHookMessageResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface WebHookApi {

    @POST("chatbot/v2/webhook/{streamId}")
    suspend fun sendWebHookMessage(
        @Path("streamId") streamId: String,
        @Header("Authorization") token: String,
        @Body body: HashMap<String, Any?>
    ): Response<WebHookMessageResponse?>

    @GET("api/botmeta/{streamId}")
    suspend fun getWebHookBotMeta(@Header("Authorization") token: String?, @Path("streamId") streamId: String?): Response<BotMetaResponse?>

    @GET("chatbot/v2/webhook/{streamId}/poll/{pollId}")
    suspend fun getPollIdData(
        @Header("Authorization") token: String,
        @Path("streamId") streamId: String,
        @Path("pollId") pollId: String
    ): Response<WebHookMessageResponse?>
}