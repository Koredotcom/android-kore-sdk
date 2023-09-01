package com.kore.korebot;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;

import kore.botssdk.adapter.BotButtonTemplateAdapter;
import kore.botssdk.listener.ComposeFooterInterface;
import kore.botssdk.listener.InvokeGenericWebViewInterface;
import kore.botssdk.models.PayloadInner;
import kore.botssdk.view.CustomTemplateView;

/**
 * Created by Pradeep Mahato on 21/7/17.
 * Copyright (c) 2014 Kore Inc. All rights reserved.
 */
public class BotButtonView extends CustomTemplateView {

    ListView autoExpandListView;
    ComposeFooterInterface composeFooterInterface;
    InvokeGenericWebViewInterface invokeGenericWebViewInterface;

    private Context context;

    public BotButtonView(Context context) {
        super(context);
        init(context);
    }

    @Override
    public void populateTemplate(PayloadInner payloadInner, boolean isLast) {
        if(payloadInner != null && payloadInner.getButtons() != null)
        {
            final BotButtonTemplateAdapter buttonTypeAdapter;
            autoExpandListView.setVisibility(VISIBLE);
            buttonTypeAdapter = new BotButtonTemplateAdapter(getContext());
            buttonTypeAdapter.setEnabled(isLast);
            autoExpandListView.setAdapter(buttonTypeAdapter);
            buttonTypeAdapter.setBotButtonModels(payloadInner.getButtons());
            buttonTypeAdapter.setComposeFooterInterface(composeFooterInterface);
            buttonTypeAdapter.setInvokeGenericWebViewInterface(invokeGenericWebViewInterface);
            buttonTypeAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public CustomTemplateView getNewInstance() {
        return new BotButtonView(context);
    }

    private void init(Context context) {
        this.context = context;
        View inflatedView = LayoutInflater.from(getContext()).inflate(R.layout.button_view, this, true);
        autoExpandListView = (ListView) inflatedView.findViewById(R.id.botCustomButtonList);
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
