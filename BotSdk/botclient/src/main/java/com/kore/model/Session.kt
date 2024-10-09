package com.kore.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Session(
    @SerializedName("new")
    @Expose
    private val newSession: Boolean = false
)