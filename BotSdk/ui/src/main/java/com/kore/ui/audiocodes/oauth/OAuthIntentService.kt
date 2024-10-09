package com.kore.ui.audiocodes.oauth

import android.content.Context
import android.content.Intent
import androidx.core.app.JobIntentService
import com.kore.common.utils.LogUtils

class OAuthIntentService : JobIntentService() {
    private val oAuthManager: OAuthManager = OAuthManager.getInstance()
    public override fun onHandleWork(intent: Intent) {
        val id = intent.data.toString()
        LogUtils.d(OAuthManager.TAG, "onHandleWork: $id")
        if (id == OAuthReceiver.Type.ACCESS_TOKEN.toString()) {
            LogUtils.d(OAuthManager.TAG, "onHandleWork authorize")
            // OAuthManager.getInstance().setAccessTokenAlarm();
        } else if (id == OAuthReceiver.Type.REFRESH_TOKEN.toString()) {
            LogUtils.d(OAuthManager.TAG, "onHandleWork refresh")
            oAuthManager.refreshToken()
        }
    }

    companion object {
        private const val JOB_ID = 1000

        @JvmStatic
        fun enqueueWork(context: Context, work: Intent) {
            enqueueWork(context, OAuthIntentService::class.java, JOB_ID, work)
        }
    }
}