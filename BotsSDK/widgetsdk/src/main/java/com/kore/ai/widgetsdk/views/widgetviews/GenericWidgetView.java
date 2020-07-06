package com.kore.ai.widgetsdk.views.widgetviews;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kora.ai.widgetsdk.R;
import com.kore.ai.widgetsdk.application.AppControl;
import com.kore.ai.widgetsdk.cache.PanelDataLRUCache;
import com.kore.ai.widgetsdk.events.KaEvents;
import com.kore.ai.widgetsdk.events.KoreEventCenter;
import com.kore.ai.widgetsdk.listeners.VerticalListViewActionHelper;
import com.kore.ai.widgetsdk.managers.UserDataManager;
import com.kore.ai.widgetsdk.models.KnowledgeDetailModelResponseNew;
import com.kore.ai.widgetsdk.models.WidgetsModel;
import com.kore.ai.widgetsdk.models.searchskill.PanelLevelData;
import com.kore.ai.widgetsdk.net.KaRestAPIHelper;
import com.kore.ai.widgetsdk.net.KaRestBuilder;
import com.kore.ai.widgetsdk.network.NetworkEvents;
import com.kore.ai.widgetsdk.room.models.AuthData;
import com.kore.ai.widgetsdk.room.models.UserData;
import com.kore.ai.widgetsdk.utils.KaUtility;
import com.kore.ai.widgetsdk.utils.NetworkUtility;
import com.kore.ai.widgetsdk.utils.Utility;
import com.kore.ai.widgetsdk.utils.Utils;
import com.kore.ai.widgetsdk.utils.WidgetConstants;
import com.kore.ai.widgetsdk.utils.WidgetViewMoreEnum;

import java.util.ArrayList;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.kore.ai.widgetsdk.utils.AppUtils.getMapObject;

/**
 * Created by Ramachandra Pradeep on 23-Apr-19.
 */

public class GenericWidgetView extends LinearLayout {

    private ProgressBar linear_progress;
    private RecyclerView autoExpandListView;
    private TextView view_more, txtTitle;
    private float dp1;
    //    private UserData userData;
    private AuthData authData;
    private UserData userData;
    KnowledgeDetailModelResponseNew responseNew;
    private View rootView;


    private WidgetsModel widget;
    private String name;
    boolean hasMore;
    LinearLayoutManager mLayoutManager;
    Timer timer;
    TimerTask timerTask;
    final Handler handler = new Handler();
    String refreshTime = "180";
    private int mPage;
    private boolean isPaginationRequired;
    WidgetViewMoreEnum widgetViewMoreEnum;
    View header_layout;
    boolean fetching;
    private boolean isFirstTime = true;
    private TextView pin_view,panel_name_view;
    Context context;
    public GenericWidgetView(Context context, int mPage, VerticalListViewActionHelper
            verticalListViewActionHelper, WidgetsModel widget,
                             String name, String refreshTime, boolean isPaginationRequired, WidgetViewMoreEnum widgetViewMoreEnum) {
        super(context);
        this.widget = widget;
        this.name = name;
        this.context=context;
        this.widgetViewMoreEnum=widgetViewMoreEnum;
        this.refreshTime = refreshTime;
        this.isPaginationRequired=isPaginationRequired;
        init(verticalListViewActionHelper, mPage);
    }

    public GenericWidgetView(Context context, AttributeSet attrs, int mPage,
                             VerticalListViewActionHelper verticalListViewActionHelper, String name) {
        super(context, attrs);
        this.name = name;
        init(verticalListViewActionHelper, mPage);
    }

    public GenericWidgetView(Context context, AttributeSet attrs, int defStyleAttr, int mPage,
                             VerticalListViewActionHelper verticalListViewActionHelper, String name) {
        super(context, attrs, defStyleAttr);
        this.name = name;
        init(verticalListViewActionHelper, mPage);
    }

    private void getUserData() {
         userData = UserDataManager.getUserData();
        authData = UserDataManager.getAuthData();
    }

    public WidgetsModel getWidget() {
        return widget;
    }

    PanelLevelData panelData;
    String panelName;
    public void setWidget(String panelName,WidgetsModel widget,boolean isPaginationRequired,PanelLevelData panelData) {
        this.widget = widget;
        this.panelData=panelData;
        this.panelName=panelName;
        this.refreshTime = widget.getAutoRefresh().getInterval()+"";
        header_layout.setVisibility(VISIBLE);
        txtTitle.setVisibility(View.VISIBLE);

        txtTitle.setText(widget.getTitle());
        this.isPaginationRequired=isPaginationRequired;
        loadData(false, false);
//        pin_view.setText(widget.isPinned() ? context.getResources().getString(R.string.icon_31) :context.getResources().getString(R.string.icon_32));
        panel_name_view.setText(KaUtility.getPanelFormatedName(widget.getName()));
        panel_name_view.setVisibility(KaUtility.isTitleAndPanelNameMatch(widget.getName(),panelName));
    }

