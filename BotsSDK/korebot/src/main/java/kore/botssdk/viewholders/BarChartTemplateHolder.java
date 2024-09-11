package kore.botssdk.viewholders;

import static kore.botssdk.models.BotResponsePayLoadText.BAR_CHART_DIRECTION_VERTICAL;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.LinearLayoutCompat;

import java.util.ArrayList;
import java.util.List;

import kore.botssdk.R;
import kore.botssdk.charts.charts.BarChart;
import kore.botssdk.charts.charts.HorizontalBarChart;
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
import kore.botssdk.models.BaseBotMessage;
import kore.botssdk.models.BotBarChartDataModel;
import kore.botssdk.models.PayloadInner;
import kore.botssdk.utils.LogUtils;
import kore.botssdk.view.CustomMarkerView;

public class BarChartTemplateHolder extends BaseViewHolder implements OnChartValueSelectedListener {
    private final int BAR_STACKED = 0;
    private final int BAR_VERTICAL = 1;
    private final int BAR_HORIZONTAL = 2;
    private final BarChart barChart;
    private final HorizontalBarChart horizontalBarChart;

    public static BarChartTemplateHolder getInstance(ViewGroup parent) {
        return new BarChartTemplateHolder(createView(R.layout.template_bar_chart, parent));
    }

    private BarChartTemplateHolder(@NonNull View itemView) {
        super(itemView, itemView.getContext());
        LinearLayoutCompat layoutBubble = itemView.findViewById(R.id.layoutBubble);
        initBubbleText(layoutBubble, false);
        barChart = itemView.findViewById(R.id.barChart);
        horizontalBarChart = itemView.findViewById(R.id.horizontalBarChart);
    }

    private void initBarChart(int type, BarChart mChart) {
        mChart.setTouchEnabled(true);
        mChart.setPinchZoom(type == BAR_VERTICAL);
        mChart.setDrawBarShadow(false);
        mChart.setDrawGridBackground(false);
        mChart.setDrawValueAboveBar(true);
        mChart.getDescription().setEnabled(false);

        if (type == BAR_VERTICAL) {
            mChart.setDragXEnabled(true);
            mChart.zoom(1f, 0f, 0, 0);
        }

        // if more than 60 entries are displayed in the chart, no values will be
        // drawn
        if (type != BAR_VERTICAL) mChart.setMaxVisibleValueCount(60);

        CustomMarkerView mv = new CustomMarkerView(itemView.getContext(), R.layout.marker_content);

        // set the marker to the chart
        mv.setChartView(mChart);
        mChart.setMarker(mv);
        mChart.setOnChartValueSelectedListener(this);
        mChart.setTouchEnabled(type != BAR_STACKED);

        Legend legend = mChart.getLegend();
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        legend.setOrientation(Legend.LegendOrientation.VERTICAL);
        legend.setDrawInside(true);
        legend.setYOffset(0f);
        legend.setXOffset(10f);
        legend.setYEntrySpace(0f);
        legend.setTextSize(8f);

        XAxis xAxis = mChart.getXAxis();
        xAxis.setGranularity(1f);
        xAxis.setCenterAxisLabels(true);
        if (type != BAR_HORIZONTAL) {
            xAxis.setValueFormatter(new ValueFormatter() {
                @Override
                public String getFormattedValue(float value) {
                    return String.valueOf((int) value);
                }
            });
        }

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setValueFormatter(new LargeValueFormatter());
        leftAxis.setDrawGridLines(false);
        leftAxis.setSpaceTop(35f);
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        mChart.getAxisRight().setEnabled(false);

        if (type == BAR_VERTICAL) {
            mChart.invalidate();
            mChart.setMaxVisibleValueCount(3);
        }
    }

