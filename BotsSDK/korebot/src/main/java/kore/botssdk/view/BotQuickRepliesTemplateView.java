package kore.botssdk.view;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import java.util.ArrayList;

import kore.botssdk.adapter.QuickRepliesTemplateAdapter;
import kore.botssdk.application.AppControl;
import kore.botssdk.listener.ComposeFooterInterface;
import kore.botssdk.listener.InvokeGenericWebViewInterface;
import kore.botssdk.models.QuickReplyTemplate;
import kore.botssdk.view.viewUtils.LayoutUtils;
import kore.botssdk.view.viewUtils.MeasureUtils;

public class BotQuickRepliesTemplateView extends ViewGroup {

    RecyclerView recyclerView;
    ComposeFooterInterface composeFooterInterface;
    InvokeGenericWebViewInterface invokeGenericWebViewInterface;
    StaggeredGridLayoutManager staggeredGridLayoutManager;

    int maxWidth, listViewHeight;
    int dp1;
    public BotQuickRepliesTemplateView(Context context) {
        super(context);
        init();
    }

    public BotQuickRepliesTemplateView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BotQuickRepliesTemplateView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        recyclerView = new RecyclerView(getContext());
//        recyclerView.setPadding((int) getResources().getDimension(R.dimen.quick_reply_recycler_layout_padding_left),
//                (int) getResources().getDimension(R.dimen.quick_reply_recycler_layout_padding_top),
//                (int) getResources().getDimension(R.dimen.quick_reply_recycler_layout_padding_right),
//                (int) getResources().getDimension(R.dimen.quick_reply_recycler_layout_padding_bottom));
        recyclerView.setClipToPadding(false);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new VerticalSpaceItemDecoration(15));
        addView(recyclerView);

        dp1 = (int) AppControl.getInstance().getDimensionUtil().density;
        maxWidth = (int) AppControl.getInstance().getDimensionUtil().screenWidth;
        listViewHeight = (int) AppControl.getInstance().getDimensionUtil().screenWidth;
    }

    public void populateQuickReplyView(ArrayList<QuickReplyTemplate> quickReplyTemplates)
    {
        if (quickReplyTemplates != null)
        {
            if(quickReplyTemplates != null && quickReplyTemplates.size() > 0)
            {
                if(quickReplyTemplates.size()/2 > 0)
                    staggeredGridLayoutManager = new StaggeredGridLayoutManager(quickReplyTemplates.size()/2, LinearLayoutManager.HORIZONTAL);
                else
                    staggeredGridLayoutManager = new StaggeredGridLayoutManager(quickReplyTemplates.size(), LinearLayoutManager.HORIZONTAL);

                recyclerView.setLayoutManager(staggeredGridLayoutManager);
            }

            QuickRepliesTemplateAdapter quickRepliesAdapter = null;
            if (recyclerView.getAdapter() == null) {
                quickRepliesAdapter = new QuickRepliesTemplateAdapter(getContext(), recyclerView);
                recyclerView.setAdapter(quickRepliesAdapter);
                quickRepliesAdapter.setInvokeGenericWebViewInterface(invokeGenericWebViewInterface);
                quickRepliesAdapter.setComposeFooterInterface(composeFooterInterface);
            }

            quickRepliesAdapter = (QuickRepliesTemplateAdapter) recyclerView.getAdapter();

            quickRepliesAdapter.setQuickReplyTemplateArrayList(quickReplyTemplates);
            quickRepliesAdapter.notifyDataSetChanged();
            listViewHeight = 60 * (quickReplyTemplates.size()/2) * dp1;
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
        MeasureUtils.measure(recyclerView, childWidthSpec, childHeightSpec);

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        View childView = getChildAt(0);
        LayoutUtils.layoutChild(childView, 0, 0);
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
