package kore.botssdk.activity;

import static kore.botssdk.activity.KaCaptureImageActivity.rotateIfNecessary;
import static kore.botssdk.net.SDKConfiguration.Client.enable_ack_delivery;
import static kore.botssdk.utils.BundleConstants.CAPTURE_IMAGE_CHOOSE_FILES_BUNDLED_PREMISSION_REQUEST;
import static kore.botssdk.utils.BundleConstants.GROUP_KEY_NOTIFICATIONS;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.FragmentTransaction;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import io.reactivex.annotations.NonNull;
import kore.botssdk.R;
import kore.botssdk.application.BotApplication;
import kore.botssdk.bot.BotClient;
import kore.botssdk.event.KoreEventCenter;
import kore.botssdk.events.SocketDataTransferModel;
import kore.botssdk.fileupload.core.KoreWorker;
import kore.botssdk.fileupload.core.UploadBulkFile;
import kore.botssdk.fragment.BotContentFragment;
import kore.botssdk.fragment.ComposeFooterFragment;
import kore.botssdk.listener.BaseSocketConnectionManager;
import kore.botssdk.listener.BotContentFragmentUpdate;
import kore.botssdk.listener.BotSocketConnectionManager;
import kore.botssdk.listener.ComposeFooterInterface;
import kore.botssdk.listener.ComposeFooterUpdate;
import kore.botssdk.listener.InvokeGenericWebViewInterface;
import kore.botssdk.listener.SocketChatListener;
import kore.botssdk.listener.TTSUpdate;
import kore.botssdk.models.AgentInfoModel;
import kore.botssdk.models.BotActiveThemeModel;
import kore.botssdk.models.BotButtonModel;
import kore.botssdk.models.BotInfoModel;
import kore.botssdk.models.BotMetaModel;
import kore.botssdk.models.BotOptionsModel;
import kore.botssdk.models.BotRequest;
import kore.botssdk.models.BotResponse;
import kore.botssdk.models.BotResponseMessage;
import kore.botssdk.models.BotResponsePayLoadText;
import kore.botssdk.models.BrandingModel;
import kore.botssdk.models.BrandingNewModel;
import kore.botssdk.models.BrandingV3Model;
import kore.botssdk.models.CalEventsTemplateModel;
import kore.botssdk.models.ComponentModel;
import kore.botssdk.models.ComponentModelPayloadText;
import kore.botssdk.models.FormActionTemplate;
import kore.botssdk.models.KnowledgeCollectionModel;
import kore.botssdk.models.KoreComponentModel;
import kore.botssdk.models.KoreMedia;
import kore.botssdk.models.PayloadHeaderModel;
import kore.botssdk.models.PayloadInner;
import kore.botssdk.models.PayloadOuter;
import kore.botssdk.models.WebHookRequestModel;
import kore.botssdk.models.WebHookResponseDataModel;
import kore.botssdk.net.BrandingRestBuilder;
import kore.botssdk.net.RestBuilder;
import kore.botssdk.net.RestResponse;
import kore.botssdk.net.SDKConfiguration;
import kore.botssdk.net.WebHookRestBuilder;
import kore.botssdk.pushnotification.PushNotificationRegister;
import kore.botssdk.utils.AsyncTaskExecutor;
import kore.botssdk.utils.BitmapUtils;
import kore.botssdk.utils.BundleConstants;
import kore.botssdk.utils.BundleUtils;
import kore.botssdk.utils.ClosingService;
import kore.botssdk.utils.DateUtils;
import kore.botssdk.utils.KaMediaUtils;
import kore.botssdk.utils.KaPermissionsHelper;
import kore.botssdk.utils.LogUtils;
import kore.botssdk.utils.StringUtils;
import kore.botssdk.utils.TTSSynthesizer;
import kore.botssdk.websocket.SocketWrapper;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Copyright (c) 2014 Kore Inc. All rights reserved.
 */
