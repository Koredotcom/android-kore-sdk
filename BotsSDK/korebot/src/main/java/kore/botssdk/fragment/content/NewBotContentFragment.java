package kore.botssdk.fragment.content;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;
import java.util.Objects;

import kore.botssdk.R;
import kore.botssdk.adapter.ChatAdapter;
import kore.botssdk.itemdecoration.ChatAdapterItemDecoration;
import kore.botssdk.models.BaseBotMessage;
import kore.botssdk.models.BotRequest;
import kore.botssdk.models.BotResponse;
import kore.botssdk.models.ComponentModel;
import kore.botssdk.models.PayloadInner;
import kore.botssdk.models.PayloadOuter;
import kore.botssdk.models.QuickReplyTemplate;
import kore.botssdk.net.SDKConfiguration;
import kore.botssdk.repository.history.HistoryRepository;
import kore.botssdk.utils.BundleUtils;
import kore.botssdk.utils.StringUtils;
import kore.botssdk.view.CircularProfileView;
import kore.botssdk.view.DotsTextView;
import kore.botssdk.view.QuickReplyView;
import kore.botssdk.viewmodels.content.BotContentViewModel;
import kore.botssdk.viewmodels.content.BotContentViewModelFactory;

@SuppressWarnings("UnKnownNullness")
public class NewBotContentFragment extends BaseContentFragment {
    private RelativeLayout rvChatContent;
    private RelativeLayout botHeaderLayout;
    private RecyclerView botsBubblesListView;
    private QuickReplyView quickReplyView;
    private LinearLayout botTypingStatusRl;
    private DotsTextView typingStatusItemDots;
    private String mChannelIconURL;
    private String mBotNameInitials;
    private int mBotIconId;
    private TextView tvChaseTitle;
    private LinearLayoutManager layoutManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bot_content_layout, null);
        getBundleInfo();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findViews(view);
        initializeBotTypingStatus(view, mChannelIconURL);
        setupAdapter();
    }

    @Override
    protected SwipeRefreshLayout getSwipeRefreshLayout(View view) {
        return view.findViewById(R.id.swipeContainerChat);
    }

    @Override
    protected ChatAdapter getChatAdapter() {
        return new ChatAdapter();
    }

    private void findViews(View view) {
        rvChatContent = view.findViewById(R.id.rvChatContent);
        botsBubblesListView = view.findViewById(R.id.chatContentListView);
        TextView headerView = view.findViewById(R.id.filesSectionHeader);
        quickReplyView = view.findViewById(R.id.quick_reply_view);
        tvChaseTitle = view.findViewById(R.id.tvChaseTitle);
        botHeaderLayout = view.findViewById(R.id.header_layout);
        headerView.setVisibility(View.GONE);
        tvChaseTitle.setText(Html.fromHtml(SDKConfiguration.Client.bot_name));
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light, android.R.color.holo_orange_light, android.R.color.holo_red_light);
        botsBubblesListView.getRecycledViewPool().setMaxRecycledViews(0, 0);
        botsBubblesListView.setItemViewCacheSize(100);
        botsBubblesListView.setItemAnimator(null);
    }

    @Override
    public void changeThemeBackGround(String bgColor, String textBgColor, String textColor, String botName) {
        if (!StringUtils.isNullOrEmpty(bgColor)) {
            rvChatContent.setBackgroundColor(Color.parseColor(bgColor));
        }

        botHeaderLayout.setBackgroundColor(Color.parseColor(textBgColor));
        tvChaseTitle.setTextColor(Color.parseColor(textColor));

        if (!StringUtils.isNullOrEmpty(botName)) tvChaseTitle.setText(botName);

        if (SDKConfiguration.OverrideKoreConfig.paginated_scroll_enable) {
            swipeRefreshLayout.setEnabled(true);
            swipeRefreshLayout.setOnRefreshListener(() -> {
                if (botsChatAdapter != null)
                    loadChatHistory(botsChatAdapter.getItemCount(), SDKConfiguration.OverrideKoreConfig.paginated_scroll_batch_size);
                else loadChatHistory(0, SDKConfiguration.OverrideKoreConfig.paginated_scroll_batch_size);
            });
        } else {
            swipeRefreshLayout.setEnabled(false);
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    private void setupAdapter() {
        botsBubblesListView.addItemDecoration(new ChatAdapterItemDecoration());
        layoutManager = new LinearLayoutManager(requireContext());
        botsBubblesListView.setLayoutManager(layoutManager);
        botsBubblesListView.setAdapter(botsChatAdapter);
        quickReplyView.setComposeFooterInterface(composeFooterInterface);
        quickReplyView.setInvokeGenericWebViewInterface(invokeGenericWebViewInterface);
    }

    private void getBundleInfo() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            mChannelIconURL = bundle.getString(BundleUtils.CHANNEL_ICON_URL);
            mBotNameInitials = bundle.getString(BundleUtils.BOT_NAME_INITIALS, "B");
            mBotIconId = bundle.getInt(BundleUtils.BOT_ICON_ID, -1);
        }
    }

    @Override
    public void showTypingStatus() {
        if (typingStatusItemDots != null) {
            botTypingStatusRl.setVisibility(View.VISIBLE);
            typingStatusItemDots.start();
        }
    }

    @Override
    public void setQuickRepliesIntoFooter(BotResponse botResponse) {
        quickReplyView.setVisibility(View.VISIBLE);
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
        botTypingStatusRl = view.findViewById(R.id.botTypingStatus);
        CircularProfileView botTypingStatusIcon = view.findViewById(R.id.typing_status_item_cpv);
        botTypingStatusIcon.populateLayout(mBotNameInitials, mChannelIconURL, null, mBotIconId, Color.parseColor(SDKConfiguration.BubbleColors.quickReplyColor), true);
        typingStatusItemDots = view.findViewById(R.id.typing_status_item_dots);
        typingStatusItemDots.setTextColor(Color.BLACK);
    }

    private void scrollToBottom() {
        final int count = botsChatAdapter.getItemCount();
        botsBubblesListView.post(() -> {
            if (count > 0) botsBubblesListView.smoothScrollToPosition(count - 1);
        });
    }

    public void addMessagesToBotChatAdapter(ArrayList<BaseBotMessage> list, boolean scrollToBottom) {
        int firstItemPosition = 0;
        if (botsChatAdapter.getItemCount() > 0) {
            firstItemPosition = ((LinearLayoutManager) Objects.requireNonNull(botsBubblesListView.getLayoutManager())).findFirstVisibleItemPosition();
        }
        botsChatAdapter.addBaseBotMessages(list);
        if (scrollToBottom) {
            scrollToBottom();
        } else if (botsChatAdapter.getItemCount() > 0) {
            int finalFirstItemPosition = firstItemPosition + list.size();
            botsBubblesListView.post(() -> layoutManager.scrollToPositionWithOffset(finalFirstItemPosition, 0));
        }
    }

    public void addMessagesToBotChatAdapter(@NonNull ArrayList<BaseBotMessage> list, boolean scrollToBottom, boolean isFirst) {
        int firstItemPosition = 0;
        if (botsChatAdapter.getItemCount() > 0) {
            firstItemPosition = ((LinearLayoutManager) Objects.requireNonNull(botsBubblesListView.getLayoutManager())).findFirstVisibleItemPosition();
        }
        if (isFirst) botsChatAdapter.addBaseBotMessages(list);
        else botsChatAdapter.addMissedBaseBotMessages(list);

        if (scrollToBottom) {
            scrollToBottom();
        } else if (botsChatAdapter.getItemCount() > 0) {
            int finalFirstItemPosition = firstItemPosition + list.size();
            botsBubblesListView.post(() -> layoutManager.scrollToPositionWithOffset(finalFirstItemPosition, 0));
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
}