package kore.botssdk.adapter;

import android.app.Activity;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import kore.botssdk.R;
import kore.botssdk.dialogs.WidgetActionSheetFragment;
import kore.botssdk.event.KoreEventCenter;
import kore.botssdk.events.CancelEvent;
import kore.botssdk.events.EntityEditEvent;
import kore.botssdk.listener.VerticalListViewActionHelper;
import kore.botssdk.models.BotResponse;
import kore.botssdk.models.CalEventsTemplateModel;
import kore.botssdk.models.WCalEventsTemplateModel;
import kore.botssdk.models.WTaskTemplateModel;
import kore.botssdk.models.Widget.Action;
import kore.botssdk.models.Widget.Element;
import kore.botssdk.utils.Constants;
import kore.botssdk.utils.DialogCaller;
import kore.botssdk.utils.Utility;

public class WidgetSelectActionsAdapter extends RecyclerView.Adapter<WidgetSelectActionsAdapter.WidgetCancelViewHolder> {

    WidgetActionSheetFragment widgetDialogActivity;
    Object actionList;
    Object model;
    Activity mainContext;
    boolean isFromFullView;
    VerticalListViewActionHelper verticalListViewActionHelper;

    public WidgetSelectActionsAdapter(Activity mainContext, WidgetActionSheetFragment widgetDialogActivity, Object model, boolean isFromFullView, VerticalListViewActionHelper verticalListViewActionHelper) {
        this.widgetDialogActivity = widgetDialogActivity;
        this.model = model;
        if (model instanceof WTaskTemplateModel) {
            this.actionList = ((WTaskTemplateModel) model).getActions();
        } else if (model instanceof WCalEventsTemplateModel) {
            this.actionList = ((WCalEventsTemplateModel) model).getActions();
        }else if(model instanceof Element){
            this.actionList = ((Element) model).getActions();
        }
        this.verticalListViewActionHelper = verticalListViewActionHelper;
        this.mainContext = mainContext;
        this.isFromFullView = isFromFullView;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public WidgetCancelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(widgetDialogActivity.getContext()).inflate(R.layout.widget_cancel_laout, parent, false);
        return new WidgetCancelViewHolder(v);
    }


