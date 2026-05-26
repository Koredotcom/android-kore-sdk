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
        intent.putExtra(SampleVoiceActivity.EXTRA_BOT_ID, "st-f016d7ce-6bc2-5989-9171-0c02c157524a");
        intent.putExtra(SampleVoiceActivity.EXTRA_CLIENT_ID, "cs-7850e78a-61b6-55ee-92ff-09b39d9c669d");
        intent.putExtra(SampleVoiceActivity.EXTRA_CLIENT_SECRET, "hNN8sXHrhWesi11M5vh3aWmJ4buSPdtovVYE/i6XldM=");
        intent.putExtra(SampleVoiceActivity.EXTRA_IDENTITY, "user1@user.com");
        intent.putExtra(SampleVoiceActivity.EXTRA_WEBSOCKET_URL, "wss://savg-sbc1.kore.ai:8443/");
        intent.putExtra(SampleVoiceActivity.EXTRA_JWT_SERVICE_URL, "https://mk2r2rmj21.execute-api.us-east-1.amazonaws.com/dev/users/sts");
        intent.putExtra(SampleVoiceActivity.EXTRA_SERVER_URL, "https://platform.kore.ai");
        intent.putExtra(SampleVoiceActivity.EXTRA_SIP_DOMAIN, "unifiedxo-prod-savg.kore.ai");

        startActivity(intent);
        finish();
    }
}
