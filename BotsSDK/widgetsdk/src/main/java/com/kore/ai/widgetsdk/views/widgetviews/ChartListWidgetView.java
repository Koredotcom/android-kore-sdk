package com.kore.ai.widgetsdk.views.widgetviews;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.kora.ai.widgetsdk.R;
import com.kore.ai.widgetsdk.activities.GenericWebViewActivity;
import com.kore.ai.widgetsdk.adapters.ChartListWidgetAdapter;
import com.kore.ai.widgetsdk.application.AppControl;
import com.kore.ai.widgetsdk.events.KoreEventCenter;
import com.kore.ai.widgetsdk.interfaces.PinUnPinnCallBack;
import com.kore.ai.widgetsdk.listeners.UpdateRefreshItem;
import com.kore.ai.widgetsdk.listeners.VerticalListViewActionHelper;
import com.kore.ai.widgetsdk.managers.UserDataManager;
import com.kore.ai.widgetsdk.models.BaseCalenderTemplateModel;
import com.kore.ai.widgetsdk.models.BotCaourselButtonModel;
import com.kore.ai.widgetsdk.models.BotResponse;
import com.kore.ai.widgetsdk.models.ContactViewListModel;
import com.kore.ai.widgetsdk.models.KnowledgeCollectionModel;
import com.kore.ai.widgetsdk.models.WelcomeChatSummaryModel;
import com.kore.ai.widgetsdk.models.Widget;
import com.kore.ai.widgetsdk.models.WidgetsModel;
import com.kore.ai.widgetsdk.models.searchskill.PanelLevelData;
import com.kore.ai.widgetsdk.net.KaRestAPIHelper;
import com.kore.ai.widgetsdk.network.NetworkEvents;
import com.kore.ai.widgetsdk.room.models.AuthData;
import com.kore.ai.widgetsdk.room.models.UserData;
import com.kore.ai.widgetsdk.utils.KaUtility;
import com.kore.ai.widgetsdk.utils.ToastUtils;
import com.kore.ai.widgetsdk.utils.Utility;
import com.kore.ai.widgetsdk.utils.WidgetDataLoader;
import com.kore.ai.widgetsdk.utils.WidgetViewMoreEnum;
import com.kore.ai.widgetsdk.views.viewutils.LayoutUtils;
import com.kore.ai.widgetsdk.views.viewutils.MeasureUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
//import kore.botssdk.activity.GenericWebViewActivity;

import static com.kore.ai.widgetsdk.utils.AppUtils.getMapObject;

public class ChartListWidgetView extends ViewGroup implements VerticalListViewActionHelper {
    private float dp1;
    public ImageView menu_meeting_btn;
    public RecyclerView upcoming_meeting_root_recycler;
    public TextView view_more, meeting_header;
    public ProgressBar meeting_progress;
    private View rootView;
    private ChartListWidgetAdapter defaultWidgetAdapter = null;
    private AuthData authData;
    private UserData userData;
    private TextView pin_view,panel_name_view;
    public WidgetsModel getWidget() {
        return mWidget;
    }

    public void setWidget(WidgetsModel mWidget, PanelLevelData panelData) {
        this.mWidget = mWidget;
        this.panelData=panelData;
        meeting_header.setText(mWidget.getTitle());
        loadData(false);
    }
    public void onEvent(NetworkEvents.NetworkConnectivityEvent event) {
        if(event!=null&&event.isNetworkConnectivity()) {
            loadData(true);
        }
    }
    private WidgetsModel mWidget;
    private String name;
    final Handler handler = new Handler();
    WidgetViewMoreEnum widgetViewMoreEnum;
    Context context;
    PanelLevelData panelData;
    public ChartListWidgetView(Context context, UpdateRefreshItem mListener, String name, WidgetViewMoreEnum widgetViewMoreEnum) {
        super(context);
        this.name = name;
        this.context=context;

        this.widgetViewMoreEnum=widgetViewMoreEnum;
        init();

    }
    private void getUserData() {
        authData = UserDataManager.getAuthData();
        userData = UserDataManager.getUserData();
    }

    private void init() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.chart_widget_layout, this, true);
        rootView = view.findViewById(R.id.meeting_root_view);
        panel_name_view=view.findViewById(R.id.panel_name_view);
        pin_view = view.findViewById(R.id.pin_view);
        menu_meeting_btn = view.findViewById(R.id.menu_meeting_btn);
        view_more = view.findViewById(R.id.view_more);
       // KoreEventCenter.register(this);

        pin_view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

