package com.kore.korebot;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.kore.korebot.customtemplates.LinkTemplateHolder;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Locale;
import java.util.Properties;
import java.util.TimeZone;

import kore.botssdk.activity.NewBotChatActivity;
import kore.botssdk.audiocodes.webrtcclient.Permissions.PermissionManager;
import kore.botssdk.audiocodes.webrtcclient.Permissions.PermissionRequest;
import kore.botssdk.net.RestResponse;
import kore.botssdk.net.SDKConfig;
import kore.botssdk.net.SDKConfiguration;
import kore.botssdk.utils.BundleUtils;

@SuppressWarnings("UnKnownNullness")
public class MainActivity extends AppCompatActivity {

    String TAG = MainActivity.class.getName();
    static boolean allPermissionsGranted = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SDKConfig.setQueryParams(getQueryParams());
        SDKConfig.setCustomData(getCustomData());
//        SDKConfig.setCustomContentFragment(new CustomContentFragment());
//        SDKConfig.setCustomFooterFragment(new CustomFooterFragment());
//        SDKConfig.setCustomHeaderFragment(BotResponse.HEADER_SIZE_COMPACT, new CustomHeaderFragment());

        //If token is empty sdk token generation will happen. if not empty we will use this token for bot connection.
        String jwtToken = "";

        //Set clientId, If jwtToken is empty this value is mandatory
        String clientId = "PLEASE_ENTER_BOT_CLIENT_ID";

        //Set clientSecret, If jwtToken is empty this value is mandatory
        String clientSecret = "PLEASE_ENTER_BOT_CLIENT_SECRET";

        //Set botId, This value is mandatory
        String botId = "PLEASE_ENTER_BOT_ID";

        //Set identity, This value is mandatory
        String identity = "PLEASE_ENTER_IDENTITY";

        //Set botName, This value is mandatory
        String botName = "PLEASE_ENTER_BOT_NAME";

        //Set serverUrl, This value is mandatory
        String serverUrl = "PLEASE_ENTER_SERVER_URL";

        //Set brandingUrl, This value is mandatory
        String brandingUrl = "PLEASE_ENTER_BRANDING_SERVER_URL";

        //Set jwtServerUrl, This value is mandatory
        String jwtServerUrl = "PLEASE_ENTER_JWT_SERVER_URL";

        //Set isWebHook
        SDKConfig.isWebHook(false);

        //Initialize the bot with bot config
        //You can pass client id and client secret as empty when you pass jwt token
        SDKConfig.initialize(botId, botName, clientId, clientSecret, identity, jwtToken, serverUrl, brandingUrl, jwtServerUrl);

        //Inject the custom template like below
        SDKConfig.setCustomTemplateViewHolder("link", LinkTemplateHolder.class);

        //Flag to show the bot icon beside the bot response
        SDKConfig.setIsShowIcon(true);

        //Flag to show the bot icon in top position or bottom of the bot response
        SDKConfig.setIsShowIconTop(true);

        //Flag to show timestamp of each bot and user messages
        SDKConfig.setIsTimeStampsRequired(true);

        //Flag to show widget panel
        SDKConfig.enableWidgetPanel(false);

        Button launchBotBtn = findViewById(R.id.launchBotBtn);
        launchBotBtn.setOnClickListener(view -> launchBotChatActivity());

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
                String permission = permissions[i];
                int grantResult = grantResults[i];
                boolean isLastPermission = i == (permissions.length - 1);

                Log.d(TAG, "onRequestPermissionsResult: permission: " + permission);
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
        Intent intent = new Intent(getApplicationContext(), NewBotChatActivity.class);
        Bundle bundle = new Bundle();
        //This should not be null
        bundle.putBoolean(BundleUtils.SHOW_PROFILE_PIC, false);
        bundle.putString(BundleUtils.BOT_NAME_INITIALS, String.valueOf(SDKConfiguration.Client.bot_name.charAt(0)));
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @SuppressLint("UnknownNullness")
    public HashMap<String, Object> getQueryParams() {
        HashMap<String, Object> queryParams = new HashMap<>();
        queryParams.put("q1", true);
        queryParams.put("q2", 4);
        queryParams.put("q3", "connect");
        return queryParams;
    }

    @SuppressLint("UnknownNullness")
    public RestResponse.BotCustomData getCustomData() {
        RestResponse.BotCustomData customData = new RestResponse.BotCustomData();
        customData.put("name", "Kore Bot");
        customData.put("emailId", "emailId");
        customData.put("mobile", "mobile");
        customData.put("accountId", "accountId");
        customData.put("timeZoneOffset", -330);
        customData.put("UserTimeInGMT", TimeZone.getDefault().getID() + " " + Locale.getDefault().getISO3Language());
        return customData;
    }

    public String getConfigValue(String name) {
        try {
            InputStream rawResource = getResources().openRawResource(R.raw.config);
            Properties properties = new Properties();
            properties.load(rawResource);
            return properties.getProperty(name);
        } catch (Resources.NotFoundException e) {
            Log.e(MainActivity.class.getSimpleName(), "Unable to find the config file: " + e.getMessage());
        } catch (IOException e) {
            Log.e(MainActivity.class.getSimpleName(), "Failed to open config file.");
        }

        return null;
    }
}