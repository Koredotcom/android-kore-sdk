package kore.botssdk.charts.renderer;

import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Typeface;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import kore.botssdk.charts.components.Legend;
import kore.botssdk.charts.components.LegendEntry;
import kore.botssdk.charts.data.ChartData;
import kore.botssdk.charts.data.PieEntry;
import kore.botssdk.charts.interfaces.datasets.IBarDataSet;
import kore.botssdk.charts.interfaces.datasets.ICandleDataSet;
import kore.botssdk.charts.interfaces.datasets.IDataSet;
import kore.botssdk.charts.interfaces.datasets.IPieDataSet;
import kore.botssdk.charts.utils.FSize;
import kore.botssdk.charts.utils.Utils;
import kore.botssdk.charts.utils.ViewPortHandler;

public class LegendRenderer extends Renderer {
    protected final Paint mLegendLabelPaint;
    protected final Paint mLegendFormPaint;
    protected final Legend mLegend;
    protected final List<LegendEntry> computedEntries = new ArrayList(16);
    protected final Paint.FontMetrics legendFontMetrics = new Paint.FontMetrics();
    private final Path mLineFormPath = new Path();

    public LegendRenderer(ViewPortHandler viewPortHandler, Legend legend) {
        super(viewPortHandler);
        this.mLegend = legend;
        this.mLegendLabelPaint = new Paint(1);
        this.mLegendLabelPaint.setTextSize(Utils.convertDpToPixel(9.0F));
        this.mLegendLabelPaint.setTextAlign(Paint.Align.LEFT);
        this.mLegendFormPaint = new Paint(1);
        this.mLegendFormPaint.setStyle(Paint.Style.FILL);
    }

    public Paint getLabelPaint() {
        return this.mLegendLabelPaint;
    }

    public Paint getFormPaint() {
        return this.mLegendFormPaint;
    }

    public void computeLegend(ChartData<?> data) {
        if (!this.mLegend.isLegendCustom()) {
            this.computedEntries.clear();

            for(int i = 0; i < data.getDataSetCount(); ++i) {
                IDataSet dataSet = data.getDataSetByIndex(i);
                List<Integer> clrs = dataSet.getColors();
                int entryCount = dataSet.getEntryCount();
                if (dataSet instanceof IBarDataSet && ((IBarDataSet)dataSet).isStacked()) {
                    IBarDataSet bds = (IBarDataSet)dataSet;
                    String[] sLabels = bds.getStackLabels();

                    for(int j = 0; j < clrs.size() && j < bds.getStackSize(); ++j) {
                        this.computedEntries.add(new LegendEntry(sLabels[j % sLabels.length], dataSet.getForm(), dataSet.getFormSize(), dataSet.getFormLineWidth(), dataSet.getFormLineDashEffect(), clrs.get(j)));
                    }

                    if (bds.getLabel() != null) {
                        this.computedEntries.add(new LegendEntry(dataSet.getLabel(), Legend.LegendForm.NONE, 0.0F / 0.0F, 0.0F / 0.0F, null, 1122867));
                    }
                } else {
                    int j;
                    if (dataSet instanceof IPieDataSet) {
                        IPieDataSet pds = (IPieDataSet)dataSet;

                        for(j = 0; j < clrs.size() && j < entryCount; ++j) {
                            this.computedEntries.add(new LegendEntry(pds.getEntryForIndex(j).getLabel(), dataSet.getForm(), dataSet.getFormSize(), dataSet.getFormLineWidth(), dataSet.getFormLineDashEffect(), clrs.get(j)));
                        }

                        if (pds.getLabel() != null) {
                            this.computedEntries.add(new LegendEntry(dataSet.getLabel(), Legend.LegendForm.NONE, 0.0F / 0.0F, 0.0F / 0.0F, null, 1122867));
                        }
                    } else {
                        if (dataSet instanceof ICandleDataSet && ((ICandleDataSet)dataSet).getDecreasingColor() != 1122867) {
                            j = ((ICandleDataSet)dataSet).getDecreasingColor();
                            j = ((ICandleDataSet)dataSet).getIncreasingColor();
                            this.computedEntries.add(new LegendEntry(null, dataSet.getForm(), dataSet.getFormSize(), dataSet.getFormLineWidth(), dataSet.getFormLineDashEffect(), j));
                            this.computedEntries.add(new LegendEntry(dataSet.getLabel(), dataSet.getForm(), dataSet.getFormSize(), dataSet.getFormLineWidth(), dataSet.getFormLineDashEffect(), j));
                        } else {
                            for(j = 0; j < clrs.size() && j < entryCount; ++j) {
                                String label;
                                if (j < clrs.size() - 1 && j < entryCount - 1) {
                                    label = null;
                                } else {
                                    label = data.getDataSetByIndex(i).getLabel();
                                }

                                this.computedEntries.add(new LegendEntry(label, dataSet.getForm(), dataSet.getFormSize(), dataSet.getFormLineWidth(), dataSet.getFormLineDashEffect(), clrs.get(j)));
                            }
                        }
                    }
                }
            }

            if (this.mLegend.getExtraEntries() != null) {
                Collections.addAll(this.computedEntries, this.mLegend.getExtraEntries());
            }

            this.mLegend.setEntries(this.computedEntries);
        }

        Typeface tf = this.mLegend.getTypeface();
        if (tf != null) {
            this.mLegendLabelPaint.setTypeface(tf);
        }

        this.mLegendLabelPaint.setTextSize(this.mLegend.getTextSize());
        this.mLegendLabelPaint.setColor(this.mLegend.getTextColor());
        this.mLegend.calculateDimensions(this.mLegendLabelPaint, this.mViewPortHandler);
    }

