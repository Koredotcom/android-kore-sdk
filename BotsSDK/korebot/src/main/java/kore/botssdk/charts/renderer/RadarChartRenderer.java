package kore.botssdk.charts.renderer;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.Drawable;

import java.util.Iterator;

import kore.botssdk.charts.animation.ChartAnimator;
import kore.botssdk.charts.charts.RadarChart;
import kore.botssdk.charts.data.RadarData;
import kore.botssdk.charts.data.RadarEntry;
import kore.botssdk.charts.formatter.ValueFormatter;
import kore.botssdk.charts.highlight.Highlight;
import kore.botssdk.charts.interfaces.datasets.IRadarDataSet;
import kore.botssdk.charts.utils.ColorTemplate;
import kore.botssdk.charts.utils.MPPointF;
import kore.botssdk.charts.utils.Utils;
import kore.botssdk.charts.utils.ViewPortHandler;

public class RadarChartRenderer extends LineRadarRenderer {
    protected final RadarChart mChart;
    protected final Paint mWebPaint;
    protected final Paint mHighlightCirclePaint;
    protected final Path mDrawDataSetSurfacePathBuffer = new Path();
    protected final Path mDrawHighlightCirclePathBuffer = new Path();

    public RadarChartRenderer(RadarChart chart, ChartAnimator animator, ViewPortHandler viewPortHandler) {
        super(animator, viewPortHandler);
        this.mChart = chart;
        this.mHighlightPaint = new Paint(1);
        this.mHighlightPaint.setStyle(Paint.Style.STROKE);
        this.mHighlightPaint.setStrokeWidth(2.0F);
        this.mHighlightPaint.setColor(Color.rgb(255, 187, 115));
        this.mWebPaint = new Paint(1);
        this.mWebPaint.setStyle(Paint.Style.STROKE);
        this.mHighlightCirclePaint = new Paint(1);
    }

    public Paint getWebPaint() {
        return this.mWebPaint;
    }

    public void initBuffers() {
    }

    public void drawData(Canvas c) {
        RadarData radarData = this.mChart.getData();
        int mostEntries = radarData.getMaxEntryCountSet().getEntryCount();
        Iterator var4 = radarData.getDataSets().iterator();

        while(var4.hasNext()) {
            IRadarDataSet set = (IRadarDataSet)var4.next();
            if (set.isVisible()) {
                this.drawDataSet(c, set, mostEntries);
            }
        }

    }

    protected void drawDataSet(Canvas c, IRadarDataSet dataSet, int mostEntries) {
        float phaseX = this.mAnimator.getPhaseX();
        float phaseY = this.mAnimator.getPhaseY();
        float sliceangle = this.mChart.getSliceAngle();
        float factor = this.mChart.getFactor();
        MPPointF center = this.mChart.getCenterOffsets();
        MPPointF pOut = MPPointF.getInstance(0.0F, 0.0F);
        Path surface = this.mDrawDataSetSurfacePathBuffer;
        surface.reset();
        boolean hasMovedToPoint = false;

        for(int j = 0; j < dataSet.getEntryCount(); ++j) {
            this.mRenderPaint.setColor(dataSet.getColor(j));
            RadarEntry e = dataSet.getEntryForIndex(j);
            Utils.getPosition(center, (e.getY() - this.mChart.getYChartMin()) * factor * phaseY, sliceangle * (float)j * phaseX + this.mChart.getRotationAngle(), pOut);
            if (!Float.isNaN(pOut.x)) {
                if (!hasMovedToPoint) {
                    surface.moveTo(pOut.x, pOut.y);
                    hasMovedToPoint = true;
                } else {
                    surface.lineTo(pOut.x, pOut.y);
                }
            }
        }

        if (dataSet.getEntryCount() > mostEntries) {
            surface.lineTo(center.x, center.y);
        }

        surface.close();
        if (dataSet.isDrawFilledEnabled()) {
            Drawable drawable = dataSet.getFillDrawable();
            if (drawable != null) {
                this.drawFilledPath(c, surface, drawable);
            } else {
                this.drawFilledPath(c, surface, dataSet.getFillColor(), dataSet.getFillAlpha());
            }
        }

        this.mRenderPaint.setStrokeWidth(dataSet.getLineWidth());
        this.mRenderPaint.setStyle(Paint.Style.STROKE);
        if (!dataSet.isDrawFilledEnabled() || dataSet.getFillAlpha() < 255) {
            c.drawPath(surface, this.mRenderPaint);
        }

        MPPointF.recycleInstance(center);
        MPPointF.recycleInstance(pOut);
    }

