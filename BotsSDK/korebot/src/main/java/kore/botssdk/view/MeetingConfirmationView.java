package kore.botssdk.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;

import java.text.MessageFormat;

import kore.botssdk.R;
import kore.botssdk.adapter.ProfileIndicationAdapter;
import kore.botssdk.application.AppControl;
import kore.botssdk.fragment.ComposeFooterFragment;
import kore.botssdk.listener.InvokeGenericWebViewInterface;
import kore.botssdk.models.MeetingConfirmationModel;
import kore.botssdk.utils.DateUtils;
import kore.botssdk.utils.KaFontUtils;
import kore.botssdk.utils.StringUtils;
import kore.botssdk.view.viewUtils.LayoutUtils;
import kore.botssdk.view.viewUtils.MeasureUtils;

import static kore.botssdk.utils.DateUtils.getTimeInAmPm;

public class MeetingConfirmationView extends ViewGroup {
    private RecyclerView recyclerView;
    private TextView locationView;
    private TextView titleView;
    private TextView dateView;

    private View slotLayout;

    float dp1;
    private float restrictedLayoutWidth;
    private Drawable backgroundDrawable;


    public void setRestrictedLayoutWidth(float restrictedLayoutWidth) {
        this.restrictedLayoutWidth = restrictedLayoutWidth;
    }

    public MeetingConfirmationView(Context context) {
        super(context);
        init();
    }

    public MeetingConfirmationView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MeetingConfirmationView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public InvokeGenericWebViewInterface getInvokeGenericWebViewInterface() {
        return invokeGenericWebViewInterface;
    }


    public void setInvokeGenericWebViewInterface(InvokeGenericWebViewInterface invokeGenericWebViewInterface) {
        this.invokeGenericWebViewInterface = invokeGenericWebViewInterface;
    }

    public ComposeFooterFragment.ComposeFooterInterface getComposeFooterInterface() {
        return composeFooterInterface;
    }

    public void setComposeFooterInterface(ComposeFooterFragment.ComposeFooterInterface composeFooterInterface) {
        this.composeFooterInterface = composeFooterInterface;
    }

    private InvokeGenericWebViewInterface invokeGenericWebViewInterface;
    private ComposeFooterFragment.ComposeFooterInterface composeFooterInterface;


    private void init() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.meeting_confirmation_layout, this, true);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        //RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        //recyclerView.setLayoutManager(layoutManager);
        //recyclerView.setItemAnimator(new DefaultItemAnimator());
       // KaFontUtils.applyCustomFont(getContext(), view);
        locationView = (TextView) view.findViewById(R.id.location_view);
        slotLayout = view.findViewById(R.id.slot_confirm_layout);
        titleView = (TextView) view.findViewById(R.id.title_view);
        dateView = (TextView) view.findViewById(R.id.date_view);
        dp1 = (int) AppControl.getInstance().getDimensionUtil().dp1;
        //backgroundDrawable = getContext().getResources().getDrawable(R.drawable.bottom_right_rounded_rectangle);
        //slotLayout.setBackground(backgroundDrawable);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int wrapSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);

        int totalHeight = getPaddingTop();
        int childWidthSpec;

        childWidthSpec = MeasureSpec.makeMeasureSpec((int) restrictedLayoutWidth, MeasureSpec.EXACTLY);
        MeasureUtils.measure(slotLayout, childWidthSpec, wrapSpec);

        totalHeight += slotLayout.getMeasuredHeight() + getPaddingBottom() + getPaddingTop();

        int parentHeightSpec = MeasureSpec.makeMeasureSpec(totalHeight, MeasureSpec.EXACTLY);
        int parentWidthSpec = MeasureSpec.makeMeasureSpec(slotLayout.getMeasuredWidth(), MeasureSpec.AT_MOST);
        setMeasuredDimension(parentWidthSpec, parentHeightSpec);
    }


    public void populateData(final MeetingConfirmationModel meetingConfirmationModel) {

        if (meetingConfirmationModel != null) {
            ProfileIndicationAdapter profileIndicationAdapter;
            if(recyclerView.getAdapter() == null) {
                profileIndicationAdapter = new ProfileIndicationAdapter(getContext(), recyclerView);
                recyclerView.setAdapter(profileIndicationAdapter);
            }else{
                profileIndicationAdapter = (ProfileIndicationAdapter) recyclerView.getAdapter();
            }
            profileIndicationAdapter.setUserDetailModels(meetingConfirmationModel.getAttendees());
            profileIndicationAdapter.notifyDataSetChanged();

            slotLayout.setVisibility(VISIBLE);
            titleView.setText(meetingConfirmationModel.getTitle());
            if (!StringUtils.isNullOrEmptyWithTrim(meetingConfirmationModel.getWhere())) {
                locationView.setText(meetingConfirmationModel.getWhere());
                locationView.setVisibility(VISIBLE);
            } else {
                locationView.setVisibility(GONE);
            }
            if(meetingConfirmationModel.getDate() > 0 && meetingConfirmationModel.getSlot_end() > 0 && meetingConfirmationModel.getSlot_start() > 0) {
                String startTime = getTimeInAmPm(meetingConfirmationModel.getSlot_start()).toLowerCase();
                String endTime = getTimeInAmPm(meetingConfirmationModel.getSlot_end()).toLowerCase();
                dateView.setText(MessageFormat.format("{0}, {1} to {2} ", DateUtils.getDate(meetingConfirmationModel.getDate()), startTime, endTime));
                dateView.setVisibility(VISIBLE);
            }else{
                dateView.setVisibility(GONE);
            }
            recyclerView.setVisibility(VISIBLE);
        } else {
            slotLayout.setVisibility(GONE);
        }

    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        final int count = getChildCount();
        int parentWidth = getMeasuredWidth();

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
