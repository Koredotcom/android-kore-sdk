package kore.botssdk.adapter;

import static kore.botssdk.viewUtils.DimensionUtil.dp1;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class ButtonTemplateAdapter extends RecyclerView.Adapter<ButtonTemplateAdapter.ViewHolder> {
    private ArrayList<BotButtonModel> buttons;
    private String splashColour;
    String textColor;
    private boolean isEnabled;
    private ComposeFooterInterface composeFooterInterface;
    private InvokeGenericWebViewInterface invokeGenericWebViewInterface;
    private final LayoutInflater layoutInflater;

    public ButtonTemplateAdapter(Context context) {
        layoutInflater = LayoutInflater.from(context);
        SharedPreferences sharedPreferences = context.getSharedPreferences(BotResponse.THEME_NAME, Context.MODE_PRIVATE);

        splashColour = "#" + Integer.toHexString(ContextCompat.getColor(context, R.color.colorPrimary));
        textColor = "#" + Integer.toHexString(ContextCompat.getColor(context, R.color.white));

        splashColour = sharedPreferences.getString(BotResponse.BUTTON_ACTIVE_BG_COLOR, splashColour);
        textColor = sharedPreferences.getString(BotResponse.BUTTON_ACTIVE_TXT_COLOR, textColor);
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
        ((GradientDrawable) holder.button.getBackground()).setStroke((int) (2 * dp1), Color.parseColor(splashColour));
        ((GradientDrawable) holder.button.getBackground()).setColor(Color.parseColor(splashColour));
        holder.button.setTextColor(Color.parseColor(textColor));
        holder.button.setText(buttonTemplate.getTitle());

        holder.button.setOnClickListener(v -> {
            if (composeFooterInterface != null && invokeGenericWebViewInterface != null && isEnabled) {
                if (BundleConstants.BUTTON_TYPE_WEB_URL.equalsIgnoreCase(buttonTemplate.getType())) {
                    invokeGenericWebViewInterface.invokeGenericWebView(buttonTemplate.getUrl());
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

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            button = itemView.findViewById(R.id.text_view);
        }
    }
}