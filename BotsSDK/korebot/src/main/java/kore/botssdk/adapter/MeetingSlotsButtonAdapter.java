package kore.botssdk.adapter;

import static kore.botssdk.utils.DateUtils.getSlotsDate;
import static kore.botssdk.utils.DateUtils.getTimeInAmPm;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.google.gson.Gson;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;

import kore.botssdk.R;
import kore.botssdk.listener.ComposeFooterInterface;
import kore.botssdk.models.MeetingSlotModel;
import kore.botssdk.utils.KaFontUtils;
import kore.botssdk.view.MeetingSlotsView;

public class MeetingSlotsButtonAdapter extends BaseAdapter {
    private ArrayList<MeetingSlotModel.Slot> meetingSlotModels = new ArrayList<>();
    private ComposeFooterInterface composeFooterInterface;
    final Context context;
    final MeetingSlotsView meetSlotView;

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    private boolean isEnabled;
    final Gson gson = new Gson();

    public MeetingSlotsButtonAdapter(Context context, MeetingSlotsView meetingSlotsView) {
        this.context = context;
        this.meetSlotView = meetingSlotsView;
    }

    @Override
    public int getCount() {
        if (meetingSlotModels != null) {
            return meetingSlotModels.size();
        } else {
            return 0;
        }
    }

    @Override
    public MeetingSlotModel.Slot getItem(int position) {
        if (position == AdapterView.INVALID_POSITION) {
            return null;
        } else {
            return meetingSlotModels.get(position);
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = View.inflate(context, R.layout.meeting_slot_button, null);
            KaFontUtils.applyCustomFont(context,convertView);
        }

        if (convertView.getTag() == null) {
            initializeViewHolder(convertView);
        }

        ViewHolder holder = (ViewHolder) convertView.getTag();

        populateVIew(holder, position);

        return convertView;
    }

    private void populateVIew(ViewHolder holder, int position) {
        final MeetingSlotModel.Slot slot = getItem(position);
        holder.textView.setTag(slot);
        String startTime = getTimeInAmPm(slot.getStart()).toLowerCase();
        String endTime = getTimeInAmPm(slot.getEnd()).toLowerCase();
        final String day = getSlotsDate(slot.getStart());
        holder.textView.setText(MessageFormat.format("{0}, {1} to {2}", day, startTime, endTime));
        holder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (composeFooterInterface != null && isEnabled) {
                    setEnabled(false);
                    meetSlotView.setMeetingLayoutAlpha(isEnabled());
                    MeetingSlotModel.Slot meetingSlotModel = (MeetingSlotModel.Slot) v.getTag();
                    if (meetingSlotModel != null) {
                        HashMap<String, ArrayList<MeetingSlotModel.Slot>> selectedSlot = new HashMap<>();
                        ArrayList<MeetingSlotModel.Slot> slotModels = new ArrayList<>();
                        slotModels.add(meetingSlotModel);
                        selectedSlot.put("slots",slotModels);
                        composeFooterInterface.sendWithSomeDelay(((TextView)v).getText().toString(), gson.toJson(selectedSlot),0,false);
                    }
                }
            }
        });

    }


    public void setMeetingsModelArrayList(ArrayList<MeetingSlotModel.Slot> meetingsModelArrayList) {
        this.meetingSlotModels = meetingsModelArrayList;
    }

    public void setComposeFooterInterface(ComposeFooterInterface composeFooterInterface) {
        this.composeFooterInterface = composeFooterInterface;
    }

    private void initializeViewHolder(View view) {
        ViewHolder holder = new ViewHolder();
        holder.textView = view.findViewById(R.id.text_view);
        ((GradientDrawable) holder.textView.getBackground()).setColor(context.getResources().getColor(isEnabled ?R.color.splash_color : R.color.color_a7b0be));
         view.setTag(holder);
    }

    private static class ViewHolder {
        TextView textView;
    }
}
