package kore.botssdk.charts.renderer;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;

import java.util.List;

import kore.botssdk.charts.animation.ChartAnimator;
import kore.botssdk.charts.buffer.BarBuffer;
import kore.botssdk.charts.data.BarData;
import kore.botssdk.charts.data.BarEntry;
import kore.botssdk.charts.formatter.ValueFormatter;
import kore.botssdk.charts.highlight.Highlight;
import kore.botssdk.charts.highlight.Range;
import kore.botssdk.charts.interfaces.dataprovider.BarDataProvider;
import kore.botssdk.charts.interfaces.datasets.IBarDataSet;
import kore.botssdk.charts.model.GradientColor;
import kore.botssdk.charts.utils.MPPointF;
import kore.botssdk.charts.utils.Transformer;
import kore.botssdk.charts.utils.Utils;
import kore.botssdk.charts.utils.ViewPortHandler;

public class BarChartRenderer extends BarLineScatterCandleBubbleRenderer {
    protected final BarDataProvider mChart;
    protected final RectF mBarRect = new RectF();
    protected BarBuffer[] mBarBuffers;
    protected final Paint mShadowPaint;
    protected final Paint mBarBorderPaint;
    private final RectF mBarShadowRectBuffer = new RectF();

    public BarChartRenderer(BarDataProvider chart, ChartAnimator animator, ViewPortHandler viewPortHandler) {
        super(animator, viewPortHandler);
        this.mChart = chart;
        this.mHighlightPaint = new Paint(1);
        this.mHighlightPaint.setStyle(Paint.Style.FILL);
        this.mHighlightPaint.setColor(Color.rgb(0, 0, 0));
        this.mHighlightPaint.setAlpha(120);
        this.mShadowPaint = new Paint(1);
        this.mShadowPaint.setStyle(Paint.Style.FILL);
        this.mBarBorderPaint = new Paint(1);
        this.mBarBorderPaint.setStyle(Paint.Style.STROKE);
    }

    public void initBuffers() {
        BarData barData = this.mChart.getBarData();
        this.mBarBuffers = new BarBuffer[barData.getDataSetCount()];

        for(int i = 0; i < this.mBarBuffers.length; ++i) {
            IBarDataSet set = barData.getDataSetByIndex(i);
            this.mBarBuffers[i] = new BarBuffer(set.getEntryCount() * 4 * (set.isStacked() ? set.getStackSize() : 1), barData.getDataSetCount(), set.isStacked());
        }

    }

    public void drawData(Canvas c) {
        BarData barData = this.mChart.getBarData();

        for(int i = 0; i < barData.getDataSetCount(); ++i) {
            IBarDataSet set = barData.getDataSetByIndex(i);
            if (set.isVisible()) {
                this.drawDataSet(c, set, i);
            }
        }

    }

