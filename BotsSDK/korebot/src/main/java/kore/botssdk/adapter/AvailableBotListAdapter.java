package kore.botssdk.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.zip.Inflater;

import kore.botssdk.R;
import kore.botssdk.models.MarketStreams;
import kore.botssdk.net.MarketStreamList;

/**
 * Created by Pradeep Mahato on 06-Jun-16.
 * Copyright (c) 2014 Kore Inc. All rights reserved.
 */
public class AvailableBotListAdapter extends BaseAdapter {

    MarketStreamList marketStreamList;
    Context context;

    public AvailableBotListAdapter(Context context, MarketStreamList marketStreamList) {
        this.marketStreamList = marketStreamList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return marketStreamList.size();
    }

    @Override
    public MarketStreams getItem(int position) {
        return marketStreamList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.available_bot_list_item_layout, null);
            initialiazeViewHolder(convertView);
        }

        ViewHolder viewHolder = (ViewHolder) convertView.getTag();

        MarketStreams marketStreams = getItem(position);

        Picasso.with(context).load(marketStreams.getIcon()).into(viewHolder.availableBotItemsImageView);
        viewHolder.availableBotItemsText.setText(marketStreams.getName());

        return convertView;
    }

    public void setMarketStreamList(MarketStreamList marketStreamList) {
        this.marketStreamList = marketStreamList;
    }

    private void initialiazeViewHolder(View view) {
        ViewHolder viewHolder = new ViewHolder();

        viewHolder.availableBotItemsImageView = (ImageView) view.findViewById(R.id.availableBotItemsImageView);
        viewHolder.availableBotItemsText = (TextView) view.findViewById(R.id.availableBotItemsText);

        view.setTag(viewHolder);
    }

    public static class ViewHolder {
        ImageView availableBotItemsImageView;
        TextView availableBotItemsText;
    }

}
