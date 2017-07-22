package kore.botssdk.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.util.Date;

import kore.botssdk.R;
import kore.botssdk.autobahn.WebSocket;
import kore.botssdk.bot.BotClient;
import kore.botssdk.fragment.BotContentFragment;
import kore.botssdk.fragment.CarouselFragment;
import kore.botssdk.fragment.ComposeFooterFragment;
import kore.botssdk.fragment.QuickReplyFragment;
import kore.botssdk.listener.BotContentFragmentUpdate;
import kore.botssdk.listener.ComposeFooterUpdate;
import kore.botssdk.listener.InvokeGenericWebViewInterface;
import kore.botssdk.listener.TTSUpdate;
import kore.botssdk.models.BotInfoModel;
import kore.botssdk.models.BotRequest;
import kore.botssdk.models.BotResponse;
import kore.botssdk.models.ComponentModel;
import kore.botssdk.models.PayloadInner;
import kore.botssdk.models.PayloadOuter;
import kore.botssdk.net.RestResponse;
import kore.botssdk.net.SDKConfiguration;
import kore.botssdk.utils.BundleUtils;
import kore.botssdk.utils.CustomToast;
import kore.botssdk.utils.DateUtils;
import kore.botssdk.utils.SocketConnectionEventStates;
import kore.botssdk.utils.StringConstants;
import kore.botssdk.utils.TTSSynthesizer;
import kore.botssdk.utils.Utils;
import kore.botssdk.websocket.SocketConnectionListener;

/**
 * Created by Pradeep Mahato on 31-May-16.
 * Copyright (c) 2014 Kore Inc. All rights reserved.
 */
public class BotChatActivity extends AppCompatActivity implements SocketConnectionListener, ComposeFooterFragment.ComposeFooterInterface, QuickReplyFragment.QuickReplyInterface, TTSUpdate, InvokeGenericWebViewInterface {

    String LOG_TAG = BotChatActivity.class.getSimpleName();

    FrameLayout chatLayoutFooterContainer;
    FrameLayout chatLayoutContentContainer;
    ProgressBar taskProgressBar;

    FragmentTransaction fragmentTransaction;
    final Handler handler = new Handler();


    String chatBot, taskBotId, jwt;

    Handler actionBarTitleUpdateHandler;

    BotClient botClient;
    BotContentFragment botContentFragment;
    CarouselFragment carouselFragment;
    ComposeFooterFragment composeFooterFragment;
    TTSSynthesizer ttsSynthesizer;
    QuickReplyFragment quickReplyFragment;

