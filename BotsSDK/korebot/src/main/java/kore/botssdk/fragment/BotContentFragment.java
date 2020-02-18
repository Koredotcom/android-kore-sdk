package kore.botssdk.fragment;

import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import kore.botssdk.R;
import kore.botssdk.adapter.ChatAdapter;
import kore.botssdk.listener.BotContentFragmentUpdate;
import kore.botssdk.listener.InvokeGenericWebViewInterface;
import kore.botssdk.listener.TTSUpdate;
import kore.botssdk.models.BaseBotMessage;
import kore.botssdk.models.BotHistory;
import kore.botssdk.models.BotHistoryMessage;
import kore.botssdk.models.BotInfoModel;
import kore.botssdk.models.BotRequest;
import kore.botssdk.models.BotResponse;
import kore.botssdk.models.Component;
import kore.botssdk.models.ComponentModel;
import kore.botssdk.models.PayloadInner;
import kore.botssdk.models.PayloadOuter;
import kore.botssdk.models.QuickReplyTemplate;
import kore.botssdk.net.RestBuilder;
import kore.botssdk.net.RestResponse;
import kore.botssdk.net.SDKConfiguration;
import kore.botssdk.net.SDKConfiguration.Client;
import kore.botssdk.retroresponse.ServerBotMsgResponse;
import kore.botssdk.utils.BundleUtils;
import kore.botssdk.utils.DateUtils;
import kore.botssdk.utils.Utils;
import kore.botssdk.view.CircularProfileView;
import kore.botssdk.view.QuickReplyView;
import kore.botssdk.views.DotsTextView;
import kore.botssdk.websocket.SocketWrapper;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Pradeep Mahato on 31-May-16.
 * Copyright (c) 2014 Kore Inc. All rights reserved.
 */
public class BotContentFragment extends BaseSpiceFragment implements BotContentFragmentUpdate {

