package kore.botssdk.charts.data;

import android.graphics.Paint;

import java.util.ArrayList;
import java.util.List;

import kore.botssdk.charts.interfaces.datasets.ICandleDataSet;
import kore.botssdk.charts.utils.Utils;

public class CandleDataSet extends LineScatterCandleRadarDataSet<CandleEntry> implements ICandleDataSet {
    private float mShadowWidth = 3.0F;
    private boolean mShowCandleBar = true;
    private float mBarSpace = 0.1F;
    private boolean mShadowColorSameAsCandle = false;
    protected Paint.Style mIncreasingPaintStyle;
    protected Paint.Style mDecreasingPaintStyle;
    protected int mNeutralColor;
    protected int mIncreasingColor;
    protected int mDecreasingColor;
    protected int mShadowColor;

    public CandleDataSet(List<CandleEntry> yVals, String label) {
        super(yVals, label);
        this.mIncreasingPaintStyle = Paint.Style.STROKE;
        this.mDecreasingPaintStyle = Paint.Style.FILL;
        this.mNeutralColor = 1122868;
        this.mIncreasingColor = 1122868;
        this.mDecreasingColor = 1122868;
        this.mShadowColor = 1122868;
    }

    public DataSet<CandleEntry> copy() {
        List<CandleEntry> entries = new ArrayList();

        for(int i = 0; i < this.mValues.size(); ++i) {
            entries.add(this.mValues.get(i).copy());
        }

        kore.botssdk.charts.data.CandleDataSet copied = new kore.botssdk.charts.data.CandleDataSet(entries, this.getLabel());
        this.copy(copied);
        return copied;
    }

    protected void copy(CandleDataSet candleDataSet) {
        super.copy(candleDataSet);
        candleDataSet.mShadowWidth = this.mShadowWidth;
        candleDataSet.mShowCandleBar = this.mShowCandleBar;
        candleDataSet.mBarSpace = this.mBarSpace;
        candleDataSet.mShadowColorSameAsCandle = this.mShadowColorSameAsCandle;
        candleDataSet.mHighLightColor = this.mHighLightColor;
        candleDataSet.mIncreasingPaintStyle = this.mIncreasingPaintStyle;
        candleDataSet.mDecreasingPaintStyle = this.mDecreasingPaintStyle;
        candleDataSet.mNeutralColor = this.mNeutralColor;
        candleDataSet.mIncreasingColor = this.mIncreasingColor;
        candleDataSet.mDecreasingColor = this.mDecreasingColor;
        candleDataSet.mShadowColor = this.mShadowColor;
    }

    protected void calcMinMax(CandleEntry e) {
        if (e.getLow() < this.mYMin) {
            this.mYMin = e.getLow();
        }

        if (e.getHigh() > this.mYMax) {
            this.mYMax = e.getHigh();
        }

        this.calcMinMaxX(e);
    }

    protected void calcMinMaxY(CandleEntry e) {
        if (e.getHigh() < this.mYMin) {
            this.mYMin = e.getHigh();
        }

        if (e.getHigh() > this.mYMax) {
            this.mYMax = e.getHigh();
        }

        if (e.getLow() < this.mYMin) {
            this.mYMin = e.getLow();
        }

        if (e.getLow() > this.mYMax) {
            this.mYMax = e.getLow();
        }

    }

    public void setBarSpace(float space) {
        if (space < 0.0F) {
            space = 0.0F;
        }

        if (space > 0.45F) {
            space = 0.45F;
        }

        this.mBarSpace = space;
    }

    public float getBarSpace() {
        return this.mBarSpace;
    }

    public void setShadowWidth(float width) {
        this.mShadowWidth = Utils.convertDpToPixel(width);
    }

    public float getShadowWidth() {
        return this.mShadowWidth;
    }

    public void setShowCandleBar(boolean showCandleBar) {
        this.mShowCandleBar = showCandleBar;
    }

    public boolean getShowCandleBar() {
        return this.mShowCandleBar;
    }

    public void setNeutralColor(int color) {
        this.mNeutralColor = color;
    }

    public int getNeutralColor() {
        return this.mNeutralColor;
    }

    public void setIncreasingColor(int color) {
        this.mIncreasingColor = color;
    }

    public int getIncreasingColor() {
        return this.mIncreasingColor;
    }

    public void setDecreasingColor(int color) {
        this.mDecreasingColor = color;
    }

    public int getDecreasingColor() {
        return this.mDecreasingColor;
    }

    public Paint.Style getIncreasingPaintStyle() {
        return this.mIncreasingPaintStyle;
    }

    public void setIncreasingPaintStyle(Paint.Style paintStyle) {
        this.mIncreasingPaintStyle = paintStyle;
    }

    public Paint.Style getDecreasingPaintStyle() {
        return this.mDecreasingPaintStyle;
    }

    public void setDecreasingPaintStyle(Paint.Style decreasingPaintStyle) {
        this.mDecreasingPaintStyle = decreasingPaintStyle;
    }

    public int getShadowColor() {
        return this.mShadowColor;
    }

    public void setShadowColor(int shadowColor) {
        this.mShadowColor = shadowColor;
    }

    public boolean getShadowColorSameAsCandle() {
        return this.mShadowColorSameAsCandle;
    }

    public void setShadowColorSameAsCandle(boolean shadowColorSameAsCandle) {
        this.mShadowColorSameAsCandle = shadowColorSameAsCandle;
    }
}
