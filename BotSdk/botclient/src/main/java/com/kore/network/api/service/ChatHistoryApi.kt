package com.kore.network.api.service

import com.kore.model.BotHistory
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface ChatHistoryApi {
    @GET("/api/1.1/botmessages/rtm")
    suspend fun getBotHistory(
        @Header("Authorization") token: String,
        @Query("botId") botId: String,
        @Query("limit") limit: Int,
        @Query("offset") offset: Int,
        @Query("forward") forward: Boolean
    ): Response<BotHistory?>

    @GET("/api/chathistory/{streamId}/ivr?")
    suspend fun getWebHookBotHistory(
        @Header("Authorization") token: String,
        @Path("streamId") streamId: String,
        @Query("botId") botId: String,
        @Query("offset") offset: Int,
        @Query("limit") limit: Int
    ): Response<BotHistory?>
}