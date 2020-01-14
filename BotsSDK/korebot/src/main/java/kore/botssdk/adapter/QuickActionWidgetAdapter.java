package kore.botssdk.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import kore.botssdk.R;
import kore.botssdk.event.KoreEventCenter;
import kore.botssdk.events.EntityEditEvent;
import kore.botssdk.models.Widget.Action;
import kore.botssdk.utils.Constants;
import kore.botssdk.utils.DialogCaller;
import kore.botssdk.utils.StringUtils;
import kore.botssdk.utils.Utility;

public class QuickActionWidgetAdapter extends RecyclerView.Adapter<QuickActionWidgetAdapter.QuickActionViewHolder>  {

    private Context context;
    private List<Action> quickReplyTemplateList;
    private String skillName;

//    private VerticalListViewActionHelper verticalListViewActionHelper;

    public QuickActionWidgetAdapter(Context context, String skillName) {
        this.context = context;
        this.skillName = skillName;
    }

    @NonNull
    @Override
    public QuickActionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(context).inflate(R.layout.action_action_adapter,parent,false);
        return new QuickActionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuickActionViewHolder holder, int position) {

        holder.tv_info.setTextColor(Color.parseColor(quickReplyTemplateList.get(position).getTheme()));
        holder.tv_info.setText(quickReplyTemplateList.get(position).getTitle());

        holder.tv_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!StringUtils.isNullOrEmpty(quickReplyTemplateList.get(position).getType()) && quickReplyTemplateList.get(position).getType().equals("postback") && !StringUtils.isNullOrEmpty(quickReplyTemplateList.get(position).getPayload())){
                if (Utility.checkIsSkillKora()) {
                    postAction(position,false);
                } else {

                        DialogCaller.showDialog(context, null, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                postAction(position,true);
                                dialog.dismiss();
                            }
                        });


                }


                }
            }
        });
    }

    private void postAction(int position, boolean append){
        EntityEditEvent event = new EntityEditEvent();
        event.setMessage((append?Constants.SKILL_UTTERANCE:"")+quickReplyTemplateList.get(position).getPayload());
        event.setScrollUpNeeded(true);
        KoreEventCenter.post(event);
    }
    @Override
    public int getItemCount() {
        return quickReplyTemplateList != null ? quickReplyTemplateList.size() : 0;
    }

    public void setData(List<Action> quickReplyTemplateList) {
        this.quickReplyTemplateList = quickReplyTemplateList;
        notifyDataSetChanged();
    }

    class QuickActionViewHolder extends RecyclerView.ViewHolder {

        TextView tv_info;
        public QuickActionViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_info=itemView.findViewById(R.id.tv_info);
        }
    }
}
