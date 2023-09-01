package kore.botssdk.view;

import android.content.Context;
import android.util.AttributeSet;

import kore.botssdk.R;
import kore.botssdk.view.viewUtils.DimensionUtil;
import kore.botssdk.view.viewUtils.LayoutUtils;
import kore.botssdk.view.viewUtils.MeasureUtils;

/**
 * Created by Pradeep Mahato on 01-Jun-16.
 * Copyright (c) 2014 Kore Inc. All rights reserved.
 */
public class ReceivedBubbleContainer extends BaseBubbleContainer {

    ReceivedBubbleLayout receivedBubbleLayout;

    public ReceivedBubbleContainer(Context context) {
        super(context);
    }

    public ReceivedBubbleContainer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ReceivedBubbleContainer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ReceivedBubbleContainer(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        receivedBubbleLayout = findViewById(R.id.receivedBubbleLayout);

        dp1 = (int) DimensionUtil.dp1;
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int parentWidth = MeasureSpec.getSize(widthMeasureSpec);
        int wrapSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);

        int totalHeight = getPaddingTop();

        int childWidthSpec;
        childWidthSpec = MeasureSpec.makeMeasureSpec(parentWidth, MeasureSpec.EXACTLY);

        totalHeight += dp1;

        /*
         * For Received Bubble Layout
         */
        childWidthSpec = MeasureSpec.makeMeasureSpec(BUBBLE_CONTENT_LAYOUT_WIDTH, MeasureSpec.AT_MOST);
        MeasureUtils.measure(receivedBubbleLayout, childWidthSpec, wrapSpec);
        totalHeight += receivedBubbleLayout.getMeasuredHeight();

        int parentHeightSpec = MeasureSpec.makeMeasureSpec(totalHeight, MeasureSpec.EXACTLY);

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
