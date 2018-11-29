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
public class KaSendBubbleContainer extends KaBaseBubbleContainer {

    KaSendBubbleLayout sendBubbleLayout;
    View headerLayout;
    public KaSendBubbleContainer(Context context) {
        super(context);
    }

    public KaSendBubbleContainer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public KaSendBubbleContainer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public KaSendBubbleContainer(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        //Find views...
        sendBubbleLayout = (KaSendBubbleLayout) findViewById(R.id.sendBubbleLayout);
        headerLayout = findViewById(R.id.headerLayout);
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
         * For Send Bubble Layout
         */
        childWidthSpec = View.MeasureSpec.makeMeasureSpec(BUBBLE_CONTENT_LAYOUT_WIDTH, View.MeasureSpec.AT_MOST);
        MeasureUtils.measure(sendBubbleLayout, childWidthSpec, wrapSpec);

        childWidthSpec = MeasureSpec.makeMeasureSpec(BUBBLE_CONTENT_LAYOUT_WIDTH, MeasureSpec.AT_MOST);
        MeasureUtils.measure(headerLayout, childWidthSpec, wrapSpec);

        totalHeight += sendBubbleLayout.getMeasuredHeight();
        totalHeight += headerLayout.getMeasuredHeight();

        int parentHeightSpec = View.MeasureSpec.makeMeasureSpec(totalHeight, View.MeasureSpec.EXACTLY);

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
