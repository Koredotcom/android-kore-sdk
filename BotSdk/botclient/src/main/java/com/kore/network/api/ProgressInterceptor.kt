package com.kore.network.api

import okhttp3.Interceptor
import okhttp3.Response

class ProgressInterceptor(private val onProgressUpdate: (progress: Int, downloadedByte: Int) -> Unit) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalResponse = chain.proceed(chain.request())
        // Wrap the response body with our ProgressResponseBody
        return originalResponse.newBuilder()
            .body(ProgressResponseBody(originalResponse.body!!, onProgressUpdate))
            .build()
    }
}