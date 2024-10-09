package com.kore.network.api

import com.google.gson.Gson
import com.google.gson.JsonParseException
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import com.kore.common.SDKConfiguration
import okhttp3.OkHttpClient.Builder
import okhttp3.RequestBody
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.lang.reflect.Type
import java.util.concurrent.TimeUnit

/**
 * Copyright (c) 2023 Kore Inc. All rights reserved.
 */
class ApiClient private constructor() {
    private lateinit var okBuilder: Builder
    private lateinit var adapterBuilder: Retrofit.Builder

    companion object {
        private var instance: ApiClient? = null
        fun getInstance(): ApiClient {
            if (instance == null) {
                instance = ApiClient()
            }
            return instance!!
        }
    }

    init {
        createDefaultAdapter()
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        this.adapterBuilder.baseUrl(SDKConfiguration.getBotConfigModel()?.botUrl ?: "")
        this.okBuilder
            .addNetworkInterceptor(ApiCacheInterceptor())
            .addInterceptor(httpLoggingInterceptor)
            .connectTimeout(ApiConstant.API_TIME_OUT, TimeUnit.MILLISECONDS)
            .readTimeout(ApiConstant.API_TIME_OUT, TimeUnit.MILLISECONDS)
            .writeTimeout(ApiConstant.API_TIME_OUT, TimeUnit.MILLISECONDS)
    }

    private fun createDefaultAdapter() {
        val json = ApiJson()
        okBuilder = Builder()
        var baseUrl = "http://localhost:10010"
        if (!baseUrl.endsWith("/")) baseUrl = "$baseUrl/"
        adapterBuilder = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonCustomConverterFactory.create(json.gson))
    }

    fun <S> createService(serviceClass: Class<S>): S {
        return adapterBuilder
            .client(okBuilder.build())
            .build()
            .create(serviceClass)
    }

    fun <S> createService(serviceClass: Class<S>, onProgressUpdate: (Int) -> Unit): S {
        okBuilder.addInterceptor(ProgressInterceptor(onProgressUpdate))
        return adapterBuilder
            .client(okBuilder.build())
            .build()
            .create(serviceClass)
    }
}

/**
 * This wrapper is to take care of this case:
 * when the deserialization fails due to JsonParseException and the
 * expected type is String, then just return the body string.
 */
internal class GsonResponseBodyConverterToString<T>(private val gson: Gson, private val type: Type) : Converter<ResponseBody, T> {
    @Suppress("UNCHECKED_CAST")
    @Throws(IOException::class)
    override fun convert(value: ResponseBody): T {
        val returned = value.string()
        return try {
            gson.fromJson(returned, type)
        } catch (e: JsonParseException) {
            returned as T
        }
    }
}

internal class GsonCustomConverterFactory private constructor(gson: Gson?) : Converter.Factory() {
    private val gson: Gson
    private val gsonConverterFactory: GsonConverterFactory

    init {
        if (gson == null) throw NullPointerException("gson == null")
        this.gson = gson
        gsonConverterFactory = GsonConverterFactory.create(gson)
    }

    override fun responseBodyConverter(type: Type, annotations: Array<Annotation>, retrofit: Retrofit): Converter<ResponseBody, *>? {
        return if (type == String::class.java) GsonResponseBodyConverterToString<Any>(
            gson,
            type
        ) else gsonConverterFactory.responseBodyConverter(type, annotations, retrofit)
    }

    override fun requestBodyConverter(
        type: Type,
        parameterAnnotations: Array<Annotation>,
        methodAnnotations: Array<Annotation>,
        retrofit: Retrofit
    ): Converter<*, RequestBody>? {
        return gsonConverterFactory.requestBodyConverter(type, parameterAnnotations, methodAnnotations, retrofit)
    }

    companion object {
        fun create(gson: Gson): GsonCustomConverterFactory {
            return GsonCustomConverterFactory(gson)
        }
    }
}