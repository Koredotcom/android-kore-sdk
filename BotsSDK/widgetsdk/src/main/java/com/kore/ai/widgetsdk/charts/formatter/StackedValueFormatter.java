package com.kore.ai.widgetsdk.charts.formatter;

import com.kore.ai.widgetsdk.charts.data.BarEntry;

import java.text.DecimalFormat;

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
