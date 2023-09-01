package kore.botssdk.charts.renderer;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.drawable.Drawable;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import kore.botssdk.charts.animation.ChartAnimator;
import kore.botssdk.charts.data.Entry;
import kore.botssdk.charts.data.LineData;
import kore.botssdk.charts.data.LineDataSet;
import kore.botssdk.charts.formatter.ValueFormatter;
import kore.botssdk.charts.highlight.Highlight;
import kore.botssdk.charts.interfaces.dataprovider.LineDataProvider;
import kore.botssdk.charts.interfaces.datasets.IDataSet;
import kore.botssdk.charts.interfaces.datasets.ILineDataSet;
import kore.botssdk.charts.utils.MPPointD;
import kore.botssdk.charts.utils.MPPointF;
import kore.botssdk.charts.utils.Transformer;
import kore.botssdk.charts.utils.Utils;
import kore.botssdk.charts.utils.ViewPortHandler;

public class LineChartRenderer extends LineRadarRenderer {
    protected final LineDataProvider mChart;
    protected final Paint mCirclePaintInner;
    protected WeakReference<Bitmap> mDrawBitmap;
    protected Canvas mBitmapCanvas;
    protected Bitmap.Config mBitmapConfig;
    protected final Path cubicPath;
    protected final Path cubicFillPath;
    private float[] mLineBuffer;
    protected final Path mGenerateFilledPathBuffer;
    private final HashMap<IDataSet, LineChartRenderer.DataSetImageCache> mImageCaches;
    private final float[] mCirclesBuffer;

    public LineChartRenderer(LineDataProvider chart, ChartAnimator animator, ViewPortHandler viewPortHandler) {
        super(animator, viewPortHandler);
        this.mBitmapConfig = Bitmap.Config.ARGB_8888;
        this.cubicPath = new Path();
        this.cubicFillPath = new Path();
        this.mLineBuffer = new float[4];
        this.mGenerateFilledPathBuffer = new Path();
        this.mImageCaches = new HashMap();
        this.mCirclesBuffer = new float[2];
        this.mChart = chart;
        this.mCirclePaintInner = new Paint(1);
        this.mCirclePaintInner.setStyle(Paint.Style.FILL);
        this.mCirclePaintInner.setColor(-1);
    }

    public void initBuffers() {
    }

    public void drawData(Canvas c) {
        int width = (int)this.mViewPortHandler.getChartWidth();
        int height = (int)this.mViewPortHandler.getChartHeight();
        Bitmap drawBitmap = this.mDrawBitmap == null ? null : this.mDrawBitmap.get();
        if (drawBitmap == null || drawBitmap.getWidth() != width || drawBitmap.getHeight() != height) {
            if (width <= 0 || height <= 0) {
                return;
            }

            drawBitmap = Bitmap.createBitmap(width, height, this.mBitmapConfig);
            this.mDrawBitmap = new WeakReference(drawBitmap);
            this.mBitmapCanvas = new Canvas(drawBitmap);
        }

        drawBitmap.eraseColor(0);
        LineData lineData = this.mChart.getLineData();
        Iterator var6 = lineData.getDataSets().iterator();

        while(var6.hasNext()) {
            ILineDataSet set = (ILineDataSet)var6.next();
            if (set.isVisible()) {
                this.drawDataSet(c, set);
            }
        }

        c.drawBitmap(drawBitmap, 0.0F, 0.0F, this.mRenderPaint);
    }

    protected void drawDataSet(Canvas c, ILineDataSet dataSet) {
        if (dataSet.getEntryCount() >= 1) {
            this.mRenderPaint.setStrokeWidth(dataSet.getLineWidth());
            this.mRenderPaint.setPathEffect(dataSet.getDashPathEffect());
            switch(dataSet.getMode()) {
                case LINEAR:
                case STEPPED:
                default:
                    this.drawLinear(c, dataSet);
                    break;
                case CUBIC_BEZIER:
                    this.drawCubicBezier(dataSet);
                    break;
                case HORIZONTAL_BEZIER:
                    this.drawHorizontalBezier(dataSet);
            }

            this.mRenderPaint.setPathEffect(null);
        }
    }

