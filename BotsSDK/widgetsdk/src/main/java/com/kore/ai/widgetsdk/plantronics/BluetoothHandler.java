package com.kore.ai.widgetsdk.plantronics;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.kore.ai.widgetsdk.events.KoreEventCenter;

public class BluetoothHandler extends Handler {
    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case PlantronicsReceiver.HEADSET_EVENT:
                PlantronicsXEventMessage message = (PlantronicsXEventMessage)msg.obj;
//                writeToLogPane(message.toString());
                Log.d("IKIDO",message.toString());
                KoreEventCenter.post(new PlantronicsEvent(PlantronicsXEventMessage.BUTTON_EVENT));
                break;
            default:
                break;
        }
    }
}
