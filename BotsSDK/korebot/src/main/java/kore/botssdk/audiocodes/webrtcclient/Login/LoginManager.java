package kore.botssdk.audiocodes.webrtcclient.Login;


import android.content.Context;

import kore.botssdk.audiocodes.webrtcclient.General.Log;
import kore.botssdk.audiocodes.webrtcclient.General.Prefs;

public class LoginManager {

    private static final String TAG = "LoginManager";

    private static boolean isAppOpen = false;


    public enum AppLoginState {
        CLOSED,
        CRUSHED,
        LOGOUT,
        ACTIVE
    }

    /**
     * @return the state the app is in CLOSED,CRUSHED,LOGOUT,ACTIVE
     */
    public static AppLoginState getAppState(Context context) {
        // Prefs.isAppOpen() = is app open prefs
        // isAppOpen = is app open static value
        AppLoginState appLoginState = AppLoginState.ACTIVE;
        if (!Prefs.isAppOpen(context) && !isAppOpen) {
            appLoginState = AppLoginState.CLOSED;
        }
        if (Prefs.isAppOpen(context) && !isAppOpen) {
            appLoginState = AppLoginState.CRUSHED;
        }
        if (!Prefs.isAppOpen(context) && isAppOpen) {
            appLoginState = AppLoginState.LOGOUT;
        }
        Log.d(TAG, "App state: " + appLoginState);
        return appLoginState;

    }

    /**
     * set the app state (CLOSED,CRUSHED,LOGOUT,ACTIVE)
     */
    public static void setAppState(Context context, AppLoginState appLoginState) {
        // Prefs.isAppOpen() = is app open prefs
        // isAppOpen = is app open static value
        switch (appLoginState) {
            case ACTIVE:
                isAppOpen = true;
                Prefs.setAppOpen(context, true);
                break;
            case CLOSED:
                isAppOpen = false;
                Prefs.setAppOpen(context, false);
                break;
            case CRUSHED:
                isAppOpen = false;
                Prefs.setAppOpen(context, true);
                break;
            case LOGOUT:
                isAppOpen = true;
                Prefs.setAppOpen(context, false);
                break;
        }
    }
}
