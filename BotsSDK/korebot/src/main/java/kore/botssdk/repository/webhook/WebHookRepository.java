package kore.botssdk.repository.webhook;

import android.content.Context;
import android.os.Handler;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

import io.reactivex.annotations.NonNull;
import kore.botssdk.listener.BaseSocketConnectionManager;
import kore.botssdk.listener.BotChatViewListener;
import kore.botssdk.models.BotMetaModel;
import kore.botssdk.models.BotResponse;
import kore.botssdk.models.BotResponseMessage;
import kore.botssdk.models.ComponentModel;
import kore.botssdk.models.PayloadHeaderModel;
import kore.botssdk.models.PayloadOuter;
import kore.botssdk.models.WebHookRequestModel;
import kore.botssdk.models.WebHookResponseDataModel;
import kore.botssdk.net.SDKConfiguration;
import kore.botssdk.net.WebHookRestBuilder;
import kore.botssdk.utils.StringUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@SuppressWarnings("UnKnownNullness")
public class WebHookRepository {
    BotChatViewListener botChatView;
    Context context;
    WebHookResponseDataModel webHookResponseDataModel;
    Gson gson = new Gson();
    final Handler handler = new Handler();
    BotMetaModel botMetaModel;
    Runnable runnable;
    private final int poll_delay = 2000;

    public WebHookRepository(Context context, BotChatViewListener chatView) {
        this.botChatView = chatView;
        this.context = context;
    }

    public void sendWebHookMessage(String jwt, boolean new_session, String msg, ArrayList<HashMap<String, String>> attachments) {
        Call<WebHookResponseDataModel> getBankingConfigService = WebHookRestBuilder.getRestAPI().sendWebHookMessage(SDKConfiguration.Client.bot_id, "bearer " + jwt, getJsonRequest(new_session, msg, attachments));
        getBankingConfigService.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<WebHookResponseDataModel> call, @NonNull Response<WebHookResponseDataModel> response) {
                if (response.isSuccessful()) {
                    webHookResponseDataModel = response.body();
                    botChatView.onConnectionStateChanged(BaseSocketConnectionManager.CONNECTION_STATE.CONNECTED, false);
                    botChatView.enableSendButton();

                    if (webHookResponseDataModel != null && webHookResponseDataModel.getData() != null && webHookResponseDataModel.getData().size() > 0) {
                        for (int i = 0; i < webHookResponseDataModel.getData().size(); i++) {
                            if (webHookResponseDataModel.getData().get(i).getVal() instanceof String)
                                botChatView.displayMessage(webHookResponseDataModel.getData().get(i).getVal().toString(), webHookResponseDataModel.getData().get(i).getType(), webHookResponseDataModel.getData().get(i).getMessageId());
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
                                            botChatView.displayMessage(payloadOuter.getPayload().getTemplate_type(), BotResponse.COMPONENT_TYPE_TEXT, webHookResponseDataModel.getData().get(i).getMessageId());
                                        }
                                    } catch (Exception ex) {
                                        String elementsAsString = gson.toJson(webHookResponseDataModel.getData().get(i).getVal());
                                        botChatView.displayMessage(elementsAsString, BotResponse.COMPONENT_TYPE_TEXT, webHookResponseDataModel.getData().get(i).getMessageId());
                                    }

                                }
                            }
                        }

                        if (!StringUtils.isNullOrEmpty(webHookResponseDataModel.getPollId()))
                            startSendingPo11(webHookResponseDataModel.getPollId(), jwt);
                    }
                } else {
                    botChatView.onConnectionStateChanged(BaseSocketConnectionManager.CONNECTION_STATE.CONNECTED, false);
                    botChatView.enableSendButton();
                }
            }

            @Override
            public void onFailure(@NonNull Call<WebHookResponseDataModel> call, @NonNull Throwable t) {
            }
        });
    }

    void startSendingPo11(String pollId, String jwt) {
        handler.postDelayed(runnable = new Runnable() {
            public void run() {
                handler.postDelayed(runnable, poll_delay);
                postPollingData(pollId, jwt);
            }
        }, poll_delay);
    }

    void postPollingData(String pollId, String jwt) {
        Call<WebHookResponseDataModel> getBankingConfigService = WebHookRestBuilder.getRestAPI().getPollIdData("bearer " + jwt, SDKConfiguration.Client.bot_id, pollId);
        getBankingConfigService.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<WebHookResponseDataModel> call, @NonNull Response<WebHookResponseDataModel> response) {
                if (response.isSuccessful()) {
                    webHookResponseDataModel = response.body();
                    botChatView.onConnectionStateChanged(BaseSocketConnectionManager.CONNECTION_STATE.CONNECTED, false);
                    botChatView.enableSendButton();

                    if (webHookResponseDataModel != null && webHookResponseDataModel.getData() != null && webHookResponseDataModel.getData().size() > 0) {
                        for (int i = 0; i < webHookResponseDataModel.getData().size(); i++) {
                            if (webHookResponseDataModel.getData().get(i).getVal() instanceof String)
                                botChatView.displayMessage(webHookResponseDataModel.getData().get(i).getVal().toString(), webHookResponseDataModel.getData().get(i).getType(), webHookResponseDataModel.getData().get(i).getMessageId());
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

    void stopSendingPolling() {
        handler.removeCallbacks(runnable);
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
            from.setId(SDKConfiguration.Client.identity);
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

                botChatView.processPayload("", botResponse);
            } else if (payloadOuter != null && !StringUtils.isNullOrEmpty(payloadOuter.getText())) {
                botChatView.displayMessage(payloadOuter.getText(), BotResponse.COMPONENT_TYPE_TEXT, "");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getWebHookMeta(String jwt) {
        Call<BotMetaModel> getBankingConfigService = WebHookRestBuilder.getRestAPI().getWebHookBotMeta("bearer " + jwt, SDKConfiguration.Client.bot_id);
        getBankingConfigService.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<BotMetaModel> call, @NonNull Response<BotMetaModel> response) {
                if (response.isSuccessful()) {
                    botChatView.getBrandingDetails();

                    botMetaModel = response.body();
                    if (botMetaModel != null) SDKConfiguration.BubbleColors.setIcon_url(botMetaModel.getIcon());
                    sendWebHookMessage(jwt,true, "ON_CONNECT", null);
                }
            }

            @Override
            public void onFailure(@NonNull Call<BotMetaModel> call, @NonNull Throwable t) {
            }
        });
    }
}
