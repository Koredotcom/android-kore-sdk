package kore.botssdk.adapter;

import android.content.Context;

import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;


import java.text.MessageFormat;
import java.util.ArrayList;


import kore.botssdk.R;
import kore.botssdk.listener.ComposeFooterInterface;
import kore.botssdk.models.BotResponse;
import kore.botssdk.models.MeetingFollowUpModel;
import kore.botssdk.models.MeetingSlotModel;
import kore.botssdk.utils.KaFontUtils;
import kore.botssdk.view.MeetingFollowupView;

import static kore.botssdk.utils.DateUtils.getFollowUpSlotsDay;

import static kore.botssdk.utils.DateUtils.getTimeInAmPm;


public class MeetingFollowupAdapter extends RecyclerView.Adapter<MeetingFollowupAdapter.ViewHolder> {

    String LOG_TAG = MeetingFollowupAdapter.class.getSimpleName();

    private ArrayList<MeetingFollowUpModel> meetingSlotModels = new ArrayList<>();
    private ComposeFooterInterface composeFooterInterface;
    private LayoutInflater ownLayoutInflator;
    Context context;
    MeetingFollowupView meetSlotView;

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    private boolean isEnabled;
    Gson gson = new Gson();

    public MeetingFollowupAdapter(Context context, MeetingFollowupView meetingSlotsView) {
        this.ownLayoutInflator = LayoutInflater.from(context);
        this.context = context;
        this.meetSlotView = meetingSlotsView;
    }





    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      View   convertView = LayoutInflater.from(context).inflate(R.layout.meeting_followup_adapterview,parent, false);
        KaFontUtils.applyCustomFont(context,convertView);
        return new ViewHolder(convertView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        populateVIew(holder, position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        if(flag)
        {
            return showCount;
        }
        if (meetingSlotModels != null) {
            return meetingSlotModels.size();
        } else {
            return 0;
        }
    }

    int showCount;
    boolean flag;
    public void setValues(int showCount,boolean flag)
    {
        this.showCount=showCount;
        this.flag=flag;
    }



    private void populateVIew(ViewHolder holder, int position) {
        final MeetingFollowUpModel root = meetingSlotModels.get(position);

        holder.title.setText(root.getTitle()!=null?root.getTitle():"");
        holder.tvpeople.setText(root.getText()!=null?root.getText():"");
        /*try {
            holder.bar_view.setBackgroundColor(Color.parseColor(root.getColor()));
        }catch (Exception e){

        }*/
        holder.slots_view.removeAllViews();
        for(MeetingSlotModel.Slot slot:root.getSlots())
        {
            String startTime = getTimeInAmPm(slot.getStart()).toUpperCase();
            String endTime = getTimeInAmPm(slot.getEnd()).toUpperCase();
            final String day = getFollowUpSlotsDay(slot.getStart());

            TextView textView1 = new TextView(context);
            textView1.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            textView1.setTypeface(ResourcesCompat.getFont(context, R.font.latomedium));
            textView1.setTextColor(Color.parseColor("#161620"));
            textView1.setTextSize(TypedValue.COMPLEX_UNIT_PX,context.getResources().getDimension(R.dimen.dimen_12dp));
            textView1.setText(MessageFormat.format("{0} {1} - {2}", day, startTime, endTime));
            textView1.setPadding(10, 10, 10, 10); // in pixels (left, top, right, bottom)
            holder.slots_view.addView(textView1);
        }

       holder.root_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(composeFooterInterface!=null) {
                    Bundle bundle = new Bundle();
                    bundle.putString("mId", root.getmId());
                    composeFooterInterface.launchActivityWithBundle(BotResponse.FOLLOWUP_MEETTING_PREVIEW, bundle);
                }
               // Toast.makeText(context,"click",Toast.LENGTH_SHORT).show();
  /*     if (composeFooterInterface != null && isEnabled) {
                    setEnabled(false);
                    meetSlotView.setMeetingLayoutAlpha(isEnabled());

                        composeFooterInterface.sendWithSomeDelay(((TextView)v).getText().toString(), "Hello 123",0,false);

                }*/
            }
        });

    }


    public void setMeetingsModelArrayList(ArrayList<MeetingFollowUpModel> meetingsModelArrayList) {
        this.meetingSlotModels = meetingsModelArrayList;
        notifyDataSetChanged();
    }

    public void setComposeFooterInterface(ComposeFooterInterface composeFooterInterface) {
        this.composeFooterInterface = composeFooterInterface;
    }



    public   class ViewHolder extends RecyclerView.ViewHolder {
        View  bar_view,root_layout;
        TextView title,tvpeople;
        LinearLayout slots_view;

        public ViewHolder(@NonNull View view) {
            super(view);
           title = (TextView) view.findViewById(R.id.title);
           root_layout = (View) view.findViewById(R.id.root_layout);
           bar_view = (View) view.findViewById(R.id.bar_view);
           slots_view = (LinearLayout) view.findViewById(R.id.slots_view);
           tvpeople = (TextView) view.findViewById(R.id.tvpeople);
        }
    }
}
