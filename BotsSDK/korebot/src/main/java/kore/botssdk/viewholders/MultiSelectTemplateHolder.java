package kore.botssdk.viewholders;

import static android.content.Context.MODE_PRIVATE;
import static kore.botssdk.models.BotResponse.BUBBLE_LEFT_BG_COLOR;
import static kore.botssdk.models.BotResponse.BUBBLE_RIGHT_BG_COLOR;
import static kore.botssdk.models.BotResponse.THEME_NAME;
import static kore.botssdk.view.viewUtils.DimensionUtil.dp1;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.view.ViewGroup;
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
    private SharedPreferences prefs;

    public static MultiSelectTemplateHolder getInstance(ViewGroup parent) {
        return new MultiSelectTemplateHolder(createView(R.layout.template_multi_select, parent));
    }

    public MultiSelectTemplateHolder(@NonNull View itemView) {
        super(itemView, itemView.getContext());
        prefs = itemView.getContext().getSharedPreferences(THEME_NAME, MODE_PRIVATE);
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
        if (payloadInner.getButtons() != null && !payloadInner.getButtons().isEmpty()) items.addAll(payloadInner.getButtons());
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
        gradientDrawable.setStroke((int) dp1, Color.parseColor(prefs.getString(BUBBLE_LEFT_BG_COLOR, "#000000")));
        selectAll.setTag(true);

        if (checkedItems.size() == payloadInner.getMultiSelectModels().size()) {
            gradientDrawable.setStroke((int) dp1, Color.parseColor(prefs.getString(BUBBLE_RIGHT_BG_COLOR, "#3F51B5")));
            gradientDrawable.setColor(Color.parseColor(prefs.getString(BUBBLE_RIGHT_BG_COLOR, "#3F51B5")));
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