package kore.botssdk.fragment;

import android.content.Context;
import android.content.SharedPreferences;
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
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;
import java.util.TimeZone;

import kore.botssdk.R;
import kore.botssdk.adapter.ChatAdapter;
import kore.botssdk.itemdecoration.ChatAdapterItemDecoration;
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
import kore.botssdk.repository.history.HistoryRepository;
import kore.botssdk.utils.BundleUtils;
import kore.botssdk.utils.DateUtils;
import kore.botssdk.utils.StringUtils;
import kore.botssdk.view.CircularProfileView;
import kore.botssdk.view.DotsTextView;
import kore.botssdk.view.QuickReplyView;
import kore.botssdk.viewmodels.content.BotContentViewModel;
import kore.botssdk.viewmodels.content.BotContentViewModelFactory;
import kore.botssdk.websocket.SocketWrapper;

@SuppressWarnings("UnKnownNullness")
public class NewBotContentFragment extends Fragment implements BotContentFragmentUpdate {
    RelativeLayout rvChatContent, botHeaderLayout;
    RecyclerView botsBubblesListView;
    ChatAdapter botsChatAdapter;
    QuickReplyView quickReplyView;
    LinearLayout botTypingStatusRl;
    private CircularProfileView botTypingStatusIcon;
    private DotsTextView typingStatusItemDots;
    ComposeFooterInterface composeFooterInterface;
    InvokeGenericWebViewInterface invokeGenericWebViewInterface;
    boolean shallShowProfilePic;
    private String mChannelIconURL;
    private String mBotNameInitials;
    TTSUpdate ttsUpdate;
    private int mBotIconId;
    boolean fetching = false;
    TextView headerView;
    SwipeRefreshLayout swipeRefreshLayout;
    LinearLayoutManager mLayoutManager;
    int offset = 0;
    SharedPreferences sharedPreferences;
    long today;
    long nextMonth;
    long janThisYear;
    long decThisYear;
    long oneYearForward;
    Pair<Long, Long> todayPair;
    Pair<Long, Long> nextMonthPair;
    TextView tvChaseTitle;
    String jwt;
    BotContentViewModel mContentViewModel;
    private LinearLayoutManager layoutManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.bot_content_layout, null);
        findViews(view);
        getBundleInfo();
        initializeBotTypingStatus(view, mChannelIconURL);
        setupAdapter();

        BotContentViewModelFactory factory = new BotContentViewModelFactory(requireActivity(), NewBotContentFragment.this, new HistoryRepository(requireActivity(), NewBotContentFragment.this));
        mContentViewModel = new ViewModelProvider(this, factory).get(BotContentViewModel.class);

        return view;
    }

    private void findViews(View view) {
        rvChatContent = view.findViewById(R.id.rvChatContent);
        botsBubblesListView = view.findViewById(R.id.chatContentListView);
        mLayoutManager = (LinearLayoutManager) botsBubblesListView.getLayoutManager();
        headerView = view.findViewById(R.id.filesSectionHeader);
        swipeRefreshLayout = view.findViewById(R.id.swipeContainerChat);
        quickReplyView = view.findViewById(R.id.quick_reply_view);
        tvChaseTitle = view.findViewById(R.id.tvChaseTitle);
        botHeaderLayout = view.findViewById(R.id.header_layout);
        headerView.setVisibility(View.GONE);
        tvChaseTitle.setText(Html.fromHtml(SDKConfiguration.Client.bot_name));
        sharedPreferences = requireActivity().getSharedPreferences(BotResponse.THEME_NAME, Context.MODE_PRIVATE);
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light, android.R.color.holo_orange_light, android.R.color.holo_red_light);
        botsBubblesListView.getRecycledViewPool().setMaxRecycledViews(0, 0);
        botsBubblesListView.setItemViewCacheSize(100);
        botsBubblesListView.setItemAnimator(null);
    }

    public void setJwtTokenForWebHook(String jwt) {
        if (!StringUtils.isNullOrEmpty(jwt)) this.jwt = jwt;
    }

    public void changeThemeBackGround(String bgColor, String textBgColor, String textColor, String botName) {
        if (!StringUtils.isNullOrEmpty(bgColor)) {
            rvChatContent.setBackgroundColor(Color.parseColor(bgColor));
        }

        botHeaderLayout.setBackgroundColor(Color.parseColor(textBgColor));
        tvChaseTitle.setTextColor(Color.parseColor(textColor));

        if (!StringUtils.isNullOrEmpty(botName)) tvChaseTitle.setText(botName);

        if (SDKConfiguration.OverrideKoreConfig.paginated_scroll_enable) {
            swipeRefreshLayout.setEnabled(true);
            swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    if (botsChatAdapter != null)
                        loadChatHistory(botsChatAdapter.getItemCount(), SDKConfiguration.OverrideKoreConfig.paginated_scroll_batch_size);
                    else loadChatHistory(0, SDKConfiguration.OverrideKoreConfig.paginated_scroll_batch_size);
                }
            });
        } else {
            swipeRefreshLayout.setEnabled(false);
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    private void setupAdapter() {
        botsChatAdapter = new ChatAdapter();
        botsChatAdapter.setComposeFooterInterface(composeFooterInterface);
        botsChatAdapter.setInvokeGenericWebViewInterface(invokeGenericWebViewInterface);
        botsBubblesListView.addItemDecoration(new ChatAdapterItemDecoration());
        layoutManager = new LinearLayoutManager(requireContext());
        botsBubblesListView.setLayoutManager(layoutManager);
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
            mBotNameInitials = bundle.getString(BundleUtils.BOT_NAME_INITIALS, "B");
            mBotIconId = bundle.getInt(BundleUtils.BOT_ICON_ID, -1);
        }
    }

    public void showTypingStatus(BotResponse botResponse) {
        if (botTypingStatusRl != null && botResponse.getMessage() != null && !botResponse.getMessage().isEmpty()) {
            if (typingStatusItemDots != null) {
                botTypingStatusRl.setVisibility(View.VISIBLE);
                typingStatusItemDots.start();
                if (StringUtils.isNullOrEmpty(mChannelIconURL) && !StringUtils.isNullOrEmpty(botResponse.getIcon())) {
                    mChannelIconURL = botResponse.getIcon();
                    botTypingStatusIcon.populateLayout(mBotNameInitials, mChannelIconURL, null, -1, Color.parseColor(SDKConfiguration.BubbleColors.quickReplyColor), true);
                }
            }
        }
    }

    public void showTypingStatus() {
        if (typingStatusItemDots != null) {
            botTypingStatusRl.setVisibility(View.VISIBLE);
            typingStatusItemDots.start();
        }
    }

    public void setQuickRepliesIntoFooter(BotResponse botResponse) {
        quickReplyView.setVisibility(View.VISIBLE);
        ArrayList<QuickReplyTemplate> quickReplyTemplates = getQuickReplies(botResponse);
        quickReplyView.populateQuickReplyView(quickReplyTemplates);
    }

    public void showCalendarIntoFooter(BotResponse botResponse) {
        if (botResponse != null && botResponse.getMessage() != null && !botResponse.getMessage().isEmpty()) {
            ComponentModel compModel = botResponse.getMessage().get(0).getComponent();
            if (compModel != null) {
                String compType = compModel.getType();
                if (BotResponse.COMPONENT_TYPE_TEMPLATE.equalsIgnoreCase(compType)) {
                    PayloadOuter payOuter = compModel.getPayload();
                    PayloadInner payInner = payOuter.getPayload();
                    if (payInner != null && BotResponse.TEMPLATE_TYPE_DATE.equalsIgnoreCase(payInner.getTemplate_type())) {
                        MaterialDatePicker.Builder<Long> builder = MaterialDatePicker.Builder.datePicker();
                        builder.setTitleText(payInner.getTitle());
                        builder.setInputMode(MaterialDatePicker.INPUT_MODE_CALENDAR);
                        builder.setCalendarConstraints(mContentViewModel.minRange(payInner.getStartDate(), payInner.getEndDate(), payInner.getFormat()).build());
                        builder.setTheme(R.style.MyMaterialCalendarTheme);

                        try {
                            MaterialDatePicker<Long> picker = builder.build();
                            picker.show(requireActivity().getSupportFragmentManager(), picker.toString());

                            picker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
                                @Override
                                public void onPositiveButtonClick(Long selection) {
                                    Calendar calendar = Calendar.getInstance();
                                    calendar.setTimeInMillis(selection);
                                    int stYear = calendar.get(Calendar.YEAR);
                                    int stMonth = calendar.get(Calendar.MONTH);
                                    int stDay = calendar.get(Calendar.DAY_OF_MONTH);

                                    String formattedDate = ((stMonth + 1) > 9 ? (stMonth + 1) : "0" + (stMonth + 1)) + "-" + (stDay > 9 ? stDay : "0" + stDay) + "-" + stYear;

                                    composeFooterInterface.onSendClick(formattedDate, false);
                                }
                            });
                        } catch (IllegalArgumentException e) {
                            e.printStackTrace();
                        }
                    } else if (payInner != null && BotResponse.TEMPLATE_TYPE_DATE_RANGE.equalsIgnoreCase(payInner.getTemplate_type())) {
                        initSettings();
                        MaterialDatePicker.Builder<Pair<Long, Long>> builder = MaterialDatePicker.Builder.dateRangePicker();
                        builder.setTitleText(payInner.getTitle());
                        builder.setInputMode(MaterialDatePicker.INPUT_MODE_CALENDAR);
                        builder.setCalendarConstraints(mContentViewModel.minRange(payInner.getStartDate(), payInner.getEndDate(), payInner.getFormat()).build());
                        builder.setTheme(R.style.MyMaterialCalendarTheme);

                        try {
                            MaterialDatePicker<Pair<Long, Long>> picker = builder.build();
                            picker.show(requireActivity().getSupportFragmentManager(), picker.toString());
                            picker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Pair<Long, Long>>() {
                                @Override
                                public void onPositiveButtonClick(Pair<Long, Long> selection) {
                                    Long startDate = selection.first;
                                    Long endDate = selection.second;

                                    Calendar cal = Calendar.getInstance();
                                    cal.setTimeInMillis(startDate);
                                    int strYear = cal.get(Calendar.YEAR);
                                    int strMonth = cal.get(Calendar.MONTH);
                                    int strDay = cal.get(Calendar.DAY_OF_MONTH);

                                    cal.setTimeInMillis(endDate);
                                    int endYear = cal.get(Calendar.YEAR);
                                    int endMonth = cal.get(Calendar.MONTH);
                                    int endDay = cal.get(Calendar.DAY_OF_MONTH);

                                    String formatedDate = DateUtils.getMonthName(strMonth) + " " + strDay + DateUtils.getDayOfMonthSuffix(strDay) + ", " + strYear;
                                    formatedDate = formatedDate + " to " + DateUtils.getMonthName(endMonth) + " " + endDay + DateUtils.getDayOfMonthSuffix(endDay) + ", " + endYear;
                                    composeFooterInterface.onSendClick(formatedDate, false);
                                }
                            });
                        } catch (IllegalArgumentException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
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

    public int getAdapterCount() {
        if (botsChatAdapter != null) return botsChatAdapter.getItemCount();
        return 0;
    }

    public void addMessageToBotChatAdapter(BotResponse botResponse) {
        botsChatAdapter.addBaseBotMessage(botResponse);
        botTypingStatusRl.setVisibility(View.GONE);
        botsBubblesListView.smoothScrollToPosition(botsChatAdapter.getItemCount());
    }

    protected void initializeBotTypingStatus(View view, String mChannelIconURL) {
        botTypingStatusRl = view.findViewById(R.id.botTypingStatus);
        botTypingStatusIcon = view.findViewById(R.id.typing_status_item_cpv);
        botTypingStatusIcon.populateLayout(mBotNameInitials, mChannelIconURL, null, mBotIconId, Color.parseColor(SDKConfiguration.BubbleColors.quickReplyColor), true);
        typingStatusItemDots = view.findViewById(R.id.typing_status_item_dots);
        typingStatusItemDots.setTextColor(Color.BLACK);
    }

    private void scrollToBottom() {
        final int count = botsChatAdapter.getItemCount();
        botsBubblesListView.post(new Runnable() {
            @Override
            public void run() {
                if (count > 0) botsBubblesListView.smoothScrollToPosition(count - 1);
            }
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

    @Override
    public void onChatHistory(ArrayList<BaseBotMessage> list, int offset, boolean scrollToBottom) {
        fetching = false;
        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.setRefreshing(false);
        }

        if (list != null) {
            this.offset = offset;
            addMessagesToBotChatAdapter(list, scrollToBottom);
        }
    }

    @Override
    public void onReconnectionChatHistory(ArrayList<BaseBotMessage> list, int offset, boolean isReconnectionHistory) {
        fetching = false;
        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.setRefreshing(false);
        }

        if (list != null) {
            this.offset = offset;
            addMessagesToBotChatAdapter(list, true, isReconnectionHistory);
        }
    }

    private void initSettings() {
        today = MaterialDatePicker.todayInUtcMilliseconds();
        Calendar calendar = getClearedUtc();
        calendar.setTimeInMillis(today);
        calendar.roll(Calendar.MONTH, 1);
        nextMonth = calendar.getTimeInMillis();

        calendar.setTimeInMillis(today);
        calendar.set(Calendar.MONTH, Calendar.JANUARY);
        janThisYear = calendar.getTimeInMillis();
        calendar.setTimeInMillis(today);
        calendar.set(Calendar.MONTH, Calendar.DECEMBER);
        decThisYear = calendar.getTimeInMillis();

        calendar.setTimeInMillis(today);
        calendar.roll(Calendar.YEAR, 1);
        oneYearForward = calendar.getTimeInMillis();

        todayPair = new Pair<>(today, today);
        nextMonthPair = new Pair<>(nextMonth, nextMonth);
    }

    private static Calendar getClearedUtc() {
        Calendar utc = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        utc.clear();
        return utc;
    }

    /* Limit selectable Date range */

    public void loadChatHistory(final int _offset, final int limit) {
        if (fetching) {
            if (swipeRefreshLayout != null) {
                swipeRefreshLayout.setRefreshing(false);
            }
            return;
        }
        fetching = true;
        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.setRefreshing(true);
        }

        if (!SDKConfiguration.Client.isWebHook)
            mContentViewModel.loadChatHistory(_offset, limit, SocketWrapper.getInstance(requireActivity().getApplicationContext()).getAccessToken());
        else mContentViewModel.loadChatHistory(_offset, limit, jwt);
    }

    public void loadReconnectionChatHistory(final int _offset, final int limit) {
        if (fetching) {
            if (swipeRefreshLayout != null) {
                swipeRefreshLayout.setRefreshing(false);
            }
            return;
        }
        fetching = true;
        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.setRefreshing(true);
        }

        if (!SDKConfiguration.Client.isWebHook)
            mContentViewModel.loadReconnectionChatHistory(_offset, limit, SocketWrapper.getInstance(requireActivity().getApplicationContext()).getAccessToken(), botsChatAdapter.getBaseBotMessageArrayList());
        else mContentViewModel.loadReconnectionChatHistory(_offset, limit, jwt, botsChatAdapter.getBaseBotMessageArrayList());
    }

}