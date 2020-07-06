package com.kore.ai.widgetsdk.views.widgetviews;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.kora.ai.widgetsdk.R;
import com.kore.ai.widgetsdk.interfaces.PinUnPinnCallBack;
import com.kore.ai.widgetsdk.managers.UserDataManager;
import com.kore.ai.widgetsdk.models.BaseCalenderTemplateModel;
import com.kore.ai.widgetsdk.models.BotCaourselButtonModel;
import com.kore.ai.widgetsdk.models.BotLineChartDataModel;
import com.kore.ai.widgetsdk.models.ContactViewListModel;
import com.kore.ai.widgetsdk.models.KnowledgeCollectionModel;
import com.kore.ai.widgetsdk.models.WelcomeChatSummaryModel;
import com.kore.ai.widgetsdk.models.WidgetsDataModel;
import com.kore.ai.widgetsdk.models.WidgetsModel;
import com.kore.ai.widgetsdk.models.searchskill.PanelLevelData;
import com.kore.ai.widgetsdk.net.KaRestAPIHelper;
import com.kore.ai.widgetsdk.room.models.AuthData;
import com.kore.ai.widgetsdk.room.models.UserData;
import com.kore.ai.widgetsdk.utils.KaUtility;
import com.kore.ai.widgetsdk.utils.NetworkUtility;
import com.kore.ai.widgetsdk.utils.Utils;
import com.kore.ai.widgetsdk.utils.WidgetDataLoader;
import com.kore.ai.widgetsdk.view.CustomMarkerView;
import com.kore.ai.widgetsdk.views.viewutils.LayoutUtils;
import com.kore.ai.widgetsdk.views.viewutils.MeasureUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.kore.ai.widgetsdk.utils.AppUtils.getMapObject;

public class LineChartWidgetView extends BaseWidgetView/* implements OnChartGestureListener*/ {

    private WidgetsModel mWidget;
    private AuthData authData;
    private UserData userData;
    private LineChart mChart;
    public ProgressBar progress;
    private View rootView;
    private TextView chartHeader;
    private Context mContext;
    private String jwtToken;

    private TextView pin_view,panel_name_view;
    public LineChartWidgetView(Context context) {
        super(context);
        mContext = context;
        init();
    }

    private void init(){
        View view = LayoutInflater.from(getContext()).inflate(R.layout.linechart_widgetview, this, true);
        rootView = view.findViewById(R.id.chart_root_view);
        chartHeader = view.findViewById(R.id.chart_header);
        progress = view.findViewById(R.id.chart_progress);
        mChart = view.findViewById(R.id.mChart);
        pin_view = view.findViewById(R.id.pin_view);
        panel_name_view=view.findViewById(R.id.panel_name_view);
        getUserData();
        mChart.getDescription().setEnabled(false);
        mChart.setTouchEnabled(false);
        CustomMarkerView mv = new CustomMarkerView(mContext, R.layout.marker_content);

// set the marker to the chart
        mChart.setMarkerView(mv);
//        mChart.setOnChartGestureListener(this);

        pin_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                KaRestAPIHelper.actionPinnAndUnPinn(authData.accessToken, userData.getId(),mWidget,panelData, new PinUnPinnCallBack() {
//                    @Override
//                    public void success() {
////                        mWidget.setPinned(!mWidget.isPinned());
////                        KaUtility.setPinnedViewState(mContext, mWidget.isPinned(), pin_view,panelData.get_id());
//
//                    }
//
//                    @Override
//                    public void failure(Throwable t) {
//
//                    }
//                });


            }
        });
    }

    private void getUserData() {
        authData = UserDataManager.getAuthData();
        userData = UserDataManager.getUserData();
    }
    PanelLevelData panelData;
    String panelName;
    public void setWidget(String panelName, WidgetsModel mWidget, PanelLevelData panelData, String jwtToken) {
        this.mWidget = mWidget;
        this.panelName=panelName;
        this.panelData=panelData;
        this.jwtToken=jwtToken;
        chartHeader.setText(mWidget.getTitle());
//        pin_view.setText(mWidget.isPinned() ? mContext.getResources().getString(R.string.icon_31) :mContext.getResources().getString(R.string.icon_32));
        panel_name_view.setText(KaUtility.getPanelFormatedName(mWidget.getName()));
        panel_name_view.setVisibility(KaUtility.isTitleAndPanelNameMatch(mWidget.getName(),panelName));

        loadData();
    }

    private void loadData() {

        /*if(PanelDataLRUCache.getInstance().getEntry(name) !=null && !shouldRefresh){
            afterDataLoad((Widget) PanelDataLRUCache.getInstance().getEntry(name));
            return;
        }*/
        if(mWidget.getCallbackURL()==null) {
            return;
        }
        progress.setVisibility(View.VISIBLE);
        Map<String,Object> result = getMapObject(mWidget.getParam());
        WidgetDataLoader.loadChartData(mWidget.getCallbackURL(), Utils.ah(jwtToken), result)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<WidgetsDataModel>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(WidgetsDataModel model) {
//                        afterDataLoad(model);
                        if(model!=null && model.getData() != null && model.getData().size() > 0 && model.getData().get(0).getElements()!=null&&model.getData().get(0).getElements().size()>0){
                            model.getData().get(0).convertElementsToModel();
                            setData(model.getData().get(0).getLineChartElements());
                        }
                        Log.d("IKIDO","Hi");
                    }

                    @Override
                    public void onError(Throwable e) {
                        progress.setVisibility(View.GONE);

                        String msg;
                        Drawable drawable=null;
                        if (!NetworkUtility.isNetworkConnectionAvailable(LineChartWidgetView.this.getContext())) {
                            //No Internet Connect
                            msg=getResources().getString(R.string.no_internet_connection);
                            drawable=getResources().getDrawable(R.drawable.no_internet);
                        } else {
                            //Oops some thing went wrong
                            msg=getResources().getString(R.string.oops);
                            drawable=getResources().getDrawable(R.drawable.oops_icon);
                        }
//                        defaultWidgetAdapter.setWidgetData(null);
//                        defaultWidgetAdapter.setMessage(msg,drawable);
                    }

                    @Override
                    public void onComplete() {
                        progress.setVisibility(View.GONE);
                    }
                });
    }

    public void setData(ArrayList<BotLineChartDataModel> lineList) {
        LineDataSet dataSet[];
        Description desc = new Description();
//        desc.setText("Hi This is challa");
        mChart.setDescription(desc);

        ArrayList<ILineDataSet> sets = new ArrayList<ILineDataSet>(lineList.size());
        dataSet = new LineDataSet[lineList.size()];

        for(int baseIndex=0; baseIndex < lineList.size(); baseIndex++){
            BotLineChartDataModel model = lineList.get(baseIndex);

            ArrayList<Entry> entry = new ArrayList<Entry>();
            for (int index = 0; index < model.getValues().size(); index++) {
                entry.add(new Entry(index,Math.round(model.getValues().get(index))));
            }
            dataSet[baseIndex] = new LineDataSet(entry, model.getTitle());
            dataSet[baseIndex].setLineWidth(2.5f);
            dataSet[baseIndex].setCircleRadius(4.5f);
            dataSet[baseIndex].setDrawValues(false);
            dataSet[baseIndex].setColor(ColorTemplate.MATERIAL_COLORS[baseIndex % 4]);
            dataSet[baseIndex].setCircleColor(ColorTemplate.getHoloBlue());
            dataSet[baseIndex].setLineWidth(1f);
            dataSet[baseIndex].setCircleRadius(3f);
            dataSet[baseIndex].setDrawCircleHole(false);
            dataSet[baseIndex].setValueTextSize(9f);
            dataSet[baseIndex].setDrawFilled(false);
            dataSet[baseIndex].setFormLineWidth(1f);
            dataSet[baseIndex].setFormSize(15.f);
            sets.add(dataSet[baseIndex]);
        }

        LineData lineData = new LineData(sets);

        // set data
        mChart.setData(lineData);
        mChart.getXAxis().setTextSize(8);
        mChart.getXAxis().setDrawAxisLine(true);
//        mChart.getXAxis().setLabelsToSkip(0);
        mChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        mChart.setDrawGridBackground(false);
        mChart.getXAxis().setDrawGridLines(false); // disable grid lines for the XAxis
        mChart.getAxisLeft().setDrawGridLines(false); // disable grid lines for the left YAxis
        mChart.getAxisRight().setDrawGridLines(false);
        mChart.getAxisRight().setEnabled(false);// disable grid lines for the right YAxis
        // get the legend (only possible after setting data)
        // get the legend (only possible after setting data)
        Legend l = mChart.getLegend();

        // modify the legend ...
        l.setForm(Legend.LegendForm.LINE);
//        l.setTypeface(mTfLight);
        l.setTextSize(11f);
        l.setTextColor(Color.WHITE);
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);

        mChart.invalidate();

    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
