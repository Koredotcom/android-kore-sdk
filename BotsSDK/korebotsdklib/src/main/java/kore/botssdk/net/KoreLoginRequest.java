package kore.botssdk.net;

import android.content.Context;

import java.util.HashMap;

import kore.botssdk.models.KoreLoginResponse;

/**
 * Created by Ramachandra Pradeep on 1/9/2017.
 */
public class KoreLoginRequest extends RestRequest<KoreLoginResponse> {

    protected HashMap<String, Object> userCredentials;

    public KoreLoginRequest(HashMap<String, Object> userCredentials) {
        super(KoreLoginResponse.class, "", "");
        this.userCredentials = userCredentials;
    }

    @Override
    public KoreLoginResponse loadDataFromNetwork() throws Exception {
        KoreLoginResponse resp = getService().loginNormalUser(userCredentials);
        return resp;
    }

}
