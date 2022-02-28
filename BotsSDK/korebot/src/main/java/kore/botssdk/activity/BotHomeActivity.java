package kore.botssdk.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

//import com.kore.ai.widgetsdk.activities.PanelMainActivity;

import com.kore.ai.widgetsdk.models.JWTTokenResponse;
import com.kore.ai.widgetsdk.net.BotJWTRestBuilder;
import com.kore.findlysdk.activity.MainActivity;

import java.util.HashMap;
import java.util.Random;

import kore.botssdk.R;
import kore.botssdk.drawables.ThemeColors;
import kore.botssdk.listener.BotSocketConnectionManager;
import kore.botssdk.models.BotBankingConfigModel;
import kore.botssdk.models.BotResponse;
import kore.botssdk.net.BotBankingRestBuilder;
import kore.botssdk.net.BotRestBuilder;
import kore.botssdk.net.SDKConfiguration;
import kore.botssdk.utils.BundleUtils;
import kore.botssdk.utils.StringUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.kore.ai.widgetsdk.net.SDKConfiguration.Client.client_id;
import static com.kore.ai.widgetsdk.net.SDKConfiguration.Client.client_secret;
import static com.kore.ai.widgetsdk.net.SDKConfiguration.Client.identity;

/**
 * Created by Pradeep Mahato on 31-May-16.
 * Copyright (c) 2014 Kore Inc. All rights reserved.
 */
public class BotHomeActivity extends BotAppCompactActivity {

    private Button launchBotBtn, launchCVSBotBtn, launchPfizerBotBtn;
    private EditText etIdentity;
    private BotBankingConfigModel botBankingConfigModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new ThemeColors(BotHomeActivity.this);
        setContentView(R.layout.bot_home_activity_layout);
//        getBankingConfig();
        findViews();
        setListeners();
//        getJWTToken();
    }

    private void findViews() {

        launchBotBtn = (Button) findViewById(R.id.launchBotBtn);
        launchCVSBotBtn = (Button) findViewById(R.id.launchCVSBotBtn);
        launchPfizerBotBtn = (Button) findViewById(R.id.launchPfizerBotBtn);
        etIdentity = (EditText) findViewById(R.id.etIdentity);
//        launchBotBtn.setText("Connect");
//        etIdentity.setText(SDKConfiguration.Client.identity);
        if(etIdentity.getText().toString() != null && etIdentity.getText().toString().length() > 0)
            etIdentity.setSelection(etIdentity.getText().toString().length());

        launchCVSBotBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setBotConfiguration("careMark", "cs-0b9dcc51-26f3-53ed-b9d9-65888e5aaaeb", "97KKpL/OF4ees3Z69voceE1nm5FnelhxrtrwOJuRMPA=", "st-bd231a03-1ab7-58fb-8862-c19416471cdb", "sidx-6fff8b04-f206-565c-bb02-fb13ae366fd3");
                launchBotChatActivity();
            }
        });

        launchPfizerBotBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setBotConfiguration("Pfizer", "cs-549d8874-cf8c-5715-bce1-cb83ec4faedb", "ZLnSvXa5fhxrRM8znYbhWOVN/yDNH8vikdIivggA6WI=", "st-8dbd1e15-1f88-5ff7-9c23-e30ac1d38212", "sidx-d9006b59-6c8c-5a78-bcbd-00e3e0ceb9aa");
                launchBotChatActivity();
            }
        });
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
//                if(!StringUtils.isNullOrEmpty(etIdentity.getText().toString()))
//                {
//                    if(StringUtils.isValidEmail(etIdentity.getText().toString()))
//                    {
//                        SDKConfiguration.Client.identity = etIdentity.getText().toString();
//                        BotSocketConnectionManager.getInstance().startAndInitiateConnectionWithConfig(getApplicationContext(),null);
//                        setBotConfiguration("Future Bank Copy", "cs-b63967bb-0599-5ec2-8e84-8af15028f86f", "Q9W/R5t2V03/aUtZ1O/M25ObJP5k/rQhHZPjC977o7o=", "st-c877d8bd-8383-5472-ab69-8410ac17cd4d", "sidx-a0d5b74c-ef8d-51df-8cf0-d32617d3e66e");
                        launchBotChatActivity();
