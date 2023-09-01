package kore.botssdk.charts.components;

import android.content.Context;
import android.graphics.Canvas;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import java.lang.ref.WeakReference;

import kore.botssdk.charts.charts.Chart;
import kore.botssdk.charts.data.Entry;
import kore.botssdk.charts.highlight.Highlight;
import kore.botssdk.charts.utils.MPPointF;

public class MarkerView extends RelativeLayout implements IMarker {
    private MPPointF mOffset = new MPPointF();
    private final MPPointF mOffset2 = new MPPointF();
    private WeakReference<Chart> mWeakChart;

    public MarkerView(Context context, int layoutResource) {
        super(context);
        this.setupLayoutResource(layoutResource);
    }

    private void setupLayoutResource(int layoutResource) {
        View inflated = LayoutInflater.from(this.getContext()).inflate(layoutResource, this);
        inflated.setLayoutParams(new LayoutParams(-2, -2));
        inflated.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED), MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
        inflated.layout(0, 0, inflated.getMeasuredWidth(), inflated.getMeasuredHeight());
    }

    public void setOffset(MPPointF offset) {
        this.mOffset = offset;
        if (this.mOffset == null) {
            this.mOffset = new MPPointF();
        }

    }

    public void setOffset(float offsetX, float offsetY) {
        this.mOffset.x = offsetX;
        this.mOffset.y = offsetY;
    }

    public MPPointF getOffset() {
        return this.mOffset;
    }

    public void setChartView(Chart chart) {
        this.mWeakChart = new WeakReference(chart);
    }

    public Chart getChartView() {
        return this.mWeakChart == null ? null : this.mWeakChart.get();
    }

    public MPPointF getOffsetForDrawingAtPoint(float posX, float posY) {
        MPPointF offset = this.getOffset();
        this.mOffset2.x = offset.x;
        this.mOffset2.y = offset.y;
        Chart chart = this.getChartView();
        float width = (float)this.getWidth();
        float height = (float)this.getHeight();
        if (posX + this.mOffset2.x < 0.0F) {
            this.mOffset2.x = -posX;
        } else if (chart != null && posX + width + this.mOffset2.x > (float)chart.getWidth()) {
            this.mOffset2.x = (float)chart.getWidth() - posX - width;
        }

        if (posY + this.mOffset2.y < 0.0F) {
            this.mOffset2.y = -posY;
        } else if (chart != null && posY + height + this.mOffset2.y > (float)chart.getHeight()) {
            this.mOffset2.y = (float)chart.getHeight() - posY - height;
        }

        return this.mOffset2;
    }

    public void refreshContent(Entry e, Highlight highlight) {
        this.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED), MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
        this.layout(0, 0, this.getMeasuredWidth(), this.getMeasuredHeight());
    }

    public void draw(Canvas canvas, float posX, float posY) {
        MPPointF offset = this.getOffsetForDrawingAtPoint(posX, posY);
        int saveId = canvas.save();
        canvas.translate(posX + offset.x, posY + offset.y);
        this.draw(canvas);
        canvas.restoreToCount(saveId);
    }
}
