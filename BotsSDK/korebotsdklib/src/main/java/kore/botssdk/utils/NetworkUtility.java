package kore.botssdk.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
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
    @SuppressLint("MissingPermission")
    public static boolean isNetworkConnectionAvailable(Context context, boolean showMessage) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        Network nw = connectivityManager.getActiveNetwork();
        if (nw == null) return false;
        NetworkCapabilities actNw = connectivityManager.getNetworkCapabilities(nw);
        return actNw != null && (actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) || actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) || actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) || actNw.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH));
    }
}