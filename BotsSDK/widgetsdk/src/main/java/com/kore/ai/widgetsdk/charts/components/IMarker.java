package com.kore.ai.widgetsdk.charts.components;
import android.graphics.Canvas;

import com.kore.ai.widgetsdk.charts.data.Entry;
import com.kore.ai.widgetsdk.charts.highlight.Highlight;
import com.kore.ai.widgetsdk.charts.utils.MPPointF;

public interface IMarker {
    MPPointF getOffset();

    MPPointF getOffsetForDrawingAtPoint(float var1, float var2);

    void refreshContent(Entry var1, Highlight var2);

    void draw(Canvas var1, float var2, float var3);
}
