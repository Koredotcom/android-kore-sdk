package kore.botssdk.adapter;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static kore.botssdk.utils.DateUtils.getDateinDayFormat;
import static kore.botssdk.utils.DateUtils.getTimeInAmPm;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;

import kore.botssdk.R;
import kore.botssdk.activity.GenericWebViewActivity;
import kore.botssdk.dialogs.WidgetDialogActivity;
import kore.botssdk.event.KoreEventCenter;
import kore.botssdk.events.CancelEvent;
import kore.botssdk.listener.ComposeFooterInterface;
import kore.botssdk.listener.RecyclerViewDataAccessor;
import kore.botssdk.listener.VerticalListViewActionHelper;
import kore.botssdk.models.BotResponse;
import kore.botssdk.models.CalEventsTemplateModel;
import kore.botssdk.models.WidgetDialogModel;
import kore.botssdk.utils.DateUtils;
import kore.botssdk.utils.StringUtils;
import kore.botssdk.view.viewHolder.EmptyWidgetViewHolder;

/**
 * Created by Ramachandra Pradeep on 02-Aug-18.
 */

public class CalendarEventsAdapter extends RecyclerView.Adapter implements RecyclerViewDataAccessor {
    private boolean isExpanded = false;
    VerticalListViewActionHelper verticalListViewActionHelper;
    ArrayList<String> selectedIds = null;
    private CalEventsTemplateModel.Duration _cursor;

    public ArrayList<CalEventsTemplateModel> getEventList() {
        return eventList;
    }

    public void setEventList(ArrayList<CalEventsTemplateModel> eventList) {
        if (eventList != null) {
            this.eventList.clear();
            this.eventList.addAll(eventList);
            this.title = "SHOW MORE";

        }
    }

    ArrayList<CalEventsTemplateModel> eventList = new ArrayList<>();
    private LayoutInflater inflater = null;
    private final int EVENTS_LIST_LIMIT = 3;
    private String title = "SHOW MORE";
    private EventSelectionListener eventSelectionListener;
    private final Context mContext;
    private final int DATA_FOUND = 1;
    private final int EMPTY_CARD = 0;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    private String type;
    private final boolean isEnabled;
    private ComposeFooterInterface composeFooterInterface;
    private final Gson gson = new Gson();
    private boolean isFromWidget;

    public boolean isFromWidget() {
        return isFromWidget;
    }

    private final Drawable selectedCheck;
    private final Drawable unSelectedCheck;

    private final Drawable insetDivider;
    private final Drawable normalDivider;

    public void setFromWidget(boolean fromWidget) {
        isFromWidget = fromWidget;
    }


    public CalendarEventsAdapter(Context mContext, String type, boolean isEnabled) {
        this.mContext = mContext;
        inflater = LayoutInflater.from(mContext);
        this.type = type;
        this.isEnabled = isEnabled;
        selectedIds = new ArrayList<>();
        selectedCheck = ResourcesCompat.getDrawable(mContext.getResources(), R.mipmap.checkbox_on, mContext.getTheme());
        unSelectedCheck = ResourcesCompat.getDrawable(mContext.getResources(), R.mipmap.checkbox_off, mContext.getTheme());

        insetDivider = ResourcesCompat.getDrawable(mContext.getResources(), R.drawable.inset_65_divider, mContext.getTheme());
        normalDivider = ResourcesCompat.getDrawable(mContext.getResources(), R.drawable.inset_divider_meetings, mContext.getTheme());
    }

    public CalEventsTemplateModel getItem(int position) {
        if (position < eventList.size())
            return eventList.get(position);
        else return null;
    }


