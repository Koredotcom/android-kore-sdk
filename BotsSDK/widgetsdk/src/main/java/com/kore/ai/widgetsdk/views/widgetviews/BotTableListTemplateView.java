//package com.kore.ai.widgetsdk.views.widgetviews;
//
//import android.content.Context;
//import android.util.AttributeSet;
//import android.view.LayoutInflater;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
//import com.kora.ai.widgetsdk.R;
//import com.kore.ai.widgetsdk.adapters.BotTableListTemlateAdapter;
//import com.kore.ai.widgetsdk.application.AppControl;
//import com.kore.ai.widgetsdk.listeners.ComposeFooterInterface;
//import com.kore.ai.widgetsdk.listeners.InvokeGenericWebViewInterface;
//import com.kore.ai.widgetsdk.listeners.VerticalListViewActionHelper;
//import com.kore.ai.widgetsdk.models.BotTableListModel;
//import com.kore.ai.widgetsdk.view.AutoExpandListView;
//
//import java.util.ArrayList;
//
//public class BotTableListTemplateView extends LinearLayout {
//
//    String LOG_TAG = BotTableListTemplateView.class.getSimpleName();
//
//    float dp1, layoutItemHeight = 0;
//    AutoExpandListView autoExpandListView;
//    TextView botCustomListViewButton;
//    TextView workBenchListViewButton;
//    LinearLayout botCustomListRoot;
//    float restrictedMaxWidth, restrictedMaxHeight;
//    ComposeFooterInterface composeFooterInterface;
//    VerticalListViewActionHelper verticalListViewActionHelper;
//    InvokeGenericWebViewInterface invokeGenericWebViewInterface;
//
//    public BotTableListTemplateView(Context context) {
//        super(context);
//        init();
//    }
//
//    public BotTableListTemplateView(Context context, AttributeSet attrs) {
//        super(context, attrs);
//        init();
//    }
//
//    public BotTableListTemplateView(Context context, AttributeSet attrs, int defStyleAttr) {
//        super(context, attrs, defStyleAttr);
//        init();
//    }
//
//    private void init() {
//        LayoutInflater.from(getContext()).inflate(R.layout.bot_table_list_template_view, this, true);
//        botCustomListRoot = (LinearLayout) findViewById(R.id.botCustomListRoot);
//        autoExpandListView = (AutoExpandListView) findViewById(R.id.botCustomListView);
//        botCustomListViewButton = (TextView) findViewById(R.id.botCustomListViewButton);
//        workBenchListViewButton = (TextView) findViewById(R.id.workBenchListViewButton);
//        dp1 = (int) AppControl.getInstance().getDimensionUtil().dp1;
//        layoutItemHeight = getResources().getDimension(R.dimen.list_item_view_height);
//
//    }
//
//    public void populateListTemplateView(ArrayList<BotTableListModel> botListModelArrayList)
//    {
//        if (botListModelArrayList != null && botListModelArrayList.size() > 0) {
//            BotTableListTemlateAdapter botListTemplateAdapter;
//            if (autoExpandListView.getAdapter() == null) {
//                botListTemplateAdapter = new BotTableListTemlateAdapter(getContext(), autoExpandListView, 4);
//                autoExpandListView.setAdapter(botListTemplateAdapter);
//                botListTemplateAdapter.setComposeFooterInterface(composeFooterInterface);
//                botListTemplateAdapter.setInvokeGenericWebViewInterface(invokeGenericWebViewInterface);
//            } else {
//                botListTemplateAdapter = (BotTableListTemlateAdapter) autoExpandListView.getAdapter();
//            }
//            botListTemplateAdapter.setBotListModelArrayList(botListModelArrayList);
//            botListTemplateAdapter.notifyDataSetChanged();
//            botCustomListRoot.setVisibility(VISIBLE);
//        }
//        else
//        {
//            botCustomListRoot.setVisibility(GONE);
//            botCustomListViewButton.setVisibility(GONE);
//        }
//    }
//
//    public void setRestrictedMaxHeight(float restrictedMaxHeight) {
//        this.restrictedMaxHeight = restrictedMaxHeight;
//    }
//
//    public void setRestrictedMaxWidth(float restrictedMaxWidth) {
//        this.restrictedMaxWidth = restrictedMaxWidth;
//    }
//
//    public void setComposeFooterInterface(ComposeFooterInterface composeFooterInterface) {
//        this.composeFooterInterface = composeFooterInterface;
//    }
//
//    public int getViewHeight() {
//        int viewHeight = 0;
//        if (autoExpandListView != null) {
//            int count = 0;
//            if (autoExpandListView.getAdapter() != null) {
//                count = autoExpandListView.getAdapter().getCount();
//            }
//            viewHeight = (int) (layoutItemHeight * count);
//        }
//        return viewHeight;
//    }
//
//    public int getViewWidth() {
//        int viewHeight = 0;
//        if (autoExpandListView != null) {
//            int count = 0;
//            if (autoExpandListView.getAdapter() != null) {
//                count = autoExpandListView.getAdapter().getCount();
//            }
//            viewHeight = (count > 0) ? (int) (restrictedMaxWidth -2*dp1) : 0;
//        }
//        return viewHeight;
//    }
//
////    @Override
////    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
////        int wrapSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
////
////        int totalHeight = getPaddingTop();
////        int childWidthSpec;
////        int totalWidth = getPaddingLeft();
////
////        childWidthSpec = MeasureSpec.makeMeasureSpec((int) restrictedMaxWidth, MeasureSpec.EXACTLY);
////        MeasureUtils.measure(botCustomListRoot, childWidthSpec, wrapSpec);
////
////        totalHeight += botCustomListRoot.getMeasuredHeight() + getPaddingBottom() + getPaddingTop();
////        totalWidth += botCustomListRoot.getMeasuredWidth() + getPaddingLeft()+getPaddingRight();
////        if(totalHeight != 0){
////            totalWidth = totalWidth + (int)(3 * dp1);
////        }
////
////        int parentHeightSpec = MeasureSpec.makeMeasureSpec(totalHeight, MeasureSpec.EXACTLY);
////        int parentWidthSpec = MeasureSpec.makeMeasureSpec(totalWidth, MeasureSpec.AT_MOST);
////        setMeasuredDimension(parentWidthSpec, parentHeightSpec);
////    }
////
////    @Override
////    protected void onLayout(boolean changed, int l, int t, int r, int b) {
////
////        final int count = getChildCount();
////        int parentWidth = getMeasuredWidth();
////
////        //get the available size of child view
////        int childLeft = 0;
////        int childTop = 0;
////
////        int itemWidth = (r - l) / getChildCount();
////
////        for (int i = 0; i < count; i++) {
////            View child = getChildAt(i);
////            if (child.getVisibility() != GONE) {
////                LayoutUtils.layoutChild(child, childLeft, childTop);
////                childTop += child.getMeasuredHeight();
////            }
////        }
////    }
//}
