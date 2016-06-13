package kore.botssdk.net;

import java.util.HashMap;

import retrofit.client.Response;

/**
 * Created by Pradeep Mahato on 08-Jun-16.
 * Copyright (c) 2014 Kore Inc. All rights reserved.
 */
public class UnSubscribePushNotificationRequest extends RestRequest<Response> {

    private HashMap<String, Object> request;

    public UnSubscribePushNotificationRequest(String accessToken, HashMap<String, Object> request) {
        super(Response.class, "", accessToken);
        this.request = request;
    }

    @Override
    public Response loadDataFromNetwork() throws Exception {
        Response response = getService().unSubscribeForPushNotification(accessTokenHeader(), request);
        return response;
    }
}
