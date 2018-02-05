package kore.botssdk.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.jar.Attributes;
import java.util.zip.Inflater;

import kore.botssdk.R;
import kore.botssdk.application.AppControl;
import kore.botssdk.view.viewUtils.LayoutUtils;
import kore.botssdk.view.viewUtils.MeasureUtils;

/**
 * Created by Shiva Krishna on 2/5/2018.
 */



public class KoraCarouselView extends ViewGroup {
    Context mContext;
    float dp1;
    TextView headerView;
    ViewPager carousalView;

    public KoraCarouselView(Context mContext){

        super(mContext);
        this.mContext = mContext;
        init();
    }
    public KoraCarouselView(Context mContext, AttributeSet attributes){
        super(mContext,attributes);
        this.mContext = mContext;
        init();
    }
    public KoraCarouselView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        dp1 = AppControl.getInstance().getDimensionUtil().dp1;
        View view  = LayoutInflater.from(getContext()).inflate(R.layout.kora_carousel_view, this, true);
        headerView = (TextView) view.findViewById(R.id.header_view);
        carousalView = (ViewPager) view.findViewById(R.id.carouselViewpager);
    }

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
        int childHeight =0;

/*
        */
/*
         * For Carousel ViewPager Layout
         *//*

         childHeight = botCarouselAdapter != null ? botCarouselAdapter.getMaxChildHeight() : 0 ;
        childWidthSpec = MeasureSpec.makeMeasureSpec(maxAllowedWidth, MeasureSpec.AT_MOST);
        childHeightSpec = MeasureSpec.makeMeasureSpec( childHeight, MeasureSpec.EXACTLY);
        MeasureUtils.measure(carouselViewpager, childWidthSpec, childHeight != 0 ? childHeightSpec : wrapSpec);
*/

        totalHeight += childHeight+getPaddingBottom()+getPaddingTop();
        int parentHeightSpec = MeasureSpec.makeMeasureSpec( totalHeight, MeasureSpec.EXACTLY);

        super.onMeasure(widthMeasureSpec, parentHeightSpec);
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
}
