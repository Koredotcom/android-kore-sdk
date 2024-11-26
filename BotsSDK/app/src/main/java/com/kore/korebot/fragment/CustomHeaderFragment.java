package com.kore.korebot.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import kore.botssdk.R;
import kore.botssdk.fragment.header.BaseHeaderFragment;
import kore.botssdk.models.BrandingHeaderModel;
import kore.botssdk.models.BrandingModel;
import kore.botssdk.net.SDKConfiguration;

public class CustomHeaderFragment extends BaseHeaderFragment {
    private View view;

    @Override
    public void setBrandingDetails(BrandingModel brandingModel) {
        this.brandingModel = brandingModel;
        updateUI();
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.bot_header_layout, null);
        updateUI();
        return view;
    }

    private void updateUI() {
        if (view == null) return;
        TextView tvBotTitle = view.findViewById(R.id.tvBotName);
        tvBotTitle.setText("Custom header");
        if (brandingModel != null) {
            if (brandingModel.getWidgetHeaderColor() != null)
                view.setBackgroundColor(Color.parseColor(brandingModel.getWidgetHeaderColor()));
            if (brandingModel.getWidgetTextColor() != null)
                tvBotTitle.setTextColor(Color.parseColor(brandingModel.getWidgetTextColor()));
        }
    }
}