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

public class MiniTableRowItemAdapter extends RecyclerView.Adapter<MiniTableRowItemAdapter.ViewHolder> {
    private final String RIGHT = "right";
    private final String CENTER = "center";
    private List<Object> rowItems;
    private List<List<String>> headers;
    private boolean isEnabled;
    private final LayoutInflater layoutInflater;

    public MiniTableRowItemAdapter(Context context, List<Object> rowItems, List<List<String>> headers) {
        layoutInflater = LayoutInflater.from(context);
        this.rowItems = rowItems;
        this.headers = headers;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(layoutInflater.inflate(R.layout.mini_table_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Object rowItem = getItem(position);
        if (rowItem == null) return;
        List<String> cols = headers.get(position);

        holder.value.setText((CharSequence) rowItem);
        holder.value.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);

        if (cols.size() > 1) {
            switch (cols.get(1)) {
                case CENTER:
                    holder.value.setGravity(Gravity.CENTER);
                    break;
                case RIGHT:
                    holder.value.setGravity(Gravity.END | Gravity.CENTER_VERTICAL);
                    break;
            }
        }

    }

    private Object getItem(int position) {
        return rowItems != null ? rowItems.get(position) : null;
    }

    @Override
    public int getItemCount() {
        return rowItems.size();
    }

    public void populateData(List<Object> rowItems, List<List<String>> headers, boolean isEnabled) {
        this.rowItems = rowItems;
        this.headers = headers;
        this.isEnabled = isEnabled;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView value;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            value = itemView.findViewById(R.id.tvTableValue);
        }
    }
}
