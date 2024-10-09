package com.kore.widgets.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Param(
    @SerializedName("from")
    val from: String = "",
    @SerializedName("inputs")
    val inputs: Any = Any()
) : Serializable