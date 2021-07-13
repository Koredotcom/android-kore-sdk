package com.kore.ai.widgetsdk.views.widgetviews;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import com.google.gson.Gson;
import com.kora.ai.widgetsdk.R;
import com.kore.ai.widgetsdk.activities.GenericWebViewActivity;
import com.kore.ai.widgetsdk.adapters.BotTableListTemlateAdapter;
import com.kore.ai.widgetsdk.events.EntityEditEvent;
import com.kore.ai.widgetsdk.events.KoreEventCenter;
import com.kore.ai.widgetsdk.fragments.WidgetActionSheetFragment;
import com.kore.ai.widgetsdk.listeners.VerticalListViewActionHelper;
import com.kore.ai.widgetsdk.managers.UserDataManager;
import com.kore.ai.widgetsdk.models.BaseCalenderTemplateModel;
import com.kore.ai.widgetsdk.models.BotCaourselButtonModel;
import com.kore.ai.widgetsdk.models.BotResponse;
import com.kore.ai.widgetsdk.models.ContactViewListModel;
import com.kore.ai.widgetsdk.models.KnowledgeCollectionModel;
import com.kore.ai.widgetsdk.models.WelcomeChatSummaryModel;
import com.kore.ai.widgetsdk.models.Widget;
import com.kore.ai.widgetsdk.models.WidgetTableListDataModel;
import com.kore.ai.widgetsdk.models.WidgetsModel;
import com.kore.ai.widgetsdk.models.searchskill.PanelLevelData;
import com.kore.ai.widgetsdk.network.NetworkEvents;
import com.kore.ai.widgetsdk.room.models.AuthData;
import com.kore.ai.widgetsdk.room.models.UserData;
import com.kore.ai.widgetsdk.utils.BundleConstants;
import com.kore.ai.widgetsdk.utils.Constants;
import com.kore.ai.widgetsdk.utils.KaUtility;
import com.kore.ai.widgetsdk.utils.NetworkUtility;
import com.kore.ai.widgetsdk.utils.StringUtils;
import com.kore.ai.widgetsdk.utils.Utility;
import com.kore.ai.widgetsdk.utils.Utils;
import com.kore.ai.widgetsdk.utils.WidgetDataLoader;
import com.kore.ai.widgetsdk.utils.WidgetViewMoreEnum;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.kore.ai.widgetsdk.utils.AppUtils.getMapObject;
import static com.kore.ai.widgetsdk.utils.KaUtility.showEmailIntent;

public class TableListWidgetView extends LinearLayout implements VerticalListViewActionHelper {
    private float dp1;
    public ImageView menu_btn,icon_image_load;
    public ListView list_widget_root_recycler;
    public TextView  widget_header;
    public View view_more;
    public ProgressBar progress;
    private View rootView;
    private BotTableListTemlateAdapter listWidgetAdapter = null;
    private AuthData authData;
    private UserData userData;
    public ImageView imgMenu;
    public TextView tvText;
    public TextView tvUrl;
    public TextView tvButton, tvFillForm;
    public LinearLayout tvButtonParent, llFormData;
    private String jwtToken;
    private TextView pin_view,panel_name_view;
    public WidgetsModel getWidget() {
        return mWidget;
    }
    PanelLevelData panelData;

    public void setWidget(WidgetsModel mWidget, PanelLevelData panelData, String trigger, String jwtToken) {
        this.mWidget = mWidget;
        this.panelData=panelData;
        this.trigger = trigger;
        this.jwtToken = jwtToken;
        widget_header.setText(mWidget.getTitle());
//        pin_view.setText(mWidget.isPinned() ? context.getResources().getString(R.string.icon_31) :context.getResources().getString(R.string.icon_32));
        panel_name_view.setText(KaUtility.getPanelFormatedName(mWidget.getName()));
        panel_name_view.setVisibility(KaUtility.isTitleAndPanelNameMatch(mWidget.getName(),name));
        loadData(false);
    }

