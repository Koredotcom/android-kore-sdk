package com.kore.ai.widgetsdk.charts.components;

import android.graphics.DashPathEffect;
import android.graphics.Paint;

import com.kore.ai.widgetsdk.charts.utils.FSize;
import com.kore.ai.widgetsdk.charts.utils.Utils;
import com.kore.ai.widgetsdk.charts.utils.ViewPortHandler;

import java.util.ArrayList;
import java.util.List;

public class Legend extends ComponentBase {
    private LegendEntry[] mEntries;
    private LegendEntry[] mExtraEntries;
    private boolean mIsLegendCustom;
    private com.kore.ai.widgetsdk.charts.components.Legend.LegendHorizontalAlignment mHorizontalAlignment;
    private com.kore.ai.widgetsdk.charts.components.Legend.LegendVerticalAlignment mVerticalAlignment;
    private com.kore.ai.widgetsdk.charts.components.Legend.LegendOrientation mOrientation;
    private boolean mDrawInside;
    private com.kore.ai.widgetsdk.charts.components.Legend.LegendDirection mDirection;
    private com.kore.ai.widgetsdk.charts.components.Legend.LegendForm mShape;
    private float mFormSize;
    private float mFormLineWidth;
    private DashPathEffect mFormLineDashEffect;
    private float mXEntrySpace;
    private float mYEntrySpace;
    private float mFormToTextSpace;
    private float mStackSpace;
    private float mMaxSizePercent;
    public float mNeededWidth;
    public float mNeededHeight;
    public float mTextHeightMax;
    public float mTextWidthMax;
    private boolean mWordWrapEnabled;
    private final List<FSize> mCalculatedLabelSizes;
    private final List<Boolean> mCalculatedLabelBreakPoints;
    private final List<FSize> mCalculatedLineSizes;

    public Legend() {
        this.mEntries = new LegendEntry[0];
        this.mIsLegendCustom = false;
        this.mHorizontalAlignment = com.kore.ai.widgetsdk.charts.components.Legend.LegendHorizontalAlignment.LEFT;
        this.mVerticalAlignment = com.kore.ai.widgetsdk.charts.components.Legend.LegendVerticalAlignment.BOTTOM;
        this.mOrientation = com.kore.ai.widgetsdk.charts.components.Legend.LegendOrientation.HORIZONTAL;
        this.mDrawInside = false;
        this.mDirection = com.kore.ai.widgetsdk.charts.components.Legend.LegendDirection.LEFT_TO_RIGHT;
        this.mShape = com.kore.ai.widgetsdk.charts.components.Legend.LegendForm.SQUARE;
        this.mFormSize = 8.0F;
        this.mFormLineWidth = 3.0F;
        this.mFormLineDashEffect = null;
        this.mXEntrySpace = 6.0F;
        this.mYEntrySpace = 0.0F;
        this.mFormToTextSpace = 5.0F;
        this.mStackSpace = 3.0F;
        this.mMaxSizePercent = 0.95F;
        this.mNeededWidth = 0.0F;
        this.mNeededHeight = 0.0F;
        this.mTextHeightMax = 0.0F;
        this.mTextWidthMax = 0.0F;
        this.mWordWrapEnabled = false;
        this.mCalculatedLabelSizes = new ArrayList(16);
        this.mCalculatedLabelBreakPoints = new ArrayList(16);
        this.mCalculatedLineSizes = new ArrayList(16);
        this.mTextSize = Utils.convertDpToPixel(10.0F);
        this.mXOffset = Utils.convertDpToPixel(5.0F);
        this.mYOffset = Utils.convertDpToPixel(3.0F);
    }

    public Legend(LegendEntry[] entries) {
        this();
        if (entries == null) {
            throw new IllegalArgumentException("entries array is NULL");
        } else {
            this.mEntries = entries;
        }
    }

    public void setEntries(List<LegendEntry> entries) {
        this.mEntries = entries.toArray(new LegendEntry[entries.size()]);
    }

    public LegendEntry[] getEntries() {
        return this.mEntries;
    }

