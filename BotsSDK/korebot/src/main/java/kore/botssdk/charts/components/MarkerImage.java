package kore.botssdk.charts.components;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;

import java.lang.ref.WeakReference;

import kore.botssdk.charts.charts.Chart;
import kore.botssdk.charts.data.Entry;
import kore.botssdk.charts.highlight.Highlight;
import kore.botssdk.charts.utils.FSize;
import kore.botssdk.charts.utils.MPPointF;

public class MarkerImage implements IMarker {
    private final Context mContext;
    private final Drawable mDrawable;
    private MPPointF mOffset = new MPPointF();
    private final MPPointF mOffset2 = new MPPointF();
    private WeakReference<Chart> mWeakChart;
    private FSize mSize = new FSize();
    private final Rect mDrawableBoundsCache = new Rect();

    public MarkerImage(Context context, int drawableResourceId) {
        this.mContext = context;
        if (Build.VERSION.SDK_INT >= 21) {
            this.mDrawable = this.mContext.getResources().getDrawable(drawableResourceId, (Resources.Theme)null);
        } else {
            this.mDrawable = this.mContext.getResources().getDrawable(drawableResourceId);
        }

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

    public void setSize(FSize size) {
        this.mSize = size;
        if (this.mSize == null) {
            this.mSize = new FSize();
        }

    }

    public FSize getSize() {
        return this.mSize;
    }

    public void setChartView(Chart chart) {
        this.mWeakChart = new WeakReference(chart);
    }

    public Chart getChartView() {
        return this.mWeakChart == null ? null : (Chart)this.mWeakChart.get();
    }

    public MPPointF getOffsetForDrawingAtPoint(float posX, float posY) {
        MPPointF offset = this.getOffset();
        this.mOffset2.x = offset.x;
        this.mOffset2.y = offset.y;
        Chart chart = this.getChartView();
        float width = this.mSize.width;
        float height = this.mSize.height;
        if (width == 0.0F && this.mDrawable != null) {
            width = (float)this.mDrawable.getIntrinsicWidth();
        }

        if (height == 0.0F && this.mDrawable != null) {
            height = (float)this.mDrawable.getIntrinsicHeight();
        }

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
    }

    public void draw(Canvas canvas, float posX, float posY) {
        if (this.mDrawable != null) {
            MPPointF offset = this.getOffsetForDrawingAtPoint(posX, posY);
            float width = this.mSize.width;
            float height = this.mSize.height;
            if (width == 0.0F) {
                width = (float)this.mDrawable.getIntrinsicWidth();
            }

            if (height == 0.0F) {
                height = (float)this.mDrawable.getIntrinsicHeight();
            }

            this.mDrawable.copyBounds(this.mDrawableBoundsCache);
            this.mDrawable.setBounds(this.mDrawableBoundsCache.left, this.mDrawableBoundsCache.top, this.mDrawableBoundsCache.left + (int)width, this.mDrawableBoundsCache.top + (int)height);
            int saveId = canvas.save();
            canvas.translate(posX + offset.x, posY + offset.y);
            this.mDrawable.draw(canvas);
            canvas.restoreToCount(saveId);
            this.mDrawable.setBounds(this.mDrawableBoundsCache);
        }
    }
}
