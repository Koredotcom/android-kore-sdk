package kore.botssdk.viewmodels.chat;

import static kore.botssdk.activity.KaCaptureImageActivity.rotateIfNecessary;
import static kore.botssdk.net.SDKConfiguration.Client.enable_ack_delivery;
import static kore.botssdk.utils.BundleConstants.GROUP_KEY_NOTIFICATIONS;
import static kore.botssdk.utils.ToastUtils.showToast;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.core.app.NotificationCompat;
import androidx.lifecycle.ViewModel;

import com.audiocodes.mv.webrtcsdk.audio.WebRTCAudioManager;
import com.audiocodes.mv.webrtcsdk.sip.enums.Transport;
import com.audiocodes.mv.webrtcsdk.useragent.AudioCodesUA;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

import kore.botssdk.R;
import kore.botssdk.activity.BotChatActivity;
import kore.botssdk.application.BotApplication;
import kore.botssdk.audiocodes.webrtcclient.Activities.CallActivity;
import kore.botssdk.audiocodes.webrtcclient.General.ACManager;
import kore.botssdk.audiocodes.webrtcclient.General.Prefs;
import kore.botssdk.audiocodes.webrtcclient.Structure.SipAccount;
import kore.botssdk.bot.BotClient;
import kore.botssdk.events.SocketDataTransferModel;
import kore.botssdk.listener.BaseSocketConnectionManager;
import kore.botssdk.listener.BotChatViewListener;
import kore.botssdk.listener.BotSocketConnectionManager;
import kore.botssdk.listener.SocketChatListener;
import kore.botssdk.models.AgentInfoModel;
import kore.botssdk.models.BotInfoModel;
import kore.botssdk.models.BotMetaModel;
import kore.botssdk.models.BotRequest;
import kore.botssdk.models.BotResponse;
import kore.botssdk.models.BotResponseMessage;
import kore.botssdk.models.BotResponsePayLoadText;
import kore.botssdk.models.ComponentModel;
import kore.botssdk.models.ComponentModelPayloadText;
import kore.botssdk.models.EventMessageModel;
import kore.botssdk.models.EventModel;
import kore.botssdk.models.PayloadInner;
import kore.botssdk.models.PayloadOuter;
import kore.botssdk.net.RestResponse;
import kore.botssdk.net.SDKConfig;
import kore.botssdk.net.SDKConfiguration;
import kore.botssdk.pushnotification.PushNotificationRegister;
import kore.botssdk.repository.branding.BrandingRepository;
import kore.botssdk.repository.webhook.WebHookRepository;
import kore.botssdk.utils.AsyncTaskExecutor;
import kore.botssdk.utils.BitmapUtils;
import kore.botssdk.utils.BundleConstants;
import kore.botssdk.utils.BundleUtils;
import kore.botssdk.utils.DateUtils;
import kore.botssdk.utils.LogUtils;
import kore.botssdk.utils.SharedPreferenceUtils;
import kore.botssdk.utils.StringUtils;
import kore.botssdk.websocket.SocketWrapper;

@SuppressWarnings("UnKnownNullness")
public class BotChatViewModel extends ViewModel {
    private static final String LOG_TAG = "BotChatActivity";
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
    protected final int compressQualityInt = 100;
    private final SharedPreferences sharedPreferences;

    public BotChatViewModel(Context context, BotClient botClient, BotChatViewListener chatView) {
        this.context = context.getApplicationContext();
        this.repository = new BrandingRepository(context, chatView);
        this.chatView = chatView;
        this.webHookRepository = new WebHookRepository(context, chatView);
        this.botClient = botClient;
        sharedPreferences = context.getSharedPreferences(BotResponse.THEME_NAME, Context.MODE_PRIVATE);
    }

    public void getBrandingDetails(String botId, String botToken, boolean isReconnection) {
        repository.getBrandingDetails(botId, botToken, isReconnection);
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
                getBrandingDetails(SDKConfiguration.Client.bot_id, SocketWrapper.getInstance(context).getAccessToken(), isReconnection);
                if (isReconnection) {
                    if (sharedPreferences.getInt(BotResponse.HISTORY_COUNT, 0) > 19) {
                        chatView.loadChatHistory(0, 20);
                    } else if (sharedPreferences.getInt(BotResponse.HISTORY_COUNT, 0) > 0) {
                        chatView.loadChatHistory(0, sharedPreferences.getInt(BotResponse.HISTORY_COUNT, 1));
                    } else {
                        chatView.loadReconnectionChatHistory(0, 10);
                    }
                }

            } else if (state == BaseSocketConnectionManager.CONNECTION_STATE.RECONNECTION_STOPPED) {
                if (!isReconnectionStopped) {
                    isReconnectionStopped = true;
                    chatView.showReconnectionStopped();
                }
            }

