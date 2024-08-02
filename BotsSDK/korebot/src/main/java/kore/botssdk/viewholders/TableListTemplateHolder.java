package kore.botssdk.viewholders;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.LinearLayoutCompat;

import java.util.ArrayList;

import kore.botssdk.R;
import kore.botssdk.adapter.BotTableListTemplateAdapter;
import kore.botssdk.models.BaseBotMessage;
import kore.botssdk.models.BotTableListModel;
import kore.botssdk.models.PayloadInner;
import kore.botssdk.view.AutoExpandListView;

public class TableListTemplateHolder extends BaseViewHolder {
    private final AutoExpandListView autoExpandListView;

    public static TableListTemplateHolder getInstance(ViewGroup parent) {
        return new TableListTemplateHolder(createView(R.layout.template_table_list, parent));
    }

    private TableListTemplateHolder(@NonNull View itemView) {
        super(itemView, itemView.getContext());
        LinearLayoutCompat layoutBubble = itemView.findViewById(R.id.layoutBubble);
        initBubbleText(layoutBubble, false);
        autoExpandListView = itemView.findViewById(R.id.botCustomListView);
    }

    @Override
    public void bind(BaseBotMessage baseBotMessage) {
        PayloadInner payloadInner = getPayloadInner(baseBotMessage);
        if (payloadInner == null) return;
        setResponseText(itemView.findViewById(R.id.layoutBubble), payloadInner.getText());
        ArrayList<BotTableListModel> botListModelArrayList = payloadInner.getTableListElements();
        BotTableListTemplateAdapter botListTemplateAdapter;
        botListTemplateAdapter = new BotTableListTemplateAdapter(itemView.getContext(), autoExpandListView, 4);
        autoExpandListView.setAdapter(botListTemplateAdapter);
        botListTemplateAdapter.setComposeFooterInterface(composeFooterInterface);
        botListTemplateAdapter.setInvokeGenericWebViewInterface(invokeGenericWebViewInterface);
        botListTemplateAdapter.setBotListModelArrayList(botListModelArrayList);
        botListTemplateAdapter.notifyDataSetChanged();
    }
}
