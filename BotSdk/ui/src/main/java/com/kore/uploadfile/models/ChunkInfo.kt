package com.kore.uploadfile.models

data class ChunkInfo(
    val id: Int = 0,
    var number: Int = 0,
    var size: Int = 0,
    var offset: Int = 0,
    var isUploaded: Boolean = false,
    var fileName: String? = null
)