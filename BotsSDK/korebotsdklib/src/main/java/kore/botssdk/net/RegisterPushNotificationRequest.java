package kore.botssdk.net;

import java.util.HashMap;

import retrofit.client.Response;

/**
 * Created by Pradeep Mahato on 08-Jun-16.
 * Copyright (c) 2014 Kore Inc. All rights reserved.
 */
public class RegisterPushNotificationRequest extends RestRequest<Response> {

    private HashMap<String, Object> request;

    public RegisterPushNotificationRequest(String userId, String accessToken, HashMap<String, Object> request) {
        super(Response.class, userId, accessToken);
        this.request = request;
    }

    @Override
    public Response loadDataFromNetwork() throws Exception {
        Response response = getService().subscribeForPushNotification(userId, accessTokenHeader(), request);
        return response;
    }
}
