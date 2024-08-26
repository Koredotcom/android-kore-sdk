package kore.botssdk.viewholders;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import kore.botssdk.R;
import kore.botssdk.adapter.AdvancedMultiSelectAdapter;
import kore.botssdk.listener.AdvanceMultiSelectListener;
import kore.botssdk.models.AdvanceMultiSelectCollectionModel;
import kore.botssdk.models.BaseBotMessage;
import kore.botssdk.models.PayloadInner;

@SuppressWarnings("UnKnownNullness")
public class AdvanceMultiSelectTemplateHolder extends BaseViewHolder implements AdvanceMultiSelectListener {

    View multiSelectLayout;
    AdvancedMultiSelectAdapter multiSelectButtonAdapter;
    TextView tvViewMore, tvAdvanceDone;
    ArrayList<AdvanceMultiSelectCollectionModel> allCheckedItems = new ArrayList<>();

    public AdvanceMultiSelectTemplateHolder(@NonNull View itemView) {
        super(itemView, itemView.getContext());
    }

    public static AdvanceMultiSelectTemplateHolder getInstance(ViewGroup parent) {
        return new AdvanceMultiSelectTemplateHolder(createView(R.layout.advance_multi_select_view, parent));
    }

    @Override
    public void bind(BaseBotMessage baseBotMessage) {
        PayloadInner payloadInner = getPayloadInner(baseBotMessage);
        RecyclerView autoExpandListView = itemView.findViewById(R.id.multi_select_list);
        autoExpandListView.setVerticalScrollBarEnabled(false);
        autoExpandListView.setLayoutManager(new LinearLayoutManager(itemView.getContext()));

        multiSelectLayout = itemView.findViewById(R.id.multi_select_layout);
        tvViewMore = itemView.findViewById(R.id.tvViewMore);
        tvAdvanceDone = itemView.findViewById(R.id.tvAdvanceDone);

        if (payloadInner != null && payloadInner.getAdvancedMultiSelectModels() != null && payloadInner.getAdvancedMultiSelectModels().size() > 0) {
            multiSelectLayout.setVisibility(VISIBLE);

            multiSelectButtonAdapter = new AdvancedMultiSelectAdapter(itemView.getContext());
            multiSelectButtonAdapter.setMultiSelectModels(payloadInner.getAdvancedMultiSelectModels());
            multiSelectButtonAdapter.setEnabled(isLastItem());
            multiSelectButtonAdapter.setComposeFooterInterface(composeFooterInterface);
            multiSelectButtonAdapter.setAdvanceMultiListener(this);
            autoExpandListView.setAdapter(multiSelectButtonAdapter);

            if (payloadInner.getAdvancedMultiSelectModels().size() > payloadInner.getLimit())
                tvViewMore.setVisibility(VISIBLE);

            tvViewMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    multiSelectButtonAdapter.refresh();
                    tvViewMore.setVisibility(GONE);
                }
            });

            tvAdvanceDone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    StringBuilder stringBuffer = new StringBuilder();
                    stringBuffer.append("Here are the selected items : ");
                    for (int i = 0; i < allCheckedItems.size(); i++) {
                        stringBuffer.append(allCheckedItems.get(i).getValue());

                        if (i != allCheckedItems.size())
                            stringBuffer.append(",");
                    }

                    multiSelectLayout.setAlpha(0.5f);
                    composeFooterInterface.onSendClick(stringBuffer.toString(), stringBuffer.toString(), true);
                }
            });

        } else {
            autoExpandListView.setAdapter(null);
            multiSelectLayout.setVisibility(GONE);
        }
    }

    @Override
    public void itemSelected(AdvanceMultiSelectCollectionModel checkedItems) {
        if (!allCheckedItems.contains(checkedItems)) {
            allCheckedItems.add(checkedItems);
        } else {
            allCheckedItems.remove(checkedItems);
        }

        if (allCheckedItems.size() > 0)
            tvAdvanceDone.setVisibility(VISIBLE);
        else
            tvAdvanceDone.setVisibility(GONE);
    }

    @Override
    public void allItemsSelected(boolean addOrRemove, ArrayList<AdvanceMultiSelectCollectionModel> checkedItems) {
        if (addOrRemove) {
            allCheckedItems.addAll(checkedItems);
        } else {
            for (int i = 0; i < checkedItems.size(); i++) {
                allCheckedItems.remove(checkedItems.get(i));
            }
        }

        if (allCheckedItems.size() > 0)
            tvAdvanceDone.setVisibility(VISIBLE);
        else
            tvAdvanceDone.setVisibility(GONE);
    }
}
