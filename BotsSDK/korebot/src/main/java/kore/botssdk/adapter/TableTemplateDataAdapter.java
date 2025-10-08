package kore.botssdk.adapter;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import kore.botssdk.R;
import kore.botssdk.utils.BundleConstants;

public class TableTemplateDataAdapter extends RecyclerView.Adapter<TableTemplateDataAdapter.ViewHolder> {

    private final List<List<String>> headers;
    private final List<Object> listItems;
    private final LayoutInflater layoutInflater;

    public TableTemplateDataAdapter(Context context, List<List<String>> headers, List<Object> items) {
        layoutInflater = LayoutInflater.from(context);
        this.headers = headers;
        this.listItems = items;
    }

    @NonNull
    @Override
    public TableTemplateDataAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TableTemplateDataAdapter.ViewHolder(layoutInflater.inflate(R.layout.table_child_cell, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TableTemplateDataAdapter.ViewHolder holder, int position) {
        Object value = listItems.get(position);
        List<String> cols = headers.get(position);

        holder.tvTableValue.setText(String.valueOf(value));
        holder.tvTableValue.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);

        if (cols.size() > 1) {
            switch (cols.get(1)) {
                case BundleConstants.CENTER:
                    holder.tvTableValue.setGravity(Gravity.CENTER);
                    break;
                case BundleConstants.RIGHT:
                    holder.tvTableValue.setGravity(Gravity.END | Gravity.CENTER_VERTICAL);
            }
        }
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvTableValue;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTableValue = itemView.findViewById(R.id.tvTableValue);
        }
    }
}
