package kore.botssdk.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Ramachandra Pradeep on 6/1/2016.
 */
public class KoreBotSharedPreferences {

    public static boolean saveCredsToPreferences(Context mContext, String userId, String accessToken) {
        boolean savedSuccessfully = false;
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(Contants.LOGIN_SHARED_PREF, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(Contants.USER_ID, userId);
        editor.putString(Contants.ACCESS_TOKEN, accessToken);
        savedSuccessfully = editor.commit();

        if (savedSuccessfully) {
            CustomToast.showToast(mContext, "Saved to pref");
        } else {
            CustomToast.showToast(mContext, "Failed to save to pref");
        }

        return savedSuccessfully;
    }

    public static String getAccessTokenFromPreferences(Context mContext){
        String accessToken = null;
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(Contants.LOGIN_SHARED_PREF, Context.MODE_PRIVATE);
        accessToken = sharedPreferences.getString(Contants.ACCESS_TOKEN,null);
        return accessToken;
    }
}
