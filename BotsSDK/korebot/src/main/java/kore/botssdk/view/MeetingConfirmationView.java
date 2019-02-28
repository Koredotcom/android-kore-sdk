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
import kore.botssdk.utils.DateUtils;
import kore.botssdk.utils.StringUtils;
import kore.botssdk.view.viewUtils.LayoutUtils;
import kore.botssdk.view.viewUtils.MeasureUtils;

import static kore.botssdk.utils.DateUtils.getTimeInAmPm;

public class MeetingConfirmationView extends ViewGroup {
    private TextView locationView;
    private TextView titleView;
    private TextView dateView, tv_time, tv_users;
    LinearLayout lin_timeview, linlocation;
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
        //RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        //recyclerView.setLayoutManager(layoutManager);
        //recyclerView.setItemAnimator(new DefaultItemAnimator());
        // KaFontUtils.applyCustomFont(getContext(), view);
        locationView = (TextView) view.findViewById(R.id.location_view);
        slotLayout = view.findViewById(R.id.slot_confirm_layout);
        titleView = (TextView) view.findViewById(R.id.title_view);
        dateView = (TextView) view.findViewById(R.id.date_view);
        tv_users = (TextView) view.findViewById(R.id.tv_users);
        tv_time = (TextView) view.findViewById(R.id.tv_time);
        lin_timeview = (LinearLayout) view.findViewById(R.id.lin_timeview);
        linlocation = (LinearLayout) view.findViewById(R.id.linlocation);
        dp1 = (int) AppControl.getInstance().getDimensionUtil().dp1;
        //backgroundDrawable = getContext().getResources().getDrawable(R.drawable.bottom_right_rounded_rectangle);
        // slotLayout.setBackground(backgroundDrawable);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int parentWidth = MeasureSpec.getSize(widthMeasureSpec);
        int wrapSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);


        int totalHeight = getPaddingTop();
        int childWidthSpec;

        childWidthSpec = MeasureSpec.makeMeasureSpec((int) (parentWidth - 28 * dp1), MeasureSpec.EXACTLY);
        MeasureUtils.measure(slotLayout, childWidthSpec, wrapSpec);
        if(slotLayout.getVisibility() == VISIBLE ) {
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
                linlocation.setVisibility(VISIBLE);
            } else {
                locationView.setVisibility(GONE);
                linlocation.setVisibility(GONE);
            }

            tv_users.setText(TextUtils.isEmpty(getFormatedAttendiesFromList(meetingConfirmationModel.getAttendees())) ? ""
                    : getFormatedAttendiesFromList(meetingConfirmationModel.getAttendees()));


            if (meetingConfirmationModel.getDate() > 0 &&
                    meetingConfirmationModel.getSlot_end() > 0 &&
                    meetingConfirmationModel.getSlot_start() > 0) {

                String startTime = getTimeInAmPm(meetingConfirmationModel.getSlot_start()).toUpperCase();
                String endTime = getTimeInAmPm(meetingConfirmationModel.getSlot_end()).toUpperCase();
                //  dateView.setText(MessageFormat.format("{0}, {1} to {2} ", DateUtils.getDate(meetingConfirmationModel.getDate()), startTime, endTime));
                dateView.setText(MessageFormat.format("{0}", DateUtils.getDateinMeetingFormat(meetingConfirmationModel.getDate()).toUpperCase()));
                tv_time.setText(startTime + "\n" + endTime);
                dateView.setVisibility(VISIBLE);
                tv_time.setVisibility(VISIBLE);
            } else {
                dateView.setVisibility(GONE);
                tv_time.setVisibility(GONE);
                lin_timeview.setVisibility(GONE);


            }
            //GON
        } else {
            slotLayout.setVisibility(GONE);
        }

    }

   /* private String getAttendies(ArrayList<String> list) {
        String users = "";
        if (list != null && list.size() > 0) {


            for (int i = 0; i < list.size(); i++) {

                if (list.size() == 1) {
                    users += list.get(i);
                    return users;
                } else if (i > 2) {
                    int remaining_users = list.size() - 3;
                    users = String.format(users + " and %1$d others", remaining_users);
                    return users;
                } else if (i == (list.size() - 1)) {
                    users += " And " + list.get(i);
                } else {
                    users += TextUtils.isEmpty(users) ? "" : ", ";
                    users += list.get(i);
                }
                //  users.substring(0, users.length() - 2);
            }

        }


        return users;
    }*/


    private String getFormatedAttendiesFromList(ArrayList<MeetingConfirmationModel.UserDetailModel> userDetailModels) {
        String users = "";
        if (userDetailModels != null && userDetailModels.size() > 0) {
            if (userDetailModels.size() == 1) {
                return userDetailModels.get(0).getFirstName();
            } else if (userDetailModels.size() == 2) {

                return String.format("%1$s and %2$s", userDetailModels.get(0).getFirstName(), userDetailModels.get(1).getFirstName());
            } else if (userDetailModels.size() == 3) {

                return String.format("%1$s , %2$s and %3$s", userDetailModels.get(0).getFirstName(), userDetailModels.get(1).getFirstName(), userDetailModels.get(2).getFirstName());
            } else {
                int remaining = userDetailModels.size() - 3;
                return String.format("%1$s , %2$s , %3$s and %4$d others", userDetailModels.get(0).getFirstName(),
                        userDetailModels.get(1).getFirstName(), userDetailModels.get(2).getFirstName(), remaining);
            }
        }


        return users;

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
