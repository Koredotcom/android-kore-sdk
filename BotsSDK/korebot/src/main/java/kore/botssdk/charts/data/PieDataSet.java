package kore.botssdk.charts.data;

import java.util.ArrayList;
import java.util.List;

import kore.botssdk.charts.interfaces.datasets.IPieDataSet;
import kore.botssdk.charts.utils.Utils;

public class PieDataSet extends kore.botssdk.charts.data.DataSet<PieEntry> implements IPieDataSet {
    private float mSliceSpace = 0.0F;
    private boolean mAutomaticallyDisableSliceSpacing;
    private float mShift = 18.0F;
    private kore.botssdk.charts.data.PieDataSet.ValuePosition mXValuePosition;
    private kore.botssdk.charts.data.PieDataSet.ValuePosition mYValuePosition;
    private boolean mUsingSliceColorAsValueLineColor;
    private int mValueLineColor;
    private float mValueLineWidth;
    private float mValueLinePart1OffsetPercentage;
    private float mValueLinePart1Length;
    private float mValueLinePart2Length;
    private boolean mValueLineVariableLength;

    public PieDataSet(List<PieEntry> yVals, String label) {
        super(yVals, label);
        this.mXValuePosition = kore.botssdk.charts.data.PieDataSet.ValuePosition.INSIDE_SLICE;
        this.mYValuePosition = kore.botssdk.charts.data.PieDataSet.ValuePosition.INSIDE_SLICE;
        this.mUsingSliceColorAsValueLineColor = false;
        this.mValueLineColor = -16777216;
        this.mValueLineWidth = 1.0F;
        this.mValueLinePart1OffsetPercentage = 75.0F;
        this.mValueLinePart1Length = 0.3F;
        this.mValueLinePart2Length = 0.4F;
        this.mValueLineVariableLength = true;
    }

    public DataSet<PieEntry> copy() {
        List<PieEntry> entries = new ArrayList();

        for(int i = 0; i < this.mValues.size(); ++i) {
            entries.add(this.mValues.get(i).copy());
        }

        kore.botssdk.charts.data.PieDataSet copied = new kore.botssdk.charts.data.PieDataSet(entries, this.getLabel());
        this.copy(copied);
        return copied;
    }

    protected void copy(kore.botssdk.charts.data.PieDataSet pieDataSet) {
        super.copy(pieDataSet);
    }

    protected void calcMinMax(PieEntry e) {
        if (e != null) {
            this.calcMinMaxY(e);
        }
    }

    public void setSliceSpace(float spaceDp) {
        if (spaceDp > 20.0F) {
            spaceDp = 20.0F;
        }

        if (spaceDp < 0.0F) {
            spaceDp = 0.0F;
        }

        this.mSliceSpace = Utils.convertDpToPixel(spaceDp);
    }

    public float getSliceSpace() {
        return this.mSliceSpace;
    }

    public void setAutomaticallyDisableSliceSpacing(boolean autoDisable) {
        this.mAutomaticallyDisableSliceSpacing = autoDisable;
    }

    public boolean isAutomaticallyDisableSliceSpacingEnabled() {
        return this.mAutomaticallyDisableSliceSpacing;
    }

    public void setSelectionShift(float shift) {
        this.mShift = Utils.convertDpToPixel(shift);
    }

    public float getSelectionShift() {
        return this.mShift;
    }

    public kore.botssdk.charts.data.PieDataSet.ValuePosition getXValuePosition() {
        return this.mXValuePosition;
    }

    public void setXValuePosition(kore.botssdk.charts.data.PieDataSet.ValuePosition xValuePosition) {
        this.mXValuePosition = xValuePosition;
    }

    public kore.botssdk.charts.data.PieDataSet.ValuePosition getYValuePosition() {
        return this.mYValuePosition;
    }

    public void setYValuePosition(kore.botssdk.charts.data.PieDataSet.ValuePosition yValuePosition) {
        this.mYValuePosition = yValuePosition;
    }

    public boolean isUsingSliceColorAsValueLineColor() {
        return this.mUsingSliceColorAsValueLineColor;
    }

    public void setUsingSliceColorAsValueLineColor(boolean usingSliceColorAsValueLineColor) {
        this.mUsingSliceColorAsValueLineColor = usingSliceColorAsValueLineColor;
    }

    public int getValueLineColor() {
        return this.mValueLineColor;
    }

    public void setValueLineColor(int valueLineColor) {
        this.mValueLineColor = valueLineColor;
    }

    public float getValueLineWidth() {
        return this.mValueLineWidth;
    }

    public void setValueLineWidth(float valueLineWidth) {
        this.mValueLineWidth = valueLineWidth;
    }

    public float getValueLinePart1OffsetPercentage() {
        return this.mValueLinePart1OffsetPercentage;
    }

    public void setValueLinePart1OffsetPercentage(float valueLinePart1OffsetPercentage) {
        this.mValueLinePart1OffsetPercentage = valueLinePart1OffsetPercentage;
    }

    public float getValueLinePart1Length() {
        return this.mValueLinePart1Length;
    }

    public void setValueLinePart1Length(float valueLinePart1Length) {
        this.mValueLinePart1Length = valueLinePart1Length;
    }

    public float getValueLinePart2Length() {
        return this.mValueLinePart2Length;
    }

    public void setValueLinePart2Length(float valueLinePart2Length) {
        this.mValueLinePart2Length = valueLinePart2Length;
    }

    public boolean isValueLineVariableLength() {
        return this.mValueLineVariableLength;
    }

    public void setValueLineVariableLength(boolean valueLineVariableLength) {
        this.mValueLineVariableLength = valueLineVariableLength;
    }

    public enum ValuePosition {
        INSIDE_SLICE,
        OUTSIDE_SLICE;

        ValuePosition() {
        }
    }
}
