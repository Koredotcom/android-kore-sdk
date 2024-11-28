package kore.botssdk.fragment.header;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import kore.botssdk.R;
import kore.botssdk.models.BrandingHeaderModel;
import kore.botssdk.models.BrandingModel;
import kore.botssdk.net.SDKConfiguration;

public class BotHeaderFragment extends BaseHeaderFragment {
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
        String title = brandingModel != null && brandingModel.getBotName() != null ? brandingModel.getBotName() : SDKConfiguration.Client.bot_name;
        if (brandingModel != null) {
            tvBotTitle.setText(!TextUtils.isEmpty(title) ? title : SDKConfiguration.Client.bot_name);
            if (brandingModel.getWidgetHeaderColor() != null)
                view.setBackgroundColor(Color.parseColor(brandingModel.getWidgetHeaderColor()));
            if (brandingModel.getWidgetTextColor() != null)
                tvBotTitle.setTextColor(Color.parseColor(brandingModel.getWidgetTextColor()));
        }
    }
}