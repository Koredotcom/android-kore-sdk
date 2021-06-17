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

public class AutoSuggestionsAdapter extends BaseAdapter
{
    private Context mContext;
    private ArrayList<String> arrAutoSuggestionList;

    public AutoSuggestionsAdapter(Context context, ArrayList<String> arrAutoSuggestionList)
    {
        this.mContext = context;
        this.arrAutoSuggestionList = arrAutoSuggestionList;
    }


    @Override
    public int getCount()
    {
        return arrAutoSuggestionList.size();
    }

    @Override
    public Object getItem(int i) {
        return arrAutoSuggestionList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        AutoSuggestionItemViewHolder popularItemViewHolder;

        if (view == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(mContext);
            view = layoutInflater.inflate(R.layout.auto_suggestion_item, null);
            popularItemViewHolder = new AutoSuggestionItemViewHolder();
            popularItemViewHolder.tvPopulatItem = view.findViewById(R.id.tvPopularItem);
            view.setTag(popularItemViewHolder);
        } else {
            popularItemViewHolder = (AutoSuggestionItemViewHolder) view.getTag();
        }

        popularItemViewHolder.tvPopulatItem.setText(arrAutoSuggestionList.get(i));

        return view;
    }

    private class AutoSuggestionItemViewHolder {
        TextView tvPopulatItem;
    }
}
