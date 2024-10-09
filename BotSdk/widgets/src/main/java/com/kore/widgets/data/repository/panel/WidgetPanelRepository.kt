package com.kore.widgets.data.repository.panel

import com.kore.widgets.Result
import com.kore.widgets.model.Panel
import com.kore.widgets.model.WidgetInfoResponse

interface WidgetPanelRepository {
    suspend fun getWidgetPanelData(botId: String, token: String, identity: String): Result<List<Panel>?>

    suspend fun getWidgetInfo(url: String, token: String, input: HashMap<String, Any>): Result<WidgetInfoResponse>
}