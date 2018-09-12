package kore.botssdk.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.MessageFormat;
import java.util.ArrayList;

import kore.botssdk.R;
import kore.botssdk.models.MeetingSlotModel;
import kore.botssdk.utils.KaFontUtils;

import static kore.botssdk.utils.DateUtils.getSlotsDate;
import static kore.botssdk.utils.DateUtils.getTimeInAmPm;


/**
 * Created by Shiva Krishna on 6/15/2018.
 */

public class AttendeeSlotsAdapter extends BaseAdapter {
    public ArrayList<MeetingSlotModel.Slot> getNormalSlots() {
        return normalSlots;
    }

    public void setNormalSlots(ArrayList<MeetingSlotModel.Slot> normalSlots) {
        this.normalSlots = normalSlots;
    }

    public ArrayList<MeetingSlotModel.Slot> getPopularSlots() {
        return popularSlots;
    }

    public void setPopularSlots(ArrayList<MeetingSlotModel.Slot> popularSlots) {
        this.popularSlots = popularSlots;
    }

    private ArrayList<MeetingSlotModel.Slot> normalSlots = new ArrayList<>();
    private ArrayList<MeetingSlotModel.Slot> popularSlots = new ArrayList<>();

    private Context mContext;
    private Drawable selectedCheck, unSelectedCheck;
    private int selectedColor, unSelectedColor;
    private LayoutInflater layoutInflater;
    private ArrayList<MeetingSlotModel.Slot> selectedSlots = new ArrayList<>();
    public void addOrRemoveSelectedSlot(MeetingSlotModel.Slot slot){
        if(selectedSlots.contains(slot)){
            selectedSlots.remove(slot);
        }else{
            selectedSlots.add(slot);
        }
    }
    public void addSelectedSlots(ArrayList<MeetingSlotModel.Slot> slots){
        selectedSlots.addAll(slots);
    }



    public AttendeeSlotsAdapter(Context mContext) {
        this.mContext = mContext;
        layoutInflater = LayoutInflater.from(mContext);
        selectedCheck = mContext.getResources().getDrawable(R.mipmap.checkbox_on);
        unSelectedCheck = mContext.getResources().getDrawable(R.mipmap.checkbox_off);
        selectedColor = mContext.getResources().getColor(R.color.color_dfdfeb);
        unSelectedColor = mContext.getResources().getColor(R.color.color_efeffc);

    }

    @Override
    public int getCount() {
        if (normalSlots.size() > 0 || popularSlots.size() > 0)
            return normalSlots.size() + popularSlots.size() + 2;
        else
            return 0;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0 || (position ==  popularSlots.size()+1)) {
            return 0;
        } else {
            return 1;
        }
    }

    @Override
    public MeetingSlotModel.Slot getItem(int position) {
        if (position == 0 || (position ==  popularSlots.size()+1)) {
            return null;
        } else if (position <= popularSlots.size()) {
            return popularSlots.get(position - 1);
        } else if(position > popularSlots.size()){
            return normalSlots.get(position - 2 - popularSlots.size());
        }else{
            return null;
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        int itemViewType = getItemViewType(position);
        if (itemViewType == 0) {
            // Inflation for Right side
            if (convertView == null || convertView.getId() != R.id.separator) {
                convertView = layoutInflater.inflate(R.layout.separator_text_view, null);
            }
        } else {
            // Inflation for Left side
            if (convertView == null || convertView.getId() == R.id.separator) {
                convertView = layoutInflater.inflate(R.layout.attendee_slot_item, null);
            }
            KaFontUtils.applyCustomFont(mContext, convertView);
        }

        if (convertView.getTag() == null) {
            initializeViewHolder(convertView, itemViewType);
        }


        return populateData(convertView, position, itemViewType);
    }

    private View populateData(View convertView, int position, int type) {
        ViewHolder viewHolder = (ViewHolder) convertView.getTag();
        if (type == 1) {
            MeetingSlotModel.Slot slot = getItem(position);
            String startTime = getTimeInAmPm(slot.getStart()).toLowerCase();
            String endTime = getTimeInAmPm(slot.getEnd()).toLowerCase();
            final String day = getSlotsDate(slot.getStart());
            boolean isSelected = isSlotSelected(slot);
            viewHolder.slotCheck.setImageDrawable(isSelected ? selectedCheck : unSelectedCheck);
            ((GradientDrawable) viewHolder.rootView.getBackground()).setColor(isSelected ? selectedColor : unSelectedColor);
            viewHolder.timeLabel.setText(MessageFormat.format("{0}, {1} to {2}", day, startTime, endTime));
        } else {
            viewHolder.slotsSeparator.setText(position == 0 ? "Most Popular Slot" : "Other Mutually Available Slots");
        }
        return convertView;
    }

    private boolean isSlotSelected(MeetingSlotModel.Slot slot) {

        return selectedSlots != null && selectedSlots.contains(slot);
    }

    public void initializeViewHolder(View convertView, int type) {

        ViewHolder viewHolder = new ViewHolder();
        if (type == 1) {
            viewHolder.timeLabel = (TextView) convertView.findViewById(R.id.slot_time);
            viewHolder.slotCheck = (ImageView) convertView.findViewById(R.id.slot_check);
            viewHolder.rootView = convertView.findViewById(R.id.root_layout);
        } else {
            viewHolder.slotsSeparator = (TextView) convertView.findViewById(R.id.separator);
            KaFontUtils.setCustomTypeface(viewHolder.slotsSeparator,KaFontUtils.ROBOTO_REGULAR, mContext);
        }
        convertView.setTag(viewHolder);
    }


    public ArrayList<MeetingSlotModel.Slot> getSelectedSlots() {
        return selectedSlots;
    }

    public void setSelectedSlots(ArrayList<MeetingSlotModel.Slot> selectedSlots) {
        this.selectedSlots = selectedSlots;
    }

    private class ViewHolder {
        TextView timeLabel;
        ImageView slotCheck;
        TextView slotsSeparator;
        View rootView;
    }


}
