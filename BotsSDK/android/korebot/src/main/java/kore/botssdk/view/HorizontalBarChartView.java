package kore.botssdk.view;

import android.content.Context;
import android.graphics.RectF;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.LargeValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.MPPointF;

import java.util.ArrayList;
import java.util.List;

import kore.botssdk.R;
import kore.botssdk.application.AppControl;
import kore.botssdk.formatters.BarChartDataFormatter;
import kore.botssdk.models.BotBarChartDataModel;
import kore.botssdk.models.PayloadInner;
import kore.botssdk.view.viewUtils.LayoutUtils;
import kore.botssdk.view.viewUtils.MeasureUtils;

public class HorizontalBarChartView extends ViewGroup implements OnChartValueSelectedListener {
    private HorizontalBarChart mChart;
    private Context mContext;
    int dp1;
    int labelCount = 0;

    public HorizontalBarChartView(Context context) {
        super(context);
        this.mContext = context;
        init();
    }

    private void init() {
        labelCount = 0;
        dp1 = (int) AppControl.getInstance().getDimensionUtil().dp1;
        mChart = new HorizontalBarChart(mContext);
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


        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setValueFormatter(new LargeValueFormatter());
        leftAxis.setDrawGridLines(false);
        leftAxis.setSpaceTop(35f);
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        mChart.getAxisRight().setEnabled(false);

        addView(mChart);
//        setBackgroundColor(mContext.getResources().getColor(R.color.bgLightBlue));
    }

    public void setData(final PayloadInner _payInner) {
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

        if (_payInner.getBarChartDataModels() != null && _payInner.getBarChartDataModels().size() > 0) {
//            List<ArrayList<BarEntry>> yVals1 = new ArrayList<>();
            int size = _payInner.getBarChartDataModels().size();

            yVals1 = new ArrayList[size];
            for (int index = 0; index < size; index++) {
                BotBarChartDataModel model = _payInner.getBarChartDataModels().get(index);
                yVals1[index] = new ArrayList<>();
//                BotBarChartDataModel model2 = _payInner.getBarChartDataModels().get(1);
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
//        data.setValueTypeface(mTfLight);
            Log.e("Values", _payInner.getxAxis()+"");
            XAxis xAxis = mChart.getXAxis();
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
//            xAxis.setTypeface(mTfLight);
            xAxis.setTextSize(8f);
            xAxis.setDrawGridLines(false);
            xAxis.setGranularity(0.5f); // only intervals of 1 day
            xAxis.setLabelCount(_payInner.getxAxis().size());
            xAxis.setDrawLabels(true);
            xAxis.setCenterAxisLabels(true);
            xAxis.setLabelRotationAngle(-60f);

            ValueFormatter xAxisFormatter = new ValueFormatter() {
                @Override
                public String getFormattedValue(float v) {
                    if(((int)(v * _payInner.getBarChartDataModels().size()) - _payInner.getBarChartDataModels().size()) >= 0 &&  ((int)(v * _payInner.getBarChartDataModels().size()) - _payInner.getBarChartDataModels().size()) < _payInner.getxAxis().size())
                        return _payInner.getxAxis().get((int)(v * _payInner.getBarChartDataModels().size()) - _payInner.getBarChartDataModels().size());
                    else
                        return "";
                }
            };

            xAxis.setValueFormatter(xAxisFormatter);

            Legend l = mChart.getLegend();
            l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
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
        int parentWidth = MeasureSpec.getSize(widthMeasureSpec);
        int maxAllowedWidth = parentWidth;
        int wrapSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);

        int totalHeight = getPaddingTop();
        int totalWidth = getPaddingLeft();

        int childWidthSpec;
        int childHeightSpec;
        int contentWidth = 0;

        /*
         * For Pie View Layout
         */
        childWidthSpec = MeasureSpec.makeMeasureSpec(maxAllowedWidth, MeasureSpec.EXACTLY);
        childHeightSpec = MeasureSpec.makeMeasureSpec((int) getResources().getDimension(R.dimen.line_layout_height), MeasureSpec.EXACTLY);

        MeasureUtils.measure(mChart, childWidthSpec, childHeightSpec);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    protected RectF mOnValueSelectedRectF = new RectF();

    @Override
    public void onValueSelected(Entry e, Highlight h) {

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
    }
}
