package com.kore.ai.widgetsdk.charts.renderer;

import com.kore.ai.widgetsdk.charts.utils.ViewPortHandler;

public abstract class Renderer {
    protected final ViewPortHandler mViewPortHandler;

    public Renderer(ViewPortHandler viewPortHandler) {
        this.mViewPortHandler = viewPortHandler;
    }
}
