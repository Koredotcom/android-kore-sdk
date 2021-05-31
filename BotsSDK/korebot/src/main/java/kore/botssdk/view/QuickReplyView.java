package kore.botssdk.view;

import android.content.Context;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import kore.botssdk.R;
import kore.botssdk.adapter.QuickRepliesAdapter;
import kore.botssdk.application.AppControl;
import kore.botssdk.fragment.ComposeFooterFragment;
import kore.botssdk.listener.ComposeFooterInterface;
import kore.botssdk.listener.InvokeGenericWebViewInterface;
import kore.botssdk.models.QuickReplyTemplate;
import kore.botssdk.view.viewUtils.LayoutUtils;
import kore.botssdk.view.viewUtils.MeasureUtils;

/**
 * Created by Pradeep Mahato on 28/7/17.
 * Copyright (c) 2014 Kore Inc. All rights reserved.
 */
public class QuickReplyView extends ViewGroup {

    RecyclerView recyclerView;
    ComposeFooterInterface composeFooterInterface;
    InvokeGenericWebViewInterface invokeGenericWebViewInterface;
    StaggeredGridLayoutManager staggeredGridLayoutManager;

    int maxWidth, listViewHeight;

    public QuickReplyView(Context context) {
        super(context);
        init();
    }

    public QuickReplyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public QuickReplyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        recyclerView = new RecyclerView(getContext());
        recyclerView.setPadding((int) getResources().getDimension(R.dimen.quick_reply_recycler_layout_padding_left),
                (int) getResources().getDimension(R.dimen.quick_reply_recycler_layout_padding_top),
                (int) getResources().getDimension(R.dimen.quick_reply_recycler_layout_padding_right),
                (int) getResources().getDimension(R.dimen.quick_reply_recycler_layout_padding_bottom));
        recyclerView.setClipToPadding(false);
        staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, LinearLayoutManager.HORIZONTAL);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        addView(recyclerView);

        maxWidth = (int) AppControl.getInstance().getDimensionUtil().screenWidth;
        listViewHeight = (int) getResources().getDimension(R.dimen.quick_reply_layout_height);
    }

    public void populateQuickReplyView(ArrayList<QuickReplyTemplate> quickReplyTemplates) {
        if (quickReplyTemplates != null)
        {
            staggeredGridLayoutManager.setSpanCount(quickReplyTemplates.size()/2 > 0 ? ((quickReplyTemplates.size()/2) + (quickReplyTemplates.size()%2)) : 1);
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
            listViewHeight = (((quickReplyTemplates.size()/2) + (quickReplyTemplates.size()%2)) * (int) getResources().getDimension(R.dimen.quick_reply_layout_height));
            recyclerView.setVisibility(VISIBLE);
        } else {
            recyclerView.setVisibility(GONE);
            listViewHeight = 0;
        }
    }

    public void setComposeFooterInterface(ComposeFooterInterface composeFooterInterface) {
        this.composeFooterInterface = composeFooterInterface;
    }

    public void setInvokeGenericWebViewInterface(InvokeGenericWebViewInterface invokeGenericWebViewInterface) {
        this.invokeGenericWebViewInterface = invokeGenericWebViewInterface;
    }

    public boolean getRecyclerVisibility(){
        if(recyclerView != null && recyclerView.getAdapter() != null)
            return false;
        return true;
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
        MeasureUtils.measure(recyclerView, childWidthSpec, childHeightSpec);

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        View childView = getChildAt(0);
        LayoutUtils.layoutChild(childView, 0, 0);
    }
}
