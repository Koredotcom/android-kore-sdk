package kore.botssdk.common.utils

import android.content.Context
import android.net.ConnectivityManager

class NetworkUtility {

    companion object {
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
        fun isNetworkConnectionAvailable(context: Context): Boolean {
            return isNetworkConnectionAvailable(context, true)
        }

        private fun isNetworkConnectionAvailable(context: Context, showMessage: Boolean): Boolean {
            var isNetworkConnectionAvailable = false
            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            if (activeNetworkInfo != null) {
                isNetworkConnectionAvailable = activeNetworkInfo.isConnected
            }
            if (!isNetworkConnectionAvailable && showMessage) {
//            CustomToast.showToast(context, "No network available");
            }
            return isNetworkConnectionAvailable
        }
    }
}