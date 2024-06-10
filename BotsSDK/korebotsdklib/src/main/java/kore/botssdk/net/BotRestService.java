/*
package kore.botssdk.net;

import android.os.Build;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.octo.android.robospice.retrofit.RetrofitGsonSpiceService;

import java.lang.reflect.Modifier;
import java.lang.reflect.Type;

import kore.botssdk.services.KoreAuthErrorHandler;
import kore.botssdk.utils.Utils;
import kore.korebotsdklib.R;

public class BotRestService */
/*extends RetrofitGsonSpiceService*//*
 {

   */
/* @Override
    public void onCreate() {
        super.onCreate();
        addRetrofitInterface(RestAPI.class);
    }

    @Override
    protected String getServerUrl() {
        return SDKConfiguration.Server.KORE_BOT_SERVER_URL;
    }

    @Override
    protected RestAdapter.Builder createRestAdapterBuilder() {
        RestAdapter.Builder builder = super.createRestAdapterBuilder();
        builder.setRequestInterceptor(new RequestInterceptor() {
            @Override
            public void intercept(RequestFacade request) {
                request.addHeader("User-Agent", getApplicationContext().getString(R.string.app_name)
                        + "/" + Utils.getBuildVersion(getApplicationContext())
                        + "(Android-" + Build.VERSION.RELEASE + ")");
            }
        });
        builder.setErrorHandler(new KoreAuthErrorHandler(BotRestService.this));
        builder.setLogLevel(RestAdapter.LogLevel.FULL);
        return builder;
    }

    @Override
    protected Converter createConverter() {
        final GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(boolean.class, new BooleanDeserializer());
        gsonBuilder.excludeFieldsWithModifiers(Modifier.STATIC, Modifier.TRANSIENT);
        final Gson gson = gsonBuilder.create();
        return new GsonConverter(gson);
    }

    class BooleanDeserializer implements JsonDeserializer {

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
    }*//*

}*/
