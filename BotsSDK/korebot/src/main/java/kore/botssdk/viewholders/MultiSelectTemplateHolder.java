package kore.botssdk.viewholders;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import kore.botssdk.R;
import kore.botssdk.adapter.MultiSelectTemplateAdapter;
import kore.botssdk.models.BaseBotMessage;
import kore.botssdk.models.BotResponse;
import kore.botssdk.models.MultiSelectBase;
import kore.botssdk.models.PayloadInner;
import kore.botssdk.utils.KaFontUtils;

public class MultiSelectTemplateHolder extends BaseViewHolder {
    private final RecyclerView recyclerView;
    private final View multiSelectLayout;

    public static MultiSelectTemplateHolder getInstance(ViewGroup parent) {
        return new MultiSelectTemplateHolder(createView(R.layout.template_multi_select, parent));
    }

    public MultiSelectTemplateHolder(@NonNull View itemView) {
        super(itemView, itemView.getContext());
        LinearLayoutCompat layoutBubble = itemView.findViewById(R.id.layoutBubble);
        initBubbleText(layoutBubble, false);
        recyclerView = itemView.findViewById(R.id.multi_select_list);
        recyclerView.setVerticalScrollBarEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(itemView.getContext()));
        KaFontUtils.applyCustomFont(itemView.getContext(), itemView);
        multiSelectLayout = itemView.findViewById(R.id.multi_select_layout);
    }

    @Override
    public void bind(BaseBotMessage baseBotMessage) {
        PayloadInner payloadInner = getPayloadInner(baseBotMessage);
        if (payloadInner == null) return;
        String msgId = ((BotResponse) baseBotMessage).getMessageId();
        multiSelectLayout.setAlpha(isLastItem() ? 1.0f : 0.5f);
        ArrayList<MultiSelectBase> items = new ArrayList<>();
        if (payloadInner.getMultiSelectModels() != null && payloadInner.getMultiSelectModels().size() > 0)
            items.addAll(payloadInner.getMultiSelectModels());
        if (payloadInner.getButtons() != null && payloadInner.getButtons().size() > 0)
            items.addAll(payloadInner.getButtons());
        MultiSelectTemplateAdapter multiSelectTemplateAdapter = new MultiSelectTemplateAdapter(itemView.getContext(), msgId, items, isLastItem());
        Map<String, Object> state = ((BotResponse) baseBotMessage).getContentState();
        if (state != null && state.containsKey(BotResponse.SELECTED_ITEM))
            multiSelectTemplateAdapter.setCheckItems((List<MultiSelectBase>) state.get(BotResponse.SELECTED_ITEM));
        multiSelectTemplateAdapter.setListener(contentStateListener);
        multiSelectTemplateAdapter.setComposeFooterInterface(composeFooterInterface);
        recyclerView.setAdapter(multiSelectTemplateAdapter);
    }
}
