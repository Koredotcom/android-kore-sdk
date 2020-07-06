package com.kore.ai.widgetsdk.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.kora.ai.widgetsdk.R;
import com.kore.ai.widgetsdk.utils.KaFontUtils;

import java.util.ArrayList;

/**
 * Created by Shiva Krishna on 1/22/2018.
 */

public class AttachmentOptionsAdapter extends BaseAdapter {
    ArrayList<String> data;
    Context mContext;

    public AttachmentOptionsAdapter(Context mContext, ArrayList<String> data) {
        this.data = data;
        this.mContext = mContext;

    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ListItemViewHolder holder;
        if (view == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(mContext);
            view = layoutInflater.inflate(R.layout.attachment_options_view, null);
            KaFontUtils.applyCustomFont(mContext, view);
            holder = new ListItemViewHolder();
            holder.title = view.findViewById(R.id.hash_text_view);
            view.setTag(holder);
        } else {
            holder = (ListItemViewHolder) view.getTag();
        }
        String dataObj = (String) getItem(position);
        holder.title.setText(dataObj);


        return view;
    }

    private class ListItemViewHolder {
        TextView title;
    }
}
