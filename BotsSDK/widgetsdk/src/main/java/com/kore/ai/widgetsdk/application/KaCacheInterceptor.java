package com.kore.ai.widgetsdk.application;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Response;

////Reference from https://blog.mindorks.com/okhttp-interceptor-making-the-most-of-it
public class KaCacheInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Response response = chain.proceed(chain.request());
        CacheControl cacheControl = new CacheControl.Builder().maxAge(10, TimeUnit.DAYS).build();

        return response.newBuilder().header("Cache-Control", cacheControl.toString()).build();
    }
}