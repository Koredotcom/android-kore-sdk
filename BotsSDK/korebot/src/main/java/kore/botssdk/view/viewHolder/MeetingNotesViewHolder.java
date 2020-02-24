package kore.botssdk.view.viewHolder;

import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import kore.botssdk.R;
import kore.botssdk.utils.Utility;

public class MeetingNotesViewHolder extends RecyclerView.ViewHolder {
        public View header;
        public View click_view;
       public TextView icon_view,root_title_view,count_view,title_view,date_view,creator_view,task_view;
        public MeetingNotesViewHolder(@NonNull View itemView) {
            super(itemView);
            icon_view=itemView.findViewById(R.id.icon_view);

            click_view=itemView.findViewById(R.id.click_view);
            header=itemView.findViewById(R.id.header);
            root_title_view=itemView.findViewById(R.id.root_title_view);
            count_view=itemView.findViewById(R.id.count_view);
            title_view=itemView.findViewById(R.id.title_view);
            date_view=itemView.findViewById(R.id.date_view);
            creator_view=itemView.findViewById(R.id.creator_view);
            task_view=itemView.findViewById(R.id.task_view);

        }
    }