    public RecyclerView getRecycler() {
        return autoExpandListView;
    }

    private void init(VerticalListViewActionHelper verticalListViewActionHelper, final int mPage) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.generic_widget_view, this, true);
        rootView = view.findViewById(R.id.item_view_root);
        pin_view = view.findViewById(R.id.pin_view);
        panel_name_view=view.findViewById(R.id.panel_name_view);
        header_layout=view.findViewById(R.id.header_layout);
        txtTitle = view.findViewById(R.id.widget_header);
        autoExpandListView = view.findViewById(R.id.item_list);
        mLayoutManager = new LinearLayoutManager(getContext());
        autoExpandListView.setLayoutManager(mLayoutManager);
        linear_progress = view.findViewById(R.id.linear_progress);
        this.mPage = mPage;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Utility.setRecyclerViewTempForOnboard(autoExpandListView, widget.get_id() /*+ widget.getFilters().get(mPage).getId()*/);
            }
        }, 500);


        pin_view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

//                KaRestAPIHelper.actionPinnAndUnPinn(authData.accessToken, userData.getId(),widget,panelData, new PinUnPinnCallBack() {
//                    @Override
//                    public void success() {
//                        widget.setPinned(!widget.isPinned());
//                        KaUtility.setPinnedViewState(context, widget.isPinned(), pin_view,panelData.get_id());
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
//                Gson gson = new Gson();
//                Intent intent = new Intent(getContext(), KaKnowledgeRightWidgetActivityFull.class);
//                Bundle args = new Bundle();
//                args.putString("widget", gson.toJson(widget));
//                args.putInt("position", mPage);
//                intent.putExtra("BUNDLE", args);
//                getContext().startActivity(intent);

            }


        });
        dp1 = (int) AppControl.getInstance().getDimensionUtil().dp1;
        getUserData();
