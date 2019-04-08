package kore.botssdk.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.MessageFormat;
import java.util.ArrayList;

import kore.botssdk.R;
import kore.botssdk.application.AppControl;
import kore.botssdk.listener.ComposeFooterInterface;
import kore.botssdk.listener.InvokeGenericWebViewInterface;
import kore.botssdk.models.MeetingConfirmationModel;
import kore.botssdk.models.MeetingSlotModel;
import kore.botssdk.utils.DateUtils;
import kore.botssdk.utils.StringUtils;
import kore.botssdk.view.viewUtils.LayoutUtils;
import kore.botssdk.view.viewUtils.MeasureUtils;

import static kore.botssdk.utils.DateUtils.getSlotsDate;
import static kore.botssdk.utils.DateUtils.getTimeInAmPm;

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
        locationView = (TextView) view.findViewById(R.id.location_view);
        slotLayout = view.findViewById(R.id.slot_confirm_layout);
        titleView = (TextView) view.findViewById(R.id.title_view);
        tv_users = (TextView) view.findViewById(R.id.tv_users);
        dp1 = (int) AppControl.getInstance().getDimensionUtil().dp1;
        label = view.findViewById(R.id.label);
        slots = view.findViewById(R.id.time_slots);
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

            tv_users.setText(TextUtils.isEmpty(getFormatedAttendiesFromList(meetingConfirmationModel.getAttendees())) ? ""
                    : getFormatedAttendiesFromList(meetingConfirmationModel.getAttendees()));


            if (meetingConfirmationModel.getSlots() != null && meetingConfirmationModel.getSlots().size() > 0) {
                slots.setText(getSlotsViewText(meetingConfirmationModel.getSlots()));
                label.setText(meetingConfirmationModel.getSlots().size() > 1 ? "Selected Slots" : "Selected Slot");
            } else {
                slots.setText("");
                label.setText("");
            }
            //GON
        } else {
            slotLayout.setVisibility(GONE);
        }

    }

    private String getSlotsViewText(ArrayList<MeetingSlotModel.Slot> slots) {
        StringBuilder message = new StringBuilder();
        for (MeetingSlotModel.Slot slot : slots) {
            message.append(getSlotsDate(slot.getStart())).append(", ").append(DateUtils.getTimeInAmPm(slot.getStart())).append(" to ").append(DateUtils.getTimeInAmPm(slot.getEnd())).append("\n");
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
                return String.format("%1$s  and %2$d others", userDetailModels.get(0).getFirstName(), remaining);

            }

        }


        return "";


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
