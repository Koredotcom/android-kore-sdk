package kore.botssdk.adapter;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.google.gson.Gson;

import kore.botssdk.R;
import kore.botssdk.dialogs.WidgetDialogActivity;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import kore.botssdk.event.KoreEventCenter;
import kore.botssdk.events.CancelEvent;
import kore.botssdk.listener.VerticalListViewActionHelper;
import kore.botssdk.models.BotResponse;
import kore.botssdk.models.CalEventsTemplateModel;
import kore.botssdk.models.WCalEventsTemplateModel;

import static kore.botssdk.utils.DateUtils.getDateinDayFormat;
import static kore.botssdk.utils.DateUtils.getTimeInAmPm;

public class WidgetCancelActionsAdapter extends RecyclerView.Adapter<WidgetCancelActionsAdapter.WidgetCancelViewHolder> {

    WidgetDialogActivity widgetDialogActivity;
    List<WCalEventsTemplateModel.Action> actionList;
    WCalEventsTemplateModel model;
    Activity mainContext;
    boolean isFromFullView;
    VerticalListViewActionHelper verticalListViewActionHelper;

    public WidgetCancelActionsAdapter(Activity mainContext, WidgetDialogActivity widgetDialogActivity,
                                      WCalEventsTemplateModel model, boolean isFromFullView, VerticalListViewActionHelper verticalListViewActionHelper
    ) {
        this.widgetDialogActivity = widgetDialogActivity;
        this.model = model;
        this.mainContext = mainContext;

        this.isFromFullView = isFromFullView;
        //   this.actionList = model.getActions();
        this.verticalListViewActionHelper = verticalListViewActionHelper;
        notifyDataSetChanged();
    }


    public void setActionItems(List<WCalEventsTemplateModel.Action> actionList) {
        this.actionList = actionList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public WidgetCancelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(widgetDialogActivity.getContext()).inflate(R.layout.widget_select_laout, parent, false);
        return new WidgetCancelViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull WidgetCancelViewHolder holder, int position) {

        holder.tv_actions.setText(actionList.get(position).getTitle());

        holder.tv_actions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String type = actionList.get(position).getType();
                if (actionList.get(position).getType().equalsIgnoreCase("view_details")) {
                    //view meeting
                    verticalListViewActionHelper.calendarItemClicked(BotResponse.TEMPLATE_TYPE_CAL_EVENTS_WIDGET, model);
                    (widgetDialogActivity).dismiss();
                } else if (type.equalsIgnoreCase("url") && actionList.get(position).getCustom_type().equalsIgnoreCase("url")) {
                    //join meeting
                    verticalListViewActionHelper.navigationToDialAndJoin("url", actionList.get(position).getUrl());
                    (widgetDialogActivity).dismiss();

                } else if (type.equalsIgnoreCase("dial") && actionList.get(position).getCustom_type().equalsIgnoreCase("dial")) {
                    verticalListViewActionHelper.navigationToDialAndJoin("dial", actionList.get(position).getDial());
                    (widgetDialogActivity).dismiss();
                }else if(type.equalsIgnoreCase("url") && actionList.get(position).getCustom_type().equalsIgnoreCase("meetingUrl"))
                {
                    verticalListViewActionHelper.navigationToDialAndJoin("meetingUrl", actionList.get(position).getUrl());
                    (widgetDialogActivity).dismiss();

                }
                else if(type.equalsIgnoreCase(BotResponse.TAKE_NOTES))
                {
                    verticalListViewActionHelper.takeNotesNavigation(model);
                    (widgetDialogActivity).dismiss();

                }

                    else {

                    HashMap<String, ArrayList<String>> hashMap = new HashMap<>();
                    ArrayList<String> list = new ArrayList<>(1);
                    list.add(model.getData().getEventId());
                    hashMap.put("ids", list);
                    KoreEventCenter.post(new CancelEvent(actionList.get(position).getUtterance(), new Gson().toJson(hashMap), 0));
                    (widgetDialogActivity).dismiss();
                    if (mainContext != null && mainContext instanceof Activity && isFromFullView) {
                        mainContext.finish();
                    }

                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return actionList != null ? actionList.size() : 0;
    }

    class WidgetCancelViewHolder extends RecyclerView.ViewHolder {
        TextView tv_actions;

        public WidgetCancelViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_actions = itemView.findViewById(R.id.tv_actions);
        }
    }
}
