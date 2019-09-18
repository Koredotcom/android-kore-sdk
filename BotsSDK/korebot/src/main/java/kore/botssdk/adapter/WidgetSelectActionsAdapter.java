package kore.botssdk.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import kore.botssdk.R;
import kore.botssdk.dialogs.WidgetDialogActivityTask;
import kore.botssdk.event.KoreEventCenter;
import kore.botssdk.events.CancelEvent;
import kore.botssdk.models.CalEventsTemplateModel;
import kore.botssdk.models.TaskTemplateModel;
import kore.botssdk.models.WTaskTemplateModel;
import kore.botssdk.utils.Constants;
import kore.botssdk.utils.DialogCaller;
import kore.botssdk.utils.Utility;

public class WidgetSelectActionsAdapter extends RecyclerView.Adapter<WidgetSelectActionsAdapter.WidgetCancelViewHolder> {

    WidgetDialogActivityTask widgetDialogActivity;
    List<CalEventsTemplateModel.Action> actionList;
    WTaskTemplateModel model;
    Activity mainContext;
    boolean isFromFullView;

    public WidgetSelectActionsAdapter(Activity mainContext, WidgetDialogActivityTask widgetDialogActivity, WTaskTemplateModel model, boolean isFromFullView) {
        this.widgetDialogActivity = widgetDialogActivity;
        this.model = model;
        this.actionList = model.getActions();
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


   public void  startActions(int position,boolean append_uttrance)
    {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("action", actionList.get(position).getTitle());
        hashMap.put("tIds", model.getId());
        KoreEventCenter.post(new CancelEvent((append_uttrance?Constants.SKILL_UTTERANCE:"")+actionList.get(position).getUtterance(), new Gson().toJson(hashMap), 0,true));
        (widgetDialogActivity).dismiss();
        if (mainContext != null && mainContext instanceof Activity && isFromFullView) {
            mainContext.finish();
        }
    }
    @Override
    public void onBindViewHolder(@NonNull WidgetCancelViewHolder holder, int position) {

        holder.tv_actions.setText(actionList.get(position).getTitle());

        holder.tv_actions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(Utility.checkIsSkillKora())
                {
                    startActions(position,false);

                }else {
                    /*AlertDialog.Builder builder = new AlertDialog.Builder(mainContext);
                    builder.setPositiveButton(mainContext.getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            startActions(position);
                            dialogInterface.dismiss();
                        }
                    });
                    builder.setNegativeButton(mainContext.getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
                    builder.setTitle("Kora");
                    builder.setMessage("You are conversing with " + Constants.SKILL_HOME + " skill now. Clicking on this will end your conversation with " + Constants.SKILL_SELECTION + " skill and move to Kora skill");
                    builder.setCancelable(false);
                    builder.show();*/
                    DialogCaller.showDialog(mainContext,new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startActions(position,true);
                            dialog.dismiss();
                        }
                    });
                }









            }
        });

    }

    @Override
    public int getItemCount() {
        return actionList!= null?actionList.size():0;
    }

    class WidgetCancelViewHolder extends RecyclerView.ViewHolder {
        TextView tv_actions;

        public WidgetCancelViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_actions = itemView.findViewById(R.id.tv_actions);
        }
    }
}
