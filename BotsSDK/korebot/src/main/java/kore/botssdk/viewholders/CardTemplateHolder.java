package kore.botssdk.viewholders;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import kore.botssdk.R;
import kore.botssdk.adapter.CardTemplateAdapter;
import kore.botssdk.itemdecoration.VerticalSpaceItemDecoration;
import kore.botssdk.models.BaseBotMessage;
import kore.botssdk.models.CardTemplateModel;
import kore.botssdk.models.PayloadInner;

public class CardTemplateHolder extends BaseViewHolder {
    private final RecyclerView rvCards;

    public static CardTemplateHolder getInstance(ViewGroup parent) {
        return new CardTemplateHolder(createView(R.layout.template_card, parent));
    }

    private CardTemplateHolder(@NonNull View itemView) {
        super(itemView, itemView.getContext());
        rvCards = itemView.findViewById(R.id.rvCardTemplate);
        rvCards.setLayoutManager(new LinearLayoutManager(itemView.getContext()));
        rvCards.addItemDecoration(new VerticalSpaceItemDecoration(10));
    }

    @Override
    public void bind(BaseBotMessage baseBotMessage) {
        PayloadInner payloadInner = getPayloadInner(baseBotMessage);
        if (payloadInner == null) return;
        ArrayList<CardTemplateModel> arrCards = payloadInner.getCardsModel();
        CardTemplateAdapter adapter = new CardTemplateAdapter(itemView.getContext(), arrCards, isLastItem());
        adapter.setComposeFooterInterface(composeFooterInterface);
        adapter.setInvokeGenericWebViewInterface(invokeGenericWebViewInterface);
        rvCards.setAdapter(adapter);
    }
}