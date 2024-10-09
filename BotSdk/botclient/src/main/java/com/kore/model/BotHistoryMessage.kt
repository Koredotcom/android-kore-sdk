package com.kore.model

import com.google.gson.annotations.SerializedName

data class BotHistoryMessage(
    @SerializedName("_id")
    var id: String?,
    @SerializedName("type")
    var type: String,
    @SerializedName("status")
    var status: String,
    @SerializedName("lmodifiedOn")
    var modifiedOn: String,
    @SerializedName("createdBy")
    var createdBy: String,
    @SerializedName("components")
    var components: List<Map<String, Any>>,
    @SerializedName("botId")
    var botId: String? = null,
    @SerializedName("orgId")
    var orgId: String? = null,
    @SerializedName("tN")
    var tN: String? = null,
    @SerializedName("createdOn")
    var createdOn: String? = null,
    @SerializedName("v")
    var v: Int? = null,
    @SerializedName("resourceid")
    var resourceId: String? = null,
    @SerializedName("lmodifiedBy")
    var modifiedBy: String? = null,
    @SerializedName("channels")
    var channels: List<Any>
)