package kore.botssdk.charts.renderer;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;

import java.util.Iterator;
import java.util.List;

import kore.botssdk.charts.animation.ChartAnimator;
import kore.botssdk.charts.data.BubbleData;
import kore.botssdk.charts.data.BubbleEntry;
import kore.botssdk.charts.formatter.ValueFormatter;
import kore.botssdk.charts.highlight.Highlight;
import kore.botssdk.charts.interfaces.dataprovider.BubbleDataProvider;
import kore.botssdk.charts.interfaces.datasets.IBubbleDataSet;
import kore.botssdk.charts.utils.MPPointF;
import kore.botssdk.charts.utils.Transformer;
import kore.botssdk.charts.utils.Utils;
import kore.botssdk.charts.utils.ViewPortHandler;

public class BubbleChartRenderer extends BarLineScatterCandleBubbleRenderer {
    protected final BubbleDataProvider mChart;
    private final float[] sizeBuffer = new float[4];
    private final float[] pointBuffer = new float[2];
    private final float[] _hsvBuffer = new float[3];

    public BubbleChartRenderer(BubbleDataProvider chart, ChartAnimator animator, ViewPortHandler viewPortHandler) {
        super(animator, viewPortHandler);
        this.mChart = chart;
        this.mRenderPaint.setStyle(Paint.Style.FILL);
        this.mHighlightPaint.setStyle(Paint.Style.STROKE);
        this.mHighlightPaint.setStrokeWidth(Utils.convertDpToPixel(1.5F));
    }

    public void initBuffers() {
    }

    public void drawData(Canvas c) {
        BubbleData bubbleData = this.mChart.getBubbleData();
        Iterator var3 = bubbleData.getDataSets().iterator();

        while(var3.hasNext()) {
            IBubbleDataSet set = (IBubbleDataSet)var3.next();
            if (set.isVisible()) {
                this.drawDataSet(c, set);
            }
        }

    }

    protected float getShapeSize(float entrySize, float maxSize, float reference, boolean normalizeSize) {
        float factor = normalizeSize ? (maxSize == 0.0F ? 1.0F : (float)Math.sqrt(entrySize / maxSize)) : entrySize;
        float shapeSize = reference * factor;
        return shapeSize;
    }

    protected void drawDataSet(Canvas c, IBubbleDataSet dataSet) {
        if (dataSet.getEntryCount() >= 1) {
            Transformer trans = this.mChart.getTransformer(dataSet.getAxisDependency());
            float phaseY = this.mAnimator.getPhaseY();
            this.mXBounds.set(this.mChart, dataSet);
            this.sizeBuffer[0] = 0.0F;
            this.sizeBuffer[2] = 1.0F;
            trans.pointValuesToPixel(this.sizeBuffer);
            boolean normalizeSize = dataSet.isNormalizeSizeEnabled();
            float maxBubbleWidth = Math.abs(this.sizeBuffer[2] - this.sizeBuffer[0]);
            float maxBubbleHeight = Math.abs(this.mViewPortHandler.contentBottom() - this.mViewPortHandler.contentTop());
            float referenceSize = Math.min(maxBubbleHeight, maxBubbleWidth);

            for(int j = this.mXBounds.min; j <= this.mXBounds.range + this.mXBounds.min; ++j) {
                BubbleEntry entry = dataSet.getEntryForIndex(j);
                this.pointBuffer[0] = entry.getX();
                this.pointBuffer[1] = entry.getY() * phaseY;
                trans.pointValuesToPixel(this.pointBuffer);
                float shapeHalf = this.getShapeSize(entry.getSize(), dataSet.getMaxSize(), referenceSize, normalizeSize) / 2.0F;
                if (this.mViewPortHandler.isInBoundsTop(this.pointBuffer[1] + shapeHalf) && this.mViewPortHandler.isInBoundsBottom(this.pointBuffer[1] - shapeHalf) && this.mViewPortHandler.isInBoundsLeft(this.pointBuffer[0] + shapeHalf)) {
                    if (!this.mViewPortHandler.isInBoundsRight(this.pointBuffer[0] - shapeHalf)) {
                        break;
                    }

                    int color = dataSet.getColor((int)entry.getX());
                    this.mRenderPaint.setColor(color);
                    c.drawCircle(this.pointBuffer[0], this.pointBuffer[1], shapeHalf, this.mRenderPaint);
                }
            }

        }
    }

