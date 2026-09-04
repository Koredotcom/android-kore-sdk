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
import android.os.Handler;
import android.os.Looper;

import androidx.core.app.NotificationCompat;
import androidx.lifecycle.ViewModel;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import kore.botssdk.R;
import kore.botssdk.activity.NewBotChatActivity;
import kore.botssdk.bot.BotClient;
import kore.botssdk.events.SocketDataTransferModel;
import kore.botssdk.listener.BaseSocketConnectionManager;
import kore.botssdk.listener.BotChatViewListener;
import kore.botssdk.listener.BotSocketConnectionManager;
import kore.botssdk.listener.SocketChatListener;
import kore.botssdk.models.AcknowledgeModel;
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
import kore.botssdk.utils.Constants;
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
    boolean isStreamMessage = false;
    ArrayList<String> arrMessageList = new ArrayList<>();
    private boolean isActivityResumed = false;
    private static final String START_TIMER = "start_timer";
    private static final String AGENT_EVENT_CONNECTED = "agent_connected";
    private static final String AGENT_EVENT_DISCONNECTED = "agent_disconnected";
    private static final String NOTIFICATION_TAG_CHAT_MESSAGE = "ChatMessageNotification";
    private static final int NOTIFICATION_TAG_NUMBER = 237891;
    private static final String KORE_PUSH_SERVICE = "Kore_Push_Service";
    private static final String KORE_ANDROID = "Kore_Android";
    private static final String NOTIFICATION = "Notification";
    Map<Long, BotRequest> messageMap = new ConcurrentHashMap<>();
    Map<Long, Runnable> timeoutMap = new ConcurrentHashMap<>();
    Handler handler = new Handler(Looper.getMainLooper());
    String strResp = "{\n" +
            "  \"type\": \"bot_response\",\n" +
            "  \"from\": \"bot\",\n" +
            "  \"message\": [\n" +
            "    {\n" +
            "      \"type\": \"text\",\n" +
            "      \"component\": {\n" +
            "        \"type\": \"template\",\n" +
            "        \"payload\": {\n" +
            "          \"type\": \"template\",\n" +
            "          \"payload\": {\n" +
            "            \"template_type\": \"carousel\",\n" +
            "            \"elements\": [\n" +
            "              {\n" +
            "                \"title\": \"⭐TotalUnlimited300Online[Onlineoffer]\",\n" +
            "                \"image_url\": \"\",\n" +
            "                \"subtitle\": \"24months\n" +
            "\n" +
            "✓UnlimitedNationalMB\n" +
            "✓12monthsMaximuminternetspeed(Promo)\n" +
            "✓300MbpsMaximuminternetspeed\n" +
            "✓UnlimitedMinutesinBulgariaandroamingEUzone\n" +
            "✓39900MBintheEUroamingarea\n" +
            "✓200MinutesforinternationalcallstoEUzone\n" +
            "✓3000MBinBalkansandTurkeyzone\n" +
            "\n" +
            "Extras\n" +
            "•+2digitalservicesfor24months\n" +
            "•YettelCloud200GBfor9months\n" +
            "•multiSIMfor6months\n" +
            "•SmartTechnicianfor3months\n" +
            "•OnlineProtectfor2months\n" +
            "•ProfileProtectfor3months\n" +
            "•OneClickInsuranceswith10%discount\n" +
            "•PLAYDIEMAXTRAatapreferentialprice\n" +
            "•AdditionalSmartphoneUniverseextraswhencombinedwithadevice\n" +
            "•WithGO+yougetservicesworthupto580€\n" +
            "\n" +
            "Monthly(first24months):20.45€|39.99BGN\n" +
            "Monthly(afterexpiration):25.56€|49.99BGN\",\n" +
            "                \"default_action\": {},\n" +
            "                \"buttons\": [\n" +
            "                  {\n" +
            "                    \"type\": \"web_url\",\n" +
            "                    \"title\": \"ViewMore\"\n" +
            "                  }\n" +
            "                ]\n" +
            "              },\n" +
            "              {\n" +
            "                \"title\": \"TotalUnlimited400\",\n" +
            "                \"image_url\": \"\",\n" +
            "                \"subtitle\": \"24months\n" +
            "\n" +
            "✓UnlimitedNationalMB\n" +
            "✓12monthsMaximuminternetspeed(Promo)\n" +
            "✓400MbpsMaximuminternetspeed\n" +
            "✓UnlimitedMinutestonationalnetworks\n" +
            "✓54100MBintheEUroamingarea\n" +
            "✓200MinutesforinternationalcallstoEUzone\n" +
            "✓2000MBinBalkansandTurkeyzone\n" +
            "\n" +
            "Extras\n" +
            "•+2Digitalservicesfor24months\n" +
            "•YettelCloud200GBfor9months\n" +
            "•multiSIMfor6months\n" +
            "•Smarttechnicianfor3months\n" +
            "•OnlineProtectfor2months\n" +
            "•ProfileProtectfor3months\n" +
            "•OneClickInsuranceswith10%discount\n" +
            "•PLAYDIEMAXTRAatapreferentialprice\n" +
            "•AdditionalSmartphoneUniverseextraswhencombinedwithadevice\n" +
            "•WithGO+yougetservicesworthupto580€\n" +
            "\n" +
            "Monthly(afterexpiration):34.76€|67.99BGN\",\n" +
            "                \"default_action\": {},\n" +
            "                \"buttons\": [\n" +
            "                  {\n" +
            "                    \"type\": \"web_url\",\n" +
            "                    \"title\": \"ViewMore\"\n" +
            "                  }\n" +
            "                ]\n" +
            "              },\n" +
            "              {\n" +
            "                \"title\": \"TotalUnlimitedMAX\",\n" +
            "                \"image_url\": \"\",\n" +
            "                \"subtitle\": \"24months\n" +
            "\n" +
            "✓UnlimitedNationalMB\n" +
            "✓MaxMaximuminternetspeed\n" +
            "✓UnlimitedMinutesinBulgariaandroamingEUzone\n" +
            "✓70900MBintheEUroamingarea\n" +
            "✓300MinutesforinternationalcallstoEUzone\n" +
            "✓3000MBinBalkansandTurkeyzone\n" +
            "✓UnlimitedSMSinallnationalnetworksandEUzone\n" +
            "\n" +
            "Extras\n" +
            "•+3digitalservicesfor24months\n" +
            "•YettelCloud200GBfor9months\n" +
            "•multiSIMfor6months\n" +
            "•Smarttechnicianfor6months\n" +
            "•OnlineProtectfor2months\n" +
            "•ProfileProtectfor3months\n" +
            "•OneClickInsurances10%discount\n" +
            "•PLAYDIEMAXTRAatapreferentialprice\n" +
            "•AdditionalSmartphoneUniverseextraswhencombinedwithadevice\n" +
            "•WithGO+yougetservicesworthupto730€\n" +
            "\n" +
            "Monthly(afterexpiration):44.99€|87.99BGN\",\n" +
            "                \"default_action\": {},\n" +
            "                \"buttons\": [\n" +
            "                  {\n" +
            "                    \"type\": \"web_url\",\n" +
            "                    \"title\": \"ViewMore\"\n" +
            "                  }\n" +
            "                ]\n" +
            "              },\n" +
            "              {\n" +
            "                \"title\": \"TotalUnlimited30\",\n" +
            "                \"image_url\": \"\",\n" +
            "                \"subtitle\": \"24months\n" +
            "\n" +
            "✓UnlimitedNationalMB\n" +
            "✓12monthsMaximuminternetspeed(Promo)\n" +
            "✓30MbpsMaximuminternetspeed\n" +
            "✓UnlimitedMinutesinBulgariaandroamingEUzone\n" +
            "✓38100MBintheEUroamingarea\n" +
            "\n" +
            "Extras\n" +
            "•+1digitalservicefor6months\n" +
            "•YettelCloud200GBfor3months\n" +
            "•multiSIMfor6months\n" +
            "•OnlineProtectfor2months\n" +
            "•ProfileProtectfor3months\n" +
            "•WithGO+yougetservicesworthupto110€\n" +
            "\n" +
            "Monthly(afterexpiration):24.54€|47.99BGN\",\n" +
            "                \"default_action\": {},\n" +
            "                \"buttons\": [\n" +
            "                  {\n" +
            "                    \"type\": \"web_url\",\n" +
            "                    \"title\": \"ViewMore\"\n" +
            "                  }\n" +
            "                ]\n" +
            "              }\n" +
            "            ]\n" +
            "          }\n" +
            "        }\n" +
            "      }\n" +
            "    }\n" +
            "  ],\n" +
            "  \"cInfo\": {\n" +
            "    \"body\": \"{\\\"type\\\":\\\"template\\\",\\\"payload\\\":{\\\"type\\\":\\\"template\\\",\\\"payload\\\":{\\\"template_type\\\":\\\"carousel\\\",\\\"elements\\\":[{\\\"title\\\":\\\"WelcometoPeter'sHats1\\\",\\\"image_url\\\":\\\"https://previews.123rf.com/images/rez_art/rez_art1405/rez_art140500072/28632615-three-beef-tacos-with-cheese-lettuce-and-tomatos-Stock-Photo-taco.jpg\\\",\\\"subtitle\\\":\\\"carouselsubtitle\\\",\\\"default_action\\\":{\\\"type\\\":\\\"web_url\\\",\\\"url\\\":\\\"https://peterssendreceiveapp.ngrok.io/view?item=103\\\"},\\\"buttons\\\":[{\\\"type\\\":\\\"postback\\\",\\\"title\\\":\\\"Buynow\\\",\\\"payload\\\":\\\"DEVELOPER_DEFINED_PAYLOAD_0\\\"},{\\\"type\\\":\\\"postback\\\",\\\"title\\\":\\\"Buynow\\\",\\\"payload\\\":\\\"DEVELOPER_DEFINED_PAYLOAD_1\\\"}]},{\\\"title\\\":\\\"WelcometoPeter'sHats2\\\",\\\"image_url\\\":\\\"https://static.pexels.com/photos/46239/salmon-dish-food-meal-46239.jpeg\\\",\\\"subtitle\\\":\\\"carouselsubtitle\\\",\\\"default_action\\\":{\\\"type\\\":\\\"web_url\\\",\\\"url\\\":\\\"https://peterssendreceiveapp.ngrok.io/view?item=103\\\"},\\\"buttons\\\":[{\\\"type\\\":\\\"postback\\\",\\\"title\\\":\\\"Buynow\\\",\\\"payload\\\":\\\"DEVELOPER_DEFINED_PAYLOAD_0\\\"},{\\\"type\\\":\\\"postback\\\",\\\"title\\\":\\\"Buynow\\\",\\\"payload\\\":\\\"DEVELOPER_DEFINED_PAYLOAD_1\\\"}]},{\\\"title\\\":\\\"WelcometoPeter'sHats3\\\",\\\"image_url\\\":\\\"https://previews.123rf.com/images/rez_art/rez_art1405/rez_art140500072/28632615-three-beef-tacos-with-cheese-lettuce-and-tomatos-Stock-Photo-taco.jpg\\\",\\\"subtitle\\\":\\\"carouselsubtitle\\\",\\\"default_action\\\":{\\\"type\\\":\\\"web_url\\\",\\\"url\\\":\\\"https://peterssendreceiveapp.ngrok.io/view?item=103\\\"},\\\"buttons\\\":[{\\\"type\\\":\\\"postback\\\",\\\"title\\\":\\\"Buynow\\\",\\\"payload\\\":\\\"DEVELOPER_DEFINED_PAYLOAD_0\\\"},{\\\"type\\\":\\\"postback\\\",\\\"title\\\":\\\"Buynow\\\",\\\"payload\\\":\\\"DEVELOPER_DEFINED_PAYLOAD_1\\\"}]},{\\\"title\\\":\\\"WelcometoPeter'sHats4\\\",\\\"image_url\\\":\\\"https://static.pexels.com/photos/416458/pexels-photo-416458.jpeg\\\",\\\"subtitle\\\":\\\"carouselsubtitle\\\",\\\"default_action\\\":{\\\"type\\\":\\\"web_url\\\",\\\"url\\\":\\\"https://peterssendreceiveapp.ngrok.io/view?item=103\\\"},\\\"buttons\\\":[{\\\"type\\\":\\\"postback\\\",\\\"title\\\":\\\"Buynow\\\",\\\"payload\\\":\\\"DEVELOPER_DEFINED_PAYLOAD_0\\\"},{\\\"type\\\":\\\"postback\\\",\\\"title\\\":\\\"Buynow\\\",\\\"payload\\\":\\\"DEVELOPER_DEFINED_PAYLOAD_1\\\"}]}]}}\"\n" +
            "  },\n" +
            "  \"messageId\": \"ms-7fcdf599-1e0d-5049-97a1-dbbf6c212f9d\",\n" +
            "  \"sessionId\": \"6a5dc4725a11c3bde2525dc4\",\n" +
            "  \"botInfo\": {\n" +
            "    \"chatBot\": \"SDKDemo\",\n" +
            "    \"taskBotId\": \"st-c2a341ba-5612-5ab2-a5b3-d4a81f6a42ea\",\n" +
            "    \"hostDomain\": \"wss://platform.kore.ai:443\",\n" +
            "    \"userId\": \"u-99d72cea-5c70-5d6f-96a3-55ec7fea44f6\"\n" +
            "  },\n" +
            "  \"createdOn\": \"2026-07-20T06:47:27.075Z\",\n" +
            "  \"xTraceId\": \"9c861d96-2856-4c4a-91a3-01cccf4c11c3\",\n" +
            "  \"botLanguage\": \"en\",\n" +
            "  \"icon\": \"https://platform.kore.ai/api/getMediaStream/market/f-6e20afb1-24b7-51b6-b539-e38c248da06d.png?n=6289699802&s=IjBVZXp0MThKeUhsVlBIcHh3RmhuR1lKWDk5NDQwZTJoZlY3SmprVmliQmc9Ig$$\",\n" +
            "  \"timestamp\": 1784530047166\n" +
            "}";

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

        SocketWrapper.getInstance(context).disConnect();
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

                if(!SDKConfiguration.Server.notificationDeviceId.isEmpty() && SDKConfiguration.OverrideKoreConfig.default_notifications)
                    new PushNotificationRegister().registerPushNotification(botClient.getUserId(), botClient.getAccessToken(), SDKConfiguration.Server.notificationDeviceId);
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

                if(!isStreamMessage)
                    processPayload(data.getPayLoad(), null);
                else processStreamMessage(data.getPayLoad());

            } else if (data.getEvent_type().equals(BaseSocketConnectionManager.EVENT_TYPE.TYPE_MESSAGE_UPDATE))
            {
                BotRequest botRequest = data.getBotRequest();
                long messageId = data.getBotRequest().getCreatedInMillis();
                botRequest.setStatus(BotRequest.MessageStatus.SENDING);
                messageMap.put(messageId, botRequest);
                startTimeout(messageId);
                chatView.updateContentListOnSend(botRequest);
            }
        }

        private void startTimeout(long messageId) {

            Runnable runnable = () ->
            {
                BotRequest botRequest = messageMap.get(messageId);

                if (botRequest != null && botRequest.getStatus() == BotRequest.MessageStatus.SENDING) {
                    botRequest.setStatus(BotRequest.MessageStatus.FAILED);
                    chatView.updateMessageStatus(botRequest);
                }
            };

            timeoutMap.put(messageId, runnable);
            handler.postDelayed(runnable, 5000); // 5 seconds
        }

        @Override
        public void onStartCompleted(boolean isReconnect) {
            getBrandingDetails(SDKConfiguration.Client.bot_id, SocketWrapper.getInstance(context).getAccessToken(), "published", "1", "en_US");
        }

    };

    private void processStreamMessage(String payload)
    {
        try {
            final BotResponse botResponse = gson.fromJson(payload, BotResponse.class);
            if (botResponse == null || botResponse.getMessage() == null || botResponse.getMessage().isEmpty()) {
                return;
            }
            isStreamMessage = botResponse.issM();

            if(botResponse.isEndChunk())
                isStreamMessage = false;

            if (!StringUtils.isNullOrEmpty(botResponse.getIcon()))
                SDKConfiguration.BubbleColors.setIcon_url(botResponse.getIcon());

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

            if(payOuter != null && payOuter.getText() != null)
            {
                chatView.addStreamingMessage(payOuter.getText(), botResponse.isEndChunk());
            }
        } catch (Exception e)
        {
            LogUtils.e("Error", String.valueOf(e));
        }
    }

    public void sendReadReceipts() {
        //Added newly for send receipts
        if (botClient != null && !arrMessageList.isEmpty() && isAgentTransfer) {
            botClient.sendReceipts(BundleConstants.MESSAGE_READ, arrMessageList.get((arrMessageList.size() - 1)));
            arrMessageList = new ArrayList<>();
        }
    }

    private void cancelTimeout(long messageId) {
        Runnable runnable = timeoutMap.get(messageId);
        if (runnable != null) {
            handler.removeCallbacks(runnable);
            timeoutMap.remove(messageId);
        }
    }

    private void handleAck(long messageId) {
        BotRequest botRequest = messageMap.get(messageId);

        if (botRequest != null) {
            botRequest.setStatus(BotRequest.MessageStatus.SENT);
            cancelTimeout(messageId);

            chatView.updateMessageStatus(botRequest);
            messageMap.remove(messageId);
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
                AcknowledgeModel acknowledgeModel = gson.fromJson(payload, AcknowledgeModel.class);
                if (acknowledgeModel != null)
                {
                    if(Objects.equals(acknowledgeModel.getType(), BundleConstants.ACK)) {
                        long messageId = acknowledgeModel.getReplyto();
                        handleAck(messageId);
                    }
                    else if(Objects.equals(acknowledgeModel.getType(), BundleConstants.SESSION_END) && SDKConfiguration.Server.getBotStatusListener() != null) {
                        SDKConfiguration.Server.getBotStatusListener().onSessionEnded(BundleConstants.SESSION_END, "Bot session has expired");
                    }
                    return;
                }

                return;
            }

            if (botResponse.getMessageId() != null) lastMsgId = botResponse.getMessageId();
            isStreamMessage = botResponse.issM();

            if(botResponse.isEndChunk())
                isStreamMessage = false;
            try {
                long timeMillis = botResponse.getTimestamp() == 0L ? botResponse.getTimeInMillis(botResponse.getCreatedOn(), true) : botResponse.getTimestamp();
                botResponse.setCreatedInMillis(timeMillis);
                botResponse.setFormattedDate(DateUtils.formattedSentDateV6(context, timeMillis));
                botResponse.setTimeStamp(botResponse.prepareLocaleTimeStamp(context, timeMillis));
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

            if (!isActivityResumed && SDKConfiguration.OverrideKoreConfig.showLocalNotification) {
//                postNotification("Kore Message", "Received new message.");
                if (SDKConfiguration.Server.getBotStatusListener() != null) {
                    SDKConfiguration.Server.getBotStatusListener().onBotMessageReceived("BotMessageReceived", payload);
                }
            }

        } catch (Exception e) {
            LogUtils.d(TAG, "Failed to complete risky operation" + e);
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
        String botInitial = "B";
        if (SDKConfiguration.Client.bot_name != null && !SDKConfiguration.Client.bot_name.isEmpty()) {
            botInitial = String.valueOf(SDKConfiguration.Client.bot_name.charAt(0));
        }
        bundle.putString(BundleUtils.BOT_NAME_INITIALS, botInitial);
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
