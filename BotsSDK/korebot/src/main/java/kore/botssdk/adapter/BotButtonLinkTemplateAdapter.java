package kore.botssdk.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import java.util.ArrayList;

import kore.botssdk.R;
import kore.botssdk.listener.ComposeFooterInterface;
import kore.botssdk.listener.InvokeGenericWebViewInterface;
import kore.botssdk.listener.RadioListListner;
import kore.botssdk.models.BotButtonModel;
import kore.botssdk.models.BotResponse;
import kore.botssdk.utils.BundleConstants;
import kore.botssdk.utils.StringUtils;

public class BotButtonLinkTemplateAdapter extends BaseAdapter {
    private final ArrayList<BotButtonModel> botButtonModels;
    private String splashColour, disabledColour, textColor, disabledTextColor;
    private boolean isEnabled;
    private ComposeFooterInterface composeFooterInterface;
    private InvokeGenericWebViewInterface invokeGenericWebViewInterface;
    private final int type;
    private RadioListListner radioListListner;
    private final Context context;

    public BotButtonLinkTemplateAdapter(Context context, ArrayList<BotButtonModel> botButtonModels, int type)
    {
        this.context = context;
        this.botButtonModels = botButtonModels;
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
            convertView = View.inflate(context, R.layout.bot_button_link_item_cell, null);
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
        holder.ivDeepLink.setVisibility(View.GONE);

        if (type == 2) {
            holder.botItemButton.setTextColor(Color.parseColor("#ffffff"));
            holder.ivDeepLink.setVisibility(View.VISIBLE);
            holder.ivDeepLink.setImageResource(R.drawable.ic_external_link__1_);

            if (buttonTemplate.isSamePageNavigation()) {
                holder.ivDeepLink.setImageResource(R.drawable.ic_deeplink__1_);
            }
        } else {
            holder.botItemButton.setTextColor(Color.parseColor("#2e6fc5"));
        }

        holder.botItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (composeFooterInterface != null && invokeGenericWebViewInterface != null && isEnabled()) {
                    if (BundleConstants.BUTTON_TYPE_WEB_URL.equalsIgnoreCase(buttonTemplate.getType())) {

                        if(!StringUtils.isNullOrEmpty(buttonTemplate.getUrl()) /*&& checkedPosition == -1*/)
                        {
                            if(radioListListner != null)
                                radioListListner.radioItemClicked(position);

                            if(buttonTemplate.isSamePageNavigation())
                                composeFooterInterface.onDeepLinkClicked(buttonTemplate.getUrl());
                            else
                                invokeGenericWebViewInterface.invokeGenericWebView(buttonTemplate.getUrl());
                        }
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

    public void setRadioListInterface(RadioListListner radioListListner) {
        this.radioListListner = radioListListner;
    }

    public void setCheckedPosition(int checkedPosition) {
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

    public void setLang(String lang)
    {
    }

    public static class ViewHolder {
        TextView botItemButton;
        ImageView ivDeepLink;
    }

    private void initializeViewHolder(View view) {
        ViewHolder viewHolder = new ViewHolder();
        viewHolder.botItemButton = view.findViewById(R.id.more_txt_view);
        viewHolder.ivDeepLink = view.findViewById(R.id.ivDeepLink);
        viewHolder.botItemButton.setTextColor(Color.parseColor("#2e6fc5"));
        view.setTag(viewHolder);
    }
}
