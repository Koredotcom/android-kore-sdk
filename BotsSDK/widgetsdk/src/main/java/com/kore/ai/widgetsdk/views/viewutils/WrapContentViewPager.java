
package com.kore.ai.widgetsdk.views.viewutils;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.viewpager.widget.ViewPager;


public class WrapContentViewPager extends ViewPager {

    public WrapContentViewPager(Context context) {
        super(context);
        initPageChangeListener();
    }

    public WrapContentViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPageChangeListener();
    }

    private void initPageChangeListener() {
        addOnPageChangeListener(new SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                requestLayout();
            }
        });
    }

   /*@Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        View child = getChildAt(getCurrentItem());
        if (child != null) {
            child.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
            int h = child.getMeasuredHeight();
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(h, MeasureSpec.EXACTLY);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }*/
   @Override
   protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
       super.onMeasure(widthMeasureSpec, heightMeasureSpec);

       // find the first child view
       View view = getChildAt(getCurrentItem());
       if (view != null) {
           // measure the first child view with the specified measure spec
           view.measure(widthMeasureSpec, heightMeasureSpec);
       }
       setMeasuredDimension(getMeasuredWidth(), measureHeight(heightMeasureSpec, view));
   }

    /**
     * Determines the height of this view
     *
     * @param measureSpec A measureSpec packed into an int
     * @param view the base view with already measured height
     *
     * @return The height of the view, honoring constraints from measureSpec
     */
    private int measureHeight(int measureSpec, View view) {
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else {
            // set the height from the base view if available
            if (view != null) {
                result = view.getMeasuredHeight();
            }
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize);
            }
        }
        return result;
    }
}
