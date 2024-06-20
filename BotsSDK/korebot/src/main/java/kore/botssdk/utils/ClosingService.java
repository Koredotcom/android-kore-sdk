package kore.botssdk.utils;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

public class ClosingService extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);

        // Handle application closing
        Intent intent = new Intent(BundleConstants.DESTROY_EVENT);
        sendBroadcast(intent);

        LogUtils.e("Application", "Closed called called");
        // Destroy the service
        stopSelf();
    }
}
