package kore.botssdk.view;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import kore.botssdk.R;
import kore.botssdk.adapter.BotCarouselItemButtonAdapter;
import kore.botssdk.models.BotCarouselModel;
import kore.botssdk.utils.BundleConstants;
import kore.botssdk.view.viewUtils.DimensionUtil;
import kore.botssdk.view.viewUtils.LayoutUtils;

/**
 * Created by Pradeep Mahato on 18/7/17.
 * Copyright (c) 2014 Kore Inc. All rights reserved.
 */
public class CarouselItemView extends ViewGroup {

    LayoutInflater ownLayoutInflater;
    int dp1;

    ImageView carouselItemImage;
    TextView carouselItemTitle, carouselItemSubTitle;
    ListView carouselButtonListview;
    RelativeLayout carouselItemRoot;
    ViewGroup container;

    BotCarouselModel botCarouselModel;
    Gson gson;


    public CarouselItemView(Context context) {
        super(context);
        init();
    }

    public CarouselItemView(Context context, ViewGroup container) {
        super(context);
        this.container = container;
        init();
    }

    public CarouselItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CarouselItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        ownLayoutInflater = LayoutInflater.from(getContext());
        dp1 = (int) DimensionUtil.dp1;

        View inflatedView;
        inflatedView = ownLayoutInflater.inflate(R.layout.carousel_item_layout, container, false);
        addView(inflatedView);
        gson = new Gson();
        findViews(getRootView());
    }

    void findViews(View view) {
        carouselItemRoot = view.findViewById(R.id.carousel_item_root);
        carouselItemImage = view.findViewById(R.id.carousel_item_image);
        carouselItemTitle = view.findViewById(R.id.carousel_item_title);
        carouselItemSubTitle = view.findViewById(R.id.carousel_item_subtitle);
        carouselButtonListview = view.findViewById(R.id.carousel_button_listview);
    }

    int carouselPosition;
    void extractFromBundle(Bundle bundle) {

        String carouselModel = bundle.getString(BundleConstants.CAROUSEL_ITEM, "");
        carouselPosition = bundle.getInt(BundleConstants.CAROUSEL_ITEM_POSITION);

        if (!carouselModel.isEmpty()) {
            botCarouselModel = gson.fromJson(carouselModel, BotCarouselModel.class);
        }
    }

    public void populateStuffs(Bundle bundle) {
        if (bundle != null) {
            extractFromBundle(bundle);
        }

        String title =  "";
        String subtitle = "";
        String imageUrl = "";

        if (botCarouselModel != null) {
            if (botCarouselModel.getTitle() != null) {
                title = botCarouselModel.getTitle();
            }
            if (botCarouselModel.getSubtitle() != null) {
                subtitle = botCarouselModel.getSubtitle();
            }
            if (botCarouselModel.getImage_url() != null) {
                imageUrl = botCarouselModel.getImage_url();
            }


            carouselItemTitle.setText(title);
            carouselItemSubTitle.setText(subtitle);

            Picasso.get().load(imageUrl).into(carouselItemImage);

            BotCarouselItemButtonAdapter botCarouselItemButtonAdapter = new BotCarouselItemButtonAdapter(getContext());
            carouselButtonListview.setAdapter(botCarouselItemButtonAdapter);
            botCarouselItemButtonAdapter.setBotCarouselButtonModels(botCarouselModel.getButtons());

        }
    }

    public void setContainer(ViewGroup container) {
        this.container = container;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int parentHeightSpec = MeasureSpec.makeMeasureSpec(250 * dp1, MeasureSpec.EXACTLY);
        int parentWidthSpec = MeasureSpec.makeMeasureSpec(250 * dp1, MeasureSpec.EXACTLY);

        super.onMeasure(parentWidthSpec, parentHeightSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        View childView = getChildAt(0);
        LayoutUtils.layoutChild(childView, 0, 0);
    }
}
