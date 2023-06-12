

package kore.botssdk.view;

import android.content.Context;
import android.graphics.Color;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

import kore.botssdk.R;
import kore.botssdk.charts.charts.LineChart;
import kore.botssdk.charts.components.Description;
import kore.botssdk.charts.components.Legend;
import kore.botssdk.charts.components.XAxis;
import kore.botssdk.charts.data.Entry;
import kore.botssdk.charts.data.LineData;
import kore.botssdk.charts.data.LineDataSet;
import kore.botssdk.charts.formatter.ValueFormatter;
import kore.botssdk.charts.interfaces.datasets.ILineDataSet;
import kore.botssdk.charts.listener.ChartTouchListener;
import kore.botssdk.charts.listener.OnChartGestureListener;
import kore.botssdk.charts.utils.ColorTemplate;
import kore.botssdk.models.BotLineChartDataModel;
import kore.botssdk.models.PayloadInner;
import kore.botssdk.utils.StringUtils;
import kore.botssdk.view.viewUtils.DimensionUtil;
import kore.botssdk.view.viewUtils.LayoutUtils;
import kore.botssdk.view.viewUtils.MeasureUtils;


/**
 * Created by Shiva Krishna on 11/6/2017.
 */


public class LineChartView extends ViewGroup implements OnChartGestureListener {
    private LineChart mChart;
    private final Context mContext;
    int dp1;

    public LineChartView(Context context) {
        super(context);
        this.mContext = context;
        init();
    }

    private void init() {
        dp1 = (int) DimensionUtil.dp1;
        mChart = new LineChart(mContext);
        mChart.getDescription().setEnabled(false);
        mChart.setTouchEnabled(true);
        CustomMarkerView mv = new CustomMarkerView(mContext, R.layout.marker_content);

// set the marker to the chart
        mChart.setMarkerView(mv);
        mChart.setOnChartGestureListener(this);
        addView(mChart);
//        setBackgroundColor(mContext.getResources().getColor(R.color.bgLightBlue));
    }

    public void setData(final PayloadInner _payInner) {


        LineDataSet[] dataSet;



        if(_payInner!=null&&_payInner.getText()!=null&& !StringUtils.isNullOrEmptyWithTrim(_payInner.getText())) {
            Description desc = new Description();
            desc.setText(_payInner.getText());
            mChart.setDescription(desc);
        }
       // mChart.getDescription().setPosition(mChart.getX()+(100*dp1),mChart.getY());

        ArrayList<BotLineChartDataModel> lineList = _payInner.getLineChartDataModels();

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
            dataSet[baseIndex].setDrawFilled(true);
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

        XAxis xAxis = mChart.getXAxis();
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return _payInner.getxAxis().get((int)value);
            }
        });
        xAxis.setLabelRotationAngle(-60f);
        xAxis.setLabelCount( _payInner.getxAxis().size(), true);

        Legend l = mChart.getLegend();

        // modify the legend ...
        l.setForm(Legend.LegendForm.LINE);
//        l.setTypeface(mTfLight);
        l.setTextSize(11f);
        l.setTextColor(Color.BLACK);
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);



    }


    private String[] getXAxisValues(ArrayList<BotLineChartDataModel> dataModels){
        String[] arr = new String[dataModels.size()];
        for(int in=0; in<dataModels.size();in++){
            arr[in] = dataModels.get(in).getTitle();
        }

        return arr;
    }
    private ArrayList<ILineDataSet> getYAxisValues(ArrayList<BotLineChartDataModel> dataModels, List<String> headers){
        ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
        SecureRandom randomGenerator = new SecureRandom();
        String[] colors = this.getResources().getStringArray(R.array.color_set);

        int size = dataModels != null && dataModels.size() > 0 ? dataModels.get(0).getValues() != null ? dataModels.get(0).getValues().size() : 0 :0;
        for (int i = 0; i <= size - 1; i++) {
            ArrayList<Entry> yVals = new ArrayList<Entry>();
            for (int j = 0; j <= dataModels.size() - 1; j++) {
                List<Float> values = dataModels.get(j).getValues();
                yVals.add(new Entry(values.get(i), j));
            }

            LineDataSet set1;
            int index = randomGenerator.nextInt(colors.length-1);
            Integer color =  Color.parseColor(colors[index]);
            set1 = new LineDataSet(yVals, headers.get(i+1));
            set1.setFillAlpha(110);
            set1.setColor(color);
            set1.setDrawValues(false);
//            set1.setDrawCubic(true);
            set1.setCircleColor(Color.BLACK);
            set1.setLineWidth(1f);
            set1.setCircleRadius(3f);
            set1.setDrawCircleHole(false);
            set1.setValueTextSize(9f);
            set1.setDrawFilled(false);
            dataSets.add(set1);
        }
        return  dataSets;
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

    @Override
    public void onChartGestureStart(MotionEvent motionEvent, ChartTouchListener.ChartGesture chartGesture) {

    }

    @Override
    public void onChartGestureEnd(MotionEvent motionEvent, ChartTouchListener.ChartGesture chartGesture) {
        // un-highlight values after the gesture is finished and no single-tap
        if(chartGesture != ChartTouchListener.ChartGesture.SINGLE_TAP)
            mChart.highlightValues(null);
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

    }
}
