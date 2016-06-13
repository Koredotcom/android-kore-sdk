package kore.botssdk.net;

import android.content.Context;

import java.util.HashMap;

/**
 * Created by Pradeep Mahato on 30-May-16.
 * Copyright (c) 2014 Kore Inc. All rights reserved.
 */
public class LoginRequest extends RestRequest<RestResponse.LoginResponse> {

    protected HashMap<String, Object> userCredentials;
    private Context context;

    public LoginRequest(Context context, HashMap<String, Object> userCredentials) {
        super(RestResponse.LoginResponse.class, "", "");
        this.userCredentials = userCredentials;
        this.context = context;
    }

    @Override
    public RestResponse.LoginResponse loadDataFromNetwork() throws Exception {
        RestResponse.LoginResponse resp = getService().loginUser(userCredentials);
        return resp;
    }

}