    protected void drawHorizontalBezier(ILineDataSet dataSet) {
        float phaseY = this.mAnimator.getPhaseY();
        Transformer trans = this.mChart.getTransformer(dataSet.getAxisDependency());
        this.mXBounds.set(this.mChart, dataSet);
        this.cubicPath.reset();
        if (this.mXBounds.range >= 1) {
            Entry prev = dataSet.getEntryForIndex(this.mXBounds.min);
            Entry cur = prev;
            this.cubicPath.moveTo(prev.getX(), prev.getY() * phaseY);

            for(int j = this.mXBounds.min + 1; j <= this.mXBounds.range + this.mXBounds.min; ++j) {
                prev = cur;
                cur = dataSet.getEntryForIndex(j);
                float cpx = prev.getX() + (cur.getX() - prev.getX()) / 2.0F;
                this.cubicPath.cubicTo(cpx, prev.getY() * phaseY, cpx, cur.getY() * phaseY, cur.getX(), cur.getY() * phaseY);
            }
        }

        if (dataSet.isDrawFilledEnabled()) {
            this.cubicFillPath.reset();
            this.cubicFillPath.addPath(this.cubicPath);
            this.drawCubicFill(this.mBitmapCanvas, dataSet, this.cubicFillPath, trans, this.mXBounds);
        }

        this.mRenderPaint.setColor(dataSet.getColor());
        this.mRenderPaint.setStyle(Paint.Style.STROKE);
        trans.pathValueToPixel(this.cubicPath);
        this.mBitmapCanvas.drawPath(this.cubicPath, this.mRenderPaint);
        this.mRenderPaint.setPathEffect(null);
    }

    protected void drawCubicBezier(ILineDataSet dataSet) {
        float phaseY = this.mAnimator.getPhaseY();
        Transformer trans = this.mChart.getTransformer(dataSet.getAxisDependency());
        this.mXBounds.set(this.mChart, dataSet);
        float intensity = dataSet.getCubicIntensity();
        this.cubicPath.reset();
        if (this.mXBounds.range >= 1) {
            float prevDx = 0.0F;
            float prevDy = 0.0F;
            float curDx = 0.0F;
            float curDy = 0.0F;
            int firstIndex = this.mXBounds.min + 1;
            int var10000 = this.mXBounds.min + this.mXBounds.range;
            Entry prev = dataSet.getEntryForIndex(Math.max(firstIndex - 2, 0));
            Entry cur = dataSet.getEntryForIndex(Math.max(firstIndex - 1, 0));
            Entry next = cur;
            int nextIndex = -1;
            if (cur == null) {
                return;
            }

            this.cubicPath.moveTo(cur.getX(), cur.getY() * phaseY);

            for(int j = this.mXBounds.min + 1; j <= this.mXBounds.range + this.mXBounds.min; ++j) {
                Entry prevPrev = prev;
                prev = cur;
                cur = nextIndex == j ? next : dataSet.getEntryForIndex(j);
                nextIndex = j + 1 < dataSet.getEntryCount() ? j + 1 : j;
                next = dataSet.getEntryForIndex(nextIndex);
                prevDx = (cur.getX() - prevPrev.getX()) * intensity;
                prevDy = (cur.getY() - prevPrev.getY()) * intensity;
                curDx = (next.getX() - prev.getX()) * intensity;
                curDy = (next.getY() - prev.getY()) * intensity;
                this.cubicPath.cubicTo(prev.getX() + prevDx, (prev.getY() + prevDy) * phaseY, cur.getX() - curDx, (cur.getY() - curDy) * phaseY, cur.getX(), cur.getY() * phaseY);
            }
        }

        if (dataSet.isDrawFilledEnabled()) {
            this.cubicFillPath.reset();
            this.cubicFillPath.addPath(this.cubicPath);
            this.drawCubicFill(this.mBitmapCanvas, dataSet, this.cubicFillPath, trans, this.mXBounds);
        }

        this.mRenderPaint.setColor(dataSet.getColor());
        this.mRenderPaint.setStyle(Paint.Style.STROKE);
        trans.pathValueToPixel(this.cubicPath);
        this.mBitmapCanvas.drawPath(this.cubicPath, this.mRenderPaint);
        this.mRenderPaint.setPathEffect(null);
    }

