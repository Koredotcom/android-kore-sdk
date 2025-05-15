package kore.botssdk.viewholders;

import static kore.botssdk.viewUtils.DimensionUtil.dp1;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Map;

import kore.botssdk.R;
import kore.botssdk.adapter.MultiSelectTemplateAdapter;
import kore.botssdk.models.BaseBotMessage;
import kore.botssdk.models.BotMultiSelectElementModel;
import kore.botssdk.models.BotResponse;
import kore.botssdk.models.MultiSelectBase;
import kore.botssdk.models.PayloadInner;
import kore.botssdk.net.SDKConfiguration;
import kore.botssdk.utils.KaFontUtils;

public class MultiSelectTemplateHolder extends BaseViewHolder {
    private final RecyclerView recyclerView;
    private final LinearLayout selectAll;
    private ArrayList<MultiSelectBase> checkedItems = new ArrayList<>();
    private ArrayList<BotMultiSelectElementModel> multiSelectElements = new ArrayList<>();

    public static MultiSelectTemplateHolder getInstance(ViewGroup parent) {
        return new MultiSelectTemplateHolder(createView(R.layout.template_multi_select, parent));
    }

    public MultiSelectTemplateHolder(@NonNull View itemView) {
        super(itemView, itemView.getContext());
        LinearLayoutCompat layoutBubble = itemView.findViewById(R.id.layoutBubble);
        initBubbleText(layoutBubble, false);
        recyclerView = itemView.findViewById(R.id.multi_select_list);
        selectAll = itemView.findViewById(R.id.check_select_all);
        recyclerView.setVerticalScrollBarEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(itemView.getContext()));
        KaFontUtils.applyCustomFont(itemView.getContext(), itemView);
    }

    @Override
    public void bind(BaseBotMessage baseBotMessage) {
        PayloadInner payloadInner = getPayloadInner(baseBotMessage);
        if (payloadInner == null) return;
        multiSelectElements = payloadInner.getMultiSelectModels();
        String msgId = ((BotResponse) baseBotMessage).getMessageId();
        ArrayList<MultiSelectBase> items = new ArrayList<>();
        if (multiSelectElements != null && !multiSelectElements.isEmpty()) items.addAll(multiSelectElements);
        if (payloadInner.getButtons() != null && payloadInner.getButtons().size() > 0) items.addAll(payloadInner.getButtons());
        MultiSelectTemplateAdapter multiSelectTemplateAdapter = new MultiSelectTemplateAdapter(msgId, items, isLastItem());
        Map<String, Object> state = ((BotResponse) baseBotMessage).getContentState();
        if (state != null && state.containsKey(BotResponse.SELECTED_ITEM)) {
            checkedItems = (ArrayList<MultiSelectBase>) state.get(BotResponse.SELECTED_ITEM);
            multiSelectTemplateAdapter.setCheckItems(checkedItems);
        }
        multiSelectTemplateAdapter.setListener(contentStateListener);
        multiSelectTemplateAdapter.setComposeFooterInterface(composeFooterInterface);
        recyclerView.setAdapter(multiSelectTemplateAdapter);

        GradientDrawable gradientDrawable = (GradientDrawable) selectAll.getBackground().mutate();
        gradientDrawable.setColor(Color.parseColor("#ffffff"));
        gradientDrawable.setStroke((int) dp1, Color.parseColor(SDKConfiguration.BubbleColors.leftBubbleSelected));
        selectAll.setTag(true);

        if (checkedItems.size() == payloadInner.getMultiSelectModels().size()) {
            gradientDrawable.setStroke((int) dp1, Color.parseColor(SDKConfiguration.BubbleColors.quickReplyColor));
            gradientDrawable.setColor(Color.parseColor(SDKConfiguration.BubbleColors.quickReplyColor));
            selectAll.setTag(false);
        }

        selectAll.setOnClickListener(v -> {
            if (isLastItem()) {
                checkedItems.removeAll(multiSelectElements);
                if ((Boolean) selectAll.getTag()) {
                    checkedItems.addAll(multiSelectElements);
                    selectAll.setTag(false);
                }
                if (contentStateListener != null) contentStateListener.onSaveState(msgId, checkedItems, BotResponse.SELECTED_ITEM);
            }
        });
    }
}