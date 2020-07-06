package com.kore.ai.widgetsdk.views.widgetviews;

import android.content.Context;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.LargeValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.kora.ai.widgetsdk.R;
import com.kore.ai.widgetsdk.formatters.BarChartDataFormatter;
import com.kore.ai.widgetsdk.interfaces.PinUnPinnCallBack;
import com.kore.ai.widgetsdk.managers.UserDataManager;
import com.kore.ai.widgetsdk.models.BaseCalenderTemplateModel;
import com.kore.ai.widgetsdk.models.BotBarChartDataModel;
import com.kore.ai.widgetsdk.models.BotCaourselButtonModel;
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
import com.kore.ai.widgetsdk.utils.ToastUtils;
import com.kore.ai.widgetsdk.utils.Utils;
import com.kore.ai.widgetsdk.utils.WidgetDataLoader;
import com.kore.ai.widgetsdk.view.CustomMarkerView;
import com.kore.ai.widgetsdk.views.viewutils.LayoutUtils;
import com.kore.ai.widgetsdk.views.viewutils.MeasureUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.kore.ai.widgetsdk.utils.AppUtils.getMapObject;

public class BarChartWidgetView extends BaseWidgetView /*implements OnChartValueSelectedListener*/ {

    private WidgetsModel mWidget;
    private AuthData authData;
    private UserData userData;
    private BarChart mChart;
    public ProgressBar progress;
    private View rootView;
    private TextView chartHeader;
    private int labelCount = 0;
    private Context mContext;
    private TextView pin_view,panel_name_view;
    private String jwtToken;

    public BarChartWidgetView(Context context) {
        super(context);
        this.mContext = context;
        init();
    }

