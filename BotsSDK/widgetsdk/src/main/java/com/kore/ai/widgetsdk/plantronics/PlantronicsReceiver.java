
package com.kore.ai.widgetsdk.plantronics;

import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;

import static android.bluetooth.BluetoothDevice.ACTION_ACL_CONNECTED;
import static android.bluetooth.BluetoothDevice.ACTION_ACL_DISCONNECTED;
import static android.bluetooth.BluetoothDevice.EXTRA_DEVICE;
import static android.bluetooth.BluetoothHeadset.ACTION_AUDIO_STATE_CHANGED;
import static android.bluetooth.BluetoothHeadset.ACTION_VENDOR_SPECIFIC_HEADSET_EVENT;
import static android.bluetooth.BluetoothHeadset.EXTRA_VENDOR_SPECIFIC_HEADSET_EVENT_ARGS;

public class PlantronicsReceiver extends BroadcastReceiver {
    private static final String TAG = "PlantronicsReceiver";
    private BluetoothHandler handler;
    public static final int HEADSET_EVENT = 198007;

    public PlantronicsReceiver(BluetoothHandler btHandler){
        handler = btHandler;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        BluetoothDevice device = intent.getParcelableExtra(EXTRA_DEVICE);
        String bdAddr = device == null ? null : device.getAddress();

        if (ACTION_ACL_CONNECTED.equals(action)) {
            //do something – connect event
        } else if (ACTION_ACL_DISCONNECTED.equals(action)) {
            //do something – disconnect event
        } else if (ACTION_VENDOR_SPECIFIC_HEADSET_EVENT.equals(action)) {
            //Process the XEvent from the Plantronics headset
            PlantronicsXEventMessage message = generateMessageFromEvent(intent);
//            boolean isButton = generateMessageFromEvent(intent);

            if (message != null) {
                Message msg = handler.obtainMessage(HEADSET_EVENT, message);
                handler.sendMessage(msg);
            }


        } else if (ACTION_AUDIO_STATE_CHANGED.equals(action)) {
            //do something
        } else {
            Log.d(TAG, "Action came in and was not processed: " + action);
        }
    }


/**
     * Unpackages the raw Plantronics XEvent message into a PlantronicsXEventMessage class
     *
     * @param intent
     * @return
     */

    private PlantronicsXEventMessage generateMessageFromEvent(Intent intent) {
        Bundle eventExtras = intent.getExtras();
        //get the arguments that the headset passed out
        Object[] args = (Object[]) eventExtras.get(EXTRA_VENDOR_SPECIFIC_HEADSET_EVENT_ARGS);
        String eventName = args[0] instanceof String?(String)args[0]:(args[0]+"");
        PlantronicsXEventMessage m = new PlantronicsXEventMessage(eventName);
        Log.d(TAG, "Event from Plantronics headset = " + eventName);
        if (PlantronicsXEventMessage.AUDIO_EVENT.equals(eventName)) {
            //parsing omitted
        } else if (PlantronicsXEventMessage.VOCALYST_EVENT.equals(eventName)) {
            //parsing omitted
        } else if (PlantronicsXEventMessage.A2DP_EVENT.equals(eventName)) {
            //parsing omitted
        } else if (PlantronicsXEventMessage.SENSOR_STATUS_EVENT.equals(eventName)) {
            //parsing omitted
        } else if (PlantronicsXEventMessage.USER_AGENT_EVENT.equals(eventName)) {
            //parsing omitted
        } else if (PlantronicsXEventMessage.LANG_EVENT.equals(eventName)) {
            //parsing omitted
        } else if (PlantronicsXEventMessage.BATTERY_EVENT.equals(eventName)) {
            //parsing omitted
        } else if (PlantronicsXEventMessage.CONNECTED_EVENT.equals(eventName)) {
            //parsing omitted
        } else if (PlantronicsXEventMessage.BUTTON_EVENT.equals(eventName)) {
            //parsing omitted
        } else if (PlantronicsXEventMessage.DON_EVENT.equals(eventName)) {
            //parsing omitted
        } else if (PlantronicsXEventMessage.DOFF_EVENT.equals(eventName)) {
            //parsing omitted
        }
        return m;
    }
}

