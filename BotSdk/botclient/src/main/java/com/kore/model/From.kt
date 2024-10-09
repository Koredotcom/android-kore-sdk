package com.kore.model

import com.google.gson.annotations.SerializedName

data class From(
    @SerializedName("id")
    var id: String? = null,
    @SerializedName("userInfo")
    var userInfo: WebHookUserInfo? = null
)