package kore.botssdk.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import kore.botssdk.models.KoraUniversalSearchModel;
import kore.botssdk.view.viewUtils.LayoutUtils;

public class UniversalSearchView extends ViewGroup {
    public UniversalSearchView(Context context) {
        super(context);
    }

    public UniversalSearchView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public UniversalSearchView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        final int count = getChildCount();
        int parentWidth = getMeasuredWidth();

        //get the available size of child view
        int childLeft = 0;
        int childTop = 0;

        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() != GONE) {
                LayoutUtils.layoutChild(child, childLeft, childTop);
                childTop += child.getMeasuredHeight();
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public void populateData(KoraUniversalSearchModel koraUniversalSearchModel){

    }
}
