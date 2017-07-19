package kore.botssdk.view.viewUtils;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import kore.botssdk.R;
import kore.botssdk.adapter.BotCarouselItemButtonAdapter;
import kore.botssdk.models.BotCarouselModel;

/**
 * Created by Pradeep Mahato on 19/7/17.
 * Copyright (c) 2014 Kore Inc. All rights reserved.
 */
public class CarouselItemViewHelper {

    int carouselPosition;

    public static class CarouselViewHolder {
        ImageView carouselItemImage;
        TextView carouselItemTitle, carouselItemSubTitle, carousel_textViw;
        ListView carouselButtonListview;
        RelativeLayout carouselItemRoot;
        RelativeLayout carouselItemWidthContainer;
    }

    public static void initializeViewHolder(View view) {
        CarouselViewHolder carouselViewHolder = new CarouselViewHolder();

        carouselViewHolder.carouselItemWidthContainer = (RelativeLayout) view.findViewById(R.id.carousel_item_width);
        carouselViewHolder.carouselItemRoot = (RelativeLayout) view.findViewById(R.id.carousel_item_root);
        carouselViewHolder.carouselItemImage = (ImageView) view.findViewById(R.id.carousel_item_image);
        carouselViewHolder.carouselItemTitle = (TextView) view.findViewById(R.id.carousel_item_title);
        carouselViewHolder.carouselItemSubTitle = (TextView) view.findViewById(R.id.carousel_item_subtitle);
        carouselViewHolder.carouselButtonListview = (ListView) view.findViewById(R.id.carousel_button_listview);
        carouselViewHolder.carousel_textViw = (TextView) view.findViewById(R.id.carousel_textViw);

        view.setTag(carouselViewHolder);
    }

    public static void populateStuffs(CarouselViewHolder carouselViewHolder, BotCarouselModel botCarouselModel, Context activityContext) {

        if (botCarouselModel != null) {
            carouselViewHolder.carouselItemTitle.setText(botCarouselModel.getTitle());
            carouselViewHolder.carouselItemSubTitle.setText(botCarouselModel.getSubtitle());

            Picasso.with(activityContext).load(botCarouselModel.getImage_url()).into(carouselViewHolder.carouselItemImage);

            BotCarouselItemButtonAdapter botCarouselItemButtonAdapter = new BotCarouselItemButtonAdapter(activityContext);
            carouselViewHolder.carouselButtonListview.setAdapter(botCarouselItemButtonAdapter);
            botCarouselItemButtonAdapter.setBotCaourselButtonModels(botCarouselModel.getButtons());

            /*carousel_textViw.setText(carouselPosition + "");
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
            }*/
        }
    }

}
