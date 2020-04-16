package kore.botssdk.view;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
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
import kore.botssdk.listener.ComposeFooterInterface;
import kore.botssdk.listener.InvokeGenericWebViewInterface;
import kore.botssdk.models.BotButtonModel;
import kore.botssdk.models.MeetingSlotModel;
import kore.botssdk.models.MeetingTemplateModel;
import kore.botssdk.utils.KaFontUtils;
import kore.botssdk.view.viewUtils.LayoutUtils;
import kore.botssdk.view.viewUtils.MeasureUtils;

public class MeetingSlotsView extends ViewGroup {
    private AutoExpandListView autoExpandListView;
    private TextView button1, button2;
    private View meetingLayout;
    private MeetingSlotsButtonAdapter slotsButtonAdapter;

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
        KaFontUtils.applyCustomFont(getContext(), view);
        meetingLayout = view.findViewById(R.id.meeting_layout);
        button1 = (TextView) view.findViewById(R.id.button1);
        button2 = view.findViewById(R.id.button2);
        dp1 = (int) AppControl.getInstance().getDimensionUtil().dp1;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            button1.setLetterSpacing(0.06f);
            button2.setLetterSpacing(0.06f);
        }
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int parentWidth = MeasureSpec.getSize(widthMeasureSpec);
        int wrapSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);


        int totalHeight = getPaddingTop();
        int childWidthSpec;

        childWidthSpec = MeasureSpec.makeMeasureSpec((int) (parentWidth - 28 * dp1), MeasureSpec.EXACTLY);
        MeasureUtils.measure(meetingLayout, childWidthSpec, wrapSpec);

        totalHeight += meetingLayout.getMeasuredHeight() + getPaddingBottom() + getPaddingTop();

        int parentHeightSpec = MeasureSpec.makeMeasureSpec(totalHeight, MeasureSpec.EXACTLY);
        int parentWidthSpec = MeasureSpec.makeMeasureSpec(meetingLayout.getMeasuredWidth(), MeasureSpec.AT_MOST);
        setMeasuredDimension(parentWidthSpec, parentHeightSpec);
    }

    public void setMeetingLayoutAlpha(boolean isEnabled){
        meetingLayout.setAlpha(isEnabled ? 1.0f : 0.5f);
        slotsButtonAdapter.setEnabled(isEnabled);

        button1.setTextColor(getResources().getColor(isEnabled ? R.color.splash_color : R.color.color_a7b0be));
        button2.setTextColor(getResources().getColor(isEnabled ? R.color.splash_color : R.color.color_a7b0be));
        ((GradientDrawable) button1.getBackground()).setStroke(2, isEnabled ? getResources().getColor(R.color.splash_color) : getResources().getColor(R.color.color_a7b0be));
        ((GradientDrawable) button2.getBackground()).setStroke(2, isEnabled ? getResources().getColor(R.color.splash_color) : getResources().getColor(R.color.color_a7b0be));

        button1.setEnabled(isEnabled);
        button2.setEnabled(isEnabled);


    }

    public void populateData(final MeetingTemplateModel meetingTemplateModel, boolean isEnabled) {

        if (meetingTemplateModel != null) {
            meetingLayout.setVisibility(VISIBLE);
            ArrayList<MeetingSlotModel.Slot> slots = meetingTemplateModel.getQuick_slots();
            slotsButtonAdapter = new MeetingSlotsButtonAdapter(getContext(), this);
            slotsButtonAdapter.setMeetingsModelArrayList(slots);
            slotsButtonAdapter.setEnabled(isEnabled);
            slotsButtonAdapter.setComposeFooterInterface(composeFooterInterface);
            autoExpandListView.setAdapter(slotsButtonAdapter);
            slotsButtonAdapter.notifyDataSetChanged();
            final ArrayList<BotButtonModel> buttonModels = meetingTemplateModel.getButtons();
            if (buttonModels != null && buttonModels.size() > 0) {

                if (buttonModels.size() >= 2) {
                    button1.setText(buttonModels.get(0).getTitle());
                    button2.setText(buttonModels.get(1).getTitle());

                    button1.setVisibility(VISIBLE);
                    button2.setVisibility(VISIBLE);
                }else if(meetingTemplateModel.getButtons().size() ==1){
                    button1.setVisibility(VISIBLE);
                    button2.setVisibility(GONE);
                    button1.setText(buttonModels.get(0).getTitle());
                }
            }else{
                button1.setVisibility(GONE);
                button2.setVisibility(GONE);
            }
            meetingLayout.setAlpha(isEnabled ? 1.0f : 0.5f);
            button1.setTextColor(getResources().getColor(isEnabled ? R.color.splash_color : R.color.color_a7b0be));
            button2.setTextColor(getResources().getColor(isEnabled ? R.color.splash_color : R.color.color_a7b0be));
            ((GradientDrawable) button1.getBackground()).setStroke(2, isEnabled ? getResources().getColor(R.color.splash_color) : getResources().getColor(R.color.color_a7b0be));
            ((GradientDrawable) button2.getBackground()).setStroke(2, isEnabled ? getResources().getColor(R.color.splash_color) : getResources().getColor(R.color.color_a7b0be));

            button1.setOnClickListener(isEnabled ? new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(composeFooterInterface != null)
                        composeFooterInterface.onSendClick(buttonModels.get(0).getPayload(),false);
                }
            } : null);
            button2.setOnClickListener(isEnabled ? new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(composeFooterInterface != null)
                        composeFooterInterface.onSendClick(buttonModels.get(1).getPayload(),false);}
            } : null);
        } else {
            autoExpandListView.setAdapter(null);
            meetingLayout.setVisibility(GONE);
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
