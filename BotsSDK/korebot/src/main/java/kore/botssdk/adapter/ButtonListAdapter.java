package kore.botssdk.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;

import kore.botssdk.R;
import kore.botssdk.adapter.ButtonListAdapter.ButtonViewHolder;
import kore.botssdk.event.KoreEventCenter;
import kore.botssdk.events.EntityEditEvent;
import kore.botssdk.models.Widget;
import kore.botssdk.models.Widget.Button;
import kore.botssdk.utils.Constants;
import kore.botssdk.utils.StringUtils;

public class ButtonListAdapter extends RecyclerView.Adapter<ButtonViewHolder> {
    private LayoutInflater inflater;
    private List<Widget.Button> buttons;
    private Context mContext;

    private String skillName;
    private String trigger;

    public ButtonListAdapter(Context context, List<Button> buttons, String trigger) {
        this.buttons = buttons;
        this.inflater = LayoutInflater.from(context);
        mContext = context;
        this.trigger = trigger;
    }

    @NonNull
    @Override
    public ButtonViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ButtonViewHolder(inflater.inflate(R.layout.button_list_item, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ButtonViewHolder holder, int i) {

//        holder.ll.setVisibility(View.VISIBLE);
        Button btn = buttons.get(i);

        holder.tv.setText(btn.getTitle());
        try {
            holder.tv.setTextColor(Color.parseColor(btn.getTheme()));
        }catch(Exception e){
            holder.tv.setTextColor(Color.parseColor("#2f91e5"));
        }

        String utt = null;
        if(!StringUtils.isNullOrEmpty(btn.getPayload())){
            utt = btn.getPayload();
        }
        if(!StringUtils.isNullOrEmpty(btn.getUtterance()) && utt == null){
            utt = btn.getUtterance();
        }
        final String utterance = utt;

        holder.tv.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

//                buttonAction(utterance);

                if(Constants.SKILL_SELECTION.equalsIgnoreCase(Constants.SKILL_HOME)||TextUtils.isEmpty(Constants.SKILL_SELECTION) ||
                        (!StringUtils.isNullOrEmpty(skillName) && !skillName.equalsIgnoreCase(Constants.SKILL_SELECTION))){
                    buttonAction(utterance,true);
                }else{
                    buttonAction(utterance,false);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return buttons != null ? buttons.size() : 0;
    }

    boolean isFullView;
    public void setIsFromFullView(boolean isFullView) {
        this.isFullView=isFullView;
    }

    public class ButtonViewHolder extends RecyclerView.ViewHolder {
        private TextView tv;
//        private LinearLayout ll;

        public ButtonViewHolder(@NonNull View itemView) {
            super(itemView);
            tv = itemView.findViewById(R.id.buttonTV);
//            ll = itemView.findViewById(R.id.buttonsLayout);

        }
    }


    public void buttonAction(String utterance, boolean appendUtterance){
        EntityEditEvent event = new EntityEditEvent();
        StringBuffer msg = new StringBuffer("");
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("refresh", Boolean.TRUE);
        if(appendUtterance && trigger!= null)
            msg = msg.append(trigger).append(" ");
        msg.append(utterance);
        event.setMessage(msg.toString());
        event.setPayLoad(new Gson().toJson(hashMap));
        event.setScrollUpNeeded(true);
        KoreEventCenter.post(event);

        try {


            if (isFullView) {
                ((Activity) mContext).finish();
            }
        } catch (Exception e) {

        }
    }

    public String getSkillName() {
        return skillName;
    }

    public void setSkillName(String skillName) {
        this.skillName = skillName;
    }


}

