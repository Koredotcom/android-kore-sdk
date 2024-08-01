package kore.botssdk.viewholders;

import static kore.botssdk.view.viewUtils.DimensionUtil.dp1;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.LinearLayoutCompat;

import java.util.ArrayList;

import kore.botssdk.R;
import kore.botssdk.adapter.MultiSelectButtonAdapter;
import kore.botssdk.models.BaseBotMessage;
import kore.botssdk.models.MultiSelectBase;
import kore.botssdk.models.PayloadInner;
import kore.botssdk.utils.KaFontUtils;
import kore.botssdk.view.AutoExpandListView;

public class MultiSelectTemplateHolder extends BaseViewHolder {
    private final AutoExpandListView autoExpandListView;
    private final View multiSelectLayout;

    public MultiSelectTemplateHolder(@NonNull View itemView) {
        super(itemView, itemView.getContext());
        LinearLayoutCompat layoutBubble = itemView.findViewById(R.id.layoutBubble);
        LinearLayoutCompat.LayoutParams params = (LinearLayoutCompat.LayoutParams) layoutBubble.getLayoutParams();
        params.bottomMargin = (int) (10 * dp1);
        initBubbleText(layoutBubble, false);
        autoExpandListView = itemView.findViewById(R.id.multi_select_list);
        autoExpandListView.setVerticalScrollBarEnabled(false);
        KaFontUtils.applyCustomFont(itemView.getContext(), itemView);
        multiSelectLayout = itemView.findViewById(R.id.multi_select_layout);
    }

    @Override
    public void bind(BaseBotMessage baseBotMessage) {
        PayloadInner payloadInner = getPayloadInner(baseBotMessage);
        if (payloadInner == null) return;
        multiSelectLayout.setAlpha(isLastItem() ? 1.0f : 0.5f);
        ArrayList<MultiSelectBase> items = new ArrayList<>();
        if (payloadInner.getMultiSelectModels() != null && payloadInner.getMultiSelectModels().size() > 0)
            items.addAll(payloadInner.getMultiSelectModels());
        if (payloadInner.getButtons() != null && payloadInner.getButtons().size() > 0)
            items.addAll(payloadInner.getButtons());
        MultiSelectButtonAdapter multiSelectButtonAdapter = new MultiSelectButtonAdapter(itemView.getContext());
        multiSelectButtonAdapter.setMultiSelectModels(items);
        multiSelectButtonAdapter.setEnabled(isLastItem());
        multiSelectButtonAdapter.setComposeFooterInterface(composeFooterInterface);
        autoExpandListView.setAdapter(multiSelectButtonAdapter);
        multiSelectButtonAdapter.notifyDataSetChanged();
    }
}
