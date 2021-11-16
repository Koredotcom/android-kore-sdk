package kore.botssdk.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

//import com.kore.ai.widgetsdk.activities.PanelMainActivity;

import java.util.Random;
import java.util.UUID;

import kore.botssdk.R;
import kore.botssdk.drawables.ThemeColors;
import kore.botssdk.listener.BotSocketConnectionManager;
import kore.botssdk.net.SDKConfiguration;
import kore.botssdk.utils.BundleUtils;
import kore.botssdk.utils.StringUtils;

/**
 * Created by Pradeep Mahato on 31-May-16.
 * Copyright (c) 2014 Kore Inc. All rights reserved.
 */
public class BotHomeActivity extends BotAppCompactActivity {

    private Button launchBotBtn;
    private EditText etIdentity;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        new ThemeColors(this);
        setContentView(R.layout.bot_home_activity_layout);

        findViews();
        setListeners();
//        getJWTToken();
    }

    private void findViews() {

        launchBotBtn = (Button) findViewById(R.id.launchBotBtn);
        etIdentity = (EditText) findViewById(R.id.etIdentity);
        launchBotBtn.setText(getResources().getString(R.string.connect));
        etIdentity.setText(SDKConfiguration.Client.identity);
        if(etIdentity.getText().toString() != null && etIdentity.getText().toString().length() > 0)
            etIdentity.setSelection(etIdentity.getText().toString().length());
    }

    private void setListeners() {
        launchBotBtn.setOnClickListener(launchBotBtnOnClickListener);

    }

    public void buttonClick(View view){
        int red= new Random().nextInt(255);
        int green= new Random().nextInt(255);
        int blue= new Random().nextInt(255);
        ThemeColors.setNewThemeColor(BotHomeActivity.this, "F26666");
    }

    @Override
    protected void onStart() {
        super.onStart();
    }



    @Override
    protected void onStop() {
        super.onStop();
    }

    /**
     * START of : Listeners
     */

    View.OnClickListener launchBotBtnOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (isOnline())
            {
                if(!StringUtils.isNullOrEmpty(etIdentity.getText().toString()))
                {
                    if(StringUtils.isValidEmail(etIdentity.getText().toString()))
                    {
                        if(!SDKConfiguration.Client.isWebHook)
                        {
                            SDKConfiguration.Client.identity = UUID.randomUUID().toString();
                            BotSocketConnectionManager.getInstance().startAndInitiateConnectionWithConfig(getApplicationContext(),null);
                            launchBotChatActivity();
                        }
                        else
                        {
                            launchBotChatActivity();
                        }
                    }
                    else
                        Toast.makeText(BotHomeActivity.this, "Please enter a valid Email.", Toast.LENGTH_SHORT).show();
                }
                else
                    Toast.makeText(BotHomeActivity.this, "Please enter your Email.", Toast.LENGTH_SHORT).show();
            } else
                {
                Toast.makeText(BotHomeActivity.this, "No internet connectivity", Toast.LENGTH_SHORT).show();
            }
        }
    };


    /**
     * Launching BotchatActivity where user can interact with bot
     *
     */
    private void launchBotChatActivity(){
        Intent intent = new Intent(getApplicationContext(), BotChatActivity.class);
        Bundle bundle = new Bundle();
        //This should not be null
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
