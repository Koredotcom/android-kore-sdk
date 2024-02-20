package kore.botssdk.charts.renderer;

import kore.botssdk.charts.utils.ViewPortHandler;

public abstract class Renderer {
    protected final ViewPortHandler mViewPortHandler;

    public Renderer(ViewPortHandler viewPortHandler) {
        this.mViewPortHandler = viewPortHandler;
    }
}
