package com.kore.ai.widgetsdk.charts.charts;

import android.content.Context;
import android.util.AttributeSet;

import com.kore.ai.widgetsdk.charts.data.ScatterData;
import com.kore.ai.widgetsdk.charts.interfaces.dataprovider.ScatterDataProvider;
import com.kore.ai.widgetsdk.charts.renderer.ScatterChartRenderer;

public class ScatterChart extends BarLineChartBase<ScatterData> implements ScatterDataProvider {
    public ScatterChart(Context context) {
        super(context);
    }

    public ScatterChart(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ScatterChart(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    protected void init() {
        super.init();
        this.mRenderer = new ScatterChartRenderer(this, this.mAnimator, this.mViewPortHandler);
        this.getXAxis().setSpaceMin(0.5F);
        this.getXAxis().setSpaceMax(0.5F);
    }

    public ScatterData getScatterData() {
        return this.mData;
    }

    public enum ScatterShape {
        SQUARE("SQUARE"),
        CIRCLE("CIRCLE"),
        TRIANGLE("TRIANGLE"),
        CROSS("CROSS"),
        X("X"),
        CHEVRON_UP("CHEVRON_UP"),
        CHEVRON_DOWN("CHEVRON_DOWN");

        private final String shapeIdentifier;

        ScatterShape(String shapeIdentifier) {
            this.shapeIdentifier = shapeIdentifier;
        }

        public String toString() {
            return this.shapeIdentifier;
        }

        public static com.kore.ai.widgetsdk.charts.charts.ScatterChart.ScatterShape[] getAllDefaultShapes() {
            return new com.kore.ai.widgetsdk.charts.charts.ScatterChart.ScatterShape[]{SQUARE, CIRCLE, TRIANGLE, CROSS, X, CHEVRON_UP, CHEVRON_DOWN};
        }
    }
}
