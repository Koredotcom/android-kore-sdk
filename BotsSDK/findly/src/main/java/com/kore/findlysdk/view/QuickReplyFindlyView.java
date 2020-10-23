package com.kore.findlysdk.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kore.findlysdk.R;
import com.kore.findlysdk.adapters.QuickRepliesAdapter;
import com.kore.findlysdk.listners.ComposeFooterInterface;
import com.kore.findlysdk.listners.InvokeGenericWebViewInterface;
import com.kore.findlysdk.models.QuickReplyTemplate;
import com.kore.findlysdk.utils.AppControl;
import com.kore.findlysdk.view.viewUtils.LayoutUtils;
import com.kore.findlysdk.view.viewUtils.MeasureUtils;

import java.util.ArrayList;

public class QuickReplyFindlyView extends ViewGroup {

    RecyclerView recyclerView;
    ComposeFooterInterface composeFooterInterface;
    InvokeGenericWebViewInterface invokeGenericWebViewInterface;

    int maxWidth, listViewHeight;

    public QuickReplyFindlyView(Context context) {
        super(context);
        init();
    }

    public QuickReplyFindlyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public QuickReplyFindlyView(Context context, AttributeSet attrs, int defStyleAttr) {
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
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        addView(recyclerView);

        maxWidth = (int) AppControl.getInstance().getDimensionUtil().screenWidth;
        listViewHeight = (int) getResources().getDimension(R.dimen.quick_reply_layout_height);
    }

    public void populateQuickReplyView(ArrayList<QuickReplyTemplate> quickReplyTemplates) {
        if (quickReplyTemplates != null) {
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
            listViewHeight = (int) getResources().getDimension(R.dimen.quick_reply_layout_height);
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
