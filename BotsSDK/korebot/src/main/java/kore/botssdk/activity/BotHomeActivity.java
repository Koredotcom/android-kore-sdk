package kore.botssdk.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import kore.botssdk.R;
import kore.botssdk.adapter.AvailableBotListAdapter;
import kore.botssdk.models.MarketStreams;
import kore.botssdk.net.GetBotMarketStreams;
import kore.botssdk.net.MarketStreamList;
import kore.botssdk.utils.BotSharedPreferences;
import kore.botssdk.utils.BundleUtils;

/**
 * Created by Pradeep Mahato on 31-May-16.
 */
public class BotHomeActivity extends BaseSpiceActivity {

    Button launchBotBtn, fetchAgainBtn;
    ListView botListView;
    AvailableBotListAdapter availableBotListAdapter;

    String LOG_TAG = BotHomeActivity.class.getSimpleName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bot_home_activity_layout);

        findViews();
        setListeners();
        getAllBotsFromMarketStream();
    }

    private void findViews() {
        launchBotBtn = (Button) findViewById(R.id.launchBotBtn);
        botListView = (ListView) findViewById(R.id.botListView);
        fetchAgainBtn = (Button) findViewById(R.id.fetchAgainBtn);
    }

    private void setListeners() {
        launchBotBtn.setOnClickListener(launchBotBtnOnClickListener);
        fetchAgainBtn.setOnClickListener(fetchAgainBtnOnClickListener);
        botListView.setOnItemClickListener(botListVieOnItemClickListener);
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

    /**
     * START of : Listeners
     */

    View.OnClickListener launchBotBtnOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getApplicationContext(), BotChatActivity.class);
            startActivity(intent);
        }
    };

    View.OnClickListener fetchAgainBtnOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            getAllBotsFromMarketStream();
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

            botChatActivityIntent.putExtras(botChatActivityBundle);

            startActivity(botChatActivityIntent);
        }
    };

    /**
     * END of : Listeners
     */

    private void getAllBotsFromMarketStream(){

        String userId = BotSharedPreferences.getUserIdFromPreferences(getApplicationContext());
        String accessToken = BotSharedPreferences.getAccessTokenFromPreferences(getApplicationContext());

        GetBotMarketStreams getBotMarketStreams = new GetBotMarketStreams(userId, accessToken);

        getSpiceManager().execute(getBotMarketStreams, new RequestListener<MarketStreamList>() {
            @Override
            public void onRequestFailure(SpiceException e) {
                Log.e(LOG_TAG, "getAllBotsFromMarketStream()\nonRequestFailure : " + e.getMessage()) ;
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
