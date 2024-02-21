package com.kore.ai.widgetsdk.views.widgetviews;

import static com.kore.ai.widgetsdk.utils.AppUtils.getMapObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.kora.ai.widgetsdk.R;
import com.kore.ai.widgetsdk.charts.charts.BarChart;
import com.kore.ai.widgetsdk.charts.components.AxisBase;
import com.kore.ai.widgetsdk.charts.components.Legend;
import com.kore.ai.widgetsdk.charts.components.XAxis;
import com.kore.ai.widgetsdk.charts.components.YAxis;
import com.kore.ai.widgetsdk.charts.data.BarData;
import com.kore.ai.widgetsdk.charts.data.BarDataSet;
import com.kore.ai.widgetsdk.charts.data.BarEntry;
import com.kore.ai.widgetsdk.charts.formatter.LargeValueFormatter;
import com.kore.ai.widgetsdk.charts.formatter.ValueFormatter;
import com.kore.ai.widgetsdk.charts.interfaces.datasets.IBarDataSet;
import com.kore.ai.widgetsdk.charts.utils.ColorTemplate;
import com.kore.ai.widgetsdk.formatters.BarChartDataFormatter;
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
import java.util.List;
import java.util.Map;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

@SuppressLint("UnknownNullness")
public class BarChartWidgetView extends BaseWidgetView /*implements OnChartValueSelectedListener*/ {

    private WidgetsModel mWidget;
    private BarChart mChart;
    public ProgressBar progress;
    private View rootView;
    private TextView chartHeader;
    private final Context mContext;
    private TextView panel_name_view;
    private String jwtToken;

    public BarChartWidgetView(Context context) {
        super(context);
        this.mContext = context;
        init();
    }

    private void init() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.barchart_widgetview, this, true);
        rootView = view.findViewById(R.id.chart_root_view);
        chartHeader = view.findViewById(R.id.chart_header);
        progress = view.findViewById(R.id.chart_progress);
        panel_name_view = view.findViewById(R.id.panel_name_view);
        mChart = view.findViewById(R.id.mChart);
        getUserData();
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
    }

    private void getUserData() {
        AuthData authData = UserDataManager.getAuthData();
        UserData userData = UserDataManager.getUserData();
    }

    PanelLevelData panelData;
    String panelName;

    public void setWidget(String panelName, WidgetsModel mWidget, PanelLevelData panelData, String jwtToken) {
        this.mWidget = mWidget;
        this.panelName = panelName;
        this.panelData = panelData;
        this.jwtToken = jwtToken;
        chartHeader.setText(mWidget.getTitle());
//        pin_view.setText(mWidget.isPinned() ? mContext.getResources().getString(R.string.icon_31) :mContext.getResources().getString(R.string.icon_32));
        panel_name_view.setText(KaUtility.getPanelFormatedName(mWidget.getName()));
        panel_name_view.setVisibility(KaUtility.isTitleAndPanelNameMatch(mWidget.getName(), panelName));


        loadData();
    }

    private void loadData() {
        if (mWidget.getCallbackURL() == null) {
            return;
        }
        progress.setVisibility(View.VISIBLE);
        Map<String, Object> result = getMapObject(mWidget.getParam());
        WidgetDataLoader.loadWidgetChartData(mWidget.getCallbackURL(), Utils.ah(jwtToken), result).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<WidgetsDataModel>() {
            @Override
            public void onSubscribe(@androidx.annotation.NonNull Disposable d) {
            }

            @Override
            public void onNext(@androidx.annotation.NonNull WidgetsDataModel model) {
                if (model.getData().get(0).getElements() != null && model.getData().get(0).getElements().size() > 0) {
                    model.getData().get(0).convertElementsToModel();
                    setData(model.getData().get(0).getBarChartElements());
                }

                Log.d("IKIDO", "Hi");
            }

            @Override
            public void onError(@NonNull Throwable e) {
                progress.setVisibility(View.GONE);
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
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
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
        ArrayList<BarEntry>[] yVals1;// = new ArrayList<BarEntry>();
        BarDataSet[] dataSet;
        List<IBarDataSet> barDataSets = new ArrayList<>();

        if (barChartElements != null && barChartElements.size() > 0) {
            int size = barChartElements.size();

            yVals1 = new ArrayList[size];
            for (int index = 0; index < size; index++) {
                BotBarChartDataModel model = barChartElements.get(index);
                yVals1[index] = new ArrayList<>();
                for (int inner = 0; inner < model.getValues().size(); inner++) {
                    yVals1[index].add(new BarEntry(inner + 1, model.getValues().get(inner), model.getDisplayValues().get(inner)));
                }
            }
            dataSet = new BarDataSet[size];

            for (int k = 0; k < size; k++) {
                dataSet[k] = new BarDataSet(yVals1[k], barChartElements.get(k).getTitle());
                dataSet[k].setColor(ColorTemplate.MATERIAL_COLORS[k % 4]);
                barDataSets.add(dataSet[k]);
            }


            BarData data = new BarData(barDataSets);
            data.setValueFormatter(new BarChartDataFormatter());

            XAxis xAxis = mChart.getXAxis();
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis.setTextSize(8f);
            xAxis.setDrawGridLines(false);
            xAxis.setGranularity(0.5f); // only intervals of 1 day
            xAxis.setLabelCount(4);
            ValueFormatter xAxisFormatter = new ValueFormatter() {
                @Override
                public String getFormattedValue(float v, AxisBase axisBase) {
                    return "";
                }
            };

            xAxis.setValueFormatter(xAxisFormatter);

            Legend l = mChart.getLegend();
            l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
            l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
            l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
            l.setDrawInside(false);
            l.setForm(Legend.LegendForm.SQUARE);
            l.setFormSize(9f);
            l.setTextSize(11f);
            l.setXEntrySpace(4f);

            mChart.setData(data);
            mChart.getBarData().setBarWidth(barWidth);

            // restrict the x-axis range
            mChart.getXAxis().setAxisMinimum(startYear);
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
}
