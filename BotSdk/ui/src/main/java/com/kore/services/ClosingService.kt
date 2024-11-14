package com.kore.services

import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import com.kore.common.utils.LogUtils
import com.kore.constants.SharedPrefConstants.HISTORY_COUNT
import com.kore.model.constants.BotResponseConstants
import com.kore.ui.utils.BundleConstants

class ClosingService : Service() {
    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onTaskRemoved(rootIntent: Intent) {
        // Handle application closing

        val intent = Intent(BundleConstants.DESTROY_EVENT)
        sendBroadcast(intent)

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.TIRAMISU) {
            val editor = getSharedPreferences(BotResponseConstants.THEME_NAME, MODE_PRIVATE).edit()
            editor.putInt(HISTORY_COUNT, 0)
            editor.apply()
        }

        LogUtils.i("onTaskRemoved", "onTaskRemoved called")
        // Destroy the service
        stopSelf()
        super.onTaskRemoved(rootIntent)
    }
}
