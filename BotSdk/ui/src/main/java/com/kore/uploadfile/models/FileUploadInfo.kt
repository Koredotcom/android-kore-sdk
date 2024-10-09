package com.kore.uploadfile.models

data class FileUploadInfo(
    var fileName: String? = null,
    var fileId: String? = null,
    var totalChunks: Int = 0,
    var uploadCount: Int = 0,
    var size: Long = 0,
    var isUploaded: Boolean = false,
    var isMerged: Boolean = false,
    var isMergeTriggered: Boolean = false,
    var filetype: String? = null,
    var fileExtension: String? = null,
    var isEncrypted: Boolean = false,
    var isRecorded: Boolean = false,
    var messageId: String? = null,
    var fileToken: String? = null,
    var fileContext: String? = null,
    var cookie: String? = null,
    var tokenExpiry: String? = null
)