    private void init(){
        View view = LayoutInflater.from(getContext()).inflate(R.layout.barchart_widgetview, this, true);
        rootView = view.findViewById(R.id.chart_root_view);
        chartHeader = view.findViewById(R.id.chart_header);
        progress = view.findViewById(R.id.chart_progress);
        pin_view = view.findViewById(R.id.pin_view);
        panel_name_view=view.findViewById(R.id.panel_name_view);
        mChart = view.findViewById(R.id.mChart);
        getUserData();
        labelCount = 0;
        mChart.setTouchEnabled(false);
        mChart.setPinchZoom(false);
        mChart.setDrawBarShadow(false);
        mChart.setDrawGridBackground(false);
        mChart.setDrawValueAboveBar(true);

        mChart.getDescription().setEnabled(false);

        // if more than 60 entries are displayed in the chart, no values will be
        // drawn
        mChart.setMaxVisibleValueCount(60);


        CustomMarkerView mv = new CustomMarkerView(mContext, R.layout.marker_content);

// set the marker to the chart
        mv.setChartView(mChart);
        mChart.setMarker(mv);
//        mChart.setOnChartValueSelectedListener(this);

        Legend l = mChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(true);
        l.setYOffset(0f);
        l.setXOffset(10f);
        l.setYEntrySpace(0f);
        l.setTextSize(8f);

        XAxis xAxis = mChart.getXAxis();
        xAxis.setGranularity(1f);
        xAxis.setCenterAxisLabels(true);
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return String.valueOf((int) value);
            }
        });

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setValueFormatter(new LargeValueFormatter());
        leftAxis.setDrawGridLines(false);
        leftAxis.setSpaceTop(35f);
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        mChart.getAxisRight().setEnabled(false);


        pin_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                KaRestAPIHelper.actionPinnAndUnPinn(authData.accessToken, userData.getId(),mWidget,panelData, new PinUnPinnCallBack() {
//                    @Override
//                    public void success() {
//                        mWidget.setPinned(!mWidget.isPinned());
//                        KaUtility.setPinnedViewState(mContext, mWidget.isPinned(), pin_view,panelData.get_id());
//
//                    }
//
//                    @Override
//                    public void failure(Throwable t) {
//                        if(!Utils.isNetworkAvailable(mContext))
//                            ToastUtils.showToast(mContext,mContext.getResources().getString(R.string.no_internet_connection));
//                        else
//                            ToastUtils.showToast(mContext,mContext.getResources().getString(R.string.oops));
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
        this.jwtToken = jwtToken;
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
        WidgetDataLoader.loadWidgetChartData(mWidget.getCallbackURL(), Utils.ah(jwtToken), result)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<WidgetsDataModel>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(WidgetsDataModel model) {
//                        afterDataLoad(model);
                        if(model!=null && model.getData().get(0).getElements()!=null&&model.getData().get(0).getElements().size()>0){
                            model.getData().get(0).convertElementsToModel();
                            setData(model.getData().get(0).getBarChartElements());
                        }

                        Log.d("IKIDO","Hi");
                    }

                    @Override
                    public void onError(Throwable e) {
                        progress.setVisibility(View.GONE);


                        String msg;
                        Drawable drawable=null;
                        if (!NetworkUtility.isNetworkConnectionAvailable(BarChartWidgetView.this.getContext())) {
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

    public void setData(ArrayList<BotBarChartDataModel> barChartElements) {
        float barWidth = 0.2f;
        float groupSpace = 0.08f;
        float barSpace = 0.03f; // x4 DataSet
        int startYear = 1;
        int groupCount = 4;
        labelCount = 0;
//        String[] company = {"Company A","Company B","Company C","Company D"};
//        int endYear = startYear + groupCount;
        ArrayList<BarEntry> yVals1[];// = new ArrayList<BarEntry>();
//        ArrayList<BarEntry> yVals2 = new ArrayList<BarEntry>();
        BarDataSet dataSet[];
        List<IBarDataSet> barDataSets = new ArrayList<>();

        if (barChartElements != null && barChartElements.size() > 0) {


//            List<ArrayList<BarEntry>> yVals1 = new ArrayList<>();
            int size = barChartElements.size();

            yVals1 = new ArrayList[size];
            for (int index = 0; index < size; index++) {
                BotBarChartDataModel model = barChartElements.get(index);
                yVals1[index] = new ArrayList<>();
//                BotBarChartDataModel model2 = _payInner.getBarChartDataModels().get(1);
                for (int inner = 0; inner < model.getValues().size(); inner++) {
                    yVals1[index].add(new BarEntry(inner + 1, model.getValues().get(inner), model.getDisplayValues().get(inner)));
                }
            }
            dataSet = new BarDataSet[size];

            for (int k = 0; k < size; k++) {
                dataSet[k] = new BarDataSet(yVals1[k],barChartElements.get(k).getTitle());
                dataSet[k].setColor(ColorTemplate.MATERIAL_COLORS[k % 4]);
                barDataSets.add(dataSet[k]);
            }


            BarData data = new BarData(barDataSets);
            data.setValueFormatter(new BarChartDataFormatter());
//        data.setValueTypeface(mTfLight);

            XAxis xAxis = mChart.getXAxis();
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
//            xAxis.setTypeface(mTfLight);
            xAxis.setTextSize(8f);
            xAxis.setDrawGridLines(false);
            xAxis.setGranularity(0.5f); // only intervals of 1 day
            xAxis.setLabelCount(4);
            ValueFormatter xAxisFormatter = new ValueFormatter() {
                @Override
                public String getFormattedValue(float v, AxisBase axisBase) {
//                    Log.d("IKIDI", "Hi The Val is "+(int) v % _payInner.getxAxis().size());
//                    return _payInner.getxAxis().get((int) (v/2) % _payInner.getxAxis().size());
                    return "";
                }
            };

            xAxis.setValueFormatter(xAxisFormatter);

//            IAxisValueFormatter custom = new MyAxisValueFormatter();

            /*YAxis leftAxis = mChart.getAxisLeft();
            leftAxis.setTypeface(mTfLight);
            leftAxis.setLabelCount(8, false);
            leftAxis.setValueFormatter(custom);
            leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
            leftAxis.setSpaceTop(15f);
            leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

            YAxis rightAxis = mChart.getAxisRight();
            rightAxis.setDrawGridLines(false);
            rightAxis.setTypeface(mTfLight);
            rightAxis.setLabelCount(8, false);
            rightAxis.setValueFormatter(custom);
            rightAxis.setSpaceTop(15f);
            rightAxis.setAxisMinimum(0f); */// this replaces setStartAtZero(true)

            Legend l = mChart.getLegend();
            l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
            l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
            l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
            l.setDrawInside(false);
            l.setForm(Legend.LegendForm.SQUARE);
            l.setFormSize(9f);
            l.setTextSize(11f);
            l.setXEntrySpace(4f);
            // l.setExtra(ColorTemplate.VORDIPLOM_COLORS, new String[] { "abc",
            // "def", "ghj", "ikl", "mno" });
            // l.setCustom(ColorTemplate.VORDIPLOM_COLORS, new String[] { "abc",
            // "def", "ghj", "ikl", "mno" });

//            XYMarkerView mv = new XYMarkerView(this, xAxisFormatter);
//            mv.setChartView(mChart); // For bounds control
//            mChart.setMarker(mv); // Set the marker to the chart

            mChart.setData(data);

            mChart.getBarData().setBarWidth(barWidth);

            // restrict the x-axis range
            mChart.getXAxis().setAxisMinimum(startYear);


            // barData.getGroupWith(...) is a helper that calculates the width each group needs based on the provided parameters
            mChart.getXAxis().setAxisMaximum(startYear + mChart.getBarData().getGroupWidth(groupSpace, barSpace) * groupCount);
            mChart.groupBars(startYear, groupSpace, barSpace);
            mChart.invalidate();
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

    protected RectF mOnValueSelectedRectF = new RectF();

   /* @Override
    public void onValueSelected(Entry e, Highlight highlight) {
        if (e == null)
            return;

        RectF bounds = mOnValueSelectedRectF;
        mChart.getBarBounds((BarEntry) e, bounds);
        MPPointF position = mChart.getPosition(e, YAxis.AxisDependency.LEFT);

        Log.i("bounds", bounds.toString());
        Log.i("position", position.toString());

        Log.i("x-index",
                "low: " + mChart.getLowestVisibleX() + ", high: "
                        + mChart.getHighestVisibleX());

        MPPointF.recycleInstance(position);
    }

    @Override
    public void onNothingSelected() {
        //
    }*/


}
