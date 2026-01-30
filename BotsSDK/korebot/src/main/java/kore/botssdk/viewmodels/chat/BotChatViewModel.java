package kore.botssdk.viewmodels.chat;

import static kore.botssdk.net.SDKConfiguration.Client.enable_ack_delivery;
import static kore.botssdk.utils.BundleConstants.GROUP_KEY_NOTIFICATIONS;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.core.app.NotificationCompat;
import androidx.lifecycle.ViewModel;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

import kore.botssdk.R;
import kore.botssdk.activity.NewBotChatActivity;
import kore.botssdk.bot.BotClient;
import kore.botssdk.events.SocketDataTransferModel;
import kore.botssdk.listener.BaseSocketConnectionManager;
import kore.botssdk.listener.BotChatViewListener;
import kore.botssdk.listener.BotSocketConnectionManager;
import kore.botssdk.listener.SocketChatListener;
import kore.botssdk.models.AgentInfoModel;
import kore.botssdk.models.BaseBotMessage;
import kore.botssdk.models.BotInfoModel;
import kore.botssdk.models.BotMetaModel;
import kore.botssdk.models.BotOptionsModel;
import kore.botssdk.models.BotRequest;
import kore.botssdk.models.BotResponse;
import kore.botssdk.models.BotResponseMessage;
import kore.botssdk.models.BotResponsePayLoadText;
import kore.botssdk.models.ComponentModel;
import kore.botssdk.models.ComponentModelPayloadText;
import kore.botssdk.models.PayloadInner;
import kore.botssdk.models.PayloadOuter;
import kore.botssdk.net.RestResponse;
import kore.botssdk.net.SDKConfiguration;
import kore.botssdk.pushnotification.PushNotificationRegister;
import kore.botssdk.repository.branding.BrandingRepository;
import kore.botssdk.repository.webhook.WebHookRepository;
import kore.botssdk.utils.BundleConstants;
import kore.botssdk.utils.BundleUtils;
import kore.botssdk.utils.DateUtils;
import kore.botssdk.utils.LogUtils;
import kore.botssdk.utils.StringUtils;
import kore.botssdk.websocket.SocketWrapper;

@SuppressWarnings("UnKnownNullness")
public class BotChatViewModel extends ViewModel {
    private static final String LOG_TAG = "NewBotChatActivity";
    private static final String TAG = BotChatViewModel.class.getName();
    Context context;
    Gson gson = new Gson();
    BotClient botClient;
    boolean isReconnectionStopped = false;
    BrandingRepository repository;
    WebHookRepository webHookRepository;
    BotChatViewListener chatView;
    BotMetaModel botMetaModel;
    String lastMsgId = "";
    boolean isAgentTransfer = false;
    ArrayList<String> arrMessageList = new ArrayList<>();
    private static String uniqueID = null;
    private static final String PREF_UNIQUE_ID = "PREF_UNIQUE_ID";
    private boolean isActivityResumed = false;
    private static final String START_TIMER = "start_timer";
    private static final String AGENT_EVENT_CONNECTED = "agent_connected";
    private static final String AGENT_EVENT_DISCONNECTED = "agent_disconnected";
    private static final String NOTIFICATION_TAG_CHAT_MESSAGE = "ChatMessageNotification";
    private static final int NOTIFICATION_TAG_NUMBER = 237891;
    private static final String KORE_PUSH_SERVICE = "Kore_Push_Service";
    private static final String KORE_ANDROID = "Kore_Android";
    private static final String NOTIFICATION = "Notification";
    public BotChatViewModel(Context context, BotClient botClient, BotChatViewListener chatView) {
        this.context = context.getApplicationContext();
        this.repository = new BrandingRepository(context, chatView);
        this.chatView = chatView;
        this.webHookRepository = new WebHookRepository(context, chatView);
        this.botClient = botClient;
    }

    public void getBrandingDetails(String botId, String botToken, String state, String version, String language) {
        repository.getBrandingDetails(botId, botToken, state, version, language);
    }

    public void setIsActivityResumed(boolean isResumed) {
        isActivityResumed = isResumed;
    }

    public BotOptionsModel getDataFromTxt() {
        BotOptionsModel botOptionsModel = null;

        try {
            InputStream is = context.getResources().openRawResource(R.raw.option);
            Reader reader = new InputStreamReader(is);
            botOptionsModel = gson.fromJson(reader, BotOptionsModel.class);
            LogUtils.e("Options Size", String.valueOf(botOptionsModel.getTasks().size()));
        } catch (Exception e) {
            LogUtils.e("Options Size", String.valueOf(e));
        }
        return botOptionsModel;
    }

    public void connectToBot(boolean isReconnect) {
        if (!SDKConfiguration.Client.isWebHook) {
            BotSocketConnectionManager.getInstance().setChatListener(sListener);
        }
        BotSocketConnectionManager.getInstance().startAndInitiateConnectionWithReconnect(context, SDKConfiguration.Server.customData, isReconnect);
    }

