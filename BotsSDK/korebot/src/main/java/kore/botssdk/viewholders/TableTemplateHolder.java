package kore.botssdk.viewholders;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.LinearLayoutCompat;
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
import kore.botssdk.models.MiniTableModel;
import kore.botssdk.models.PayloadInner;

public class TableTemplateHolder extends BaseViewHolder {
    private static final int LIMIT = 3;
    private final RecyclerView rvTableView;
    private final RecyclerView rvTableViewHeader;
    private final TextView botTableShowMoreButton;

    public static TableTemplateHolder getInstance(ViewGroup parent) {
        return new TableTemplateHolder(createView(R.layout.table_template, parent));
    }

    public TableTemplateHolder(@NonNull View itemView) {
        super(itemView, itemView.getContext());
        rvTableView = itemView.findViewById(R.id.rvTableView);
        rvTableViewHeader = itemView.findViewById(R.id.rvTableViewHeader);
        botTableShowMoreButton = itemView.findViewById(R.id.botTableShowMoreButton);
        rvTableView.setLayoutManager(new LinearLayoutManager(itemView.getContext(), LinearLayoutManager.VERTICAL, false));
        rvTableView.addItemDecoration(new VerticalSpaceItemDecoration(LinearLayoutManager.VERTICAL));
        LinearLayoutCompat layoutBubble = itemView.findViewById(R.id.layoutBubble);
        initBubbleText(layoutBubble, false);
    }

    @Override
    public void bind(BaseBotMessage baseBotMessage) {
        PayloadInner payloadInner = getPayloadInner(baseBotMessage);
        if (payloadInner == null) return;
        setResponseText(itemView.findViewById(R.id.layoutBubble), payloadInner.getText(), baseBotMessage.getTimeStamp());

        rvTableViewHeader.setLayoutManager(new GridLayoutManager(itemView.getContext(), payloadInner.getColumns().size()));
        rvTableViewHeader.setAdapter(new TableTemplateHeaderAdapter(itemView.getContext(), payloadInner.getColumns(), isLastItem()));
        List<MiniTableModel> lists = new ArrayList<>();
        int size = ((ArrayList<?>) payloadInner.getElements()).size();
        for (int j = 0; j < size; j++) {
            MiniTableModel model = new MiniTableModel();
            model.setElements((List<Object>) (((LinkedTreeMap<?, ?>) ((ArrayList<?>) payloadInner.getElements()).get(j))).get("Values"));
            lists.add(model);
        }

        List<MiniTableModel> dataList = lists.size() > LIMIT ? lists.subList(0, LIMIT) : lists;
        rvTableView.setLayoutManager(new LinearLayoutManager(itemView.getContext(), LinearLayoutManager.VERTICAL, false));
        rvTableView.setAdapter(new TableTemplateAdapter(itemView.getContext(), payloadInner.getColumns(), dataList, isLastItem()));

        botTableShowMoreButton.setVisibility(lists.size() > LIMIT ? View.VISIBLE : View.GONE);
        botTableShowMoreButton.setOnClickListener(v -> showTableViewDialog(itemView.getContext(), payloadInner.getColumns(), lists));
    }

    private void showTableViewDialog(Context context, List<List<String>> headersList, List<MiniTableModel> dataList) {
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.tableview_dialog);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;

        dialog.getWindow().setAttributes(lp);

        RecyclerView rvTableData = dialog.findViewById(R.id.rvData);
        RecyclerView rvHeader = dialog.findViewById(R.id.rvHeader);
        ImageView ivDialogClose = dialog.findViewById(R.id.ivDialogClose);
        rvHeader.setLayoutManager(new GridLayoutManager(context, headersList.size()));
        rvTableData.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));

        rvHeader.setAdapter(new TableTemplateHeaderAdapter(context, headersList, isLastItem()));
        rvTableData.setAdapter(new TableTemplateAdapter(context, headersList, dataList, isLastItem()));

        ivDialogClose.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }
}