@SuppressLint("UnknownNullness")
public class BotChatActivity extends BotAppCompactActivity implements ComposeFooterInterface, TTSUpdate, InvokeGenericWebViewInterface {
    final String LOG_TAG = BotChatActivity.class.getSimpleName();
    FrameLayout chatLayoutFooterContainer;
    FrameLayout chatLayoutContentContainer;
    FrameLayout chatLayoutPanelContainer;
    ProgressBar taskProgressBar;
    FragmentTransaction fragmentTransaction;
    final Handler handler = new Handler();
    String chatBot, taskBotId, jwt;
    BotClient botClient;
    BotContentFragment botContentFragment;
    ComposeFooterFragment composeFooterFragment;
    TTSSynthesizer ttsSynthesizer;
    BotContentFragmentUpdate botContentFragmentUpdate;
    ComposeFooterUpdate composeFooterUpdate;
    final Gson gson = new Gson();
    SharedPreferences sharedPreferences;
    protected final int compressQualityInt = 100;
    final Handler messageHandler = new Handler();
    private String fileUrl;
    BotActiveThemeModel brandingNewDos;
    WebHookResponseDataModel webHookResponseDataModel;
    BotMetaModel botMetaModel;
    Runnable runnable;
    private final int poll_delay = 2000;
    String lastMsgId = "";
    private static String uniqueID = null;
    private static final String PREF_UNIQUE_ID = "PREF_UNIQUE_ID";
    boolean isAgentTransfer = false;
    ArrayList<String> arrMessageList = new ArrayList<>();
    boolean isReconnectionStopped = false;
    String resp = "";
    BroadcastReceiver onDestroyReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (isAgentTransfer && botClient != null) {
                botClient.sendAgentCloseMessage("", SDKConfiguration.Client.bot_name, SDKConfiguration.Client.bot_id);
            }

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(BundleConstants.IS_RECONNECT, false);
            editor.putInt(BotResponse.HISTORY_COUNT, 0);
            editor.apply();
        }
    };

    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bot_chat_layout);

        findViews();
        getBundleInfo();
        getDataFromTxt();

        botClient = new BotClient(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            registerReceiver(onDestroyReceiver, new IntentFilter(BundleConstants.DESTROY_EVENT), RECEIVER_NOT_EXPORTED);
        else registerReceiver(onDestroyReceiver, new IntentFilter(BundleConstants.DESTROY_EVENT));

        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        //Add Bot Content Fragment
        botContentFragment = new BotContentFragment();
        botContentFragment.setArguments(getIntent().getExtras());
        botContentFragment.setComposeFooterInterface(this);
        botContentFragment.setInvokeGenericWebViewInterface(this);
        fragmentTransaction.add(R.id.chatLayoutContentContainer, botContentFragment).commit();
        setBotContentFragmentUpdate(botContentFragment);

        //Add Bot Compose Footer Fragment
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        composeFooterFragment = new ComposeFooterFragment();
        composeFooterFragment.setArguments(getIntent().getExtras());
        composeFooterFragment.setComposeFooterInterface(this);
        composeFooterFragment.setBottomOptionData(getDataFromTxt());
        composeFooterFragment.setBotClient(botClient);
        fragmentTransaction.add(R.id.chatLayoutFooterContainer, composeFooterFragment).commit();
        setComposeFooterUpdate(composeFooterFragment);

        ttsSynthesizer = new TTSSynthesizer(this);
        setupTextToSpeech();
        KoreEventCenter.register(this);

        if (!SDKConfiguration.Client.isWebHook) {
            BotSocketConnectionManager.getInstance().setChatListener(sListener);
        }

        BotSocketConnectionManager.getInstance().startAndInitiateConnectionWithReconnect(getApplicationContext(), SDKConfiguration.Server.customData, sharedPreferences.getBoolean(BundleConstants.IS_RECONNECT, false));

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (isOnline()) {
                    showCloseAlert();
                }
            }
        });

        startService(new Intent(getApplicationContext(), ClosingService.class));
    }

    final SocketChatListener sListener = new SocketChatListener() {
        @Override
        public void onMessage(BotResponse botResponse) {
            processPayload("", botResponse);
        }

        @Override
        public void onConnectionStateChanged(BaseSocketConnectionManager.CONNECTION_STATE state, boolean isReconnection) {
            if (state == BaseSocketConnectionManager.CONNECTION_STATE.CONNECTED) {
                isReconnectionStopped = false;
                getBrandingDetails();

                if (botContentFragment != null && isReconnection) {
                    if (sharedPreferences.getInt(BotResponse.HISTORY_COUNT, 0) > 19) botContentFragment.loadChatHistory(0, 20);
                    else if (sharedPreferences.getInt(BotResponse.HISTORY_COUNT, 0) > 0)
                        botContentFragment.loadChatHistory(0, sharedPreferences.getInt(BotResponse.HISTORY_COUNT, 1));
                    else botContentFragment.loadReconnectionChatHistory(0, 10);
                }
            } else if (state == BaseSocketConnectionManager.CONNECTION_STATE.RECONNECTION_STOPPED) {
                if (!isReconnectionStopped) {
                    isReconnectionStopped = true;
                    showReconnectionStopped();
                }
            }

            new PushNotificationRegister().registerPushNotification(botClient.getUserId(), botClient.getAccessToken(), getUniqueDeviceId(BotChatActivity.this));
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

    public void postNotification(String title, String pushMessage) {
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder nBuilder;
        if (Build.VERSION.SDK_INT >= 26) {
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel notificationChannel = new NotificationChannel("Kore_Push_Service", "Kore_Android", importance);
            mNotificationManager.createNotificationChannel(notificationChannel);
            nBuilder = new NotificationCompat.Builder(this, notificationChannel.getId());
        } else {
            nBuilder = new NotificationCompat.Builder(this);
        }

        nBuilder.setContentTitle(title).setSmallIcon(R.drawable.ic_launcher).setColor(Color.parseColor("#009dab")).setContentText(pushMessage).setGroup(GROUP_KEY_NOTIFICATIONS).setGroupSummary(true).setAutoCancel(true).setPriority(NotificationCompat.PRIORITY_HIGH);
        if (alarmSound != null) {
            nBuilder.setSound(alarmSound);
        }

        Intent intent = new Intent(getApplicationContext(), BotChatActivity.class);
        Bundle bundle = new Bundle();
        //This should not be null
        bundle.putBoolean(BundleUtils.SHOW_PROFILE_PIC, false);
        bundle.putString(BundleUtils.PICK_TYPE, "Notification");
        bundle.putString(BundleUtils.BOT_NAME_INITIALS, String.valueOf(SDKConfiguration.Client.bot_name.charAt(0)));
        intent.putExtras(bundle);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_MUTABLE);
        nBuilder.setContentIntent(pendingIntent);

        Notification notification = nBuilder.build();
        notification.ledARGB = 0xff0000FF;

        mNotificationManager.notify("YUIYUYIU", 237891, notification);
    }

    @Override
    protected void onDestroy() {
        if (isAgentTransfer && botClient != null)
            botClient.sendAgentCloseMessage("", SDKConfiguration.Client.bot_name, SDKConfiguration.Client.bot_id);

        new PushNotificationRegister().unsubscribePushNotification(botClient.getUserId(), botClient.getAccessToken(), sharedPreferences.getString("PREF_UNIQUE_ID", getUniqueDeviceId(BotChatActivity.this)));

        if (botClient != null) botClient.disconnect();
        KoreEventCenter.unregister(this);
        super.onDestroy();
    }

    private void getBundleInfo() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            jwt = bundle.getString(BundleUtils.JWT_TKN, "");
        }
        chatBot = SDKConfiguration.Client.bot_name;
        taskBotId = SDKConfiguration.Client.bot_id;
    }

    private void findViews() {
        chatLayoutFooterContainer = findViewById(R.id.chatLayoutFooterContainer);
        chatLayoutContentContainer = findViewById(R.id.chatLayoutContentContainer);
        chatLayoutPanelContainer = findViewById(R.id.chatLayoutPanelContainer);
        taskProgressBar = findViewById(R.id.taskProgressBar);
        sharedPreferences = getSharedPreferences(BotResponse.THEME_NAME, Context.MODE_PRIVATE);
        RestBuilder.setContext(BotChatActivity.this);
        WebHookRestBuilder.setContext(BotChatActivity.this);
        BrandingRestBuilder.setContext(BotChatActivity.this);
    }

    void updateTitleBar(BaseSocketConnectionManager.CONNECTION_STATE socketConnectionEvents) {

        switch (socketConnectionEvents) {
            case CONNECTING:
                taskProgressBar.setVisibility(View.VISIBLE);
                break;
            case CONNECTED:
                taskProgressBar.setVisibility(View.GONE);
                composeFooterFragment.enableSendButton();

                break;
            case DISCONNECTED:
            case CONNECTED_BUT_DISCONNECTED:
                taskProgressBar.setVisibility(View.VISIBLE);
                composeFooterFragment.setDisabled(true);
                composeFooterFragment.updateUI();
                break;

            default:
                taskProgressBar.setVisibility(View.GONE);
        }
    }


    private void setupTextToSpeech() {
        composeFooterFragment.setTtsUpdate(BotSocketConnectionManager.getInstance());
        botContentFragment.setTtsUpdate(BotSocketConnectionManager.getInstance());
    }

    public void onEvent(String jwt) {
        this.jwt = jwt;
        if (SDKConfiguration.Client.isWebHook) {
            if (botContentFragment != null) botContentFragment.setJwtTokenForWebHook(jwt);

            if (composeFooterFragment != null) composeFooterFragment.setJwtToken(jwt);

            getWebHookMeta();
        }
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

    public void onEvent(BrandingModel brandingModel) {
        SharedPreferences.Editor editor = getSharedPreferences(BotResponse.THEME_NAME, Context.MODE_PRIVATE).edit();
        editor.putString(BotResponse.BUBBLE_LEFT_BG_COLOR, brandingModel.getBotchatBgColor());
        editor.putString(BotResponse.BUBBLE_LEFT_TEXT_COLOR, brandingModel.getBotchatTextColor());
        editor.putString(BotResponse.BUBBLE_RIGHT_BG_COLOR, brandingModel.getUserchatBgColor());
        editor.putString(BotResponse.BUBBLE_RIGHT_TEXT_COLOR, brandingModel.getUserchatTextColor());
        editor.putString(BotResponse.BUTTON_ACTIVE_BG_COLOR, brandingModel.getButtonActiveBgColor());
        editor.putString(BotResponse.BUTTON_ACTIVE_TXT_COLOR, brandingModel.getButtonActiveTextColor());
        editor.putString(BotResponse.BUTTON_INACTIVE_BG_COLOR, brandingModel.getButtonInactiveBgColor());
        editor.putString(BotResponse.BUTTON_INACTIVE_TXT_COLOR, brandingModel.getButtonInactiveTextColor());
        editor.putString(BotResponse.WIDGET_BG_COLOR, brandingModel.getWidgetBodyColor());
        editor.putString(BotResponse.WIDGET_TXT_COLOR, brandingModel.getWidgetTextColor());
        editor.putString(BotResponse.WIDGET_BORDER_COLOR, brandingModel.getWidgetBorderColor());
        editor.putString(BotResponse.BUTTON_BORDER_COLOR, brandingModel.getButtonBorderColor());
        editor.putString(BotResponse.WIDGET_DIVIDER_COLOR, brandingModel.getWidgetDividerColor());
        editor.putString(BotResponse.BUBBLE_STYLE, brandingModel.getChatBubbleStyle());
        editor.apply();

        SDKConfiguration.BubbleColors.quickReplyColor = brandingModel.getButtonActiveBgColor();
        SDKConfiguration.BubbleColors.quickReplyTextColor = brandingModel.getButtonActiveTextColor();
        SDKConfiguration.BubbleColors.quickBorderColor = brandingModel.getButtonBorderColor();

        if (botContentFragment != null)
            botContentFragment.changeThemeBackGround(brandingModel.getWidgetBodyColor(), brandingModel.getWidgetHeaderColor(), brandingModel.getWidgetTextColor(), brandingModel.getBotName());

        if (composeFooterFragment != null)
            composeFooterFragment.changeThemeBackGround(brandingModel.getWidgetFooterColor(), brandingModel.getWidgetFooterHintColor());
    }


    public void onEvent(BotResponse botResponse) {
        processPayload("", botResponse);
    }

    public void updateActionbar(boolean isSelected, String type, ArrayList<BotButtonModel> buttonModels) {

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

    @Override
    public void externalReadWritePermission(String fileUrl) {
        this.fileUrl = fileUrl;
        if (!KaPermissionsHelper.hasPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            KaPermissionsHelper.requestForPermission(this, CAPTURE_IMAGE_CHOOSE_FILES_BUNDLED_PREMISSION_REQUEST, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
    }

    @Override
    public void onDeepLinkClicked(String url) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @androidx.annotation.NonNull String[] permissions, @androidx.annotation.NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAPTURE_IMAGE_CHOOSE_FILES_BUNDLED_PREMISSION_REQUEST) {
            if (KaPermissionsHelper.hasPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE/*,Manifest.permission.RECORD_AUDIO*/)) {

                if (!StringUtils.isNullOrEmpty(fileUrl)) KaMediaUtils.saveFileFromUrlToKorePath(BotChatActivity.this, fileUrl);
            } else {
                Toast.makeText(getApplicationContext(), "Access denied. Operation failed !!", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onPause() {
        BotApplication.activityPaused();
        ttsSynthesizer.stopTextToSpeech();
        super.onPause();
    }

    @Override
    public void onSendClick(String message, boolean isFromUtterance) {
        if (!StringUtils.isNullOrEmpty(message)) {
            if (!SDKConfiguration.Client.isWebHook) BotSocketConnectionManager.getInstance().sendMessage(message, null);
            else {
                addSentMessageToChat(message);
                sendWebHookMessage(false, message, null);
                BotSocketConnectionManager.getInstance().stopTextToSpeech();
            }
        }
    }


    @Override
    public void onSendClick(String message, String payload, boolean isFromUtterance) {
        if (!SDKConfiguration.Client.isWebHook) {
            if (payload != null) {
                BotSocketConnectionManager.getInstance().sendPayload(message, payload);
            } else {
                BotSocketConnectionManager.getInstance().sendMessage(message, "");
            }
        } else {
            BotSocketConnectionManager.getInstance().stopTextToSpeech();
            if (payload != null) {
                addSentMessageToChat(message);
                sendWebHookMessage(false, payload, null);
            } else {
                addSentMessageToChat(message);
                sendWebHookMessage(false, message, null);
            }
        }
    }

    @Override
    public void onSendClick(String message, ArrayList<HashMap<String, String>> attachments, boolean isFromUtterance) {
        if (attachments != null && attachments.size() > 0) {
            if (!SDKConfiguration.Client.isWebHook) BotSocketConnectionManager.getInstance().sendAttachmentMessage(message, attachments);
            else {
                addSentMessageToChat(message);
                sendWebHookMessage(false, message, attachments);
            }
        }
    }

    @Override
    public void onFormActionButtonClicked(FormActionTemplate fTemplate) {

    }

    @Override
    public void launchActivityWithBundle(String type, Bundle payload) {

    }

    @Override
    public void sendWithSomeDelay(String message, String payload, long time, boolean isScrollupNeeded) {
        LogUtils.e("Message", message);
    }

    @Override
    public void copyMessageToComposer(String text, boolean isForOnboard) {
        composeFooterFragment.setComposeText(text);
    }

    @Override
    public void showMentionNarratorContainer(boolean show, String natxt, String cotext, String res, boolean isEnd, boolean showOverlay, String templateType) {

    }

    @Override
    public void openFullView(String templateType, String data, CalEventsTemplateModel.Duration duration, int position) {

    }


    public void setBotContentFragmentUpdate(BotContentFragmentUpdate botContentFragmentUpdate) {
        this.botContentFragmentUpdate = botContentFragmentUpdate;
    }

    public void setComposeFooterUpdate(ComposeFooterUpdate composeFooterUpdate) {
        this.composeFooterUpdate = composeFooterUpdate;
    }


    /**
     * payload processing
     */

    void processPayload(String payload, BotResponse botLocalResponse) {
        if (botLocalResponse == null) BotSocketConnectionManager.getInstance().stopDelayMsgTimer();

        try {
            final BotResponse botResponse = botLocalResponse != null ? botLocalResponse : gson.fromJson(payload, BotResponse.class);
            if (botResponse == null || botResponse.getMessage() == null || botResponse.getMessage().isEmpty()) {
                return;
            }
            if (botResponse.getMessageId() != null) lastMsgId = botResponse.getMessageId();
            try {
                long timeMillis = botResponse.getTimeInMillis(botResponse.getCreatedOn(), true);
                botResponse.setCreatedInMillis(timeMillis);
                botResponse.setFormattedDate(DateUtils.formattedSentDateV6(timeMillis));
                botResponse.setTimeStamp(botResponse.prepareTimeStamp(timeMillis));
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }

            if (botClient != null && enable_ack_delivery)
                botClient.sendMsgAcknowledgement(botResponse.getTimestamp(), botResponse.getKey());

            LogUtils.d(LOG_TAG, payload);
            isAgentTransfer = botResponse.isFromAgent();

            if (!StringUtils.isNullOrEmpty(botResponse.getIcon())) SDKConfiguration.BubbleColors.setIcon_url(botResponse.getIcon());

            if (composeFooterFragment != null) composeFooterFragment.setIsAgentConnected(isAgentTransfer);

            if (botClient != null && isAgentTransfer) {
                botClient.sendReceipts(BundleConstants.MESSAGE_DELIVERED, botResponse.getMessageId());
                if (BotApplication.isActivityVisible()) {
                    botClient.sendReceipts(BundleConstants.MESSAGE_READ, botResponse.getMessageId());
                } else {
                    arrMessageList.add(botResponse.getMessageId());
                }
            }

            PayloadOuter payOuter = null;
            if (!botResponse.getMessage().isEmpty()) {
                ComponentModel compModel = botResponse.getMessage().get(0).getComponent();
                if (compModel != null) {
                    payOuter = compModel.getPayload();
                    if (payOuter != null) {
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

            if (!BotApplication.isActivityVisible()) {
                postNotification("Kore Message", "Received new message.");
            }

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    botContentFragment.addMessageToBotChatAdapter(botResponse);
                    textToSpeech(botResponse);
                    botContentFragment.setQuickRepliesIntoFooter(botResponse);
                    botContentFragment.showCalendarIntoFooter(botResponse);
                }
            }, BundleConstants.TYPING_STATUS_TIME);
        } catch (Exception e) {
            e.printStackTrace();
            if (e instanceof JsonSyntaxException) {
                try {
                    //This is the case Bot returning user sent message from another channel
                    if (botContentFragment != null) {
                        BotRequest botRequest = gson.fromJson(payload, BotRequest.class);
                        if (botRequest != null && botRequest.getMessage() != null && !StringUtils.isNullOrEmpty(botRequest.getMessage().getBody())) {
                            botRequest.setCreatedOn(DateUtils.isoFormatter.format(new Date()));
                            botContentFragment.updateContentListOnSend(botRequest);
                        } else {
                            final AgentInfoModel botResponse = gson.fromJson(payload, AgentInfoModel.class);

                            if (botResponse == null || botResponse.getMessage() == null || StringUtils.isNullOrEmpty(botResponse.getMessage().getType())) {
                                return;
                            }

                            if (botResponse.getMessage().getType().equalsIgnoreCase("agent_connected")) {
                                setPreferenceObject(botResponse.getMessage().getAgentInfo(), BotResponse.AGENT_INFO_KEY);
                            } else if (botResponse.getMessage().getType().equalsIgnoreCase("agent_disconnected")) {
                                setPreferenceObject("", BotResponse.AGENT_INFO_KEY);
                            }

                            if (botResponse.getCustomEvent().equalsIgnoreCase(BotResponse.EVENT)) {
                                if (botResponse.getMessage() != null && !StringUtils.isNullOrEmpty(botResponse.getMessage().getType()) && botResponse.getMessage().getType().equalsIgnoreCase(BundleConstants.TYPING))
                                    botContentFragment.showTypingStatus();
                            }
                        }
                    }
                } catch (Exception e1) {
                    try {
                        final BotResponsePayLoadText botResponse = gson.fromJson(payload, BotResponsePayLoadText.class);
                        if (botResponse == null || botResponse.getMessage() == null || botResponse.getMessage().isEmpty()) {
                            return;
                        }
                        LogUtils.d(LOG_TAG, payload);

                        if (!StringUtils.isNullOrEmpty(botResponse.getIcon()))
                            SDKConfiguration.BubbleColors.setIcon_url(botResponse.getIcon());

                        if (!botResponse.getMessage().isEmpty()) {
                            ComponentModelPayloadText compModel = botResponse.getMessage().get(0).getComponent();
                            if (compModel != null && !StringUtils.isNullOrEmpty(compModel.getPayload())) {
                                displayMessage(compModel.getPayload(), BotResponse.COMPONENT_TYPE_TEXT, botResponse.getMessageId());
                            }
                        }
                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }
                }
            }
        }
    }

    void showCloseAlert() {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        if (sharedPreferences != null) {
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putBoolean(BundleConstants.IS_RECONNECT, true);
                            editor.putInt(BotResponse.HISTORY_COUNT, botContentFragment.getAdapterCount());
                            editor.apply();
                        }
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        if (sharedPreferences != null) {
                            if (botClient != null && isAgentTransfer)
                                botClient.sendAgentCloseMessage("", SDKConfiguration.Client.bot_name, SDKConfiguration.Client.bot_id);

                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putBoolean(BundleConstants.IS_RECONNECT, false);
                            editor.putInt(BotResponse.HISTORY_COUNT, 0);
                            editor.apply();
                        }
                        break;
                }

                BotSocketConnectionManager.killInstance();
                finish();
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(BotChatActivity.this);
        builder.setMessage(R.string.close_or_minimize).setCancelable(false).setPositiveButton(R.string.minimize, dialogClickListener).setNegativeButton(R.string.close, dialogClickListener).show();
    }

    public void setPreferenceObject(Object modal, String key) {
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        Gson gson = new Gson();
        String jsonObject = gson.toJson(modal);
        prefsEditor.putString(key, jsonObject);
        prefsEditor.apply();
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
    public void handleUserActions(String action, HashMap<String, Object> payload) {


    }

    @Override
    public void onStop() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
            List<ActivityManager.RunningTaskInfo> taskList = activityManager.getRunningTasks(10);

            if (!taskList.isEmpty()) {
                ActivityManager.RunningTaskInfo runningTaskInfo = taskList.get(0);
                if (runningTaskInfo.topActivity != null && !runningTaskInfo.topActivity.getClassName().contains("kore.botssdk")) {
                    if (botClient != null) {
                        botClient.sendAgentCloseMessage("", SDKConfiguration.Client.bot_name, SDKConfiguration.Client.bot_id);

                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean(BundleConstants.IS_RECONNECT, false);
                        editor.putInt(BotResponse.HISTORY_COUNT, 0);
                        String jsonObject = new Gson().toJson("");
                        editor.putString(BotResponse.AGENT_INFO_KEY, jsonObject);
                        editor.apply();
                    }
                }
            } else {
                if (botClient != null) {
                    botClient.sendAgentCloseMessage("", SDKConfiguration.Client.bot_name, SDKConfiguration.Client.bot_id);

                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean(BundleConstants.IS_RECONNECT, false);
                    editor.putInt(BotResponse.HISTORY_COUNT, 0);
                    String jsonObject = new Gson().toJson("");
                    editor.putString(BotResponse.AGENT_INFO_KEY, jsonObject);
                    editor.apply();
                }
            }
        }

        BotSocketConnectionManager.getInstance().unSubscribe();
        super.onStop();
    }

    public BotOptionsModel getDataFromTxt() {
        BotOptionsModel botOptionsModel = null;

        try {
            InputStream is = getResources().openRawResource(R.raw.option);
            Reader reader = new InputStreamReader(is);
            botOptionsModel = gson.fromJson(reader, BotOptionsModel.class);
            LogUtils.e("Options Size", String.valueOf(botOptionsModel.getTasks().size()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return botOptionsModel;
    }

    public BrandingModel getBrandingDataFromTxt() {
        BotActiveThemeModel botActiveThemeModel;
        try {
            InputStream is = getResources().openRawResource(R.raw.branding_response);
            Reader reader = new InputStreamReader(is);
            botActiveThemeModel = gson.fromJson(reader, BotActiveThemeModel.class);
            if (botActiveThemeModel != null) {
                BrandingModel brandingModel = new BrandingModel();
                brandingModel.setBotchatBgColor(botActiveThemeModel.getBotMessage().getBubbleColor());
                brandingModel.setBotchatTextColor(botActiveThemeModel.getBotMessage().getFontColor());
                brandingModel.setUserchatBgColor(botActiveThemeModel.getUserMessage().getBubbleColor());
                brandingModel.setUserchatTextColor(botActiveThemeModel.getUserMessage().getFontColor());

                brandingModel.setButtonActiveBgColor(botActiveThemeModel.getButtons().getDefaultButtonColor());
                brandingModel.setButtonActiveTextColor(botActiveThemeModel.getButtons().getDefaultFontColor());

                brandingModel.setButtonInactiveBgColor(botActiveThemeModel.getButtons().getOnHoverButtonColor());
                brandingModel.setButtonInactiveTextColor(botActiveThemeModel.getButtons().getOnHoverFontColor());
                brandingModel.setButtonBorderColor(botActiveThemeModel.getButtons().getBorderColor());

                brandingModel.setBotName(SDKConfiguration.Client.bot_name);
                brandingModel.setWidgetBodyColor(botActiveThemeModel.getWidgetBody().getBackgroundColor());
                brandingModel.setWidgetTextColor(botActiveThemeModel.getWidgetHeader().getFontColor());
                brandingModel.setWidgetHeaderColor(botActiveThemeModel.getWidgetHeader().getBackgroundColor());
                brandingModel.setWidgetFooterColor(botActiveThemeModel.getWidgetFooter().getBackgroundColor());
                brandingModel.setWidgetFooterBorderColor(botActiveThemeModel.getWidgetFooter().getBorderColor());
                brandingModel.setWidgetFooterHintColor(botActiveThemeModel.getWidgetFooter().getPlaceHolder());
                return brandingModel;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void displayMessage(String text, String type, String messageId) {
        if (!lastMsgId.equalsIgnoreCase(messageId)) {
            try {
                PayloadOuter payloadOuter = gson.fromJson(text, PayloadOuter.class);

                if (StringUtils.isNullOrEmpty(payloadOuter.getType())) payloadOuter.setType(type);

                ComponentModel componentModel = new ComponentModel();
                componentModel.setType(payloadOuter.getType());
                componentModel.setPayload(payloadOuter);

                BotResponseMessage botResponseMessage = new BotResponseMessage();
                botResponseMessage.setType(componentModel.getType());
                botResponseMessage.setComponent(componentModel);

                ArrayList<BotResponseMessage> arrBotResponseMessages = new ArrayList<>();
                arrBotResponseMessages.add(botResponseMessage);

                BotResponse botResponse = new BotResponse();
                botResponse.setType(componentModel.getType());
                botResponse.setMessage(arrBotResponseMessages);
                botResponse.setMessageId(messageId);

                if (botMetaModel != null && !StringUtils.isNullOrEmpty(botMetaModel.getIcon())) botResponse.setIcon(botMetaModel.getIcon());

                processPayload("", botResponse);
            } catch (Exception e) {
//                PayloadInner payloadInner = new PayloadInner();
//                payloadInner.setTemplate_type("text");

                PayloadOuter payloadOuter = new PayloadOuter();
                payloadOuter.setText(text);
                payloadOuter.setType("text");
//                payloadOuter.setPayload(payloadInner);

                ComponentModel componentModel = new ComponentModel();
                componentModel.setType("text");
                componentModel.setPayload(payloadOuter);

                BotResponseMessage botResponseMessage = new BotResponseMessage();
                botResponseMessage.setType("text");
                botResponseMessage.setComponent(componentModel);

                ArrayList<BotResponseMessage> arrBotResponseMessages = new ArrayList<>();
                arrBotResponseMessages.add(botResponseMessage);

                BotResponse botResponse = new BotResponse();
                botResponse.setType("text");
                botResponse.setMessage(arrBotResponseMessages);
                botResponse.setMessageId(messageId);
                botResponse.setIcon(SDKConfiguration.BubbleColors.getIcon_url());

                if (botMetaModel != null && !StringUtils.isNullOrEmpty(botMetaModel.getIcon())) botResponse.setIcon(botMetaModel.getIcon());

                processPayload("", botResponse);
            }
        }

    }

    public void displayMessage(PayloadOuter payloadOuter) {
        try {
            if (payloadOuter != null && payloadOuter.getPayload() != null) {
                ComponentModel componentModel = new ComponentModel();
                componentModel.setType(payloadOuter.getType());
                componentModel.setPayload(payloadOuter);

                BotResponseMessage botResponseMessage = new BotResponseMessage();
                botResponseMessage.setType(componentModel.getType());
                botResponseMessage.setComponent(componentModel);

                ArrayList<BotResponseMessage> arrBotResponseMessages = new ArrayList<>();
                arrBotResponseMessages.add(botResponseMessage);

                BotResponse botResponse = new BotResponse();
                botResponse.setType(componentModel.getType());
                botResponse.setMessage(arrBotResponseMessages);

                if (botMetaModel != null && !StringUtils.isNullOrEmpty(botMetaModel.getIcon())) botResponse.setIcon(botMetaModel.getIcon());

                processPayload("", botResponse);
            } else if (payloadOuter != null && !StringUtils.isNullOrEmpty(payloadOuter.getText())) {
                displayMessage(payloadOuter.getText(), BotResponse.COMPONENT_TYPE_TEXT, "");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        BotApplication.activityResumed();
        if (!SDKConfiguration.Client.isWebHook) {
            BotSocketConnectionManager.getInstance().checkConnectionAndRetry(getApplicationContext(), false);
            updateTitleBar(BotSocketConnectionManager.getInstance().getConnection_state());
        }

        //Added newly for send receipts
        if (botClient != null && arrMessageList.size() > 0 && isAgentTransfer) {
            botClient.sendReceipts(BundleConstants.MESSAGE_READ, arrMessageList.get((arrMessageList.size() - 1)));
            arrMessageList = new ArrayList<>();
        }

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
            LogUtils.e(BotChatActivity.class.getSimpleName(), "ComposeFooterFragment not found");
            return false;
        }
    }

    private void stopTextToSpeech() {
        try {
            ttsSynthesizer.stopTextToSpeech();
        } catch (IllegalArgumentException exception) {
            exception.printStackTrace();
        }
    }

    void textToSpeech(BotResponse botResponse) {
        if (isTTSEnabled() && botResponse.getMessage() != null && !botResponse.getMessage().isEmpty()) {
            String botResponseTextualFormat = "";
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
                    } else if (BotResponse.TEMPLATE_TYPE_LIST.equalsIgnoreCase(payInner.getTemplate_type())) {
                        botResponseTextualFormat = payInner.getText();
                    }
                }
            }
            if (BotSocketConnectionManager.getInstance().isTTSEnabled()) {
                BotSocketConnectionManager.getInstance().startSpeak(botResponseTextualFormat);
            }
        }
    }


    @SuppressLint("MissingPermission")
    protected boolean isOnline() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        Network nw = connectivityManager.getActiveNetwork();
        if (nw == null) return false;
        NetworkCapabilities actNw = connectivityManager.getNetworkCapabilities(nw);
        return actNw != null && (actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) || actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) || actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) || actNw.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH));
    }

    public void sendImage(String fP, String fN, String fPT) {
        new SaveCapturedImageTask(fP, fN, fPT).executeAsync();
    }

    protected class SaveCapturedImageTask extends AsyncTaskExecutor<String> {
        private final String filePath;
        private final String fileName;
        private final String filePathThumbnail;
        private String orientation;
        private String extn = null;

        public SaveCapturedImageTask(String filePath, String fileName, String filePathThumbnail) {
            this.filePath = filePath;
            this.fileName = fileName;
            this.filePathThumbnail = filePathThumbnail;
        }

        @Override
        protected void doInBackground(String... strings) {
            OutputStream fOut = null;
            if (filePath != null) {
                extn = filePath.substring(filePath.lastIndexOf(".") + 1);
                Bitmap thePic = BitmapUtils.decodeBitmapFromFile(filePath, 800, 600, false);
                if (thePic != null) {
                    try {
                        // compress the image
                        File _file = new File(filePath);

                        LogUtils.d(LOG_TAG, " file.exists() ---------------------------------------- " + _file.exists());
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            fOut = Files.newOutputStream(_file.toPath());
                        } else fOut = new FileOutputStream(_file);

                        thePic.compress(Bitmap.CompressFormat.JPEG, compressQualityInt, fOut);
                        thePic = rotateIfNecessary(filePath, thePic);
                        orientation = thePic.getWidth() > thePic.getHeight() ? BitmapUtils.ORIENTATION_LS : BitmapUtils.ORIENTATION_PT;
                        fOut.flush();
                        fOut.close();
                    } catch (Exception e) {
                        LogUtils.e(LOG_TAG, e.toString());
                    } finally {
                        try {
                            assert fOut != null;
                            fOut.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }

        @Override
        protected void onPostExecute() {
            if (extn != null) {
                if (!SDKConfiguration.Client.isWebHook) {
                    KoreWorker.getInstance().addTask(new UploadBulkFile(fileName, filePath, "bearer " + SocketWrapper.getInstance(BotChatActivity.this).getAccessToken(), SocketWrapper.getInstance(BotChatActivity.this).getBotUserId(), "workflows", extn, KoreMedia.BUFFER_SIZE_IMAGE, new Messenger(messagesMediaUploadAcknowledgeHandler), filePathThumbnail, "AT_" + System.currentTimeMillis(), BotChatActivity.this, BitmapUtils.obtainMediaTypeOfExtn(extn), (!SDKConfiguration.Client.isWebHook ? SDKConfiguration.Server.SERVER_URL : SDKConfiguration.Server.koreAPIUrl), orientation, true, SDKConfiguration.Client.isWebHook, SDKConfiguration.Client.bot_id));
                } else {
                    KoreWorker.getInstance().addTask(new UploadBulkFile(fileName, filePath, "bearer " + jwt, SocketWrapper.getInstance(BotChatActivity.this).getBotUserId(), "workflows", extn, KoreMedia.BUFFER_SIZE_IMAGE, new Messenger(messagesMediaUploadAcknowledgeHandler), filePathThumbnail, "AT_" + System.currentTimeMillis(), BotChatActivity.this, BitmapUtils.obtainMediaTypeOfExtn(extn), (!SDKConfiguration.Client.isWebHook ? SDKConfiguration.Server.SERVER_URL : SDKConfiguration.Server.koreAPIUrl), orientation, true, SDKConfiguration.Client.isWebHook, SDKConfiguration.Client.webHook_bot_id));
                }
            } else {
                showToast("Unable to attach!");
            }
        }

        @Override
        protected void onCancelled() {
            // update UI on task cancelled
            showToast("Unable to attach!");
        }
    }

    @SuppressLint("HandlerLeak")
    final Handler messagesMediaUploadAcknowledgeHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public synchronized void handleMessage(Message msg) {
            Bundle reply = msg.getData();
            LogUtils.e("shri", reply + "------------------------------");
            if (reply.getBoolean("success", true)) {
                String mediaFilePath = reply.getString("filePath");
                String MEDIA_TYPE = reply.getString("fileExtn");
                String mediaFileId = reply.getString("fileId");
                String mediaFileName = reply.getString("fileName");
                String componentType = reply.getString("componentType");
                String thumbnailURL = reply.getString("thumbnailURL");
                String fileSize = reply.getString("fileSize");
                KoreComponentModel koreMedia = new KoreComponentModel();
                koreMedia.setMediaType(BitmapUtils.getAttachmentType(componentType));
                HashMap<String, Object> cmpData = new HashMap<>(1);
                cmpData.put("fileName", mediaFileName);

                koreMedia.setComponentData(cmpData);
                koreMedia.setMediaFileName(getComponentId(componentType));
                koreMedia.setMediaFilePath(mediaFilePath);
                koreMedia.setFileSize(fileSize);

                koreMedia.setMediafileId(mediaFileId);
                koreMedia.setMediaThumbnail(thumbnailURL);

                messageHandler.postDelayed(new Runnable() {
                    public void run() {
                        HashMap<String, String> attachmentKey = new HashMap<>();
                        attachmentKey.put("fileName", mediaFileName + "." + MEDIA_TYPE);
                        attachmentKey.put("fileType", componentType);
                        attachmentKey.put("fileId", mediaFileId);
                        attachmentKey.put("localFilePath", mediaFilePath);
                        attachmentKey.put("fileExtn", MEDIA_TYPE);
                        attachmentKey.put("thumbnailURL", thumbnailURL);
                        composeFooterFragment.addAttachmentToAdapter(attachmentKey);
                    }
                }, 400);
            } else {
                String errorMsg = reply.getString(UploadBulkFile.error_msz_key);
                if (!TextUtils.isEmpty(errorMsg)) {
                    LogUtils.i("File upload error", errorMsg);
                    showToast(errorMsg);
                }
            }
        }
    };

    public void mediaAttachment(HashMap<String, String> attachmentKey) {
        messageHandler.postDelayed(new Runnable() {
            public void run() {
                composeFooterFragment.addAttachmentToAdapter(attachmentKey);
            }
        }, 400);
    }

    String getComponentId(String componentType) {
        if (componentType != null) {
            if (componentType.equalsIgnoreCase(KoreMedia.MEDIA_TYPE_IMAGE)) {
                return "image_" + System.currentTimeMillis();
            } else if (componentType.equalsIgnoreCase(KoreMedia.MEDIA_TYPE_VIDEO)) {
                return "video_" + System.currentTimeMillis();
            } else {
                return "doc_" + System.currentTimeMillis();
            }
        }
        return "";
    }

    void getBrandingDetails() {
        Call<ResponseBody> getBankingConfigService = BrandingRestBuilder.getRestAPI().getBrandingNewDetails(SDKConfiguration.Client.bot_id, "bearer " + SocketWrapper.getInstance(BotChatActivity.this).getAccessToken(), "published", "1", "en_US", SDKConfiguration.Client.bot_id);
        getBankingConfigService.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@androidx.annotation.NonNull Call<ResponseBody> call, @androidx.annotation.NonNull Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        resp = response.body().string();
                        Type avtiveThemeType = new TypeToken<BotActiveThemeModel>() {
                        }.getType();
                        brandingNewDos = gson.fromJson(resp, avtiveThemeType);
                        if (brandingNewDos != null) {
                            BrandingModel brandingModel = new BrandingModel();
                            brandingModel.setBotchatBgColor(brandingNewDos.getBotMessage().getBubbleColor());
                            brandingModel.setBotchatTextColor(brandingNewDos.getBotMessage().getFontColor());
                            brandingModel.setUserchatBgColor(brandingNewDos.getUserMessage().getBubbleColor());
                            brandingModel.setUserchatTextColor(brandingNewDos.getUserMessage().getFontColor());

                            brandingModel.setButtonActiveBgColor(brandingNewDos.getButtons().getDefaultButtonColor());
                            brandingModel.setButtonActiveTextColor(brandingNewDos.getButtons().getDefaultFontColor());

                            brandingModel.setButtonInactiveBgColor(brandingNewDos.getButtons().getOnHoverButtonColor());
                            brandingModel.setButtonInactiveTextColor(brandingNewDos.getButtons().getOnHoverFontColor());
                            brandingModel.setButtonBorderColor(brandingNewDos.getButtons().getBorderColor());

                            brandingModel.setBotName(SDKConfiguration.Client.bot_name);
                            brandingModel.setWidgetBodyColor(brandingNewDos.getWidgetBody().getBackgroundColor());
                            brandingModel.setWidgetTextColor(brandingNewDos.getWidgetHeader().getFontColor());
                            brandingModel.setWidgetHeaderColor(brandingNewDos.getWidgetHeader().getBackgroundColor());
                            brandingModel.setWidgetFooterColor(brandingNewDos.getWidgetFooter().getBackgroundColor());
                            brandingModel.setWidgetFooterBorderColor(brandingNewDos.getWidgetFooter().getBorderColor());
                            brandingModel.setWidgetFooterHintColor(brandingNewDos.getWidgetFooter().getPlaceHolder());
                            onEvent(brandingModel);
                        } else {
                            throw new Exception("Something went wrong!");
                        }
                    } catch (Exception e) {
                        try {
                            Type avtiveThemeType = new TypeToken<BrandingNewModel>() {
                            }.getType();
                            BrandingNewModel brandingNewModel = gson.fromJson(resp, avtiveThemeType);
                            if (brandingNewModel != null) {
                                BrandingV3Model brandingNewDos = brandingNewModel.getV3();
                                try {
                                    BrandingModel brandingModel = new BrandingModel();
                                    brandingModel.setBotchatBgColor(brandingNewDos.getBody().getBot_message().getBg_color());
                                    brandingModel.setBotchatTextColor(brandingNewDos.getBody().getBot_message().getColor());

                                    brandingModel.setUserchatBgColor(brandingNewDos.getBody().getUser_message().getBg_color());
                                    brandingModel.setUserchatTextColor(brandingNewDos.getBody().getUser_message().getColor());

                                    if (brandingNewDos.getGeneral() != null && brandingNewDos.getGeneral().getColors() != null && brandingNewDos.getGeneral().getColors().isUseColorPaletteOnly()) {
                                        brandingModel.setButtonActiveBgColor(brandingNewDos.getGeneral().getColors().getPrimary());
                                        brandingModel.setButtonActiveTextColor(brandingNewDos.getGeneral().getColors().getPrimary_text());
                                        brandingModel.setButtonInactiveBgColor(brandingNewDos.getGeneral().getColors().getSecondary());
                                        brandingModel.setButtonInactiveTextColor(brandingNewDos.getGeneral().getColors().getSecondary_text());
                                        SDKConfiguration.BubbleColors.quickReplyColor = brandingNewDos.getGeneral().getColors().getPrimary();
                                        SDKConfiguration.BubbleColors.quickReplyTextColor = brandingNewDos.getGeneral().getColors().getPrimary_text();
                                        brandingModel.setBotchatBgColor(brandingNewDos.getGeneral().getColors().getSecondary());
                                        brandingModel.setBotchatTextColor(brandingNewDos.getGeneral().getColors().getPrimary_text());
                                        brandingModel.setUserchatBgColor(brandingNewDos.getGeneral().getColors().getPrimary());
                                        brandingModel.setUserchatTextColor(brandingNewDos.getGeneral().getColors().getSecondary_text());
                                    }

                                    brandingModel.setBotName(SDKConfiguration.Client.bot_name);
                                    brandingModel.setWidgetBodyColor(brandingNewDos.getBody().getBackground().getColor());
                                    brandingModel.setWidgetTextColor((brandingNewDos.getHeader().getTitle().getColor()));
                                    brandingModel.setWidgetHeaderColor(brandingNewDos.getHeader().getBg_color());
                                    brandingModel.setWidgetFooterColor(brandingNewDos.getFooter().getBg_color());
                                    brandingModel.setWidgetFooterBorderColor(brandingNewDos.getFooter().getCompose_bar().getOutline_color());
                                    brandingModel.setWidgetFooterHintColor(brandingNewDos.getFooter().getCompose_bar().getOutline_color());
                                    brandingModel.setWidgetFooterHintText(brandingNewDos.getFooter().getCompose_bar().getPlaceholder());
                                    brandingModel.setChatBubbleStyle(brandingNewDos.getChat_bubble().getStyle());
                                    onEvent(brandingModel);

                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }
                            } else {
                                onEvent(getBrandingDataFromTxt());
                            }

                        } catch (Exception ex) {
                            onEvent(getBrandingDataFromTxt());
                            throw new RuntimeException(ex);
                        }
                    }
                } else {
                    onEvent(getBrandingDataFromTxt());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                onEvent(getBrandingDataFromTxt());
            }
        });
    }

    void sendWebHookMessage(boolean new_session, String msg, ArrayList<HashMap<String, String>> attachments) {
        Call<WebHookResponseDataModel> getBankingConfigService = WebHookRestBuilder.getRestAPI().sendWebHookMessage(SDKConfiguration.Client.webHook_bot_id, "bearer " + jwt, getJsonRequest(new_session, msg, attachments));
        getBankingConfigService.enqueue(new Callback<WebHookResponseDataModel>() {
            @Override
            public void onResponse(@NonNull Call<WebHookResponseDataModel> call, @NonNull Response<WebHookResponseDataModel> response) {
                if (response.isSuccessful()) {
                    webHookResponseDataModel = response.body();
                    taskProgressBar.setVisibility(View.GONE);
                    composeFooterFragment.enableSendButton();

                    if (webHookResponseDataModel != null && webHookResponseDataModel.getData() != null && webHookResponseDataModel.getData().size() > 0) {
                        for (int i = 0; i < webHookResponseDataModel.getData().size(); i++) {
                            if (webHookResponseDataModel.getData().get(i).getVal() instanceof String)
                                displayMessage(webHookResponseDataModel.getData().get(i).getVal().toString(), webHookResponseDataModel.getData().get(i).getType(), webHookResponseDataModel.getData().get(i).getMessageId());
                            else if (webHookResponseDataModel.getData().get(i).getVal() != null) {
                                try {
                                    String elementsAsString = gson.toJson(webHookResponseDataModel.getData().get(i).getVal());
                                    Type carouselType = new TypeToken<PayloadOuter>() {
                                    }.getType();
                                    PayloadOuter payloadOuter = gson.fromJson(elementsAsString, carouselType);
                                    displayMessage(payloadOuter);
                                } catch (Exception e) {
                                    try {
                                        String elementsAsString = gson.toJson(webHookResponseDataModel.getData().get(i).getVal());
                                        Type carouselType = new TypeToken<PayloadHeaderModel>() {
                                        }.getType();
                                        PayloadHeaderModel payloadOuter = gson.fromJson(elementsAsString, carouselType);
                                        if (payloadOuter != null && payloadOuter.getPayload() != null) {
                                            displayMessage(payloadOuter.getPayload().getTemplate_type(), BotResponse.COMPONENT_TYPE_TEXT, webHookResponseDataModel.getData().get(i).getMessageId());
                                        }
                                    } catch (Exception ex) {
                                        String elementsAsString = gson.toJson(webHookResponseDataModel.getData().get(i).getVal());
                                        displayMessage(elementsAsString, BotResponse.COMPONENT_TYPE_TEXT, webHookResponseDataModel.getData().get(i).getMessageId());
                                    }

                                }
                            }
                        }

                        if (!StringUtils.isNullOrEmpty(webHookResponseDataModel.getPollId()))
                            startSendingPo11(webHookResponseDataModel.getPollId());
                    }
                } else {
                    taskProgressBar.setVisibility(View.GONE);
                    composeFooterFragment.enableSendButton();
                }
            }

            @Override
            public void onFailure(@NonNull Call<WebHookResponseDataModel> call, @NonNull Throwable t) {
            }
        });
    }

    private void getWebHookMeta() {
        Call<BotMetaModel> getBankingConfigService = WebHookRestBuilder.getRestAPI().getWebHookBotMeta("bearer " + jwt, SDKConfiguration.Client.webHook_bot_id);
        getBankingConfigService.enqueue(new Callback<BotMetaModel>() {
            @Override
            public void onResponse(@NonNull Call<BotMetaModel> call, @NonNull Response<BotMetaModel> response) {
                if (response.isSuccessful()) {
                    botMetaModel = response.body();
                    if (botMetaModel != null) SDKConfiguration.BubbleColors.setIcon_url(botMetaModel.getIcon());
                    sendWebHookMessage(true, "ON_CONNECT", null);
                }
            }

            @Override
            public void onFailure(@NonNull Call<BotMetaModel> call, @NonNull Throwable t) {
            }
        });
    }

    void postPollingData(String pollId) {
        Call<WebHookResponseDataModel> getBankingConfigService = WebHookRestBuilder.getRestAPI().getPollIdData("bearer " + jwt, SDKConfiguration.Client.webHook_bot_id, pollId);
        getBankingConfigService.enqueue(new Callback<WebHookResponseDataModel>() {
            @Override
            public void onResponse(@NonNull Call<WebHookResponseDataModel> call, @NonNull Response<WebHookResponseDataModel> response) {
                if (response.isSuccessful()) {
                    webHookResponseDataModel = response.body();
                    taskProgressBar.setVisibility(View.GONE);
                    composeFooterFragment.enableSendButton();

                    if (webHookResponseDataModel != null && webHookResponseDataModel.getData() != null && webHookResponseDataModel.getData().size() > 0) {
                        for (int i = 0; i < webHookResponseDataModel.getData().size(); i++) {
                            if (webHookResponseDataModel.getData().get(i).getVal() instanceof String)
                                displayMessage(webHookResponseDataModel.getData().get(i).getVal().toString(), webHookResponseDataModel.getData().get(i).getType(), webHookResponseDataModel.getData().get(i).getMessageId());
                        }

                        stopSendingPolling();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<WebHookResponseDataModel> call, @NonNull Throwable t) {
            }
        });
    }

    private void addSentMessageToChat(String message) {
        //Update the bot content list with the send message
        RestResponse.BotMessage botMessage = new RestResponse.BotMessage(message);
        RestResponse.BotPayLoad botPayLoad = new RestResponse.BotPayLoad();
        botPayLoad.setMessage(botMessage);
        BotInfoModel botInfo = new BotInfoModel(SDKConfiguration.Client.bot_name, SDKConfiguration.Client.bot_id, null);
        botPayLoad.setBotInfo(botInfo);
        Gson gson = new Gson();
        String jsonPayload = gson.toJson(botPayLoad);

        BotRequest botRequest = gson.fromJson(jsonPayload, BotRequest.class);
        botRequest.setCreatedOn(DateUtils.isoFormatter.format(new Date()));
        sListener.onMessage(new SocketDataTransferModel(BaseSocketConnectionManager.EVENT_TYPE.TYPE_MESSAGE_UPDATE, message, botRequest, false));
    }

    private HashMap<String, Object> getJsonRequest(boolean new_session, String msg, ArrayList<HashMap<String, String>> attachments) {
        HashMap<String, Object> hsh = new HashMap<>();

        try {
            WebHookRequestModel webHookRequestModel = new WebHookRequestModel();
            WebHookRequestModel.Session session = new WebHookRequestModel.Session();
            session.setNewSession(new_session);
            webHookRequestModel.setSession(session);
            hsh.put("session", session);

            WebHookRequestModel.Message message = new WebHookRequestModel.Message();
            message.setVal(msg);

            if (new_session) message.setType("event");
            else message.setType("text");

            webHookRequestModel.setMessage(message);
            hsh.put("message", message);

            WebHookRequestModel.From from = new WebHookRequestModel.From();
            from.setId(SDKConfiguration.Client.webHook_identity);
            WebHookRequestModel.From.WebHookUserInfo userInfo = new WebHookRequestModel.From.WebHookUserInfo();
            userInfo.setFirstName("");
            userInfo.setLastName("");
            userInfo.setEmail("");
            from.setUserInfo(userInfo);
            webHookRequestModel.setFrom(from);
            hsh.put("from", from);

            WebHookRequestModel.To to = new WebHookRequestModel.To();
            to.setId("Kore.ai");
            WebHookRequestModel.To.GroupInfo groupInfo = new WebHookRequestModel.To.GroupInfo();
            groupInfo.setId("");
            groupInfo.setName("");
            to.setGroupInfo(groupInfo);
            webHookRequestModel.setTo(to);
            hsh.put("to", to);

            WebHookRequestModel.Token token = new WebHookRequestModel.Token();
            hsh.put("token", token);

            if (attachments != null && attachments.size() > 0) hsh.put("attachments", attachments);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return hsh;
    }

    void startSendingPo11(String pollId) {
        handler.postDelayed(runnable = new Runnable() {
            public void run() {
                handler.postDelayed(runnable, poll_delay);
                postPollingData(pollId);
            }
        }, poll_delay);
    }

    public String getUniqueDeviceId(Context context) {
        if (uniqueID == null) {
            SharedPreferences sharedPrefs = context.getSharedPreferences(PREF_UNIQUE_ID, Context.MODE_PRIVATE);
            uniqueID = sharedPrefs.getString(PREF_UNIQUE_ID, null);
            if (uniqueID == null) {
                uniqueID = UUID.randomUUID().toString();
                SharedPreferences.Editor editor = sharedPrefs.edit();
                editor.putString(PREF_UNIQUE_ID, uniqueID);
                editor.apply();
            }
        }
        return uniqueID;
    }

    void stopSendingPolling() {
        handler.removeCallbacks(runnable);
    }

    void showReconnectionStopped() {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == DialogInterface.BUTTON_POSITIVE) {
                    BotSocketConnectionManager.killInstance();
                    finish();
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(BotChatActivity.this);
        builder.setMessage(R.string.bot_not_connected).setCancelable(false).setPositiveButton(R.string.ok, dialogClickListener).show();
    }

}