            new PushNotificationRegister().registerPushNotification(context, botClient.getUserId(), botClient.getAccessToken(), getUniqueDeviceId(context));
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
    };

    public void sendReadReceipts() {
        //Added newly for send receipts
        if (botClient != null && arrMessageList.size() > 0 && isAgentTransfer) {
            botClient.sendReceipts(BundleConstants.MESSAGE_READ, arrMessageList.get((arrMessageList.size() - 1)));
            arrMessageList = new ArrayList<>();
        }
    }

    /**
     * payload processing
     */
    public void processPayload(String payload, BotResponse botLocalResponse) {
        if (botLocalResponse == null) BotSocketConnectionManager.getInstance().stopDelayMsgTimer();

        if (payload.contains("Form_Submitted")) {
            Intent intent = new Intent("finish_activity");
            context.sendBroadcast(intent);
        }

        try {
            final BotResponse botResponse = botLocalResponse != null ? botLocalResponse : gson.fromJson(payload, BotResponse.class);
            if (botResponse == null || botResponse.getMessage() == null || botResponse.getMessage().isEmpty()) {
                return;
            }
            try {
                long timeMillis = botResponse.getTimeInMillis(botResponse.getCreatedOn(), true);
                botResponse.setCreatedInMillis(timeMillis);
                botResponse.setFormattedDate(DateUtils.formattedSentDateV6(timeMillis));
                botResponse.setTimeStamp(botResponse.prepareTimeStamp(timeMillis));
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }

            if (!StringUtils.isNullOrEmpty(botResponse.getIcon()) && StringUtils.isNullOrEmpty(SDKConfiguration.BubbleColors.getIcon_url()))
                SDKConfiguration.BubbleColors.setIcon_url(botResponse.getIcon());

            if (botClient != null && enable_ack_delivery)
                botClient.sendMsgAcknowledgement(botResponse.getTimestamp(), botResponse.getKey());

            LogUtils.d(LOG_TAG, payload);
            isAgentTransfer = botResponse.isFromAgent();

            chatView.setIsAgentConnected(isAgentTransfer);

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
                        } else if (payOuter.getText() != null && payOuter.getText().contains("*")) {
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

            chatView.showTypingStatus();

            if (payloadInner != null) {
                payloadInner.convertElementToAppropriate();
            }

            if (!BotApplication.isActivityVisible()) {
                postNotification("Kore Message", "Received new message.");
            }

            if (botResponse.getMessageId() != null) lastMsgId = botResponse.getMessageId();
            chatView.addMessageToAdapter(botResponse);
        } catch (Exception e) {
            if (e instanceof JsonSyntaxException) {
                LogUtils.d(LOG_TAG, payload);
                try {
                    EventModel eventModel = gson.fromJson(payload, EventModel.class);
                    if (eventModel != null && eventModel.getMessage() != null) {
                        if (!StringUtils.isNullOrEmpty(eventModel.getMessage().getSipURI()) && eventModel.getMessage().getType().equalsIgnoreCase(BundleConstants.CALL_AGENT_WEBRTC)) {
                            EventMessageModel eventMessageModel = eventModel.getMessage();
                            if (eventMessageModel != null) {
                                SipAccount sipAccount = new SipAccount();
                                sipAccount.setUsername(botClient.getUserId());
                                sipAccount.setDisplayName(botClient.getUserId());
                                sipAccount.setDomain(eventMessageModel.getDomain());
                                sipAccount.setProxy(getProxyUrl(eventMessageModel.getAddresses().get(0)));
                                sipAccount.setPort(5060);
                                sipAccount.setTransport(Transport.UDP);

                                Prefs.setSipAccount(sipAccount);
                                Prefs.setAutoRedirect(true);

                                chatView.showAlertDialog(eventModel);
                            }
                        } else if (eventModel.getMessage().getType().equalsIgnoreCase(BundleConstants.TERMINATE_AGENT_WEBRTC)) {

                            chatView.hideAlertDialog();

                            if (ACManager.getInstance().getActiveSession() != null) {
                                int sessionIndex = ACManager.getInstance().getActiveSession().getSessionID();
                                if (AudioCodesUA.getInstance().getSession(sessionIndex) != null) {
                                    AudioCodesUA.getInstance().getSession(sessionIndex).terminate();
                                    WebRTCAudioManager.getInstance().setWebRTcAudioRouteListener(null);

                                    if (BotApplication.getCurrentActivity() instanceof CallActivity) {
                                        BotApplication.getCurrentActivity().finish();
                                    }
                                }
                            }
                        }
                    } else {
                        BotRequest botRequest = gson.fromJson(payload, BotRequest.class);
                        if (botRequest != null && botRequest.getMessage() != null && botRequest.getMessage().getBody() != null) {
                            botRequest.setCreatedOn(DateUtils.isoFormatter.format(new Date()));
                            chatView.updateContentListOnSend(botRequest);
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
                                    chatView.showTypingStatus();
                            }
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    try {
                        //This is the case Bot returning user sent message from another channel
                        BotRequest botRequest = gson.fromJson(payload, BotRequest.class);
                        botRequest.setCreatedOn(DateUtils.isoFormatter.format(new Date()));
                        chatView.updateContentListOnSend(botRequest);
                    } catch (Exception e1) {
                        try {
                            final BotResponsePayLoadText botResponse = gson.fromJson(payload, BotResponsePayLoadText.class);
                            if (botResponse == null || botResponse.getMessage() == null || botResponse.getMessage().isEmpty()) {
                                return;
                            }
                            LogUtils.d(LOG_TAG, payload);
                            if (!botResponse.getMessage().isEmpty()) {
                                ComponentModelPayloadText compModel = botResponse.getMessage().get(0).getComponent();
                                if (compModel != null && !StringUtils.isNullOrEmpty(compModel.getPayload())) {
                                    displayMessage(compModel.getPayload(), BotResponse.COMPONENT_TYPE_TEXT, botResponse.getMessageId(), botResponse.getIcon());
                                }
                            }
                        } catch (Exception e2) {
                            e2.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    public String getProxyUrl(String proxy) {
        String[] strProxy = proxy.split("//");
        if (strProxy.length > 0) {
            String[] proxy1 = strProxy[1].split(":");
            if (proxy1.length > 0) return proxy1[0];
        }
        return "";
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
                        Gson gson = new Gson();
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
            NotificationChannel notificationChannel = new NotificationChannel("Kore_Push_Service", "Kore_Android", importance);
            mNotificationManager.createNotificationChannel(notificationChannel);
            nBuilder = new NotificationCompat.Builder(context, notificationChannel.getId());
        } else {
            nBuilder = new NotificationCompat.Builder(context);
        }

        nBuilder.setContentTitle(title).setSmallIcon(R.mipmap.ic_launcher).setColor(Color.parseColor("#009dab")).setContentText(pushMessage).setGroup(GROUP_KEY_NOTIFICATIONS).setGroupSummary(true).setAutoCancel(true).setPriority(NotificationCompat.PRIORITY_HIGH);
        if (alarmSound != null) {
            nBuilder.setSound(alarmSound);
        }

        Intent intent = new Intent(context, BotChatActivity.class);
        Bundle bundle = new Bundle();
        //This should not be null
        bundle.putBoolean(BundleUtils.SHOW_PROFILE_PIC, false);
        bundle.putString(BundleUtils.PICK_TYPE, "Notification");
        bundle.putString(BundleUtils.BOT_NAME_INITIALS, String.valueOf(SDKConfiguration.Client.bot_name.charAt(0)));
        intent.putExtras(bundle);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_MUTABLE);
        nBuilder.setContentIntent(pendingIntent);

        Notification notification = nBuilder.build();
        notification.ledARGB = 0xff0000FF;

        mNotificationManager.notify("YUIYUYIU", 237891, notification);
    }

    public void displayMessage(String text, String type, String messageId, String icon) {
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
                botResponse.setIcon(icon);

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
                botResponse.setIcon(icon);

                if (botMetaModel != null && !StringUtils.isNullOrEmpty(botMetaModel.getIcon())) botResponse.setIcon(botMetaModel.getIcon());

                processPayload("", botResponse);
            }
        }
    }

    public void setPreferenceObject(Object modal, String key) {
        SharedPreferences.Editor prefsEditor = context.getSharedPreferences(BotResponse.THEME_NAME, Context.MODE_PRIVATE).edit();
        Gson gson = new Gson();
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

    public void sendImage(String filePath, String fileName, String filePathThumbnail) {
        new SaveCapturedImageTask(filePath, fileName, filePathThumbnail).executeAsync();
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
                    } catch (Exception e) {
                        LogUtils.e(LOG_TAG, e.toString());
                    } finally {
                        try {
                            if (fOut != null) fOut.close();
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
                chatView.uploadBulkFile(fileName, filePath, extn, filePathThumbnail, orientation);
            } else {
                showToast(context, "Unable to attach!");
            }
        }

        @Override
        protected void onCancelled() {
            // update UI on task cancelled
            showToast(context, "Unable to attach!");
        }
    }

}
