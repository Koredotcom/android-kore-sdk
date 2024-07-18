package kore.botssdk.view;

import static kore.botssdk.net.SDKConfiguration.BubbleColors.BubbleUI;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;

import java.util.Arrays;
import java.util.Collections;

import kore.botssdk.utils.BubbleConstants;
import kore.botssdk.view.viewUtils.LayoutUtils;
import kore.botssdk.view.viewUtils.MeasureUtils;

/**
 * Copyright (c) 2014 Kore Inc. All rights reserved.
 */
@SuppressLint("UnKnownNullness")
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
        textMediaLayoutGravity = BubbleConstants.GRAVITY_RIGHT;
        super.setLeftSide(false);
    }

    @Override
    protected void initializeBubbleBorderPass1() {
        BUBBLE_CONTENT_TOP_MARGIN = 0;
        BUBBLE_CONTENT_BOTTOM_MARGIN = BubbleUI ? (int)(8 * dp1) : (int)(21 * dp1);
        BUBBLE_LEFT_PROFILE_PIC = 0;
        if (isContinuousMessage) {
            BUBBLE_TOP_BORDER = 0;
        } else {
            BUBBLE_TOP_BORDER = (int) (7 * dp1);
        }
        BUBBLE_LEFT_BORDER = 0;
        BUBBLE_RIGHT_BORDER = BubbleUI ? (int) (2 * dp6 + dp6 + 2 * dp1) : 0;
        BUBBLE_DOWN_BORDER = 0;
        BUBBLE_LEFT_ARROW_WIDTH = 0;
        BUBBLE_RIGHT_ARROW_WIDTH = BubbleUI ?(int) dp6 : 0;

    }

    @Override
    void initializeBubbleBorderPass2() {
        BUBBLE_CONTENT_RIGHT_BORDER = 0; //this is always 0...
        BUBBLE_CONTENT_LEFT_BORDER = bubbleTextMediaLayout.getLeft() - BUBBLE_CONTENT_LEFT_MARGIN;
    }

    @Override
    protected void initializeBubbleContentDimen() {
        super.initializeBubbleContentDimen();

        maxContentDimen[0] = BUBBLE_LEFT_BORDER + BUBBLE_LEFT_ARROW_WIDTH + BUBBLE_CONTENT_LEFT_MARGIN + Collections.max(Arrays.asList(textMediaDimen[0],timeStampsTextView.getMeasuredWidth()))+ BUBBLE_CONTENT_RIGHT_MARGIN + BUBBLE_RIGHT_ARROW_WIDTH + BUBBLE_RIGHT_BORDER;

        maxBubbleDimen[0] = BUBBLE_LEFT_PROFILE_PIC + maxContentDimen[0];

        maxBubbleDimen[1] = BUBBLE_SEPARATION_DISTANCE + BUBBLE_TOP_BORDER + BUBBLE_CONTENT_TOP_MARGIN +
                textMediaDimen[1] + BUBBLE_CONTENT_BOTTOM_MARGIN + BUBBLE_DOWN_BORDER+timeStampsTextView.getMeasuredHeight();
        maxContentDimen[1] = BUBBLE_CONTENT_TOP_MARGIN  + textMediaDimen[1] + BUBBLE_CONTENT_BOTTOM_MARGIN+timeStampsTextView.getMeasuredHeight();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int maxAllowedWidth = MeasureSpec.getSize(widthMeasureSpec);
        int wrapSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);


        int childWidthSpec;
        /*
         * For TextMedia Layout
         */
        childWidthSpec = MeasureSpec.makeMeasureSpec(maxAllowedWidth, MeasureSpec.AT_MOST);
        MeasureUtils.measure(bubbleTextMediaLayout, childWidthSpec, wrapSpec);
        MeasureUtils.measure(timeStampsTextView, wrapSpec, wrapSpec);


        initializeBubbleDimensionalParametersPhase1(); //Initiliaze params

        int parentHeightSpec = MeasureSpec.makeMeasureSpec(maxBubbleDimen[1], MeasureSpec.EXACTLY);
        int parentWidthSpec = MeasureSpec.makeMeasureSpec(maxBubbleDimen[0], MeasureSpec.EXACTLY);

        super.onMeasure(parentWidthSpec, parentHeightSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {


        int bubbleTextMediaLayouMarginTop = BUBBLE_CONTENT_TOP_MARGIN;
        int bubbleTextMediaLayouMarginRight = BUBBLE_CONTENT_RIGHT_MARGIN + BUBBLE_RIGHT_ARROW_WIDTH + BUBBLE_RIGHT_BORDER;


        int top = getPaddingTop()  + BUBBLE_SEPARATION_DISTANCE, left;
        int containerWidth = getMeasuredWidth();

        LayoutUtils.layoutChild(timeStampsTextView, containerWidth - (int)(timeStampsTextView.getMeasuredWidth() + bubbleTextMediaLayouMarginRight + dp10), top+bubbleTextMediaLayouMarginTop);

        /*
         * For TextMedia Layout
         */

        left = (int)(containerWidth - (14 * dp1 + bubbleTextMediaLayout.getMeasuredWidth()));
        top += bubbleTextMediaLayouMarginTop+timeStampsTextView.getMeasuredHeight();

        LayoutUtils.layoutChild(bubbleTextMediaLayout, left, top);

        initializeBubbleDimensionalParametersPhase2(); //Initialize paramters, now that its layed out...
    }
}
