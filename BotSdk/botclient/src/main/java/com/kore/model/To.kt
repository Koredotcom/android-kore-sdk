package com.kore.model

import com.google.gson.annotations.SerializedName

data class To(
    @SerializedName("id")
    var id: String? = null,
    @SerializedName("groupInfo")
    var groupInfo: GroupInfo? = null
)