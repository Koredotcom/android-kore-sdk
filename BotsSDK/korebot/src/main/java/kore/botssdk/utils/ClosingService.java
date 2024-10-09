package kore.botssdk.utils;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.Nullable;

import kore.botssdk.models.BotResponse;

public class ClosingService extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {

        // Handle application closing
        Intent intent = new Intent(BundleConstants.DESTROY_EVENT);
        sendBroadcast(intent);

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.TIRAMISU) {
            SharedPreferences.Editor editor = getSharedPreferences(BotResponse.THEME_NAME, Context.MODE_PRIVATE).edit();
            editor.putBoolean(BundleConstants.IS_RECONNECT, false);
            editor.putInt(BotResponse.HISTORY_COUNT, 0);
            editor.apply();
        }

        LogUtils.e("onTaskRemoved", "onTaskRemoved called");
        // Destroy the service
        stopSelf();
        super.onTaskRemoved(rootIntent);
    }
}