//                KaRestAPIHelper.actionPinnAndUnPinn(authData.accessToken, userData.getId(),mWidget,panelData, new PinUnPinnCallBack() {
//                    @Override
//                    public void success() {
//                        mWidget.setPinned(!mWidget.isPinned());
//                        KaUtility.setPinnedViewState(context, mWidget.isPinned(), pin_view,panelData.get_id());
//
//                    }
//
//                    @Override
//                    public void failure(Throwable t) {
//
//                        if(!Utils.isNetworkAvailable(context))
//                            ToastUtils.showToast(context,context.getResources().getString(R.string.no_internet_connection));
//                        else
//                            ToastUtils.showToast(context,context.getResources().getString(R.string.oops));
//
//                    }
//                });
            }
        });
        view_more.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

//                String data = new Gson().toJson(defaultWidgetAdapter.getData());
//                Intent intent = new Intent(getContext(), FullViewActivity.class);
//                intent.putExtra(BundleConstants.TEMPLATE_TYPE, BotResponse.TEMPLATE_TYPE_CHART_LIST);
//                intent.putExtra(BundleConstants.TITLE_NAME,mWidget.getTitle());
//                intent.putExtra(BundleConstants.FROM_WIDGET, true);
//                intent.putExtra(BundleConstants.DATA, data);
//                getContext().startActivity(intent);
            }
        });
        menu_meeting_btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
