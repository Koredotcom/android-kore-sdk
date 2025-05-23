package com.kore.services

import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import com.kore.common.utils.LogUtils
import com.kore.constants.SharedPrefConstants.HISTORY_COUNT
import com.kore.model.constants.BotResponseConstants
import com.kore.ui.utils.BundleConstants
import androidx.core.content.edit

class ClosingService : Service() {
    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onTaskRemoved(rootIntent: Intent) {
        // Handle application closing
        val intent = Intent(BundleConstants.DESTROY_EVENT)
        intent.setPackage(packageName)
        sendBroadcast(intent)

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.TIRAMISU) {
            getSharedPreferences(BotResponseConstants.THEME_NAME, MODE_PRIVATE).edit { putInt(HISTORY_COUNT, 0) }
        }

        LogUtils.i("onTaskRemoved", "onTaskRemoved called")
        // Destroy the service
        stopSelf()
        super.onTaskRemoved(rootIntent)
    }
}
