package kore.botssdk.fragment;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.util.Locale;

import kore.botssdk.R;
import kore.botssdk.adapter.BotsChatAdapter;
import kore.botssdk.listener.BotContentFragmentUpdate;
import kore.botssdk.models.BotRequest;
import kore.botssdk.models.BotResponse;
import kore.botssdk.utils.BundleUtils;
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
    private TextToSpeech textToSpeech;

    boolean shallShowProfilePic;
    private String mChannelIconURL;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.bot_content_layout, null);
        findViews(view);
        getBundleInfo();
        initializeBotTypingStatus(view, mChannelIconURL);
        setupAdapter();
        setupTextToSpeech();
        return view;
    }

    private void findViews(View view) {
        botsBubblesListView = (ListView) view.findViewById(R.id.botsBubblesListView);
    }

    private void setupAdapter() {
        botsChatAdapter = new BotsChatAdapter(getActivity());
        botsBubblesListView.setAdapter(botsChatAdapter);
        botsChatAdapter.setShallShowProfilePic(shallShowProfilePic);
    }

    private void setupTextToSpeech() {
        textToSpeech = new TextToSpeech(getActivity(), new TextToSpeech.OnInitListener() {

            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    textToSpeech.setLanguage(Locale.US);
                }
            }
        });
    }

    private void getBundleInfo() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            shallShowProfilePic = bundle.getBoolean(BundleUtils.SHOW_PROFILE_PIC, false);
            mChannelIconURL = bundle.getString(BundleUtils.CHANNEL_ICON_URL);
        }
    }

    @Override
    public void onPause() {
        if (textToSpeech != null) {
            textToSpeech.stop();
        }
        super.onPause();
    }

    public void addMessageToBotChatAdapter(final BotResponse botResponse) {
        if (botTypingStatusRl != null && botResponse.getMessage() != null && !botResponse.getMessage().isEmpty()) {
            botTypingStatusRl.setVisibility(View.VISIBLE);
            botTypingStatusRl.postDelayed(new Runnable() {
                @Override
                public void run() {
                    botsChatAdapter.addBaseBotMessage(botResponse);
//                        scrollToBottom();
                    botTypingStatusRl.setVisibility(View.GONE);
                    botsBubblesListView.smoothScrollToPosition(botsChatAdapter.getCount());
                    textToSpeech(botResponse);
                }
            }, 2000);
        }
        if (typingStatusItemDots != null && botResponse.getMessage() != null && !botResponse.getMessage().isEmpty()) {
            typingStatusItemDots.start();
            Log.d("Hey", "Started animation");
        }
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
            botsChatAdapter.addBaseBotMessage(botRequest);
            scrollToBottom();
        }
    }

    private void textToSpeech(BotResponse botResponse) {
        if (botResponse.getMessage() != null && !botResponse.getMessage().isEmpty()) {
            String botResponseTextualFormat = botResponse.getTempMessage().getcInfo().getBody();
            if (textToSpeech != null) {
                textToSpeech.stop();
            }
            textToSpeech.speak(botResponseTextualFormat, TextToSpeech.QUEUE_FLUSH, null);
        }
    }
}
