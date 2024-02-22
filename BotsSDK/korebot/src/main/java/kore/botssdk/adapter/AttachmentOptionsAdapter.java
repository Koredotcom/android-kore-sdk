package kore.botssdk.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;

import kore.botssdk.R;
import kore.botssdk.utils.KaFontUtils;

/**
 * Created by Shiva Krishna on 1/22/2018.
 */

public class AttachmentOptionsAdapter extends BaseAdapter {
    final ArrayList<String> data;
    final Context mContext;

    public AttachmentOptionsAdapter(@NonNull Context mContext, @NonNull ArrayList<String> data) {
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
            view = View.inflate(mContext, R.layout.attachment_options_view, null);
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

    static class ListItemViewHolder {
        TextView title;
    }
}
