package com.kore.ai.widgetsdk.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkUtility {

    public final static int HTTP_ERROR_CODE_403 = 403;
    public final static String HTTP_SCHEME = "http";
    public final static String FILE_SCHEME = "file:";
    public final static String CONTENT_SCHEME = "content:";

    /**
     * Method to check Network Connections
     *
     * @param context
     * @return boolean value
     */

    public static boolean isNetworkConnectionAvailable(Context context) {
        return isNetworkConnectionAvailable(context, true);
    }

    public static boolean isNetworkConnectionAvailable(Context context, boolean showMessage) {
        boolean isNetworkConnectionAvailable = false;

        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        if (activeNetworkInfo != null) {
            isNetworkConnectionAvailable = activeNetworkInfo.isConnected();
        }

        if (!isNetworkConnectionAvailable && showMessage) {
//            CustomToast.showToast(context, "No network available");

        }
        return isNetworkConnectionAvailable;
    }
}