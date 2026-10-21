package com.kore.korebot.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;

import com.kore.korebot.R;

import kore.botssdk.fragment.header.BaseHeaderFragment;
import kore.botssdk.models.BrandingModel;
import kore.botssdk.net.SDKConfig;

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
        ImageView ivHeaderClose = view.findViewById(R.id.ivHeaderClose);

        tvBotTitle.setText(ContextCompat.getString(requireContext(), R.string.custom_header));
        ivHeaderClose.setOnClickListener(v -> {
            if (SDKConfig.isAgentCloseConfirmationEnabled() && isHumanAgentConnected()) {
                new AlertDialog.Builder(requireContext())
                        .setMessage(kore.botssdk.R.string.agent_close_confirmation)
                        .setCancelable(false)
                        .setPositiveButton(kore.botssdk.R.string.yes,
                                (dialog, which) -> sendAgentCloseEvent())
                        .setNegativeButton(kore.botssdk.R.string.no,
                                (dialog, which) -> dialog.dismiss())
                        .show();
            } else if (ivHeaderMinimize != null) {
                // Reuse the SDK's normal close/minimize/cancel flow.
                ivHeaderMinimize.performClick();
            }
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
