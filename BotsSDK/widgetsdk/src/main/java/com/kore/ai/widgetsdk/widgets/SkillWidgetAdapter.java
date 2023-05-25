package com.kore.ai.widgetsdk.widgets;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kora.ai.widgetsdk.R;
import com.kore.ai.widgetsdk.adapters.DissMissBaseSheet;
import com.kore.ai.widgetsdk.events.KoreEventCenter;
import com.kore.ai.widgetsdk.listeners.ChildToActivityActions;
import com.kore.ai.widgetsdk.models.Widget;
import com.kore.ai.widgetsdk.utils.Constants;
import com.kore.ai.widgetsdk.utils.DialogCaller;
import com.kore.ai.widgetsdk.utils.StringUtils;
import com.kore.ai.widgetsdk.utils.Utility;
import com.kore.ai.widgetsdk.viewholder.SkillWidgetViewHolder;

import java.util.List;

public class SkillWidgetAdapter extends RecyclerView.Adapter<SkillWidgetViewHolder> {

    Context context;
    List<Widget.Action> model;

    public String getSkillName() {
        return skillName;
    }

    public void setSkillName(String skillName) {
        this.skillName = skillName;
    }

    String skillName;

    public SkillWidgetAdapter(Context context, List<Widget.Action> model) {
        this.context = context;
        this.model = model;
    }

    @NonNull
    @Override
    public SkillWidgetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.skill_widget_adapter_layout, parent, false);

        return new SkillWidgetViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SkillWidgetViewHolder holder, final int position) {

        holder.item_text.setText(model.get(position).getTitle());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Utility.checkIsSkillKora()) {
                    postAction(position);
                } else {
                    if(!StringUtils.isNullOrEmpty(skillName) && !skillName.equalsIgnoreCase(Constants.SKILL_SELECTION)) {
                        DialogCaller.showDialog(context, skillName, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                postAction(position);
                                dialog.dismiss();
                            }
                        });
                    }else{
                        postAction(position);
                    }

                }


            }
        });
    }

    private void postAction(int position)
    {
        ((ChildToActivityActions) context).shootUtterance(""+model.get(position).getUtterance(), model.get(position).getPayload(), null,true);
        KoreEventCenter.post(new DissMissBaseSheet());
    }
    @Override
    public int getItemCount() {
        return model.size();
    }
}