    protected void drawDataSet(Canvas c, IBarDataSet dataSet, int index) {
        Transformer trans = this.mChart.getTransformer(dataSet.getAxisDependency());
        this.mBarBorderPaint.setColor(dataSet.getBarBorderColor());
        this.mBarBorderPaint.setStrokeWidth(Utils.convertDpToPixel(dataSet.getBarBorderWidth()));
        boolean drawBorder = dataSet.getBarBorderWidth() > 0.0F;
        float phaseX = this.mAnimator.getPhaseX();
        float phaseY = this.mAnimator.getPhaseY();
        if (this.mChart.isDrawBarShadowEnabled()) {
            this.mShadowPaint.setColor(dataSet.getBarShadowColor());
            BarData barData = this.mChart.getBarData();
            float barWidth = barData.getBarWidth();
            float barWidthHalf = barWidth / 2.0F;
            int i = 0;

            for(int count = Math.min((int)Math.ceil((float)dataSet.getEntryCount() * phaseX), dataSet.getEntryCount()); i < count; ++i) {
                BarEntry e = dataSet.getEntryForIndex(i);
                float x = e.getX();
                this.mBarShadowRectBuffer.left = x - barWidthHalf;
                this.mBarShadowRectBuffer.right = x + barWidthHalf;
                trans.rectValueToPixel(this.mBarShadowRectBuffer);
                if (this.mViewPortHandler.isInBoundsLeft(this.mBarShadowRectBuffer.right)) {
                    if (!this.mViewPortHandler.isInBoundsRight(this.mBarShadowRectBuffer.left)) {
                        break;
                    }

                    this.mBarShadowRectBuffer.top = this.mViewPortHandler.contentTop();
                    this.mBarShadowRectBuffer.bottom = this.mViewPortHandler.contentBottom();
                    c.drawRect(this.mBarShadowRectBuffer, this.mShadowPaint);
                }
            }
        }

        BarBuffer buffer = this.mBarBuffers[index];
        buffer.setPhases(phaseX, phaseY);
        buffer.setDataSet(index);
        buffer.setInverted(this.mChart.isInverted(dataSet.getAxisDependency()));
        buffer.setBarWidth(this.mChart.getBarData().getBarWidth());
        buffer.feed(dataSet);
        trans.pointValuesToPixel(buffer.buffer);
        boolean isSingleColor = dataSet.getColors().size() == 1;
        if (isSingleColor) {
            this.mRenderPaint.setColor(dataSet.getColor());
        }

        for(int j = 0; j < buffer.size(); j += 4) {
            if (this.mViewPortHandler.isInBoundsLeft(buffer.buffer[j + 2])) {
                if (!this.mViewPortHandler.isInBoundsRight(buffer.buffer[j])) {
                    break;
                }

                if (!isSingleColor) {
                    this.mRenderPaint.setColor(dataSet.getColor(j / 4));
                }

                if (dataSet.getGradientColor() != null) {
                    GradientColor gradientColor = dataSet.getGradientColor();
                    this.mRenderPaint.setShader(new LinearGradient(buffer.buffer[j], buffer.buffer[j + 3], buffer.buffer[j], buffer.buffer[j + 1], gradientColor.getStartColor(), gradientColor.getEndColor(), Shader.TileMode.MIRROR));
                }

                if (dataSet.getGradientColors() != null) {
                    this.mRenderPaint.setShader(new LinearGradient(buffer.buffer[j], buffer.buffer[j + 3], buffer.buffer[j], buffer.buffer[j + 1], dataSet.getGradientColor(j / 4).getStartColor(), dataSet.getGradientColor(j / 4).getEndColor(), Shader.TileMode.MIRROR));
                }

                c.drawRect(buffer.buffer[j], buffer.buffer[j + 1], buffer.buffer[j + 2], buffer.buffer[j + 3], this.mRenderPaint);
                if (drawBorder) {
                    c.drawRect(buffer.buffer[j], buffer.buffer[j + 1], buffer.buffer[j + 2], buffer.buffer[j + 3], this.mBarBorderPaint);
                }
            }
        }

    }

    protected void prepareBarHighlight(float x, float y1, float y2, float barWidthHalf, Transformer trans) {
        float left = x - barWidthHalf;
        float right = x + barWidthHalf;
        this.mBarRect.set(left, y1, right, y2);
        trans.rectToPixelPhase(this.mBarRect, this.mAnimator.getPhaseY());
    }

