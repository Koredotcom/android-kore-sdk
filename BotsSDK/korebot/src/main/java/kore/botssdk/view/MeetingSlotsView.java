package kore.botssdk.view;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;

import kore.botssdk.R;
import kore.botssdk.adapter.MeetingSlotsButtonAdapter;
import kore.botssdk.application.AppControl;
import kore.botssdk.fragment.ComposeFooterFragment;
import kore.botssdk.listener.ComposeFooterInterface;
import kore.botssdk.listener.InvokeGenericWebViewInterface;
import kore.botssdk.models.MeetingSlotModel;
import kore.botssdk.models.MeetingTemplateModel;
import kore.botssdk.utils.BundleConstants;
import kore.botssdk.utils.KaFontUtils;
import kore.botssdk.view.viewUtils.LayoutUtils;
import kore.botssdk.view.viewUtils.MeasureUtils;

public class MeetingSlotsView extends ViewGroup{
    private AutoExpandListView autoExpandListView;
    private TextView showMore;
    private View meetingLayout;

    float dp1;
    Gson gson = new Gson();
    private float restrictedLayoutWidth;


    public void setRestrictedLayoutWidth(float restrictedLayoutWidth) {
        this.restrictedLayoutWidth = restrictedLayoutWidth;
    }

    public MeetingSlotsView(Context context) {
        super(context);
        init();
    }

    public MeetingSlotsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MeetingSlotsView(Context context, AttributeSet attrs, int defStyleAttr) {
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
        View view = LayoutInflater.from(getContext()).inflate(R.layout.meeting_slots_template, this, true);
        autoExpandListView = (AutoExpandListView) view.findViewById(R.id.slots_list);
        KaFontUtils.applyCustomFont(getContext(),view);
        meetingLayout = view.findViewById(R.id.meeting_layout);
        showMore = (TextView) view.findViewById(R.id.show_more_view);
        dp1 = (int) AppControl.getInstance().getDimensionUtil().dp1;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            showMore.setLetterSpacing(0.06f);
        }
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int wrapSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);

        int totalHeight = getPaddingTop();
        int childWidthSpec;

        childWidthSpec = MeasureSpec.makeMeasureSpec((int) restrictedLayoutWidth, MeasureSpec.UNSPECIFIED);
        MeasureUtils.measure(meetingLayout, childWidthSpec, wrapSpec);
        int  childWidth = meetingLayout.getMeasuredWidth();
        if (childWidth > restrictedLayoutWidth) {
            childWidthSpec = MeasureSpec.makeMeasureSpec((int) restrictedLayoutWidth, MeasureSpec.AT_MOST);
            MeasureUtils.measure(meetingLayout, childWidthSpec, wrapSpec);
        }
        totalHeight += meetingLayout.getMeasuredHeight()+getPaddingBottom()+getPaddingTop();
        if(meetingLayout.getMeasuredHeight() !=0 ){
            totalHeight+=18*dp1;
        }
        int parentHeightSpec = MeasureSpec.makeMeasureSpec( totalHeight, MeasureSpec.EXACTLY);
        int parentWidthSpec = MeasureSpec.makeMeasureSpec(meetingLayout.getMeasuredWidth(), MeasureSpec.AT_MOST);
        setMeasuredDimension(parentWidthSpec, parentHeightSpec);
    }


    public void populateData(final MeetingTemplateModel meetingTemplateModel, boolean isEnabled){

        if(meetingTemplateModel != null) {
            meetingLayout.setVisibility(VISIBLE);
            ArrayList<MeetingSlotModel.Slot> slots = meetingTemplateModel != null ? meetingTemplateModel.getQuick_slots() : new ArrayList<MeetingSlotModel.Slot>();
            MeetingSlotsButtonAdapter slotsButtonAdapter = new MeetingSlotsButtonAdapter(getContext());
            slotsButtonAdapter.setMeetingsModelArrayList(slots);
            slotsButtonAdapter.setEnabled(isEnabled);
            slotsButtonAdapter.setComposeFooterInterface(composeFooterInterface);
            autoExpandListView.setAdapter(slotsButtonAdapter);
            slotsButtonAdapter.notifyDataSetChanged();
            showMore.setVisibility(slots.size() > 0 && meetingTemplateModel != null && meetingTemplateModel.isShowMore() ? VISIBLE : GONE);
            meetingLayout.setAlpha(isEnabled ? 1.0f : 0.5f);
            showMore.setTextColor(getResources().getColor(isEnabled ? R.color.splash_color : R.color.color_a7b0be));
            showMore.setOnClickListener(isEnabled ? new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putString(BundleConstants.SLOTS_LIST, meetingTemplateModel != null ? gson.toJson(meetingTemplateModel.getWorking_hours()) : "");
                    bundle.putString(BundleConstants.QUICK_SLOTS, meetingTemplateModel != null ? gson.toJson(meetingTemplateModel.getQuick_slots()) : "");
                    composeFooterInterface.launchActivityWithBundle(0, bundle);
                }
            } : null);
        }else{
            meetingLayout.setVisibility(GONE);
        }
    }



    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        final int count = getChildCount();
        int parentWidth = getMeasuredWidth();

        //get the available size of child view
        int childLeft = 0;
        int childTop =(int)(6 * dp1);

        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() != GONE) {
                LayoutUtils.layoutChild(child, childLeft, childTop);
                childTop += child.getMeasuredHeight();
            }
        }
    }
}
