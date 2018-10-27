package kore.botssdk.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import java.util.Arrays;
import java.util.Collections;

import kore.botssdk.view.viewUtils.LayoutUtils;
import kore.botssdk.view.viewUtils.MeasureUtils;

/**
 * Created by Pradeep Mahato on 01-Jun-16.
 * Copyright (c) 2014 Kore Inc. All rights reserved.
 */
public class KaSendBubbleLayout extends KaBaseBubbleLayout {

    public KaSendBubbleLayout(Context context) {
        super(context);
        init();
    }

    public KaSendBubbleLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public KaSendBubbleLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public KaSendBubbleLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }
    public int getLinkTextColor() {
        return RIGHT_BUBBLE_LINK_COLOR;
    }

    private void init() {
        textMediaLayoutGravity = TextMediaLayout.GRAVITY_RIGHT;
        super.setLeftSide(false);
    }

    @Override
    protected void initializeBubbleBorderPass1() {
        BUBBLE_CONTENT_TOP_MARGIN = (int) (8 * dp1);
        BUBBLE_CONTENT_BOTTOM_MARGIN = (int)(8 * dp1);
        BUBBLE_LEFT_PROFILE_PIC = 0;
        if (isContinuousMessage && isSeparatedClosely) {
            BUBBLE_TOP_BORDER = (int) (1 * dp1);
        } else {
            BUBBLE_TOP_BORDER = (int) (8 * dp1);
        }
        BUBBLE_LEFT_BORDER = 0;
        BUBBLE_RIGHT_BORDER = (int) (2 * dp6 + dp6 + 2 * dp1);
        BUBBLE_DOWN_BORDER = 0;
        BUBBLE_LEFT_ARROW_WIDTH = 0;
        BUBBLE_RIGHT_ARROW_WIDTH = (int) dp6;

    }

    @Override
    void initializeBubbleBorderPass2() {
        BUBBLE_CONTENT_RIGHT_BORDER = 0; //this is always 0...
        BUBBLE_CONTENT_LEFT_BORDER = bubbleTextMediaLayout.getLeft() - BUBBLE_CONTENT_LEFT_MARGIN;

        invalidate();
    }

    @Override
    protected void initializeBubbleContentDimen() {
        super.initializeBubbleContentDimen();

       // headerLayoutDimen[0] = BUBBLE_LEFT_BORDER + BUBBLE_LEFT_ARROW_WIDTH + headerLayout.getMeasuredWidth() + BUBBLE_RIGHT_ARROW_WIDTH + BUBBLE_RIGHT_BORDER;
        maxContentDimen[0] = BUBBLE_LEFT_BORDER + BUBBLE_LEFT_ARROW_WIDTH + BUBBLE_CONTENT_LEFT_MARGIN + Collections.max(Arrays.asList(textMediaDimen[0],timeStampsTextView.getMeasuredWidth()))+ BUBBLE_CONTENT_RIGHT_MARGIN + BUBBLE_RIGHT_ARROW_WIDTH + BUBBLE_RIGHT_BORDER;

        maxBubbleDimen[0] = BUBBLE_LEFT_PROFILE_PIC + maxContentDimen[0];

        maxBubbleDimen[1] = BUBBLE_SEPARATION_DISTANCE + BUBBLE_TOP_BORDER + BUBBLE_CONTENT_TOP_MARGIN +
                textMediaDimen[1] + BUBBLE_CONTENT_BOTTOM_MARGIN + BUBBLE_DOWN_BORDER+timeStampsTextView.getMeasuredHeight();
        maxContentDimen[1] = BUBBLE_CONTENT_TOP_MARGIN  + textMediaDimen[1] + BUBBLE_CONTENT_BOTTOM_MARGIN+timeStampsTextView.getMeasuredHeight();
    }

/*
    @Override
    protected void populateHeaderLayout(int position, BaseBotMessage baseBotMessage) {
        try {
            headerLayout.populateHeader(DateUtils.getTimeStamp(baseBotMessage.getCreatedOn(), false));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
*/

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int parentWidth = View.MeasureSpec.getSize(widthMeasureSpec);
        int maxAllowedWidth = parentWidth;
        int wrapSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);

        int totalHeight = getPaddingTop();
        int totalWidth = getPaddingLeft();

        int childWidthSpec;
        int childHeightSpec;
        int contentWidth = 0;

        /*
         * For TextMedia Layout
         */
        childWidthSpec = View.MeasureSpec.makeMeasureSpec(maxAllowedWidth, View.MeasureSpec.AT_MOST);
        MeasureUtils.measure(bubbleTextMediaLayout, childWidthSpec, wrapSpec);
        contentWidth = bubbleTextMediaLayout.getMeasuredWidth();
        MeasureUtils.measure(timeStampsTextView, wrapSpec, wrapSpec);


        initializeBubbleDimensionalParametersPhase1(); //Initiliaze params

        int parentHeightSpec = View.MeasureSpec.makeMeasureSpec(maxBubbleDimen[1], View.MeasureSpec.EXACTLY);
        int parentWidthSpec = View.MeasureSpec.makeMeasureSpec(maxBubbleDimen[0], View.MeasureSpec.EXACTLY);

        super.onMeasure(parentWidthSpec, parentHeightSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        //Consider the paddings and manupulate them it first..
        l += getPaddingLeft();
        t += getPaddingTop();
        r -= getPaddingRight();
        b -= getPaddingBottom();

        int bubbleTextMediaLayouMarginLeft = BUBBLE_CONTENT_LEFT_MARGIN;
        int bubbleTextMediaLayouMarginTop = BUBBLE_CONTENT_TOP_MARGIN;
        int bubbleTextMediaLayouMarginRight = BUBBLE_CONTENT_RIGHT_MARGIN + BUBBLE_RIGHT_ARROW_WIDTH + BUBBLE_RIGHT_BORDER;
        int bubbleTextMediaLayouMarginBottom = BUBBLE_CONTENT_BOTTOM_MARGIN + BUBBLE_DOWN_BORDER;

        int top = getPaddingTop()  + BUBBLE_SEPARATION_DISTANCE, left;
        int containerWidth = getMeasuredWidth();


        /*
         * For TextMedia Layout
         */
        left = containerWidth - (bubbleTextMediaLayouMarginRight + bubbleTextMediaLayout.getMeasuredWidth());
        top += bubbleTextMediaLayouMarginTop+BUBBLE_TOP_BORDER;

        LayoutUtils.layoutChild(bubbleTextMediaLayout, left, top);


        left = containerWidth - (timeStampsTextView.getMeasuredWidth()+bubbleTextMediaLayouMarginRight);
        top = bubbleTextMediaLayout.getBottom()+ BUBBLE_CONTENT_BOTTOM_MARGIN;
        LayoutUtils.layoutChild(timeStampsTextView, left, top);
        initializeBubbleDimensionalParametersPhase2(); //Initialize paramters, now that its layed out...
    }
}
