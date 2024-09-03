package kore.botssdk.viewholders;

import static kore.botssdk.viewUtils.DimensionUtil.dp1;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import kore.botssdk.R;
import kore.botssdk.adapter.TableHeaderAdapter;
import kore.botssdk.adapter.TableResponsiveAdapter;
import kore.botssdk.adapter.TableRowAdapter;
import kore.botssdk.itemdecoration.VerticalSpaceItemDecoration;
import kore.botssdk.models.BaseBotMessage;
import kore.botssdk.models.BotTableDataModel;
import kore.botssdk.models.PayloadInner;

public class TableResponsiveTemplateHolder extends BaseViewHolder {
    private final TextView tvShowMore;
    private final RecyclerView rvTableView;

    public static TableResponsiveTemplateHolder getInstance(ViewGroup parent) {
        return new TableResponsiveTemplateHolder(createView(R.layout.template_table_responsive, parent));
    }

    private TableResponsiveTemplateHolder(@NonNull View itemView) {
        super(itemView, itemView.getContext());
        LinearLayoutCompat layoutBubble = itemView.findViewById(R.id.layoutBubble);
        initBubbleText(layoutBubble, false);
        rvTableView = itemView.findViewById(R.id.rvTableView);
        tvShowMore = itemView.findViewById(R.id.tvShowMore);
        rvTableView.setLayoutManager(new LinearLayoutManager(itemView.getContext(), LinearLayoutManager.VERTICAL, false));
        rvTableView.addItemDecoration(new VerticalSpaceItemDecoration((int) (1 * dp1)));
    }

    @Override
    public void bind(BaseBotMessage baseBotMessage) {
        PayloadInner payloadInner = getPayloadInner(baseBotMessage);
        if (payloadInner == null) return;
        setResponseText(itemView.findViewById(R.id.layoutBubble), payloadInner.getText());
        List<BotTableDataModel> rows = payloadInner.getTable_elements_data();
        List<List<String>> columns = payloadInner.getColumns();

        tvShowMore.setVisibility(rows.size() > 3 ? View.VISIBLE : View.GONE);
        tvShowMore.setOnClickListener(view -> showTableViewDialog(view.getContext(), columns, rows));
        rvTableView.setAdapter(new TableResponsiveAdapter(itemView.getContext(), rows, columns));
    }

    private void showTableViewDialog(Context context, List<List<String>> cols, List<BotTableDataModel> values) {
        Dialog dialog = new Dialog(context, R.style.MyTableDialogTheme);
        dialog.setContentView(R.layout.table_dialog_view);
        RecyclerView rvTableView = dialog.findViewById(R.id.rvTableView);
        RecyclerView rvTableViewHeader = dialog.findViewById(R.id.rvTableViewHeader);
        ImageView ivCancel = dialog.findViewById(R.id.ivCancel);

        ivCancel.setOnClickListener(view -> dialog.dismiss());

        rvTableView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        rvTableView.addItemDecoration(new VerticalSpaceItemDecoration(LinearLayoutManager.VERTICAL));
        List<List<String>> rowValues = new ArrayList<>();
        for (BotTableDataModel model : values) {
            rowValues.add(model.getValues());
        }
        rvTableView.setAdapter(new TableRowAdapter(context, rowValues, cols, isLastItem()));
        rvTableViewHeader.setLayoutManager(new GridLayoutManager(context, cols.size()));
        rvTableViewHeader.setAdapter(new TableHeaderAdapter(context, cols, isLastItem()));
        dialog.show();
    }
}
