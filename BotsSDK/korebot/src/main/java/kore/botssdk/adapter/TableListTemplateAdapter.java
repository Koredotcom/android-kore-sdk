package kore.botssdk.adapter;

import static kore.botssdk.view.viewUtils.DimensionUtil.dp1;

import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import kore.botssdk.R;
import kore.botssdk.itemdecoration.VerticalSpaceItemDecoration;
import kore.botssdk.listener.ComposeFooterInterface;
import kore.botssdk.listener.InvokeGenericWebViewInterface;
import kore.botssdk.models.BotTableListModel;
import kore.botssdk.utils.StringUtils;

public class TableListTemplateAdapter extends RecyclerView.Adapter<TableListTemplateAdapter.ViewHolder> {
    private final List<BotTableListModel> botTableListModels;
    private final boolean isEnabled;
    private ComposeFooterInterface composeFooterInterface;
    private InvokeGenericWebViewInterface invokeGenericWebViewInterface;

    public TableListTemplateAdapter(List<BotTableListModel> botTableListModels, boolean isEnabled) {
        this.botTableListModels = botTableListModels;
        this.isEnabled = isEnabled;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_table_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BotTableListModel botTableListModel = getItem(position);
        if (botTableListModel == null) return;
        if (!StringUtils.isNullOrEmpty(botTableListModel.getSectionHeader())) {
            holder.botListItemTitle.setTag(botTableListModel);
            holder.botListItemTitle.setVisibility(View.VISIBLE);
            holder.botListItemTitle.setText(botTableListModel.getSectionHeader());
            holder.botListItemTitle.setTypeface(null, Typeface.BOLD);
        }

        if (!StringUtils.isNullOrEmpty(botTableListModel.getSectionHeaderDesc())) {
            holder.botListItemDesc.setTag(botTableListModel);
            holder.botListItemDesc.setVisibility(View.VISIBLE);
            holder.botListItemDesc.setText(botTableListModel.getSectionHeaderDesc());
            holder.botListItemDesc.setTypeface(null, Typeface.BOLD);
        }

        if (botTableListModel.getRowItems() != null && botTableListModel.getRowItems().size() > 0) {
            TableListInnerAdapter botTableListInnerAdapter = new TableListInnerAdapter(botTableListModel.getRowItems(), isEnabled);
            botTableListInnerAdapter.setComposeFooterInterface(composeFooterInterface);
            botTableListInnerAdapter.setInvokeGenericWebViewInterface(invokeGenericWebViewInterface);
            holder.recyclerView.setAdapter(botTableListInnerAdapter);
        }
    }

    @Override
    public int getItemCount() {
        return botTableListModels != null ? botTableListModels.size() : 0;
    }

    private BotTableListModel getItem(int position) {
        return botTableListModels != null ? botTableListModels.get(position) : null;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView botListItemTitle;
        TextView botListItemDesc;
        RecyclerView recyclerView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            botListItemTitle = itemView.findViewById(R.id.bot_list_item_title);
            botListItemDesc = itemView.findViewById(R.id.bot_list_item_desc);
            recyclerView = itemView.findViewById(R.id.botTableListView);
            recyclerView.setLayoutManager(new LinearLayoutManager(itemView.getContext()));
            recyclerView.addItemDecoration(new VerticalSpaceItemDecoration((int) (4 * dp1)));
        }
    }

    public void setComposeFooterInterface(@NonNull ComposeFooterInterface composeFooterInterface) {
        this.composeFooterInterface = composeFooterInterface;
    }

    public void setInvokeGenericWebViewInterface(@NonNull InvokeGenericWebViewInterface invokeGenericWebViewInterface) {
        this.invokeGenericWebViewInterface = invokeGenericWebViewInterface;
    }
}
