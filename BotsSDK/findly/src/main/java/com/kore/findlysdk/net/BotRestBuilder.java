package com.kore.findlysdk.net;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Jampana Sudheer on 18-09-2020.
 */

public class BotRestBuilder {

    private static BotRestAPI botRestBuilder;

    private BotRestBuilder(){}

    public static BotRestAPI getBotJWTRestAPI(){
        if(botRestBuilder == null) {
            botRestBuilder = new Retrofit.Builder()
                    .baseUrl(SDKConfiguration.Server.KORE_BOT_SERVER_URL)
                    .addConverterFactory(createConverter())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(getClient())
                    .build().create(BotRestAPI.class);
        }
        return botRestBuilder;
    }
    private static OkHttpClient getClient(){
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .addInterceptor(interceptor)
                .build();
//        client.interceptors().add(KoreRequestInterceptor.getInstance(mContext));
        return client;
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
}
