package kore.botssdk.viewholders;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import static kore.botssdk.viewUtils.DimensionUtil.dp1;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Map;

import kore.botssdk.R;
import kore.botssdk.adapter.AdvancedMultiSelectAdapter;
import kore.botssdk.itemdecoration.VerticalSpaceItemDecoration;
import kore.botssdk.listener.AdvanceMultiSelectListener;
import kore.botssdk.models.AdvanceMultiSelectCollectionModel;
import kore.botssdk.models.AdvancedMultiSelectModel;
import kore.botssdk.models.BaseBotMessage;
import kore.botssdk.models.BotResponse;
import kore.botssdk.models.PayloadInner;
import kore.botssdk.net.SDKConfiguration;

@SuppressWarnings("UnKnownNullness")
public class AdvanceMultiSelectTemplateHolder extends BaseViewHolder implements AdvanceMultiSelectListener {
    private ArrayList<AdvanceMultiSelectCollectionModel> allCheckedItems = new ArrayList<>();
    private String msgId = "";

    public AdvanceMultiSelectTemplateHolder(@NonNull View itemView) {
        super(itemView, itemView.getContext());
    }

    public static AdvanceMultiSelectTemplateHolder getInstance(ViewGroup parent) {
        return new AdvanceMultiSelectTemplateHolder(createView(R.layout.template_advanced_multi_select, parent));
    }

    @Override
    public void bind(BaseBotMessage baseBotMessage) {
        PayloadInner payloadInner = getPayloadInner(baseBotMessage);
        if (payloadInner == null) return;
        setResponseText(itemView.findViewById(R.id.layoutBubble), payloadInner.getHeading(), baseBotMessage.getTimeStamp());
        msgId = ((BotResponse) baseBotMessage).getMessageId();
        Map<String, Object> contentState = ((BotResponse) baseBotMessage).getContentState();
        if (contentState != null && contentState.containsKey(BotResponse.SELECTED_ITEM)) {
            allCheckedItems = (ArrayList<AdvanceMultiSelectCollectionModel>) contentState.get(BotResponse.SELECTED_ITEM);
        }
        RecyclerView autoExpandListView = itemView.findViewById(R.id.multi_select_list);
        autoExpandListView.setVerticalScrollBarEnabled(false);
        autoExpandListView.setLayoutManager(new LinearLayoutManager(itemView.getContext()));

        View multiSelectLayout = itemView.findViewById(R.id.multi_select_layout);
        TextView tvViewMore = itemView.findViewById(R.id.tvViewMore);
        TextView tvAdvanceDone = itemView.findViewById(R.id.tvAdvanceDone);
        ArrayList<AdvancedMultiSelectModel> models = payloadInner.getAdvancedMultiSelectModels();

        GradientDrawable gradientDrawable = (GradientDrawable) tvAdvanceDone.getBackground();
        gradientDrawable.setStroke((int) (1 * dp1), Color.parseColor(SDKConfiguration.BubbleColors.quickReplyColor));
        gradientDrawable.setColor(Color.parseColor(SDKConfiguration.BubbleColors.quickReplyColor));
        tvAdvanceDone.setTextColor(Color.parseColor(SDKConfiguration.BubbleColors.quickReplyTextColor));

        if (models != null && !models.isEmpty()) {
            multiSelectLayout.setVisibility(VISIBLE);
            tvAdvanceDone.setVisibility(allCheckedItems.isEmpty() ? GONE : VISIBLE);
            AdvancedMultiSelectAdapter multiSelectButtonAdapter = new AdvancedMultiSelectAdapter();
            multiSelectButtonAdapter.setMultiSelectModels(models);
            multiSelectButtonAdapter.setEnabled(isLastItem());
            multiSelectButtonAdapter.setAdvanceMultiListener(this);
            multiSelectButtonAdapter.setCheckedItems(allCheckedItems);
            autoExpandListView.setAdapter(multiSelectButtonAdapter);

            if (payloadInner.getAdvancedMultiSelectModels().size() > payloadInner.getLimit()) {
                if (contentState != null && contentState.containsKey(BotResponse.IS_VIEW_MORE)) {
                    tvViewMore.setVisibility(GONE);
                    multiSelectButtonAdapter.refresh();
                } else
                    tvViewMore.setVisibility(VISIBLE);
            } else {
                tvViewMore.setVisibility(GONE);
            }

            tvViewMore.setOnClickListener(v -> {
                contentStateListener.onSaveState(msgId, true, BotResponse.IS_VIEW_MORE);
            });

            tvAdvanceDone.setOnClickListener(v -> {
                if (!isLastItem()) return;
                StringBuilder stringBuffer = new StringBuilder();
                stringBuffer.append("Here are the selected items : ");
                for (int i = 0; i < allCheckedItems.size(); i++) {
                    stringBuffer.append(allCheckedItems.get(i).getTitle());

                    if (i != allCheckedItems.size() - 1)
                        stringBuffer.append(",");
                }

                composeFooterInterface.onSendClick(stringBuffer.toString(), stringBuffer.toString(), true);
            });

        } else {
            autoExpandListView.setAdapter(null);
            multiSelectLayout.setVisibility(GONE);
        }
    }

    @Override
    public void itemSelected(AdvanceMultiSelectCollectionModel checkedItems) {
        if (!isLastItem()) return;
        if (!allCheckedItems.contains(checkedItems)) {
            allCheckedItems.add(checkedItems);
        } else {
            allCheckedItems.remove(checkedItems);
        }
        contentStateListener.onSaveState(msgId, allCheckedItems, BotResponse.SELECTED_ITEM);
    }

    @Override
    public void allItemsSelected(boolean addOrRemove, ArrayList<AdvanceMultiSelectCollectionModel> checkedItems) {
        if (!isLastItem()) return;
        allCheckedItems.removeAll(checkedItems);
        if (addOrRemove) {
            allCheckedItems.addAll(checkedItems);
        }
        contentStateListener.onSaveState(msgId, allCheckedItems, BotResponse.SELECTED_ITEM);
    }
}