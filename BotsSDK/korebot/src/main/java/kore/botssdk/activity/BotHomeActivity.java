package kore.botssdk.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import kore.botssdk.R;
import kore.botssdk.adapter.AvailableBotListAdapter;
import kore.botssdk.models.MarketStreams;
import kore.botssdk.net.BotRestService;
import kore.botssdk.net.GetBotMarketStreams;
import kore.botssdk.net.MarketStreamList;
import kore.botssdk.utils.BotSharedPreferences;
import kore.botssdk.utils.BundleUtils;
import kore.botssdk.utils.Contants;

/**
 * Created by Pradeep Mahato on 31-May-16.
 * Copyright (c) 2014 Kore Inc. All rights reserved.
 */
public class BotHomeActivity extends AppCompatActivity {

    Button launchBotBtn;
    ListView botListView;
    AvailableBotListAdapter availableBotListAdapter;

    SpiceManager spiceManager = new SpiceManager(BotRestService.class);

    String loginMode = Contants.NORMAL_FLOW;

    String LOG_TAG = BotHomeActivity.class.getSimpleName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bot_home_activity_layout);

        findViews();
        setListeners();
        getBundleInfo();
    }

    private void findViews() {
        launchBotBtn = (Button) findViewById(R.id.launchBotBtn);
        botListView = (ListView) findViewById(R.id.botListView);
    }

    private void setListeners() {
        launchBotBtn.setOnClickListener(launchBotBtnOnClickListener);
        botListView.setOnItemClickListener(botListVieOnItemClickListener);
    }

    private void getBundleInfo() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            loginMode = bundle.getString(BundleUtils.LOGIN_MODE, Contants.NORMAL_FLOW);
        }
    }

    private void setAdapter(MarketStreamList marketStreamList) {

        if (availableBotListAdapter == null) {
            availableBotListAdapter = new AvailableBotListAdapter(getApplicationContext(), marketStreamList);
            botListView.setAdapter(availableBotListAdapter);
        } else {
            availableBotListAdapter.setMarketStreamList(marketStreamList);
            availableBotListAdapter.notifyDataSetChanged();
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        spiceManager.start(getApplicationContext());
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (loginMode.equalsIgnoreCase(Contants.NORMAL_FLOW)) {
            getAllBotsFromMarketStream();
        }
    }

    @Override
    protected void onStop() {
        spiceManager.shouldStop();
        super.onStop();
    }

    /**
     * START of : Listeners
     */

    View.OnClickListener launchBotBtnOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getApplicationContext(), BotChatActivity.class);

            Bundle bundle = new Bundle();
            bundle.putString(BundleUtils.LOGIN_MODE, loginMode);
            bundle.putBoolean(BundleUtils.SHOW_PROFILE_PIC, false);
            intent.putExtras(bundle);

            startActivity(intent);
        }
    };

    AdapterView.OnItemClickListener botListVieOnItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            MarketStreams marketStreams = availableBotListAdapter.getItem(position);

            Intent botChatActivityIntent = new Intent(getApplicationContext(), BotChatActivity.class);

            Bundle botChatActivityBundle = new Bundle();
            botChatActivityBundle.putString(BundleUtils.CHATBOT, marketStreams.getName());
            botChatActivityBundle.putString(BundleUtils.TASKBOTID, marketStreams.get_id());
            botChatActivityBundle.putBoolean(BundleUtils.SHOW_PROFILE_PIC, true);

            botChatActivityIntent.putExtras(botChatActivityBundle);

            startActivity(botChatActivityIntent);
        }
    };

    /**
     * END of : Listeners
     */

    private void getAllBotsFromMarketStream() {

        String userId = BotSharedPreferences.getUserIdFromPreferences(getApplicationContext());
        String accessToken = BotSharedPreferences.getAccessTokenFromPreferences(getApplicationContext());

        GetBotMarketStreams getBotMarketStreams = new GetBotMarketStreams(userId, accessToken);

        spiceManager.execute(getBotMarketStreams, new RequestListener<MarketStreamList>() {
            @Override
            public void onRequestFailure(SpiceException e) {
                Log.e(LOG_TAG, "getAllBotsFromMarketStream()\nonRequestFailure : " + e.getMessage());
            }

            @Override
            public void onRequestSuccess(MarketStreamList marketStreamList) {
                Log.e(LOG_TAG, "getAllBotsFromMarketStream()\n" +
                        "onRequestSuccess : listSize -> " + marketStreamList.size());
                setAdapter(marketStreamList);
            }
        });

    }
}
