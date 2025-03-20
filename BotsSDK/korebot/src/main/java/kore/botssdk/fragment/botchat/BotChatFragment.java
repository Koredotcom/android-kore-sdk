package kore.botssdk.fragment.botchat;

import static android.content.Context.ACTIVITY_SERVICE;
import static android.content.Context.RECEIVER_NOT_EXPORTED;

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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import kore.botssdk.R;
import kore.botssdk.activity.GenericWebViewActivity;
import kore.botssdk.bot.BotClient;
import kore.botssdk.event.KoreEventCenter;
import kore.botssdk.fragment.content.BaseContentFragment;
import kore.botssdk.fragment.content.NewBotContentFragment;
import kore.botssdk.fragment.footer.BaseFooterFragment;
import kore.botssdk.fragment.footer.ComposeFooterFragment;
import kore.botssdk.fragment.header.BaseHeaderFragment;
import kore.botssdk.fragment.header.BotHeaderFragment;
import kore.botssdk.listener.BotChatCloseListener;
import kore.botssdk.listener.BaseSocketConnectionManager;
import kore.botssdk.listener.BotChatViewListener;
import kore.botssdk.listener.BotSocketConnectionManager;
import kore.botssdk.listener.ComposeFooterInterface;
import kore.botssdk.listener.InvokeGenericWebViewInterface;
import kore.botssdk.models.BotRequest;
import kore.botssdk.models.BotResponse;
import kore.botssdk.models.BrandingModel;
import kore.botssdk.models.FormActionTemplate;
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
import kore.botssdk.utils.StringUtils;
import kore.botssdk.viewmodels.chat.BotChatViewModel;
import kore.botssdk.viewmodels.chat.BotChatViewModelFactory;

