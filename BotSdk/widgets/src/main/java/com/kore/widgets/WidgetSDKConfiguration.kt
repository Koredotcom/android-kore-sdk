package com.kore.widgets

import com.kore.widgets.model.WidgetConfigModel

object WidgetSDKConfiguration {
    private var botConfigModel: WidgetConfigModel? = null

    fun initialize(botConfigModel: WidgetConfigModel) {
        this.botConfigModel = botConfigModel
    }

    fun getWidgetConfigModel(): WidgetConfigModel? = botConfigModel
}