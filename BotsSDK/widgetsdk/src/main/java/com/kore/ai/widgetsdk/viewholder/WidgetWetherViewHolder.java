package com.kore.ai.widgetsdk.viewholder;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kora.ai.widgetsdk.R;

public class WidgetWetherViewHolder extends RecyclerView.ViewHolder {

    public LinearLayout summery_items;
    public TextView meetIcon,dueIcon, OverDueIcon;
    public TextView tv_wether_title, tv_wether_description, tv_upcoming_meeting, tv_task_due, tv_task_overdue;
    public ImageView img_wether_icon;
    public TextView tv_wether_type;
    public ViewGroup wether_info_layout;
    public View row_first,row_second,row_third;


    public WidgetWetherViewHolder(@NonNull View itemView) {
        super(itemView);

        row_first=itemView.findViewById(R.id.row_first);
        row_second=itemView.findViewById(R.id.row_second);
        row_third=itemView.findViewById(R.id.row_third);

        summery_items=itemView.findViewById(R.id.summery_items);
        tv_wether_title = (TextView) itemView.findViewById(R.id.tv_wether_title);
        tv_wether_description = (TextView) itemView.findViewById(R.id.tv_wether_description);
        tv_upcoming_meeting = (TextView) itemView.findViewById(R.id.tv_upcoming_meeting);
        tv_task_due = (TextView) itemView.findViewById(R.id.tv_task_due);
        tv_task_overdue = (TextView) itemView.findViewById(R.id.tv_task_overdue);
        tv_wether_type = (TextView) itemView.findViewById(R.id.tv_wether_type);
        wether_info_layout = itemView.findViewById(R.id.wether_info_layout);

        img_wether_icon = (ImageView) itemView.findViewById(R.id.img_wether_icon);

        meetIcon = (TextView)itemView.findViewById(R.id.meetingIcon);
        dueIcon = (TextView)itemView.findViewById(R.id.taskDueIcon);
        OverDueIcon = (TextView)itemView.findViewById(R.id.taskOverDueIcon);


    }
}
