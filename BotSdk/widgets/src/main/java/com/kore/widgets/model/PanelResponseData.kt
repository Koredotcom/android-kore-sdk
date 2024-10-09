package com.kore.widgets.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class PanelResponseData(
    @SerializedName("panels")
    @Expose
    private val panels: List<Panel>? = null
) : Serializable