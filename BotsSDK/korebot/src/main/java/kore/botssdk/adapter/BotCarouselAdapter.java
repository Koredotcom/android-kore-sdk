package kore.botssdk.adapter;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import kore.botssdk.R;
import kore.botssdk.fragment.CarouselFragment;
import kore.botssdk.models.BotCarouselModel;
import kore.botssdk.utils.BundleConstants;

/**
 * Created by Pradeep Mahato on 14-July-17.
 * Copyright (c) 2014 Kore Inc. All rights reserved.
 */
public class BotCarouselAdapter extends PagerAdapter {

    ArrayList<BotCarouselModel> botCarouselModels = new ArrayList<>();
    Activity activityContext;
    LayoutInflater ownLayoutInflater;

    public BotCarouselAdapter(FragmentManager fm, Activity activityContext) {
        super();
        this.activityContext = activityContext;
        ownLayoutInflater = activityContext.getLayoutInflater();
    }

    public void setBotCarouselModels(ArrayList<BotCarouselModel> botCarouselModels) {
        this.botCarouselModels = botCarouselModels;
    }

    @Override
    public int getCount() {
        return botCarouselModels.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((RelativeLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View carouselItemLayout = ownLayoutInflater.inflate(R.layout.carousel_item_layout, container, false);
        findViews(carouselItemLayout);
        Bundle bundle = new Bundle();
        bundle.putString(BundleConstants.CAROUSEL_ITEM, gson.toJson(botCarouselModels.get(position)));
        bundle.putInt(BundleConstants.CAROUSEL_ITEM_POSITION, position);
        populateStuffs(bundle);

        container.addView(carouselItemLayout);

        return carouselItemLayout;

    }

    @Override
    public float getPageWidth(int position) {
        float nbPages = getCount(); // You could display partial pages using a float value
        if (getCount() == 0) {
            return super.getPageWidth(position);
        } else {
            return 1.0f;
        }
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout) object);
    }

    void findViews(View view) {
        carouselItemWidthContainer = (RelativeLayout) view.findViewById(R.id.carousel_item_width);
        carouselItemRoot = (RelativeLayout) view.findViewById(R.id.carousel_item_root);
        carouselItemImage = (ImageView) view.findViewById(R.id.carousel_item_image);
        carouselItemTitle = (TextView) view.findViewById(R.id.carousel_item_title);
        carouselItemSubTitle = (TextView) view.findViewById(R.id.carousel_item_subtitle);
        carouselButtonListview = (ListView) view.findViewById(R.id.carousel_button_listview);
        carousel_textViw = (TextView) view.findViewById(R.id.carousel_textViw);
    }

    ImageView carouselItemImage;
    TextView carouselItemTitle, carouselItemSubTitle, carousel_textViw;
    ListView carouselButtonListview;
    RelativeLayout carouselItemRoot;
    int carouselPosition;
    RelativeLayout carouselItemWidthContainer;
    BotCarouselModel botCarouselModel;
    Gson gson = new Gson();

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

        if (botCarouselModel != null) {
            carouselItemTitle.setText(botCarouselModel.getTitle());
            carouselItemSubTitle.setText(botCarouselModel.getSubtitle());

            Picasso.with(activityContext).load(botCarouselModel.getImage_url()).into(carouselItemImage);

            BotCarouselItemButtonAdapter botCarouselItemButtonAdapter = new BotCarouselItemButtonAdapter(activityContext);
            carouselButtonListview.setAdapter(botCarouselItemButtonAdapter);
            botCarouselItemButtonAdapter.setBotCaourselButtonModels(botCarouselModel.getButtons());

            carousel_textViw.setText(carouselPosition + "");
            switch (carouselPosition) {
                case 0:
                    carouselItemWidthContainer.setBackgroundColor(0xffff0000);
                    break;
                case 1:
                    carouselItemWidthContainer.setBackgroundColor(0xff00ff00);
                    break;
                case 2:
                    carouselItemWidthContainer.setBackgroundColor(0xff0000ff);
                    break;
                default:
                    carouselItemWidthContainer.setBackgroundColor(0xff0f0f0f);
            }
        }
    }

    public Fragment getItem(int position) {
        return getFragment(position);
    }

    private Fragment getFragment(int position) {
        CarouselFragment carouselFragment = new CarouselFragment();

        Bundle bundle = new Bundle();
//        bundle.putString(BundleConstants.CAROUSEL_ITEM, gson.toJson(botCarouselModels.get(position)));
        bundle.putInt(BundleConstants.CAROUSEL_ITEM_POSITION, position);

        carouselFragment.setArguments(bundle);
        return carouselFragment;
    }

}
