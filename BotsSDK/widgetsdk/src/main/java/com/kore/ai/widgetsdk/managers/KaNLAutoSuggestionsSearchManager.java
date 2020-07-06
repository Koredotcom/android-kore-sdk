package com.kore.ai.widgetsdk.managers;/*
package com.kore.ai.koreassistant.managers;

import android.content.Context;

import com.google.gson.Gson;
import com.kore.ai.koreassistant.net.KaRestResponse;
import com.kore.ai.koreassistant.utils.StringUtils;
import com.kore.korelib.utils.KoreLogger;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import javax.net.ssl.HttpsURLConnection;

import kore.botssdk.net.SDKConfiguration;
import ssl.KoreHttpsUrlConnectionBuilder;

*/
/**
 * Created by Ramachandra Pradeep on 13-Feb-18.
 *//*


public class KaNLAutoSuggestionsSearchManager{

    private Context mContext;
    private String accessToken;
    private String URL;

    public KaNLAutoSuggestionsSearchManager(Context mContext, String accessToken, String Url){
        this.mContext = mContext;
        this.accessToken = accessToken;
        this.URL = SDKConfiguration.Server.SERVER_URL +"/"+ Url;
    }


    public KaRestResponse.AutoSuggestionsResponse getAutoSuggestions(String filterString){

        String text = "";
        BufferedReader reader=null;
        HttpsURLConnection httpsURLConnection = null;
        String Url = URL+"?q="+filterString;
        KaRestResponse.AutoSuggestionsResponse resp = null;
        // Send data
        try{

            // Send POST data request
            KoreHttpsUrlConnectionBuilder koreHttpsUrlConnectionBuilder = new KoreHttpsUrlConnectionBuilder(mContext, Url);
//            koreHttpsUrlConnectionBuilder.pinKoreCertificateToConnection();
            httpsURLConnection = koreHttpsUrlConnectionBuilder.getHttpsURLConnection();

            httpsURLConnection.setRequestMethod("GET");
            httpsURLConnection.setConnectTimeout(60000);
            httpsURLConnection.setRequestProperty("Authorization","bearer "+accessToken);
            BufferedReader input = new BufferedReader(new InputStreamReader(httpsURLConnection.getInputStream()));
            for( int c = input.read(); c != -1; c = input.read() ) {
                text = text + (char)c;
            }
            input.close();
            httpsURLConnection.disconnect();
            if(!StringUtils.isNullOrEmpty(text)){
                resp = new Gson().fromJson(text, KaRestResponse.AutoSuggestionsResponse.class);
            }
        }
        catch(Exception ex){
            KoreLogger.debugLog("KaNLAutoSuggestionsSearchManager", ex.toString());
        }
        finally{
            try{reader.close();}catch(Exception ex) {}
        }

        return resp;

    }
}
*/
