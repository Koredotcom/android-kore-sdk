package kore.botssdk.adapter;

import static kore.botssdk.utils.DateUtils.getSlotsDate;
import static kore.botssdk.utils.DateUtils.getTimeInAmPm;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.text.MessageFormat;
import java.util.ArrayList;

import kore.botssdk.R;
import kore.botssdk.databinding.AttendeeSlotItemBinding;
import kore.botssdk.models.MeetingSlotModel;
import kore.botssdk.utils.SelectionUtils;


public class AttendeeSlotsAdapter extends RecyclerView.Adapter<AttendeeSlotsAdapter.ViewHolder> {

    public void setNormalSlots(@NonNull ArrayList<MeetingSlotModel.Slot> normalSlots) {
        this.normalSlots = normalSlots;
    }

    public void setPopularSlots(@NonNull ArrayList<MeetingSlotModel.Slot> popularSlots) {
        this.popularSlots = popularSlots;
    }

    private ArrayList<MeetingSlotModel.Slot> normalSlots = new ArrayList<>();
    private ArrayList<MeetingSlotModel.Slot> popularSlots = new ArrayList<>();

    private final Drawable selectedCheck;
    private final Drawable unSelectedCheck;
    private final int selectedColor;
    private final int unSelectedColor;
    private final LayoutInflater layoutInflater;
    ArrayList<MeetingSlotModel.Slot> selectedSlots = new ArrayList<>();
    final SlotSelectionListener slotSelectionListener;
    boolean isEnabled = false;
    void addOrRemoveSelectedSlot(MeetingSlotModel.Slot slot){
        if(selectedSlots.contains(slot)){
            selectedSlots.remove(slot);
        }else{
            selectedSlots.add(slot);
        }
    }
    public void addSelectedSlots(@NonNull ArrayList<MeetingSlotModel.Slot> slots){
        selectedSlots.addAll(slots);
    }



    public AttendeeSlotsAdapter(@NonNull Context mContext, @NonNull SlotSelectionListener slotSelectionListener) {
        layoutInflater = LayoutInflater.from(mContext);
        selectedCheck = ResourcesCompat.getDrawable(mContext.getResources() , R.mipmap.checkbox_on, mContext.getTheme());
        unSelectedCheck = ResourcesCompat.getDrawable(mContext.getResources() , R.mipmap.checkbox_off, mContext.getTheme());
        selectedColor = ResourcesCompat.getColor(mContext.getResources(), R.color.color_dfdfeb, mContext.getTheme());
        unSelectedColor = ResourcesCompat.getColor(mContext.getResources(), R.color.white, mContext.getTheme());
        this.slotSelectionListener = slotSelectionListener;
    }

    @Override
    public int getItemCount() {
      if(popularSlots.size() > 0 && normalSlots.size() > 0){
          return popularSlots.size()+ normalSlots.size()+2;
      }else if(popularSlots.size() > 0){
          return popularSlots.size()+1;
      }else if(normalSlots.size() > 0){
          return normalSlots.size() +1;
      }else{
          return 0;
      }
    }

    @Override
    public int getItemViewType(int position) {
        if(popularSlots.size() > 0 && position == 0){
            return 0;
        }else if(normalSlots.size() > 0 && position == 0){
            return 1;
        }else if(popularSlots.size() > 0 && normalSlots.size() > 0 && position == popularSlots.size()+1){
            return 1;
        }else{
            return 2;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        if(holder.attendeeSlotItemBinding != null){
            final MeetingSlotModel.Slot slot = getItem(position);
            String startTime = getTimeInAmPm(slot.getStart()).toLowerCase();
            String endTime = getTimeInAmPm(slot.getEnd()).toLowerCase();
            final String day = getSlotsDate(slot.getStart());
            boolean isSelected = isSlotSelected(slot);
            holder.attendeeSlotItemBinding.slotCheck.setImageDrawable(isSelected ? selectedCheck : unSelectedCheck);
            holder.attendeeSlotItemBinding.slotCheck.setAlpha(isEnabled ? 1.0f : 0.5f);
            holder.attendeeSlotItemBinding.getRoot().setBackgroundColor(isSelected ? selectedColor : unSelectedColor);
            holder.attendeeSlotItemBinding.slotTime.setText(MessageFormat.format("{0}, {1} to {2}", day, startTime, endTime));
            holder.attendeeSlotItemBinding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(isEnabled) {
                        addOrRemoveSelectedSlot(slot);
                        SelectionUtils.setSelectedSlots(selectedSlots);
                        slotSelectionListener.onSlotSelectionChanged();
                        notifyItemChanged(holder.getBindingAdapterPosition());
                    }
                }
            });
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType){
            case 0:
                return new ViewHolder(layoutInflater.inflate(R.layout.most_poular_header, parent, false));
            case 1:
                return new ViewHolder(layoutInflater.inflate(R.layout.separator_text_view, parent, false));
            case 2:
                default:
                return new ViewHolder(DataBindingUtil.inflate(layoutInflater, R.layout.attendee_slot_item, parent, false));

        }
    }

    @SuppressLint("UnknownNullness")
    public MeetingSlotModel.Slot getItem(int position) {
        if(popularSlots.size() > 0 && position == 0){
            return null;
        }else if(normalSlots.size() > 0 && position == 0){
            return null;
        }else if(popularSlots.size() > 0 && normalSlots.size() > 0 && position == popularSlots.size()+1){
            return null;
        }else if(popularSlots.size() > 0 && position <= popularSlots.size()){
            return popularSlots.get(position-1);
        }else if(normalSlots.size() > 0 && position > popularSlots.size()){
            return normalSlots.get(position-popularSlots.size()-(popularSlots.size() == 0 ? 1 : 2));
        }else{
            return null;
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private boolean isSlotSelected(MeetingSlotModel.Slot slot) {

        return selectedSlots != null && selectedSlots.contains(slot);
    }


    @NonNull
    public ArrayList<MeetingSlotModel.Slot> getSelectedSlots() {
        return selectedSlots;
    }

    public void setSelectedSlots(@NonNull ArrayList<MeetingSlotModel.Slot> selectedSlots) {
        this.selectedSlots = selectedSlots;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        AttendeeSlotItemBinding attendeeSlotItemBinding;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ((TextView)itemView).setLetterSpacing(0.06f);
        }
        public ViewHolder(@NonNull AttendeeSlotItemBinding slotItemBinding) {
            super(slotItemBinding.getRoot());
            this.attendeeSlotItemBinding = slotItemBinding;
        }
    }

    public interface SlotSelectionListener {
        void onSlotSelectionChanged();
    }
}
