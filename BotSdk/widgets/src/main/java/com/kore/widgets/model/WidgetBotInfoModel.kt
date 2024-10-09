package com.kore.widgets.model

import com.google.gson.annotations.SerializedName

/**
 * Copyright (c) 2023 Kore Inc. All rights reserved.
 */
data class WidgetBotInfoModel(
    @SerializedName("chatBot")
    val botName: String,
    @SerializedName("taskBotId")
    val botId: String,
    val customData: Map<String, String?>?,
    val channelClient: String = "Android"
)