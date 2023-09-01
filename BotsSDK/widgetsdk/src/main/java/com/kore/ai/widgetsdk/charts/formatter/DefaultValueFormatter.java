package com.kore.ai.widgetsdk.charts.formatter;

import java.text.DecimalFormat;

public class DefaultValueFormatter extends ValueFormatter {
    protected DecimalFormat mFormat;

    public DefaultValueFormatter(int digits) {
        StringBuffer b = new StringBuffer();

        for(int i = 0; i < digits; ++i) {
            if (i == 0) {
                b.append(".");
            }

            b.append("0");
        }

        this.mFormat = new DecimalFormat("###,###,###,##0" + b);
    }

    public String getFormattedValue(float value) {
        return this.mFormat.format(value);
    }

    public void setup(int digits)
    {
        StringBuffer b = new StringBuffer();

        for(int i = 0; i < digits; ++i) {
            if (i == 0) {
                b.append(".");
            }

            b.append("0");
        }

        this.mFormat = new DecimalFormat("###,###,###,##0" + b);
    }
}