    public void drawValues(Canvas c) {
        BubbleData bubbleData = this.mChart.getBubbleData();
        if (bubbleData != null) {
            if (this.isDrawingValuesAllowed(this.mChart)) {
                List<IBubbleDataSet> dataSets = bubbleData.getDataSets();
                float lineHeight = (float)Utils.calcTextHeight(this.mValuePaint, "1");

                for(int i = 0; i < dataSets.size(); ++i) {
                    IBubbleDataSet dataSet = dataSets.get(i);
                    if (this.shouldDrawValues(dataSet) && dataSet.getEntryCount() >= 1) {
                        this.applyValueTextStyle(dataSet);
                        float phaseX = Math.max(0.0F, Math.min(1.0F, this.mAnimator.getPhaseX()));
                        float phaseY = this.mAnimator.getPhaseY();
                        this.mXBounds.set(this.mChart, dataSet);
                        float[] positions = this.mChart.getTransformer(dataSet.getAxisDependency()).generateTransformedValuesBubble(dataSet, phaseY, this.mXBounds.min, this.mXBounds.max);
                        float alpha = phaseX == 1.0F ? phaseY : phaseX;
                        ValueFormatter formatter = dataSet.getValueFormatter();
                        MPPointF iconsOffset = MPPointF.getInstance(dataSet.getIconsOffset());
                        iconsOffset.x = Utils.convertDpToPixel(iconsOffset.x);
                        iconsOffset.y = Utils.convertDpToPixel(iconsOffset.y);

                        for(int j = 0; j < positions.length; j += 2) {
                            int valueTextColor = dataSet.getValueTextColor(j / 2 + this.mXBounds.min);
                            valueTextColor = Color.argb(Math.round(255.0F * alpha), Color.red(valueTextColor), Color.green(valueTextColor), Color.blue(valueTextColor));
                            float x = positions[j];
                            float y = positions[j + 1];
                            if (!this.mViewPortHandler.isInBoundsRight(x)) {
                                break;
                            }

                            if (this.mViewPortHandler.isInBoundsLeft(x) && this.mViewPortHandler.isInBoundsY(y)) {
                                BubbleEntry entry = dataSet.getEntryForIndex(j / 2 + this.mXBounds.min);
                                if (dataSet.isDrawValuesEnabled()) {
                                    this.drawValue(c, formatter.getBubbleLabel(entry), x, y + 0.5F * lineHeight, valueTextColor);
                                }

                                if (entry.getIcon() != null && dataSet.isDrawIconsEnabled()) {
                                    Drawable icon = entry.getIcon();
                                    Utils.drawImage(c, icon, (int)(x + iconsOffset.x), (int)(y + iconsOffset.y), icon.getIntrinsicWidth(), icon.getIntrinsicHeight());
                                }
                            }
                        }

                        MPPointF.recycleInstance(iconsOffset);
                    }
                }
            }

        }
    }

    public void drawValue(Canvas c, String valueText, float x, float y, int color) {
        this.mValuePaint.setColor(color);
        c.drawText(valueText, x, y, this.mValuePaint);
    }

    public void drawExtras(Canvas c) {
    }

    public void drawHighlighted(Canvas c, Highlight[] indices) {
        BubbleData bubbleData = this.mChart.getBubbleData();
        float phaseY = this.mAnimator.getPhaseY();
        Highlight[] var5 = indices;
        int var6 = indices.length;

        for(int var7 = 0; var7 < var6; ++var7) {
            Highlight high = var5[var7];
            IBubbleDataSet set = bubbleData.getDataSetByIndex(high.getDataSetIndex());
            if (set != null && set.isHighlightEnabled()) {
                BubbleEntry entry = set.getEntryForXValue(high.getX(), high.getY());
                if (entry.getY() == high.getY() && this.isInBoundsX(entry, set)) {
                    Transformer trans = this.mChart.getTransformer(set.getAxisDependency());
                    this.sizeBuffer[0] = 0.0F;
                    this.sizeBuffer[2] = 1.0F;
                    trans.pointValuesToPixel(this.sizeBuffer);
                    boolean normalizeSize = set.isNormalizeSizeEnabled();
                    float maxBubbleWidth = Math.abs(this.sizeBuffer[2] - this.sizeBuffer[0]);
                    float maxBubbleHeight = Math.abs(this.mViewPortHandler.contentBottom() - this.mViewPortHandler.contentTop());
                    float referenceSize = Math.min(maxBubbleHeight, maxBubbleWidth);
                    this.pointBuffer[0] = entry.getX();
                    this.pointBuffer[1] = entry.getY() * phaseY;
                    trans.pointValuesToPixel(this.pointBuffer);
                    high.setDraw(this.pointBuffer[0], this.pointBuffer[1]);
                    float shapeHalf = this.getShapeSize(entry.getSize(), set.getMaxSize(), referenceSize, normalizeSize) / 2.0F;
                    if (this.mViewPortHandler.isInBoundsTop(this.pointBuffer[1] + shapeHalf) && this.mViewPortHandler.isInBoundsBottom(this.pointBuffer[1] - shapeHalf) && this.mViewPortHandler.isInBoundsLeft(this.pointBuffer[0] + shapeHalf)) {
                        if (!this.mViewPortHandler.isInBoundsRight(this.pointBuffer[0] - shapeHalf)) {
                            break;
                        }

                        int originalColor = set.getColor((int)entry.getX());
                        Color.RGBToHSV(Color.red(originalColor), Color.green(originalColor), Color.blue(originalColor), this._hsvBuffer);
                        float[] var10000 = this._hsvBuffer;
                        var10000[2] *= 0.5F;
                        int color = Color.HSVToColor(Color.alpha(originalColor), this._hsvBuffer);
                        this.mHighlightPaint.setColor(color);
                        this.mHighlightPaint.setStrokeWidth(set.getHighlightCircleWidth());
                        c.drawCircle(this.pointBuffer[0], this.pointBuffer[1], shapeHalf, this.mHighlightPaint);
                    }
                }
            }
        }

    }
}
