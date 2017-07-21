package kore.botssdk.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import kore.botssdk.R;
import kore.botssdk.models.BotButtonModel;

/**
 * Created by Anil Kumar on 12/1/2016.
 */
public class BotButtonTypeAdapter extends BaseAdapter {
    private ArrayList<BotButtonModel> botButtonModels = new ArrayList<>();
    private LayoutInflater ownLayoutInflater = null;

    public BotButtonTypeAdapter(Context context) {
        ownLayoutInflater = LayoutInflater.from(context);
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
        convertView = ownLayoutInflater.inflate(R.layout.bot_carousel_item_button_layout, null);
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

    public static class ViewHolder {
        public TextView botItemButton;
    }

    void initializeViewHolder(View view) {
        ViewHolder viewHolder = new ViewHolder();
        viewHolder.botItemButton = (TextView) view.findViewById(R.id.bot_carousel_item_button);

        view.setTag(viewHolder);
    }

    public void setBotButtonModels(ArrayList<BotButtonModel> botButtonModels) {
        this.botButtonModels = botButtonModels;
    }
}