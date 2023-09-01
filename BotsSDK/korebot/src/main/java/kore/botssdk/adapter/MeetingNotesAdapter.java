package kore.botssdk.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import kore.botssdk.R;
import kore.botssdk.listener.RecyclerViewDataAccessor;
import kore.botssdk.listener.VerticalListViewActionHelper;
import kore.botssdk.models.CalEventsTemplateModel;
import kore.botssdk.utils.DateUtils;
import kore.botssdk.utils.Utility;
import kore.botssdk.view.viewHolder.MeetingNotesViewHolder;

public class MeetingNotesAdapter extends RecyclerView.Adapter<MeetingNotesViewHolder> implements RecyclerViewDataAccessor {

    final Context context;
    ArrayList<CalEventsTemplateModel> modelData;
    VerticalListViewActionHelper verticalListViewActionHelper;

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


        CalEventsTemplateModel model = modelData.get(position);
        holder.header.setVisibility(View.GONE);
        holder.date_view.setText(DateUtils.getDateMMMDDYYYY(model.getDuration().getStart(), model.getDuration().getEnd()));
        String text = Utility.getFormatedAttendiesFromList(model.getAttendees());
        holder.creator_view.setText(text);
        holder.title_view.setText(model.getTitle());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                verticalListViewActionHelper.meetingNotesNavigation(context,model.getMeetingNoteId(),model.getEventId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return modelData!=null?modelData.size():0;
    }


    @Override
    public ArrayList getData() {
        return modelData;
    }

    @Override
    public void setData(ArrayList modelData) {

        this.modelData = modelData;
        notifyItemRangeChanged(0, modelData.size() - 1);
    }

    @Override
    public void setExpanded(boolean isExpanded) {

    }

    @Override
    public void setVerticalListViewActionHelper(VerticalListViewActionHelper verticalListViewActionHelper) {
        this.verticalListViewActionHelper = verticalListViewActionHelper;
    }
}
