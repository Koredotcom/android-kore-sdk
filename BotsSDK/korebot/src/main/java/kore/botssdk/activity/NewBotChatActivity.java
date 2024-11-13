package kore.botssdk.activity;

import static android.view.View.VISIBLE;
import static kore.botssdk.net.SDKConfig.isMinimized;
import static kore.botssdk.utils.BundleConstants.CHOOSE_IMAGE_BUNDLED_PERMISSION_REQUEST;
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
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
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
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.google.gson.Gson;
import com.kore.ai.widgetsdk.fragments.BottomPanelFragment;
import com.kore.ai.widgetsdk.listeners.WidgetComposeFooterInterface;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;

import kore.botssdk.R;
import kore.botssdk.adapter.PromotionsAdapter;
import kore.botssdk.adapter.WelcomeStarterButtonsAdapter;
import kore.botssdk.adapter.WelcomeStaticLinkListAdapter;
import kore.botssdk.adapter.WelcomeStaticLinksAdapter;
import kore.botssdk.audiocodes.webrtcclient.General.ACManager;
import kore.botssdk.audiocodes.webrtcclient.General.AppUtils;
import kore.botssdk.audiocodes.webrtcclient.General.Prefs;
import kore.botssdk.bot.BotClient;
import kore.botssdk.event.KoreEventCenter;
import kore.botssdk.events.SocketDataTransferModel;
import kore.botssdk.fileupload.core.KoreWorker;
import kore.botssdk.fileupload.core.UploadBulkFile;
import kore.botssdk.fragment.BotContentFragment;
import kore.botssdk.fragment.ComposeFooterFragment;
import kore.botssdk.listener.BaseSocketConnectionManager;
import kore.botssdk.listener.BotChatViewListener;
import kore.botssdk.listener.BotSocketConnectionManager;
import kore.botssdk.listener.ComposeFooterInterface;
import kore.botssdk.listener.InvokeGenericWebViewInterface;
import kore.botssdk.listener.TTSUpdate;
import kore.botssdk.models.BotBrandingModel;
import kore.botssdk.models.BotInfoModel;
import kore.botssdk.models.BotRequest;
import kore.botssdk.models.BotResponse;
import kore.botssdk.models.BrandingWelcomeModel;
import kore.botssdk.models.EventModel;
import kore.botssdk.models.KoreComponentModel;
import kore.botssdk.models.KoreMedia;
import kore.botssdk.net.RestResponse;
import kore.botssdk.net.SDKConfig;
import kore.botssdk.net.SDKConfiguration;
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
import kore.botssdk.utils.ToastUtils;
import kore.botssdk.view.AutoExpandListView;
import kore.botssdk.view.HeightAdjustableViewPager;
import kore.botssdk.view.viewUtils.RoundedCornersTransform;
import kore.botssdk.viewmodels.chat.BotChatViewModel;
import kore.botssdk.viewmodels.chat.BotChatViewModelFactory;
import kore.botssdk.websocket.SocketWrapper;

@SuppressWarnings("UnKnownNullness")
public class NewBotChatActivity extends BotAppCompactActivity implements BotChatViewListener, ComposeFooterInterface, TTSUpdate, InvokeGenericWebViewInterface, WidgetComposeFooterInterface {
    private final String LOG_TAG = NewBotChatActivity.class.getSimpleName();
    private ProgressBar taskProgressBar;
    private String jwt;
    private Handler actionBarTitleUpdateHandler;
    private BotClient botClient;
    private BotContentFragment botContentFragment;
    private ComposeFooterFragment composeFooterFragment;
    private TTSSynthesizer ttsSynthesizer;
    private final Gson gson = new Gson();
    private RelativeLayout rlChatWindow;
    private SharedPreferences sharedPreferences;
    private final Handler messageHandler = new Handler();
    private String fileUrl;
    private Dialog progressBar, welcomeDialog;
    private boolean isAgentTransfer = false;
    private Dialog alertDialog;
    private BotChatViewModel mViewModel;

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

        BotChatViewModelFactory factory = new BotChatViewModelFactory(NewBotChatActivity.this, botClient, NewBotChatActivity.this);
        mViewModel = new ViewModelProvider(this, factory).get(BotChatViewModel.class);

