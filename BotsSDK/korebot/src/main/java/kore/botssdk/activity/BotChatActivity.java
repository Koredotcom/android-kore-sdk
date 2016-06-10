package kore.botssdk.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.widget.FrameLayout;

import kore.botssdk.R;
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

    FragmentTransaction fragmentTransaction;

    String chatBot, taskBotId;
    String loginMode = Contants.NORMAL_FLOW;

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

        if (loginMode.equalsIgnoreCase(Contants.NORMAL_FLOW)) {
            connectToWebSocket();
        } else {
            connectToWebSocketAnonymous();
        }

        updateTitleBar();
    }

    @Override
    protected void onDestroy() {
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
    }

    private void updateTitleBar() {
        String botName = (chatBot != null && !chatBot.isEmpty()) ? chatBot : ((loginMode.equalsIgnoreCase(Contants.NORMAL_FLOW)) ? "Kore Bot" : "Kore Bot - anonymous");
        getSupportActionBar().setSubtitle(botName);
    }

    private void connectToWebSocket(){
        String accessToken = BotSharedPreferences.getAccessTokenFromPreferences(getApplicationContext());
        SocketWrapper.getInstance().connect(accessToken, chatBot, taskBotId, spiceManager);
    }

    private void connectToWebSocketAnonymous() {
        String demoClientId = getResources().getString(R.string.demo_client_id);
        String demoSecretKey = getResources().getString(R.string.demo_secret_key);

        SocketWrapper.getInstance().connect(demoClientId, demoSecretKey, spiceManager);
    }

}
