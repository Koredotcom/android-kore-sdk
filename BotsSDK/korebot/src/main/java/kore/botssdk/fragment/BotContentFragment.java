package kore.botssdk.fragment;

import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;

import kore.botssdk.R;
import kore.botssdk.adapter.ChatAdapter;
import kore.botssdk.listener.BotContentFragmentUpdate;
import kore.botssdk.listener.ComposeFooterInterface;
import kore.botssdk.listener.InvokeGenericWebViewInterface;
import kore.botssdk.listener.TTSUpdate;
import kore.botssdk.models.BaseBotMessage;
import kore.botssdk.models.BotRequest;
import kore.botssdk.models.BotResponse;
import kore.botssdk.models.ComponentModel;
import kore.botssdk.models.PayloadInner;
import kore.botssdk.models.PayloadOuter;
import kore.botssdk.models.QuickReplyTemplate;
import kore.botssdk.net.SDKConfiguration;
import kore.botssdk.utils.BundleUtils;
import kore.botssdk.view.CircularProfileView;
import kore.botssdk.view.QuickReplyView;
import kore.botssdk.views.DotsTextView;

/**
 * Created by Pradeep Mahato on 31-May-16.
 * Copyright (c) 2014 Kore Inc. All rights reserved.
 */
public class BotContentFragment extends BaseSpiceFragment implements BotContentFragmentUpdate {

    RecyclerView botsBubblesListView;
    ChatAdapter botsChatAdapter;
    QuickReplyView quickReplyView;
    String LOG_TAG = BotContentFragment.class.getSimpleName();
    private LinearLayout botTypingStatusRl;
    private CircularProfileView botTypingStatusIcon;
    private DotsTextView typingStatusItemDots;
    ComposeFooterInterface composeFooterInterface;
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
    private LinearLayoutManager mLayoutManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.bot_content_layout, null);
        findViews(view);
        getBundleInfo();
        initializeBotTypingStatus(view, mChannelIconURL);
        setupAdapter();
        loadChatHistory(0,limit);
        return view;
    }
    private void findViews(View view) {
        botsBubblesListView = (RecyclerView) view.findViewById(R.id.chatContentListView);
        headerView = view.findViewById(R.id.filesSectionHeader);
        botsBubblesListView.addOnScrollListener(onScrollListener);
        quickReplyView = view.findViewById(R.id.quick_reply_view);
        mLayoutManager = (LinearLayoutManager) botsBubblesListView.getLayoutManager();
    }

    private void setupAdapter() {
        botsChatAdapter = new ChatAdapter(getActivity());
        botsChatAdapter.setComposeFooterInterface(composeFooterInterface);
        botsChatAdapter.setInvokeGenericWebViewInterface(invokeGenericWebViewInterface);
        botsChatAdapter.setActivityContext(getActivity());
        botsBubblesListView.setAdapter(botsChatAdapter);
        quickReplyView.setComposeFooterInterface(composeFooterInterface);
        quickReplyView.setInvokeGenericWebViewInterface(invokeGenericWebViewInterface);
    }

    public void setTtsUpdate(TTSUpdate ttsUpdate) {
        this.ttsUpdate = ttsUpdate;
    }

    public void setComposeFooterInterface(ComposeFooterInterface composeFooterInterface) {
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
            mBotNameInitials = bundle.getString(BundleUtils.BOT_NAME_INITIALS,"B");
            mBotIconId = bundle.getInt(BundleUtils.BOT_ICON_ID,-1);
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
        botsBubblesListView.smoothScrollToPosition(botsChatAdapter.getItemCount());
    }

    protected void initializeBotTypingStatus(View view, String mChannelIconURL) {
        botTypingStatusRl = (LinearLayout) view.findViewById(R.id.botTypingStatus);
        botTypingStatusIcon = (CircularProfileView) view.findViewById(R.id.typing_status_item_cpv);
        botTypingStatusIcon.populateLayout(mBotNameInitials, mChannelIconURL, null, mBotIconId, Color.parseColor(SDKConfiguration.BubbleColors.quickReplyColor), true);
        typingStatusItemDots = (DotsTextView) view.findViewById(R.id.typing_status_item_dots);
    }

    private void scrollToBottom() {
        final int count = botsChatAdapter.getItemCount();
        botsBubblesListView.post(new Runnable() {
            @Override
            public void run() {
                botsBubblesListView.scrollToPosition(count - 1);
            }
        });
    }


    private int limit = 30;
    RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            int pastVisiblesItems = mLayoutManager.findFirstVisibleItemPosition();

            if (dy > 0) //check for scroll down
            {
                if (!fetching && hasMore) {
                    if (pastVisiblesItems <= 10) {
                        fetching = true;
                        loadChatHistory(botsChatAdapter.getItemCount(), limit);
                    }
                }
            }
        }
    };


    public void addMessagesToBotChatAdapter(ArrayList<BaseBotMessage> list,boolean scrollToBottom) {
        botsChatAdapter.addBaseBotMessages(list);
        if(scrollToBottom) {
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
    private void loadChatHistory(final int offset, final int limit){
        if(fetching)return;
        fetching = true;

        /*spiceDBManager.execute(new BotChatsDBRequest(getActivity().getApplicationContext(), null, offset, limit), new RequestListener<BotChatDBResponse>() {
            @Override
            public void onRequestFailure(SpiceException e) {
                fetching = false;
            }

            @Override
            public void onRequestSuccess(BotChatDBResponse list) {
                fetching = false;
                if(list != null && list.size() > 0){
                    ArrayList<BaseBotMessage> baseBotMessages = new ArrayList<>(list.size());
                    for(BotMessageDBModel botMessageDBModel : list){
                        if(botMessageDBModel.isSentMessage()){
                            botMessageDBModel.dProcessMesssage();
                            String message = gson.toJson(botMessageDBModel.getMessage().get(0));
                            BotRequest botRequest = gson.fromJson(message,BotRequest.class);
                            baseBotMessages.add(botRequest);
                        }else {
                            botMessageDBModel.dProcessMesssage();
                            String message = gson.toJson(botMessageDBModel);
                            BotResponse botResponse = gson.fromJson(message,BotResponse.class);
                            baseBotMessages.add(botResponse);
                        }

                    }
                    Collections.reverse(baseBotMessages);
                    addMessagesToBotChatAdapter(baseBotMessages,offset == 0);

                }
                if((list == null || list.size() < limit) && offset != 0){
                    hasMore = false;
                }
            }
        });*/


    }
}
