package com.kore.korebot;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import kore.botssdk.listener.ComposeFooterInterface;
import kore.botssdk.listener.InvokeGenericWebViewInterface;
import kore.botssdk.view.CustomTemplateView;

/**
 * Copyright (c) 2014 Kore Inc. All rights reserved.
 */
public class BotButtonView extends CustomTemplateView {

    RecyclerView autoExpandListView;
    ComposeFooterInterface composeFooterInterface;
    InvokeGenericWebViewInterface invokeGenericWebViewInterface;

    private Context context;

    public BotButtonView(Context context) {
        super(context);
        init(context);
    }

    @Override
    public void populateTemplate(String payloadInner, boolean isLast) {
//        if(payloadInner != null && payloadInner.getButtons() != null)
//        {
//            final BotButtonTemplateAdapter buttonTypeAdapter;
//            autoExpandListView.setVisibility(VISIBLE);
//            buttonTypeAdapter = new BotButtonTemplateAdapter(getContext(), BotResponse.TEMPLATE_TYPE_LIST);
//            buttonTypeAdapter.setEnabled(isLast);
//            autoExpandListView.setAdapter(buttonTypeAdapter);
//            buttonTypeAdapter.setBotButtonModels(payloadInner.getButtons());
//            buttonTypeAdapter.setComposeFooterInterface(composeFooterInterface);
//            buttonTypeAdapter.setInvokeGenericWebViewInterface(invokeGenericWebViewInterface);
//            buttonTypeAdapter.notifyItemRangeChanged(0, (payloadInner.getButtons().size() - 1));
//        }
    }

    @Override
    public CustomTemplateView getNewInstance() {
        return new BotButtonView(context);
    }

    private void init(Context context) {
        this.context = context;
        View inflatedView = LayoutInflater.from(getContext()).inflate(R.layout.button_view, this, true);
        autoExpandListView = inflatedView.findViewById(R.id.botCustomButtonList);
    }

    @Override
    public void setComposeFooterInterface(ComposeFooterInterface composeFooterInterface) {
        this.composeFooterInterface = composeFooterInterface;
    }

    @Override
    public void setInvokeGenericWebViewInterface(InvokeGenericWebViewInterface invokeGenericWebViewInterface) {
        this.invokeGenericWebViewInterface = invokeGenericWebViewInterface;
    }
}
