package com.kore.ai.widgetsdk.adapters;

import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.kora.ai.widgetsdk.R;
import com.kore.ai.widgetsdk.events.KoreEventCenter;
import com.kore.ai.widgetsdk.events.ShootUtteranceEvent;
import com.kore.ai.widgetsdk.listeners.ChildToActivityActions;
import com.kore.ai.widgetsdk.models.TrendingHashTagModel;
import com.kore.ai.widgetsdk.utils.Constants;
import com.kore.ai.widgetsdk.utils.DialogCaller;
import com.kore.ai.widgetsdk.utils.Utility;
import com.kore.ai.widgetsdk.utils.WidgetViewMoreEnum;

import java.util.ArrayList;

public class HashTagWidgetAdapter extends RecyclerView.Adapter {

    FragmentActivity activity;

    public ArrayList<TrendingHashTagModel> getHashTagList() {
        return hashList;
    }

    public void setHashTagList(ArrayList<TrendingHashTagModel> hashList) {
        this.hashList = hashList;
    }

    private ArrayList<TrendingHashTagModel> hashList;
    int DATA = 1;
    int NO_DATA = 0;
    private static final int MESSAGE = 2;
    boolean isFullView;
    String msg;
    Drawable errorIcon;

    public HashTagWidgetAdapter(FragmentActivity activity, ArrayList<TrendingHashTagModel> hashList) {
        this.activity = activity;
        this.hashList = hashList;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == DATA) {
            View view = LayoutInflater.from(activity).inflate(R.layout.hashtag_adapter, parent, false);
            return new HashViewHolder(view);
        } else {
            View view = LayoutInflater.from(activity).inflate(R.layout.card_empty_widget_layout, parent, false);
            return new EmptyHashViewHolder(view);

        }
    }

    private void postAction(int position, HashViewHolder holder, boolean append_uttrance) {
        if (isFullView) {
            ShootUtteranceEvent event = new ShootUtteranceEvent();
            event.setMessage((append_uttrance ? Constants.SKILL_UTTERANCE : "") + hashList.get(position).getDefaultAction().getUtterance());
            event.setPayLoad(hashList.get(position).getName());
            KoreEventCenter.post(event);
            KoreEventCenter.post(new DissMissBaseSheet());
            activity.finish();


        } else {
            ((ChildToActivityActions) activity).shootUtterance((append_uttrance ? Constants.SKILL_UTTERANCE : "") + hashList.get(position).getDefaultAction().getUtterance(), null, null, false);
            KoreEventCenter.post(new DissMissBaseSheet());
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof HashViewHolder) {

            ((HashViewHolder) holder).tv_hashtag.setText(hashList.get(position).getName());
            int count = hashList.get(position).getCount();
            ((HashViewHolder) holder).tv_numberofviews.setText(hashList.get(position).getTitle_right());
            ((HashViewHolder) holder).rootView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (Utility.checkIsSkillKora()) {
                        postAction(position, ((HashViewHolder) holder), false);
                    } else {
                        DialogCaller.showDialog(activity, null,new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                postAction(position, ((HashViewHolder) holder), true);
                                dialog.dismiss();
                            }
                        });

                    }


                }
            });
        } else if (holder instanceof EmptyHashViewHolder) {
            ((EmptyHashViewHolder) holder).tv_message.setText(holder.getItemViewType() == NO_DATA ? "No Hashtags" : msg);
            ((EmptyHashViewHolder) holder).img_icon.setImageDrawable(holder.getItemViewType() == NO_DATA ? ContextCompat.getDrawable(activity, R.drawable.no_meeting) : errorIcon);

        }

    }

    @Override
    public int getItemViewType(int position) {

        if (hashList != null && hashList.size() > 0) {
            return DATA;
        }
        if (msg != null && !msg.equalsIgnoreCase("")) {
            return MESSAGE;
        }
        return NO_DATA;
    }


    WidgetViewMoreEnum widgetViewMoreEnum;
    public void setViewMoreEnum(WidgetViewMoreEnum widgetViewMoreEnum) {
        this.widgetViewMoreEnum=widgetViewMoreEnum;
    }
    @Override
    public int getItemCount() {
        if(widgetViewMoreEnum!=null&&widgetViewMoreEnum==WidgetViewMoreEnum.EXPAND_VIEW)
        {
            return hashList != null && hashList.size() > 0 ? hashList.size() : 1;
        }
        return hashList != null && hashList.size() > 0 ? (!isFullView && hashList.size() > 3 ? 3 : hashList.size()) : 1;


    }

    public void setFullView(boolean isFullView) {

        this.isFullView = isFullView;
    }

    public void setMessage(String msg, Drawable errorIcon) {
        this.msg = msg;
        this.errorIcon = errorIcon;
    }

    class HashViewHolder extends RecyclerView.ViewHolder {
        TextView tv_hashtag;
        TextView tv_numberofviews;
        View rootView;

        public HashViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_hashtag = itemView.findViewById(R.id.tv_hashtag);
            tv_numberofviews = itemView.findViewById(R.id.tv_numberofviews);
            rootView = itemView.findViewById(R.id.rootView);
        }
    }

    class EmptyHashViewHolder extends RecyclerView.ViewHolder {
        TextView tv_message;
        ImageView img_icon;

        public EmptyHashViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_message = itemView.findViewById(R.id.tv_message);
            img_icon = itemView.findViewById(R.id.img_icon);
        }
    }


}