    public float getMaximumEntryWidth(Paint p) {
        float max = 0.0F;
        float maxFormSize = 0.0F;
        float formToTextSpace = Utils.convertDpToPixel(this.mFormToTextSpace);
        LegendEntry[] var5 = this.mEntries;
        int var6 = var5.length;

        for(int var7 = 0; var7 < var6; ++var7) {
            LegendEntry entry = var5[var7];
            float formSize = Utils.convertDpToPixel(Float.isNaN(entry.formSize) ? this.mFormSize : entry.formSize);
            if (formSize > maxFormSize) {
                maxFormSize = formSize;
            }

            String label = entry.label;
            if (label != null) {
                float length = (float)Utils.calcTextWidth(p, label);
                if (length > max) {
                    max = length;
                }
            }
        }

        return max + maxFormSize + formToTextSpace;
    }

    public float getMaximumEntryHeight(Paint p) {
        float max = 0.0F;
        LegendEntry[] var3 = this.mEntries;
        int var4 = var3.length;

        for(int var5 = 0; var5 < var4; ++var5) {
            LegendEntry entry = var3[var5];
            String label = entry.label;
            if (label != null) {
                float length = (float)Utils.calcTextHeight(p, label);
                if (length > max) {
                    max = length;
                }
            }
        }

        return max;
    }

    public LegendEntry[] getExtraEntries() {
        return this.mExtraEntries;
    }

    public void setExtra(List<LegendEntry> entries) {
        this.mExtraEntries = entries.toArray(new LegendEntry[entries.size()]);
    }

    public void setExtra(LegendEntry[] entries) {
        if (entries == null) {
            entries = new LegendEntry[0];
        }

        this.mExtraEntries = entries;
    }

    public void setExtra(int[] colors, String[] labels) {
        List<LegendEntry> entries = new ArrayList();

        for(int i = 0; i < Math.min(colors.length, labels.length); ++i) {
            LegendEntry entry = new LegendEntry();
            entry.formColor = colors[i];
            entry.label = labels[i];
            if (entry.formColor != 1122868 && entry.formColor != 0) {
                if (entry.formColor == 1122867) {
                    entry.form = com.kore.ai.widgetsdk.charts.components.Legend.LegendForm.EMPTY;
                }
            } else {
                entry.form = com.kore.ai.widgetsdk.charts.components.Legend.LegendForm.NONE;
            }

            entries.add(entry);
        }

        this.mExtraEntries = entries.toArray(new LegendEntry[entries.size()]);
    }

    public void setCustom(LegendEntry[] entries) {
        this.mEntries = entries;
        this.mIsLegendCustom = true;
    }

    public void setCustom(List<LegendEntry> entries) {
        this.mEntries = entries.toArray(new LegendEntry[entries.size()]);
        this.mIsLegendCustom = true;
    }

    public void resetCustom() {
        this.mIsLegendCustom = false;
    }

    public boolean isLegendCustom() {
        return this.mIsLegendCustom;
    }

    public com.kore.ai.widgetsdk.charts.components.Legend.LegendHorizontalAlignment getHorizontalAlignment() {
        return this.mHorizontalAlignment;
    }

    public void setHorizontalAlignment(com.kore.ai.widgetsdk.charts.components.Legend.LegendHorizontalAlignment value) {
        this.mHorizontalAlignment = value;
    }

    public com.kore.ai.widgetsdk.charts.components.Legend.LegendVerticalAlignment getVerticalAlignment() {
        return this.mVerticalAlignment;
    }

    public void setVerticalAlignment(com.kore.ai.widgetsdk.charts.components.Legend.LegendVerticalAlignment value) {
        this.mVerticalAlignment = value;
    }

    public com.kore.ai.widgetsdk.charts.components.Legend.LegendOrientation getOrientation() {
        return this.mOrientation;
    }

    public void setOrientation(com.kore.ai.widgetsdk.charts.components.Legend.LegendOrientation value) {
        this.mOrientation = value;
    }

    public boolean isDrawInsideEnabled() {
        return this.mDrawInside;
    }

    public void setDrawInside(boolean value) {
        this.mDrawInside = value;
    }

    public com.kore.ai.widgetsdk.charts.components.Legend.LegendDirection getDirection() {
        return this.mDirection;
    }

    public void setDirection(com.kore.ai.widgetsdk.charts.components.Legend.LegendDirection pos) {
        this.mDirection = pos;
    }

    public com.kore.ai.widgetsdk.charts.components.Legend.LegendForm getForm() {
        return this.mShape;
    }

    public void setForm(com.kore.ai.widgetsdk.charts.components.Legend.LegendForm shape) {
        this.mShape = shape;
    }

