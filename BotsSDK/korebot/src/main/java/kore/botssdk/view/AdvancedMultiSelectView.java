package kore.botssdk.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import kore.botssdk.R;
import kore.botssdk.adapter.AdvancedMultiSelectAdapter;
import kore.botssdk.listener.ComposeFooterInterface;
import kore.botssdk.listener.InvokeGenericWebViewInterface;
import kore.botssdk.listener.MultiSelectAllListner;
import kore.botssdk.models.AdvanceMultiSelectCollectionModel;
import kore.botssdk.models.PayloadInner;
import kore.botssdk.view.viewUtils.DimensionUtil;

@SuppressLint("UnknownNullness")
public class AdvancedMultiSelectView extends LinearLayout {
    private AutoExpandListView autoExpandListView;
    private View multiSelectLayout;
    AdvancedMultiSelectAdapter multiSelectButtonAdapter;
    TextView tvViewMore;

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
        autoExpandListView.setVerticalScrollBarEnabled(false);
        multiSelectLayout = view.findViewById(R.id.multi_select_layout);
        tvViewMore = view.findViewById(R.id.tvViewMore);
        float dp1 = (int) DimensionUtil.dp1;
    }


    public void setMultiSelectLayoutAlpha(boolean isEnabled) {
        multiSelectLayout.setAlpha(isEnabled ? 1.0f : 0.5f);
        multiSelectButtonAdapter.setEnabled(isEnabled);
    }

    public void populateData(final PayloadInner payloadInner, boolean isEnabled) {

        if (payloadInner != null && payloadInner.getAdvancedMultiSelectModels() != null && payloadInner.getAdvancedMultiSelectModels().size() > 0)
        {
            multiSelectLayout.setVisibility(VISIBLE);

            ArrayList<AdvanceMultiSelectCollectionModel> items = new ArrayList<>();
            multiSelectButtonAdapter = new AdvancedMultiSelectAdapter(getContext());
            multiSelectButtonAdapter.setMultiSelectModels(payloadInner.getAdvancedMultiSelectModels());
            multiSelectButtonAdapter.setEnabled(isEnabled);
            multiSelectButtonAdapter.setComposeFooterInterface(composeFooterInterface);
            autoExpandListView.setAdapter(multiSelectButtonAdapter);

//            if(payloadInner.getCheckedParentItems() != null) {
//                multiSelectButtonAdapter.setCheckAll(payloadInner.getCheckedParentItems());
//
//                if(items != null && payloadInner.getCheckedParentItems().size() == items.size())
//                    check_select_all.setChecked(true);
//            }

            multiSelectLayout.setAlpha(isEnabled ? 1.0f : 0.5f);

            if(payloadInner.getAdvancedMultiSelectModels().size() > payloadInner.getLimit())
                tvViewMore.setVisibility(VISIBLE);

            tvViewMore.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    multiSelectButtonAdapter.refresh();
                    tvViewMore.setVisibility(GONE);
                }
            });

        } else {
            autoExpandListView.setAdapter(null);
            multiSelectLayout.setVisibility(GONE);
        }
    }

    public void setRestrictedMaxWidth(float restrictedMaxWidth) {
    }
}
