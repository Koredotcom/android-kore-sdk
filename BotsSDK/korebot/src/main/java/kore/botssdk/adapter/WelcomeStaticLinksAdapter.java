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
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;

import java.util.ArrayList;

import kore.botssdk.R;
import kore.botssdk.listener.ComposeFooterInterface;
import kore.botssdk.listener.InvokeGenericWebViewInterface;
import kore.botssdk.models.BrandingQuickStartButtonButtonsModel;

public class WelcomeStaticLinksAdapter extends PagerAdapter {
    private ArrayList<BrandingQuickStartButtonButtonsModel> arrBrandingQuickButtons;
    final Context context;
    ComposeFooterInterface composeFooterInterface;
    InvokeGenericWebViewInterface invokeGenericWebViewInterface;
    String bgColor;

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
