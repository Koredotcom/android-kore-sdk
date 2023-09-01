package kore.botssdk.charts.utils;

import android.graphics.Matrix;
import android.graphics.RectF;
import android.view.View;

public class ViewPortHandler {
    protected final Matrix mMatrixTouch = new Matrix();
    protected final RectF mContentRect = new RectF();
    protected float mChartWidth = 0.0F;
    protected float mChartHeight = 0.0F;
    private float mMinScaleY = 1.0F;
    private float mMaxScaleY = 3.4028235E38F;
    private float mMinScaleX = 1.0F;
    private float mMaxScaleX = 3.4028235E38F;
    private float mScaleX = 1.0F;
    private float mScaleY = 1.0F;
    private float mTransX = 0.0F;
    private float mTransY = 0.0F;
    private float mTransOffsetX = 0.0F;
    private float mTransOffsetY = 0.0F;
    protected final float[] valsBufferForFitScreen = new float[9];
    protected final Matrix mCenterViewPortMatrixBuffer = new Matrix();
    protected final float[] matrixBuffer = new float[9];

    public ViewPortHandler() {
    }

    public void setChartDimens(float width, float height) {
        float offsetLeft = this.offsetLeft();
        float offsetTop = this.offsetTop();
        float offsetRight = this.offsetRight();
        float offsetBottom = this.offsetBottom();
        this.mChartHeight = height;
        this.mChartWidth = width;
        this.restrainViewPort(offsetLeft, offsetTop, offsetRight, offsetBottom);
    }

    public boolean hasChartDimens() {
        return this.mChartHeight > 0.0F && this.mChartWidth > 0.0F;
    }

    public void restrainViewPort(float offsetLeft, float offsetTop, float offsetRight, float offsetBottom) {
        this.mContentRect.set(offsetLeft, offsetTop, this.mChartWidth - offsetRight, this.mChartHeight - offsetBottom);
    }

    public float offsetLeft() {
        return this.mContentRect.left;
    }

    public float offsetRight() {
        return this.mChartWidth - this.mContentRect.right;
    }

    public float offsetTop() {
        return this.mContentRect.top;
    }

    public float offsetBottom() {
        return this.mChartHeight - this.mContentRect.bottom;
    }

    public float contentTop() {
        return this.mContentRect.top;
    }

    public float contentLeft() {
        return this.mContentRect.left;
    }

    public float contentRight() {
        return this.mContentRect.right;
    }

    public float contentBottom() {
        return this.mContentRect.bottom;
    }

    public float contentWidth() {
        return this.mContentRect.width();
    }

    public float contentHeight() {
        return this.mContentRect.height();
    }

    public RectF getContentRect() {
        return this.mContentRect;
    }

    public MPPointF getContentCenter() {
        return MPPointF.getInstance(this.mContentRect.centerX(), this.mContentRect.centerY());
    }

    public float getChartHeight() {
        return this.mChartHeight;
    }

    public float getChartWidth() {
        return this.mChartWidth;
    }

    public float getSmallestContentExtension() {
        return Math.min(this.mContentRect.width(), this.mContentRect.height());
    }

    public Matrix zoomIn(float x, float y) {
        Matrix save = new Matrix();
        this.zoomIn(x, y, save);
        return save;
    }

    public void zoomIn(float x, float y, Matrix outputMatrix) {
        outputMatrix.reset();
        outputMatrix.set(this.mMatrixTouch);
        outputMatrix.postScale(1.4F, 1.4F, x, y);
    }

    public Matrix zoomOut(float x, float y) {
        Matrix save = new Matrix();
        this.zoomOut(x, y, save);
        return save;
    }

    public void zoomOut(float x, float y, Matrix outputMatrix) {
        outputMatrix.reset();
        outputMatrix.set(this.mMatrixTouch);
        outputMatrix.postScale(0.7F, 0.7F, x, y);
    }

    public void resetZoom(Matrix outputMatrix) {
        outputMatrix.reset();
        outputMatrix.set(this.mMatrixTouch);
        outputMatrix.postScale(1.0F, 1.0F, 0.0F, 0.0F);
    }

    public Matrix zoom(float scaleX, float scaleY) {
        Matrix save = new Matrix();
        this.zoom(scaleX, scaleY, save);
        return save;
    }

    public void zoom(float scaleX, float scaleY, Matrix outputMatrix) {
        outputMatrix.reset();
        outputMatrix.set(this.mMatrixTouch);
        outputMatrix.postScale(scaleX, scaleY);
    }

