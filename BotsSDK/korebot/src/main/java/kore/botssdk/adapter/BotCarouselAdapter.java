package kore.botssdk.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.viewpager.widget.PagerAdapter;

import java.util.ArrayList;

import kore.botssdk.R;
import kore.botssdk.listener.ComposeFooterInterface;
import kore.botssdk.listener.InvokeGenericWebViewInterface;
import kore.botssdk.models.BotCarouselModel;
import kore.botssdk.utils.KaFontUtils;
import kore.botssdk.view.viewUtils.CarouselItemViewHelper;

/**
 * Created by Pradeep Mahato on 14-July-17.
 * Copyright (c) 2014 Kore Inc. All rights reserved.
 */
@SuppressLint("UnknownNullness")
public class BotCarouselAdapter extends PagerAdapter {

    private ArrayList<? extends BotCarouselModel> botCarouselModels = new ArrayList<>();
    private final Activity activityContext;
    private final ComposeFooterInterface composeFooterInterface;
    private final InvokeGenericWebViewInterface invokeGenericWebViewInterface;
    private final LayoutInflater ownLayoutInflater;
    private final float pageWidth;
    private String type;

    public BotCarouselAdapter(ComposeFooterInterface composeFooterInterface,
                              InvokeGenericWebViewInterface invokeGenericWebViewInterface,
                              Activity activityContext) {
        super();
        this.activityContext = activityContext;
        this.composeFooterInterface = composeFooterInterface;
        this.invokeGenericWebViewInterface = invokeGenericWebViewInterface;
        ownLayoutInflater = activityContext.getLayoutInflater();

        TypedValue typedValue = new TypedValue();
        activityContext.getResources().getValue(R.dimen.carousel_item_width_factor, typedValue, true);
        pageWidth = typedValue.getFloat();
    }

    @Override
    public int getCount() {
        if (botCarouselModels == null) {
            return 0;
        } else {
            return botCarouselModels.size();
        }
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == ( object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        final View carouselItemLayout = ownLayoutInflater.inflate(R.layout.carousel_item_layout, container, false);
        KaFontUtils.applyCustomFont(activityContext,carouselItemLayout);
        CarouselItemViewHelper.initializeViewHolder(carouselItemLayout);
        CarouselItemViewHelper.CarouselViewHolder carouselViewHolder = (CarouselItemViewHelper.CarouselViewHolder) carouselItemLayout.getTag();
        CarouselItemViewHelper.populateStuffs(carouselViewHolder, composeFooterInterface, invokeGenericWebViewInterface, botCarouselModels.get(position), activityContext,type);
        container.addView(carouselItemLayout);
        return carouselItemLayout;
    }

    @Override
    public void finishUpdate(@NonNull ViewGroup container) {
        super.finishUpdate(container);
       // applyParams();
    }


    public void setBotCarouselModels(ArrayList<? extends BotCarouselModel> botCarouselModels) {
        this.botCarouselModels = botCarouselModels;
    }

    @Override
    public float getPageWidth(int position) {
        if (getCount() == 0) {
            return super.getPageWidth(position);
        } else {
            return pageWidth;
        }
    }

    @Override
    public void destroyItem(ViewGroup container, int position, @NonNull Object object) {
        container.removeView(( CardView) object);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
