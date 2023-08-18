package kore.botssdk.charts.data;

import android.graphics.drawable.Drawable;

import kore.botssdk.charts.highlight.Range;

public class BarEntry extends Entry {
    private float[] mYVals;
    private Range[] mRanges;
    private float mNegativeSum;
    private float mPositiveSum;

    public BarEntry(float x, float y) {
        super(x, y);
    }

    public BarEntry(float x, float y, Object data) {
        super(x, y, data);
    }

    public BarEntry(float x, float y, Drawable icon) {
        super(x, y, icon);
    }

    public BarEntry(float x, float y, Drawable icon, Object data) {
        super(x, y, icon, data);
    }

    public BarEntry(float x, float[] vals) {
        super(x, calcSum(vals));
        this.mYVals = vals;
        this.calcPosNegSum();
        this.calcRanges();
    }

    public BarEntry(float x, float[] vals, Object data) {
        super(x, calcSum(vals), data);
        this.mYVals = vals;
        this.calcPosNegSum();
        this.calcRanges();
    }

    public BarEntry(float x, float[] vals, Drawable icon) {
        super(x, calcSum(vals), icon);
        this.mYVals = vals;
        this.calcPosNegSum();
        this.calcRanges();
    }

    public BarEntry(float x, float[] vals, Drawable icon, Object data) {
        super(x, calcSum(vals), icon, data);
        this.mYVals = vals;
        this.calcPosNegSum();
        this.calcRanges();
    }

    public kore.botssdk.charts.data.BarEntry copy() {
        kore.botssdk.charts.data.BarEntry copied = new kore.botssdk.charts.data.BarEntry(this.getX(), this.getY(), this.getData());
        copied.setVals(this.mYVals);
        return copied;
    }

    public float[] getYVals() {
        return this.mYVals;
    }

    public void setVals(float[] vals) {
        this.setY(calcSum(vals));
        this.mYVals = vals;
        this.calcPosNegSum();
        this.calcRanges();
    }

    public float getY() {
        return super.getY();
    }

    public Range[] getRanges() {
        return this.mRanges;
    }

    public boolean isStacked() {
        return this.mYVals != null;
    }

    /** @deprecated */
    @Deprecated
    public float getBelowSum(int stackIndex) {
        return this.getSumBelow(stackIndex);
    }

    public float getSumBelow(int stackIndex) {
        if (this.mYVals == null) {
            return 0.0F;
        } else {
            float remainder = 0.0F;

            for(int index = this.mYVals.length - 1; index > stackIndex && index >= 0; --index) {
                remainder += this.mYVals[index];
            }

            return remainder;
        }
    }

    public float getPositiveSum() {
        return this.mPositiveSum;
    }

    public float getNegativeSum() {
        return this.mNegativeSum;
    }

    private void calcPosNegSum() {
        if (this.mYVals == null) {
            this.mNegativeSum = 0.0F;
            this.mPositiveSum = 0.0F;
        } else {
            float sumNeg = 0.0F;
            float sumPos = 0.0F;
            float[] var3 = this.mYVals;
            int var4 = var3.length;

            for(int var5 = 0; var5 < var4; ++var5) {
                float f = var3[var5];
                if (f <= 0.0F) {
                    sumNeg += Math.abs(f);
                } else {
                    sumPos += f;
                }
            }

            this.mNegativeSum = sumNeg;
            this.mPositiveSum = sumPos;
        }
    }

    private static float calcSum(float[] vals) {
        if (vals == null) {
            return 0.0F;
        } else {
            float sum = 0.0F;
            float[] var2 = vals;
            int var3 = vals.length;

            for(int var4 = 0; var4 < var3; ++var4) {
                float f = var2[var4];
                sum += f;
            }

            return sum;
        }
    }

    protected void calcRanges() {
        float[] values = this.getYVals();
        if (values != null && values.length != 0) {
            this.mRanges = new Range[values.length];
            float negRemain = -this.getNegativeSum();
            float posRemain = 0.0F;

            for(int i = 0; i < this.mRanges.length; ++i) {
                float value = values[i];
                if (value < 0.0F) {
                    this.mRanges[i] = new Range(negRemain, negRemain - value);
                    negRemain -= value;
                } else {
                    this.mRanges[i] = new Range(posRemain, posRemain + value);
                    posRemain += value;
                }
            }

        }
    }
}
