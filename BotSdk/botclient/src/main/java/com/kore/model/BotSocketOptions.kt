package com.kore.model

import java.net.URI

data class BotSocketOptions(private val protocol: String, private val port: Int, private val host: String) {
    fun replaceOptions(url: String, options: BotSocketOptions): String = try {
        val oldUri = URI(url)
        val uri = URI(
            options.protocol.ifEmpty { oldUri.scheme },
            oldUri.userInfo,
            options.host.ifEmpty { oldUri.host },
            if (options.port != -1) options.port else oldUri.port, oldUri.path, oldUri.query, null
        )
        uri.toString()
    } catch (e: Exception) {
        e.printStackTrace()
        url
    }
}