    public Matrix zoom(float scaleX, float scaleY, float x, float y) {
        Matrix save = new Matrix();
        this.zoom(scaleX, scaleY, x, y, save);
        return save;
    }

    public void zoom(float scaleX, float scaleY, float x, float y, Matrix outputMatrix) {
        outputMatrix.reset();
        outputMatrix.set(this.mMatrixTouch);
        outputMatrix.postScale(scaleX, scaleY, x, y);
    }

    public Matrix setZoom(float scaleX, float scaleY) {
        Matrix save = new Matrix();
        this.setZoom(scaleX, scaleY, save);
        return save;
    }

    public void setZoom(float scaleX, float scaleY, Matrix outputMatrix) {
        outputMatrix.reset();
        outputMatrix.set(this.mMatrixTouch);
        outputMatrix.setScale(scaleX, scaleY);
    }

    public Matrix setZoom(float scaleX, float scaleY, float x, float y) {
        Matrix save = new Matrix();
        save.set(this.mMatrixTouch);
        save.setScale(scaleX, scaleY, x, y);
        return save;
    }

    public Matrix fitScreen() {
        Matrix save = new Matrix();
        this.fitScreen(save);
        return save;
    }

    public void fitScreen(Matrix outputMatrix) {
        this.mMinScaleX = 1.0F;
        this.mMinScaleY = 1.0F;
        outputMatrix.set(this.mMatrixTouch);
        float[] vals = this.valsBufferForFitScreen;

        for(int i = 0; i < 9; ++i) {
            vals[i] = 0.0F;
        }

        outputMatrix.getValues(vals);
        vals[2] = 0.0F;
        vals[5] = 0.0F;
        vals[0] = 1.0F;
        vals[4] = 1.0F;
        outputMatrix.setValues(vals);
    }

    public Matrix translate(float[] transformedPts) {
        Matrix save = new Matrix();
        this.translate(transformedPts, save);
        return save;
    }

    public void translate(float[] transformedPts, Matrix outputMatrix) {
        outputMatrix.reset();
        outputMatrix.set(this.mMatrixTouch);
        float x = transformedPts[0] - this.offsetLeft();
        float y = transformedPts[1] - this.offsetTop();
        outputMatrix.postTranslate(-x, -y);
    }

    public void centerViewPort(float[] transformedPts, View view) {
        Matrix save = this.mCenterViewPortMatrixBuffer;
        save.reset();
        save.set(this.mMatrixTouch);
        float x = transformedPts[0] - this.offsetLeft();
        float y = transformedPts[1] - this.offsetTop();
        save.postTranslate(-x, -y);
        this.refresh(save, view, true);
    }

    public Matrix refresh(Matrix newMatrix, View chart, boolean invalidate) {
        this.mMatrixTouch.set(newMatrix);
        this.limitTransAndScale(this.mMatrixTouch, this.mContentRect);
        if (invalidate) {
            chart.invalidate();
        }

        newMatrix.set(this.mMatrixTouch);
        return newMatrix;
    }

    public void limitTransAndScale(Matrix matrix, RectF content) {
        matrix.getValues(this.matrixBuffer);
        float curTransX = this.matrixBuffer[2];
        float curScaleX = this.matrixBuffer[0];
        float curTransY = this.matrixBuffer[5];
        float curScaleY = this.matrixBuffer[4];
        this.mScaleX = Math.min(Math.max(this.mMinScaleX, curScaleX), this.mMaxScaleX);
        this.mScaleY = Math.min(Math.max(this.mMinScaleY, curScaleY), this.mMaxScaleY);
        float width = 0.0F;
        float height = 0.0F;
        if (content != null) {
            width = content.width();
            height = content.height();
        }

        float maxTransX = -width * (this.mScaleX - 1.0F);
        this.mTransX = Math.min(Math.max(curTransX, maxTransX - this.mTransOffsetX), this.mTransOffsetX);
        float maxTransY = height * (this.mScaleY - 1.0F);
        this.mTransY = Math.max(Math.min(curTransY, maxTransY + this.mTransOffsetY), -this.mTransOffsetY);
        this.matrixBuffer[2] = this.mTransX;
        this.matrixBuffer[0] = this.mScaleX;
        this.matrixBuffer[5] = this.mTransY;
        this.matrixBuffer[4] = this.mScaleY;
        matrix.setValues(this.matrixBuffer);
    }

    public void setMinimumScaleX(float xScale) {
        if (xScale < 1.0F) {
            xScale = 1.0F;
        }

        this.mMinScaleX = xScale;
        this.limitTransAndScale(this.mMatrixTouch, this.mContentRect);
    }