    public void drawValues(Canvas c) {
        if (this.isDrawingValuesAllowed(this.mChart)) {
            List<IBarDataSet> dataSets = this.mChart.getBarData().getDataSets();
            float valueOffsetPlus = Utils.convertDpToPixel(4.5F);
            float posOffset = 0.0F;
            float negOffset = 0.0F;
            boolean drawValueAboveBar = this.mChart.isDrawValueAboveBarEnabled();

            for(int i = 0; i < this.mChart.getBarData().getDataSetCount(); ++i) {
                IBarDataSet dataSet = dataSets.get(i);
                if (this.shouldDrawValues(dataSet)) {
                    this.applyValueTextStyle(dataSet);
                    boolean isInverted = this.mChart.isInverted(dataSet.getAxisDependency());
                    float valueTextHeight = (float)Utils.calcTextHeight(this.mValuePaint, "8");
                    posOffset = drawValueAboveBar ? -valueOffsetPlus : valueTextHeight + valueOffsetPlus;
                    negOffset = drawValueAboveBar ? valueTextHeight + valueOffsetPlus : -valueOffsetPlus;
                    if (isInverted) {
                        posOffset = -posOffset - valueTextHeight;
                        negOffset = -negOffset - valueTextHeight;
                    }

                    BarBuffer buffer = this.mBarBuffers[i];
                    float phaseY = this.mAnimator.getPhaseY();
                    ValueFormatter formatter = dataSet.getValueFormatter();
                    MPPointF iconsOffset = MPPointF.getInstance(dataSet.getIconsOffset());
                    iconsOffset.x = Utils.convertDpToPixel(iconsOffset.x);
                    iconsOffset.y = Utils.convertDpToPixel(iconsOffset.y);
                    float x;
                    if (!dataSet.isStacked()) {
                        for(int j = 0; (float)j < (float)buffer.buffer.length * this.mAnimator.getPhaseX(); j += 4) {
                            x = (buffer.buffer[j] + buffer.buffer[j + 2]) / 2.0F;
                            if (!this.mViewPortHandler.isInBoundsRight(x)) {
                                break;
                            }

                            if (this.mViewPortHandler.isInBoundsY(buffer.buffer[j + 1]) && this.mViewPortHandler.isInBoundsLeft(x)) {
                                BarEntry entry = dataSet.getEntryForIndex(j / 4);
                                float val = entry.getY();
                                if (dataSet.isDrawValuesEnabled()) {
                                    this.drawValue(c, formatter.getBarLabel(entry), x, val >= 0.0F ? buffer.buffer[j + 1] + posOffset : buffer.buffer[j + 3] + negOffset, dataSet.getValueTextColor(j / 4));
                                }

                                if (entry.getIcon() != null && dataSet.isDrawIconsEnabled()) {
                                    Drawable icon = entry.getIcon();
                                    float py = val >= 0.0F ? buffer.buffer[j + 1] + posOffset : buffer.buffer[j + 3] + negOffset;
                                    x = x + iconsOffset.x;
                                    py += iconsOffset.y;
                                    Utils.drawImage(c, icon, (int)x, (int)py, icon.getIntrinsicWidth(), icon.getIntrinsicHeight());
                                }
                            }
                        }
                    } else {
                        Transformer trans = this.mChart.getTransformer(dataSet.getAxisDependency());
                        int bufferIndex = 0;
                        int index = 0;

                        label181:
                        while(true) {
                            float[] vals;
                            label179:
                            while(true) {
                                if (!((float)index < (float)dataSet.getEntryCount() * this.mAnimator.getPhaseX())) {
                                    break label181;
                                }

                                BarEntry entry = dataSet.getEntryForIndex(index);
                                vals = entry.getYVals();
                                x = (buffer.buffer[bufferIndex] + buffer.buffer[bufferIndex + 2]) / 2.0F;
                                int color = dataSet.getValueTextColor(index);
                                float px;
                                float py;
                                if (vals == null) {
                                    if (!this.mViewPortHandler.isInBoundsRight(x)) {
                                        break label181;
                                    }

                                    if (!this.mViewPortHandler.isInBoundsY(buffer.buffer[bufferIndex + 1]) || !this.mViewPortHandler.isInBoundsLeft(x)) {
                                        continue;
                                    }

                                    if (dataSet.isDrawValuesEnabled()) {
                                        this.drawValue(c, formatter.getBarLabel(entry), x, buffer.buffer[bufferIndex + 1] + (entry.getY() >= 0.0F ? posOffset : negOffset), color);
                                    }

                                    if (entry.getIcon() != null && dataSet.isDrawIconsEnabled()) {
                                        Drawable icon = entry.getIcon();
                                        py = buffer.buffer[bufferIndex + 1] + (entry.getY() >= 0.0F ? posOffset : negOffset);
                                        px = x + iconsOffset.x;
                                        py += iconsOffset.y;
                                        Utils.drawImage(c, icon, (int)px, (int)py, icon.getIntrinsicWidth(), icon.getIntrinsicHeight());
                                    }
                                    break;
                                }

                                float[] transformed = new float[vals.length * 2];
                                px = 0.0F;
                                py = -entry.getNegativeSum();
                                int k = 0;

                                float y;
                                for(int idx = 0; k < transformed.length; ++idx) {
                                    float value = vals[idx];
                                    if (value == 0.0F && (px == 0.0F || py == 0.0F)) {
                                        y = value;
                                    } else if (value >= 0.0F) {
                                        px += value;
                                        y = px;
                                    } else {
                                        y = py;
                                        py -= value;
                                    }

                                    transformed[k + 1] = y * phaseY;
                                    k += 2;
                                }

                                trans.pointValuesToPixel(transformed);
                                k = 0;

                                while(true) {
                                    if (k >= transformed.length) {
                                        break label179;
                                    }

                                    float val = vals[k / 2];
                                    boolean drawBelow = val == 0.0F && py == 0.0F && px > 0.0F || val < 0.0F;
                                    y = transformed[k + 1] + (drawBelow ? negOffset : posOffset);
                                    if (!this.mViewPortHandler.isInBoundsRight(x)) {
                                        break label179;
                                    }

                                    if (this.mViewPortHandler.isInBoundsY(y) && this.mViewPortHandler.isInBoundsLeft(x)) {
                                        if (dataSet.isDrawValuesEnabled()) {
                                            this.drawValue(c, formatter.getBarStackedLabel(val, entry), x, y, color);
                                        }

                                        if (entry.getIcon() != null && dataSet.isDrawIconsEnabled()) {
                                            Drawable icon = entry.getIcon();
                                            Utils.drawImage(c, icon, (int)(x + iconsOffset.x), (int)(y + iconsOffset.y), icon.getIntrinsicWidth(), icon.getIntrinsicHeight());
                                        }
                                    }

                                    k += 2;
                                }
                            }

                            bufferIndex = vals == null ? bufferIndex + 4 : bufferIndex + 4 * vals.length;
                            ++index;
                        }
                    }

                    MPPointF.recycleInstance(iconsOffset);
                }
            }
        }

    }

