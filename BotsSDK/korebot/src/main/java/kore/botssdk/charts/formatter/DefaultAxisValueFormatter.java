package kore.botssdk.charts.formatter;

import java.text.DecimalFormat;

public class DefaultAxisValueFormatter extends ValueFormatter {
    protected final DecimalFormat mFormat;
    protected final int digits;

    public DefaultAxisValueFormatter(int digits) {
        this.digits = digits;
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

    public int getDecimalDigits() {
        return this.digits;
    }
}