    @Override
    public int getItemViewType(int position) {
        if (eventList != null && eventList.size() > 0) {
            return DATA_FOUND;
        }

        return EMPTY_CARD;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == EMPTY_CARD) {


            View view = inflater.inflate(R.layout.card_empty_widget_layout, parent, false);
            return new EmptyWidgetViewHolder(view);
        } else
            return new ViewHolder(inflater.inflate(R.layout.calendar_event_list_item, parent, false));
    }


    public String checkStringNull(String value) {

        if (value != null && !value.trim().equalsIgnoreCase("")) {
            return value.trim();
        }
        return "";


    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holderData, int position) {
        if (holderData.getItemViewType() == EMPTY_CARD) {
            EmptyWidgetViewHolder emptyHolder = (EmptyWidgetViewHolder) holderData;

            emptyHolder.tv_disrcription.setText(R.string.no_upcoming_meetings);
            emptyHolder.img_icon.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.no_meeting));


        } else {

            final ViewHolder holder = (ViewHolder) holderData;
            final CalEventsTemplateModel model = eventList.get(position);

            String date = DateUtils.getCalDay((long) model.getDuration().getStart()).toUpperCase();
            holder.txtDateTime.setText(date);

            if (selectedIds.size() > 0) {
                holder.checkbox.setVisibility(VISIBLE);
                holder.checkbox.setImageDrawable(selectedIds.contains(model.getEventId()) ? selectedCheck : unSelectedCheck);
            } else {
                holder.checkbox.setVisibility(GONE);
            }


            holder.txtTitle.setText(model.getTitle());
            if (!StringUtils.isNullOrEmptyWithTrim(model.getWhere())) {
                holder.txtPlace.setText(model.getWhere());
                holder.txtPlace.setVisibility(VISIBLE);
            } else {
                holder.txtPlace.setVisibility(GONE);

            }

            if(!StringUtils.isNullOrEmpty(model.getReqTextToDisplay()))
                holder.tv_time.setText(model.getReqTextToDisplay());
            else {
                String str = DateUtils.calendar_list_format_2.format(model.getDuration().getStart()) + "\n" + DateUtils.calendar_list_format_2.format(model.getDuration().getEnd());
                holder.tv_time.setText(str);
            }


            holder.tv_users.setText(getFormatedAttendiesFromList(model.getAttendees()));

            if(StringUtils.isNullOrEmpty(model.getMeetingNoteId())){
                holder.tv_meeting_notes.setVisibility(View.GONE);
            }else{
                holder.tv_meeting_notes.setVisibility(View.VISIBLE);
                holder.tv_meeting_notes.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        verticalListViewActionHelper.meetingNotesNavigation(mContext,model.getMeetingNoteId(),model.getEventId());
                    }
                });
            }
            if (position == 0 || model.isShowDate()) {
                holder.tvborder.setVisibility(VISIBLE);
                holder.txtDateTime.setVisibility(VISIBLE);
            } else {
                holder.tvborder.setVisibility(GONE);
                holder.txtDateTime.setVisibility(GONE);
            }

            if(model.getColor()!=null)
                holder.sideBar.setBackgroundColor(Color.parseColor(model.getColor()));

            if (position < getItemCount() - 1) {
                holder.divider.setBackground(getItem(position + 1).isShowDate() ? insetDivider : normalDivider);
            }
            if(position == eventList.size()-1 && eventList.size()<=3)
                holder.divider.setVisibility(View.GONE);
            holder.innerlayout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    if (verticalListViewActionHelper != null && isFromWidget()) {
                        if (selectedIds.contains(model.getEventId())) {
                            selectedIds.remove(model.getEventId());
                        } else {
                            selectedIds.add(model.getEventId());

                        }
                        verticalListViewActionHelper.widgetItemSelected(true, selectedIds.size());
                        return true;
                    }
                    return false;
                }
            });
            holder.innerlayout.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (BotResponse.TEMPLATE_TYPE_CAL_EVENTS_WIDGET.equalsIgnoreCase(type) || isFromWidget()) {
                        //from left widget click

                        if (selectedIds != null && selectedIds.size() > 0 && verticalListViewActionHelper != null) {
                            // multiple item can be selected after long press and single click on other items
                            if (selectedIds.contains(model.getEventId())) {
                                selectedIds.remove(model.getEventId());
                            } else {
                                selectedIds.add(model.getEventId());
                            }
                            verticalListViewActionHelper.widgetItemSelected(true, selectedIds.size());

                        } else {
                            WidgetDialogModel widgetDialogModel = new WidgetDialogModel();
                            widgetDialogModel.setAttendies(checkStringNull(holder.tv_users.getText() != null ? holder.tv_users.getText().toString().trim() : ""));
                            widgetDialogModel.setLocation(checkStringNull(holder.txtPlace.getText() != null ? holder.txtPlace.getText().toString().trim() : ""));
                            widgetDialogModel.setTime(checkStringNull(holder.tv_time.getText() != null ? holder.tv_time.getText().toString().trim() : ""));
                            widgetDialogModel.setTitle(checkStringNull(holder.txtTitle.getText() != null ? holder.txtTitle.getText().toString().trim() : ""));
                            widgetDialogModel.setColor(checkStringNull(model.getColor()));

                            final WidgetDialogActivity dialogActivity = new WidgetDialogActivity(mContext, widgetDialogModel, null, false,verticalListViewActionHelper);

                            dialogActivity.show();

                            dialogActivity.findViewById(R.id.img_cancel).setOnClickListener(new OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    dialogActivity.dissmissanim();
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            dialogActivity.dismiss();
                                        }
                                    }, 400);

                                }
                            });
                        }
                    } else if (BotResponse.TEMPLATE_TYPE_CAL_EVENTS.equalsIgnoreCase(type)) {
                        try {
                            verticalListViewActionHelper.calendarItemClicked(BotResponse.TEMPLATE_TYPE_CAL_EVENTS, model);
                        } catch (Exception e) {
                            launchWebView(model.getHtmlLink());
                        }
                    } else if (isEnabled) {
                        if (model.getDefaultAction() != null && model.getDefaultAction().getType() != null && model.getDefaultAction().getType().equals("url")) {
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(model.getDefaultAction().getUrl()));
                            try {
                                mContext.startActivity(browserIntent);
                            }catch (ActivityNotFoundException ex){
                                ex.printStackTrace();
                            }
                        }else if(model.getDefaultAction() != null && model.getDefaultAction().getType() != null && model.getDefaultAction().getType().equals("postback")){
                            String message = model.getTitle() + " : " + getDateinDayFormat((long) model.getDuration().getStart()) + ", " + getTimeInAmPm((long) model.getDuration().getStart()) + " - " + getTimeInAmPm((long) model.getDuration().getEnd());
                            if (composeFooterInterface != null) {
                                composeFooterInterface.sendWithSomeDelay(StringUtils.isNullOrEmpty(model.getDefaultAction().getTitle())?message:model.getDefaultAction().getTitle(),
                                        model.getDefaultAction().getPayload(), 0, false);
                            } else {
                                KoreEventCenter.post(new CancelEvent(StringUtils.isNullOrEmpty(model.getDefaultAction().getTitle())?message:model.getDefaultAction().getTitle(),
                                        model.getDefaultAction().getPayload(), 0, false));
                                ((Activity) mContext).finish();
                            }
                        }else {
                            HashMap<String, String> hashMap = new HashMap<>();
                            hashMap.put("meetingId", model.getEventId());
                            String message = model.getTitle() + " : " + getDateinDayFormat((long) model.getDuration().getStart()) + ", " + getTimeInAmPm((long) model.getDuration().getStart()) + " - " + getTimeInAmPm((long) model.getDuration().getEnd());
                            if (composeFooterInterface != null) {
                                composeFooterInterface.sendWithSomeDelay(message, gson.toJson(hashMap), 0, false);
                            } else {
                                KoreEventCenter.post(new CancelEvent(message, gson.toJson(hashMap), 0, false));
                                ((Activity) mContext).finish();
                            }
                        }
                    }
                }
            });
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return eventList != null && eventList.size() > 0 ? (!isExpanded && eventList.size() > 3 ? 3 : eventList.size()) : 1;
    }


    public ComposeFooterInterface getComposeFooterInterface() {
        return composeFooterInterface;
    }

    public void setComposeFooterInterface(ComposeFooterInterface composeFooterInterface) {
        this.composeFooterInterface = composeFooterInterface;
    }

    @Override
    public ArrayList getData() {
        return eventList;
    }

    @Override
    public void setData(ArrayList data) {
        this.eventList = data;
        if (eventList != null) {
            try {
                this.eventList = expandEventList(eventList);
            }catch (Exception e)
            {
                e.printStackTrace();
            }
            this.eventList = sortEventList(eventList);

            if(eventList.size()>3){
                if(verticalListViewActionHelper!=null)
                    verticalListViewActionHelper.meetingWidgetViewMoreVisibility(true);
            }
        }
    }

    private ArrayList<CalEventsTemplateModel> expandEventList(ArrayList<CalEventsTemplateModel> eventList){
        ArrayList<CalEventsTemplateModel> reqData = new ArrayList<>();
        for (CalEventsTemplateModel data : eventList) {
            long _start = (long) data.getDuration().getStart();
            long _end = (long) data.getDuration().getEnd();

            //getting the days count, observed different values if we pass the timestamo directly so we are giving the that day early hours 00:00AM
            int _days = DateUtils.getDays( DateUtils.getDDMMYYYY((long) data.getDuration().getEnd()).getTime()- DateUtils.getDDMMYYYY((long) data.getDuration().getStart()).getTime());

            Date eventStartDate = DateUtils.getDDMMYYYY(_start);

            Date cursorStartDate = DateUtils.getDDMMYYYY((long) _cursor.getStart());
            Date cursorEndDate = DateUtils.getDDMMYYYY((long) _cursor.getEnd());

            //CHECK FOR MORE THAN A DAY EVENT
            if(_days>0) {

                long _ostart = _start;

                double st = 0;
                double ed = 0;

                for (int i = 0; i <= _days; i++) {
                    if(eventStartDate != null)
                    {
                        if(eventStartDate.compareTo(cursorStartDate)<0){
                            _start += (24*60*60*1000);
                            _start = Objects.requireNonNull(DateUtils.getDDMMYYYY(_start)).getTime();
                            eventStartDate = DateUtils.getDDMMYYYY(_start);
                            continue;
                        }
                    }

                    if(Objects.requireNonNull(DateUtils.getDDMMYYYY((long) st)).compareTo(cursorEndDate) == 0) {
                        break;
                    }

                    CalEventsTemplateModel _data = null;
                    try {
                        _data = data.clone();
                    } catch (CloneNotSupportedException e) {
                        e.printStackTrace();
                    }

                    if(_data != null)
                    {
                        String txt = "";

                        if (i == 0) {
                            if (_data.isAllDay()) {
                                txt = "All Day\nDay (" + (i + 1) + "/" + (_days + 1) + ")";
                                _data.setReqTextToDisplayForDetails(DateUtils.getMorethanDayDate(_ostart, _end));
                            } else {
                                txt = "From\n" + DateUtils.calendar_list_format_2.format(_data.getDuration().getStart()) + "\nDay (" + (i + 1) + "/" + (_days + 1) + ")";
                                _data.setReqTextToDisplayForDetails(DateUtils.getMorethanDayDateTime(_ostart, _end));
                            }
                            _data.setReqTextToDisplay(txt);

                            st += _start;
                            ed = _start + (30 * 60000);

                        } else if (i == _days) {
                            if (_data.isAllDay()) {
                                txt = "All Day\nDay (" + (i + 1) + "/" + (_days + 1) + ")";
                                _data.setReqTextToDisplayForDetails(DateUtils.getMorethanDayDate(_ostart, _end));
                            } else {
                                if (Objects.requireNonNull(DateUtils.getDDMMYYYY(_start)).compareTo(DateUtils.getDDMMYYYY(_end)) == 0 && DateUtils.getOneDayMiliseconds(_end - _start) >= 23.98f) {
                                    txt = "All Day";
                                }
                                else {
                                    txt = "Till\n" + DateUtils.calendar_list_format_2.format(_data.getDuration().getEnd()) + "\nDay (" + (i + 1) + "/" + (_days + 1) + ")";
                                }
                                _data.setReqTextToDisplayForDetails(DateUtils.getMorethanDayDateTime(_ostart, _end));
                            }
                            _data.setReqTextToDisplay(txt);

                            st = _end;
                            st = Objects.requireNonNull(DateUtils.getDDMMYYYY((long) st)).getTime();
                            ed = _end;
                        } else {
                            txt = "All Day\nDay (" + (i + 1) + "/" + (_days + 1) + ")";
                            _data.setReqTextToDisplay(txt);
                            if(_data.isAllDay()){
                                _data.setReqTextToDisplayForDetails(DateUtils.getMorethanDayDate(_ostart, _end));
                            }else{
                                _data.setReqTextToDisplayForDetails(DateUtils.getMorethanDayDateTime(_ostart, _end));
                            }

                            if (st == 0) {
                                st += _start;
                            } else {
                                st += 24 * 60 * 60 * 1000;
                                st = Objects.requireNonNull(DateUtils.getDDMMYYYY((long) st)).getTime();
                            }
                            ed = st + (30 * 60000);
                        }

                        CalEventsTemplateModel.Duration _duration = _data.getDuration();

                        _duration.setStart(st);
                        _duration.setEnd(ed);
                        _data.setDuration(_duration);

                        reqData.add(_data);
                    }

                }//inner for
            }else{
                if(StringUtils.isNullOrEmpty(data.getReqTextToDisplay()) && Objects.requireNonNull(DateUtils.getDDMMYYYY(_start)).compareTo(DateUtils.getDDMMYYYY(_end)) == 0 && DateUtils.getOneDayMiliseconds(_end - _start) >= 23.98f) {
                    data.setReqTextToDisplay("All Day");
                    data.setReqTextToDisplayForDetails(DateUtils.getDayDate(_start));
                }

                reqData.add(data);
            }
        }
        return reqData;
    }

    public ArrayList<CalEventsTemplateModel> sortEventList(ArrayList<CalEventsTemplateModel> eventList) {

        ArrayList<CalEventsTemplateModel> newSortedData = new ArrayList<>();
        LinkedHashMap<String, ArrayList<CalEventsTemplateModel>> list = new LinkedHashMap<>();
        eventList.sort(new Comparator<CalEventsTemplateModel>() {
            public int compare(CalEventsTemplateModel o1, CalEventsTemplateModel o2) {
                try {
                    return Double.compare(o1.getDuration().getStart(), o2.getDuration().getStart());
                } catch (Exception e) {
                    e.printStackTrace();
                    return 0;
                }


            }
        });
        for (CalEventsTemplateModel data : eventList) {
//            String key = DateUtils.getDay((long)data.getDuration().getStart());
            String key = DateUtils.calendar_event_list_format1.format(data.getDuration().getStart()).toUpperCase();
            ArrayList<CalEventsTemplateModel> sortmap = list.get(key);
            if (sortmap == null) {
                ArrayList<CalEventsTemplateModel> temp = new ArrayList<>();
                data.setShowDate(true);
                temp.add(data);
                list.put(key, temp);

            } else {
                sortmap.add(data);
                list.put(key, sortmap);
            }
        }
        Set<String> keys = list.keySet();
        for (String k : keys) {
            newSortedData.addAll(Objects.requireNonNull(list.get(k)));
        }
        return newSortedData;
    }

    @Override
    public void setExpanded(boolean isExpanded) {
        this.isExpanded = isExpanded;
    }

    @Override
    public void setVerticalListViewActionHelper(VerticalListViewActionHelper verticalListViewActionHelper) {
        this.verticalListViewActionHelper = verticalListViewActionHelper;
    }

    public void setCursorDuration(CalEventsTemplateModel.Duration cursor) {
        _cursor = cursor;
    }

    public CalEventsTemplateModel.Duration getCursorDuration(){
        return _cursor;
    }


    public interface EventSelectionListener {
        void onEventSelected(String url);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView rowIndex;
        final TextView txtDateTime;
        final LinearLayout layoutDetails;
        final LinearLayout innerlayout;
        public final View sideBar;
        public final TextView txtTitle;
        public final TextView txtPlace;
        public final TextView tv_time;
        public final TextView tvborder;
        public final TextView tv_users;
        public final ImageView checkbox;
        public final View divider;
        public final TextView tv_meeting_notes;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtDateTime = itemView.findViewById(R.id.txtDateAndTime);
            layoutDetails = itemView.findViewById(R.id.layout_deails);
            innerlayout = itemView.findViewById(R.id.innerlayout);

            sideBar = itemView.findViewById(R.id.sideBar);

            txtTitle = itemView.findViewById(R.id.txtTitle);
            tv_time = itemView.findViewById(R.id.tv_time);
            txtPlace = itemView.findViewById(R.id.txtPlace);
            tvborder = itemView.findViewById(R.id.tvborder);
            tv_users = itemView.findViewById(R.id.tv_users);
            tv_meeting_notes = itemView.findViewById(R.id.tv_meeting_note);
            checkbox = itemView.findViewById(R.id.checkbox);
            divider = itemView.findViewById(R.id.divider);

        }
    }


    private CalEventsTemplateModel gModel;
    private final int CAL_PERMISSION_REQUEST = 3221;


    private void launchWebView(String htmlLink) {
        Intent intent = new Intent(mContext, GenericWebViewActivity.class);
        intent.putExtra("url", htmlLink);
        intent.putExtra("header", mContext.getResources().getString(R.string.app_name));
        mContext.startActivity(intent);
    }

    private String getFormatedAttendiesFromList(List<CalEventsTemplateModel.Attendee> userDetailModels) {
        String users = "";
        if (userDetailModels != null && userDetailModels.size() > 0) {
            if (userDetailModels.size() == 1) {

                return userDetailModels.get(0).getName() != null ? userDetailModels.get(0).getName() : userDetailModels.get(0).getEmail();
            } else {
                int remaining = userDetailModels.size() - 1;
                if (remaining > 1)
                    return String.format(Locale.US, "%1$s and %2$d others",
                            userDetailModels.get(0).getName() != null ? userDetailModels.get(0).getName() : userDetailModels.get(0).getEmail(), remaining);
                else
                    return String.format(Locale.US,"%1$s and %2$d other",
                            userDetailModels.get(0).getName() != null ? userDetailModels.get(0).getName() : userDetailModels.get(0).getEmail(), remaining);
            }
        }


        return users;

    }
}
