package kore.botssdk.view;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import kore.botssdk.R;
import kore.botssdk.adapter.BotCarouselAdapter;
import kore.botssdk.listener.ComposeFooterInterface;
import kore.botssdk.listener.InvokeGenericWebViewInterface;
import kore.botssdk.models.BotCarouselModel;
import kore.botssdk.view.viewUtils.DimensionUtil;
import kore.botssdk.view.viewUtils.LayoutUtils;
import kore.botssdk.view.viewUtils.MeasureUtils;

/**
 * Created by Pradeep Mahato on 13-July-17.
 * Copyright (c) 2014 Kore Inc. All rights reserved.
 */

public class BotCarouselView extends ViewGroup {

    HeightAdjustableViewPager carouselViewpager;
    int dp1;

    private int layoutWidth, layoutHeight;
    private int carouselViewWidth, carouselViewHeight;
    Activity activityContext;
    ComposeFooterInterface composeFooterInterface;
    InvokeGenericWebViewInterface invokeGenericWebViewInterface;
    BotCarouselAdapter botCarouselAdapter;


    public BotCarouselView(Context context) {
        super(context);
        init();
    }

    public BotCarouselView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BotCarouselView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        dp1 = (int) DimensionUtil.dp1;
        View inflatedView = LayoutInflater.from(getContext()).inflate(R.layout.bot_carousel_view, this, true);
        carouselViewpager = inflatedView.findViewById(R.id.carouselViewpager);
        carouselViewpager.setAddExtraHeight(true);
        TypedValue typedValue = new TypedValue();
        int pageMargin = (int) getResources().getDimension(R.dimen.carousel_item_page_margin);
        carouselViewHeight = (int) getResources().getDimension(R.dimen.carousel_layout_height);

        carouselViewpager.setPageMargin(pageMargin);

    }
    public void populateCarouselView(ArrayList<? extends BotCarouselModel> botCarouselModelArrayList){
        populateCarouselView(botCarouselModelArrayList,null);
    }

    public void populateCarouselView(ArrayList<? extends BotCarouselModel> botCarouselModelArrayList, String type) {
        if (composeFooterInterface != null && activityContext != null) {
//            if (carouselViewpager.getAdapter() == null) {
                carouselViewpager.setOffscreenPageLimit(4);
                botCarouselAdapter = new BotCarouselAdapter(composeFooterInterface, invokeGenericWebViewInterface, activityContext);
                botCarouselAdapter.setBotCarouselModels(botCarouselModelArrayList);
                botCarouselAdapter.setType(type);
                carouselViewpager.setAdapter(botCarouselAdapter);
                botCarouselAdapter.notifyDataSetChanged();
                carouselViewpager.setSwipeLocked(botCarouselModelArrayList != null && botCarouselModelArrayList.size() ==1);
        /*        for(int i=0;i<carouselViewpager.getChildCount();i++){
                    View view = carouselViewpager.getChildAt(i).findViewById(R.id.carousel_item_root);
                    ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
                    layoutParams.height = botCarouselAdapter.getMaxChildHeight();
                    view.setLayoutParams(layoutParams);
                    invalidate();
                }*/

//            } else {
//                botCarouselAdapter = (BotCarouselAdapter) carouselViewpager.getAdapter();
//                botCarouselAdapter.setBotCarouselModels(botCarouselModelArrayList);
//                botCarouselAdapter.notifyDataSetChanged();
//            }
        }
    }

    public void setLayoutWidth(int layoutWidth) {
        this.layoutWidth = layoutWidth;
    }

    public void setLayoutHeight(int layoutHeight) {
        this.layoutHeight = layoutHeight;
    }

    public void setComposeFooterInterface(ComposeFooterInterface composeFooterInterface) {
        this.composeFooterInterface = composeFooterInterface;
    }

    public void setInvokeGenericWebViewInterface(InvokeGenericWebViewInterface invokeGenericWebViewInterface) {
        this.invokeGenericWebViewInterface = invokeGenericWebViewInterface;
    }

    public void setActivityContext(Activity activityContext) {
        this.activityContext = activityContext;
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




        childWidthSpec = MeasureSpec.makeMeasureSpec(maxAllowedWidth, MeasureSpec.AT_MOST);
        // childHeightSpec = MeasureSpec.makeMeasureSpec( childHeight , MeasureSpec.EXACTLY);
        MeasureUtils.measure(carouselViewpager, childWidthSpec, wrapSpec);

        totalHeight += carouselViewpager.getMeasuredHeight()+getPaddingBottom()+getPaddingTop();
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