    protected void drawCubicFill(Canvas c, ILineDataSet dataSet, Path spline, Transformer trans, XBounds bounds) {
        float fillMin = dataSet.getFillFormatter().getFillLinePosition(dataSet, this.mChart);
        spline.lineTo(dataSet.getEntryForIndex(bounds.min + bounds.range).getX(), fillMin);
        spline.lineTo(dataSet.getEntryForIndex(bounds.min).getX(), fillMin);
        spline.close();
        trans.pathValueToPixel(spline);
        Drawable drawable = dataSet.getFillDrawable();
        if (drawable != null) {
            this.drawFilledPath(c, spline, drawable);
        } else {
            this.drawFilledPath(c, spline, dataSet.getFillColor(), dataSet.getFillAlpha());
        }

    }

    protected void drawLinear(Canvas c, ILineDataSet dataSet) {
        int entryCount = dataSet.getEntryCount();
        boolean isDrawSteppedEnabled = dataSet.getMode() == LineDataSet.Mode.STEPPED;
        int pointsPerEntryPair = isDrawSteppedEnabled ? 4 : 2;
        Transformer trans = this.mChart.getTransformer(dataSet.getAxisDependency());
        float phaseY = this.mAnimator.getPhaseY();
        this.mRenderPaint.setStyle(Paint.Style.STROKE);
        Canvas canvas = null;
        if (dataSet.isDashedLineEnabled()) {
            canvas = this.mBitmapCanvas;
        } else {
            canvas = c;
        }

        this.mXBounds.set(this.mChart, dataSet);
        if (dataSet.isDrawFilledEnabled() && entryCount > 0) {
            this.drawLinearFill(c, dataSet, trans, this.mXBounds);
        }

        Entry e;
        if (dataSet.getColors().size() > 1) {
            if (this.mLineBuffer.length <= pointsPerEntryPair * 2) {
                this.mLineBuffer = new float[pointsPerEntryPair * 4];
            }

            for(int j = this.mXBounds.min; j <= this.mXBounds.range + this.mXBounds.min; ++j) {
                e = dataSet.getEntryForIndex(j);
                if (e != null) {
                    this.mLineBuffer[0] = e.getX();
                    this.mLineBuffer[1] = e.getY() * phaseY;
                    if (j < this.mXBounds.max) {
                        e = dataSet.getEntryForIndex(j + 1);
                        if (e == null) {
                            break;
                        }

                        if (isDrawSteppedEnabled) {
                            this.mLineBuffer[2] = e.getX();
                            this.mLineBuffer[3] = this.mLineBuffer[1];
                            this.mLineBuffer[4] = this.mLineBuffer[2];
                            this.mLineBuffer[5] = this.mLineBuffer[3];
                            this.mLineBuffer[6] = e.getX();
                            this.mLineBuffer[7] = e.getY() * phaseY;
                        } else {
                            this.mLineBuffer[2] = e.getX();
                            this.mLineBuffer[3] = e.getY() * phaseY;
                        }
                    } else {
                        this.mLineBuffer[2] = this.mLineBuffer[0];
                        this.mLineBuffer[3] = this.mLineBuffer[1];
                    }

                    trans.pointValuesToPixel(this.mLineBuffer);
                    if (!this.mViewPortHandler.isInBoundsRight(this.mLineBuffer[0])) {
                        break;
                    }

                    if (this.mViewPortHandler.isInBoundsLeft(this.mLineBuffer[2]) && (this.mViewPortHandler.isInBoundsTop(this.mLineBuffer[1]) || this.mViewPortHandler.isInBoundsBottom(this.mLineBuffer[3]))) {
                        this.mRenderPaint.setColor(dataSet.getColor(j));
                        canvas.drawLines(this.mLineBuffer, 0, pointsPerEntryPair * 2, this.mRenderPaint);
                    }
                }
            }
        } else {
            if (this.mLineBuffer.length < Math.max(entryCount * pointsPerEntryPair, pointsPerEntryPair) * 2) {
                this.mLineBuffer = new float[Math.max(entryCount * pointsPerEntryPair, pointsPerEntryPair) * 4];
            }

            Entry e1 = dataSet.getEntryForIndex(this.mXBounds.min);
            if (e1 != null) {
                int j = 0;

                int size;
                for(size = this.mXBounds.min; size <= this.mXBounds.range + this.mXBounds.min; ++size) {
                    e1 = dataSet.getEntryForIndex(size == 0 ? 0 : size - 1);
                    e = dataSet.getEntryForIndex(size);
                    if (e1 != null && e != null) {
                        this.mLineBuffer[j++] = e1.getX();
                        this.mLineBuffer[j++] = e1.getY() * phaseY;
                        if (isDrawSteppedEnabled) {
                            this.mLineBuffer[j++] = e.getX();
                            this.mLineBuffer[j++] = e1.getY() * phaseY;
                            this.mLineBuffer[j++] = e.getX();
                            this.mLineBuffer[j++] = e1.getY() * phaseY;
                        }

                        this.mLineBuffer[j++] = e.getX();
                        this.mLineBuffer[j++] = e.getY() * phaseY;
                    }
                }

                if (j > 0) {
                    trans.pointValuesToPixel(this.mLineBuffer);
                    size = Math.max((this.mXBounds.range + 1) * pointsPerEntryPair, pointsPerEntryPair) * 2;
                    this.mRenderPaint.setColor(dataSet.getColor());
                    canvas.drawLines(this.mLineBuffer, 0, size, this.mRenderPaint);
                }
            }
        }

        this.mRenderPaint.setPathEffect(null);
    }

