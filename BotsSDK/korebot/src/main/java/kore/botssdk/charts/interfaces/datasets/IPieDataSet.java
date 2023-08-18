package kore.botssdk.charts.interfaces.datasets;

import kore.botssdk.charts.data.PieDataSet;
import kore.botssdk.charts.data.PieEntry;

public interface IPieDataSet extends IDataSet<PieEntry> {
    float getSliceSpace();

    boolean isAutomaticallyDisableSliceSpacingEnabled();

    float getSelectionShift();

    PieDataSet.ValuePosition getXValuePosition();

    PieDataSet.ValuePosition getYValuePosition();

    boolean isUsingSliceColorAsValueLineColor();

    int getValueLineColor();

    float getValueLineWidth();

    float getValueLinePart1OffsetPercentage();

    float getValueLinePart1Length();

    float getValueLinePart2Length();

    boolean isValueLineVariableLength();
}
