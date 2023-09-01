package com.kore.ai.widgetsdk.charts.renderer;

import com.kore.ai.widgetsdk.charts.animation.ChartAnimator;
import com.kore.ai.widgetsdk.charts.data.DataSet;
import com.kore.ai.widgetsdk.charts.data.Entry;
import com.kore.ai.widgetsdk.charts.interfaces.dataprovider.BarLineScatterCandleBubbleDataProvider;
import com.kore.ai.widgetsdk.charts.interfaces.datasets.IBarLineScatterCandleBubbleDataSet;
import com.kore.ai.widgetsdk.charts.interfaces.datasets.IDataSet;
import com.kore.ai.widgetsdk.charts.utils.ViewPortHandler;

public abstract class BarLineScatterCandleBubbleRenderer extends DataRenderer {
    protected final XBounds mXBounds = new XBounds();

    public BarLineScatterCandleBubbleRenderer(ChartAnimator animator, ViewPortHandler viewPortHandler) {
        super(animator, viewPortHandler);
    }

    protected boolean shouldDrawValues(IDataSet set) {
        return set.isVisible() && (set.isDrawValuesEnabled() || set.isDrawIconsEnabled());
    }

    protected boolean isInBoundsX(Entry e, IBarLineScatterCandleBubbleDataSet set) {
        if (e == null) {
            return false;
        } else {
            float entryIndex = (float)set.getEntryIndex(e);
            return e != null && !(entryIndex >= (float)set.getEntryCount() * this.mAnimator.getPhaseX());
        }
    }

    protected class XBounds {
        public int min;
        public int max;
        public int range;

        protected XBounds() {
        }

        public void set(BarLineScatterCandleBubbleDataProvider chart, IBarLineScatterCandleBubbleDataSet dataSet) {
            float phaseX = Math.max(0.0F, Math.min(1.0F, BarLineScatterCandleBubbleRenderer.this.mAnimator.getPhaseX()));
            float low = chart.getLowestVisibleX();
            float high = chart.getHighestVisibleX();
            Entry entryFrom = dataSet.getEntryForXValue(low, 0.0F / 0.0F, DataSet.Rounding.DOWN);
            Entry entryTo = dataSet.getEntryForXValue(high, 0.0F / 0.0F, DataSet.Rounding.UP);
            this.min = entryFrom == null ? 0 : dataSet.getEntryIndex(entryFrom);
            this.max = entryTo == null ? 0 : dataSet.getEntryIndex(entryTo);
            this.range = (int)((float)(this.max - this.min) * phaseX);
        }
    }
}
