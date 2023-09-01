package kore.botssdk.charts.renderer;

import kore.botssdk.charts.animation.ChartAnimator;
import kore.botssdk.charts.data.DataSet;
import kore.botssdk.charts.data.Entry;
import kore.botssdk.charts.interfaces.dataprovider.BarLineScatterCandleBubbleDataProvider;
import kore.botssdk.charts.interfaces.datasets.IBarLineScatterCandleBubbleDataSet;
import kore.botssdk.charts.interfaces.datasets.IDataSet;
import kore.botssdk.charts.utils.ViewPortHandler;

public abstract class BarLineScatterCandleBubbleRenderer extends DataRenderer {
    protected final BarLineScatterCandleBubbleRenderer.XBounds mXBounds = new BarLineScatterCandleBubbleRenderer.XBounds();

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
