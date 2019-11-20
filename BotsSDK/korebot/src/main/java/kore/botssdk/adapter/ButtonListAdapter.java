package kore.botssdk.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import kore.botssdk.R;
import kore.botssdk.adapter.ButtonListAdapter.ButtonViewHolder;
import kore.botssdk.event.KoreEventCenter;
import kore.botssdk.events.EntityEditEvent;
import kore.botssdk.models.Widget;
import kore.botssdk.models.Widget.Button;
import kore.botssdk.utils.StringUtils;

public class ButtonListAdapter extends RecyclerView.Adapter<ButtonViewHolder> {
    private LayoutInflater inflater;
    private List<Widget.Button> buttons;
    private Context mContext;

    private String skillName;

    public ButtonListAdapter(Context context, List<Button> buttons) {
        this.buttons = buttons;
        this.inflater = LayoutInflater.from(context);
        mContext = context;
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
        holder.tv.setTextColor(Color.parseColor(btn.getTheme()));

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

                buttonAction(utterance);

               /* if (Utility.checkIsSkillKora()) {
                    buttonAction(utterance);
                } else {
                    if(!StringUtils.isNullOrEmpty(skillName) && !skillName.equalsIgnoreCase(Constants.SKILL_SELECTION)) {
                        DialogCaller.showDialog(mContext, skillName, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                buttonAction(utterance);
                                dialog.dismiss();
                            }
                        });
                    }else{
                        buttonAction(utterance);
                    }
                }*/
            }
        });
    }

    @Override
    public int getItemCount() {
        return buttons != null ? buttons.size() : 0;
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


    public void buttonAction(String utterance){
        EntityEditEvent event = new EntityEditEvent();

        event.setMessage("" + utterance);
        event.setPayLoad(null);
        KoreEventCenter.post(event);
    }

    public String getSkillName() {
        return skillName;
    }

    public void setSkillName(String skillName) {
        this.skillName = skillName;
    }


}

