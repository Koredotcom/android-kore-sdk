package kore.botssdk.pushnotification;

import java.util.HashMap;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import kore.botssdk.net.RegisterPushNotificationRequest;
import kore.botssdk.net.UnSubscribePushNotificationRequest;
import kore.botssdk.utils.Constants;
import kore.botssdk.utils.Utils;
import okhttp3.ResponseBody;

/**
 * Copyright (c) 2014 Kore Inc. All rights reserved.
 */

public class PushNotificationRegister {
    /**
     * Register for Push notification
     *
     * @param userId      : UserId for whom push notification is required
     * @param accessToken : User's access token
     * @param deviceId    : Android device ID
     */

    public void registerPushNotification(String userId, String accessToken, String deviceId) {

        HashMap<String, Object> pushNotificationRequestMap = new HashMap<>();
        pushNotificationRequestMap.put(Constants.PUSH_NOTIF_OS_TYPE, Constants.PUSH_NOTIF_OS_TYPE_ANDROID);
        pushNotificationRequestMap.put(Constants.PUSH_NOTIF_DEVICE_ID, deviceId);

        new RegisterPushNotificationRequest(userId, Utils.accessTokenHeader(accessToken), pushNotificationRequestMap).loadDataFromNetwork().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<ResponseBody>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(ResponseBody response) {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }

    /**
     * Unregister Push notification
     *
     * @param accessToken : User's access token for whom the notification has to be unregistered
     * @param deviceId    : Android device ID
     */

    public void unsubscribePushNotification(String userId, String accessToken, String deviceId) {

        HashMap<String, Object> pushNotificationRequestMap = new HashMap<>();
        pushNotificationRequestMap.put(Constants.PUSH_NOTIF_DEVICE_ID, deviceId);
        pushNotificationRequestMap.put(Constants.PUSH_NOTIF_OS_TYPE, Constants.PUSH_NOTIF_OS_TYPE_ANDROID);

        new UnSubscribePushNotificationRequest(userId, Utils.accessTokenHeader(accessToken), pushNotificationRequestMap).loadDataFromNetwork().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(ResponseBody response) {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });

    }

}
