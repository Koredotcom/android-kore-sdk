package kore.botssdk.charts.renderer;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;

import java.util.List;

import kore.botssdk.charts.animation.ChartAnimator;
import kore.botssdk.charts.buffer.BarBuffer;
import kore.botssdk.charts.buffer.HorizontalBarBuffer;
import kore.botssdk.charts.data.BarData;
import kore.botssdk.charts.data.BarEntry;
import kore.botssdk.charts.formatter.ValueFormatter;
import kore.botssdk.charts.highlight.Highlight;
import kore.botssdk.charts.interfaces.dataprovider.BarDataProvider;
import kore.botssdk.charts.interfaces.dataprovider.ChartInterface;
import kore.botssdk.charts.interfaces.datasets.IBarDataSet;
import kore.botssdk.charts.utils.MPPointF;
import kore.botssdk.charts.utils.Transformer;
import kore.botssdk.charts.utils.Utils;
import kore.botssdk.charts.utils.ViewPortHandler;

public class HorizontalBarChartRenderer extends BarChartRenderer {
    private final RectF mBarShadowRectBuffer = new RectF();

    public HorizontalBarChartRenderer(BarDataProvider chart, ChartAnimator animator, ViewPortHandler viewPortHandler) {
        super(chart, animator, viewPortHandler);
        this.mValuePaint.setTextAlign(Paint.Align.LEFT);
    }

