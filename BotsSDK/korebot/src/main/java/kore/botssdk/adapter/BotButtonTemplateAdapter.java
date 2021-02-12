package kore.botssdk.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import java.util.ArrayList;

import kore.botssdk.R;
import kore.botssdk.application.AppControl;
import kore.botssdk.listener.ComposeFooterInterface;
import kore.botssdk.listener.InvokeGenericWebViewInterface;
import kore.botssdk.models.BotButtonModel;
import kore.botssdk.models.BotResponse;
import kore.botssdk.net.SDKConfiguration;
import kore.botssdk.utils.BundleConstants;

/**
 * Created by Anil Kumar on 12/1/2016.
 */
public class BotButtonTemplateAdapter extends BaseAdapter {
    private ArrayList<BotButtonModel> botButtonModels = new ArrayList<>();
    private LayoutInflater ownLayoutInflater = null;
    private String splashColour, disabledColour, textColor, disabledTextColor;
    private boolean isEnabled;
    private SharedPreferences sharedPreferences;
    private float dp1;
    private ComposeFooterInterface composeFooterInterface;
    private InvokeGenericWebViewInterface invokeGenericWebViewInterface;

    public BotButtonTemplateAdapter(Context context)
    {
        ownLayoutInflater = LayoutInflater.from(context);
        sharedPreferences = context.getSharedPreferences(BotResponse.THEME_NAME, Context.MODE_PRIVATE);

        splashColour = "#" + Integer.toHexString(ContextCompat.getColor(context, R.color.colorPrimary));
        disabledColour = "#" + Integer.toHexString(ContextCompat.getColor(context, R.color.meetingsDisabled));
        textColor = "#" + Integer.toHexString(ContextCompat.getColor(context, R.color.white));
        disabledTextColor = "#" + Integer.toHexString(ContextCompat.getColor(context, R.color.white));

//        splashColour = sharedPreferences.getString(BotResponse.BUTTON_ACTIVE_BG_COLOR, splashColour);
//        disabledColour = sharedPreferences.getString(BotResponse.BUTTON_INACTIVE_BG_COLOR, disabledColour);
//        textColor = sharedPreferences.getString(BotResponse.BUTTON_ACTIVE_TXT_COLOR, textColor);
//        disabledTextColor = sharedPreferences.getString(BotResponse.BUTTON_INACTIVE_TXT_COLOR, disabledTextColor);

        dp1 = AppControl.getInstance().getDimensionUtil().dp1;
    }

    @Override
    public int getCount() {
        if (botButtonModels != null) {
            return botButtonModels.size();
        } else {
            return 0;
        }
    }

    @Override
    public BotButtonModel getItem(int position) {
        return botButtonModels.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = ownLayoutInflater.inflate(R.layout.meeting_slot_button, null);
        }

        if (convertView.getTag() == null) {
            initializeViewHolder(convertView);
        }

        ViewHolder holder = (ViewHolder) convertView.getTag();
        populateView(holder, position);

        return convertView;
    }

    private void populateView(ViewHolder holder, int position) {
        BotButtonModel buttonTemplate = getItem(position);

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

    public static class ViewHolder {
        TextView botItemButton;
    }

    private void initializeViewHolder(View view) {
        ViewHolder viewHolder = new ViewHolder();
        viewHolder.botItemButton = (TextView) view.findViewById(R.id.text_view);

//        ((GradientDrawable) viewHolder.botItemButton.getBackground()).setColor(isEnabled ? Color.parseColor(splashColour) : Color.parseColor(disabledColour));
        ((GradientDrawable) viewHolder.botItemButton.getBackground()).setStroke((int)(2*dp1), isEnabled ? Color.parseColor(splashColour) : Color.parseColor(disabledColour));
        viewHolder.botItemButton.setTextColor(isEnabled ? Color.parseColor(splashColour) : Color.parseColor(disabledColour));
        view.setTag(viewHolder);
    }

    public void setBotButtonModels(ArrayList<BotButtonModel> botButtonModels) {
        this.botButtonModels = botButtonModels;
    }
}