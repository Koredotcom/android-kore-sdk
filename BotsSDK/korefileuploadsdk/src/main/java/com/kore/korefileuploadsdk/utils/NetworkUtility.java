package com.kore.korefileuploadsdk.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

public class NetworkUtility {

    public final static int HTTP_ERROR_CODE_403 = 403;
    public final static String HTTP_SCHEME = "http";
    public final static String FILE_SCHEME = "file:";

    /**
     * Method to check Network Connections
     *
     * @param context
     * @return boolean value
     */

    public static boolean isNetworkConnectionAvailable(Context context) {
        return isNetworkConnectionAvailable(context, false);
    }

    public static boolean isNetworkConnectionAvailable(Context context, boolean showMessage) {
        boolean isNetworkConnectionAvailable = false;

        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        if (activeNetworkInfo != null) {
            isNetworkConnectionAvailable = activeNetworkInfo.isConnected();
        }

        if (!isNetworkConnectionAvailable && showMessage) {
            Toast.makeText(context, "No network available",Toast.LENGTH_LONG).show();

        }
        return isNetworkConnectionAvailable;
    }
}