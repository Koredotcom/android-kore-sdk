package kore.botssdk.charts.formatter;

import java.util.Collection;

public class IndexAxisValueFormatter extends ValueFormatter {
    private String[] mValues = new String[0];
    private int mValueCount = 0;

    public IndexAxisValueFormatter() {
    }

    public IndexAxisValueFormatter(String[] values) {
        if (values != null) {
            this.setValues(values);
        }

    }

    public IndexAxisValueFormatter(Collection<String> values) {
        if (values != null) {
            this.setValues(values.toArray(new String[values.size()]));
        }

    }

    public String getFormattedValue(float value) {
        int index = Math.round(value);
        return index >= 0 && index < this.mValueCount && index == (int)value ? this.mValues[index] : "";
    }

    public String[] getValues() {
        return this.mValues;
    }

    public void setValues(String[] values) {
        if (values == null) {
            values = new String[0];
        }

        this.mValues = values;
        this.mValueCount = values.length;
    }
}
