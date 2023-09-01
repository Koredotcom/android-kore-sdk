package kore.botssdk.charts.renderer;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;

import java.util.Iterator;
import java.util.List;

import kore.botssdk.charts.animation.ChartAnimator;
import kore.botssdk.charts.data.CandleData;
import kore.botssdk.charts.data.CandleEntry;
import kore.botssdk.charts.formatter.ValueFormatter;
import kore.botssdk.charts.highlight.Highlight;
import kore.botssdk.charts.interfaces.dataprovider.CandleDataProvider;
import kore.botssdk.charts.interfaces.datasets.ICandleDataSet;
import kore.botssdk.charts.utils.MPPointD;
import kore.botssdk.charts.utils.MPPointF;
import kore.botssdk.charts.utils.Transformer;
import kore.botssdk.charts.utils.Utils;
import kore.botssdk.charts.utils.ViewPortHandler;

public class CandleStickChartRenderer extends LineScatterCandleRadarRenderer {
    protected final CandleDataProvider mChart;
    private final float[] mShadowBuffers = new float[8];
    private final float[] mBodyBuffers = new float[4];
    private final float[] mRangeBuffers = new float[4];
    private final float[] mOpenBuffers = new float[4];
    private final float[] mCloseBuffers = new float[4];

    public CandleStickChartRenderer(CandleDataProvider chart, ChartAnimator animator, ViewPortHandler viewPortHandler) {
        super(animator, viewPortHandler);
        this.mChart = chart;
    }

    public void initBuffers() {
    }

    public void drawData(Canvas c) {
        CandleData candleData = this.mChart.getCandleData();
        Iterator var3 = candleData.getDataSets().iterator();

        while(var3.hasNext()) {
            ICandleDataSet set = (ICandleDataSet)var3.next();
            if (set.isVisible()) {
                this.drawDataSet(c, set);
            }
        }

    }

