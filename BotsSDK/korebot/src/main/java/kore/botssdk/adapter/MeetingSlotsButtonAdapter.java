package kore.botssdk.adapter;

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
import kore.botssdk.fragment.ComposeFooterFragment;
import kore.botssdk.models.MeetingSlotModel;
import kore.botssdk.utils.KaFontUtils;

import static kore.botssdk.utils.DateUtils.getSlotsDate;
import static kore.botssdk.utils.DateUtils.getTimeInAmPm;

public class MeetingSlotsButtonAdapter extends BaseAdapter {

    String LOG_TAG = MeetingSlotsButtonAdapter.class.getSimpleName();

    private ArrayList<MeetingSlotModel.Slot> meetingSlotModels = new ArrayList<>();
    private ComposeFooterFragment.ComposeFooterInterface composeFooterInterface;
    private LayoutInflater ownLayoutInflator;
    Context context;

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    private boolean isEnabled;
    Gson gson = new Gson();

    public MeetingSlotsButtonAdapter(Context context) {
        this.ownLayoutInflator = LayoutInflater.from(context);
        this.context = context;
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
            convertView = ownLayoutInflator.inflate(R.layout.meeting_slot_button, null);
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
        MeetingSlotModel.Slot slot = getItem(position);
        holder.textView.setTag(slot);
        String startTime = getTimeInAmPm(slot.getStart()).toLowerCase();
        String endTime = getTimeInAmPm(slot.getEnd()).toLowerCase();
        final String day = getSlotsDate(slot.getStart());
        holder.textView.setText(MessageFormat.format("{0}, {1} to {2}", day, startTime, endTime));
        holder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (composeFooterInterface != null && isEnabled) {
                    MeetingSlotModel.Slot meetingSlotModel = (MeetingSlotModel.Slot) v.getTag();
                    if (meetingSlotModel != null) {
                        HashMap<String, Long> selectedSlot = new HashMap<>();
                        selectedSlot.put("day", meetingSlotModel.getStart());
                        selectedSlot.put("startTime", meetingSlotModel.getStart());
                        selectedSlot.put("endTime", meetingSlotModel.getEnd());
                        composeFooterInterface.sendWithSomeDelay(((TextView)v).getText().toString(), gson.toJson(selectedSlot),0);
                    }
                }
            }
        });

    }


    public void setMeetingsModelArrayList(ArrayList<MeetingSlotModel.Slot> meetingsModelArrayList) {
        this.meetingSlotModels = meetingsModelArrayList;
    }

    public void setComposeFooterInterface(ComposeFooterFragment.ComposeFooterInterface composeFooterInterface) {
        this.composeFooterInterface = composeFooterInterface;
    }

    private void initializeViewHolder(View view) {
        ViewHolder holder = new ViewHolder();
        holder.textView = (TextView) view.findViewById(R.id.text_view);
        ((GradientDrawable) holder.textView.getBackground()).setColor(context.getResources().getColor(isEnabled ?R.color.splash_color : R.color.color_a7b0be));
         view.setTag(holder);
    }

    private static class ViewHolder {
        TextView textView;
    }
}