        findViews();
        getBundleInfo();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            registerReceiver(onDestroyReceiver, new IntentFilter(BundleConstants.DESTROY_EVENT), RECEIVER_NOT_EXPORTED);
        } else {
            registerReceiver(onDestroyReceiver, new IntentFilter(BundleConstants.DESTROY_EVENT));
        }

        mViewModel.connectToBot(isMinimized());
        startService(new Intent(getApplicationContext(), ClosingService.class));
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
                    }
                    break;
                case DialogInterface.BUTTON_NEUTRAL:
                    dialog.dismiss();
                    break;
            }

            BotSocketConnectionManager.killInstance();
            finish();
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(NewBotChatActivity.this);
        builder.setMessage(R.string.app_name).setCancelable(false).setPositiveButton(R.string.minimize, dialogClickListener).setNegativeButton(R.string.close, dialogClickListener).setNeutralButton(R.string.cancel, dialogClickListener).show();
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

    private void findViews() {
        rlChatWindow = findViewById(R.id.rlChatWindow);
        taskProgressBar = findViewById(R.id.taskProgressBar);
        sharedPreferences = getSharedPreferences(BotResponse.THEME_NAME, Context.MODE_PRIVATE);
        showProgressDialogue();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        //Add Bot Content Fragment
        botContentFragment = new BotContentFragment();
        botContentFragment.setArguments(getIntent().getExtras());
        botContentFragment.setComposeFooterInterface(this);
        botContentFragment.setInvokeGenericWebViewInterface(this);
        fragmentTransaction.add(R.id.chatLayoutContentContainer, botContentFragment).commit();

        //Add Bot Compose Footer Fragment
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        composeFooterFragment = new ComposeFooterFragment();
        composeFooterFragment.setArguments(getIntent().getExtras());
        composeFooterFragment.setComposeFooterInterface(this);
        composeFooterFragment.setBotClient(botClient);
        fragmentTransaction.add(R.id.chatLayoutFooterContainer, composeFooterFragment).commit();

        ttsSynthesizer = new TTSSynthesizer(this);
        setupTextToSpeech();
        KoreEventCenter.register(this);
        attachFragments();

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (isOnline()) {
                    showCloseAlert();
                }
            }
        });
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
            mViewModel.getWebHookMeta(jwt);
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

    public void setButtonBranding(BotBrandingModel brandingModel) {
        if (brandingModel != null) {
            SharedPreferences.Editor editor = getSharedPreferences(BotResponse.THEME_NAME, Context.MODE_PRIVATE).edit();

            if (brandingModel.getBody() != null && brandingModel.getBody().getBot_message() != null) {
                editor.putString(BotResponse.BUBBLE_LEFT_BG_COLOR, brandingModel.getBody().getBot_message().getBg_color());
                editor.putString(BotResponse.BUBBLE_LEFT_TEXT_COLOR, brandingModel.getBody().getBot_message().getColor());
            }

            if (brandingModel.getBody() != null && brandingModel.getBody().getUser_message() != null) {
                editor.putString(BotResponse.BUBBLE_RIGHT_BG_COLOR, brandingModel.getBody().getUser_message().getBg_color());
                editor.putString(BotResponse.BUBBLE_RIGHT_TEXT_COLOR, brandingModel.getBody().getUser_message().getColor());
            }

            if (brandingModel.getGeneral() != null && brandingModel.getGeneral().getColors() != null && brandingModel.getGeneral().getColors().isUseColorPaletteOnly()) {
                editor.putString(BotResponse.BUTTON_ACTIVE_BG_COLOR, brandingModel.getGeneral().getColors().getPrimary());
                editor.putString(BotResponse.BUTTON_ACTIVE_TXT_COLOR, brandingModel.getGeneral().getColors().getPrimary_text());
                editor.putString(BotResponse.BUTTON_INACTIVE_BG_COLOR, brandingModel.getGeneral().getColors().getSecondary());
                editor.putString(BotResponse.BUTTON_INACTIVE_TXT_COLOR, brandingModel.getGeneral().getColors().getSecondary_text());
                SDKConfiguration.BubbleColors.quickReplyColor = brandingModel.getGeneral().getColors().getPrimary();
                SDKConfiguration.BubbleColors.quickReplyTextColor = brandingModel.getGeneral().getColors().getPrimary_text();
                editor.putString(BotResponse.BUBBLE_LEFT_BG_COLOR, brandingModel.getGeneral().getColors().getSecondary());
                editor.putString(BotResponse.BUBBLE_LEFT_TEXT_COLOR, brandingModel.getGeneral().getColors().getPrimary_text());
                editor.putString(BotResponse.BUBBLE_RIGHT_BG_COLOR, brandingModel.getGeneral().getColors().getPrimary());
                editor.putString(BotResponse.BUBBLE_RIGHT_TEXT_COLOR, brandingModel.getGeneral().getColors().getSecondary_text());
            }
            editor.apply();
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
                if (!StringUtils.isNullOrEmpty(fileUrl)) KaMediaUtils.saveFileFromUrlToKorePath(NewBotChatActivity.this, fileUrl);
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
        mViewModel.setIsActivityResumed(false);
        ttsSynthesizer.stopTextToSpeech();
        super.onPause();
    }

    @Override
    public void onSendClick(String message, boolean isFromUtterance) {
        if (!StringUtils.isNullOrEmpty(message)) {
            closeWelcomeDialog();

            if (!SDKConfiguration.Client.isWebHook) BotSocketConnectionManager.getInstance().sendMessage(message, null);
            else {
                addSentMessageToChat(message);
                mViewModel.sendWebHookMessage(jwt, false, message, null);
                BotSocketConnectionManager.getInstance().stopTextToSpeech();
            }
        }
    }

    @Override
    public void onSendClick(String message, String payload, boolean isFromUtterance) {
        closeWelcomeDialog();
        if (!SDKConfiguration.Client.isWebHook) {
            BotSocketConnectionManager.getInstance().sendPayload(message, StringUtils.isNullOrEmpty(payload) ? payload : "");
        } else {
            BotSocketConnectionManager.getInstance().stopTextToSpeech();
            addSentMessageToChat(message);
            mViewModel.sendWebHookMessage(jwt, false, StringUtils.isNullOrEmpty(payload) ? message : payload, null);
        }
    }

    @Override
    public void onSendClick(String message, ArrayList<HashMap<String, String>> attachments, boolean isFromUtterance) {
        if (attachments != null && !attachments.isEmpty()) {
            if (!SDKConfiguration.Client.isWebHook) BotSocketConnectionManager.getInstance().sendAttachmentMessage(message, attachments);
            else {
                addSentMessageToChat(message);
                mViewModel.sendWebHookMessage(jwt, false, message, attachments);
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
        botContentFragment.setQuickRepliesIntoFooter(botResponse);
        botContentFragment.showCalendarIntoFooter(botResponse);
    }

    @Override
    public void onConnectionStateChanged(BaseSocketConnectionManager.CONNECTION_STATE state, boolean isReconnection) {
        if (state == BaseSocketConnectionManager.CONNECTION_STATE.CONNECTED) {
            taskProgressBar.setVisibility(View.GONE);
            composeFooterFragment.enableSendButton();
        }
    }

    @Override
    public void onBrandingDetails(BotBrandingModel botOptionsModel, boolean isWelcomeVisible) {
        if (botOptionsModel != null) {
            setButtonBranding(botOptionsModel);

            if (botOptionsModel.getChat_bubble() != null && !StringUtils.isNullOrEmpty(botOptionsModel.getChat_bubble().getStyle())) {
                sharedPreferences.edit().putString(BundleConstants.BUBBLE_STYLE, botOptionsModel.getChat_bubble().getStyle()).apply();
            }

            if (botOptionsModel.getBody() != null && !StringUtils.isNullOrEmpty(botOptionsModel.getBody().getBubble_style())) {
                sharedPreferences.edit().putString(BundleConstants.BUBBLE_STYLE, botOptionsModel.getBody().getBubble_style()).apply();
            }

            if (botOptionsModel.getGeneral() != null && botOptionsModel.getGeneral().getColors() != null && botOptionsModel.getGeneral().getColors().isUseColorPaletteOnly()) {
                botOptionsModel.getHeader().setBg_color(botOptionsModel.getGeneral().getColors().getSecondary());
                botOptionsModel.getFooter().setBg_color(botOptionsModel.getGeneral().getColors().getSecondary());
                botOptionsModel.getFooter().getCompose_bar().setOutline_color(botOptionsModel.getGeneral().getColors().getPrimary());
                botOptionsModel.getFooter().getCompose_bar().setInline_color(botOptionsModel.getGeneral().getColors().getSecondary_text());
                botOptionsModel.getHeader().getTitle().setColor(botOptionsModel.getGeneral().getColors().getPrimary());
                botOptionsModel.getHeader().getSub_title().setColor(botOptionsModel.getGeneral().getColors().getPrimary());
            }

            if (botOptionsModel.getWelcome_screen() != null && isWelcomeVisible) {
                if (botOptionsModel.getWelcome_screen().isShow()) showWelcomeDialog(botOptionsModel);
            }

            if (botOptionsModel.getOverride_kore_config() != null && botOptionsModel.getOverride_kore_config().isEnable()) {
                SDKConfiguration.OverrideKoreConfig.isEmojiShortcutEnable = botOptionsModel.getOverride_kore_config().isEmoji_short_cut();
                SDKConfiguration.OverrideKoreConfig.typing_indicator_timeout = botOptionsModel.getOverride_kore_config().getTyping_indicator_timeout();
                if (botOptionsModel.getOverride_kore_config().getHistory() != null) {
                    SDKConfiguration.OverrideKoreConfig.history_enable = botOptionsModel.getOverride_kore_config().getHistory().isEnable();
                    if (botOptionsModel.getOverride_kore_config().getHistory().getRecent() != null)
                        SDKConfiguration.OverrideKoreConfig.history_batch_size = botOptionsModel.getOverride_kore_config().getHistory().getRecent().getBatch_size();
                    if (botOptionsModel.getOverride_kore_config().getHistory().getPaginated_scroll() != null) {
                        SDKConfiguration.OverrideKoreConfig.paginated_scroll_enable = botOptionsModel.getOverride_kore_config().getHistory().getPaginated_scroll().isEnable();
                        SDKConfiguration.OverrideKoreConfig.paginated_scroll_batch_size = botOptionsModel.getOverride_kore_config().getHistory().getPaginated_scroll().getBatch_size();
                        SDKConfiguration.OverrideKoreConfig.paginated_scroll_loading_label = botOptionsModel.getOverride_kore_config().getHistory().getPaginated_scroll().getLoading_label();
                    }
                }
            }

            if (composeFooterFragment != null) {
                composeFooterFragment.setBotBrandingModel(botOptionsModel);
            }

            if (botContentFragment != null) {
                botContentFragment.setBotBrandingModel(botOptionsModel);
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
        mViewModel.processPayload(payload, botResponse);
    }

    @Override
    public void displayMessage(String text, String type, String messageId) {
        mViewModel.displayMessage(text, type, messageId, SDKConfiguration.BubbleColors.getIcon_url());
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
    public void loadChatHistory(int _offset, int limit) {
        botContentFragment.loadChatHistory(_offset, limit);
    }

    @Override
    public void loadReconnectionChatHistory(int _offset, int limit) {
        botContentFragment.loadReconnectionChatHistory(_offset, limit);
    }

    @Override
    public void onStop() {
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
        mViewModel.setIsActivityResumed(true);
        if (!SDKConfiguration.Client.isWebHook) {
            BotSocketConnectionManager.getInstance().checkConnectionAndRetry(getApplicationContext(), false);
            updateTitleBar(BotSocketConnectionManager.getInstance().getConnection_state());
        }

        if (mViewModel != null)
            mViewModel.sendReadReceipts();

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

    private void stopTextToSpeech() {
        try {
            ttsSynthesizer.stopTextToSpeech();
        } catch (IllegalArgumentException exception) {
            exception.printStackTrace();
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
            composerFragment.setPanelComposeFooterInterface(NewBotChatActivity.this, SDKConfiguration.Client.identity);
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
        mViewModel.sendImage(fP, fN, fPT);
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
                    ToastUtils.showToast(NewBotChatActivity.this, "Unable to attach!");
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
        updateContentListOnSend(botRequest);
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

    void showWelcomeDialog(BotBrandingModel botOptionsModel) {
        RelativeLayout llHeaderLayout = null;
        LinearLayout llOuterHeader, llStartConversation, llBottomPower, llStarterLogo;
        AutoExpandListView lvPromotions;
        ConstraintLayout clStarter;
        ScrollView svWelcome;
        ImageView ivStarterLogo;

        welcomeDialog = new Dialog(this, R.style.MyDialogTheme);
        View view = LayoutInflater.from(this).inflate(R.layout.welcome_screen, null);
        llOuterHeader = view.findViewById(R.id.llOuterHeader);
        llStartConversation = view.findViewById(R.id.llStartConversation);
        lvPromotions = view.findViewById(R.id.lvPromotions);
        clStarter = view.findViewById(R.id.clStarter);
        llBottomPower = view.findViewById(R.id.llBottomPower);
        svWelcome = view.findViewById(R.id.svWelcome);
        llStarterLogo = view.findViewById(R.id.llStarterLogo);
        ivStarterLogo = view.findViewById(R.id.ivStarterLogo);

        welcomeDialog.setContentView(view);
        Objects.requireNonNull(welcomeDialog.getWindow()).setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        welcomeDialog.setCancelable(false);
        welcomeDialog.setCanceledOnTouchOutside(false);

        llStartConversation.setOnClickListener(v -> closeWelcomeDialog());

        if (botOptionsModel != null && botOptionsModel.getWelcome_screen() != null) {
            BrandingWelcomeModel welcomeModel = botOptionsModel.getWelcome_screen();

            if (!StringUtils.isNullOrEmpty(welcomeModel.getLayout())) {
                if (welcomeModel.getLayout().equalsIgnoreCase(BundleUtils.LAYOUT_LARGE)) {
                    llHeaderLayout = (RelativeLayout) View.inflate(NewBotChatActivity.this, R.layout.welcome_header_2, null);
                } else if (welcomeModel.getLayout().equalsIgnoreCase(BundleUtils.LAYOUT_MEDIUM)) {
                    llHeaderLayout = (RelativeLayout) View.inflate(NewBotChatActivity.this, R.layout.welcome_header_3, null);
                } else llHeaderLayout = (RelativeLayout) View.inflate(NewBotChatActivity.this, R.layout.welcome_header, null);
            }

            if (llHeaderLayout != null) {
                RelativeLayout rlHeader = llHeaderLayout.findViewById(R.id.rlHeader);
                TextView tvWelcomeHeader = llHeaderLayout.findViewById(R.id.tvWelcomeHeader);
                TextView tvWelcomeTitle = llHeaderLayout.findViewById(R.id.tvWelcomeTitle);
                TextView tvWelcomeDescription = llHeaderLayout.findViewById(R.id.tvWelcomeDescription);
                ImageView ivWelcomeLogo = llHeaderLayout.findViewById(R.id.ivWelcomeLogo);
                ConstraintLayout llInnerHeader = llHeaderLayout.findViewById(R.id.llInnerHeader);

                if (!StringUtils.isNullOrEmpty(welcomeModel.getTitle().getName())) {
                    tvWelcomeHeader.setText(welcomeModel.getTitle().getName());
                }

                if (!StringUtils.isNullOrEmpty(welcomeModel.getSub_title().getName()))
                    tvWelcomeTitle.setText(welcomeModel.getSub_title().getName());

                if (!StringUtils.isNullOrEmpty(welcomeModel.getNote().getName()))
                    tvWelcomeDescription.setText(welcomeModel.getNote().getName());

                if (welcomeModel.getBackground() != null) {
                    if (!StringUtils.isNullOrEmpty(welcomeModel.getBackground().getType())) {
                        if (welcomeModel.getBackground().getType().equalsIgnoreCase(BundleUtils.COLOR)) {
                            llInnerHeader.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(welcomeModel.getBackground().getColor())));
                        } else if (!StringUtils.isNullOrEmpty(welcomeModel.getBackground().getImg())) {
                            Glide.with(NewBotChatActivity.this).load(welcomeModel.getBackground().getImg()).into(new CustomTarget<Drawable>() {
                                @Override
                                public void onResourceReady(@androidx.annotation.NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                    llInnerHeader.setBackground(null);
                                    rlHeader.setBackground(resource);
                                }

                                @Override
                                public void onLoadCleared(@Nullable Drawable placeholder) {
                                }
                            });
                        }
                    }
                }

                if (welcomeModel.getLogo() != null && !StringUtils.isNullOrEmpty(welcomeModel.getLogo().getLogo_url())) {
                    Picasso.get().load(welcomeModel.getLogo().getLogo_url()).transform(new RoundedCornersTransform()).into(ivWelcomeLogo);
                }

                if (botOptionsModel.getHeader() != null && botOptionsModel.getHeader().getIcon() != null) {
                    if (botOptionsModel.getHeader().getIcon().getType().equalsIgnoreCase(BundleUtils.CUSTOM)) {
                        llStarterLogo.setBackgroundResource(0);
                        Picasso.get().load(botOptionsModel.getHeader().getIcon().getIcon_url()).transform(new RoundedCornersTransform()).into(ivStarterLogo);
                        ivStarterLogo.setLayoutParams(new LinearLayout.LayoutParams((int) (40 * dp1), (int) (40 * dp1)));
                    } else {
                        switch (botOptionsModel.getHeader().getIcon().getIcon_url()) {
                            case "icon-1":
                                ivStarterLogo.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_icon_1, getTheme()));
                                break;
                            case "icon-2":
                                ivStarterLogo.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_icon_2, getTheme()));
                                break;
                            case "icon-3":
                                ivStarterLogo.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_icon_3, getTheme()));
                                break;
                            case "icon-4":
                                ivStarterLogo.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_icon_4, getTheme()));
                                break;
                        }
                    }
                }

                if (welcomeModel.getTop_fonts() != null) {
                    tvWelcomeHeader.setTextColor(Color.parseColor(welcomeModel.getTop_fonts().getColor()));
                    tvWelcomeTitle.setTextColor(Color.parseColor(welcomeModel.getTop_fonts().getColor()));
                    tvWelcomeDescription.setTextColor(Color.parseColor(welcomeModel.getTop_fonts().getColor()));
                    ivStarterLogo.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(welcomeModel.getTop_fonts().getColor())));
                }

                if (botOptionsModel.getGeneral() != null && botOptionsModel.getGeneral().getColors() != null && botOptionsModel.getGeneral().getColors().isUseColorPaletteOnly()) {
                    tvWelcomeHeader.setTextColor(Color.parseColor(botOptionsModel.getGeneral().getColors().getSecondary_text()));
                    tvWelcomeTitle.setTextColor(Color.parseColor(botOptionsModel.getGeneral().getColors().getSecondary_text()));
                    tvWelcomeDescription.setTextColor(Color.parseColor(botOptionsModel.getGeneral().getColors().getSecondary_text()));
                    llStarterLogo.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(botOptionsModel.getGeneral().getColors().getPrimary())));
                    ivStarterLogo.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(botOptionsModel.getGeneral().getColors().getSecondary_text())));
                    llInnerHeader.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(botOptionsModel.getGeneral().getColors().getPrimary())));

                    if (!StringUtils.isNullOrEmpty(botOptionsModel.getGeneral().getColors().getSecondary()))
                        svWelcome.setBackgroundColor(Color.parseColor(botOptionsModel.getGeneral().getColors().getSecondary()));
                }

                if (welcomeModel.getBottom_background() != null && !StringUtils.isNullOrEmpty(welcomeModel.getBottom_background().getColor())) {
                    llBottomPower.setBackgroundColor(Color.parseColor(welcomeModel.getBottom_background().getColor()));
                }
            }

            RecyclerView rvStarterButtons = view.findViewById(R.id.rvStarterButtons);
            HeightAdjustableViewPager hvpLinks = view.findViewById(R.id.hvpLinks);
            RecyclerView rvLinks = view.findViewById(R.id.rvLinks);
            TextView tvStarterTitle = view.findViewById(R.id.tvStarterTitle);
            TextView tvStarterDesc = view.findViewById(R.id.tvStarterDesc);
            TextView tvStartConversation = view.findViewById(R.id.tvStartConversation);
            RelativeLayout rlLinks = view.findViewById(R.id.rlLinks);

            rvLinks.setLayoutManager(new LinearLayoutManager(NewBotChatActivity.this, LinearLayoutManager.VERTICAL, false));

            llOuterHeader.addView(llHeaderLayout);

            if (welcomeModel.getStarter_box() != null) {
                if (welcomeModel.getStarter_box().isShow()) {
                    clStarter.setVisibility(View.VISIBLE);

                    if (!StringUtils.isNullOrEmpty(welcomeModel.getStarter_box().getTitle())) {
                        tvStarterTitle.setVisibility(View.VISIBLE);

                        tvStarterTitle.setText(welcomeModel.getStarter_box().getTitle());
                        tvStartConversation.setText(welcomeModel.getStarter_box().getTitle());
                    }

                    if (!StringUtils.isNullOrEmpty(welcomeModel.getStarter_box().getSub_text())) {
                        tvStarterDesc.setVisibility(View.VISIBLE);
                        tvStarterDesc.setText(welcomeModel.getStarter_box().getSub_text());
                    }

                    if (welcomeModel.getStarter_box().getStart_conv_button() != null) {
                        if (!StringUtils.isNullOrEmpty(welcomeModel.getStarter_box().getStart_conv_button().getColor())) {
                            StateListDrawable gradientDrawable = (StateListDrawable) llStartConversation.getBackground();
                            gradientDrawable.setTint(Color.parseColor(welcomeModel.getStarter_box().getStart_conv_button().getColor()));
                            if (botOptionsModel.getGeneral() != null && botOptionsModel.getGeneral().getColors() != null && botOptionsModel.getGeneral().getColors().isUseColorPaletteOnly()) {
                                gradientDrawable.setTint(Color.parseColor(botOptionsModel.getGeneral().getColors().getPrimary()));
                            }

                            llStartConversation.setBackground(gradientDrawable);
                        }
                    }

                    if (welcomeModel.getStarter_box().getStart_conv_text() != null) {
                        if (!StringUtils.isNullOrEmpty(welcomeModel.getStarter_box().getStart_conv_text().getColor())) {
                            tvStartConversation.setTextColor(Color.parseColor(welcomeModel.getStarter_box().getStart_conv_text().getColor()));

                            if (botOptionsModel.getGeneral() != null && botOptionsModel.getGeneral().getColors() != null && botOptionsModel.getGeneral().getColors().isUseColorPaletteOnly() && !StringUtils.isNullOrEmpty(botOptionsModel.getGeneral().getColors().getSecondary_text())) {
                                tvStartConversation.setTextColor(Color.parseColor(botOptionsModel.getGeneral().getColors().getSecondary_text()));
                            }
                        }
                    }

                    if (welcomeModel.getStarter_box().getQuick_start_buttons() != null && welcomeModel.getStarter_box().getQuick_start_buttons().getButtons() != null && welcomeModel.getStarter_box().getQuick_start_buttons().getButtons().size() > 0) {
                        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(NewBotChatActivity.this);

                        if (!StringUtils.isNullOrEmpty(welcomeModel.getStarter_box().getQuick_start_buttons().getStyle())) {
                            if (welcomeModel.getStarter_box().getQuick_start_buttons().getStyle().equalsIgnoreCase(BotResponse.TEMPLATE_TYPE_LIST)) {
                                layoutManager.setFlexDirection(FlexDirection.COLUMN);
                                layoutManager.setJustifyContent(JustifyContent.FLEX_START);
                                rvStarterButtons.setLayoutManager(layoutManager);

                                WelcomeStarterButtonsAdapter quickRepliesAdapter = new WelcomeStarterButtonsAdapter(NewBotChatActivity.this, BotResponse.TEMPLATE_TYPE_LIST, !StringUtils.isNullOrEmpty(botOptionsModel.getGeneral().getColors().getSecondary()) ? botOptionsModel.getGeneral().getColors().getSecondary() : "#a7b0be");
                                quickRepliesAdapter.setWelcomeStarterButtonsArrayList(welcomeModel.getStarter_box().getQuick_start_buttons().getButtons());
                                rvStarterButtons.setAdapter(quickRepliesAdapter);
                            } else {
                                layoutManager.setFlexDirection(FlexDirection.ROW);
                                layoutManager.setJustifyContent(JustifyContent.FLEX_START);
                                rvStarterButtons.setLayoutManager(layoutManager);

                                WelcomeStarterButtonsAdapter quickRepliesAdapter = new WelcomeStarterButtonsAdapter(NewBotChatActivity.this, BotResponse.TEMPLATE_TYPE_CAROUSEL, (!StringUtils.isNullOrEmpty(botOptionsModel.getGeneral().getColors().getSecondary()) && botOptionsModel.getGeneral().getColors().isUseColorPaletteOnly()) ? botOptionsModel.getGeneral().getColors().getSecondary() : "#a7b0be");
                                quickRepliesAdapter.setWelcomeStarterButtonsArrayList(welcomeModel.getStarter_box().getQuick_start_buttons().getButtons());
                                quickRepliesAdapter.setComposeFooterInterface(NewBotChatActivity.this);
                                quickRepliesAdapter.setInvokeGenericWebViewInterface(NewBotChatActivity.this);
                                rvStarterButtons.setAdapter(quickRepliesAdapter);
                            }
                        }
                    }
                }
            }

            if (welcomeModel.getPromotional_content().getPromotions() != null && welcomeModel.getPromotional_content().isShow() && welcomeModel.getPromotional_content().getPromotions().size() > 0) {
                lvPromotions.setVisibility(VISIBLE);
                lvPromotions.setAdapter(new PromotionsAdapter(NewBotChatActivity.this, welcomeModel.getPromotional_content().getPromotions()));
            }

            if (welcomeModel.getStatic_links() != null) {
                if (welcomeModel.getStarter_box().isShow()) {
                    if (welcomeModel.getStatic_links().getLinks() != null && !welcomeModel.getStatic_links().getLinks().isEmpty()) {
                        rlLinks.setVisibility(View.VISIBLE);

                        if (!StringUtils.isNullOrEmpty(welcomeModel.getStatic_links().getLayout()) && welcomeModel.getStatic_links().getLayout().equalsIgnoreCase(BotResponse.TEMPLATE_TYPE_CAROUSEL)) {
                            hvpLinks.setVisibility(View.VISIBLE);
                            WelcomeStaticLinksAdapter quickRepliesAdapter = new WelcomeStaticLinksAdapter(NewBotChatActivity.this, welcomeModel.getStatic_links().getLinks(), (!StringUtils.isNullOrEmpty(botOptionsModel.getGeneral().getColors().getSecondary()) && botOptionsModel.getGeneral().getColors().isUseColorPaletteOnly()) ? botOptionsModel.getGeneral().getColors().getSecondary() : "#a7b0be");
                            quickRepliesAdapter.setComposeFooterInterface(NewBotChatActivity.this);
                            quickRepliesAdapter.setInvokeGenericWebViewInterface(NewBotChatActivity.this);
                            hvpLinks.setAdapter(quickRepliesAdapter);
                        } else {
                            rvLinks.setVisibility(View.VISIBLE);
                            WelcomeStaticLinkListAdapter welcomeStaticLinkListAdapter = new WelcomeStaticLinkListAdapter(NewBotChatActivity.this, rvLinks);
                            welcomeStaticLinkListAdapter.setWelcomeStaticLinksArrayList(welcomeModel.getStatic_links().getLinks());
                            welcomeStaticLinkListAdapter.setComposeFooterInterface(NewBotChatActivity.this);
                            welcomeStaticLinkListAdapter.setInvokeGenericWebViewInterface(NewBotChatActivity.this);
                            rvLinks.setAdapter(welcomeStaticLinkListAdapter);
                        }
                    } else rlLinks.setVisibility(View.GONE);
                }
            }
        }

        welcomeDialog.show();
        rlChatWindow.setVisibility(VISIBLE);
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
        alertDialog = new Dialog(NewBotChatActivity.this);
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
                botClient.sendMessage(gson.toJsonTree(eventModel.getMessage()));

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
                    ACManager.getInstance().startLogin(NewBotChatActivity.this, false, true);
                }

                if (!ACManager.getInstance().isRegisterState() && Prefs.getAutoLogin(NewBotChatActivity.this)) {
                    Toast.makeText(NewBotChatActivity.this, R.string.no_registration, Toast.LENGTH_SHORT).show();
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

        AlertDialog.Builder builder = new AlertDialog.Builder(NewBotChatActivity.this);
        builder.setMessage(R.string.bot_not_connected).setCancelable(false).setPositiveButton(R.string.ok, dialogClickListener).show();
    }

    @Override
    public void getBrandingDetails() {
    }

    @Override
    public void addAttachmentToAdapter(HashMap<String, String> attachmentKey) {
    }

    @Override
    public void uploadBulkFile(String fileName, String filePath, String extn, String filePathThumbnail, String orientation) {
        if (!SDKConfiguration.Client.isWebHook) {
            KoreWorker.getInstance().addTask(new UploadBulkFile(fileName, filePath, "bearer " + SocketWrapper.getInstance(NewBotChatActivity.this).getAccessToken(), SocketWrapper.getInstance(NewBotChatActivity.this).getBotUserId(), "workflows", extn, KoreMedia.BUFFER_SIZE_IMAGE, new Messenger(messagesMediaUploadAcknowledgeHandler), filePathThumbnail, "AT_" + System.currentTimeMillis(), NewBotChatActivity.this, BitmapUtils.obtainMediaTypeOfExtn(extn), (!SDKConfiguration.Client.isWebHook ? SDKConfiguration.Server.SERVER_URL : SDKConfiguration.Server.SERVER_URL), orientation, true, SDKConfiguration.Client.isWebHook, SDKConfiguration.Client.bot_id));
        } else {
            KoreWorker.getInstance().addTask(new UploadBulkFile(fileName, filePath, "bearer " + jwt, SocketWrapper.getInstance(NewBotChatActivity.this).getBotUserId(), "workflows", extn, KoreMedia.BUFFER_SIZE_IMAGE, new Messenger(messagesMediaUploadAcknowledgeHandler), filePathThumbnail, "AT_" + System.currentTimeMillis(), NewBotChatActivity.this, BitmapUtils.obtainMediaTypeOfExtn(extn), (!SDKConfiguration.Client.isWebHook ? SDKConfiguration.Server.SERVER_URL : SDKConfiguration.Server.SERVER_URL), orientation, true, SDKConfiguration.Client.isWebHook, SDKConfiguration.Client.bot_id));
        }
    }
}
