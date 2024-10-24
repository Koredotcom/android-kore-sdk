package kore.botssdk.fragment;

import static kore.botssdk.view.viewUtils.DimensionUtil.dp1;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;

import kore.botssdk.R;
import kore.botssdk.adapter.ChatAdapter;
import kore.botssdk.itemdecoration.ChatAdapterItemDecoration;
import kore.botssdk.listener.BotContentFragmentUpdate;
import kore.botssdk.listener.BotSocketConnectionManager;
import kore.botssdk.listener.ComposeFooterInterface;
import kore.botssdk.listener.InvokeGenericWebViewInterface;
import kore.botssdk.listener.TTSUpdate;
import kore.botssdk.models.BaseBotMessage;
import kore.botssdk.models.BotBrandingModel;
import kore.botssdk.models.BotRequest;
import kore.botssdk.models.BotResponse;
import kore.botssdk.models.BrandingBodyModel;
import kore.botssdk.models.BrandingHeaderModel;
import kore.botssdk.models.ComponentModel;
import kore.botssdk.models.PayloadInner;
import kore.botssdk.models.PayloadOuter;
import kore.botssdk.models.QuickReplyTemplate;
import kore.botssdk.net.SDKConfiguration;
import kore.botssdk.utils.BundleConstants;
import kore.botssdk.utils.BundleUtils;
import kore.botssdk.utils.DateUtils;
import kore.botssdk.utils.StringUtils;
import kore.botssdk.view.CircularProfileView;
import kore.botssdk.view.QuickReplyView;
import kore.botssdk.view.viewUtils.RoundedCornersTransform;
import kore.botssdk.viewmodels.content.BotContentViewModel;
import kore.botssdk.viewmodels.content.BotContentViewModelFactory;
import kore.botssdk.websocket.SocketWrapper;

@SuppressWarnings("UnKnownNullness")
public class BotContentFragment extends Fragment implements BotContentFragmentUpdate {
    RelativeLayout rlBody;
    RecyclerView botsBubblesListView;
    ChatAdapter botsChatAdapter;
    QuickReplyView quickReplyView;
    LinearLayout botTypingStatusRl;
    CircularProfileView botTypingStatusIcon;
    ComposeFooterInterface composeFooterInterface;
    InvokeGenericWebViewInterface invokeGenericWebViewInterface;
    String mChannelIconURL;
    String mBotNameInitials;
    int mBotIconId;
    boolean fetching = false;
    SwipeRefreshLayout swipeRefreshLayout;
    int offset = 0;
    SharedPreferences sharedPreferences;
    String jwt;
    RelativeLayout llBotHeader;
    BotContentViewModel mContentViewModel;
    TTSUpdate ttsUpdate;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = View.inflate(requireActivity(), R.layout.bot_content_layout, null);
        getBundleInfo();
        findViews(view);
        initializeBotTypingStatus(view, mChannelIconURL);
        setupAdapter();

        BotContentViewModelFactory factory = new BotContentViewModelFactory(requireActivity(), BotContentFragment.this);
        mContentViewModel = new ViewModelProvider(this, factory).get(BotContentViewModel.class);

