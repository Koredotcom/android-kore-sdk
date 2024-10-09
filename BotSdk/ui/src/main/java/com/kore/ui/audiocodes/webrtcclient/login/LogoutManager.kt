package com.kore.ui.audiocodes.webrtcclient.login

import android.content.Context
import android.os.Process
import android.util.Log
import com.kore.ui.audiocodes.webrtcclient.general.ACManager
import com.kore.ui.audiocodes.webrtcclient.general.NotificationUtils.removeAllNotifications

class LogoutManager {
    companion object {
        private const val TAG = "LogoutManager"
        private const val LOGOUT_TIMEOUT_INTERVAL = 1
        var closeAppThread: Thread? = null
        fun closeApplication(context: Context, acManager: ACManager) {
            Log.d(TAG, "closeApplication")
            Log.d(TAG, "close GUI")
            LoginManager.setAppState(context, LoginManager.AppLoginState.CLOSED)

//        if(BotApplication.Companion.applicationContext()!=null)
//        {
//            Log.d(TAG, "close Activity");
//            BotApplication.Companion.applicationContext().finish();;
//        }
//        else
//        {
//            if(BotApplication.getPreviousActivity()!=null)
//            {
//                Log.d(TAG, "close prev Activity");
//                BotApplication.getPreviousActivity().finish();;
//            }
//        }
            if (acManager.isRegisterState) {
                Log.d(TAG, "Unregister client")
                acManager.startLogout()
            }
            closeAppThread = Thread {
                try {
                    Thread.sleep(LOGOUT_TIMEOUT_INTERVAL.toLong())
                } catch (e: InterruptedException) {
                    Log.d(TAG, "closeAppThread interapted")
                }
                endCloseApplication(context)
            }
            closeAppThread!!.start()
        }

        private fun endCloseApplication(context: Context) {
            removeAllNotifications(context)
            Log.d(TAG, "close Process")
            Process.killProcess(Process.myPid())
        }
    }
}
