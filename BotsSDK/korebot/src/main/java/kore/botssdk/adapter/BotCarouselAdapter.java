package kore.botssdk.adapter;

import android.app.Activity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.cardview.widget.CardView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
public class BotCarouselAdapter extends PagerAdapter {

    private ArrayList<? extends BotCarouselModel> botCarouselModels = new ArrayList<>();
    private Activity activityContext;
    private ComposeFooterInterface composeFooterInterface;
    private InvokeGenericWebViewInterface invokeGenericWebViewInterface;
    private LayoutInflater ownLayoutInflater;
    private float pageWidth = 0.8f;
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
    public boolean isViewFromObject(View view, Object object) {
        return view == ( object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        final View carouselItemLayout = ownLayoutInflater.inflate(R.layout.carousel_item_layout, container, false);
        KaFontUtils.applyCustomFont(activityContext,carouselItemLayout);
        CarouselItemViewHelper.initializeViewHolder(carouselItemLayout);
        CarouselItemViewHelper.CarouselViewHolder carouselViewHolder = (CarouselItemViewHelper.CarouselViewHolder) carouselItemLayout.getTag();
       // carouselViewHolder.carouselItemSubTitle.setMaxLines(maxLines);
        CarouselItemViewHelper.populateStuffs(carouselViewHolder, composeFooterInterface, invokeGenericWebViewInterface, botCarouselModels.get(position), activityContext,type);
        container.addView(carouselItemLayout);
/*        ViewTreeObserver vto = carouselItemLayout.getViewTreeObserver();
        vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener()
        {
            public boolean onPreDraw()
            {
                CarouselItemViewHelper.CarouselViewHolder holder = (CarouselItemViewHelper.CarouselViewHolder) carouselItemLayout.getTag();
                int height = (int)(holder.carouselItemImage.getMeasuredHeight() + holder.carouselItemSubTitle.getMeasuredHeight() + holder.carouselItemTitle.getMeasuredHeight() + (botCarouselModels.get(position).getButtons() != null ? botCarouselModels.get(position).getButtons().size() * 48 * dp1 :0)+ 45 * dp1);
                if(botCarouselModels.get(position) instanceof KnowledgeDetailModel){
                    height = height+holder.hashTagsView.getMeasuredHeight()+(holder.knowledgeMode.getMeasuredHeight() > holder.knowledgeType.getMeasuredHeight() ? holder.knowledgeMode.getMeasuredHeight() : holder.knowledgeType.getMeasuredHeight());
                }

                heights.add(height);
                ViewGroup.LayoutParams layoutParams = holder.carouselItemRoot.getLayoutParams();
                layoutParams.height = height;
                holder.carouselItemRoot.setLayoutParams(layoutParams);

              //  views.add(holder.carouselItemRoot);
                return true;
            }
        });*/
        return carouselItemLayout;

    }

    @Override
    public void finishUpdate(ViewGroup container) {
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
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(( CardView) object);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
