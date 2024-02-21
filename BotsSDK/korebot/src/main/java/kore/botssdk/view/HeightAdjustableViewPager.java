package kore.botssdk.view;

import static kore.botssdk.view.viewUtils.DimensionUtil.dp1;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.viewpager.widget.ViewPager;

/**
 * Created by Shiva Krishna on 2/9/2018.
 */

public class HeightAdjustableViewPager extends ViewPager {

    private boolean swipeLocked;
    private boolean addExtraHeight;

    public HeightAdjustableViewPager(Context context) {
        super(context);
    }

    public HeightAdjustableViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)  {
        int mode = MeasureSpec.getMode(heightMeasureSpec);
        // Unspecified means that the ViewPager is in a ScrollView WRAP_CONTENT.
        // At Most means that the ViewPager is not in a ScrollView WRAP_CONTENT.
        if (mode == MeasureSpec.UNSPECIFIED || mode == MeasureSpec.AT_MOST) {
            // super has to be called in the beginning so the child views can be initialized.
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            int height = 0;
            for (int i = 0; i < getChildCount(); i++) {
                View child = getChildAt(i);
                child.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
                int childMeasuredHeight = child.getMeasuredHeight();
                if (childMeasuredHeight > height) {
                    height = childMeasuredHeight;
                }
            }
            if (height != 0 && addExtraHeight) {
                height = height + (int) (25 * dp1);
            }
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(height+getPaddingTop(), MeasureSpec.EXACTLY);
    /*        for(int i = 0; i < getChildCount(); i++) {
                View child = getChildAt(i);
                child.getLayoutParams().height = height;
                child.requestLayout();
            }*/
        }


        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return !swipeLocked && super.onTouchEvent(event);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return !swipeLocked && super.onInterceptTouchEvent(event);
    }

    @Override
    public boolean canScrollHorizontally(int direction) {
        return !swipeLocked && super.canScrollHorizontally(direction);
    }

    public void setSwipeLocked(boolean swipeLocked) {
        this.swipeLocked = swipeLocked;
    }

    public boolean isAddExtraHeight() {
        return addExtraHeight;
    }

    public void setAddExtraHeight(boolean addExtraHeight) {
        this.addExtraHeight = addExtraHeight;
    }
}