package kore.botssdk.adapter;

import static android.view.Gravity.CENTER_VERTICAL;

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

public class MiniTableItemAdapter extends RecyclerView.Adapter<MiniTableItemAdapter.ViewHolder> {

    private final String RIGHT = "right";
    private final String CENTER = "center";
    private List<String> tableItems;
    private boolean isEnabled;
    private final LayoutInflater layoutInflater;
    private List<String> headers;

    public MiniTableItemAdapter(Context context, List<String> tableItems, List<String> headers) {
        layoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        MiniTableItemAdapter.ViewHolder holder = new ViewHolder(layoutInflater.inflate(R.layout.mini_table_item, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String item = getItem(position);
        if (item == null) return;
        holder.value.setText(item);
        holder.value.setGravity(Gravity.START & CENTER_VERTICAL);
        if (headers.size() > 1) {
            switch (headers.get(1)) {
                case CENTER:
                    holder.value.setGravity(Gravity.CENTER);
                    break;
                case RIGHT:
                    holder.value.setGravity(Gravity.END | Gravity.CENTER_VERTICAL);
                    break;
            }
        }
    }

    private String getItem(int position) {
        return tableItems != null ? tableItems.get(position) : null;
    }

    @Override
    public int getItemCount() {
        return tableItems == null ? 0 : tableItems.size();
    }

    public void populateData(List<String> list, boolean isEnabled, List<String> headers) {
        this.headers = headers;
        this.tableItems = list;
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
