package com.kore.ai.widgetsdk.views.widgetviews;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kora.ai.widgetsdk.R;
import com.kore.ai.widgetsdk.application.AppControl;
import com.kore.ai.widgetsdk.events.KoreEventCenter;
import com.kore.ai.widgetsdk.interfaces.PinUnPinnCallBack;
import com.kore.ai.widgetsdk.managers.UserDataManager;
import com.kore.ai.widgetsdk.models.Widget;
import com.kore.ai.widgetsdk.models.searchskill.PanelLevelData;
import com.kore.ai.widgetsdk.net.KaRestAPIHelper;
import com.kore.ai.widgetsdk.network.NetworkEvents;
import com.kore.ai.widgetsdk.room.models.AuthData;
import com.kore.ai.widgetsdk.room.models.UserData;
import com.kore.ai.widgetsdk.utils.KaUtility;
import com.kore.ai.widgetsdk.utils.ToastUtils;
import com.kore.ai.widgetsdk.utils.Utils;
import com.kore.ai.widgetsdk.utils.WidgetViewMoreEnum;
import com.kore.ai.widgetsdk.views.viewutils.LayoutUtils;
import com.kore.ai.widgetsdk.views.viewutils.MeasureUtils;
import com.kore.ai.widgetsdk.widgets.SkillWidgetAdapter;

public class SkillWidgetView extends ViewGroup {
    private RecyclerView recycler_root;
    private Widget mWidget;
    private float dp1;
    private String name;
    private TextView header;
    private RelativeLayout rootView;
    WidgetViewMoreEnum widgetViewMoreEnum;
    private TextView pin_view,panel_name_view;
    Context context;
    private UserData userData;
    private AuthData authData;
    public SkillWidgetView(Context context, String name, WidgetViewMoreEnum widgetViewMoreEnum) {
        super(context);
        this.context=context;
        this.name = name;
        this.widgetViewMoreEnum=widgetViewMoreEnum;
        init();
    }

    public Widget getWidget() {
        return mWidget;
    }

    PanelLevelData panelData;
    public void setWidget(Widget mWidget,PanelLevelData panelData) {
        this.mWidget = mWidget;
        this.panelData=panelData;
        loadData();
    }

    public void onEvent(NetworkEvents.NetworkConnectivityEvent event) {
        if(event!=null&&event.isNetworkConnectivity()) {
            loadData();
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
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        KoreEventCenter.register(this);

    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        KoreEventCenter.unregister(this);
    }

    private void init() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.skill_widget_layout, this, true);
        dp1 = (int) AppControl.getInstance().getDimensionUtil().dp1;
        pin_view = view.findViewById(R.id.pin_view);
        panel_name_view.findViewById(R.id.panel_name_view);
        userData = UserDataManager.getUserData();
        authData = UserDataManager.getAuthData();
       // KoreEventCenter.register(this);
        recycler_root = view.findViewById(R.id.recycler_root);
        rootView=findViewById(R.id.rootView);
        recycler_root.setLayoutManager(new LinearLayoutManager(getContext()));
        header=view.findViewById(R.id.header);

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
//                        if(!Utils.isNetworkAvailable(context))
//                            ToastUtils.showToast(context,context.getResources().getString(R.string.no_internet_connection));
//                        else
//                            ToastUtils.showToast(context,context.getResources().getString(R.string.oops));
//                    }
//                });


            }
        });

    }

    private void loadData(){
        header.setText(mWidget.getTitle());
        pin_view.setText(mWidget.isPinned() ? context.getResources().getString(R.string.icon_31) :context.getResources().getString(R.string.icon_32));
        panel_name_view.setText(KaUtility.getPanelFormatedName(mWidget.getPname()));
        panel_name_view.setVisibility(KaUtility.isTitleAndPanelNameMatch(mWidget.getPname(),name));

        if(mWidget!=null&&mWidget.getActions()!=null) {
            SkillWidgetAdapter adapter = new SkillWidgetAdapter(getContext(), mWidget.getActions());
            adapter.setSkillName(name);
            recycler_root.setAdapter(adapter);
        }
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
}