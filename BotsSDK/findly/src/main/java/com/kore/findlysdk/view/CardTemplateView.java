package com.kore.findlysdk.view;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.RecyclerView;

import com.kore.findlysdk.R;
import com.kore.findlysdk.adapters.CardTemplateAdapter;
import com.kore.findlysdk.listners.ComposeFooterInterface;
import com.kore.findlysdk.listners.InvokeGenericWebViewInterface;
import com.kore.findlysdk.models.CardsTemplateModel;
import com.kore.findlysdk.models.PayloadInner;
import com.kore.findlysdk.utils.AppControl;

import java.util.ArrayList;

public class CardTemplateView extends LinearLayout
{
    float dp1, layoutItemHeight = 0;
    float restrictedMaxWidth, restrictedMaxHeight;
    private ComposeFooterInterface composeFooterInterface;
    private InvokeGenericWebViewInterface invokeGenericWebViewInterface;
    private AutoExpandListView alvCards;
    int maxWidth, listViewHeight;
    private CardTemplateAdapter cardTemplateAdapter;

    public CardTemplateView(Context context) {
        super(context);
        init();
    }

    public CardTemplateView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CardTemplateView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.card_template_view, this, true);
        alvCards = (AutoExpandListView) findViewById(R.id.alvCards);
        dp1 = (int) AppControl.getInstance().getDimensionUtil().dp1;
        layoutItemHeight = getResources().getDimension(R.dimen.list_item_view_height);
        listViewHeight = (int) AppControl.getInstance().getDimensionUtil().screenWidth;
    }

    public void populateCardTemplateView(PayloadInner payloadInner)
    {
        if (payloadInner != null)
        {
            if(payloadInner.getArrCardsTemplateModels() != null && payloadInner.getArrCardsTemplateModels().size() > 0)
            {
                alvCards.setVisibility(View.VISIBLE);
                cardTemplateAdapter = new CardTemplateAdapter(getContext(), payloadInner.getArrCardsTemplateModels());
                cardTemplateAdapter.setComposeFooterInterface(composeFooterInterface);
                alvCards.setAdapter(cardTemplateAdapter);
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
}