    public void renderLegend(Canvas c) {
        if (this.mLegend.isEnabled()) {
            Typeface tf = this.mLegend.getTypeface();
            if (tf != null) {
                this.mLegendLabelPaint.setTypeface(tf);
            }

            this.mLegendLabelPaint.setTextSize(this.mLegend.getTextSize());
            this.mLegendLabelPaint.setColor(this.mLegend.getTextColor());
            float labelLineHeight = Utils.getLineHeight(this.mLegendLabelPaint, this.legendFontMetrics);
            float labelLineSpacing = Utils.getLineSpacing(this.mLegendLabelPaint, this.legendFontMetrics) + Utils.convertDpToPixel(this.mLegend.getYEntrySpace());
            float formYOffset = labelLineHeight - (float)Utils.calcTextHeight(this.mLegendLabelPaint, "ABC") / 2.0F;
            LegendEntry[] entries = this.mLegend.getEntries();
            float formToTextSpace = Utils.convertDpToPixel(this.mLegend.getFormToTextSpace());
            float xEntrySpace = Utils.convertDpToPixel(this.mLegend.getXEntrySpace());
            Legend.LegendOrientation orientation = this.mLegend.getOrientation();
            Legend.LegendHorizontalAlignment horizontalAlignment = this.mLegend.getHorizontalAlignment();
            Legend.LegendVerticalAlignment verticalAlignment = this.mLegend.getVerticalAlignment();
            Legend.LegendDirection direction = this.mLegend.getDirection();
            float defaultFormSize = Utils.convertDpToPixel(this.mLegend.getFormSize());
            float stackSpace = Utils.convertDpToPixel(this.mLegend.getStackSpace());
            float yoffset = this.mLegend.getYOffset();
            float xoffset = this.mLegend.getXOffset();
            float originPosX = 0.0F;
            switch(horizontalAlignment) {
                case LEFT:
                    if (orientation == Legend.LegendOrientation.VERTICAL) {
                        originPosX = xoffset;
                    } else {
                        originPosX = this.mViewPortHandler.contentLeft() + xoffset;
                    }

                    if (direction == Legend.LegendDirection.RIGHT_TO_LEFT) {
                        originPosX += this.mLegend.mNeededWidth;
                    }
                    break;
                case RIGHT:
                    if (orientation == Legend.LegendOrientation.VERTICAL) {
                        originPosX = this.mViewPortHandler.getChartWidth() - xoffset;
                    } else {
                        originPosX = this.mViewPortHandler.contentRight() - xoffset;
                    }

                    if (direction == Legend.LegendDirection.LEFT_TO_RIGHT) {
                        originPosX -= this.mLegend.mNeededWidth;
                    }
                    break;
                case CENTER:
                    if (orientation == Legend.LegendOrientation.VERTICAL) {
                        originPosX = this.mViewPortHandler.getChartWidth() / 2.0F;
                    } else {
                        originPosX = this.mViewPortHandler.contentLeft() + this.mViewPortHandler.contentWidth() / 2.0F;
                    }

                    originPosX += direction == Legend.LegendDirection.LEFT_TO_RIGHT ? xoffset : -xoffset;
                    if (orientation == Legend.LegendOrientation.VERTICAL) {
                        originPosX = (float)((double)originPosX + (direction == Legend.LegendDirection.LEFT_TO_RIGHT ? (double)(-this.mLegend.mNeededWidth) / 2.0D + (double)xoffset : (double)this.mLegend.mNeededWidth / 2.0D - (double)xoffset));
                    }
            }

            switch(orientation) {
                case HORIZONTAL:
                    List<FSize> calculatedLineSizes = this.mLegend.getCalculatedLineSizes();
                    List<FSize> calculatedLabelSizes = this.mLegend.getCalculatedLabelSizes();
                    List<Boolean> calculatedLabelBreakPoints = this.mLegend.getCalculatedLabelBreakPoints();
                    float posX = originPosX;
                    float posY = 0.0F;
                    switch(verticalAlignment) {
                        case TOP:
                            posY = yoffset;
                            break;
                        case BOTTOM:
                            posY = this.mViewPortHandler.getChartHeight() - yoffset - this.mLegend.mNeededHeight;
                            break;
                        case CENTER:
                            posY = (this.mViewPortHandler.getChartHeight() - this.mLegend.mNeededHeight) / 2.0F + yoffset;
                    }

                    int lineIndex = 0;
                    int i = 0;

                    for(int count = entries.length; i < count; ++i) {
                        LegendEntry e = entries[i];
                        boolean drawingForm = e.form != Legend.LegendForm.NONE;
                        float formSize = Float.isNaN(e.formSize) ? defaultFormSize : Utils.convertDpToPixel(e.formSize);
                        if (i < calculatedLabelBreakPoints.size() && calculatedLabelBreakPoints.get(i)) {
                            posX = originPosX;
                            posY += labelLineHeight + labelLineSpacing;
                        }

                        if (posX == originPosX && horizontalAlignment == Legend.LegendHorizontalAlignment.CENTER && lineIndex < calculatedLineSizes.size()) {
                            posX += (direction == Legend.LegendDirection.RIGHT_TO_LEFT ? calculatedLineSizes.get(lineIndex).width : -calculatedLineSizes.get(lineIndex).width) / 2.0F;
                            ++lineIndex;
                        }

                        boolean isStacked = e.label == null;
                        if (drawingForm) {
                            if (direction == Legend.LegendDirection.RIGHT_TO_LEFT) {
                                posX -= formSize;
                            }

                            this.drawForm(c, posX, posY + formYOffset, e, this.mLegend);
                            if (direction == Legend.LegendDirection.LEFT_TO_RIGHT) {
                                posX += formSize;
                            }
                        }

                        if (!isStacked) {
                            if (drawingForm) {
                                posX += direction == Legend.LegendDirection.RIGHT_TO_LEFT ? -formToTextSpace : formToTextSpace;
                            }

                            if (direction == Legend.LegendDirection.RIGHT_TO_LEFT) {
                                posX -= calculatedLabelSizes.get(i).width;
                            }

                            this.drawLabel(c, posX, posY + labelLineHeight, e.label);
                            if (direction == Legend.LegendDirection.LEFT_TO_RIGHT) {
                                posX += calculatedLabelSizes.get(i).width;
                            }

                            posX += direction == Legend.LegendDirection.RIGHT_TO_LEFT ? -xEntrySpace : xEntrySpace;
                        } else {
                            posX += direction == Legend.LegendDirection.RIGHT_TO_LEFT ? -stackSpace : stackSpace;
                        }
                    }

                    return;
                case VERTICAL:
                    float stack = 0.0F;
                    boolean wasStacked = false;
                    posY = 0.0F;
                    switch(verticalAlignment) {
                        case TOP:
                            posY = horizontalAlignment == Legend.LegendHorizontalAlignment.CENTER ? 0.0F : this.mViewPortHandler.contentTop();
                            posY += yoffset;
                            break;
                        case BOTTOM:
                            posY = horizontalAlignment == Legend.LegendHorizontalAlignment.CENTER ? this.mViewPortHandler.getChartHeight() : this.mViewPortHandler.contentBottom();
                            posY -= this.mLegend.mNeededHeight + yoffset;
                            break;
                        case CENTER:
                            posY = this.mViewPortHandler.getChartHeight() / 2.0F - this.mLegend.mNeededHeight / 2.0F + this.mLegend.getYOffset();
                    }

                    for(i = 0; i < entries.length; ++i) {
                        LegendEntry e = entries[i];
                        boolean drawingForm = e.form != Legend.LegendForm.NONE;
                        float formSize = Float.isNaN(e.formSize) ? defaultFormSize : Utils.convertDpToPixel(e.formSize);
                        posX = originPosX;
                        if (drawingForm) {
                            if (direction == Legend.LegendDirection.LEFT_TO_RIGHT) {
                                posX = originPosX + stack;
                            } else {
                                posX = originPosX - (formSize - stack);
                            }

                            this.drawForm(c, posX, posY + formYOffset, e, this.mLegend);
                            if (direction == Legend.LegendDirection.LEFT_TO_RIGHT) {
                                posX += formSize;
                            }
                        }

                        if (e.label == null) {
                            stack += formSize + stackSpace;
                            wasStacked = true;
                        } else {
                            if (drawingForm && !wasStacked) {
                                posX += direction == Legend.LegendDirection.LEFT_TO_RIGHT ? formToTextSpace : -formToTextSpace;
                            } else if (wasStacked) {
                                posX = originPosX;
                            }

                            if (direction == Legend.LegendDirection.RIGHT_TO_LEFT) {
                                posX -= (float)Utils.calcTextWidth(this.mLegendLabelPaint, e.label);
                            }

                            if (!wasStacked) {
                                this.drawLabel(c, posX, posY + labelLineHeight, e.label);
                            } else {
                                posY += labelLineHeight + labelLineSpacing;
                                this.drawLabel(c, posX, posY + labelLineHeight, e.label);
                            }

                            posY += labelLineHeight + labelLineSpacing;
                            stack = 0.0F;
                        }
                    }
            }

        }
    }

