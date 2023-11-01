package kore.botssdk.delegate

import android.content.Context

interface LoginTokenDelegate {
    fun getLoginToken(context: Context): String
}