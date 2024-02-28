package kore.botssdk.audiocodes.webrtcclient.Activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;

import com.audiocodes.mv.webrtcsdk.useragent.AudioCodesUA;

import kore.botssdk.R;
import kore.botssdk.activity.BotChatActivity;
import kore.botssdk.activity.BotHomeActivity;
import kore.botssdk.application.BotApplication;
import kore.botssdk.audiocodes.oauth.OAuthManager;
import kore.botssdk.audiocodes.webrtcclient.General.ACManager;
import kore.botssdk.audiocodes.webrtcclient.General.Log;
import kore.botssdk.audiocodes.webrtcclient.General.NotificationUtils;
import kore.botssdk.audiocodes.webrtcclient.General.Prefs;
import kore.botssdk.audiocodes.webrtcclient.Login.LoginManager;
import kore.botssdk.audiocodes.webrtcclient.Permissions.PermissionManager;
import kore.botssdk.audiocodes.webrtcclient.Permissions.PermissionRequest;


public class SplashActivity extends BaseAppCompatActivity {

    private static final String TAG = "SplashActivity";

    private static final int delayStartupInterval=500;

    private static boolean isPermissionRequestActive;
    private static boolean allPermissionsGranted=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        hasToolbar=false;
        super.onCreate(savedInstanceState);
        Log.d(TAG,"onCreate");
        appStateCheck();
        setContentView(R.layout.splash_activity);

        appPermissionCheck();

    }

    private void appStateCheck()
    {
        NotificationUtils.removeAllNotifications();
        if (LoginManager.getAppState() == LoginManager.AppLoginState.CRUSHED) {
            //silent login


        }
        else if (LoginManager.getAppState() == LoginManager.AppLoginState.ACTIVE) {
            //skip splash screen
            startApp();
        }
    }

    private void appPermissionCheck()
    {

        Log.d(TAG, "Check requestAllPermissions");
        isPermissionRequestActive = true;
        PermissionRequest permissionRequest =  new PermissionRequest()
        {
            @Override
            public void granted()
            {
                Log.d(TAG, " PermissionRequest: granted");
//                initStartApp();
            }


            @Override
            public void revoked()
            {
                //Can close app if not all permission is approved
                Log.d(TAG, " PermissionRequest: revoked");

//                allPermissionsGranted=false;
//                initStartApp();
                //finish();
                //LogoutManager.closeApplication(true);
            }

            @Override
            public void allResults(boolean allGranted) {
                allPermissionsGranted=allGranted;
                initStartApp();
            }
        };
        PermissionManager.getInstance().requestAllPermissions(SplashActivity.this, permissionRequest);

    }

    private void initStartApp()
    {
        isPermissionRequestActive = false;
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        if (LoginManager.getAppState() == LoginManager.AppLoginState.CLOSED) {
            //for full login
            new DelayStartApp().execute();
        } else {
            //for quick login
            startApp();
        }
    }

    private void startApp()
    {
        OAuthManager.getInstance().initialize(BotApplication.getGlobalContext(), new OAuthManager.EventsCallback() {
            @Override
            public void onRelogin() {
                android.util.Log.d(TAG, "relogin!");
                Prefs.setFirstLogin(true);
                BotApplication.getCurrentActivity().finish();
                Intent intent = new Intent(BotApplication.getGlobalContext(), BotChatActivity.class);
                intent.putExtra(LoginActivity.OAUTH_FAILED, true);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                BotApplication.getGlobalContext().startActivity(intent);
            }

            @Override
            public void onUpdateToken(String token) {
                AudioCodesUA.getInstance().setOauthToken(token);
            }
        });

        Log.d(TAG, "startApp");
        //firstLogin
        //Prefs.setFirstLogin(true);
        boolean isFirstLogin = Prefs.isFirstLogin();
        Log.d(TAG, "isFirstLogin: "+isFirstLogin);


        Prefs.setUsePush(true);
        //check if all permission are granted
        if (allPermissionsGranted)
        {
            if (isFirstLogin)
            {
                startNextActivity(BotHomeActivity.class);
                finish();
                return;
            }
            //start login and open app main screen
            ACManager.getInstance().startLogin();
            startNextActivity(BotHomeActivity.class);
            finish();
        }
        else {
            startNextActivity(PermissionDeniedActivity.class);
            finish();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if(requestCode == PermissionManager.REQUEST_CODE_ASK_PERMISSIONS)
        {
            int grantedRes=0;
            for (int i =0; i< permissions.length; i++)
            {
                String permision = permissions[i];
                int grantResult = grantResults[i];
                boolean isLastPermission = i == (permissions.length-1);

                Log.d(TAG, "onRequestPermissionsResult: permision: "+permision);
                Log.d(TAG, "onRequestPermissionsResult: grantResult: "+grantResult);
                Log.d(TAG, "isLastPermission: "+isLastPermission);

                //calculate results results
                grantedRes+=grantResult;

                switch (grantResult)
                {
                    case PackageManager.PERMISSION_GRANTED:
                        PermissionManager.getInstance().getPermissionRequestList().get(i).granted();
                        break;
                    case PackageManager.PERMISSION_DENIED:
                        PermissionManager.getInstance().getPermissionRequestList().get(i).revoked();
                        break;

                }
            }

            if(permissions.length>0) {
                PermissionManager.getInstance().getPermissionRequestList().get(permissions.length - 1).allResults(grantedRes == 0);
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
    @Override
    protected void onStop()
    {
        super.onStop();
        Log.d(TAG, "onStop");
        if(isPermissionRequestActive)
        {
            finish();
        }
    }

    class DelayStartApp extends AsyncTask<String, Void, Boolean> {


        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            startApp();
        }

        @Override
        protected Boolean doInBackground(String... params) {
            try {
                synchronized (this) {
                    // Wait given period of time or exit on touch
                    Thread.sleep(delayStartupInterval);
                }
            } catch (InterruptedException ex) {
            }
            return null;
        }
    }
}
