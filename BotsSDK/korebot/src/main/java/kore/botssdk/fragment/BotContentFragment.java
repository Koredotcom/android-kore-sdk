package kore.botssdk.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

import kore.botssdk.R;
import kore.botssdk.adapter.BotsChatAdapter;
import kore.botssdk.listener.BotContentFragmentUpdate;
import kore.botssdk.listener.TTSUpdate;
import kore.botssdk.models.BotRequest;
import kore.botssdk.models.BotResponse;
import kore.botssdk.utils.BundleUtils;
import kore.botssdk.view.BotCarouselView;
import kore.botssdk.view.CircularProfileView;
import kore.botssdk.views.DotsTextView;

/**
 * Created by Pradeep Mahato on 31-May-16.
 * Copyright (c) 2014 Kore Inc. All rights reserved.
 */
public class BotContentFragment extends BaseSpiceFragment implements BotContentFragmentUpdate {

    ListView botsBubblesListView;
    BotsChatAdapter botsChatAdapter;
    String LOG_TAG = BotContentFragment.class.getSimpleName();
    private LinearLayout botTypingStatusRl;
    private CircularProfileView botTypingStatusIcon;
    private DotsTextView typingStatusItemDots;
    ComposeFooterFragment.ComposeFooterInterface composeFooterInterface;

    boolean shallShowProfilePic;
    private String mChannelIconURL;
    private TTSUpdate ttsUpdate;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.bot_content_layout, null);
        findViews(view);
        getBundleInfo();
        initializeBotTypingStatus(view, mChannelIconURL);
        setupAdapter();
        return view;
    }
    BotCarouselView botCarouselView;
    private void findViews(View view) {
        botsBubblesListView = (ListView) view.findViewById(R.id.botsBubblesListView);
        botCarouselView = (BotCarouselView) view.findViewById(R.id.botCV);
//        botCarouselView.setFragmentManager(getFragmentManager());
//        botCarouselView.setActivityContext(getActivity());
        botCarouselView.setVisibility(View.GONE);
    }

    private void setupAdapter() {
        botsChatAdapter = new BotsChatAdapter(getActivity());
        botsChatAdapter.setComposeFooterInterface(composeFooterInterface);
        botsChatAdapter.setActivityContext(getActivity());
        botsBubblesListView.setAdapter(botsChatAdapter);
        botsChatAdapter.setShallShowProfilePic(shallShowProfilePic);
    }

    public void setTtsUpdate(TTSUpdate ttsUpdate) {
        this.ttsUpdate = ttsUpdate;
    }

    public void setComposeFooterInterface(ComposeFooterFragment.ComposeFooterInterface composeFooterInterface) {
        this.composeFooterInterface = composeFooterInterface;
    }

    private void getBundleInfo() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            shallShowProfilePic = bundle.getBoolean(BundleUtils.SHOW_PROFILE_PIC, false);
            mChannelIconURL = bundle.getString(BundleUtils.CHANNEL_ICON_URL);
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

    public void addMessageToBotChatAdapter(BotResponse botResponse) {
        botsChatAdapter.addBaseBotMessage(botResponse);
        botTypingStatusRl.setVisibility(View.GONE);
        botsBubblesListView.smoothScrollToPosition(botsChatAdapter.getCount());
    }

    protected void initializeBotTypingStatus(View view, String mChannelIconURL) {
        botTypingStatusRl = (LinearLayout) view.findViewById(R.id.botTypingStatus);
        botTypingStatusIcon = (CircularProfileView) view.findViewById(R.id.typing_status_item_cpv);
        botTypingStatusIcon.populateLayout("B", mChannelIconURL, null, -1, 0, true);
        typingStatusItemDots = (DotsTextView) view.findViewById(R.id.typing_status_item_dots);
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

    @Override
    public void updateContentListOnSend(BotRequest botRequest) {
        if (botRequest.getMessage() != null) {
            if (ttsUpdate != null) {
                ttsUpdate.ttsOnStop();
            }
            if (botsChatAdapter != null) {
                botsChatAdapter.addBaseBotMessage(botRequest);
                scrollToBottom();
            }
        }
    }

}
