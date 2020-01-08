package kore.botssdk.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import kore.botssdk.R;
import kore.botssdk.utils.Utility;

public class MeetingNotesAdapter extends RecyclerView.Adapter<MeetingNotesAdapter.MeetingNotesViewHolder> {

    Context context;

    public MeetingNotesAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public MeetingNotesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.us_meeting_layout, parent, false);
        return new MeetingNotesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MeetingNotesViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class MeetingNotesViewHolder extends RecyclerView.ViewHolder {

        TextView icon_view,root_title_view,count_view,title_view,date_view,creator_view,task_view;
        public MeetingNotesViewHolder(@NonNull View itemView) {
            super(itemView);
            icon_view=itemView.findViewById(R.id.icon_view);

            icon_view=itemView.findViewById(R.id.icon_view);
            icon_view.setTypeface(Utility.getTypeFaceObj(context));
            icon_view.setBackground(Utility.changeColorOfDrawable(context, Color.parseColor("#4e74f0")));

            root_title_view=itemView.findViewById(R.id.root_title_view);
            count_view=itemView.findViewById(R.id.count_view);
            title_view=itemView.findViewById(R.id.title_view);
            date_view=itemView.findViewById(R.id.date_view);
            creator_view=itemView.findViewById(R.id.creator_view);
            task_view=itemView.findViewById(R.id.task_view);
        }
    }
}