    public void drawValues(Canvas c) {
        float phaseX = this.mAnimator.getPhaseX();
        float phaseY = this.mAnimator.getPhaseY();
        float sliceangle = this.mChart.getSliceAngle();
        float factor = this.mChart.getFactor();
        MPPointF center = this.mChart.getCenterOffsets();
        MPPointF pOut = MPPointF.getInstance(0.0F, 0.0F);
        MPPointF pIcon = MPPointF.getInstance(0.0F, 0.0F);
        float yoffset = Utils.convertDpToPixel(5.0F);

        for(int i = 0; i < this.mChart.getData().getDataSetCount(); ++i) {
            IRadarDataSet dataSet = this.mChart.getData().getDataSetByIndex(i);
            if (this.shouldDrawValues(dataSet)) {
                this.applyValueTextStyle(dataSet);
                ValueFormatter formatter = dataSet.getValueFormatter();
                MPPointF iconsOffset = MPPointF.getInstance(dataSet.getIconsOffset());
                iconsOffset.x = Utils.convertDpToPixel(iconsOffset.x);
                iconsOffset.y = Utils.convertDpToPixel(iconsOffset.y);

                for(int j = 0; j < dataSet.getEntryCount(); ++j) {
                    RadarEntry entry = dataSet.getEntryForIndex(j);
                    Utils.getPosition(center, (entry.getY() - this.mChart.getYChartMin()) * factor * phaseY, sliceangle * (float)j * phaseX + this.mChart.getRotationAngle(), pOut);
                    if (dataSet.isDrawValuesEnabled()) {
                        this.drawValue(c, formatter.getRadarLabel(entry), pOut.x, pOut.y - yoffset, dataSet.getValueTextColor(j));
                    }

                    if (entry.getIcon() != null && dataSet.isDrawIconsEnabled()) {
                        Drawable icon = entry.getIcon();
                        Utils.getPosition(center, entry.getY() * factor * phaseY + iconsOffset.y, sliceangle * (float)j * phaseX + this.mChart.getRotationAngle(), pIcon);
                        pIcon.y += iconsOffset.x;
                        Utils.drawImage(c, icon, (int)pIcon.x, (int)pIcon.y, icon.getIntrinsicWidth(), icon.getIntrinsicHeight());
                    }
                }

                MPPointF.recycleInstance(iconsOffset);
            }
        }

        MPPointF.recycleInstance(center);
        MPPointF.recycleInstance(pOut);
        MPPointF.recycleInstance(pIcon);
    }

    public void drawValue(Canvas c, String valueText, float x, float y, int color) {
        this.mValuePaint.setColor(color);
        c.drawText(valueText, x, y, this.mValuePaint);
    }

    public void drawExtras(Canvas c) {
        this.drawWeb(c);
    }

    protected void drawWeb(Canvas c) {
        float sliceangle = this.mChart.getSliceAngle();
        float factor = this.mChart.getFactor();
        float rotationangle = this.mChart.getRotationAngle();
        MPPointF center = this.mChart.getCenterOffsets();
        this.mWebPaint.setStrokeWidth(this.mChart.getWebLineWidth());
        this.mWebPaint.setColor(this.mChart.getWebColor());
        this.mWebPaint.setAlpha(this.mChart.getWebAlpha());
        int xIncrements = 1 + this.mChart.getSkipWebLineCount();
        int maxEntryCount = this.mChart.getData().getMaxEntryCountSet().getEntryCount();
        MPPointF p = MPPointF.getInstance(0.0F, 0.0F);

        int labelCount;
        for(labelCount = 0; labelCount < maxEntryCount; labelCount += xIncrements) {
            Utils.getPosition(center, this.mChart.getYRange() * factor, sliceangle * (float)labelCount + rotationangle, p);
            c.drawLine(center.x, center.y, p.x, p.y, this.mWebPaint);
        }

        MPPointF.recycleInstance(p);
        this.mWebPaint.setStrokeWidth(this.mChart.getWebLineWidthInner());
        this.mWebPaint.setColor(this.mChart.getWebColorInner());
        this.mWebPaint.setAlpha(this.mChart.getWebAlpha());
        labelCount = this.mChart.getYAxis().mEntryCount;
        MPPointF p1out = MPPointF.getInstance(0.0F, 0.0F);
        MPPointF p2out = MPPointF.getInstance(0.0F, 0.0F);

        for(int j = 0; j < labelCount; ++j) {
            for(int i = 0; i < this.mChart.getData().getEntryCount(); ++i) {
                float r = (this.mChart.getYAxis().mEntries[j] - this.mChart.getYChartMin()) * factor;
                Utils.getPosition(center, r, sliceangle * (float)i + rotationangle, p1out);
                Utils.getPosition(center, r, sliceangle * (float)(i + 1) + rotationangle, p2out);
                c.drawLine(p1out.x, p1out.y, p2out.x, p2out.y, this.mWebPaint);
            }
        }

        MPPointF.recycleInstance(p1out);
        MPPointF.recycleInstance(p2out);
    }

