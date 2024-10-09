package com.kore.widgets.model

import com.google.gson.annotations.SerializedName

data class WidgetInfoModel(
    @SerializedName("templateType")
    val templateType: String? = null,

    @SerializedName("pie_type")
    val pieType: String? = null,

    @SerializedName("widgetName")
    val widgetName: String? = null,

    @SerializedName("description")
    val description: String? = null,

    @SerializedName("title")
    val title: String? = null,

    @SerializedName("formLink")
    val formLink: String? = null,

    @SerializedName("headerOptions")
    val headerOptions: HeaderOptionsModel? = null,

    @SerializedName("elements")
    val elements: List<Map<String, *>>? = null,

    @SerializedName("buttons")
    val buttons: List<ButtonModel>? = null,

    @SerializedName("login")
    val loginModel: LoginModel? = null
)
