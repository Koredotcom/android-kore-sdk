package com.kore.ai.widgetsdk.views.widgetviews;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.kora.ai.widgetsdk.R;
import com.kore.ai.widgetsdk.cache.PanelDataLRUCache;
import com.kore.ai.widgetsdk.events.KoreEventCenter;
import com.kore.ai.widgetsdk.events.NewMeetingEvent;
import com.kore.ai.widgetsdk.events.ShowLayoutEvent;
import com.kore.ai.widgetsdk.interfaces.PinUnPinnCallBack;
import com.kore.ai.widgetsdk.listeners.UpdateRefreshItem;
import com.kore.ai.widgetsdk.listeners.VerticalListViewActionHelper;
import com.kore.ai.widgetsdk.managers.UserDataManager;
import com.kore.ai.widgetsdk.models.BaseCalenderTemplateModel;
import com.kore.ai.widgetsdk.models.BotCaourselButtonModel;
import com.kore.ai.widgetsdk.models.BotResponse;
import com.kore.ai.widgetsdk.models.ContactViewListModel;
import com.kore.ai.widgetsdk.models.KnowledgeCollectionModel;
import com.kore.ai.widgetsdk.models.WUpcomingMeetingModel;
import com.kore.ai.widgetsdk.models.WelcomeChatSummaryModel;
import com.kore.ai.widgetsdk.models.WidgetsModel;
import com.kore.ai.widgetsdk.models.searchskill.PanelLevelData;
import com.kore.ai.widgetsdk.net.KaRestAPIHelper;
import com.kore.ai.widgetsdk.network.NetworkEvents;
import com.kore.ai.widgetsdk.room.models.AuthData;
import com.kore.ai.widgetsdk.room.models.UserData;
import com.kore.ai.widgetsdk.utils.KaUtility;
import com.kore.ai.widgetsdk.utils.NetworkUtility;
import com.kore.ai.widgetsdk.utils.ToastUtils;
import com.kore.ai.widgetsdk.utils.Utility;
import com.kore.ai.widgetsdk.utils.Utils;
import com.kore.ai.widgetsdk.utils.WidgetConstants;
import com.kore.ai.widgetsdk.utils.WidgetDataLoader;
import com.kore.ai.widgetsdk.utils.WidgetViewMoreEnum;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.kore.ai.widgetsdk.utils.AppUtils.getMapObject;

public class MeetingWidgetView extends LinearLayout implements VerticalListViewActionHelper {
//    private float dp1;
//    private UserData userData;
    private AuthData authData;
    private UserData userData;
    public ImageView menu_meeting_btn;
    public RecyclerView upcoming_meeting_root_recycler;
    public TextView pin_view,view_more, meeting_header;
    public ProgressBar meeting_progress;
//    private WCalEventsAdapter calendarEventsAdapter = null;
    private boolean isFirstTime = true;
    private PanelLevelData panelData;
//    private UpdateRefreshItem mListener;

    public WidgetsModel getWidget() {
        return mWidget;
    }

    public void setWidget(WidgetsModel mWidget, PanelLevelData panelData) {
        this.mWidget = mWidget;
        this.panelData = panelData;
        meeting_header.setText(mWidget.getTitle());
        loadMeetingData(false);
//        pin_view.setText(mWidget.isPinned() ? getContext().getResources().getString(R.string.icon_31) :getContext().getResources().getString(R.string.icon_32));
    }

    private WidgetsModel mWidget;
    private String name;
    Timer timer;
    TimerTask timerTask;
    final Handler handler = new Handler();
    WidgetViewMoreEnum widgetViewMoreEnum;
//    boolean isReorderRequireed;
    /*public MeetingWidgetView(Context context, Widget mWidget, UpdateRefreshItem mListener,boolean isReorderRequireed) {
        super(context);
        this.mListener = mListener;
        this.mWidget = mWidget;
        init();
        this.isReorderRequireed=isReorderRequireed;
    }*/

    public MeetingWidgetView(Context context, UpdateRefreshItem mListener, String name, WidgetViewMoreEnum widgetViewMoreEnum) {
        super(context);
//        this.mListener = mListener;
        this.name = name;
        this.widgetViewMoreEnum=widgetViewMoreEnum;
        init();

    }

    public MeetingWidgetView(Context context, AttributeSet attrs, int defStyleAttr, WidgetsModel mWidget, UpdateRefreshItem mListener) {
        super(context, attrs, defStyleAttr);
//        this.mListener = mListener;
        this.mWidget = mWidget;

        init();
    }


