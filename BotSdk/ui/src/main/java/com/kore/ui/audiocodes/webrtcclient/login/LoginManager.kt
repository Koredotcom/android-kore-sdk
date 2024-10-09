package com.kore.ui.audiocodes.webrtcclient.login

import android.content.Context
import android.util.Log
import com.kore.ui.audiocodes.webrtcclient.general.Prefs

object LoginManager {
    private const val TAG = "LoginManager"
    private var isAppOpen = false

    /**
     * @return the state the app is in CLOSED,CRUSHED,LOGOUT,ACTIVE
     */
    fun getAppState(context: Context): AppLoginState {
        // Prefs.isAppOpen() = is app open prefs
        // isAppOpen = is app open static value
        var appLoginState = AppLoginState.ACTIVE
        if (!Prefs.getIsAppOpen(context) && !isAppOpen) {
            appLoginState = AppLoginState.CLOSED
        }
        if (Prefs.getIsAppOpen(context) && !isAppOpen) {
            appLoginState = AppLoginState.CRUSHED
        }
        if (!Prefs.getIsAppOpen(context) && isAppOpen) {
            appLoginState = AppLoginState.LOGOUT
        }
        Log.d(TAG, "App state: $appLoginState")
        return appLoginState
    }

    /**
     * set the app state (CLOSED,CRUSHED,LOGOUT,ACTIVE)
     */
    fun setAppState(context: Context, appLoginState: AppLoginState) {
        // Prefs.isAppOpen() = is app open prefs
        // isAppOpen = is app open static value
        when (appLoginState) {
            AppLoginState.ACTIVE -> {
                isAppOpen = true
                Prefs.setIsAppOpen(context, true)
            }

            AppLoginState.CLOSED -> {
                isAppOpen = false
                Prefs.setIsAppOpen(context, false)
            }

            AppLoginState.CRUSHED -> {
                isAppOpen = false
                Prefs.setIsAppOpen(context, true)
            }

            AppLoginState.LOGOUT -> {
                isAppOpen = true
                Prefs.setIsAppOpen(context, false)
            }

            else -> {}
        }
    }

    enum class AppLoginState {
        CLOSED,
        CRUSHED,
        LOGOUT,
        ACTIVE
    }
}