    public void drawHighlighted(Canvas c, Highlight[] indices) {
        float sliceangle = this.mChart.getSliceAngle();
        float factor = this.mChart.getFactor();
        MPPointF center = this.mChart.getCenterOffsets();
        MPPointF pOut = MPPointF.getInstance(0.0F, 0.0F);
        RadarData radarData = this.mChart.getData();
        Highlight[] var8 = indices;
        int var9 = indices.length;

        for(int var10 = 0; var10 < var9; ++var10) {
            Highlight high = var8[var10];
            IRadarDataSet set = radarData.getDataSetByIndex(high.getDataSetIndex());
            if (set != null && set.isHighlightEnabled()) {
                RadarEntry e = set.getEntryForIndex((int)high.getX());
                if (this.isInBoundsX(e, set)) {
                    float y = e.getY() - this.mChart.getYChartMin();
                    Utils.getPosition(center, y * factor * this.mAnimator.getPhaseY(), sliceangle * high.getX() * this.mAnimator.getPhaseX() + this.mChart.getRotationAngle(), pOut);
                    high.setDraw(pOut.x, pOut.y);
                    this.drawHighlightLines(c, pOut.x, pOut.y, set);
                    if (set.isDrawHighlightCircleEnabled() && !Float.isNaN(pOut.x) && !Float.isNaN(pOut.y)) {
                        int strokeColor = set.getHighlightCircleStrokeColor();
                        if (strokeColor == 1122867) {
                            strokeColor = set.getColor(0);
                        }

                        if (set.getHighlightCircleStrokeAlpha() < 255) {
                            strokeColor = ColorTemplate.colorWithAlpha(strokeColor, set.getHighlightCircleStrokeAlpha());
                        }

                        this.drawHighlightCircle(c, pOut, set.getHighlightCircleInnerRadius(), set.getHighlightCircleOuterRadius(), set.getHighlightCircleFillColor(), strokeColor, set.getHighlightCircleStrokeWidth());
                    }
                }
            }
        }

        MPPointF.recycleInstance(center);
        MPPointF.recycleInstance(pOut);
    }

    public void drawHighlightCircle(Canvas c, MPPointF point, float innerRadius, float outerRadius, int fillColor, int strokeColor, float strokeWidth) {
        c.save();
        outerRadius = Utils.convertDpToPixel(outerRadius);
        innerRadius = Utils.convertDpToPixel(innerRadius);
        if (fillColor != 1122867) {
            Path p = this.mDrawHighlightCirclePathBuffer;
            p.reset();
            p.addCircle(point.x, point.y, outerRadius, Path.Direction.CW);
            if (innerRadius > 0.0F) {
                p.addCircle(point.x, point.y, innerRadius, Path.Direction.CCW);
            }

            this.mHighlightCirclePaint.setColor(fillColor);
            this.mHighlightCirclePaint.setStyle(Paint.Style.FILL);
            c.drawPath(p, this.mHighlightCirclePaint);
        }

        if (strokeColor != 1122867) {
            this.mHighlightCirclePaint.setColor(strokeColor);
            this.mHighlightCirclePaint.setStyle(Paint.Style.STROKE);
            this.mHighlightCirclePaint.setStrokeWidth(Utils.convertDpToPixel(strokeWidth));
            c.drawCircle(point.x, point.y, outerRadius, this.mHighlightCirclePaint);
        }

        c.restore();
    }
}
