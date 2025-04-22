package kore.botssdk.view;

import static kore.botssdk.viewUtils.DimensionUtil.dp1;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import java.util.ArrayList;

import kore.botssdk.R;
import kore.botssdk.adapter.QuickRepliesAdapter;
import kore.botssdk.listener.ComposeFooterInterface;
import kore.botssdk.listener.InvokeGenericWebViewInterface;
import kore.botssdk.models.QuickReplyTemplate;
import kore.botssdk.net.SDKConfiguration;
import kore.botssdk.viewUtils.DimensionUtil;
import kore.botssdk.viewUtils.LayoutUtils;
import kore.botssdk.viewUtils.MeasureUtils;

/*
 * Copyright (c) 2014 Kore Inc. All rights reserved.
 */
public class QuickReplyView extends ViewGroup {

    RecyclerView recyclerView;
    ComposeFooterInterface composeFooterInterface;
    InvokeGenericWebViewInterface invokeGenericWebViewInterface;
    StaggeredGridLayoutManager staggeredGridLayoutManager;
    LinearLayoutManager linearLayoutManager;
    View quickReplyView;
    int maxWidth, listViewHeight;
    boolean isStacked;
    View vQuickReplies;

    public QuickReplyView(Context context) {
        super(context);
        init(context);
    }

    public QuickReplyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public QuickReplyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        quickReplyView = LayoutInflater.from(context).inflate(R.layout.quick_reply_layout, null);
        recyclerView = quickReplyView.findViewById(R.id.rlQuickReplies);
        vQuickReplies = quickReplyView.findViewById(R.id.vQuickReplies);

        recyclerView.setPadding((int) getResources().getDimension(R.dimen.quick_reply_recycler_layout_padding_left), (int) getResources().getDimension(R.dimen.quick_reply_recycler_layout_padding_top), (int) getResources().getDimension(R.dimen.quick_reply_recycler_layout_padding_right), (int) getResources().getDimension(R.dimen.quick_reply_recycler_layout_padding_bottom));
        recyclerView.setClipToPadding(false);

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        addView(quickReplyView);

        maxWidth = (int) new DimensionUtil(getContext()).screenWidth;
        listViewHeight = (int) getResources().getDimension(R.dimen.quick_reply_layout_height);
    }

    public void populateQuickReplyView(ArrayList<QuickReplyTemplate> quickReplyTemplates) {
        if (quickReplyTemplates != null) {
            if (isStacked) {
                staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, LinearLayoutManager.HORIZONTAL);
                recyclerView.setLayoutManager(staggeredGridLayoutManager);
                staggeredGridLayoutManager.setSpanCount(quickReplyTemplates.size() / 2 > 0 ? ((quickReplyTemplates.size() / 2) + (quickReplyTemplates.size() % 2)) : 1);
                listViewHeight = (((quickReplyTemplates.size() / 2) + (quickReplyTemplates.size() % 2)) * (int) getResources().getDimension(R.dimen.quick_reply_layout_height));
            } else {
                linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
                recyclerView.setLayoutManager(linearLayoutManager);
                listViewHeight = (int) getResources().getDimension(R.dimen.quick_reply_layout_height) + (int) (10 * dp1);
            }
            QuickRepliesAdapter quickRepliesAdapter = null;
            if (recyclerView.getAdapter() == null) {
                quickRepliesAdapter = new QuickRepliesAdapter(getContext(), recyclerView);
                recyclerView.setAdapter(quickRepliesAdapter);
                quickRepliesAdapter.setInvokeGenericWebViewInterface(invokeGenericWebViewInterface);
                quickRepliesAdapter.setComposeFooterInterface(composeFooterInterface);
            }

            quickRepliesAdapter = (QuickRepliesAdapter) recyclerView.getAdapter();
            quickRepliesAdapter.setQuickReplyTemplateArrayList(quickReplyTemplates);
            quickRepliesAdapter.notifyDataSetChanged();
            quickReplyView.setVisibility(VISIBLE);
            vQuickReplies.setBackgroundColor(Color.parseColor(SDKConfiguration.BubbleColors.leftBubbleSelected));
        } else {
            quickReplyView.setVisibility(GONE);
            listViewHeight = 0;
        }
    }

    public void setStacked(boolean isStacked) {
        this.isStacked = isStacked;
    }

    public void setComposeFooterInterface(ComposeFooterInterface composeFooterInterface) {
        this.composeFooterInterface = composeFooterInterface;
    }

    public void setInvokeGenericWebViewInterface(InvokeGenericWebViewInterface invokeGenericWebViewInterface) {
        this.invokeGenericWebViewInterface = invokeGenericWebViewInterface;
    }

    public boolean getRecyclerVisibility() {
        return recyclerView == null || recyclerView.getAdapter() == null;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int parentWidth = MeasureSpec.getSize(widthMeasureSpec);
        int maxAllowedWidth = parentWidth;
        int wrapSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);

        int totalHeight = getPaddingTop();
        int totalWidth = getPaddingLeft();

        int childWidthSpec;
        int childHeightSpec;
        int contentWidth = 0;

        /*
         * For Carousel ViewPager Layout
         */
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(listViewHeight, MeasureSpec.EXACTLY);
        childWidthSpec = widthMeasureSpec;
        childHeightSpec = heightMeasureSpec;
        MeasureUtils.measure(quickReplyView, childWidthSpec, childHeightSpec);

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        View childView = getChildAt(0);
        LayoutUtils.layoutChild(childView, 0, 0);
    }
}
