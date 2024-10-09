package com.kore.common.utils

import android.annotation.SuppressLint
import android.content.Context
import android.provider.Settings

class DeviceUtils {
    companion object {
        @SuppressLint("HardwareIds")
        fun getDeviceId(context: Context): String = Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
    }
}