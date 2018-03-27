package kore.botssdk.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

import static kore.botssdk.view.viewUtils.DimensionUtil.dp1;

/**
 * Created by Shiva Krishna on 2/9/2018.
 */

public class HeightAdjustableViewPager extends ViewPager {

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
            if(getChildCount() > 2 && height !=0) {
                height = height + (int) (45 * dp1);
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
}