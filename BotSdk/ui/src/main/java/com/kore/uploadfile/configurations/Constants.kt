package com.kore.uploadfile.configurations

object Constants {
    const val UPLOAD_ERROR_CODE_404 = 404
    const val MERGE_RE_ATTEMPT_COUNT = 3
    const val CONNECTION_TIMEOUT = 2 * 60000
    const val CONNECTION_READ_TIMEOUT = 2 * 60000
    const val SERVER_ERROR = "ERROR WHILE CONTACTING WITH SERVER"
    const val NETWORK_FAILURE = "NOT_CONNECTED"
    const val MESSAGE_ID = "messageId"
    const val LOCAL_FILE_ID = "EMPTY_FILE_ID"
    var HTTP_AGENT: String? = null
    @JvmStatic
    val userAgent: String?
        get() {
            if (HTTP_AGENT == null) {
                HTTP_AGENT = System.getProperty("http.agent")
            }
            return HTTP_AGENT
        }
}