package kore.botssdk.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.AttrRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.format.DateFormat;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointBackward;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.gson.Gson;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
import kore.botssdk.dialogs.CalenderActionSheetFragment;
import kore.botssdk.dialogs.DateRangeCalendarActionSheetFragment;
import kore.botssdk.dialogs.OptionsActionSheetFragment;
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
import kore.botssdk.retroresponse.ServerBotMsgResponse;
import kore.botssdk.utils.BundleUtils;
import kore.botssdk.utils.DateUtils;
import kore.botssdk.utils.StringUtils;
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
public class BotContentFragment extends Fragment implements BotContentFragmentUpdate {

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
    private SwipeRefreshLayout swipeRefreshLayout;
    private LinearLayoutManager mLayoutManager;
    private int offset = 0;

    //Date Range
    private long today;
    private long nextMonth;
    private long janThisYear;
    private long decThisYear;
    private long oneYearForward;
    private Pair<Long, Long> todayPair;
    private Pair<Long, Long> nextMonthPair;

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
        botsBubblesListView =  view.findViewById(R.id.chatContentListView);
        mLayoutManager = (LinearLayoutManager) botsBubblesListView.getLayoutManager();
        headerView = view.findViewById(R.id.filesSectionHeader);
        swipeRefreshLayout = view.findViewById(R.id.swipeContainerChat);
        quickReplyView = view.findViewById(R.id.quick_reply_view);
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                  if (botsChatAdapter != null)
                        loadChatHistory(botsChatAdapter.getItemCount(), limit);
                    else
                        loadChatHistory(0, limit);
                }

        });
    }

    private void setupAdapter() {
        botsChatAdapter = new ChatAdapter(getActivity());
        botsChatAdapter.setComposeFooterInterface(composeFooterInterface);
        botsChatAdapter.setInvokeGenericWebViewInterface(invokeGenericWebViewInterface);
        botsChatAdapter.setActivityContext(getActivity());
        botsBubblesListView.setAdapter(botsChatAdapter);
//        botsChatAdapter.setShallShowProfilePic(shallShowProfilePic);
       // botsBubblesListView.setOnScrollListener(onScrollListener);
//        quickReplyView = new QuickReplyView(getContext());
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
                Log.d("Hey", "Started animation");
            }
        }
    }

    public void setQuickRepliesIntoFooter(BotResponse botResponse) {
        ArrayList<QuickReplyTemplate> quickReplyTemplates = getQuickReplies(botResponse);
        quickReplyView.populateQuickReplyView(quickReplyTemplates);
    }

    public void showCalendarIntoFooter(BotResponse botResponse)
    {
        if (botResponse != null && botResponse.getMessage() != null && !botResponse.getMessage().isEmpty()) {
            ComponentModel compModel = botResponse.getMessage().get(0).getComponent();
            if (compModel != null) {
                String compType = compModel.getType();
                if (BotResponse.COMPONENT_TYPE_TEMPLATE.equalsIgnoreCase(compType)) {
                    PayloadOuter payOuter = compModel.getPayload();
                    PayloadInner payInner = payOuter.getPayload();
                    if (payInner != null && BotResponse.TEMPLATE_TYPE_DATE.equalsIgnoreCase(payInner.getTemplate_type()))
                    {
//                        CalenderActionSheetFragment bottomSheetDialog = new CalenderActionSheetFragment();
//                        bottomSheetDialog.setisFromFullView(false);
//                        bottomSheetDialog.setSkillName("skillName","trigger");
//                        bottomSheetDialog.setData(payInner);
//                        bottomSheetDialog.setComposeFooterInterface(composeFooterInterface);
//                        bottomSheetDialog.show(((FragmentActivity) getContext()).getSupportFragmentManager(), "add_tags");

                        Calendar cal = Calendar.getInstance();
                        cal.setTimeInMillis(MaterialDatePicker.todayInUtcMilliseconds());
                        int strYear = cal.get(Calendar.YEAR);
                        int strMonth = cal.get(Calendar.MONTH);
                        int strDay = cal.get(Calendar.DAY_OF_MONTH);
                        String minDate = strMonth+"-"+strDay+"-"+strYear;

                        MaterialDatePicker.Builder<Long> builder =  MaterialDatePicker.Builder.datePicker();
                        builder.setTitleText(payInner.getTitle());
                        builder.setInputMode(MaterialDatePicker.INPUT_MODE_CALENDAR);
                        builder.setCalendarConstraints(minRange(minDate, payInner.getFormat()).build());
                        builder.setTheme(R.style.MyMaterialCalendarTheme);

                        try
                        {
                            MaterialDatePicker<Long> picker = builder.build();
                            picker.show(getFragmentManager(), picker.toString());

                            picker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
                                @Override public void onPositiveButtonClick(Long selection) {
                                    Calendar calendar = Calendar.getInstance();
                                    calendar.setTimeInMillis(selection);
                                    int stYear = calendar.get(Calendar.YEAR);
                                    int stMonth = calendar.get(Calendar.MONTH);
                                    int stDay = calendar.get(Calendar.DAY_OF_MONTH);

                                    String formatedDate = "";
                                    formatedDate = DateUtils.getMonthName(stMonth)+" "+stDay+DateUtils.getDayOfMonthSuffix(stDay)+", "+stYear;

                                    if(!formatedDate.isEmpty())
                                        composeFooterInterface.onSendClick(formatedDate, false);
                                }
                            });
                        }
                        catch (IllegalArgumentException e) {}
                    }
                    else if (payInner != null && BotResponse.TEMPLATE_TYPE_DATE_RANGE.equalsIgnoreCase(payInner.getTemplate_type()))
                    {
                        initSettings();
                        MaterialDatePicker.Builder<Pair<Long, Long>> builder =  MaterialDatePicker.Builder.dateRangePicker();
                        builder.setTitleText(payInner.getTitle());
                        builder.setInputMode(MaterialDatePicker.INPUT_MODE_CALENDAR);
                        builder.setCalendarConstraints(limitRange(payInner.getEndDate(), payInner.getFormat()).build());
                        builder.setTheme(R.style.MyMaterialCalendarTheme);

                        try
                        {
                            MaterialDatePicker<Pair<Long, Long>> picker = builder.build();
                            picker.show(getFragmentManager(), picker.toString());
                            picker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Pair<Long, Long>>() {
                                @Override public void onPositiveButtonClick(Pair<Long,Long> selection) {
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

                                    String formatedDate = "";
                                        formatedDate = DateUtils.getMonthName(strMonth)+" "+strDay+DateUtils.getDayOfMonthSuffix(strDay)+", "+strYear;
                                        formatedDate = formatedDate +" to "+ DateUtils.getMonthName(endMonth)+" "+endDay+DateUtils.getDayOfMonthSuffix(endDay)+", "+endYear;


                                    if(!formatedDate.isEmpty())
                                        composeFooterInterface.onSendClick(formatedDate, false);
                                }
                            });
                        }
                        catch (IllegalArgumentException e) {}
                    }
                }
            }
        }
    }

    private static int resolveOrThrow(Context context, @AttrRes int attributeResId) {
        TypedValue typedValue = new TypedValue();
        if (context.getTheme().resolveAttribute(attributeResId, typedValue, true)) {
            return typedValue.data;
        }
        throw new IllegalArgumentException(context.getResources().getResourceName(attributeResId));
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
        typingStatusItemDots.setTextColor(Color.BLACK);
    }

    private void scrollToBottom() {
        final int count = botsChatAdapter.getItemCount();
        botsBubblesListView.post(new Runnable() {
            @Override
            public void run() {
                botsBubblesListView.smoothScrollToPosition(count - 1);
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

                    Call<BotHistory> _resp = RestBuilder.getRestAPI().getBotHistory("bearer " + SocketWrapper.getInstance(getActivity().getApplicationContext()).getAccessToken(), Client.bot_id, limit, _offset, true);
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
    private CalendarConstraints.Builder limitRange(String date, String format) {

        CalendarConstraints.Builder constraintsBuilderRange = new CalendarConstraints.Builder();

        Calendar calendarStart = Calendar.getInstance();
        Calendar calendarEnd = Calendar.getInstance();

//        int year = 2020;
//        int startMonth = 1;
//        int startDate = 1;
//
//        calendarStart.set(year, startMonth - 1, startDate);

        Date endDate = stringToDate(date, format);
        calendarStart.setTime(endDate);

        long minDate = calendarStart.getTimeInMillis();
        long maxDate = calendarEnd.getTimeInMillis();


        constraintsBuilderRange.setStart(minDate);
        constraintsBuilderRange.setEnd(maxDate);
        constraintsBuilderRange.setValidator(new RangeValidator(minDate, maxDate));

        return constraintsBuilderRange;
    }

    /*
      Limit selectable Date range
    */
    private CalendarConstraints.Builder minRange(String date, String format) {

        CalendarConstraints.Builder constraintsBuilderRange = new CalendarConstraints.Builder();
        constraintsBuilderRange.setValidator(DateValidatorPointForward.now());

        return constraintsBuilderRange;
    }

    private Date stringToDate(String aDate,String aFormat) {

        SimpleDateFormat format = new SimpleDateFormat("M-DD-YYYY");
        try
        {
            Date date = format.parse(aDate);
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null;
    }


    static class RangeValidator implements CalendarConstraints.DateValidator {

        long minDate, maxDate;

        RangeValidator(long minDate, long maxDate) {
            this.minDate = minDate;
            this.maxDate = maxDate;
        }

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


    private void loadChatHistory(final int _offset, final int limit) {
        if (fetching){
            if (swipeRefreshLayout != null) {
                swipeRefreshLayout.setRefreshing(false);
            }
            return;
        }
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
                        fetching = false;
                        if (swipeRefreshLayout != null) {
                            swipeRefreshLayout.setRefreshing(false);
                        }
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
