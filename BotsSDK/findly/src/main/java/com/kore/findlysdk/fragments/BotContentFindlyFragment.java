package com.kore.findlysdk.fragments;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Parcel;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.AttrRes;
import androidx.annotation.Nullable;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.gson.Gson;
import com.kore.findlysdk.R;
import com.kore.findlysdk.adapters.ChatAdapter;
import com.kore.findlysdk.listners.BotContentFragmentUpdate;
import com.kore.findlysdk.listners.ComposeFooterInterface;
import com.kore.findlysdk.listners.InvokeGenericWebViewInterface;
import com.kore.findlysdk.listners.TTSUpdate;
import com.kore.findlysdk.models.BaseBotMessage;
import com.kore.findlysdk.models.BotRequest;
import com.kore.findlysdk.models.BotResponse;
import com.kore.findlysdk.models.ComponentModel;
import com.kore.findlysdk.models.PayloadInner;
import com.kore.findlysdk.models.PayloadOuter;
import com.kore.findlysdk.models.QuickReplyTemplate;
import com.kore.findlysdk.net.SDKConfiguration;
import com.kore.findlysdk.utils.BundleUtils;
import com.kore.findlysdk.utils.DateUtils;
import com.kore.findlysdk.view.CircularProfileFindlyView;
import com.kore.findlysdk.view.QuickReplyFindlyView;
import com.kore.findlysdk.views.DotsTextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by Pradeep Mahato on 31-May-16.
 * Copyright (c) 2014 Kore Inc. All rights reserved.
 */
public class BotContentFindlyFragment extends Fragment implements BotContentFragmentUpdate {
    RelativeLayout rvChatContent;
    RecyclerView botsBubblesListView;
    ChatAdapter botsChatAdapter;
    QuickReplyFindlyView quickReplyFindlyView;
    String LOG_TAG = BotContentFindlyFragment.class.getSimpleName();
    private LinearLayout botTypingStatusRl;
    private CircularProfileFindlyView botTypingStatusFindlyIcon;
    private DotsTextView typingStatusItemFindlyDots;
    ComposeFooterInterface composeFooterInterface;
    InvokeGenericWebViewInterface invokeGenericWebViewInterface;
    boolean shallShowProfilePic;
    private String mChannelIconURL;
    private String mBotNameInitials;
    private TTSUpdate ttsUpdate;
    private int mBotIconId;
    private boolean fetching = false;
    private boolean hasMore = true;
    private TextView headerView, tvTheme1, tvTheme2;
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

