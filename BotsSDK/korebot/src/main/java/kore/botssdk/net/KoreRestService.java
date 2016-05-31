package kore.botssdk.net;

import android.os.Build;

import com.octo.android.robospice.retrofit.RetrofitGsonSpiceService;

import kore.botssdk.R;
import kore.botssdk.Utils.Utils;
import kore.botssdk.services.KoreAuthErrorHandler;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;

/**
 * Created by Pradeep Mahato on 30-May-16.
 */
public class KoreRestService extends RetrofitGsonSpiceService{

    @Override
    public void onCreate() {
        super.onCreate();
        addRetrofitInterface(KoreRestAPI.class);
    }

    @Override
    protected String getServerUrl() {
        return ServerConfig.SERVER_URL;
    }


    @Override
    protected RestAdapter.Builder createRestAdapterBuilder() {
        KoreRestBuilderFactory builderFactory = new KoreRestBuilderFactory(getApplicationContext());
        RestAdapter.Builder builder = builderFactory.getRestAdapterBuilder();
        builder.setRequestInterceptor(new RequestInterceptor() {
            @Override
            public void intercept(RequestFacade request) {
                // TODO add any other generic headers required
                request.addHeader("User-Agent", getApplicationContext().getString(R.string.app_name)
                        + "/" + Utils.getBuildVersion(getApplicationContext())
                        + "(Android-"+ Build.VERSION.RELEASE + ")");
            }
        });
        builder.setErrorHandler(new KoreAuthErrorHandler(KoreRestService.this));
        builder.setLogLevel(RestAdapter.LogLevel.FULL);
        return builder;
    }
}
