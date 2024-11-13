package kore.botssdk.audiocodes.webrtcclient.Login;

import android.app.Activity;
import android.content.Context;

import kore.botssdk.audiocodes.webrtcclient.Callbacks.CallBackHandler;
import kore.botssdk.audiocodes.webrtcclient.General.ACManager;
import kore.botssdk.audiocodes.webrtcclient.General.Log;
import kore.botssdk.audiocodes.webrtcclient.General.NotificationUtils;

public class LogoutManager {
    private static final String TAG = "LogoutManager";
    private static final int LOGOUT_TIMEOUT_INTERVAL = 1;
    static Thread closeAppThread;

    public static void closeApplication(Context context) {
        Log.d(TAG, "closeApplication");
        Log.d(TAG, "close GUI");

        LoginManager.setAppState(context, LoginManager.AppLoginState.CLOSED);

//        if(BotApplication.getCurrentActivity()!=null)
//        {
//            Log.d(TAG, "close Activity");
        ((Activity) context).finish();
        ;
//        }
//        else
//        {
//            if(BotApplication.getPreviousActivity()!=null)
//            {
//                Log.d(TAG, "close prev Activity");
//        BotApplication.getPreviousActivity().finish();
        ;
//            }
//        }

        if (ACManager.getInstance().isRegisterState()) {
            Log.d(TAG, "Unregister client");
            ACManager.getInstance().startLogout();
        }

        closeAppThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(LOGOUT_TIMEOUT_INTERVAL);
                } catch (InterruptedException e) {
                    Log.d(TAG, "closeAppThread interapted");
                }
                endCloseApplication(context);
            }
        });
        closeAppThread.start();
    }

    CallBackHandler.LoginStateChanged loginStateChanged = new CallBackHandler.LoginStateChanged() {
        @Override
        public void loginStateChange(Context context, boolean state) {
            if (!state) {
                if (closeAppThread != null) {
                    closeAppThread.interrupt();
                } else {
                    endCloseApplication(context);
                }
            }
            //Toast.makeText(BotApplication.getGlobalContext(), "login state: "+state, Toast.LENGTH_SHORT).show();
            //endCloseApplication();
        }
    };

    public static void endCloseApplication(Context context) {
        NotificationUtils.removeAllNotifications(context);
        Log.d(TAG, "close Process");
        android.os.Process.killProcess(android.os.Process.myPid());
    }

}
