package kore.botssdk.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import kore.botssdk.R;
import kore.botssdk.utils.BubbleConstants;
import kore.botssdk.view.viewUtils.DimensionUtil;
import kore.botssdk.view.viewUtils.LayoutUtils;
import kore.botssdk.view.viewUtils.MeasureUtils;

/**
 * Created by Pradeep Mahato on 08-Jun-16.
 * Copyright (c) 2014 Kore Inc. All rights reserved.headerla
 */
public class HeaderLayout extends ViewGroup {

    private TextView headerTextView;
    private float dp1;

    final int gravity = 0;

    public static final int HEADER_TEXTVIEW_ID = 1980091;

    public HeaderLayout(Context context) {
        super(context);
        init();
    }

    public HeaderLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public HeaderLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    /*public HeaderLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }*/

    private void init() {

        if (!isInEditMode()) {
            dp1 = DimensionUtil.dp1;
        }

        //Add a textView
        headerTextView = new TextView(getContext());
        headerTextView.setLinkTextColor(getResources().getColor(R.color.mentionsAndHashTagColor));
        headerTextView.setTextColor(getContext().getResources().getColor(R.color.footer_color_dark_grey));

        RelativeLayout.LayoutParams txtVwParams = new RelativeLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        headerTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 12);
        headerTextView.setText("");

        headerTextView.setLayoutParams(txtVwParams);
        headerTextView.setSingleLine(false);
        headerTextView.setClickable(false);
        headerTextView.setId(HEADER_TEXTVIEW_ID);
        float dp5 = dp1 * 5;
        headerTextView.setPadding(0, 0, 0, (int) dp5);
        if (gravity == BubbleConstants.GRAVITY_LEFT) {
            headerTextView.setGravity(Gravity.LEFT);
        } else if (gravity == BubbleConstants.GRAVITY_RIGHT) {
            headerTextView.setGravity(Gravity.RIGHT);
        }
        headerTextView.setFocusable(false);
        headerTextView.setClickable(false);
        headerTextView.setLongClickable(false);
        addView(headerTextView);

    }

    public void populateHeader(String timeStamp) {
        populateTimeStamp(timeStamp);
    }

    private void populateTimeStamp(String timeStamp) {
        headerTextView.setText(timeStamp);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int parentWidth = MeasureSpec.getSize(widthMeasureSpec);
        int parentHeight = MeasureSpec.getSize(heightMeasureSpec);

        int childWidthSpec, childHeightSpec;
        int wrapSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        int containerWidth = 0;

        final int count = getChildCount();
        int totalHeight = 0;
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            int childHeight = 0, childWidth = 0;

            if (child.getId() == HEADER_TEXTVIEW_ID) {
                childWidthSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
                MeasureUtils.measure(child, childWidthSpec, wrapSpec);
                childHeight = child.getMeasuredHeight();
            }

            totalHeight += childHeight;
            containerWidth = (containerWidth < child.getMeasuredWidth()) ? child.getMeasuredWidth() : containerWidth;
        }

        containerWidth = containerWidth;

        int parentHeightSpec = MeasureSpec.makeMeasureSpec(totalHeight, MeasureSpec.EXACTLY);
        int parentWidthSpec = MeasureSpec.makeMeasureSpec(containerWidth, MeasureSpec.AT_MOST);
        setMeasuredDimension(parentWidthSpec, parentHeightSpec);

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        final int count = getChildCount();
        int parentWidth = getMeasuredWidth();

        //get the available size of child view
        int childLeft = this.getPaddingLeft();
        int childTop = this.getPaddingTop();

        //walk through each child, and arrange it from left to right
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() != GONE) {
                LayoutUtils.layoutChild(child, childLeft, childTop);
                childTop += child.getMeasuredHeight();
            }
        }
    }
}
