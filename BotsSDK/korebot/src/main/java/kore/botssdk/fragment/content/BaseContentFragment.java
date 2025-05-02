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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;

import kore.botssdk.R;
import kore.botssdk.adapter.ChatAdapter;
import kore.botssdk.listener.BotContentFragmentUpdate;
import kore.botssdk.listener.ComposeFooterInterface;
import kore.botssdk.listener.InvokeGenericWebViewInterface;
import kore.botssdk.models.BaseBotMessage;
import kore.botssdk.models.BotRequest;
import kore.botssdk.models.BotResponse;
import kore.botssdk.models.ComponentModel;
import kore.botssdk.models.PayloadInner;
import kore.botssdk.models.PayloadOuter;
import kore.botssdk.net.SDKConfiguration;
import kore.botssdk.repository.history.HistoryRepository;
import kore.botssdk.utils.DateUtils;
import kore.botssdk.utils.StringUtils;
import kore.botssdk.viewmodels.content.BotContentViewModel;
import kore.botssdk.viewmodels.content.BotContentViewModelFactory;
import kore.botssdk.websocket.SocketWrapper;

@SuppressWarnings("UnKnownNullness")
public abstract class BaseContentFragment extends Fragment implements BotContentFragmentUpdate {
    protected ChatAdapter botsChatAdapter;
    protected ComposeFooterInterface composeFooterInterface;
    protected InvokeGenericWebViewInterface invokeGenericWebViewInterface;
    private boolean fetching = false;
    protected SwipeRefreshLayout swipeRefreshLayout;
    protected String jwt;
    protected BotContentViewModel mContentViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        botsChatAdapter = getChatAdapter();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        BotContentViewModelFactory factory = new BotContentViewModelFactory(requireActivity(), BaseContentFragment.this, new HistoryRepository(requireActivity(), BaseContentFragment.this));
        mContentViewModel = new ViewModelProvider(this, factory).get(BotContentViewModel.class);
        botsChatAdapter.setComposeFooterInterface(composeFooterInterface);
        botsChatAdapter.setInvokeGenericWebViewInterface(invokeGenericWebViewInterface);
        swipeRefreshLayout = getSwipeRefreshLayout(view);
    }

    protected abstract ChatAdapter getChatAdapter();

    protected abstract SwipeRefreshLayout getSwipeRefreshLayout(View view);

    public void setJwtTokenForWebHook(String jwt) {
        if (!StringUtils.isNullOrEmpty(jwt)) this.jwt = jwt;
    }

    public abstract void changeThemeBackGround(String bgColor, String textBgColor, String textColor, String botName);

    public abstract void showTypingStatus();

    public abstract void setQuickRepliesIntoFooter(BotResponse botResponse);

    public abstract void addMessageToBotChatAdapter(BotResponse botResponse);

    public abstract void addMessagesToBotChatAdapter(ArrayList<BaseBotMessage> list, boolean scrollToBottom);

    public abstract void addMessagesToBotChatAdapter(@NonNull ArrayList<BaseBotMessage> list, boolean scrollToBottom, boolean isFirst);

    public abstract void updateContentListOnSend(BotRequest botRequest);

    public void setComposeFooterInterface(ComposeFooterInterface composeFooterInterface) {
        this.composeFooterInterface = composeFooterInterface;
        if (botsChatAdapter != null) botsChatAdapter.setComposeFooterInterface(composeFooterInterface);
    }

    public void setInvokeGenericWebViewInterface(InvokeGenericWebViewInterface invokeGenericWebViewInterface) {
        this.invokeGenericWebViewInterface = invokeGenericWebViewInterface;
        if (botsChatAdapter != null) botsChatAdapter.setInvokeGenericWebViewInterface(invokeGenericWebViewInterface);
    }

    public int getAdapterCount() {
        if (botsChatAdapter != null) return botsChatAdapter.getItemCount();
        return 0;
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
                                int stYear = calendar.get(Calendar.YEAR);
                                int stMonth = calendar.get(Calendar.MONTH);
                                int stDay = calendar.get(Calendar.DAY_OF_MONTH);

                                String formattedDate = ((stMonth + 1) > 9 ? (stMonth + 1) : "0" + (stMonth + 1)) + "/" + (stDay > 9 ? stDay : "0" + stDay) + "/" + stYear;

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
                                cal.setTimeInMillis(startDate);
                                int strYear = cal.get(Calendar.YEAR);
                                int strMonth = cal.get(Calendar.MONTH);
                                int strDay = cal.get(Calendar.DAY_OF_MONTH);

                                cal.setTimeInMillis(endDate);
                                int endYear = cal.get(Calendar.YEAR);
                                int endMonth = cal.get(Calendar.MONTH);
                                int endDay = cal.get(Calendar.DAY_OF_MONTH);

                                String formatedStartDate = ((strDay + 1) > 9 ? (strDay + 1) : "0" + (strDay + 1)) + "-" + ((strMonth + 1) > 9 ? (strMonth + 1) : "0" + (strMonth + 1)) + "-" + strYear;
                                String formatedEndDate = ((endDay + 1) > 9 ? (endDay + 1) : "0" + (endDay + 1)) + "-" + ((endMonth + 1) > 9 ? (endMonth + 1) : "0" + (endMonth + 1)) + "-" + endYear;
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

    @Override
    public void onChatHistory(ArrayList<BaseBotMessage> list, int offset, boolean scrollToBottom) {
        fetching = false;
        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.setRefreshing(false);
        }

        if (list != null) {
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
            addMessagesToBotChatAdapter(list, true, isReconnectionHistory);
        }
    }

    private void initSettings() {
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

    public boolean getmChannelIconURL() {
        return getmChannelIconURL();
    }
}