    final SocketChatListener sListener = new SocketChatListener() {
        @Override
        public void onMessage(BotResponse botResponse) {
            processPayload("", botResponse);
        }

        @Override
        public void onConnectionStateChanged(BaseSocketConnectionManager.CONNECTION_STATE state, boolean isReconnection) {
            if (state == BaseSocketConnectionManager.CONNECTION_STATE.CONNECTED) {
                chatView.onConnectionStateChanged(state, isReconnection);
                isReconnectionStopped = false;
                chatView.loadOnConnectionHistory(isReconnection);
                new PushNotificationRegister().registerPushNotification(botClient.getUserId(), botClient.getAccessToken(), getUniqueDeviceId(context));
            } else if (state == BaseSocketConnectionManager.CONNECTION_STATE.RECONNECTION_STOPPED) {
                if (!isReconnectionStopped) {
                    isReconnectionStopped = true;
                    chatView.showReconnectionStopped();
                }
            }

            chatView.updateTitleBar(state);
        }

        @Override
        public void onMessage(SocketDataTransferModel data) {
            if (data == null) return;
            if (data.getEvent_type().equals(BaseSocketConnectionManager.EVENT_TYPE.TYPE_TEXT_MESSAGE)) {
                processPayload(data.getPayLoad(), null);

            } else if (data.getEvent_type().equals(BaseSocketConnectionManager.EVENT_TYPE.TYPE_MESSAGE_UPDATE)) {
                chatView.updateContentListOnSend(data.getBotRequest());
            }
        }

        @Override
        public void onStartCompleted(boolean isReconnect) {
            getBrandingDetails(SDKConfiguration.Client.bot_id, SocketWrapper.getInstance(context).getAccessToken(), "published", "1", "en_US");
        }

    };

    public void sendReadReceipts() {
        //Added newly for send receipts
        if (botClient != null && !arrMessageList.isEmpty() && isAgentTransfer) {
            botClient.sendReceipts(BundleConstants.MESSAGE_READ, arrMessageList.get((arrMessageList.size() - 1)));
            arrMessageList = new ArrayList<>();
        }
    }


