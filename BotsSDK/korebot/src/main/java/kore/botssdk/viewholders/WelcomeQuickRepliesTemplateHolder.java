package kore.botssdk.viewholders;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import java.util.ArrayList;

import kore.botssdk.R;
import kore.botssdk.adapter.QuickRepliesTemplateAdapter;
import kore.botssdk.itemdecoration.VerticalSpaceItemDecoration;
import kore.botssdk.models.BaseBotMessage;
import kore.botssdk.models.PayloadInner;
import kore.botssdk.models.QuickReplyTemplate;

public class WelcomeQuickRepliesTemplateHolder extends BaseViewHolder {
    private final RecyclerView recyclerView;

    public static WelcomeQuickRepliesTemplateHolder getInstance(ViewGroup parent) {
        return new WelcomeQuickRepliesTemplateHolder(createView(R.layout.template_welcome_quick_replies, parent));
    }

    private WelcomeQuickRepliesTemplateHolder(@NonNull View itemView) {
        super(itemView, itemView.getContext());
        LinearLayoutCompat layoutBubble = itemView.findViewById(R.id.layoutBubble);
        initBubbleText(layoutBubble, false);
        recyclerView = itemView.findViewById(R.id.replies);
        recyclerView.setClipToPadding(false);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new VerticalSpaceItemDecoration(15));
    }

    @Override
    public void bind(BaseBotMessage baseBotMessage) {
        PayloadInner payloadInner = getPayloadInner(baseBotMessage);
        if (payloadInner == null) return;
        setResponseText(itemView.findViewById(R.id.layoutBubble), payloadInner.getText());
        ArrayList<QuickReplyTemplate> quickReplyTemplates = payloadInner.getQuick_replies();
        StaggeredGridLayoutManager staggeredGridLayoutManager;

        if (quickReplyTemplates.size() / 2 > 0)
            staggeredGridLayoutManager = new StaggeredGridLayoutManager(quickReplyTemplates.size() / 2, LinearLayoutManager.HORIZONTAL);
        else
            staggeredGridLayoutManager = new StaggeredGridLayoutManager(quickReplyTemplates.size(), LinearLayoutManager.HORIZONTAL);

        recyclerView.setLayoutManager(staggeredGridLayoutManager);

        QuickRepliesTemplateAdapter quickRepliesAdapter = null;
        if (recyclerView.getAdapter() == null) {
            quickRepliesAdapter = new QuickRepliesTemplateAdapter(itemView.getContext(), recyclerView, isLastItem());
            quickRepliesAdapter.setInvokeGenericWebViewInterface(invokeGenericWebViewInterface);
            quickRepliesAdapter.setComposeFooterInterface(composeFooterInterface);
        }
        quickRepliesAdapter = (QuickRepliesTemplateAdapter) recyclerView.getAdapter();
        quickRepliesAdapter.setQuickReplyTemplateArrayList(quickReplyTemplates);
        recyclerView.setAdapter(quickRepliesAdapter);
    }
}
