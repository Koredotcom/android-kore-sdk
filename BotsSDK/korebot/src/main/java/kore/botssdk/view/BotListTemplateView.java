package kore.botssdk.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import kore.botssdk.R;
import kore.botssdk.adapter.BotListTemplateAdapter;
import kore.botssdk.listener.ComposeFooterInterface;
import kore.botssdk.listener.InvokeGenericWebViewInterface;
import kore.botssdk.models.BotButtonModel;
import kore.botssdk.models.BotListModel;
import kore.botssdk.utils.BundleConstants;
import kore.botssdk.view.viewUtils.DimensionUtil;
import kore.botssdk.view.viewUtils.LayoutUtils;
import kore.botssdk.view.viewUtils.MeasureUtils;

/**
 * Created by Pradeep Mahato on 22/7/17.
 * Copyright (c) 2014 Kore Inc. All rights reserved.
 */
public class BotListTemplateView extends ViewGroup {

    String LOG_TAG = BotListTemplateView.class.getSimpleName();

    float dp1, layoutItemHeight = 0;
    AutoExpandListView autoExpandListView;
    TextView botCustomListViewButton;
    LinearLayout botCustomListRoot;
    float restrictedMaxWidth, restrictedMaxHeight;
    ComposeFooterInterface composeFooterInterface;
    InvokeGenericWebViewInterface invokeGenericWebViewInterface;

    public BotListTemplateView(Context context) {
        super(context);
        init();
    }

    public BotListTemplateView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BotListTemplateView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.bot_custom_list, this, true);
        botCustomListRoot = findViewById(R.id.botCustomListRoot);
        autoExpandListView = findViewById(R.id.botCustomListView);
        botCustomListViewButton = findViewById(R.id.botCustomListViewButton);
        dp1 = (int) DimensionUtil.dp1;
        layoutItemHeight = getResources().getDimension(R.dimen.list_item_view_height);

    }

    public void populateListTemplateView(ArrayList<BotListModel> botListModelArrayList, final ArrayList<BotButtonModel> botButtonModelArrayList) {


        if (botListModelArrayList != null && botListModelArrayList.size() > 0) {
            BotListTemplateAdapter botListTemplateAdapter;
            if (autoExpandListView.getAdapter() == null) {
                botListTemplateAdapter = new BotListTemplateAdapter(getContext(), autoExpandListView);
                autoExpandListView.setAdapter(botListTemplateAdapter);
                botListTemplateAdapter.setComposeFooterInterface(composeFooterInterface);
                botListTemplateAdapter.setInvokeGenericWebViewInterface(invokeGenericWebViewInterface);
            } else {
                botListTemplateAdapter = (BotListTemplateAdapter) autoExpandListView.getAdapter();
            }
            botListTemplateAdapter.setBotListModelArrayList(botListModelArrayList);
            botListTemplateAdapter.notifyDataSetChanged();
            botCustomListRoot.setVisibility(VISIBLE);
            if(botButtonModelArrayList != null && botButtonModelArrayList.size() > 0) {
                botCustomListViewButton.setText(botButtonModelArrayList.get(0).getTitle());
                botCustomListViewButton.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (composeFooterInterface != null && invokeGenericWebViewInterface != null) {
                            BotButtonModel botButtonModel = botButtonModelArrayList.get(0);
                            if (BundleConstants.BUTTON_TYPE_WEB_URL.equalsIgnoreCase(botButtonModel.getType())) {
                                invokeGenericWebViewInterface.invokeGenericWebView(botButtonModel.getUrl());
                            } else if (BundleConstants.BUTTON_TYPE_POSTBACK.equalsIgnoreCase(botButtonModel.getType())) {
                                String payload = botButtonModel.getPayload();
                                String message = botCustomListViewButton.getText().toString();
                                composeFooterInterface.onSendClick(message, payload, false);
                            }
                        }
                    }
                });
                botCustomListViewButton.setVisibility(botListModelArrayList.size() > 3 ? VISIBLE : GONE);
            }
        } else {
            botCustomListRoot.setVisibility(GONE);
            botCustomListViewButton.setVisibility(GONE);
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

    public int getViewHeight() {
        int viewHeight = 0;
        if (autoExpandListView != null) {
            int count = 0;
            if (autoExpandListView.getAdapter() != null) {
                count = autoExpandListView.getAdapter().getCount();
            }
            viewHeight = (int) (layoutItemHeight * count);
        }
        return viewHeight;
    }

    public int getViewWidth() {
        int viewHeight = 0;
        if (autoExpandListView != null) {
            int count = 0;
            if (autoExpandListView.getAdapter() != null) {
                count = autoExpandListView.getAdapter().getCount();
            }
            viewHeight = (count > 0) ? (int) (restrictedMaxWidth -2*dp1) : 0;
        }
        return viewHeight;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int wrapSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);

        int totalHeight = getPaddingTop();
        int childWidthSpec;
        int totalWidth = getPaddingLeft();

        childWidthSpec = MeasureSpec.makeMeasureSpec((int) restrictedMaxWidth, MeasureSpec.EXACTLY);
        MeasureUtils.measure(botCustomListRoot, childWidthSpec, wrapSpec);

        totalHeight += botCustomListRoot.getMeasuredHeight() + getPaddingBottom() + getPaddingTop();
        totalWidth += botCustomListRoot.getMeasuredWidth() + getPaddingLeft()+getPaddingRight();
        if(totalHeight != 0){
            totalWidth = totalWidth + (int)(3 * dp1);
        }

        int parentHeightSpec = MeasureSpec.makeMeasureSpec(totalHeight, MeasureSpec.EXACTLY);
        int parentWidthSpec = MeasureSpec.makeMeasureSpec(totalWidth, MeasureSpec.AT_MOST);
        setMeasuredDimension(parentWidthSpec, parentHeightSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        final int count = getChildCount();
        int parentWidth = getMeasuredWidth();

        //get the available size of child view
        int childLeft = 0;
        int childTop = 0;

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
