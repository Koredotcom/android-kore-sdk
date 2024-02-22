package kore.botssdk.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import kore.botssdk.R;
import kore.botssdk.adapter.BotCarouselItemButtonAdapter;
import kore.botssdk.models.BotCarouselModel;
import kore.botssdk.utils.BundleConstants;

/**
 * Created by Pradeep Mahato on 14-July-17.
 * Copyright (c) 2014 Kore Inc. All rights reserved.
 */
public class CarouselFragment extends Fragment {

    ImageView carouselItemImage;
    TextView carouselItemTitle, carouselItemSubTitle;
    ListView carouselButtonListview;
    RelativeLayout carouselItemRoot;

    BotCarouselModel botCarouselModel;
    Gson gson;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.carousel_item_layout, container, false);

        gson = new Gson();
        extractFromBundle();
        findViews(view);
        populateView();

        return view;
    }

    void findViews(View view) {
        carouselItemRoot = view.findViewById(R.id.carousel_item_root);
        carouselItemImage = view.findViewById(R.id.carousel_item_image);
        carouselItemTitle = view.findViewById(R.id.carousel_item_title);
        carouselItemSubTitle = view.findViewById(R.id.carousel_item_subtitle);
        carouselButtonListview = view.findViewById(R.id.carousel_button_listview);
    }

    int carouselPosition;
    void extractFromBundle() {
        Bundle bundle = getArguments();

        String carouselModel = bundle.getString(BundleConstants.CAROUSEL_ITEM, "");
        carouselPosition = bundle.getInt(BundleConstants.CAROUSEL_ITEM_POSITION);

        if (!carouselModel.isEmpty()) {
            botCarouselModel = gson.fromJson(carouselModel, BotCarouselModel.class);
        }
    }

    void populateView() {
        if (botCarouselModel != null) {
            carouselItemTitle.setText(botCarouselModel.getTitle());
            carouselItemSubTitle.setText(botCarouselModel.getSubtitle());

            Picasso.get().load(botCarouselModel.getImage_url()).into(carouselItemImage);

            if(botCarouselModel.getButtons() != null) {
                BotCarouselItemButtonAdapter botCarouselItemButtonAdapter = new BotCarouselItemButtonAdapter(getActivity());
                carouselButtonListview.setAdapter(botCarouselItemButtonAdapter);
                botCarouselItemButtonAdapter.setBotCarouselButtonModels(botCarouselModel.getButtons());
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}
