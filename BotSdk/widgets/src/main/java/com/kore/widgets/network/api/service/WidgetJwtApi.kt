package com.kore.widgets.network.api.service

import com.kore.widgets.model.WidgetJwtTokenResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.HeaderMap
import retrofit2.http.POST
import retrofit2.http.Url

/**
 * Copyright (c) 2023 Kore Inc. All rights reserved.
 */
interface WidgetJwtApi {
    @POST
    suspend fun getJwtToken(
        @Url url: String,
        @HeaderMap headers: HashMap<String, Any>?,
        @Body body: HashMap<String, Any>
    ): Response<WidgetJwtTokenResponse?>
}