    private boolean isJson(String text) {
        if (text == null || text.trim().isEmpty()) {
            return false;
        }
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readTree(text) != null;
        } catch (Exception e) {
            return false;
        }
    }


    /**
     * payload processing
     */
    public void processPayload(String payload, BotResponse botLocalResponse) {
        if (botLocalResponse == null) BotSocketConnectionManager.getInstance().stopDelayMsgTimer();
        try {
            final BotResponse botResponse = botLocalResponse != null ? botLocalResponse : gson.fromJson(payload, BotResponse.class);
            if (botResponse == null || botResponse.getMessage() == null || botResponse.getMessage().isEmpty()) {
                return;
            }
            if (botResponse.getMessageId() != null) lastMsgId = botResponse.getMessageId();

            try {
                long timeMillis = botResponse.getTimestamp() == 0L ? botResponse.getTimeInMillis(botResponse.getCreatedOn(), true) : botResponse.getTimestamp();
                botResponse.setCreatedInMillis(timeMillis);
                botResponse.setFormattedDate(DateUtils.formattedSentDateV6(timeMillis));
                botResponse.setTimeStamp(botResponse.prepareTimeStamp(timeMillis));
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }

            if (botClient != null && enable_ack_delivery)
                botClient.sendMsgAcknowledgement(botResponse.getTimestamp() + "", botResponse.getKey());

            LogUtils.d(LOG_TAG, payload);
            isAgentTransfer = botResponse.isFromAgent();

            if (!StringUtils.isNullOrEmpty(botResponse.getIcon())) SDKConfiguration.BubbleColors.setIcon_url(botResponse.getIcon());

            chatView.setIsAgentConnected(isAgentTransfer);

            if (botClient != null && isAgentTransfer) {
                botClient.sendReceipts(BundleConstants.MESSAGE_DELIVERED, botResponse.getMessageId());
                if (isActivityResumed) {
                    botClient.sendReceipts(BundleConstants.MESSAGE_READ, botResponse.getMessageId());
                } else {
                    arrMessageList.add(botResponse.getMessageId());
                }
            }

            chatView.showTypingStatus();

            PayloadOuter payOuter = null;
            if (!botResponse.getMessage().isEmpty()) {
                ComponentModel compModel = botResponse.getMessage().get(0).getComponent();
                if (compModel != null) {
                    payOuter = compModel.getPayload();
                    if (payOuter != null) {
                        if (payOuter.getText() != null && payOuter.getText().contains("&quot")) {
                            gson = new Gson();
                            payOuter = gson.fromJson(payOuter.getText().replace("&quot;", "\""), PayloadOuter.class);
                        }
                    }
                }
            }
            final PayloadInner payloadInner = payOuter == null ? null : payOuter.getPayload();
            if (payloadInner != null && payloadInner.getTemplate_type() != null && START_TIMER.equalsIgnoreCase(payloadInner.getTemplate_type())) {
                BotSocketConnectionManager.getInstance().startDelayMsgTimer();
            }

            chatView.showTypingStatus();

            if (payloadInner != null) {
                payloadInner.convertElementToAppropriate();
                chatView.addMessageToAdapter(botResponse);
            } else if (!getMessageText(botResponse).isBlank()) {
                chatView.addMessageToAdapter(botResponse);
            } else chatView.stopTypingStatus();

            if (!isActivityResumed) {
                postNotification("Kore Message", "Received new message.");
            }

        } catch (Exception e) {
            LogUtils.e(TAG, "Failed to complete risky operation" + e);
            if (e instanceof JsonSyntaxException) {
                try {
                    //This is the case Bot returning user sent message from another channel
                    BotRequest botRequest = gson.fromJson(payload, BotRequest.class);
                    if (botRequest != null && botRequest.getMessage() != null && !StringUtils.isNullOrEmpty(botRequest.getMessage().getBody())) {
                        if (!StringUtils.isNullOrEmpty(botRequest.getMessage().getRenderMsg())) {
                            botRequest.getMessage().setBody(botRequest.getMessage().getRenderMsg());
                        }
                        botRequest.setCreatedOn(DateUtils.isoFormatter.format(new Date()));
                        chatView.updateContentListOnSend(botRequest);
                    } else {
                        final AgentInfoModel botResponse = gson.fromJson(payload, AgentInfoModel.class);

                        if (botResponse == null || botResponse.getMessage() == null || StringUtils.isNullOrEmpty(botResponse.getMessage().getType())) {
                            return;
                        }

                        if (botResponse.getMessage().getType().equalsIgnoreCase(AGENT_EVENT_CONNECTED)) {
                            setPreferenceObject(botResponse.getMessage().getAgentInfo(), BotResponse.AGENT_INFO_KEY);
                        } else if (botResponse.getMessage().getType().equalsIgnoreCase(AGENT_EVENT_DISCONNECTED)) {
                            setPreferenceObject("", BotResponse.AGENT_INFO_KEY);
                        }

                        if (botResponse.getCustomEvent().equalsIgnoreCase(BotResponse.EVENT)) {
                            if (botResponse.getMessage() != null && !StringUtils.isNullOrEmpty(botResponse.getMessage().getType()) && botResponse.getMessage().getType().equalsIgnoreCase(BundleConstants.TYPING))
                                chatView.showTypingStatus();
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
                        LogUtils.e("Exception", String.valueOf(e2));
                    }
                }
            }
        }
    }

    private String getMessageText(BaseBotMessage baseBotMessage) {
        ComponentModel componentModel = getComponentModel(baseBotMessage);
        String message = "";

        if(componentModel != null)
        {
            String compType = componentModel.getType();
            PayloadOuter payOuter = componentModel.getPayload();

            if (BotResponse.COMPONENT_TYPE_TEXT.equalsIgnoreCase(compType)) {
                message = payOuter.getText();
            } else if (BotResponse.COMPONENT_TYPE_ERROR.equalsIgnoreCase(payOuter.getType())) {
                message = payOuter.getPayload().getText();
            } else if (payOuter.getType() != null && payOuter.getType().equals(BotResponse.COMPONENT_TYPE_TEXT)) {
                message = payOuter.getText();
            }
            PayloadInner payInner;
            if (payOuter.getText() != null) {
                if (payOuter.getText().contains("&quot"))
                    message = payOuter.getText().replace("&quot;", "\"");
                else message = payOuter.getText();
            }
            payInner = payOuter.getPayload();
            if (payInner != null && !StringUtils.isNullOrEmptyWithTrim(payInner.getText())) {
                message = payInner.getText();
            } else if (payInner != null && !StringUtils.isNullOrEmptyWithTrim(payInner.getText_message()))
                message = payInner.getText_message();
            else if (payInner != null && !StringUtils.isNullOrEmptyWithTrim(payInner.getTitle()))
                message = payInner.getTitle();
            else if (payInner != null && !StringUtils.isNullOrEmptyWithTrim(payInner.getHeading()))
                message = payInner.getHeading();
            else if (payInner != null && !StringUtils.isNullOrEmptyWithTrim(payInner.getTemplate_type())) {
                message = payInner.getTemplate_type();
            } else if (StringUtils.isNullOrEmptyWithTrim(payOuter.getText()) && payOuter.getType() != null) {
                message = payOuter.getType();
            }
        }

        return message;
    }

    protected ComponentModel getComponentModel(BaseBotMessage baseBotMessage) {
        ComponentModel compModel = null;
        if (baseBotMessage instanceof BotResponse && ((BotResponse) baseBotMessage).getMessage() != null && !((BotResponse) baseBotMessage).getMessage().isEmpty()) {
            compModel = ((BotResponse) baseBotMessage).getMessage().get(0).getComponent();
        }
        return compModel;
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

    public String getUniqueID() {
        return uniqueID;
    }

    public void addSentMessageToChat(String message) {
        //Update the bot content list with the send message
        RestResponse.BotMessage botMessage = new RestResponse.BotMessage(message, "");
        RestResponse.BotPayLoad botPayLoad = new RestResponse.BotPayLoad();
        botPayLoad.setMessage(botMessage);
        BotInfoModel botInfo = new BotInfoModel(SDKConfiguration.Client.bot_name, SDKConfiguration.Client.bot_id, null);
        botPayLoad.setBotInfo(botInfo);
        gson = new Gson();
        String jsonPayload = gson.toJson(botPayLoad);

        BotRequest botRequest = gson.fromJson(jsonPayload, BotRequest.class);
        botRequest.setCreatedOn(DateUtils.isoFormatter.format(new Date()));
        sListener.onMessage(new SocketDataTransferModel(BaseSocketConnectionManager.EVENT_TYPE.TYPE_MESSAGE_UPDATE, message, botRequest, false));
    }

    public void textToSpeech(BotResponse botResponse, boolean isTTSEnabled) {
        if (isTTSEnabled && botResponse.getMessage() != null && !botResponse.getMessage().isEmpty()) {
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
                        gson = new Gson();
                        payOuter = gson.fromJson(payOuter.getText().replace("&quot;", "\""), PayloadOuter.class);
                    }
                    payInner = payOuter.getPayload();

                    if (payInner.getSpeech_hint() != null) {
                        botResponseTextualFormat = payInner.getSpeech_hint();
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

    public void postNotification(String title, String pushMessage) {
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder nBuilder;
        if (Build.VERSION.SDK_INT >= 26) {
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel notificationChannel = new NotificationChannel(KORE_PUSH_SERVICE, KORE_ANDROID, importance);
            mNotificationManager.createNotificationChannel(notificationChannel);
            nBuilder = new NotificationCompat.Builder(context, notificationChannel.getId());
        } else {
            nBuilder = new NotificationCompat.Builder(context);
        }

        nBuilder.setContentTitle(title).setSmallIcon(R.drawable.ic_launcher).setColor(Color.parseColor("#009dab")).setContentText(pushMessage).setGroup(GROUP_KEY_NOTIFICATIONS).setGroupSummary(true).setAutoCancel(true).setPriority(NotificationCompat.PRIORITY_HIGH);
        if (alarmSound != null) {
            nBuilder.setSound(alarmSound);
        }

        Intent intent = new Intent(context, NewBotChatActivity.class);
        Bundle bundle = new Bundle();
        //This should not be null
        bundle.putBoolean(BundleUtils.SHOW_PROFILE_PIC, false);
        bundle.putString(BundleUtils.PICK_TYPE, NOTIFICATION);
        bundle.putString(BundleUtils.BOT_NAME_INITIALS, String.valueOf(SDKConfiguration.Client.bot_name.charAt(0)));
        intent.putExtras(bundle);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_MUTABLE);
        nBuilder.setContentIntent(pendingIntent);

        Notification notification = nBuilder.build();
        notification.ledARGB = 0xff0000FF;

        mNotificationManager.notify(NOTIFICATION_TAG_CHAT_MESSAGE, NOTIFICATION_TAG_NUMBER, notification);
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
                botResponse.setIcon(SDKConfiguration.BubbleColors.getIcon_url());

                if (botMetaModel != null && !StringUtils.isNullOrEmpty(botMetaModel.getIcon())) botResponse.setIcon(botMetaModel.getIcon());

                processPayload("", botResponse);
            } catch (Exception e) {
                PayloadOuter payloadOuter = new PayloadOuter();
                payloadOuter.setText(text);
                payloadOuter.setType("text");

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

    public void setPreferenceObject(Object modal, String key) {
        SharedPreferences.Editor prefsEditor = context.getSharedPreferences(BotResponse.THEME_NAME, Context.MODE_PRIVATE).edit();
        gson = new Gson();
        String jsonObject = gson.toJson(modal);
        prefsEditor.putString(key, jsonObject);
        prefsEditor.apply();
    }

    public void sendWebHookMessage(String jwt, boolean b, String message, ArrayList<HashMap<String, String>> attachments) {
        webHookRepository.sendWebHookMessage(jwt, b, message, attachments);
    }

    public void getWebHookMeta(String jwt) {
        webHookRepository.getWebHookMeta(jwt);
    }

}