//        adapter = new KnowledgeRecyclerAdapter(null, getContext());
//        adapter.setExpanded(false);
//        adapter.setViewMoreEnum(widgetViewMoreEnum);
//        adapter.setVerticalListViewActionHelper(verticalListViewActionHelper);

        loadData(false, false);


       /* autoExpandListView.addOnScrollListener(new EndlessRecyclerOnScrollListener(mLayoutManager) {
            @Override
            public void onLoadMore(int current_page, int totoalItemCount) {

                if (hasMore&&isPaginationRequired) {
                    loadData(true, true);
                }
            }
        });*/


        autoExpandListView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int visibleItemCount =mLayoutManager .getChildCount();
                int totalItemCount = mLayoutManager.getItemCount();
                int firstVisibleItemPosition = mLayoutManager.findFirstVisibleItemPosition();

                if (hasMore&&isPaginationRequired&&!fetching&&(visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                        && firstVisibleItemPosition >= 0)
                {

                    loadData(true, true);
                }
            }
        });

    }


    public void startTimer() {
        //set a new Timer
        if(isFirstTime)return;
        timer = new Timer();

        //initialize the TimerTask's job
        initializeTimerTask();
        if (refreshTime == null) refreshTime = "180";
        timer.schedule(timerTask, WidgetConstants.PANEL_INIT_REF_TIME, Long.parseLong(refreshTime) * 1000); //
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
                          loadData(false, true);
                    }
                });
            }
        };
    }

 /*   @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int wrapSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        int widthSpec = MeasureSpec.makeMeasureSpec(((int) AppControl.getInstance().getDimensionUtil().screenWidth - (int) (20 * dp1)), MeasureSpec.EXACTLY);
        int totalHeight = getPaddingTop();

        MeasureUtils.measure(rootView, widthSpec, wrapSpec);
        totalHeight += rootView.getMeasuredHeight() + getPaddingBottom() + getPaddingTop();
        if (rootView.getMeasuredHeight() != 0) {
            totalHeight += 5 * dp1;
        }
        int parentHeightSpec = MeasureSpec.makeMeasureSpec(totalHeight, MeasureSpec.EXACTLY);
        int parentWidthSpec = MeasureSpec.makeMeasureSpec(rootView.getMeasuredWidth(), MeasureSpec.EXACTLY);
        setMeasuredDimension(parentWidthSpec, parentHeightSpec);
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        final int count = getChildCount();
        int parentWidth = getMeasuredWidth();

        //get the available size of child view
        int childLeft = 0;
        int childTop = (int) (6 * dp1);

        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() != GONE) {
                LayoutUtils.layoutChild(child, childLeft, childTop);
                childTop += child.getMeasuredHeight();
            }
        }
    }*/

    public void onEventMainThread(KaEvents.RefreshKnowledgeList list) {
        try {
            //When user add/deletes or edit knowledge it will be called
            responseNew = null;
            loadData(false, true);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        startTimer();
        KoreEventCenter.register(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopTimerTask();
        KoreEventCenter.unregister(this);
    }


    public void onEventMainThread(KaEvents.InformationSharedEvent knowledgeDetailModel) {
        try {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    loadData(false, true);
                }
            }, 2000);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void onEvent(NetworkEvents.NetworkConnectivityEvent event) {
        if (event != null && event.isNetworkConnectivity()) {
            loadData(false, true);
        }
    }

    public void loadData(final boolean isPagination, boolean shouldRefresh) {
        if (widget == null||fetching) return;

        if (PanelDataLRUCache.getInstance().getEntry(name) != null && !shouldRefresh && !isPagination) {
            afterDataLoad((KnowledgeDetailModelResponseNew) PanelDataLRUCache.getInstance().getEntry(name), false);
            isFirstTime = false;
            return;
        }

        String api = null;
        Map<String, Object> param = null;
        Object body = null;
        // responseNew = null;
        if (isPagination == false || responseNew == null || responseNew.getHook() == null) {
            if (widget.getTemplateType().equals("List")) {
                api = widget.getCallbackURL();
//                body = widget.getHook().getBody();
//                param = getMapObject(widget.getHook().getParams());
            } else {
//                api = widget.getFilters().get(mPage).getHook().getApi();
//                body = widget.getFilters().get(mPage).getHook().getBody();
//                param = getMapObject(widget.getFilters().get(mPage).getHook().getParams());
            }

        } else {
            api = responseNew.getHook().getApi();
            body = responseNew.getHook().getBody();
            param = getMapObject(responseNew.getHook().getParams());
        }

        if (authData == null) return;
        linear_progress.setVisibility(View.VISIBLE);

        fetching=true;
        Call<KnowledgeDetailModelResponseNew> knowledgeReq = KaRestBuilder.getKaRestAPI().getRecentKnowledgePanel(Utils.ah(authData.accessToken),
                api, param, body);
        KaRestAPIHelper.enqueueWithRetry(knowledgeReq, new Callback<KnowledgeDetailModelResponseNew>() {
            @Override
            public void onResponse(Call<KnowledgeDetailModelResponseNew> call, Response<KnowledgeDetailModelResponseNew> response) {
                linear_progress.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    responseNew = response.body();
                    if (responseNew != null) {
                        if (!isPagination)
                            PanelDataLRUCache.getInstance().putEntry(name, responseNew);
                        afterDataLoad(responseNew, isPagination);
                    }
                    fetching=false;
                }

            }

            @Override
            public void onFailure(Call<KnowledgeDetailModelResponseNew> call, Throwable t) {
                linear_progress.setVisibility(View.GONE);
                String msg;
                fetching=false;
                Drawable drawable = null;
                if (!NetworkUtility.isNetworkConnectionAvailable(GenericWidgetView.this.getContext())) {
                    //No Internet Connect
                    msg = getResources().getString(R.string.no_internet_connection);
                    drawable = getResources().getDrawable(R.drawable.no_internet);
                } else {
                    //Oops some thing went wrong
                    msg = getResources().getString(R.string.oops);
                    drawable = getResources().getDrawable(R.drawable.oops_icon);
                }
//                adapter.setData(null);
//                adapter.setMessage(msg, drawable);
//                autoExpandListView.setAdapter(adapter);
//                view_more.setVisibility(GONE);
//                adapter.notifyDataSetChanged();

            }
        });
    }


    private void afterDataLoad(KnowledgeDetailModelResponseNew knowledgeDetailModelResponse, boolean isPagintn) {
        hasMore = knowledgeDetailModelResponse.getHasMore();
//        adapter.setViewMoreEnum(widgetViewMoreEnum);
//        view_more.setVisibility(knowledgeDetailModelResponse.getElements().size() > 3  && Utility.isViewMoreVisible(widgetViewMoreEnum) ? View.VISIBLE : View.GONE);
//
//        if (isPagintn) {
//            ArrayList temp = adapter.getData();
//            if (temp == null) {
//                adapter.setData((ArrayList) knowledgeDetailModelResponse.getElements());
//            } else {
//                int offSet = adapter.getItemCount();
//                temp.addAll(knowledgeDetailModelResponse.getElements());
//                adapter.setData((ArrayList) temp);
//                adapter.notifyItemRangeInserted(offSet, knowledgeDetailModelResponse.getElements().size());
//                return;
//
//            }
//        } else {
//            adapter.setData((ArrayList) knowledgeDetailModelResponse.getElements());
//        }
//        //  adapter.setData((ArrayList)knowledgeDetailModelResponse.getElements());
//
//
//        autoExpandListView.setAdapter(adapter);
//        adapter.notifyDataSetChanged();
        //       view_more.setVisibility(knowledgeDetailModelResponse.getElements().size()>3&&hasMore?View.VISIBLE:View.GONE);

    }

}
