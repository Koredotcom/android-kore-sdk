package com.kore.ai.widgetsdk.views.viewutils;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.DecelerateInterpolator;
import android.widget.Scroller;

import androidx.viewpager.widget.ViewPager;

import java.lang.reflect.Field;

/**
 * Created by Ramachandra Pradeep on 04-Apr-19.
 */

public class MainViewPager extends ViewPager {
    private Boolean disable = false;
    public MainViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        setMyScroller();
    }

    private void setMyScroller() {
        try {
            Class<?> viewpager = ViewPager.class;
            Field scroller = viewpager.getDeclaredField("mScroller");
            scroller.setAccessible(true);
            scroller.set(this, new MyScroller(getContext()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return !disable && super.onTouchEvent(event);
    }

    public class MyScroller extends Scroller {
        public MyScroller(Context context) {
            super(context, new DecelerateInterpolator());
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy, int duration) {
            super.startScroll(startX, startY, dx, dy, 500 /*1 secs*/);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return !disable && super.onInterceptTouchEvent(event);
    }


    public void disableScroll(Boolean disable){
        //When disable = true not work the scroll and when disble = false work the scroll
        this.disable = disable;
    }
}
