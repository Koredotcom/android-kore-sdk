package kore.botssdk.charts.data;

import java.util.ArrayList;
import java.util.List;

import kore.botssdk.charts.charts.ScatterChart;
import kore.botssdk.charts.interfaces.datasets.IScatterDataSet;
import kore.botssdk.charts.renderer.scatter.ChevronDownShapeRenderer;
import kore.botssdk.charts.renderer.scatter.ChevronUpShapeRenderer;
import kore.botssdk.charts.renderer.scatter.CircleShapeRenderer;
import kore.botssdk.charts.renderer.scatter.CrossShapeRenderer;
import kore.botssdk.charts.renderer.scatter.IShapeRenderer;
import kore.botssdk.charts.renderer.scatter.SquareShapeRenderer;
import kore.botssdk.charts.renderer.scatter.TriangleShapeRenderer;
import kore.botssdk.charts.renderer.scatter.XShapeRenderer;

public class ScatterDataSet extends LineScatterCandleRadarDataSet<kore.botssdk.charts.data.Entry> implements IScatterDataSet {
    private float mShapeSize = 15.0F;
    protected IShapeRenderer mShapeRenderer = new SquareShapeRenderer();
    private float mScatterShapeHoleRadius = 0.0F;
    private int mScatterShapeHoleColor = 1122867;

    public ScatterDataSet(List<kore.botssdk.charts.data.Entry> yVals, String label) {
        super(yVals, label);
    }

    public DataSet<kore.botssdk.charts.data.Entry> copy() {
        List<kore.botssdk.charts.data.Entry> entries = new ArrayList();

        for(int i = 0; i < this.mValues.size(); ++i) {
            entries.add(this.mValues.get(i).copy());
        }

        kore.botssdk.charts.data.ScatterDataSet copied = new kore.botssdk.charts.data.ScatterDataSet(entries, this.getLabel());
        this.copy(copied);
        return copied;
    }

    protected void copy(ScatterDataSet scatterDataSet) {
        super.copy(scatterDataSet);
        scatterDataSet.mShapeSize = this.mShapeSize;
        scatterDataSet.mShapeRenderer = this.mShapeRenderer;
        scatterDataSet.mScatterShapeHoleRadius = this.mScatterShapeHoleRadius;
        scatterDataSet.mScatterShapeHoleColor = this.mScatterShapeHoleColor;
    }

    public void setScatterShapeSize(float size) {
        this.mShapeSize = size;
    }

    public float getScatterShapeSize() {
        return this.mShapeSize;
    }

    public void setScatterShape(ScatterChart.ScatterShape shape) {
        this.mShapeRenderer = getRendererForShape(shape);
    }

    public void setShapeRenderer(IShapeRenderer shapeRenderer) {
        this.mShapeRenderer = shapeRenderer;
    }

    public IShapeRenderer getShapeRenderer() {
        return this.mShapeRenderer;
    }

    public void setScatterShapeHoleRadius(float holeRadius) {
        this.mScatterShapeHoleRadius = holeRadius;
    }

    public float getScatterShapeHoleRadius() {
        return this.mScatterShapeHoleRadius;
    }

    public void setScatterShapeHoleColor(int holeColor) {
        this.mScatterShapeHoleColor = holeColor;
    }

    public int getScatterShapeHoleColor() {
        return this.mScatterShapeHoleColor;
    }

    public static IShapeRenderer getRendererForShape(ScatterChart.ScatterShape shape) {
        switch(shape) {
            case SQUARE:
                return new SquareShapeRenderer();
            case CIRCLE:
                return new CircleShapeRenderer();
            case TRIANGLE:
                return new TriangleShapeRenderer();
            case CROSS:
                return new CrossShapeRenderer();
            case X:
                return new XShapeRenderer();
            case CHEVRON_UP:
                return new ChevronUpShapeRenderer();
            case CHEVRON_DOWN:
                return new ChevronDownShapeRenderer();
            default:
                return null;
        }
    }
}
