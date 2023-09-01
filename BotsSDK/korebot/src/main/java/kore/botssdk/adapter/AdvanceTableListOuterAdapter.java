package kore.botssdk.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kore.ai.widgetsdk.utils.KaFontUtils;

import java.util.ArrayList;

import kore.botssdk.R;
import kore.botssdk.models.AdvanceListTableModel;

public class AdvanceTableListOuterAdapter extends BaseAdapter
{
    private final Context context;
    private final ArrayList<AdvanceListTableModel> arrAdvanceListTableModels;
    private final LayoutInflater layoutInflater;

    public AdvanceTableListOuterAdapter(Context context, ArrayList<AdvanceListTableModel> arrAdvanceListTableModels)
    {
        this.context = context;
        this.arrAdvanceListTableModels = arrAdvanceListTableModels;
        this.layoutInflater = LayoutInflater.from(context);
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

    private static class DetailsViewHolder {
        private RecyclerView rvTableList;
    }
}
