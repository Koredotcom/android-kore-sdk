package kore.botssdk.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.view.AccessibilityDelegateCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import kore.botssdk.R;
import kore.botssdk.listener.ComposeFooterInterface;
import kore.botssdk.listener.InvokeGenericWebViewInterface;
import kore.botssdk.models.BotButtonModel;
import kore.botssdk.models.BotResponse;
import kore.botssdk.utils.BundleConstants;
import kore.botssdk.utils.StringUtils;

public class ButtonLinkTemplateAdapter extends RecyclerView.Adapter<ButtonLinkTemplateAdapter.ViewHolder> {
    private final ArrayList<BotButtonModel> buttons;
    private final boolean isEnabled;
    private ComposeFooterInterface composeFooterInterface;
    private InvokeGenericWebViewInterface invokeGenericWebViewInterface;
    String textColor;

    public ButtonLinkTemplateAdapter(Context context, ArrayList<BotButtonModel> buttons, boolean isEnabled) {
        this.buttons = buttons;
        this.isEnabled = isEnabled;
        SharedPreferences sharedPreferences = context.getSharedPreferences(BotResponse.THEME_NAME, Context.MODE_PRIVATE);

        int defaultColor = ContextCompat.getColor(context, R.color.white);
        textColor = sharedPreferences.getString(BotResponse.BUTTON_ACTIVE_TXT_COLOR, "#" + Integer.toHexString(defaultColor));
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.bot_button_link_item_cell, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BotButtonModel buttonTemplate = getItem(position);
        if (buttonTemplate == null) return;

        String title = buttonTemplate.getTitle();
        holder.button.setText(title);
        holder.ivDeepLink.setVisibility(View.GONE);

        try {
            holder.button.setTextColor(Color.parseColor(textColor));
        } catch (Exception e) {
            holder.button.setTextColor(Color.parseColor("#2e6fc5"));
        }

        // WCAG Compliance:
        // 1. Set content description on the clickable container
        holder.bot_options_more.setContentDescription(title);
        holder.bot_options_more.setEnabled(isEnabled);
        holder.bot_options_more.setAlpha(isEnabled ? 1.0f : 0.5f);

        // 2. Use AccessibilityDelegate to announce the container as a Button role
        ViewCompat.setAccessibilityDelegate(holder.bot_options_more, new AccessibilityDelegateCompat() {
            @Override
            public void onInitializeAccessibilityNodeInfo(View host, AccessibilityNodeInfoCompat info) {
                super.onInitializeAccessibilityNodeInfo(host, info);
                info.setClassName(Button.class.getName());
                info.setClickable(isEnabled);
            }
        });

        // 3. Singular click listener on the parent container (removes redundant focus points)
        holder.bot_options_more.setOnClickListener(v -> {
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
                    String payload = buttonTemplate.getPayload();
                    composeFooterInterface.onSendClick(title, payload, false);
                }
            }
        });
    }

    private BotButtonModel getItem(int position) {
        return (buttons != null && position >= 0 && position < buttons.size()) ? buttons.get(position) : null;
    }

    @Override
    public int getItemCount() {
        return buttons != null ? buttons.size() : 0;
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
        LinearLayout bot_options_more;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            button = itemView.findViewById(R.id.more_txt_view);
            ivDeepLink = itemView.findViewById(R.id.ivDeepLink);
            bot_options_more = itemView.findViewById(R.id.bot_options_more);

            // WCAG: Group focus on the parent container, prevent sub-views from being focused separately
            button.setImportantForAccessibility(View.IMPORTANT_FOR_ACCESSIBILITY_NO);
            ivDeepLink.setImportantForAccessibility(View.IMPORTANT_FOR_ACCESSIBILITY_NO);
        }
    }
}