    protected void drawLinearFill(Canvas c, ILineDataSet dataSet, Transformer trans, XBounds bounds) {
        Path filled = this.mGenerateFilledPathBuffer;
        int startingIndex = bounds.min;
        int endingIndex = bounds.range + bounds.min;
        int iterations = 0;

        int currentEndIndex;
        int currentStartIndex;
        do {
            currentStartIndex = startingIndex + iterations * 128;
            currentEndIndex = currentStartIndex + 128;
            currentEndIndex = currentEndIndex > endingIndex ? endingIndex : currentEndIndex;
            if (currentStartIndex <= currentEndIndex) {
                this.generateFilledPath(dataSet, currentStartIndex, currentEndIndex, filled);
                trans.pathValueToPixel(filled);
                Drawable drawable = dataSet.getFillDrawable();
                if (drawable != null) {
                    this.drawFilledPath(c, filled, drawable);
                } else {
                    this.drawFilledPath(c, filled, dataSet.getFillColor(), dataSet.getFillAlpha());
                }
            }

            ++iterations;
        } while(currentStartIndex <= currentEndIndex);

    }

    private void generateFilledPath(ILineDataSet dataSet, int startIndex, int endIndex, Path outputPath) {
        float fillMin = dataSet.getFillFormatter().getFillLinePosition(dataSet, this.mChart);
        float phaseY = this.mAnimator.getPhaseY();
        boolean isDrawSteppedEnabled = dataSet.getMode() == LineDataSet.Mode.STEPPED;
        Path filled = outputPath;
        outputPath.reset();
        Entry entry = dataSet.getEntryForIndex(startIndex);
        outputPath.moveTo(entry.getX(), fillMin);
        outputPath.lineTo(entry.getX(), entry.getY() * phaseY);
        Entry currentEntry = null;
        Entry previousEntry = entry;

        for(int x = startIndex + 1; x <= endIndex; ++x) {
            currentEntry = dataSet.getEntryForIndex(x);
            if (isDrawSteppedEnabled) {
                filled.lineTo(currentEntry.getX(), previousEntry.getY() * phaseY);
            }

            filled.lineTo(currentEntry.getX(), currentEntry.getY() * phaseY);
            previousEntry = currentEntry;
        }

        if (currentEntry != null) {
            filled.lineTo(currentEntry.getX(), fillMin);
        }

        filled.close();
    }

