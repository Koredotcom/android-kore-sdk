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
        intent.putExtra(SampleVoiceActivity.EXTRA_BOT_ID, "st-3e2406df-8df0-5b0a-81d9-7862fe1c718a");
        intent.putExtra(SampleVoiceActivity.EXTRA_CLIENT_ID, "cs-9d78f6d6-3d70-57f8-8f78-eb93bef11dd3");
        intent.putExtra(SampleVoiceActivity.EXTRA_CLIENT_SECRET, "pZwmJd164NifbmXJKyJhG3rxzc3rSdn8OAuvcdP1k+0=");
        intent.putExtra(SampleVoiceActivity.EXTRA_IDENTITY, "user1@user.com");
        intent.putExtra(SampleVoiceActivity.EXTRA_WEBSOCKET_URL, "wss://savg-sbc1.kore.ai:8443/");// wss://aaaa-aaa1.aaa.aa:<port number>/
        intent.putExtra(SampleVoiceActivity.EXTRA_JWT_SERVICE_URL, "https://mk2r2rmj21.execute-api.us-east-1.amazonaws.com/dev/users/sts");
        intent.putExtra(SampleVoiceActivity.EXTRA_SERVER_URL, "https://platform.kore.ai");
        intent.putExtra(SampleVoiceActivity.EXTRA_SIP_DOMAIN, "unifiedxo-prod-savg.kore.ai"); //xxxxxxxx-prod-xxxx.xxxx.xx

        startActivity(intent);
        finish();
    }
}
