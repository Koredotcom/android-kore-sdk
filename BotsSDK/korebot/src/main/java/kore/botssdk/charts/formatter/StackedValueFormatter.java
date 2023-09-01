package kore.botssdk.charts.formatter;

import java.text.DecimalFormat;

import kore.botssdk.charts.data.BarEntry;

public class StackedValueFormatter extends ValueFormatter {
    private final boolean mDrawWholeStack;
    private final String mSuffix;
    private final DecimalFormat mFormat;

    public StackedValueFormatter(boolean drawWholeStack, String suffix, int decimals) {
        this.mDrawWholeStack = drawWholeStack;
        this.mSuffix = suffix;
        StringBuffer b = new StringBuffer();

        for(int i = 0; i < decimals; ++i) {
            if (i == 0) {
                b.append(".");
            }

            b.append("0");
        }

        this.mFormat = new DecimalFormat("###,###,###,##0" + b);
    }

    public String getBarStackedLabel(float value, BarEntry entry) {
        if (!this.mDrawWholeStack) {
            float[] vals = entry.getYVals();
            if (vals != null) {
                if (vals[vals.length - 1] == value) {
                    return this.mFormat.format(entry.getY()) + this.mSuffix;
                }

                return "";
            }
        }

        return this.mFormat.format(value) + this.mSuffix;
    }
}
