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
public class SendBubbleContainer extends BaseBubbleContainer {

    SendBubbleLayout sendBubbleLayout;
    View headerLayout;

    public SendBubbleContainer(Context context) {
        super(context);
    }

    public SendBubbleContainer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SendBubbleContainer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public SendBubbleContainer(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        //Find views...
        sendBubbleLayout = (SendBubbleLayout) findViewById(R.id.sendBubbleLayout);
        headerLayout = findViewById(R.id.headerLayout);

        dp1 = (int) AppControl.getInstance().getDimensionUtil().dp1;

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
         * For Send Bubble Layout
         */
        childWidthSpec = MeasureSpec.makeMeasureSpec(BUBBLE_CONTENT_LAYOUT_WIDTH, MeasureSpec.AT_MOST);
        MeasureUtils.measure(sendBubbleLayout, childWidthSpec, wrapSpec);

        childWidthSpec = MeasureSpec.makeMeasureSpec(BUBBLE_CONTENT_LAYOUT_WIDTH, MeasureSpec.AT_MOST);
        MeasureUtils.measure(headerLayout, childWidthSpec, wrapSpec);

        totalHeight += sendBubbleLayout.getMeasuredHeight();
        totalHeight += headerLayout.getMeasuredHeight();

        int parentHeightSpec = MeasureSpec.makeMeasureSpec(totalHeight, MeasureSpec.EXACTLY);

        super.onMeasure(widthMeasureSpec, parentHeightSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        int top = getPaddingTop();
        int left = getPaddingLeft();
        int parentWidth = getMeasuredWidth();

        /*
         * For Received Bubble Layout
         */
        top += dp1;
        int viewLeft = (parentWidth - getPaddingRight()) - sendBubbleLayout.getMeasuredWidth();
        LayoutUtils.layoutChild(sendBubbleLayout, viewLeft, top);
    }
}
