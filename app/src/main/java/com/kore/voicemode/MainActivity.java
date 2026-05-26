package com.kore.voicemode;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import kore.botssdk.voicemode.SampleVoiceActivity;

/**
 * Launcher activity that opens the WebRTC sample voice call UI.
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = new Intent(getApplicationContext(), SampleVoiceActivity.class);
//        intent.putExtra(SampleVoiceActivity.EXTRA_BOT_ID, "BOT_ID");
//        intent.putExtra(SampleVoiceActivity.EXTRA_CLIENT_ID, "CLIENT_ID");
//        intent.putExtra(SampleVoiceActivity.EXTRA_CLIENT_SECRET, "CLIENT_SECRET");
//        intent.putExtra(SampleVoiceActivity.EXTRA_IDENTITY, "user1@user.com");
//        intent.putExtra(SampleVoiceActivity.EXTRA_WEBSOCKET_URL, "WEB_SOCKET_URL");// wss://aaaa-aaa1.aaa.aa:<port number>/
//        intent.putExtra(SampleVoiceActivity.EXTRA_JWT_SERVICE_URL, "JWT_SERVICE_URL");
//        intent.putExtra(SampleVoiceActivity.EXTRA_SERVER_URL, "SERVER_URL");
//        intent.putExtra(SampleVoiceActivity.EXTRA_SIP_DOMAIN, "SIP_DOMAIN"); //xxxxxxxx-prod-xxxx.xxxx.xx

        startActivity(intent);
        finish();
    }
}
