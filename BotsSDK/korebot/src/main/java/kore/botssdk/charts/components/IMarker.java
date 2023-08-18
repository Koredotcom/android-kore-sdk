package kore.botssdk.charts.components;
import android.graphics.Canvas;

import kore.botssdk.charts.data.Entry;
import kore.botssdk.charts.highlight.Highlight;
import kore.botssdk.charts.utils.MPPointF;

public interface IMarker {
    MPPointF getOffset();

    MPPointF getOffsetForDrawingAtPoint(float var1, float var2);

    void refreshContent(Entry var1, Highlight var2);

    void draw(Canvas var1, float var2, float var3);
}
