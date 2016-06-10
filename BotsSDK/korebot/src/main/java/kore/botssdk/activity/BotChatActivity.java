package kore.botssdk.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import de.greenrobot.event.EventBus;
import kore.botssdk.R;
import kore.botssdk.SocketConnectionEvents;
import kore.botssdk.fragment.BotContentFragment;
import kore.botssdk.fragment.ComposeFooterFragment;
import kore.botssdk.utils.BundleUtils;
import kore.botssdk.utils.Contants;
import kore.botssdk.utils.BotSharedPreferences;
import kore.botssdk.websocket.SocketWrapper;

/**
 * Created by Pradeep Mahato on 31-May-16.
 */
public class BotChatActivity extends BaseSpiceActivity {

    FrameLayout chatLayoutFooterContainer;
    FrameLayout chatLayoutContentContainer;
    ProgressBar taskProgressBar;

    FragmentTransaction fragmentTransaction;

    String chatBot, taskBotId;
    String loginMode = Contants.NORMAL_FLOW;

    Handler actionBarTitleUpdateHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bot_chat_layout);
        findViews();
        getBundleInfo();

        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        //Add Bot Content Fragment
        BotContentFragment botContentFragment = new BotContentFragment();
        botContentFragment.setArguments(getIntent().getExtras());
        fragmentTransaction.add(R.id.chatLayoutContentContainer, botContentFragment).commit();

        //Add Bot Compose Footer Fragment
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        ComposeFooterFragment composeFooterFragment = new ComposeFooterFragment();
        composeFooterFragment.setArguments(getIntent().getExtras());
        fragmentTransaction.add(R.id.chatLayoutFooterContainer, composeFooterFragment).commit();

        updateTitleBar();
        EventBus.getDefault().register(this);

        if (loginMode.equalsIgnoreCase(Contants.NORMAL_FLOW)) {
            connectToWebSocket();
        } else {
            connectToWebSocketAnonymous();
        }

    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        SocketWrapper.getInstance().disConnect();
        super.onDestroy();
    }

    private void getBundleInfo() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            chatBot = bundle.getString(BundleUtils.CHATBOT, "");
            taskBotId = bundle.getString(BundleUtils.TASKBOTID, "");
            loginMode = bundle.getString(BundleUtils.LOGIN_MODE, Contants.NORMAL_FLOW);
        }
    }

    private void findViews() {
        chatLayoutFooterContainer = (FrameLayout) findViewById(R.id.chatLayoutFooterContainer);
        chatLayoutContentContainer = (FrameLayout) findViewById(R.id.chatLayoutContentContainer);
        taskProgressBar = (ProgressBar) findViewById(R.id.taskProgressBar);
    }

    private void updateTitleBar() {
        String botName = (chatBot != null && !chatBot.isEmpty()) ? chatBot : ((loginMode.equalsIgnoreCase(Contants.NORMAL_FLOW)) ? "Kore Bot" : "Kore Bot - anonymous");
        getSupportActionBar().setSubtitle(botName);
    }

    private void updateTitleBar(SocketConnectionEvents socketConnectionEvents) {

        String titleMsg = "";
        switch (socketConnectionEvents.getSocketConnectionEventStates()) {
            case CONNECTING:
                titleMsg = getString(R.string.socket_connecting);
                taskProgressBar.setVisibility(View.VISIBLE);
                break;
            case CONNECTED:
                titleMsg = getString(R.string.socket_connected);
                taskProgressBar.setVisibility(View.GONE);
                updateActionBar();
                break;
            case DISCONNECTED:
                titleMsg = getString(R.string.socket_disconnected);
                taskProgressBar.setVisibility(View.GONE);
                updateActionBar();
                break;
            case FAILED_TO_CONNECT:
                titleMsg = getString(R.string.socket_failed);
                taskProgressBar.setVisibility(View.GONE);
                updateActionBar();
                break;
            case RECONNECTING:
                titleMsg = getString(R.string.socket_connected);
                taskProgressBar.setVisibility(View.VISIBLE);
                break;
        }

        if (!titleMsg.isEmpty()) {
            getSupportActionBar().setSubtitle(titleMsg);
        }
    }

    private void connectToWebSocket(){
        String accessToken = BotSharedPreferences.getAccessTokenFromPreferences(getApplicationContext());
        SocketWrapper.getInstance().connect(accessToken, chatBot, taskBotId, spiceManager);

        EventBus.getDefault().post(new SocketConnectionEvents(SocketConnectionEvents.SocketConnectionEventStates.CONNECTING));
    }

    private void connectToWebSocketAnonymous() {
        String demoClientId = getResources().getString(R.string.demo_client_id);
        String demoSecretKey = getResources().getString(R.string.demo_secret_key);

        SocketWrapper.getInstance().connect(demoClientId, demoSecretKey, spiceManager);

        EventBus.getDefault().post(new SocketConnectionEvents(SocketConnectionEvents.SocketConnectionEventStates.CONNECTING));
    }

    public void onEventMainThread(SocketConnectionEvents socketConnectionEvents) {
        updateTitleBar (socketConnectionEvents);
    }

    private void updateActionBar() {
        if (actionBarTitleUpdateHandler == null) {
            actionBarTitleUpdateHandler = new Handler();
        }

        actionBarTitleUpdateHandler.removeCallbacks(actionBarUpdateRunnable);
        actionBarTitleUpdateHandler.postDelayed(actionBarUpdateRunnable, 4000);

    }

    Runnable actionBarUpdateRunnable = new Runnable() {
        @Override
        public void run() {
            updateTitleBar();
        }
    };

}