    @Override
    public void bind(BaseBotMessage baseBotMessage) {
        PayloadInner payloadInner = getPayloadInner(baseBotMessage);
        if (payloadInner == null) return;
        setResponseText(itemView.findViewById(R.id.layoutBubble), payloadInner.getText());
        int barType = payloadInner.isStacked() ? BAR_STACKED : payloadInner.getDirection().equals(BAR_CHART_DIRECTION_VERTICAL) ? BAR_VERTICAL : BAR_HORIZONTAL;
        barChart.setVisibility(barType != BAR_HORIZONTAL ? View.VISIBLE : View.GONE);
        horizontalBarChart.setVisibility(barType == BAR_HORIZONTAL ? View.VISIBLE : View.GONE);
        BarChart mChart = barType == BAR_HORIZONTAL ? horizontalBarChart : barChart;
        initBarChart(barType, mChart);

        float barWidth = 0.2f;
        float groupSpace = 0.08f;
        float barSpace = 0.03f; // x4 DataSet
        int startYear = 1;
        int groupCount = 4;
//        labelCount = 0;
        ArrayList<BarEntry>[] yValues;// = new ArrayList<BarEntry>();
        BarDataSet[] dataSet;
        List<IBarDataSet> barDataSets = new ArrayList<>();

        if (payloadInner.getBarChartDataModels() != null && payloadInner.getBarChartDataModels().size() > 0) {
            int size = payloadInner.getBarChartDataModels().size();
            String[] labels = new String[size];
            ArrayList<BotBarChartDataModel> dataList = new ArrayList<>(size);
            yValues = new ArrayList[size];
            if (barType == BAR_STACKED) {
                for (int in = 0; in < size; in++) {
                    dataList.add(payloadInner.getBarChartDataModels().get(in));
                    labels[in] = payloadInner.getBarChartDataModels().get(in).getTitle();
                }
                for (int k = 0; k < dataList.get(0).getValues().size(); k++) {
                    float[] arr = new float[size];
                    for (int j = 0; j < size; j++) {
                        arr[j] = dataList.get(j).getValues().get(k);
                    }
                    yValues[0] = new ArrayList<>();
                    yValues[0].add(new BarEntry(k + 1, arr, ""));
                }
            } else {
                for (int index = 0; index < size; index++) {
                    BotBarChartDataModel model = payloadInner.getBarChartDataModels().get(index);
                    yValues[index] = new ArrayList<>();
                    for (int inner = 0; inner < model.getValues().size(); inner++) {
                        yValues[index].add(new BarEntry(inner + 1, model.getValues().get(inner), model.getDisplayValues().get(inner)));
                    }
                }
            }

            dataSet = new BarDataSet[size];

            if (barType == BAR_STACKED) {
                dataSet[0] = new BarDataSet(yValues[0], "");
                dataSet[0].setColors(getColors(size));
                dataSet[0].setStackLabels(labels);
                barDataSets.add(dataSet[0]);
            } else {
                for (int k = 0; k < size; k++) {
                    dataSet[k] = new BarDataSet(yValues[k], payloadInner.getBarChartDataModels().get(k).getTitle());
                    dataSet[k].setColor(ColorTemplate.MATERIAL_COLORS[k % 4]);
                    barDataSets.add(dataSet[k]);
                }
            }

            BarData data = new BarData(barDataSets);
            data.setValueFormatter(new BarChartDataFormatter());
            XAxis xAxis = mChart.getXAxis();
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis.setTextSize(8f);
            xAxis.setDrawGridLines(false);
            if (barType == BAR_STACKED) {
                xAxis.setLabelCount(4);
            }
            xAxis.setGranularity(barType == BAR_STACKED ? 1f : 0.5f); // only intervals of 1 day
            xAxis.setLabelCount(payloadInner.getxAxis().size());
            if (barType == BAR_HORIZONTAL) {
                xAxis.setDrawLabels(true);
                xAxis.setCenterAxisLabels(true);
            }
            xAxis.setLabelRotationAngle(-60f);

            ValueFormatter xAxisFormatter = new ValueFormatter() {
                @Override
                public String getFormattedValue(float v) {
                    if (((int) (v * 2) - 2) >= 0 && ((int) (v * 2) - 2) < payloadInner.getxAxis().size())
                        return payloadInner.getxAxis().get((int) (v * 2) - 2);
                    else
                        return "";
                }
            };

            xAxis.setValueFormatter(xAxisFormatter);

            Legend legend = mChart.getLegend();
            legend.setVerticalAlignment(barType == BAR_HORIZONTAL ? Legend.LegendVerticalAlignment.BOTTOM : Legend.LegendVerticalAlignment.TOP);
            legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
            legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
            legend.setDrawInside(false);
            legend.setForm(Legend.LegendForm.SQUARE);
            legend.setFormSize(9f);
            legend.setTextSize(11f);
            legend.setXEntrySpace(4f);

            mChart.setData(data);

            mChart.getBarData().setBarWidth(barWidth);
            if (barType == BAR_STACKED) {
                mChart.setFitBars(true);
                return;
            }

            // restrict the x-axis range
            mChart.getXAxis().setAxisMinimum(startYear);

            // barData.getGroupWith(...) is a helper that calculates the width each group needs based on the provided parameters
            mChart.getXAxis().setAxisMaximum(startYear + mChart.getBarData().getGroupWidth(groupSpace, barSpace) * groupCount);
            mChart.groupBars(startYear, groupSpace, barSpace);
        }
    }

    private int[] getColors(int stackSize) {
        // have as many colors as stack-values per entry
        int[] colors = new int[stackSize];
        System.arraycopy(ColorTemplate.MATERIAL_COLORS, 0, colors, 0, colors.length);
        return colors;
    }

    @Override
    public void onValueSelected(Entry var1, Highlight var2) {
    }

    @Override
    public void onNothingSelected() {
    }
}