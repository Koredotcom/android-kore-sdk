package kore.botssdk.view;

import android.content.Context;
import android.graphics.Rect;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import kore.botssdk.R;
import kore.botssdk.adapter.CardTemplateAdapter;
import kore.botssdk.listener.ComposeFooterInterface;
import kore.botssdk.listener.InvokeGenericWebViewInterface;
import kore.botssdk.models.CardTemplateModel;

public class CardTemplateView extends LinearLayout {

    private final RecyclerView rvCards;
    private ComposeFooterInterface composeFooterInterface;
    private InvokeGenericWebViewInterface invokeGenericWebViewInterface;

    public CardTemplateView(Context context)
    {
        super(context);
        View view =  LayoutInflater.from(getContext()).inflate(R.layout.card_template_view, this, true);
        rvCards = view.findViewById(R.id.rvCardTemplate);
    }

    public void populateCardsView(ArrayList<CardTemplateModel> arrCards)
    {
        if(arrCards != null)
        {
            rvCards.setLayoutManager(new LinearLayoutManager(getContext()));
            rvCards.addItemDecoration(new VerticalSpaceItemDecoration(10));
            rvCards.setAdapter(new CardTemplateAdapter(getContext(), arrCards));
        }
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
