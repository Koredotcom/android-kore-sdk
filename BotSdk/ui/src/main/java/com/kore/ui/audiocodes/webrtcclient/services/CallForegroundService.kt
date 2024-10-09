package com.kore.ui.audiocodes.webrtcclient.services

import android.annotation.SuppressLint
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.kore.ui.audiocodes.webrtcclient.general.NotificationUtils
import com.kore.ui.audiocodes.webrtcclient.general.NotificationUtils.addServiceNotification
import com.kore.ui.audiocodes.webrtcclient.general.NotificationUtils.removeServiceNotification

class CallForegroundService : Service() {
    override fun onCreate() {
        Log.d(TAG, "onCreate")
        super.onCreate()
    }

    @SuppressLint("ForegroundServiceType")
    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        Log.d(TAG, "Received intent: " + intent.action)
        val action = intent.action
        if (START_FOREGROUND == action) {
            Log.d(TAG, "Received Start Foreground Intent ")
            val notification = addServiceNotification(this)
            startForeground(NotificationUtils.NOTIFICATION_SERVICE_ID, notification)
        } else if (STOP_FOREGROUND == action) {
            Log.d(TAG, "Received Stop Foreground Intent")
            stopForeground(true)
            stopSelf()
        }
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy")
    }

    override fun onBind(intent: Intent): IBinder? {
        // Used only in case of bound services.
        return null
    }

    companion object {
        private const val TAG = "CallForeService"
        private const val START_FOREGROUND = "START_FOREGROUND"
        private const val STOP_FOREGROUND = "STOP_FOREGROUND"
        fun startService(context: Context) {
            Log.d(TAG, "startService foreground ")
            val startIntent = Intent(context, CallForegroundService::class.java)
            startIntent.action = START_FOREGROUND
            context.startService(startIntent)
        }

        fun stopService(context: Context) {
            Log.d(TAG, "stopService foreground ")
            val stopIntent = Intent(context, CallForegroundService::class.java)
            stopIntent.action = STOP_FOREGROUND
            context.stopService(stopIntent)
            removeServiceNotification(context)
        }
    }
}