    public void startTimer() {
        //set a new Timer
        if(isFirstTime)return;
        timer = new Timer();

        //initialize the TimerTask's job
        initializeTimerTask();

        if(mWidget.getAutoRefresh() != null)
            timer.schedule(timerTask, WidgetConstants.PANEL_INIT_REF_TIME, Long.parseLong(mWidget.getAutoRefresh().getInterval()+"")*60*1000); //
    }

    public void stopTimerTask() {
        //stop the timer, if it's not already null
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    public void initializeTimerTask() {

        timerTask = new TimerTask() {
            public void run() {

                //use a handler to run a toast that shows the current timestamp
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if(mWidget != null)
                            loadMeetingData(true);
                    }
                });
            }
        };
    }

    private void init() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.card_upcoming_meeting_layout, this, true);
//        rootView = view.findViewById(R.id.meeting_root_view);
        menu_meeting_btn = view.findViewById(R.id.menu_meeting_btn);
        pin_view = view.findViewById(R.id.pin_view);
        menu_meeting_btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
//                showPopUpMenu();
            }
        });
        upcoming_meeting_root_recycler = view.findViewById(R.id.upcoming_meeting_root_recycler);
        upcoming_meeting_root_recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        view_more = view.findViewById(R.id.view_more);

        view_more.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

//                String data = new Gson().toJson(calendarEventsAdapter.getData());
//                //openFullView(BotResponse.TEMPLATE_TYPE_CAL_EVENTS, data);
//                Intent intent = new Intent(getContext(), FullViewActivity.class);
//                intent.putExtra(BundleConstants.TEMPLATE_TYPE, BotResponse.TEMPLATE_TYPE_CAL_EVENTS);
//                intent.putExtra(BundleConstants.FROM_WIDGET, true);
//                intent.putExtra(BundleConstants.DATA_CALENDER_MULTICATION, new Gson().toJson(calendarEventsAdapter.getMultiActions()));
//                intent.putExtra(BundleConstants.DATA, data);
//                intent.putExtra(BundleConstants.DURATION, calendarEventsAdapter.get_cursor());
//                getContext().startActivity(intent);
            }
        });
        meeting_header = view.findViewById(R.id.meeting_header);
        meeting_progress = view.findViewById(R.id.meeting_progress);
        getUserData();
//        dp1 = (int) AppControl.getInstance().getDimensionUtil().dp1;
//        calendarEventsAdapter = new WCalEventsAdapter(getContext(), BotResponse.TEMPLATE_TYPE_CAL_EVENTS_WIDGET, true, false);
//        calendarEventsAdapter.setFromWidget(true);
//        calendarEventsAdapter.setViewMoreEnum(widgetViewMoreEnum);
//        calendarEventsAdapter.setVerticalListViewActionHelper(this);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Utility.setRecyclerViewTempForOnboard(upcoming_meeting_root_recycler,mWidget.get_id());
            }
        },500);
