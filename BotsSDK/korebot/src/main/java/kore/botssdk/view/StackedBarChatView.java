package kore.botssdk.view;

import android.content.Context;
import android.graphics.RectF;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.BarChart;
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

/**
 * Created by Ramachandra Pradeep on 11-May-18.
 */

public class StackedBarChatView extends ViewGroup implements OnChartValueSelectedListener {

    private BarChart mChart;
    private Context mContext;
    int dp1;
    int labelCount = 0;

    public StackedBarChatView(Context context) {
        super(context);
        this.mContext = context;
        init();
    }

    private void init() {
        labelCount = 0;
        dp1 = (int) AppControl.getInstance().getDimensionUtil().dp1;
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


            int size = _payInner.getBarChartDataModels().size();


            yVals1 = new ArrayList[1];
//                for(int index = 0; index < size; index++) {
//            BotBarChartDataModel model = _payInner.getBarChartDataModels().get(0);
            yVals1[0] = new ArrayList<>();
//                BotBarChartDataModel model2 = _payInner.getBarChartDataModels().get(1);
            ArrayList<BotBarChartDataModel> dataList = new ArrayList<>(size);
            String labels[] = new String[size];
            for (int in = 0; in < size; in++) {
                dataList.add(_payInner.getBarChartDataModels().get(in));
                labels[in] = _payInner.getBarChartDataModels().get(in).getTitle();
            }

            for (int k = 0; k < dataList.get(0).getValues().size(); k++) {
                float arr[] = new float[size];
                for (int j = 0; j < size; j++) {
                    arr[j] = dataList.get(j).getValues().get(k);
                }
                yVals1[0].add(new BarEntry(k + 1, arr, ""));
            }


//                }
            dataSet = new BarDataSet[1];

            dataSet[0] = new BarDataSet(yVals1[0], "");
            dataSet[0].setColors(getColors(size));
            dataSet[0].setStackLabels(labels);
            barDataSets.add(dataSet[0]);
//            }


            BarData data = new BarData(barDataSets);
            data.setValueFormatter(new BarChartDataFormatter());
//        data.setValueTypeface(mTfLight);

            XAxis xAxis = mChart.getXAxis();
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
//            xAxis.setTypeface(mTfLight);
            xAxis.setTextSize(8f);
            xAxis.setDrawGridLines(false);
            xAxis.setGranularity(1f); // only intervals of 1 day
            xAxis.setLabelCount(4);
            xAxis.setLabelRotationAngle(-60f);

            ValueFormatter xAxisFormatter = new ValueFormatter() {
                @Override
                public String getFormattedValue(float v) {
                    if(((int)v) < _payInner.getxAxis().size())
                        return _payInner.getxAxis().get((int)v);
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

        for (int i = 0; i < colors.length; i++) {
            colors[i] = ColorTemplate.MATERIAL_COLORS[i];
        }

        return colors;
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
    public void onValueSelected(Entry e, Highlight highlight) {
       /* if (e == null)
            return;

        RectF bounds = mOnValueSelectedRectF;
        mChart.getBarBounds((BarEntry) e, bounds);
        MPPointF position = mChart.getPosition(e, YAxis.AxisDependency.LEFT);

        Log.i("bounds", bounds.toString());
        Log.i("position", position.toString());

        Log.i("x-index",
                "low: " + mChart.getLowestVisibleX() + ", high: "
                        + mChart.getHighestVisibleX());

        MPPointF.recycleInstance(position);*/
    }

    @Override
    public void onNothingSelected() {
        //
    }
}
