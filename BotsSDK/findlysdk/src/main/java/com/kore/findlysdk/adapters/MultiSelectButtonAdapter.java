package com.kore.findlysdk.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kore.findlysdk.R;
import com.kore.findlysdk.listners.ComposeFooterInterface;
import com.kore.findlysdk.models.BotButtonModel;
import com.kore.findlysdk.models.BotMultiSelectElementModel;
import com.kore.findlysdk.models.BotResponse;
import com.kore.findlysdk.models.MultiSelectBase;
import com.kore.findlysdk.utils.KaFontUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class MultiSelectButtonAdapter extends BaseAdapter {

    private ArrayList<MultiSelectBase> multiSelectModels = new ArrayList<>();
    private ComposeFooterInterface composeFooterInterface;
    private LayoutInflater ownLayoutInflator;
    private Context context;
    private Set<MultiSelectBase> checkedItems = new HashSet<>();
    private SharedPreferences sharedPreferences;

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    private boolean isEnabled;

    public MultiSelectButtonAdapter(Context context) {
        this.ownLayoutInflator = LayoutInflater.from(context);
        this.context = context;
        this.sharedPreferences = context.getSharedPreferences(BotResponse.THEME_NAME, Context.MODE_PRIVATE);
    }

    @Override
    public int getCount() {
        if (multiSelectModels != null) {
            return multiSelectModels.size();
        } else {
            return 0;
        }
    }

    @Override
    public MultiSelectBase getItem(int position) {
        if (position == AdapterView.INVALID_POSITION) {
            return null;
        } else {
            return multiSelectModels.get(position);
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            if (multiSelectModels.get(position) instanceof BotMultiSelectElementModel) {
                convertView = ownLayoutInflator.inflate(R.layout.multi_select_findly_item, null);
                holder.textView = convertView.findViewById(R.id.text_view);
                holder.checkBox = convertView.findViewById(R.id.check_multi_item);
                holder.root_layout = convertView.findViewById(R.id.root_layout);
            } else {
                convertView = ownLayoutInflator.inflate(R.layout.multiselect_findly_button, null);
                holder.textView = (TextView) convertView.findViewById(R.id.text_view_button);
                holder.root_layout_btn = convertView.findViewById(R.id.root_layout);
            }
            convertView.setTag(holder);
            KaFontUtils.applyCustomFont(context, convertView);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if(holder != null && sharedPreferences != null && holder.root_layout != null)
        {
            holder.root_layout.setBackgroundColor(Color.parseColor(sharedPreferences.getString(BotResponse.BUTTON_ACTIVE_BG_COLOR, "#FFFFFF")));
        }

        if(holder != null && sharedPreferences != null && holder.root_layout_btn != null)
        {
            holder.root_layout_btn.setBackgroundColor(Color.parseColor(sharedPreferences.getString(BotResponse.BUTTON_ACTIVE_BG_COLOR, "#FFFFFF")));
        }

        if(holder != null && sharedPreferences != null &&  holder.textView != null)
        {
            holder.textView.setTextColor(Color.parseColor(sharedPreferences.getString(BotResponse.BUTTON_ACTIVE_TXT_COLOR, "#000000")));
        }

        populateVIew(holder, position,multiSelectModels.get(position) instanceof BotMultiSelectElementModel?0:1);

        return convertView;
    }

    private void populateVIew(ViewHolder holder, int position,int type) {
        final MultiSelectBase item = getItem(position);
        if(type ==0) {
            holder.textView.setTag(item);
            holder.textView.setText(((BotMultiSelectElementModel)item).getTitle());
            holder.checkBox.setChecked(checkedItems.contains(item));
            holder.checkBox.setTag(item);
            holder.checkBox.setOnClickListener(itemSelectionListener);
            /*holder.checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (composeFooterInterface != null && isEnabled) {
                        setEnabled(false);
                        multiSelectView.setMeetingLayoutAlpha(isEnabled());
                   *//* MeetingSlotModel.Slot meetingSlotModel = (MeetingSlotModel.Slot) v.getTag();
                    if (meetingSlotModel != null) {
                        HashMap<String, ArrayList<MeetingSlotModel.Slot>> selectedSlot = new HashMap<>();
                        ArrayList<MeetingSlotModel.Slot> slotModels = new ArrayList<>();
                        slotModels.add(meetingSlotModel);
                        selectedSlot.put("slots",slotModels);
                        composeFooterInterface.sendWithSomeDelay(((TextView)v).getText().toString(), gson.toJson(selectedSlot),0,false);
                    }*//*
                    }
                }
            });*/
        }else{
            holder.textView.setTag(item);
            holder.textView.setText(((BotButtonModel)item).getTitle());
            holder.textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(composeFooterInterface!= null && isEnabled && checkedItems.size()>0){
                        StringBuffer sb = new StringBuffer();
                        StringBuffer sbValue = new StringBuffer();
                        for(MultiSelectBase item:checkedItems){
                            if(!sb.toString().isEmpty())
                                sb.append(",");
                            sb.append(((BotMultiSelectElementModel)item).getTitle());

                            if(!sbValue.toString().isEmpty())
                                sbValue.append(",");
                            sbValue.append(((BotMultiSelectElementModel)item).getValue());
                        }
                        composeFooterInterface.onSendClick(sb.toString(),sbValue.toString(),false);
                    }

                }
            });
        }

    }
    private View.OnClickListener itemSelectionListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(isEnabled) {
                MultiSelectBase item = (MultiSelectBase) v.getTag();
                if (((CompoundButton) v).isChecked()) {
                    checkedItems.add(item);
                } else {
                    checkedItems.remove(item);
                }
            }else{
                ((CompoundButton) v).setChecked(false);
            }
        }
    };

    public void setMultiSelectModels(ArrayList<MultiSelectBase> multiSelectModels) {
        this.multiSelectModels = multiSelectModels;
    }

    public void setComposeFooterInterface(ComposeFooterInterface composeFooterInterface) {
        this.composeFooterInterface = composeFooterInterface;
    }

    private void initializeViewHolder(View view, int type) {
        ViewHolder holder = new ViewHolder();

        if(type == 0){
            holder.textView = view.findViewById(R.id.text_view);
            holder.checkBox = view.findViewById(R.id.check_multi_item);
        }else{
            holder.textView = (TextView) view.findViewById(R.id.text_view_button);
        }
        view.setTag(holder);
    }

    private static class ViewHolder {
        CheckBox checkBox;
        TextView textView;
        RelativeLayout root_layout;
        LinearLayout root_layout_btn;
    }
}