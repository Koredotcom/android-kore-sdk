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

public class TableTemplateHeaderAdapter extends RecyclerView.Adapter<TableTemplateHeaderAdapter.ViewHolder> {

    private final List<List<String>> headers;
    private final LayoutInflater layoutInflater;

    public TableTemplateHeaderAdapter(Context context, List<List<String>> headers) {
        layoutInflater = LayoutInflater.from(context);
        this.headers = headers;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TableTemplateHeaderAdapter.ViewHolder(layoutInflater.inflate(R.layout.table_child_cell, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        List<String> value = headers.get(position);
        if (value != null) {
            holder.tvTableValue.setText(value.get(0));
            holder.tvTableValue.setGravity(Gravity.CENTER_VERTICAL);

            if (value.size() > 1) {
                switch (value.get(1)) {
                    case BundleConstants.CENTER: {
                        holder.tvTableValue.setGravity(Gravity.CENTER);
                    }
                    break;
                    case BundleConstants.RIGHT: {
                        holder.tvTableValue.setGravity(Gravity.END|Gravity.CENTER_VERTICAL);
                    }
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return headers.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvTableValue;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTableValue = itemView.findViewById(R.id.tvTableValue);
        }
    }
}
