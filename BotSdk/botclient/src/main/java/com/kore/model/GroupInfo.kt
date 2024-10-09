package com.kore.model

import com.google.gson.annotations.SerializedName

data class GroupInfo(
    @SerializedName("id")
    private val id: String? = null,
    @SerializedName("name")
    private val name: String? = null
)