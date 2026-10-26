/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.util.AttributeSet
 *  android.view.View
 *  android.view.View$MeasureSpec
 *  android.view.ViewGroup
 */
package com.korevg.mv.webrtcsdk.session.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

public class PercentFrameLayout
extends ViewGroup {
    private int xPercent = 0;
    private int yPercent = 0;
    private int widthPercent = 100;
    private int heightPercent = 100;

    public PercentFrameLayout(Context context) {
        super(context);
    }

    public PercentFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PercentFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setPosition(int xPercent, int yPercent, int widthPercent, int heightPercent) {
        this.xPercent = xPercent;
        this.yPercent = yPercent;
        this.widthPercent = widthPercent;
        this.heightPercent = heightPercent;
    }

    public boolean shouldDelayChildPressedState() {
        return false;
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = PercentFrameLayout.getDefaultSize((int)Integer.MAX_VALUE, (int)widthMeasureSpec);
        int height = PercentFrameLayout.getDefaultSize((int)Integer.MAX_VALUE, (int)heightMeasureSpec);
        this.setMeasuredDimension(View.MeasureSpec.makeMeasureSpec((int)width, (int)0x40000000), View.MeasureSpec.makeMeasureSpec((int)height, (int)0x40000000));
        int childWidthMeasureSpec = View.MeasureSpec.makeMeasureSpec((int)(width * this.widthPercent / 100), (int)Integer.MIN_VALUE);
        int childHeightMeasureSpec = View.MeasureSpec.makeMeasureSpec((int)(height * this.heightPercent / 100), (int)Integer.MIN_VALUE);
        for (int i = 0; i < this.getChildCount(); ++i) {
            View child = this.getChildAt(i);
            if (child.getVisibility() == 8) continue;
            child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
        }
    }

    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        int width = right - left;
        int height = bottom - top;
        int subWidth = width * this.widthPercent / 100;
        int subHeight = height * this.heightPercent / 100;
        int subLeft = left + width * this.xPercent / 100;
        int subTop = top + height * this.yPercent / 100;
        for (int i = 0; i < this.getChildCount(); ++i) {
            View child = this.getChildAt(i);
            if (child.getVisibility() == 8) continue;
            int childWidth = child.getMeasuredWidth();
            int childHeight = child.getMeasuredHeight();
            int childLeft = subLeft + (subWidth - childWidth) / 2;
            int childTop = subTop + (subHeight - childHeight) / 2;
            child.layout(childLeft, childTop, childLeft + childWidth, childTop + childHeight);
        }
    }
}

