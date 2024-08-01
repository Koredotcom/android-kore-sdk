package kore.botssdk.viewholders;

import static kore.botssdk.view.viewUtils.DimensionUtil.dp1;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.LinearLayoutCompat;

import java.util.ArrayList;

import kore.botssdk.R;
import kore.botssdk.charts.charts.PieChart;
import kore.botssdk.charts.components.Description;
import kore.botssdk.charts.components.Legend;
import kore.botssdk.charts.components.LegendEntry;
import kore.botssdk.charts.data.Entry;
import kore.botssdk.charts.data.PieData;
import kore.botssdk.charts.data.PieDataSet;
import kore.botssdk.charts.data.PieEntry;
import kore.botssdk.charts.formatter.PercentFormatter;
import kore.botssdk.charts.highlight.Highlight;
import kore.botssdk.charts.listener.OnChartValueSelectedListener;
import kore.botssdk.models.BaseBotMessage;
import kore.botssdk.models.BotPieChartElementModel;
import kore.botssdk.models.PayloadInner;
import kore.botssdk.utils.LogUtils;
import kore.botssdk.view.CustomMarkerView;

public class PieChartTemplateHolder extends BaseViewHolderNew {
    private final PieChart pieChart;
    private final String PIE_TYPE_DONUT = "donut";

    public static PieChartTemplateHolder getInstance(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.template_pie_chart, parent, false);
        return new PieChartTemplateHolder(view);
    }

    private PieChartTemplateHolder(@NonNull View itemView) {
        super(itemView, itemView.getContext());
        LinearLayoutCompat layoutBubble = itemView.findViewById(R.id.layoutBubble);
        LinearLayoutCompat.LayoutParams params = (LinearLayoutCompat.LayoutParams) layoutBubble.getLayoutParams();
        params.bottomMargin = (int) (10 * dp1);
        initBubbleText(layoutBubble, false);
        pieChart = itemView.findViewById(R.id.pieChart);
    }

    @Override
    public void bind(BaseBotMessage baseBotMessage) {
        PayloadInner payloadInner = getPayloadInner(baseBotMessage);
        if (payloadInner == null || (payloadInner.getPieChartElements() == null || payloadInner.getPieChartElements().isEmpty())) return;
        ArrayList<BotPieChartElementModel> elementModels = payloadInner.getPieChartElements();
        ArrayList<String> xValues = new ArrayList<>(elementModels.size());
        ArrayList<PieEntry> yValues = new ArrayList<>(elementModels.size());
        ArrayList<String> labels = new ArrayList<>(elementModels.size());

        for (int i = 0; i < elementModels.size(); i++) {
            xValues.add(elementModels.get(i).getTitle());
            yValues.add(new PieEntry((float) elementModels.get(i).getValue(), " "));
            labels.add(elementModels.get(i).getTitle() + " " + elementModels.get(i).getValue());
        }
        String pieType = payloadInner.getPie_type();
        float holeRadius;
        float transparentCircleRadius;
        if (pieType != null && pieType.equals(PIE_TYPE_DONUT)) {
            holeRadius = 56f;
            transparentCircleRadius = 61f;
        } else {
            holeRadius = 0f;
            transparentCircleRadius = 0f;
        }
        pieChart.setUsePercentValues(true);
        pieChart.setDrawHoleEnabled(true);
        Description sDesc = new Description();
        sDesc.setText("");
        pieChart.setDescription(sDesc);
        pieChart.setHoleRadius(holeRadius);
        pieChart.setTransparentCircleRadius(transparentCircleRadius);

        pieChart.setRotationAngle(-30);
        pieChart.setRotationEnabled(true);

        pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                if (e == null) return;
                LogUtils.i("VAL SELECTED", "Value: " + e.getY() + ", index: " + h.getX() + ", DataSet index: " + h.getDataSetIndex());
            }

            @Override
            public void onNothingSelected() {
                LogUtils.i("PieChart", "nothing selected");
            }
        });

        addData(xValues, yValues, payloadInner.getPie_type());

        ArrayList<LegendEntry> arrLegendEntries = new ArrayList<>();
        String[] colorsArray = itemView.getContext().getResources().getStringArray(R.array.color_set);
        for (int i = 0; i < yValues.size(); i++) {
            LegendEntry legendEntryA = new LegendEntry();
            legendEntryA.label = labels.get(i);
            legendEntryA.formColor = Color.parseColor(colorsArray[i]);
            arrLegendEntries.add(legendEntryA);
        }

        Legend l = pieChart.getLegend();
        l.setCustom(arrLegendEntries);
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);

        CustomMarkerView mv = new CustomMarkerView(itemView.getContext(), R.layout.marker_content);
        pieChart.setMarkerView(mv);

        // entry label styling
        pieChart.setEntryLabelColor(Color.WHITE);
        pieChart.setEntryLabelTextSize(12f);
    }


    private void addData(ArrayList<String> xValues, ArrayList<PieEntry> yValues, String pieType) {
        PieDataSet dataSet = new PieDataSet(yValues, "");
        dataSet.setSliceSpace(3);
        dataSet.setSelectionShift(5);

        ArrayList<Integer> colors = new ArrayList<Integer>();
        String[] colorsArray = itemView.getResources().getStringArray(R.array.color_set);
        for (String color : colorsArray) {
            colors.add(Color.parseColor(color));
        }

        dataSet.setColors(colors);

        if (pieType != null && pieType.equals(PIE_TYPE_DONUT)) {
            dataSet.setValueLinePart1OffsetPercentage(80.f);
            dataSet.setValueLinePart1Length(0.2f);
            dataSet.setValueLinePart2Length(0.4f);
            dataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        }

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.WHITE);

        pieChart.setData(data);
        pieChart.highlightValues(null);
        pieChart.invalidate();
    }
}
