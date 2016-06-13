package kore.botssdk.view;

import android.content.Context;
import android.util.AttributeSet;

import java.text.ParseException;
import java.util.Arrays;
import java.util.Collections;

import kore.botssdk.R;
import kore.botssdk.models.BaseBotMessage;
import kore.botssdk.models.BotResponse;
import kore.botssdk.models.BotResponseMessage;
import kore.botssdk.utils.Contants;
import kore.botssdk.utils.DateUtils;
import kore.botssdk.view.viewUtils.LayoutUtils;
import kore.botssdk.view.viewUtils.MeasureUtils;

/**
 * Created by Pradeep Mahato on 01-Jun-16.
 * Copyright (c) 2014 Kore Inc. All rights reserved.
 */
public class ReceivedBubbleLayout extends BaseBubbleLayout {

    CircularProfileView cpvSenderImage;

    public ReceivedBubbleLayout(Context context) {
        super(context);
        init();
    }

    public ReceivedBubbleLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ReceivedBubbleLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public ReceivedBubbleLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        textMediaLayoutGravity = TextMediaLayout.GRAVITY_LEFT;
        super.setLeftSide(true);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        cpvSenderImage = (CircularProfileView) findViewById(R.id.cpvSenderImage);
    }

    @Override
    void initializeBubbleBorderPass1() {

        BUBBLE_LEFT_PROFILE_PIC = cpvSenderImage.getMeasuredWidth();
        BUBBLE_LEFT_PROFILE_PIC_MARGIN_LEFT = (BUBBLE_LEFT_PROFILE_PIC != 0) ? (int) (10 * dp1) : 0;
        BUBBLE_LEFT_PROFILE_PIC_MARGIN_RIGHT = (BUBBLE_LEFT_PROFILE_PIC != 0) ? 0 : 0;
        if (isContinuousMessage && isSeparatedClosely) {
            BUBBLE_TOP_BORDER = (int) dp1;
        } else {
            BUBBLE_TOP_BORDER = headerLayout.getMeasuredHeight();//int) (dp14 + dp1);
        }
        BUBBLE_LEFT_BORDER = (int) ((!isGroupMessage) ? dp4 : dp1);
        BUBBLE_RIGHT_BORDER = (int) dp1;
        BUBBLE_DOWN_BORDER = (int) dp1;
        BUBBLE_ARROW_WIDTH = (int) ((isGroupMessage) ? dp1 : dp10);
        BUBBLE_LEFT_ARROW_WIDTH = (int) (BUBBLE_ARROW_WIDTH / 2 + 7 * dp1);
        BUBBLE_RIGHT_ARROW_WIDTH = 0;
        BUBBLE_CONTENT_TOP_MARGIN = (int) dp14;
    }


    @Override
    void initializeBubbleBorderPass2() {
        BUBBLE_CONTENT_RIGHT_BORDER = 0; //this is always 0...
        BUBBLE_CONTENT_LEFT_BORDER = bubbleTextMediaLayout.getLeft() - BUBBLE_CONTENT_LEFT_MARGIN;
        BUBBLE_CONTENT_LEFT_BORDER = 0; //this is always 0...

        invalidate();
    }

    @Override
    protected void initializeBubbleContentDimen() {
        super.initializeBubbleContentDimen();

        headerLayoutDimen[0] = BUBBLE_LEFT_BORDER + BUBBLE_LEFT_PROFILE_PIC_MARGIN_LEFT + BUBBLE_LEFT_PROFILE_PIC + BUBBLE_LEFT_PROFILE_PIC_MARGIN_RIGHT + BUBBLE_LEFT_ARROW_WIDTH + headerLayout.getMeasuredWidth();
        maxContentDimen[0] = BUBBLE_LEFT_BORDER + BUBBLE_LEFT_PROFILE_PIC_MARGIN_LEFT + BUBBLE_LEFT_PROFILE_PIC + BUBBLE_LEFT_PROFILE_PIC_MARGIN_RIGHT + BUBBLE_LEFT_ARROW_WIDTH + BUBBLE_CONTENT_LEFT_MARGIN + textMediaDimen[0] + BUBBLE_CONTENT_RIGHT_MARGIN + BUBBLE_RIGHT_ARROW_WIDTH + BUBBLE_RIGHT_BORDER;

        headerLayoutDimen[1] = headerLayout.getMeasuredHeight();
        maxBubbleDimen[0] = Collections.max(Arrays.asList(maxContentDimen[0], headerLayoutDimen[0]));

        maxBubbleDimen[1] = BUBBLE_SEPARATION_DISTANCE + BUBBLE_TOP_BORDER + BUBBLE_CONTENT_TOP_MARGIN +
                headerLayoutDimen[1] + textMediaDimen[1] + BUBBLE_CONTENT_BOTTOM_MARGIN + BUBBLE_DOWN_BORDER;
        maxContentDimen[1] = BUBBLE_CONTENT_TOP_MARGIN + headerLayoutDimen[1] + textMediaDimen[1] + BUBBLE_CONTENT_BOTTOM_MARGIN;
    }

    @Override
    protected void populateHeaderLayout(int position, BaseBotMessage baseBotMessage) {
        try {
            headerLayout.populateHeader(DateUtils.getTimeStamp(baseBotMessage.getCreatedOn(), true));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void cosmeticChanges(BaseBotMessage baseBotMessage, int position) {
        super.cosmeticChanges(baseBotMessage, position);
        cosmetiseForProfilePic(baseBotMessage);
    }

    protected void cosmetiseForProfilePic(BaseBotMessage baseBotMessage) {
        if (isGroupMessage) {
            String icon = ((BotResponse) baseBotMessage).getIcon();
            cpvSenderImage.setVisibility(VISIBLE);
            cpvSenderImage.populateLayout(" ", null, icon, null, -1, 0, true, BUBBLE_LEFT_PROFILE_PIC, BUBBLE_LEFT_PROFILE_PIC);
        } else {
            cpvSenderImage.setVisibility(GONE);
        }
    }

    /**
     * Layout Manipulation Section
     */
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
        contentWidth = bubbleTextMediaLayout.getWidth();

        /*
         * For Sender icon [CPV]
         */
        float cpvSenderImageDimen = dp1 * 35;
        childWidthSpec = MeasureSpec.makeMeasureSpec((int) cpvSenderImageDimen, MeasureSpec.EXACTLY);
        childHeightSpec = MeasureSpec.makeMeasureSpec((int) cpvSenderImageDimen, MeasureSpec.EXACTLY);
        cpvSenderImage.setDimens(cpvSenderImageDimen, cpvSenderImageDimen);
        MeasureUtils.measure(cpvSenderImage, childWidthSpec, childHeightSpec);

        int cpvMarginLeft, cpvMarginRight;
        if (cpvSenderImage.getMeasuredWidth() != 0) {
            cpvMarginLeft = cpvMarginRight = (int) dp4;
        } else {
            cpvMarginLeft = cpvMarginRight = 0;
        }


        /*
         * For Time Stamp
         */
        MeasureUtils.measure(headerLayout, wrapSpec, wrapSpec);

        /*
         * For Simplified Bubble Layout
         */
        int width = BUBBLE_CONTENT_LEFT_MARGIN + bubbleTextMediaLayout.getMeasuredWidth() + BUBBLE_CONTENT_RIGHT_MARGIN;
        int height = BUBBLE_CONTENT_TOP_MARGIN + bubbleTextMediaLayout.getMeasuredHeight() + BUBBLE_CONTENT_BOTTOM_MARGIN;
        childWidthSpec = MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY);
        childHeightSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);

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

        int top = 0, left = 0;

        left = BUBBLE_LEFT_BORDER;
        top = getPaddingTop() + BUBBLE_TOP_BORDER + BUBBLE_SEPARATION_DISTANCE;

        /*
         * For Time Stamp
         */
        left += BUBBLE_LEFT_PROFILE_PIC_MARGIN_LEFT + cpvSenderImage.getMeasuredWidth() + BUBBLE_LEFT_PROFILE_PIC_MARGIN_RIGHT + BUBBLE_LEFT_ARROW_WIDTH;
        LayoutUtils.layoutChild(headerLayout, left, top);
        top = headerLayout.getBottom();

        /*
         * For Sender icon [CPV]
         */
        if (cpvSenderImage.getVisibility() != GONE) {
            left = BUBBLE_LEFT_BORDER + BUBBLE_LEFT_PROFILE_PIC_MARGIN_LEFT;
            LayoutUtils.layoutChild(cpvSenderImage, left, top);
            left = cpvSenderImage.getRight() + BUBBLE_LEFT_PROFILE_PIC_MARGIN_RIGHT + BUBBLE_LEFT_ARROW_WIDTH;
        } else {
            left = BUBBLE_LEFT_BORDER + BUBBLE_LEFT_ARROW_WIDTH;
        }

        top = getPaddingTop() + BUBBLE_TOP_BORDER + BUBBLE_SEPARATION_DISTANCE;


        int bubbleTextMediaLayouMarginLeft = BUBBLE_CONTENT_LEFT_MARGIN;
        int bubbleTextMediaLayouMarginTop = BUBBLE_CONTENT_TOP_MARGIN + BUBBLE_FORWARD_LAYOUT_HEIGHT_CONSIDERATION_FOR_PAINT;
        int bubbleTextMediaLayouMarginRight = BUBBLE_CONTENT_RIGHT_MARGIN + BUBBLE_RIGHT_ARROW_WIDTH + BUBBLE_RIGHT_BORDER;
        int bubbleTextMediaLayouMarginBottom = BUBBLE_CONTENT_BOTTOM_MARGIN + BUBBLE_DOWN_BORDER;

        /*
         * For TextMedia Layout
         */
        left += bubbleTextMediaLayouMarginLeft;
        top = headerLayout.getBottom() + bubbleTextMediaLayouMarginTop;
        LayoutUtils.layoutChild(bubbleTextMediaLayout, left, top);

         /*
         * For re-adjusting CPV
         */
        if (cpvSenderImage.getVisibility() != GONE) {
            int cpvLeft = BUBBLE_LEFT_BORDER + BUBBLE_LEFT_PROFILE_PIC_MARGIN_LEFT;
            int cpvTop = bubbleTextMediaLayout.getBottom() + BUBBLE_CONTENT_BOTTOM_MARGIN - cpvSenderImage.getMeasuredHeight();
            LayoutUtils.layoutChild(cpvSenderImage, cpvLeft, cpvTop);
        }

        initializeBubbleDimensionalParametersPhase2(); //Initialize paramters, now that its layed out...

    }
}
