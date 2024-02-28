package kore.botssdk.audiocodes.webrtcclient.Login;


import kore.botssdk.application.BotApplication;
import kore.botssdk.audiocodes.webrtcclient.Callbacks.CallBackHandler;
import kore.botssdk.audiocodes.webrtcclient.General.ACManager;
import kore.botssdk.audiocodes.webrtcclient.General.Log;
import kore.botssdk.audiocodes.webrtcclient.General.NotificationUtils;

public class LogoutManager {

    private static final String TAG = "LogoutManager";


    private static int LOGOUT_TIMEOUT_INTERVAL = 1;
    private static Thread closeAppThread;

    public static void closeApplication()
    {
        Log.d(TAG, "closeApplication");
        Log.d(TAG, "close GUI");

        LoginManager.setAppState(LoginManager.AppLoginState.CLOSED);

        if(BotApplication.getCurrentActivity()!=null)
        {
            Log.d(TAG, "close Activity");
            BotApplication.getCurrentActivity().finish();;
        }
        else
        {
            if(BotApplication.getPreviousActivity()!=null)
            {
                Log.d(TAG, "close prev Activity");
                BotApplication.getPreviousActivity().finish();;
            }
        }

        if (ACManager.getInstance().isRegisterState())
        {
            Log.d(TAG, "Unregister client");
            ACManager.getInstance().startLogout();
        }


        closeAppThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(LOGOUT_TIMEOUT_INTERVAL);
                } catch (InterruptedException e) {
                    Log.d(TAG,"closeAppThread interapted");
                }
                endCloseApplication();

            }
        });
        closeAppThread.start();
    }

    CallBackHandler.LoginStateChanged loginStateChanged = new CallBackHandler.LoginStateChanged() {
        @Override
        public void loginStateChange(boolean state) {
            if(!state) {
                if (closeAppThread != null) {
                    closeAppThread.interrupt();
                } else {
                    endCloseApplication();
                }
            }
            //Toast.makeText(BotApplication.getGlobalContext(), "login state: "+state, Toast.LENGTH_SHORT).show();
            //endCloseApplication();
        }
    };

    public static void endCloseApplication()
    {
        NotificationUtils.removeAllNotifications();
        Log.d(TAG, "close Process");
        android.os.Process.killProcess(android.os.Process.myPid());
    }

}
