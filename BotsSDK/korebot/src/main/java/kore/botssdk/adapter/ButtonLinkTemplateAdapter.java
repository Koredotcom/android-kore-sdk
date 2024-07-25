package kore.botssdk.adapter;

import static kore.botssdk.view.viewUtils.DimensionUtil.dp1;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
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
    private ArrayList<BotButtonModel> buttons;
    private String splashColour, textColor, disabledTextColor;
    private String disabledColour;
    private boolean isEnabled;
    private ComposeFooterInterface composeFooterInterface;
    private InvokeGenericWebViewInterface invokeGenericWebViewInterface;
    private final LayoutInflater layoutInflater;

    public ButtonLinkTemplateAdapter(Context context) {
        layoutInflater = LayoutInflater.from(context);
        SharedPreferences sharedPreferences = context.getSharedPreferences(BotResponse.THEME_NAME, Context.MODE_PRIVATE);

        splashColour = "#" + Integer.toHexString(ContextCompat.getColor(context, R.color.colorPrimary));
        disabledColour = "#" + Integer.toHexString(ContextCompat.getColor(context, R.color.meetingsDisabled));
        textColor = "#" + Integer.toHexString(ContextCompat.getColor(context, R.color.white));
        disabledTextColor = "#" + Integer.toHexString(ContextCompat.getColor(context, R.color.white));

        splashColour = sharedPreferences.getString(BotResponse.BUTTON_ACTIVE_BG_COLOR, splashColour);
        disabledColour = sharedPreferences.getString(BotResponse.BUTTON_INACTIVE_BG_COLOR, disabledColour);
        textColor = sharedPreferences.getString(BotResponse.BUTTON_ACTIVE_TXT_COLOR, textColor);
        disabledTextColor = sharedPreferences.getString(BotResponse.BUTTON_INACTIVE_TXT_COLOR, disabledTextColor);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(layoutInflater.inflate(R.layout.meeting_slot_button, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BotButtonModel buttonTemplate = getItem(position);
        if (buttonTemplate == null) return;
        ((GradientDrawable) holder.button.getBackground()).setStroke((int) (2 * dp1), isEnabled ? Color.parseColor(splashColour) : Color.parseColor(disabledColour));
        holder.button.setText(buttonTemplate.getTitle());

        holder.button.setOnClickListener(v -> {
            if (BundleConstants.BUTTON_TYPE_WEB_URL.equalsIgnoreCase(buttonTemplate.getType())) {

                if (!StringUtils.isNullOrEmpty(buttonTemplate.getUrl())) {
//                        if(radioListListner != null)
//                            radioListListner.radioItemClicked(position);

                    if (buttonTemplate.isSamePageNavigation())
                        composeFooterInterface.onDeepLinkClicked(buttonTemplate.getUrl());
                    else
                        invokeGenericWebViewInterface.invokeGenericWebView(buttonTemplate.getUrl());
                }
            } else if (BundleConstants.BUTTON_TYPE_URL.equalsIgnoreCase(buttonTemplate.getType())) {
                invokeGenericWebViewInterface.invokeGenericWebView(buttonTemplate.getUrl());
            } else if (isEnabled && BundleConstants.BUTTON_TYPE_USER_INTENT.equalsIgnoreCase(buttonTemplate.getType())) {
                invokeGenericWebViewInterface.handleUserActions(buttonTemplate.getAction(), buttonTemplate.getCustomData());
            } else if (isEnabled) {
                String title = buttonTemplate.getTitle();
                String payload = buttonTemplate.getPayload();
                composeFooterInterface.onSendClick(title, payload, false);
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

    public void populateData(ArrayList<BotButtonModel> buttons, boolean isEnabled) {
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
        ImageView ivDeepLink;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            button = itemView.findViewById(R.id.more_txt_view);
            ivDeepLink = itemView.findViewById(R.id.ivDeepLink);
            button.setTextColor(Color.parseColor("#2e6fc5"));
        }
    }
}
