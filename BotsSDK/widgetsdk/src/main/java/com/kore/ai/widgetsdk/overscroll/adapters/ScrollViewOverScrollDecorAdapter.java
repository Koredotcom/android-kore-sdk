package com.kore.ai.widgetsdk.overscroll.adapters;

import android.view.View;
import android.widget.ScrollView;


public class ScrollViewOverScrollDecorAdapter implements IOverScrollDecoratorAdapter {

    protected final ScrollView mView;

    public ScrollViewOverScrollDecorAdapter(ScrollView view) {
        mView = view;
    }

    @Override
    public View getView() {
        return mView;
    }

    @Override
    public boolean isInAbsoluteStart() {
        return !mView.canScrollVertically(-1);
    }

    @Override
    public boolean isInAbsoluteEnd() {
        return !mView.canScrollVertically(1);
    }
}
