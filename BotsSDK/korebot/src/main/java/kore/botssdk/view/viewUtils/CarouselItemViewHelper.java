package kore.botssdk.view.viewUtils;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import kore.botssdk.R;
import kore.botssdk.adapter.BotCarouselItemButtonAdapter;
import kore.botssdk.application.AppControl;
import kore.botssdk.fragment.ComposeFooterFragment;
import kore.botssdk.listener.InvokeGenericWebViewInterface;
import kore.botssdk.models.BotCaourselButtonModel;
import kore.botssdk.models.BotCarouselModel;
import kore.botssdk.utils.BundleConstants;

/**
 * Created by Pradeep Mahato on 19/7/17.
 * Copyright (c) 2014 Kore Inc. All rights reserved.
 */
public class CarouselItemViewHelper {

    public static class CarouselViewHolder {
        ImageView carouselItemImage;
        TextView carouselItemTitle, carouselItemSubTitle;
        ListView carouselButtonListview;
        CardView carouselItemRoot;
    }

    public static void initializeViewHolder(View view) {
        CarouselViewHolder carouselViewHolder = new CarouselViewHolder();

        carouselViewHolder.carouselItemRoot = (CardView) view.findViewById(R.id.carousel_item_root);
        carouselViewHolder.carouselItemImage = (ImageView) view.findViewById(R.id.carousel_item_image);
        carouselViewHolder.carouselItemTitle = (TextView) view.findViewById(R.id.carousel_item_title);
        carouselViewHolder.carouselItemSubTitle = (TextView) view.findViewById(R.id.carousel_item_subtitle);
        carouselViewHolder.carouselButtonListview = (ListView) view.findViewById(R.id.carousel_button_listview);

        view.setTag(carouselViewHolder);
    }

    public static void populateStuffs(CarouselViewHolder carouselViewHolder,
                                      final ComposeFooterFragment.ComposeFooterInterface composeFooterInterface,
                                      final InvokeGenericWebViewInterface invokeGenericWebViewInterface,
                                      final BotCarouselModel botCarouselModel,
                                      Context activityContext) {

        if (botCarouselModel != null) {

            float dp1 = AppControl.getInstance().getDimensionUtil().dp1;

            carouselViewHolder.carouselItemTitle.setText(botCarouselModel.getTitle());
            carouselViewHolder.carouselItemSubTitle.setText(botCarouselModel.getSubtitle());

            Picasso.with(activityContext).load(botCarouselModel.getImage_url()).into(carouselViewHolder.carouselItemImage);

            BotCarouselItemButtonAdapter botCarouselItemButtonAdapter = new BotCarouselItemButtonAdapter(activityContext);
            carouselViewHolder.carouselButtonListview.setAdapter(botCarouselItemButtonAdapter);
            botCarouselItemButtonAdapter.setBotCaourselButtonModels(botCarouselModel.getButtons());
            carouselViewHolder.carouselButtonListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (composeFooterInterface != null && invokeGenericWebViewInterface != null) {
                        BotCaourselButtonModel botCaourselButtonModel = (BotCaourselButtonModel) parent.getAdapter().getItem(position);
                        if (botCarouselModel.getDefault_action() != null &&
                                BundleConstants.BUTTON_TYPE_WEB_URL.equalsIgnoreCase(botCarouselModel.getDefault_action().getType())) {
                            invokeGenericWebViewInterface.invokeGenericWebView(botCarouselModel.getDefault_action().getUrl());
                        } else {
                            String buttonPayload = botCaourselButtonModel.getPayload();
                            composeFooterInterface.onSendClick(buttonPayload);
                        }
                    }
                }
            });

            carouselViewHolder.carouselItemRoot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (invokeGenericWebViewInterface != null) {
                        if (botCarouselModel.getDefault_action() != null &&
                                BundleConstants.BUTTON_TYPE_WEB_URL.equalsIgnoreCase(botCarouselModel.getDefault_action().getType())) {
                            invokeGenericWebViewInterface.invokeGenericWebView(botCarouselModel.getDefault_action().getUrl());
                        }
                    }
                }
            });
        }
    }

    private static int getButtonHeight(Context context, int itemCount, float dp1) {
        return (int) (context.getResources().getDimension(R.dimen.carousel_view_button_height_individual) * dp1 + 4 * dp1);
    }
}
