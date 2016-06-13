package kore.botssdk.pushnotification;

import android.content.Context;
import android.provider.Settings;

import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.request.listener.RequestListener;

import java.util.HashMap;

import kore.botssdk.net.BaseSpiceManager;
import kore.botssdk.net.RegisterPushNotificationRequest;
import kore.botssdk.net.UnSubscribePushNotificationRequest;
import kore.botssdk.utils.Constants;
import retrofit.client.Response;

/**
 * Created by Pradeep Mahato on 08-Jun-16.
 * Copyright (c) 2014 Kore Inc. All rights reserved.
 */
public class PushNotificationRegistrar extends BaseSpiceManager {

    RequestListener<Response> requestListener;

    /**
     * @param requestListener : Callback for requests
     */
    public PushNotificationRegistrar(RequestListener<Response> requestListener) {
        this.requestListener = requestListener;
    }

    /**
     * Register for Push notification
     *
     * @param context
     * @param userId : UserId for whom push notification is required
     * @param accessToken : User's access token
     */
    public void registerPushNotification(Context context, String userId, String accessToken) {

        String deviceId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);

        HashMap<String, Object> pushNotificationRequestMap = new HashMap<>();
        pushNotificationRequestMap.put(Constants.PUSH_NOTIF_OS_TYPE, Constants.PUSH_NOTIF_OS_TYPE_ANDROID);
        pushNotificationRequestMap.put(Constants.PUSH_NOTIF_DEVICE_ID, deviceId);

        RegisterPushNotificationRequest registerPushNotificationRequest = new RegisterPushNotificationRequest(userId, accessToken, pushNotificationRequestMap);

        if (!isConnected()) start(context);
        getSpiceManager().execute(registerPushNotificationRequest, requestListener);

    }

    /**
     * Unregister Push notification
     *
     * @param context
     * @param accessToken : User's access token for whom the notification has to be unregistered
     */
    public void unsubscribePushNotification(Context context, String accessToken) {

        String deviceId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);

        HashMap<String, Object> pushNotificationRequestMap = new HashMap<>();
        pushNotificationRequestMap.put(Constants.PUSH_NOTIF_DEVICE_ID, deviceId);

        UnSubscribePushNotificationRequest unSubscribePushNotificationRequest = new UnSubscribePushNotificationRequest(accessToken, pushNotificationRequestMap);

        if (!isConnected()) start(context);

        getSpiceManager().execute(unSubscribePushNotificationRequest, requestListener);

    }

}
