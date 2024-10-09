package com.kore.widgets.model

import com.google.gson.annotations.SerializedName

data class WidgetInfoResponse(
    @SerializedName("data")
    val data: List<WidgetInfoModel>
)