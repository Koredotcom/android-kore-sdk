package kore.botssdk.adapter;

import static android.content.Context.MODE_PRIVATE;
import static kore.botssdk.view.viewUtils.DimensionUtil.dp1;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
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

public class ButtonTemplateAdapter extends RecyclerView.Adapter<ButtonTemplateAdapter.BotButtonViewHolder> {
    private final Context context;
    private final ArrayList<BotButtonModel> buttons;
    private final boolean isLastItem;
    private String buttonBgColor;
    private String activeTextColor;
    private String invertBgColor;
    private String invertTextColor;
    private final boolean isFullWidth;
    private final boolean isStackedButtons;
    private final String variation;

    private ComposeFooterInterface composeFooterInterface;
    private InvokeGenericWebViewInterface invokeGenericWebViewInterface;

    public ButtonTemplateAdapter(Context context, ArrayList<BotButtonModel> buttons, boolean isLastItem, boolean isFullWidth, boolean isStackedButtons, String variation) {
        this.context = context;
        this.buttons = buttons;
        this.isLastItem = isLastItem;
        this.isFullWidth = isFullWidth;
        this.isStackedButtons = isStackedButtons;
        this.variation = variation;

        SharedPreferences sharedPreferences = context.getSharedPreferences(BotResponse.THEME_NAME, MODE_PRIVATE);

        buttonBgColor = "#efeffc";
        activeTextColor = "#" + Integer.toHexString(ContextCompat.getColor(context, R.color.white));
        invertBgColor = "#3F51B5";
        invertTextColor = activeTextColor;

        buttonBgColor = sharedPreferences.getString(BotResponse.BUBBLE_LEFT_BG_COLOR, buttonBgColor);
        activeTextColor = sharedPreferences.getString(BotResponse.BUTTON_ACTIVE_TXT_COLOR, activeTextColor);
        invertBgColor = sharedPreferences.getString(BotResponse.BUBBLE_RIGHT_BG_COLOR, invertBgColor);
        invertTextColor = sharedPreferences.getString(BotResponse.BUBBLE_RIGHT_TEXT_COLOR, invertTextColor);
    }

    @NonNull
    @Override
    public ButtonTemplateAdapter.BotButtonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View convertView = LayoutInflater.from(context).inflate(R.layout.meeting_slot_button, parent, false);
        return new ButtonTemplateAdapter.BotButtonViewHolder(convertView);
    }

    @Override
    public void onBindViewHolder(ButtonTemplateAdapter.BotButtonViewHolder holder, int position) {
        BotButtonModel buttonMap = buttons.get(position);
        holder.buttonTitle.setText(buttonMap.getTitle());

        holder.rootLayout.setOnClickListener(v -> {
            try {
                String type = buttonMap.getType();
                if (invokeGenericWebViewInterface != null && (BundleConstants.BUTTON_TYPE_USER_INTENT.equals(type) ||
                        BundleConstants.BUTTON_TYPE_URL.equals(type) ||
                        BundleConstants.BUTTON_TYPE_WEB_URL.equals(type))) {
                    if (!StringUtils.isNullOrEmpty(buttonMap.getUrl())) {
                        invokeGenericWebViewInterface.invokeGenericWebView(buttonMap.getUrl());
                    }
                } else if (isLastItem && composeFooterInterface != null) {
                    if (!StringUtils.isNullOrEmpty(buttonMap.getPayload())) {
                        composeFooterInterface.onSendClick(buttonMap.getTitle(), buttonMap.getPayload(), false);
                    } else {
                        composeFooterInterface.onSendClick(buttonMap.getTitle(), false);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        GradientDrawable bgDrawable = (GradientDrawable) holder.buttonTitle.getBackground();
        bgDrawable.setCornerRadius(4 * dp1);

        if (isFullWidth || isStackedButtons || BotResponse.PLAIN.equals(variation)) {
            bgDrawable.setStroke((int) (1 * dp1), Color.parseColor(buttonBgColor));
            bgDrawable.setColor(Color.WHITE);
            holder.buttonTitle.setTextColor(Color.parseColor(activeTextColor));
        } else if (BotResponse.BACKGROUND_INVERTED.equals(variation)) {
            int color = Color.parseColor(invertBgColor);
            bgDrawable.setColor(color);
            bgDrawable.setStroke((int) (1 * dp1), color);
            holder.buttonTitle.setTextColor(Color.parseColor(invertTextColor));
        } else if (BotResponse.TEXT_INVERTED.equals(variation)) {
            int color = Color.parseColor(buttonBgColor);
            holder.buttonTitle.setTextColor(Color.parseColor(invertBgColor));
            bgDrawable.setColor(color);
            bgDrawable.setStroke((int) (1 * dp1), color);
        } else if (BotResponse.DIGITAL_FORM.equals(variation)) {
            int color = Color.TRANSPARENT;
            holder.buttonTitle.setTextColor(Color.parseColor(invertBgColor));
            bgDrawable.setColor(color);
            bgDrawable.setStroke((int) (1 * dp1), color);
        } else {
            int color = Color.parseColor(buttonBgColor);
            holder.buttonTitle.setTextColor(Color.parseColor(activeTextColor));
            bgDrawable.setColor(color);
            bgDrawable.setStroke((int) (1 * dp1), color);
        }

        if (isFullWidth) {
            holder.rootLayout.getLayoutParams().width = LinearLayout.LayoutParams.MATCH_PARENT;
            holder.buttonTitle.getLayoutParams().width = LinearLayout.LayoutParams.MATCH_PARENT;
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
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

    public static class BotButtonViewHolder extends RecyclerView.ViewHolder {
        TextView buttonTitle;
        LinearLayout rootLayout;

        public BotButtonViewHolder(View view) {
            super(view);
            buttonTitle = view.findViewById(R.id.button_title);
            rootLayout = view.findViewById(R.id.root_layout);
        }
    }
}