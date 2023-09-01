package kore.botssdk.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

import kore.botssdk.R;
import kore.botssdk.adapter.BotButtonTemplateAdapter;
import kore.botssdk.listener.ComposeFooterInterface;
import kore.botssdk.listener.InvokeGenericWebViewInterface;
import kore.botssdk.models.BotButtonModel;
import kore.botssdk.view.viewUtils.DimensionUtil;
import kore.botssdk.view.viewUtils.LayoutUtils;
import kore.botssdk.view.viewUtils.MeasureUtils;

/**
 * Created by Pradeep Mahato on 21/7/17.
 * Copyright (c) 2014 Kore Inc. All rights reserved.
 */
public class BotButtonView extends ViewGroup {

    float dp1, layoutItemHeight = 0;
    ListView autoExpandListView;
    float restrictedMaxWidth, restrictedMaxHeight;
    ComposeFooterInterface composeFooterInterface;
    InvokeGenericWebViewInterface invokeGenericWebViewInterface;

    public BotButtonView(Context context) {
        super(context);
        init();
    }

    public BotButtonView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BotButtonView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        dp1 = DimensionUtil.dp1;
        View inflatedView = LayoutInflater.from(getContext()).inflate(R.layout.bot_custom_button_view, this, true);
        autoExpandListView = inflatedView.findViewById(R.id.botCustomButtonList);

        layoutItemHeight = getResources().getDimension(R.dimen.carousel_view_button_height_individual);
//        setBackgroundColor(0xffffffff);
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
        if (botButtonModels != null) {
            autoExpandListView.setVisibility(VISIBLE);
            buttonTypeAdapter = new BotButtonTemplateAdapter(getContext());
            buttonTypeAdapter.setEnabled(enabled);
            autoExpandListView.setAdapter(buttonTypeAdapter);
//            autoExpandListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                @Override
//                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                    if (composeFooterInterface != null && invokeGenericWebViewInterface != null && buttonTypeAdapter.isEnabled()) {
//
//                        BotButtonModel botButtonModel = ((BotButtonModel) parent.getItemAtPosition(position));
//                        if (BundleConstants.BUTTON_TYPE_WEB_URL.equalsIgnoreCase(botButtonModel.getType())) {
//                            invokeGenericWebViewInterface.invokeGenericWebView(botButtonModel.getUrl());
//                        }
//                        else if(BundleConstants.BUTTON_TYPE_URL.equalsIgnoreCase(botButtonModel.getType())) {
//                            invokeGenericWebViewInterface.invokeGenericWebView(botButtonModel.getUrl());
//                        }else if(BundleConstants.BUTTON_TYPE_USER_INTENT.equalsIgnoreCase(botButtonModel.getType())){
//                            buttonTypeAdapter.setEnabled(false);
//                            invokeGenericWebViewInterface.handleUserActions(botButtonModel.getAction(),botButtonModel.getCustomData());
//                        }else{
//                            buttonTypeAdapter.setEnabled(false);
//                            String title = botButtonModel.getTitle();
//                            String payload = botButtonModel.getPayload();
//                            composeFooterInterface.onSendClick(title, payload,false);
//                        }
//                    }
//                }
//            });
            buttonTypeAdapter.setBotButtonModels(botButtonModels);
            buttonTypeAdapter.setComposeFooterInterface(composeFooterInterface);
            buttonTypeAdapter.setInvokeGenericWebViewInterface(invokeGenericWebViewInterface);
            buttonTypeAdapter.notifyDataSetChanged();
        } else {
            autoExpandListView.setAdapter(null);
            autoExpandListView.setVisibility(GONE);
        }

    }

    private int getViewHeight() {
        int viewHeight = 0;
        if (autoExpandListView != null) {
            int count = 0;
            if (autoExpandListView.getAdapter() != null) {
                count = autoExpandListView.getAdapter().getCount();
            }
            viewHeight = (int) (layoutItemHeight * count)+(int)(count*(3*dp1));
        }
        return viewHeight;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int viewHeight = getViewHeight();
        int viewWidth = (viewHeight == 0) ? 0 : (int) restrictedMaxWidth;
        int childHeightSpec = MeasureSpec.makeMeasureSpec(viewHeight, MeasureSpec.EXACTLY);
        int childWidthSpec = MeasureSpec.makeMeasureSpec(viewWidth, MeasureSpec.EXACTLY);

        MeasureUtils.measure(autoExpandListView, childWidthSpec, childHeightSpec);

        int parentWidthSpec = childWidthSpec;
        int parentHeightSpec = childHeightSpec;

        super.onMeasure(parentWidthSpec, parentHeightSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        final int count = getChildCount();
        int parentWidth = getMeasuredWidth();

        //get the available size of child view
        int childLeft = this.getPaddingLeft();
        int childTop = this.getPaddingTop();

        int itemWidth = (r - l) / getChildCount();

        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() != GONE) {
                LayoutUtils.layoutChild(child, childLeft, childTop);
                childTop += child.getMeasuredHeight();
            }
        }
    }
}