    private WidgetsModel mWidget;
    private String name;
    private String trigger;
    WidgetViewMoreEnum widgetViewMoreEnum;
    Context context;
    public TableListWidgetView(Context context, String name, WidgetViewMoreEnum widgetViewMoreEnum) {
        super(context);
        this.context=context;
        this.name = name;

        this.widgetViewMoreEnum=widgetViewMoreEnum;
        init();

    }
    private void getUserData() {
        authData = UserDataManager.getAuthData();
        userData = UserDataManager.getUserData();

    }

    private void init() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.tablelist_widget_layout, this, true);
        rootView = view.findViewById(R.id.meeting_root_view);
        pin_view = view.findViewById(R.id.pin_view);
        panel_name_view=view.findViewById(R.id.panel_name_view);
        icon_image_load=view.findViewById(R.id.icon_image_load);
        view_more = view.findViewById(R.id.view_more);
        menu_btn = view.findViewById(R.id.menu_meeting_btn);
        menu_btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
//                showPopUpMenu();
            }
        });
        // KoreEventCenter.register(this);
        list_widget_root_recycler = view.findViewById(R.id.upcoming_meeting_root_recycler);
//        list_widget_root_recycler.setLayoutManager(new LinearLayoutManager(getContext()));

        pin_view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

//                KaRestAPIHelper.actionPinnAndUnPinn(authData.accessToken, userData.getId(),mWidget,panelData, new PinUnPinnCallBack() {
//                    @Override
//                    public void success() {
////                        mWidget.setPinned(true);
//                        KaUtility.setPinnedViewState(context, true, pin_view,panelData.get_id());
//
//                    }
//
//                    @Override
//                    public void failure(Throwable t) {
//                        if(!Utils.isNetworkAvailable(context))
//                            ToastUtils.showToast(context,context.getResources().getString(R.string.no_internet_connection));
//                        else
//                            ToastUtils.showToast(context,context.getResources().getString(R.string.oops));
//                    }
//                });


            }
        });
        view_more = view.findViewById(R.id.view_more);
        view_more.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
//                String data = new Gson().toJson(listWidgetAdapter.getData());
//                Intent intent = new Intent(getContext(), FullViewActivity.class);
//                intent.putExtra(BundleConstants.TEMPLATE_TYPE, BotResponse.TEMPLATE_TYPE_LIST);
//                intent.putExtra(BundleConstants.TITLE_NAME,mWidget.getTitle());
//                intent.putExtra(BundleConstants.TRIGGER,trigger);
//                intent.putExtra(BundleConstants.FROM_WIDGET, true);
//                intent.putExtra(BundleConstants.DATA, data);
//                getContext().startActivity(intent);
            }
        });
        widget_header = view.findViewById(R.id.meeting_header);
        progress = view.findViewById(R.id.meeting_progress);
        dp1 = (int) Utility.convertDpToPixel(context, 1);
        listWidgetAdapter = new BotTableListTemlateAdapter(getContext(), 4);
//        listWidgetAdapter.setVerticalListViewActionHelper(this);
        imgMenu = view.findViewById(R.id.icon_image);
        tvButton = view.findViewById(R.id.tv_button);
        tvText = view.findViewById(R.id.tv_text);
        tvUrl = view.findViewById(R.id.tv_url);
        tvButtonParent = view.findViewById(R.id.tv_values_layout);
        llFormData = view.findViewById(R.id.llFormData);
        tvFillForm = view.findViewById(R.id.tvFillForm);
        getUserData();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        KoreEventCenter.register(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        KoreEventCenter.unregister(this);
    }

//    @Override
//    protected void onLayout(boolean changed, int l, int t, int r, int b) {
//        final int count = getChildCount();
//        int parentWidth = getMeasuredWidth();
//
//        //get the available size of child view
//        int childLeft = 0;
//        int childTop = 0;
//
//        for (int i = 0; i < count; i++) {
//            View child = getChildAt(i);
//            if (child.getVisibility() != GONE) {
//                LayoutUtils.layoutChild(child, childLeft, childTop);
//                childTop += child.getMeasuredHeight();
//            }
//        }
//    }

