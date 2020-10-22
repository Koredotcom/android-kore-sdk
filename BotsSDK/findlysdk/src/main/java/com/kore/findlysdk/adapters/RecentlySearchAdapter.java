package com.kore.findlysdk.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.kore.findlysdk.R;
import com.kore.findlysdk.models.PopularSearchModel;

import java.util.ArrayList;

public class RecentlySearchAdapter extends BaseAdapter
{
    private Context context;
    private ArrayList<String> arrRecentSearchs;

    public RecentlySearchAdapter(Context context, ArrayList<String> arrRecentSearchs)
    {
        this.context = context;
        this.arrRecentSearchs = arrRecentSearchs;
    }

    @Override
    public int getCount() {
        return arrRecentSearchs.size();
    }

    @Override
    public Object getItem(int i) {
        return arrRecentSearchs.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup)
    {
        RecentItemViewHolder popularItemViewHolder;

        if (view == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            view = layoutInflater.inflate(R.layout.popular_search_findly_item, null);
            popularItemViewHolder = new RecentItemViewHolder();
            popularItemViewHolder.tvPopulatItem = view.findViewById(R.id.tvPopularItem);
            view.setTag(popularItemViewHolder);
        } else {
            popularItemViewHolder = (RecentItemViewHolder) view.getTag();
        }

        String recentlyAsked = (String) getItem(i);
        popularItemViewHolder.tvPopulatItem.setText(recentlyAsked);

        return view;
    }

    private class RecentItemViewHolder {
        TextView tvPopulatItem;
    }
}