    public void drawValue(Canvas c, String valueText, float x, float y, int color) {
        this.mValuePaint.setColor(color);
        c.drawText(valueText, x, y, this.mValuePaint);
    }

    public void drawHighlighted(Canvas c, Highlight[] indices) {
        BarData barData = this.mChart.getBarData();
        Highlight[] var4 = indices;
        int var5 = indices.length;

        for(int var6 = 0; var6 < var5; ++var6) {
            Highlight high = var4[var6];
            IBarDataSet set = barData.getDataSetByIndex(high.getDataSetIndex());
            if (set != null && set.isHighlightEnabled()) {
                BarEntry e = set.getEntryForXValue(high.getX(), high.getY());
                if (this.isInBoundsX(e, set)) {
                    Transformer trans = this.mChart.getTransformer(set.getAxisDependency());
                    this.mHighlightPaint.setColor(set.getHighLightColor());
                    this.mHighlightPaint.setAlpha(set.getHighLightAlpha());
                    boolean isStack = high.getStackIndex() >= 0 && e.isStacked();
                    float y1;
                    float y2;
                    if (isStack) {
                        if (this.mChart.isHighlightFullBarEnabled()) {
                            y1 = e.getPositiveSum();
                            y2 = -e.getNegativeSum();
                        } else {
                            Range range = e.getmRanges()[high.getStackIndex()];
                            y1 = range.from;
                            y2 = range.to;
                        }
                    } else {
                        y1 = e.getY();
                        y2 = 0.0F;
                    }

                    this.prepareBarHighlight(e.getX(), y1, y2, barData.getBarWidth() / 2.0F, trans);
                    this.setHighlightDrawPos(high, this.mBarRect);
                    c.drawRect(this.mBarRect, this.mHighlightPaint);
                }
            }
        }

    }

    protected void setHighlightDrawPos(Highlight high, RectF bar) {
        high.setDraw(bar.centerX(), bar.top);
    }

    public void drawExtras(Canvas c) {
    }
}