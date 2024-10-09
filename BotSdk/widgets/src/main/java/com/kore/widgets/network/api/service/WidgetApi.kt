package com.kore.widgets.network.api.service

import com.kore.widgets.model.Panel
import com.kore.widgets.model.WidgetInfoResponse
import com.kore.widgets.model.WidgetsModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.QueryMap
import retrofit2.http.Url

/**
 * Copyright (c) 2023 Kore Inc. All rights reserved.
 */
interface WidgetApi {
    @GET("/widgetsdk/{clientId}/panels?resolveWidgets=true")
    suspend fun getWidgetPanelData(
        @Path("clientId") clientId: String?,
        @Header("Authorization") token: String?,
        @Query("from") identity: String?
    ): Response<List<Panel>?>

    @POST
    suspend fun getWidgetInfo(
        @Url url: String,
        @Header("Authorization") token: String,
        @QueryMap(encoded = true) param: HashMap<String, Any>
    ): Response<WidgetInfoResponse>
}