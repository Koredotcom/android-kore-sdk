package kore.botssdk.activity;

import static kore.botssdk.utils.ToastUtils.showToast;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
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

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import kore.botssdk.R;
import kore.botssdk.application.BotApplication;
import kore.botssdk.bot.BotClient;
import kore.botssdk.event.KoreEventCenter;
import kore.botssdk.fileupload.core.KoreWorker;
import kore.botssdk.fileupload.core.UploadBulkFile;
import kore.botssdk.fragment.ComposeFooterFragment;
import kore.botssdk.fragment.NewBotContentFragment;
import kore.botssdk.listener.BaseSocketConnectionManager;
import kore.botssdk.listener.BotChatViewListener;
import kore.botssdk.listener.BotContentFragmentUpdate;
import kore.botssdk.listener.BotSocketConnectionManager;
import kore.botssdk.listener.ComposeFooterInterface;
import kore.botssdk.listener.ComposeFooterUpdate;
import kore.botssdk.listener.InvokeGenericWebViewInterface;
import kore.botssdk.models.BotRequest;
import kore.botssdk.models.BotResponse;
import kore.botssdk.models.BrandingModel;
import kore.botssdk.models.FormActionTemplate;
import kore.botssdk.models.KoreComponentModel;
import kore.botssdk.models.KoreMedia;
import kore.botssdk.net.BrandingRestBuilder;
import kore.botssdk.net.RestBuilder;
import kore.botssdk.net.SDKConfiguration;
import kore.botssdk.net.WebHookRestBuilder;
import kore.botssdk.pushnotification.PushNotificationRegister;
import kore.botssdk.repository.branding.BrandingRepository;
import kore.botssdk.repository.webhook.WebHookRepository;
import kore.botssdk.utils.BitmapUtils;
import kore.botssdk.utils.BundleConstants;
import kore.botssdk.utils.BundleUtils;
import kore.botssdk.utils.ClosingService;
import kore.botssdk.utils.LogUtils;
import kore.botssdk.utils.NetworkUtility;
import kore.botssdk.utils.StringUtils;
import kore.botssdk.viewmodels.chat.BotChatViewModel;
import kore.botssdk.viewmodels.chat.BotChatViewModelFactory;
import kore.botssdk.websocket.SocketWrapper;

@SuppressWarnings("UnKnownNullness")
public class NewBotChatActivity extends AppCompatActivity implements BotChatViewListener, ComposeFooterInterface, InvokeGenericWebViewInterface {
    FrameLayout chatLayoutFooterContainer;
    FrameLayout chatLayoutContentContainer;
    FrameLayout chatLayoutPanelContainer;
    ProgressBar taskProgressBar;
    FragmentTransaction fragmentTransaction;
    String chatBot, taskBotId, jwt;
    BotClient botClient;
    NewBotContentFragment botContentFragment;
    ComposeFooterFragment composeFooterFragment;
    BotContentFragmentUpdate botContentFragmentUpdate;
    ComposeFooterUpdate composeFooterUpdate;
    SharedPreferences sharedPreferences;
    BotChatViewModel mViewModel;
    boolean isAgentTransfer;
    boolean isReconnection;
    final Handler messageHandler = new Handler();

    BroadcastReceiver onDestroyReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (isAgentTransfer && botClient != null) {
                botClient.sendAgentCloseMessage("", SDKConfiguration.Client.bot_name, SDKConfiguration.Client.bot_id);
            }

