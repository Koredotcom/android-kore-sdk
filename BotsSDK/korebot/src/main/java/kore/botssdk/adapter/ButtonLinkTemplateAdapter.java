package kore.botssdk.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import kore.botssdk.R;
import kore.botssdk.listener.ComposeFooterInterface;
import kore.botssdk.listener.InvokeGenericWebViewInterface;
import kore.botssdk.models.BotButtonModel;
import kore.botssdk.utils.BundleConstants;
import kore.botssdk.utils.StringUtils;

public class ButtonLinkTemplateAdapter extends RecyclerView.Adapter<ButtonLinkTemplateAdapter.ViewHolder> {
    private final ArrayList<BotButtonModel> buttons;
    private final boolean isEnabled;
    private ComposeFooterInterface composeFooterInterface;
    private InvokeGenericWebViewInterface invokeGenericWebViewInterface;

    public ButtonLinkTemplateAdapter(ArrayList<BotButtonModel> buttons, boolean isEnabled) {
        this.buttons = buttons;
        this.isEnabled = isEnabled;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.meeting_slot_button, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BotButtonModel buttonTemplate = getItem(position);
        if (buttonTemplate == null) return;
        holder.button.setText(buttonTemplate.getTitle());
        holder.ivDeepLink.setVisibility(View.GONE);

        holder.button.setTextColor(Color.parseColor("#2e6fc5"));

        holder.button.setOnClickListener(v -> {
            if (composeFooterInterface != null && invokeGenericWebViewInterface != null && isEnabled) {
                if (BundleConstants.BUTTON_TYPE_WEB_URL.equalsIgnoreCase(buttonTemplate.getType())) {

                    if (!StringUtils.isNullOrEmpty(buttonTemplate.getUrl())) {
                        if (buttonTemplate.isSamePageNavigation())
                            composeFooterInterface.onDeepLinkClicked(buttonTemplate.getUrl());
                        else
                            invokeGenericWebViewInterface.invokeGenericWebView(buttonTemplate.getUrl());
                    }
                } else if (BundleConstants.BUTTON_TYPE_URL.equalsIgnoreCase(buttonTemplate.getType())) {
                    invokeGenericWebViewInterface.invokeGenericWebView(buttonTemplate.getUrl());
                } else if (BundleConstants.BUTTON_TYPE_USER_INTENT.equalsIgnoreCase(buttonTemplate.getType())) {
                    invokeGenericWebViewInterface.handleUserActions(buttonTemplate.getAction(), buttonTemplate.getCustomData());
                } else {
                    String title = buttonTemplate.getTitle();
                    String payload = buttonTemplate.getPayload();
                    composeFooterInterface.onSendClick(title, payload, false);
                }
            }
        });
    }

    private BotButtonModel getItem(int position) {
        return buttons != null ? buttons.get(position) : null;
    }

    @Override
    public int getItemCount() {
        return buttons.size();
    }

    public void setComposeFooterInterface(ComposeFooterInterface composeFooterInterface) {
        this.composeFooterInterface = composeFooterInterface;
    }

    public void setInvokeGenericWebViewInterface(InvokeGenericWebViewInterface invokeGenericWebViewInterface) {
        this.invokeGenericWebViewInterface = invokeGenericWebViewInterface;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView button;
        ImageView ivDeepLink;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            button = itemView.findViewById(R.id.more_txt_view);
            ivDeepLink = itemView.findViewById(R.id.ivDeepLink);
            button.setTextColor(Color.parseColor("#2e6fc5"));
        }
    }
}
