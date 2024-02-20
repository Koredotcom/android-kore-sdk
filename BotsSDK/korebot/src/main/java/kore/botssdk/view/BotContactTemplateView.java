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
import kore.botssdk.adapter.BotContactTemplateAdapter;
import kore.botssdk.listener.ComposeFooterInterface;
import kore.botssdk.listener.InvokeGenericWebViewInterface;
import kore.botssdk.models.ContactTemplateModel;
import kore.botssdk.utils.StringUtils;
import kore.botssdk.view.viewUtils.DimensionUtil;
import kore.botssdk.view.viewUtils.LayoutUtils;
import kore.botssdk.view.viewUtils.MeasureUtils;

public class BotContactTemplateView extends ViewGroup {

    String LOG_TAG = BotListTemplateView.class.getSimpleName();

    float dp1, layoutItemHeight = 0;
    AutoExpandListView autoExpandListView;
    TextView botCustomListViewButton, botListViewTitle;
    LinearLayout botCustomListRoot;
    float restrictedMaxWidth, restrictedMaxHeight;
    ComposeFooterInterface composeFooterInterface;
    InvokeGenericWebViewInterface invokeGenericWebViewInterface;

    public BotContactTemplateView(Context context) {
        super(context);
        init();
    }

    public BotContactTemplateView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BotContactTemplateView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.bot_custom_list, this, true);
        botCustomListRoot = findViewById(R.id.botCustomListRoot);
        autoExpandListView = findViewById(R.id.botCustomListView);
        botCustomListViewButton = findViewById(R.id.botCustomListViewButton);
        botListViewTitle = findViewById(R.id.botListViewTitle);
        dp1 = (int) DimensionUtil.dp1;
        layoutItemHeight = getResources().getDimension(R.dimen.list_item_view_height);

    }

    public void populateContactTemplateView(ArrayList<ContactTemplateModel> botListModelArrayList, String title) {


        if (botListModelArrayList != null && botListModelArrayList.size() > 0)
        {
            botListViewTitle.setVisibility(GONE);
            if(!StringUtils.isNullOrEmpty(title))
            {
                botListViewTitle.setVisibility(VISIBLE);
                botListViewTitle.setText(title);
            }

            BotContactTemplateAdapter botListTemplateAdapter;
            if (autoExpandListView.getAdapter() == null)
            {
                botListTemplateAdapter = new BotContactTemplateAdapter(getContext(), autoExpandListView);
                autoExpandListView.setAdapter(botListTemplateAdapter);
                botListTemplateAdapter.setComposeFooterInterface(composeFooterInterface);
                botListTemplateAdapter.setInvokeGenericWebViewInterface(invokeGenericWebViewInterface);
            } else {
                botListTemplateAdapter = (BotContactTemplateAdapter) autoExpandListView.getAdapter();
            }
            botListTemplateAdapter.setBotListModelArrayList(botListModelArrayList);
            botListTemplateAdapter.notifyDataSetChanged();
            botCustomListRoot.setVisibility(VISIBLE);

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
            viewHeight = (count > 0) ? (int) (restrictedMaxWidth*dp1) : 0;
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
