package kore.botssdk.activity;

import static android.view.View.VISIBLE;
import static kore.botssdk.activity.GenericWebViewActivity.EXTRA_HEADER;
import static kore.botssdk.activity.GenericWebViewActivity.EXTRA_URL;
import static kore.botssdk.utils.BundleConstants.CLOSE_CHAT_BOT_EVENT;
import static kore.botssdk.utils.BundleConstants.MINIMIZE_CHAT_BOT_EVENT;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import kore.botssdk.R;
import kore.botssdk.bot.BotClient;
import kore.botssdk.dialogs.TemplateBottomSheetFragment;
import kore.botssdk.event.KoreEventCenter;
import kore.botssdk.fragment.content.BaseContentFragment;
import kore.botssdk.fragment.content.NewBotContentFragment;
import kore.botssdk.fragment.footer.BaseFooterFragment;
import kore.botssdk.fragment.footer.ComposeFooterFragment;
import kore.botssdk.fragment.header.BaseHeaderFragment;
import kore.botssdk.fragment.header.BotHeaderFragment;
import kore.botssdk.listener.BaseSocketConnectionManager;
import kore.botssdk.listener.BotChatViewListener;
import kore.botssdk.listener.BotSocketConnectionManager;
import kore.botssdk.listener.ComposeFooterInterface;
import kore.botssdk.listener.InvokeGenericWebViewInterface;
import kore.botssdk.models.BotRequest;
import kore.botssdk.models.BotResponse;
import kore.botssdk.models.BrandingModel;
import kore.botssdk.net.BrandingRestBuilder;
import kore.botssdk.net.RestBuilder;
import kore.botssdk.net.SDKConfig;
import kore.botssdk.net.SDKConfiguration;
import kore.botssdk.net.WebHookRestBuilder;
import kore.botssdk.pushnotification.PushNotificationRegister;
import kore.botssdk.utils.BundleConstants;
import kore.botssdk.utils.BundleUtils;
import kore.botssdk.utils.ClosingService;
import kore.botssdk.utils.LogUtils;
import kore.botssdk.utils.NetworkUtility;
import kore.botssdk.utils.StringUtils;
import kore.botssdk.viewmodels.chat.BotChatViewModel;
import kore.botssdk.viewmodels.chat.BotChatViewModelFactory;

