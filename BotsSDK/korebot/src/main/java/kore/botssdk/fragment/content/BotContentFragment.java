package kore.botssdk.fragment.content;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcel;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.CompositeDateValidator;
import com.google.android.material.datepicker.DateValidatorPointBackward;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.gson.Gson;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;
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
import kore.botssdk.itemdecoration.ChatAdapterItemDecoration;
import kore.botssdk.listener.BotContentFragmentUpdate;
import kore.botssdk.listener.ComposeFooterInterface;
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
import kore.botssdk.net.WebHookRestBuilder;
import kore.botssdk.retroresponse.ServerBotMsgResponse;
import kore.botssdk.utils.BundleUtils;
import kore.botssdk.utils.DateUtils;
import kore.botssdk.utils.StringUtils;
import kore.botssdk.utils.Utils;
import kore.botssdk.view.CircularProfileView;
import kore.botssdk.view.DotsTextView;
import kore.botssdk.view.QuickReplyView;
import kore.botssdk.websocket.SocketWrapper;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Copyright (c) 2014 Kore Inc. All rights reserved.
 */
@SuppressLint("UnknownNullness")
public class BotContentFragment extends Fragment implements BotContentFragmentUpdate {
    RelativeLayout rvChatContent;
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
    final Gson gson = new Gson();
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
    String jwt;
    private LinearLayoutManager layoutManager;

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

