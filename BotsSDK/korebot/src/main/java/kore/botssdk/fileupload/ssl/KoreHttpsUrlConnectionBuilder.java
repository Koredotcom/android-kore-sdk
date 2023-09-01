package kore.botssdk.fileupload.ssl;

import android.content.Context;

import java.net.URL;

import javax.net.ssl.HttpsURLConnection;


public class KoreHttpsUrlConnectionBuilder {
    private HttpsURLConnection httpsURLConnection;

    public KoreHttpsUrlConnectionBuilder(Context context, String uri){
        try {
            URL url = new URL(uri);
            httpsURLConnection = (HttpsURLConnection) url.openConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public HttpsURLConnection getHttpsURLConnection(){
        return httpsURLConnection;
    }
}
