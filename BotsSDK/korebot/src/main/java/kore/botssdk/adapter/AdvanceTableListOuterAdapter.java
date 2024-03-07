package kore.botssdk.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import kore.botssdk.R;
import kore.botssdk.models.AdvanceListTableModel;
import kore.botssdk.utils.KaFontUtils;

public class AdvanceTableListOuterAdapter extends BaseAdapter
{
    private final Context context;
    private final ArrayList<AdvanceListTableModel> arrAdvanceListTableModels;

    public AdvanceTableListOuterAdapter(@NonNull Context context, @NonNull ArrayList<AdvanceListTableModel> arrAdvanceListTableModels)
    {
        this.context = context;
        this.arrAdvanceListTableModels = arrAdvanceListTableModels;
    }

    @Override
    public int getCount()
    {
        return arrAdvanceListTableModels.size();
    }

    @Override
    public Object getItem(int i) {
        return arrAdvanceListTableModels.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        DetailsViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.advance_list_table_outer_cell, null);
            KaFontUtils.applyCustomFont(context, convertView);
            holder = new DetailsViewHolder();
            holder.rvTableList = convertView.findViewById(R.id.rvTableList);
            convertView.setTag(holder);
        } else {
            holder = (DetailsViewHolder) convertView.getTag();
        }

        holder.rvTableList.setLayoutManager(new GridLayoutManager(context, 2));
        holder.rvTableList.setAdapter(new AdvanceTableListAdapter(context, arrAdvanceListTableModels.get(position).getRowData()));

        return convertView;
    }

    static class DetailsViewHolder {
        RecyclerView rvTableList;
    }
}
