package kore.botssdk.audiocodes.oauth;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.core.app.JobIntentService;

public class OAuthIntentService extends JobIntentService {

    private static final int JOB_ID = 1000;

    public static void enqueueWork(Context context, Intent work) {
        enqueueWork(context, OAuthIntentService.class, JOB_ID, work);
    }

    @Override
    public void onHandleWork(Intent intent) {
        String id = intent.getData().toString();
        Log.d(OAuthManager.TAG, "onHandleWork: " + id);
        if(id.equals(OAuthReceiver.Type.ACCESS_TOKEN.toString())){
            Log.d(OAuthManager.TAG, "onHandleWork authorize");
           // OAuthManager.getInstance().setAccessTokenAlarm();
        }
        else if(id.equals(OAuthReceiver.Type.REFRESH_TOKEN.toString())){
            Log.d(OAuthManager.TAG, "onHandleWork refresh");
            OAuthManager.getInstance().refreshToke();
        }
    }
}
