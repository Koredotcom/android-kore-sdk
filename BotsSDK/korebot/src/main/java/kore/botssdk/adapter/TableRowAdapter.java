package kore.botssdk.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import kore.botssdk.R;

public class TableRowAdapter extends RecyclerView.Adapter<TableRowAdapter.ViewHolder> {
    private final List<List<String>> rowItems;
    private final List<List<String>> headers;
    private final boolean isEnabled;
    private final LayoutInflater layoutInflater;


    public TableRowAdapter(Context context, List<List<String>> rowItems, List<List<String>> headers, boolean isEnabled) {
        layoutInflater = LayoutInflater.from(context);
        this.rowItems = rowItems;
        this.headers = headers;
        this.isEnabled = isEnabled;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(layoutInflater.inflate(R.layout.table_row, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        List<String> rowModel = getItem(position);
        if (rowModel == null) return;
        holder.rvContent.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.table_data_row_odd));

        if ((position % 2) == 0) {
            holder.rvContent.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.table_data_row_even));
        }
        holder.rvContent.setLayoutManager(new GridLayoutManager(holder.itemView.getContext(), rowItems.get(position).size()));

        holder.rvContent.setAdapter(new TableRowItemAdapter(holder.itemView.getContext(), rowItems.get(position), headers));
    }

    private List<String> getItem(int position) {
        return rowItems != null ? rowItems.get(position) : null;
    }

    @Override
    public int getItemCount() {
        return rowItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public RecyclerView rvContent;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            rvContent = itemView.findViewById(R.id.rvContentRow);
        }
    }
}
