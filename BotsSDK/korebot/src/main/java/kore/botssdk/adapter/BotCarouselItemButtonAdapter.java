package kore.botssdk.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

import java.util.ArrayList;

import kore.botssdk.R;
import kore.botssdk.models.BotCaourselButtonModel;
import kore.botssdk.utils.KaFontUtils;

/**
 * Created by Pradeep Mahato on 14/7/17.
 * Copyright (c) 2014 Kore Inc. All rights reserved.
 */
public class BotCarouselItemButtonAdapter extends BaseAdapter {

    ArrayList<BotCaourselButtonModel> botCaourselButtonModels = new ArrayList<>();
    Context context;
    LayoutInflater ownLayoutInflater;

    public BotCarouselItemButtonAdapter(Context context) {
        this.context = context;
        ownLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return botCaourselButtonModels != null ? botCaourselButtonModels.size() : 0;
    }

    @Override
    public BotCaourselButtonModel getItem(int position) {
        return botCaourselButtonModels.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void setBotCaourselButtonModels(ArrayList<BotCaourselButtonModel> botCaourselButtonModels) {
        this.botCaourselButtonModels = botCaourselButtonModels;
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = ownLayoutInflater.inflate(R.layout.bot_carousel_item_button_layout, null);
        KaFontUtils.applyCustomFont(context,convertView);
        if (convertView.getTag() == null) {
            initializeViewHolder(convertView);
        }

        ViewHolder holder = (ViewHolder) convertView.getTag();
        populateView(holder, position);

        return convertView;
    }

    private void populateView(ViewHolder holder, int position) {
        BotCaourselButtonModel botCaourselButtonModel = getItem(position);
        holder.botCarouselItemButton.setText(botCaourselButtonModel.getTitle());
    }

    public static class ViewHolder {
        Button botCarouselItemButton;
    }

    /**
     * View Holder Initialization
     */
    private View initializeViewHolder(View view) {

        ViewHolder holder = new ViewHolder();

        holder.botCarouselItemButton = (Button) view.findViewById(R.id.bot_carousel_item_button);
        view.setTag(holder);

        return view;

    }
}