    protected void drawDataSet(Canvas c, ICandleDataSet dataSet) {
        Transformer trans = this.mChart.getTransformer(dataSet.getAxisDependency());
        float phaseY = this.mAnimator.getPhaseY();
        float barSpace = dataSet.getBarSpace();
        boolean showCandleBar = dataSet.getShowCandleBar();
        this.mXBounds.set(this.mChart, dataSet);
        this.mRenderPaint.setStrokeWidth(dataSet.getShadowWidth());

        for(int j = this.mXBounds.min; j <= this.mXBounds.range + this.mXBounds.min; ++j) {
            CandleEntry e = dataSet.getEntryForIndex(j);
            if (e != null) {
                float xPos = e.getX();
                float open = e.getOpen();
                float close = e.getClose();
                float high = e.getHigh();
                float low = e.getLow();
                if (showCandleBar) {
                    this.mShadowBuffers[0] = xPos;
                    this.mShadowBuffers[2] = xPos;
                    this.mShadowBuffers[4] = xPos;
                    this.mShadowBuffers[6] = xPos;
                    if (open > close) {
                        this.mShadowBuffers[1] = high * phaseY;
                        this.mShadowBuffers[3] = open * phaseY;
                        this.mShadowBuffers[5] = low * phaseY;
                        this.mShadowBuffers[7] = close * phaseY;
                    } else if (open < close) {
                        this.mShadowBuffers[1] = high * phaseY;
                        this.mShadowBuffers[3] = close * phaseY;
                        this.mShadowBuffers[5] = low * phaseY;
                        this.mShadowBuffers[7] = open * phaseY;
                    } else {
                        this.mShadowBuffers[1] = high * phaseY;
                        this.mShadowBuffers[3] = open * phaseY;
                        this.mShadowBuffers[5] = low * phaseY;
                        this.mShadowBuffers[7] = this.mShadowBuffers[3];
                    }

                    trans.pointValuesToPixel(this.mShadowBuffers);
                    if (dataSet.getShadowColorSameAsCandle()) {
                        if (open > close) {
                            this.mRenderPaint.setColor(dataSet.getDecreasingColor() == 1122867 ? dataSet.getColor(j) : dataSet.getDecreasingColor());
                        } else if (open < close) {
                            this.mRenderPaint.setColor(dataSet.getIncreasingColor() == 1122867 ? dataSet.getColor(j) : dataSet.getIncreasingColor());
                        } else {
                            this.mRenderPaint.setColor(dataSet.getNeutralColor() == 1122867 ? dataSet.getColor(j) : dataSet.getNeutralColor());
                        }
                    } else {
                        this.mRenderPaint.setColor(dataSet.getShadowColor() == 1122867 ? dataSet.getColor(j) : dataSet.getShadowColor());
                    }

                    this.mRenderPaint.setStyle(Paint.Style.STROKE);
                    c.drawLines(this.mShadowBuffers, this.mRenderPaint);
                    this.mBodyBuffers[0] = xPos - 0.5F + barSpace;
                    this.mBodyBuffers[1] = close * phaseY;
                    this.mBodyBuffers[2] = xPos + 0.5F - barSpace;
                    this.mBodyBuffers[3] = open * phaseY;
                    trans.pointValuesToPixel(this.mBodyBuffers);
                    if (open > close) {
                        if (dataSet.getDecreasingColor() == 1122867) {
                            this.mRenderPaint.setColor(dataSet.getColor(j));
                        } else {
                            this.mRenderPaint.setColor(dataSet.getDecreasingColor());
                        }

                        this.mRenderPaint.setStyle(dataSet.getDecreasingPaintStyle());
                        c.drawRect(this.mBodyBuffers[0], this.mBodyBuffers[3], this.mBodyBuffers[2], this.mBodyBuffers[1], this.mRenderPaint);
                    } else if (open < close) {
                        if (dataSet.getIncreasingColor() == 1122867) {
                            this.mRenderPaint.setColor(dataSet.getColor(j));
                        } else {
                            this.mRenderPaint.setColor(dataSet.getIncreasingColor());
                        }

                        this.mRenderPaint.setStyle(dataSet.getIncreasingPaintStyle());
                        c.drawRect(this.mBodyBuffers[0], this.mBodyBuffers[1], this.mBodyBuffers[2], this.mBodyBuffers[3], this.mRenderPaint);
                    } else {
                        if (dataSet.getNeutralColor() == 1122867) {
                            this.mRenderPaint.setColor(dataSet.getColor(j));
                        } else {
                            this.mRenderPaint.setColor(dataSet.getNeutralColor());
                        }

                        c.drawLine(this.mBodyBuffers[0], this.mBodyBuffers[1], this.mBodyBuffers[2], this.mBodyBuffers[3], this.mRenderPaint);
                    }
                } else {
                    this.mRangeBuffers[0] = xPos;
                    this.mRangeBuffers[1] = high * phaseY;
                    this.mRangeBuffers[2] = xPos;
                    this.mRangeBuffers[3] = low * phaseY;
                    this.mOpenBuffers[0] = xPos - 0.5F + barSpace;
                    this.mOpenBuffers[1] = open * phaseY;
                    this.mOpenBuffers[2] = xPos;
                    this.mOpenBuffers[3] = open * phaseY;
                    this.mCloseBuffers[0] = xPos + 0.5F - barSpace;
                    this.mCloseBuffers[1] = close * phaseY;
                    this.mCloseBuffers[2] = xPos;
                    this.mCloseBuffers[3] = close * phaseY;
                    trans.pointValuesToPixel(this.mRangeBuffers);
                    trans.pointValuesToPixel(this.mOpenBuffers);
                    trans.pointValuesToPixel(this.mCloseBuffers);
                    int barColor;
                    if (open > close) {
                        barColor = dataSet.getDecreasingColor() == 1122867 ? dataSet.getColor(j) : dataSet.getDecreasingColor();
                    } else if (open < close) {
                        barColor = dataSet.getIncreasingColor() == 1122867 ? dataSet.getColor(j) : dataSet.getIncreasingColor();
                    } else {
                        barColor = dataSet.getNeutralColor() == 1122867 ? dataSet.getColor(j) : dataSet.getNeutralColor();
                    }

                    this.mRenderPaint.setColor(barColor);
                    c.drawLine(this.mRangeBuffers[0], this.mRangeBuffers[1], this.mRangeBuffers[2], this.mRangeBuffers[3], this.mRenderPaint);
                    c.drawLine(this.mOpenBuffers[0], this.mOpenBuffers[1], this.mOpenBuffers[2], this.mOpenBuffers[3], this.mRenderPaint);
                    c.drawLine(this.mCloseBuffers[0], this.mCloseBuffers[1], this.mCloseBuffers[2], this.mCloseBuffers[3], this.mRenderPaint);
                }
            }
        }

    }

