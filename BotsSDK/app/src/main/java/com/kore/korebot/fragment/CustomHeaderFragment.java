package com.kore.korebot.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.kore.korebot.R;

import kore.botssdk.fragment.header.BaseHeaderFragment;
import kore.botssdk.models.BrandingModel;

public class CustomHeaderFragment extends BaseHeaderFragment {
    private View view;
    ImageView ivHeaderMinimize;
    @Override
    public void setBrandingDetails(BrandingModel brandingModel) {
        this.brandingModel = brandingModel;
        updateUI();
    }

    @Override
    public ImageView getMinimize() {
        return ivHeaderMinimize;
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.bot_header_layout, null);
        updateUI();
        return view;
    }

    private void updateUI() {
        if (view == null) return;
        TextView tvBotTitle = view.findViewById(R.id.tvBotName);
        ivHeaderMinimize = view.findViewById(kore.botssdk.R.id.ivHeaderMinimize);

        tvBotTitle.setText(ContextCompat.getString(requireContext(), R.string.custom_header));
        if (brandingModel != null) {
            if (brandingModel.getWidgetHeaderColor() != null)
                view.setBackgroundColor(Color.parseColor(brandingModel.getWidgetHeaderColor()));
            if (brandingModel.getWidgetTextColor() != null)
                tvBotTitle.setTextColor(Color.parseColor(brandingModel.getWidgetTextColor()));
        }
    }
}