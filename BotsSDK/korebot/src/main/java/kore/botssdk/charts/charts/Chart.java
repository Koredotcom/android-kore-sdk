package kore.botssdk.charts.charts;

import android.animation.ValueAnimator;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.annotation.RequiresApi;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import kore.botssdk.charts.animation.ChartAnimator;
import kore.botssdk.charts.animation.Easing;
import kore.botssdk.charts.components.Description;
import kore.botssdk.charts.components.IMarker;
import kore.botssdk.charts.components.Legend;
import kore.botssdk.charts.components.XAxis;
import kore.botssdk.charts.data.ChartData;
import kore.botssdk.charts.data.Entry;
import kore.botssdk.charts.formatter.DefaultValueFormatter;
import kore.botssdk.charts.formatter.ValueFormatter;
import kore.botssdk.charts.highlight.ChartHighlighter;
import kore.botssdk.charts.highlight.Highlight;
import kore.botssdk.charts.highlight.IHighlighter;
import kore.botssdk.charts.interfaces.dataprovider.ChartInterface;
import kore.botssdk.charts.interfaces.datasets.IDataSet;
import kore.botssdk.charts.listener.ChartTouchListener;
import kore.botssdk.charts.listener.OnChartGestureListener;
import kore.botssdk.charts.listener.OnChartValueSelectedListener;
import kore.botssdk.charts.renderer.DataRenderer;
import kore.botssdk.charts.renderer.LegendRenderer;
import kore.botssdk.charts.utils.MPPointF;
import kore.botssdk.charts.utils.Utils;
import kore.botssdk.charts.utils.ViewPortHandler;
import kore.botssdk.utils.LogUtils;

public abstract class Chart<T extends ChartData<? extends IDataSet<? extends Entry>>> extends ViewGroup implements ChartInterface {
    public static final String LOG_TAG = "MPAndroidChart";
    protected boolean mLogEnabled = false;
    protected T mData = null;
    protected boolean mHighLightPerTapEnabled = true;
    private boolean mDragDecelerationEnabled = true;
    private float mDragDecelerationFrictionCoef = 0.9F;
    protected final DefaultValueFormatter mDefaultValueFormatter = new DefaultValueFormatter(0);
    protected Paint mDescPaint;
    protected Paint mInfoPaint;
    protected XAxis mXAxis;
    protected boolean mTouchEnabled = true;
    protected Description mDescription;
    protected Legend mLegend;
    protected OnChartValueSelectedListener mSelectionListener;
    protected ChartTouchListener mChartTouchListener;
    private String mNoDataText = "No chart data available.";
    private OnChartGestureListener mGestureListener;
    protected LegendRenderer mLegendRenderer;
    protected DataRenderer mRenderer;
    protected IHighlighter mHighlighter;
    protected ViewPortHandler mViewPortHandler = new ViewPortHandler();
    protected ChartAnimator mAnimator;
    private float mExtraTopOffset = 0.0F;
    private float mExtraRightOffset = 0.0F;
    private float mExtraBottomOffset = 0.0F;
    private float mExtraLeftOffset = 0.0F;
    private boolean mOffsetsCalculated = false;
    protected Highlight[] mIndicesToHighlight;
    protected float mMaxHighlightDistance = 0.0F;
    protected boolean mDrawMarkers = true;
    protected IMarker mMarker;
    public static final int PAINT_GRID_BACKGROUND = 4;
    public static final int PAINT_INFO = 7;
    public static final int PAINT_DESCRIPTION = 11;
    public static final int PAINT_HOLE = 13;
    public static final int PAINT_CENTER_TEXT = 14;
    public static final int PAINT_LEGEND_LABEL = 18;
    protected final ArrayList<Runnable> mJobs = new ArrayList();
    private boolean mUnbind = false;

    public Chart(Context context) {
        super(context);
        this.init();
    }

    public Chart(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.init();
    }

    public Chart(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.init();
    }

