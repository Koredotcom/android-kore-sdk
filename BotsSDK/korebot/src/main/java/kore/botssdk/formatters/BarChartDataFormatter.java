package kore.botssdk.formatters;

import java.text.DecimalFormat;

import kore.botssdk.charts.components.AxisBase;
import kore.botssdk.charts.data.Entry;
import kore.botssdk.charts.formatter.IAxisValueFormatter;
import kore.botssdk.charts.formatter.ValueFormatter;
import kore.botssdk.charts.utils.ViewPortHandler;

/**
 * Created by Ramachandra Pradeep on 10-May-18.
 */

public class BarChartDataFormatter extends ValueFormatter implements IAxisValueFormatter {
    private static String[] SUFFIX = new String[]{"", "k", "m", "b", "t"};
//    private static final int MAX_LENGTH = 5;
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

    public void setAppendix(String appendix) {
        this.mText = appendix;
    }

    public void setSuffix(String[] suff) {
        SUFFIX = suff;
    }

    private String makePretty(double number) {
        String r = this.mFormat.format(number);
        int numericValue1 = Character.getNumericValue(r.charAt(r.length() - 1));
        int numericValue2 = Character.getNumericValue(r.charAt(r.length() - 2));
        int combined = Integer.valueOf(numericValue2 + "" + numericValue1).intValue();

        for(r = r.replaceAll("E[0-9][0-9]", SUFFIX[combined / 3]); r.length() > 5 || r.matches("[0-9]+\\.[a-z]"); r = r.substring(0, r.length() - 2) + r.substring(r.length() - 1)) {
        }

        return r;
    }

    public int getDecimalDigits() {
        return 0;
    }
}
