package kore.botssdk.charts.charts;

import static kore.botssdk.charts.components.Legend.LegendVerticalAlignment.BOTTOM;
import static kore.botssdk.charts.components.Legend.LegendVerticalAlignment.TOP;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import kore.botssdk.charts.components.XAxis;
import kore.botssdk.charts.components.YAxis;
import kore.botssdk.charts.data.BarLineScatterCandleBubbleData;
import kore.botssdk.charts.data.Entry;
import kore.botssdk.charts.highlight.ChartHighlighter;
import kore.botssdk.charts.highlight.Highlight;
import kore.botssdk.charts.interfaces.dataprovider.BarLineScatterCandleBubbleDataProvider;
import kore.botssdk.charts.interfaces.datasets.IBarLineScatterCandleBubbleDataSet;
import kore.botssdk.charts.jobs.AnimatedMoveViewJob;
import kore.botssdk.charts.jobs.AnimatedZoomJob;
import kore.botssdk.charts.jobs.MoveViewJob;
import kore.botssdk.charts.jobs.ZoomJob;
import kore.botssdk.charts.listener.BarLineChartTouchListener;
import kore.botssdk.charts.listener.OnDrawListener;
import kore.botssdk.charts.renderer.XAxisRenderer;
import kore.botssdk.charts.renderer.YAxisRenderer;
import kore.botssdk.charts.utils.MPPointD;
import kore.botssdk.charts.utils.MPPointF;
import kore.botssdk.charts.utils.Transformer;
import kore.botssdk.charts.utils.Utils;
import kore.botssdk.utils.LogUtils;

@SuppressLint({"RtlHardcoded"})
public abstract class BarLineChartBase<T extends BarLineScatterCandleBubbleData<? extends IBarLineScatterCandleBubbleDataSet<? extends Entry>>> extends Chart<T> implements BarLineScatterCandleBubbleDataProvider {
    protected int mMaxVisibleCount = 100;
    protected boolean mAutoScaleMinMaxEnabled = false;
    protected boolean mPinchZoomEnabled = false;
    protected boolean mDoubleTapToZoomEnabled = true;
    protected boolean mHighlightPerDragEnabled = true;
    private boolean mDragXEnabled = true;
    private boolean mDragYEnabled = true;
    private boolean mScaleXEnabled = true;
    private boolean mScaleYEnabled = true;
    protected Paint mGridBackgroundPaint;
    protected Paint mBorderPaint;
    protected boolean mDrawGridBackground = false;
    protected boolean mDrawBorders = false;
    protected boolean mClipValuesToContent = false;
    protected float mMinOffset = 15.0F;
    protected boolean mKeepPositionOnRotation = false;
    protected OnDrawListener mDrawListener;
    protected YAxis mAxisLeft;
    protected YAxis mAxisRight;
    protected YAxisRenderer mAxisRendererLeft;
    protected YAxisRenderer mAxisRendererRight;
    protected Transformer mLeftAxisTransformer;
    protected Transformer mRightAxisTransformer;
    protected XAxisRenderer mXAxisRenderer;
    private long totalTime = 0L;
    private long drawCycles = 0L;
    private final RectF mOffsetsBuffer = new RectF();
    protected final Matrix mZoomMatrixBuffer = new Matrix();
    protected final Matrix mFitScreenMatrixBuffer = new Matrix();
    private boolean mCustomViewPortEnabled = false;
    protected final float[] mGetPositionBuffer = new float[2];
    protected final MPPointD posForGetLowestVisibleX = MPPointD.getInstance(0.0D, 0.0D);
    protected final MPPointD posForGetHighestVisibleX = MPPointD.getInstance(0.0D, 0.0D);
    protected final float[] mOnSizeChangedBuffer = new float[2];

