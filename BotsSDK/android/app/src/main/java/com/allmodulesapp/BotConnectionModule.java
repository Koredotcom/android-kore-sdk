package com.allmodulesapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.util.Log;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

import java.util.UUID;

import kore.botssdk.activity.BotChatActivity;
import kore.botssdk.listener.BotSocketConnectionManager;
import kore.botssdk.net.SDKConfiguration;

public class BotConnectionModule extends ReactContextBaseJavaModule {
    private Context context;
    private SharedPreferences sharedPreferences;
    private String preferenceName = "MashreqPreferences";
    private String BOT_ID = "BOT_ID";
    private String BOT_NAME = "BOT_NAME";
    private String IDENTITY = "IDENTITY";
    private String AUTHORIZATION = "AUTHORIZATION";
    private String XAUTH = "XAUTH";

    BotConnectionModule(ReactApplicationContext context) {
        super(context);
        this.context = context;
        sharedPreferences = context.getSharedPreferences(preferenceName, Context.MODE_PRIVATE);
    }

    @ReactMethod
    public void initialize(String bot_id, String bot_name, String authorisation, String xauth, String identity)
    {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(BOT_ID , bot_id);
        editor.putString(BOT_NAME, bot_name);
        editor.putString(AUTHORIZATION, authorisation);
        editor.putString(XAUTH, xauth);
        editor.putString(IDENTITY, identity);
        editor.commit();
    }

    @ReactMethod
    public void show()
    {
        SDKConfiguration.Client.bot_id = sharedPreferences.getString(BOT_ID, "");
        SDKConfiguration.Client.bot_name = sharedPreferences.getString(BOT_NAME, "");
        SDKConfiguration.Client.identity = sharedPreferences.getString(IDENTITY, "");
        SDKConfiguration.Client.authorization = sharedPreferences.getString(AUTHORIZATION, "");
        SDKConfiguration.Client.xauth = sharedPreferences.getString(XAUTH, "");

        BotSocketConnectionManager.getInstance().startAndInitiateConnectionWithConfig(context, SDKConfiguration.Client.authorization, SDKConfiguration.Client.xauth);

        Intent intent = new Intent(context, BotChatActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    public String getName() {
        return "BotConnectionModule";
    }
}

