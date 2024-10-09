package com.kore.widgets.model

import com.google.gson.annotations.SerializedName

data class ImageModel(
    val imageType: String? = null,
    val namespace: String? = null,
    @SerializedName("image_src")
    val imageSrc: String? = null,
    val radius: Int = 0,
    val size: String? = null,
    val type: String? = null,
    val url: String? = null,
    val payload: String? = null,
    val utterance: String? = null
)