package kore.botssdk.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import kore.botssdk.R;

public class TableResponsiveChildAdapter extends RecyclerView.Adapter<TableResponsiveChildAdapter.ViewHolder> {
    private final List<String> listItems;
    private final List<List<String>> listHeaderItems;
    private final LayoutInflater inflater;

    public TableResponsiveChildAdapter(Context context, List<String> listItems, List<List<String>> listHeaderItems) {
        inflater = LayoutInflater.from(context);
        this.listItems = listItems;
        this.listHeaderItems = listHeaderItems;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.table_responsive_child, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        List<String> col = listHeaderItems.get(position);
        holder.tvTableColumnName.setText(col.get(0));
        holder.tvTableValue.setText(listItems.get(position));
    }

    @Override
    public int getItemCount() {
        return listHeaderItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTableColumnName;
        TextView tvTableValue;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTableColumnName = itemView.findViewById(R.id.tvTableColumnName);
            tvTableValue = itemView.findViewById(R.id.tvTableValue);
        }
    }
}
