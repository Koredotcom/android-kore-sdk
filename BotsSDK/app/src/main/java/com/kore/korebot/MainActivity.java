package com.kore.korebot;

import static android.Manifest.permission.POST_NOTIFICATIONS;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.kore.korebot.customtemplates.LinkTemplateHolder;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Locale;
import java.util.Properties;
import java.util.TimeZone;

import kore.botssdk.activity.NewBotChatActivity;
import kore.botssdk.net.RestResponse;
import kore.botssdk.net.SDKConfig;
import kore.botssdk.net.SDKConfiguration;
import kore.botssdk.utils.BundleUtils;
import kore.botssdk.utils.LangUtils;

public class MainActivity extends AppCompatActivity {
    String botId, clientSecret, botName, serverUrl;
    String jwtToken, clientId, identity, brandingUrl, jwtServerUrl;
    String localBranding = "{\n" +
            "   \"_id\":\"wsth-793c50ac-ebe1-53c0-853a-065f41d7c683\",\n" +
            "   \"streamId\":\"st-89185ffe-d861-54c4-b3b9-8f182997b2be\",\n" +
            "   \"__v\":0,\n" +
            "   \"activeTheme\":true,\n" +
            "   \"botMessage\":{\n" +
            "      \"bubbleColor\":\"#445877\",\n" +
            "      \"fontColor\":\"#fbfcff\",\n" +
            "      \"borderColor\":\"#F3F5F8\"\n" +
            "   },\n" +
            "   \"buttons\":{\n" +
            "      \"defaultButtonColor\":\"#0dd6fd\",\n" +
            "      \"defaultFontColor\":\"#ffffff\",\n" +
            "      \"onHoverButtonColor\":\"#0659d2\",\n" +
            "      \"onHoverFontColor\":\"#ffffff\",\n" +
            "      \"borderColor\":\"#0D6EFD\"\n" +
            "   },\n" +
            "   \"createdBy\":\"u-f37609a4-6430-5f4c-8724-abfc08e174ae\",\n" +
            "   \"createdOn\":\"2025-02-11T05:37:19.516Z\",\n" +
            "   \"defaultTheme\":false,\n" +
            "   \"digitalViews\":{\n" +
            "      \"panelTheme\":\"theme_one\"\n" +
            "   },\n" +
            "   \"generalAttributes\":{\n" +
            "      \"bubbleShape\":\"square\",\n" + //circle or square
            "      \"borderColor\":\"#F3F5F8\"\n" +
            "   },\n" +
            "   \"lastModifiedBy\":\"u-f37609a4-6430-5f4c-8724-abfc08e174ae\",\n" +
            "   \"lastModifiedOn\":\"2025-02-11T05:36:54.512Z\",\n" +
            "   \"refId\":\"8c3dc424-f82d-5e06-83b1-ecb9390a03e0\",\n" +
            "   \"state\":\"published\",\n" +
            "   \"themeName\":\"New Theme\",\n" +
            "   \"userMessage\":{\n" +
            "      \"bubbleColor\":\"#fdc80d\",\n" +
            "      \"fontColor\":\"#a10808\",\n" +
            "      \"borderColor\":\"#0D6EFD\"\n" +
            "   },\n" +
            "   \"widgetBody\":{\n" +
            "      \"backgroundImage\":\"\",\n" +
            "      \"backgroundColor\":\"#ffffff\",\n" +
            "      \"useBackgroundImage\":false\n" +
            "   },\n" +
            "   \"widgetFooter\":{\n" +
            "      \"backgroundColor\":\"#ffa4a4\",\n" +
            "      \"fontColor\":\"#4e4c4c\",\n" +
            "      \"borderColor\":\"#E4E5E7\",\n" +
            "      \"placeHolder\":\"#6b3e3e\"\n" +
            "   },\n" +
            "   \"widgetHeader\":{\n" +
            "      \"backgroundColor\":\"#fb0dfd\",\n" +
            "      \"fontColor\":\"#ffffff\",\n" +
            "      \"borderColor\":\"#e5e8ec\"\n" +
            "   }\n" +
            "}";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        //Can set Language for Bot SDK
        LangUtils.setAppLanguages(this, LangUtils.LANG_EN);

//        Can set your customized Header view in the chat window by using this method. By extending BaseHeaderFragment. Can find examples under fragments package
//        SDKConfig.addCustomHeaderFragment(new CustomHeaderFragment());

//        Can set your customized Content view in the chat window by using this method. By extending BaseContentFragment. Can find examples under fragments package
//        SDKConfig.addCustomContentFragment(new CustomContentFragment());

//        Can set your customized Footer view in the chat window by using this method. By extending BaseFooterFragment. Can find examples under fragments package
//        SDKConfig.addCustomFooterFragment(new CustomFooterFragment());

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

        //You can pass the custom data to the bot by using this method. Can get sample format from the mentioned method
        SDKConfig.setCustomData(getCustomData());

        //You can set query parameters to the socket url by using this method. Can get sample format from the mentioned method
        SDKConfig.setQueryParams(getQueryParams());

        //Inject the custom template like below
        SDKConfig.setCustomTemplateViewHolder("link", LinkTemplateHolder.class);

        //Flag to show the bot icon beside the bot response
        SDKConfig.setIsShowIcon(true);

        //Flag to show the bot icon in top position or bottom of the bot response
        SDKConfig.setIsShowIconTop(false);

        //Flag to show timestamp of each bot and user messages
        SDKConfig.setIsTimeStampsRequired(true);

        //Flag to show bot header or hide the header
        SDKConfig.setIsShowHeader(true);

        //Set local branding by overriding the branding api response
        SDKConfig.setLocalBranding(true, localBranding);

        SDKConfiguration.OverrideKoreConfig.showAttachment = true;
        SDKConfiguration.OverrideKoreConfig.showASRMicroPhone = true;
        SDKConfiguration.OverrideKoreConfig.showTextToSpeech = true;

        Button launchBotBtn = findViewById(R.id.launchBotBtn);
        launchBotBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchBotChatActivity();
            }
        });

//        askNotificationPermission();
    }

    /**
     * Launching BotChatActivity where user can interact with bot
     */
    void launchBotChatActivity() {
        Intent intent = new Intent(getApplicationContext(), NewBotChatActivity.class);
        Bundle bundle = new Bundle();
        //This should not be null
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

    // Declare the launcher at the top of your Activity/Fragment:
    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (!isGranted) {
                    Toast.makeText(this, "Permission needed to send push notifications", Toast.LENGTH_SHORT).show();
                }
            });

    private void askNotificationPermission() {
        // This is only necessary for API level >= 33 (TIRAMISU)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, POST_NOTIFICATIONS) !=
                    PackageManager.PERMISSION_GRANTED) {
                // FCM SDK (and your app) can post notifications.
                requestPermissionLauncher.launch(POST_NOTIFICATIONS);
            }
        }
    }

}