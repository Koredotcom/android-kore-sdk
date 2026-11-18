package com.kore.korebot.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.kore.korebot.R;

import kore.botssdk.fragment.header.BaseHeaderFragment;
import kore.botssdk.models.BrandingModel;

public class CustomHeaderFragment extends BaseHeaderFragment {
    private View view;
    ImageView ivHeaderMinimize;
    TextView tvBotTitle;

    @Override
    public void setBrandingDetails(BrandingModel brandingModel) {
        this.brandingModel = brandingModel;
        updateUI();
    }

    @Override
    public ImageView getMinimize() {
        return ivHeaderMinimize;
    }

    @Override
    public void onAgentConnected(boolean isConnected) {
        if(isConnected)
            tvBotTitle.setText("Agent Connected");
        else
            tvBotTitle.setText("Kore.ai Bot");
    }


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.bot_header_layout, null);
        updateUI();
        return view;
    }

    private void updateUI() {
        if (view == null) return;
        tvBotTitle = view.findViewById(R.id.tvBotName);
        ivHeaderMinimize = view.findViewById(kore.botssdk.R.id.ivHeaderMinimize);
        ImageView ivHeaderClose = view.findViewById(R.id.ivHeaderClose);

        tvBotTitle.setText("Kore.ai Bot");
        ivHeaderClose.setOnClickListener(v -> {
            handleCloseClick(() -> {
                if (ivHeaderMinimize != null) ivHeaderMinimize.performClick();
            });
        });

        if (brandingModel != null) {
            if (brandingModel.getWidgetHeaderColor() != null)
                view.setBackgroundColor(Color.parseColor(brandingModel.getWidgetHeaderColor()));
            if (brandingModel.getWidgetTextColor() != null) {
                tvBotTitle.setTextColor(Color.parseColor(brandingModel.getWidgetTextColor()));
                ivHeaderMinimize.setColorFilter(Color.parseColor(brandingModel.getWidgetTextColor()));
                ivHeaderClose.setColorFilter(Color.parseColor(brandingModel.getWidgetTextColor()));
            }
        }
    }
}
