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
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.TimeZone;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
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
import kore.botssdk.models.BotHistory;
import kore.botssdk.models.BotHistoryMessage;
import kore.botssdk.models.BotInfoModel;
import kore.botssdk.models.BotRequest;
import kore.botssdk.models.BotResponse;
import kore.botssdk.models.BrandingBodyModel;
import kore.botssdk.models.BrandingHeaderModel;
import kore.botssdk.models.Component;
import kore.botssdk.models.ComponentModel;
import kore.botssdk.models.PayloadInner;
import kore.botssdk.models.PayloadOuter;
import kore.botssdk.models.QuickReplyTemplate;
import kore.botssdk.net.RestBuilder;
import kore.botssdk.net.RestResponse;
import kore.botssdk.net.SDKConfiguration;
import kore.botssdk.net.WebHookRestBuilder;
import kore.botssdk.retroresponse.ServerBotMsgResponse;
import kore.botssdk.utils.BundleConstants;
import kore.botssdk.utils.BundleUtils;
import kore.botssdk.utils.DateUtils;
import kore.botssdk.utils.StringUtils;
import kore.botssdk.utils.Utils;
import kore.botssdk.view.CircularProfileView;
import kore.botssdk.view.LoadingDots;
import kore.botssdk.view.QuickReplyView;
import kore.botssdk.view.viewUtils.RoundedCornersTransform;
import kore.botssdk.websocket.SocketWrapper;
import retrofit2.Call;
import retrofit2.Response;

@SuppressWarnings("UnKnownNullness")
public class NewBotContentFragment extends Fragment implements BotContentFragmentUpdate {
    private RelativeLayout rlBody;
    private RecyclerView botsBubblesListView;
    private ChatAdapter botsChatAdapter;
    private QuickReplyView quickReplyView;
    private LinearLayout botTypingStatusRl;
    private CircularProfileView botTypingStatusIcon;
    private ComposeFooterInterface composeFooterInterface;
    private InvokeGenericWebViewInterface invokeGenericWebViewInterface;
    private String mChannelIconURL;
    private String mBotNameInitials;
    private int mBotIconId;
    private boolean fetching = false;
    private final Gson gson = new Gson();
    private SwipeRefreshLayout swipeRefreshLayout;
    private int offset = 0;
    private SharedPreferences sharedPreferences;
    private String jwt;
    private RelativeLayout llBotHeader;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = View.inflate(requireActivity(), R.layout.bot_content_layout, null);
        getBundleInfo();
        findViews(view);
        initializeBotTypingStatus(view, mChannelIconURL);
        setupAdapter();

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
            boolean shallShowProfilePic = bundle.getBoolean(BundleUtils.SHOW_PROFILE_PIC, false);
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

