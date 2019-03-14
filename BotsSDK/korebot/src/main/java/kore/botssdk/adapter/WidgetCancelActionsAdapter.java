package kore.botssdk.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import kore.botssdk.R;
import kore.botssdk.dialogs.WidgetDialogActivity;


import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import kore.botssdk.models.CalEventsTemplateModel;

public class WidgetCancelActionsAdapter extends RecyclerView.Adapter<WidgetCancelActionsAdapter.WidgetCancelViewHolder> {

    WidgetDialogActivity widgetDialogActivity;
    List<CalEventsTemplateModel.Action> actionList;

    public WidgetCancelActionsAdapter(WidgetDialogActivity widgetDialogActivity, List<CalEventsTemplateModel.Action> actionList) {
        this.widgetDialogActivity = widgetDialogActivity;
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

            }
        });

    }

    @Override
    public int getItemCount() {
        return actionList!= null ?actionList.size():0;
    }

    class WidgetCancelViewHolder extends RecyclerView.ViewHolder {
        TextView tv_actions;

        public WidgetCancelViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_actions = itemView.findViewById(R.id.tv_actions);
        }
    }
}