    public void setMaximumScaleX(float xScale) {
        if (xScale == 0.0F) {
            xScale = 3.4028235E38F;
        }

        this.mMaxScaleX = xScale;
        this.limitTransAndScale(this.mMatrixTouch, this.mContentRect);
    }

    public void setMinMaxScaleX(float minScaleX, float maxScaleX) {
        if (minScaleX < 1.0F) {
            minScaleX = 1.0F;
        }

        if (maxScaleX == 0.0F) {
            maxScaleX = 3.4028235E38F;
        }

        this.mMinScaleX = minScaleX;
        this.mMaxScaleX = maxScaleX;
        this.limitTransAndScale(this.mMatrixTouch, this.mContentRect);
    }

    public void setMinimumScaleY(float yScale) {
        if (yScale < 1.0F) {
            yScale = 1.0F;
        }

        this.mMinScaleY = yScale;
        this.limitTransAndScale(this.mMatrixTouch, this.mContentRect);
    }

    public void setMaximumScaleY(float yScale) {
        if (yScale == 0.0F) {
            yScale = 3.4028235E38F;
        }

        this.mMaxScaleY = yScale;
        this.limitTransAndScale(this.mMatrixTouch, this.mContentRect);
    }

    public void setMinMaxScaleY(float minScaleY, float maxScaleY) {
        if (minScaleY < 1.0F) {
            minScaleY = 1.0F;
        }

        if (maxScaleY == 0.0F) {
            maxScaleY = 3.4028235E38F;
        }

        this.mMinScaleY = minScaleY;
        this.mMaxScaleY = maxScaleY;
        this.limitTransAndScale(this.mMatrixTouch, this.mContentRect);
    }

    public Matrix getMatrixTouch() {
        return this.mMatrixTouch;
    }

    public boolean isInBoundsX(float x) {
        return this.isInBoundsLeft(x) && this.isInBoundsRight(x);
    }

    public boolean isInBoundsY(float y) {
        return this.isInBoundsTop(y) && this.isInBoundsBottom(y);
    }

    public boolean isInBounds(float x, float y) {
        return this.isInBoundsX(x) && this.isInBoundsY(y);
    }

    public boolean isInBoundsLeft(float x) {
        return this.mContentRect.left <= x + 1.0F;
    }

    public boolean isInBoundsRight(float x) {
        x = (float)((int)(x * 100.0F)) / 100.0F;
        return this.mContentRect.right >= x - 1.0F;
    }

    public boolean isInBoundsTop(float y) {
        return this.mContentRect.top <= y;
    }

    public boolean isInBoundsBottom(float y) {
        y = (float)((int)(y * 100.0F)) / 100.0F;
        return this.mContentRect.bottom >= y;
    }

    public float getScaleX() {
        return this.mScaleX;
    }

    public float getScaleY() {
        return this.mScaleY;
    }

    public float getMinScaleX() {
        return this.mMinScaleX;
    }

    public float getMaxScaleX() {
        return this.mMaxScaleX;
    }

    public float getMinScaleY() {
        return this.mMinScaleY;
    }

    public float getMaxScaleY() {
        return this.mMaxScaleY;
    }

    public float getTransX() {
        return this.mTransX;
    }

    public float getTransY() {
        return this.mTransY;
    }

    public boolean isFullyZoomedOut() {
        return this.isFullyZoomedOutX() && this.isFullyZoomedOutY();
    }

    public boolean isFullyZoomedOutY() {
        return !(this.mScaleY > this.mMinScaleY) && !(this.mMinScaleY > 1.0F);
    }

    public boolean isFullyZoomedOutX() {
        return !(this.mScaleX > this.mMinScaleX) && !(this.mMinScaleX > 1.0F);
    }

    public void setDragOffsetX(float offset) {
        this.mTransOffsetX = Utils.convertDpToPixel(offset);
    }

    public void setDragOffsetY(float offset) {
        this.mTransOffsetY = Utils.convertDpToPixel(offset);
    }

    public boolean hasNoDragOffset() {
        return this.mTransOffsetX <= 0.0F && this.mTransOffsetY <= 0.0F;
    }

    public boolean canZoomOutMoreX() {
        return this.mScaleX > this.mMinScaleX;
    }

    public boolean canZoomInMoreX() {
        return this.mScaleX < this.mMaxScaleX;
    }

    public boolean canZoomOutMoreY() {
        return this.mScaleY > this.mMinScaleY;
    }

    public boolean canZoomInMoreY() {
        return this.mScaleY < this.mMaxScaleY;
    }
}
