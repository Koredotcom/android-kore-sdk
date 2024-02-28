package kore.botssdk.audiocodes.webrtcclient.Receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;

import com.audiocodes.mv.webrtcsdk.useragent.AudioCodesUA;

import kore.botssdk.audiocodes.webrtcclient.General.ACManager;
import kore.botssdk.audiocodes.webrtcclient.General.Log;

public class EventReceiver extends BroadcastReceiver {
    static final String TAG = "EventsReceiver";
    private static final String CONNECTIVITY_ACTION = "android.net.conn.CONNECTIVITY_CHANGE";

    private static boolean connected = true;

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Log.d(TAG, "onReceive: " + action);

        NetworkInfo networkInfo = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
        Log.d(TAG, "Internet is connected: " + networkInfo.isConnected());
        if (!networkInfo.isConnected() ) {
            ACManager.getInstance().loginStateChanged(false,0,"CONNECTIVITY_CHANGE");

        }
        if (networkInfo.isConnected() ) {
            AudioCodesUA.getInstance().handleNetworkChange();
        }
        connected = networkInfo.isConnected();
    }
}
