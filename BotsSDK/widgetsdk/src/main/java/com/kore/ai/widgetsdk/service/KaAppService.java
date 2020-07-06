package com.kore.ai.widgetsdk.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;
import android.util.Log;

import com.kore.ai.widgetsdk.network.NetworkEvents;

import de.greenrobot.event.EventBus;

/**
 * Created by Ramachandra Pradeep on 28-Oct-18.
 */

public class KaAppService extends Service {

    private boolean isFirstTime = true;
    @Override
    public void onCreate() {
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkStateReceiver, filter);
        super.onCreate();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
//        KoraSocketConnectionManager.killInstance();
        unregisterReceiver(networkStateReceiver);
        super.onDestroy();
        Log.d("IKIDO", "Service Destroyed");
    }

    //For network monitor
    BroadcastReceiver networkStateReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
//            boolean noConnectivity = intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false);
            ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            boolean hasConnectivity = (activeNetworkInfo!= null && activeNetworkInfo.isConnected());

            if(hasConnectivity) {
                if(!isFirstTime) {
                    onConnectionFound(activeNetworkInfo);
                }
                isFirstTime = false;
//                Log.d("IKIDO","On connection found");
            } else {
                if(!isFirstTime) {
                    onConnectionLost(activeNetworkInfo);
                }
                isFirstTime = false;
//                Log.d("IKIDO","On connection lost");
            }
        }
    };
    private void onConnectionFound(NetworkInfo activeNetworkInfo) {
        EventBus.getDefault().post(new NetworkEvents.NetworkConnectivityEvent(activeNetworkInfo, true));
    }
    private void onConnectionLost(NetworkInfo activeNetworkInfo){
        EventBus.getDefault().post(new NetworkEvents.NetworkConnectivityEvent(activeNetworkInfo, false));
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        stopSelf();
    }
}
