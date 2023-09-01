package kore.botssdk.charts.data;

import java.util.ArrayList;
import java.util.List;

import kore.botssdk.charts.interfaces.datasets.IBubbleDataSet;
import kore.botssdk.charts.utils.Utils;

public class BubbleDataSet extends BarLineScatterCandleBubbleDataSet<BubbleEntry> implements IBubbleDataSet {
    protected float mMaxSize;
    protected boolean mNormalizeSize = true;
    private float mHighlightCircleWidth = 2.5F;

    public BubbleDataSet(List<BubbleEntry> yVals, String label) {
        super(yVals, label);
    }

    public void setHighlightCircleWidth(float width) {
        this.mHighlightCircleWidth = Utils.convertDpToPixel(width);
    }

    public float getHighlightCircleWidth() {
        return this.mHighlightCircleWidth;
    }

    protected void calcMinMax(BubbleEntry e) {
        super.calcMinMax(e);
        float size = e.getSize();
        if (size > this.mMaxSize) {
            this.mMaxSize = size;
        }

    }

    public DataSet<BubbleEntry> copy() {
        List<BubbleEntry> entries = new ArrayList();

        for(int i = 0; i < this.mValues.size(); ++i) {
            entries.add(this.mValues.get(i).copy());
        }

        kore.botssdk.charts.data.BubbleDataSet copied = new kore.botssdk.charts.data.BubbleDataSet(entries, this.getLabel());
        this.copy(copied);
        return copied;
    }

    protected void copy(BubbleDataSet bubbleDataSet) {
        bubbleDataSet.mHighlightCircleWidth = this.mHighlightCircleWidth;
        bubbleDataSet.mNormalizeSize = this.mNormalizeSize;
    }

    public float getMaxSize() {
        return this.mMaxSize;
    }

    public boolean isNormalizeSizeEnabled() {
        return this.mNormalizeSize;
    }

    public void setNormalizeSizeEnabled(boolean normalizeSize) {
        this.mNormalizeSize = normalizeSize;
    }
}