    public BarLineChartBase(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public BarLineChartBase(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BarLineChartBase(Context context) {
        super(context);
    }

    protected void init() {
        super.init();
        this.mAxisLeft = new YAxis(YAxis.AxisDependency.LEFT);
        this.mAxisRight = new YAxis(YAxis.AxisDependency.RIGHT);
        this.mLeftAxisTransformer = new Transformer(this.mViewPortHandler);
        this.mRightAxisTransformer = new Transformer(this.mViewPortHandler);
        this.mAxisRendererLeft = new YAxisRenderer(this.mViewPortHandler, this.mAxisLeft, this.mLeftAxisTransformer);
        this.mAxisRendererRight = new YAxisRenderer(this.mViewPortHandler, this.mAxisRight, this.mRightAxisTransformer);
        this.mXAxisRenderer = new XAxisRenderer(this.mViewPortHandler, this.mXAxis, this.mLeftAxisTransformer);
        this.setHighlighter(new ChartHighlighter(this));
        this.mChartTouchListener = new BarLineChartTouchListener(this, this.mViewPortHandler.getMatrixTouch(), 3.0F);
        this.mGridBackgroundPaint = new Paint();
        this.mGridBackgroundPaint.setStyle(Paint.Style.FILL);
        this.mGridBackgroundPaint.setColor(Color.rgb(240, 240, 240));
        this.mBorderPaint = new Paint();
        this.mBorderPaint.setStyle(Paint.Style.STROKE);
        this.mBorderPaint.setColor(-16777216);
        this.mBorderPaint.setStrokeWidth(Utils.convertDpToPixel(1.0F));
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (this.mData != null) {
            long starttime = System.currentTimeMillis();
            this.drawGridBackground(canvas);
            if (this.mAutoScaleMinMaxEnabled) {
                this.autoScale();
            }

            if (this.mAxisLeft.isEnabled()) {
                this.mAxisRendererLeft.computeAxis(this.mAxisLeft.mAxisMinimum, this.mAxisLeft.mAxisMaximum, this.mAxisLeft.isInverted());
            }

            if (this.mAxisRight.isEnabled()) {
                this.mAxisRendererRight.computeAxis(this.mAxisRight.mAxisMinimum, this.mAxisRight.mAxisMaximum, this.mAxisRight.isInverted());
            }

            if (this.mXAxis.isEnabled()) {
                this.mXAxisRenderer.computeAxis(this.mXAxis.mAxisMinimum, this.mXAxis.mAxisMaximum, false);
            }

            this.mXAxisRenderer.renderAxisLine(canvas);
            this.mAxisRendererLeft.renderAxisLine(canvas);
            this.mAxisRendererRight.renderAxisLine(canvas);
            if (this.mXAxis.isDrawGridLinesBehindDataEnabled()) {
                this.mXAxisRenderer.renderGridLines(canvas);
            }

            if (this.mAxisLeft.isDrawGridLinesBehindDataEnabled()) {
                this.mAxisRendererLeft.renderGridLines(canvas);
            }

            if (this.mAxisRight.isDrawGridLinesBehindDataEnabled()) {
                this.mAxisRendererRight.renderGridLines(canvas);
            }

            if (this.mXAxis.isEnabled() && this.mXAxis.isDrawLimitLinesBehindDataEnabled()) {
                this.mXAxisRenderer.renderLimitLines(canvas);
            }

            if (this.mAxisLeft.isEnabled() && this.mAxisLeft.isDrawLimitLinesBehindDataEnabled()) {
                this.mAxisRendererLeft.renderLimitLines(canvas);
            }

            if (this.mAxisRight.isEnabled() && this.mAxisRight.isDrawLimitLinesBehindDataEnabled()) {
                this.mAxisRendererRight.renderLimitLines(canvas);
            }

            int clipRestoreCount = canvas.save();
            canvas.clipRect(this.mViewPortHandler.getContentRect());
            this.mRenderer.drawData(canvas);
            if (!this.mXAxis.isDrawGridLinesBehindDataEnabled()) {
                this.mXAxisRenderer.renderGridLines(canvas);
            }

            if (!this.mAxisLeft.isDrawGridLinesBehindDataEnabled()) {
                this.mAxisRendererLeft.renderGridLines(canvas);
            }

            if (!this.mAxisRight.isDrawGridLinesBehindDataEnabled()) {
                this.mAxisRendererRight.renderGridLines(canvas);
            }

            if (this.valuesToHighlight()) {
                this.mRenderer.drawHighlighted(canvas, this.mIndicesToHighlight);
            }

            canvas.restoreToCount(clipRestoreCount);
            this.mRenderer.drawExtras(canvas);
            if (this.mXAxis.isEnabled() && !this.mXAxis.isDrawLimitLinesBehindDataEnabled()) {
                this.mXAxisRenderer.renderLimitLines(canvas);
            }

            if (this.mAxisLeft.isEnabled() && !this.mAxisLeft.isDrawLimitLinesBehindDataEnabled()) {
                this.mAxisRendererLeft.renderLimitLines(canvas);
            }

            if (this.mAxisRight.isEnabled() && !this.mAxisRight.isDrawLimitLinesBehindDataEnabled()) {
                this.mAxisRendererRight.renderLimitLines(canvas);
            }

            this.mXAxisRenderer.renderAxisLabels(canvas);
            this.mAxisRendererLeft.renderAxisLabels(canvas);
            this.mAxisRendererRight.renderAxisLabels(canvas);
            if (this.isClipValuesToContentEnabled()) {
                clipRestoreCount = canvas.save();
                canvas.clipRect(this.mViewPortHandler.getContentRect());
                this.mRenderer.drawValues(canvas);
                canvas.restoreToCount(clipRestoreCount);
            } else {
                this.mRenderer.drawValues(canvas);
            }

            this.mLegendRenderer.renderLegend(canvas);
            this.drawDescription(canvas);
            this.drawMarkers(canvas);
            if (this.mLogEnabled) {
                long drawtime = System.currentTimeMillis() - starttime;
                this.totalTime += drawtime;
                ++this.drawCycles;
                long average = this.totalTime / this.drawCycles;
                LogUtils.i("MPAndroidChart", "Drawtime: " + drawtime + " ms, average: " + average + " ms, cycles: " + this.drawCycles);
            }

        }
    }

    public void resetTracking() {
        this.totalTime = 0L;
        this.drawCycles = 0L;
    }

    protected void prepareValuePxMatrix() {
        if (this.mLogEnabled) {
            LogUtils.i("MPAndroidChart", "Preparing Value-Px Matrix, xmin: " + this.mXAxis.mAxisMinimum + ", xmax: " + this.mXAxis.mAxisMaximum + ", xdelta: " + this.mXAxis.mAxisRange);
        }

        this.mRightAxisTransformer.prepareMatrixValuePx(this.mXAxis.mAxisMinimum, this.mXAxis.mAxisRange, this.mAxisRight.mAxisRange, this.mAxisRight.mAxisMinimum);
        this.mLeftAxisTransformer.prepareMatrixValuePx(this.mXAxis.mAxisMinimum, this.mXAxis.mAxisRange, this.mAxisLeft.mAxisRange, this.mAxisLeft.mAxisMinimum);
    }

    protected void prepareOffsetMatrix() {
        this.mRightAxisTransformer.prepareMatrixOffset(this.mAxisRight.isInverted());
        this.mLeftAxisTransformer.prepareMatrixOffset(this.mAxisLeft.isInverted());
    }

    public void notifyDataSetChanged() {
        if (this.mData == null) {
            if (this.mLogEnabled) {
                LogUtils.i("MPAndroidChart", "Preparing... DATA NOT SET.");
            }

        } else {
            if (this.mLogEnabled) {
                LogUtils.i("MPAndroidChart", "Preparing...");
            }

            if (this.mRenderer != null) {
                this.mRenderer.initBuffers();
            }

            this.calcMinMax();
            this.mAxisRendererLeft.computeAxis(this.mAxisLeft.mAxisMinimum, this.mAxisLeft.mAxisMaximum, this.mAxisLeft.isInverted());
            this.mAxisRendererRight.computeAxis(this.mAxisRight.mAxisMinimum, this.mAxisRight.mAxisMaximum, this.mAxisRight.isInverted());
            this.mXAxisRenderer.computeAxis(this.mXAxis.mAxisMinimum, this.mXAxis.mAxisMaximum, false);
            if (this.mLegend != null) {
                this.mLegendRenderer.computeLegend(this.mData);
            }

            this.calculateOffsets();
        }
    }

    protected void autoScale() {
        float fromX = this.getLowestVisibleX();
        float toX = this.getHighestVisibleX();
        this.mData.calcMinMaxY(fromX, toX);
        this.mXAxis.calculate(this.mData.getXMin(), this.mData.getXMax());
        if (this.mAxisLeft.isEnabled()) {
            this.mAxisLeft.calculate(this.mData.getYMin(YAxis.AxisDependency.LEFT), this.mData.getYMax(YAxis.AxisDependency.LEFT));
        }

        if (this.mAxisRight.isEnabled()) {
            this.mAxisRight.calculate(this.mData.getYMin(YAxis.AxisDependency.RIGHT), this.mData.getYMax(YAxis.AxisDependency.RIGHT));
        }

        this.calculateOffsets();
    }

    protected void calcMinMax() {
        this.mXAxis.calculate(this.mData.getXMin(), this.mData.getXMax());
        this.mAxisLeft.calculate(this.mData.getYMin(YAxis.AxisDependency.LEFT), this.mData.getYMax(YAxis.AxisDependency.LEFT));
        this.mAxisRight.calculate(this.mData.getYMin(YAxis.AxisDependency.RIGHT), this.mData.getYMax(YAxis.AxisDependency.RIGHT));
    }

    protected void calculateLegendOffsets(RectF offsets) {
        offsets.left = 0.0F;
        offsets.right = 0.0F;
        offsets.top = 0.0F;
        offsets.bottom = 0.0F;
        if (this.mLegend != null && this.mLegend.isEnabled() && !this.mLegend.isDrawInsideEnabled()) {
            switch(this.mLegend.getOrientation()) {
                case VERTICAL:
                    switch(this.mLegend.getHorizontalAlignment()) {
                        case LEFT:
                            offsets.left += Math.min(this.mLegend.mNeededWidth, this.mViewPortHandler.getChartWidth() * this.mLegend.getMaxSizePercent()) + this.mLegend.getXOffset();
                            return;
                        case RIGHT:
                            offsets.right += Math.min(this.mLegend.mNeededWidth, this.mViewPortHandler.getChartWidth() * this.mLegend.getMaxSizePercent()) + this.mLegend.getXOffset();
                            return;
                        case CENTER:
                            if (this.mLegend.getVerticalAlignment() == TOP) {
                                offsets.top += Math.min(this.mLegend.mNeededHeight, this.mViewPortHandler.getChartHeight() * this.mLegend.getMaxSizePercent()) + this.mLegend.getYOffset();
                                return;
                            } else if (this.mLegend.getVerticalAlignment() == BOTTOM) {
                                offsets.bottom += Math.min(this.mLegend.mNeededHeight, this.mViewPortHandler.getChartHeight() * this.mLegend.getMaxSizePercent()) + this.mLegend.getYOffset();
                                return;
                            }
                        default:
                            return;
                    }
                case HORIZONTAL:
                    switch(this.mLegend.getVerticalAlignment()) {
                        case TOP:
                            offsets.top += Math.min(this.mLegend.mNeededHeight, this.mViewPortHandler.getChartHeight() * this.mLegend.getMaxSizePercent()) + this.mLegend.getYOffset();
                            break;
                        case BOTTOM:
                            offsets.bottom += Math.min(this.mLegend.mNeededHeight, this.mViewPortHandler.getChartHeight() * this.mLegend.getMaxSizePercent()) + this.mLegend.getYOffset();
                    }
            }
        }

    }

    public void calculateOffsets() {
        if (!this.mCustomViewPortEnabled) {
            float offsetLeft = 0.0F;
            float offsetRight = 0.0F;
            float offsetTop = 0.0F;
            float offsetBottom = 0.0F;
            this.calculateLegendOffsets(this.mOffsetsBuffer);
            offsetLeft += this.mOffsetsBuffer.left;
            offsetTop += this.mOffsetsBuffer.top;
            offsetRight += this.mOffsetsBuffer.right;
            offsetBottom += this.mOffsetsBuffer.bottom;
            if (this.mAxisLeft.needsOffset()) {
                offsetLeft += this.mAxisLeft.getRequiredWidthSpace(this.mAxisRendererLeft.getPaintAxisLabels());
            }

            if (this.mAxisRight.needsOffset()) {
                offsetRight += this.mAxisRight.getRequiredWidthSpace(this.mAxisRendererRight.getPaintAxisLabels());
            }

            float xLabelHeight;
            if (this.mXAxis.isEnabled() && this.mXAxis.isDrawLabelsEnabled()) {
                xLabelHeight = (float)this.mXAxis.mLabelRotatedHeight + this.mXAxis.getYOffset();
                if (this.mXAxis.getPosition() == XAxis.XAxisPosition.BOTTOM) {
                    offsetBottom += xLabelHeight;
                } else if (this.mXAxis.getPosition() == XAxis.XAxisPosition.TOP) {
                    offsetTop += xLabelHeight;
                } else if (this.mXAxis.getPosition() == XAxis.XAxisPosition.BOTH_SIDED) {
                    offsetBottom += xLabelHeight;
                    offsetTop += xLabelHeight;
                }
            }

            offsetTop += this.getExtraTopOffset();
            offsetRight += this.getExtraRightOffset();
            offsetBottom += this.getExtraBottomOffset();
            offsetLeft += this.getExtraLeftOffset();
            xLabelHeight = Utils.convertDpToPixel(this.mMinOffset);
            this.mViewPortHandler.restrainViewPort(Math.max(xLabelHeight, offsetLeft), Math.max(xLabelHeight, offsetTop), Math.max(xLabelHeight, offsetRight), Math.max(xLabelHeight, offsetBottom));
            if (this.mLogEnabled) {
                LogUtils.i("MPAndroidChart", "offsetLeft: " + offsetLeft + ", offsetTop: " + offsetTop + ", offsetRight: " + offsetRight + ", offsetBottom: " + offsetBottom);
                LogUtils.i("MPAndroidChart", "Content: " + this.mViewPortHandler.getContentRect().toString());
            }
        }

        this.prepareOffsetMatrix();
        this.prepareValuePxMatrix();
    }

    protected void drawGridBackground(Canvas c) {
        if (this.mDrawGridBackground) {
            c.drawRect(this.mViewPortHandler.getContentRect(), this.mGridBackgroundPaint);
        }

        if (this.mDrawBorders) {
            c.drawRect(this.mViewPortHandler.getContentRect(), this.mBorderPaint);
        }

    }

    public Transformer getTransformer(YAxis.AxisDependency which) {
        return which == YAxis.AxisDependency.LEFT ? this.mLeftAxisTransformer : this.mRightAxisTransformer;
    }

    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        if (this.mChartTouchListener != null && this.mData != null) {
            return this.mTouchEnabled && this.mChartTouchListener.onTouch(this, event);
        } else {
            return false;
        }
    }

    public void computeScroll() {
        if (this.mChartTouchListener instanceof BarLineChartTouchListener) {
            ((BarLineChartTouchListener)this.mChartTouchListener).computeScroll();
        }

    }

    public void zoomIn() {
        MPPointF center = this.mViewPortHandler.getContentCenter();
        this.mViewPortHandler.zoomIn(center.x, -center.y, this.mZoomMatrixBuffer);
        this.mViewPortHandler.refresh(this.mZoomMatrixBuffer, this, false);
        MPPointF.recycleInstance(center);
        this.calculateOffsets();
        this.postInvalidate();
    }

    public void zoomOut() {
        MPPointF center = this.mViewPortHandler.getContentCenter();
        this.mViewPortHandler.zoomOut(center.x, -center.y, this.mZoomMatrixBuffer);
        this.mViewPortHandler.refresh(this.mZoomMatrixBuffer, this, false);
        MPPointF.recycleInstance(center);
        this.calculateOffsets();
        this.postInvalidate();
    }

    public void resetZoom() {
        this.mViewPortHandler.resetZoom(this.mZoomMatrixBuffer);
        this.mViewPortHandler.refresh(this.mZoomMatrixBuffer, this, false);
        this.calculateOffsets();
        this.postInvalidate();
    }

    public void zoom(float scaleX, float scaleY, float x, float y) {
        this.mViewPortHandler.zoom(scaleX, scaleY, x, -y, this.mZoomMatrixBuffer);
        this.mViewPortHandler.refresh(this.mZoomMatrixBuffer, this, false);
        this.calculateOffsets();
        this.postInvalidate();
    }

    public void zoom(float scaleX, float scaleY, float xValue, float yValue, YAxis.AxisDependency axis) {
        Runnable job = ZoomJob.getInstance(this.mViewPortHandler, scaleX, scaleY, xValue, yValue, this.getTransformer(axis), axis, this);
        this.addViewportJob(job);
    }

    public void zoomToCenter(float scaleX, float scaleY) {
        MPPointF center = this.getCenterOffsets();
        Matrix save = this.mZoomMatrixBuffer;
        this.mViewPortHandler.zoom(scaleX, scaleY, center.x, -center.y, save);
        this.mViewPortHandler.refresh(save, this, false);
    }

    public void zoomAndCenterAnimated(float scaleX, float scaleY, float xValue, float yValue, YAxis.AxisDependency axis, long duration) {
        MPPointD origin = this.getValuesByTouchPoint(this.mViewPortHandler.contentLeft(), this.mViewPortHandler.contentTop(), axis);
        Runnable job = AnimatedZoomJob.getInstance(this.mViewPortHandler, this, this.getTransformer(axis), this.getAxis(axis), this.mXAxis.mAxisRange, scaleX, scaleY, this.mViewPortHandler.getScaleX(), this.mViewPortHandler.getScaleY(), xValue, yValue, (float)origin.x, (float)origin.y, duration);
        this.addViewportJob(job);
        MPPointD.recycleInstance(origin);
    }

    public void fitScreen() {
        Matrix save = this.mFitScreenMatrixBuffer;
        this.mViewPortHandler.fitScreen(save);
        this.mViewPortHandler.refresh(save, this, false);
        this.calculateOffsets();
        this.postInvalidate();
    }

    public void setScaleMinima(float scaleX, float scaleY) {
        this.mViewPortHandler.setMinimumScaleX(scaleX);
        this.mViewPortHandler.setMinimumScaleY(scaleY);
    }

    public void setVisibleXRangeMaximum(float maxXRange) {
        float xScale = this.mXAxis.mAxisRange / maxXRange;
        this.mViewPortHandler.setMinimumScaleX(xScale);
    }

    public void setVisibleXRangeMinimum(float minXRange) {
        float xScale = this.mXAxis.mAxisRange / minXRange;
        this.mViewPortHandler.setMaximumScaleX(xScale);
    }

    public void setVisibleXRange(float minXRange, float maxXRange) {
        float minScale = this.mXAxis.mAxisRange / minXRange;
        float maxScale = this.mXAxis.mAxisRange / maxXRange;
        this.mViewPortHandler.setMinMaxScaleX(minScale, maxScale);
    }

    public void setVisibleYRangeMaximum(float maxYRange, YAxis.AxisDependency axis) {
        float yScale = this.getAxisRange(axis) / maxYRange;
        this.mViewPortHandler.setMinimumScaleY(yScale);
    }

    public void setVisibleYRangeMinimum(float minYRange, YAxis.AxisDependency axis) {
        float yScale = this.getAxisRange(axis) / minYRange;
        this.mViewPortHandler.setMaximumScaleY(yScale);
    }

    public void setVisibleYRange(float minYRange, float maxYRange, YAxis.AxisDependency axis) {
        float minScale = this.getAxisRange(axis) / minYRange;
        float maxScale = this.getAxisRange(axis) / maxYRange;
        this.mViewPortHandler.setMinMaxScaleY(minScale, maxScale);
    }

    public void moveViewToX(float xValue) {
        Runnable job = MoveViewJob.getInstance(this.mViewPortHandler, xValue, 0.0F, this.getTransformer(YAxis.AxisDependency.LEFT), this);
        this.addViewportJob(job);
    }

    public void moveViewTo(float xValue, float yValue, YAxis.AxisDependency axis) {
        float yInView = this.getAxisRange(axis) / this.mViewPortHandler.getScaleY();
        Runnable job = MoveViewJob.getInstance(this.mViewPortHandler, xValue, yValue + yInView / 2.0F, this.getTransformer(axis), this);
        this.addViewportJob(job);
    }

    public void moveViewToAnimated(float xValue, float yValue, YAxis.AxisDependency axis, long duration) {
        MPPointD bounds = this.getValuesByTouchPoint(this.mViewPortHandler.contentLeft(), this.mViewPortHandler.contentTop(), axis);
        float yInView = this.getAxisRange(axis) / this.mViewPortHandler.getScaleY();
        Runnable job = AnimatedMoveViewJob.getInstance(this.mViewPortHandler, xValue, yValue + yInView / 2.0F, this.getTransformer(axis), this, (float)bounds.x, (float)bounds.y, duration);
        this.addViewportJob(job);
        MPPointD.recycleInstance(bounds);
    }

    public void centerViewToY(float yValue, YAxis.AxisDependency axis) {
        float valsInView = this.getAxisRange(axis) / this.mViewPortHandler.getScaleY();
        Runnable job = MoveViewJob.getInstance(this.mViewPortHandler, 0.0F, yValue + valsInView / 2.0F, this.getTransformer(axis), this);
        this.addViewportJob(job);
    }

    public void centerViewTo(float xValue, float yValue, YAxis.AxisDependency axis) {
        float yInView = this.getAxisRange(axis) / this.mViewPortHandler.getScaleY();
        float xInView = this.getXAxis().mAxisRange / this.mViewPortHandler.getScaleX();
        Runnable job = MoveViewJob.getInstance(this.mViewPortHandler, xValue - xInView / 2.0F, yValue + yInView / 2.0F, this.getTransformer(axis), this);
        this.addViewportJob(job);
    }

    public void centerViewToAnimated(float xValue, float yValue, YAxis.AxisDependency axis, long duration) {
        MPPointD bounds = this.getValuesByTouchPoint(this.mViewPortHandler.contentLeft(), this.mViewPortHandler.contentTop(), axis);
        float yInView = this.getAxisRange(axis) / this.mViewPortHandler.getScaleY();
        float xInView = this.getXAxis().mAxisRange / this.mViewPortHandler.getScaleX();
        Runnable job = AnimatedMoveViewJob.getInstance(this.mViewPortHandler, xValue - xInView / 2.0F, yValue + yInView / 2.0F, this.getTransformer(axis), this, (float)bounds.x, (float)bounds.y, duration);
        this.addViewportJob(job);
        MPPointD.recycleInstance(bounds);
    }

    public void setViewPortOffsets(final float left, final float top, final float right, final float bottom) {
        this.mCustomViewPortEnabled = true;
        this.post(new Runnable() {
            public void run() {
                 BarLineChartBase.this.mViewPortHandler.restrainViewPort(left, top, right, bottom);
                 BarLineChartBase.this.prepareOffsetMatrix();
                 BarLineChartBase.this.prepareValuePxMatrix();
            }
        });
    }

    public void resetViewPortOffsets() {
        this.mCustomViewPortEnabled = false;
        this.calculateOffsets();
    }

    protected float getAxisRange(YAxis.AxisDependency axis) {
        return axis == YAxis.AxisDependency.LEFT ? this.mAxisLeft.mAxisRange : this.mAxisRight.mAxisRange;
    }

    public void setOnDrawListener(OnDrawListener drawListener) {
        this.mDrawListener = drawListener;
    }

    public OnDrawListener getDrawListener() {
        return this.mDrawListener;
    }

    public MPPointF getPosition(Entry e, YAxis.AxisDependency axis) {
        if (e == null) {
            return null;
        } else {
            this.mGetPositionBuffer[0] = e.getX();
            this.mGetPositionBuffer[1] = e.getY();
            this.getTransformer(axis).pointValuesToPixel(this.mGetPositionBuffer);
            return MPPointF.getInstance(this.mGetPositionBuffer[0], this.mGetPositionBuffer[1]);
        }
    }

    public void setMaxVisibleValueCount(int count) {
        this.mMaxVisibleCount = count;
    }

    public int getMaxVisibleCount() {
        return this.mMaxVisibleCount;
    }

    public void setHighlightPerDragEnabled(boolean enabled) {
        this.mHighlightPerDragEnabled = enabled;
    }

    public boolean isHighlightPerDragEnabled() {
        return this.mHighlightPerDragEnabled;
    }

    public void setGridBackgroundColor(int color) {
        this.mGridBackgroundPaint.setColor(color);
    }

    public void setDragEnabled(boolean enabled) {
        this.mDragXEnabled = enabled;
        this.mDragYEnabled = enabled;
    }

    public boolean isDragEnabled() {
        return this.mDragXEnabled || this.mDragYEnabled;
    }

    public void setDragXEnabled(boolean enabled) {
        this.mDragXEnabled = enabled;
    }

    public boolean isDragXEnabled() {
        return this.mDragXEnabled;
    }

    public void setDragYEnabled(boolean enabled) {
        this.mDragYEnabled = enabled;
    }

    public boolean isDragYEnabled() {
        return this.mDragYEnabled;
    }

    public void setScaleEnabled(boolean enabled) {
        this.mScaleXEnabled = enabled;
        this.mScaleYEnabled = enabled;
    }

    public void setScaleXEnabled(boolean enabled) {
        this.mScaleXEnabled = enabled;
    }

    public void setScaleYEnabled(boolean enabled) {
        this.mScaleYEnabled = enabled;
    }

    public boolean isScaleXEnabled() {
        return this.mScaleXEnabled;
    }

    public boolean isScaleYEnabled() {
        return this.mScaleYEnabled;
    }

    public void setDoubleTapToZoomEnabled(boolean enabled) {
        this.mDoubleTapToZoomEnabled = enabled;
    }

    public boolean isDoubleTapToZoomEnabled() {
        return this.mDoubleTapToZoomEnabled;
    }

    public void setDrawGridBackground(boolean enabled) {
        this.mDrawGridBackground = enabled;
    }

    public void setDrawBorders(boolean enabled) {
        this.mDrawBorders = enabled;
    }

    public boolean isDrawBordersEnabled() {
        return this.mDrawBorders;
    }

    public void setClipValuesToContent(boolean enabled) {
        this.mClipValuesToContent = enabled;
    }

    public boolean isClipValuesToContentEnabled() {
        return this.mClipValuesToContent;
    }

    public void setBorderWidth(float width) {
        this.mBorderPaint.setStrokeWidth(Utils.convertDpToPixel(width));
    }

    public void setBorderColor(int color) {
        this.mBorderPaint.setColor(color);
    }

    public float getMinOffset() {
        return this.mMinOffset;
    }

    public void setMinOffset(float minOffset) {
        this.mMinOffset = minOffset;
    }

    public boolean isKeepPositionOnRotation() {
        return this.mKeepPositionOnRotation;
    }

    public void setKeepPositionOnRotation(boolean keepPositionOnRotation) {
        this.mKeepPositionOnRotation = keepPositionOnRotation;
    }

    public MPPointD getValuesByTouchPoint(float x, float y, YAxis.AxisDependency axis) {
        MPPointD result = MPPointD.getInstance(0.0D, 0.0D);
        this.getValuesByTouchPoint(x, y, axis, result);
        return result;
    }

    public void getValuesByTouchPoint(float x, float y, YAxis.AxisDependency axis, MPPointD outputPoint) {
        this.getTransformer(axis).getValuesByTouchPoint(x, y, outputPoint);
    }

    public MPPointD getPixelForValues(float x, float y, YAxis.AxisDependency axis) {
        return this.getTransformer(axis).getPixelForValues(x, y);
    }

    public Entry getEntryByTouchPoint(float x, float y) {
        Highlight h = this.getHighlightByTouchPoint(x, y);
        return h != null ? this.mData.getEntryForHighlight(h) : null;
    }

    public IBarLineScatterCandleBubbleDataSet getDataSetByTouchPoint(float x, float y) {
        Highlight h = this.getHighlightByTouchPoint(x, y);
        return h != null ? (IBarLineScatterCandleBubbleDataSet)((BarLineScatterCandleBubbleData)this.mData).getDataSetByIndex(h.getDataSetIndex()) : null;
    }

    public float getLowestVisibleX() {
        this.getTransformer(YAxis.AxisDependency.LEFT).getValuesByTouchPoint(this.mViewPortHandler.contentLeft(), this.mViewPortHandler.contentBottom(), this.posForGetLowestVisibleX);
        float result = (float)Math.max(this.mXAxis.mAxisMinimum, this.posForGetLowestVisibleX.x);
        return result;
    }

    public float getHighestVisibleX() {
        this.getTransformer(YAxis.AxisDependency.LEFT).getValuesByTouchPoint(this.mViewPortHandler.contentRight(), this.mViewPortHandler.contentBottom(), this.posForGetHighestVisibleX);
        float result = (float)Math.min(this.mXAxis.mAxisMaximum, this.posForGetHighestVisibleX.x);
        return result;
    }

    public float getVisibleXRange() {
        return Math.abs(this.getHighestVisibleX() - this.getLowestVisibleX());
    }

    public float getScaleX() {
        return this.mViewPortHandler == null ? 1.0F : this.mViewPortHandler.getScaleX();
    }

    public float getScaleY() {
        return this.mViewPortHandler == null ? 1.0F : this.mViewPortHandler.getScaleY();
    }

    public boolean isFullyZoomedOut() {
        return this.mViewPortHandler.isFullyZoomedOut();
    }

    public YAxis getAxisLeft() {
        return this.mAxisLeft;
    }

    public YAxis getAxisRight() {
        return this.mAxisRight;
    }

    public YAxis getAxis(YAxis.AxisDependency axis) {
        return axis == YAxis.AxisDependency.LEFT ? this.mAxisLeft : this.mAxisRight;
    }

    public boolean isInverted(YAxis.AxisDependency axis) {
        return this.getAxis(axis).isInverted();
    }

    public void setPinchZoom(boolean enabled) {
        this.mPinchZoomEnabled = enabled;
    }

    public boolean isPinchZoomEnabled() {
        return this.mPinchZoomEnabled;
    }

    public void setDragOffsetX(float offset) {
        this.mViewPortHandler.setDragOffsetX(offset);
    }

    public void setDragOffsetY(float offset) {
        this.mViewPortHandler.setDragOffsetY(offset);
    }

    public boolean hasNoDragOffset() {
        return this.mViewPortHandler.hasNoDragOffset();
    }

    public XAxisRenderer getRendererXAxis() {
        return this.mXAxisRenderer;
    }

    public void setXAxisRenderer(XAxisRenderer xAxisRenderer) {
        this.mXAxisRenderer = xAxisRenderer;
    }

    public YAxisRenderer getRendererLeftYAxis() {
        return this.mAxisRendererLeft;
    }

    public void setRendererLeftYAxis(YAxisRenderer rendererLeftYAxis) {
        this.mAxisRendererLeft = rendererLeftYAxis;
    }

    public YAxisRenderer getRendererRightYAxis() {
        return this.mAxisRendererRight;
    }

    public void setRendererRightYAxis(YAxisRenderer rendererRightYAxis) {
        this.mAxisRendererRight = rendererRightYAxis;
    }

    public float getYChartMax() {
        return Math.max(this.mAxisLeft.mAxisMaximum, this.mAxisRight.mAxisMaximum);
    }

    public float getYChartMin() {
        return Math.min(this.mAxisLeft.mAxisMinimum, this.mAxisRight.mAxisMinimum);
    }

    public boolean isAnyAxisInverted() {
        if (this.mAxisLeft.isInverted()) {
            return true;
        } else {
            return this.mAxisRight.isInverted();
        }
    }

    public void setAutoScaleMinMaxEnabled(boolean enabled) {
        this.mAutoScaleMinMaxEnabled = enabled;
    }

    public boolean isAutoScaleMinMaxEnabled() {
        return this.mAutoScaleMinMaxEnabled;
    }

    public void setPaint(Paint p, int which) {
        super.setPaint(p, which);
        if (which == 4) {
            this.mGridBackgroundPaint = p;
        }
    }

    public Paint getPaint(int which) {
        Paint p = super.getPaint(which);
        if (p != null) {
            return p;
        } else {
            if (which == 4) {
                return this.mGridBackgroundPaint;
            }
            return null;
        }
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        this.mOnSizeChangedBuffer[0] = this.mOnSizeChangedBuffer[1] = 0.0F;
        if (this.mKeepPositionOnRotation) {
            this.mOnSizeChangedBuffer[0] = this.mViewPortHandler.contentLeft();
            this.mOnSizeChangedBuffer[1] = this.mViewPortHandler.contentTop();
            this.getTransformer(YAxis.AxisDependency.LEFT).pixelsToValue(this.mOnSizeChangedBuffer);
        }

        super.onSizeChanged(w, h, oldw, oldh);
        if (this.mKeepPositionOnRotation) {
            this.getTransformer(YAxis.AxisDependency.LEFT).pointValuesToPixel(this.mOnSizeChangedBuffer);
            this.mViewPortHandler.centerViewPort(this.mOnSizeChangedBuffer, this);
        } else {
            this.mViewPortHandler.refresh(this.mViewPortHandler.getMatrixTouch(), this, true);
        }

    }
}
