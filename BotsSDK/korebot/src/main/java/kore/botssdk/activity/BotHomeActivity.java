package kore.botssdk.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

//import com.kore.ai.widgetsdk.activities.PanelMainActivity;

import java.util.ArrayList;
import java.util.Random;

import kore.botssdk.R;
import kore.botssdk.drawables.ThemeColors;
import kore.botssdk.event.KoreEventCenter;
import kore.botssdk.listener.BotSocketConnectionManager;
import kore.botssdk.models.BotOptionsModel;
import kore.botssdk.models.BrandingNewModel;
import kore.botssdk.models.TokenResponseModel;
import kore.botssdk.net.RestBuilder;
import kore.botssdk.net.RestResponse;
import kore.botssdk.net.SDKConfiguration;
import kore.botssdk.utils.BundleUtils;
import kore.botssdk.utils.StringUtils;
import kore.botssdk.websocket.SocketWrapper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Pradeep Mahato on 31-May-16.
 * Copyright (c) 2014 Kore Inc. All rights reserved.
 */
public class BotHomeActivity extends BotAppCompactActivity {

    private Button launchBotBtn;
    private EditText etIdentity;
    private TokenResponseModel tokenResponseModel;

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
                        SDKConfiguration.Client.identity = etIdentity.getText().toString();
                        getFinastraToken();
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

    private void getFinastraToken()
    {
            RestResponse.BotCustomData botCustomData = new RestResponse.BotCustomData();
            botCustomData.put("tenantId", SDKConfiguration.Client.tenant_id);
            botCustomData.put("uniqueUserId", SDKConfiguration.Client.uniqueuserId);

        Call<TokenResponseModel> getBankingConfigService = RestBuilder.getTokenRestAPI().getFinastraTokenDetails(botCustomData, "published", "1","en_US");
        getBankingConfigService.enqueue(new Callback<TokenResponseModel>() {
            @Override
            public void onResponse(Call<TokenResponseModel> call, Response<TokenResponseModel> response)
            {
                if (response.isSuccessful())
                {
                    tokenResponseModel = response.body();
                    SDKConfiguration.Client.bot_name = tokenResponseModel.getBotInfo().getName();
                    SDKConfiguration.Client.bot_id = tokenResponseModel.getBotInfo().get_id();
                    SDKConfiguration.Server.setServerUrl(tokenResponseModel.getKoreAPIUrl());
                    SDKConfiguration.Server.setKoreBotServerUrl(tokenResponseModel.getKoreAPIUrl());

                    BotSocketConnectionManager.getInstance().startAndInitiateConnection(getApplicationContext(), null, tokenResponseModel.getJwt(), tokenResponseModel.getBotInfo().getName(), tokenResponseModel.getBotInfo().get_id());
                    launchBotChatActivity();
                }
            }

            @Override
            public void onFailure(Call<TokenResponseModel> call, Throwable t) {
                Log.e("Skill Panel Data", t.toString());
            }
        });
    }
}
