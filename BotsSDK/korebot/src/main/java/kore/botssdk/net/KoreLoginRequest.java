package kore.botssdk.net;

import android.content.Context;

import java.util.HashMap;

/**
 * Created by Pradeep Mahato on 30-May-16.
 */
public class KoreLoginRequest extends KoreRestRequest<KoreRestResponse.KoreLoginResponse> {

    protected HashMap<String, Object> userCredentials;
    private Context context;

    public KoreLoginRequest(Context context, HashMap<String, Object> userCredentials) {
        super(KoreRestResponse.KoreLoginResponse.class, "", "");
        this.userCredentials = userCredentials;
        this.context = context;
    }

    @Override
    public KoreRestResponse.KoreLoginResponse loadDataFromNetwork() throws Exception {
        KoreRestResponse.KoreLoginResponse resp = getService().loginUser(userCredentials);
        return resp;
    }

}
