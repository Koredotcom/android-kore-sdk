package kore.botssdk.view;/*
package kore.botssdk.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;

import kore.botssdk.R;
import kore.botssdk.adapter.CalendarEventsAdapter;
import kore.botssdk.application.AppControl;
import kore.botssdk.fragment.ComposeFooterFragment;
import kore.botssdk.listener.ComposeFooterInterface;
import kore.botssdk.models.CalEventsTemplateModel;
import kore.botssdk.view.viewUtils.LayoutUtils;
import kore.botssdk.view.viewUtils.MeasureUtils;

*/
/**
 * Created by Ramachandra Pradeep on 02-Aug-18.
 *//*


public class CalendarEventsTemplateView extends ViewGroup {

    private AutoExpandListView listView;
    private LinearLayout calCustomListRoot;
    private float dp1 = 0;
    private ComposeFooterInterface composeFooterInterface;

    public float getRestrictedMaxWidth() {
        return restrictedMaxWidth;
    }

    public void setRestrictedMaxWidth(float restrictedMaxWidth) {
        this.restrictedMaxWidth = restrictedMaxWidth;
    }

    private float restrictedMaxWidth;

    public CalendarEventsTemplateView(Context context) {
        super(context);
        init();
    }

    public CalendarEventsTemplateView(Context context, AttributeSet attrs){
        super(context,attrs);
        init();
    }

    public CalendarEventsTemplateView(Context context, AttributeSet attrs, int defStyleAttr){
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.calendar_custom_list, this, true);
        calCustomListRoot = (LinearLayout) findViewById(R.id.calCustomListRoot);
        listView = (AutoExpandListView) findViewById(R.id.calCustomListView);

        dp1 = (int) AppControl.getInstance().getDimensionUtil().dp1;
       // layoutItemHeight = getResources().getDimension(R.dimen.calendar_event_list_row_height);

    }

    public void populateCalendarEvents(ArrayList<CalEventsTemplateModel> eventsList,String type, final boolean isEnabled){
        if(eventsList != null && eventsList.size() > 0) {
            calCustomListRoot.setVisibility(VISIBLE);
            CalendarEventsAdapter calAdapter;
            calAdapter = new CalendarEventsAdapter(getContext(), type, isEnabled);
            calAdapter.setComposeFooterInterface(composeFooterInterface);
            listView.setAdapter(calAdapter);
            calAdapter.setEventList(eventsList);
            calAdapter.notifyDataSetChanged();
            calCustomListRoot.setAlpha(isEnabled ? 1.0f : 0.5f);
            calCustomListRoot.setClickable(false);
        }else{
            calCustomListRoot.setVisibility(GONE);
        }

    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int wrapSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);

        int totalHeight = getPaddingTop();
        int childWidthSpec;

        childWidthSpec = MeasureSpec.makeMeasureSpec((int) restrictedMaxWidth, MeasureSpec.EXACTLY);
        MeasureUtils.measure(calCustomListRoot, childWidthSpec, wrapSpec);


   */
/*     int  childWidth = calCustomListRoot.getMeasuredWidth();
        if (childWidth > restrictedMaxWidth) {
            childWidthSpec = MeasureSpec.makeMeasureSpec((int) restrictedMaxWidth, MeasureSpec.AT_MOST);
            MeasureUtils.measure(calCustomListRoot, childWidthSpec, wrapSpec);
        }*//*

        totalHeight += calCustomListRoot.getMeasuredHeight()+getPaddingBottom()+getPaddingTop();
        if(calCustomListRoot.getMeasuredHeight() !=0 ){
            totalHeight+=12*dp1;
        }
        int parentHeightSpec = MeasureSpec.makeMeasureSpec( totalHeight, MeasureSpec.EXACTLY);
        int parentWidthSpec = MeasureSpec.makeMeasureSpec(calCustomListRoot.getMeasuredWidth(), MeasureSpec.AT_MOST);
        setMeasuredDimension(parentWidthSpec, parentHeightSpec);
    }









*/
/*

    public int getViewHeight() {
        int viewHeight = 0;
        if (listView != null && listView.getAdapter() != null) {
            int rCount = listView.getAdapter().getCount();
            for (int i = 0; i < rCount; i++) {
                View listItem = listView.getAdapter().getView(i, null, listView);
                if(listItem != null) {
                    listItem.measure(0, 0);
                    viewHeight += listItem.getMeasuredHeight();
                }
            }
            viewHeight+=(layoutItemHeight+(1.5*rCount*dp1));
        }

        return viewHeight;
    }

    public int getViewWidth() {
        int viewHeight = 0;
        if (listView != null) {
            int count = 0;
            if (listView.getAdapter() != null) {
                count = listView.getAdapter().getCount();
            }
            viewHeight = (count > 0) ? (int) restrictedMaxWidth : 0;
        }
        return viewHeight;
    }
*//*

*/
/*
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int parentWidth = MeasureSpec.getSize(widthMeasureSpec);
        int maxAllowedWidth = parentWidth;
        int wrapSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);

        int viewHeight = getViewHeight();
        int viewWidth = getViewWidth();
        int childHeightSpec = MeasureSpec.makeMeasureSpec(viewHeight, MeasureSpec.EXACTLY);
        int childWidthSpec = MeasureSpec.makeMeasureSpec(viewWidth, MeasureSpec.EXACTLY);

       // MeasureUtils.measure(listView, childWidthSpec, childHeightSpec);


//        viewHeight += botCustomListViewButton.getMeasuredHeight();
      //  childHeightSpec = MeasureSpec.makeMeasureSpec(viewHeight, MeasureSpec.EXACTLY);
        MeasureUtils.measure(calCustomListRoot, childWidthSpec, childHeightSpec);

        int parentWidthSpec = childWidthSpec;
        int parentHeightSpec = childHeightSpec;

        super.onMeasure(parentWidthSpec, parentHeightSpec);
    }*//*


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

    public ComposeFooterInterface getComposeFooterInterface() {
        return composeFooterInterface;
    }

    public void setComposeFooterInterface(ComposeFooterInterface composeFooterInterface) {
        this.composeFooterInterface = composeFooterInterface;
    }


}
*/
