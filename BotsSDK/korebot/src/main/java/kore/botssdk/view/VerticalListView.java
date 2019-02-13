package kore.botssdk.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;
import kore.botssdk.R;
import kore.botssdk.adapter.KnowledgeRecyclerAdapter;
import kore.botssdk.adapter.KoraEmailRecyclerAdapter;
import kore.botssdk.adapter.KoraFilesRecyclerAdapter;
import kore.botssdk.application.AppControl;
import kore.botssdk.listener.ComposeFooterInterface;
import kore.botssdk.listener.InvokeGenericWebViewInterface;
import kore.botssdk.listener.RecyclerViewDataAccessor;
import kore.botssdk.models.BotResponse;
import kore.botssdk.view.viewUtils.LayoutUtils;
import kore.botssdk.view.viewUtils.MeasureUtils;

/**
 * Created by Ramachandra Pradeep on 09-Aug-18.
 */

public class VerticalListView extends ViewGroup {
    private RecyclerView recyclerView;
    private CardView rootLayout;
    private int dp1;
    private TextView viewMore;
    private DividerItemDecoration dividerKnowledge,dividerEmail,dividerDrive;


    public ComposeFooterInterface getComposeFooterInterface() {
        return composeFooterInterface;
    }

    private Drawable recyclerDrawable, insetDrawable;

    public void setComposeFooterInterface(ComposeFooterInterface composeFooterInterface) {
        this.composeFooterInterface = composeFooterInterface;
        if(recyclerView.getAdapter() != null){
            ((RecyclerViewDataAccessor)(recyclerView.getAdapter())).setComposeFooterInterface(composeFooterInterface);
        }
    }

    private ComposeFooterInterface composeFooterInterface;
    private InvokeGenericWebViewInterface invokeGenericWebViewInterface;

    public VerticalListView(Context mContext) {

        super(mContext);
        init();
    }

    public VerticalListView(Context mContext, AttributeSet attributes) {
        super(mContext, attributes);
        init();
    }

    public VerticalListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.kora_files_carousel_view, this, true);
        recyclerView = view.findViewById(R.id.recycler_view);
        viewMore = view.findViewById(R.id.view_more);
        rootLayout = view.findViewById(R.id.root_layout);
        viewMore.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                RecyclerView.Adapter adapter = recyclerView.getAdapter();
                if (adapter == null) return;
                ((RecyclerViewDataAccessor) adapter).setExpanded(true);
                adapter.notifyDataSetChanged();
                composeFooterInterface.openFullView(getTemplateType(adapter), ((RecyclerViewDataAccessor) recyclerView.getAdapter()).getData());
                viewMore.setVisibility(GONE);
            }
        });

        dp1 = (int) AppControl.getInstance().getDimensionUtil().dp1;

        dividerDrive = new
                DividerItemDecoration(getContext(),
                DividerItemDecoration.VERTICAL);
        dividerEmail = new
                DividerItemDecoration(getContext(),
                DividerItemDecoration.VERTICAL);
        dividerKnowledge = new
                DividerItemDecoration(getContext(),
                DividerItemDecoration.VERTICAL);

        dividerEmail.setDrawable(getResources().getDrawable(R.drawable.recycler_divider));
        dividerKnowledge.setDrawable(getResources().getDrawable(R.drawable.recycler_divider));
        dividerDrive.setDrawable(getResources().getDrawable(R.drawable.inset_65_divider));

    }

    private String getTemplateType(RecyclerView.Adapter adapter) {
        if (adapter instanceof KoraFilesRecyclerAdapter) {
            return BotResponse.TEMPLATE_TYPE_FILES_LOOKUP;
        } else if (adapter instanceof KoraEmailRecyclerAdapter) {
            return BotResponse.TEMPLATE_TYPE_KORA_SEARCH_CAROUSAL;
        } else {
            return "";
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int parentWidth = MeasureSpec.getSize(widthMeasureSpec);
        int wrapSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);


        int totalHeight = getPaddingTop();
        int childWidthSpec;

        childWidthSpec = MeasureSpec.makeMeasureSpec((int) parentWidth - 32 * dp1, MeasureSpec.EXACTLY);
        MeasureUtils.measure(rootLayout, childWidthSpec, wrapSpec);

        totalHeight += rootLayout.getMeasuredHeight() + getPaddingBottom() + getPaddingTop();

        int parentHeightSpec = MeasureSpec.makeMeasureSpec(totalHeight, MeasureSpec.EXACTLY);
        int parentWidthSpec = MeasureSpec.makeMeasureSpec(rootLayout.getMeasuredWidth(), MeasureSpec.AT_MOST);
        setMeasuredDimension(parentWidthSpec, parentHeightSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        final int count = getChildCount();
        //get the available size of child view
        int childLeft = 0;
        int childTop = 0;

        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() != GONE) {
                LayoutUtils.layoutChild(child, childLeft, childTop);
                childTop += child.getMeasuredHeight();
            }
        }
    }

    public void prepareDataSetAndPopulate(ArrayList data, String templateType) {
        if (data == null || data.size() == 0) {
            rootLayout.setVisibility(GONE);
        } else {
            switch (templateType) {
                case BotResponse.TEMPLATE_TYPE_FILES_LOOKUP:
                    KoraFilesRecyclerAdapter koraCarousalAdapter = new KoraFilesRecyclerAdapter(data, getContext());
                    recyclerView.setAdapter(koraCarousalAdapter);
                    recyclerView.addItemDecoration(dividerDrive);
                    koraCarousalAdapter.notifyDataSetChanged();
                    break;
                case BotResponse.TEMPLATE_TYPE_KORA_SEARCH_CAROUSAL:
                    KoraEmailRecyclerAdapter koraEmailRecyclerAdapter = new KoraEmailRecyclerAdapter(data, getContext());
                    koraEmailRecyclerAdapter.setInvokeGenericWebViewInterface(invokeGenericWebViewInterface);
                    recyclerView.setAdapter(koraEmailRecyclerAdapter);
                    recyclerView.addItemDecoration(dividerEmail);
                    koraEmailRecyclerAdapter.notifyDataSetChanged();
                    break;
                case BotResponse.TEMPLATE_TYPE_KORA_CAROUSAL:
                    KnowledgeRecyclerAdapter knowledgeRecyclerAdapter = new KnowledgeRecyclerAdapter(data,getContext());
                    knowledgeRecyclerAdapter.setInvokeGenericWebViewInterface(invokeGenericWebViewInterface);
                    knowledgeRecyclerAdapter.setComposeFooterInterface(composeFooterInterface);
                    recyclerView.setAdapter(knowledgeRecyclerAdapter);
                    recyclerView.addItemDecoration(dividerKnowledge);
                    knowledgeRecyclerAdapter.notifyDataSetChanged();
                default:

            }

            viewMore.setVisibility(data.size() > 3 ? VISIBLE : GONE);
            if (data.size() > 3) {
                viewMore.setText(String.format(getContext().getResources().getString(R.string.view_more), data.size() - 3));
            }
            rootLayout.setVisibility(VISIBLE);
        }

    }

    public void setInvokeGenericWebViewInterface(InvokeGenericWebViewInterface invokeGenericWebViewInterface) {
        this.invokeGenericWebViewInterface = invokeGenericWebViewInterface;
        if(recyclerView.getAdapter() != null){
            ((RecyclerViewDataAccessor)(recyclerView.getAdapter())).setInvokeGenericWebViewInterface(invokeGenericWebViewInterface);
        }
    }
}
