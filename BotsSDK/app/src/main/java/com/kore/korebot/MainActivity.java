package com.kore.korebot;

import static android.Manifest.permission.POST_NOTIFICATIONS;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.kore.korebot.customtemplates.LinkTemplateHolder;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;
import java.util.Properties;
import java.util.TimeZone;

import kore.botssdk.activity.NewBotChatActivity;
import kore.botssdk.models.BrandingModel;
import kore.botssdk.net.RestResponse;
import kore.botssdk.net.SDKConfig;
import kore.botssdk.net.SDKConfiguration;
import kore.botssdk.utils.BundleUtils;
import kore.botssdk.utils.LangUtils;
import kore.botssdk.utils.LogUtils;

public class MainActivity extends AppCompatActivity {
    String botId, clientSecret, botName, serverUrl;
    String jwtToken, clientId, identity, brandingUrl, jwtServerUrl;

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
        String clientId = getConfigValue("clientId");

        //Set clientSecret, If jwtToken is empty this value is mandatory
        String clientSecret = getConfigValue("clientSecret");

        //Set botId, This value is mandatory
        String botId = getConfigValue("botId");

        //Set identity, This value is mandatory
        String identity = getConfigValue("identity");

        //Set botName, This value is mandatory
        String botName = getConfigValue("botName");

        //Set serverUrl, This value is mandatory
        String serverUrl = getConfigValue("serverUrl");

        //Set brandingUrl, This value is mandatory
        String brandingUrl = getConfigValue("brandingUrl");

        //Set jwtServerUrl, This value is mandatory
        String jwtServerUrl = getConfigValue("jwtServerUrl");

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

        //Set local branding model by overriding the branding api response
        SDKConfig.setLocalBranding(false, getLocalBrandingModel());

        //Method to reset the bot connection and start a new session by overriding the previous state
        // SDKConfig.disconnectBotSession(MainActivity.this);

        SDKConfiguration.OverrideKoreConfig.showAttachment = true;
        SDKConfiguration.OverrideKoreConfig.showASRMicroPhone = true;
        SDKConfiguration.OverrideKoreConfig.showTextToSpeech = true;

        //Enable the flag if the bot needs to support Emoji short cuts decryption
        SDKConfiguration.OverrideKoreConfig.isEmojiShortcutEnable = false;

        Button launchBotBtn = findViewById(R.id.launchBotBtn);
        launchBotBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchBotChatActivity();
            }
        });

//        askNotificationPermission();
    }

    // You can do the assignment inside onAttach or onCreate, i.e, before the activity is displayed
    ActivityResultLauncher<Intent> chatActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes
                        Intent data = result.getData();
                        if (data != null && data.hasExtra(BundleUtils.CHAT_BOT_CLOSE_OR_MINIMIZED)) {
                            LogUtils.e("ChatBot is", Objects.requireNonNull(data.getStringExtra(BundleUtils.CHAT_BOT_CLOSE_OR_MINIMIZED)));
                        }
                    }
                }
            });

    /**
     * Launching BotChatActivity where user can interact with bot
     */
    void launchBotChatActivity() {
        Intent intent = new Intent(getApplicationContext(), NewBotChatActivity.class);
        Bundle bundle = new Bundle();
        //This should not be null
        bundle.putString(BundleUtils.BOT_NAME_INITIALS, String.valueOf(SDKConfiguration.Client.bot_name.charAt(0)));
        intent.putExtras(bundle);
        chatActivityResultLauncher.launch(intent);
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

    private BrandingModel getLocalBrandingModel()
    {
        BrandingModel brandingModel = new BrandingModel();
        brandingModel.setBotchatBgColor("#F3F5F8");
        brandingModel.setBotchatTextColor("#202124");
        brandingModel.setButtonActiveBgColor("#0D6EFD");
        brandingModel.setButtonActiveTextColor("#ffffff");
        brandingModel.setButtonInactiveBgColor("#0659d2");
        brandingModel.setButtonInactiveTextColor("#ffffff");
        brandingModel.setUserchatBgColor("#0D6EFD");
        brandingModel.setUserchatTextColor("#ffffff");
        brandingModel.setWidgetHeaderColor("#0D6EFD");
        brandingModel.setWidgetFooterColor("#FFFFFF");
        brandingModel.setWidgetFooterBorderColor("#E4E5E7");
        brandingModel.setWidgetFooterHintColor("#262626");
        brandingModel.setWidgetFooterHintText("Type your message...");
        brandingModel.setWidgetTextColor("#ffffff");
        brandingModel.setWidgetBorderColor("#E4E5E7");
        brandingModel.setButtonBorderColor("#0D6EFD");
        brandingModel.setWidgetBodyColor("#ffffff");
        brandingModel.setBotName("Bot Name");
        brandingModel.setChatBubbleStyle("square");
        return brandingModel;
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

    @Override
    protected void onDestroy() {
        SDKConfig.disconnectBotSession(MainActivity.this);
        super.onDestroy();
    }
}