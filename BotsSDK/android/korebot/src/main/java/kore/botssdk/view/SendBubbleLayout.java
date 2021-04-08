package kore.botssdk.view;

import android.content.Context;
import android.util.AttributeSet;

import java.text.ParseException;
import java.util.Arrays;
import java.util.Collections;

import kore.botssdk.R;
import kore.botssdk.models.BaseBotMessage;
import kore.botssdk.utils.BubbleConstants;
import kore.botssdk.utils.DateUtils;
import kore.botssdk.view.viewUtils.LayoutUtils;
import kore.botssdk.view.viewUtils.MeasureUtils;

/**
 * Created by Pradeep Mahato on 01-Jun-16.
 * Copyright (c) 2014 Kore Inc. All rights reserved.
 */
public class SendBubbleLayout extends BaseBubbleLayout {

    public SendBubbleLayout(Context context) {
        super(context);
        init();
    }

    public SendBubbleLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SendBubbleLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public SendBubbleLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }
    public int getLinkTextColor() {
        return getResources().getColor(R.color.right_bubble_text_color);
    }

    private void init() {
        textMediaLayoutGravity = BubbleConstants.GRAVITY_RIGHT;
        super.setLeftSide(false);
    }

    @Override
    protected void initializeBubbleBorderPass1() {

        BUBBLE_LEFT_PROFILE_PIC = 0;
        if (isContinuousMessage && isSeparatedClosely) {
            BUBBLE_TOP_BORDER = (int) (1 * dp1);
        } else {
            BUBBLE_TOP_BORDER = (int) (1 * dp1 + 14 * dp1);
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

        headerLayoutDimen[0] = BUBBLE_LEFT_BORDER + BUBBLE_LEFT_ARROW_WIDTH + headerLayout.getMeasuredWidth() + BUBBLE_RIGHT_ARROW_WIDTH + BUBBLE_RIGHT_BORDER;
        maxContentDimen[0] = BUBBLE_LEFT_BORDER + BUBBLE_LEFT_ARROW_WIDTH + BUBBLE_CONTENT_LEFT_MARGIN + textMediaDimen[0] + BUBBLE_CONTENT_RIGHT_MARGIN + BUBBLE_RIGHT_ARROW_WIDTH + BUBBLE_RIGHT_BORDER;

        maxBubbleDimen[0] = BUBBLE_LEFT_PROFILE_PIC + Collections.max(Arrays.asList(maxContentDimen[0], headerLayoutDimen[0]));

        headerLayoutDimen[1] = headerLayout.getMeasuredHeight();
        maxBubbleDimen[1] = BUBBLE_SEPARATION_DISTANCE + BUBBLE_TOP_BORDER + BUBBLE_CONTENT_TOP_MARGIN +
                headerLayoutDimen[1] + textMediaDimen[1] + BUBBLE_CONTENT_BOTTOM_MARGIN + BUBBLE_DOWN_BORDER;
        maxContentDimen[1] = BUBBLE_CONTENT_TOP_MARGIN + headerLayoutDimen[1] + textMediaDimen[1] + BUBBLE_CONTENT_BOTTOM_MARGIN;
    }

    @Override
    protected void populateHeaderLayout(int position, BaseBotMessage baseBotMessage) {
        try {
            headerLayout.populateHeader(DateUtils.getTimeStamp(baseBotMessage.getCreatedOn(), false));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int parentWidth = MeasureSpec.getSize(widthMeasureSpec);
        int maxAllowedWidth = parentWidth;
        int wrapSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);

        int totalHeight = getPaddingTop();
        int totalWidth = getPaddingLeft();

        int childWidthSpec;
        int childHeightSpec;
        int contentWidth = 0;

        /*
         * For TextMedia Layout
         */
        childWidthSpec = MeasureSpec.makeMeasureSpec(maxAllowedWidth, MeasureSpec.AT_MOST);
        MeasureUtils.measure(bubbleTextMediaLayout, childWidthSpec, wrapSpec);
        contentWidth = bubbleTextMediaLayout.getMeasuredWidth();

        /*
         * For Time Stamp
         */
        MeasureUtils.measure(headerLayout, wrapSpec, wrapSpec);

        initializeBubbleDimensionalParametersPhase1(); //Initiliaze params

        int parentHeightSpec = MeasureSpec.makeMeasureSpec(maxBubbleDimen[1], MeasureSpec.EXACTLY);
        int parentWidthSpec = MeasureSpec.makeMeasureSpec(maxBubbleDimen[0], MeasureSpec.EXACTLY);

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

        int top = getPaddingTop() + BUBBLE_TOP_BORDER + BUBBLE_SEPARATION_DISTANCE, left;
        int containerWidth = getMeasuredWidth();
        /*
         * For Time Stamp
         */
        left = containerWidth - (BUBBLE_RIGHT_ARROW_WIDTH + BUBBLE_RIGHT_BORDER + headerLayout.getMeasuredWidth());
        LayoutUtils.layoutChild(headerLayout, left, top);
        top = headerLayout.getBottom();

        /*
         * For TextMedia Layout
         */
        left = containerWidth - (bubbleTextMediaLayouMarginRight + bubbleTextMediaLayout.getMeasuredWidth());
        top += bubbleTextMediaLayouMarginTop;

        LayoutUtils.layoutChild(bubbleTextMediaLayout, left, top);

        initializeBubbleDimensionalParametersPhase2(); //Initialize paramters, now that its layed out...
    }
}
