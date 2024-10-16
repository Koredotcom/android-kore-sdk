package com.kore.ai.widgetsdk.koranet;


import android.annotation.SuppressLint;
import android.content.Context;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


@SuppressLint("UnknownNullness")
public final class KORestBuilder {

    public static final byte BUILDER_TYPE_BOT_REST = 110;
    public static final byte BUILDER_TYPE_KORA_REST = 111;
    private static Retrofit instanceKora;
    private static Retrofit instanceBot;
    private static KORestAPI serviceKora;
    public static Context mContext;

    private KORestBuilder(){}

    private static Retrofit getBuilder(byte builderType, String URL) {
        synchronized (KORestBuilder.class) {
            if (instanceKora == null) {
                instanceKora = new Retrofit.Builder()
                        .baseUrl(URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .client(getSSLPinnedClient(URL))
                        .build();
            }
            return instanceKora;
        }
    }

    public void init(Context mContext){
        KORestBuilder.mContext = mContext;
    }

    private static OkHttpClient getSSLPinnedClient(String URL){
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
//        SSLContext context = SSLHelper.getSSLContextWithCertificate(mContext, URL);
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .addInterceptor(interceptor)
//        .sslSocketFactory(context.getSocketFactory())
                .build();
//        client.interceptors().add(KoreRequestInterceptor.getInstance(mContext));
        return client;
    }
}
