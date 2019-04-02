package kore.botssdk.adapter;

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
import kore.botssdk.dialogs.WidgetDialogActivity;
import kore.botssdk.dialogs.WidgetDialogActivityTask;
import kore.botssdk.event.KoreEventCenter;
import kore.botssdk.events.CancelEvent;
import kore.botssdk.models.CalEventsTemplateModel;
import kore.botssdk.models.TaskTemplateModel;
import kore.botssdk.models.WTaskTemplateModel;

public class WidgetSelectActionsAdapter extends RecyclerView.Adapter<WidgetSelectActionsAdapter.WidgetCancelViewHolder> {

    WidgetDialogActivityTask widgetDialogActivity;
    List<CalEventsTemplateModel.Action> actionList;
    WTaskTemplateModel model;

    public WidgetSelectActionsAdapter(WidgetDialogActivityTask widgetDialogActivity, WTaskTemplateModel model) {
        this.widgetDialogActivity = widgetDialogActivity;
        this.model = model;
        this.actionList = model.getActions();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public WidgetCancelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(widgetDialogActivity.getContext()).inflate(R.layout.widget_cancel_laout, parent, false);
        return new WidgetCancelViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull WidgetCancelViewHolder holder, int position) {

        holder.tv_actions.setText(actionList.get(position).getTitle());

        holder.tv_actions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("action", actionList.get(position).getTitle());
                hashMap.put("tIds", model.getId());
                KoreEventCenter.post(new CancelEvent(actionList.get(position).getUtterance(), new Gson().toJson(hashMap), 0));
                (widgetDialogActivity).dismiss();
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
