package com.kore.ai.widgetsdk.views;

import android.view.View;

import androidx.viewpager.widget.ViewPager;

public class ViewPagerUtils {

    public static View getCurrentView(ViewPager viewPager) {
        final int currentItem = viewPager.getCurrentItem();
        for (int i = 0; i < viewPager.getChildCount(); i++) {
            final View child = viewPager.getChildAt(i);
            final ViewPager.LayoutParams layoutParams = (ViewPager.LayoutParams) child.getLayoutParams();
            if (!layoutParams.isDecor) {
                return child;
            }
        }
        return null;
    }

}