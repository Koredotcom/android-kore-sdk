package com.kore.uploadfile.configurations

object FileUploadEndPoints {
    /**
     * End points for logged in users
     */
    @JvmField
    var FILE_TOKEN = "/api/1.1/users/%s/file/token"

    @JvmField
    var CHUNK_UPLOAD = "/api/1.1/users/%s/file/%s/chunk"

    @JvmField
    var MERGE_FILE = "/api/1.1/users/%s/file/%s"
    /**
     * End points for anonymous user
     */
    @JvmField
    var ANONYMOUS_FILE_TOKEN = "/api/1.1/attachment/file/token"
    @JvmField
    var ANONYMOUS_CHUNK_UPLOAD = "/api/1.1/users/%s/file/%s/chunk"
    @JvmField
    var ANONYMOUS_MERGE = "/api/1.1/users/%s/file/%s"

    /**
     * End points for anonymous user webHook
     */
    @JvmField
    var WEBHOOK_ANONYMOUS_FILE_TOKEN = "api/attachments/%s/%s/token"
    @JvmField
    var WEBHOOK_ANONYMOUS_CHUNK_UPLOAD = "api/attachments/%s/%s/token/%s/chunk"
    @JvmField
    var WEBHOOK_ANONYMOUS_MERGE = "api/attachments/%s/ivr/token/%s"
}