    private void findViews(View view) {
        rvChatContent = view.findViewById(R.id.rvChatContent);
        botsBubblesListView = view.findViewById(R.id.chatContentListView);
        mLayoutManager = (LinearLayoutManager) botsBubblesListView.getLayoutManager();
        headerView = view.findViewById(R.id.filesSectionHeader);
        swipeRefreshLayout = view.findViewById(R.id.swipeContainerChat);
        quickReplyView = view.findViewById(R.id.quick_reply_view);
        headerView.setVisibility(View.GONE);
        sharedPreferences = requireActivity().getSharedPreferences(BotResponse.THEME_NAME, Context.MODE_PRIVATE);
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light, android.R.color.holo_orange_light, android.R.color.holo_red_light);
        botsBubblesListView.getRecycledViewPool().setMaxRecycledViews(0, 0);
        botsBubblesListView.setItemViewCacheSize(100);
        botsBubblesListView.setItemAnimator(null);
    }

    public void setJwtTokenForWebHook(String jwt) {
        if (!StringUtils.isNullOrEmpty(jwt)) this.jwt = jwt;
    }

    public void changeThemeBackGround(String bgColor) {
        if (!StringUtils.isNullOrEmpty(bgColor)) {
            rvChatContent.setBackgroundColor(Color.parseColor(bgColor));
        }

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
                        builder.setCalendarConstraints(minRange(payInner.getStartDate(), payInner.getEndDate(), payInner.getFormat()).build());
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
                        builder.setCalendarConstraints(minRange(payInner.getStartDate(), payInner.getEndDate(), payInner.getFormat()).build());
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

    public void addMessagesToBotChatAdapter(ArrayList<BaseBotMessage> list, boolean scrollToBottom, boolean isFirst) {
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

    }

    @Override
    public void onReconnectionChatHistory(ArrayList<BaseBotMessage> list, int offset, boolean isReconnectionHistory) {

    }

    private Observable<ServerBotMsgResponse> getHistoryRequest(final int _offset, final int limit) {
        return Observable.create(new ObservableOnSubscribe<ServerBotMsgResponse>() {
            @Override
            public void subscribe(ObservableEmitter<ServerBotMsgResponse> emitter) {
                try {
                    ServerBotMsgResponse re = new ServerBotMsgResponse();

                    Call<BotHistory> _resp = RestBuilder.getRestAPI().getBotHistory("bearer " + SocketWrapper.getInstance(getActivity().getApplicationContext()).getAccessToken(), Client.bot_id, limit, _offset, true);
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
                                        BotResponse r = Utils.buildBotMessage(outer, msg.getBotId(), Client.bot_name, msg.getCreatedOn(), msg.getId());
                                        r.setType(msg.getType());
                                        r.setIcon(history.getIcon());
                                        msgs.add(r);
                                    } catch (com.google.gson.JsonSyntaxException ex) {
                                        BotResponse r = Utils.buildBotMessage(data, msg.getBotId(), Client.bot_name, msg.getCreatedOn(), msg.getId());
                                        r.setType(msg.getType());
                                        r.setIcon(history.getIcon());
                                        msgs.add(r);
                                    }
                                } else {
                                    try {
                                        String message = msg.getComponents().get(0).getData().getText();
                                        RestResponse.BotMessage botMessage = new RestResponse.BotMessage(message, "");
                                        RestResponse.BotPayLoad botPayLoad = new RestResponse.BotPayLoad();
                                        botPayLoad.setMessage(botMessage);
                                        BotInfoModel botInfo = new BotInfoModel(Client.bot_name, Client.bot_id, null);
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

    private Observable<ServerBotMsgResponse> getWebHookHistoryRequest(final int _offset, final int limit) {
        return Observable.create(new ObservableOnSubscribe<ServerBotMsgResponse>() {
            @Override
            public void subscribe(ObservableEmitter<ServerBotMsgResponse> emitter) {
                try {
                    ServerBotMsgResponse re = new ServerBotMsgResponse();

                    Call<BotHistory> _resp = WebHookRestBuilder.getRestAPI().getWebHookBotHistory("bearer " + jwt, Client.bot_id, Client.bot_id, limit, _offset);
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
                                        RestResponse.BotMessage botMessage = new RestResponse.BotMessage(message, "");
                                        RestResponse.BotPayLoad botPayLoad = new RestResponse.BotPayLoad();
                                        botPayLoad.setMessage(botMessage);
                                        BotInfoModel botInfo = new BotInfoModel(Client.bot_name, Client.bot_id, null);
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

    /*
      Limit selectable Date range
    */
    private CalendarConstraints.Builder minRange(String startDate, String date, String format) {

        if (StringUtils.isNullOrEmpty(date) && StringUtils.isNullOrEmpty(startDate)) {
            CalendarConstraints.Builder constraintsBuilderRange = new CalendarConstraints.Builder();
            constraintsBuilderRange.setValidator(DateValidatorPointForward.now());

            return constraintsBuilderRange;
        } else if (!StringUtils.isNullOrEmpty(startDate) && !StringUtils.isNullOrEmpty(date)) {
            CalendarConstraints.DateValidator dateValidatorMin = DateValidatorPointForward.from(DateUtils.getDateFromFormat(startDate, format, 0));
            CalendarConstraints.DateValidator dateValidatorMax = DateValidatorPointBackward.before(DateUtils.getDateFromFormat(date, format, 1));

            ArrayList<CalendarConstraints.DateValidator> listValidators = new ArrayList<CalendarConstraints.DateValidator>();
            listValidators.add(dateValidatorMin);
            listValidators.add(dateValidatorMax);

            CalendarConstraints.DateValidator validators = CompositeDateValidator.allOf(listValidators);
            CalendarConstraints.Builder constraintsBuilderRange = new CalendarConstraints.Builder();
            constraintsBuilderRange.setValidator(validators);

            return constraintsBuilderRange;
        } else if (!StringUtils.isNullOrEmpty(startDate)) {
            CalendarConstraints.DateValidator dateValidatorMin = DateValidatorPointForward.from(DateUtils.getDateFromFormat(startDate, format, 0));

            ArrayList<CalendarConstraints.DateValidator> listValidators = new ArrayList<CalendarConstraints.DateValidator>();
            listValidators.add(dateValidatorMin);

            CalendarConstraints.DateValidator validators = CompositeDateValidator.allOf(listValidators);
            CalendarConstraints.Builder constraintsBuilderRange = new CalendarConstraints.Builder();
            constraintsBuilderRange.setValidator(validators);

            return constraintsBuilderRange;
        } else if (!StringUtils.isNullOrEmpty(date)) {
            CalendarConstraints.DateValidator dateValidatorMax = DateValidatorPointBackward.before(DateUtils.getDateFromFormat(date, format, 1));

            ArrayList<CalendarConstraints.DateValidator> listValidators = new ArrayList<CalendarConstraints.DateValidator>();
            listValidators.add(dateValidatorMax);

            CalendarConstraints.DateValidator validators = CompositeDateValidator.allOf(listValidators);
            CalendarConstraints.Builder constraintsBuilderRange = new CalendarConstraints.Builder();
            constraintsBuilderRange.setValidator(validators);

            return constraintsBuilderRange;
        } else {
            CalendarConstraints.Builder constraintsBuilderRange = new CalendarConstraints.Builder();
            constraintsBuilderRange.setValidator(DateValidatorPointForward.now());

            return constraintsBuilderRange;
        }
    }

    static class RangeValidator implements CalendarConstraints.DateValidator {

        final long minDate;
        final long maxDate;

        RangeValidator(Parcel parcel) {
            minDate = parcel.readLong();
            maxDate = parcel.readLong();
        }

        @Override
        public boolean isValid(long date) {
            return !(minDate > date || maxDate < date);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeLong(minDate);
            dest.writeLong(maxDate);
        }

        public static final Creator<RangeValidator> CREATOR = new Creator<RangeValidator>() {

            @Override
            public RangeValidator createFromParcel(Parcel parcel) {
                return new RangeValidator(parcel);
            }

            @Override
            public RangeValidator[] newArray(int size) {
                return new RangeValidator[size];
            }
        };


    }

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

        if (!Client.isWebHook)
            getHistoryRequest(_offset, limit).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<ServerBotMsgResponse>() {
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

                    if (list != null && !list.isEmpty()) {
                        addMessagesToBotChatAdapter(list, _offset == 0);
                    }

                    if (list != null) {
                        list.size();
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
                    fetching = false;
                    if (swipeRefreshLayout != null) {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }
            });
        else
            getWebHookHistoryRequest(_offset, limit).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<ServerBotMsgResponse>() {
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
                        offset = re.getOriginalSize();
                    }

                    if (list != null && !list.isEmpty()) {
                        addMessagesToBotChatAdapter(list, _offset == 0);
                    }

                    if (list != null) {
                        list.size();
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
                    fetching = false;
                    if (swipeRefreshLayout != null) {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }
            });
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

        if (!Client.isWebHook)
            getHistoryRequest(_offset, limit).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<ServerBotMsgResponse>() {
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
                        int pos = 0;
                        ArrayList<BaseBotMessage> botResp = new ArrayList<>();
                        ArrayList<BaseBotMessage> currentList = botsChatAdapter.getBaseBotMessageArrayList();
                        ArrayList<BaseBotMessage> requiredList = new ArrayList<>();

                        for (int i = 0; i < list.size(); i++) {
                            if (list.get(i) instanceof BotResponse) {
                                botResp.add(list.get(i));
                            }
                        }

                        for (int i = 0; i < currentList.size(); i++) {
                            for (int j = 0; j < botResp.size(); j++) {
                                if (currentList.get(i).getCreatedInMillis() == botResp.get(j).getCreatedInMillis()) {
                                    pos = j;
                                }
                            }
                        }

                        if (pos != 0 && (pos + 1) < botResp.size()) {
                            for (int i = pos + 1; i < botResp.size(); i++) {
                                if (((BotResponse) botResp.get(i)).getMessage() != null && ((BotResponse) botResp.get(i)).getMessage().get(0) != null && ((BotResponse) botResp.get(i)).getMessage().get(0).getComponent() != null && ((BotResponse) botResp.get(i)).getMessage().get(0).getComponent().getPayload() != null && ((BotResponse) botResp.get(i)).getMessage().get(0).getComponent().getPayload().getPayload() != null && ((BotResponse) botResp.get(i)).getMessage().get(0).getComponent().getPayload().getPayload().getTemplate_type().equalsIgnoreCase(BotResponse.LIVE_AGENT))
                                    ((BotResponse) botResp.get(i)).setFromAgent(true);
                                requiredList.add(botResp.get(i));
                            }
                        }

                        botTypingStatusRl.setVisibility(View.GONE);
                        addMessagesToBotChatAdapter(requiredList, true, false);
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
                    fetching = false;
                    if (swipeRefreshLayout != null) {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }
            });
        else
            getWebHookHistoryRequest(_offset, limit).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<ServerBotMsgResponse>() {
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
                        offset = re.getOriginalSize();
                    }

                    if (list != null && list.size() > 0) {
                        addMessagesToBotChatAdapter(list, true);
                    }

                    if (list != null && list.size() > 0) {
                        addMessagesToBotChatAdapter(list, _offset == 0);
                    }

                    if (list != null) {
                        list.size();
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
                    fetching = false;
                    if (swipeRefreshLayout != null) {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }
            });
    }
}
