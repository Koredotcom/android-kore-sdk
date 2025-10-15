package kore.botssdk.fragment.content;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.datepicker.MaterialDatePicker;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

import kore.botssdk.R;
import kore.botssdk.adapter.ChatAdapter;
import kore.botssdk.listener.BotContentFragmentUpdate;
import kore.botssdk.listener.ComposeFooterInterface;
import kore.botssdk.listener.InvokeGenericWebViewInterface;
import kore.botssdk.listener.TTSUpdate;
import kore.botssdk.models.BaseBotMessage;
import kore.botssdk.models.BotBrandingModel;
import kore.botssdk.models.BotRequest;
import kore.botssdk.models.BotResponse;
import kore.botssdk.models.ComponentModel;
import kore.botssdk.models.PayloadInner;
import kore.botssdk.models.PayloadOuter;
import kore.botssdk.models.QuickReplyTemplate;
import kore.botssdk.net.SDKConfiguration;
import kore.botssdk.utils.BundleUtils;
import kore.botssdk.utils.StringUtils;
import kore.botssdk.viewmodels.content.BotContentViewModel;
import kore.botssdk.viewmodels.content.BotContentViewModelFactory;
import kore.botssdk.websocket.SocketWrapper;

@SuppressWarnings("UnKnownNullness")
public abstract class BaseContentFragment extends Fragment implements BotContentFragmentUpdate {
    private final int limit = 10;
    protected ChatAdapter botsChatAdapter;
    protected ComposeFooterInterface composeFooterInterface;
    protected InvokeGenericWebViewInterface invokeGenericWebViewInterface;
    protected String mChannelIconURL;
    protected String mBotNameInitials;
    protected int mBotIconId;
    protected boolean fetching = false;
    protected SwipeRefreshLayout swipeRefreshLayout;
    protected int offset = 0;
    protected String jwt;
    protected BotContentViewModel mContentViewModel;
    protected TTSUpdate ttsUpdate;