    public void startActions(int position, boolean append_uttrance) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("action", ((List<CalEventsTemplateModel.Action>) actionList).get(position).getTitle());
        hashMap.put("tIds", ((WTaskTemplateModel) model).getId());
        KoreEventCenter.post(new CancelEvent((append_uttrance ? Constants.SKILL_UTTERANCE : "") + ((List<CalEventsTemplateModel.Action>) actionList).get(position).getUtterance(), new Gson().toJson(hashMap), 0, true));
        (widgetDialogActivity).dismiss();
        if (mainContext != null && mainContext instanceof Activity && isFromFullView) {
            mainContext.finish();
        }
        dissmissbaseSheet();
    }


    private void dissmissbaseSheet() {
        KoreEventCenter.post(new DissMissBaseSheet());
    }

    @Override
    public void onBindViewHolder(@NonNull WidgetCancelViewHolder holder, int position) {

        if (model instanceof WTaskTemplateModel) {

            CalEventsTemplateModel.Action action = ((List<CalEventsTemplateModel.Action>) actionList).get(position);
            String text;
            if(action.getType() != null && action.getType().equals("postback"))
                text = "\""+action.getTitle()+"\"";
            else
                text = action.getTitle();
            //Widget Task
            holder.tv_actions.setText(text);

            holder.tv_actions.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (Utility.checkIsSkillKora()) {
                        startActions(position, false);

                    } else {
                        DialogCaller.showDialog(mainContext,null, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                startActions(position, true);
                                dialog.dismiss();
                            }
                        });
                    }


                }

            });

        } else if (model instanceof WCalEventsTemplateModel) {

            holder.tv_actions.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    WCalEventsTemplateModel.Action action = ((WCalEventsTemplateModel) model).getActions().get(position);
                    String type = ((WCalEventsTemplateModel) model).getActions().get(position).getType();
                    if (((WCalEventsTemplateModel) model).getActions().get(position).getType().equalsIgnoreCase("view_details")) {
                        //view meeting
                        verticalListViewActionHelper.calendarItemClicked(BotResponse.TEMPLATE_TYPE_CAL_EVENTS_WIDGET, (WCalEventsTemplateModel) model);
                        (widgetDialogActivity).dismiss();
                    } else if (type.equalsIgnoreCase("url") && ((WCalEventsTemplateModel) model).getActions().get(position).getCustom_type().equalsIgnoreCase("url")) {
                        //join meeting
                        verticalListViewActionHelper.navigationToDialAndJoin("url", action.getUrl());
                        (widgetDialogActivity).dismiss();

                    } else if (type.equalsIgnoreCase("dial") && action.getCustom_type().equalsIgnoreCase("dial")) {
                        verticalListViewActionHelper.navigationToDialAndJoin("dial", action.getDial());
                        (widgetDialogActivity).dismiss();
                    } else if (type.equalsIgnoreCase("url") && action.getCustom_type().equalsIgnoreCase("meetingUrl")) {
                        verticalListViewActionHelper.navigationToDialAndJoin("meetingUrl", action.getUrl());
                        (widgetDialogActivity).dismiss();

                    } else if (type.equalsIgnoreCase(BotResponse.TAKE_NOTES)) {
                        verticalListViewActionHelper.takeNotesNavigation((WCalEventsTemplateModel) model);
                        (widgetDialogActivity).dismiss();

                    } else {
                        if (Utility.checkIsSkillKora()) {
                            postAction(position, false);
                        } else {

                            DialogCaller.showDialog(mainContext,null, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    postAction(position, true);
                                    dialog.dismiss();
                                }
                            });
                        }

                    }
                }
            });
            //Widget Meeting
            WCalEventsTemplateModel.Action action = ((WCalEventsTemplateModel) model).getActions().get(position);
            String text;
            if(action.getType() != null && action.getType().equals("postback"))
                text = "\""+action.getTitle()+"\"";
            else
                text = action.getTitle();
            holder.tv_actions.setText(text);


        }else if (model instanceof Element) {
            Action act = ((Element) model).getActions().get(position);
            String text;

            if (act.getType().equalsIgnoreCase("url")) {
                holder.tv_actions.setText(act.getTitle());
                holder.tv_actions.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        verticalListViewActionHelper.navigationToDialAndJoin("url", act.getUrl());
                        (widgetDialogActivity).dismiss();
                    }
                });

            } else {

                if (act.getType() != null && act.getType().equals("postback")) {
                    text = "\"" + act.getTitle() + "\"";
                }
                else {
                    text = act.getTitle();
                }

                holder.tv_actions.setText(text);

                holder.tv_actions.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        (widgetDialogActivity).dismiss();

                        if (Utility.checkIsSkillKora()) {
                            EntityEditEvent event = new EntityEditEvent();
                            event.setMessage("" + act.getUtterance());
                            event.setPayLoad(null);
                            KoreEventCenter.post(event);

                        } else {
                            DialogCaller.showDialog(mainContext, null, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    EntityEditEvent event = new EntityEditEvent();
                                    event.setMessage("" + act.getUtterance());
                                    event.setPayLoad(null);
                                    KoreEventCenter.post(event);
                                    dialog.dismiss();
                                }
                            });
                        }
                    }
                });
            }
        }
    }

    private void postAction(int position, boolean append_uttrance) {
        HashMap<String, ArrayList<String>> hashMap = new HashMap<>();
        ArrayList<String> list = new ArrayList<>(1);
        list.add(((WCalEventsTemplateModel) model).getData().getEventId());
        hashMap.put("ids", list);
        KoreEventCenter.post(new DissMissBaseSheet());
        KoreEventCenter.post(new CancelEvent((append_uttrance ? Constants.SKILL_UTTERANCE : "") + ((WCalEventsTemplateModel) model).getActions().get(position).getUtterance(), new Gson().toJson(hashMap), 0, true));
        (widgetDialogActivity).dismiss();
        if (mainContext != null && mainContext instanceof Activity && isFromFullView) {
            mainContext.finish();
        }
    }

    @Override
    public int getItemCount() {
        if (model instanceof WTaskTemplateModel) {
            return actionList != null ? ((List<CalEventsTemplateModel.Action>) actionList).size() : 0;
        } else if (model instanceof WCalEventsTemplateModel) {
            return model != null && actionList != null ? ((WCalEventsTemplateModel) model).getActions().size() : 0;
        }else if(model instanceof Element){
            return model != null && actionList != null ? ((Element)model).getActions().size() : 0;
        }
        return 0;
    }

    class WidgetCancelViewHolder extends RecyclerView.ViewHolder {
        TextView tv_actions;

        public WidgetCancelViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_actions = itemView.findViewById(R.id.tv_actions);
        }
    }
}
