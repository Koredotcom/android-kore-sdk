package kore.botssdk.charts.interfaces.datasets;

import kore.botssdk.charts.data.Entry;
import kore.botssdk.charts.renderer.scatter.IShapeRenderer;

public interface IScatterDataSet extends ILineScatterCandleRadarDataSet<Entry> {
    float getScatterShapeSize();

    float getScatterShapeHoleRadius();

    int getScatterShapeHoleColor();

    IShapeRenderer getShapeRenderer();
}

