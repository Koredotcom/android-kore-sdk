/*
package kore.botssdk.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import java.util.HashMap;

import kore.botssdk.R;
import kore.botssdk.models.KoreLoginResponse;
import kore.botssdk.net.BotRestService;
import kore.botssdk.net.KoreLoginRequest;
import kore.botssdk.net.SDKConfiguration;
import kore.botssdk.utils.BundleUtils;

*/
/**
 * Created by Pradeep Mahato on 26-May-16.
 * Copyright (c) 2014 Kore Inc. All rights reserved.
 *//*

@Deprecated
public class MainActivity extends AppCompatActivity {

    private Button startUsingBot;
    private String userId, accessToken;
    private ProgressDialog mProgressDialog;
//    private TextView txtBotName;
    private boolean isAnonymous;
    SpiceManager spiceManager = new SpiceManager(BotRestService.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        isAnonymous = SDKConfiguration.Server.IS_ANONYMOUS_USER;
        findViewsAndSetListeners();
//        clearPref();
        getSupportActionBar().setSubtitle("Bot");
    }

    private void findViewsAndSetListeners() {
        startUsingBot = (Button) findViewById(R.id.startUsingBot);
        startUsingBot.setText("Chat with "+SDKConfiguration.Client.bot_name);
        startUsingBot.setOnClickListener(startUsingBotBtnOnClickListener);

       */
/* normalLoginBtn = (Button) findViewById(R.id.normalLoginBtn);
        normalLoginBtn.setOnClickListener(normalLoginBtnOnClickListener);*//*


//        txtBotName = (TextView) findViewById(R.id.txtBotName);
//        txtBotName.setText(SDKConfiguration.Config.chatBotName);


    }

   */
/* private void clearPref() {
        BotSharedPreferences.clearPreferences(MainActivity.this);
    }

    private boolean isAlreadyLoggedIn() {
        return BotSharedPreferences.getAccessTokenFromPreferences(getApplicationContext()) != null
                && BotSharedPreferences.getUserIdFromPreferences(getApplicationContext()) != null;
    }*//*


    protected void showProgress(String msg) {
        if (!isFinishing()) {
            if (mProgressDialog != null && mProgressDialog.isShowing())
                dismissProgress();

//            mProgressDialog = new ProgressDialog(getApplicationContext(), R.style.CompletelyTransparentTheme);
            mProgressDialog = ProgressDialog.show(this, getResources().getString(R.string.app_name), msg);
            mProgressDialog.setContentView(R.layout.progress_indicator);
            mProgressDialog.show();
        }
    }

    */
/**
     * Method to dismiss the progress dialog
     *//*

    protected void dismissProgress() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
//        clearPref();
    }

    @Override
    protected void onStart() {
        super.onStart();
        spiceManager.start(getApplicationContext());
    }

    @Override
    protected void onStop() {
        spiceManager.shouldStop();
        super.onStop();
    }

    */
/**
     * Start of : Listeners
     *//*


    View.OnClickListener startUsingBotBtnOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            launchBotChatActivity(isAnonymous);
        }
    };


    private void loginAndLaunchBotActivity() {
        showProgress("Please wait while logging in");
        HashMap<String, Object> credMap = new HashMap<String, Object>();
      //  credMap.put("username", SDKConfiguration.Config.email_id);
      //  credMap.put("password", SDKConfiguration.Config.password);
        credMap.put("scope", "friends");
        credMap.put("client_secret", "1");
        credMap.put("client_id", "1");
        credMap.put("grant_type", "password");

        KoreLoginRequest request = new KoreLoginRequest(credMap);

        spiceManager.execute(request, new RequestListener<KoreLoginResponse>() {
            @Override
            public void onRequestFailure(SpiceException e) {
                dismissProgress();
                Toast.makeText(MainActivity.this,"Error while log in",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onRequestSuccess(KoreLoginResponse koreLoginResponse) {
                dismissProgress();
                userId = koreLoginResponse.getUserInfo().getUserId();
                accessToken = koreLoginResponse.getAuthInfo().getAccessToken();
                launchBotChatActivity(false);
            }
        });

    }




    private void launchBotChatActivity(boolean isAnonymous) {

        Intent botChatActivityIntent = new Intent(getApplicationContext(), BotChatActivity.class);

        Bundle botChatActivityBundle = new Bundle();
        botChatActivityBundle.putString(BundleUtils.BOT_NAME, SDKConfiguration.Client.bot_name);
        botChatActivityBundle.putString(BundleUtils.BOT_ID, SDKConfiguration.Client.bot_id);
        botChatActivityBundle.putBoolean(BundleUtils.SHOW_PROFILE_PIC, true);
//        botChatActivityBundle.putString(BundleUtils.CHANNEL_ICON_URL,marketStreams.getIcon());
        botChatActivityIntent.putExtras(botChatActivityBundle);

        startActivity(botChatActivityIntent);

    }

    */
/*private void saveToPrefAndLaunch(boolean isAnonymous) {
//        boolean successfullySaved = BotSharedPreferences.saveCredsToPreferences(MainActivity.this, SDKConfiguration.Config.demo_user_id, SDKConfiguration.Config.demo_auth_token);

            launchBotChatActivity(isAnonymous);
    }*//*


    */
/**
     * End of : Utility Methods
     *//*


}
*/
