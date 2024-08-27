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

public class TableRowItemAdapter extends RecyclerView.Adapter<TableRowItemAdapter.ViewHolder> {
    private static final String RIGHT = "right";
    private static final String CENTER = "center";
    private final List<String> rowItems;
    private final List<List<String>> headers;
    private final LayoutInflater layoutInflater;

    public TableRowItemAdapter(Context context, List<String> rowItems, List<List<String>> headers) {
        layoutInflater = LayoutInflater.from(context);
        this.rowItems = rowItems;
        this.headers = headers;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(layoutInflater.inflate(R.layout.table_row_item, parent, false));
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

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView value;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            value = itemView.findViewById(R.id.tvTableValue);
        }
    }
}
