package com.kore.ui.views

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.viewpager.widget.ViewPager

class HeightAdjustableViewPager : ViewPager {
    private var swipeLocked = false
    private var isAddExtraHeight = false

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context!!, attrs) {
        clipChildren = false
        clipToPadding = false
        // to avoid fade effect at the end of the page
        overScrollMode = 2
        offscreenPageLimit = 3
    }

    override fun onMeasure(widthMeasureSpec: Int, heightSpec: Int) {
        var heightMeasureSpec = heightSpec
        val mode = MeasureSpec.getMode(heightMeasureSpec)
        // Unspecified means that the ViewPager is in a ScrollView WRAP_CONTENT.
        // At Most means that the ViewPager is not in a ScrollView WRAP_CONTENT.
        if (mode == MeasureSpec.UNSPECIFIED || mode == MeasureSpec.AT_MOST) {
            // super has to be called in the beginning so the child views can be initialized.
            super.onMeasure(widthMeasureSpec, heightMeasureSpec)
            var height = 0
            for (i in 0 until childCount) {
                val child = getChildAt(i)
                child.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED))
                val childMeasuredHeight = child.measuredHeight
                if (childMeasuredHeight > height) {
                    height = childMeasuredHeight
                }
            }
            if (height != 0 && isAddExtraHeight) {
                height += 50
            }
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(height + paddingTop, MeasureSpec.EXACTLY)
            /*        for(int i = 0; i < getChildCount(); i++) {
                View child = getChildAt(i);
                child.getLayoutParams().height = height;
                child.requestLayout();
            }*/
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        return !swipeLocked && super.onTouchEvent(event)
    }

    override fun onInterceptTouchEvent(event: MotionEvent): Boolean {
        return !swipeLocked && super.onInterceptTouchEvent(event)
    }

    override fun canScrollHorizontally(direction: Int): Boolean {
        return !swipeLocked && super.canScrollHorizontally(direction)
    }

    fun setSwipeLocked(swipeLocked: Boolean) {
        this.swipeLocked = swipeLocked
    }
}