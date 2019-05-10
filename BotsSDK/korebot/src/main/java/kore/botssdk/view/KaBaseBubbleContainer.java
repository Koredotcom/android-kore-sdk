package kore.botssdk.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;

/**
 * Created by Pradeep Mahato on 01-Jun-16.
 * Copyright (c) 2014 Kore Inc. All rights reserved.
 */
public abstract class KaBaseBubbleContainer extends ViewGroup {

    protected int dp1;
    protected int BUBBLE_CONTENT_LAYOUT_WIDTH, BUBBLE_CONTENT_LAYOUT_HEIGHT;
    private boolean viewActive =true;

    public KaBaseBubbleContainer(Context context) {
        super(context);
    }

    public KaBaseBubbleContainer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public KaBaseBubbleContainer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /*public KaBaseBubbleContainer(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }*/

    public void setDimensions(int containerWidth, int containerHeight) {
        BUBBLE_CONTENT_LAYOUT_WIDTH = containerWidth;
        BUBBLE_CONTENT_LAYOUT_HEIGHT = containerHeight;
    }
    public boolean isViewActive() {
        return viewActive;
    }


    public void setViewActive(boolean viewActive) {
        this.viewActive = viewActive;
    }
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return !isViewActive();
    }

}
