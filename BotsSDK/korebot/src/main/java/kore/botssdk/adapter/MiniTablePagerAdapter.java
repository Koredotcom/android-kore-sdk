package kore.botssdk.adapter;

import static kore.botssdk.viewUtils.DimensionUtil.dp1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import kore.botssdk.R;
import kore.botssdk.models.BotMiniTableModel;

public class MiniTablePagerAdapter extends RecyclerView.Adapter<MiniTablePagerAdapter.ViewHolder> {
    private ArrayList<BotMiniTableModel> miniTableModels;
    private boolean isEnabled;
    private final LayoutInflater layoutInflater;

    public MiniTablePagerAdapter(Context context) {
        layoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        MiniTablePagerAdapter.ViewHolder holder = new ViewHolder(layoutInflater.inflate(R.layout.mini_table_page, parent, false));
        holder.rvHeader.setPadding(0, 0, (int) (20 * dp1), 0);
        holder.rvHeader.setClipToPadding(false);
        holder.rvContent.setClipToPadding(false);
        new PagerSnapHelper().attachToRecyclerView(holder.rvHeader);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BotMiniTableModel miniTableModel = getItem(position);
        if (miniTableModel == null) return;
        TableHeaderAdapter headerAdapter = new TableHeaderAdapter(holder.itemView.getContext(), miniTableModel.getPrimary(), isEnabled);
        TableRowAdapter itemAdapter = new TableRowAdapter(holder.itemView.getContext(), miniTableModel.getAdditional(), miniTableModel.getPrimary(), isEnabled);
        holder.rvHeader.setLayoutManager(new GridLayoutManager(holder.itemView.getContext(), miniTableModel.getPrimary().size()));
        holder.rvContent.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext(), LinearLayoutManager.VERTICAL, false));
        holder.rvHeader.setAdapter(headerAdapter);
        holder.rvContent.setAdapter(itemAdapter);
    }

    private BotMiniTableModel getItem(int position) {
        return miniTableModels != null ? miniTableModels.get(position) : null;
    }

    @Override
    public int getItemCount() {
        return miniTableModels.size();
    }

    public void populateData(ArrayList<BotMiniTableModel> buttons, boolean isEnabled) {
        this.miniTableModels = buttons;
        this.isEnabled = isEnabled;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public RecyclerView rvHeader;
        public RecyclerView rvContent;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            rvHeader = itemView.findViewById(R.id.rvTableHeader);
            rvContent = itemView.findViewById(R.id.rvTableContent);
        }
    }
}
