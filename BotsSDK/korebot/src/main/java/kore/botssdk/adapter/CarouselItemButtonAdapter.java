package kore.botssdk.adapter;

import static kore.botssdk.viewUtils.DimensionUtil.dp1;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import kore.botssdk.R;
import kore.botssdk.listener.ComposeFooterInterface;
import kore.botssdk.listener.InvokeGenericWebViewInterface;
import kore.botssdk.models.BotCaourselButtonModel;
import kore.botssdk.net.SDKConfiguration;
import kore.botssdk.utils.BundleConstants;
import kore.botssdk.utils.KaFontUtils;

public class CarouselItemButtonAdapter extends RecyclerView.Adapter<CarouselItemButtonAdapter.ViewHolder> {
    private ArrayList<BotCaourselButtonModel> buttons;
    private boolean isEnabled;
    private ComposeFooterInterface composeFooterInterface;
    private InvokeGenericWebViewInterface invokeGenericWebViewInterface;
    private final LayoutInflater layoutInflater;

    public CarouselItemButtonAdapter(Context context) {
        layoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(layoutInflater.inflate(R.layout.carousel_item_button_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BotCaourselButtonModel carouselButtonModel = getItem(position);
        if (carouselButtonModel == null) return;
        KaFontUtils.applyCustomFont(holder.button.getContext(), holder.itemView);

        GradientDrawable gradientDrawable = (GradientDrawable)holder.button.getBackground().mutate();
        gradientDrawable.setColor(Color.parseColor(SDKConfiguration.BubbleColors.quickReplyColor));
        gradientDrawable.setStroke((int) (1 * dp1), Color.parseColor(SDKConfiguration.BubbleColors.quickReplyColor));
        holder.button.setTextColor(Color.parseColor(SDKConfiguration.BubbleColors.quickReplyTextColor));

        holder.button.setText(carouselButtonModel.getTitle());
        holder.button.setOnClickListener(view -> {
            if (invokeGenericWebViewInterface != null) {
                if (BundleConstants.BUTTON_TYPE_WEB_URL.equalsIgnoreCase(carouselButtonModel.getType())) {
                    invokeGenericWebViewInterface.invokeGenericWebView(carouselButtonModel.getUrl());
                    return;
                } else if (BundleConstants.BUTTON_TYPE_USER_INTENT.equalsIgnoreCase(carouselButtonModel.getType())) {
                    invokeGenericWebViewInterface.handleUserActions(carouselButtonModel.getAction(), carouselButtonModel.getCustomData());
                    return;
                }
            }
            if (isEnabled && composeFooterInterface != null) {
                if (BundleConstants.BUTTON_TYPE_POSTBACK.equalsIgnoreCase(carouselButtonModel.getType())) {
                    String buttonPayload = carouselButtonModel.getPayload();
                    String buttonTitle = carouselButtonModel.getTitle();
                    composeFooterInterface.onSendClick(buttonTitle, buttonPayload, false);
                } else if (BundleConstants.BUTTON_TYPE_POSTBACK_DISP_PAYLOAD.equalsIgnoreCase(carouselButtonModel.getType())) {
                    String buttonPayload = carouselButtonModel.getPayload();
                    composeFooterInterface.onSendClick(buttonPayload, buttonPayload, false);
                } else if (BundleConstants.BUTTON_TYPE_TEXT.equalsIgnoreCase(carouselButtonModel.getType())) {
                    String buttonTitle = carouselButtonModel.getTitle();
                    composeFooterInterface.onSendClick(buttonTitle, false);
                } else {
                    String buttonPayload = carouselButtonModel.getPayload();
                    String buttonTitle = carouselButtonModel.getTitle();
                    composeFooterInterface.onSendClick(buttonTitle, buttonPayload, false);
                }
            }
        });
    }

    private BotCaourselButtonModel getItem(int position) {
        return buttons != null ? buttons.get(position) : null;
    }

    @Override
    public int getItemCount() {
        return buttons.size();
    }

    public void populateData(ArrayList<BotCaourselButtonModel> buttons, boolean isEnabled) {
        this.buttons = buttons;
        this.isEnabled = isEnabled;
        notifyDataSetChanged();
    }

    public void setComposeFooterInterface(ComposeFooterInterface composeFooterInterface) {
        this.composeFooterInterface = composeFooterInterface;
    }

    public void setInvokeGenericWebViewInterface(InvokeGenericWebViewInterface invokeGenericWebViewInterface) {
        this.invokeGenericWebViewInterface = invokeGenericWebViewInterface;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView button;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            button = itemView.findViewById(R.id.bot_carousel_item_button);
        }
    }
}
