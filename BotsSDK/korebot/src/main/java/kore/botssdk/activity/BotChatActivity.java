package kore.botssdk.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.j256.ormlite.stmt.QueryBuilder;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import kore.botssdk.BotDb.BotDataPersister;
import kore.botssdk.BotDb.BotMessageDBModel;
import kore.botssdk.BotDb.DBHelper;
import kore.botssdk.R;
import kore.botssdk.bot.BotClient;
import kore.botssdk.event.KoreEventCenter;
import kore.botssdk.events.SocketDataTransferModel;
import kore.botssdk.fragment.BotContentFragment;
import kore.botssdk.fragment.CarouselFragment;
import kore.botssdk.fragment.ComposeFooterFragment;
import kore.botssdk.fragment.QuickReplyFragment;
import kore.botssdk.listener.BaseSocketConnectionManager;
import kore.botssdk.listener.BotContentFragmentUpdate;
import kore.botssdk.listener.BotSocketConnectionManager;
import kore.botssdk.listener.ComposeFooterUpdate;
import kore.botssdk.listener.InvokeGenericWebViewInterface;
import kore.botssdk.listener.SocketChatListener;
import kore.botssdk.listener.TTSUpdate;
import kore.botssdk.models.BotHistory;
import kore.botssdk.models.BotHistoryMessage;
import kore.botssdk.models.BotInfoModel;
import kore.botssdk.models.BotRequest;
import kore.botssdk.models.BotResponse;
import kore.botssdk.models.BotResponseMessage;
import kore.botssdk.models.Component;
import kore.botssdk.models.ComponentModel;
import kore.botssdk.models.FormActionTemplate;
import kore.botssdk.models.PayloadInner;
import kore.botssdk.models.PayloadOuter;
import kore.botssdk.net.RestRequest;
import kore.botssdk.net.RestResponse;
import kore.botssdk.net.SDKConfiguration;
import kore.botssdk.utils.BundleConstants;
import kore.botssdk.utils.BundleUtils;
import kore.botssdk.utils.CustomToast;
import kore.botssdk.utils.DateUtils;
import kore.botssdk.utils.TTSSynthesizer;
import kore.botssdk.utils.Utils;
import kore.botssdk.websocket.SocketWrapper;

/**
 * Created by Pradeep Mahato on 31-May-16.
 * Copyright (c) 2014 Kore Inc. All rights reserved.
 */
public class BotChatActivity extends BotAppCompactActivity implements  ComposeFooterFragment.ComposeFooterInterface, QuickReplyFragment.QuickReplyInterface, TTSUpdate, InvokeGenericWebViewInterface {

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
    boolean isItFirstConnect = true;
    private Gson gson = new Gson();

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
        KoreEventCenter.register(this);
        BotSocketConnectionManager.getInstance().setChatListener(sListener);