//                showPopUpMenu();
            }
        });
        upcoming_meeting_root_recycler = view.findViewById(R.id.upcoming_meeting_root_recycler);
        upcoming_meeting_root_recycler.setLayoutManager(new GridLayoutManager(getContext(),4));
        /*ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(getContext(), R.dimen.min_width_middle);
        upcoming_meeting_root_recycler.addItemDecoration(itemDecoration);*/

        meeting_header = view.findViewById(R.id.meeting_header);
        meeting_progress = view.findViewById(R.id.meeting_progress);
        dp1 = (int) AppControl.getInstance().getDimensionUtil().dp1;
        defaultWidgetAdapter = new ChartListWidgetAdapter(getContext(), BotResponse.TEMPLATE_TYPE_CAL_EVENTS_WIDGET, true, false);
        defaultWidgetAdapter.setVerticalListViewActionHelper(this);
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

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        final int count = getChildCount();
        int parentWidth = getMeasuredWidth();

        //get the available size of child view
        int childLeft = 0;
        int childTop = 0;

        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() != GONE) {
                LayoutUtils.layoutChild(child, childLeft, childTop);
                childTop += child.getMeasuredHeight();
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int parentWidth = MeasureSpec.getSize(widthMeasureSpec);
        int wrapSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);

        int totalHeight = getPaddingTop();
        int childWidthSpec;

        childWidthSpec = MeasureSpec.makeMeasureSpec((int) (parentWidth - (2 * dp1)), MeasureSpec.EXACTLY);
        MeasureUtils.measure(rootView, childWidthSpec, wrapSpec);

        totalHeight += rootView.getMeasuredHeight() + getPaddingBottom() + getPaddingTop();

        int parentHeightSpec = MeasureSpec.makeMeasureSpec(totalHeight, MeasureSpec.EXACTLY);
        int parentWidthSpec = MeasureSpec.makeMeasureSpec(rootView.getMeasuredWidth(), MeasureSpec.AT_MOST);
        setMeasuredDimension(parentWidthSpec, parentHeightSpec);
    }

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
    public void navigationToDialAndJoin(String actionType, String actionLink) {

        if (actionType.equalsIgnoreCase("dial")) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("type", "dial");
            map.put("dial", actionLink);
            KaUtility.handleCustomActions(map, getContext());
        } else if (actionType.equalsIgnoreCase("url")) {
            HashMap<String, Object> map2 = new HashMap<>();
            map2.put("type", "url");
            map2.put("url", actionLink);
            KaUtility.handleCustomActions(map2, getContext());
        } else if (actionType.equalsIgnoreCase("meetingUrl")) {
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

    private void loadData(boolean shouldRefresh) {

        /*if(PanelDataLRUCache.getInstance().getEntry(name) !=null && !shouldRefresh){
            afterDataLoad((Widget) PanelDataLRUCache.getInstance().getEntry(name));
            return;
        }*/
//        pin_view.setText(mWidget.isPinned() ? getResources().getString(R.string.icon_31) : getResources().getString(R.string.icon_32));
        panel_name_view.setText(KaUtility.getPanelFormatedName(mWidget.getName()));
        panel_name_view.setVisibility(KaUtility.isTitleAndPanelNameMatch(mWidget.getName(),name));

        meeting_progress.setVisibility(View.VISIBLE);

        Object params = "{\"from\":\"sri.harsha@kore.com\",\"inputs\":{}}";
        Map<String,Object> result = getMapObject(params);
//        WidgetDataLoader.loadForDefaultServiceData(mWidget.getCallbackURL(), Utils.ah(authData.getAccessToken()), result)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Observer<Widget>() {
//                    @Override
//                    public void onSubscribe(Disposable d) {
//                    }
//
//                    @Override
//                    public void onNext(Widget model) {
////                        PanelDataLRUCache.getInstance().putEntry(name,model);
//                        afterDataLoad(model);
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        meeting_progress.setVisibility(View.GONE);
//                        if (e.getMessage().equalsIgnoreCase("410") || e.getMessage().equalsIgnoreCase("401"))
//                        {
//                            //KoreEventCenter.post(new OnTokenExpired());
//                        }
//
//                        String msg;
//                        Drawable drawable=null;
//                        if (!NetworkUtility.isNetworkConnectionAvailable(ChartListWidgetView.this.getContext())) {
//                            //No Internet Connect
//                            msg=getResources().getString(R.string.no_internet_connection);
//                            drawable=getResources().getDrawable(R.drawable.no_internet);
//                        } else {
//                            //Oops some thing went wrong
//                            msg=getResources().getString(R.string.oops);
//                            drawable=getResources().getDrawable(R.drawable.oops_icon);
//                        }
//                        defaultWidgetAdapter.setMessage(msg,drawable);
//                        view_more.setVisibility(GONE);
//                        upcoming_meeting_root_recycler.setAdapter(defaultWidgetAdapter);
//                        defaultWidgetAdapter.notifyDataSetChanged();
//                    }
//
//                    @Override
//                    public void onComplete() {
//                        meeting_progress.setVisibility(View.GONE);
//                    }
//                });
    }

    private void afterDataLoad(Widget model){

        meeting_header.setText(mWidget.getTitle());
        if(model.getElements() == null || model.getElements().size() == 0){
//            calendarEventsAdapter.setMessage(model.getPlaceholder(),null);
        }
        //  upcoming_meeting_root_recycler.tv_upcoming_meeting.setText(model.getSummary());
        //  CalendarEventsAdapter calendarEventsAdapter = null;

        if (model != null && model.getElements() != null && model.getElements().size() > 0) {

            if (model.getElements() != null && model.getElements().size() > 3&& Utility.isViewMoreVisible(widgetViewMoreEnum)) {
                view_more.setVisibility(View.VISIBLE);
            }

            defaultWidgetAdapter.setCalData(new ArrayList<>(model.getElements()));
            defaultWidgetAdapter.setPreviewLength(3);
            defaultWidgetAdapter.setMultiActions(model.getMultiActions());
//            calendarEventsAdapter.setPreviewLength(model.getPreview_length());
            upcoming_meeting_root_recycler.setAdapter(defaultWidgetAdapter);
            defaultWidgetAdapter.notifyDataSetChanged();
        }else if(model.getTemplateType().equals("loginURL")){
            upcoming_meeting_root_recycler.setLayoutManager(new LinearLayoutManager(getContext()));
            defaultWidgetAdapter.setCalData(null);
            defaultWidgetAdapter.setLoginModel(model.getLogin());
            upcoming_meeting_root_recycler.setAdapter(defaultWidgetAdapter);
            defaultWidgetAdapter.setLoginNeeded(true);
        }

        else {
            defaultWidgetAdapter.setCalData(null);
            upcoming_meeting_root_recycler.setAdapter(defaultWidgetAdapter);
            defaultWidgetAdapter.notifyDataSetChanged();
        }
        //KoreEventCenter.post(new ShowLayoutEvent(0));
    }
}