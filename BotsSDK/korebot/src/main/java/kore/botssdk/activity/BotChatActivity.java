package kore.botssdk.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import androidx.fragment.app.FragmentTransaction;

import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.kore.ai.widgetsdk.activities.PanelMainActivity;
import com.kore.ai.widgetsdk.adapters.KaWidgetBaseAdapterNew;
import com.kore.ai.widgetsdk.adapters.PannelAdapter;
import com.kore.ai.widgetsdk.fragments.BottomPanelFragment;
import com.kore.ai.widgetsdk.interfaces.PanelInterface;
import com.kore.ai.widgetsdk.listeners.UpdateRefreshItem;
import com.kore.ai.widgetsdk.listeners.VerticalListViewActionHelper;
import com.kore.ai.widgetsdk.models.BaseCalenderTemplateModel;
import com.kore.ai.widgetsdk.models.BotCaourselButtonModel;
import com.kore.ai.widgetsdk.models.ContactViewListModel;
import com.kore.ai.widgetsdk.models.JWTTokenResponse;
import com.kore.ai.widgetsdk.models.PanelBaseModel;
import com.kore.ai.widgetsdk.models.PanelResponseData;
import com.kore.ai.widgetsdk.models.WelcomeChatSummaryModel;
import com.kore.ai.widgetsdk.models.WidgetsModel;
import com.kore.ai.widgetsdk.models.searchskill.PanelLevelData;
import com.kore.ai.widgetsdk.net.BotJWTRestBuilder;
import com.kore.ai.widgetsdk.net.KaRestAPIHelper;
import com.kore.ai.widgetsdk.net.KaRestBuilder;
import com.kore.ai.widgetsdk.utils.AppUtils;
import com.kore.ai.widgetsdk.utils.KaFontUtils;
import com.kore.ai.widgetsdk.utils.KaUtility;
import com.kore.ai.widgetsdk.utils.SharedPreferenceUtils;
import com.kore.ai.widgetsdk.utils.StringUtils;
import com.kore.ai.widgetsdk.utils.Utility;
import com.kore.ai.widgetsdk.utils.Utils;
import com.kore.ai.widgetsdk.utils.WidgetConstants;
import com.kore.ai.widgetsdk.utils.WidgetViewMoreEnum;
import com.kore.ai.widgetsdk.views.widgetviews.ArticlesWidgetView;
import com.kore.ai.widgetsdk.views.widgetviews.BarChartWidgetView;
import com.kore.ai.widgetsdk.views.widgetviews.ChartListWidgetView;
import com.kore.ai.widgetsdk.views.widgetviews.CustomBottomSheetBehavior;
import com.kore.ai.widgetsdk.views.widgetviews.DefaultWidgetView;
import com.kore.ai.widgetsdk.views.widgetviews.GenericWidgetView;
import com.kore.ai.widgetsdk.views.widgetviews.LineChartWidgetView;
import com.kore.ai.widgetsdk.views.widgetviews.ListWidgetView;
import com.kore.ai.widgetsdk.views.widgetviews.MeetingWidgetView;
import com.kore.ai.widgetsdk.views.widgetviews.PieChartWidgetView;
import com.kore.ai.widgetsdk.views.widgetviews.SkillWidgetView;
import com.kore.ai.widgetsdk.views.widgetviews.TrendingHashTagView;
import com.squareup.picasso.Picasso;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

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
import kore.botssdk.listener.ComposeFooterInterface;
import kore.botssdk.listener.ComposeFooterUpdate;
import kore.botssdk.listener.InvokeGenericWebViewInterface;
import kore.botssdk.listener.SocketChatListener;
import kore.botssdk.listener.TTSUpdate;
import kore.botssdk.models.BotButtonModel;
import kore.botssdk.models.BotRequest;
import kore.botssdk.models.BotResponse;
import kore.botssdk.models.BotResponseMessage;
import kore.botssdk.models.CalEventsTemplateModel.Duration;
import kore.botssdk.models.ComponentModel;
import kore.botssdk.models.FormActionTemplate;
import kore.botssdk.models.KnowledgeCollectionModel;
import kore.botssdk.models.PayloadInner;
import kore.botssdk.models.PayloadOuter;
import kore.botssdk.net.SDKConfiguration;
import kore.botssdk.utils.BundleConstants;
import kore.botssdk.utils.BundleUtils;
import kore.botssdk.utils.DateUtils;
import kore.botssdk.utils.TTSSynthesizer;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.kore.ai.widgetsdk.net.SDKConfiguration.Client.client_id;
import static com.kore.ai.widgetsdk.net.SDKConfiguration.Client.client_secret;
import static com.kore.ai.widgetsdk.net.SDKConfiguration.Client.identity;

