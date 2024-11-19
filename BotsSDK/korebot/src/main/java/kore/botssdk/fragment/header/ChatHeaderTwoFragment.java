package kore.botssdk.fragment.header;

import static kore.botssdk.view.viewUtils.DimensionUtil.dp1;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;

import kore.botssdk.R;
import kore.botssdk.models.BotResponse;
import kore.botssdk.models.BrandingHeaderButtonsModel;
import kore.botssdk.models.BrandingHeaderModel;
import kore.botssdk.models.BrandingQuickStartButtonActionModel;
import kore.botssdk.net.SDKConfiguration;
import kore.botssdk.utils.BundleConstants;
import kore.botssdk.utils.BundleUtils;

public class ChatHeaderTwoFragment extends BaseHeaderFragment {
    private BrandingHeaderModel brandingModel;

    @Override
    public void setBrandingDetails(BrandingHeaderModel brandingModel) {
        this.brandingModel = brandingModel;
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bot_header_2, null);
        TextView tvBotTitle = view.findViewById(R.id.tvBotTitle);
        TextView tvBotDesc = view.findViewById(R.id.tvBotDesc);
        ImageView ivBotAvatar = view.findViewById(R.id.ivBotAvatar);
        LinearLayout llBotAvatar = view.findViewById(R.id.llBotAvatar);
        ImageView ivBotHelp = view.findViewById(R.id.ivBotHelp);
        ImageView ivBotSupport = view.findViewById(R.id.ivBotSupport);
        ImageView ivBotClose = view.findViewById(R.id.ivBotClose);
        ImageView ivBotArrowBack = view.findViewById(R.id.ivBotArrowBack);

        if (brandingModel != null) {
            String title = brandingModel.getTitle() != null ? brandingModel.getTitle().getName() : null;
            tvBotTitle.setText(!TextUtils.isEmpty(title) ? title : SDKConfiguration.Client.bot_name);
            tvBotDesc.setText(brandingModel.getSub_title() != null ? brandingModel.getSub_title().getName() : null);
            view.setBackgroundColor(Color.parseColor(brandingModel.getBg_color()));

            if (brandingModel.getIcon() != null) {
                if (BundleUtils.CUSTOM.equals(brandingModel.getIcon().getType())) {
                    llBotAvatar.setBackgroundResource(0);
                    Glide.with(requireActivity())
                            .load(brandingModel.getIcon().getIcon_url())
                            .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                            .into(ivBotAvatar)
                            .onLoadFailed(ResourcesCompat.getDrawable(getResources(), R.mipmap.ic_launcher, requireContext().getTheme()));

                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams((int) (40 * dp1), (int) (40 * dp1));
                    ivBotAvatar.setLayoutParams(layoutParams);
                } else {
                    llBotAvatar.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(brandingModel.getBg_color())));

                    switch (brandingModel.getIcon().getIcon_url()) {
                        case BotResponse.ICON_1:
                            ivBotAvatar.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_icon_1, requireContext().getTheme()));
                            break;
                        case BotResponse.ICON_2:
                            ivBotAvatar.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_icon_2, requireContext().getTheme()));
                            break;
                        case BotResponse.ICON_3:
                            ivBotAvatar.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_icon_3, requireContext().getTheme()));
                            break;
                        case BotResponse.ICON_4:
                            ivBotAvatar.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_icon_4, requireContext().getTheme()));
                            break;
                    }
                }
            }

            if (brandingModel.getButtons() != null) {
                BrandingHeaderButtonsModel buttons = brandingModel.getButtons();
                ColorStateList bgColorTint = ColorStateList.valueOf(Color.parseColor(brandingModel.getIcons_color()));
                ivBotArrowBack.setBackgroundTintList(bgColorTint);

                ivBotHelp.setVisibility(buttons.getHelp() != null && buttons.getHelp().isShow() ? View.VISIBLE : View.GONE);
                ivBotSupport.setVisibility(buttons.getLive_agent() != null && buttons.getLive_agent().isShow() ? View.VISIBLE : View.GONE);
                ivBotClose.setVisibility(buttons.getClose() != null && buttons.getClose().isShow() ? View.VISIBLE : View.GONE);

                ivBotHelp.setBackgroundTintList(bgColorTint);
                ivBotSupport.setBackgroundTintList(bgColorTint);
                ivBotClose.setBackgroundTintList(bgColorTint);

                if (buttons.getHelp() != null) {
                    ivBotHelp.setOnClickListener(v -> onClick(buttons.getHelp().getAction()));
                }
                if (buttons.getLive_agent() != null) {
                    ivBotSupport.setOnClickListener(v -> onClick(buttons.getLive_agent().getAction()));
                }
                if (buttons.getClose() != null) {
                    ivBotClose.setOnClickListener(v -> onClick(buttons.getClose().getAction()));
                }
            }
        }

        ivBotArrowBack.setOnClickListener(v -> requireActivity().getOnBackPressedDispatcher().onBackPressed());
        ivBotClose.setOnClickListener(v -> requireActivity().getOnBackPressedDispatcher().onBackPressed());

        return view;
    }

    private void onClick(BrandingQuickStartButtonActionModel actionModel) {
        if (actionModel != null) {
            switch (actionModel.getType()) {
                case BundleConstants.BUTTON_TYPE_URL:
                case BundleConstants.BUTTON_TYPE_WEB_URL:
                    if (actionModel.getValue() != null) {
                        if (invokeGenericWebViewInterface != null) {
                            invokeGenericWebViewInterface.invokeGenericWebView(actionModel.getValue());
                        }
                    }
                    break;
                default:
                    String actionValue = actionModel.getValue() != null ? actionModel.getValue() : actionModel.getTitle();
                    if (actionValue != null && composeFooterInterface != null) {
                        composeFooterInterface.onSendClick(actionModel.getValue(), false);
                    }
                    break;
            }
        }
    }
}