package kore.botssdk.view;

import android.content.Context;
import android.util.AttributeSet;

import java.util.Arrays;
import java.util.Collections;

import kore.botssdk.view.viewUtils.LayoutUtils;
import kore.botssdk.view.viewUtils.MeasureUtils;

/**
 * Created by Pradeep Mahato on 01-Jun-16.
 */
public class ReceivedBubbleLayout extends BaseBubbleLayout {

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
    void initializeBubbleBorderPass1() {
        BUBBLE_LEFT_PROFILE_PIC = 0;
        BUBBLE_LEFT_PROFILE_PIC_MARGIN_LEFT = (BUBBLE_LEFT_PROFILE_PIC != 0) ? (int) (10 * dp1) : 0;
        BUBBLE_LEFT_PROFILE_PIC_MARGIN_RIGHT = (BUBBLE_LEFT_PROFILE_PIC != 0) ? 0 : 0;
        if (isContinuousMessage && isSeparatedClosely) {
            BUBBLE_TOP_BORDER = (int) dp1;
        } else {
            BUBBLE_TOP_BORDER = (int) (dp14 + dp1);
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

        maxContentDimen[0] = BUBBLE_LEFT_BORDER + BUBBLE_LEFT_PROFILE_PIC_MARGIN_LEFT + BUBBLE_LEFT_PROFILE_PIC + BUBBLE_LEFT_PROFILE_PIC_MARGIN_RIGHT + BUBBLE_LEFT_ARROW_WIDTH + BUBBLE_CONTENT_LEFT_MARGIN + textMediaDimen[0] + BUBBLE_CONTENT_RIGHT_MARGIN + BUBBLE_RIGHT_ARROW_WIDTH + BUBBLE_RIGHT_BORDER;

        maxBubbleDimen[0] = Collections.max(Arrays.asList(maxContentDimen[0]));

        maxBubbleDimen[1] = BUBBLE_SEPARATION_DISTANCE + BUBBLE_TOP_BORDER + BUBBLE_CONTENT_TOP_MARGIN +
                textMediaDimen[1] + BUBBLE_CONTENT_BOTTOM_MARGIN + BUBBLE_DOWN_BORDER;
        maxContentDimen[1] = BUBBLE_CONTENT_TOP_MARGIN + textMediaDimen[1] + BUBBLE_CONTENT_BOTTOM_MARGIN;
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

        left = BUBBLE_LEFT_BORDER + BUBBLE_LEFT_ARROW_WIDTH;
        top = getPaddingTop() + BUBBLE_TOP_BORDER + BUBBLE_SEPARATION_DISTANCE;


        int bubbleTextMediaLayouMarginLeft = BUBBLE_CONTENT_LEFT_MARGIN;
        int bubbleTextMediaLayouMarginTop = BUBBLE_CONTENT_TOP_MARGIN + BUBBLE_FORWARD_LAYOUT_HEIGHT_CONSIDERATION_FOR_PAINT;
        int bubbleTextMediaLayouMarginRight = BUBBLE_CONTENT_RIGHT_MARGIN + BUBBLE_RIGHT_ARROW_WIDTH + BUBBLE_RIGHT_BORDER;
        int bubbleTextMediaLayouMarginBottom = BUBBLE_CONTENT_BOTTOM_MARGIN + BUBBLE_DOWN_BORDER;

        /*
         * For TextMedia Layout
         */
        left += bubbleTextMediaLayouMarginLeft;
        top = bubbleTextMediaLayouMarginTop;
        LayoutUtils.layoutChild(bubbleTextMediaLayout, left, top);

        initializeBubbleDimensionalParametersPhase2(); //Initialize paramters, now that its layed out...

    }
}
