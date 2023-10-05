package kore.botssdk.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import java.util.ArrayList;

import kore.botssdk.R;
import kore.botssdk.adapter.BotCarouselAdapter;
import kore.botssdk.listener.ComposeFooterInterface;
import kore.botssdk.listener.InvokeGenericWebViewInterface;
import kore.botssdk.models.BotCarouselModel;
import kore.botssdk.view.viewUtils.DimensionUtil;

/**
 * Created by Pradeep Mahato on 13-July-17.
 * Copyright (c) 2014 Kore Inc. All rights reserved.
 */
@SuppressLint("UnknownNullness")
public class BotCarouselView extends LinearLayout {

    HeightAdjustableViewPager carouselViewpager;
//    HorizontalInfiniteCycleViewPager carouselViewpager;
    int dp1;

    private int carouselViewWidth;
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
//        View inflatedView = LayoutInflater.from(getContext()).inflate(R.layout.bot_carousel_infinite, this, true);
        carouselViewpager = inflatedView.findViewById(R.id.carouselViewpager);
//        carouselViewpager.setAddExtraHeight(true);
        int pageMargin = (int) getResources().getDimension(R.dimen.carousel_item_page_margin);
        // Disable clip to padding
//        carouselViewpager.setClipToPadding(false);
//        // set padding manually, the more you set the padding the more you see of prev & next page
//        carouselViewpager.setPadding(40 * dp1, 0, 20, 0);
        carouselViewpager.setPageMargin(pageMargin);

    }
    public void populateCarouselView(ArrayList<? extends BotCarouselModel> botCarouselModelArrayList){
        populateCarouselView(botCarouselModelArrayList,null);
    }

    public void populateCarouselView(ArrayList<? extends BotCarouselModel> botCarouselModelArrayList, String type) {
        if (composeFooterInterface != null && activityContext != null)
        {
            botCarouselAdapter = new BotCarouselAdapter(composeFooterInterface, invokeGenericWebViewInterface, activityContext);
            botCarouselAdapter.setBotCarouselModels(botCarouselModelArrayList);
            botCarouselAdapter.setType(type);
            carouselViewpager.setAdapter(botCarouselAdapter);
        }
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

}
