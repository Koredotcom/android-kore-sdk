package kore.botssdk.adapter;

import static kore.botssdk.view.viewUtils.DimensionUtil.dp1;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;

import java.util.ArrayList;

import kore.botssdk.R;
import kore.botssdk.fileupload.utils.StringUtils;
import kore.botssdk.listener.ComposeFooterInterface;
import kore.botssdk.listener.InvokeGenericWebViewInterface;
import kore.botssdk.models.BotResponse;
import kore.botssdk.models.BrandingQuickStartButtonActionModel;
import kore.botssdk.models.BrandingQuickStartButtonButtonsModel;
import kore.botssdk.utils.BundleConstants;

public class WelcomeStaticLinksAdapter extends PagerAdapter {
    private final ArrayList<BrandingQuickStartButtonButtonsModel> arrBrandingQuickButtons;
    private final Context context;
    private ComposeFooterInterface composeFooterInterface;
    private InvokeGenericWebViewInterface invokeGenericWebViewInterface;
    private final String bgColor;
    private DialogFragment dialogFragment;

    public WelcomeStaticLinksAdapter(@NonNull Context context, @NonNull ArrayList<BrandingQuickStartButtonButtonsModel> quickReplyTemplateArrayList, String bgColor) {
        this.context = context;
        this.arrBrandingQuickButtons = quickReplyTemplateArrayList;
        this.bgColor = bgColor;
    }

    public void setComposeFooterInterface(@NonNull ComposeFooterInterface composeFooterInterface) {
        this.composeFooterInterface = composeFooterInterface;
    }

    public void setInvokeGenericWebViewInterface(@NonNull InvokeGenericWebViewInterface invokeGenericWebViewInterface) {
        this.invokeGenericWebViewInterface = invokeGenericWebViewInterface;
    }

    public void setDialogFragment(DialogFragment dialogFragment) {
        this.dialogFragment = dialogFragment;
    }

    @Override
    public int getCount() {
        return arrBrandingQuickButtons.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        final View carouselItemLayout = LayoutInflater.from(context).inflate(R.layout.welcome_static_links, container, false);
        StaticViewHolder staticViewHolder = new StaticViewHolder(carouselItemLayout, bgColor);
        BrandingQuickStartButtonButtonsModel quickReplyTemplate = arrBrandingQuickButtons.get(position);
        staticViewHolder.link_title.setText(quickReplyTemplate.getTitle());
        staticViewHolder.link_desc.setText(quickReplyTemplate.getDescription());
        container.addView(carouselItemLayout);

        staticViewHolder.itemView.setOnClickListener((v) -> {
            BrandingQuickStartButtonActionModel action = quickReplyTemplate.getAction();
            if (!StringUtils.isNullOrEmpty(action.getValue())) {
                if (action.getValue() == null) return;
                if (composeFooterInterface != null && BundleConstants.BUTTON_TYPE_POSTBACK.equals(action.getType())) {
                    composeFooterInterface.onSendClick(quickReplyTemplate.getTitle(), action.getValue(), false);
                    if (dialogFragment != null) dialogFragment.dismiss();
                } else if (invokeGenericWebViewInterface != null && BundleConstants.BUTTON_TYPE_USER_INTENT.equals(action.getType())) {
                    invokeGenericWebViewInterface.invokeGenericWebView(action.getValue());
                } else if (composeFooterInterface != null && BundleConstants.BUTTON_TYPE_TEXT.equals(action.getType())) {
                    composeFooterInterface.onSendClick(quickReplyTemplate.getTitle(), action.getValue(), false);
                    if (dialogFragment != null) dialogFragment.dismiss();
                } else if (invokeGenericWebViewInterface != null && BundleConstants.BUTTON_TYPE_WEB_URL.equals(action.getType()) || BundleConstants.BUTTON_TYPE_URL.equals(action.getType())) {
                    invokeGenericWebViewInterface.invokeGenericWebView(action.getValue());
                } else if (composeFooterInterface != null) {
                    composeFooterInterface.onSendClick(quickReplyTemplate.getTitle(), action.getValue(), false);
                    if (dialogFragment != null) dialogFragment.dismiss();
                }
            }
        });

        return carouselItemLayout;
    }

    public static class StaticViewHolder extends RecyclerView.ViewHolder {
        final TextView link_title;
        final TextView link_desc;
        RelativeLayout quick_reply_view;

        public StaticViewHolder(@NonNull View itemView, String bgColor) {
            super(itemView);
            link_title = itemView.findViewById(R.id.link_title);
            link_desc = itemView.findViewById(R.id.link_desc);
            quick_reply_view = itemView.findViewById(R.id.quick_reply_view);

            GradientDrawable gradientDrawable = (GradientDrawable) quick_reply_view.getBackground();
            gradientDrawable.setStroke((int) (1 * dp1), Color.parseColor(bgColor));
        }
    }
}