//        loadMeetingData(false);
//        pin_view.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                KaRestAPIHelper.actionPinnAndUnPinn(authData.accessToken, userData.getId(),mWidget,panelData, new PinUnPinnCallBack() {
//                    @Override
//                    public void success() {
////                        mWidget.setPinned(!mWidget.isPinned());
////                        KaUtility.setPinnedViewState(getContext(), mWidget.isPinned(), pin_view,panelData.get_id());
//                    }
//
//                    @Override
//                    public void failure(Throwable t) {
//                        if(!Utils.isNetworkAvailable(getContext()))
//                            ToastUtils.showToast(getContext(),getContext().getResources().getString(R.string.no_internet_connection));
//                        else
//                            ToastUtils.showToast(getContext(),getContext().getResources().getString(R.string.oops));
//                    }
//                });
//
//
//            }
//        });
    }

    private void getUserData() {
        userData = UserDataManager.getUserData();
        authData = UserDataManager.getAuthData();
    }

    /*public void showPopUpMenu() {
        PopupMenu popup = new PopupMenu(getContext(), menu_meeting_btn);
        //Inflating the Popup using xml file
        popup.getMenuInflater().inflate(R.menu.reorder_menu, popup.getMenu());
       // MenuItem reorderItem = popup.getMenu().findItem(R.id.reorder_item);
       // reorderItem.setVisible(isReorderRequireed);
        //registering popup with OnMenuItemClickListener
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
               *//* if (item.getTitle().equals("Reorder")) {
                    if (mListener != null)
                        mListener.onWidgetMenuButtonClicked();
                } else*//* if (item.getTitle().equals("Refresh")) {
                    loadMeetingData(true);
                }

                return true;
            }
        });

        popup.show();//showing popup menu
    }*/

    private void loadMeetingData(boolean shouldRefresh) {

            if(PanelDataLRUCache.getInstance().getEntry(name) !=null && !shouldRefresh && PanelDataLRUCache.getInstance().getEntry(name) instanceof WUpcomingMeetingModel){
                afterDataLoad((WUpcomingMeetingModel) PanelDataLRUCache.getInstance().getEntry(name));
                isFirstTime = false;
                return;
            }
            meeting_progress.setVisibility(View.VISIBLE);
//            Map<String,Object> result = getMapObject(mWidget.getHook().getParams());
//            WidgetDataLoader.loadUpcomingMeetingData(mWidget.getCallbackURL(), Utils.ah(authData.getAccessToken()))
//                    .subscribeOn(Schedulers.io())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(new Observer<WUpcomingMeetingModel>() {
//                        @Override
//                        public void onSubscribe(Disposable d) {
//                        }
//
//                        @Override
//                        public void onNext(WUpcomingMeetingModel model) {
//                            PanelDataLRUCache.getInstance().putEntry(name,model);
//                            afterDataLoad(model);
//                        }
//
//                        @Override
//                        public void onError(Throwable e) {
//                            meeting_progress.setVisibility(View.GONE);
//                            if (e.getMessage().equalsIgnoreCase("410") || e.getMessage().equalsIgnoreCase("401"))
//                            {
//                                KoreEventCenter.post(new OnTokenExpired());
//                            }
//
//                            String msg;
//                            Drawable drawable=null;
//                            if (!NetworkUtility.isNetworkConnectionAvailable(MeetingWidgetView.this.getContext())) {
//                                //No Internet Connect
//                                msg=getResources().getString(R.string.no_internet_connection);
//                                drawable=getResources().getDrawable(R.drawable.no_internet);
//                            } else {
//                                //Oops some thing went wrong
//                                msg=getResources().getString(R.string.oops);
//                                drawable=getResources().getDrawable(R.drawable.oops_icon);
//                            }
//                            calendarEventsAdapter.setCalData(null);
//                            calendarEventsAdapter.setMessage(msg,drawable);
//                            view_more.setVisibility(GONE);
//                            upcoming_meeting_root_recycler.setAdapter(calendarEventsAdapter);
//                            calendarEventsAdapter.notifyDataSetChanged();
//                        }
//
//                        @Override
//                        public void onComplete() {
//                            meeting_progress.setVisibility(View.GONE);
//                        }
//                    });
        }



    private void afterDataLoad(WUpcomingMeetingModel model){
      /*  if (mListener != null)
            mListener.updateWeatherWidgetSummery(WidgetConstants.MEETINGS_TEMPLATE, model.getSummary().getText());
*/
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

//            calendarEventsAdapter.set_cursor(model.getCursor());
//            calendarEventsAdapter.setCalData(new ArrayList<>(model.getElements()));
//            calendarEventsAdapter.setMultiActions(model.getMultiActions());
//         //   calendarEventsAdapter.setPreviewLength(model.getPreview_length());
//            upcoming_meeting_root_recycler.setAdapter(calendarEventsAdapter);
//            calendarEventsAdapter.notifyDataSetChanged();
        } else {
//            calendarEventsAdapter.setData(null);
//            upcoming_meeting_root_recycler.setAdapter(calendarEventsAdapter);
//            calendarEventsAdapter.notifyDataSetChanged();
        }
        KoreEventCenter.post(new ShowLayoutEvent(0));
    }

    public void onEventMainThread(NewMeetingEvent event) {
        loadMeetingData(true);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        KoreEventCenter.register(this);
        startTimer();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopTimerTask();
        KoreEventCenter.unregister(this);
    }

    /*@Override
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
    }*/

   /* @Override
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
    }*/

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
//            Intent intent = new Intent(getContext(), GenericWebViewActivity.class);
//            intent.putExtra("url", actionLink);
//            intent.putExtra("header", getContext().getResources().getString(kore.botssdk.R.string.app_name));
//            getContext().startActivity(intent);
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
            view_more.setVisibility(visible&&Utility.isViewMoreVisible(widgetViewMoreEnum) ?View.VISIBLE: View.GONE);
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
            loadMeetingData(true);
        }
    }
}