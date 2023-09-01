package com.kore.ai.widgetsdk.charts.renderer;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.kore.ai.widgetsdk.charts.animation.ChartAnimator;
import com.kore.ai.widgetsdk.charts.highlight.Highlight;
import com.kore.ai.widgetsdk.charts.interfaces.dataprovider.ChartInterface;
import com.kore.ai.widgetsdk.charts.interfaces.datasets.IDataSet;
import com.kore.ai.widgetsdk.charts.utils.Utils;
import com.kore.ai.widgetsdk.charts.utils.ViewPortHandler;

public abstract class DataRenderer extends Renderer {
    protected final ChartAnimator mAnimator;
    protected final Paint mRenderPaint;
    protected Paint mHighlightPaint;
    protected final Paint mDrawPaint;
    protected final Paint mValuePaint;

    public DataRenderer(ChartAnimator animator, ViewPortHandler viewPortHandler) {
        super(viewPortHandler);
        this.mAnimator = animator;
        this.mRenderPaint = new Paint(1);
        this.mRenderPaint.setStyle(Paint.Style.FILL);
        this.mDrawPaint = new Paint(4);
        this.mValuePaint = new Paint(1);
        this.mValuePaint.setColor(Color.rgb(63, 63, 63));
        this.mValuePaint.setTextAlign(Paint.Align.CENTER);
        this.mValuePaint.setTextSize(Utils.convertDpToPixel(9.0F));
        this.mHighlightPaint = new Paint(1);
        this.mHighlightPaint.setStyle(Paint.Style.STROKE);
        this.mHighlightPaint.setStrokeWidth(2.0F);
        this.mHighlightPaint.setColor(Color.rgb(255, 187, 115));
    }

    protected boolean isDrawingValuesAllowed(ChartInterface chart) {
        return (float)chart.getData().getEntryCount() < (float)chart.getMaxVisibleCount() * this.mViewPortHandler.getScaleX();
    }

    public Paint getPaintValues() {
        return this.mValuePaint;
    }

    public Paint getPaintHighlight() {
        return this.mHighlightPaint;
    }

    public Paint getPaintRender() {
        return this.mRenderPaint;
    }

    protected void applyValueTextStyle(IDataSet set) {
        this.mValuePaint.setTypeface(set.getValueTypeface());
        this.mValuePaint.setTextSize(set.getValueTextSize());
    }

    public abstract void initBuffers();

    public abstract void drawData(Canvas var1);

    public abstract void drawValues(Canvas var1);

    public abstract void drawValue(Canvas var1, String var2, float var3, float var4, int var5);

    public abstract void drawExtras(Canvas var1);

    public abstract void drawHighlighted(Canvas var1, Highlight[] var2);
}
