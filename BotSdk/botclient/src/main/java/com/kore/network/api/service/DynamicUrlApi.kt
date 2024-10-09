package com.kore.network.api.service

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Streaming
import retrofit2.http.Url

/**
 * Copyright (c) 2023 Kore Inc. All rights reserved.
 */
interface DynamicUrlApi {
    @GET
    @Streaming
    suspend fun downloadFile(@Url url: String): Response<ResponseBody>
}