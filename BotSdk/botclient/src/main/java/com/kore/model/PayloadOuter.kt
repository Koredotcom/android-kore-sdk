package com.kore.model

import com.google.gson.annotations.SerializedName

/**
 * Copyright (c) 2023 Kore Inc. All rights reserved.
 */
data class PayloadOuter(
    @SerializedName("type")
    var type: String? = null,
    @SerializedName("payload")
    var payload: HashMap<String, Any>? = null,
    @SerializedName("text")
    var text: String? = null,
    @SerializedName("speech_hint")
    val speechHint: String? = null,
    @SerializedName("template_type")
    val templateType: String? = null
)
