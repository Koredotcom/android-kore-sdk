package kore.botssdk.net;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.octo.android.robospice.retrofit.RetrofitGsonSpiceService;

import java.lang.reflect.Modifier;
import java.lang.reflect.Type;

import retrofit.RestAdapter;
import retrofit.converter.Converter;
import retrofit.converter.GsonConverter;

/**
 * Created by Pradeep Mahato on 30-May-16.
 */
public class KoreRestBuilderFactory extends RetrofitGsonSpiceService {

    private Context mContext;

    public KoreRestBuilderFactory(Context context)
    {
        this.mContext = context;
    }
    public RestAdapter.Builder getRestAdapterBuilder()
    {
        RestAdapter.Builder builder = super.createRestAdapterBuilder();

            return  builder;
    }

    /*private RestAdapter.Builder setSSLPinning(RestAdapter.Builder builder){
        SSLContext context = SSLHelper.getSSLContextWithCertificate(mContext, ServerConfig.SERVER_URL);
        OkHttpClient client = new OkHttpClient();
        client.setReadTimeout(60, TimeUnit.SECONDS);
        client.setConnectTimeout(60, TimeUnit.SECONDS);
        client.setSslSocketFactory(context.getSocketFactory());
        builder.setClient(new OkClient(client));
        return builder;
    }*/

    @Override
    protected String getServerUrl() {
        return ServerConfig.SERVER_URL;
    }

    @Override
    protected Converter createConverter() {
        final GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(boolean.class, new BooleanDeserializer());
        gsonBuilder.excludeFieldsWithModifiers(Modifier.STATIC, Modifier.TRANSIENT);
        final Gson gson = gsonBuilder.create();
        return  new GsonConverter(gson);
    }

    class BooleanDeserializer implements JsonDeserializer {

        @Override
        public Object deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws com.google.gson.JsonParseException {
            boolean value;
            try{
                value = json.getAsInt() > 0 ? true: false;
            }catch (NumberFormatException ex){
                value = json.getAsBoolean();
            }
            return value;
        }
    }

}