    public void setFormSize(float size) {
        this.mFormSize = size;
    }

    public float getFormSize() {
        return this.mFormSize;
    }

    public void setFormLineWidth(float size) {
        this.mFormLineWidth = size;
    }

    public float getFormLineWidth() {
        return this.mFormLineWidth;
    }

    public void setFormLineDashEffect(DashPathEffect dashPathEffect) {
        this.mFormLineDashEffect = dashPathEffect;
    }

    public DashPathEffect getFormLineDashEffect() {
        return this.mFormLineDashEffect;
    }

    public float getXEntrySpace() {
        return this.mXEntrySpace;
    }

    public void setXEntrySpace(float space) {
        this.mXEntrySpace = space;
    }

    public float getYEntrySpace() {
        return this.mYEntrySpace;
    }

    public void setYEntrySpace(float space) {
        this.mYEntrySpace = space;
    }

    public float getFormToTextSpace() {
        return this.mFormToTextSpace;
    }

    public void setFormToTextSpace(float space) {
        this.mFormToTextSpace = space;
    }

    public float getStackSpace() {
        return this.mStackSpace;
    }

    public void setStackSpace(float space) {
        this.mStackSpace = space;
    }

    public void setWordWrapEnabled(boolean enabled) {
        this.mWordWrapEnabled = enabled;
    }

    public boolean isWordWrapEnabled() {
        return this.mWordWrapEnabled;
    }

    public float getMaxSizePercent() {
        return this.mMaxSizePercent;
    }

    public void setMaxSizePercent(float maxSize) {
        this.mMaxSizePercent = maxSize;
    }

    public List<FSize> getCalculatedLabelSizes() {
        return this.mCalculatedLabelSizes;
    }

    public List<Boolean> getCalculatedLabelBreakPoints() {
        return this.mCalculatedLabelBreakPoints;
    }

    public List<FSize> getCalculatedLineSizes() {
        return this.mCalculatedLineSizes;
    }