    protected void drawForm(Canvas c, float x, float y, LegendEntry entry, Legend legend) {
        if (entry.formColor != 1122868 && entry.formColor != 1122867 && entry.formColor != 0) {
            int restoreCount = c.save();
            Legend.LegendForm form = entry.form;
            if (form == Legend.LegendForm.DEFAULT) {
                form = legend.getForm();
            }

            this.mLegendFormPaint.setColor(entry.formColor);
            float formSize = Utils.convertDpToPixel(Float.isNaN(entry.formSize) ? legend.getFormSize() : entry.formSize);
            float half = formSize / 2.0F;
            switch(form) {
                case NONE:
                case EMPTY:
                default:
                    break;
                case DEFAULT:
                case CIRCLE:
                    this.mLegendFormPaint.setStyle(Paint.Style.FILL);
                    c.drawCircle(x + half, y, half, this.mLegendFormPaint);
                    break;
                case SQUARE:
                    this.mLegendFormPaint.setStyle(Paint.Style.FILL);
                    c.drawRect(x, y - half, x + formSize, y + half, this.mLegendFormPaint);
                    break;
                case LINE:
                    float formLineWidth = Utils.convertDpToPixel(Float.isNaN(entry.formLineWidth) ? legend.getFormLineWidth() : entry.formLineWidth);
                    DashPathEffect formLineDashEffect = entry.formLineDashEffect == null ? legend.getFormLineDashEffect() : entry.formLineDashEffect;
                    this.mLegendFormPaint.setStyle(Paint.Style.STROKE);
                    this.mLegendFormPaint.setStrokeWidth(formLineWidth);
                    this.mLegendFormPaint.setPathEffect(formLineDashEffect);
                    this.mLineFormPath.reset();
                    this.mLineFormPath.moveTo(x, y);
                    this.mLineFormPath.lineTo(x + formSize, y);
                    c.drawPath(this.mLineFormPath, this.mLegendFormPaint);
            }

            c.restoreToCount(restoreCount);
        }
    }

    protected void drawLabel(Canvas c, float x, float y, String label) {
        c.drawText(label, x, y, this.mLegendLabelPaint);
    }
}
