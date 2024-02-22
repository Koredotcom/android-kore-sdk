package kore.botssdk.view;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;

import kore.botssdk.R;
import kore.botssdk.adapter.AttendeeSlotsAdapter;
import kore.botssdk.databinding.AttendeeSlotSelectionViewBinding;
import kore.botssdk.event.KoreEventCenter;
import kore.botssdk.events.SendWithSomeDelay;
import kore.botssdk.listener.ComposeFooterInterface;
import kore.botssdk.listener.InvokeGenericWebViewInterface;
import kore.botssdk.models.AttendeeSlotTemplateModel;
import kore.botssdk.models.MeetingSlotModel;
import kore.botssdk.utils.SelectionUtils;
import kore.botssdk.view.viewUtils.DimensionUtil;
import kore.botssdk.view.viewUtils.LayoutUtils;
import kore.botssdk.view.viewUtils.MeasureUtils;

public class AttendeeSlotSelectionView extends ViewGroup implements AttendeeSlotsAdapter.SlotSelectionListener {

    private AttendeeSlotSelectionViewBinding attendeeSlotSelectionViewBinding;

    float dp1;
    final Gson gson = new Gson();

    public AttendeeSlotSelectionView(Context context) {
        super(context);
        init();
    }

    public AttendeeSlotSelectionView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AttendeeSlotSelectionView(Context context, AttributeSet attrs, int defStyleAttr) {
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
    ComposeFooterInterface composeFooterInterface;


    private void init() {
        attendeeSlotSelectionViewBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.attendee_slot_selection_view, this, true);
        dp1 = (int) DimensionUtil.dp1;
        attendeeSlotSelectionViewBinding.slotsList.setItemAnimator(null);
        ((GradientDrawable)attendeeSlotSelectionViewBinding.confirm.getBackground()).setColor(getContext().getResources().getColor(R.color.splash_color));
        ((GradientDrawable)attendeeSlotSelectionViewBinding.decline.getBackground()).setColor(getContext().getResources().getColor(R.color.white));
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int wrapSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        View rootView = attendeeSlotSelectionViewBinding.getRoot();
        int parentWidth = MeasureSpec.getSize(widthMeasureSpec);
        int totalHeight = getPaddingTop();
        int childWidthSpec;

        childWidthSpec = MeasureSpec.makeMeasureSpec((int) (parentWidth - 28 * dp1), MeasureSpec.EXACTLY);
        MeasureUtils.measure(rootView, childWidthSpec, wrapSpec);

        totalHeight += rootView.getMeasuredHeight() + getPaddingBottom();

        int parentHeightSpec = MeasureSpec.makeMeasureSpec(totalHeight, MeasureSpec.EXACTLY);
        int parentWidthSpec = MeasureSpec.makeMeasureSpec(rootView.getMeasuredWidth(), MeasureSpec.AT_MOST);
        setMeasuredDimension(parentWidthSpec, parentHeightSpec);
    }


    public void populateData(final AttendeeSlotTemplateModel meetingTemplateModel, final boolean isEnabled) {
        View rootView = attendeeSlotSelectionViewBinding.getRoot();
        if (meetingTemplateModel != null) {
            attendeeSlotSelectionViewBinding.setMeetingModel(meetingTemplateModel.getMeetingDetails());
            rootView.setVisibility(VISIBLE);
            final AttendeeSlotsAdapter slotsButtonAdapter;
            ArrayList<MeetingSlotModel.Slot> popularSlots = meetingTemplateModel.getPopularSlots();
            ArrayList<MeetingSlotModel.Slot> otherSlots = meetingTemplateModel.getOtherSlots();

            slotsButtonAdapter = new AttendeeSlotsAdapter(getContext(),this);
            slotsButtonAdapter.setNormalSlots(otherSlots);
            slotsButtonAdapter.setPopularSlots(popularSlots);
            slotsButtonAdapter.setHasStableIds(true);
            slotsButtonAdapter.setEnabled(isEnabled);

            if (SelectionUtils.getSelectedSlots() == null || SelectionUtils.getSelectedSlots().size() == 0) {
                slotsButtonAdapter.addSelectedSlots(popularSlots);
                slotsButtonAdapter.addSelectedSlots(otherSlots);
            } else {
                slotsButtonAdapter.setSelectedSlots(SelectionUtils.getSelectedSlots());
            }

            attendeeSlotSelectionViewBinding.slotsList.setAdapter(slotsButtonAdapter);
            slotsButtonAdapter.notifyDataSetChanged();
            attendeeSlotSelectionViewBinding.actions.setAlpha(isEnabled ? 1.0f : 0.5f);
            attendeeSlotSelectionViewBinding.confirm.setAlpha(slotsButtonAdapter.getSelectedSlots().size() > 0 ? 1.0f : 0.5f);

            attendeeSlotSelectionViewBinding.confirm.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    ArrayList<MeetingSlotModel.Slot> slots = slotsButtonAdapter.getSelectedSlots();
                    if (isEnabled && slots.size() > 0) {
                        HashMap<String, ArrayList> payload = new HashMap<>();
                        payload.put("slots", slots);
                        if(composeFooterInterface != null)
                            composeFooterInterface.sendWithSomeDelay(getContext().getResources().getQuantityString(R.plurals.confirm_slots, slots.size(), slots.size()), gson.toJson(payload), 0,false);
                        else
                            KoreEventCenter.post(new SendWithSomeDelay(getContext().getResources().getQuantityString(R.plurals.confirm_slots, slots.size(), slots.size()), gson.toJson(payload), 0,false));
                    }

                }
            });
            attendeeSlotSelectionViewBinding.decline.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    HashMap<String, ArrayList> payload = new HashMap<>();
                    if(composeFooterInterface != null)
                        composeFooterInterface.sendWithSomeDelay("Decline", gson.toJson(payload), 0,false);
                    else
                        KoreEventCenter.post(new SendWithSomeDelay("Decline", gson.toJson(payload), 0,false));

                }
            });
        } else {
            rootView.setVisibility(GONE);
            attendeeSlotSelectionViewBinding.setMeetingModel(null);
            attendeeSlotSelectionViewBinding.slotsList.setAdapter(null);
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

    @Override
    public void onSlotSelectionChanged() {
        attendeeSlotSelectionViewBinding.confirm.setAlpha(SelectionUtils.getSelectedSlots().size() > 0 ? 1.0f : 0.5f);
    }
}