@SuppressWarnings("UnKnownNullness")
public class NewBotChatActivity extends BotAppCompactActivity implements BotChatViewListener, ComposeFooterInterface, InvokeGenericWebViewInterface {
    private ProgressBar taskProgressBar;
    private String jwt;
    BotClient botClient;
    private BaseHeaderFragment botHeaderFragment;
    BaseContentFragment botContentFragment;
    private BaseFooterFragment baseFooterFragment;
    SharedPreferences sharedPreferences;
    private BotChatViewModel mViewModel;
    boolean isAgentTransfer;
    private String botName = SDKConfiguration.Client.bot_name;
    private final BroadcastReceiver minimizeBotChatReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (Objects.equals(intent.getAction(), MINIMIZE_CHAT_BOT_EVENT)) {
                if (sharedPreferences != null) {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean(BundleConstants.IS_RECONNECT, true);
                    editor.putInt(BotResponse.HISTORY_COUNT, botContentFragment.getAdapterCount());
                    editor.apply();
                    BotSocketConnectionManager.killInstance();
                    String name = intent.getStringExtra("ActivityToLaunch");
                    int launchMode = intent.getIntExtra("LaunchMode", -1);
                    if (name != null) {
                        try {
                            Class className = Class.forName(name);
                            Intent activityIntent = new Intent(context, className);
                            if (launchMode != -1)
                                activityIntent.setFlags(launchMode);
                            context.startActivity(activityIntent);
                        } catch (ClassNotFoundException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    finish();
                }
            }
        }
    };
    private final BroadcastReceiver closeBotChatReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (Objects.equals(intent.getAction(), CLOSE_CHAT_BOT_EVENT)) {
                if (botClient != null) BotSocketConnectionManager.killInstance();
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean(BundleConstants.IS_RECONNECT, false);
                editor.putInt(BotResponse.HISTORY_COUNT, 0);
                editor.apply();
                finish();
            }
        }
    };

    private final BroadcastReceiver onDestroyReceiver = new BroadcastReceiver() {
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

        if (SDKConfiguration.OverrideKoreConfig.disable_action_bar && getSupportActionBar() != null)
            getSupportActionBar().hide();

        setContentLayout(R.layout.bot_chat_layout);

        botClient = new BotClient(NewBotChatActivity.this);

        BotChatViewModelFactory factory = new BotChatViewModelFactory(NewBotChatActivity.this, botClient, NewBotChatActivity.this);
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

        LocalBroadcastManager.getInstance(this).registerReceiver(onDestroyReceiver, new IntentFilter(BundleConstants.DESTROY_EVENT));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            registerReceiver(closeBotChatReceiver, new IntentFilter(CLOSE_CHAT_BOT_EVENT), RECEIVER_EXPORTED);
            registerReceiver(minimizeBotChatReceiver, new IntentFilter(MINIMIZE_CHAT_BOT_EVENT), RECEIVER_EXPORTED);
        } else {
            registerReceiver(closeBotChatReceiver, new IntentFilter(CLOSE_CHAT_BOT_EVENT));
            registerReceiver(minimizeBotChatReceiver, new IntentFilter(MINIMIZE_CHAT_BOT_EVENT));
        }

        startService(new Intent(getApplicationContext(), ClosingService.class));
    }

    private void getBundleInfo() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            jwt = bundle.getString(BundleUtils.JWT_TKN, "");
        }
    }

    private void findViews() {
        taskProgressBar = findViewById(R.id.taskProgressBar);
        sharedPreferences = getSharedPreferences(BotResponse.THEME_NAME, Context.MODE_PRIVATE);
        RestBuilder.setContext(NewBotChatActivity.this);
        WebHookRestBuilder.setContext(NewBotChatActivity.this);
        BrandingRestBuilder.setContext(NewBotChatActivity.this);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        //Adding Bot Content Fragment
        BaseContentFragment customContentFragment = SDKConfig.getCustomContentFragment();
        botContentFragment = customContentFragment != null ? customContentFragment : new NewBotContentFragment();
        botContentFragment.setArguments(getIntent().getExtras());
        botContentFragment.setComposeFooterInterface(this);
        botContentFragment.setInvokeGenericWebViewInterface(this);
        fragmentTransaction.add(R.id.chatLayoutContentContainer, botContentFragment).commit();

        //Adding Bot Compose Footer Fragment
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        BaseFooterFragment customFooterFragment = SDKConfig.getCustomFooterFragment();
        baseFooterFragment = customFooterFragment != null ? customFooterFragment : new ComposeFooterFragment();
        baseFooterFragment.setArguments(getIntent().getExtras());
        baseFooterFragment.setComposeFooterInterface(this);
        baseFooterFragment.setBottomOptionData(mViewModel.getDataFromTxt());
        baseFooterFragment.setBotClient(botClient);
        fragmentTransaction.add(R.id.chatLayoutFooterContainer, baseFooterFragment).commit();

        if (SDKConfig.isShowHeader()) {
            botHeaderFragment = SDKConfig.getCustomHeaderFragment();
            if (botHeaderFragment == null) botHeaderFragment = new BotHeaderFragment();
            botHeaderFragment.setComposeFooterInterface(this);
            botHeaderFragment.setInvokeGenericWebViewInterface(this);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.header_container, botHeaderFragment);
            transaction.commitNow();
        }

        setupTextToSpeech();
        KoreEventCenter.register(this);

        changeStatusBarColor(SDKConfig.isUpdateStatusBarColor() ? sharedPreferences.getString(BundleConstants.STATUS_BAR_COLOR, "#FF3F51B5") : "");
    }

    private void setupTextToSpeech() {
        baseFooterFragment.setTtsUpdate(BotSocketConnectionManager.getInstance());
    }

    @Override
    public void addMessageToAdapter(BotResponse baseBotMessage) {
        botContentFragment.addMessageToBotChatAdapter(baseBotMessage);
        mViewModel.textToSpeech(baseBotMessage, baseFooterFragment.isTTSEnabled());
        botContentFragment.setQuickRepliesIntoFooter(baseBotMessage);
        botContentFragment.showCalendarIntoFooter(baseBotMessage);
        showTemplateBottomSheet(baseBotMessage);
    }

    @Override
    public void onConnectionStateChanged(BaseSocketConnectionManager.CONNECTION_STATE state, boolean isReconnection) {
        if (state == BaseSocketConnectionManager.CONNECTION_STATE.CONNECTED) {
            taskProgressBar.setVisibility(View.GONE);
            baseFooterFragment.enableSendButton();
        }
    }

    @Override
    public void onBrandingDetails(BrandingModel brandingModel) {
        if (brandingModel != null) {
            botName = brandingModel.getBotName();
            if (botContentFragment != null)
                botContentFragment.changeThemeBackGround(brandingModel.getWidgetBodyColor(), brandingModel.getWidgetHeaderColor(), brandingModel.getWidgetTextColor(), brandingModel.getBotName());

            if (baseFooterFragment != null)
                baseFooterFragment.changeThemeBackGround(brandingModel.getWidgetFooterColor(), brandingModel.getWidgetFooterHintColor());

            if (botHeaderFragment != null) {
                botHeaderFragment.setBrandingDetails(brandingModel);
            }

            sharedPreferences.edit().putString(BundleConstants.STATUS_BAR_COLOR, brandingModel.getWidgetHeaderColor()).apply();
        }
    }

    @Override
    public void loadOnConnectionHistory(boolean isReconnect) {
        if (botContentFragment != null && isReconnect) {
            if (sharedPreferences.getInt(BotResponse.HISTORY_COUNT, 0) > SDKConfiguration.OverrideKoreConfig.history_batch_size)
                botContentFragment.loadChatHistory(0, SDKConfiguration.OverrideKoreConfig.history_batch_size);
            else if (sharedPreferences.getInt(BotResponse.HISTORY_COUNT, 0) > 0)
                botContentFragment.loadChatHistory(0, sharedPreferences.getInt(BotResponse.HISTORY_COUNT, 1));
            else if (SDKConfiguration.Client.history_on_network_resume)
                botContentFragment.loadReconnectionChatHistory(0, SDKConfiguration.OverrideKoreConfig.history_batch_size);
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
        baseFooterFragment.setIsAgentConnected(isAgentConnected);
    }

    @Override
    public void enableSendButton() {
        baseFooterFragment.enableSendButton();
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
                taskProgressBar.setVisibility(VISIBLE);
                break;
            case CONNECTED: {
                taskProgressBar.setVisibility(View.GONE);
                baseFooterFragment.enableSendButton();
            }
            break;
            case DISCONNECTED:
            case CONNECTED_BUT_DISCONNECTED: {
                taskProgressBar.setVisibility(VISIBLE);
                baseFooterFragment.setDisabled(true);
                baseFooterFragment.updateUI();
            }
            break;
            default:
                taskProgressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void showReconnectionStopped() {
        DialogInterface.OnClickListener dialogClickListener = (dialog, which) -> {
            if (which == DialogInterface.BUTTON_POSITIVE) {
                BotSocketConnectionManager.killInstance();
                finish();
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
        baseFooterFragment.addAttachmentToAdapter(attachmentKey);
    }

    @Override
    public void uploadBulkFile(String fileName, String filePath, String extension, String filePathThumbnail, String orientation) {
    }

    @Override
    public void onSendClick(String message, boolean isFromUtterance) {
        botContentFragment.showTypingStatus();

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
        botContentFragment.showTypingStatus();
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
        botContentFragment.showTypingStatus();
        if (attachments != null && !attachments.isEmpty()) {
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
            if (baseFooterFragment != null) baseFooterFragment.setJwtToken(jwt);
            mViewModel.getWebHookMeta(jwt);
        }
    }

    @Override
    public void copyMessageToComposer(String text, boolean isForOnboard) {
        baseFooterFragment.setComposeText(text);
    }

    @Override
    public void onDeepLinkClicked(String url) {
    }

    @Override
    public void invokeGenericWebView(String url) {
        if (url != null && !url.isEmpty()) {
            Intent intent = new Intent(this, GenericWebViewActivity.class);
            intent.putExtra(EXTRA_URL, url);
            intent.putExtra(EXTRA_HEADER, !botName.isEmpty() ? botName : SDKConfiguration.Client.bot_name);
            startActivity(intent);
        }
    }

    @Override
    public void handleUserActions(String payload, HashMap<String, Object> type) {

    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(closeBotChatReceiver);
        if (isAgentTransfer && botClient != null)
            botClient.sendAgentCloseMessage("", SDKConfiguration.Client.bot_name, SDKConfiguration.Client.bot_id);

        new PushNotificationRegister().unsubscribePushNotification(botClient.getUserId(), botClient.getAccessToken(), sharedPreferences.getString("PREF_UNIQUE_ID", mViewModel.getUniqueID()));

        if (botClient != null) botClient.disconnect();
        KoreEventCenter.unregister(this);
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        mViewModel.setIsActivityResumed(true);

        if (!SDKConfiguration.Client.isWebHook) {
            BotSocketConnectionManager.getInstance().checkConnectionAndRetry(getApplicationContext(), false);
            updateTitleBar(BotSocketConnectionManager.getInstance().getConnection_state());
        }

        mViewModel.sendReadReceipts();
        super.onResume();
    }

    @Override
    protected void onPause() {
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

                            Intent intent = new Intent();
                            intent.putExtra(BundleUtils.CHAT_BOT_CLOSE_OR_MINIMIZED, BundleUtils.CHAT_BOT_MINIMIZED);
                            setResult(RESULT_OK, intent);
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
                            Intent intent = new Intent();
                            intent.putExtra(BundleUtils.CHAT_BOT_CLOSE_OR_MINIMIZED, BundleUtils.CHAT_BOT_CLOSE);
                            setResult(RESULT_OK, intent);
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

    private void showTemplateBottomSheet(BotResponse botResponse) {
        if (botResponse.getMessage() == null || botResponse.getMessage().get(0) == null || botResponse.getMessage().get(0).getComponent() == null ||
                botResponse.getMessage().get(0).getComponent().getPayload() == null ||
                botResponse.getMessage().get(0).getComponent().getPayload().getPayload() == null ||
                botResponse.getMessage().get(0).getComponent().getPayload().getPayload().getTemplate_type() == null ||
                !botResponse.getMessage().get(0).getComponent().getPayload().getPayload().getSliderView()) {
            return;
        }
        TemplateBottomSheetFragment bottomSheetFragment = new TemplateBottomSheetFragment();
        bottomSheetFragment.setComposeFooterInterface(this);
        bottomSheetFragment.setInvokeGenericWebViewInterface(this);
        bottomSheetFragment.show(botResponse, getSupportFragmentManager());
    }

    @Override
    protected void onStop() {
        mViewModel.setIsActivityResumed(false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
            List<ActivityManager.AppTask> taskList = activityManager.getAppTasks();

            if (!taskList.isEmpty()) {
                String topClassName = Objects.requireNonNull(taskList.get(0).getTaskInfo().topActivity).toString();
                if (!topClassName.contains(getApplicationContext().getPackageName())) {

                    if (botClient != null) {
                        botClient.sendAgentCloseMessage("", SDKConfiguration.Client.bot_name, SDKConfiguration.Client.bot_id);

                        LogUtils.e("onStop", "onStop called");

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


    @Override
    public void onStart() {
        new Handler().post(() -> BotSocketConnectionManager.getInstance().subscribe());
        super.onStart();
    }
}
