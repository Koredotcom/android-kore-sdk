package kore.botssdk.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import kore.botssdk.R;
import kore.botssdk.models.BotButtonModel;
import kore.botssdk.net.SDKConfiguration;

/**
 * Created by Anil Kumar on 12/1/2016.
 */
public class BotButtonTemplateAdapter extends BaseAdapter {
    private ArrayList<BotButtonModel> botButtonModels = new ArrayList<>();
    private LayoutInflater ownLayoutInflater = null;
    private int splashColor;
    private int disabledColor;
    private boolean isEnabled;
    public BotButtonTemplateAdapter(Context context) {
        ownLayoutInflater = LayoutInflater.from(context);
        splashColor = context.getResources().getColor(R.color.splash_color);
        disabledColor = context.getResources().getColor(R.color.meetingsDisabled);
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
        ((GradientDrawable) viewHolder.botItemButton.getBackground()).setColor(isEnabled ? splashColor : disabledColor);
        view.setTag(viewHolder);
    }

    public void setBotButtonModels(ArrayList<BotButtonModel> botButtonModels) {
        this.botButtonModels = botButtonModels;
    }
}