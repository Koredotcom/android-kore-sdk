package kore.botssdk.charts.renderer;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Objects;

import kore.botssdk.charts.animation.ChartAnimator;
import kore.botssdk.charts.charts.PieChart;
import kore.botssdk.charts.data.Entry;
import kore.botssdk.charts.data.PieData;
import kore.botssdk.charts.data.PieDataSet;
import kore.botssdk.charts.data.PieEntry;
import kore.botssdk.charts.formatter.ValueFormatter;
import kore.botssdk.charts.highlight.Highlight;
import kore.botssdk.charts.interfaces.datasets.IPieDataSet;
import kore.botssdk.charts.utils.MPPointF;
import kore.botssdk.charts.utils.Utils;
import kore.botssdk.charts.utils.ViewPortHandler;

public class PieChartRenderer extends DataRenderer {
    protected final PieChart mChart;
    protected final Paint mHolePaint;
    protected final Paint mTransparentCirclePaint;
    protected final Paint mValueLinePaint;
    private final TextPaint mCenterTextPaint;
    private final Paint mEntryLabelsPaint;
    private StaticLayout mCenterTextLayout;
    private CharSequence mCenterTextLastValue;
    private final RectF mCenterTextLastBounds = new RectF();
    private final RectF[] mRectBuffer = new RectF[]{new RectF(), new RectF(), new RectF()};
    protected WeakReference<Bitmap> mDrawBitmap;
    protected Canvas mBitmapCanvas;
    private final Path mPathBuffer = new Path();
    private final RectF mInnerRectBuffer = new RectF();
    private final Path mHoleCirclePath = new Path();
    protected final Path mDrawCenterTextPathBuffer = new Path();
    protected final RectF mDrawHighlightedRectF = new RectF();

    public PieChartRenderer(PieChart chart, ChartAnimator animator, ViewPortHandler viewPortHandler) {
        super(animator, viewPortHandler);
        this.mChart = chart;
        this.mHolePaint = new Paint(1);
        this.mHolePaint.setColor(-1);
        this.mHolePaint.setStyle(Paint.Style.FILL);
        this.mTransparentCirclePaint = new Paint(1);
        this.mTransparentCirclePaint.setColor(-1);
        this.mTransparentCirclePaint.setStyle(Paint.Style.FILL);
        this.mTransparentCirclePaint.setAlpha(105);
        this.mCenterTextPaint = new TextPaint(1);
        this.mCenterTextPaint.setColor(-16777216);
        this.mCenterTextPaint.setTextSize(Utils.convertDpToPixel(12.0F));
        this.mValuePaint.setTextSize(Utils.convertDpToPixel(13.0F));
        this.mValuePaint.setColor(-1);
        this.mValuePaint.setTextAlign(Paint.Align.CENTER);
        this.mEntryLabelsPaint = new Paint(1);
        this.mEntryLabelsPaint.setColor(-1);
        this.mEntryLabelsPaint.setTextAlign(Paint.Align.CENTER);
        this.mEntryLabelsPaint.setTextSize(Utils.convertDpToPixel(13.0F));
        this.mValueLinePaint = new Paint(1);
        this.mValueLinePaint.setStyle(Paint.Style.STROKE);
    }

    public Paint getPaintHole() {
        return this.mHolePaint;
    }

    public Paint getPaintTransparentCircle() {
        return this.mTransparentCirclePaint;
    }

    public TextPaint getPaintCenterText() {
        return this.mCenterTextPaint;
    }