    public void drawValues(Canvas c) {
        if (this.isDrawingValuesAllowed(this.mChart)) {
            List<ICandleDataSet> dataSets = this.mChart.getCandleData().getDataSets();

            for(int i = 0; i < dataSets.size(); ++i) {
                ICandleDataSet dataSet = dataSets.get(i);
                if (this.shouldDrawValues(dataSet) && dataSet.getEntryCount() >= 1) {
                    this.applyValueTextStyle(dataSet);
                    Transformer trans = this.mChart.getTransformer(dataSet.getAxisDependency());
                    this.mXBounds.set(this.mChart, dataSet);
                    float[] positions = trans.generateTransformedValuesCandle(dataSet, this.mAnimator.getPhaseX(), this.mAnimator.getPhaseY(), this.mXBounds.min, this.mXBounds.max);
                    float yOffset = Utils.convertDpToPixel(5.0F);
                    ValueFormatter formatter = dataSet.getValueFormatter();
                    MPPointF iconsOffset = MPPointF.getInstance(dataSet.getIconsOffset());
                    iconsOffset.x = Utils.convertDpToPixel(iconsOffset.x);
                    iconsOffset.y = Utils.convertDpToPixel(iconsOffset.y);

                    for(int j = 0; j < positions.length; j += 2) {
                        float x = positions[j];
                        float y = positions[j + 1];
                        if (!this.mViewPortHandler.isInBoundsRight(x)) {
                            break;
                        }

                        if (this.mViewPortHandler.isInBoundsLeft(x) && this.mViewPortHandler.isInBoundsY(y)) {
                            CandleEntry entry = dataSet.getEntryForIndex(j / 2 + this.mXBounds.min);
                            if (dataSet.isDrawValuesEnabled()) {
                                this.drawValue(c, formatter.getCandleLabel(entry), x, y - yOffset, dataSet.getValueTextColor(j / 2));
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

    public void drawValue(Canvas c, String valueText, float x, float y, int color) {
        this.mValuePaint.setColor(color);
        c.drawText(valueText, x, y, this.mValuePaint);
    }

    public void drawExtras(Canvas c) {
    }

    public void drawHighlighted(Canvas c, Highlight[] indices) {
        CandleData candleData = this.mChart.getCandleData();
        Highlight[] var4 = indices;
        int var5 = indices.length;

        for(int var6 = 0; var6 < var5; ++var6) {
            Highlight high = var4[var6];
            ICandleDataSet set = candleData.getDataSetByIndex(high.getDataSetIndex());
            if (set != null && set.isHighlightEnabled()) {
                CandleEntry e = set.getEntryForXValue(high.getX(), high.getY());
                if (this.isInBoundsX(e, set)) {
                    float lowValue = e.getLow() * this.mAnimator.getPhaseY();
                    float highValue = e.getHigh() * this.mAnimator.getPhaseY();
                    float y = (lowValue + highValue) / 2.0F;
                    MPPointD pix = this.mChart.getTransformer(set.getAxisDependency()).getPixelForValues(e.getX(), y);
                    high.setDraw((float)pix.x, (float)pix.y);
                    this.drawHighlightLines(c, (float)pix.x, (float)pix.y, set);
                }
            }
        }

    }
}
