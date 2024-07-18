package com.kore.korebot;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.kore.korebot.customtemplates.LinkTemplateView;

import java.util.HashMap;
import java.util.Locale;
import java.util.TimeZone;
import java.util.UUID;

import kore.botssdk.activity.BotChatActivity;
import kore.botssdk.net.RestResponse;
import kore.botssdk.net.SDKConfig;
import kore.botssdk.net.SDKConfiguration;
import kore.botssdk.utils.BundleUtils;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SDKConfiguration.Server.setQueryParams(getQueryParams());
        SDKConfiguration.Server.setCustomData(getCustomData());

        //If token is empty sdk token generation will happen. if not empty we will use this token for bot connection.
        String jwtToken = "";

        //Set clientId, If jwtToken is empty this value is mandatory
        String clientId = "";

        //Set clientSecret, If jwtToken is empty this value is mandatory
        String clientSecret = "";

        //Set botId, This value is mandatory
        String botId = "";

        //Set identity, This value is mandatory
        String identity = "";

        //Set botName, This value is mandatory
        String botName = "Kore.ai Bot";

        //Set serverUrl, This value is mandatory
        String serverUrl = "";

        //Set brandingUrl, This value is mandatory
        String brandingUrl = "";

        //Set Server url
        SDKConfig.setServerUrl(serverUrl);
        //Set Branding url
        SDKConfig.setBrandingUrl(brandingUrl);

        //Initialize the bot with bot config
        //You can pass client id and client secret as empty when you pass jwt token
        SDKConfig.initialize(botId, botName, clientId, clientSecret, identity, jwtToken);

        //Inject the custom template like below
        SDKConfig.setCustomTemplateView("link", new LinkTemplateView(MainActivity.this));

        //Flag to show the bot icon beside the bot response
        SDKConfiguration.BubbleColors.showIcon = false;

        //Flag to show the bot icon in top position or bottom of the bot response
        SDKConfiguration.BubbleColors.showIconTop = false;

        //Flag to show timestamp of each bot and user messages
        SDKConfiguration.setTimeStampsRequired(false);

        Button launchBotBtn = findViewById(R.id.launchBotBtn);
        launchBotBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchBotChatActivity();
            }
        });

    }

    /**
     * Launching BotChatActivity where user can interact with bot
     */
    void launchBotChatActivity() {
        Intent intent = new Intent(getApplicationContext(), BotChatActivity.class);
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
}