    public void showTypingStatus(BotResponse botResponse) {
        if (botTypingStatusRl != null && botResponse.getMessage() != null && !botResponse.getMessage().isEmpty()) {
            botTypingStatusRl.setVisibility(View.VISIBLE);
            if (StringUtils.isNullOrEmpty(mChannelIconURL) && !StringUtils.isNullOrEmpty(botResponse.getIcon())) {
                mChannelIconURL = botResponse.getIcon();
                botTypingStatusIcon.populateLayout(mBotNameInitials, mChannelIconURL, null, -1, Color.parseColor(SDKConfiguration.BubbleColors.quickReplyColor), true);
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
                        int strYear = cal.get(Calendar.YEAR);
                        int strMonth = cal.get(Calendar.MONTH);
                        int strDay = cal.get(Calendar.DAY_OF_MONTH);

                        MaterialDatePicker.Builder<Long> builder = MaterialDatePicker.Builder.datePicker();
                        builder.setTitleText(payInner.getTitle());
                        builder.setInputMode(MaterialDatePicker.INPUT_MODE_CALENDAR);
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
        LoadingDots ldDots = view.findViewById(R.id.ldDots);
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

    }

    @Override
    public void onReconnectionChatHistory(ArrayList<BaseBotMessage> list, int offset, boolean isReconnectionHistory) {

    }

    private Observable<ServerBotMsgResponse> getHistoryRequest(final int _offset, final int limit) {
        return Observable.create(emitter -> {
            try {
                ServerBotMsgResponse re = new ServerBotMsgResponse();

                Call<BotHistory> _resp;
                if (SDKConfiguration.Client.isWebHook) {
                    _resp = WebHookRestBuilder.getRestAPI().getWebHookBotHistory("bearer " + jwt, SDKConfiguration.Client.bot_id, SDKConfiguration.Client.bot_id, limit, _offset);
                } else {
                    _resp = RestBuilder.getRestAPI().getBotHistory("bearer " + SocketWrapper.getInstance(requireActivity().getApplicationContext()).getAccessToken(), SDKConfiguration.Client.bot_id, limit, _offset, true);
                }
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
                                String data = components.get(0).getData().getText().replaceAll("&quot;", "\"");
                                try {
                                    PayloadOuter outer = gson.fromJson(data, PayloadOuter.class);
                                    BotResponse r = Utils.buildBotMessage(outer, msg.getBotId(), SDKConfiguration.Client.bot_name, msg.getCreatedOn(), msg.getId());
                                    r.setType(msg.getType());
                                    r.setIcon(history.getIcon());
                                    msgs.add(r);
                                } catch (com.google.gson.JsonSyntaxException ex) {
                                    BotResponse r = Utils.buildBotMessage(data, msg.getBotId(), SDKConfiguration.Client.bot_name, msg.getCreatedOn(), msg.getId());
                                    r.setType(msg.getType());
                                    r.setIcon(history.getIcon());
                                    msgs.add(r);
                                }
                            } else {
                                try {
                                    String message = msg.getComponents().get(0).getData().getText();
                                    RestResponse.BotMessage botMessage = new RestResponse.BotMessage(message);
                                    RestResponse.BotPayLoad botPayLoad = new RestResponse.BotPayLoad();
                                    botPayLoad.setMessage(botMessage);
                                    BotInfoModel botInfo = new BotInfoModel(SDKConfiguration.Client.bot_name, SDKConfiguration.Client.bot_id, null);
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
        });
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

        getHistoryRequest(_offset, limit).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<ServerBotMsgResponse>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
            }

            @Override
            public void onNext(@NonNull ServerBotMsgResponse re) {
                fetching = false;

                if (swipeRefreshLayout != null) {
                    swipeRefreshLayout.setRefreshing(false);
                }

                ArrayList<BaseBotMessage> list = null;
                list = re.getBotMessages();
                offset = _offset + re.getOriginalSize();

                if (list != null && list.size() > 0) {
                    addMessagesToBotChatAdapter(list, offset == 0);
                }

                if ((list == null || list.size() < limit) && offset != 0) {
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
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

    public int getAdapterCount() {
        if (botsChatAdapter != null)
            return botsChatAdapter.getItemCount();
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

        getHistoryRequest(_offset, limit, false).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<ServerBotMsgResponse>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
            }

            @Override
            public void onNext(@NonNull ServerBotMsgResponse re) {
                fetching = false;

                if (swipeRefreshLayout != null) {
                    swipeRefreshLayout.setRefreshing(false);
                }

                ArrayList<BaseBotMessage> list = null;
                list = re.getBotMessages();
                offset = _offset + re.getOriginalSize();

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
            public void onError(@NonNull Throwable e) {
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

    private Observable<ServerBotMsgResponse> getHistoryRequest(final int _offset, final int limit, boolean setIconUrl) {
        return Observable.create(emitter -> {
            try {
                ServerBotMsgResponse re = new ServerBotMsgResponse();

                Call<BotHistory> _resp = RestBuilder.getRestAPI().getBotHistory("bearer " + SocketWrapper.getInstance(requireActivity()).getAccessToken(), SDKConfiguration.Client.bot_id, limit, _offset, true);
                Response<BotHistory> rBody = _resp.execute();
                BotHistory history = rBody.body();

                if (rBody.isSuccessful() && history != null) {

                    if (!StringUtils.isNullOrEmpty(history.getIcon()) && setIconUrl)
                        SDKConfiguration.BubbleColors.setIcon_url(history.getIcon());

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
                                    BotResponse r = Utils.buildBotMessage(outer, msg.getBotId(), SDKConfiguration.Client.bot_name, msg.getCreatedOn(), msg.getId());
                                    r.setType(msg.getType());
                                    msgs.add(r);
                                } catch (com.google.gson.JsonSyntaxException ex) {
                                    BotResponse r = Utils.buildBotMessage(data, msg.getBotId(), SDKConfiguration.Client.bot_name, msg.getCreatedOn(), msg.getId());
                                    r.setType(msg.getType());
                                    msgs.add(r);
                                }
                            } else {
                                try {
                                    String message = msg.getComponents().get(0).getData().getText();
                                    RestResponse.BotMessage botMessage = new RestResponse.BotMessage(message);
                                    RestResponse.BotPayLoad botPayLoad = new RestResponse.BotPayLoad();
                                    botPayLoad.setMessage(botMessage);
                                    BotInfoModel botInfo = new BotInfoModel(SDKConfiguration.Client.bot_name, SDKConfiguration.Client.bot_id, null);
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
                        re.setOriginalSize(messages.size());
                    }
                }

                emitter.onNext(re);
                emitter.onComplete();
            } catch (Exception e) {
                emitter.onError(e);
            }
        });
    }
}