package kore.botssdk.net;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.concurrent.TimeUnit;

import kore.botssdk.net.SDKConfiguration.Server;
import okhttp3.Dispatcher;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestBuilder {

    private static RestAPI restAPI;
    private static Context mContext;

    private RestBuilder(){}

    public static RestAPI getRestAPI(){
        if(restAPI == null) {
            restAPI = new Retrofit.Builder()
                    .baseUrl(Server.KORE_BOT_SERVER_URL)
                    .addConverterFactory(new NullOnEmptyConverterFactory())
                    .addConverterFactory(createConverter())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(getClient())
                    .build().create(RestAPI.class);
        }
        return restAPI;
    }

    public static void setContext(Context context)
    {
        mContext = context;
    }

    private static OkHttpClient getClient(){
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        Dispatcher dispatcher = new Dispatcher();
        dispatcher.setMaxRequests(1);

//        if(SDKConfiguration.SSLConfig.isSSLEnable)
//        {
//            return new OkHttpClient.Builder()
//                    .connectTimeout(60, TimeUnit.SECONDS)
//                    .readTimeout(60, TimeUnit.SECONDS)
//                    .addInterceptor(interceptor)
//                    .dispatcher(dispatcher)
//                    .sslSocketFactory(SSLHelper.getSSLContextWithCertificate(mContext, SDKConfiguration.Server.KORE_BOT_SERVER_URL).getSocketFactory(), SSLHelper.systemDefaultTrustManager())
//                    //.interceptors(KoreRequestInterceptor.getInstance(getApplicationContext()))
//                    //.authenticator(new KoraRequestAuthenticator(KORestBuilder.mContext))
//                    .build();
//        }
//        else
//        {
            return new OkHttpClient.Builder()
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .readTimeout(60, TimeUnit.SECONDS)
                    .addInterceptor(interceptor)
                    .dispatcher(dispatcher)
//                    .interceptors(KoreRequestInterceptor.getInstance(getApplicationContext()))
                    //.authenticator(new KoraRequestAuthenticator(KORestBuilder.mContext))
                    .build();
//        }
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
                value = json.getAsInt() > 0;
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
