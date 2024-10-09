package com.kore.widgets.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class WidgetsModel(
    @SerializedName("login")
    @Expose
    val login: LoginModel? = null,

    @SerializedName("_id")
    val id: String,

    @SerializedName("refId")
    val refId: String? = null,

    @SerializedName("__v")
    val __v: Int = 0,

    @SerializedName("adminTaskStatus")
    val adminTaskStatus: String? = null,

    @SerializedName("approvalRequestedLanguages")
    val approvalRequestedLanguages: List<String>? = null,

    @SerializedName("approvedLanguages")
    val approvedLanguages: List<String>? = null,

    @SerializedName("authProfiles")
    val authProfiles: List<String>? = null,

    @SerializedName("autoRefresh")
    val autoRefresh: AutoRefresh,

    @SerializedName("param")
    val param: Param? = null,

    @SerializedName("cacheInterval")
    val cacheInterval: Int = 0,

    @SerializedName("contextMap")
    val contextMap: String? = null,

    @SerializedName("createdBy")
    val createdBy: String? = null,

    @SerializedName("createdOn")
    val createdOn: String? = null,

    @SerializedName("lMod")
    val lMod: String? = null,

    @SerializedName("lModBy")
    val lModBy: String? = null,

    @SerializedName("name")
    val name: String? = null,

    @SerializedName("source")
    val source: Source? = null,

    @SerializedName("state")
    val state: String? = null,

    @SerializedName("streamId")
    val streamId: String? = null,

    @SerializedName("templateType")
    val templateType: String? = null,

    @SerializedName("visibility")
    val visibility: Visibility? = null,

    @SerializedName("title")
    val title: String? = null,

    @SerializedName("description")
    val description: String? = null,

    @SerializedName("callbackURL")
    val callbackURL: String? = null,
) : Serializable