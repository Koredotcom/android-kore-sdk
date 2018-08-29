package kore.botssdk.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;

import kore.botssdk.R;
import kore.botssdk.adapter.AttendeeSlotsAdapter;
import kore.botssdk.application.AppControl;
import kore.botssdk.fragment.ComposeFooterFragment;
import kore.botssdk.listener.InvokeGenericWebViewInterface;
import kore.botssdk.models.AttendeeSlotTemplateModel;
import kore.botssdk.models.MeetingSlotModel;
import kore.botssdk.utils.KaFontUtils;
import kore.botssdk.view.viewUtils.LayoutUtils;
import kore.botssdk.view.viewUtils.MeasureUtils;

public class AttendeeSlotSelectionView extends ViewGroup {
    private AutoExpandListView autoExpandListView;
    private View meetingLayout;
    private TextView confirmView,declineView;

    float dp1;
    Gson gson = new Gson();
    private float restrictedLayoutWidth;
    public static HashMap<Integer,ArrayList> dataMap = new HashMap<>();



    public void setRestrictedLayoutWidth(float restrictedLayoutWidth) {
        this.restrictedLayoutWidth = restrictedLayoutWidth;
    }

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

    public ComposeFooterFragment.ComposeFooterInterface getComposeFooterInterface() {
        return composeFooterInterface;
    }

    public void setComposeFooterInterface(ComposeFooterFragment.ComposeFooterInterface composeFooterInterface) {
        this.composeFooterInterface = composeFooterInterface;
    }

    private InvokeGenericWebViewInterface invokeGenericWebViewInterface;
    private ComposeFooterFragment.ComposeFooterInterface composeFooterInterface;


    private void init() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.attendee_slot_selection_view, this, true);
        autoExpandListView = (AutoExpandListView) view.findViewById(R.id.slots_list);
        confirmView = (TextView) view.findViewById(R.id.confirm);
        declineView = (TextView) view.findViewById(R.id.decline);

        KaFontUtils.applyCustomFont(getContext(), view);
        meetingLayout = view.findViewById(R.id.meeting_layout);
        dp1 = (int) AppControl.getInstance().getDimensionUtil().dp1;
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int wrapSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);

        int totalHeight = getPaddingTop();
        int childWidthSpec;

        childWidthSpec = MeasureSpec.makeMeasureSpec((int) restrictedLayoutWidth, MeasureSpec.EXACTLY);
        MeasureUtils.measure(meetingLayout, childWidthSpec, wrapSpec);
        if(meetingLayout.getMeasuredHeight() !=0) {
            totalHeight += meetingLayout.getMeasuredHeight() + getPaddingBottom() + getPaddingTop()+12 * dp1;
        }
        int parentHeightSpec = MeasureSpec.makeMeasureSpec(totalHeight, MeasureSpec.EXACTLY);
        int parentWidthSpec = MeasureSpec.makeMeasureSpec(meetingLayout.getMeasuredWidth(), MeasureSpec.AT_MOST);
        setMeasuredDimension(parentWidthSpec, parentHeightSpec);
    }


    public void populateData(final int viewPosition,final AttendeeSlotTemplateModel meetingTemplateModel, final boolean isEnabled) {
        final AttendeeSlotsAdapter slotsButtonAdapter;
            ArrayList<MeetingSlotModel.Slot> popularSlots = meetingTemplateModel != null ? meetingTemplateModel.getPopularSlots() : new ArrayList<MeetingSlotModel.Slot>();
            ArrayList<MeetingSlotModel.Slot> otherSlots = meetingTemplateModel != null ? meetingTemplateModel.getOtherSlots() : new ArrayList<MeetingSlotModel.Slot>();
            slotsButtonAdapter = new AttendeeSlotsAdapter(getContext());
            slotsButtonAdapter.setNormalSlots(otherSlots);
            slotsButtonAdapter.setPopularSlots(popularSlots);

            if (meetingTemplateModel != null && (!isEnabled || dataMap.get(viewPosition) == null)) {
                slotsButtonAdapter.addSelectedSlots(popularSlots);
                slotsButtonAdapter.addSelectedSlots(otherSlots);
                if (isEnabled && dataMap.get(viewPosition) == null) {
                    dataMap.put(viewPosition, slotsButtonAdapter.getSelectedSlots());
                }else if(!isEnabled){
                    dataMap.remove(viewPosition);
                }
            } else if (isEnabled && dataMap.get(viewPosition) != null) {
                slotsButtonAdapter.setSelectedSlots(dataMap.get(viewPosition));
            }
            autoExpandListView.setAdapter(slotsButtonAdapter);
            slotsButtonAdapter.notifyDataSetChanged();
            meetingLayout.setAlpha(isEnabled ? 1.0f : 0.5f);
            confirmView.setAlpha(slotsButtonAdapter.getSelectedSlots().size() > 0 ? 1.0f : 0.5f);
            meetingLayout.setVisibility(meetingTemplateModel != null ? VISIBLE : GONE);
            autoExpandListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if(isEnabled) {
                        slotsButtonAdapter.addOrRemoveSelectedSlot(slotsButtonAdapter.getItem(position));
                        slotsButtonAdapter.notifyDataSetChanged();
                        dataMap.put(viewPosition, slotsButtonAdapter.getSelectedSlots());
                        confirmView.setAlpha(slotsButtonAdapter.getSelectedSlots().size() > 0 ? 1.0f : 0.5f);
                    }

                }
            });

            confirmView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    ArrayList<MeetingSlotModel.Slot> slots = slotsButtonAdapter.getSelectedSlots();
                    if(isEnabled && slots.size() > 0){
                        HashMap<String,ArrayList> payload = new HashMap<>();
                        payload.put("slots",slots);
                        composeFooterInterface.sendWithSomeDelay(getContext().getResources().getQuantityString(R.plurals.confirm_slots,slots.size(),slots.size()) ,gson.toJson(payload),0);
                    }

                }
            });
            declineView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    HashMap<String,ArrayList> payload = new HashMap<>();
                    composeFooterInterface.sendWithSomeDelay("Decline" ,gson.toJson(payload),0);

                }
            });
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        final int count = getChildCount();
        //get the available size of child view
        int childLeft = 0;
        int childTop = (int) (12 * dp1);

        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() != GONE) {
                LayoutUtils.layoutChild(child, childLeft, childTop);
                childTop += child.getMeasuredHeight();
            }
        }
    }
}