//    @Override
//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        int parentWidth = MeasureSpec.getSize(widthMeasureSpec);
//        int wrapSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
//
//        int totalHeight = getPaddingTop();
//        int childWidthSpec;
//
//        childWidthSpec = MeasureSpec.makeMeasureSpec((int) (parentWidth - (2 * dp1)), MeasureSpec.EXACTLY);
//        MeasureUtils.measure(rootView, childWidthSpec, wrapSpec);
//
//        totalHeight += rootView.getMeasuredHeight() + getPaddingBottom() + getPaddingTop();
//
//        int parentHeightSpec = MeasureSpec.makeMeasureSpec(totalHeight, MeasureSpec.EXACTLY);
//        int parentWidthSpec = MeasureSpec.makeMeasureSpec(rootView.getMeasuredWidth(), MeasureSpec.AT_MOST);
//        setMeasuredDimension(parentWidthSpec, parentHeightSpec);
//    }

    @Override
    public void knowledgeItemClicked(Bundle extras, boolean isKnowledge) {

    }

    @Override
    public void driveItemClicked(BotCaourselButtonModel botCaourselButtonModel) {

    }

    @Override
    public void emailItemClicked(String action, HashMap customData) {

    }

    @Override
    public void calendarItemClicked(String action, BaseCalenderTemplateModel model) {
        if (BotResponse.TEMPLATE_TYPE_CAL_EVENTS_WIDGET.equalsIgnoreCase(action)) {
//            Intent intent = new Intent(getContext(), ViewMeetingDetailsActivity.class);
//            String data = new Gson().toJson(model);
//            intent.putExtra("data", data);
//            intent.putExtra("fromMeetingWidget", true);
//
//            CalenderEventData _data = ((WCalEventsTemplateModel) model).getData();
//            intent.putExtra(BundleConstants.REQ_TEXT_TO_DISPLAY,_data.getReqTextToDispForDetails());
//
//            getContext().startActivity(intent);
        }
    }

    @Override
    public void tasksSelectedOrDeselected(boolean selecetd) {

    }

    @Override
    public void widgetItemSelected(boolean isSelected, int count) {

    }

    @Override
    public void navigationToDialAndJoin(String actiontype, String actionLink) {

        if (actiontype.equalsIgnoreCase("dial")) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("type", "dial");
            map.put("dial", actionLink);
            KaUtility.handleCustomActions(map, getContext());
        } else if (actiontype.equalsIgnoreCase("url")) {
            HashMap<String, Object> map2 = new HashMap<>();
            map2.put("type", "url");
            map2.put("url", actionLink);
            KaUtility.handleCustomActions(map2, getContext());
        } else if (actiontype.equalsIgnoreCase("meetingUrl")) {
            Intent intent = new Intent(getContext(), GenericWebViewActivity.class);
            intent.putExtra("url", actionLink);
            intent.putExtra("header", getContext().getResources().getString(R.string.app_name));
            getContext().startActivity(intent);
        }

    }

    @Override
    public void takeNotesNavigation(BaseCalenderTemplateModel baseCalenderTemplateModel) {
//        Intent intent = new Intent(getContext(), MeetingNotesCreationActivity.class);
//        intent.putExtra(BundleConstants.NOTES_DATA,baseCalenderTemplateModel);
//
//        CalenderEventData _data = ((WCalEventsTemplateModel) baseCalenderTemplateModel).getData();
//        intent.putExtra(BundleConstants.REQ_TEXT_TO_DISPLAY,_data.getReqTextToDispForDetails());
//
//        getContext().startActivity(intent);
    }

    @Override
    public void meetingNotesNavigation(Context context, String mId, String eId) {

    }

    @Override
    public void meetingWidgetViewMoreVisibility(boolean visible) {
        if(view_more!=null)
            view_more.setVisibility(visible&& Utility.isViewMoreVisible(widgetViewMoreEnum) ?View.VISIBLE: View.GONE);
    }

    @Override
    public void calendarContactItemClick(ContactViewListModel model) {

    }

    @Override
    public void welcomeSummaryItemClick(WelcomeChatSummaryModel model) {

    }

    @Override
    public void knowledgeCollectionItemClick(KnowledgeCollectionModel.DataElements elements, String id) {

    }

    public void onEvent(NetworkEvents.NetworkConnectivityEvent event) {
        if(event!=null&&event.isNetworkConnectivity()) {
            loadData(true);
        }
    }

    private void loadData(boolean shouldRefresh) {
        if(mWidget.getCallbackURL()==null) {
            return;
        }
        progress.setVisibility(View.VISIBLE);

        Map<String,Object> result = getMapObject(mWidget.getParam());
        WidgetDataLoader.loadTableListWidgetServiceData(mWidget.getCallbackURL(), Utils.ah(jwtToken),result)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<WidgetTableListDataModel>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(WidgetTableListDataModel model) {
//                        WidgetListDataModel botOptionsModel = gson.fromJson(resp, WidgetListDataModel.class);
                        afterDataLoad(model);
                    }

                    @Override
                    public void onError(Throwable e) {
                        progress.setVisibility(View.GONE);
                        if (e.getMessage().equalsIgnoreCase("410") || e.getMessage().equalsIgnoreCase("401"))
                        {
                            //KoreEventCenter.post(new OnTokenExpired());
                        }

                        String msg;
                        Drawable drawable=null;
                        if (!NetworkUtility.isNetworkConnectionAvailable(TableListWidgetView.this.getContext())) {
                            //No Internet Connect
                            msg=getResources().getString(R.string.no_internet_connection);
                            drawable=getResources().getDrawable(R.drawable.no_internet);
                        } else {
                            //Oops some thing went wrong
                            msg=getResources().getString(R.string.oops);
                            drawable=getResources().getDrawable(R.drawable.oops_icon);
                        }
//                        listWidgetAdapter.setWidget(null);
//                        listWidgetAdapter.setMessage(msg,drawable);
//                        view_more.setVisibility(GONE);
//                        list_widget_root_recycler.setAdapter(listWidgetAdapter);
//                        listWidgetAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onComplete() {
                        progress.setVisibility(View.GONE);
                    }
                });
    }

    public void buttonAction(String utt, boolean appendUtterance){
        String utterance = null;

        utterance = utt;

        if(utterance == null)return;
        if(utterance !=null && (utterance.startsWith("tel:") || utterance.startsWith("mailto:"))){
            if(utterance.startsWith("tel:")){
                KaUtility.launchDialer(getContext(),utterance);
            }else if(utterance.startsWith("mailto:")){
                KaUtility.showEmailIntent((Activity) getContext(),utterance.split(":")[1]);
            }
            return;
        }
        EntityEditEvent event = new EntityEditEvent();
        StringBuffer msg = new StringBuffer("");
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("refresh", Boolean.TRUE);
        if(appendUtterance && trigger!= null)
            msg = msg.append(trigger).append(" ");
        msg.append(utterance);
        event.setMessage(msg.toString());
        event.setPayLoad(new Gson().toJson(hashMap));
        event.setScrollUpNeeded(true);
        KoreEventCenter.post(event);

    }
    public void buttonAction(Widget.Button button, boolean appendUtterance){
        String utterance = null;
        if(button != null){
            utterance = button.getUtterance();
        }
        if(utterance == null)return;
        if(utterance !=null && (utterance.startsWith("tel:") || utterance.startsWith("mailto:"))){
            if(utterance.startsWith("tel:")){
                KaUtility.launchDialer(getContext(),utterance);
            }else if(utterance.startsWith("mailto:")){
                showEmailIntent((Activity) getContext(),utterance.split(":")[1]);
            }
            return;
        }
        EntityEditEvent event = new EntityEditEvent();
        StringBuffer msg = new StringBuffer("");
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("refresh", Boolean.TRUE);
        if(appendUtterance && trigger!= null)
            msg = msg.append(trigger).append(" ");
        msg.append(utterance);
        event.setMessage(msg.toString());
        event.setPayLoad(new Gson().toJson(hashMap));
        event.setScrollUpNeeded(true);
        KoreEventCenter.post(event);

        /*      try {


         *//* if (isFullView) {
                ((Activity) mContext).finish();
            }*//*
        } catch (Exception e) {

        }*/
    }
    private void afterDataLoad(final WidgetTableListDataModel model){

        widget_header.setText(mWidget.getTitle());
        if(model.getData().get(0).getHeaderOptions() != null && model.getData().get(0).getHeaderOptions().getType()!=null ) {
            switch (model.getData().get(0).getHeaderOptions().getType()){
                case "button":
                    icon_image_load.setVisibility(GONE);
                    imgMenu.setVisibility(GONE);
                    tvText.setVisibility(GONE);
                    tvUrl.setVisibility(GONE);
                    tvButtonParent.setVisibility(VISIBLE);
                    String btnTitle = "";
                    if(model.getData().get(0).getHeaderOptions().getButton() != null && model.getData().get(0).getHeaderOptions().getButton().getTitle() != null)
                        btnTitle = model.getData().get(0).getHeaderOptions().getButton().getTitle();
                    else
                        btnTitle = model.getData().get(0).getHeaderOptions().getText();
                    if(!StringUtils.isNullOrEmpty(btnTitle))
                        tvButton.setText(btnTitle);
                    else
                        tvButtonParent.setVisibility(GONE);


                    tvButton.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (Constants.SKILL_SELECTION.equalsIgnoreCase(Constants.SKILL_HOME) || TextUtils.isEmpty(Constants.SKILL_SELECTION) ||
                                    (!StringUtils.isNullOrEmpty(name) && !name.equalsIgnoreCase(Constants.SKILL_SELECTION))) {
                                buttonAction(model.getData().get(0).getHeaderOptions().getButton(), true);
                            } else {
                                buttonAction(model.getData().get(0).getHeaderOptions().getButton(), false);
                            }
                        }
                    });

                    break;
                case "menu":
                    icon_image_load.setVisibility(GONE);
                    imgMenu.setVisibility(VISIBLE);
                    tvText.setVisibility(GONE);
                    tvButtonParent.setVisibility(GONE);
                    tvUrl.setVisibility(GONE);

                    imgMenu.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if ( model.getData().get(0).getHeaderOptions()!= null &&  model.getData().get(0).getHeaderOptions().getMenu()!= null && model.getData().get(0).getHeaderOptions().getMenu().size() > 0) {

                                WidgetActionSheetFragment bottomSheetDialog = new WidgetActionSheetFragment();
                                bottomSheetDialog.setisFromFullView(false);
                                bottomSheetDialog.setSkillName(name,trigger);
                                bottomSheetDialog.setData(model,true);

                                bottomSheetDialog.setVerticalListViewActionHelper(null);
                                bottomSheetDialog.show(((FragmentActivity) getContext()).getSupportFragmentManager(), "add_tags");

                            }
                        }
                    });


                    break;
                case "text":
                    icon_image_load.setVisibility(GONE);
                    imgMenu.setVisibility(GONE);
                    tvText.setVisibility(VISIBLE);
                    tvText.setText(model.getData().get(0).getHeaderOptions().getText());
                    tvButtonParent.setVisibility(GONE);
                    tvUrl.setVisibility(GONE);
                    break;
                case "url":
                    icon_image_load.setVisibility(GONE);
                    imgMenu.setVisibility(GONE);
                    tvText.setVisibility(GONE);
                    SpannableString content = new SpannableString(model.getData().get(0).getHeaderOptions().getUrl().getTitle()!=null?model.getData().get(0).getHeaderOptions().getUrl().getTitle():model.getData().get(0).getHeaderOptions().getUrl().getLink());
                    content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
                    tvUrl.setText(content);
                    tvButtonParent.setVisibility(GONE);
                    tvUrl.setVisibility(VISIBLE);
                    tvUrl.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(model.getData().get(0).getHeaderOptions().getUrl().getLink() != null) {
                                Intent intent = new Intent(getContext(), GenericWebViewActivity.class);
                                intent.putExtra("url", model.getData().get(0).getHeaderOptions().getUrl().getLink());
                                intent.putExtra("header", getContext().getResources().getString(R.string.app_name));
                                getContext().startActivity(intent);
                            }
                        }
                    });
                    break;

                case "image":
                    icon_image_load.setVisibility(VISIBLE);
                    if(model.getData().get(0).getHeaderOptions().getImage()!=null&&model.getData().get(0).getHeaderOptions().getImage().getImage_src()!=null) {
                        Picasso.get().load(model.getData().get(0).getHeaderOptions().getImage().getImage_src()).into(icon_image_load);
                        icon_image_load.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {


                                if (Constants.SKILL_SELECTION.equalsIgnoreCase(Constants.SKILL_HOME) || TextUtils.isEmpty(Constants.SKILL_SELECTION) ||
                                        (!StringUtils.isNullOrEmpty(name) && !name.equalsIgnoreCase(Constants.SKILL_SELECTION))) {
                                    // buttonAction(model.getHeaderOptions().getButton(), true);
                                    buttonAction(model.getData().get(0).getHeaderOptions().getImage().getUtterance()!=null?model.getData().get(0).getHeaderOptions().getImage().getUtterance():model.getData().get(0).getHeaderOptions().getImage().getPayload()!=null?model.getData().get(0).getHeaderOptions().getImage().getPayload():"",true);

                                } else {
                                    //buttonAction(model.getHeaderOptions().getButton(), false);
                                    buttonAction(model.getData().get(0).getHeaderOptions().getImage().getUtterance()!=null?model.getData().get(0).getHeaderOptions().getImage().getUtterance():model.getData().get(0).getHeaderOptions().getImage().getPayload()!=null?model.getData().get(0).getHeaderOptions().getImage().getPayload():"",false);

                                }
                            }
                            //       }
                        });
                    }
                    break;


            }

        }
        if (model != null && model.getData().get(0).getRecords() != null && model.getData().get(0).getRecords().size() > 0 && !model.getData().get(0).getTemplateType().equals("loginURL")) {
            listWidgetAdapter = new BotTableListTemlateAdapter(getContext(), model.getData().get(0).getRecords().size());
            if (model.getData().get(0).getRecords() != null && model.getData().get(0).getRecords().size() > 3&& Utility.isViewMoreVisible(widgetViewMoreEnum)) {
                view_more.setVisibility(View.VISIBLE);
            }

            listWidgetAdapter.setBotListModelArrayList(new ArrayList<>(model.getData().get(0).getRecords()));
//            listWidgetAdapter.setMultiActions(model.());
//            calendarEventsAdapter.setPreviewLength(model.getPreview_length());
            list_widget_root_recycler.setAdapter(listWidgetAdapter);
//            listWidgetAdapter.setPreviewLength(3);
            listWidgetAdapter.notifyDataSetChanged();
        }
        else if(model.getData().get(0).getTemplateType().equals("loginURL")){
            if(model != null ) {
                listWidgetAdapter.setBotListModelArrayList(null);
//                listWidgetAdapter.setLoginModel(model.getData().get(0).getLoginModel());
                list_widget_root_recycler.setAdapter(listWidgetAdapter);
//                listWidgetAdapter.setLoginNeeded(true);
            }
        }
        else if(model.getData().get(0).getTemplateType().equals("form"))
        {
            if(model != null)
            {
                list_widget_root_recycler.setVisibility(GONE);
                llFormData.setVisibility(VISIBLE);
                tvFillForm.setText(mWidget.getTitle());

                tvFillForm.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(getContext() instanceof Activity &&model.getData().get(0).getFormLink()!=null&&!StringUtils.isNullOrEmptyWithTrim(model.getData().get(0).getFormLink())) {
                            Intent intent = new Intent(getContext(), GenericWebViewActivity.class);
                            intent.putExtra("url", model.getData().get(0).getFormLink());
                            intent.putExtra("header",mWidget.getTitle());
                            ((Activity)getContext()).startActivityForResult(intent, BundleConstants.REQ_CODE_REFRESH_CURRENT_PANEL);
                        }else{
                            Toast.makeText(getContext(),"Instance not activity",Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        }
        else {
            listWidgetAdapter.setBotListModelArrayList(null);
            list_widget_root_recycler.setAdapter(listWidgetAdapter);
            listWidgetAdapter.notifyDataSetChanged();
        }
        //KoreEventCenter.post(new ShowLayoutEvent(0));
    }


}
