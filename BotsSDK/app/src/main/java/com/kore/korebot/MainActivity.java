package com.kore.korebot;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.kore.korebot.customtemplates.LinkTemplateHolder;

import java.util.HashMap;

import kore.botssdk.activity.NewBotChatActivity;
import kore.botssdk.models.NotificationModel;
import kore.botssdk.net.RestResponse;
import kore.botssdk.net.SDKConfig;
import kore.botssdk.net.SDKConfiguration;
import kore.botssdk.pushnotification.PushNotificationRegister;
import kore.botssdk.utils.BundleUtils;
import kore.botssdk.utils.LangUtils;
import kore.botssdk.utils.LogUtils;
import kore.botssdk.utils.StringUtils;
import kore.botssdk.websocket.BotStatusListener;

@SuppressLint("HardcodedPassword")
public class MainActivity extends AppCompatActivity implements BotStatusListener {

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
        String clientId = "YOUR_CLIENT_ID";

        //Set clientSecret, If jwtToken is empty this value is mandatory
        String clientSecret = "YOUR_CLIENT_SECRET";

        //Set botId, This value is mandatory
        String botId = "YOUR_BOT_ID";

        //Set identity, This value is mandatory
        String identity = "YOUR_IDENTITY";

        //Set botName, This value is mandatory
        String botName = "YOUR_BOT_NAME";

        //Set serverUrl, This value is mandatory
        String serverUrl = "YOUR_SERVER_URL";

        //Set brandingUrl, This value is mandatory
        String brandingUrl = "YOUR_BRANDING_SERVER_URL";

        //Set jwtServerUrl, This value is mandatory
        String jwtServerUrl = "YOUR_JWT_SERVER_URL";

        //Set isWebHook
        SDKConfig.isWebHook(false);

        //Initialize the bot with bot config
        //You can pass client id and client secret as empty when you pass jwt token
        SDKConfig.initialize(botId, botName, clientId, clientSecret, identity, jwtToken, serverUrl, brandingUrl, jwtServerUrl);

        //You can pass the custom data to the bot by using this method. Can get sample format from the mentioned method
        SDKConfig.setCustomData(getCustomData(jwtToken));

        //You can set query parameters to the socket url by using this method. Can get sample format from the mentioned method
        SDKConfig.setQueryParams(getQueryParams());

        //Inject the custom template like below
        SDKConfig.setCustomTemplateViewHolder("link", LinkTemplateHolder.class);

        //Set the listener to the SDK to get the Bot callbacks to the parent application
        SDKConfig.setBotStatusUpdateListener(this);

        Button launchBotBtn = findViewById(R.id.launchBotBtn);
        launchBotBtn.setOnClickListener(view -> launchBotChatActivity());
    }

    // You can do the assignment inside onAttach or onCreate, i.e, before the activity is displayed
    ActivityResultLauncher<Intent> chatActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    // There are no request codes
                    Intent data = result.getData();
                    if (data != null && data.hasExtra(BundleUtils.CHAT_BOT_CLOSE_OR_MINIMIZED)) {
                        String chatBotStatus = data.getStringExtra(BundleUtils.CHAT_BOT_CLOSE_OR_MINIMIZED);
                        if (chatBotStatus != null) {
                            LogUtils.e("ChatBot is", chatBotStatus);
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
        String botInitial = "B";
        if (SDKConfiguration.Client.bot_name != null && !SDKConfiguration.Client.bot_name.isEmpty()) {
            botInitial = String.valueOf(SDKConfiguration.Client.bot_name.charAt(0));
        }
        bundle.putString(BundleUtils.BOT_NAME_INITIALS, botInitial);
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
    public RestResponse.BotCustomData getCustomData(String jwtToken) {
        RestResponse.BotCustomData customData = new RestResponse.BotCustomData();
        customData.put("jwt_token", jwtToken);
        return customData;
    }

    @Override
    protected void onDestroy() {

        NotificationModel notificationModel = SDKConfiguration.Server.getNotificationModel();
        if (!SDKConfiguration.OverrideKoreConfig.default_notifications && notificationModel != null && StringUtils.isNotEmpty(notificationModel.getDeviceId()) &&
                StringUtils.isNotEmpty(notificationModel.getUserId()) && StringUtils.isNotEmpty(notificationModel.getAccessToken()))
            new PushNotificationRegister().unsubscribePushNotification(notificationModel.getUserId(), notificationModel.getAccessToken(), notificationModel.getDeviceId());

        super.onDestroy();
    }

    @Override
    public void onBotConnected() {
        LogUtils.e("Bot Current Status", "Bot Connected");

    }

    @Override
    public void onBotDisconnected(String event_code, String event_message) {
        LogUtils.e(event_code, event_message);
    }

    @Override
    public void onBotConnectionFail(String event_code, String event_message) {
        LogUtils.e(event_code, event_message);
    }

    @Override
    public void onDeepLinkClicked(String event_code, String url) {
        LogUtils.e(event_code, url);
    }

    @Override
    public void onBotMessageReceived(String event_code, String message) {
        LogUtils.e(event_code, message);
    }

    @Override
    public void onSessionEnded(String event_code, String message) {
        LogUtils.e(event_code, message);
    }
}
