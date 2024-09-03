package kore.botssdk.viewholders;

import android.graphics.Color;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.LinearLayoutCompat;

import java.util.ArrayList;

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
import kore.botssdk.models.BaseBotMessage;
import kore.botssdk.models.BotLineChartDataModel;
import kore.botssdk.models.PayloadInner;
import kore.botssdk.utils.StringUtils;
import kore.botssdk.view.CustomMarkerView;

public class LineChartTemplateHolder extends BaseViewHolder implements OnChartGestureListener {
    private final LineChart lineChart;

    public static LineChartTemplateHolder getInstance(ViewGroup parent) {
        return new LineChartTemplateHolder(createView(R.layout.template_line_chart, parent));
    }

    private LineChartTemplateHolder(@NonNull View itemView) {
        super(itemView, itemView.getContext());
        LinearLayoutCompat layoutBubble = itemView.findViewById(R.id.layoutBubble);
        initBubbleText(layoutBubble, false);
        lineChart = itemView.findViewById(R.id.lineChart);
        lineChart.getDescription().setEnabled(false);
        lineChart.setTouchEnabled(true);
        CustomMarkerView markerView = new CustomMarkerView(itemView.getContext(), R.layout.marker_content);
        lineChart.setMarker(markerView);
        lineChart.setOnChartGestureListener(this);
    }

    @Override
    public void bind(BaseBotMessage baseBotMessage) {
        PayloadInner payloadInner = getPayloadInner(baseBotMessage);
        if (payloadInner == null) return;
        setResponseText(itemView.findViewById(R.id.layoutBubble), payloadInner.getText());
        if (payloadInner.getText() != null && !StringUtils.isNullOrEmptyWithTrim(payloadInner.getText())) {
            Description desc = new Description();
            desc.setText(payloadInner.getText());
            lineChart.setDescription(desc);
        }

        ArrayList<BotLineChartDataModel> lineList = payloadInner.getLineChartDataModels();

        ArrayList<ILineDataSet> sets = new ArrayList<ILineDataSet>(lineList.size());
        LineDataSet[] dataSet = new LineDataSet[lineList.size()];

        for (int baseIndex = 0; baseIndex < lineList.size(); baseIndex++) {
            BotLineChartDataModel model = lineList.get(baseIndex);

            ArrayList<Entry> entry = new ArrayList<Entry>();
            for (int index = 0; index < model.getValues().size(); index++) {
                entry.add(new Entry(index, Math.round(model.getValues().get(index))));
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
        lineChart.setData(lineData);
        lineChart.getXAxis().setTextSize(8);
        lineChart.getXAxis().setDrawAxisLine(true);
        lineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        lineChart.setDrawGridBackground(false);
        lineChart.getXAxis().setDrawGridLines(false); // disable grid lines for the XAxis
        lineChart.getAxisLeft().setDrawGridLines(false); // disable grid lines for the left YAxis
        lineChart.getAxisRight().setDrawGridLines(false);
        lineChart.getAxisRight().setEnabled(false);// disable grid lines for the right YAxis

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return payloadInner.getxAxis().get((int) value);
            }
        });
        xAxis.setLabelRotationAngle(-60f);
        xAxis.setLabelCount(payloadInner.getxAxis().size(), true);

        Legend l = lineChart.getLegend();

        // modify the legend ...
        l.setForm(Legend.LegendForm.LINE);
        l.setTextSize(11f);
        l.setTextColor(Color.BLACK);
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
    }

    @Override
    public void onChartGestureStart(MotionEvent var1, ChartTouchListener.ChartGesture var2) {
    }

    @Override
    public void onChartGestureEnd(MotionEvent var1, ChartTouchListener.ChartGesture chartGesture) {
        if (chartGesture != ChartTouchListener.ChartGesture.SINGLE_TAP)
            lineChart.highlightValues(null);
    }

    @Override
    public void onChartLongPressed(MotionEvent var1) {
    }

    @Override
    public void onChartDoubleTapped(MotionEvent var1) {
    }

    @Override
    public void onChartSingleTapped(MotionEvent var1) {
    }

    @Override
    public void onChartFling(MotionEvent var1, MotionEvent var2, float var3, float var4) {
    }

    @Override
    public void onChartScale(MotionEvent var1, float var2, float var3) {
    }

    @Override
    public void onChartTranslate(MotionEvent var1, float var2, float var3) {
    }
}
