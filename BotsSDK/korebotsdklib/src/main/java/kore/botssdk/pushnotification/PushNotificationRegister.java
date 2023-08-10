package kore.botssdk.pushnotification;

import android.content.Context;

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
import retrofit2.Response;

/**
 * Created by Pradeep Mahato on 08-Jun-16.
 * Copyright (c) 2014 Kore Inc. All rights reserved.
 */

public class PushNotificationRegister  {

//    RequestListener<Response> requestListener;

/**
 * @param requestListener : Callback for requests
 */

//    public PushNotificationRegister(RequestListener<Response> requestListener) {
//        this.requestListener = requestListener;
//    }

    /**
     * Register for Push notification
     *
     * @param context
     * @param userId : UserId for whom push notification is required
     * @param accessToken : User's access token
     * @param deviceId : Android device ID
     */

    public void registerPushNotification(Context context, String userId, String accessToken, String deviceId) {

        HashMap<String, Object> pushNotificationRequestMap = new HashMap<>();
        pushNotificationRequestMap.put(Constants.PUSH_NOTIF_OS_TYPE, Constants.PUSH_NOTIF_OS_TYPE_ANDROID);
        pushNotificationRequestMap.put(Constants.PUSH_NOTIF_DEVICE_ID, deviceId);

        new RegisterPushNotificationRequest(userId, Utils.accessTokenHeader(accessToken), pushNotificationRequestMap)
                .loadDataFromNetwork()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseBody>() {
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

        // if (!isConnected()) start(context);
        //getSpiceManager().execute(registerPushNotificationRequest, requestListener);

    }

    /**
     * Unregister Push notification
     *
     * @param context
     * @param accessToken : User's access token for whom the notification has to be unregistered
     * @param deviceId : Android device ID
     */

    public void unsubscribePushNotification(Context context, String userId, String accessToken, String deviceId) {

        HashMap<String, Object> pushNotificationRequestMap = new HashMap<>();
        pushNotificationRequestMap.put(Constants.PUSH_NOTIF_DEVICE_ID, deviceId);

        new UnSubscribePushNotificationRequest(userId, Utils.accessTokenHeader(accessToken), pushNotificationRequestMap)
                .loadDataFromNetwork()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseBody>() {
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

//        if (!isConnected()) start(context);
//            getSpiceManager().execute(unSubscribePushNotificationRequest, requestListener);
    }

}
