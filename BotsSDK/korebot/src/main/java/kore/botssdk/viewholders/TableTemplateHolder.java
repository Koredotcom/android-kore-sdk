package kore.botssdk.viewholders;

import static kore.botssdk.view.viewUtils.DimensionUtil.dp1;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import kore.botssdk.R;
import kore.botssdk.adapter.TableHeaderAdapter;
import kore.botssdk.itemdecoration.VerticalSpaceItemDecoration;
import kore.botssdk.models.BaseBotMessage;
import kore.botssdk.models.BotTableDataModel;
import kore.botssdk.models.PayloadInner;

public class TableTemplateHolder extends BaseViewHolder {
    private final RecyclerView rvTableView;
    private final RecyclerView rvTableViewHeader;

    public TableTemplateHolder(@NonNull View itemView) {
        super(itemView, itemView.getContext());
        LinearLayoutCompat layoutBubble = itemView.findViewById(R.id.layoutBubble);
        LinearLayoutCompat.LayoutParams params = (LinearLayoutCompat.LayoutParams) layoutBubble.getLayoutParams();
        params.bottomMargin = (int) (10 * dp1);
        initBubbleText(layoutBubble, false);
        rvTableView = itemView.findViewById(R.id.rvTableView);
        rvTableViewHeader = itemView.findViewById(R.id.rvTableViewHeader);
        rvTableView.setLayoutManager(new LinearLayoutManager(itemView.getContext(), LinearLayoutManager.VERTICAL, false));
        rvTableView.addItemDecoration(new VerticalSpaceItemDecoration(LinearLayoutManager.VERTICAL));

    }

    @Override
    public void bind(BaseBotMessage baseBotMessage) {
        PayloadInner payloadInner = getPayloadInner(baseBotMessage);
        if (payloadInner == null) return;
        List<BotTableDataModel> rows = payloadInner.getTable_elements_data();
        rvTableViewHeader.setLayoutManager(new GridLayoutManager(itemView.getContext(), payloadInner.getColumns().size()));
        rvTableViewHeader.setAdapter(new TableHeaderAdapter(itemView.getContext(), payloadInner.getColumns(), isLastItem()));
//        rvTableView.setAdapter(new TableRowAdapter(itemView.getContext(), rows, payloadInner.getColumns(), isLastItem()));
    }
}
