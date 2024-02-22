package kore.botssdk.view.viewUtils;

import static android.view.View.GONE;

import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.StrikethroughSpan;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.squareup.picasso.Picasso;

import org.apache.commons.lang3.StringEscapeUtils;

import kore.botssdk.R;
import kore.botssdk.adapter.BotCarouselItemButtonAdapter;
import kore.botssdk.listener.ComposeFooterInterface;
import kore.botssdk.listener.InvokeGenericWebViewInterface;
import kore.botssdk.models.BotCaourselButtonModel;
import kore.botssdk.models.BotCarouselModel;
import kore.botssdk.models.BotListDefaultModel;
import kore.botssdk.models.BotResponse;
import kore.botssdk.utils.BundleConstants;
import kore.botssdk.utils.StringUtils;
import kore.botssdk.utils.Utils;

/**
 * Created by Pradeep Mahato on 19/7/17.
 * Copyright (c) 2014 Kore Inc. All rights reserved.
 */
public class CarouselItemViewHelper {

    public static class CarouselViewHolder {
        public ImageView carouselItemImage;
        public TextView carouselItemTitle;
        public TextView carouselItemSubTitle;
        public TextView hashTagsView;
        public TextView knowledgeType;
        public TextView knowledgeMode;
        public RelativeLayout koraItems;
        ListView carouselButtonListview;
        public CardView carouselItemRoot;

        FrameLayout carouselOfferPrice_FL,carouselSavedPrice_FL;
        TextView carousel_item_offer,carousel_item_save_price;
    }

    public static void initializeViewHolder(View view) {
        CarouselViewHolder carouselViewHolder = new CarouselViewHolder();

        carouselViewHolder.carouselItemRoot = view.findViewById(R.id.carousel_item_root);
        carouselViewHolder.carouselItemImage = view.findViewById(R.id.carousel_item_image);
        carouselViewHolder.carouselItemTitle = view.findViewById(R.id.carousel_item_title);
        carouselViewHolder.carouselItemSubTitle = view.findViewById(R.id.carousel_item_subtitle);
        carouselViewHolder.carouselButtonListview = view.findViewById(R.id.carousel_button_listview);

        carouselViewHolder.hashTagsView = view.findViewById(R.id.hash_tags_view);
        carouselViewHolder.knowledgeType = view.findViewById(R.id.knowledge_type);
        carouselViewHolder.knowledgeMode = view.findViewById(R.id.knowledge_mode);
        carouselViewHolder.koraItems = view.findViewById(R.id.kora_items);
        carouselViewHolder.carouselOfferPrice_FL = view.findViewById(R.id.offer_price_fl);
        carouselViewHolder.carouselSavedPrice_FL = view.findViewById(R.id.saved_price_fl);

        carouselViewHolder.carousel_item_offer = view.findViewById(R.id.carousel_item_offer);
        carouselViewHolder.carousel_item_save_price= view.findViewById(R.id.carousel_item_saved);

        view.setTag(carouselViewHolder);
    }

