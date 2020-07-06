package com.kore.ai.widgetsdk.net;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.kore.ai.widgetsdk.koranet.KORestBuilder;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.concurrent.TimeUnit;

import okhttp3.Dispatcher;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Ramachandra Pradeep on 27-Oct-18.
 */

public class KaRestBuilder {
    private static KaRestAPI kaRestAPI;

    private KaRestBuilder(){}


    public static KaRestAPI getKaRestAPI(){
        if(kaRestAPI == null) {
            kaRestAPI = new Retrofit.Builder()
                    .baseUrl(SDKConfiguration.Server.SERVER_URL)
                    .addConverterFactory(new NullOnEmptyConverterFactory())
                    .addConverterFactory(createConverter())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(getClient())
                    .build().create(KaRestAPI.class);
        }
        return kaRestAPI;
    }

    public static KaRestAPI getWidgetKaRestAPI(){
        if(kaRestAPI == null) {
            kaRestAPI = new Retrofit.Builder()
                    .baseUrl(SDKConfiguration.Server.WIDGETS_SERVER_URL)
                    .addConverterFactory(new NullOnEmptyConverterFactory())
                    .addConverterFactory(createConverter())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(getClient())
                    .build().create(KaRestAPI.class);
        }
        return kaRestAPI;
    }

    private static OkHttpClient getClient(){
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        Dispatcher dispatcher = new Dispatcher();
        dispatcher.setMaxRequests(1);

        //        client.interceptors().add(KoreRequestInterceptor.getInstance(mContext));
        return new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .addInterceptor(interceptor)
                .dispatcher(dispatcher)
                .authenticator(new KoraRequestAuthenticator(KORestBuilder.mContext))
                .build();
    }

    private static GsonConverterFactory createConverter() {
        final GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(boolean.class, new BooleanDeserializer());
        gsonBuilder.excludeFieldsWithModifiers(Modifier.STATIC, Modifier.TRANSIENT);
        final Gson gson = gsonBuilder.create();
        return GsonConverterFactory.create(gson);
    }

    static class BooleanDeserializer implements JsonDeserializer {

        @Override
        public Object deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws com.google.gson.JsonParseException {
            boolean value;
            try {
                value = json.getAsInt() > 0 ? true : false;
            } catch (NumberFormatException ex) {
                value = json.getAsBoolean();
            }
            return value;
        }
    }

    static class NullOnEmptyConverterFactory extends Converter.Factory {

        @Override
        public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
            final Converter<ResponseBody, ?> delegate = retrofit.nextResponseBodyConverter(this, type, annotations);
            return new Converter<ResponseBody, Object>() {
                @Override
                public Object convert(ResponseBody body) throws IOException {
                    if (body.contentLength() == 0) return null;
                    return delegate.convert(body);                }
            };
        }
    }


}
