package kore.botssdk.activity;

import static android.view.View.VISIBLE;
import static kore.botssdk.activity.GenericWebViewActivity.EXTRA_HEADER;
import static kore.botssdk.activity.GenericWebViewActivity.EXTRA_URL;
import static kore.botssdk.models.BotResponse.HEADER_SIZE_COMPACT;
import static kore.botssdk.models.BotResponse.HEADER_SIZE_LARGE;
import static kore.botssdk.net.SDKConfig.getCustomContentFragment;
import static kore.botssdk.net.SDKConfig.getCustomFooterFragment;
import static kore.botssdk.net.SDKConfig.isMinimized;
import static kore.botssdk.utils.BundleConstants.CHOOSE_IMAGE_BUNDLED_PERMISSION_REQUEST;
import static kore.botssdk.utils.BundleConstants.CLOSE_CHAT_BOT_EVENT;
import static kore.botssdk.utils.BundleConstants.MINIMIZE_CHAT_BOT_EVENT;
import static kore.botssdk.view.viewUtils.DimensionUtil.dp1;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.gson.Gson;
import com.kore.ai.widgetsdk.fragments.BottomPanelFragment;
import com.kore.ai.widgetsdk.listeners.WidgetComposeFooterInterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import kore.botssdk.R;
import kore.botssdk.audiocodes.webrtcclient.General.ACManager;
import kore.botssdk.audiocodes.webrtcclient.General.AppUtils;
import kore.botssdk.audiocodes.webrtcclient.General.Prefs;
import kore.botssdk.bot.BotClient;
import kore.botssdk.dialogs.AdvanceMultiSelectSheetFragment;
import kore.botssdk.event.KoreEventCenter;
import kore.botssdk.events.SocketDataTransferModel;
import kore.botssdk.fileupload.core.KoreWorker;
import kore.botssdk.fileupload.core.UploadBulkFile;
import kore.botssdk.fragment.content.BaseContentFragment;
import kore.botssdk.fragment.content.BotContentFragment;
import kore.botssdk.fragment.footer.BaseFooterFragment;
import kore.botssdk.fragment.footer.ComposeFooterFragment;
import kore.botssdk.fragment.header.BaseHeaderFragment;
import kore.botssdk.fragment.header.ChatHeaderOneFragment;
import kore.botssdk.fragment.header.ChatHeaderThreeFragment;
import kore.botssdk.fragment.header.ChatHeaderTwoFragment;
import kore.botssdk.fragment.welcome.WelcomeScreenFragment;
import kore.botssdk.listener.BaseSocketConnectionManager;
import kore.botssdk.listener.BotChatViewListener;
import kore.botssdk.listener.BotSocketConnectionManager;
import kore.botssdk.listener.ComposeFooterInterface;
import kore.botssdk.listener.InvokeGenericWebViewInterface;
import kore.botssdk.listener.TTSUpdate;
import kore.botssdk.models.BotBrandingModel;
import kore.botssdk.models.BotRequest;
import kore.botssdk.models.BotResponse;
import kore.botssdk.models.BrandingHeaderModel;
import kore.botssdk.models.EventModel;
import kore.botssdk.models.KoreComponentModel;
import kore.botssdk.models.KoreMedia;
import kore.botssdk.models.PayloadInner;
import kore.botssdk.net.SDKConfig;
import kore.botssdk.net.SDKConfiguration;
import kore.botssdk.utils.BitmapUtils;
import kore.botssdk.utils.BundleConstants;
import kore.botssdk.utils.BundleUtils;
import kore.botssdk.utils.ClosingService;
import kore.botssdk.utils.KaMediaUtils;
import kore.botssdk.utils.KaPermissionsHelper;
import kore.botssdk.utils.LogUtils;
import kore.botssdk.utils.StringUtils;
import kore.botssdk.utils.TTSSynthesizer;
import kore.botssdk.utils.ToastUtils;
import kore.botssdk.view.viewUtils.DimensionUtil;
import kore.botssdk.viewmodels.chat.BotChatViewModel;
import kore.botssdk.viewmodels.chat.BotChatViewModelFactory;
import kore.botssdk.websocket.SocketWrapper;

