package kore.botssdk.view.viewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import kore.botssdk.R;

public class MeetingNotesViewHolder extends RecyclerView.ViewHolder {
        public final View header;
        public final View click_view;
       public final TextView icon_view;
    public final TextView root_title_view;
    public final TextView count_view;
    public final TextView title_view;
    public final TextView date_view;
    public final TextView creator_view;
    public final TextView task_view;
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