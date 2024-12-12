package com.kore.ai.widgetsdk.formatters;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.DecimalFormat;

public class BarChartDataFormatter extends ValueFormatter implements IAxisValueFormatter {
    private static String[] SUFFIX = new String[]{"", "k", "m", "b", "t"};
    private final DecimalFormat mFormat;
    private String mText;

    public BarChartDataFormatter() {
        this.mText = "";
        this.mFormat = new DecimalFormat("###E00");
    }

    public BarChartDataFormatter(String appendix) {
        this();
        this.mText = appendix;
    }

    public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
        return this.makePretty(value) + this.mText;
    }

    public String getFormattedValue(float value, AxisBase axis) {
        return "HI";
    }

    private String makePretty(double number) {
        String r = this.mFormat.format(number);
        int numericValue1 = Character.getNumericValue(r.charAt(r.length() - 1));
        int numericValue2 = Character.getNumericValue(r.charAt(r.length() - 2));
        int combined = Integer.valueOf(numericValue2 + "" + numericValue1).intValue();

        return r;
    }

}