/**
 * Created by Pradeep Mahato on 31-May-16.
 * Copyright (c) 2014 Kore Inc. All rights reserved.
 */
public class BotChatActivity extends BotAppCompactActivity implements ComposeFooterInterface,
                                        QuickReplyFragment.QuickReplyInterface,
                                        TTSUpdate, InvokeGenericWebViewInterface, PanelInterface,
                                        VerticalListViewActionHelper, UpdateRefreshItem
{
    String LOG_TAG = BotChatActivity.class.getSimpleName();
    FrameLayout chatLayoutFooterContainer;
    FrameLayout chatLayoutContentContainer;
    FrameLayout chatLayoutPanelContainer;
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

    //For Bottom Panel
    private ProgressBar progressBarPanel;
    private RecyclerView pannel_recycler;
    private PannelAdapter pannelAdapter;
    private String jwtToken;
    private TextView emptyPanelView;
    private JWTTokenResponse jwtKeyResponse;
    private SharedPreferenceUtils sharedPreferenceUtils;
    private String packageName = "com.kore.koreapp";
    private String appName = "Kore";
    private CustomBottomSheetBehavior mBottomSheetBehavior;
    private boolean keyBoardShowing = false;
    private PanelBaseModel pModels;
    private LinearLayout perssiatentPanel, persistentSubLayout;
    private ImageView img_skill;
    private TextView closeBtnPanel, editButton;
    private RecyclerView recyclerView_panel;
    private LinearLayout single_item_container;
    private KaWidgetBaseAdapterNew widgetBaseAdapter;
    private TextView img_icon, txtTitle;

    //Fragment Approch
    private FrameLayout composerView;
    private BottomPanelFragment composerFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bot_chat_layout);
        findViews();
        findPanelView();
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
        attachFragments();