    private static Calendar getClearedUtc() {
        Calendar utc = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        utc.clear();
        return utc;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getBundleInfo();
        botsChatAdapter = new ChatAdapter();
        botsChatAdapter.setComposeFooterInterface(composeFooterInterface);
        botsChatAdapter.setInvokeGenericWebViewInterface(invokeGenericWebViewInterface);
        BotContentViewModelFactory factory = new BotContentViewModelFactory(requireActivity(), BaseContentFragment.this);
        mContentViewModel = new ViewModelProvider(this, factory).get(BotContentViewModel.class);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        swipeRefreshLayout = getSwipeRefreshLayout(view);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            if (botsChatAdapter != null) loadChatHistory(botsChatAdapter.getItemCount(), limit);
            else loadChatHistory(0, limit);
        });
    }

    public void setJwtTokenForWebHook(String jwt) {
        if (!StringUtils.isNullOrEmpty(jwt)) this.jwt = jwt;
    }

    public void setTtsUpdate(TTSUpdate ttsUpdate) {
        this.ttsUpdate = ttsUpdate;
    }

    public void setComposeFooterInterface(ComposeFooterInterface composeFooterInterface) {
        if (botsChatAdapter != null)
            botsChatAdapter.setComposeFooterInterface(composeFooterInterface);
        this.composeFooterInterface = composeFooterInterface;
    }

    public void setInvokeGenericWebViewInterface(InvokeGenericWebViewInterface invokeGenericWebViewInterface) {
        this.invokeGenericWebViewInterface = invokeGenericWebViewInterface;
        if (botsChatAdapter != null)
            botsChatAdapter.setInvokeGenericWebViewInterface(invokeGenericWebViewInterface);
    }

    private void getBundleInfo() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            mChannelIconURL = bundle.getString(BundleUtils.CHANNEL_ICON_URL);
            mBotNameInitials = bundle.getString(BundleUtils.BOT_NAME_INITIALS, "B");
            mBotIconId = bundle.getInt(BundleUtils.BOT_ICON_ID, -1);
        }
    }

    public abstract void setBotBrandingModel(BotBrandingModel botBrandingModel);

    protected abstract SwipeRefreshLayout getSwipeRefreshLayout(View view);

    public abstract void showTypingStatus();

    public abstract void stopTypingStatus();

    public abstract void setQuickRepliesIntoFooter(BotResponse botResponse);

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
                        builder.setPositiveButtonText(getString(R.string.confirm));
                        builder.setInputMode(MaterialDatePicker.INPUT_MODE_CALENDAR);
                        builder.setCalendarConstraints(mContentViewModel.minRange(payInner.getStartDate(), payInner.getEndDate(), payInner.getFormat()).build());
                        builder.setTheme(R.style.MyMaterialCalendarTheme);

                        try {
                            MaterialDatePicker<Long> picker = builder.build();
                            picker.show(requireActivity().getSupportFragmentManager(), picker.toString());
                            picker.addOnPositiveButtonClickListener(selection -> {
                                Calendar calendar = Calendar.getInstance();
                                calendar.setTimeInMillis(selection);

                                String formattedDate;
                                if (payInner.getFormat() != null && !payInner.getFormat().isEmpty()) {
                                    String dateFormat = payInner.getFormat().replaceAll("DD", "dd").replaceAll("YY", "yy");
                                    SimpleDateFormat formatter = new SimpleDateFormat(dateFormat, Locale.ENGLISH);
                                    formattedDate = formatter.format(calendar.getTime());
                                } else {
                                    int stYear = calendar.get(Calendar.YEAR);
                                    int stMonth = calendar.get(Calendar.MONTH);
                                    int stDay = calendar.get(Calendar.DAY_OF_MONTH);
                                    formattedDate = ((stMonth + 1) > 9 ? (stMonth + 1) : "0" + (stMonth + 1)) + "/" + (stDay > 9 ? stDay : "0" + stDay) + "/" + stYear;
                                }

                                composeFooterInterface.onSendClick(formattedDate, false);
                            });
                        } catch (IllegalArgumentException e) {
                            e.printStackTrace();
                        }
                    } else if (payInner != null && BotResponse.TEMPLATE_TYPE_DATE_RANGE.equalsIgnoreCase(payInner.getTemplate_type())) {
                        initSettings();
                        MaterialDatePicker.Builder<Pair<Long, Long>> builder = MaterialDatePicker.Builder.dateRangePicker();
                        builder.setTitleText(payInner.getTitle());
                        builder.setPositiveButtonText(getString(R.string.confirm));
                        builder.setInputMode(MaterialDatePicker.INPUT_MODE_CALENDAR);
                        builder.setCalendarConstraints(mContentViewModel.minRange(payInner.getStartDate(), payInner.getEndDate(), payInner.getFormat()).build());
                        builder.setTheme(R.style.MyMaterialCalendarTheme);

                        try {
                            MaterialDatePicker<Pair<Long, Long>> picker = builder.build();
                            picker.show(requireActivity().getSupportFragmentManager(), picker.toString());
                            picker.addOnPositiveButtonClickListener(selection -> {
                                Long startDate = selection.first;
                                Long endDate = selection.second;

                                Calendar cal = Calendar.getInstance();

                                String formatedStartDate;
                                String formatedEndDate;
                                if (payInner.getFormat() != null && !payInner.getFormat().isEmpty()) {
                                    String dateFormat = payInner.getFormat().replaceAll("DD", "dd").replaceAll("YY", "yy");
                                    SimpleDateFormat formatter = new SimpleDateFormat(dateFormat, Locale.ENGLISH);
                                    cal.setTimeInMillis(startDate);
                                    formatedStartDate = formatter.format(cal.getTime());
                                    cal.setTimeInMillis(endDate);
                                    formatedEndDate = formatter.format(cal.getTime());
                                } else {
                                    cal.setTimeInMillis(startDate);
                                    int strYear = cal.get(Calendar.YEAR);
                                    int strMonth = cal.get(Calendar.MONTH);
                                    int strDay = cal.get(Calendar.DAY_OF_MONTH);

                                    cal.setTimeInMillis(endDate);
                                    int endYear = cal.get(Calendar.YEAR);
                                    int endMonth = cal.get(Calendar.MONTH);
                                    int endDay = cal.get(Calendar.DAY_OF_MONTH);

                                    formatedStartDate = ((strDay + 1) > 9 ? (strDay + 1) : "0" + (strDay + 1)) + "-" + ((strMonth + 1) > 9 ? (strMonth + 1) : "0" + (strMonth + 1)) + "-" + strYear;
                                    formatedEndDate = ((endDay + 1) > 9 ? (endDay + 1) : "0" + (endDay + 1)) + "-" + ((endMonth + 1) > 9 ? (endMonth + 1) : "0" + (endMonth + 1)) + "-" + endYear;
                                }
                                formatedStartDate = formatedStartDate + " to " + formatedEndDate;
                                composeFooterInterface.onSendClick(formatedStartDate, false);
                            });
                        } catch (IllegalArgumentException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    protected ArrayList<QuickReplyTemplate> getQuickReplies(BotResponse botResponse) {
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

    public abstract void addMessageToBotChatAdapter(BotResponse botResponse);

    public abstract void addMessagesToBotChatAdapter(ArrayList<BaseBotMessage> list, boolean scrollToBottom);

    public abstract void addMessagesToBotChatAdapter(ArrayList<BaseBotMessage> list, boolean scrollToBottom, boolean isFirst);

    public abstract void updateContentListOnSend(BotRequest botRequest);

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
        //Date Range
        long today = MaterialDatePicker.todayInUtcMilliseconds();
        Calendar calendar = getClearedUtc();
        calendar.setTimeInMillis(today);
        calendar.roll(Calendar.MONTH, 1);

        calendar.setTimeInMillis(today);
        calendar.set(Calendar.MONTH, Calendar.JANUARY);
        calendar.setTimeInMillis(today);
        calendar.set(Calendar.MONTH, Calendar.DECEMBER);

        calendar.setTimeInMillis(today);
        calendar.roll(Calendar.YEAR, 1);
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

        if (!SDKConfiguration.Client.isWebHook)
            mContentViewModel.loadChatHistory(_offset, limit, SocketWrapper.getInstance(requireActivity().getApplicationContext()).getAccessToken());
        else mContentViewModel.loadChatHistory(_offset, limit, jwt);
    }

    public int getAdapterCount() {
        if (botsChatAdapter != null) return botsChatAdapter.getItemCount();
        return 0;
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
        else
            mContentViewModel.loadReconnectionChatHistory(_offset, limit, jwt, botsChatAdapter.getBaseBotMessageArrayList());
    }
}
