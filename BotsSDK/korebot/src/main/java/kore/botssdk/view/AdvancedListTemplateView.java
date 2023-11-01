package kore.botssdk.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;

import kore.botssdk.R;
import kore.botssdk.adapter.AdvancedListAdapter;
import kore.botssdk.dialogs.AdvancedListActionSheetFragment;
import kore.botssdk.listener.ComposeFooterInterface;
import kore.botssdk.listener.InvokeGenericWebViewInterface;
import kore.botssdk.models.PayloadInner;
import kore.botssdk.utils.StringUtils;
import kore.botssdk.view.viewUtils.DimensionUtil;

public class AdvancedListTemplateView extends LinearLayout {
    final float dp1;
    final AutoExpandListView autoExpandListView;
    final TextView botCustomListViewButton;
    final TextView botListViewTitle;
    final TextView tvDescription;
    final LinearLayout botCustomListRoot;
    ComposeFooterInterface composeFooterInterface;
    InvokeGenericWebViewInterface invokeGenericWebViewInterface;
    private final ImageView ivSorting;
    private AdvancedListAdapter botListTemplateAdapter;

    public AdvancedListTemplateView(Context context)
    {
        super(context);
        LayoutInflater.from(getContext()).inflate(R.layout.advancelist_view, this, true);
        botCustomListRoot = findViewById(R.id.botCustomListRoot);
        autoExpandListView = findViewById(R.id.botCustomListView);
        botCustomListViewButton = findViewById(R.id.botCustomListViewButton);
        botListViewTitle = findViewById(R.id.botListViewTitle);
        tvDescription = findViewById(R.id.tvDescription);
        ivSorting = findViewById(R.id.ivSorting);

        dp1 = (int) DimensionUtil.dp1;
        botCustomListViewButton.setVisibility(GONE);
    }

    public void populateAdvancedListTemplateView(PayloadInner payloadInner) {
        if(payloadInner != null)
        {
            if(!StringUtils.isNullOrEmpty(payloadInner.getTitle()))
            {
                botListViewTitle.setVisibility(VISIBLE);
                botListViewTitle.setText(payloadInner.getTitle());
            }

            if(!StringUtils.isNullOrEmpty(payloadInner.getDescription()))
            {
                tvDescription.setVisibility(VISIBLE);
                tvDescription.setText(payloadInner.getDescription());
            }

            if (payloadInner.getListItems() != null && payloadInner.getListItems().size() > 0)
            {

                if (autoExpandListView.getAdapter() == null)
                {
                    botListTemplateAdapter = new AdvancedListAdapter(getContext(), autoExpandListView);
                    botListTemplateAdapter.setComposeFooterInterface(composeFooterInterface);
                    botListTemplateAdapter.setInvokeGenericWebViewInterface(invokeGenericWebViewInterface);
                } else {
                    botListTemplateAdapter = (AdvancedListAdapter) autoExpandListView.getAdapter();
                }
                botListTemplateAdapter.dispalyCount(payloadInner.getListItemDisplayCount());
                botListTemplateAdapter.setBotListModelArrayList(payloadInner.getListItems());
                botListTemplateAdapter.notifyDataSetChanged();
                autoExpandListView.setAdapter(botListTemplateAdapter);
                botCustomListRoot.setVisibility(VISIBLE);

                if(!StringUtils.isNullOrEmpty(payloadInner.getSeeMoreTitle()) && payloadInner.getListItemDisplayCount() != 0 && payloadInner.getListItemDisplayCount() < payloadInner.getListItems().size())
                {
                    botCustomListViewButton.setVisibility(VISIBLE);
                    botCustomListViewButton.setText(payloadInner.getSeeMoreTitle());
                    botCustomListViewButton.setTextColor(getContext().getColor(R.color.color_blue_1_1));
                }

                botCustomListViewButton.setOnClickListener(v -> {
                    AdvancedListActionSheetFragment bottomSheetDialog = new AdvancedListActionSheetFragment();
                    bottomSheetDialog.setTitle(payloadInner.getTitle());
                    bottomSheetDialog.setSkillName("skillName","trigger");
                    bottomSheetDialog.setData(payloadInner.getListItems());
                    bottomSheetDialog.setComposeFooterInterface(composeFooterInterface);
                    bottomSheetDialog.setInvokeGenericWebViewInterface(invokeGenericWebViewInterface);
                    bottomSheetDialog.show(((FragmentActivity) getContext()).getSupportFragmentManager(), "add_tags");
                });

            } else {
                botCustomListRoot.setVisibility(GONE);
                botCustomListViewButton.setVisibility(GONE);
            }
        }
    }

    public void setComposeFooterInterface(ComposeFooterInterface composeFooterInterface) {
        this.composeFooterInterface = composeFooterInterface;
    }

    public void setInvokeGenericWebViewInterface(InvokeGenericWebViewInterface invokeGenericWebViewInterface) {
        this.invokeGenericWebViewInterface = invokeGenericWebViewInterface;
    }

}