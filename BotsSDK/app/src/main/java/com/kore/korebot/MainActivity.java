package com.kore.korebot;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;
import java.util.Locale;
import java.util.TimeZone;
import java.util.UUID;

import kore.botssdk.activity.BotChatActivity;
import kore.botssdk.audiocodes.webrtcclient.Permissions.PermissionManager;
import kore.botssdk.audiocodes.webrtcclient.Permissions.PermissionRequest;
import kore.botssdk.listener.BotSocketConnectionManager;
import kore.botssdk.net.RestResponse;
import kore.botssdk.net.SDKConfiguration;
import kore.botssdk.utils.BundleUtils;

public class MainActivity extends AppCompatActivity {

    String TAG = MainActivity.class.getName();
    static boolean allPermissionsGranted = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SDKConfiguration.setCustomTemplateView("link", new LinkTemplateView(MainActivity.this));
        SDKConfiguration.Server.setQueryParams(getQueryParams());
        SDKConfiguration.Server.setCustomData(getCustomData());

        Button launchBotBtn = findViewById(R.id.launchBotBtn);
        launchBotBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchBotChatActivity();
            }
        });

        appPermissionCheck();
    }

    private void appPermissionCheck() {

        Log.d(TAG, "Check requestAllPermissions");
        boolean isPermissionRequestActive = true;
        PermissionRequest permissionRequest = new PermissionRequest() {
            @Override
            public void granted() {
                Log.d(TAG, " PermissionRequest: granted");
            }

            @Override
            public void revoked() {
                //Can close app if not all permission is approved
                Log.d(TAG, " PermissionRequest: revoked");
            }

            @Override
            public void allResults(boolean allGranted) {
                allPermissionsGranted = allGranted;
            }
        };
        PermissionManager.getInstance().requestAllPermissions(MainActivity.this, permissionRequest);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == PermissionManager.REQUEST_CODE_ASK_PERMISSIONS) {
            int grantedRes = 0;
            for (int i = 0; i < permissions.length; i++) {
                String permision = permissions[i];
                int grantResult = grantResults[i];
                boolean isLastPermission = i == (permissions.length - 1);

                Log.d(TAG, "onRequestPermissionsResult: permision: " + permision);
                Log.d(TAG, "onRequestPermissionsResult: grantResult: " + grantResult);
                Log.d(TAG, "isLastPermission: " + isLastPermission);

                //calculate results results
                grantedRes += grantResult;

                switch (grantResult) {
                    case PackageManager.PERMISSION_GRANTED:
                        PermissionManager.getInstance().getPermissionRequestList().get(i).granted();
                        break;
                    case PackageManager.PERMISSION_DENIED:
                        PermissionManager.getInstance().getPermissionRequestList().get(i).revoked();
                        break;

                }
            }

            if (permissions.length > 0) {
                PermissionManager.getInstance().getPermissionRequestList().get(permissions.length - 1).allResults(grantedRes == 0);
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    /**
     * Launching BotChatActivity where user can interact with bot
     */
    void launchBotChatActivity() {
        Intent intent = new Intent(getApplicationContext(), BotChatActivity.class);
        Bundle bundle = new Bundle();
        //This should not be null
        bundle.putBoolean(BundleUtils.SHOW_PROFILE_PIC, false);
        bundle.putString(BundleUtils.BOT_NAME_INITIALS, String.valueOf(SDKConfiguration.Client.bot_name.charAt(0)));
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @SuppressLint("UnknownNullness")
    public HashMap<String, Object> getQueryParams()
    {
        HashMap<String, Object> queryParams = new HashMap<>();
        queryParams.put("q1", true);
        queryParams.put("q2", 4);
        queryParams.put("q3", "connect");
        return queryParams;
    }

    @SuppressLint("UnknownNullness")
    public RestResponse.BotCustomData getCustomData()
    {
        RestResponse.BotCustomData customData = new RestResponse.BotCustomData();
        customData.put("name", "Kore Bot");
        customData.put("emailId", "emailId");
        customData.put("mobile", "mobile");
        customData.put("accountId", "accountId");
        customData.put("timeZoneOffset", -330);
        customData.put("UserTimeInGMT", TimeZone.getDefault().getID() + " " + Locale.getDefault().getISO3Language());
        return customData;
    }
}