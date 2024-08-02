package kore.botssdk.viewholders;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.internal.LinkedTreeMap;

import java.util.ArrayList;
import java.util.List;

import kore.botssdk.R;
import kore.botssdk.adapter.TableTemplateAdapter;
import kore.botssdk.adapter.TableTemplateHeaderAdapter;
import kore.botssdk.itemdecoration.VerticalSpaceItemDecoration;
import kore.botssdk.models.BaseBotMessage;
import kore.botssdk.models.PayloadInner;
import kore.botssdk.view.tableview.model.MiniTableModel;

public class TableTemplateHolder extends BaseViewHolder {
    private final RecyclerView rvTableView;
    private final RecyclerView rvTableViewHeader;

    public TableTemplateHolder(@NonNull View itemView) {
        super(itemView, itemView.getContext());
        rvTableView = itemView.findViewById(R.id.rvTableView);
        rvTableViewHeader = itemView.findViewById(R.id.rvTableViewHeader);
        rvTableView.setLayoutManager(new LinearLayoutManager(itemView.getContext(), LinearLayoutManager.VERTICAL, false));
        rvTableView.addItemDecoration(new VerticalSpaceItemDecoration(LinearLayoutManager.VERTICAL));

    }

    public static TableTemplateHolder getInstance(ViewGroup parent) {
        return new TableTemplateHolder(createView(R.layout.table_template, parent));
    }

    @Override
    public void bind(BaseBotMessage baseBotMessage) {
        PayloadInner payloadInner = getPayloadInner(baseBotMessage);
        if (payloadInner == null) return;
        rvTableViewHeader.setLayoutManager(new GridLayoutManager(itemView.getContext(), payloadInner.getColumns().size()));
        rvTableViewHeader.setAdapter(new TableTemplateHeaderAdapter(itemView.getContext(), payloadInner.getColumns(), isLastItem()));

        List<MiniTableModel> lists = new ArrayList<>();
        int size = ((ArrayList<?>) payloadInner.getElements()).size();
        for (int j = 0; j < size; j++) {
            MiniTableModel model = new MiniTableModel();
            model.setElements(((ArrayList)(((LinkedTreeMap)((ArrayList) payloadInner.getElements()).get(j))).get("Values")));
            lists.add(model);
        }

        rvTableView.setLayoutManager(new LinearLayoutManager(itemView.getContext(), LinearLayoutManager.VERTICAL, false));
        rvTableView.setAdapter(new TableTemplateAdapter(itemView.getContext(), payloadInner.getColumns(), lists, isLastItem()));
    }
}