//                    }
//                    else
//                        Toast.makeText(BotHomeActivity.this, "Please enter a valid Email.", Toast.LENGTH_SHORT).show();
//                }
//                else
//                    Toast.makeText(BotHomeActivity.this, "Please enter your Email.", Toast.LENGTH_SHORT).show();
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
        Intent intent = new Intent(getApplicationContext(), FindlyActivity.class);
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

    private void getBankingConfig() {
        Call<BotBankingConfigModel> getBankingConfigService = BotBankingRestBuilder.getBotConfigService().getBankingConfig();
        getBankingConfigService.enqueue(new Callback<BotBankingConfigModel>() {
            @Override
            public void onResponse(Call<BotBankingConfigModel> call, Response<BotBankingConfigModel> response) {
                if (response.isSuccessful())
                {
                    botBankingConfigModel = response.body();
                    int colorStep = 15;
                    int red = Math.round(Color.red(Color.parseColor(botBankingConfigModel.getHeader_color())) / colorStep) * colorStep;
                    int green = Math.round(Color.green(Color.parseColor(botBankingConfigModel.getHeader_color())) / colorStep) * colorStep;
                    int blue = Math.round(Color.blue(Color.parseColor(botBankingConfigModel.getHeader_color())) / colorStep) * colorStep;
                    String stringColor = "#"+Integer.toHexString(Color.rgb(red, green, blue)).substring(2);

                    SharedPreferences sharedPreferences = getSharedPreferences(BotResponse.THEME_NAME, Context.MODE_PRIVATE);
                    if(!sharedPreferences.getString(BotResponse.BUBBLE_RIGHT_BG_COLOR, "").equalsIgnoreCase(stringColor))
                    {
                        SharedPreferences.Editor editor = getSharedPreferences(BotResponse.THEME_NAME, Context.MODE_PRIVATE).edit();
                        editor.putString(BotResponse.HEADER_TITLE, botBankingConfigModel.getHeader_title());
                        editor.putString(BotResponse.BUBBLE_RIGHT_BG_COLOR, botBankingConfigModel.getHeader_color());
                        editor.putString(BotResponse.BACK_IMAGE, botBankingConfigModel.getBack_img());
                        editor.putString(BotResponse.TOP_LEFT_ICON, botBankingConfigModel.getTop_left_icon());
                        editor.putString(BotResponse.HEADER_TITLE, botBankingConfigModel.getHeader_title());
                        editor.putBoolean(BotResponse.THEME_CHANGE_CALLED, true);
                        editor.apply();
                        SDKConfiguration.BubbleColors.setBotIconColor(botBankingConfigModel.getHeader_color());
                        ThemeColors.setNewThemeColor(BotHomeActivity.this, Color.red(Color.parseColor(botBankingConfigModel.getHeader_color())), Color.green(Color.parseColor(botBankingConfigModel.getHeader_color())), Color.blue(Color.parseColor(botBankingConfigModel.getHeader_color())));
                    }
                }
            }

            @Override
            public void onFailure(Call<BotBankingConfigModel> call, Throwable t) {
                Log.e("Skill Panel Data", t.toString());
            }
        });
    }

    private void setBotConfiguration(String botName, String clientId, String clientSecret, String botId, String sxId)
    {
        com.kore.findlysdk.net.SDKConfiguration.Client.setBot_name(botName);
        com.kore.findlysdk.net.SDKConfiguration.Client.setClient_id(clientId);
        com.kore.findlysdk.net.SDKConfiguration.Client.setClient_secret(clientSecret);
        com.kore.findlysdk.net.SDKConfiguration.Client.setBot_id(botId);
        com.kore.findlysdk.net.SDKConfiguration.setSDIX(sxId);
    }
}