    public void drawValues(Canvas c) {
        if (this.isDrawingValuesAllowed(this.mChart)) {
            List<ILineDataSet> dataSets = this.mChart.getLineData().getDataSets();

            for(int i = 0; i < dataSets.size(); ++i) {
                ILineDataSet dataSet = dataSets.get(i);
                if (this.shouldDrawValues(dataSet) && dataSet.getEntryCount() >= 1) {
                    this.applyValueTextStyle(dataSet);
                    Transformer trans = this.mChart.getTransformer(dataSet.getAxisDependency());
                    int valOffset = (int)(dataSet.getCircleRadius() * 1.75F);
                    if (!dataSet.isDrawCirclesEnabled()) {
                        valOffset /= 2;
                    }

                    this.mXBounds.set(this.mChart, dataSet);
                    float[] positions = trans.generateTransformedValuesLine(dataSet, this.mAnimator.getPhaseX(), this.mAnimator.getPhaseY(), this.mXBounds.min, this.mXBounds.max);
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
                            Entry entry = dataSet.getEntryForIndex(j / 2 + this.mXBounds.min);
                            if (dataSet.isDrawValuesEnabled()) {
                                this.drawValue(c, formatter.getPointLabel(entry), x, y - (float)valOffset, dataSet.getValueTextColor(j / 2));
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
        this.drawCircles(c);
    }

    protected void drawCircles(Canvas c) {
        this.mRenderPaint.setStyle(Paint.Style.FILL);
        float phaseY = this.mAnimator.getPhaseY();
        this.mCirclesBuffer[0] = 0.0F;
        this.mCirclesBuffer[1] = 0.0F;
        List<ILineDataSet> dataSets = this.mChart.getLineData().getDataSets();

        for(int i = 0; i < dataSets.size(); ++i) {
            ILineDataSet dataSet = dataSets.get(i);
            if (dataSet.isVisible() && dataSet.isDrawCirclesEnabled() && dataSet.getEntryCount() != 0) {
                this.mCirclePaintInner.setColor(dataSet.getCircleHoleColor());
                Transformer trans = this.mChart.getTransformer(dataSet.getAxisDependency());
                this.mXBounds.set(this.mChart, dataSet);
                float circleRadius = dataSet.getCircleRadius();
                float circleHoleRadius = dataSet.getCircleHoleRadius();
                boolean drawCircleHole = dataSet.isDrawCircleHoleEnabled() && circleHoleRadius < circleRadius && circleHoleRadius > 0.0F;
                boolean drawTransparentCircleHole = drawCircleHole && dataSet.getCircleHoleColor() == 1122867;
                LineChartRenderer.DataSetImageCache imageCache;
                if (this.mImageCaches.containsKey(dataSet)) {
                    imageCache = this.mImageCaches.get(dataSet);
                } else {
                    imageCache = new LineChartRenderer.DataSetImageCache();
                    this.mImageCaches.put(dataSet, imageCache);
                }

                boolean changeRequired = imageCache.init(dataSet);
                if (changeRequired) {
                    imageCache.fill(dataSet, drawCircleHole, drawTransparentCircleHole);
                }

                int boundsRangeCount = this.mXBounds.range + this.mXBounds.min;

                for(int j = this.mXBounds.min; j <= boundsRangeCount; ++j) {
                    Entry e = dataSet.getEntryForIndex(j);
                    if (e == null) {
                        break;
                    }

                    this.mCirclesBuffer[0] = e.getX();
                    this.mCirclesBuffer[1] = e.getY() * phaseY;
                    trans.pointValuesToPixel(this.mCirclesBuffer);
                    if (!this.mViewPortHandler.isInBoundsRight(this.mCirclesBuffer[0])) {
                        break;
                    }

                    if (this.mViewPortHandler.isInBoundsLeft(this.mCirclesBuffer[0]) && this.mViewPortHandler.isInBoundsY(this.mCirclesBuffer[1])) {
                        Bitmap circleBitmap = imageCache.getBitmap(j);
                        if (circleBitmap != null) {
                            c.drawBitmap(circleBitmap, this.mCirclesBuffer[0] - circleRadius, this.mCirclesBuffer[1] - circleRadius, null);
                        }
                    }
                }
            }
        }

    }

    public void drawHighlighted(Canvas c, Highlight[] indices) {
        LineData lineData = this.mChart.getLineData();
        Highlight[] var4 = indices;
        int var5 = indices.length;

        for(int var6 = 0; var6 < var5; ++var6) {
            Highlight high = var4[var6];
            ILineDataSet set = lineData.getDataSetByIndex(high.getDataSetIndex());
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

    public void setBitmapConfig(Bitmap.Config config) {
        this.mBitmapConfig = config;
        this.releaseBitmap();
    }

    public Bitmap.Config getBitmapConfig() {
        return this.mBitmapConfig;
    }

    public void releaseBitmap() {
        if (this.mBitmapCanvas != null) {
            this.mBitmapCanvas.setBitmap(null);
            this.mBitmapCanvas = null;
        }

        if (this.mDrawBitmap != null) {
            Bitmap drawBitmap = this.mDrawBitmap.get();
            if (drawBitmap != null) {
                drawBitmap.recycle();
            }

            this.mDrawBitmap.clear();
            this.mDrawBitmap = null;
        }

    }

    private class DataSetImageCache {
        private final Path mCirclePathBuffer;
        private Bitmap[] circleBitmaps;

        private DataSetImageCache() {
            this.mCirclePathBuffer = new Path();
        }

        protected boolean init(ILineDataSet set) {
            int size = set.getCircleColorCount();
            boolean changeRequired = false;
            if (this.circleBitmaps == null) {
                this.circleBitmaps = new Bitmap[size];
                changeRequired = true;
            } else if (this.circleBitmaps.length != size) {
                this.circleBitmaps = new Bitmap[size];
                changeRequired = true;
            }

            return changeRequired;
        }

        protected void fill(ILineDataSet set, boolean drawCircleHole, boolean drawTransparentCircleHole) {
            int colorCount = set.getCircleColorCount();
            float circleRadius = set.getCircleRadius();
            float circleHoleRadius = set.getCircleHoleRadius();

            for(int i = 0; i < colorCount; ++i) {
                Bitmap.Config conf = Bitmap.Config.ARGB_4444;
                Bitmap circleBitmap = Bitmap.createBitmap((int)((double)circleRadius * 2.1D), (int)((double)circleRadius * 2.1D), conf);
                Canvas canvas = new Canvas(circleBitmap);
                this.circleBitmaps[i] = circleBitmap;
                LineChartRenderer.this.mRenderPaint.setColor(set.getCircleColor(i));
                if (drawTransparentCircleHole) {
                    this.mCirclePathBuffer.reset();
                    this.mCirclePathBuffer.addCircle(circleRadius, circleRadius, circleRadius, Path.Direction.CW);
                    this.mCirclePathBuffer.addCircle(circleRadius, circleRadius, circleHoleRadius, Path.Direction.CCW);
                    canvas.drawPath(this.mCirclePathBuffer, LineChartRenderer.this.mRenderPaint);
                } else {
                    canvas.drawCircle(circleRadius, circleRadius, circleRadius, LineChartRenderer.this.mRenderPaint);
                    if (drawCircleHole) {
                        canvas.drawCircle(circleRadius, circleRadius, circleHoleRadius, LineChartRenderer.this.mCirclePaintInner);
                    }
                }
            }

        }

        protected Bitmap getBitmap(int index) {
            return this.circleBitmaps[index % this.circleBitmaps.length];
        }
    }
}
