package com.kore.findlysdk.view;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.kore.findlysdk.R;
import com.kore.findlysdk.adapters.AgentQuickOptionsTemplateAdapter;
import com.kore.findlysdk.listners.ComposeFooterInterface;
import com.kore.findlysdk.listners.InvokeGenericWebViewInterface;
import com.kore.findlysdk.models.PayloadInner;
import com.kore.findlysdk.utils.AppControl;
import com.kore.findlysdk.utils.StringUtils;
import com.kore.findlysdk.view.viewUtils.CircleTransform;
import com.squareup.picasso.Picasso;

public class AgentTransferTemplateView extends LinearLayout
{
    private TextView tvAgentCardText;
    private ImageView ivAgentImage;
    private TextView tvAgentName;
    private TextView tvAgentRole;
    float dp1, layoutItemHeight = 0;
    private CircleTransform circleTransform;
    float restrictedMaxWidth, restrictedMaxHeight;
    ComposeFooterInterface composeFooterInterface;
    InvokeGenericWebViewInterface invokeGenericWebViewInterface;
    private RecyclerView rvAgentButtons;
    private LinearLayout llAgentDetails;
    int maxWidth, listViewHeight;

    public AgentTransferTemplateView(Context context) {
        super(context);
        init();
    }

    public AgentTransferTemplateView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AgentTransferTemplateView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.agent_transfer_findly_template, this, true);
        tvAgentCardText = (TextView) findViewById(R.id.tvAgentCardText);
        ivAgentImage = (ImageView) findViewById(R.id.ivAgentImage);
        tvAgentName = (TextView) findViewById(R.id.tvAgentName);
        tvAgentRole = (TextView) findViewById(R.id.tvAgentRole);
        rvAgentButtons = (RecyclerView) findViewById(R.id.rvAgentButtons);
        llAgentDetails = (LinearLayout) findViewById(R.id.llAgentDetails);
        circleTransform = new CircleTransform();

        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(3, LinearLayoutManager.HORIZONTAL);
        rvAgentButtons.setLayoutManager(staggeredGridLayoutManager);
        rvAgentButtons.setItemAnimator(new DefaultItemAnimator());
        rvAgentButtons.addItemDecoration(new VerticalSpaceItemDecoration(15));

        dp1 = (int) AppControl.getInstance().getDimensionUtil().dp1;
        layoutItemHeight = getResources().getDimension(R.dimen.list_item_view_height);
        listViewHeight = (int) AppControl.getInstance().getDimensionUtil().screenWidth;
    }

    public void populateAgentTemplateView(PayloadInner payloadInner)
    {
        if (payloadInner != null)
        {
            if(payloadInner.getButtons() != null && payloadInner.getButtons().size() > 0)
            {
                rvAgentButtons.setVisibility(View.VISIBLE);
                llAgentDetails.setVisibility(View.GONE);

                AgentQuickOptionsTemplateAdapter quickRepliesAdapter = null;
                if (rvAgentButtons.getAdapter() == null) {
                    quickRepliesAdapter = new AgentQuickOptionsTemplateAdapter(getContext(), rvAgentButtons);
                    rvAgentButtons.setAdapter(quickRepliesAdapter);
                    quickRepliesAdapter.setInvokeGenericWebViewInterface(invokeGenericWebViewInterface);
                    quickRepliesAdapter.setComposeFooterInterface(composeFooterInterface);
                }

                quickRepliesAdapter = (AgentQuickOptionsTemplateAdapter) rvAgentButtons.getAdapter();

                quickRepliesAdapter.setAgentQuickReplyTemplateArrayList(payloadInner.getButtons());
                quickRepliesAdapter.notifyDataSetChanged();
                listViewHeight = (int)(60 * (payloadInner.getButtons().size()/2) * dp1);
            }
            else
            {
                rvAgentButtons.setVisibility(GONE);
                llAgentDetails.setVisibility(View.VISIBLE);
                tvAgentCardText.setText(payloadInner.getText());
                tvAgentName.setText(payloadInner.getTitle());
                tvAgentRole.setText(payloadInner.getSubtitle());

                if(!StringUtils.isNullOrEmpty(payloadInner.getImage_url()))
                {
                    ivAgentImage.setVisibility(View.VISIBLE);
                    Picasso.get().load(payloadInner.getImage_url()).transform(circleTransform).into(ivAgentImage);
                }
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

    public class VerticalSpaceItemDecoration extends RecyclerView.ItemDecoration {

        private final int verticalSpaceHeight;

        public VerticalSpaceItemDecoration(int verticalSpaceHeight) {
            this.verticalSpaceHeight = verticalSpaceHeight;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                                   RecyclerView.State state) {
            outRect.bottom = verticalSpaceHeight;
        }
    }
}
