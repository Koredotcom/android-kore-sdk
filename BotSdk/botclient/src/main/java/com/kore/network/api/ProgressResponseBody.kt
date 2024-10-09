package com.kore.network.api

import okhttp3.MediaType
import okhttp3.ResponseBody
import okio.*
import java.io.IOException

class ProgressResponseBody(
    private val responseBody: ResponseBody,
    private val onProgressUpdate: (progress: Int) -> Unit
) : ResponseBody() {

    private var bufferedSource: BufferedSource? = null

    override fun contentType(): MediaType? {
        return responseBody.contentType()
    }

    override fun contentLength(): Long {
        return responseBody.contentLength()
    }

    override fun source(): BufferedSource {
        if (bufferedSource == null) {
            bufferedSource = source(responseBody.source()).buffer()
        }
        return bufferedSource!!
    }

    private fun source(source: Source): Source {
        return object : ForwardingSource(source) {
            var totalBytesRead: Long = 0
            var lastProgress = 0

            @Throws(IOException::class)
            override fun read(sink: Buffer, byteCount: Long): Long {
                val bytesRead = super.read(sink, byteCount)
                totalBytesRead += if (bytesRead != -1L) bytesRead else 0

                // Calculate progress and call the update callback
                val contentLength = responseBody.contentLength()
                if (contentLength > 0) {  // Prevent division by zero
                    val progress = ((totalBytesRead * 100) / contentLength).toInt()
                    if (progress != lastProgress && progress < 100) {
                        lastProgress = progress
                        onProgressUpdate(progress)  // Update progress
                    }
                }
                return bytesRead
            }
        }
    }
}