    public Paint getPaintEntryLabels() {
        return this.mEntryLabelsPaint;
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

            drawBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_4444);
            this.mDrawBitmap = new WeakReference<>(drawBitmap);
            this.mBitmapCanvas = new Canvas(drawBitmap);
        }

        drawBitmap.eraseColor(0);
        PieData pieData = this.mChart.getData();

        for (IPieDataSet set : pieData.getDataSets()) {
            if (set.isVisible() && set.getEntryCount() > 0) {
                this.drawDataSet(c, set);
            }
        }

    }

    protected float calculateMinimumRadiusForSpacedSlice(MPPointF center, float radius, float angle, float arcStartPointX, float arcStartPointY, float startAngle, float sweepAngle) {
        float angleMiddle = startAngle + sweepAngle / 2.0F;
        float arcEndPointX = center.x + radius * (float)Math.cos((startAngle + sweepAngle) * 0.017453292F);
        float arcEndPointY = center.y + radius * (float)Math.sin((startAngle + sweepAngle) * 0.017453292F);
        float arcMidPointX = center.x + radius * (float)Math.cos(angleMiddle * 0.017453292F);
        float arcMidPointY = center.y + radius * (float)Math.sin(angleMiddle * 0.017453292F);
        double basePointsDistance = Math.sqrt(Math.pow(arcEndPointX - arcStartPointX, 2.0D) + Math.pow(arcEndPointY - arcStartPointY, 2.0D));
        float containedTriangleHeight = (float)(basePointsDistance / 2.0D * Math.tan((180.0D - (double)angle) / 2.0D * 0.017453292519943295D));
        float spacedRadius = radius - containedTriangleHeight;
        spacedRadius = (float)((double)spacedRadius - Math.sqrt(Math.pow(arcMidPointX - (arcEndPointX + arcStartPointX) / 2.0F, 2.0D) + Math.pow(arcMidPointY - (arcEndPointY + arcStartPointY) / 2.0F, 2.0D)));
        return spacedRadius;
    }

    protected float getSliceSpace(IPieDataSet dataSet) {
        if (!dataSet.isAutomaticallyDisableSliceSpacingEnabled()) {
            return dataSet.getSliceSpace();
        } else {
            float spaceSizeRatio = dataSet.getSliceSpace() / this.mViewPortHandler.getSmallestContentExtension();
            float minValueRatio = dataSet.getYMin() / this.mChart.getData().getYValueSum() * 2.0F;
            return spaceSizeRatio > minValueRatio ? 0.0F : dataSet.getSliceSpace();
        }
    }

    protected void drawDataSet(Canvas c, IPieDataSet dataSet) {
        float angle = 0.0F;
        float rotationAngle = this.mChart.getRotationAngle();
        float phaseX = this.mAnimator.getPhaseX();
        float phaseY = this.mAnimator.getPhaseY();
        RectF circleBox = this.mChart.getCircleBox();
        int entryCount = dataSet.getEntryCount();
        float[] drawAngles = this.mChart.getDrawAngles();
        MPPointF center = this.mChart.getCenterCircleBox();
        float radius = this.mChart.getRadius();
        boolean drawInnerArc = this.mChart.isDrawHoleEnabled() && !this.mChart.isDrawSlicesUnderHoleEnabled();
        float userInnerRadius = drawInnerArc ? radius * (this.mChart.getHoleRadius() / 100.0F) : 0.0F;
        float roundedRadius = (radius - radius * this.mChart.getHoleRadius() / 100.0F) / 2.0F;
        RectF roundedCircleBox = new RectF();
        boolean drawRoundedSlices = drawInnerArc && this.mChart.isDrawRoundedSlicesEnabled();
        int visibleAngleCount = 0;

        for(int j = 0; j < entryCount; ++j) {
            if (Math.abs(dataSet.getEntryForIndex(j).getY()) > Utils.FLOAT_EPSILON) {
                ++visibleAngleCount;
            }
        }

        float sliceSpace = visibleAngleCount <= 1 ? 0.0F : this.getSliceSpace(dataSet);

        for(int j = 0; j < entryCount; ++j) {
            float sliceAngle = drawAngles[j];
            float innerRadius = userInnerRadius;
            Entry e = dataSet.getEntryForIndex(j);
            if (!(Math.abs(e.getY()) > Utils.FLOAT_EPSILON)) {
                angle += sliceAngle * phaseX;
            } else if (this.mChart.needsHighlight(j) && !drawRoundedSlices) {
                angle += sliceAngle * phaseX;
            } else {
                boolean accountForSliceSpacing = sliceSpace > 0.0F && sliceAngle <= 180.0F;
                this.mRenderPaint.setColor(dataSet.getColor(j));
                float sliceSpaceAngleOuter = visibleAngleCount == 1 ? 0.0F : sliceSpace / (0.017453292F * radius);
                float startAngleOuter = rotationAngle + (angle + sliceSpaceAngleOuter / 2.0F) * phaseY;
                float sweepAngleOuter = (sliceAngle - sliceSpaceAngleOuter) * phaseY;
                if (sweepAngleOuter < 0.0F) {
                    sweepAngleOuter = 0.0F;
                }

                this.mPathBuffer.reset();
                float arcStartPointX;
                float arcStartPointY;
                if (drawRoundedSlices) {
                    arcStartPointX = center.x + (radius - roundedRadius) * (float)Math.cos(startAngleOuter * 0.017453292F);
                    arcStartPointY = center.y + (radius - roundedRadius) * (float)Math.sin(startAngleOuter * 0.017453292F);
                    roundedCircleBox.set(arcStartPointX - roundedRadius, arcStartPointY - roundedRadius, arcStartPointX + roundedRadius, arcStartPointY + roundedRadius);
                }

                arcStartPointX = center.x + radius * (float)Math.cos(startAngleOuter * 0.017453292F);
                arcStartPointY = center.y + radius * (float)Math.sin(startAngleOuter * 0.017453292F);
                if (sweepAngleOuter >= 360.0F && sweepAngleOuter % 360.0F <= Utils.FLOAT_EPSILON) {
                    this.mPathBuffer.addCircle(center.x, center.y, radius, Path.Direction.CW);
                } else {
                    if (drawRoundedSlices) {
                        this.mPathBuffer.arcTo(roundedCircleBox, startAngleOuter + 180.0F, -180.0F);
                    }

                    this.mPathBuffer.arcTo(circleBox, startAngleOuter, sweepAngleOuter);
                }

                this.mInnerRectBuffer.set(center.x - userInnerRadius, center.y - userInnerRadius, center.x + userInnerRadius, center.y + userInnerRadius);
                float minSpacedRadius;
                float startAngleInner;
                float sweepAngleInner;
                float endAngleInner;
                if (!drawInnerArc || !(userInnerRadius > 0.0F) && !accountForSliceSpacing) {
                    if (sweepAngleOuter % 360.0F > Utils.FLOAT_EPSILON) {
                        if (accountForSliceSpacing) {
                            minSpacedRadius = startAngleOuter + sweepAngleOuter / 2.0F;
                            startAngleInner = this.calculateMinimumRadiusForSpacedSlice(center, radius, sliceAngle * phaseY, arcStartPointX, arcStartPointY, startAngleOuter, sweepAngleOuter);
                            sweepAngleInner = center.x + startAngleInner * (float)Math.cos(minSpacedRadius * 0.017453292F);
                            endAngleInner = center.y + startAngleInner * (float)Math.sin(minSpacedRadius * 0.017453292F);
                            this.mPathBuffer.lineTo(sweepAngleInner, endAngleInner);
                        } else {
                            this.mPathBuffer.lineTo(center.x, center.y);
                        }
                    }
                } else {
                    if (accountForSliceSpacing) {
                        minSpacedRadius = this.calculateMinimumRadiusForSpacedSlice(center, radius, sliceAngle * phaseY, arcStartPointX, arcStartPointY, startAngleOuter, sweepAngleOuter);
                        if (minSpacedRadius < 0.0F) {
                            minSpacedRadius = -minSpacedRadius;
                        }

                        innerRadius = Math.max(userInnerRadius, minSpacedRadius);
                    }

                    minSpacedRadius = visibleAngleCount != 1 && innerRadius != 0.0F ? sliceSpace / (0.017453292F * innerRadius) : 0.0F;
                    startAngleInner = rotationAngle + (angle + minSpacedRadius / 2.0F) * phaseY;
                    sweepAngleInner = (sliceAngle - minSpacedRadius) * phaseY;
                    if (sweepAngleInner < 0.0F) {
                        sweepAngleInner = 0.0F;
                    }

                    endAngleInner = startAngleInner + sweepAngleInner;
                    if (sweepAngleOuter >= 360.0F && sweepAngleOuter % 360.0F <= Utils.FLOAT_EPSILON) {
                        this.mPathBuffer.addCircle(center.x, center.y, innerRadius, Path.Direction.CCW);
                    } else {
                        if (drawRoundedSlices) {
                            float x = center.x + (radius - roundedRadius) * (float)Math.cos(endAngleInner * 0.017453292F);
                            float y = center.y + (radius - roundedRadius) * (float)Math.sin(endAngleInner * 0.017453292F);
                            roundedCircleBox.set(x - roundedRadius, y - roundedRadius, x + roundedRadius, y + roundedRadius);
                            this.mPathBuffer.arcTo(roundedCircleBox, endAngleInner, 180.0F);
                        } else {
                            this.mPathBuffer.lineTo(center.x + innerRadius * (float)Math.cos(endAngleInner * 0.017453292F), center.y + innerRadius * (float)Math.sin(endAngleInner * 0.017453292F));
                        }

                        this.mPathBuffer.arcTo(this.mInnerRectBuffer, endAngleInner, -sweepAngleInner);
                    }
                }

                this.mPathBuffer.close();
                this.mBitmapCanvas.drawPath(this.mPathBuffer, this.mRenderPaint);
                angle += sliceAngle * phaseX;
            }
        }

        MPPointF.recycleInstance(center);
    }

    public void drawValues(Canvas c) {
        MPPointF center = this.mChart.getCenterCircleBox();
        float radius = this.mChart.getRadius();
        float rotationAngle = this.mChart.getRotationAngle();
        float[] drawAngles = this.mChart.getDrawAngles();
        float[] absoluteAngles = this.mChart.getAbsoluteAngles();
        float phaseX = this.mAnimator.getPhaseX();
        float phaseY = this.mAnimator.getPhaseY();
        float roundedRadius = (radius - radius * this.mChart.getHoleRadius() / 100.0F) / 2.0F;
        float holeRadiusPercent = this.mChart.getHoleRadius() / 100.0F;
        float labelRadiusOffset = radius / 10.0F * 3.6F;
        if (this.mChart.isDrawHoleEnabled()) {
            labelRadiusOffset = (radius - radius * holeRadiusPercent) / 2.0F;
            if (!this.mChart.isDrawSlicesUnderHoleEnabled() && this.mChart.isDrawRoundedSlicesEnabled()) {
                rotationAngle = (float)((double)rotationAngle + (double)(roundedRadius * 360.0F) / (6.283185307179586D * (double)radius));
            }
        }

        float labelRadius = radius - labelRadiusOffset;
        PieData data = this.mChart.getData();
        List<IPieDataSet> dataSets = data.getDataSets();
        float yValueSum = data.getYValueSum();
        boolean drawEntryLabels = this.mChart.isDrawEntryLabelsEnabled();
        int xIndex = 0;
        c.save();
        float offset = Utils.convertDpToPixel(5.0F);

        for(int i = 0; i < dataSets.size(); ++i) {
            IPieDataSet dataSet = dataSets.get(i);
            boolean drawValues = dataSet.isDrawValuesEnabled();
            if (drawValues || drawEntryLabels) {
                PieDataSet.ValuePosition xValuePosition = dataSet.getXValuePosition();
                PieDataSet.ValuePosition yValuePosition = dataSet.getYValuePosition();
                this.applyValueTextStyle(dataSet);
                float lineHeight = (float)Utils.calcTextHeight(this.mValuePaint, "Q") + Utils.convertDpToPixel(4.0F);
                ValueFormatter formatter = dataSet.getValueFormatter();
                int entryCount = dataSet.getEntryCount();
                this.mValueLinePaint.setColor(dataSet.getValueLineColor());
                this.mValueLinePaint.setStrokeWidth(Utils.convertDpToPixel(dataSet.getValueLineWidth()));
                float sliceSpace = this.getSliceSpace(dataSet);
                MPPointF iconsOffset = MPPointF.getInstance(dataSet.getIconsOffset());
                iconsOffset.x = Utils.convertDpToPixel(iconsOffset.x);
                iconsOffset.y = Utils.convertDpToPixel(iconsOffset.y);

                for(int j = 0; j < entryCount; ++j) {
                    PieEntry entry = dataSet.getEntryForIndex(j);
                    float angle;
                    if (xIndex == 0) {
                        angle = 0.0F;
                    } else {
                        angle = absoluteAngles[xIndex - 1] * phaseX;
                    }

                    float sliceAngle = drawAngles[xIndex];
                    float sliceSpaceMiddleAngle = sliceSpace / (0.017453292F * labelRadius);
                    float angleOffset = (sliceAngle - sliceSpaceMiddleAngle / 2.0F) / 2.0F;
                    angle += angleOffset;
                    float transformedAngle = rotationAngle + angle * phaseY;
                    float value = this.mChart.isUsePercentValuesEnabled() ? entry.getY() / yValueSum * 100.0F : entry.getY();
                    String formattedValue = formatter.getPieLabel(value, entry);
                    String entryLabel = entry.getLabel();
                    float sliceXBase = (float)Math.cos(transformedAngle * 0.017453292F);
                    float sliceYBase = (float)Math.sin(transformedAngle * 0.017453292F);
                    boolean drawXOutside = drawEntryLabels && xValuePosition == PieDataSet.ValuePosition.OUTSIDE_SLICE;
                    boolean drawYOutside = drawValues && yValuePosition == PieDataSet.ValuePosition.OUTSIDE_SLICE;
                    boolean drawXInside = drawEntryLabels && xValuePosition == PieDataSet.ValuePosition.INSIDE_SLICE;
                    boolean drawYInside = drawValues && yValuePosition == PieDataSet.ValuePosition.INSIDE_SLICE;
                    float x;
                    float y;
                    if (drawXOutside || drawYOutside) {
                        x = dataSet.getValueLinePart1Length();
                        y = dataSet.getValueLinePart2Length();
                        y = dataSet.getValueLinePart1OffsetPercentage() / 100.0F;
                        float line1Radius;
                        if (this.mChart.isDrawHoleEnabled()) {
                            line1Radius = (radius - radius * holeRadiusPercent) * y + radius * holeRadiusPercent;
                        } else {
                            line1Radius = radius * y;
                        }

                        float polyline2Width = dataSet.isValueLineVariableLength() ? labelRadius * y * (float)Math.abs(Math.sin(transformedAngle * 0.017453292F)) : labelRadius * y;
                        float pt0x = line1Radius * sliceXBase + center.x;
                        float pt0y = line1Radius * sliceYBase + center.y;
                        float pt1x = labelRadius * (1.0F + x) * sliceXBase + center.x;
                        float pt1y = labelRadius * (1.0F + x) * sliceYBase + center.y;
                        float pt2x;
                        float pt2y;
                        float labelPtx;
                        float labelPty;
                        if ((double)transformedAngle % 360.0D >= 90.0D && (double)transformedAngle % 360.0D <= 270.0D) {
                            pt2x = pt1x - polyline2Width;
                            pt2y = pt1y;
                            this.mValuePaint.setTextAlign(Paint.Align.RIGHT);
                            if (drawXOutside) {
                                this.mEntryLabelsPaint.setTextAlign(Paint.Align.RIGHT);
                            }

                            labelPtx = pt2x - offset;
                        } else {
                            pt2x = pt1x + polyline2Width;
                            pt2y = pt1y;
                            this.mValuePaint.setTextAlign(Paint.Align.LEFT);
                            if (drawXOutside) {
                                this.mEntryLabelsPaint.setTextAlign(Paint.Align.LEFT);
                            }

                            labelPtx = pt2x + offset;
                        }
                        labelPty = pt1y;

                        if (dataSet.getValueLineColor() != 1122867) {
                            if (dataSet.isUsingSliceColorAsValueLineColor()) {
                                this.mValueLinePaint.setColor(dataSet.getColor(j));
                            }

                            c.drawLine(pt0x, pt0y, pt1x, pt1y, this.mValueLinePaint);
                            c.drawLine(pt1x, pt1y, pt2x, pt2y, this.mValueLinePaint);
                        }

                        if (drawXOutside && drawYOutside) {
                            this.drawValue(c, formattedValue, labelPtx, labelPty, dataSet.getValueTextColor(j));
                            if (j < data.getEntryCount() && entryLabel != null) {
                                this.drawEntryLabel(c, entryLabel, labelPtx, labelPty + lineHeight);
                            }
                        } else if (drawXOutside) {
                            if (j < data.getEntryCount() && entryLabel != null) {
                                this.drawEntryLabel(c, entryLabel, labelPtx, labelPty + lineHeight / 2.0F);
                            }
                        } else {
                            this.drawValue(c, formattedValue, labelPtx, labelPty + lineHeight / 2.0F, dataSet.getValueTextColor(j));
                        }
                    }

                    if (drawXInside || drawYInside) {
                        x = labelRadius * sliceXBase + center.x;
                        y = labelRadius * sliceYBase + center.y;
                        this.mValuePaint.setTextAlign(Paint.Align.CENTER);
                        if (drawXInside && drawYInside) {
                            this.drawValue(c, formattedValue, x, y, dataSet.getValueTextColor(j));
                            if (j < data.getEntryCount() && entryLabel != null) {
                                this.drawEntryLabel(c, entryLabel, x, y + lineHeight);
                            }
                        } else if (drawXInside) {
                            if (j < data.getEntryCount() && entryLabel != null) {
                                this.drawEntryLabel(c, entryLabel, x, y + lineHeight / 2.0F);
                            }
                        } else {
                            this.drawValue(c, formattedValue, x, y + lineHeight / 2.0F, dataSet.getValueTextColor(j));
                        }
                    }

                    if (entry.getIcon() != null && dataSet.isDrawIconsEnabled()) {
                        Drawable icon = entry.getIcon();
                        y = (labelRadius + iconsOffset.y) * sliceXBase + center.x;
                        y = (labelRadius + iconsOffset.y) * sliceYBase + center.y;
                        y += iconsOffset.x;
                        Utils.drawImage(c, icon, (int)y, (int)y, icon.getIntrinsicWidth(), icon.getIntrinsicHeight());
                    }

                    ++xIndex;
                }

                MPPointF.recycleInstance(iconsOffset);
            }
        }

        MPPointF.recycleInstance(center);
        c.restore();
    }

    public void drawValue(Canvas c, String valueText, float x, float y, int color) {
        this.mValuePaint.setColor(color);
        c.drawText(valueText, x, y, this.mValuePaint);
    }

    protected void drawEntryLabel(Canvas c, String label, float x, float y) {
        c.drawText(label, x, y, this.mEntryLabelsPaint);
    }

    public void drawExtras(Canvas c) {
        this.drawHole(c);
        c.drawBitmap(this.mDrawBitmap.get(), 0.0F, 0.0F, null);
        this.drawCenterText(c);
    }

    protected void drawHole(Canvas c) {
        if (this.mChart.isDrawHoleEnabled() && this.mBitmapCanvas != null) {
            float radius = this.mChart.getRadius();
            float holeRadius = radius * (this.mChart.getHoleRadius() / 100.0F);
            MPPointF center = this.mChart.getCenterCircleBox();
            if (Color.alpha(this.mHolePaint.getColor()) > 0) {
                this.mBitmapCanvas.drawCircle(center.x, center.y, holeRadius, this.mHolePaint);
            }

            if (Color.alpha(this.mTransparentCirclePaint.getColor()) > 0 && this.mChart.getTransparentCircleRadius() > this.mChart.getHoleRadius()) {
                int alpha = this.mTransparentCirclePaint.getAlpha();
                float secondHoleRadius = radius * (this.mChart.getTransparentCircleRadius() / 100.0F);
                this.mTransparentCirclePaint.setAlpha((int)((float)alpha * this.mAnimator.getPhaseX() * this.mAnimator.getPhaseY()));
                this.mHoleCirclePath.reset();
                this.mHoleCirclePath.addCircle(center.x, center.y, secondHoleRadius, Path.Direction.CW);
                this.mHoleCirclePath.addCircle(center.x, center.y, holeRadius, Path.Direction.CCW);
                this.mBitmapCanvas.drawPath(this.mHoleCirclePath, this.mTransparentCirclePaint);
                this.mTransparentCirclePaint.setAlpha(alpha);
            }

            MPPointF.recycleInstance(center);
        }

    }

    protected void drawCenterText(Canvas c) {
        CharSequence centerText = this.mChart.getCenterText();
        if (this.mChart.isDrawCenterTextEnabled() && centerText != null) {
            MPPointF center = this.mChart.getCenterCircleBox();
            MPPointF offset = this.mChart.getCenterTextOffset();
            float x = center.x + offset.x;
            float y = center.y + offset.y;
            float innerRadius = this.mChart.isDrawHoleEnabled() && !this.mChart.isDrawSlicesUnderHoleEnabled() ? this.mChart.getRadius() * (this.mChart.getHoleRadius() / 100.0F) : this.mChart.getRadius();
            RectF holeRect = this.mRectBuffer[0];
            holeRect.left = x - innerRadius;
            holeRect.top = y - innerRadius;
            holeRect.right = x + innerRadius;
            holeRect.bottom = y + innerRadius;
            RectF boundingRect = this.mRectBuffer[1];
            boundingRect.set(holeRect);
            float radiusPercent = this.mChart.getCenterTextRadiusPercent() / 100.0F;
            if ((double)radiusPercent > 0.0D) {
                boundingRect.inset((boundingRect.width() - boundingRect.width() * radiusPercent) / 2.0F, (boundingRect.height() - boundingRect.height() * radiusPercent) / 2.0F);
            }

            float layoutHeight;
            if (!Objects.equals(centerText, this.mCenterTextLastValue) || !Objects.equals(boundingRect, this.mCenterTextLastBounds)) {
                this.mCenterTextLastBounds.set(boundingRect);
                this.mCenterTextLastValue = centerText;
                layoutHeight = this.mCenterTextLastBounds.width();
                this.mCenterTextLayout = new StaticLayout(centerText, 0, centerText.length(), this.mCenterTextPaint, (int)Math.max(Math.ceil(layoutHeight), 1.0D), Layout.Alignment.ALIGN_CENTER, 1.0F, 0.0F, false);
            }

            layoutHeight = (float)this.mCenterTextLayout.getHeight();
            c.save();
            Path path = this.mDrawCenterTextPathBuffer;
            path.reset();
            path.addOval(holeRect, Path.Direction.CW);
            c.clipPath(path);

            c.translate(boundingRect.left, boundingRect.top + (boundingRect.height() - layoutHeight) / 2.0F);
            this.mCenterTextLayout.draw(c);
            c.restore();
            MPPointF.recycleInstance(center);
            MPPointF.recycleInstance(offset);
        }

    }

    public void drawHighlighted(Canvas c, Highlight[] indices) {
        boolean drawInnerArc = this.mChart.isDrawHoleEnabled() && !this.mChart.isDrawSlicesUnderHoleEnabled();
        if (!drawInnerArc || !this.mChart.isDrawRoundedSlicesEnabled()) {
            float phaseX = this.mAnimator.getPhaseX();
            float phaseY = this.mAnimator.getPhaseY();
            float rotationAngle = this.mChart.getRotationAngle();
            float[] drawAngles = this.mChart.getDrawAngles();
            float[] absoluteAngles = this.mChart.getAbsoluteAngles();
            MPPointF center = this.mChart.getCenterCircleBox();
            float radius = this.mChart.getRadius();
            float userInnerRadius = drawInnerArc ? radius * (this.mChart.getHoleRadius() / 100.0F) : 0.0F;
            RectF highlightedCircleBox = this.mDrawHighlightedRectF;
            highlightedCircleBox.set(0.0F, 0.0F, 0.0F, 0.0F);

            for (Highlight highlight : indices) {
                int index = (int) highlight.getX();
                if (index < drawAngles.length) {
                    IPieDataSet set = this.mChart.getData().getDataSetByIndex(highlight.getDataSetIndex());
                    if (set != null && set.isHighlightEnabled()) {
                        int entryCount = set.getEntryCount();
                        int visibleAngleCount = 0;

                        for (int j = 0; j < entryCount; ++j) {
                            if (Math.abs(set.getEntryForIndex(j).getY()) > Utils.FLOAT_EPSILON) {
                                ++visibleAngleCount;
                            }
                        }

                        float angle;
                        if (index == 0) {
                            angle = 0.0F;
                        } else {
                            angle = absoluteAngles[index - 1] * phaseX;
                        }

                        float sliceSpace = visibleAngleCount <= 1 ? 0.0F : set.getSliceSpace();
                        float sliceAngle = drawAngles[index];
                        float innerRadius = userInnerRadius;
                        float shift = set.getSelectionShift();
                        float highlightedRadius = radius + shift;
                        highlightedCircleBox.set(this.mChart.getCircleBox());
                        highlightedCircleBox.inset(-shift, -shift);
                        boolean accountForSliceSpacing = sliceSpace > 0.0F && sliceAngle <= 180.0F;
                        this.mRenderPaint.setColor(set.getColor(index));
                        float sliceSpaceAngleOuter = visibleAngleCount == 1 ? 0.0F : sliceSpace / (0.017453292F * radius);
                        float sliceSpaceAngleShifted = visibleAngleCount == 1 ? 0.0F : sliceSpace / (0.017453292F * highlightedRadius);
                        float startAngleOuter = rotationAngle + (angle + sliceSpaceAngleOuter / 2.0F) * phaseY;
                        float sweepAngleOuter = (sliceAngle - sliceSpaceAngleOuter) * phaseY;
                        if (sweepAngleOuter < 0.0F) {
                            sweepAngleOuter = 0.0F;
                        }

                        float startAngleShifted = rotationAngle + (angle + sliceSpaceAngleShifted / 2.0F) * phaseY;
                        float sweepAngleShifted = (sliceAngle - sliceSpaceAngleShifted) * phaseY;
                        if (sweepAngleShifted < 0.0F) {
                            sweepAngleShifted = 0.0F;
                        }

                        this.mPathBuffer.reset();
                        if (sweepAngleOuter >= 360.0F && sweepAngleOuter % 360.0F <= Utils.FLOAT_EPSILON) {
                            this.mPathBuffer.addCircle(center.x, center.y, highlightedRadius, Path.Direction.CW);
                        } else {
                            this.mPathBuffer.moveTo(center.x + highlightedRadius * (float) Math.cos(startAngleShifted * 0.017453292F), center.y + highlightedRadius * (float) Math.sin(startAngleShifted * 0.017453292F));
                            this.mPathBuffer.arcTo(highlightedCircleBox, startAngleShifted, sweepAngleShifted);
                        }

                        float sliceSpaceRadius = 0.0F;
                        if (accountForSliceSpacing) {
                            sliceSpaceRadius = this.calculateMinimumRadiusForSpacedSlice(center, radius, sliceAngle * phaseY, center.x + radius * (float) Math.cos(startAngleOuter * 0.017453292F), center.y + radius * (float) Math.sin(startAngleOuter * 0.017453292F), startAngleOuter, sweepAngleOuter);
                        }

                        this.mInnerRectBuffer.set(center.x - userInnerRadius, center.y - userInnerRadius, center.x + userInnerRadius, center.y + userInnerRadius);
                        float angleMiddle;
                        float arcEndPointX;
                        float sweepAngleInner;
                        if (drawInnerArc && (userInnerRadius > 0.0F || accountForSliceSpacing)) {
                            if (accountForSliceSpacing) {
                                angleMiddle = sliceSpaceRadius;
                                if (sliceSpaceRadius < 0.0F) {
                                    angleMiddle = -sliceSpaceRadius;
                                }

                                innerRadius = Math.max(userInnerRadius, angleMiddle);
                            }

                            angleMiddle = visibleAngleCount != 1 && innerRadius != 0.0F ? sliceSpace / (0.017453292F * innerRadius) : 0.0F;
                            arcEndPointX = rotationAngle + (angle + angleMiddle / 2.0F) * phaseY;
                            sweepAngleInner = (sliceAngle - angleMiddle) * phaseY;
                            if (sweepAngleInner < 0.0F) {
                                sweepAngleInner = 0.0F;
                            }

                            float endAngleInner = arcEndPointX + sweepAngleInner;
                            if (sweepAngleOuter >= 360.0F && sweepAngleOuter % 360.0F <= Utils.FLOAT_EPSILON) {
                                this.mPathBuffer.addCircle(center.x, center.y, innerRadius, Path.Direction.CCW);
                            } else {
                                this.mPathBuffer.lineTo(center.x + innerRadius * (float) Math.cos(endAngleInner * 0.017453292F), center.y + innerRadius * (float) Math.sin(endAngleInner * 0.017453292F));
                                this.mPathBuffer.arcTo(this.mInnerRectBuffer, endAngleInner, -sweepAngleInner);
                            }
                        } else if (sweepAngleOuter % 360.0F > Utils.FLOAT_EPSILON) {
                            if (accountForSliceSpacing) {
                                angleMiddle = startAngleOuter + sweepAngleOuter / 2.0F;
                                arcEndPointX = center.x + sliceSpaceRadius * (float) Math.cos(angleMiddle * 0.017453292F);
                                sweepAngleInner = center.y + sliceSpaceRadius * (float) Math.sin(angleMiddle * 0.017453292F);
                                this.mPathBuffer.lineTo(arcEndPointX, sweepAngleInner);
                            } else {
                                this.mPathBuffer.lineTo(center.x, center.y);
                            }
                        }

                        this.mPathBuffer.close();
                        this.mBitmapCanvas.drawPath(this.mPathBuffer, this.mRenderPaint);
                    }
                }
            }

            MPPointF.recycleInstance(center);
        }
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
}
