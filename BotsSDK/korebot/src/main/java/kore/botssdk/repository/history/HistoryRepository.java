package kore.botssdk.repository.history;

import android.content.Context;

import androidx.annotation.NonNull;

import com.google.gson.Gson;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.TimeZone;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import kore.botssdk.listener.BotContentFragmentUpdate;
import kore.botssdk.models.BaseBotMessage;
import kore.botssdk.models.BotHistory;
import kore.botssdk.models.BotHistoryMessage;
import kore.botssdk.models.BotInfoModel;
import kore.botssdk.models.BotRequest;
import kore.botssdk.models.BotResponse;
import kore.botssdk.models.Component;
import kore.botssdk.models.PayloadOuter;
import kore.botssdk.net.RestBuilder;
import kore.botssdk.net.RestResponse;
import kore.botssdk.net.SDKConfiguration;
import kore.botssdk.net.WebHookRestBuilder;
import kore.botssdk.retroresponse.ServerBotMsgResponse;
import kore.botssdk.utils.DateUtils;
import kore.botssdk.utils.Utils;
import retrofit2.Call;
import retrofit2.Response;

@SuppressWarnings("UnKnownNullness")
public class HistoryRepository {
    BotContentFragmentUpdate botContentFragmentUpdate;
    Context context;
    Gson gson = new Gson();

    public HistoryRepository(Context context, BotContentFragmentUpdate botContentFragmentUpdate) {
        this.botContentFragmentUpdate = botContentFragmentUpdate;
        this.context = context;
    }

    public Observable<ServerBotMsgResponse> getHistoryRequest(final int _offset, final int limit, String jwt) {
        return Observable.create(new ObservableOnSubscribe<>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<ServerBotMsgResponse> emitter) {
                try {
                    ServerBotMsgResponse re = new ServerBotMsgResponse();

                    Call<BotHistory> _resp = RestBuilder.getRestAPI().getBotHistory("bearer " + jwt, SDKConfiguration.Client.bot_id, limit, _offset, true);
                    Response<BotHistory> rBody = _resp.execute();
                    BotHistory history = rBody.body();
                    if (rBody.isSuccessful() && history != null) {
                        List<BotHistoryMessage> messages = history.getMessages();
                        ArrayList<BaseBotMessage> msgs;
                        if (messages != null && !messages.isEmpty()) {
                            msgs = new ArrayList<>();
                            for (int index = 0; index < messages.size(); index++) {
                                BotHistoryMessage msg = messages.get(index);
                                if (msg.getType().equals(BotResponse.MESSAGE_TYPE_OUTGOING)) {
                                    List<Component> components = msg.getComponents();
                                    String data = components.get(0).getData().getText();
                                    if (data != null && data.isEmpty()) continue;
                                    try {
                                        PayloadOuter outer = gson.fromJson(data, PayloadOuter.class);
                                        BotResponse r = Utils.buildBotMessage(outer, msg.getBotId(), SDKConfiguration.Client.bot_name, msg.getCreatedOn(), msg.getId());
                                        r.setType(msg.getType());
                                        r.setIcon(history.getIcon());
                                        msgs.add(r);
                                    } catch (com.google.gson.JsonSyntaxException ex) {
                                        BotResponse r = Utils.buildBotMessage(data, msg.getBotId(), SDKConfiguration.Client.bot_name, msg.getCreatedOn(), msg.getId());
                                        r.setType(msg.getType());
                                        r.setIcon(history.getIcon());
                                        msgs.add(r);
                                    }
                                } else {
                                    try {
                                        String message = msg.getComponents().get(0).getData().getText();
                                        RestResponse.BotMessage botMessage = new RestResponse.BotMessage(message);
                                        RestResponse.BotPayLoad botPayLoad = new RestResponse.BotPayLoad();
                                        botPayLoad.setMessage(botMessage);
                                        BotInfoModel botInfo = new BotInfoModel(SDKConfiguration.Client.bot_name, SDKConfiguration.Client.bot_id, null);
                                        botPayLoad.setBotInfo(botInfo);
                                        Gson gson = new Gson();
                                        String jsonPayload = gson.toJson(botPayLoad);

                                        BotRequest botRequest = gson.fromJson(jsonPayload, BotRequest.class);
                                        long cTime = Objects.requireNonNull(DateUtils.isoFormatter.parse(msg.getCreatedOn())).getTime() + TimeZone.getDefault().getRawOffset();
                                        String createdTime = DateUtils.isoFormatter.format(new Date(cTime));
                                        botRequest.setCreatedOn(createdTime);
                                        try {
                                            long timeMillis = botRequest.getTimeInMillis(msg.getCreatedOn(), true);
                                            botRequest.setCreatedInMillis(timeMillis);
                                            botRequest.setFormattedDate(DateUtils.formattedSentDateV6(timeMillis));
                                            botRequest.setTimeStamp(botRequest.prepareTimeStamp(timeMillis));
                                        } catch (ParseException e) {
                                            throw new RuntimeException(e);
                                        }
                                        msgs.add(botRequest);

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }

                            re.setBotMessages(msgs);
                            re.setOriginalSize(messages.size());
                        }
                    }

                    emitter.onNext(re);
                    emitter.onComplete();
                } catch (Exception e) {
                    emitter.onError(e);
                }
            }
        });
    }

