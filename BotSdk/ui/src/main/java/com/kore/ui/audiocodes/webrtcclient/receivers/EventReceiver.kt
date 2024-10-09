package com.kore.ui.audiocodes.webrtcclient.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.NetworkInfo
import android.net.wifi.WifiManager
import android.util.Log
import com.audiocodes.mv.webrtcsdk.useragent.AudioCodesUA
import com.kore.ui.audiocodes.webrtcclient.general.ACManager

class EventReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action
        Log.d(TAG, "onReceive: $action")
        val networkInfo = intent.getParcelableExtra<NetworkInfo>(WifiManager.EXTRA_NETWORK_INFO)
        Log.d(TAG, "Internet is connected: " + networkInfo!!.isConnected)
        if (!networkInfo.isConnected) {
            ACManager.getInstance().loginStateChanged(false, 0, "CONNECTIVITY_CHANGE")
        }
        if (networkInfo.isConnected) {
            AudioCodesUA.getInstance().handleNetworkChange()
        }
        connected = networkInfo.isConnected
    }

    companion object {
        const val TAG = "EventsReceiver"
        private const val CONNECTIVITY_ACTION = "android.net.conn.CONNECTIVITY_CHANGE"
        private var connected = true
    }
}
