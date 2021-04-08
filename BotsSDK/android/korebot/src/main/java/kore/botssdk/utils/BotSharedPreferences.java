package kore.botssdk.utils;/*
package kore.botssdk.utils;

import android.content.Context;
import android.content.SharedPreferences;

*/
/**
 * Created by Ramachandra Pradeep on 6/1/2016.
 * Copyright (c) 2014 Kore Inc. All rights reserved.
 *//*

public class BotSharedPreferences {

    public static boolean saveCredsToPreferences(Context mContext, String userId, String accessToken) {
        boolean savedSuccessfully = false;
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(StringConstants.LOGIN_SHARED_PREF, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(Constants.USER_ID, userId);
        editor.putString(Constants.ACCESS_TOKEN, accessToken);
        savedSuccessfully = editor.commit();

        if (savedSuccessfully) {
            CustomToast.showToast(mContext, "Saved to pref");
        } else {
            CustomToast.showToast(mContext, "Failed to save to pref");
        }

        return savedSuccessfully;
    }

    public static boolean clearPreferences(Context mContext) {
        boolean savedSuccessfully = false;
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(StringConstants.LOGIN_SHARED_PREF, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.remove(Constants.USER_ID);
        editor.remove(Constants.ACCESS_TOKEN);
        savedSuccessfully = editor.commit();

        if (savedSuccessfully) {
            CustomToast.showToast(mContext, "Successfully cleared");
        } else {
            CustomToast.showToast(mContext, "Failed to clear");
        }

        return savedSuccessfully;
    }

    public static String getAccessTokenFromPreferences(Context context){
        String accessToken = null;
        SharedPreferences sharedPreferences = context.getSharedPreferences(StringConstants.LOGIN_SHARED_PREF, Context.MODE_PRIVATE);
        accessToken = sharedPreferences.getString(Constants.ACCESS_TOKEN,null);
        return accessToken;
    }

    public static String getUserIdFromPreferences(Context context) {
        String userId = null;
        SharedPreferences sharedPreferences = context.getSharedPreferences(StringConstants.LOGIN_SHARED_PREF, Context.MODE_PRIVATE);
        userId = sharedPreferences.getString(Constants.USER_ID,null);
        return userId;
    }
}
*/
