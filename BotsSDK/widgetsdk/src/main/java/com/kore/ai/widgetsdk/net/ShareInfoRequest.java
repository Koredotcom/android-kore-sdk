package com.kore.ai.widgetsdk.net;

import com.kore.ai.widgetsdk.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;


/**
 * Created by Shiva Krishna on 2/23/2018.
 */

public class ShareInfoRequest {

    protected final String userId;
    protected final String kid;
    protected final HashMap<String,ArrayList<KaRestResponse.SharedList>> payload;
    private final boolean notify;
    private final int methodType;
    private final String accessToken;
    private final Callback<KaRestResponse.ShareResponse> callback;


    public ShareInfoRequest(String accessToken, String userId, String kid, boolean notify, int methodType,
                            HashMap<String,ArrayList<KaRestResponse.SharedList>> payload, Callback<KaRestResponse.ShareResponse> callback) {
        this.userId = userId;
        this.kid = kid;
        this.accessToken = accessToken;
        this.payload = payload;
        this.notify = notify;
        this.methodType = methodType;
        this.callback = callback;
    }

    public void loadDataFromNetwork() {
        Call<KaRestResponse.ShareResponse> request;
        switch (methodType) {
            case 1:
                request = KaRestBuilder.getKaRestAPI().shareInfo(Utils.ah(accessToken), userId, kid, notify, payload);
                break;
            case 2:
                request = KaRestBuilder.getKaRestAPI().updateSharePrivilege(Utils.ah(accessToken), userId, kid, payload);
                break;
            case 3:
                request = KaRestBuilder.getKaRestAPI().unShareArticle(Utils.ah(accessToken), userId, kid);
                break;
            case 4:
                request = KaRestBuilder.getKaRestAPI().getSharedUsers(Utils.ah(accessToken), userId, kid);
                break;
            default:
                request = KaRestBuilder.getKaRestAPI().shareInfo(Utils.ah(accessToken), userId, kid, notify, payload);
                break;

        }

        KaRestAPIHelper.enqueueWithRetry(request, callback);

    }

}