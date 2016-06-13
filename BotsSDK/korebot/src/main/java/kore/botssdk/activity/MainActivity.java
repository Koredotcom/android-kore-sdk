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

    Button anonymousLoginBtn;
    Button normalLoginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViews();
        setListeners();
        clearPref();
        getSupportActionBar().setSubtitle("Login");
    }

    private void findViews() {
        anonymousLoginBtn = (Button) findViewById(R.id.anonymousLoginBtn);
        normalLoginBtn = (Button) findViewById(R.id.normalLoginBtn);
    }

    private void setListeners() {
        anonymousLoginBtn.setOnClickListener(anonymousLoginBtnOnClickListener);
        normalLoginBtn.setOnClickListener(normalLoginBtnOnClickListener);
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
        String userId = getResources().getString(R.string.demo_user_id);
        String authToken = getResources().getString(R.string.demo_auth_token);

        boolean successfullySaved = BotSharedPreferences.saveCredsToPreferences(MainActivity.this, userId, authToken);

        if (successfullySaved) {
            launchBotHomeActivity(isAnonymous);
        }
    }

    /**
     * End of : Utility Methods
     */

}
