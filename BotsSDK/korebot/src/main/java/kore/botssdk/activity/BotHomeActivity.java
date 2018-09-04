package kore.botssdk.activity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import java.util.UUID;

import kore.botssdk.R;
import kore.botssdk.models.JWTTokenResponse;
import kore.botssdk.net.BotDemoRestService;
import kore.botssdk.net.BotRestService;
import kore.botssdk.net.JWTGrantRequest;
import kore.botssdk.net.SDKConfiguration;
import kore.botssdk.utils.BundleUtils;

/**
 * Created by Pradeep Mahato on 31-May-16.
 * Copyright (c) 2014 Kore Inc. All rights reserved.
 */
public class BotHomeActivity extends BotAppCompactActivity {

    private Button launchBotBtn;
    private SpiceManager spiceManager = new SpiceManager(BotRestService.class);
    private SpiceManager spiceManagerForJWT = new SpiceManager(BotDemoRestService.class);


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bot_home_activity_layout);

        findViews();
        setListeners();
//        getJWTToken();
    }

    private void findViews() {

        launchBotBtn = (Button) findViewById(R.id.launchBotBtn);
        launchBotBtn.setText(SDKConfiguration.Client.bot_name);
    }

    private void setListeners() {
        launchBotBtn.setOnClickListener(launchBotBtnOnClickListener);

    }

    @Override
    protected void onStart() {
        super.onStart();
        spiceManager.start(getApplicationContext());
        spiceManagerForJWT.start(getApplicationContext());
    }



    @Override
    protected void onStop() {
        spiceManager.shouldStop();
        spiceManagerForJWT.shouldStop();
        super.onStop();
    }

    /**
     * START of : Listeners
     */

    View.OnClickListener launchBotBtnOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (isOnline()) {
                showProgress("",true);
                getJWTToken();
            } else {
                Toast.makeText(BotHomeActivity.this, "No internet connectivity", Toast.LENGTH_SHORT).show();
            }
        }
    };

    /**
     * This Service call to generate JWT (JSON Web Tokens)- this service will be used in the assertion function injected to obtain the connection.
     */

    private void getJWTToken(){
        JWTGrantRequest request = new JWTGrantRequest(SDKConfiguration.Client.client_id,
                SDKConfiguration.Client.client_secret,SDKConfiguration.Server.IS_ANONYMOUS_USER? UUID.randomUUID().toString():SDKConfiguration.Client.identity,SDKConfiguration.Server.IS_ANONYMOUS_USER);
        spiceManagerForJWT.execute(request, new RequestListener<JWTTokenResponse>() {
            @Override
            public void onRequestFailure(SpiceException e) {
                dismissProgress();
                Toast.makeText(BotHomeActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRequestSuccess(JWTTokenResponse jwt) {
                dismissProgress();
                launchBotChatActivity(jwt.getJwt());
            }
        });
    }

    /**
     * Launching BotchatActivity where user can interact with bot
     * @param jwt
     */
    private void launchBotChatActivity(String jwt){
        Intent intent = new Intent(getApplicationContext(), BotChatActivity.class);
        Bundle bundle = new Bundle();
        //This should not be null
        bundle.putString(BundleUtils.JWT_TOKEN, jwt);
        bundle.putBoolean(BundleUtils.SHOW_PROFILE_PIC, false);
        bundle.putString(BundleUtils.BOT_NAME_INITIALS,SDKConfiguration.Client.bot_name.charAt(0)+"");
        intent.putExtras(bundle);

        startActivity(intent);
    }

    protected boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnected()) {
            return true;
        } else {
            return false;
        }
    }
}
