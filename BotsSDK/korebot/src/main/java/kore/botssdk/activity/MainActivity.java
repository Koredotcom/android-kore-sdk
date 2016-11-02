package kore.botssdk.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

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

    private Button anonymousLoginBtn;
    private Button normalLoginBtn;
    private boolean isAnonymous;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        isAnonymous = SDKConfiguration.Server.IS_ANONYMOUS_USER;
        findViewsAndSetListeners();
        clearPref();
        getSupportActionBar().setSubtitle("Login");
    }

    private void findViewsAndSetListeners() {
        anonymousLoginBtn = (Button) findViewById(R.id.anonymousLoginBtn);
        anonymousLoginBtn.setOnClickListener(anonymousLoginBtnOnClickListener);

        normalLoginBtn = (Button) findViewById(R.id.normalLoginBtn);
        normalLoginBtn.setOnClickListener(normalLoginBtnOnClickListener);

        if(isAnonymous)normalLoginBtn.setVisibility(View.GONE);
        else anonymousLoginBtn.setVisibility(View.GONE);
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

    View.OnClickListener anonymousLoginBtnOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            launchBotHomeActivity(true);
        }
    };

    View.OnClickListener normalLoginBtnOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (isAlreadyLoggedIn()) {
                launchBotHomeActivity(false);
                finish();
            } else {
                saveToPrefAndLaunch(false);
            }
        }
    };

    /**
     * End of : Listeners
     */

    /**
     * Start of : Utility Methods
     */


    private void launchBotHomeActivity(boolean isAnonymous) {
        Intent intent = new Intent(getApplicationContext(), BotHomeActivity.class);

        Bundle bundle = new Bundle();
        bundle.putString(BundleUtils.LOGIN_MODE, (isAnonymous) ? Contants.ANONYMOUS_FLOW : Contants.NORMAL_FLOW);
        intent.putExtras(bundle);

        startActivity(intent);
    }

    private void saveToPrefAndLaunch(boolean isAnonymous) {
        boolean successfullySaved = BotSharedPreferences.saveCredsToPreferences(MainActivity.this, SDKConfiguration.Client.demo_user_id, SDKConfiguration.Client.demo_auth_token);

        if (successfullySaved) {
            launchBotHomeActivity(isAnonymous);
        }
    }

    /**
     * End of : Utility Methods
     */

}
