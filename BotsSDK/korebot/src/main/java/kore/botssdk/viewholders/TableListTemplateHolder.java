package kore.botssdk.viewholders;

import static kore.botssdk.view.viewUtils.DimensionUtil.dp1;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import kore.botssdk.R;
import kore.botssdk.adapter.TableListTemplateAdapter;
import kore.botssdk.itemdecoration.VerticalSpaceItemDecoration;
import kore.botssdk.models.BaseBotMessage;
import kore.botssdk.models.BotTableListModel;
import kore.botssdk.models.PayloadInner;

public class TableListTemplateHolder extends BaseViewHolder {
    private final RecyclerView recyclerView;

    public static TableListTemplateHolder getInstance(ViewGroup parent) {
        return new TableListTemplateHolder(createView(R.layout.template_table_list, parent));
    }

    private TableListTemplateHolder(@NonNull View itemView) {
        super(itemView, itemView.getContext());
        LinearLayoutCompat layoutBubble = itemView.findViewById(R.id.layoutBubble);
        initBubbleText(layoutBubble, false);
        recyclerView = itemView.findViewById(R.id.botCustomListView);
        recyclerView.setLayoutManager(new LinearLayoutManager(itemView.getContext()));
        recyclerView.addItemDecoration(new VerticalSpaceItemDecoration((int) (1 * dp1)));
    }

    @Override
    public void bind(BaseBotMessage baseBotMessage) {
        PayloadInner payloadInner = getPayloadInner(baseBotMessage);
        if (payloadInner == null) return;
        setResponseText(itemView.findViewById(R.id.layoutBubble), payloadInner.getText());
        ArrayList<BotTableListModel> botListModelArrayList = payloadInner.getTableListElements();
        TableListTemplateAdapter botListTemplateAdapter;
        botListTemplateAdapter = new TableListTemplateAdapter(botListModelArrayList, isLastItem());
        botListTemplateAdapter.setComposeFooterInterface(composeFooterInterface);
        botListTemplateAdapter.setInvokeGenericWebViewInterface(invokeGenericWebViewInterface);
        recyclerView.setAdapter(botListTemplateAdapter);
    }
}
