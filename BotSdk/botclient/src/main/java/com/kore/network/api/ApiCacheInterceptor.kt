package com.kore.network.api

import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import java.util.concurrent.TimeUnit

////Reference from https://blog.mindorks.com/okhttp-interceptor-making-the-most-of-it
class ApiCacheInterceptor : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val response: Response = chain.proceed(chain.request())
        val cacheControl: CacheControl = CacheControl.Builder().maxAge(10, TimeUnit.DAYS).build()
        return response.newBuilder().header("Cache-Control", cacheControl.toString()).build()
    }
}