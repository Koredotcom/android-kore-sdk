package kore.botssdk.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import kore.botssdk.R;
import kore.botssdk.application.AppControl;
import kore.botssdk.view.viewUtils.LayoutUtils;
import kore.botssdk.view.viewUtils.MeasureUtils;


/**
 * Created by Pradeep Mahato on 01-Jun-16.
 * Copyright (c) 2014 Kore Inc. All rights reserved.
 */
public class KaReceivedBubbleContainer extends KaBaseBubbleContainer {

    KaReceivedBubbleLayout receivedBubbleLayout;

    public KaReceivedBubbleContainer(Context context) {
        super(context);
    }

    public KaReceivedBubbleContainer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public KaReceivedBubbleContainer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public KaReceivedBubbleContainer(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        receivedBubbleLayout = (KaReceivedBubbleLayout) findViewById(R.id.receivedBubbleLayout);

        dp1 = (int) AppControl.getInstance().getDimensionUtil().dp1;
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int parentWidth = View.MeasureSpec.getSize(widthMeasureSpec);
        int wrapSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);

        int totalHeight = getPaddingTop();

        int childWidthSpec;
        childWidthSpec = View.MeasureSpec.makeMeasureSpec(parentWidth, View.MeasureSpec.EXACTLY);

        totalHeight += dp1;

        /*
         * For Received Bubble Layout
         */
        childWidthSpec = View.MeasureSpec.makeMeasureSpec(BUBBLE_CONTENT_LAYOUT_WIDTH, View.MeasureSpec.AT_MOST);
        MeasureUtils.measure(receivedBubbleLayout, childWidthSpec, wrapSpec);
        totalHeight += receivedBubbleLayout.getMeasuredHeight();

        int parentHeightSpec = View.MeasureSpec.makeMeasureSpec(totalHeight, View.MeasureSpec.EXACTLY);

        super.onMeasure(widthMeasureSpec, parentHeightSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        //Consider the paddings and manupulate them it first..
        l += getPaddingLeft();
        t += getPaddingTop();
        r -= getPaddingRight();
        b -= getPaddingBottom();

        int top = getPaddingTop();
        int left = getPaddingLeft();

        /*
         * For Received Bubble Layout
         */
        top = dp1;
        LayoutUtils.layoutChild(receivedBubbleLayout, left, top);
    }
}
