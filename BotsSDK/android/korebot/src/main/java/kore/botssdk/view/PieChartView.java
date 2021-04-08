package kore.botssdk.view;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.util.ArrayList;

import kore.botssdk.R;
import kore.botssdk.application.AppControl;
import kore.botssdk.view.viewUtils.LayoutUtils;
import kore.botssdk.view.viewUtils.MeasureUtils;

/**
 * Created by Ramachandra Pradeep on 30-Oct-17.
 */

public class PieChartView extends ViewGroup {
    private PieChart mChart;
    private Context mContext;
    int dp1;
    private float holeRadius;
    private float transparentCircleRadius;
    private String pieType;

    private final String PIE_TYPE_REGULAR = "regular";
    private final String PIE_TYPE_DONUT = "donut";

    public PieChartView(Context context) {
        super(context);
        this.mContext = context;
        init();
    }

    private void init(){
        dp1 = (int) AppControl.getInstance().getDimensionUtil().dp1;
        mChart = new PieChart(mContext);
        addView(mChart);
        setBackgroundColor(mContext.getResources().getColor(R.color.bgLightBlue));
    }

    public void populatePieChart(String desc, String pieType, ArrayList<String> xVals, ArrayList<PieEntry> yVals, ArrayList<String> arrLabels){
        this.pieType = pieType;
        if(pieType != null && pieType.equals(PIE_TYPE_DONUT)){
            holeRadius = 56f;
            transparentCircleRadius = 61f;
        }else{
            holeRadius = 0f;
            transparentCircleRadius = 0f;
        }
        mChart.setUsePercentValues(true);
        mChart.setDrawHoleEnabled(true);
        Description sdesc = new Description();
        sdesc.setText(desc);
        mChart.setDescription(sdesc);
        mChart.setHoleRadius(holeRadius);
        mChart.setTransparentCircleRadius(transparentCircleRadius);


        mChart.setRotationAngle(-30);
        mChart.setRotationEnabled(true);

        mChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {

                if (e == null)
                    return;
                Log.i("VAL SELECTED",
                        "Value: " + e.getY() + ", index: " + h.getX()
                                + ", DataSet index: " + h.getDataSetIndex());
            }

            @Override
            public void onNothingSelected() {
                Log.i("PieChart", "nothing selected");
            }
        });

        addData(xVals,yVals);

        ArrayList<LegendEntry> arrLegendEntries = new ArrayList<>();
        String[] colorsArray = this.getResources().getStringArray(R.array.color_set);
        for (int i = 0 ; i < yVals.size(); i++)
        {
            LegendEntry legendEntryA = new LegendEntry();
            legendEntryA.label = arrLabels.get(i);
            legendEntryA.formColor = Color.parseColor(colorsArray[i]);
            arrLegendEntries.add(legendEntryA);
        }

        Legend l = mChart.getLegend();
        l.setCustom(arrLegendEntries);
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
//        l.setXEntrySpace(7f);
//        l.setYEntrySpace(0f);
//        l.setYOffset(0f);

        CustomMarkerView mv = new CustomMarkerView (getContext(), R.layout.marker_content);
        mChart.setMarkerView(mv);

        // entry label styling
        mChart.setEntryLabelColor(Color.WHITE);
//        chart.setEntryLabelTypeface(tfRegular);
        mChart.setEntryLabelTextSize(12f);
    }

    private void addData(ArrayList<String> xVals, ArrayList<PieEntry> yVals){
        PieDataSet dataSet = new PieDataSet(yVals,"");
        dataSet.setSliceSpace(3);
        dataSet.setSelectionShift(5);

        ArrayList<Integer> colors = new ArrayList<Integer>();
        String[] colorsArray = this.getResources().getStringArray(R.array.color_set);
        for(String color : colorsArray){
            colors.add(Color.parseColor(color));
        }

        dataSet.setColors(colors);

        if(pieType != null && pieType.equals(PIE_TYPE_DONUT)){
            dataSet.setValueLinePart1OffsetPercentage(80.f);
            dataSet.setValueLinePart1Length(0.2f);
            dataSet.setValueLinePart2Length(0.4f);

            dataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        }



        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.WHITE);

        mChart.setData(data);
        mChart.highlightValues(null);
        mChart.invalidate();

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
        childHeightSpec = MeasureSpec.makeMeasureSpec((int) getResources().getDimension(R.dimen.pie_layout_height), MeasureSpec.EXACTLY);

        MeasureUtils.measure(mChart, childWidthSpec, childHeightSpec);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
