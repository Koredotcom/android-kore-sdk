package com.kore.widgets.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Source(
    @SerializedName("type")
    val type: String? = null,
    @SerializedName("resource")
    val resource: String? = null
) : Serializable