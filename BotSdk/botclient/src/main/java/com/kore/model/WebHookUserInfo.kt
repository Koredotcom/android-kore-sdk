package com.kore.model

import com.google.gson.annotations.SerializedName

data class WebHookUserInfo(
    @SerializedName("firstName")
    private val firstName: String? = null,
    @SerializedName("lastName")
    private val lastName: String? = null,
    @SerializedName("email")
    private val email: String? = null
)