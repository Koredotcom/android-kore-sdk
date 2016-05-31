package kore.botssdk.application;

import android.app.Application;

/**
 * Created by Pradeep Mahato on 31-May-16.
 */
public class BotApplication extends Application {

    AppControl appControl;

    @Override
    public void onCreate() {
        super.onCreate();
        appControl = new AppControl(getApplicationContext());
    }
}
