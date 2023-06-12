package kore.botssdk.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import kore.botssdk.R;
import kore.botssdk.adapter.MultiSelectButtonAdapter;
import kore.botssdk.listener.ComposeFooterInterface;
import kore.botssdk.listener.InvokeGenericWebViewInterface;
import kore.botssdk.models.MultiSelectBase;
import kore.botssdk.models.PayloadInner;
import kore.botssdk.utils.KaFontUtils;
import kore.botssdk.view.viewUtils.DimensionUtil;
import kore.botssdk.view.viewUtils.LayoutUtils;
import kore.botssdk.view.viewUtils.MeasureUtils;

public class MultiSelectView extends ViewGroup {
    private AutoExpandListView autoExpandListView;
    private View multiSelectLayout;
    private MultiSelectButtonAdapter multiSelectButtonAdapter;
    private float dp1;


    public MultiSelectView(Context context) {
        super(context);
        init();
    }

    public MultiSelectView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MultiSelectView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public InvokeGenericWebViewInterface getInvokeGenericWebViewInterface() {
        return invokeGenericWebViewInterface;
    }


    public void setInvokeGenericWebViewInterface(InvokeGenericWebViewInterface invokeGenericWebViewInterface) {
        this.invokeGenericWebViewInterface = invokeGenericWebViewInterface;
    }

    public ComposeFooterInterface getComposeFooterInterface() {
        return composeFooterInterface;
    }

    public void setComposeFooterInterface(ComposeFooterInterface composeFooterInterface) {
        this.composeFooterInterface = composeFooterInterface;
    }

    private InvokeGenericWebViewInterface invokeGenericWebViewInterface;
    private ComposeFooterInterface composeFooterInterface;


    private void init() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.multi_select_template, this, true);
        autoExpandListView = view.findViewById(R.id.multi_select_list);
        autoExpandListView.setVerticalScrollBarEnabled(false);
        KaFontUtils.applyCustomFont(getContext(), view);
        multiSelectLayout = view.findViewById(R.id.multi_select_layout);
        dp1 = (int) DimensionUtil.dp1;
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int parentWidth = MeasureSpec.getSize(widthMeasureSpec);
        int wrapSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);

        int totalHeight = getPaddingTop();
        int childWidthSpec;

        childWidthSpec = MeasureSpec.makeMeasureSpec((int) (parentWidth - 28 * dp1), MeasureSpec.EXACTLY);
        MeasureUtils.measure(multiSelectLayout, childWidthSpec, wrapSpec);

        totalHeight += multiSelectLayout.getMeasuredHeight() + getPaddingBottom() + getPaddingTop();

        int parentHeightSpec = MeasureSpec.makeMeasureSpec(totalHeight, MeasureSpec.EXACTLY);
        int parentWidthSpec = MeasureSpec.makeMeasureSpec(multiSelectLayout.getMeasuredWidth(), MeasureSpec.AT_MOST);
        setMeasuredDimension(parentWidthSpec, parentHeightSpec);
    }

    public void setMultiSelectLayoutAlpha(boolean isEnabled){
        multiSelectLayout.setAlpha(isEnabled ? 1.0f : 0.5f);
        multiSelectButtonAdapter.setEnabled(isEnabled);

        /*button1.setTextColor(getResources().getColor(isEnabled ? R.color.splash_color : R.color.color_a7b0be));
        button2.setTextColor(getResources().getColor(isEnabled ? R.color.splash_color : R.color.color_a7b0be));
        ((GradientDrawable) button1.getBackground()).setStroke(2, isEnabled ? getResources().getColor(R.color.splash_color) : getResources().getColor(R.color.color_a7b0be));
        ((GradientDrawable) button2.getBackground()).setStroke(2, isEnabled ? getResources().getColor(R.color.splash_color) : getResources().getColor(R.color.color_a7b0be));

        button1.setEnabled(isEnabled);
        button2.setEnabled(isEnabled);*/


    }

    public void populateData(final PayloadInner payloadInner, boolean isEnabled) {

        if (payloadInner != null) {
            multiSelectLayout.setVisibility(VISIBLE);
            ArrayList<MultiSelectBase> items = new ArrayList<>();
            if(payloadInner.getMultiSelectModels() != null && payloadInner.getMultiSelectModels().size()>0)
                items.addAll(payloadInner.getMultiSelectModels());
            if(payloadInner.getButtons() != null && payloadInner.getButtons().size() >0)
                items.addAll(payloadInner.getButtons());
            multiSelectButtonAdapter = new MultiSelectButtonAdapter(getContext());
            multiSelectButtonAdapter.setMultiSelectModels(items);
            multiSelectButtonAdapter.setEnabled(isEnabled);
            multiSelectButtonAdapter.setComposeFooterInterface(composeFooterInterface);
            autoExpandListView.setAdapter(multiSelectButtonAdapter);
            multiSelectButtonAdapter.notifyDataSetChanged();

            multiSelectLayout.setAlpha(isEnabled ? 1.0f : 0.5f);

        } else {
            autoExpandListView.setAdapter(null);
            multiSelectLayout.setVisibility(GONE);
        }
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
}