//        getJWToken();
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
        chatLayoutPanelContainer   = (FrameLayout) findViewById(R.id.chatLayoutPanelContainer);
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

    public void updateActionbar(boolean isSelected,String type,ArrayList<BotButtonModel> buttonModels) {

    }

    @Override
    public void lauchMeetingNotesAction(Context context, String mid, String eid) {

    }

    @Override
    public void showAfterOnboard(boolean isdiscard) {

    }

    @Override
    public void onPanelClicked(Object pModel, boolean isFirstLaunch) {

    }

    @Override
    public void knowledgeCollectionItemClick(KnowledgeCollectionModel.DataElements elements, String id) {

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
    public void onSendClick(String message,boolean isFromUtterance) {
        BotSocketConnectionManager.getInstance().sendMessage(message, null);
    }

    @Override
    public void onSendClick(String message, String payload, boolean isFromUtterance) {
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
    public void launchActivityWithBundle(String type, Bundle payload) {

    }

    @Override
    public void sendWithSomeDelay(String message, String payload,long time,boolean isScrollupNeeded) {

    }

    @Override
    public void copyMessageToComposer(String text, boolean isForOnboard) {

    }

    @Override
    public void showMentionNarratorContainer(boolean show, String natxt,String cotext, String res, boolean isEnd, boolean showOverlay,String templateType) {

    }

    @Override
    public void openFullView(String templateType, String data, Duration duration, int position) {

    }



    public void setBotContentFragmentUpdate(BotContentFragmentUpdate botContentFragmentUpdate) {
        this.botContentFragmentUpdate = botContentFragmentUpdate;
    }

    public void setComposeFooterUpdate(ComposeFooterUpdate composeFooterUpdate) {
        this.composeFooterUpdate = composeFooterUpdate;
    }


    @Override
    public void onQuickReplyItemClicked(String text) {
        onSendClick(text,false);
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


    @Override
    public void onBackPressed() {
        if (isOnline()) {
            BotSocketConnectionManager.getInstance().killInstance();
        }
            finish();
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

    private void attachFragments() {
        composerView = findViewById(R.id.chatLayoutPanelContainer);
        composerFragment = new BottomPanelFragment();
        composerFragment.setArguments(getIntent().getExtras());
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.chatLayoutPanelContainer, composerFragment).commit();
//        composerFragment.setComposeFooterInterface(BotChatActivity.this);
//        composerFragment.setInvokeGenericWebViewInterface(this);
//        actionsContainer = findViewById(R.id.actions_container);
        // footerContainer = findViewById(R.id.footer_container);


    }

    private void findPanelView()
    {
        pannel_recycler = (RecyclerView)findViewById(R.id.pannel_recycler);
        progressBarPanel = (ProgressBar) findViewById(R.id.progressBarPanel);
        emptyPanelView = (TextView)findViewById(R.id.emptyView);

        perssiatentPanel = findViewById(R.id.persistentPanel);
        persistentSubLayout = findViewById(R.id.panel_sub_layout);
        img_skill = findViewById(R.id.img_skill);
        txtTitle = findViewById(R.id.txtTitle);
        editButton = (TextView) findViewById(R.id.editButton);
        editButton.setTypeface(KaUtility.getTypeFaceObj(this));

        recyclerView_panel = findViewById(R.id.recyclerView_panel);
        closeBtnPanel = (TextView) findViewById(R.id.closeBtnPanel);
        closeBtnPanel.setTypeface(KaUtility.getTypeFaceObj(this));
        single_item_container = findViewById(R.id.single_item_container);

        recyclerView_panel.setLayoutManager(new LinearLayoutManager(this));
        mBottomSheetBehavior = CustomBottomSheetBehavior.from(perssiatentPanel);

        pannel_recycler.setLayoutManager(new LinearLayoutManager(BotChatActivity.this, LinearLayoutManager.HORIZONTAL, false));
    }

    /*
    @PanelName : Launch default Panel based on name
     */
    public void getPanelData(final String panelName, final boolean isFromPresence) {
        Call<List<PanelResponseData.Panel>> baseWidgetData = KaRestBuilder.getWidgetKaRestAPI().
                getWidgetPannelData(com.kore.ai.widgetsdk.net.SDKConfiguration.Client.bot_id, Utils.ah(jwtToken), com.kore.ai.widgetsdk.net.SDKConfiguration.Client.identity);
        KaRestAPIHelper.enqueueWithRetry(baseWidgetData, new Callback<List<PanelResponseData.Panel>>() {
            @Override
            public void onResponse(Call<List<PanelResponseData.Panel>> call, Response<List<PanelResponseData.Panel>> response) {
                if (response != null && response.isSuccessful()) {
                    progressBarPanel.setVisibility(View.GONE);
                    pannel_recycler.setVisibility(View.VISIBLE);
                    Log.e("Panel Response", response.body()+"");
                    PanelResponseData panelResponseData = new PanelResponseData();
                    panelResponseData.setPanels(response.body());
                    updatePannelData(panelResponseData, panelName,isFromPresence);
                }
                else {
//                    ((KoraMainActivity) getActivity()).dismissMainLoader();
                    progressBarPanel.setVisibility(View.GONE);
                    if (pannelAdapter == null || pannelAdapter.getItemCount() <= 0) {
                        emptyPanelView.setText(getString(com.kora.ai.widgetsdk.R.string.oops));
                        emptyPanelView.setTypeface(KaFontUtils.getCustomTypeface("regular", BotChatActivity.this));
                        emptyPanelView.setVisibility(View.VISIBLE);
                    } else {
                        emptyPanelView.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<PanelResponseData.Panel>> call, Throwable t) {
                progressBarPanel.setVisibility(View.GONE);
                if (pannelAdapter == null || pannelAdapter.getItemCount() <= 0) {
                    emptyPanelView.setText(getString(com.kora.ai.widgetsdk.R.string.oops));
                    emptyPanelView.setTypeface(KaFontUtils.getCustomTypeface("regular", BotChatActivity.this));
                    emptyPanelView.setVisibility(View.VISIBLE);
                } else {
                    emptyPanelView.setVisibility(View.GONE);
                }
            }
        });
    }

    private void updatePannelData(PanelResponseData panelResponseData, String panelName, boolean isFromPresence) {

        if (panelResponseData != null && panelResponseData.getPanels() != null && panelResponseData.getPanels().size() > 0) {
            emptyPanelView.setVisibility(View.GONE);
            pannelAdapter = new PannelAdapter(BotChatActivity.this, panelResponseData, this);
            pannel_recycler.setAdapter(pannelAdapter);
            PanelBaseModel model = getHomeModelData(panelResponseData, panelName);

            if (model != null) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        SharedPreferenceUtils sharedPreferenceUtils = SharedPreferenceUtils.getInstance(BotChatActivity.this);
                        if (sharedPreferenceUtils.getKeyValue(com.kore.ai.widgetsdk.utils.BundleConstants.IS_ON_BOARDED, false)) {
                        }
                    }
                }, 700);
            }

        } else {
            emptyPanelView.setTypeface(KaFontUtils.getCustomTypeface("regular", BotChatActivity.this));
            emptyPanelView.setVisibility(View.VISIBLE);
        }
    }

    private void getJWToken() {

        HashMap<String, Object> hsh = new HashMap<>();
        hsh.put("clientId", client_id);
        hsh.put("clientSecret",client_secret);
        hsh.put("identity", identity);
        hsh.put("aud","https://idproxy.kore.com/authorize");
        hsh.put("isAnonymous",1);

        Call<JWTTokenResponse> getJWTTokenService = BotJWTRestBuilder.getBotJWTRestAPI().getJWTToken(hsh);
        getJWTTokenService.enqueue(new Callback<JWTTokenResponse>() {
            @Override
            public void onResponse(Call<JWTTokenResponse> call, Response<JWTTokenResponse> response) {
                if (response.isSuccessful()) {
                    jwtKeyResponse = response.body();
                    jwtToken = jwtKeyResponse.getJwt();

                    if(sharedPreferenceUtils != null)
                    {
                        sharedPreferenceUtils.putKeyValue("JWToken", jwtToken);
                    }

                    getPanelData("home",false);
                }
            }

            @Override
            public void onFailure(Call<JWTTokenResponse> call, Throwable t) {
            }
        });
    }

    private PanelBaseModel getHomeModelData(PanelResponseData panelResponseData, String panelName) {
        PanelBaseModel model = null;
        if (panelResponseData != null && panelResponseData.getPanels() != null && panelResponseData.getPanels().size() > 0) {
            for (PanelResponseData.Panel panel : panelResponseData.getPanels()) {
                if (panel != null && panel.getName() != null && panel.getName().equalsIgnoreCase(panelName)) {
                    model = new PanelBaseModel();
                    panel.setItemClicked(true);
                    model.setData(panel);
                    return model;
                }
            }

        }

        return model;
    }

    @Override
    public void onPanelClicked(PanelBaseModel pModel)
    {
        if (pModel != null && ((PanelBaseModel) pModel).getData().get_id().equalsIgnoreCase(com.kore.ai.widgetsdk.utils.StringUtils.kora_thread)) {
            try {
                if (isAppInstalled(getApplicationContext(), packageName))
                {
                    Intent _intent = new Intent(Intent.ACTION_VIEW, Uri.parse("koretest://messages"));
                    _intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(_intent);
                }
                else
                {
                    try {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + packageName)));

                    } catch (android.content.ActivityNotFoundException anfe) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + packageName)));
                    }
                }
            } catch (Exception e) {
                if (isAppEnabled(getApplicationContext(), packageName)) {
                    Intent intent = getApplicationContext().getPackageManager().getLaunchIntentForPackage(packageName);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    getApplicationContext().startActivity(intent);
                } else
                    Toast.makeText(getApplicationContext(), appName + " app is not enabled.", Toast.LENGTH_SHORT).show();
            }
            return;
        } else if (pModel != null && ((PanelBaseModel) pModel).getData().get_id().equalsIgnoreCase(com.kore.ai.widgetsdk.utils.StringUtils.kora_team)) {
            try {
                if (isAppInstalled(getApplicationContext(), packageName)) {
                    Intent _intent = new Intent(Intent.ACTION_VIEW, Uri.parse("koretest://teams"/*"http://threads.com/thread"*/));
                    _intent.putExtra("KoreHomeState", "KoreHomeState");
                    _intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(_intent);
                } else {
                    try {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + packageName)));
                    } catch (android.content.ActivityNotFoundException anfe) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + packageName)));
                    }
                }
            } catch (Exception e) {
                if (isAppEnabled(getApplicationContext(), packageName)) {
                    Intent intent = getApplicationContext().getPackageManager().getLaunchIntentForPackage(packageName);
                    intent.putExtra("KoreHomeState", "KoreHomeState");
                    getApplicationContext().startActivity(intent);
                } else
                    Toast.makeText(getApplicationContext(), appName + " app is not enabled.", Toast.LENGTH_SHORT).show();
            }
            return;
        }


        if (mBottomSheetBehavior != null) {
            if (keyBoardShowing)
                AppUtils.showHideVirtualKeyboard(BotChatActivity.this, null, false);
            pModels = removeSystemHealth((PanelBaseModel) pModel);
            persistentSubLayout.setVisibility(View.VISIBLE);
            perssiatentPanel.setVisibility(View.VISIBLE);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    updatePanelData((PanelBaseModel) pModel, false);
                }
            }, 500);

            perssiatentPanel.post(new Runnable() {
                @Override
                public void run() {
//                    if(!isFromPresence) {
                    mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
//                    }
                }
            });
        }
    }

    private static boolean isAppEnabled(Context context, String packageName) {
        boolean appStatus = false;
        try {
            ApplicationInfo ai = context.getPackageManager().getApplicationInfo(packageName, 0);
            if (ai != null) {
                appStatus = ai.enabled;
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return appStatus;
    }

    private static boolean isAppInstalled(Context context, String packageName) {
        PackageManager pm = context.getPackageManager();
        try {
            pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException ignored) {
        }
        return false;
    }

    private PanelBaseModel removeSystemHealth(PanelBaseModel model) {
        if (model != null && model.getData() != null && model.getData().getWidgets() != null) {
            boolean isHomePanel = model.getData().getName().equalsIgnoreCase("home");
            ArrayList<WidgetsModel> newWidgets = new ArrayList<>();
            for (int index = 0; index < model.getData().getWidgets().size(); index++) {
                if (model.getData().getWidgets().get(index).getTemplateType() != null &&
                        model.getData().getWidgets().get(index).getTemplateType().equalsIgnoreCase(WidgetConstants.SYSTEM_HEALTH_TEMPLATE)) {
//                    model.getData().getWidgets().remove(index);
                    continue;
                }
                if (isHomePanel && model.getData().getWidgets().get(index).getTemplateType() != null &&
                        model.getData().getWidgets().get(index).getTemplateType().equalsIgnoreCase(WidgetConstants.MEETINGS_TEMPLATE_SERVER)) {
//                    model.getData().getWidgets().remove(index);
                    continue;
                }
                newWidgets.add( model.getData().getWidgets().get(index));
            }
            model.getData().setWidgets(newWidgets);
        }
        return model;
    }

    private void updatePanelData(PanelBaseModel pModels, boolean isFirstLaunch)
    {
        int size = pModels.getData().getWidgets().size();

//        if (size == 0) {
//            showToast("No data to show");
//            return;
//        }

        LinearLayout img_background = (LinearLayout) findViewById(com.kora.ai.widgetsdk.R.id.panel_title_icon);
        img_background.setBackgroundResource(0);

        try {
            img_skill.setVisibility(VISIBLE);
            String imageData;
            imageData = pModels.getData().getIcon();
            if (imageData.contains(",")) {
                imageData = imageData.substring(imageData.indexOf(",") + 1);
                byte[] decodedString = Base64.decode(imageData.getBytes(), Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                img_skill.setImageBitmap(decodedByte);
            } else {
                Picasso.get().load(imageData).into(img_skill);
//                selectedItem.setColor(getResources().getColor(android.R.color.transparent));
            }
        } catch (Exception e) {
            img_skill.setVisibility(GONE);
        }


        if (size > 1) {
            List<WidgetsModel> tempModel = sortData(pModels);
            if (tempModel != null && tempModel.size() > 1) {
                editButton.setVisibility(View.VISIBLE);
            }
            else {
                editButton.setVisibility(View.GONE);
            }

            recyclerView_panel.setVisibility(View.VISIBLE);
            single_item_container.removeAllViews();
            single_item_container.setVisibility(View.GONE);
            if (widgetBaseAdapter == null)
                widgetBaseAdapter = new KaWidgetBaseAdapterNew(this, size > 1 ? WidgetViewMoreEnum.COLLAPSE_VIEW : WidgetViewMoreEnum.EXPAND_VIEW, isFirstLaunch, (""));
            widgetBaseAdapter.setFirstLaunch(isFirstLaunch);
            recyclerView_panel.setAdapter(widgetBaseAdapter);
            widgetBaseAdapter.setWidget(pModels, jwtToken);
            widgetBaseAdapter.notifyDataSetChanged();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Utility.setRecyclerViewTempForOnboard(recyclerView_panel, pModels.getData().get_id());
                }
            }, 500);
        } else {
            editButton.setVisibility(View.GONE);
            single_item_container.setVisibility(View.VISIBLE);
            recyclerView_panel.setVisibility(View.GONE);
            if (widgetBaseAdapter != null) {
                recyclerView_panel.setAdapter(null);
                widgetBaseAdapter.notifyDataSetChanged();
            }
            single_item_container.removeAllViews();
            single_item_container.addView(getWidgetObject(pModels));
        }

        txtTitle.setText(pModels.getData().getName());
    }

    public int getItemViewType(WidgetsModel widget) {


        switch (widget.getTemplateType().toLowerCase()) {
            case WidgetConstants.MEETINGS_TEMPLATE_SERVER:
                return WidgetConstants.MEETINGS_TEMPLATE;
            case WidgetConstants.CHART_LIST:
                return WidgetConstants.CHART_LIST_TEMPLATE;

            case WidgetConstants.TASK_LIST:
                if (widget.getTemplateType().toLowerCase().equals("list")) {
                    return WidgetConstants.TASKS_SINGLE_TEMPLATE;
                } else {
                    return WidgetConstants.TASK_LIST_TEMPLATE;
                }

            case WidgetConstants.FILES_TEMPLATE_SERVER:
                if (widget.getTemplateType().toLowerCase().equals("list")) {
                    return WidgetConstants.FILES_SINGLE_TEMPLATE;
                } else {
                    return WidgetConstants.FILES_TEMPLATE;
                }

            case WidgetConstants.HASH_TAG_TEMPLATE_SERVER:
                return WidgetConstants.HASH_TAG_TEMPLATE;

            case WidgetConstants.ARTICLES_TEMPLATE_SERVER:
                return WidgetConstants.ARTICLES_TEMPLATE;

            case WidgetConstants.ANNOUNCEMENTS_TEMPLATE_SERVER:
                return WidgetConstants.ANNOUNCEMENTS_TEMPLATE;

            case WidgetConstants.SKILL_TEMPLATE_SERVER:
                return WidgetConstants.SKILL_TEMPLATE;

            case WidgetConstants.CLOUD_TEMPLATE_SERVER:
                return WidgetConstants.CLOUD_TEMPLATE;

            case WidgetConstants.HEADLINE_TEMPLATE_SERVER:
                return WidgetConstants.HEADLINE_TEMPLATE;
            case WidgetConstants.PIE_CHART:
                return WidgetConstants.PIE_CHART_TEMPLATE;
            case WidgetConstants.BAR_CHART:
                return WidgetConstants.BAR_CHART_TEMPLATE;
            case WidgetConstants.LINE_CHART:
                return WidgetConstants.LINE_CHART_TEMPLATE;
            case WidgetConstants.LIST_WIDGET:
                return WidgetConstants.LIST_WIDGET_TEMPLATE;
            default:
                return WidgetConstants.DEFAULT_TEMPLATE;

        }
    }

    public View getWidgetObject(PanelBaseModel pModels) {
        View view = null;
        PanelLevelData panelData = new PanelLevelData();
        panelData.set_id("Id");
        panelData.setSkillId("Skill Id");
        panelData.setName(pModels.getData().getName() != null ? pModels.getData().getName() : "");

        switch (getItemViewType(pModels.getData().getWidgets().get(0))) {
            case WidgetConstants.MEETINGS_TEMPLATE:
                MeetingWidgetView mView = new MeetingWidgetView(this, this, pModels.getData().getName(), WidgetViewMoreEnum.EXPAND_VIEW);
                mView.setWidget(pModels.getData().getWidgets().get(0), panelData);
                view = mView;
                break;
            case WidgetConstants.CHART_LIST_TEMPLATE:
                ChartListWidgetView chartListWidgetView = new ChartListWidgetView(this, this, pModels.getData().getName(), WidgetViewMoreEnum.EXPAND_VIEW);
                chartListWidgetView.setWidget(pModels.getData().getWidgets().get(0), panelData);
                view = chartListWidgetView;
                break;
            case WidgetConstants.ARTICLES_TEMPLATE:
                ArticlesWidgetView articlesWidgetView = new ArticlesWidgetView(this, WidgetViewMoreEnum.EXPAND_VIEW);
                articlesWidgetView.setWidget(pModels.getData().getName(),pModels.getData().getWidgets().get(0), 0, true, panelData);
                view = articlesWidgetView;
                break;
//            case WidgetConstants.HASH_TAG_TEMPLATE:
//                TrendingHashTagView hashTagView = new TrendingHashTagView(this, this, pModels.getData().getName(), WidgetViewMoreEnum.EXPAND_VIEW);
//                view = hashTagView;
//                break;

            case WidgetConstants.SKILL_TEMPLATE:
                SkillWidgetView skillWidgetView = new SkillWidgetView(this, pModels.getData().getName(), WidgetViewMoreEnum.EXPAND_VIEW);
//                skillWidgetView.setWidget(pModels.getData().getWidgets().get(0), panelData);
                view = skillWidgetView;
                break;

            case WidgetConstants.FILES_SINGLE_TEMPLATE:
            case WidgetConstants.TASKS_SINGLE_TEMPLATE:
                GenericWidgetView gwv = new GenericWidgetView(this, 0, this, null,
                        pModels.getData().getName(), "180", false, WidgetViewMoreEnum.EXPAND_VIEW);
                gwv.setWidget(pModels.getData().getName(),pModels.getData().getWidgets().get(0), true, panelData);
                view = gwv;
                break;
            case WidgetConstants.PIE_CHART_TEMPLATE:
                PieChartWidgetView pieChartWidgetView = new PieChartWidgetView(this, pModels.getData().getName());
                pieChartWidgetView.setWidget(pModels.getData().getName(),pModels.getData().getWidgets().get(0), panelData, jwtToken);
                view = pieChartWidgetView;
                break;
            case WidgetConstants.BAR_CHART_TEMPLATE:
                BarChartWidgetView barChartWidgetView = new BarChartWidgetView(this);
                barChartWidgetView.setWidget(pModels.getData().getName(),pModels.getData().getWidgets().get(0), panelData, jwtToken);
                view = barChartWidgetView;
                break;

            case WidgetConstants.LINE_CHART_TEMPLATE:
                LineChartWidgetView lineChartWidgetView = new LineChartWidgetView(this);
                lineChartWidgetView.setWidget(pModels.getData().getName(),pModels.getData().getWidgets().get(0), panelData, jwtToken);
                view = lineChartWidgetView;
                break;

            case WidgetConstants.LIST_WIDGET_TEMPLATE:
                ListWidgetView listWidgetView = new ListWidgetView(this, pModels.getData().getName(), WidgetViewMoreEnum.EXPAND_VIEW);
                listWidgetView.setWidget(pModels.getData().getWidgets().get(0), panelData,"Ask MyIT", jwtToken);
                view = listWidgetView;
                break;

            case WidgetConstants.DEFAULT_TEMPLATE:
                DefaultWidgetView dView = new DefaultWidgetView(this, pModels.getData().getName(), WidgetViewMoreEnum.EXPAND_VIEW);
                dView.setWidget(pModels.getData().getWidgets().get(0), panelData, jwtToken);
                view = dView;
                break;
        }

        return view;

    }

    public List<WidgetsModel> sortData(PanelBaseModel pModels) {
        List<WidgetsModel> widgetsList = new ArrayList<>();
        if (pModels != null && pModels.getData() != null) {

            for (WidgetsModel widget : pModels.getData().getWidgets()) {
                if (!StringUtils.isNullOrEmptyWithTrim(widget.getName())) {
                    widgetsList.add(widget);
                }
            }
        }
        return widgetsList;
    }

    @Override
    public void knowledgeItemClicked(Bundle extras, boolean isKnowledge) {

    }

    @Override
    public void driveItemClicked(BotCaourselButtonModel botCaourselButtonModel) {

    }

    @Override
    public void emailItemClicked(String action, HashMap customData) {

    }

    @Override
    public void calendarItemClicked(String action, BaseCalenderTemplateModel model) {

    }

    @Override
    public void tasksSelectedOrDeselected(boolean selecetd) {

    }

    @Override
    public void widgetItemSelected(boolean isSelected, int count) {

    }

    @Override
    public void navigationToDialAndJoin(String actiontype, String actionLink) {

    }

    @Override
    public void takeNotesNavigation(BaseCalenderTemplateModel baseCalenderTemplateModel) {

    }

    @Override
    public void meetingNotesNavigation(Context context, String mId, String eId) {

    }

    @Override
    public void meetingWidgetViewMoreVisibility(boolean visible) {

    }

    @Override
    public void calendarContactItemClick(ContactViewListModel model) {

    }

    @Override
    public void welcomeSummaryItemClick(WelcomeChatSummaryModel model) {

    }

    @Override
    public void knowledgeCollectionItemClick(com.kore.ai.widgetsdk.models.KnowledgeCollectionModel.DataElements elements, String id) {

    }

    @Override
    public void updateItemToRefresh(int pos) {

    }

    @Override
    public void updateWeatherWidgetSummery(int type, String summary) {

    }

    @Override
    public void onWidgetMenuButtonClicked() {

    }
}
