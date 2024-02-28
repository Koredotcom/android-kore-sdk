package kore.botssdk.adapter;

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
import kore.botssdk.view.viewUtils.DimensionUtil;

/**
 * Created by Anil Kumar on 12/1/2016.
 */
public class BotButtonTemplateAdapter extends RecyclerView.Adapter<BotButtonTemplateAdapter.ViewHolder> {
    private ArrayList<BotButtonModel> botButtonModels = new ArrayList<>();
    private String splashColour, textColor, disabledTextColor;
    private String disabledColour, type;
    private boolean isEnabled;
    private final float dp1;
    ComposeFooterInterface composeFooterInterface;
    InvokeGenericWebViewInterface invokeGenericWebViewInterface;
    private final Context context;

    public BotButtonTemplateAdapter(Context context, String type)
    {
        this.context = context;
        this.type = type;

        SharedPreferences sharedPreferences = context.getSharedPreferences(BotResponse.THEME_NAME, Context.MODE_PRIVATE);
        splashColour = "#" + Integer.toHexString(ContextCompat.getColor(context, R.color.colorPrimary));
        disabledColour = "#" + Integer.toHexString(ContextCompat.getColor(context, R.color.meetingsDisabled));
        textColor = "#" + Integer.toHexString(ContextCompat.getColor(context, R.color.white));
        disabledTextColor = "#" + Integer.toHexString(ContextCompat.getColor(context, R.color.white));
        splashColour = sharedPreferences.getString(BotResponse.BUTTON_ACTIVE_BG_COLOR, splashColour);
        disabledColour = sharedPreferences.getString(BotResponse.BUTTON_INACTIVE_BG_COLOR, disabledColour);
        textColor = sharedPreferences.getString(BotResponse.BUTTON_ACTIVE_TXT_COLOR, textColor);
        disabledTextColor = sharedPreferences.getString(BotResponse.BUTTON_INACTIVE_TXT_COLOR, disabledTextColor);
        dp1 = DimensionUtil.dp1;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View convertView;

        if(type.equalsIgnoreCase(BotResponse.TEMPLATE_TYPE_LIST))
            convertView = LayoutInflater.from(context).inflate(R.layout.button_view_cell_full, parent, false);
        else
            convertView = LayoutInflater.from(context).inflate(R.layout.meeting_slot_button, parent, false);
        return new ViewHolder(convertView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BotButtonModel buttonTemplate = botButtonModels.get(position);

        ((GradientDrawable) holder.botItemButton.getBackground()).setColor(isEnabled ? Color.parseColor(splashColour) : Color.parseColor(disabledColour));
        ((GradientDrawable) holder.botItemButton.getBackground()).setStroke((int)(2*dp1), isEnabled ? Color.parseColor(splashColour) : Color.parseColor(disabledColour));
        holder.botItemButton.setTextColor(isEnabled ? Color.parseColor(textColor) : Color.parseColor(disabledTextColor));

        holder.botItemButton.setText(buttonTemplate.getTitle());

        holder.botItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (composeFooterInterface != null && invokeGenericWebViewInterface != null && isEnabled()) {

                    if (BundleConstants.BUTTON_TYPE_WEB_URL.equalsIgnoreCase(buttonTemplate.getType())) {
                        invokeGenericWebViewInterface.invokeGenericWebView(buttonTemplate.getUrl());
                    }
                    else if(BundleConstants.BUTTON_TYPE_URL.equalsIgnoreCase(buttonTemplate.getType())) {
                        invokeGenericWebViewInterface.invokeGenericWebView(buttonTemplate.getUrl());
                    }else if(BundleConstants.BUTTON_TYPE_USER_INTENT.equalsIgnoreCase(buttonTemplate.getType())){
                        setEnabled(false);
                        invokeGenericWebViewInterface.handleUserActions(buttonTemplate.getAction(),buttonTemplate.getCustomData());
                    }else{
                        setEnabled(false);
                        String title = buttonTemplate.getTitle();
                        String payload = buttonTemplate.getPayload();
                        composeFooterInterface.onSendClick(title, payload,false);
                    }
                }
            }
        });
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        if (botButtonModels != null) {
            return botButtonModels.size();
        } else {
            return 0;
        }
    }

    public void setComposeFooterInterface(ComposeFooterInterface composeFooterInterface) {
        this.composeFooterInterface = composeFooterInterface;
    }

    public void setInvokeGenericWebViewInterface(InvokeGenericWebViewInterface invokeGenericWebViewInterface) {
        this.invokeGenericWebViewInterface = invokeGenericWebViewInterface;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView botItemButton;

        public ViewHolder(View view)
        {
            super(view);
            botItemButton = view.findViewById(R.id.text_view);
        }
    }

    public void setBotButtonModels(ArrayList<BotButtonModel> botButtonModels) {
        this.botButtonModels = botButtonModels;
    }
}