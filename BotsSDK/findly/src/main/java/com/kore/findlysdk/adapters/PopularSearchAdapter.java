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

public class PopularSearchAdapter extends BaseAdapter
{
    private Context mContext;
    private ArrayList<PopularSearchModel> arrPopularSearchModels;

    public PopularSearchAdapter(Context context, ArrayList<PopularSearchModel> arrPopularSearchModels)
    {
        this.mContext = context;
        this.arrPopularSearchModels = arrPopularSearchModels;
    }

    @Override
    public int getCount() {
        return arrPopularSearchModels.size();
    }

    @Override
    public Object getItem(int i) {
        return arrPopularSearchModels.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup)
    {
        PopularItemViewHolder popularItemViewHolder;

        if (view == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(mContext);
            view = layoutInflater.inflate(R.layout.popular_search_findly_item, null);
            popularItemViewHolder = new PopularItemViewHolder();
            popularItemViewHolder.tvPopulatItem = view.findViewById(R.id.tvPopularItem);
            view.setTag(popularItemViewHolder);
        } else {
            popularItemViewHolder = (PopularItemViewHolder) view.getTag();
        }

        PopularSearchModel popularSearchModel = (PopularSearchModel) getItem(i);
        popularItemViewHolder.tvPopulatItem.setText(popularSearchModel.get_id());

        return view;
    }

    private class PopularItemViewHolder {
        TextView tvPopulatItem;
    }
}
