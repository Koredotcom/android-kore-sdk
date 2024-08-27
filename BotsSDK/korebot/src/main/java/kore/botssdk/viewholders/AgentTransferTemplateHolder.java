package kore.botssdk.viewholders;

import static android.view.View.GONE;

import android.view.View;
import android.view.ViewGroup;
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
import kore.botssdk.itemdecoration.VerticalSpaceItemDecoration;
import kore.botssdk.models.BaseBotMessage;
import kore.botssdk.models.PayloadInner;
import kore.botssdk.utils.StringUtils;
import kore.botssdk.view.AgentTransferTemplateView;
import kore.botssdk.view.viewUtils.CircleTransform;

public class AgentTransferTemplateHolder extends BaseViewHolder {
    private final TextView tvAgentCardText;
    private final ImageView ivAgentImage;
    private final TextView tvAgentName;
    private final TextView tvAgentRole;
    private final CircleTransform circleTransform;
    private final RecyclerView rvAgentButtons;
    private final LinearLayout llAgentDetails;

    public static AgentTransferTemplateHolder getInstance(ViewGroup parent) {
        return new AgentTransferTemplateHolder(createView(R.layout.agent_transfer_template, parent));
    }

    private AgentTransferTemplateHolder(@NonNull View itemView) {
        super(itemView, itemView.getContext());
        tvAgentCardText = itemView.findViewById(R.id.tvAgentCardText);
        ivAgentImage = itemView.findViewById(R.id.ivAgentImage);
        tvAgentName = itemView.findViewById(R.id.tvAgentName);
        tvAgentRole = itemView.findViewById(R.id.tvAgentRole);
        rvAgentButtons = itemView.findViewById(R.id.rvAgentButtons);
        llAgentDetails = itemView.findViewById(R.id.llAgentDetails);
        circleTransform = new CircleTransform();
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(3, LinearLayoutManager.HORIZONTAL);
        rvAgentButtons.setLayoutManager(staggeredGridLayoutManager);
        rvAgentButtons.setItemAnimator(new DefaultItemAnimator());
        rvAgentButtons.addItemDecoration(new VerticalSpaceItemDecoration(15));
    }

    @Override
    public void bind(BaseBotMessage baseBotMessage) {
        PayloadInner payloadInner = getPayloadInner(baseBotMessage);
        if (payloadInner == null) return;
        if (payloadInner.getButtons() != null && payloadInner.getButtons().size() > 0) {
            rvAgentButtons.setVisibility(View.VISIBLE);
            llAgentDetails.setVisibility(GONE);

            AgentQuickOptionsTemplateAdapter quickRepliesAdapter;
            if (rvAgentButtons.getAdapter() == null) {
                quickRepliesAdapter = new AgentQuickOptionsTemplateAdapter(itemView.getContext(), rvAgentButtons);
                rvAgentButtons.setAdapter(quickRepliesAdapter);
                quickRepliesAdapter.setInvokeGenericWebViewInterface(invokeGenericWebViewInterface);
                quickRepliesAdapter.setComposeFooterInterface(composeFooterInterface);
            }

            quickRepliesAdapter = (AgentQuickOptionsTemplateAdapter) rvAgentButtons.getAdapter();
            quickRepliesAdapter.setAgentQuickReplyTemplateArrayList(payloadInner.getButtons());
            quickRepliesAdapter.notifyItemRangeChanged(0, payloadInner.getButtons().size());
        } else {
            rvAgentButtons.setVisibility(GONE);
            llAgentDetails.setVisibility(View.VISIBLE);
            tvAgentCardText.setText(payloadInner.getText());
            tvAgentName.setText(payloadInner.getTitle());
            tvAgentRole.setText(payloadInner.getSubtitle());

            if (!StringUtils.isNullOrEmpty(payloadInner.getImage_url())) {
                ivAgentImage.setVisibility(View.VISIBLE);
                Picasso.get().load(payloadInner.getImage_url()).transform(circleTransform).into(ivAgentImage);
            }
        }
    }
}
