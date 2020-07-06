
package com.kore.ai.widgetsdk.helpers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.kore.ai.widgetsdk.events.KoreEventCenter;
import com.kore.ai.widgetsdk.events.TimeZoneChangedEvent;

/**
 * Created by Ramachandra Pradeep on 14-Feb-19.
 */


public class TimeZoneChangeReceiver   extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        final String action = intent.getAction();

        if (action.equals(Intent.ACTION_TIMEZONE_CHANGED)) {
            KoreEventCenter.post(new TimeZoneChangedEvent(true));
        }
    }
}

