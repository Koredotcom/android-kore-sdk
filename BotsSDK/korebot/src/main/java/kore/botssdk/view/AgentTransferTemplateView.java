package kore.botssdk.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.squareup.picasso.Picasso;

import kore.botssdk.R;
import kore.botssdk.adapter.AgentQuickOptionsTemplateAdapter;
import kore.botssdk.application.AppControl;
import kore.botssdk.listener.ComposeFooterInterface;
import kore.botssdk.listener.InvokeGenericWebViewInterface;
import kore.botssdk.models.PayloadInner;
import kore.botssdk.utils.StringUtils;
import kore.botssdk.view.viewUtils.CircleTransform;
import kore.botssdk.view.viewUtils.DimensionUtil;

@SuppressLint("UnknownNullness")
public class AgentTransferTemplateView extends LinearLayout
{
    private TextView tvAgentCardText;
    private ImageView ivAgentImage;
    private TextView tvAgentName;
    private TextView tvAgentRole;
    float dp1, layoutItemHeight = 0;
    private CircleTransform circleTransform;
    float restrictedMaxWidth;
    ComposeFooterInterface composeFooterInterface;
    InvokeGenericWebViewInterface invokeGenericWebViewInterface;
    private RecyclerView rvAgentButtons;
    private LinearLayout llAgentDetails;
    int listViewHeight;

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
        LayoutInflater.from(getContext()).inflate(R.layout.agent_transfer_template, this, true);
        tvAgentCardText = findViewById(R.id.tvAgentCardText);
        ivAgentImage = findViewById(R.id.ivAgentImage);
        tvAgentName = findViewById(R.id.tvAgentName);
        tvAgentRole = findViewById(R.id.tvAgentRole);
        rvAgentButtons = findViewById(R.id.rvAgentButtons);
        llAgentDetails = findViewById(R.id.llAgentDetails);
        circleTransform = new CircleTransform();

        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(3, LinearLayoutManager.HORIZONTAL);
        rvAgentButtons.setLayoutManager(staggeredGridLayoutManager);
        rvAgentButtons.setItemAnimator(new DefaultItemAnimator());
        rvAgentButtons.addItemDecoration(new VerticalSpaceItemDecoration(15));

        dp1 = (int) DimensionUtil.dp1;
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

                AgentQuickOptionsTemplateAdapter quickRepliesAdapter;
                if (rvAgentButtons.getAdapter() == null) {
                    quickRepliesAdapter = new AgentQuickOptionsTemplateAdapter(getContext(), rvAgentButtons);
                    rvAgentButtons.setAdapter(quickRepliesAdapter);
                    quickRepliesAdapter.setInvokeGenericWebViewInterface(invokeGenericWebViewInterface);
                    quickRepliesAdapter.setComposeFooterInterface(composeFooterInterface);
                }

                quickRepliesAdapter = (AgentQuickOptionsTemplateAdapter) rvAgentButtons.getAdapter();
                quickRepliesAdapter.setAgentQuickReplyTemplateArrayList(payloadInner.getButtons());
                quickRepliesAdapter.notifyItemRangeChanged(0, payloadInner.getButtons().size());
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

    public void setRestrictedMaxWidth(float restrictedMaxWidth) {
        this.restrictedMaxWidth = restrictedMaxWidth;
    }

    public void setComposeFooterInterface(ComposeFooterInterface composeFooterInterface) {
        this.composeFooterInterface = composeFooterInterface;
    }

    public void setInvokeGenericWebViewInterface(InvokeGenericWebViewInterface invokeGenericWebViewInterface) {
        this.invokeGenericWebViewInterface = invokeGenericWebViewInterface;
    }

    public static class VerticalSpaceItemDecoration extends RecyclerView.ItemDecoration {

        private final int verticalSpaceHeight;

        public VerticalSpaceItemDecoration(int verticalSpaceHeight) {
            this.verticalSpaceHeight = verticalSpaceHeight;
        }

        @Override
        public void getItemOffsets(Rect outRect, @NonNull View view, @NonNull RecyclerView parent,
                                   @NonNull RecyclerView.State state) {
            outRect.bottom = verticalSpaceHeight;
        }
    }
}