    public void initBuffers() {
        BarData barData = this.mChart.getBarData();
        this.mBarBuffers = new HorizontalBarBuffer[barData.getDataSetCount()];

        for(int i = 0; i < this.mBarBuffers.length; ++i) {
            IBarDataSet set = barData.getDataSetByIndex(i);
            this.mBarBuffers[i] = new HorizontalBarBuffer(set.getEntryCount() * 4 * (set.isStacked() ? set.getStackSize() : 1), barData.getDataSetCount(), set.isStacked());
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
                this.mBarShadowRectBuffer.top = x - barWidthHalf;
                this.mBarShadowRectBuffer.bottom = x + barWidthHalf;
                trans.rectValueToPixel(this.mBarShadowRectBuffer);
                if (this.mViewPortHandler.isInBoundsTop(this.mBarShadowRectBuffer.bottom)) {
                    if (!this.mViewPortHandler.isInBoundsBottom(this.mBarShadowRectBuffer.top)) {
                        break;
                    }

                    this.mBarShadowRectBuffer.left = this.mViewPortHandler.contentLeft();
                    this.mBarShadowRectBuffer.right = this.mViewPortHandler.contentRight();
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

        for(int j = 0; j < buffer.size() && this.mViewPortHandler.isInBoundsTop(buffer.buffer[j + 3]); j += 4) {
            if (this.mViewPortHandler.isInBoundsBottom(buffer.buffer[j + 1])) {
                if (!isSingleColor) {
                    this.mRenderPaint.setColor(dataSet.getColor(j / 4));
                }

                c.drawRect(buffer.buffer[j], buffer.buffer[j + 1], buffer.buffer[j + 2], buffer.buffer[j + 3], this.mRenderPaint);
                if (drawBorder) {
                    c.drawRect(buffer.buffer[j], buffer.buffer[j + 1], buffer.buffer[j + 2], buffer.buffer[j + 3], this.mBarBorderPaint);
                }
            }
        }

    }

    public void drawValues(Canvas c) {
        if (this.isDrawingValuesAllowed(this.mChart)) {
            List<IBarDataSet> dataSets = this.mChart.getBarData().getDataSets();
            float valueOffsetPlus = Utils.convertDpToPixel(5.0F);
            float posOffset = 0.0F;
            float negOffset = 0.0F;
            boolean drawValueAboveBar = this.mChart.isDrawValueAboveBarEnabled();

            for(int i = 0; i < this.mChart.getBarData().getDataSetCount(); ++i) {
                IBarDataSet dataSet = dataSets.get(i);
                if (this.shouldDrawValues(dataSet)) {
                    boolean isInverted = this.mChart.isInverted(dataSet.getAxisDependency());
                    this.applyValueTextStyle(dataSet);
                    float halfTextHeight = (float)Utils.calcTextHeight(this.mValuePaint, "10") / 2.0F;
                    ValueFormatter formatter = dataSet.getValueFormatter();
                    BarBuffer buffer = this.mBarBuffers[i];
                    float phaseY = this.mAnimator.getPhaseY();
                    MPPointF iconsOffset = MPPointF.getInstance(dataSet.getIconsOffset());
                    iconsOffset.x = Utils.convertDpToPixel(iconsOffset.x);
                    iconsOffset.y = Utils.convertDpToPixel(iconsOffset.y);
                    float posY;
                    float negY;
                    if (!dataSet.isStacked()) {
                        for(int j = 0; (float)j < (float)buffer.buffer.length * this.mAnimator.getPhaseX(); j += 4) {
                            float y = (buffer.buffer[j + 1] + buffer.buffer[j + 3]) / 2.0F;
                            if (!this.mViewPortHandler.isInBoundsTop(buffer.buffer[j + 1])) {
                                break;
                            }

                            if (this.mViewPortHandler.isInBoundsX(buffer.buffer[j]) && this.mViewPortHandler.isInBoundsBottom(buffer.buffer[j + 1])) {
                                BarEntry entry = dataSet.getEntryForIndex(j / 4);
                                float val = entry.getY();
                                String formattedValue = formatter.getBarLabel(entry);
                                float valueTextWidth = (float)Utils.calcTextWidth(this.mValuePaint, formattedValue);
                                posOffset = drawValueAboveBar ? valueOffsetPlus : -(valueTextWidth + valueOffsetPlus);
                                negOffset = drawValueAboveBar ? -(valueTextWidth + valueOffsetPlus) : valueOffsetPlus;
                                if (isInverted) {
                                    posOffset = -posOffset - valueTextWidth;
                                    negOffset = -negOffset - valueTextWidth;
                                }

                                if (dataSet.isDrawValuesEnabled()) {
                                    this.drawValue(c, formattedValue, buffer.buffer[j + 2] + (val >= 0.0F ? posOffset : negOffset), y + halfTextHeight, dataSet.getValueTextColor(j / 2));
                                }

                                if (entry.getIcon() != null && dataSet.isDrawIconsEnabled()) {
                                    Drawable icon = entry.getIcon();
                                    posY = buffer.buffer[j + 2] + (val >= 0.0F ? posOffset : negOffset);
                                    posY += iconsOffset.x;
                                    negY = y + iconsOffset.y;
                                    Utils.drawImage(c, icon, (int)posY, (int)negY, icon.getIntrinsicWidth(), icon.getIntrinsicHeight());
                                }
                            }
                        }
                    } else {
                        Transformer trans = this.mChart.getTransformer(dataSet.getAxisDependency());
                        int bufferIndex = 0;
                        int index = 0;

                        label213:
                        while(true) {
                            float[] vals;
                            label211:
                            while(true) {
                                if (!((float)index < (float)dataSet.getEntryCount() * this.mAnimator.getPhaseX())) {
                                    break label213;
                                }

                                BarEntry entry = dataSet.getEntryForIndex(index);
                                int color = dataSet.getValueTextColor(index);
                                vals = entry.getYVals();
                                float py;
                                if (vals == null) {
                                    if (!this.mViewPortHandler.isInBoundsTop(buffer.buffer[bufferIndex + 1])) {
                                        break label213;
                                    }

                                    if (!this.mViewPortHandler.isInBoundsX(buffer.buffer[bufferIndex]) || !this.mViewPortHandler.isInBoundsBottom(buffer.buffer[bufferIndex + 1])) {
                                        continue;
                                    }

                                    String formattedValue = formatter.getBarLabel(entry);
                                    posY = (float)Utils.calcTextWidth(this.mValuePaint, formattedValue);
                                    posOffset = drawValueAboveBar ? valueOffsetPlus : -(posY + valueOffsetPlus);
                                    negOffset = drawValueAboveBar ? -(posY + valueOffsetPlus) : valueOffsetPlus;
                                    if (isInverted) {
                                        posOffset = -posOffset - posY;
                                        negOffset = -negOffset - posY;
                                    }

                                    if (dataSet.isDrawValuesEnabled()) {
                                        this.drawValue(c, formattedValue, buffer.buffer[bufferIndex + 2] + (entry.getY() >= 0.0F ? posOffset : negOffset), buffer.buffer[bufferIndex + 1] + halfTextHeight, color);
                                    }

                                    if (entry.getIcon() != null && dataSet.isDrawIconsEnabled()) {
                                        Drawable icon = entry.getIcon();
                                        float px = buffer.buffer[bufferIndex + 2] + (entry.getY() >= 0.0F ? posOffset : negOffset);
                                        py = buffer.buffer[bufferIndex + 1];
                                        px += iconsOffset.x;
                                        py += iconsOffset.y;
                                        Utils.drawImage(c, icon, (int)px, (int)py, icon.getIntrinsicWidth(), icon.getIntrinsicHeight());
                                    }
                                    break;
                                }

                                float[] transformed = new float[vals.length * 2];
                                posY = 0.0F;
                                negY = -entry.getNegativeSum();
                                int k = 0;

                                float valueTextWidth;
                                for(int idx = 0; k < transformed.length; ++idx) {
                                    float value = vals[idx];
                                    if (value == 0.0F && (posY == 0.0F || negY == 0.0F)) {
                                        valueTextWidth = value;
                                    } else if (value >= 0.0F) {
                                        posY += value;
                                        valueTextWidth = posY;
                                    } else {
                                        valueTextWidth = negY;
                                        negY -= value;
                                    }

                                    transformed[k] = valueTextWidth * phaseY;
                                    k += 2;
                                }

                                trans.pointValuesToPixel(transformed);
                                k = 0;

                                while(true) {
                                    if (k >= transformed.length) {
                                        break label211;
                                    }

                                    py = vals[k / 2];
                                    String formattedValue = formatter.getBarStackedLabel(py, entry);
                                    valueTextWidth = (float)Utils.calcTextWidth(this.mValuePaint, formattedValue);
                                    posOffset = drawValueAboveBar ? valueOffsetPlus : -(valueTextWidth + valueOffsetPlus);
                                    negOffset = drawValueAboveBar ? -(valueTextWidth + valueOffsetPlus) : valueOffsetPlus;
                                    if (isInverted) {
                                        posOffset = -posOffset - valueTextWidth;
                                        negOffset = -negOffset - valueTextWidth;
                                    }

                                    boolean drawBelow = py == 0.0F && negY == 0.0F && posY > 0.0F || py < 0.0F;
                                    float x = transformed[k] + (drawBelow ? negOffset : posOffset);
                                    float y = (buffer.buffer[bufferIndex + 1] + buffer.buffer[bufferIndex + 3]) / 2.0F;
                                    if (!this.mViewPortHandler.isInBoundsTop(y)) {
                                        break label211;
                                    }

                                    if (this.mViewPortHandler.isInBoundsX(x) && this.mViewPortHandler.isInBoundsBottom(y)) {
                                        if (dataSet.isDrawValuesEnabled()) {
                                            this.drawValue(c, formattedValue, x, y + halfTextHeight, color);
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

    protected void prepareBarHighlight(float x, float y1, float y2, float barWidthHalf, Transformer trans) {
        float top = x - barWidthHalf;
        float bottom = x + barWidthHalf;
        this.mBarRect.set(y1, top, y2, bottom);
        trans.rectToPixelPhaseHorizontal(this.mBarRect, this.mAnimator.getPhaseY());
    }

    protected void setHighlightDrawPos(Highlight high, RectF bar) {
        high.setDraw(bar.centerY(), bar.right);
    }

    protected boolean isDrawingValuesAllowed(ChartInterface chart) {
        return (float)chart.getData().getEntryCount() < (float)chart.getMaxVisibleCount() * this.mViewPortHandler.getScaleY();
    }
}
