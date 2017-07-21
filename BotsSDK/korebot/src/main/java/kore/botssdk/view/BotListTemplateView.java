package kore.botssdk.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.ArrayList;

import kore.botssdk.R;
import kore.botssdk.adapter.BotListTemplateAdapter;
import kore.botssdk.application.AppControl;
import kore.botssdk.fragment.ComposeFooterFragment.ComposeFooterInterface;
import kore.botssdk.models.BotButtonModel;
import kore.botssdk.models.BotListModel;
import kore.botssdk.view.viewUtils.LayoutUtils;
import kore.botssdk.view.viewUtils.MeasureUtils;

/**
 * Created by Anil Kumar on 12/8/2016.
 */
public class BotListTemplateView extends ViewGroup {

    String LOG_TAG = BotListTemplateView.class.getSimpleName();

    float dp1, layoutItemHeight = 0;
    AutoExpandListView autoExpandListView;
    Button botCustomListViewButton;
    LinearLayout botCustomListRoot;
    float restrictedMaxWidth, restrictedMaxHeight;
    ComposeFooterInterface composeFooterInterface;

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
        botCustomListRoot = (LinearLayout) findViewById(R.id.botCustomListRoot);
        autoExpandListView = (AutoExpandListView) findViewById(R.id.botCustomListView);
        botCustomListViewButton = (Button) findViewById(R.id.botCustomListViewButton);
        dp1 = (int) AppControl.getInstance().getDimensionUtil().dp1;
        layoutItemHeight = getResources().getDimension(R.dimen.list_item_view_height);

    }

    public void populateListTemplateView(ArrayList<BotListModel> botListModelArrayList, final ArrayList<BotButtonModel> botButtonModelArrayList) {
        BotListTemplateAdapter botListTemplateAdapter;
        if (autoExpandListView.getAdapter() == null) {
            botListTemplateAdapter = new BotListTemplateAdapter(getContext(), autoExpandListView);
            autoExpandListView.setAdapter(botListTemplateAdapter);
            botListTemplateAdapter.setComposeFooterInterface(composeFooterInterface);
        } else {
            botListTemplateAdapter = (BotListTemplateAdapter) autoExpandListView.getAdapter();
        }
        botListTemplateAdapter.setBotListModelArrayList(botListModelArrayList);
        botListTemplateAdapter.notifyDataSetChanged();

        if (botButtonModelArrayList != null && botButtonModelArrayList.size() > 0) {
            botCustomListViewButton.setText(botButtonModelArrayList.get(0).getTitle());
            botCustomListViewButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (composeFooterInterface != null) {
                        String message = botButtonModelArrayList.get(0).getPayload();
                        composeFooterInterface.onSendClick(message);
                    }
                }
            });
            botCustomListViewButton.setVisibility(VISIBLE);
        } else {
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
            viewHeight = (count > 0) ? (int) restrictedMaxWidth : 0;
        }
        return viewHeight;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int parentWidth = MeasureSpec.getSize(widthMeasureSpec);
        int maxAllowedWidth = parentWidth;
        int wrapSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);

        int viewHeight = getViewHeight();
        int viewWidth = getViewWidth();
        int childHeightSpec = MeasureSpec.makeMeasureSpec(viewHeight, MeasureSpec.EXACTLY);
        int childWidthSpec = MeasureSpec.makeMeasureSpec(viewWidth, MeasureSpec.EXACTLY);

        MeasureUtils.measure(autoExpandListView, childWidthSpec, childHeightSpec);

        MeasureUtils.measure(botCustomListViewButton, childWidthSpec, wrapSpec);

        viewHeight += botCustomListViewButton.getMeasuredHeight();
        childHeightSpec = MeasureSpec.makeMeasureSpec(viewHeight, MeasureSpec.EXACTLY);
        MeasureUtils.measure(botCustomListRoot, childWidthSpec, childHeightSpec);

        int parentWidthSpec = childWidthSpec;
        int parentHeightSpec = childHeightSpec;

        super.onMeasure(parentWidthSpec, parentHeightSpec);
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
