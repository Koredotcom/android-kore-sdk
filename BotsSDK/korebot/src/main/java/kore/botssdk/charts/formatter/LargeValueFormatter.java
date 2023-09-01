package kore.botssdk.charts.formatter;

import java.text.DecimalFormat;

public class LargeValueFormatter extends ValueFormatter {
    private String[] mSuffix;
    private int mMaxLength;
    private final DecimalFormat mFormat;
    private String mText;

    public LargeValueFormatter() {
        this.mSuffix = new String[]{"", "k", "m", "b", "t"};
        this.mMaxLength = 5;
        this.mText = "";
        this.mFormat = new DecimalFormat("###E00");
    }

    public LargeValueFormatter(String appendix) {
        this();
        this.mText = appendix;
    }

    public String getFormattedValue(float value) {
        return this.makePretty(value) + this.mText;
    }

    public void setAppendix(String appendix) {
        this.mText = appendix;
    }

    public void setSuffix(String[] suffix) {
        this.mSuffix = suffix;
    }

    public void setMaxLength(int maxLength) {
        this.mMaxLength = maxLength;
    }

    private String makePretty(double number) {
        String r = this.mFormat.format(number);
        int numericValue1 = Character.getNumericValue(r.charAt(r.length() - 1));
        int numericValue2 = Character.getNumericValue(r.charAt(r.length() - 2));
        int combined = Integer.valueOf(numericValue2 + "" + numericValue1);

        for(r = r.replaceAll("E[0-9][0-9]", this.mSuffix[combined / 3]); r.length() > this.mMaxLength || r.matches("[0-9]+\\.[a-z]"); r = r.substring(0, r.length() - 2) + r.substring(r.length() - 1)) {
        }

        return r;
    }

    public int getDecimalDigits() {
        return 0;
    }
}