    public void calculateDimensions(Paint labelpaint, ViewPortHandler viewPortHandler) {
        float defaultFormSize = Utils.convertDpToPixel(this.mFormSize);
        float stackSpace = Utils.convertDpToPixel(this.mStackSpace);
        float formToTextSpace = Utils.convertDpToPixel(this.mFormToTextSpace);
        float xEntrySpace = Utils.convertDpToPixel(this.mXEntrySpace);
        float yEntrySpace = Utils.convertDpToPixel(this.mYEntrySpace);
        boolean wordWrapEnabled = this.mWordWrapEnabled;
        LegendEntry[] entries = this.mEntries;
        int entryCount = entries.length;
        this.mTextWidthMax = this.getMaximumEntryWidth(labelpaint);
        this.mTextHeightMax = this.getMaximumEntryHeight(labelpaint);
        float labelLineHeight;
        float maxHeight;
        float width;
        float maxLineWidth;
        switch(this.mOrientation) {
            case VERTICAL:
                labelLineHeight = 0.0F;
                maxHeight = 0.0F;
                width = 0.0F;
                maxLineWidth = Utils.getLineHeight(labelpaint);
                boolean wasStacked = false;

                for(int i = 0; i < entryCount; ++i) {
                    LegendEntry e = entries[i];
                    boolean drawingForm = e.form != com.kore.ai.widgetsdk.charts.components.Legend.LegendForm.NONE;
                    float formSize = Float.isNaN(e.formSize) ? defaultFormSize : Utils.convertDpToPixel(e.formSize);
                    String label = e.label;
                    if (!wasStacked) {
                        width = 0.0F;
                    }

                    if (drawingForm) {
                        if (wasStacked) {
                            width += stackSpace;
                        }

                        width += formSize;
                    }

                    if (label == null) {
                        wasStacked = true;
                        width += formSize;
                        if (i < entryCount - 1) {
                            width += stackSpace;
                        }
                    } else {
                        if (drawingForm && !wasStacked) {
                            width += formToTextSpace;
                        } else if (wasStacked) {
                            labelLineHeight = Math.max(labelLineHeight, width);
                            maxHeight += maxLineWidth + yEntrySpace;
                            width = 0.0F;
                            wasStacked = false;
                        }

                        width += (float)Utils.calcTextWidth(labelpaint, label);
                        if (i < entryCount - 1) {
                            maxHeight += maxLineWidth + yEntrySpace;
                        }
                    }

                    labelLineHeight = Math.max(labelLineHeight, width);
                }

                this.mNeededWidth = labelLineHeight;
                this.mNeededHeight = maxHeight;
                break;
            case HORIZONTAL:
                labelLineHeight = Utils.getLineHeight(labelpaint);
                maxHeight = Utils.getLineSpacing(labelpaint) + yEntrySpace;
                width = viewPortHandler.contentWidth() * this.mMaxSizePercent;
                maxLineWidth = 0.0F;
                float currentLineWidth = 0.0F;
                float requiredWidth = 0.0F;
                int stackedStartIndex = -1;
                this.mCalculatedLabelBreakPoints.clear();
                this.mCalculatedLabelSizes.clear();
                this.mCalculatedLineSizes.clear();

                for(int i = 0; i < entryCount; ++i) {
                    LegendEntry e = entries[i];
                    boolean drawingForm = e.form != com.kore.ai.widgetsdk.charts.components.Legend.LegendForm.NONE;
                    float formSize = Float.isNaN(e.formSize) ? defaultFormSize : Utils.convertDpToPixel(e.formSize);
                    String label = e.label;
                    this.mCalculatedLabelBreakPoints.add(false);
                    if (stackedStartIndex == -1) {
                        requiredWidth = 0.0F;
                    } else {
                        requiredWidth += stackSpace;
                    }

                    if (label != null) {
                        this.mCalculatedLabelSizes.add(Utils.calcTextSize(labelpaint, label));
                        requiredWidth += drawingForm ? formToTextSpace + formSize : 0.0F;
                        requiredWidth += this.mCalculatedLabelSizes.get(i).width;
                    } else {
                        this.mCalculatedLabelSizes.add(FSize.getInstance(0.0F, 0.0F));
                        requiredWidth += drawingForm ? formSize : 0.0F;
                        if (stackedStartIndex == -1) {
                            stackedStartIndex = i;
                        }
                    }

                    if (label != null || i == entryCount - 1) {
                        float requiredSpacing = currentLineWidth == 0.0F ? 0.0F : xEntrySpace;
                        if (wordWrapEnabled && currentLineWidth != 0.0F && !(width - currentLineWidth >= requiredSpacing + requiredWidth)) {
                            this.mCalculatedLineSizes.add(FSize.getInstance(currentLineWidth, labelLineHeight));
                            maxLineWidth = Math.max(maxLineWidth, currentLineWidth);
                            this.mCalculatedLabelBreakPoints.set(stackedStartIndex > -1 ? stackedStartIndex : i, true);
                            currentLineWidth = requiredWidth;
                        } else {
                            currentLineWidth += requiredSpacing + requiredWidth;
                        }

                        if (i == entryCount - 1) {
                            this.mCalculatedLineSizes.add(FSize.getInstance(currentLineWidth, labelLineHeight));
                            maxLineWidth = Math.max(maxLineWidth, currentLineWidth);
                        }
                    }

                    stackedStartIndex = label != null ? -1 : stackedStartIndex;
                }

                this.mNeededWidth = maxLineWidth;
                this.mNeededHeight = labelLineHeight * (float)this.mCalculatedLineSizes.size() + maxHeight * (float)(this.mCalculatedLineSizes.size() == 0 ? 0 : this.mCalculatedLineSizes.size() - 1);
        }

        this.mNeededHeight += this.mYOffset;
        this.mNeededWidth += this.mXOffset;
    }

    public enum LegendDirection {
        LEFT_TO_RIGHT,
        RIGHT_TO_LEFT;

        LegendDirection() {
        }
    }

    public enum LegendOrientation {
        HORIZONTAL,
        VERTICAL;

        LegendOrientation() {
        }
    }

    public enum LegendVerticalAlignment {
        TOP,
        CENTER,
        BOTTOM;

        LegendVerticalAlignment() {
        }
    }

    public enum LegendHorizontalAlignment {
        LEFT,
        CENTER,
        RIGHT;

        LegendHorizontalAlignment() {
        }
    }

    public enum LegendForm {
        NONE,
        EMPTY,
        DEFAULT,
        SQUARE,
        CIRCLE,
        LINE;

        LegendForm() {
        }
    }
}
