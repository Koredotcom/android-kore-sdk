package com.kore.uploadfile.models

data class MissingChunks(
    var errors: List<UploadError> = ArrayList()
)