       // connectToWebSocketAnonymous();

    }

    SocketChatListener sListener = new SocketChatListener() {
        @Override
        public void onMessage(BotResponse botResponse) {
            processPayload("", botResponse);
        }

        @Override
        public void onConnectionStateChanged(BaseSocketConnectionManager.CONNECTION_STATE state, boolean isReconnection) {
            if(state == BaseSocketConnectionManager.CONNECTION_STATE.CONNECTED){
                //fetchBotMessages();
            }
            updateTitleBar(state);
        }

        @Override
        public void onMessage(SocketDataTransferModel data) {
            if (data == null) return;
            if (data.getEvent_type().equals(BaseSocketConnectionManager.EVENT_TYPE.TYPE_TEXT_MESSAGE)) {
                processPayload(data.getPayLoad(), null);

            } else if (data.getEvent_type().equals(BaseSocketConnectionManager.EVENT_TYPE.TYPE_MESSAGE_UPDATE)) {
                if (botContentFragment != null) {
                    botContentFragment.updateContentListOnSend(data.getBotRequest());
                }
            }
        }
    };

    @Override
    protected void onDestroy() {
        botClient.disconnect();
        KoreEventCenter.unregister(this);
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
//        String botName = (chatBot != null && !chatBot.isEmpty()) ? chatBot : ((SDKConfiguration.Server.IS_ANONYMOUS_USER) ? chatBot + " - anonymous" : chatBot);
//        getSupportActionBar().setSubtitle(botName);
    }

    private void updateTitleBar(BaseSocketConnectionManager.CONNECTION_STATE socketConnectionEvents) {

        String titleMsg = "";
        switch (socketConnectionEvents) {
            case CONNECTING:
                titleMsg = getString(R.string.socket_connecting);
                taskProgressBar.setVisibility(View.VISIBLE);
                updateActionBar();
                break;
            case CONNECTED:
                /*if(isItFirstConnect)
                    botClient.sendMessage("welcomedialog");*/
                titleMsg = getString(R.string.socket_connected);
                taskProgressBar.setVisibility(View.GONE);
                composeFooterFragment.enableSendButton();
                updateActionBar();
                break;
            case DISCONNECTED:
            case CONNECTED_BUT_DISCONNECTED:
                titleMsg = getString(R.string.socket_disconnected);
                taskProgressBar.setVisibility(View.VISIBLE);
                composeFooterFragment.setDisabled(true);
                composeFooterFragment.updateUI();
                updateActionBar();
                break;

            default:
                titleMsg = getString(R.string.socket_connecting);
                taskProgressBar.setVisibility(View.GONE);
                updateActionBar();

        }
    }

    private void setupTextToSpeech() {
        composeFooterFragment.setTtsUpdate(BotSocketConnectionManager.getInstance());
        botContentFragment.setTtsUpdate(BotSocketConnectionManager.getInstance());
    }



    public void onEvent(SocketDataTransferModel data) {
        if (data == null) return;
        if (data.getEvent_type().equals(BaseSocketConnectionManager.EVENT_TYPE.TYPE_TEXT_MESSAGE)) {
            processPayload(data.getPayLoad(), null);
        } else if (data.getEvent_type().equals(BaseSocketConnectionManager.EVENT_TYPE.TYPE_MESSAGE_UPDATE)) {
            if (botContentFragment != null) {
                botContentFragment.updateContentListOnSend(data.getBotRequest());
            }
        }
    }

    public void onEvent(BaseSocketConnectionManager.CONNECTION_STATE states) {
        updateTitleBar(states);
    }


    public void onEvent(BotResponse botResponse) {
        processPayload("", botResponse);
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
    public void onSendClick(String message) {
        BotSocketConnectionManager.getInstance().sendMessage(message, null);
    }

    @Override
    public void onSendClick(String message, String payload) {
        if(payload != null){
            BotSocketConnectionManager.getInstance().sendPayload(message, payload);
        }else{
            BotSocketConnectionManager.getInstance().sendMessage(message, payload);
        }

        toggleQuickRepliesVisiblity(false);
    }

    @Override
    public void onFormActionButtonClicked(FormActionTemplate fTemplate) {

    }

    @Override
    public void launchActivityWithBundle(int type, Bundle payload) {

    }

    @Override
    public void sendWithSomeDelay(String message, String payload,long time) {

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

    /**
     * payload processing
     */

    private void processPayload(String payload, BotResponse botLocalResponse) {
        if (botLocalResponse == null) BotSocketConnectionManager.getInstance().stopDelayMsgTimer();

        try {
            final BotResponse botResponse = botLocalResponse != null ? botLocalResponse : gson.fromJson(payload, BotResponse.class);
            if (botResponse == null || botResponse.getMessage() == null || botResponse.getMessage().isEmpty()) {
                return;
            }

            Log.d(LOG_TAG, payload);
            boolean resolved = true;
            PayloadOuter payOuter = null;
//            PayloadInner payInner = null;
            if (!botResponse.getMessage().isEmpty()) {
                ComponentModel compModel = botResponse.getMessage().get(0).getComponent();
                if (compModel != null) {
                    payOuter = compModel.getPayload();
                    if (payOuter != null) {
                        /*if (payOuter.getText() != null && payOuter.getText().contains("&quot")) {
                            payOuter.setText(payOuter.getText().replace("&quot;", "\""));
                            payOuter = gson.fromJson(payOuter.getText().replace("&quot;", "\""), PayloadOuter.class);
                            // payOuter = gson.fromJson(payOuter.getText().replace("&quot;", "\""), PayloadOuter.class);
                        }*/

                        if (payOuter.getText() != null && payOuter.getText().contains("&quot")) {
                            Gson gson = new Gson();
                            payOuter = gson.fromJson(payOuter.getText().replace("&quot;", "\""), PayloadOuter.class);
                        }
                    }
                }
            }
            final PayloadInner payloadInner = payOuter == null ? null : payOuter.getPayload();
            if (payloadInner != null && payloadInner.getTemplate_type() != null && "start_timer".equalsIgnoreCase(payloadInner.getTemplate_type())) {
                BotSocketConnectionManager.getInstance().startDelayMsgTimer();
            }
            botContentFragment.showTypingStatus(botResponse);
            if (payloadInner != null) {
                payloadInner.convertElementToAppropriate();
            }
            if (resolved) {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        botContentFragment.addMessageToBotChatAdapter(botResponse);
                        textToSpeech(botResponse);
                        botContentFragment.setQuickRepliesIntoFooter(botResponse);
                    }
                }, BundleConstants.TYPING_STATUS_TIME);
            }
        } catch (Exception e) {
            /*Toast.makeText(getApplicationContext(), "Invalid JSON", Toast.LENGTH_SHORT).show();*/
            e.printStackTrace();
            if (e instanceof JsonSyntaxException) {
                try {
                    //This is the case Bot returning user sent message from another channel
                    if (botContentFragment != null) {
                        BotRequest botRequest = gson.fromJson(payload, BotRequest.class);
                        botRequest.setCreatedOn(DateUtils.isoFormatter.format(new Date()));
                        botContentFragment.updateContentListOnSend(botRequest);
                    }
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
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
    public void handleUserActions(String action, HashMap<String,Object > payload) {


    }
    @Override
    public void onStop() {
        BotSocketConnectionManager.getInstance().unSubscribe();
        super.onStop();
    }

    @Override
    public void onStart() {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                BotSocketConnectionManager.getInstance().subscribe();
            }
        });
        super.onStart();
    }

    @Override
    protected void onResume() {
        BotSocketConnectionManager.getInstance().checkConnectionAndRetry(getApplicationContext(), false);
        updateTitleBar(BotSocketConnectionManager.getInstance().getConnection_state());
        super.onResume();
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
        try {
            ttsSynthesizer.stopTextToSpeech();
        }catch (IllegalArgumentException exception){
            exception.printStackTrace();
        }
    }

    private void textToSpeech(BotResponse botResponse) {
        if (isTTSEnabled() && botResponse.getMessage() != null && !botResponse.getMessage().isEmpty()) {
            String botResponseTextualFormat = "";
            BotResponseMessage msg = ((BotResponse) botResponse).getTempMessage();
            ComponentModel componentModel = botResponse.getMessage().get(0).getComponent();
            if (componentModel != null) {
                String compType = componentModel.getType();
                PayloadOuter payOuter = componentModel.getPayload();
                if (BotResponse.COMPONENT_TYPE_TEXT.equalsIgnoreCase(compType) || payOuter.getType() == null) {
                    botResponseTextualFormat = payOuter.getText();
                } else if (BotResponse.COMPONENT_TYPE_ERROR.equalsIgnoreCase(payOuter.getType())) {
                    botResponseTextualFormat = payOuter.getPayload().getText();
                } else if (BotResponse.COMPONENT_TYPE_TEMPLATE.equalsIgnoreCase(payOuter.getType()) || BotResponse.COMPONENT_TYPE_MESSAGE.equalsIgnoreCase(payOuter.getType())) {
                    PayloadInner payInner;
                    if (payOuter.getText() != null && payOuter.getText().contains("&quot")) {
                        Gson gson = new Gson();
                        payOuter = gson.fromJson(payOuter.getText().replace("&quot;", "\""), PayloadOuter.class);
                    }
                    payInner = payOuter.getPayload();

                    if (payInner.getSpeech_hint() != null) {
                        botResponseTextualFormat = payInner.getSpeech_hint();
//                        ttsSynthesizer.speak(botResponseTextualFormat);
                        } else if (BotResponse.TEMPLATE_TYPE_BUTTON.equalsIgnoreCase(payInner.getTemplate_type())) {
                            botResponseTextualFormat = payInner.getText();
                        } else if (BotResponse.TEMPLATE_TYPE_QUICK_REPLIES.equalsIgnoreCase(payInner.getTemplate_type())) {
                            botResponseTextualFormat = payInner.getText();
                        } else if (BotResponse.TEMPLATE_TYPE_CAROUSEL.equalsIgnoreCase(payInner.getTemplate_type())) {
                            botResponseTextualFormat = payInner.getText();
                        } else if (BotResponse.TEMPLATE_TYPE_CAROUSEL_ADV.equalsIgnoreCase(payInner.getTemplate_type())) {
                            botResponseTextualFormat = payInner.getText();
                        }
                        else if (BotResponse.TEMPLATE_TYPE_LIST.equalsIgnoreCase(payInner.getTemplate_type())) {
                            botResponseTextualFormat = payInner.getText();
                        }
                    }
                }
            if (BotSocketConnectionManager.getInstance().isTTSEnabled()) {
                BotSocketConnectionManager.getInstance().startSpeak(botResponseTextualFormat);
            }
            }
        }


    private void toggleQuickRepliesVisiblity(boolean visible){
        if (visible) {
            quickReplyFragment.toggleQuickReplyContainer(View.VISIBLE);
        } else {
            quickReplyFragment.toggleQuickReplyContainer(View.GONE);
        }
    }

}
