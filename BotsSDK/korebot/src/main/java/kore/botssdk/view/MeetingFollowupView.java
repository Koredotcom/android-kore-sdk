package kore.botssdk.view;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;

import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import java.util.ArrayList;

import kore.botssdk.R;
import kore.botssdk.adapter.MeetingFollowupAdapter;

import kore.botssdk.application.AppControl;
import kore.botssdk.listener.ComposeFooterInterface;
import kore.botssdk.listener.InvokeGenericWebViewInterface;

import kore.botssdk.models.MeetingFollowUpModel;
import kore.botssdk.models.MeetingSlotModel;
import kore.botssdk.models.MeetingTemplateModel;
import kore.botssdk.utils.KaFontUtils;
import kore.botssdk.view.viewUtils.LayoutUtils;
import kore.botssdk.view.viewUtils.MeasureUtils;

public class MeetingFollowupView extends ViewGroup {
    private RecyclerView autoExpandListView;

    private View meeting_followup_layout;
    private MeetingFollowupAdapter followupAdapter;

    float dp1;
    private float restrictedLayoutWidth;


    public void setRestrictedLayoutWidth(float restrictedLayoutWidth) {
        this.restrictedLayoutWidth = restrictedLayoutWidth;
    }

    public MeetingFollowupView(Context context) {
        super(context);
        init();
    }

    public MeetingFollowupView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MeetingFollowupView(Context context, AttributeSet attrs, int defStyleAttr) {
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
        View view = LayoutInflater.from(getContext()).inflate(R.layout.meeting_followup_template, this, true);
        autoExpandListView = (RecyclerView) view.findViewById(R.id.followup_list);

        autoExpandListView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        autoExpandListView.setHasFixedSize(true);
        KaFontUtils.applyCustomFont(getContext(), view);
        meeting_followup_layout = view.findViewById(R.id.meeting_followup_layout);
        followupAdapter=new MeetingFollowupAdapter(this.getContext(),this);
        dp1 = (int) AppControl.getInstance().getDimensionUtil().dp1;
        //populateData(getDummyData(),true);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int wrapSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        View rootView = meeting_followup_layout;
        int parentWidth = MeasureSpec.getSize(widthMeasureSpec);
        int totalHeight = getPaddingTop();
        int childWidthSpec;

        childWidthSpec = MeasureSpec.makeMeasureSpec((int) (parentWidth - 28 * dp1), MeasureSpec.EXACTLY);
        MeasureUtils.measure(rootView, childWidthSpec, wrapSpec);

        totalHeight += rootView.getMeasuredHeight() + getPaddingBottom();

        int parentHeightSpec = MeasureSpec.makeMeasureSpec(totalHeight, MeasureSpec.EXACTLY);
        int parentWidthSpec = MeasureSpec.makeMeasureSpec(rootView.getMeasuredWidth(), MeasureSpec.EXACTLY);
        setMeasuredDimension(parentWidthSpec, parentHeightSpec);
    }

    public void setMeetingLayoutAlpha(boolean isEnabled){
        meeting_followup_layout.setAlpha(isEnabled ? 1.0f : 0.5f);
        followupAdapter.setEnabled(isEnabled);




    }

    public void populateData(final ArrayList<MeetingFollowUpModel> meetingTemplateModel, boolean isEnabled) {

        if (meetingTemplateModel != null) {
            meeting_followup_layout.setVisibility(VISIBLE);
            followupAdapter = new MeetingFollowupAdapter(this.getContext(), this);
            followupAdapter.setMeetingsModelArrayList(meetingTemplateModel);
            followupAdapter.setEnabled(isEnabled);
            followupAdapter.setComposeFooterInterface(composeFooterInterface);
            autoExpandListView.setAdapter(followupAdapter);
            followupAdapter.notifyDataSetChanged();

        } else {
          autoExpandListView.setAdapter(followupAdapter);
           meeting_followup_layout.setVisibility(GONE);

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