    public static void populateStuffs(CarouselViewHolder carouselViewHolder,
                                      final ComposeFooterInterface composeFooterInterface,
                                      final InvokeGenericWebViewInterface invokeGenericWebViewInterface,
                                      final BotCarouselModel botCarouselModel,
                                      final Context activityContext,final String type) {

        if (botCarouselModel != null) {



            carouselViewHolder.carouselItemTitle.setText(botCarouselModel.getTitle());
            if (!StringUtils.isNullOrEmptyWithTrim(botCarouselModel.getSubtitle())) {
                carouselViewHolder.carouselItemSubTitle.setText(BotResponse.TEMPLATE_TYPE_WELCOME_CAROUSEL.equalsIgnoreCase(type) ? botCarouselModel.getSubtitle() : Html.fromHtml(StringEscapeUtils.unescapeHtml4(botCarouselModel.getSubtitle()).replaceAll("<br>", "")));
               carouselViewHolder.carouselItemSubTitle.setMaxLines(BotResponse.TEMPLATE_TYPE_WELCOME_CAROUSEL.equalsIgnoreCase(type) ? Integer.MAX_VALUE : 3);
                carouselViewHolder.carouselItemSubTitle.setVisibility(View.VISIBLE);
            }else{
                carouselViewHolder.carouselItemSubTitle.setVisibility(GONE);
            }
            try {
                if(botCarouselModel.getImage_url() != null && !botCarouselModel.getImage_url().isEmpty()) {
                    Picasso.get().load(botCarouselModel.getImage_url()).into(carouselViewHolder.carouselItemImage);
                    carouselViewHolder.carouselItemImage.setVisibility(View.VISIBLE);
                }else{
                    carouselViewHolder.carouselItemImage.setVisibility(GONE);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            if(botCarouselModel.getButtons() != null) {
                BotCarouselItemButtonAdapter botCarouselItemButtonAdapter = new BotCarouselItemButtonAdapter(activityContext);
                carouselViewHolder.carouselButtonListview.setAdapter(botCarouselItemButtonAdapter);
                botCarouselItemButtonAdapter.setBotCarouselButtonModels(botCarouselModel.getButtons());
            }

            String price = Utils.isNullOrEmpty(botCarouselModel.getPrice())?"":botCarouselModel.getPrice();
            String cost_price = Utils.isNullOrEmpty(botCarouselModel.getCost_price())?"":botCarouselModel.getCost_price();

            String text = (price+" "+ cost_price).trim();

            SpannableStringBuilder ssBuilder = new SpannableStringBuilder(text);

            // Initialize a new StrikeThroughSpan to display strike through text
            StrikethroughSpan strikethroughSpan = new StrikethroughSpan();

            // Apply the strike through text to the span
            ssBuilder.setSpan(
                    strikethroughSpan, // Span to add
                    text.indexOf(price), // Start of the span (inclusive)
                    text.indexOf(price) + price.length(), // End of the span (exclusive)
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE // Do not extend the span when text add later
            );

            if(!Utils.isNullOrEmpty(botCarouselModel.getPrice()) || !Utils.isNullOrEmpty(botCarouselModel.getCost_price())) {
                carouselViewHolder.carouselOfferPrice_FL.setVisibility(View.VISIBLE);
                if(!Utils.isNullOrEmpty(botCarouselModel.getCost_price()))
                    carouselViewHolder.carousel_item_offer.setText(ssBuilder);
                else
                    carouselViewHolder.carousel_item_offer.setText(text);
            }else{
                carouselViewHolder.carouselOfferPrice_FL.setVisibility(View.GONE);
            }

            if(!Utils.isNullOrEmpty(botCarouselModel.getSaved_price())) {
                carouselViewHolder.carouselSavedPrice_FL.setVisibility(View.VISIBLE);
                carouselViewHolder.carousel_item_save_price.setText(botCarouselModel.getSaved_price());
            }else{
                carouselViewHolder.carouselSavedPrice_FL.setVisibility(View.GONE);
            }

            BotCarouselItemButtonAdapter botCarouselItemButtonAdapter = new BotCarouselItemButtonAdapter(activityContext);
            carouselViewHolder.carouselButtonListview.setAdapter(botCarouselItemButtonAdapter);
           // carouselViewHolder.carouselButtonListview.getLayoutParams().height = (int)(botCarouselModel.getButtons() != null ? botCarouselModel.getButtons().size() * (48 * dp1) : 0);
            botCarouselItemButtonAdapter.setBotCarouselButtonModels(botCarouselModel.getButtons());
            carouselViewHolder.carouselButtonListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (composeFooterInterface != null && invokeGenericWebViewInterface != null) {
                        BotCaourselButtonModel botCaourselButtonModel = (BotCaourselButtonModel) parent.getAdapter().getItem(position);
                        if (BundleConstants.BUTTON_TYPE_WEB_URL.equalsIgnoreCase(botCaourselButtonModel.getType())) {
                            invokeGenericWebViewInterface.invokeGenericWebView(botCaourselButtonModel.getUrl());
                        } else if (BundleConstants.BUTTON_TYPE_POSTBACK.equalsIgnoreCase(botCaourselButtonModel.getType())) {
                            String buttonPayload = botCaourselButtonModel.getPayload();
                            String buttonTitle = botCaourselButtonModel.getTitle();
                            composeFooterInterface.onSendClick(buttonTitle, buttonPayload,false);
                        }else if(BundleConstants.BUTTON_TYPE_HELP_RESOLVE.equalsIgnoreCase(botCaourselButtonModel.getType())){
                            Bundle extras = new Bundle();
                            extras.putString(BundleConstants.RESOURCE_ID,botCaourselButtonModel.getId());
                            composeFooterInterface.launchActivityWithBundle(BotResponse.TEMPLATE_TYPE_WELCOME_CAROUSEL,extras);
                        }else if(BundleConstants.BUTTON_TYPE_POSTBACK_DISP_PAYLOAD.equalsIgnoreCase(botCaourselButtonModel.getType())){
                            String buttonPayload = botCaourselButtonModel.getPayload();
//                            String buttonTitle = botCaourselButtonModel.getTitle();
                            composeFooterInterface.onSendClick(buttonPayload, buttonPayload,false);
                        }else if (BundleConstants.BUTTON_TYPE_USER_INTENT.equalsIgnoreCase(botCaourselButtonModel.getType())) {
                            invokeGenericWebViewInterface.handleUserActions(botCaourselButtonModel.getAction(),botCaourselButtonModel.getCustomData());
                        }else if(BundleConstants.BUTTON_TYPE_TEXT.equalsIgnoreCase(botCaourselButtonModel.getType())){
                            String buttonTitle = botCaourselButtonModel.getTitle();
                            composeFooterInterface.onSendClick(buttonTitle,false);
                        }else{
                            String buttonPayload = botCaourselButtonModel.getPayload();
                            String buttonTitle = botCaourselButtonModel.getTitle();
                            composeFooterInterface.onSendClick(buttonTitle, buttonPayload,false);
                        }
                    }
                }
            });

            carouselViewHolder.carouselItemRoot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BotListDefaultModel botListDefaultModel = botCarouselModel.getDefault_action();
                    if (invokeGenericWebViewInterface != null && botListDefaultModel != null) {
                        if (BundleConstants.BUTTON_TYPE_WEB_URL.equalsIgnoreCase(botListDefaultModel.getType())) {
                            invokeGenericWebViewInterface.invokeGenericWebView(botListDefaultModel.getUrl());
                        } else if (BundleConstants.BUTTON_TYPE_USER_INTENT.equalsIgnoreCase(botListDefaultModel.getType())) {
                            invokeGenericWebViewInterface.handleUserActions(botListDefaultModel.getAction(), botListDefaultModel.getCustomData());
                        }
                    } else if (composeFooterInterface != null && botListDefaultModel != null) {
                        if (BundleConstants.BUTTON_TYPE_POSTBACK.equalsIgnoreCase(botListDefaultModel.getType())) {
                            String buttonPayload = botCarouselModel.getDefault_action().getPayload();
                            composeFooterInterface.onSendClick(buttonPayload,false);
                        } else if (BundleConstants.BUTTON_TYPE_POSTBACK_DISP_PAYLOAD.equalsIgnoreCase(botListDefaultModel.getType())) {
                            String buttonPayload = botCarouselModel.getDefault_action().getPayload();
                            composeFooterInterface.onSendClick(buttonPayload,false);
                        }
                    }
                }
            });

                carouselViewHolder.koraItems.setVisibility(View.GONE);

        }
    }

    private static int getButtonHeight(Context context, int itemCount, float dp1) {
        return (int) (context.getResources().getDimension(R.dimen.carousel_view_button_height_individual) * dp1 + 4 * dp1);
    }


}
