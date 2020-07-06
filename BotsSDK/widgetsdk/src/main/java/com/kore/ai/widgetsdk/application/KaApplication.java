package com.kore.ai.widgetsdk.application;

import android.app.Application;

import com.kore.ai.widgetsdk.utils.NetworkUtility;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import okhttp3.Cache;
import okhttp3.OkHttpClient;

public class KaApplication extends Application {


    private static Cache picassoDiskCache;

    @Override
    public void onCreate() {
        super.onCreate();
        setUpPicasso();
    }

    //Reference from  https://stackoverflow.com/questions/39988422/okhttpclient-cannot-resolve-method-setcache
    private void setUpPicasso() {
        if (picassoDiskCache == null) {
            File httpCacheDirectory = new File(getCacheDir(), "responses");
            int cacheSize = 10*1024*1024;
            picassoDiskCache = new Cache(httpCacheDirectory, cacheSize);

            OkHttpClient client = new OkHttpClient.Builder()
                    .addNetworkInterceptor(new KaCacheInterceptor())
                    .cache(picassoDiskCache)
                    .build();
            //Picasso picasso = new Picasso.Builder(context).downloader(new OkHttpDownloader(okClient)).build();
            Picasso picasso =  new Picasso.Builder(this).addRequestHandler(new KaSecurePicassoRequestHandler()).downloader(new OkHttp3Downloader(client)).build();
            Picasso.setSingletonInstance(picasso);
        }

    }


    public static void invalidatePicassoCache(String url) {
        try {
            Iterator<String> iterator = picassoDiskCache.urls();
            while (iterator.hasNext()) {
                String nextUrl = iterator.next();
                if (nextUrl.equalsIgnoreCase(url)) {
                    // This will invalidate disk cache
                    iterator.remove();
                    break;
                }
            }
        } catch (IOException e) {
//            KoreLogger.errorLog("KaApplication", "Error while clearing disk cache", e);
        }
        // This will invalidate memory cache
        if (url.startsWith(NetworkUtility.HTTP_SCHEME) ||
                url.startsWith(NetworkUtility.FILE_SCHEME)) {
            Picasso.get().invalidate(url);
        } else {
            Picasso.get().invalidate("file://" + url);
        }
    }
}
