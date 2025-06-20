package kore.botssdk.fragment.content;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;

import java.util.ArrayList;

import kore.botssdk.R;
import kore.botssdk.itemdecoration.ChatAdapterItemDecoration;
import kore.botssdk.models.BaseBotMessage;
import kore.botssdk.models.BotBrandingModel;
import kore.botssdk.models.BotRequest;
import kore.botssdk.models.BotResponse;
import kore.botssdk.models.BrandingBodyModel;
import kore.botssdk.models.QuickReplyTemplate;
import kore.botssdk.net.SDKConfiguration;
import kore.botssdk.utils.BundleConstants;
import kore.botssdk.utils.BundleUtils;
import kore.botssdk.utils.StringUtils;
import kore.botssdk.view.CircularProfileView;
import kore.botssdk.view.QuickReplyView;
import kore.botssdk.viewmodels.content.BotContentViewModel;
import kore.botssdk.viewmodels.content.BotContentViewModelFactory;

@SuppressWarnings("UnKnownNullness")
public class BotContentFragment extends BaseContentFragment {
    private RelativeLayout rlBody;
    private RecyclerView botsBubblesListView;
    private QuickReplyView quickReplyView;
    private LinearLayout botTypingStatusRl;
    private SharedPreferences sharedPreferences;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = View.inflate(requireActivity(), R.layout.bot_content_layout, null);
        BotContentViewModelFactory factory = new BotContentViewModelFactory(requireActivity(), BotContentFragment.this);
        mContentViewModel = new ViewModelProvider(this, factory).get(BotContentViewModel.class);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findViews(view);
        initializeBotTypingStatus(view, mChannelIconURL);
        setupAdapter();
    }

    private void findViews(View view) {
        botsBubblesListView = view.findViewById(R.id.chatContentListView);
        TextView headerView = view.findViewById(R.id.filesSectionHeader);
        quickReplyView = view.findViewById(R.id.quick_reply_view);
        rlBody = view.findViewById(R.id.rlBody);

        headerView.setVisibility(View.GONE);
        sharedPreferences = requireActivity().getSharedPreferences(BotResponse.THEME_NAME, Context.MODE_PRIVATE);
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light, android.R.color.holo_orange_light, android.R.color.holo_red_light);
        botsBubblesListView.setItemViewCacheSize(100);
        botsBubblesListView.setItemAnimator(null);
    }

    private void setupAdapter() {
        botsBubblesListView.addItemDecoration(new ChatAdapterItemDecoration());
        botsBubblesListView.setAdapter(botsChatAdapter);
        quickReplyView.setComposeFooterInterface(composeFooterInterface);
        quickReplyView.setInvokeGenericWebViewInterface(invokeGenericWebViewInterface);
    }

    @Override
    public void setBotBrandingModel(BotBrandingModel botBrandingModel) {
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
        if (botBrandingModel != null) {
            if (botBrandingModel.getBody() != null) {
                BrandingBodyModel bodyModel = botBrandingModel.getBody();
                if (bodyModel != null) {
                    if (bodyModel.getBackground() != null && !StringUtils.isNullOrEmpty(bodyModel.getBackground().getType())) {
                        if (BundleConstants.COLOR.equalsIgnoreCase(bodyModel.getBackground().getType()) && !StringUtils.isNullOrEmpty(bodyModel.getBackground().getColor())) {
                            rlBody.setBackgroundColor(Color.parseColor(bodyModel.getBackground().getColor()));
                        } else if (!StringUtils.isNullOrEmpty(bodyModel.getBackground().getImg())) {
                            Glide.with(requireActivity()).load(bodyModel.getBackground().getImg()).into(new CustomTarget<Drawable>() {
                                @Override
                                public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                    rlBody.setBackground(resource);
                                }

                                @Override
                                public void onLoadCleared(@Nullable Drawable placeholder) {
                                }
                            });
                        }
                    }

                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    if (bodyModel.getTime_stamp() != null) {
                        if (!StringUtils.isNullOrEmpty(bodyModel.getTime_stamp().getColor())) {
                            editor.putString(BotResponse.TIME_STAMP_TXT_COLOR, bodyModel.getTime_stamp().getColor());
                        }

                        SDKConfiguration.Client.timeStampBottom = !bodyModel.getTime_stamp().getPosition().equalsIgnoreCase(BundleUtils.TOP);
                        SDKConfiguration.setTimeStampsRequired(bodyModel.getTime_stamp().isShow());
                    }

                    editor.apply();
                }
            }
        }
    }

    @Override
    protected SwipeRefreshLayout getSwipeRefreshLayout(View view) {
        return view.findViewById(R.id.swipeContainerChat);
    }

    @Override
    public void showTypingStatus() {
        botTypingStatusRl.setVisibility(View.VISIBLE);
    }

    @Override
    public void setQuickRepliesIntoFooter(BotResponse botResponse) {
        ArrayList<QuickReplyTemplate> quickReplyTemplates = getQuickReplies(botResponse);
        quickReplyView.populateQuickReplyView(quickReplyTemplates);
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
    }

    private void scrollToBottom() {
        final int count = botsChatAdapter.getItemCount();
        if (count > 0)
            botsBubblesListView.post(() -> botsBubblesListView.smoothScrollToPosition(count - 1));
    }

    public void addMessagesToBotChatAdapter(ArrayList<BaseBotMessage> list, boolean scrollToBottom) {
        botsChatAdapter.addBaseBotMessages(list);
        if (scrollToBottom) {
            scrollToBottom();
        }
    }

    public void addMessagesToBotChatAdapter(ArrayList<BaseBotMessage> list, boolean scrollToBottom, boolean isFirst) {
        if (isFirst) botsChatAdapter.addBaseBotMessages(list);
        else botsChatAdapter.addMissedBaseBotMessages(list);

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
}
