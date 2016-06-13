package kore.botssdk.pushnotification;

import android.content.Context;
import android.provider.Settings;

import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.request.listener.RequestListener;

import java.util.HashMap;

import kore.botssdk.net.RegisterPushNotificationRequest;
import kore.botssdk.net.UnSubscribePushNotificationRequest;
import kore.botssdk.utils.Constants;
import retrofit.client.Response;

/**
 * Created by Pradeep Mahato on 08-Jun-16.
 * Copyright (c) 2014 Kore Inc. All rights reserved.
 */
public class PushNotificationRegistrar {

    SpiceManager spiceManager;
    RequestListener<Response> requestListener;

    public PushNotificationRegistrar(SpiceManager spiceManager, RequestListener<Response> requestListener) {
        this.spiceManager = spiceManager;
        this.requestListener = requestListener;
    }

    public void registerPushNotification(Context context, String userId, String accessToken) {

        String deviceId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);

        HashMap<String, Object> pushNotificationRequestMap = new HashMap<>();
        pushNotificationRequestMap.put(Constants.PUSH_NOTIF_OS_TYPE, Constants.PUSH_NOTIF_OS_TYPE_ANDROID);
        pushNotificationRequestMap.put(Constants.PUSH_NOTIF_DEVICE_ID, deviceId);

        RegisterPushNotificationRequest registerPushNotificationRequest = new RegisterPushNotificationRequest(userId, accessToken, pushNotificationRequestMap);

        spiceManager.execute(registerPushNotificationRequest, requestListener);

    }

    public void unsubscribePushNotification(Context context, String accessToken) {

        String deviceId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);

        HashMap<String, Object> pushNotificationRequestMap = new HashMap<>();
        pushNotificationRequestMap.put(Constants.PUSH_NOTIF_DEVICE_ID, deviceId);

        UnSubscribePushNotificationRequest unSubscribePushNotificationRequest = new UnSubscribePushNotificationRequest(accessToken, pushNotificationRequestMap);

        spiceManager.execute(unSubscribePushNotificationRequest, requestListener);

    }

}
