package kore.botssdk.charts.renderer;

import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.Log;

import java.util.Iterator;
import java.util.List;

import kore.botssdk.charts.animation.ChartAnimator;
import kore.botssdk.charts.data.Entry;
import kore.botssdk.charts.data.ScatterData;
import kore.botssdk.charts.formatter.ValueFormatter;
import kore.botssdk.charts.highlight.Highlight;
import kore.botssdk.charts.interfaces.dataprovider.ScatterDataProvider;
import kore.botssdk.charts.interfaces.datasets.IScatterDataSet;
import kore.botssdk.charts.renderer.scatter.IShapeRenderer;
import kore.botssdk.charts.utils.MPPointD;
import kore.botssdk.charts.utils.MPPointF;
import kore.botssdk.charts.utils.Transformer;
import kore.botssdk.charts.utils.Utils;
import kore.botssdk.charts.utils.ViewPortHandler;
import kore.botssdk.utils.LogUtils;

public class ScatterChartRenderer extends LineScatterCandleRadarRenderer {
    protected final ScatterDataProvider mChart;
    final float[] mPixelBuffer = new float[2];

    public ScatterChartRenderer(ScatterDataProvider chart, ChartAnimator animator, ViewPortHandler viewPortHandler) {
        super(animator, viewPortHandler);
        this.mChart = chart;
    }

    public void initBuffers() {
    }

    public void drawData(Canvas c) {
        ScatterData scatterData = this.mChart.getScatterData();
        Iterator var3 = scatterData.getDataSets().iterator();

        while(var3.hasNext()) {
            IScatterDataSet set = (IScatterDataSet)var3.next();
            if (set.isVisible()) {
                this.drawDataSet(c, set);
            }
        }

    }

    protected void drawDataSet(Canvas c, IScatterDataSet dataSet) {
        if (dataSet.getEntryCount() >= 1) {
            ViewPortHandler viewPortHandler = this.mViewPortHandler;
            Transformer trans = this.mChart.getTransformer(dataSet.getAxisDependency());
            float phaseY = this.mAnimator.getPhaseY();
            IShapeRenderer renderer = dataSet.getShapeRenderer();
            if (renderer == null) {
                LogUtils.i("MISSING", "There's no IShapeRenderer specified for ScatterDataSet");
            } else {
                int max = (int)Math.min(Math.ceil((float)dataSet.getEntryCount() * this.mAnimator.getPhaseX()), (float)dataSet.getEntryCount());

                for(int i = 0; i < max; ++i) {
                    Entry e = dataSet.getEntryForIndex(i);
                    this.mPixelBuffer[0] = e.getX();
                    this.mPixelBuffer[1] = e.getY() * phaseY;
                    trans.pointValuesToPixel(this.mPixelBuffer);
                    if (!viewPortHandler.isInBoundsRight(this.mPixelBuffer[0])) {
                        break;
                    }

                    if (viewPortHandler.isInBoundsLeft(this.mPixelBuffer[0]) && viewPortHandler.isInBoundsY(this.mPixelBuffer[1])) {
                        this.mRenderPaint.setColor(dataSet.getColor(i / 2));
                        renderer.renderShape(c, dataSet, this.mViewPortHandler, this.mPixelBuffer[0], this.mPixelBuffer[1], this.mRenderPaint);
                    }
                }

            }
        }
    }

    public void drawValues(Canvas c) {
        if (this.isDrawingValuesAllowed(this.mChart)) {
            List<IScatterDataSet> dataSets = this.mChart.getScatterData().getDataSets();

            for(int i = 0; i < this.mChart.getScatterData().getDataSetCount(); ++i) {
                IScatterDataSet dataSet = dataSets.get(i);
                if (this.shouldDrawValues(dataSet) && dataSet.getEntryCount() >= 1) {
                    this.applyValueTextStyle(dataSet);
                    this.mXBounds.set(this.mChart, dataSet);
                    float[] positions = this.mChart.getTransformer(dataSet.getAxisDependency()).generateTransformedValuesScatter(dataSet, this.mAnimator.getPhaseX(), this.mAnimator.getPhaseY(), this.mXBounds.min, this.mXBounds.max);
                    float shapeSize = Utils.convertDpToPixel(dataSet.getScatterShapeSize());
                    ValueFormatter formatter = dataSet.getValueFormatter();
                    MPPointF iconsOffset = MPPointF.getInstance(dataSet.getIconsOffset());
                    iconsOffset.x = Utils.convertDpToPixel(iconsOffset.x);
                    iconsOffset.y = Utils.convertDpToPixel(iconsOffset.y);

                    for(int j = 0; j < positions.length && this.mViewPortHandler.isInBoundsRight(positions[j]); j += 2) {
                        if (this.mViewPortHandler.isInBoundsLeft(positions[j]) && this.mViewPortHandler.isInBoundsY(positions[j + 1])) {
                            Entry entry = dataSet.getEntryForIndex(j / 2 + this.mXBounds.min);
                            if (dataSet.isDrawValuesEnabled()) {
                                this.drawValue(c, formatter.getPointLabel(entry), positions[j], positions[j + 1] - shapeSize, dataSet.getValueTextColor(j / 2 + this.mXBounds.min));
                            }

                            if (entry.getIcon() != null && dataSet.isDrawIconsEnabled()) {
                                Drawable icon = entry.getIcon();
                                Utils.drawImage(c, icon, (int)(positions[j] + iconsOffset.x), (int)(positions[j + 1] + iconsOffset.y), icon.getIntrinsicWidth(), icon.getIntrinsicHeight());
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
        ScatterData scatterData = this.mChart.getScatterData();
        Highlight[] var4 = indices;
        int var5 = indices.length;

        for(int var6 = 0; var6 < var5; ++var6) {
            Highlight high = var4[var6];
            IScatterDataSet set = scatterData.getDataSetByIndex(high.getDataSetIndex());
            if (set != null && set.isHighlightEnabled()) {
                Entry e = set.getEntryForXValue(high.getX(), high.getY());
                if (this.isInBoundsX(e, set)) {
                    MPPointD pix = this.mChart.getTransformer(set.getAxisDependency()).getPixelForValues(e.getX(), e.getY() * this.mAnimator.getPhaseY());
                    high.setDraw((float)pix.x, (float)pix.y);
                    this.drawHighlightLines(c, (float)pix.x, (float)pix.y, set);
                }
            }
        }

    }
}
