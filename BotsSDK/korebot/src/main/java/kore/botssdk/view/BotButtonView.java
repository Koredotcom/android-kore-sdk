package kore.botssdk.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;

import java.util.ArrayList;

import kore.botssdk.R;
import kore.botssdk.activity.WelcomeScreenActivity;
import kore.botssdk.adapter.BotButtonTemplateAdapter;
import kore.botssdk.listener.ComposeFooterInterface;
import kore.botssdk.listener.InvokeGenericWebViewInterface;
import kore.botssdk.models.BotButtonModel;
import kore.botssdk.models.BotResponse;
import kore.botssdk.view.viewUtils.DimensionUtil;
import kore.botssdk.view.viewUtils.LayoutUtils;
import kore.botssdk.view.viewUtils.MeasureUtils;

/**
 * Created by Pradeep Mahato on 21/7/17.
 * Copyright (c) 2014 Kore Inc. All rights reserved.
 */
public class BotButtonView extends LinearLayout {

    float dp1, layoutItemHeight = 0;
    RecyclerView autoExpandListView;
    float restrictedMaxWidth;
    ComposeFooterInterface composeFooterInterface;
    InvokeGenericWebViewInterface invokeGenericWebViewInterface;

    public BotButtonView(Context context) {
        super(context);
        init(context);
    }

    public BotButtonView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public BotButtonView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        dp1 = DimensionUtil.dp1;
        View inflatedView = LayoutInflater.from(getContext()).inflate(R.layout.bot_custom_button_view, this, true);
        autoExpandListView = inflatedView.findViewById(R.id.botCustomButtonList);

        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(context);
        layoutManager.setFlexDirection(FlexDirection.ROW);
        layoutManager.setJustifyContent(JustifyContent.FLEX_START);
        autoExpandListView.setLayoutManager(layoutManager);

        layoutItemHeight = getResources().getDimension(R.dimen.carousel_view_button_height_individual);
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

    public void populateButtonList(ArrayList<BotButtonModel> botButtonModels, final boolean enabled) {
        final BotButtonTemplateAdapter buttonTypeAdapter;
        if (botButtonModels != null)
        {
            autoExpandListView.setVisibility(VISIBLE);
            buttonTypeAdapter = new BotButtonTemplateAdapter(getContext(), BotResponse.TEMPLATE_TYPE_BUTTON);
            buttonTypeAdapter.setEnabled(enabled);
            autoExpandListView.setAdapter(buttonTypeAdapter);
            buttonTypeAdapter.setBotButtonModels(botButtonModels);
            buttonTypeAdapter.setComposeFooterInterface(composeFooterInterface);
            buttonTypeAdapter.setInvokeGenericWebViewInterface(invokeGenericWebViewInterface);
            buttonTypeAdapter.notifyItemRangeChanged(0, (botButtonModels.size() - 1));
        } else {
            autoExpandListView.setAdapter(null);
            autoExpandListView.setVisibility(GONE);
        }
    }
}
