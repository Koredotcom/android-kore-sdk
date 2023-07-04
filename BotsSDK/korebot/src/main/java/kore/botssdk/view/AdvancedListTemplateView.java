package kore.botssdk.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;

import kore.botssdk.R;
import kore.botssdk.adapter.AdvancedListAdapter;
import kore.botssdk.application.AppControl;
import kore.botssdk.dialogs.AdvancedListActionSheetFragment;
import kore.botssdk.listener.ComposeFooterInterface;
import kore.botssdk.listener.InvokeGenericWebViewInterface;
import kore.botssdk.models.PayloadInner;
import kore.botssdk.utils.StringUtils;

public class AdvancedListTemplateView extends LinearLayout {

    String LOG_TAG = BotListTemplateView.class.getSimpleName();

    float dp1, layoutItemHeight = 0;
    AutoExpandListView autoExpandListView;
    TextView botCustomListViewButton, botListViewTitle;
    LinearLayout botCustomListRoot;
    float restrictedMaxWidth, restrictedMaxHeight;
    ComposeFooterInterface composeFooterInterface;
    InvokeGenericWebViewInterface invokeGenericWebViewInterface;

    public AdvancedListTemplateView(Context context) {
        super(context);
        init();
    }

    public AdvancedListTemplateView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AdvancedListTemplateView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.advancelist_view, this, true);
        botCustomListRoot = (LinearLayout) findViewById(R.id.botCustomListRoot);
        autoExpandListView = (AutoExpandListView) findViewById(R.id.botCustomListView);
        botCustomListViewButton = (TextView) findViewById(R.id.botCustomListViewButton);
        botListViewTitle = (TextView) findViewById(R.id.botListViewTitle);
        dp1 = (int) AppControl.getInstance().getDimensionUtil().dp1;
        layoutItemHeight = getResources().getDimension(R.dimen.list_item_view_height);
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

            if (payloadInner.getListItems() != null && payloadInner.getListItems().size() > 0) {
                AdvancedListAdapter botListTemplateAdapter;
                if (autoExpandListView.getAdapter() == null) {
                    botListTemplateAdapter = new AdvancedListAdapter(getContext(), autoExpandListView);
                    botListTemplateAdapter.setComposeFooterInterface(composeFooterInterface);
                    botListTemplateAdapter.setInvokeGenericWebViewInterface(invokeGenericWebViewInterface);
                } else {
                    botListTemplateAdapter = (AdvancedListAdapter) autoExpandListView.getAdapter();
                }
                botListTemplateAdapter.dispalyCount(payloadInner.getListItemDisplayCount());
                botListTemplateAdapter.setPayloadInner(payloadInner);
                botListTemplateAdapter.setBotListModelArrayList(payloadInner.getListItems());
                autoExpandListView.setAdapter(botListTemplateAdapter);
                botListTemplateAdapter.notifyDataSetChanged();
                botCustomListRoot.setVisibility(VISIBLE);

                if(payloadInner.getSeeMore().equalsIgnoreCase("true") && payloadInner.getListItemDisplayCount() != 0 && payloadInner.getListItemDisplayCount() < payloadInner.getListItems().size())
                {
                    botCustomListViewButton.setVisibility(VISIBLE);
                    botCustomListViewButton.setText("See More");
                    botCustomListViewButton.setTextColor(getContext().getColor(R.color.color_blue_1_1));
                }

                botCustomListViewButton.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AdvancedListActionSheetFragment bottomSheetDialog = new AdvancedListActionSheetFragment();
                        bottomSheetDialog.setTitle(payloadInner.getTitle());
                        bottomSheetDialog.setSkillName("skillName","trigger");
                        bottomSheetDialog.setData(payloadInner.getListItems());
                        bottomSheetDialog.setComposeFooterInterface(composeFooterInterface);
                        bottomSheetDialog.setInvokeGenericWebViewInterface(invokeGenericWebViewInterface);
                        bottomSheetDialog.show(((FragmentActivity) getContext()).getSupportFragmentManager(), "add_tags");
                    }
                });

            } else {
                botCustomListRoot.setVisibility(GONE);
                botCustomListViewButton.setVisibility(GONE);
            }
        }
    }

    public void setRestrictedMaxHeight(float restrictedMaxHeight) {
        this.restrictedMaxHeight = restrictedMaxHeight;
    }

    public void setRestrictedMaxWidth(float restrictedMaxWidth) {
        this.restrictedMaxWidth = restrictedMaxWidth;
    }

    public void setComposeFooterInterface(ComposeFooterInterface composeFooterInterface) {
        this.composeFooterInterface = composeFooterInterface;
    }

    public void setInvokeGenericWebViewInterface(InvokeGenericWebViewInterface invokeGenericWebViewInterface) {
        this.invokeGenericWebViewInterface = invokeGenericWebViewInterface;
    }

    public int getViewHeight() {
        int viewHeight = 0;
        if (autoExpandListView != null) {
            int count = 0;
            if (autoExpandListView.getAdapter() != null) {
                count = autoExpandListView.getAdapter().getCount();
            }
            viewHeight = (int) (layoutItemHeight * count);
        }
        return viewHeight;
    }

    public int getViewWidth() {
        int viewHeight = 0;
        if (autoExpandListView != null) {
            int count = 0;
            if (autoExpandListView.getAdapter() != null) {
                count = autoExpandListView.getAdapter().getCount();
            }
            viewHeight = (count > 0) ? (int) (restrictedMaxWidth -2*dp1) : 0;
        }
        return viewHeight;
    }
}