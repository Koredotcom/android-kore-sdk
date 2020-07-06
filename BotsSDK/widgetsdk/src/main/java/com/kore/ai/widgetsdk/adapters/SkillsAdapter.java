package com.kore.ai.widgetsdk.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.kora.ai.widgetsdk.R;
import com.kore.ai.widgetsdk.dialogs.SkillSwitchDialog;
import com.kore.ai.widgetsdk.events.KoreEventCenter;
import com.kore.ai.widgetsdk.events.ShootUtteranceEvent;
import com.kore.ai.widgetsdk.models.PayloadInner;
import com.kore.ai.widgetsdk.net.SDKConfiguration;
import com.kore.ai.widgetsdk.utils.Constants;
import com.kore.ai.widgetsdk.utils.DialogCaller;
import com.kore.ai.widgetsdk.utils.KaUtility;
import com.kore.ai.widgetsdk.utils.StringUtils;
import com.kore.ai.widgetsdk.utils.Utility;
import com.squareup.picasso.Picasso;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;

public class SkillsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int SECTION_VIEW = 0;
    public static final int CONTENT_VIEW = 1;
    WeakReference<Context> mContextWeakReference;
    private SkillSwitchDialog skillSwitchDialog;

    private ArrayList<PayloadInner.Skill> data;
    private Context mContext;

    public SkillsAdapter(ArrayList<PayloadInner.Skill> data, Context context, SkillSwitchDialog skillSwitchDialog) {
        this.data = data;
        this.mContextWeakReference = new WeakReference<Context>(context);
        this.skillSwitchDialog = skillSwitchDialog;
        this.mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = null;
        if (viewType == SECTION_VIEW) {
            listItem = layoutInflater.inflate(R.layout.skill_switch_section_item, parent, false);
            return new SkillSectionHeaderViewHolder(listItem);
        }
        listItem = layoutInflater.inflate(R.layout.skill_switch_list_item, parent, false);
        SkillItemViewHolder viewHolder = new SkillItemViewHolder(listItem);
        return viewHolder;
    }

    private void doSkillItemAction(int position) {
        final PayloadInner.Skill skill = ((PayloadInner.Skill) data.get(position));
        if (skill.isCurrentSkill()) {
            return;
        }
        final String utterance = skill.getTrigger();

        if (Utility.checkIsSkillKora()) {
            postAction(utterance, skill);
        }else{
            if(!StringUtils.isNullOrEmpty(skill.getName()) && !skill.getName().equalsIgnoreCase(Constants.SKILL_SELECTION)) {
                DialogCaller.showDialog(mContext, skill.getName(), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        postAction(utterance, skill);
                        dialog.dismiss();
                    }
                });
            }else{
                postAction(utterance, skill);
            }
        }
        skillSwitchDialog.dismissDialog();
    }

    private void postAction(String utterance, PayloadInner.Skill skill){
        ShootUtteranceEvent event = new ShootUtteranceEvent();
        if (StringUtils.isNullOrEmpty(utterance)) {
            String skName = StringUtils.isNullOrEmpty(skill.getName())?"Kora":skill.getName();
            utterance = "Switch to "+skName;
        }
        event.setMessage(utterance);
        event.setBody(skill.getId());
        event.setScrollUpNeeded(true);
        if(skill.getId() != null){
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("skillId", skill.getId());
            event.setDataMap(hashMap);
        }
        KoreEventCenter.post(event);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        Context context = mContextWeakReference.get();
        if (context == null) {
            return;
        }

        if (SECTION_VIEW == getItemViewType(position)) {
            SkillSectionHeaderViewHolder sectionVH = (SkillSectionHeaderViewHolder) holder;
            sectionVH.headerTitleTextview.setText(data.get(position).getName());
            return;
        }
        SkillItemViewHolder skillItemVH = (SkillItemViewHolder) holder;
        skillItemVH.textView.setText(data.get(position).getName());
        skillItemVH.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doSkillItemAction(position);
            }
        });
        if (data.get(position).isCurrentSkill()) {
            skillItemVH.skillSwitchCancel.setVisibility(View.VISIBLE);
            skillItemVH.skillSwitchCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    skillSwitchDialog.dismissDialog();
                }
            });
        }

        skillItemVH.rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doSkillItemAction(position);
            }
        });


        Picasso.get().load(SDKConfiguration.Server.SERVER_URL + "/" + data.get(position).getIcon())
                //.networkPolicy(NetworkPolicy.OFFLINE)
                .transform(KaUtility.getRoundTransformation())
                .resize(50,50)
                .error(getErrorIcon(data.get(position).getName().toLowerCase(),context))
                .into(skillItemVH.imageView);/*, new com.squareup.picasso.Callback() {
            @Override
            public void onSuccess() {
            }

            @Override
            public void onError() {
                if (data.get(position).getName().toLowerCase().equals("kora")) {
                    skillItemVH.imageView.setImageDrawable(context.getResources().getDrawable(R.mipmap.kora_icon));
                } else {
                    skillItemVH.imageView.setImageDrawable(context.getResources().getDrawable(R.mipmap.temp_skill));
                }
            }
        });*/
    }

    private Drawable getErrorIcon(String name, Context context){
        if(name !=null){
            if(name.equals("kora"))
                return context.getResources().getDrawable(R.mipmap.kora_icon);
            else if(name.equalsIgnoreCase(Constants.SKILL_HOME))
                return context.getResources().getDrawable(R.mipmap.kora_icon);
            else return context.getResources().getDrawable(R.mipmap.temp_skill);
        }
        return context.getResources().getDrawable(R.mipmap.temp_skill);
    }
    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (data.get(position).isSectionItem()) {
            return SECTION_VIEW;
        } else {
            return CONTENT_VIEW;
        }
    }

    public static class SkillItemViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView, skillSwitchCancel;
        TextView textView;
        RelativeLayout rl;


        public SkillItemViewHolder(View itemView) {
            super(itemView);
            this.imageView = (ImageView) itemView.findViewById(R.id.imageView);
            this.textView = (TextView) itemView.findViewById(R.id.textView);
            this.skillSwitchCancel = (ImageView) itemView.findViewById(R.id.skill_switch_cancel_iv);
            this.rl = (RelativeLayout) itemView.findViewById(R.id.recentSkillRL);
        }
    }

    public class SkillSectionHeaderViewHolder extends RecyclerView.ViewHolder {
        TextView headerTitleTextview;

        public SkillSectionHeaderViewHolder(View itemView) {
            super(itemView);
            headerTitleTextview = (TextView) itemView.findViewById(R.id.skill_switch_scetion_tv);
        }
    }


}