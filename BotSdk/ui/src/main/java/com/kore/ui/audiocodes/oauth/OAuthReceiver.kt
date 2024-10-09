package com.kore.ui.audiocodes.oauth

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import com.kore.common.utils.LogUtils.i
import com.kore.ui.audiocodes.oauth.OAuthIntentService.Companion.enqueueWork

class OAuthReceiver : BroadcastReceiver() {
    enum class Type {
        REFRESH_TOKEN,
        ACCESS_TOKEN
    }

    override fun onReceive(context: Context, intent: Intent) {
        Log.d(OAuthManager.TAG, "onReceive alarm")
        enqueueWork(context, intent)
    }

    companion object {
        fun setRefreshTokenAlarm(context: Context, interval: Long) {
            setAlarm(context, interval, Type.REFRESH_TOKEN)
        }

        fun setAccessTokenAlarm(context: Context,interval: Long) {
            setAlarm(context, interval, Type.ACCESS_TOKEN)
        }

        @SuppressLint("ScheduleExactAlarm")
        private fun setAlarm(context: Context, actualInterval: Long, type: Type) {
            var interval = actualInterval
            i(OAuthManager.TAG, "set Alarm. interval: $interval seconds. Type $type")
            interval *= 1000
            val am = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val alarmIntent = Intent(context, OAuthReceiver::class.java)
            alarmIntent.data = Uri.parse(type.toString())
            var startingTime = System.currentTimeMillis()
            startingTime += interval
            val pendingIntent = PendingIntent.getBroadcast(
                context,
                0,
                alarmIntent,
                PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            am.setExact(AlarmManager.RTC_WAKEUP, startingTime, pendingIntent)
        }
    }
}