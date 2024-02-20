package kore.botssdk.events;

/**
 * Created by Ramachandra Pradeep on 02-Mar-18.
 */

import android.net.NetworkInfo;

/**
 * Created by Pradeep on 09-Jul-15.
 */
public class NetworkEvents {

    public static class NetworkConnectivityEvent {
        final NetworkInfo networkInfo;
        final boolean networkConnectivity;

        public NetworkConnectivityEvent(NetworkInfo networkInfo) {
            this.networkInfo = networkInfo;
            this.networkConnectivity = (networkInfo!= null && networkInfo.isConnected());
        }

        public NetworkConnectivityEvent(NetworkInfo networkInfo, boolean networkConnectivity) {
            this.networkInfo = networkInfo;
            this.networkConnectivity = networkConnectivity;
        }

        public NetworkInfo getNetworkInfo() {
            return networkInfo;
        }

        public boolean isNetworkConnectivity() {
            return networkConnectivity;
        }
    }

    public static class NoNetworkEvent {

    }
}