        View view = inflater.inflate(R.layout.bot_content_findly_layout, null);
        // create the popup window
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true;
        findViews(view);
        getBundleInfo();
        initializeBotTypingStatus(view, mChannelIconURL);
        setupAdapter();
//        loadChatHistory(0, limit);
        return view;
    }

    private void findViews(View view) {
        rvChatContent = (RelativeLayout) view.findViewById(R.id.rvChatContent);
        botsBubblesListView = view.findViewById(R.id.chatContentListView);
        mLayoutManager = (LinearLayoutManager) botsBubblesListView.getLayoutManager();
        headerView = view.findViewById(R.id.filesSectionHeader);
        swipeRefreshLayout = view.findViewById(R.id.swipeContainerChat);
        quickReplyFindlyView = (QuickReplyFindlyView) view.findViewById(R.id.quick_reply_findly_view);
        headerView.setVisibility(View.GONE);
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        swipeRefreshLayout.setEnabled(false);
        swipeRefreshLayout.setRefreshing(false);

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void changeThemeBackGround(String bgColor, String textColor) {
        rvChatContent.setBackgroundColor(Color.parseColor(bgColor));

        GradientDrawable gradientDrawable = (GradientDrawable) headerView.getBackground();
        gradientDrawable.setColor(Color.parseColor(bgColor));
        headerView.setTextColor(Color.parseColor(textColor));
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
        quickReplyFindlyView.setComposeFooterInterface(composeFooterInterface);
        quickReplyFindlyView.setInvokeGenericWebViewInterface(invokeGenericWebViewInterface);
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
            if (typingStatusItemFindlyDots != null) {
                botTypingStatusRl.setVisibility(View.VISIBLE);
                typingStatusItemFindlyDots.start();
                Log.d("Hey", "Started animation");
            }
        }
    }

    public void setQuickRepliesIntoFooter(BotResponse botResponse) {
        ArrayList<QuickReplyTemplate> quickReplyTemplates = getQuickReplies(botResponse);
        quickReplyFindlyView.populateQuickReplyView(quickReplyTemplates);
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
                        Calendar cal = Calendar.getInstance();
                        cal.setTimeInMillis(MaterialDatePicker.todayInUtcMilliseconds());
                        int strYear = cal.get(Calendar.YEAR);
                        int strMonth = cal.get(Calendar.MONTH);
                        int strDay = cal.get(Calendar.DAY_OF_MONTH);
                        String minDate = strMonth + "-" + strDay + "-" + strYear;

                        MaterialDatePicker.Builder<Long> builder = MaterialDatePicker.Builder.datePicker();
                        builder.setTitleText(payInner.getTitle());
                        builder.setInputMode(MaterialDatePicker.INPUT_MODE_CALENDAR);
                        builder.setCalendarConstraints(minRange(minDate, payInner.getFormat()).build());
                        builder.setTheme(R.style.MyMaterialCalendarTheme);

                        try {
                            MaterialDatePicker<Long> picker = builder.build();
                            picker.show(getFragmentManager(), picker.toString());

                            picker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
                                @Override
                                public void onPositiveButtonClick(Long selection) {
                                    Calendar calendar = Calendar.getInstance();
                                    calendar.setTimeInMillis(selection);
                                    int stYear = calendar.get(Calendar.YEAR);
                                    int stMonth = calendar.get(Calendar.MONTH);
                                    int stDay = calendar.get(Calendar.DAY_OF_MONTH);

                                    String formatedDate = "";
                                    formatedDate = DateUtils.getMonthName(stMonth) + " " + stDay + DateUtils.getDayOfMonthSuffix(stDay) + ", " + stYear;

                                    if (!formatedDate.isEmpty())
                                        composeFooterInterface.onSendClick(formatedDate, false);
                                }
                            });
                        } catch (IllegalArgumentException e) {
                        }
                    } else if (payInner != null && BotResponse.TEMPLATE_TYPE_DATE_RANGE.equalsIgnoreCase(payInner.getTemplate_type())) {
                        initSettings();
                        MaterialDatePicker.Builder<Pair<Long, Long>> builder = MaterialDatePicker.Builder.dateRangePicker();
                        builder.setTitleText(payInner.getTitle());
                        builder.setInputMode(MaterialDatePicker.INPUT_MODE_CALENDAR);
                        builder.setCalendarConstraints(limitRange(payInner.getEndDate(), payInner.getFormat()).build());
                        builder.setTheme(R.style.MyMaterialCalendarTheme);

                        try {
                            MaterialDatePicker<Pair<Long, Long>> picker = builder.build();
                            picker.show(getFragmentManager(), picker.toString());
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

                                    String formatedDate = "";
                                    formatedDate = DateUtils.getMonthName(strMonth) + " " + strDay + DateUtils.getDayOfMonthSuffix(strDay) + ", " + strYear;
                                    formatedDate = formatedDate + " to " + DateUtils.getMonthName(endMonth) + " " + endDay + DateUtils.getDayOfMonthSuffix(endDay) + ", " + endYear;


                                    if (!formatedDate.isEmpty())
                                        composeFooterInterface.onSendClick(formatedDate, false);
                                }
                            });
                        } catch (IllegalArgumentException e) {
                        }
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

    public void removeAllChatMessages()
    {
        botsChatAdapter.removeAllBotMessages();
//        botsChatAdapter = new ChatAdapter(getActivity());
    }

    protected void initializeBotTypingStatus(View view, String mChannelIconURL) {
        botTypingStatusRl = (LinearLayout) view.findViewById(R.id.botTypingStatus);
        botTypingStatusFindlyIcon = (CircularProfileFindlyView) view.findViewById(R.id.typing_status_item_findly_cpv);

        if(botTypingStatusFindlyIcon != null)
            botTypingStatusFindlyIcon.populateLayout(mBotNameInitials, mChannelIconURL, null, mBotIconId, Color.parseColor(SDKConfiguration.BubbleColors.quickReplyColor), true);
        typingStatusItemFindlyDots = (DotsTextView) view.findViewById(R.id.typing_status_item_findly_dots);

        if(botTypingStatusFindlyIcon != null)
            typingStatusItemFindlyDots.setTextColor(Color.BLACK);
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
                quickReplyFindlyView.populateQuickReplyView(null);
                scrollToBottom();
            }
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

    private Date stringToDate(String aDate, String aFormat) {

        SimpleDateFormat format = new SimpleDateFormat("M-DD-YYYY");
        try {
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
}
