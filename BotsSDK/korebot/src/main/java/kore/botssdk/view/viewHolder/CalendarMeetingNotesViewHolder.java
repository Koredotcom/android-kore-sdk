package kore.botssdk.view.viewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import kore.botssdk.R;

public class CalendarMeetingNotesViewHolder extends RecyclerView.ViewHolder {
        public View divider;
        public View click_view,sidebar;
       public TextView title_view,date_view,creator_view,date_title;
        public CalendarMeetingNotesViewHolder(@NonNull View itemView) {
            super(itemView);
            divider=itemView.findViewById(R.id.divider);
            click_view=itemView.findViewById(R.id.click_view);
            title_view=itemView.findViewById(R.id.title_view);
            date_view=itemView.findViewById(R.id.date_view);
            creator_view=itemView.findViewById(R.id.creator_view);
            sidebar=itemView.findViewById(R.id.sidebar);
            date_title=itemView.findViewById(R.id.date_title);
        }
    }