        return view;
    }

    private void findViews(View view) {
        botsBubblesListView = view.findViewById(R.id.chatContentListView);
        TextView headerView = view.findViewById(R.id.filesSectionHeader);
        swipeRefreshLayout = view.findViewById(R.id.swipeContainerChat);
        quickReplyView = view.findViewById(R.id.quick_reply_view);
        llBotHeader = view.findViewById(R.id.llBotHeader);
        rlBody = view.findViewById(R.id.rlBody);

        headerView.setVisibility(View.GONE);
        sharedPreferences = requireActivity().getSharedPreferences(BotResponse.THEME_NAME, Context.MODE_PRIVATE);
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light, android.R.color.holo_orange_light, android.R.color.holo_red_light);
        botsBubblesListView.setItemViewCacheSize(100);
        botsBubblesListView.setItemAnimator(null);

        swipeRefreshLayout.setOnRefreshListener(() -> {
            if (botsChatAdapter != null) loadChatHistory(botsChatAdapter.getItemCount(), limit);
            else loadChatHistory(0, limit);
        });
    }

    public void setJwtTokenForWebHook(String jwt) {
        if (!StringUtils.isNullOrEmpty(jwt)) this.jwt = jwt;
    }

    private void setupAdapter() {
        botsChatAdapter = new ChatAdapter();
        botsChatAdapter.setComposeFooterInterface(composeFooterInterface);
        botsChatAdapter.setInvokeGenericWebViewInterface(invokeGenericWebViewInterface);
        botsBubblesListView.addItemDecoration(new ChatAdapterItemDecoration());
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
            mChannelIconURL = bundle.getString(BundleUtils.CHANNEL_ICON_URL);
            mBotNameInitials = bundle.getString(BundleUtils.BOT_NAME_INITIALS, "B");
            mBotIconId = bundle.getInt(BundleUtils.BOT_ICON_ID, -1);
        }
    }

    public void setBotBrandingModel(BotBrandingModel botBrandingModel) {

        if (botBrandingModel != null) {
            BrandingHeaderModel headerModel = botBrandingModel.getHeader();
            llBotHeader.removeAllViews();

            if (headerModel != null) {
                if (headerModel.getSize().equalsIgnoreCase(BundleUtils.COMPACT))
                    llBotHeader.addView(View.inflate(requireActivity(), R.layout.bot_header, null));
                else if (headerModel.getSize().equalsIgnoreCase(BundleUtils.LAYOUT_LARGE))
                    llBotHeader.addView(View.inflate(requireActivity(), R.layout.bot_header_3, null));
                else llBotHeader.addView(View.inflate(requireActivity(), R.layout.bot_header_2, null));

                if (!StringUtils.isNullOrEmpty(headerModel.getBg_color()))
                    llBotHeader.setBackgroundColor(Color.parseColor(headerModel.getBg_color()));
            } else llBotHeader.addView(View.inflate(requireActivity(), R.layout.bot_header, null));

            TextView tvBotTitle = llBotHeader.findViewById(R.id.tvBotTitle);
            TextView tvBotDesc = llBotHeader.findViewById(R.id.tvBotDesc);
            ImageView ivBotAvatar = llBotHeader.findViewById(R.id.ivBotAvatar);
            LinearLayout llBotAvatar = llBotHeader.findViewById(R.id.llBotAvatar);
            ImageView ivBotHelp = llBotHeader.findViewById(R.id.ivBotHelp);
            ImageView ivBotSupport = llBotHeader.findViewById(R.id.ivBotSupport);
            ImageView ivBotClose = llBotHeader.findViewById(R.id.ivBotClose);
            ImageView ivBotArrowBack = llBotHeader.findViewById(R.id.ivBotArrowBack);

            ivBotAvatar.setVisibility(View.GONE);
            ivBotHelp.setVisibility(View.GONE);
            ivBotSupport.setVisibility(View.GONE);
            ivBotClose.setVisibility(View.GONE);

            ivBotArrowBack.setOnClickListener(v -> composeFooterInterface.sendWithSomeDelay(BundleUtils.OPEN_WELCOME, "", 0, false));

            if (headerModel != null) {
                if (headerModel.getTitle() != null && !StringUtils.isNullOrEmpty(headerModel.getTitle().getName())) {
                    tvBotTitle.setText(headerModel.getTitle().getName());
                    if (!StringUtils.isNullOrEmpty(headerModel.getTitle().getColor())) {
                        tvBotTitle.setTextColor(Color.parseColor(headerModel.getTitle().getColor()));
                    }
                }

                if (headerModel.getSub_title() != null && !StringUtils.isNullOrEmpty(headerModel.getSub_title().getName())) {
                    tvBotDesc.setText(headerModel.getSub_title().getName());
                    if (!StringUtils.isNullOrEmpty(headerModel.getSub_title().getColor())) {
                        tvBotDesc.setTextColor(Color.parseColor(headerModel.getSub_title().getColor()));
                    }
                }

                if (headerModel.getIcon() != null && headerModel.getIcon().isShow()) {
                    ivBotAvatar.setVisibility(View.VISIBLE);

                    if (headerModel.getIcon().getType().equalsIgnoreCase(BundleUtils.CUSTOM)) {
                        llBotAvatar.setBackgroundResource(0);
                        Picasso.get().load(headerModel.getIcon().getIcon_url()).transform(new RoundedCornersTransform()).into(ivBotAvatar);
                        ivBotAvatar.setLayoutParams(new LinearLayout.LayoutParams((int) (40 * dp1), (int) (40 * dp1)));
                    } else {
                        switch (headerModel.getIcon().getIcon_url()) {
                            case "icon-1":
                                ivBotAvatar.setImageDrawable(ResourcesCompat.getDrawable(requireActivity().getResources(), R.drawable.ic_icon_1, requireActivity().getTheme()));
                                break;
                            case "icon-2":
                                ivBotAvatar.setImageDrawable(ResourcesCompat.getDrawable(requireActivity().getResources(), R.drawable.ic_icon_2, requireActivity().getTheme()));
                                break;
                            case "icon-3":
                                ivBotAvatar.setImageDrawable(ResourcesCompat.getDrawable(requireActivity().getResources(), R.drawable.ic_icon_3, requireActivity().getTheme()));
                                break;
                            case "icon-4":
                                ivBotAvatar.setImageDrawable(ResourcesCompat.getDrawable(requireActivity().getResources(), R.drawable.ic_icon_4, requireActivity().getTheme()));
                                break;
                        }

                        if (botBrandingModel.getGeneral() != null && botBrandingModel.getGeneral().getColors() != null && botBrandingModel.getGeneral().getColors().isUseColorPaletteOnly()) {
                            llBotAvatar.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(botBrandingModel.getGeneral().getColors().getPrimary())));
                            ivBotAvatar.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(botBrandingModel.getGeneral().getColors().getSecondary_text())));
                        }
                    }
                }

                if (headerModel.getButtons() != null) {
                    if (headerModel.getButtons().getHelp() != null && headerModel.getButtons().getHelp().isShow()) {
                        ivBotHelp.setVisibility(View.VISIBLE);

                        if (!StringUtils.isNullOrEmpty(headerModel.getIcons_color()))
                            ivBotHelp.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(headerModel.getIcons_color())));

                        ivBotHelp.setOnClickListener(v -> {
                            if (headerModel.getButtons().getHelp().getAction() != null && !StringUtils.isNullOrEmpty(headerModel.getButtons().getHelp().getAction().getType())) {
                                if (BundleConstants.BUTTON_TYPE_WEB_URL.equalsIgnoreCase(headerModel.getButtons().getHelp().getAction().getType()) || BundleConstants.BUTTON_TYPE_URL.equalsIgnoreCase(headerModel.getButtons().getHelp().getAction().getType())) {
                                    invokeGenericWebViewInterface.invokeGenericWebView(headerModel.getButtons().getHelp().getAction().getValue());
                                } else if (BundleConstants.BUTTON_TYPE_POSTBACK.equalsIgnoreCase(headerModel.getButtons().getHelp().getAction().getType())) {
                                    if (!StringUtils.isNullOrEmpty(headerModel.getButtons().getHelp().getAction().getValue()))
                                        composeFooterInterface.onSendClick(headerModel.getButtons().getHelp().getAction().getValue(), false);
                                    else if (!StringUtils.isNullOrEmpty(headerModel.getButtons().getHelp().getAction().getTitle())) {
                                        composeFooterInterface.onSendClick(headerModel.getButtons().getHelp().getAction().getTitle(), false);
                                    }
                                }
                            }
                        });
                    }

                    if (headerModel.getButtons().getLive_agent() != null && headerModel.getButtons().getLive_agent().isShow()) {
                        ivBotSupport.setVisibility(View.VISIBLE);

                        if (!StringUtils.isNullOrEmpty(headerModel.getIcons_color()))
                            ivBotSupport.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(headerModel.getIcons_color())));

                        ivBotSupport.setOnClickListener(v -> {
                            if (headerModel.getButtons().getLive_agent().getAction() != null && !StringUtils.isNullOrEmpty(headerModel.getButtons().getLive_agent().getAction().getType())) {
                                if (BundleConstants.BUTTON_TYPE_WEB_URL.equalsIgnoreCase(headerModel.getButtons().getLive_agent().getAction().getType()) || BundleConstants.BUTTON_TYPE_URL.equalsIgnoreCase(headerModel.getButtons().getLive_agent().getAction().getType())) {
                                    invokeGenericWebViewInterface.invokeGenericWebView(headerModel.getButtons().getLive_agent().getAction().getValue());
                                } else if (BundleConstants.BUTTON_TYPE_POSTBACK.equalsIgnoreCase(headerModel.getButtons().getLive_agent().getAction().getType())) {
                                    if (!StringUtils.isNullOrEmpty(headerModel.getButtons().getLive_agent().getAction().getValue()))
                                        composeFooterInterface.onSendClick(headerModel.getButtons().getLive_agent().getAction().getValue(), false);
                                    else if (!StringUtils.isNullOrEmpty(headerModel.getButtons().getLive_agent().getAction().getTitle())) {
                                        composeFooterInterface.onSendClick(headerModel.getButtons().getLive_agent().getAction().getTitle(), false);
                                    }
                                }
                            }
                        });
                    }

                    if (headerModel.getButtons().getClose() != null && headerModel.getButtons().getClose().isShow()) {
                        ivBotClose.setVisibility(View.VISIBLE);

                        if (!StringUtils.isNullOrEmpty(headerModel.getIcons_color()))
                            ivBotClose.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(headerModel.getIcons_color())));

                        ivBotClose.setOnClickListener(v -> {
                            BotSocketConnectionManager.killInstance();
                            requireActivity().finish();
                        });
                    }
                }

                if (!StringUtils.isNullOrEmpty(headerModel.getIcons_color()))
                    ivBotArrowBack.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(headerModel.getIcons_color())));

            }

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

    public void showTypingStatus() {
        botTypingStatusRl.setVisibility(View.VISIBLE);
    }

    public void setQuickRepliesIntoFooter(BotResponse botResponse) {
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
                        Calendar cal = Calendar.getInstance();
                        cal.setTimeInMillis(MaterialDatePicker.todayInUtcMilliseconds());
                        MaterialDatePicker.Builder<Long> builder = MaterialDatePicker.Builder.datePicker();
                        builder.setTitleText(payInner.getTitle());
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

                                String formattedDate = "";
                                formattedDate = ((stMonth + 1) > 9 ? (stMonth + 1) : "0" + (stMonth + 1)) + "-" + (stDay > 9 ? stDay : "0" + stDay) + "-" + stYear;

                                composeFooterInterface.onSendClick(formattedDate, false);
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

                                String formattedDate = "";
                                formattedDate = DateUtils.getMonthName(strMonth) + " " + strDay + DateUtils.getDayOfMonthSuffix(strDay) + ", " + strYear;
                                formattedDate = formattedDate + " to " + DateUtils.getMonthName(endMonth) + " " + endDay + DateUtils.getDayOfMonthSuffix(endDay) + ", " + endYear;

                                composeFooterInterface.onSendClick(formattedDate, false);
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

    public void addMessageToBotChatAdapter(BotResponse botResponse) {
        botsChatAdapter.addBaseBotMessage(botResponse);
        botTypingStatusRl.setVisibility(View.GONE);
        botsBubblesListView.smoothScrollToPosition(botsChatAdapter.getItemCount());
    }

    protected void initializeBotTypingStatus(View view, String mChannelIconURL) {
        botTypingStatusRl = view.findViewById(R.id.botTypingStatus);
        botTypingStatusIcon = view.findViewById(R.id.typing_status_item_cpv);
        botTypingStatusIcon.populateLayout(mBotNameInitials, mChannelIconURL, null, mBotIconId, Color.parseColor(SDKConfiguration.BubbleColors.quickReplyColor), true);
    }

    private void scrollToBottom() {
        final int count = botsChatAdapter.getItemCount();
        botsBubblesListView.post(() -> botsBubblesListView.smoothScrollToPosition(count - 1));
    }

    private final int limit = 10;

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

    private static Calendar getClearedUtc() {
        Calendar utc = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        utc.clear();
        return utc;
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
        else mContentViewModel.loadReconnectionChatHistory(_offset, limit, jwt, botsChatAdapter.getBaseBotMessageArrayList());
    }

}