@SuppressWarnings("UnKnownNullness")
public class BotChatFragment extends Fragment implements BotChatViewListener, ComposeFooterInterface, InvokeGenericWebViewInterface {
    private ProgressBar taskProgressBar;
    private String jwt;
    BotClient botClient;
    private BaseHeaderFragment botHeaderFragment;
    BaseContentFragment botContentFragment;
    private BaseFooterFragment baseFooterFragment;
    SharedPreferences sharedPreferences;
    private BotChatViewModel mViewModel;
    boolean isAgentTransfer;
    private BotChatFragmentListener fragmentListener;
    BotChatCloseListener activityCloseListener;

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
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bot_chat_layout, null);
        botClient = new BotClient(requireContext());

        BotChatViewModelFactory factory = new BotChatViewModelFactory(requireContext(), botClient, BotChatFragment.this);
        mViewModel = new ViewModelProvider(this, factory).get(BotChatViewModel.class);

        findViews(view);
        getBundleInfo();

        mViewModel.connectToBot(sharedPreferences.getBoolean(BundleConstants.IS_RECONNECT, false));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requireContext().registerReceiver(onDestroyReceiver, new IntentFilter(BundleConstants.DESTROY_EVENT), RECEIVER_NOT_EXPORTED);
        } else {
            requireContext().registerReceiver(onDestroyReceiver, new IntentFilter(BundleConstants.DESTROY_EVENT));
        }

        requireContext().startService(new Intent(requireContext(), ClosingService.class));
        return view;
    }

    private void getBundleInfo() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            jwt = bundle.getString(BundleUtils.JWT_TKN, "");
        }
    }

    private void findViews(View view) {
        taskProgressBar = view.findViewById(R.id.taskProgressBar);
        sharedPreferences = requireContext().getSharedPreferences(BotResponse.THEME_NAME, Context.MODE_PRIVATE);
        RestBuilder.setContext(requireContext());
        WebHookRestBuilder.setContext(requireContext());
        BrandingRestBuilder.setContext(requireContext());

        if (SDKConfig.isShowHeader()) {
            botHeaderFragment = SDKConfig.getCustomHeaderFragment();
            if (botHeaderFragment == null) botHeaderFragment = new BotHeaderFragment();
            botHeaderFragment.setComposeFooterInterface(this);
            botHeaderFragment.setInvokeGenericWebViewInterface(this);
            FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
            transaction.add(R.id.header_container, botHeaderFragment);
            transaction.commitNow();
        }

        FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
        //Add Bot Content Fragment
        BaseContentFragment customContentFragment = SDKConfig.getCustomContentFragment();
        botContentFragment = customContentFragment != null ? customContentFragment : new NewBotContentFragment();
        botContentFragment.setArguments(getArguments());
        botContentFragment.setComposeFooterInterface(this);
        botContentFragment.setInvokeGenericWebViewInterface(this);
        fragmentTransaction.add(R.id.chatLayoutContentContainer, botContentFragment).commit();

        //Add Bot Compose Footer Fragment
        fragmentTransaction = getChildFragmentManager().beginTransaction();
        BaseFooterFragment customFooterFragment = SDKConfig.getCustomFooterFragment();
        baseFooterFragment = customFooterFragment != null ? customFooterFragment : new ComposeFooterFragment();
        baseFooterFragment.setArguments(getArguments());
        baseFooterFragment.setComposeFooterInterface(this);
        baseFooterFragment.setBottomOptionData(mViewModel.getDataFromTxt());
        baseFooterFragment.setBotClient(botClient);
        fragmentTransaction.add(R.id.chatLayoutFooterContainer, baseFooterFragment).commit();

        setupTextToSpeech();
        KoreEventCenter.register(this);
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
            if (botContentFragment != null)
                botContentFragment.changeThemeBackGround(brandingModel.getWidgetBodyColor(), brandingModel.getWidgetHeaderColor(), brandingModel.getWidgetTextColor(), brandingModel.getBotName());

            if (baseFooterFragment != null)
                baseFooterFragment.changeThemeBackGround(brandingModel.getWidgetFooterColor(), brandingModel.getWidgetFooterHintColor());

            if (botHeaderFragment != null)
                botHeaderFragment.setBrandingDetails(brandingModel);
        }

        loadOnConnectionHistory(false);
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

    public void setActivityCloseListener(BotChatCloseListener activityCloseListener) {
        this.activityCloseListener = activityCloseListener;
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
                taskProgressBar.setVisibility(View.VISIBLE);
                break;
            case CONNECTED: {
                taskProgressBar.setVisibility(View.GONE);
                baseFooterFragment.enableSendButton();
            }
            break;
            case DISCONNECTED:
            case CONNECTED_BUT_DISCONNECTED: {
                taskProgressBar.setVisibility(View.VISIBLE);
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
                if (fragmentListener != null) fragmentListener.onReconnectionAttemptExceed();
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setMessage(R.string.bot_not_connected).setCancelable(false).setPositiveButton(R.string.ok, dialogClickListener).show();
    }

    @Override
    public void getBrandingDetails() {
        mViewModel.getBrandingDetails(SDKConfiguration.Client.bot_id, jwt, "published", "1", "en_US");
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
    public void addAttachmentToAdapter(HashMap<String, String> attachmentKey) {
        baseFooterFragment.addAttachmentToAdapter(attachmentKey);
    }

    @Override
    public void uploadBulkFile(String fileName, String filePath, String extn, String filePathThumbnail, String orientation) {
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
    public void onFormActionButtonClicked(FormActionTemplate fTemplate) {
    }

    @Override
    public void copyMessageToComposer(String text, boolean isForOnboard) {
        baseFooterFragment.setComposeText(text);
    }

    @Override
    public void sendImage(String fP, String fN, String fPT) {
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
            Intent intent = new Intent(requireContext(), GenericWebViewActivity.class);
            intent.putExtra("url", url);
            intent.putExtra("header", getResources().getString(R.string.app_name));
            startActivity(intent);
        }
    }

    @Override
    public void handleUserActions(String payload, HashMap<String, Object> type) {
    }

    public SharedPreferences getSharedPreferences() {
        return sharedPreferences;
    }

    @Override
    public void onDestroy() {
        if (isAgentTransfer && botClient != null)
            botClient.sendAgentCloseMessage("", SDKConfiguration.Client.bot_name, SDKConfiguration.Client.bot_id);

        new PushNotificationRegister().unsubscribePushNotification(botClient.getUserId(), botClient.getAccessToken(), sharedPreferences.getString("PREF_UNIQUE_ID", mViewModel.getUniqueID()));

        if (botClient != null) botClient.disconnect();
        KoreEventCenter.unregister(this);
        super.onDestroy();
    }

    @Override
    public void onResume() {
        mViewModel.setIsActivityResumed(true);

        if (!SDKConfiguration.Client.isWebHook) {
            BotSocketConnectionManager.getInstance().checkConnectionAndRetry(requireContext(), false);
            updateTitleBar(BotSocketConnectionManager.getInstance().getConnection_state());
        }

        mViewModel.sendReadReceipts();
        super.onResume();
    }

    public void showCloseAlert() {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_NEUTRAL:
                        dialog.dismiss();
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        if (sharedPreferences != null) {
                            if (botClient != null && isAgentTransfer) {
                                botClient.sendAgentCloseMessage("", SDKConfiguration.Client.bot_name, SDKConfiguration.Client.bot_id);
                            }

                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putBoolean("IS_RECONNECT", false);
                            editor.putInt("HISTORY_COUNT", 0);
                            editor.apply();
                            BotSocketConnectionManager.killInstance();
                            activityCloseListener.onChatBotClosed();
                        }
                        break;
                    case DialogInterface.BUTTON_POSITIVE:
                        if (sharedPreferences != null) {
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putBoolean("IS_RECONNECT", true);
                            editor.putInt("HISTORY_COUNT", botContentFragment.getAdapterCount());
                            editor.apply();
                            BotSocketConnectionManager.killInstance();
                            activityCloseListener.onChatBotMinimized();
                        }
                }

            }
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setMessage(R.string.close_or_minimize).setCancelable(false).setPositiveButton(R.string.minimize, dialogClickListener).setNegativeButton(R.string.close, dialogClickListener).setNeutralButton(R.string.cancel, dialogClickListener).show();
    }

    @Override
    public void onStop() {
        mViewModel.setIsActivityResumed(false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityManager activityManager = (ActivityManager) requireContext().getSystemService(ACTIVITY_SERVICE);
            List<ActivityManager.AppTask> taskList = activityManager.getAppTasks();

            if (!taskList.isEmpty() && taskList.get(0) != null && taskList.get(0).getTaskInfo() != null && taskList.get(0).getTaskInfo().topActivity != null) {
                String topClassName = Objects.requireNonNull(taskList.get(0).getTaskInfo().topActivity).toString();
                if (!topClassName.contains(requireContext().getPackageName())) {

                    if (botClient != null) {
                        botClient.sendAgentCloseMessage("", SDKConfiguration.Client.bot_name, SDKConfiguration.Client.bot_id);

                        LogUtils.e("onStop", "onStop called");

                        SharedPreferences.Editor prefsEditor = requireContext().getSharedPreferences(BotResponse.THEME_NAME, Context.MODE_PRIVATE).edit();
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

    public void setListener(BotChatFragmentListener listener) {
        fragmentListener = listener;
    }
}
