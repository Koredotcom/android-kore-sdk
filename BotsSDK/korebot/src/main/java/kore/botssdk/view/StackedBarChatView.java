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
 * Created by Ramachandra Pradeep on 11-May-18.
 */
@SuppressLint("UnknownNullness")
public class StackedBarChatView extends ViewGroup implements OnChartValueSelectedListener {

    private BarChart mChart;
    private final Context mContext;
    int dp1;
    int labelCount = 0;

    public StackedBarChatView(Context context) {
        super(context);
        this.mContext = context;
        init();
    }

    private void init() {
        labelCount = 0;
        dp1 = (int) DimensionUtil.dp1;
        mChart = new BarChart(mContext);
        mChart.setTouchEnabled(true);
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
        mChart.setOnChartValueSelectedListener(this);
        mChart.setTouchEnabled(false);

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
    }

    public void setData(final PayloadInner _payInner) {
        float barWidth = 0.2f;
        labelCount = 0;
        ArrayList<BarEntry>[] yVals1;// = new ArrayList<BarEntry>();
        BarDataSet[] dataSet;
        List<IBarDataSet> barDataSets = new ArrayList<>();

        if (_payInner.getBarChartDataModels() != null && _payInner.getBarChartDataModels().size() > 0) {
            int size = _payInner.getBarChartDataModels().size();
            yVals1 = new ArrayList[1];
            yVals1[0] = new ArrayList<>();
            ArrayList<BotBarChartDataModel> dataList = new ArrayList<>(size);
            String[] labels = new String[size];
            for (int in = 0; in < size; in++) {
                dataList.add(_payInner.getBarChartDataModels().get(in));
                labels[in] = _payInner.getBarChartDataModels().get(in).getTitle();
            }

            for (int k = 0; k < dataList.get(0).getValues().size(); k++) {
                float[] arr = new float[size];
                for (int j = 0; j < size; j++) {
                    arr[j] = dataList.get(j).getValues().get(k);
                }
                yVals1[0].add(new BarEntry(k + 1, arr, ""));
            }

            dataSet = new BarDataSet[1];

            dataSet[0] = new BarDataSet(yVals1[0], "");
            dataSet[0].setColors(getColors(size));
            dataSet[0].setStackLabels(labels);
            barDataSets.add(dataSet[0]);

            BarData data = new BarData(barDataSets);
            data.setValueFormatter(new BarChartDataFormatter());

            XAxis xAxis = mChart.getXAxis();
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis.setTextSize(8f);
            xAxis.setDrawGridLines(false);
            xAxis.setGranularity(1f); // only intervals of 1 day
            xAxis.setLabelCount(4);
            xAxis.setLabelRotationAngle(-60f);

            ValueFormatter xAxisFormatter = new ValueFormatter() {
                @Override
                public String getFormattedValue(float v) {
                    if (((int) v) < _payInner.getxAxis().size())
                        return _payInner.getxAxis().get((int) v);
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
            mChart.setFitBars(true);

            mChart.getBarData().setBarWidth(barWidth);

        }

    }

    private int[] getColors(int stacksize) {
        // have as many colors as stack-values per entry
        int[] colors = new int[stacksize];

        System.arraycopy(ColorTemplate.MATERIAL_COLORS, 0, colors, 0, colors.length);

        return colors;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        final int count = getChildCount();

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
    }
}
