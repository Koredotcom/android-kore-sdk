package com.kore.common.utils

import android.content.Context
import android.net.ConnectivityManager

class NetworkUtils {
    companion object {
        private const val LOG_TAG = "NetworkUtils"
        const val HTTP_ERROR_CODE_403 = 403
        const val HTTP_SCHEME = "http"
        const val FILE_SCHEME = "file:"
        const val CONTENT_SCHEME = "content:"

        /**
         * Method to check Network Connections
         *
         * @param context
         * @return boolean value
         */
        fun isNetworkAvailable(context: Context): Boolean {
            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val isNetworkAvailable = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork) != null
            if (!isNetworkAvailable) LogUtils.e(LOG_TAG, "No internet is available!. Please connect to the internet!")
            return isNetworkAvailable
        }
    }
}