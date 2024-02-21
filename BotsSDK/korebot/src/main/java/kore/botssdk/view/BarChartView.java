package kore.botssdk.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import kore.botssdk.R;
import kore.botssdk.charts.charts.BarChart;
import kore.botssdk.charts.components.Legend;
import kore.botssdk.charts.components.XAxis;
import kore.botssdk.charts.components.YAxis;
import kore.botssdk.charts.data.BarData;
import kore.botssdk.charts.data.BarDataSet;
import kore.botssdk.charts.data.BarEntry;
import kore.botssdk.charts.data.Entry;
import kore.botssdk.charts.formatter.LargeValueFormatter;
import kore.botssdk.charts.formatter.ValueFormatter;
import kore.botssdk.charts.highlight.Highlight;
import kore.botssdk.charts.interfaces.datasets.IBarDataSet;
import kore.botssdk.charts.listener.OnChartValueSelectedListener;
import kore.botssdk.charts.utils.ColorTemplate;
import kore.botssdk.formatters.BarChartDataFormatter;
import kore.botssdk.models.BotBarChartDataModel;
import kore.botssdk.models.PayloadInner;
import kore.botssdk.view.viewUtils.DimensionUtil;
import kore.botssdk.view.viewUtils.LayoutUtils;
import kore.botssdk.view.viewUtils.MeasureUtils;

/**
 * Created by Ramachandra Pradeep on 11-Apr-18.
 */
@SuppressLint("UnknownNullness")
public class BarChartView extends ViewGroup implements OnChartValueSelectedListener {
    private BarChart mChart;
    private final Context mContext;
    int dp1;
    int labelCount = 0;

    public BarChartView(Context context) {
        super(context);
        this.mContext = context;
        init();
    }

    private void init() {
        labelCount = 0;
        dp1 = (int) DimensionUtil.dp1;
        mChart = new BarChart(mContext);
        mChart.setTouchEnabled(true);
        mChart.setPinchZoom(true);
        mChart.setDrawBarShadow(false);
        mChart.setDrawGridBackground(false);
        mChart.setDrawValueAboveBar(true);
        mChart.setDragXEnabled(true);
        mChart.getDescription().setEnabled(false);
        mChart.zoom(1f,0f,0,0);

        CustomMarkerView mv = new CustomMarkerView(mContext, R.layout.marker_content);

// set the marker to the chart
        mv.setChartView(mChart);
        mChart.setMarker(mv);
        mChart.setOnChartValueSelectedListener(this);
        mChart.setTouchEnabled(true);

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

        addView(mChart);


        // if more than 60 entries are displayed in the chart, no values will be
        // drawn
        mChart.invalidate();
        mChart.setMaxVisibleValueCount(3);
//        setBackgroundColor(mContext.getResources().getColor(R.color.bgLightBlue));
    }

    public void setData(final PayloadInner _payInner) {
        float barWidth = 0.2f;
        float groupSpace = 0.08f;
        float barSpace = 0.03f; // x4 DataSet
        int startYear = 1;
        int groupCount = 4;
        labelCount = 0;
        ArrayList<BarEntry>[] yVals1;// = new ArrayList<BarEntry>();
        BarDataSet[] dataSet;
        List<IBarDataSet> barDataSets = new ArrayList<>();

        if (_payInner.getBarChartDataModels() != null && _payInner.getBarChartDataModels().size() > 0) {
            int size = _payInner.getBarChartDataModels().size();

            yVals1 = new ArrayList[size];
            for (int index = 0; index < size; index++) {
                BotBarChartDataModel model = _payInner.getBarChartDataModels().get(index);
                yVals1[index] = new ArrayList<>();
                for (int inner = 0; inner < model.getValues().size(); inner++) {
                    yVals1[index].add(new BarEntry(inner + 1, model.getValues().get(inner), model.getDisplayValues().get(inner)));
                }

            }

            dataSet = new BarDataSet[size];

            for (int k = 0; k < size; k++) {
                dataSet[k] = new BarDataSet(yVals1[k], _payInner.getBarChartDataModels().get(k).getTitle());
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
            xAxis.setLabelCount(_payInner.getxAxis().size());
            xAxis.setLabelRotationAngle(-60f);

            ValueFormatter xAxisFormatter = new ValueFormatter() {
                @Override
                public String getFormattedValue(float v) {
                    if(((int)(v * 2) - 2) >= 0 &&  ((int)(v * 2) - 2) < _payInner.getxAxis().size())
                        return _payInner.getxAxis().get((int)(v * 2) - 2);
                    else
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

            // barData.getGroupWith(...) is a helper that calculates the width each group needs based on the provided parameters
            mChart.getXAxis().setAxisMaximum(startYear + mChart.getBarData().getGroupWidth(groupSpace, barSpace) * groupCount);
            mChart.groupBars(startYear, groupSpace, barSpace);

        }

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        final int count = getChildCount();
        int parentWidth = getMeasuredWidth();

        //get the available size of child view
        int childLeft = this.getPaddingLeft();
        int childTop = this.getPaddingTop();

        //walk through each child, and arrange it from left to right
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
        int maxAllowedWidth = MeasureSpec.getSize(widthMeasureSpec);

        int childWidthSpec;
        int childHeightSpec;

        /*
         * For Pie View Layout
         */
        childWidthSpec = MeasureSpec.makeMeasureSpec(maxAllowedWidth, MeasureSpec.EXACTLY);
        childHeightSpec = MeasureSpec.makeMeasureSpec((int) getResources().getDimension(R.dimen.line_layout_height), MeasureSpec.EXACTLY);

        MeasureUtils.measure(mChart, childWidthSpec, childHeightSpec);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public void onValueSelected(Entry e, Highlight highlight) {
    }

    @Override
    public void onNothingSelected() {
        //
    }
}