package com.kore.ai.widgetsdk.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.kora.ai.widgetsdk.R;
import com.kore.ai.widgetsdk.utils.KaFontUtils;

import java.util.ArrayList;

public class ListedUtteranceAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<String> utterancesList;
    // private HashMap<String, KoreContact> contactHashMap;
    LayoutInflater layoutInflater;

    public ListedUtteranceAdapter(Context mContext) {
        this.mContext = mContext;
        layoutInflater = LayoutInflater.from(mContext);
    }


    @Override
    public int getCount() {
        return utterancesList != null ? utterancesList.size() : 0;
    }

    @Override
    public String getItem(int position) {
        if (utterancesList != null && utterancesList.size() > position)
            return utterancesList.get(position);
        else
            return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        SharesViewHolder holder;

        if (view == null) {
            view = layoutInflater.inflate(R.layout.listed_utterance_item, null);
            KaFontUtils.applyCustomFont(mContext, view);
            holder = new SharesViewHolder();
            holder.utterance_view = view.findViewById(R.id.utterance_view);
            holder.utterance_view.setTypeface(null,Typeface.ITALIC);
            view.setTag(holder);
        } else {
            holder = (SharesViewHolder) view.getTag();
        }

        populateData(holder, position);

        return view;
    }

    private void populateData(SharesViewHolder holder, int position) {
        String dataObj = (String) getItem(position);
        holder.utterance_view.setText(dataObj);
    }

    public void setUtteranceList(ArrayList<String> utterancesList) {
        this.utterancesList = utterancesList;
    }


    private class SharesViewHolder {
        private TextView utterance_view;

    }
}
