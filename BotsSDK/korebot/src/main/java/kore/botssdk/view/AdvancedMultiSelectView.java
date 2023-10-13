package kore.botssdk.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import kore.botssdk.R;
import kore.botssdk.adapter.AdvanceMultiSelectCollectionsAdapter;
import kore.botssdk.adapter.AdvancedMultiSelectAdapter;
import kore.botssdk.listener.AdvanceMultiSelectListner;
import kore.botssdk.listener.ComposeFooterInterface;
import kore.botssdk.listener.InvokeGenericWebViewInterface;
import kore.botssdk.models.AdvanceMultiSelectCollectionModel;
import kore.botssdk.models.PayloadInner;

@SuppressLint("UnknownNullness")
public class AdvancedMultiSelectView extends LinearLayout implements AdvanceMultiSelectListner {
    private AutoExpandListView autoExpandListView;
    View multiSelectLayout;
    AdvancedMultiSelectAdapter multiSelectButtonAdapter;
    TextView tvViewMore, tvAdvanceDone;
    ArrayList<AdvanceMultiSelectCollectionModel> allCheckedItems = new ArrayList<>();
    public AdvancedMultiSelectView(Context context) {
        super(context);
        init(context);
    }

    public InvokeGenericWebViewInterface getInvokeGenericWebViewInterface() {
        return invokeGenericWebViewInterface;
    }


    public void setInvokeGenericWebViewInterface(InvokeGenericWebViewInterface invokeGenericWebViewInterface) {
        this.invokeGenericWebViewInterface = invokeGenericWebViewInterface;
    }

    public ComposeFooterInterface getComposeFooterInterface() {
        return composeFooterInterface;
    }

    public void setComposeFooterInterface(ComposeFooterInterface composeFooterInterface) {
        this.composeFooterInterface = composeFooterInterface;
    }

    private InvokeGenericWebViewInterface invokeGenericWebViewInterface;
    ComposeFooterInterface composeFooterInterface;

    private void init(Context context) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.advance_multi_select_view, this, true);
        autoExpandListView = view.findViewById(R.id.multi_select_list);
        LinearLayout llAdvanceSection = view.findViewById(R.id.llAdvanceSection);
        autoExpandListView.setVerticalScrollBarEnabled(false);
        multiSelectLayout = view.findViewById(R.id.multi_select_layout);
        tvViewMore = view.findViewById(R.id.tvViewMore);
        tvAdvanceDone = view.findViewById(R.id.tvAdvanceDone);
    }


    public void populateData(final PayloadInner payloadInner, boolean isEnabled) {

        if (payloadInner != null && payloadInner.getAdvancedMultiSelectModels() != null && payloadInner.getAdvancedMultiSelectModels().size() > 0)
        {
            multiSelectLayout.setVisibility(VISIBLE);

            multiSelectButtonAdapter = new AdvancedMultiSelectAdapter(getContext());
            multiSelectButtonAdapter.setMultiSelectModels(payloadInner.getAdvancedMultiSelectModels());
            multiSelectButtonAdapter.setEnabled(isEnabled);
            multiSelectButtonAdapter.setComposeFooterInterface(composeFooterInterface);
            multiSelectButtonAdapter.setAdvanceMultiListner(AdvancedMultiSelectView.this);
            autoExpandListView.setAdapter(multiSelectButtonAdapter);

            if(payloadInner.getAdvancedMultiSelectModels().size() > payloadInner.getLimit())
                tvViewMore.setVisibility(VISIBLE);

            tvViewMore.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    multiSelectButtonAdapter.refresh();
                    tvViewMore.setVisibility(GONE);
                }
            });

            tvAdvanceDone.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    StringBuilder stringBuffer = new StringBuilder();
                    stringBuffer.append("Here are the selected items : ");
                    for (int i = 0; i < allCheckedItems.size(); i++)
                    {
                        stringBuffer.append(allCheckedItems.get(i).getValue());

                        if(i != allCheckedItems.size())
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

        if(allCheckedItems.size() > 0)
            tvAdvanceDone.setVisibility(VISIBLE);
        else
            tvAdvanceDone.setVisibility(GONE);
    }

    @Override
    public void allItemsSelected(boolean addOrRemove, ArrayList<AdvanceMultiSelectCollectionModel> checkedItems) {
        if(addOrRemove)
        {
            allCheckedItems.addAll(checkedItems);
        }
        else
        {
            for (int i = 0; i < checkedItems.size(); i++)
            {
                allCheckedItems.remove(checkedItems.get(i));
            }
        }

        if(allCheckedItems.size() > 0)
            tvAdvanceDone.setVisibility(VISIBLE);
        else
            tvAdvanceDone.setVisibility(GONE);
    }
}
