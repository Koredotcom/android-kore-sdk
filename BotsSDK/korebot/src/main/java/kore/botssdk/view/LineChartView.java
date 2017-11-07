

package kore.botssdk.view;

import android.content.Context;
import android.graphics.Color;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.utils.Utils;

import java.util.ArrayList;
import java.util.Random;

import kore.botssdk.R;
import kore.botssdk.application.AppControl;
import kore.botssdk.models.BotLineChartDataModel;
import kore.botssdk.view.viewUtils.LayoutUtils;
import kore.botssdk.view.viewUtils.MeasureUtils;


/**
 * Created by Shiva Krishna on 11/6/2017.
 */


public class LineChartView extends ViewGroup implements OnChartGestureListener {
    private LineChart mChart;
    private Context mContext;
    int dp1;

    public LineChartView(Context context) {
        super(context);
        this.mContext = context;
        init();
    }

    private void init() {
        dp1 = (int) AppControl.getInstance().getDimensionUtil().dp1;
        mChart = new LineChart(mContext);
        mChart.setTouchEnabled(true);
        CustomMarkerView mv = new CustomMarkerView(mContext, R.layout.marker_content);

// set the marker to the chart
        mChart.setMarkerView(mv);
        mChart.setOnChartGestureListener(this);
        addView(mChart);
//        setBackgroundColor(mContext.getColor(R.color.bgLightBlue));
    }

    public void setData(ArrayList<BotLineChartDataModel> data,ArrayList<String> headers) {



        mChart.setDescription(headers.get(0));
        ArrayList<ILineDataSet> dataSets = getYAxisValues(data,headers);
        LineData lineData = new LineData(getXAxisValues(data), dataSets);


        // set data
        mChart.setData(lineData);
        mChart.getXAxis().setTextSize(8);
        mChart.getXAxis().setDrawAxisLine(true);
        mChart.getXAxis().setLabelsToSkip(0);
        mChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        mChart.setDrawGridBackground(false);
        mChart.getXAxis().setDrawGridLines(false); // disable grid lines for the XAxis
        mChart.getAxisLeft().setDrawGridLines(false); // disable grid lines for the left YAxis
        mChart.getAxisRight().setDrawGridLines(false);
        mChart.getAxisRight().setEnabled(false);// disable grid lines for the right YAxis
        // get the legend (only possible after setting data)
        Legend l = mChart.getLegend();
        mChart.setExtraBottomOffset(20);
        mChart.setPinchZoom(true);
        // modify the legend ...
         l.setPosition(Legend.LegendPosition.ABOVE_CHART_CENTER);
        l.setForm(Legend.LegendForm.LINE);

    }


    private ArrayList<String> getXAxisValues(ArrayList<BotLineChartDataModel> dataModels){
        ArrayList<String> xVals = new ArrayList<String>();
        for(BotLineChartDataModel botLineChartDataModel : dataModels){
            xVals.add(botLineChartDataModel.getTitle());
        }

        return xVals;
    }
    private ArrayList<ILineDataSet> getYAxisValues(ArrayList<BotLineChartDataModel> dataModels, ArrayList<String> headers){
        ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
        Random randomGenerator = new Random();
        String[] colors = this.getResources().getStringArray(R.array.color_set);

        int size = dataModels != null && dataModels.size() > 0 ? dataModels.get(0).getValues() != null ? dataModels.get(0).getValues().size() : 0 :0;
        for (int i = 0; i <= size - 1; i++) {
            ArrayList<Entry> yVals = new ArrayList<Entry>();
            for (int j = 0; j <= dataModels.size() - 1; j++) {
                ArrayList<Float> values = dataModels.get(j).getValues();
                yVals.add(new Entry(values.get(i), j));
            }

            LineDataSet set1;
            int index = randomGenerator.nextInt(colors.length-1);
            Integer color =  Color.parseColor(colors[index]);
            set1 = new LineDataSet(yVals, headers.get(i+1));
            set1.setFillAlpha(110);
            set1.setColor(color);
            set1.setDrawValues(false);
            set1.setDrawCubic(true);
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
