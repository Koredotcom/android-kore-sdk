package kore.botssdk.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import java.util.ArrayList;

import kore.botssdk.R;
import kore.botssdk.models.BotButtonModel;
import kore.botssdk.models.BotResponse;
import kore.botssdk.net.SDKConfiguration;

import static kore.botssdk.view.viewUtils.DimensionUtil.dp1;

/**
 * Created by Anil Kumar on 12/1/2016.
 */
public class BotButtonTemplateAdapter extends BaseAdapter {
    private ArrayList<BotButtonModel> botButtonModels = new ArrayList<>();
    private LayoutInflater ownLayoutInflater = null;
    private String splashColour, disabledColour, textColor, disableTextColor;
    private boolean isEnabled;
    private SharedPreferences sharedPreferences;
    private GradientDrawable btnDrawable;
    private Context mContext;

    public BotButtonTemplateAdapter(Context context)
    {
        this.mContext = context;
        ownLayoutInflater = LayoutInflater.from(context);
        sharedPreferences = context.getSharedPreferences(BotResponse.THEME_NAME, Context.MODE_PRIVATE);

        splashColour = "#" + Integer.toHexString(ContextCompat.getColor(context, R.color.splash_color) & 0x00ffffff);
        disabledColour = "#" + Integer.toHexString(ContextCompat.getColor(context, R.color.meetingsDisabled) & 0x00ffffff);
        textColor = "#" + Integer.toHexString(ContextCompat.getColor(context, R.color.white) & 0x00ffffff);
        disableTextColor = "#" + Integer.toHexString(ContextCompat.getColor(context, R.color.white) & 0x00ffffff);

        splashColour = sharedPreferences.getString(BotResponse.BUTTON_ACTIVE_BG_COLOR, splashColour);
        disabledColour = sharedPreferences.getString(BotResponse.BUTTON_INACTIVE_BG_COLOR, disabledColour);
        textColor = sharedPreferences.getString(BotResponse.BUTTON_ACTIVE_TXT_COLOR, textColor);
        disableTextColor = sharedPreferences.getString(BotResponse.BUTTON_INACTIVE_TXT_COLOR, textColor);
        Log.e("Inactive Text color", disableTextColor);
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
//        btnDrawable = (GradientDrawable) mContext.getResources().getDrawable(R.drawable.theme_1_button_bg);
//        btnDrawable.setStroke((int) (2*dp1), Color.parseColor(splashColour));
//        holder.botItemButton.setTextColor(Color.parseColor(textColor));
        holder.botItemButton.setText(buttonTemplate.getTitle());
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
        ((GradientDrawable) viewHolder.botItemButton.getBackground()).setStroke((int) (2*dp1) , isEnabled ? Color.parseColor(splashColour) : Color.parseColor(disabledColour));
        viewHolder.botItemButton.setTextColor(isEnabled ? Color.parseColor(textColor) : Color.parseColor(disableTextColor));
        view.setTag(viewHolder);
    }

    public void setBotButtonModels(ArrayList<BotButtonModel> botButtonModels) {
        this.botButtonModels = botButtonModels;
    }
}