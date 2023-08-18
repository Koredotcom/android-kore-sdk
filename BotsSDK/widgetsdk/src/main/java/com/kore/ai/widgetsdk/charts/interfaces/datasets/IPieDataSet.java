package com.kore.ai.widgetsdk.charts.interfaces.datasets;

import com.kore.ai.widgetsdk.charts.data.PieDataSet;
import com.kore.ai.widgetsdk.charts.data.PieEntry;

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
