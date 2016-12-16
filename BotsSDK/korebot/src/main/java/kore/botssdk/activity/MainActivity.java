package kore.botssdk.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import kore.botssdk.R;
import kore.botssdk.net.SDKConfiguration;
import kore.botssdk.utils.BotSharedPreferences;
import kore.botssdk.utils.BundleUtils;
import kore.botssdk.utils.Contants;
import kore.botssdk.utils.CustomToast;
import kore.botssdk.net.LoginRequest;
import kore.botssdk.net.RestResponse;

/**
 * Created by Pradeep Mahato on 26-May-16.
 * Copyright (c) 2014 Kore Inc. All rights reserved.
 */
public class MainActivity extends AppCompatActivity {

    private Button startUsingBot;
    private TextView txtBotName;
    private boolean isAnonymous;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        isAnonymous = SDKConfiguration.Server.IS_ANONYMOUS_USER;
        findViewsAndSetListeners();
        clearPref();
        getSupportActionBar().setSubtitle("Bot");
    }

    private void findViewsAndSetListeners() {
        startUsingBot = (Button) findViewById(R.id.startUsingBot);
        startUsingBot.setOnClickListener(startUsingBotBtnOnClickListener);

       /* normalLoginBtn = (Button) findViewById(R.id.normalLoginBtn);
        normalLoginBtn.setOnClickListener(normalLoginBtnOnClickListener);*/

        txtBotName = (TextView) findViewById(R.id.txtBotName);
        txtBotName.setText(SDKConfiguration.Client.chatBotName);


    }

    private void clearPref() {
        BotSharedPreferences.clearPreferences(MainActivity.this);
    }

    private boolean isAlreadyLoggedIn() {
        return BotSharedPreferences.getAccessTokenFromPreferences(getApplicationContext()) != null
                && BotSharedPreferences.getUserIdFromPreferences(getApplicationContext()) != null;
    }

    @Override
    protected void onResume() {
        super.onResume();
        clearPref();
    }

    /**
     * Start of : Listeners
     */

    View.OnClickListener startUsingBotBtnOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(isAnonymous) {
                launchBotChatActivity(isAnonymous);
            }else{
                BotSharedPreferences.clearPreferences(MainActivity.this);
                if (isAlreadyLoggedIn()) {
                    launchBotChatActivity(false);
                    finish();
                } else {
                    saveToPrefAndLaunch(false);
                }
            }
        }
    };



    /**
     * End of : Listeners
     */

    /**
     * Start of : Utility Methods
     */


    private void launchBotChatActivity(boolean isAnonymous) {

        Intent intent = new Intent(getApplicationContext(), BotChatActivity.class);

        Bundle bundle = new Bundle();
        bundle.putString(BundleUtils.LOGIN_MODE, (isAnonymous) ? Contants.ANONYMOUS_FLOW : Contants.NORMAL_FLOW);
        bundle.putBoolean(BundleUtils.SHOW_PROFILE_PIC, false);
        intent.putExtras(bundle);

        startActivity(intent);

    }

    private void saveToPrefAndLaunch(boolean isAnonymous) {
        boolean successfullySaved = BotSharedPreferences.saveCredsToPreferences(MainActivity.this, SDKConfiguration.Client.demo_user_id, SDKConfiguration.Client.demo_auth_token);

        if (successfullySaved) {
            launchBotChatActivity(isAnonymous);
        }
    }

    /**
     * End of : Utility Methods
     */

}