            LogUtils.e("onDestroyReceiver", "onDestroyReceiver called");
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(BundleConstants.IS_RECONNECT, false);
            editor.putInt(BotResponse.HISTORY_COUNT, 0);
            editor.apply();
        }
    };

    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bot_chat_layout);
        botClient = new BotClient(NewBotChatActivity.this);

        BotChatViewModelFactory factory = new BotChatViewModelFactory(NewBotChatActivity.this, botClient, NewBotChatActivity.this, new BrandingRepository(NewBotChatActivity.this, NewBotChatActivity.this), new WebHookRepository(NewBotChatActivity.this, NewBotChatActivity.this));
        mViewModel = new ViewModelProvider(this, factory).get(BotChatViewModel.class);

        findViews();
        getBundleInfo();

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (NetworkUtility.isNetworkConnectionAvailable(NewBotChatActivity.this)) {
                    showCloseAlert();
                }
            }
        });

        mViewModel.connectToBot(sharedPreferences.getBoolean(BundleConstants.IS_RECONNECT, false));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            registerReceiver(onDestroyReceiver, new IntentFilter(BundleConstants.DESTROY_EVENT), RECEIVER_NOT_EXPORTED);
        else registerReceiver(onDestroyReceiver, new IntentFilter(BundleConstants.DESTROY_EVENT));

        startService(new Intent(getApplicationContext(), ClosingService.class));
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
        RestBuilder.setContext(NewBotChatActivity.this);
        WebHookRestBuilder.setContext(NewBotChatActivity.this);
        BrandingRestBuilder.setContext(NewBotChatActivity.this);

        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        //Add Bot Content Fragment
        botContentFragment = new NewBotContentFragment();
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
        composeFooterFragment.setBottomOptionData(mViewModel.getDataFromTxt());
        composeFooterFragment.setBotClient(botClient);
        fragmentTransaction.add(R.id.chatLayoutFooterContainer, composeFooterFragment).commit();
        setComposeFooterUpdate(composeFooterFragment);

        KoreEventCenter.register(this);
    }

    @Override
    public void addMessageToAdapter(BotResponse baseBotMessage) {
        botContentFragment.addMessageToBotChatAdapter(baseBotMessage);
        botContentFragment.setQuickRepliesIntoFooter(baseBotMessage);
        botContentFragment.showCalendarIntoFooter(baseBotMessage);
    }

    @Override
    public void onConnectionStateChanged(BaseSocketConnectionManager.CONNECTION_STATE state, boolean isReconnection) {
        if (state == BaseSocketConnectionManager.CONNECTION_STATE.CONNECTED) {
            this.isReconnection = isReconnection;
            taskProgressBar.setVisibility(View.GONE);
            composeFooterFragment.enableSendButton();
        }
    }

    public void setBotContentFragmentUpdate(BotContentFragmentUpdate botContentFragmentUpdate) {
        this.botContentFragmentUpdate = botContentFragmentUpdate;
    }

    public void setComposeFooterUpdate(ComposeFooterUpdate composeFooterUpdate) {
        this.composeFooterUpdate = composeFooterUpdate;
    }

    @Override
    public void onBrandingDetails(BrandingModel brandingModel) {

        if (brandingModel != null) {
            if (botContentFragment != null)
                botContentFragment.changeThemeBackGround(brandingModel.getWidgetBodyColor(), brandingModel.getWidgetHeaderColor(), brandingModel.getWidgetTextColor(), brandingModel.getBotName());

            if (composeFooterFragment != null)
                composeFooterFragment.changeThemeBackGround(brandingModel.getWidgetFooterColor(), brandingModel.getWidgetFooterHintColor());
        }

        if (botContentFragment != null && isReconnection) {
            if (sharedPreferences.getInt(BotResponse.HISTORY_COUNT, 0) > SDKConfiguration.OverrideKoreConfig.history_batch_size)
                botContentFragment.loadChatHistory(0, SDKConfiguration.OverrideKoreConfig.history_batch_size);
            else if (sharedPreferences.getInt(BotResponse.HISTORY_COUNT, 0) > 0)
                botContentFragment.loadChatHistory(0, sharedPreferences.getInt(BotResponse.HISTORY_COUNT, 1));
            else botContentFragment.loadReconnectionChatHistory(0, SDKConfiguration.OverrideKoreConfig.history_batch_size);
        } else if (SDKConfiguration.OverrideKoreConfig.history_initial_call && SDKConfiguration.OverrideKoreConfig.history_enable && botContentFragment != null) {
            botContentFragment.loadChatHistory(0, SDKConfiguration.OverrideKoreConfig.history_batch_size);
        }
    }

    @Override
    public void updateContentListOnSend(BotRequest botRequest) {
        botContentFragment.updateContentListOnSend(botRequest);
    }

    @Override
    public void showTypingStatus() {
        botContentFragment.showTypingStatus();
    }

    @Override
    public void setIsAgentConnected(boolean isAgentConnected) {
        isAgentTransfer = isAgentConnected;
        composeFooterFragment.setIsAgentConnected(isAgentConnected);
    }

    @Override
    public void enableSendButton() {
        composeFooterFragment.enableSendButton();
    }

    @Override
    public void processPayload(String payload, BotResponse botResponse) {
        mViewModel.processPayload(payload, botResponse);
    }

    @Override
    public void displayMessage(String text, String type, String messageId) {
        mViewModel.displayMessage(text, type, messageId);
    }

    @Override
    public void updateTitleBar(BaseSocketConnectionManager.CONNECTION_STATE state) {
        switch (state) {
            case CONNECTING:
                taskProgressBar.setVisibility(View.VISIBLE);
                break;
            case CONNECTED: {
                taskProgressBar.setVisibility(View.GONE);
                composeFooterFragment.enableSendButton();
            }
            break;
            case DISCONNECTED:
            case CONNECTED_BUT_DISCONNECTED: {
                taskProgressBar.setVisibility(View.VISIBLE);
                composeFooterFragment.setDisabled(true);
                composeFooterFragment.updateUI();
            }
            break;
            default:
                taskProgressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void showReconnectionStopped() {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == DialogInterface.BUTTON_POSITIVE) {
                    BotSocketConnectionManager.killInstance();
                    finish();
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(NewBotChatActivity.this);
        builder.setMessage(R.string.bot_not_connected).setCancelable(false).setPositiveButton(R.string.ok, dialogClickListener).show();
    }

    @Override
    public void getBrandingDetails() {
        mViewModel.getBrandingDetails(SDKConfiguration.Client.bot_id, jwt, "published", "1", "en_US");
    }

    @Override
    public void addAttachmentToAdapter(HashMap<String, String> attachmentKey) {
        composeFooterFragment.addAttachmentToAdapter(attachmentKey);
    }

    @Override
    public void uploadBulkFile(String fileName, String filePath, String extn, String filePathThumbnail, String orientation) {
        if (!SDKConfiguration.Client.isWebHook) {
            KoreWorker.getInstance().addTask(new UploadBulkFile(fileName, filePath, "bearer " + SocketWrapper.getInstance(NewBotChatActivity.this).getAccessToken(), SocketWrapper.getInstance(NewBotChatActivity.this).getBotUserId(), "workflows", extn, KoreMedia.BUFFER_SIZE_IMAGE, new Messenger(messagesMediaUploadAcknowledgeHandler), filePathThumbnail, "AT_" + System.currentTimeMillis(), NewBotChatActivity.this, BitmapUtils.obtainMediaTypeOfExtn(extn), SDKConfiguration.Server.SERVER_URL, orientation, true, SDKConfiguration.Client.isWebHook, SDKConfiguration.Client.bot_id));
        } else {
            KoreWorker.getInstance().addTask(new UploadBulkFile(fileName, filePath, "bearer " + jwt, SocketWrapper.getInstance(NewBotChatActivity.this).getBotUserId(), "workflows", extn, KoreMedia.BUFFER_SIZE_IMAGE, new Messenger(messagesMediaUploadAcknowledgeHandler), filePathThumbnail, "AT_" + System.currentTimeMillis(), NewBotChatActivity.this, BitmapUtils.obtainMediaTypeOfExtn(extn), SDKConfiguration.Server.SERVER_URL, orientation, true, SDKConfiguration.Client.isWebHook, SDKConfiguration.Client.bot_id));
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
                        addAttachmentToAdapter(attachmentKey);
                    }
                }, 400);
            } else {
                String errorMsg = reply.getString(UploadBulkFile.error_msz_key);
                if (!TextUtils.isEmpty(errorMsg)) {
                    LogUtils.i("File upload error", errorMsg);
                    showToast(NewBotChatActivity.this, errorMsg);
                }
            }
        }
    };

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

    @Override
    public void onSendClick(String message, boolean isFromUtterance) {
        if (!StringUtils.isNullOrEmpty(message)) {
            if (!SDKConfiguration.Client.isWebHook) BotSocketConnectionManager.getInstance().sendMessage(message, null);
            else {
                mViewModel.addSentMessageToChat(message);
                mViewModel.sendWebHookMessage(jwt, false, message, null);
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
                mViewModel.addSentMessageToChat(message);
                mViewModel.sendWebHookMessage(jwt, false, payload, null);
            } else {
                mViewModel.addSentMessageToChat(message);
                mViewModel.sendWebHookMessage(jwt, false, message, null);
            }
        }
    }

    @Override
    public void onSendClick(String message, ArrayList<HashMap<String, String>> attachments, boolean isFromUtterance) {
        if (attachments != null && attachments.size() > 0) {
            if (!SDKConfiguration.Client.isWebHook) BotSocketConnectionManager.getInstance().sendAttachmentMessage(message, attachments);
            else {
                mViewModel.addSentMessageToChat(message);
                mViewModel.sendWebHookMessage(jwt, false, message, attachments);
            }
        }
    }

    public void onEvent(String jwt) {
        this.jwt = jwt;
        if (SDKConfiguration.Client.isWebHook) {
            if (botContentFragment != null) botContentFragment.setJwtTokenForWebHook(jwt);

            if (composeFooterFragment != null) composeFooterFragment.setJwtToken(jwt);

            mViewModel.getWebHookMeta(jwt);
        }
    }

    @Override
    public void onFormActionButtonClicked(FormActionTemplate fTemplate) {

    }

    @Override
    public void copyMessageToComposer(String text, boolean isForOnboard) {
        composeFooterFragment.setComposeText(text);
    }

    @Override
    public void onPanelClicked(Object pModel, boolean isFirstLaunch) {

    }

    @Override
    public void sendImage(String fP, String fN, String fPT) {
        mViewModel.sendImage(fP, fN, fPT);
    }

    @Override
    public void externalReadWritePermission(String fileUrl) {

    }

    @Override
    public void onDeepLinkClicked(String url) {

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
    public void handleUserActions(String payload, HashMap<String, Object> type) {

    }

    @Override
    protected void onDestroy() {
        if (isAgentTransfer && botClient != null)
            botClient.sendAgentCloseMessage("", SDKConfiguration.Client.bot_name, SDKConfiguration.Client.bot_id);

        new PushNotificationRegister().unsubscribePushNotification(botClient.getUserId(), botClient.getAccessToken(), sharedPreferences.getString("PREF_UNIQUE_ID", mViewModel.getUniqueID()));

        if (botClient != null) botClient.disconnect();
        KoreEventCenter.unregister(this);
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        BotApplication.activityResumed();

        if (!SDKConfiguration.Client.isWebHook) {
            BotSocketConnectionManager.getInstance().checkConnectionAndRetry(getApplicationContext(), false);
            updateTitleBar(BotSocketConnectionManager.getInstance().getConnection_state());
        }

        mViewModel.sendReadReceipts();
        super.onResume();
    }

    @Override
    protected void onPause() {
        BotApplication.activityPaused();
        super.onPause();
    }

    void showCloseAlert() {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE: {
                        if (sharedPreferences != null) {
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putBoolean(BundleConstants.IS_RECONNECT, true);
                            editor.putInt(BotResponse.HISTORY_COUNT, botContentFragment.getAdapterCount());
                            editor.apply();
                            BotSocketConnectionManager.killInstance();
                            finish();
                        }
                    }
                    break;
                    case DialogInterface.BUTTON_NEGATIVE: {
                        if (sharedPreferences != null) {
                            if (botClient != null && isAgentTransfer)
                                botClient.sendAgentCloseMessage("", SDKConfiguration.Client.bot_name, SDKConfiguration.Client.bot_id);

                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putBoolean(BundleConstants.IS_RECONNECT, false);
                            editor.putInt(BotResponse.HISTORY_COUNT, 0);
                            editor.apply();
                            BotSocketConnectionManager.killInstance();
                            finish();
                        }
                    }
                    break;
                    case DialogInterface.BUTTON_NEUTRAL:
                        dialog.dismiss();
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(NewBotChatActivity.this);
        builder.setMessage(R.string.close_or_minimize).setCancelable(false).setPositiveButton(R.string.minimize, dialogClickListener).setNegativeButton(R.string.close, dialogClickListener).setNeutralButton(R.string.cancel, dialogClickListener).show();
    }


    @Override
    protected void onStop() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
            List<ActivityManager.RunningTaskInfo> taskList = activityManager.getRunningTasks(10);

            if (!taskList.isEmpty()) {
                ActivityManager.RunningTaskInfo runningTaskInfo = taskList.get(0);
                if (runningTaskInfo.topActivity != null && (!runningTaskInfo.topActivity.getClassName().contains(getApplicationContext().getPackageName()) && !runningTaskInfo.topActivity.getClassName().contains("kore.botssdk"))) {
                    if (botClient != null) {
                        botClient.sendAgentCloseMessage("", SDKConfiguration.Client.bot_name, SDKConfiguration.Client.bot_id);

                        SharedPreferences.Editor prefsEditor = getSharedPreferences(BotResponse.THEME_NAME, Context.MODE_PRIVATE).edit();
                        String jsonObject = new Gson().toJson("");
                        prefsEditor.putString(BotResponse.AGENT_INFO_KEY, jsonObject);
                        prefsEditor.putBoolean(BundleConstants.IS_RECONNECT, false);
                        prefsEditor.putInt(BotResponse.HISTORY_COUNT, 0);
                        prefsEditor.apply();
                    }
                }
            }
        }
        super.onStop();
    }
}
