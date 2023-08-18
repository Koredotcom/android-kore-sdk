package kore.botssdk.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import kore.botssdk.R;
import kore.botssdk.adapter.FormActionsAdapter;
import kore.botssdk.application.AppControl;
import kore.botssdk.listener.ComposeFooterInterface;
import kore.botssdk.models.FormActionTemplate;
import kore.botssdk.view.viewUtils.LayoutUtils;
import kore.botssdk.view.viewUtils.MeasureUtils;

/**
 * Created by Ramachandra Pradeep on 09-Feb-18.
 */

public class FormActionView extends ViewGroup {
    RecyclerView recyclerView;
    ComposeFooterInterface composeFooterInterface;

    int maxWidth, listViewHeight;


    public FormActionView(Context context) {
        super(context);
        init();
    }
    public FormActionView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FormActionView(Context context, AttributeSet attrs, int defStyleAttr) {
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
    public void populateFormActionView(ArrayList<FormActionTemplate> formActionTemplates) {
        if (formActionTemplates != null) {
            FormActionsAdapter formActionAdapter = null;
            if (recyclerView.getAdapter() == null) {
                formActionAdapter = new FormActionsAdapter(getContext(), recyclerView);
                recyclerView.setAdapter(formActionAdapter);
                formActionAdapter.setComposeFooterInterface(composeFooterInterface);
            }

            formActionAdapter = (FormActionsAdapter) recyclerView.getAdapter();

            formActionAdapter.setQuickReplyTemplateArrayList(formActionTemplates);
            formActionAdapter.notifyDataSetChanged();
            recyclerView.setVisibility(VISIBLE);
        } else {
            recyclerView.setVisibility(GONE);
        }
    }

    public void setComposeFooterInterface(ComposeFooterInterface composeFooterInterface) {
        this.composeFooterInterface = composeFooterInterface;
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
/*        childWidthSpec = widthMeasureSpec;
        childHeightSpec = heightMeasureSpec;*/
        MeasureUtils.measure(recyclerView, wrapSpec, heightMeasureSpec);
        widthMeasureSpec = MeasureSpec.makeMeasureSpec(recyclerView.getMeasuredWidth(), MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        View childView = getChildAt(0);
        LayoutUtils.layoutChild(childView, 0, 0);
    }
}