    ListView botsBubblesListView;
    ChatAdapter botsChatAdapter;
    QuickReplyView quickReplyView;
    String LOG_TAG = BotContentFragment.class.getSimpleName();
    private LinearLayout botTypingStatusRl;
    private CircularProfileView botTypingStatusIcon;
    private DotsTextView typingStatusItemDots;
    ComposeFooterFragment.ComposeFooterInterface composeFooterInterface;
    InvokeGenericWebViewInterface invokeGenericWebViewInterface;
    boolean shallShowProfilePic;
    private String mChannelIconURL;
    private String mBotNameInitials;
    private TTSUpdate ttsUpdate;
    private int mBotIconId;
    private boolean fetching = false;
    private boolean hasMore = true;
    private TextView headerView;
    private Gson gson = new Gson();
    private SwipeRefreshLayout swipeRefreshLayout;
    private int offset = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.bot_content_layout, null);
        findViews(view);
        getBundleInfo();
        initializeBotTypingStatus(view, mChannelIconURL);
        setupAdapter();
        loadChatHistory(0, limit);
        return view;
    }

    private void findViews(View view) {
        botsBubblesListView = (ListView) view.findViewById(R.id.chatContentListView);
        headerView = view.findViewById(R.id.filesSectionHeader);
        swipeRefreshLayout = ((SwipeRefreshLayout) view.findViewById(R.id.swipeContainerChat));

        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
    }

    private void setupAdapter() {
        botsChatAdapter = new ChatAdapter(getActivity());
        botsChatAdapter.setComposeFooterInterface(composeFooterInterface);
        botsChatAdapter.setInvokeGenericWebViewInterface(invokeGenericWebViewInterface);
        botsChatAdapter.setActivityContext(getActivity());
        botsBubblesListView.setAdapter(botsChatAdapter);
        botsChatAdapter.setShallShowProfilePic(shallShowProfilePic);
        botsBubblesListView.setOnScrollListener(onScrollListener);
        quickReplyView = new QuickReplyView(getContext());
        quickReplyView.setComposeFooterInterface(composeFooterInterface);
        quickReplyView.setInvokeGenericWebViewInterface(invokeGenericWebViewInterface);
        botsBubblesListView.addFooterView(quickReplyView);
    }

    public void setTtsUpdate(TTSUpdate ttsUpdate) {
        this.ttsUpdate = ttsUpdate;
    }

    public void setComposeFooterInterface(ComposeFooterFragment.ComposeFooterInterface composeFooterInterface) {
        this.composeFooterInterface = composeFooterInterface;
    }

    public void setInvokeGenericWebViewInterface(InvokeGenericWebViewInterface invokeGenericWebViewInterface) {
        this.invokeGenericWebViewInterface = invokeGenericWebViewInterface;
    }

    private void getBundleInfo() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            shallShowProfilePic = bundle.getBoolean(BundleUtils.SHOW_PROFILE_PIC, false);
            mChannelIconURL = bundle.getString(BundleUtils.CHANNEL_ICON_URL);
            mBotNameInitials = bundle.getString(BundleUtils.BOT_NAME_INITIALS, "B");
            mBotIconId = bundle.getInt(BundleUtils.BOT_ICON_ID, -1);
        }
    }

    public void showTypingStatus(BotResponse botResponse) {
        if (botTypingStatusRl != null && botResponse.getMessage() != null && !botResponse.getMessage().isEmpty()) {
            if (typingStatusItemDots != null) {
                botTypingStatusRl.setVisibility(View.VISIBLE);
                typingStatusItemDots.start();
                Log.d("Hey", "Started animation");
            }
        }
    }

    public void setQuickRepliesIntoFooter(BotResponse botResponse) {
        ArrayList<QuickReplyTemplate> quickReplyTemplates = getQuickReplies(botResponse);
        quickReplyView.populateQuickReplyView(quickReplyTemplates);
    }

    private ArrayList<QuickReplyTemplate> getQuickReplies(BotResponse botResponse) {

        ArrayList<QuickReplyTemplate> quickReplyTemplates = null;

        if (botResponse != null && botResponse.getMessage() != null && !botResponse.getMessage().isEmpty()) {
            ComponentModel compModel = botResponse.getMessage().get(0).getComponent();
            if (compModel != null) {
                String compType = compModel.getType();
                if (BotResponse.COMPONENT_TYPE_TEMPLATE.equalsIgnoreCase(compType)) {
                    PayloadOuter payOuter = compModel.getPayload();
                    PayloadInner payInner = payOuter.getPayload();
                    if (payInner != null && BotResponse.TEMPLATE_TYPE_QUICK_REPLIES.equalsIgnoreCase(payInner.getTemplate_type())) {
                        quickReplyTemplates = payInner.getQuick_replies();
                    }
                }
            }
        }

        return quickReplyTemplates;
    }

    public void addMessageToBotChatAdapter(BotResponse botResponse) {
        botsChatAdapter.addBaseBotMessage(botResponse);
        botTypingStatusRl.setVisibility(View.GONE);
        botsBubblesListView.smoothScrollToPosition(botsChatAdapter.getCount());
    }

    protected void initializeBotTypingStatus(View view, String mChannelIconURL) {
        botTypingStatusRl = (LinearLayout) view.findViewById(R.id.botTypingStatus);
        botTypingStatusIcon = (CircularProfileView) view.findViewById(R.id.typing_status_item_cpv);
        botTypingStatusIcon.populateLayout(mBotNameInitials, mChannelIconURL, null, mBotIconId, Color.parseColor(SDKConfiguration.BubbleColors.quickReplyColor), true);
        typingStatusItemDots = (DotsTextView) view.findViewById(R.id.typing_status_item_dots);
        typingStatusItemDots.setTextColor(Color.parseColor(SDKConfiguration.BubbleColors.typingStatusDotsColor));
    }

    private void scrollToBottom() {
        final int count = botsChatAdapter.getCount();
        botsBubblesListView.post(new Runnable() {
            @Override
            public void run() {
                botsBubblesListView.setSelection(count - 1);
            }
        });
    }


    private int limit = 30;
    AbsListView.OnScrollListener onScrollListener = new AbsListView.OnScrollListener() {
        public int firstVisibleItem = -1;

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
   /*         if (!fetching && hasMore && totalItemCount > 0 && (totalItemCount - visibleItemCount) <= (firstVisibleItem + 6)) {
                // I load the next page of gigs using a background task,
                // but you can call any function here.
                loadChatHistory(chatAdapter.getCount(),limit);
            }*/

            BaseBotMessage baseBotMessage = ((BaseBotMessage) botsChatAdapter.getItem(firstVisibleItem));
            if (baseBotMessage != null) {
                headerView.setText(DateUtils.formattedSentDateV6(baseBotMessage.getCreatedInMillis()));
            }
            if (this.firstVisibleItem == firstVisibleItem || visibleItemCount == 0 || totalItemCount == 0) {
                return;
            }
            if ((firstVisibleItem <= 10 && this.firstVisibleItem > firstVisibleItem) && !fetching && hasMore) {
                loadChatHistory(botsChatAdapter.getCount(), limit);
            }
            this.firstVisibleItem = firstVisibleItem;
        }


    };


    public void addMessagesToBotChatAdapter(ArrayList<BaseBotMessage> list, boolean scrollToBottom) {
        botsChatAdapter.addBaseBotMessages(list);
        if (scrollToBottom) {
            scrollToBottom();
        }
    }

    public void updateContentListOnSend(BotRequest botRequest) {
        if (botRequest.getMessage() != null) {
            if (botsChatAdapter != null) {
                botsChatAdapter.addBaseBotMessage(botRequest);
                quickReplyView.populateQuickReplyView(null);
                scrollToBottom();
            }
        }
    }


    private Observable<ServerBotMsgResponse> getHistoryRequest(final int _offset, final int limit) {
        return Observable.create(new ObservableOnSubscribe<ServerBotMsgResponse>() {
            @Override
            public void subscribe(ObservableEmitter<ServerBotMsgResponse> emitter) throws Exception {
                try {
                    ServerBotMsgResponse re = new ServerBotMsgResponse();

                    Call<BotHistory> _resp = RestBuilder.getRestAPI().getHistory("bearer " + SocketWrapper.getInstance(getActivity().getApplicationContext()).getAccessToken(), Client.bot_id, limit, _offset, true);
                    Response<BotHistory> rBody = _resp.execute();
                    BotHistory history = rBody.body();

                    if (rBody.isSuccessful()) {
                        List<BotHistoryMessage> messages = history.getMessages();
                        ArrayList<BaseBotMessage> msgs = null;
                        if (messages != null && messages.size() > 0) {
                            msgs = new ArrayList<>();
                            for (int index = 0; index < messages.size(); index++) {
                                BotHistoryMessage msg = messages.get(index);
                                if (msg.getType().equals(BotResponse.MESSAGE_TYPE_OUTGOING)) {
                                    List<Component> components = msg.getComponents();
                                    String data = components.get(0).getData().getText();
                                    try {
                                        PayloadOuter outer = gson.fromJson(data, PayloadOuter.class);
                                        BotResponse r = Utils.buildBotMessage(outer, msg.getBotId(), Client.bot_name, msg.getCreatedOn(), msg.getId());
                                        r.setType(msg.getType());
                                        msgs.add(r);
                                    } catch (com.google.gson.JsonSyntaxException ex) {
                                        BotResponse r = Utils.buildBotMessage(data, msg.getBotId(), Client.bot_name, msg.getCreatedOn(), msg.getId());
                                        r.setType(msg.getType());
                                        msgs.add(r);
                                    }
                                } else {
                                    try {
                                        String message = msg.getComponents().get(0).getData().getText();
                                        RestResponse.BotMessage botMessage = new RestResponse.BotMessage(message);
                                        RestResponse.BotPayLoad botPayLoad = new RestResponse.BotPayLoad();
                                        botPayLoad.setMessage(botMessage);
                                        BotInfoModel botInfo = new BotInfoModel(Client.bot_name, Client.bot_id, null);
                                        botPayLoad.setBotInfo(botInfo);
                                        Gson gson = new Gson();
                                        String jsonPayload = gson.toJson(botPayLoad);

                                        BotRequest botRequest = gson.fromJson(jsonPayload, BotRequest.class);
                                        long cTime = DateUtils.isoFormatter.parse(((BotHistoryMessage) msg).getCreatedOn()).getTime() + TimeZone.getDefault().getRawOffset();
                                        String createdTime = DateUtils.isoFormatter.format(new Date(cTime));
                                        botRequest.setCreatedOn(createdTime);
                                        msgs.add(botRequest);

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }

                            re.setBotMessages(msgs);
                            re.setOriginalSize(messages != null ? messages.size() : 0);
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


    private void loadChatHistory(final int _offset, final int limit) {
        if (fetching) return;
        fetching = true;
        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.setRefreshing(true);
        }

        getHistoryRequest(_offset, limit)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ServerBotMsgResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ServerBotMsgResponse re) {
                        fetching = false;

                        if (swipeRefreshLayout != null) {
                            swipeRefreshLayout.setRefreshing(false);
                        }

                        ArrayList<BaseBotMessage> list = null;
                        if (re != null) {
                            list = re.getBotMessages();
                            offset = _offset + re.getOriginalSize();
                        }

                        if (list != null && list.size() > 0) {
                            addMessagesToBotChatAdapter(list, offset == 0);
                        }

                        if ((list == null || list.size() < limit) && offset != 0) {
                            hasMore = false;
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        fetching = false;
                        if (swipeRefreshLayout != null) {
                            swipeRefreshLayout.setRefreshing(false);
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });

/*

        //accessToken, botId, limit, offset, true
        RestRequest<ServerBotMsgResponse> request = new RestRequest<ServerBotMsgResponse>(ServerBotMsgResponse.class, null, null) {
            @Override
            public ServerBotMsgResponse loadDataFromNetwork() throws Exception {
                ServerBotMsgResponse re = new ServerBotMsgResponse();
                try {
                    BotHistory history = getService().getHistory("bearer " + SocketWrapper.getInstance(getActivity().getApplicationContext()).getAccessToken(),
                            SDKConfiguration.Client.bot_id, limit, _offset, true);

                    List<BotHistoryMessage> messages = history.getMessages();
                    ArrayList<BaseBotMessage> msgs = null;
                    if (messages != null && messages.size() > 0) {
                        msgs = new ArrayList<>();
                        for (int index = 0; index < messages.size(); index++) {
                            BotHistoryMessage msg = messages.get(index);
                            if (msg.getType().equals(BotResponse.MESSAGE_TYPE_OUTGOING)) {
                                List<Component> components = msg.getComponents();
                                String data = components.get(0).getData().getText();
                                try {
                                    PayloadOuter outer = gson.fromJson(data, PayloadOuter.class);
                                    BotResponse r = Utils.buildBotMessage(outer, msg.getBotId(), Client.bot_name, msg.getCreatedOn(), msg.getId());
                                    r.setType(msg.getType());
                                    msgs.add(r);
                                } catch (com.google.gson.JsonSyntaxException ex) {
                                    BotResponse r = Utils.buildBotMessage(data, msg.getBotId(), Client.bot_name, msg.getCreatedOn(), msg.getId());
                                    r.setType(msg.getType());
                                    msgs.add(r);
                                }
                            } else {
                                try {
                                    String message = msg.getComponents().get(0).getData().getText();
                                    RestResponse.BotMessage botMessage = new RestResponse.BotMessage(message);
                                    RestResponse.BotPayLoad botPayLoad = new RestResponse.BotPayLoad();
                                    botPayLoad.setMessage(botMessage);
                                    BotInfoModel botInfo = new BotInfoModel(Client.bot_name, Client.bot_id, null);
                                    botPayLoad.setBotInfo(botInfo);
                                    Gson gson = new Gson();
                                    String jsonPayload = gson.toJson(botPayLoad);

                                    BotRequest botRequest = gson.fromJson(jsonPayload, BotRequest.class);
                                    long cTime = DateUtils.isoFormatter.parse(((BotHistoryMessage) msg).getCreatedOn()).getTime() + TimeZone.getDefault().getRawOffset();
                                    String createdTime = DateUtils.isoFormatter.format(new Date(cTime));
                                    botRequest.setCreatedOn(createdTime);
                                    msgs.add(botRequest);

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        re.setBotMessages(msgs);
                        re.setOriginalSize(messages != null ? messages.size() : 0);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                return re;
            }
        };

        getSpiceManager().execute(request, new RequestListener<ServerBotMsgResponse>() {
            @Override
            public void onRequestFailure(SpiceException e) {
                //        dismissProgress();
                fetching = false;
                if (swipeRefreshLayout != null) {
                    swipeRefreshLayout.setRefreshing(false);
                }
            }

            @Override
            public void onRequestSuccess(ServerBotMsgResponse re) {

                fetching = false;

                if (swipeRefreshLayout != null) {
                    swipeRefreshLayout.setRefreshing(false);
                }

                ArrayList<BaseBotMessage> list = null;
                if (re != null) {
                    list = re.getBotMessages();
                    offset = _offset + re.getOriginalSize();
                }

                if (list != null && list.size() > 0) {
                    addMessagesToBotChatAdapter(list, offset == 0);
                }

                if ((list == null || list.size() < limit) && offset != 0) {
                    hasMore = false;
                }
            }
        });*/

    }
}
