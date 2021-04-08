package kore.botssdk.listener;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import kore.botssdk.utils.NetworkUtility;

public class NetworkStateReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if(NetworkUtility.isNetworkConnectionAvailable(context)){
            BotSocketConnectionManager.getInstance().checkConnectionAndRetry(context,false);
        }

    }
}
