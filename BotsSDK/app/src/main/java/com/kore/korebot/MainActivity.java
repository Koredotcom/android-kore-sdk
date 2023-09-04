package com.kore.korebot;

import static kore.botssdk.utils.BundleConstants.CAPTURE_IMAGE_BUNDLED_PREMISSION_REQUEST;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.kore.korebot.customviews.LinkTemplateView;

import kore.botssdk.activity.BotChatActivity;
import kore.botssdk.net.SDKConfig;
import kore.botssdk.utils.BundleUtils;
import kore.botssdk.utils.KaPermissionsHelper;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@NonNull Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkAppPermission();

        SDKConfig.setCustomTemplateView("link", new LinkTemplateView(MainActivity.this));

        SDKConfig.initialize(
                "st-b9889c46-218c-58f7-838f-73ae9203488c",
                "Sudheer Bot",
                "cs-1e845b00-81ad-5757-a1e7-d0f6fea227e9",
                "5OcBSQtH/k6Q/S6A3bseYfOee02YjjLLTNoT1qZDBso=",
                "anilkumar.routhu@kore.com"
        );

        Button launchBotBtn = findViewById(R.id.launchBotBtn);
        launchBotBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchBotChatActivity();
            }
        });

    }

    private void checkAppPermission()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if(!KaPermissionsHelper.hasPermission(MainActivity.this, Manifest.permission.READ_MEDIA_IMAGES))
            {
                KaPermissionsHelper.requestForPermission(this, CAPTURE_IMAGE_BUNDLED_PREMISSION_REQUEST,
                        Manifest.permission.READ_MEDIA_IMAGES, Manifest.permission.READ_MEDIA_VIDEO, Manifest.permission.READ_MEDIA_AUDIO);
            }
        }
    }

    /**
     * Launching BotchatActivity where user can interact with bot
     *
     */
    void launchBotChatActivity(){
        Intent intent = new Intent(getApplicationContext(), BotChatActivity.class);
        Bundle bundle = new Bundle();
        //This should not be null
        bundle.putBoolean(BundleUtils.SHOW_PROFILE_PIC, false);
        bundle.putString(BundleUtils.BOT_NAME_INITIALS,"B");
        intent.putExtras(bundle);

        startActivity(intent);
    }
}