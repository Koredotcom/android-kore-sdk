package com.kore.ai.widgetsdk.adapters;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.kora.ai.widgetsdk.R;
import com.kore.ai.widgetsdk.activities.GenericWebViewActivity;
import com.kore.ai.widgetsdk.events.CancelEvent;
import com.kore.ai.widgetsdk.events.EntityEditEvent;
import com.kore.ai.widgetsdk.events.KoreEventCenter;
import com.kore.ai.widgetsdk.fragments.WidgetActionSheetFragment;
import com.kore.ai.widgetsdk.listeners.VerticalListViewActionHelper;
import com.kore.ai.widgetsdk.models.BaseChartModel;
import com.kore.ai.widgetsdk.models.BotResponse;
import com.kore.ai.widgetsdk.models.CalEventsTemplateModel;
import com.kore.ai.widgetsdk.models.WCalEventsTemplateModel;
import com.kore.ai.widgetsdk.models.WTaskTemplateModel;
import com.kore.ai.widgetsdk.models.Widget;
import com.kore.ai.widgetsdk.models.WidgetListElementModel;
import com.kore.ai.widgetsdk.models.WidgetListModel;
import com.kore.ai.widgetsdk.utils.Constants;
import com.kore.ai.widgetsdk.utils.DialogCaller;
import com.kore.ai.widgetsdk.utils.StringUtils;
import com.kore.ai.widgetsdk.utils.Utility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.kore.ai.widgetsdk.adapters.ListWidgetButtonAdapter.showEmailIntent;

public class WidgetSelectActionsAdapter extends RecyclerView.Adapter<WidgetSelectActionsAdapter.WidgetCancelViewHolder> {

    WidgetActionSheetFragment widgetDialogActivity;
    Object actionList;
    Object model;
    Activity mainContext;
    boolean isFromFullView;


    private String skillName;
    private String trigger;
    private VerticalListViewActionHelper verticalListViewActionHelper;
    private boolean isFromListMenu = false;