@SuppressWarnings("UnKnownNullness")
public class BotChatActivity extends BotAppCompactActivity implements BotChatViewListener, ComposeFooterInterface, TTSUpdate, InvokeGenericWebViewInterface, WidgetComposeFooterInterface {
    private ProgressBar taskProgressBar;
    private String jwt;
    private Handler actionBarTitleUpdateHandler;
    private BotClient botClient;
    private BaseContentFragment botContentFragment;
    private BaseFooterFragment composeFooterFragment;
    private TTSSynthesizer ttsSynthesizer;
    private final Gson gson = new Gson();
    private RelativeLayout rlChatWindow;
    private SharedPreferences sharedPreferences;
    private final Handler messageHandler = new Handler();
    private String fileUrl;
    private Dialog progressBar, welcomeDialog;
    boolean isAgentTransfer = false;
    private Dialog alertDialog;
    private BotChatViewModel viewModel;
    private boolean isWelcomeVisible = false;
    private String botName = SDKConfiguration.Client.bot_name;
    private final BroadcastReceiver minimizeBotChatReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (Objects.equals(intent.getAction(), MINIMIZE_CHAT_BOT_EVENT)) {
                if (sharedPreferences != null) {
                    SDKConfig.setIsMinimized(true);
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
                SDKConfig.setIsMinimized(false);
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

            SharedPreferences.Editor editor = sharedPreferences.edit();
            SDKConfig.setIsMinimized(false);
            editor.putInt(BotResponse.HISTORY_COUNT, 0);
            editor.apply();
        }
    };

    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bot_chat_layout);
        botClient = new BotClient(this);

        new DimensionUtil(BotChatActivity.this);

        BotChatViewModelFactory factory = new BotChatViewModelFactory(BotChatActivity.this, botClient, BotChatActivity.this);
        viewModel = new ViewModelProvider(this, factory).get(BotChatViewModel.class);

        findViews();
        getBundleInfo();

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (isOnline()) {
                    showCloseAlert();
                }
            }
        });

        LocalBroadcastManager.getInstance(this).registerReceiver(onDestroyReceiver, new IntentFilter(BundleConstants.DESTROY_EVENT));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            registerReceiver(closeBotChatReceiver, new IntentFilter(CLOSE_CHAT_BOT_EVENT), RECEIVER_EXPORTED);
            registerReceiver(minimizeBotChatReceiver, new IntentFilter(MINIMIZE_CHAT_BOT_EVENT), RECEIVER_EXPORTED);
        } else {
            registerReceiver(closeBotChatReceiver, new IntentFilter(CLOSE_CHAT_BOT_EVENT));
            registerReceiver(minimizeBotChatReceiver, new IntentFilter(MINIMIZE_CHAT_BOT_EVENT));
        }

        viewModel.connectToBot(isMinimized());
        startService(new Intent(getApplicationContext(), ClosingService.class));
    }

    @Override
    protected void onDestroy() {
        if (isAgentTransfer && botClient != null)
            botClient.sendAgentCloseMessage("", SDKConfiguration.Client.bot_name, SDKConfiguration.Client.bot_id);

        if (progressBar != null) progressBar.dismiss();

        if (welcomeDialog != null) {
            welcomeDialog.hide();
        }
        if (botClient != null) botClient.disconnect();
        KoreEventCenter.unregister(this);
        super.onDestroy();
    }

    private void getBundleInfo() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            jwt = bundle.getString(BundleUtils.JWT_TKN, "");
        }
    }

    private void addHeaderFragmentToActivity(Fragment fragment, BrandingHeaderModel brandingHeaderModel) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction tr = fm.beginTransaction();

        if (fragment instanceof BaseHeaderFragment) {
            ((BaseHeaderFragment) fragment).setBrandingDetails(brandingHeaderModel);
            ((BaseHeaderFragment) fragment).setComposeFooterInterface(this);
            ((BaseHeaderFragment) fragment).setInvokeGenericWebViewInterface(this);
        }
        tr.add(R.id.header_container, fragment);
        tr.commitAllowingStateLoss();
    }

    private void findViews() {
        rlChatWindow = findViewById(R.id.rlChatWindow);
        taskProgressBar = findViewById(R.id.taskProgressBar);
        sharedPreferences = getSharedPreferences(BotResponse.THEME_NAME, Context.MODE_PRIVATE);
        showProgressDialogue();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        //Add Bot Content Fragment
        botContentFragment = getCustomContentFragment();
        if (botContentFragment == null)
            botContentFragment = new BotContentFragment();
        botContentFragment.setArguments(getIntent().getExtras());
        botContentFragment.setComposeFooterInterface(this);
        botContentFragment.setInvokeGenericWebViewInterface(this);
        fragmentTransaction.add(R.id.chatLayoutContentContainer, botContentFragment).commit();

        //Add Bot Compose Footer Fragment
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        composeFooterFragment = getCustomFooterFragment();
        if (composeFooterFragment == null)
            composeFooterFragment = new ComposeFooterFragment();
        composeFooterFragment.setArguments(getIntent().getExtras());
        composeFooterFragment.setComposeFooterInterface(this);
        composeFooterFragment.setBotClient(botClient);
        fragmentTransaction.add(R.id.chatLayoutFooterContainer, composeFooterFragment).commit();

        ttsSynthesizer = new TTSSynthesizer(this);
        setupTextToSpeech();
        KoreEventCenter.register(this);
        attachFragments();
    }

    @Override
    public void updateTitleBar(BaseSocketConnectionManager.CONNECTION_STATE socketConnectionEvents) {
        switch (socketConnectionEvents) {
            case CONNECTING:
                taskProgressBar.setVisibility(View.VISIBLE);
                updateActionBar();
                break;
            case CONNECTED:
                taskProgressBar.setVisibility(View.GONE);
                composeFooterFragment.enableSendButton();
                updateActionBar();
                break;
            case DISCONNECTED:
            case CONNECTED_BUT_DISCONNECTED:
                taskProgressBar.setVisibility(View.VISIBLE);
                composeFooterFragment.setDisabled(true);
                composeFooterFragment.updateUI();
                updateActionBar();
                break;

            default:
                taskProgressBar.setVisibility(View.GONE);
                updateActionBar();
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
            viewModel.getWebHookMeta(jwt);
        } else if (botClient != null && !botClient.isConnected()) {
            BotSocketConnectionManager.getInstance().startAndInitiateConnectionWithReconnect(getApplicationContext(), null, isMinimized());
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

    public void onEvent(BotResponse botResponse) {
        processPayload("", botResponse);
    }

    @Override
    public void externalReadWritePermission(String fileUrl) {
        this.fileUrl = fileUrl;
        if (!KaPermissionsHelper.hasPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            KaPermissionsHelper.requestForPermission(this, CHOOSE_IMAGE_BUNDLED_PERMISSION_REQUEST, Manifest.permission.READ_EXTERNAL_STORAGE);
        }
    }

    @Override
    public void onDeepLinkClicked(String url) {
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @androidx.annotation.NonNull String[] permissions, @androidx.annotation.NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CHOOSE_IMAGE_BUNDLED_PERMISSION_REQUEST) {
            if (KaPermissionsHelper.hasPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                if (!StringUtils.isNullOrEmpty(fileUrl)) KaMediaUtils.saveFileFromUrlToKorePath(BotChatActivity.this, fileUrl);
            } else {
                Toast.makeText(getApplicationContext(), "Access denied. Operation failed !!", Toast.LENGTH_LONG).show();
            }
        }
    }

    void updateActionBar() {
        if (actionBarTitleUpdateHandler == null) {
            actionBarTitleUpdateHandler = new Handler();
        }
    }

    @Override
    protected void onPause() {
        ttsSynthesizer.stopTextToSpeech();
        super.onPause();
    }

    private void changeStatusBarColor(String color) {
        if (SDKConfig.isUpdateStatusBarColor()) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor(color));
        }
    }

    @Override
    public void onSendClick(String message, boolean isFromUtterance) {
        if (!StringUtils.isNullOrEmpty(message)) {
            closeWelcomeDialog();

            if (!SDKConfiguration.Client.isWebHook) BotSocketConnectionManager.getInstance().sendMessage(message, null);
            else {
                viewModel.addSentMessageToChat(message);
                viewModel.sendWebHookMessage(jwt, false, message, null);
                BotSocketConnectionManager.getInstance().stopTextToSpeech();
            }
        }
    }

    @Override
    public void onSendClick(String message, String payload, boolean isFromUtterance) {
        closeWelcomeDialog();
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
                viewModel.addSentMessageToChat(message);
                viewModel.sendWebHookMessage(jwt, false, payload, null);
            } else {
                viewModel.addSentMessageToChat(message);
                viewModel.sendWebHookMessage(jwt, false, message, null);
            }
        }
    }

    @Override
    public void onSendClick(String message, ArrayList<HashMap<String, String>> attachments, boolean isFromUtterance) {
        if (attachments != null && !attachments.isEmpty()) {
            if (!SDKConfiguration.Client.isWebHook) BotSocketConnectionManager.getInstance().sendAttachmentMessage(message, attachments);
            else {
                viewModel.addSentMessageToChat(message);
                viewModel.sendWebHookMessage(jwt, false, message, attachments);
            }
        }
    }

    @Override
    public void sendWithSomeDelay(String message, String payload, long time, boolean isScrollUpNeeded) {
        if (message.equalsIgnoreCase(BundleUtils.OPEN_WELCOME)) {
            if (welcomeDialog != null) welcomeDialog.show();
        }
    }

    @Override
    public void copyMessageToComposer(String text, boolean isForOnboard) {
        composeFooterFragment.setComposeText(text);
    }

    @Override
    public void addMessageToAdapter(BotResponse botResponse) {
        botContentFragment.addMessageToBotChatAdapter(botResponse);
        viewModel.textToSpeech(botResponse, composeFooterFragment.isTTSEnabled());
        botContentFragment.setQuickRepliesIntoFooter(botResponse);
        botContentFragment.showCalendarIntoFooter(botResponse);
        showTemplateBottomSheet(botResponse);
    }

    @Override
    public void onConnectionStateChanged(BaseSocketConnectionManager.CONNECTION_STATE state, boolean isReconnection) {
        if (state == BaseSocketConnectionManager.CONNECTION_STATE.CONNECTED) {
            taskProgressBar.setVisibility(View.GONE);
            composeFooterFragment.enableSendButton();
        }
    }

    @Override
    public void onBrandingDetails(BotBrandingModel botBrandingModel) {
        if (botBrandingModel != null) {
            botName = botBrandingModel.getHeader().getTitle().getName();
            if (botBrandingModel.getHeader() != null)
                changeStatusBarColor(botBrandingModel.getHeader().getBg_color());
            if (!isWelcomeVisible && botBrandingModel.getWelcome_screen() != null && botBrandingModel.getWelcome_screen().isShow()) {
                isWelcomeVisible = true;
                WelcomeScreenFragment fragment = new WelcomeScreenFragment();
                fragment.setBotBrandingModel(botBrandingModel);
                fragment.setComposeFooterInterface(this);
                fragment.setInvokeGenericWebViewInterface(this);
                fragment.show(getSupportFragmentManager(), fragment.getClass().getName());
            }
            rlChatWindow.setVisibility(VISIBLE);

            if (composeFooterFragment != null) {
                composeFooterFragment.setBotBrandingModel(botBrandingModel);
            }

            if (botContentFragment != null) {
                botContentFragment.setBotBrandingModel(botBrandingModel);
            }
            BrandingHeaderModel header = botBrandingModel.getHeader();
            if (header != null) {
                BaseHeaderFragment customHeaderFragment = SDKConfig.getCustomHeaderFragment(header.getSize());
                switch (header.getSize()) {
                    case HEADER_SIZE_COMPACT:
                        addHeaderFragmentToActivity(customHeaderFragment != null ? customHeaderFragment : new ChatHeaderOneFragment(), header);
                        break;
                    case HEADER_SIZE_LARGE:
                        addHeaderFragmentToActivity(customHeaderFragment != null ? customHeaderFragment : new ChatHeaderThreeFragment(), header);
                        break;
                    default:
                        addHeaderFragmentToActivity(customHeaderFragment != null ? customHeaderFragment : new ChatHeaderTwoFragment(), header);
                }
            }
        }

        closeProgressDialogue();
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
        this.isAgentTransfer = isAgentConnected;
        composeFooterFragment.setIsAgentConnected(isAgentConnected);
    }

    @Override
    public void enableSendButton() {
        composeFooterFragment.enableSendButton();
    }

    @Override
    public void processPayload(String payload, BotResponse botResponse) {
        viewModel.processPayload(payload, botResponse);
    }

    @Override
    public void displayMessage(String text, String type, String messageId) {
        viewModel.displayMessage(text, type, messageId, SDKConfiguration.BubbleColors.getIcon_url());
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
    public void handleUserActions(String action, HashMap<String, Object> payload) {
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

    private void showTemplateBottomSheet(BotResponse botResponse) {
        if (botResponse.getMessage() == null || botResponse.getMessage().get(0) == null || botResponse.getMessage().get(0).getComponent() == null ||
                botResponse.getMessage().get(0).getComponent().getPayload() == null ||
                botResponse.getMessage().get(0).getComponent().getPayload().getPayload() == null ||
                botResponse.getMessage().get(0).getComponent().getPayload().getPayload().getTemplate_type() == null) {
            return;
        }
        PayloadInner payloadInner = botResponse.getMessage().get(0).getComponent().getPayload().getPayload();
        switch (payloadInner.getTemplate_type()) {
            case BotResponse.ADVANCED_MULTI_SELECT_TEMPLATE -> {
                if (payloadInner.getSliderView()) {
                    AdvanceMultiSelectSheetFragment fragment = new AdvanceMultiSelectSheetFragment();
                    fragment.setData(payloadInner);
                    fragment.setComposeFooterInterface(this);
                    fragment.show(getSupportFragmentManager(), AdvanceMultiSelectSheetFragment.class.getName());
                }
            }
        }
    }

    @Override
    public void onStop() {
        viewModel.setIsActivityResumed(false);
        BotSocketConnectionManager.getInstance().unSubscribe();
        super.onStop();
    }

    @Override
    public void onStart() {
        new Handler().post(() -> BotSocketConnectionManager.getInstance().subscribe());
        super.onStart();
    }

    @Override
    protected void onResume() {
        viewModel.setIsActivityResumed(true);
        if (!SDKConfiguration.Client.isWebHook) {
            BotSocketConnectionManager.getInstance().checkConnectionAndRetry(getApplicationContext(), false);
            updateTitleBar(BotSocketConnectionManager.getInstance().getConnection_state());
        }

        if (viewModel != null)
            viewModel.sendReadReceipts();

        super.onResume();
    }

    void showCloseAlert() {
        DialogInterface.OnClickListener dialogClickListener = (dialog, which) -> {
            switch (which) {
                case DialogInterface.BUTTON_POSITIVE:
                    if (sharedPreferences != null) {
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        SDKConfig.setIsMinimized(true);
                        editor.putInt(BotResponse.HISTORY_COUNT, botContentFragment.getAdapterCount());
                        editor.apply();
                        BotSocketConnectionManager.killInstance();

                        Intent intent = new Intent();
                        intent.putExtra(BundleUtils.CHAT_BOT_CLOSE_OR_MINIMIZED, BundleUtils.CHAT_BOT_MINIMIZED);
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                    break;
                case DialogInterface.BUTTON_NEGATIVE:
                    if (sharedPreferences != null) {
                        if (botClient != null)
                            botClient.sendAgentCloseMessage("", SDKConfiguration.Client.bot_name, SDKConfiguration.Client.bot_id);

                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        SDKConfig.setIsMinimized(false);
                        editor.putInt(BotResponse.HISTORY_COUNT, 0);
                        editor.apply();
                        BotSocketConnectionManager.killInstance();
                        Intent intent = new Intent();
                        intent.putExtra(BundleUtils.CHAT_BOT_CLOSE_OR_MINIMIZED, BundleUtils.CHAT_BOT_CLOSE);
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                    break;
                case DialogInterface.BUTTON_NEUTRAL:
                    dialog.dismiss();
                    break;
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(BotChatActivity.this);
        builder.setMessage(R.string.close_or_minimize).setCancelable(false).setPositiveButton(R.string.minimize, dialogClickListener).setNegativeButton(R.string.close, dialogClickListener).setNeutralButton(R.string.cancel, dialogClickListener).show();
    }

    @Override
    public void ttsUpdateListener(boolean isTTSEnabled) {
        stopTextToSpeech();
    }

    @Override
    public void ttsOnStop() {
        stopTextToSpeech();
    }

    private void stopTextToSpeech() {
        try {
            ttsSynthesizer.stopTextToSpeech();
        } catch (IllegalArgumentException exception) {
            LogUtils.stackTrace(exception);
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

    private void attachFragments() {
        if (SDKConfiguration.Client.enablePanel) {
            //Fragment Approach
            FrameLayout composerView = findViewById(R.id.chatLayoutPanelContainer);
            composerView.setVisibility(VISIBLE);
            BottomPanelFragment composerFragment = new BottomPanelFragment();
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) bundle.putString("bgColor", sharedPreferences.getString(BotResponse.BUTTON_INACTIVE_BG_COLOR, "#ffffff"));

            composerFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.chatLayoutPanelContainer, composerFragment).commit();
            composerFragment.setPanelComposeFooterInterface(BotChatActivity.this, SDKConfiguration.Client.identity);
        }
    }

    @Override
    public void onPanelSendClick(String message, boolean isFromUtterance) {
        BotSocketConnectionManager.getInstance().sendMessage(message, null);
    }

    @Override
    public void onPanelSendClick(String message, String payload, boolean isFromUtterance) {
        if (payload != null) {
            BotSocketConnectionManager.getInstance().sendPayload(message, payload);
        } else {
            BotSocketConnectionManager.getInstance().sendMessage(message, payload);
        }
    }

    public void sendImage(String fP, String fN, String fPT) {
        viewModel.sendImage(fP, fN, fPT);
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

                messageHandler.postDelayed(() -> {
                    HashMap<String, String> attachmentKey = new HashMap<>();
                    attachmentKey.put("fileName", mediaFileName + "." + MEDIA_TYPE);
                    attachmentKey.put("fileType", componentType);
                    attachmentKey.put("fileId", mediaFileId);
                    attachmentKey.put("localFilePath", mediaFilePath);
                    attachmentKey.put("fileExtn", MEDIA_TYPE);
                    attachmentKey.put("thumbnailURL", thumbnailURL);
                    composeFooterFragment.addAttachmentToAdapter(attachmentKey);
                }, 400);
            } else {
                String errorMsg = reply.getString(UploadBulkFile.error_msz_key);
                if (!TextUtils.isEmpty(errorMsg)) {
                    LogUtils.i("File upload error", errorMsg);
                    ToastUtils.showToast(BotChatActivity.this, "Unable to attach!");
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

    void showProgressDialogue() {
        progressBar = new Dialog(this);
        View view = LayoutInflater.from(this).inflate(R.layout.progress_bar_dialog, null);
        progressBar.setContentView(view);
        Objects.requireNonNull(progressBar.getWindow()).setLayout((int) (250 * dp1), (int) (100 * dp1));
        progressBar.setCancelable(false);
        progressBar.setCanceledOnTouchOutside(false);
        progressBar.show();
    }

    void closeWelcomeDialog() {
        if (welcomeDialog != null && welcomeDialog.isShowing()) {
            welcomeDialog.dismiss();

            closeProgressDialogue();
        }
    }

    void closeProgressDialogue() {
        if (progressBar != null) progressBar.hide();
    }

    @Override
    public void showAlertDialog(EventModel eventModel) {
        alertDialog = new Dialog(BotChatActivity.this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.incoming_call_layout, null);
        alertDialog.setContentView(dialogView);
        alertDialog.setCancelable(false);

        TextView tvAgentName = dialogView.findViewById(R.id.tvAgentName);
        TextView tvCallType = dialogView.findViewById(R.id.tvTypeOfCall);
        TextView tvCallAccept = dialogView.findViewById(R.id.tvCallAccept);
        TextView tvCallReject = dialogView.findViewById(R.id.tvCallReject);

        tvAgentName.setText(eventModel.getMessage().getFirstName());
        tvCallType.setText(getString(R.string.incoming_audio_call));

        if (eventModel.getMessage().isVideoCall()) tvCallType.setText(getString(R.string.incoming_video_call));

        tvCallAccept.setOnClickListener(v -> {
            if (eventModel.getMessage() != null) {
                eventModel.getMessage().setType("call_agent_webrtc_accepted");
                botClient.sendMessage(gson.toJson(eventModel.getMessage()));

                AppUtils.setEventModel(eventModel);
                AppUtils.setBotClient(botClient);

                openNextScreen(eventModel.getMessage().getSipUser(), eventModel.getMessage().isVideoCall());
                alertDialog.dismiss();
            }
        });

        tvCallReject.setOnClickListener(v -> alertDialog.dismiss());

        alertDialog.show();
    }

    @Override
    public void hideAlertDialog() {
        if (alertDialog != null && alertDialog.isShowing()) alertDialog.dismiss();
    }

    void openNextScreen(String sipUser, boolean isVideoCall) {
        Prefs.setFirstLogin(this, false);
        //start login and open app main screen
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (!ACManager.getInstance().isRegisterState()) {
                    ACManager.getInstance().startLogin(BotChatActivity.this, false, true);
                }

                if (!ACManager.getInstance().isRegisterState() && Prefs.getAutoLogin(BotChatActivity.this)) {
                    Toast.makeText(BotChatActivity.this, R.string.no_registration, Toast.LENGTH_SHORT).show();
                } else {
                    ACManager.getInstance().callNumber(sipUser, isVideoCall);
                }
            }
        }).start();
    }

    @Override
    public void showReconnectionStopped() {
        DialogInterface.OnClickListener dialogClickListener = (dialog, which) -> {
            if (which == DialogInterface.BUTTON_POSITIVE) {
                finish();
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(BotChatActivity.this);
        builder.setMessage(R.string.bot_not_connected).setCancelable(false).setPositiveButton(R.string.ok, dialogClickListener).show();
    }

    @Override
    public void getBrandingDetails() {
        viewModel.getBrandingDetails();
    }

    @Override
    public void uploadBulkFile(String fileName, String filePath, String extn, String filePathThumbnail, String orientation) {
        if (!SDKConfiguration.Client.isWebHook) {
            KoreWorker.getInstance().addTask(new UploadBulkFile(fileName, filePath, "bearer " + SocketWrapper.getInstance(BotChatActivity.this).getAccessToken(), SocketWrapper.getInstance(BotChatActivity.this).getBotUserId(), "workflows", extn, KoreMedia.BUFFER_SIZE_IMAGE, new Messenger(messagesMediaUploadAcknowledgeHandler), filePathThumbnail, "AT_" + System.currentTimeMillis(), BotChatActivity.this, BitmapUtils.obtainMediaTypeOfExtn(extn), (SDKConfiguration.Server.SERVER_URL), orientation, true, SDKConfiguration.Client.isWebHook, SDKConfiguration.Client.bot_id));
        } else {
            KoreWorker.getInstance().addTask(new UploadBulkFile(fileName, filePath, "bearer " + jwt, SocketWrapper.getInstance(BotChatActivity.this).getBotUserId(), "workflows", extn, KoreMedia.BUFFER_SIZE_IMAGE, new Messenger(messagesMediaUploadAcknowledgeHandler), filePathThumbnail, "AT_" + System.currentTimeMillis(), BotChatActivity.this, BitmapUtils.obtainMediaTypeOfExtn(extn), (SDKConfiguration.Server.SERVER_URL), orientation, true, SDKConfiguration.Client.isWebHook, SDKConfiguration.Client.bot_id));
        }
    }
}