    BotContentFragmentUpdate botContentFragmentUpdate;
    ComposeFooterUpdate composeFooterUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bot_chat_layout);
        findViews();
        getBundleInfo();

        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        //Add Bot Content Fragment
        botContentFragment = new BotContentFragment();
        botContentFragment.setArguments(getIntent().getExtras());
        botContentFragment.setComposeFooterInterface(this);
        botContentFragment.setInvokeGenericWebViewInterface(this);
        fragmentTransaction.add(R.id.chatLayoutContentContainer, botContentFragment).commit();
        setBotContentFragmentUpdate(botContentFragment);

        //Add Suggestion Fragment
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        quickReplyFragment = new QuickReplyFragment();
        quickReplyFragment.setArguments(getIntent().getExtras());
        quickReplyFragment.setListener(BotChatActivity.this);
        fragmentTransaction.add(R.id.quickReplyLayoutFooterContainer, quickReplyFragment).commit();

        //Add Bot Compose Footer Fragment
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        composeFooterFragment = new ComposeFooterFragment();
        composeFooterFragment.setArguments(getIntent().getExtras());
        composeFooterFragment.setComposeFooterInterface(this);
        fragmentTransaction.add(R.id.chatLayoutFooterContainer, composeFooterFragment).commit();
        setComposeFooterUpdate(composeFooterFragment);

        updateTitleBar();

        botClient = new BotClient(this);
        ttsSynthesizer = new TTSSynthesizer(this);
        setupTextToSpeech();

        connectToWebSocketAnonymous();
    }

    @Override
    protected void onDestroy() {
        botClient.disconnect();
        super.onDestroy();
    }

    private void getBundleInfo() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {

            jwt = bundle.getString(BundleUtils.JWT_TOKEN, "");
        }
        chatBot = SDKConfiguration.Client.bot_name;
        taskBotId = SDKConfiguration.Client.bot_id;
    }

    private void findViews() {
        chatLayoutFooterContainer = (FrameLayout) findViewById(R.id.chatLayoutFooterContainer);
        chatLayoutContentContainer = (FrameLayout) findViewById(R.id.chatLayoutContentContainer);
        taskProgressBar = (ProgressBar) findViewById(R.id.taskProgressBar);
    }

    private void updateTitleBar() {
        String botName = (chatBot != null && !chatBot.isEmpty()) ? chatBot : ((SDKConfiguration.Server.IS_ANONYMOUS_USER) ? chatBot + " - anonymous" : chatBot);
        getSupportActionBar().setSubtitle(botName);
    }

    private void updateTitleBar(SocketConnectionEventStates socketConnectionEvents) {

        String titleMsg = "";
        switch (socketConnectionEvents) {
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

        if (Utils.isNetworkAvailable(this) && !titleMsg.isEmpty()) {
            getSupportActionBar().setSubtitle(titleMsg);
        } else {
            CustomToast.showToast(getApplicationContext(), "No network avilable.");
            getSupportActionBar().setSubtitle("Disconnected");
        }
    }

    private void setupTextToSpeech() {
        composeFooterFragment.setTtsUpdate(BotChatActivity.this);
        botContentFragment.setTtsUpdate(BotChatActivity.this);
    }

    private void connectToWebSocketAnonymous() {
        botClient.connectAsAnonymousUser(jwt, SDKConfiguration.Client.client_id, chatBot, taskBotId, BotChatActivity.this);
        updateTitleBar(SocketConnectionEventStates.CONNECTING);
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

    @Override
    protected void onPause() {
        ttsSynthesizer.stopTextToSpeech();
        super.onPause();
    }

    @Override
    public void onOpen() {
        if (composeFooterUpdate != null) {
            composeFooterUpdate.enableSendButton();
            composeFooterUpdate = null;
        }
        //By sending null initiating sending which are un-delivered in pool
        botClient.sendMessage(null, null, null);
        updateTitleBar(SocketConnectionEventStates.CONNECTED);
    }

    @Override
    public void onClose(WebSocket.WebSocketConnectionObserver.WebSocketCloseNotification code, String reason) {
        switch (code) {
            case CONNECTION_LOST:
                updateTitleBar(SocketConnectionEventStates.DISCONNECTED);
                return;
            case CANNOT_CONNECT:
            case PROTOCOL_ERROR:
            case INTERNAL_ERROR:
            case SERVER_ERROR:
                updateTitleBar(SocketConnectionEventStates.FAILED_TO_CONNECT);
                break;
        }
        updateTitleBar();
    }

    @Override
    public void onTextMessage(String payload) {
        processPayload(payload);
    }

    @Override
    public void onRawTextMessage(byte[] payload) {

    }

    @Override
    public void onBinaryMessage(byte[] payload) {

    }

    @Override
    public void onSendClick(String message) {

        stopTextToSpeech();
        botClient.sendMessage(message, chatBot, taskBotId);

        if (botContentFragmentUpdate != null) {
            //Update the bot content list with the send message
            RestResponse.BotMessage botMessage = new RestResponse.BotMessage(message);
            RestResponse.BotPayLoad botPayLoad = new RestResponse.BotPayLoad();
            botPayLoad.setMessage(botMessage);
            BotInfoModel botInfo = new BotInfoModel(chatBot, taskBotId);
            botPayLoad.setBotInfo(botInfo);
            Gson gson = new Gson();
            String jsonPayload = gson.toJson(botPayLoad);

            BotRequest botRequest = gson.fromJson(jsonPayload, BotRequest.class);
            botRequest.setCreatedOn(DateUtils.isoFormatter.format(new Date()));

            botContentFragmentUpdate.updateContentListOnSend(botRequest);
        }

        toggleQuickRepliesVisiblity(false);
    }

    public void setBotContentFragmentUpdate(BotContentFragmentUpdate botContentFragmentUpdate) {
        this.botContentFragmentUpdate = botContentFragmentUpdate;
    }

    public void setComposeFooterUpdate(ComposeFooterUpdate composeFooterUpdate) {
        this.composeFooterUpdate = composeFooterUpdate;
    }


    @Override
    public void onQuickReplyItemClicked(String text) {
        onSendClick(text);
    }

    private void processPayload(String payload) {

        Gson gson = new Gson();
        try {
            final BotResponse botResponse = gson.fromJson(payload, BotResponse.class);
            if (botResponse.getMessage() == null || botResponse.getMessage().isEmpty()) {
                return;
            }

            Log.d(LOG_TAG, payload);
            if (!botResponse.getMessage().isEmpty()) {
                ComponentModel compModel = botResponse.getMessage().get(0).getComponent();
                if (compModel != null) {
                    PayloadOuter payOuter = compModel.getPayload();
                    PayloadInner payInner;
                    if (payOuter.getText() != null && payOuter.getText().contains("&quot")) {
                        payOuter = gson.fromJson(payOuter.getText().replace("&quot;", "\""), PayloadOuter.class);
                    }
                    if (payOuter.getPayload() != null) {
                        payOuter.getPayload().convertElementToAppropriate();
                    }
                }
            }
            botContentFragment.showTypingStatus(botResponse);

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    stopTextToSpeech();
                    checkForQuickReplies(botResponse);
                    botContentFragment.addMessageToBotChatAdapter(botResponse);
                    textToSpeech(botResponse);
                }
            }, StringConstants.TYPING_STATUS_TIME);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void invokeGenericWebView(String url) {
        if (url != null && !url.isEmpty()) {
            Intent intent = new Intent(getApplicationContext(), GenericWebViewActivity.class);
            intent.putExtra("url", url);
            intent.putExtra("header", getResources().getString(R.string.app_name));
            startActivity(intent);
        }
    }

    @Override
    public void ttsUpdateListener(boolean isTTSEnabled) {
        stopTextToSpeech();
    }

    @Override
    public void ttsOnStop() {
        stopTextToSpeech();
    }

    public boolean isTTSEnabled() {
        if (composeFooterFragment != null) {
            return composeFooterFragment.isTTSEnabled();
        } else {
            Log.e(BotChatActivity.class.getSimpleName(), "ComposeFooterFragment not found");
            return false;
        }
    }

    private void stopTextToSpeech() {
        ttsSynthesizer.stopTextToSpeech();
    }

    private void textToSpeech(BotResponse botResponse) {
        if (isTTSEnabled() && botResponse.getMessage() != null && !botResponse.getMessage().isEmpty()) {
            String botResponseTextualFormat = "";//botResponse.getTempMessage().getcInfo().getBody();
            ttsSynthesizer.speak(botResponseTextualFormat);
        }
    }

    private void toggleQuickRepliesVisiblity(boolean visible){
        if (visible) {
            quickReplyFragment.toggleQuickReplyContainer(View.VISIBLE);
        } else {
            quickReplyFragment.toggleQuickReplyContainer(View.GONE);
        }
    }

    private void checkForQuickReplies(BotResponse botResponse) {
        if (botResponse.getMessage() == null || botResponse.getMessage().isEmpty()) {
            return;
        }

        ComponentModel compModel = botResponse.getMessage().get(0).getComponent();
        if (compModel != null) {
            String compType = compModel.getType();
            if (BotResponse.COMPONENT_TYPE_TEMPLATE.equalsIgnoreCase(compType)) {

                PayloadOuter payOuter = compModel.getPayload();
                PayloadInner payInner = payOuter.getPayload();
                if (BotResponse.TEMPLATE_TYPE_QUICK_REPLIES.equalsIgnoreCase(payInner.getTemplate_type())) {
                    quickReplyFragment.populateQuickReplyViews(payInner.getQuick_replies());
                }

            }
        }
    }
}
