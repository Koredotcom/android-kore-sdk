package kore.botssdk.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import kore.botssdk.R;
import kore.botssdk.models.BotTableDataModel;
import kore.botssdk.net.SDKConfiguration;

public class TableResponsiveAdapter extends RecyclerView.Adapter<TableResponsiveAdapter.ViewHolder> {
    private final LayoutInflater inflater;
    private final List<BotTableDataModel> rows;
    private final List<List<String>> columns;
    private final Context mContext;

    public TableResponsiveAdapter(@NonNull Context context, @NonNull List<BotTableDataModel> rows, @NonNull List<List<String>> columns) {
        this.rows = rows;
        this.columns = columns;
        this.inflater = LayoutInflater.from(context);
        mContext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(inflater.inflate(R.layout.table_responsive_row, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BotTableDataModel model = getItem(position);
        List<String> values = model.getValues();
        if (values == null) return;

        holder.rlTableResponsive.setBackgroundResource(0);

        if((position % 2) == 0)
            holder.rlTableResponsive.setBackgroundColor(Color.parseColor(SDKConfiguration.BubbleColors.leftBubbleSelected));

        holder.tvTableSerial.setText(values.get(0));
        holder.tvTableTitle.setText(values.get(1));

        holder.rvTableResponsive.setLayoutManager(new GridLayoutManager(mContext, values.size() / 2));
        holder.rvTableResponsive.setAdapter(new TableResponsiveChildAdapter(mContext, values, columns));

        holder.ivTableResponsiveExpand.setOnClickListener(view -> {
            if (holder.rvTableResponsive.getVisibility() == View.GONE) {
                holder.rvTableResponsive.setVisibility(View.VISIBLE);
                holder.llTableResponsive.setVisibility(View.GONE);
                holder.ivTableResponsiveExpand.setRotation(90f);
            } else {
                holder.rvTableResponsive.setVisibility(View.GONE);
                holder.llTableResponsive.setVisibility(View.VISIBLE);
                holder.ivTableResponsiveExpand.setRotation(0f);
            }
        });
    }

    @Override
    public int getItemCount() {
        return rows == null ? 0 : Math.min(rows.size(), 4);
    }

    public BotTableDataModel getItem(int position) {
        return rows == null ? null : rows.get(position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTableSerial;
        TextView tvTableTitle;
        RecyclerView rvTableResponsive;
        LinearLayout llTableResponsive;
        ImageView ivTableResponsiveExpand;
        RelativeLayout rlTableResponsive;

        public ViewHolder(@NonNull View view) {
            super(view);
            tvTableSerial = view.findViewById(R.id.tvTableSerial);
            tvTableTitle = view.findViewById(R.id.tvTableTitle);
            rvTableResponsive = view.findViewById(R.id.rvTableResponsive);
            llTableResponsive = view.findViewById(R.id.llTableResponsive);
            ivTableResponsiveExpand = view.findViewById(R.id.ivTableResponsiveExpand);
            rlTableResponsive = view.findViewById(R.id.rlTableResponsive);
        }
    }
}

