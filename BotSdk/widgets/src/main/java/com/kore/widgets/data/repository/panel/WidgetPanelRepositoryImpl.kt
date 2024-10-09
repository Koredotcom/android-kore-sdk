package com.kore.widgets.data.repository.panel

import com.kore.widgets.Result
import com.kore.widgets.model.Panel
import com.kore.widgets.model.WidgetInfoResponse
import com.kore.widgets.network.api.ApiClient
import com.kore.widgets.network.api.service.WidgetApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class WidgetPanelRepositoryImpl : WidgetPanelRepository {

    override suspend fun getWidgetPanelData(botId: String, token: String, identity: String): Result<List<Panel>?> {
        return withContext(Dispatchers.IO) {
            try {
                val widgetApi: WidgetApi = ApiClient.getInstance().createService(WidgetApi::class.java)
                val result = widgetApi.getWidgetPanelData(botId, "bearer $token", identity)
                if (result.isSuccessful) {
                    Result.Success(result.body() as List<Panel>)
                } else {
                    Result.Error(Exception("Something went wrong!"))
                }
            } catch (ex: Exception) {
                ex.printStackTrace()
                Result.Error(ex)
            }
        }
    }

    override suspend fun getWidgetInfo(url: String, token: String, input: HashMap<String, Any>): Result<WidgetInfoResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val widgetApi: WidgetApi = ApiClient.getInstance().createService(WidgetApi::class.java)
                val result = widgetApi.getWidgetInfo(url, "bearer $token", input)
                if (result.isSuccessful) {
                    Result.Success(result.body() as WidgetInfoResponse)
                } else {
                    Result.Error(Exception("Something went wrong!"))
                }
            } catch (ex: Exception) {
                ex.printStackTrace()
                Result.Error(ex)
            }
        }
    }
}