//        KoreEventCenter.register(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
//        KoreEventCenter.unregister(this);
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

    }

    @Override
    public void tasksSelectedOrDeselected(boolean selecetd) {

    }

    @Override
    public void widgetItemSelected(boolean isSelected, int count) {

    }

    @Override
    public void navigationToDialAndJoin(String actiontype, String actionLink) {

    }

    @Override
    public void takeNotesNavigation(BaseCalenderTemplateModel baseCalenderTemplateModel) {

    }

    @Override
    public void meetingNotesNavigation(Context context, String mId, String eId) {

    }

    @Override
    public void meetingWidgetViewMoreVisibility(boolean visible) {

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
        int parentWidth = View.MeasureSpec.getSize(widthMeasureSpec);
        int wrapSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);

        int totalHeight = getPaddingTop();
        int childWidthSpec;

        childWidthSpec = View.MeasureSpec.makeMeasureSpec((int) (parentWidth - (2 * dp1)), View.MeasureSpec.EXACTLY);
        MeasureUtils.measure(rootView, childWidthSpec, wrapSpec);

        totalHeight += rootView.getMeasuredHeight() + getPaddingBottom() + getPaddingTop();

        int parentHeightSpec = View.MeasureSpec.makeMeasureSpec(totalHeight, View.MeasureSpec.EXACTLY);
        int parentWidthSpec = View.MeasureSpec.makeMeasureSpec(rootView.getMeasuredWidth(), View.MeasureSpec.AT_MOST);
        setMeasuredDimension(parentWidthSpec, parentHeightSpec);
    }


    /*@Override
    public void onChartGestureStart(MotionEvent motionEvent, ChartTouchListener.ChartGesture chartGesture) {

    }

    @Override
    public void onChartGestureEnd(MotionEvent motionEvent, ChartTouchListener.ChartGesture chartGesture) {
        // un-highlight values after the gesture is finished and no single-tap
//        if(chartGesture != ChartTouchListener.ChartGesture.SINGLE_TAP)
//            mChart.highlightValues(null);
    }

    @Override
    public void onChartLongPressed(MotionEvent motionEvent) {

    }

    @Override
    public void onChartDoubleTapped(MotionEvent motionEvent) {

    }

    @Override
    public void onChartSingleTapped(MotionEvent motionEvent) {

    }

    @Override
    public void onChartFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {

    }

    @Override
    public void onChartScale(MotionEvent motionEvent, float v, float v1) {

    }

    @Override
    public void onChartTranslate(MotionEvent motionEvent, float v, float v1) {

    }*/
}
