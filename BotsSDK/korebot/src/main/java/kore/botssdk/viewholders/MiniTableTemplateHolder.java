package kore.botssdk.viewholders;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import kore.botssdk.R;
import kore.botssdk.adapter.MiniTablePagerAdapter;
import kore.botssdk.models.BaseBotMessage;
import kore.botssdk.models.PayloadInner;

public class MiniTableTemplateHolder extends BaseViewHolder {
    private final RecyclerView recyclerView;
    private final MiniTablePagerAdapter adapter;

    public static MiniTableTemplateHolder getInstance(ViewGroup parent) {
        return new MiniTableTemplateHolder(createView(R.layout.template_mini_table, parent));
    }

    private MiniTableTemplateHolder(@NonNull View itemView) {
        super(itemView, itemView.getContext());
        LinearLayoutCompat layoutBubble = itemView.findViewById(R.id.layoutBubble);
        initBubbleText(layoutBubble, false);
        recyclerView = itemView.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(itemView.getContext(), LinearLayoutManager.HORIZONTAL, false));
        adapter = new MiniTablePagerAdapter(itemView.getContext());
    }

    @Override
    public void bind(BaseBotMessage baseBotMessage) {
        PayloadInner payloadInner = getPayloadInner(baseBotMessage);
        if (payloadInner == null) return;
        recyclerView.setAdapter(adapter);
        adapter.populateData(payloadInner.getMiniTableDataModels(), isLastItem());
    }
}
