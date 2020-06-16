package kore.botssdk.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import kore.botssdk.R;
import kore.botssdk.listener.RecyclerViewDataAccessor;
import kore.botssdk.listener.VerticalListViewActionHelper;
import kore.botssdk.models.CalEventsTemplateModel;
import kore.botssdk.utils.DateUtils;
import kore.botssdk.utils.Utility;
import kore.botssdk.utils.WidgetViewMoreEnum;
import kore.botssdk.view.viewHolder.CalendarMeetingNotesViewHolder;
import kore.botssdk.view.viewHolder.EmptyWidgetViewHolder;
import kore.botssdk.view.viewHolder.MeetingNotesViewHolder;

import static android.view.View.GONE;

public class CalendarMeetingNotesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements RecyclerViewDataAccessor {

    Context context;
    ArrayList<CalEventsTemplateModel> modelData;
    VerticalListViewActionHelper verticalListViewActionHelper;

    public CalendarMeetingNotesAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if(viewType==DATA_FOUND) {
            View view = LayoutInflater.from(context).inflate(R.layout.cal_meeting_notes_layout, parent, false);
            return new CalendarMeetingNotesViewHolder(view);
        }
        else {

            View view = LayoutInflater.from(context).inflate(R.layout.card_empty_widget_layout, parent, false);
            return new EmptyWidgetViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holderOrig, int position) {

        if(holderOrig instanceof CalendarMeetingNotesViewHolder) {
            CalendarMeetingNotesViewHolder holder=(CalendarMeetingNotesViewHolder)holderOrig;
            CalEventsTemplateModel model = modelData.get(position);
            holder.date_view.setText(DateUtils.getDateMMMDDYYYYNotes_multidate(model.getDuration().getStart(), model.getDuration().getEnd()));
         //   String text = Utility.getFormatedAttendiesFromList(model.getAttendees());
            holder.creator_view.setText(model.getnNotes());
            holder.title_view.setText(model.getTitle());
            if(TextUtils.isEmpty(model.getTitle())) {
                holder.title_view.setText(context.getResources().getString(R.string.no_title_lbl));
            }
            holder.divider.setVisibility(View.VISIBLE);
            if (model.isShowDate())
            {
                holder.date_title.setVisibility(View.VISIBLE);
                String date = DateUtils.formattedSentDateV8_Notes((long) model.getDuration().getStart());
                holder.date_title.setText(date);
            }
            else {
                holder.date_title.setVisibility(GONE);
            }
            if(model.getColor()!=null)
                holder.sidebar.setBackgroundColor(Color.parseColor(model.getColor()));

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    verticalListViewActionHelper.meetingNotesNavigation(context, model.getMeetingNoteId(), model.getEventId());
                }
            });
        }
        else {
            EmptyWidgetViewHolder emptyHolder = (EmptyWidgetViewHolder) holderOrig;

            emptyHolder.tv_disrcription.setText(msg != null ? msg : "No Meeting Notes");

            emptyHolder.img_icon.setImageDrawable(holderOrig.getItemViewType() == EMPTY_CARD ? ContextCompat.getDrawable(context, R.drawable.no_meeting) : errorIcon);
            if (errorIcon == null) {
                emptyHolder.img_icon.setVisibility(GONE);
            }

        }
    }

    @Override
    public int getItemCount() {
   /*     if(isFromWidget&&widgetViewMoreEnum!=null&&widgetViewMoreEnum==WidgetViewMoreEnum.EXPAND_VIEW)
        {
            return  modelData != null && modelData.size() > 0 ? (isFromWidget && modelData.size() > 3 ? 3 : modelData.size()) : 0;
        }

        return modelData != null ? modelData.size() : 0;*/

        if (widgetViewMoreEnum!=null&&widgetViewMoreEnum==WidgetViewMoreEnum.EXPAND_VIEW) {
            return modelData != null && modelData.size() > 0 ? modelData.size() : 1;
        }
        return modelData != null && modelData.size() > 0 ? (isFromWidget && modelData.size() > 3 ? 3 : modelData.size()) : 1;

    }

    public void setMessage(String msg, Drawable errorIcon) {
        this.msg = msg;
        this.errorIcon = errorIcon;
    }
    String msg;
    Drawable errorIcon;
    private int DATA_FOUND = 1;
    private int EMPTY_CARD = 0;
    private int MESSAGE = 2;
    @Override
    public int getItemViewType(int position) {

        if(modelData!=null&&modelData.size()>0)
        {
            return DATA_FOUND;
        }
        if (msg != null && !msg.equalsIgnoreCase("")) {
            return MESSAGE;
        }
        return EMPTY_CARD;
    }

    @Override
    public ArrayList getData() {
        return modelData;
    }

    @Override
    public void setData(ArrayList modelData) {

        this.modelData = modelData;
        notifyDataSetChanged();
    }
    public void refreshNewData(ArrayList newModelData){
        if(this.modelData != null && newModelData!=null){
            modelData.clear();
            modelData.addAll(newModelData);
            notifyDataSetChanged();
        }
    }

    public void addData(ArrayList modelData){
        if(this.modelData != null){
            int size = this.modelData.size();
            this.modelData.addAll(modelData);
            notifyItemInserted(size);
        }
    }
    @Override
    public void setExpanded(boolean isExpanded) {

    }

    @Override
    public void setVerticalListViewActionHelper(VerticalListViewActionHelper verticalListViewActionHelper) {
        this.verticalListViewActionHelper = verticalListViewActionHelper;
    }

    boolean isFromWidget;
    WidgetViewMoreEnum widgetViewMoreEnum;
    public void setFromWidget(boolean isFromWidget) {

        this.isFromWidget=isFromWidget;
    }

    public void setWidgetEnum(WidgetViewMoreEnum widgetViewMoreEnum) {
        this.widgetViewMoreEnum=widgetViewMoreEnum;
    }
}
