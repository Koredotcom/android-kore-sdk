package com.kore.ai.widgetsdk.listeners;

import android.view.MotionEvent;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class SwipeDetector implements View.OnTouchListener {

    private static final int MIN_DISTANCE = 50;
    private float downX;
    private float downY;
    private LinearLayoutManager linearLayoutManager;

    public SwipeDetector(LinearLayoutManager linearLayoutManager) {
        this.linearLayoutManager = linearLayoutManager;
    }


    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                downX = event.getX();
                downY = event.getY();
                v.getParent().getParent().requestDisallowInterceptTouchEvent(true);
                //   return true;
            }
            case MotionEvent.ACTION_UP: {
                float upX = event.getX();
                float upY = event.getY();

                float deltaX = downX - upX;
                float deltaY = downY - upY;

                // swipe horizontal?
                if (Math.abs(deltaX) > MIN_DISTANCE) {
                    v.getParent().getParent().requestDisallowInterceptTouchEvent(false);
                    return false;
                } else if (Math.abs(deltaY) > MIN_DISTANCE) {
                    v.getParent().getParent().requestDisallowInterceptTouchEvent(false);
                    return false;
                } else if (linearLayoutManager != null && linearLayoutManager.findFirstCompletelyVisibleItemPosition() == 0 && linearLayoutManager.findLastCompletelyVisibleItemPosition() == ((RecyclerView) (v)).getChildCount() - 1 ) {
                    v.getParent().getParent().requestDisallowInterceptTouchEvent(false);
                    return false;
                }else{
                    v.getParent().getParent().requestDisallowInterceptTouchEvent(true);
                }

                //     return true;
            }
        }
        return false;
    }
}