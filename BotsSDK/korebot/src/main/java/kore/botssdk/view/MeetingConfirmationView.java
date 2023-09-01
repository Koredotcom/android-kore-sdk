package kore.botssdk.view;

import static kore.botssdk.utils.DateUtils.getSlotsDate;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;

import java.util.ArrayList;
import java.util.Locale;

import kore.botssdk.R;
import kore.botssdk.event.KoreEventCenter;
import kore.botssdk.events.ProfileColorUpdateEvent;
import kore.botssdk.listener.ComposeFooterInterface;
import kore.botssdk.listener.InvokeGenericWebViewInterface;
import kore.botssdk.models.MeetingConfirmationModel;
import kore.botssdk.models.MeetingSlotModel;
import kore.botssdk.net.SDKConfiguration;
import kore.botssdk.utils.BundleConstants;
import kore.botssdk.utils.DateUtils;
import kore.botssdk.utils.StringUtils;
import kore.botssdk.view.viewUtils.DimensionUtil;
import kore.botssdk.view.viewUtils.LayoutUtils;
import kore.botssdk.view.viewUtils.MeasureUtils;

public class MeetingConfirmationView extends ViewGroup {
    private TextView locationView;
    private TextView titleView;
    private TextView tv_users;
    private View slotLayout;
    private TextView label;
    private TextView slots;

    float dp1;


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

    public ComposeFooterInterface getComposeFooterInterface() {
        return composeFooterInterface;
    }

    public void setComposeFooterInterface(ComposeFooterInterface composeFooterInterface) {
        this.composeFooterInterface = composeFooterInterface;
    }

    private InvokeGenericWebViewInterface invokeGenericWebViewInterface;
    private ComposeFooterInterface composeFooterInterface;


    private void init() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.meeting_confirmation_layout, this, true);
        locationView = view.findViewById(R.id.location_view);
        slotLayout = view.findViewById(R.id.slot_confirm_layout);
        LayerDrawable shape = (LayerDrawable) ResourcesCompat.getDrawable(getContext().getResources(), R.drawable.shadow_layer_background, getContext().getTheme());
        assert shape != null;
        GradientDrawable outer = (GradientDrawable) shape.findDrawableByLayerId(R.id.inner);
        outer.setColor(Color.parseColor(SDKConfiguration.BubbleColors.getProfileColor())+ BundleConstants.TRANSPERANCY_50_PERCENT);
        slotLayout.setBackground(shape);
        titleView = view.findViewById(R.id.title_view);
        tv_users = view.findViewById(R.id.tv_users);
        dp1 = (int) DimensionUtil.dp1;
        label = view.findViewById(R.id.label);
        slots = view.findViewById(R.id.time_slots);
    }

    public void onEvent(ProfileColorUpdateEvent event){
        LayerDrawable shape = (LayerDrawable) ResourcesCompat.getDrawable(getContext().getResources(), R.drawable.shadow_layer_background, getContext().getTheme());
        assert shape != null;
        GradientDrawable outer = (GradientDrawable) shape.findDrawableByLayerId(R.id.inner);
        outer.setColor(Color.parseColor(SDKConfiguration.BubbleColors.getProfileColor())+ BundleConstants.TRANSPERANCY_50_PERCENT);
        slotLayout.setBackground(shape);
    }
    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        KoreEventCenter.register(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        KoreEventCenter.unregister(this);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int parentWidth = MeasureSpec.getSize(widthMeasureSpec);
        int wrapSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);


        int totalHeight = getPaddingTop();
        int childWidthSpec;

        childWidthSpec = MeasureSpec.makeMeasureSpec((int) (parentWidth - 28 * dp1), MeasureSpec.EXACTLY);
        MeasureUtils.measure(slotLayout, childWidthSpec, wrapSpec);
        if (slotLayout.getVisibility() == VISIBLE) {
            totalHeight += slotLayout.getMeasuredHeight() + getPaddingBottom() + getPaddingTop();
        }
        int parentHeightSpec = MeasureSpec.makeMeasureSpec(totalHeight, MeasureSpec.EXACTLY);
        int parentWidthSpec = MeasureSpec.makeMeasureSpec(slotLayout.getMeasuredWidth(), MeasureSpec.AT_MOST);
        setMeasuredDimension(parentWidthSpec, parentHeightSpec);
    }


    public void populateData(final MeetingConfirmationModel meetingConfirmationModel) {

        if (meetingConfirmationModel != null) {
            slotLayout.setVisibility(VISIBLE);
            titleView.setText(meetingConfirmationModel.getTitle());
            if (!StringUtils.isNullOrEmptyWithTrim(meetingConfirmationModel.getWhere())) {
                locationView.setText(meetingConfirmationModel.getWhere());
                locationView.setVisibility(VISIBLE);
            } else {
                locationView.setVisibility(GONE);
            }

            if(TextUtils.isEmpty(getFormatedAttendiesFromList(meetingConfirmationModel.getAttendees())))
                tv_users.setVisibility(View.GONE);
            else
                tv_users.setText(getFormatedAttendiesFromList(meetingConfirmationModel.getAttendees()));


            if (meetingConfirmationModel.getSlots() != null && meetingConfirmationModel.getSlots().size() > 0) {
                slots.setVisibility(View.VISIBLE);
                slots.setText(getSlotsViewText(meetingConfirmationModel.getSlots()));
                label.setVisibility(View.VISIBLE);
                label.setText(meetingConfirmationModel.getSlots().size() > 1 ? "Selected Slots" : "Selected Slot");
            } else {
                slots.setVisibility(View.GONE);
                label.setVisibility(View.GONE);
            }
            //GON
        } else {
            slotLayout.setVisibility(GONE);
        }

    }

    private String getSlotsViewText(ArrayList<MeetingSlotModel.Slot> slots) {
        StringBuilder message = new StringBuilder();
        for (MeetingSlotModel.Slot slot : slots) {
            //crashed for sapuser@kore.com account, getting slot null
            if(slot != null) {
                message.append(getSlotsDate(slot.getStart())).append(", ").append(DateUtils.getTimeInAmPm(slot.getStart())).append(" to ").append(DateUtils.getTimeInAmPm(slot.getEnd())).append("\n");
            }
        }
        return message.toString().trim();
    }



    private String getFormatedAttendiesFromList(ArrayList<MeetingConfirmationModel.UserDetailModel> userDetailModels) {
        //  String users = "";
        if (userDetailModels != null && userDetailModels.size() > 0) {
            if (userDetailModels.size() == 1) {
                return userDetailModels.get(0).getFirstName();
            } else {
                int remaining = userDetailModels.size() - 1;
                if(remaining > 1)
                    return String.format(Locale.US, "%1$s  and %2$d others", userDetailModels.get(0).getFirstName(), remaining);
                else
                    return String.format(Locale.US, "%1$s  and %2$d other", userDetailModels.get(0).getFirstName(), remaining);
            }

        }


        return "";


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