    public Observable<ServerBotMsgResponse> getWebHookHistoryRequest(final int _offset, final int limit, String jwt) {
        return Observable.create(new ObservableOnSubscribe<>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<ServerBotMsgResponse> emitter) {
                try {
                    ServerBotMsgResponse re = new ServerBotMsgResponse();

                    Call<BotHistory> _resp = WebHookRestBuilder.getRestAPI().getWebHookBotHistory("bearer " + jwt, SDKConfiguration.Client.bot_id, SDKConfiguration.Client.bot_id, limit, _offset);
                    Response<BotHistory> rBody = _resp.execute();
                    BotHistory history = rBody.body();

                    if (rBody.isSuccessful() && history != null) {
                        List<BotHistoryMessage> messages = history.getMessages();
                        ArrayList<BaseBotMessage> msgs;
                        if (messages != null && messages.size() > 0) {
                            msgs = new ArrayList<>();
                            for (int index = 0; index < messages.size(); index++) {
                                BotHistoryMessage msg = messages.get(index);
                                if (msg.getType().equals(BotResponse.MESSAGE_TYPE_OUTGOING)) {
                                    List<Component> components = msg.getComponents();
                                    String data = components.get(0).getData().getText();
                                    try {
                                        PayloadOuter outer = gson.fromJson(data, PayloadOuter.class);
                                        BotResponse r = Utils.buildBotMessage(outer, msg.getBotId(), SDKConfiguration.Client.bot_name, msg.getCreatedOn(), msg.getId());
                                        r.setType(msg.getType());
                                        msgs.add(r);
                                    } catch (com.google.gson.JsonSyntaxException ex) {
                                        BotResponse r = Utils.buildBotMessage(data, msg.getBotId(), SDKConfiguration.Client.bot_name, msg.getCreatedOn(), msg.getId());
                                        r.setType(msg.getType());
                                        msgs.add(r);
                                    }
                                } else {
                                    try {
                                        String message = msg.getComponents().get(0).getData().getText();
                                        RestResponse.BotMessage botMessage = new RestResponse.BotMessage(message);
                                        RestResponse.BotPayLoad botPayLoad = new RestResponse.BotPayLoad();
                                        botPayLoad.setMessage(botMessage);
                                        BotInfoModel botInfo = new BotInfoModel(SDKConfiguration.Client.bot_name, SDKConfiguration.Client.bot_id, null);
                                        botPayLoad.setBotInfo(botInfo);
                                        Gson gson = new Gson();
                                        String jsonPayload = gson.toJson(botPayLoad);

                                        BotRequest botRequest = gson.fromJson(jsonPayload, BotRequest.class);
                                        long cTime = Objects.requireNonNull(DateUtils.isoFormatter.parse(msg.getCreatedOn())).getTime() + TimeZone.getDefault().getRawOffset();
                                        String createdTime = DateUtils.isoFormatter.format(new Date(cTime));
                                        botRequest.setCreatedOn(createdTime);
                                        msgs.add(botRequest);

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }

                            re.setBotMessages(msgs);
                            re.setOriginalSize(messages.size());
                        }
                    }

                    emitter.onNext(re);
                    emitter.onComplete();
                } catch (Exception e) {
                    emitter.onError(e);
                }
            }
        });
    }

}