    protected void init() {
        this.setWillNotDraw(false);
        this.mAnimator = new ChartAnimator(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                Chart.this.postInvalidate();
            }
        });
        Utils.init(this.getContext());
        this.mMaxHighlightDistance = Utils.convertDpToPixel(500.0F);
        this.mDescription = new Description();
        this.mLegend = new Legend();
        this.mLegendRenderer = new LegendRenderer(this.mViewPortHandler, this.mLegend);
        this.mXAxis = new XAxis();
        this.mDescPaint = new Paint(1);
        this.mInfoPaint = new Paint(1);
        this.mInfoPaint.setColor(Color.rgb(247, 189, 51));
        this.mInfoPaint.setTextAlign(Paint.Align.CENTER);
        this.mInfoPaint.setTextSize(Utils.convertDpToPixel(12.0F));
        if (this.mLogEnabled) {
            LogUtils.i("", "Chart.init()");
        }

    }

    public void setData(T data) {
        this.mData = data;
        this.mOffsetsCalculated = false;
        if (data != null) {
            this.setupDefaultFormatter(data.getYMin(), data.getYMax());
            Iterator var2 = this.mData.getDataSets().iterator();

            while(true) {
                IDataSet set;
                do {
                    if (!var2.hasNext()) {
                        this.notifyDataSetChanged();
                        if (this.mLogEnabled) {
                            LogUtils.i("MPAndroidChart", "Data is set.");
                        }

                        return;
                    }

                    set = (IDataSet)var2.next();
                } while(!set.needsFormatter() && set.getValueFormatter() != this.mDefaultValueFormatter);

                set.setValueFormatter(this.mDefaultValueFormatter);
            }
        }
    }

    public void clear() {
        this.mData = null;
        this.mOffsetsCalculated = false;
        this.mIndicesToHighlight = null;
        this.mChartTouchListener.setLastHighlighted(null);
        this.invalidate();
    }

    public void clearValues() {
        this.mData.clearValues();
        this.invalidate();
    }

    public boolean isEmpty() {
        if (this.mData == null) {
            return true;
        } else {
            return this.mData.getEntryCount() <= 0;
        }
    }

    public abstract void notifyDataSetChanged();

    protected abstract void calculateOffsets();

    protected abstract void calcMinMax();

    protected void setupDefaultFormatter(float min, float max) {
        float reference = 0.0F;
        if (this.mData != null && this.mData.getEntryCount() >= 2) {
            reference = Math.abs(max - min);
        } else {
            reference = Math.max(Math.abs(min), Math.abs(max));
        }

        int digits = Utils.getDecimals(reference);
        this.mDefaultValueFormatter.setup(digits);
    }

    protected void onDraw(Canvas canvas) {
        if (this.mData == null) {
            boolean hasText = !TextUtils.isEmpty(this.mNoDataText);
            if (hasText) {
                MPPointF c = this.getCenter();
                canvas.drawText(this.mNoDataText, c.x, c.y, this.mInfoPaint);
            }

        } else {
            if (!this.mOffsetsCalculated) {
                this.calculateOffsets();
                this.mOffsetsCalculated = true;
            }

        }
    }

    protected void drawDescription(Canvas c) {
        if (this.mDescription != null && this.mDescription.isEnabled()) {
            MPPointF position = this.mDescription.getPosition();
            this.mDescPaint.setTypeface(this.mDescription.getTypeface());
            this.mDescPaint.setTextSize(this.mDescription.getTextSize());
            this.mDescPaint.setColor(this.mDescription.getTextColor());
            this.mDescPaint.setTextAlign(this.mDescription.getTextAlign());
            float x;
            float y;
            if (position == null) {
                x = (float)this.getWidth() - this.mViewPortHandler.offsetRight() - this.mDescription.getXOffset();
                y = (float)this.getHeight() - this.mViewPortHandler.offsetBottom() - this.mDescription.getYOffset();
            } else {
                x = position.x;
                y = position.y;
            }

            c.drawText(this.mDescription.getText(), x, y, this.mDescPaint);
        }

    }

    public float getMaxHighlightDistance() {
        return this.mMaxHighlightDistance;
    }

    public void setMaxHighlightDistance(float distDp) {
        this.mMaxHighlightDistance = Utils.convertDpToPixel(distDp);
    }

    public Highlight[] getHighlighted() {
        return this.mIndicesToHighlight;
    }

    public boolean isHighlightPerTapEnabled() {
        return this.mHighLightPerTapEnabled;
    }

    public void setHighlightPerTapEnabled(boolean enabled) {
        this.mHighLightPerTapEnabled = enabled;
    }

    public boolean valuesToHighlight() {
        return this.mIndicesToHighlight != null && this.mIndicesToHighlight.length > 0 && this.mIndicesToHighlight[0] != null;
    }

    protected void setLastHighlighted(Highlight[] highs) {
        if (highs != null && highs.length > 0 && highs[0] != null) {
            this.mChartTouchListener.setLastHighlighted(highs[0]);
        } else {
            this.mChartTouchListener.setLastHighlighted(null);
        }

    }

    public void highlightValues(Highlight[] highs) {
        this.mIndicesToHighlight = highs;
        this.setLastHighlighted(highs);
        this.invalidate();
    }

    public void highlightValue(float x, int dataSetIndex) {
        this.highlightValue(x, dataSetIndex, true);
    }

    public void highlightValue(float x, float y, int dataSetIndex) {
        this.highlightValue(x, y, dataSetIndex, true);
    }

    public void highlightValue(float x, int dataSetIndex, boolean callListener) {
        this.highlightValue(x, 0.0F, dataSetIndex, callListener);
    }

    public void highlightValue(float x, float y, int dataSetIndex, boolean callListener) {
        if (dataSetIndex >= 0 && dataSetIndex < this.mData.getDataSetCount()) {
            this.highlightValue(new Highlight(x, y, dataSetIndex), callListener);
        } else {
            this.highlightValue(null, callListener);
        }

    }

    public void highlightValue(Highlight highlight) {
        this.highlightValue(highlight, false);
    }

    public void highlightValue(Highlight high, boolean callListener) {
        Entry e = null;
        if (high == null) {
            this.mIndicesToHighlight = null;
        } else {
            if (this.mLogEnabled) {
                LogUtils.i("MPAndroidChart", "Highlighted: " + high);
            }

            e = this.mData.getEntryForHighlight(high);
            if (e == null) {
                this.mIndicesToHighlight = null;
                high = null;
            } else {
                this.mIndicesToHighlight = new Highlight[]{high};
            }
        }

        this.setLastHighlighted(this.mIndicesToHighlight);
        if (callListener && this.mSelectionListener != null) {
            if (!this.valuesToHighlight()) {
                this.mSelectionListener.onNothingSelected();
            } else {
                this.mSelectionListener.onValueSelected(e, high);
            }
        }

        this.invalidate();
    }

    public Highlight getHighlightByTouchPoint(float x, float y) {
        if (this.mData == null) {
            LogUtils.e("MPAndroidChart", "Can't select by touch. No data set.");
            return null;
        } else {
            return this.getHighlighter().getHighlight(x, y);
        }
    }

    public void setOnTouchListener(ChartTouchListener l) {
        this.mChartTouchListener = l;
    }

    public ChartTouchListener getOnTouchListener() {
        return this.mChartTouchListener;
    }

    protected void drawMarkers(Canvas canvas) {
        if (this.mMarker != null && this.isDrawMarkersEnabled() && this.valuesToHighlight()) {
            for(int i = 0; i < this.mIndicesToHighlight.length; ++i) {
                Highlight highlight = this.mIndicesToHighlight[i];
                IDataSet set = this.mData.getDataSetByIndex(highlight.getDataSetIndex());
                Entry e = this.mData.getEntryForHighlight(this.mIndicesToHighlight[i]);
                int entryIndex = set.getEntryIndex(e);
                if (e != null && !((float)entryIndex > (float)set.getEntryCount() * this.mAnimator.getPhaseX())) {
                    float[] pos = this.getMarkerPosition(highlight);
                    if (this.mViewPortHandler.isInBounds(pos[0], pos[1])) {
                        this.mMarker.refreshContent(e, highlight);
                        this.mMarker.draw(canvas, pos[0], pos[1]);
                    }
                }
            }

        }
    }

    protected float[] getMarkerPosition(Highlight high) {
        return new float[]{high.getDrawX(), high.getDrawY()};
    }

    public ChartAnimator getAnimator() {
        return this.mAnimator;
    }

    public boolean isDragDecelerationEnabled() {
        return this.mDragDecelerationEnabled;
    }

    public void setDragDecelerationEnabled(boolean enabled) {
        this.mDragDecelerationEnabled = enabled;
    }

    public float getDragDecelerationFrictionCoef() {
        return this.mDragDecelerationFrictionCoef;
    }

    public void setDragDecelerationFrictionCoef(float newValue) {
        if (newValue < 0.0F) {
            newValue = 0.0F;
        }

        if (newValue >= 1.0F) {
            newValue = 0.999F;
        }

        this.mDragDecelerationFrictionCoef = newValue;
    }

    @RequiresApi(11)
    public void animateXY(int durationMillisX, int durationMillisY, Easing.EasingFunction easingX, Easing.EasingFunction easingY) {
        this.mAnimator.animateXY(durationMillisX, durationMillisY, easingX, easingY);
    }

    @RequiresApi(11)
    public void animateXY(int durationMillisX, int durationMillisY, Easing.EasingFunction easing) {
        this.mAnimator.animateXY(durationMillisX, durationMillisY, easing);
    }

    @RequiresApi(11)
    public void animateX(int durationMillis, Easing.EasingFunction easing) {
        this.mAnimator.animateX(durationMillis, easing);
    }

    @RequiresApi(11)
    public void animateY(int durationMillis, Easing.EasingFunction easing) {
        this.mAnimator.animateY(durationMillis, easing);
    }

    @RequiresApi(11)
    public void animateX(int durationMillis) {
        this.mAnimator.animateX(durationMillis);
    }

    @RequiresApi(11)
    public void animateY(int durationMillis) {
        this.mAnimator.animateY(durationMillis);
    }

    @RequiresApi(11)
    public void animateXY(int durationMillisX, int durationMillisY) {
        this.mAnimator.animateXY(durationMillisX, durationMillisY);
    }

    public XAxis getXAxis() {
        return this.mXAxis;
    }

    public ValueFormatter getDefaultValueFormatter() {
        return this.mDefaultValueFormatter;
    }

    public void setOnChartValueSelectedListener(OnChartValueSelectedListener l) {
        this.mSelectionListener = l;
    }

    public void setOnChartGestureListener(OnChartGestureListener l) {
        this.mGestureListener = l;
    }

    public OnChartGestureListener getOnChartGestureListener() {
        return this.mGestureListener;
    }

    public float getYMax() {
        return this.mData.getYMax();
    }

    public float getYMin() {
        return this.mData.getYMin();
    }

    public float getXChartMax() {
        return this.mXAxis.mAxisMaximum;
    }

    public float getXChartMin() {
        return this.mXAxis.mAxisMinimum;
    }

    public float getXRange() {
        return this.mXAxis.mAxisRange;
    }

    public MPPointF getCenter() {
        return MPPointF.getInstance((float)this.getWidth() / 2.0F, (float)this.getHeight() / 2.0F);
    }

    public MPPointF getCenterOffsets() {
        return this.mViewPortHandler.getContentCenter();
    }

    public void setExtraOffsets(float left, float top, float right, float bottom) {
        this.setExtraLeftOffset(left);
        this.setExtraTopOffset(top);
        this.setExtraRightOffset(right);
        this.setExtraBottomOffset(bottom);
    }

    public void setExtraTopOffset(float offset) {
        this.mExtraTopOffset = Utils.convertDpToPixel(offset);
    }

    public float getExtraTopOffset() {
        return this.mExtraTopOffset;
    }

    public void setExtraRightOffset(float offset) {
        this.mExtraRightOffset = Utils.convertDpToPixel(offset);
    }

    public float getExtraRightOffset() {
        return this.mExtraRightOffset;
    }

    public void setExtraBottomOffset(float offset) {
        this.mExtraBottomOffset = Utils.convertDpToPixel(offset);
    }

    public float getExtraBottomOffset() {
        return this.mExtraBottomOffset;
    }

    public void setExtraLeftOffset(float offset) {
        this.mExtraLeftOffset = Utils.convertDpToPixel(offset);
    }

    public float getExtraLeftOffset() {
        return this.mExtraLeftOffset;
    }

    public void setLogEnabled(boolean enabled) {
        this.mLogEnabled = enabled;
    }

    public boolean isLogEnabled() {
        return this.mLogEnabled;
    }

    public void setNoDataText(String text) {
        this.mNoDataText = text;
    }

    public void setNoDataTextColor(int color) {
        this.mInfoPaint.setColor(color);
    }

    public void setNoDataTextTypeface(Typeface tf) {
        this.mInfoPaint.setTypeface(tf);
    }

    public void setTouchEnabled(boolean enabled) {
        this.mTouchEnabled = enabled;
    }

    public void setMarker(IMarker marker) {
        this.mMarker = marker;
    }

    public IMarker getMarker() {
        return this.mMarker;
    }

    /** @deprecated */
    @Deprecated
    public void setMarkerView(IMarker v) {
        this.setMarker(v);
    }

    /** @deprecated */
    @Deprecated
    public IMarker getMarkerView() {
        return this.getMarker();
    }

    public void setDescription(Description desc) {
        this.mDescription = desc;
    }

    public Description getDescription() {
        return this.mDescription;
    }

    public Legend getLegend() {
        return this.mLegend;
    }

    public LegendRenderer getLegendRenderer() {
        return this.mLegendRenderer;
    }

    public RectF getContentRect() {
        return this.mViewPortHandler.getContentRect();
    }

    public void disableScroll() {
        ViewParent parent = this.getParent();
        if (parent != null) {
            parent.requestDisallowInterceptTouchEvent(true);
        }

    }

    public void enableScroll() {
        ViewParent parent = this.getParent();
        if (parent != null) {
            parent.requestDisallowInterceptTouchEvent(false);
        }

    }

    public void setPaint(Paint p, int which) {
        switch(which) {
            case 7:
                this.mInfoPaint = p;
                break;
            case 11:
                this.mDescPaint = p;
        }

    }

    public Paint getPaint(int which) {
        switch(which) {
            case 7:
                return this.mInfoPaint;
            case 11:
                return this.mDescPaint;
            default:
                return null;
        }
    }

    /** @deprecated */
    @Deprecated
    public boolean isDrawMarkerViewsEnabled() {
        return this.isDrawMarkersEnabled();
    }

    /** @deprecated */
    @Deprecated
    public void setDrawMarkerViews(boolean enabled) {
        this.setDrawMarkers(enabled);
    }

    public boolean isDrawMarkersEnabled() {
        return this.mDrawMarkers;
    }

    public void setDrawMarkers(boolean enabled) {
        this.mDrawMarkers = enabled;
    }

    public T getData() {
        return this.mData;
    }

    public ViewPortHandler getViewPortHandler() {
        return this.mViewPortHandler;
    }

    public DataRenderer getRenderer() {
        return this.mRenderer;
    }

    public void setRenderer(DataRenderer renderer) {
        if (renderer != null) {
            this.mRenderer = renderer;
        }

    }

    public IHighlighter getHighlighter() {
        return this.mHighlighter;
    }

    public void setHighlighter(ChartHighlighter highlighter) {
        this.mHighlighter = highlighter;
    }

    public MPPointF getCenterOfView() {
        return this.getCenter();
    }

    public Bitmap getChartBitmap() {
        Bitmap returnedBitmap = Bitmap.createBitmap(this.getWidth(), this.getHeight(), Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(returnedBitmap);
        Drawable bgDrawable = this.getBackground();
        if (bgDrawable != null) {
            bgDrawable.draw(canvas);
        } else {
            canvas.drawColor(-1);
        }

        this.draw(canvas);
        return returnedBitmap;
    }

    public boolean saveToPath(String title, String pathOnSD) {
        Bitmap b = this.getChartBitmap();
        FileOutputStream stream = null;

        try {
            stream = new FileOutputStream(Environment.getExternalStorageDirectory().getPath() + pathOnSD + "/" + title + ".png");
            b.compress(Bitmap.CompressFormat.PNG, 40, stream);
            stream.close();
            return true;
        } catch (Exception var6) {
            var6.printStackTrace();
            return false;
        }
    }

    public boolean saveToGallery(String fileName, String subFolderPath, String fileDescription, Bitmap.CompressFormat format, int quality) {
        if (quality < 0 || quality > 100) {
            quality = 50;
        }

        long currentTime = System.currentTimeMillis();
        File extBaseDir = Environment.getExternalStorageDirectory();
        File file = new File(extBaseDir.getAbsolutePath() + "/DCIM/" + subFolderPath);
        if (!file.exists() && !file.mkdirs()) {
            return false;
        } else {
            String mimeType = "";
            switch(format) {
                case PNG:
                    mimeType = "image/png";
                    if (!fileName.endsWith(".png")) {
                        fileName = fileName + ".png";
                    }
                    break;
                case WEBP:
                    mimeType = "image/webp";
                    if (!fileName.endsWith(".webp")) {
                        fileName = fileName + ".webp";
                    }
                    break;
                case JPEG:
                default:
                    mimeType = "image/jpeg";
                    if (!fileName.endsWith(".jpg") && !fileName.endsWith(".jpeg")) {
                        fileName = fileName + ".jpg";
                    }
            }

            String filePath = file.getAbsolutePath() + "/" + fileName;
            FileOutputStream out = null;

            try {
                out = new FileOutputStream(filePath);
                Bitmap b = this.getChartBitmap();
                b.compress(format, quality, out);
                out.flush();
                out.close();
            } catch (IOException var16) {
                var16.printStackTrace();
                return false;
            }
            finally {
                try {
                    //Closing output stream
                    if (out != null) out.close();
                }
                catch (Exception e){e.printStackTrace();}
            }

            long size = (new File(filePath)).length();
            ContentValues values = new ContentValues(8);
            values.put("title", fileName);
            values.put("_display_name", fileName);
            values.put("date_added", currentTime);
            values.put("mime_type", mimeType);
            values.put("description", fileDescription);
            values.put("orientation", 0);
            values.put("_data", filePath);
            values.put("_size", size);
            return this.getContext().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values) != null;
        }
    }

    public boolean saveToGallery(String fileName, int quality) {
        return this.saveToGallery(fileName, "", "MPAndroidChart-Library Save", Bitmap.CompressFormat.PNG, quality);
    }

    public boolean saveToGallery(String fileName) {
        return this.saveToGallery(fileName, "", "MPAndroidChart-Library Save", Bitmap.CompressFormat.PNG, 40);
    }

    public void removeViewportJob(Runnable job) {
        this.mJobs.remove(job);
    }

    public void clearAllViewportJobs() {
        this.mJobs.clear();
    }

    public void addViewportJob(Runnable job) {
        if (this.mViewPortHandler.hasChartDimens()) {
            this.post(job);
        } else {
            this.mJobs.add(job);
        }

    }

    public ArrayList<Runnable> getJobs() {
        return this.mJobs;
    }

    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        for(int i = 0; i < this.getChildCount(); ++i) {
            this.getChildAt(i).layout(left, top, right, bottom);
        }

    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int size = (int)Utils.convertDpToPixel(50.0F);
        this.setMeasuredDimension(Math.max(this.getSuggestedMinimumWidth(), resolveSize(size, widthMeasureSpec)), Math.max(this.getSuggestedMinimumHeight(), resolveSize(size, heightMeasureSpec)));
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        if (this.mLogEnabled) {
            LogUtils.i("MPAndroidChart", "OnSizeChanged()");
        }

        if (w > 0 && h > 0 && w < 10000 && h < 10000) {
            if (this.mLogEnabled) {
                LogUtils.i("MPAndroidChart", "Setting chart dimens, width: " + w + ", height: " + h);
            }

            this.mViewPortHandler.setChartDimens((float)w, (float)h);
        } else if (this.mLogEnabled) {
            LogUtils.w("MPAndroidChart", "*Avoiding* setting chart dimens! width: " + w + ", height: " + h);
        }

        this.notifyDataSetChanged();
        Iterator var5 = this.mJobs.iterator();

        while(var5.hasNext()) {
            Runnable r = (Runnable)var5.next();
            this.post(r);
        }

        this.mJobs.clear();
        super.onSizeChanged(w, h, oldw, oldh);
    }

    public void setHardwareAccelerationEnabled(boolean enabled) {
        if (enabled) {
            this.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        } else {
            this.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }

    }

    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (this.mUnbind) {
            this.unbindDrawables(this);
        }

    }

    private void unbindDrawables(View view) {
        if (view.getBackground() != null) {
            view.getBackground().setCallback(null);
        }

        if (view instanceof ViewGroup) {
            for(int i = 0; i < ((ViewGroup)view).getChildCount(); ++i) {
                this.unbindDrawables(((ViewGroup)view).getChildAt(i));
            }

            ((ViewGroup)view).removeAllViews();
        }

    }

    public void setUnbindEnabled(boolean enabled) {
        this.mUnbind = enabled;
    }
}