    public WidgetSelectActionsAdapter(Activity mainContext, WidgetActionSheetFragment widgetDialogActivity, Object model,
                                      boolean isFromFullView, VerticalListViewActionHelper verticalListViewActionHelper,
                                      String skillName, String trigger, boolean isFromListMenu) {
        this.widgetDialogActivity = widgetDialogActivity;
        this.model = model;
        this.skillName = skillName;
        this.isFromListMenu = isFromListMenu;
        this.trigger = trigger;
        if (model instanceof WTaskTemplateModel) {
            this.actionList = ((WTaskTemplateModel) model).getActions();
        } else if (model instanceof WCalEventsTemplateModel) {
            this.actionList = ((WCalEventsTemplateModel) model).getActions();
        } else if (model instanceof Widget.Element) {
            this.actionList = ((Widget.Element) model).getActions();
        } else if (model instanceof WidgetListElementModel) {
            WidgetListElementModel elementModel = (WidgetListElementModel) model;
            if(isFromListMenu)
                this.actionList = elementModel.getValue().getMenu();
            else
                this.actionList = elementModel.getButtons();
        }
        else if(model instanceof WidgetListModel)
        {
            WidgetListModel listModel=(WidgetListModel)model;
            this.actionList=listModel.getHeaderOptions().getMenu();
        }
        else if(model instanceof BaseChartModel)
        {
            BaseChartModel baseChartModel=(BaseChartModel)model;
            this.actionList=baseChartModel.getHeaderOptions().getMenu();
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
            if (action.getType() != null && action.getType().equals("postback"))
                text = "\"" + action.getTitle() + "\"";
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
                        DialogCaller.showDialog(mainContext, null, new DialogInterface.OnClickListener() {
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

                            DialogCaller.showDialog(mainContext, null, new DialogInterface.OnClickListener() {
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
            if (action.getType() != null && action.getType().equals("postback"))
                text = "\"" + action.getTitle() + "\"";
            else
                text = action.getTitle();
            holder.tv_actions.setText(text);


        } else if (model instanceof Widget.Element) {
            Widget.Action act = ((Widget.Element) model).getActions().get(position);
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
                } else {
                    text = act.getTitle();
                }

                holder.tv_actions.setText(text);

                holder.tv_actions.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        (widgetDialogActivity).dismiss();

                        if (Constants.SKILL_SELECTION.equalsIgnoreCase(Constants.SKILL_HOME) || TextUtils.isEmpty(Constants.SKILL_SELECTION) ||
                                (!StringUtils.isNullOrEmpty(skillName) && !skillName.equalsIgnoreCase(Constants.SKILL_SELECTION))) {
                            buttonAction(act.getUtterance(), true);
                        } else {
                            buttonAction(act.getUtterance(), false);
                        }
                        /*if (Utility.checkIsSkillKora()) {
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
                        }*/
                    }
                });
            }
        } else if (model instanceof WidgetListElementModel) {
            WidgetListElementModel elementModel2 = (WidgetListElementModel) model;
            Widget.Button button = null;
            if(isFromListMenu)
                button = elementModel2.getValue().getMenu().get(position);
            else
                button = elementModel2.getButtons().get(position);
            holder.tv_actions.setText(button.getTitle());


            Widget.Button finalButton = button;
            holder.tv_actions.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    (widgetDialogActivity).dismiss();
                    if (Constants.SKILL_SELECTION.equalsIgnoreCase(Constants.SKILL_HOME) || TextUtils.isEmpty(Constants.SKILL_SELECTION) ||
                            (!StringUtils.isNullOrEmpty(skillName) && !skillName.equalsIgnoreCase(Constants.SKILL_SELECTION))) {


                        buttonClick(finalButton, true) ;
                    } else {
                        buttonClick(finalButton, false) ;
                    }
                }
            });
        }
        else if(model instanceof WidgetListModel)
        {
            WidgetListModel wL=(WidgetListModel)model;
          //  return (actionList!=null&&ba!=null&&ba.getHeaderOptions()!=null&&ba.getHeaderOptions().getMenu()!=null)?ba.getHeaderOptions().getMenu().size():0;
            holder.tv_actions.setText(wL.getHeaderOptions().getMenu().get(position).getTitle());
            Widget.Button finalButton = wL.getHeaderOptions().getMenu().get(position);
            holder.tv_actions.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    (widgetDialogActivity).dismiss();
                    if (Constants.SKILL_SELECTION.equalsIgnoreCase(Constants.SKILL_HOME) || TextUtils.isEmpty(Constants.SKILL_SELECTION) ||
                            (!StringUtils.isNullOrEmpty(skillName) && !skillName.equalsIgnoreCase(Constants.SKILL_SELECTION))) {


                        buttonClick(finalButton, true) ;
                    } else {
                        buttonClick(finalButton, false) ;
                    }
                }
            });

        }else if(model instanceof BaseChartModel)
        {
            BaseChartModel ba=(BaseChartModel)model;
            //  return (actionList!=null&&ba!=null&&ba.getHeaderOptions()!=null&&ba.getHeaderOptions().getMenu()!=null)?ba.getHeaderOptions().getMenu().size():0;
            holder.tv_actions.setText(ba.getHeaderOptions().getMenu().get(position).getTitle());
            Widget.Button finalButton = ba.getHeaderOptions().getMenu().get(position);
            holder.tv_actions.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    (widgetDialogActivity).dismiss();
                    if (Constants.SKILL_SELECTION.equalsIgnoreCase(Constants.SKILL_HOME) || TextUtils.isEmpty(Constants.SKILL_SELECTION) ||
                            (!StringUtils.isNullOrEmpty(skillName) && !skillName.equalsIgnoreCase(Constants.SKILL_SELECTION))) {


                        buttonClick(finalButton, true) ;
                    } else {
                        buttonClick(finalButton, false) ;
                    }
                }
            });
        }
    }

    public static boolean hasPermission(Context context, String... permission) {
        boolean shouldShowRequestPermissionRationale = true;
        if (Build.VERSION.SDK_INT >= 23) {
            int permissionLength = permission.length;
            for (int i = 0; i < permissionLength; i++) {
                shouldShowRequestPermissionRationale = shouldShowRequestPermissionRationale &&
                        ActivityCompat.checkSelfPermission(context, permission[i]) == PackageManager.PERMISSION_GRANTED;
            }
        }
        return shouldShowRequestPermissionRationale;
    }

    @SuppressLint("MissingPermission")
    public static void launchDialer(Context context, String url) {
        try {
            Intent intent = new Intent(hasPermission(context, Manifest.permission.CALL_PHONE) ? Intent.ACTION_CALL : Intent.ACTION_DIAL);
            intent.setData(Uri.parse(url));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_SINGLE_TOP
                    | Intent.FLAG_ACTIVITY_CLEAR_TOP
                    | Intent.FLAG_ACTIVITY_NO_HISTORY
                    | Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
            context.startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(context, "Invalid url!", Toast.LENGTH_SHORT).show();
        }
    }

    public void buttonClick(Widget.Button button, boolean appendUtterance) {
        if (button.getType() != null && button.getType().equalsIgnoreCase("url") && button.getUrl() != null) {
            Intent intent = new Intent(mainContext, GenericWebViewActivity.class);
            intent.putExtra("url", button.getUrl());
            intent.putExtra("header", mainContext.getResources().getString(R.string.app_name));
            mainContext.startActivity(intent);

        } else {
            String utterance = button.getUtterance();
            if (utterance != null && (utterance.startsWith("tel:") || utterance.startsWith("mailto:"))) {
                if (utterance.startsWith("tel:")) {
                    launchDialer(mainContext, utterance);
                } else if (utterance.startsWith("mailto:")) {
                    showEmailIntent((Activity) mainContext, utterance.split(":")[1]);
                }
                return;
            }
            buttonAction(utterance, appendUtterance);
        }
    }

    public void buttonAction(String utterance, boolean appendUtterance) {


        EntityEditEvent event = new EntityEditEvent();
        StringBuffer msg = new StringBuffer("");
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("refresh", Boolean.TRUE);
        if (appendUtterance && trigger != null)
            msg = msg.append(trigger).append(" ");
        msg.append(utterance);
        event.setMessage(msg.toString());
        event.setPayLoad(new Gson().toJson(hashMap));
        event.setScrollUpNeeded(true);
        KoreEventCenter.post(event);
    }

    public String getSkillName() {
        return skillName;
    }

    public void setSkillName(String skillName) {
        this.skillName = skillName;
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
        } else if (model instanceof Widget.Element) {
            return model != null && actionList != null ? ((Widget.Element) model).getActions().size() : 0;
        } else if (model instanceof WidgetListElementModel) {
            if(!isFromListMenu)
                return model != null && ((WidgetListElementModel) model).getButtons() != null ? ((WidgetListElementModel) model).getButtons().size() : 0;
            else
                return model != null && ((WidgetListElementModel) model).getValue() != null &&((WidgetListElementModel) model).getValue().getMenu() != null ? ((WidgetListElementModel) model).getValue().getMenu().size() : 0;
        }
        else if(model instanceof BaseChartModel)
        {
            BaseChartModel ba=(BaseChartModel)model;
            return (actionList!=null&&ba!=null&&ba.getHeaderOptions()!=null&&ba.getHeaderOptions().getMenu()!=null)?ba.getHeaderOptions().getMenu().size():0;
        }else if(model instanceof WidgetListModel)
        {
            WidgetListModel ba=(WidgetListModel)model;
            return (actionList!=null&&ba!=null&&ba.getHeaderOptions()!=null&&ba.getHeaderOptions().getMenu()!=null)?ba.getHeaderOptions().getMenu().size():0;
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
