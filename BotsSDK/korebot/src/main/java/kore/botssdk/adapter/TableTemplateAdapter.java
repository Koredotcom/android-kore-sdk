package kore.botssdk.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import kore.botssdk.R;
import kore.botssdk.view.tableview.model.MiniTableModel;

public class TableTemplateAdapter extends RecyclerView.Adapter<TableTemplateAdapter.ViewHolder> {

    private final List<List<String>> headers;
    private final List<MiniTableModel> listItems;
    private final boolean isEnabled;
    private final LayoutInflater layoutInflater;
    private final Context context;

    public TableTemplateAdapter(Context context, List<List<String>> headers, List<MiniTableModel> items, boolean isEnabled) {
        layoutInflater = LayoutInflater.from(context);
        this.headers = headers;
        this.listItems = items;
        this.isEnabled = isEnabled;
        this.context = context;
    }

    @NonNull
    @Override
    public TableTemplateAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TableTemplateAdapter.ViewHolder(layoutInflater.inflate(R.layout.table_row, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TableTemplateAdapter.ViewHolder holder, int position) {
        if (listItems != null) {
            MiniTableModel values = listItems.get(position);
            holder.rvChildTableLayout.setLayoutManager(new GridLayoutManager(context, values.getElements().size()));
            holder.rvChildTableLayout.setAdapter(new TableTemplateDataAdapter(context, headers, values.getElements(), isEnabled));
        }
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public RecyclerView rvChildTableLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            rvChildTableLayout = itemView.findViewById(R.id.rvContentRow);
        }
    }
}
