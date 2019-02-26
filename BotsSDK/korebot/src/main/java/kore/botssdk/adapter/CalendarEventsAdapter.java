package kore.botssdk.adapter;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.provider.CalendarContract;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import io.reactivex.internal.fuseable.HasUpstreamObservableSource;
import kore.botssdk.R;
import kore.botssdk.activity.GenericWebViewActivity;
import kore.botssdk.fragment.ComposeFooterFragment;
import kore.botssdk.listener.ComposeFooterInterface;
import kore.botssdk.listener.RecyclerViewDataAccessor;
import kore.botssdk.listener.VerticalListViewActionHelper;
import kore.botssdk.models.BotResponse;
import kore.botssdk.models.CalEventsTemplateModel;
import kore.botssdk.models.MeetingConfirmationModel;
import kore.botssdk.models.PayloadInner;
import kore.botssdk.utils.AppPermissionsHelper;
import kore.botssdk.utils.DateUtils;
import kore.botssdk.utils.KaFontUtils;
import kore.botssdk.utils.StringUtils;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static kore.botssdk.utils.DateUtils.getDateinDayFormat;
import static kore.botssdk.utils.DateUtils.getTimeInAmPm;

/**
 * Created by Ramachandra Pradeep on 02-Aug-18.
 */

public class CalendarEventsAdapter extends RecyclerView.Adapter<CalendarEventsAdapter.ViewHolder> implements RecyclerViewDataAccessor {
    private boolean isExpanded = false;
    VerticalListViewActionHelper verticalListViewActionHelper;

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
    private int EVENTS_LIST_LIMIT = 3;
    private String title = "SHOW MORE";
    private EventSelectionListener eventSelectionListener;
    private Context mContext;
    private String dateLast="";
    private String type;
    private boolean isEnabled;
    private ComposeFooterInterface composeFooterInterface;
    private Gson gson = new Gson();

    public CalendarEventsAdapter(Context mContext, String type, boolean isEnabled) {
        this.mContext = mContext;
        inflater = LayoutInflater.from(mContext);
        this.type = type;
        this.isEnabled = isEnabled;

//        EVENTS_LIST_LIMIT = 3;
//        title = "SHOW MORE";
    }


    public CalEventsTemplateModel getItem(int position) {
        if (position < eventList.size())
            return eventList.get(position);
        else return null;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.calendar_event_list_item, null));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final CalEventsTemplateModel model = (CalEventsTemplateModel) eventList.get(position);
        //  holder.rowIndex.setText("" + (position + 1));
        String date=DateUtils.calendar_event_list_format1.format(model.getDuration().getStart()).toUpperCase();
        holder.txtDateTime.setText(date);


        holder.txtTitle.setText(model.getTitle());
       // holder.txtPlace.setText(model.getWhere());
        if (!StringUtils.isNullOrEmptyWithTrim(model.getWhere())) {
            holder.txtPlace.setText(model.getWhere());
            holder.txtPlace.setVisibility(VISIBLE);
            holder.lin_cal_loc.setVisibility(VISIBLE);
        } else {
            holder.txtPlace.setVisibility(GONE);
            holder.lin_cal_loc.setVisibility(GONE);
        }
        holder.tv_time.setText(DateUtils.calendar_list_format_2.format(model.getDuration().getStart()) + "\n" + DateUtils.calendar_list_format_2.format(model.getDuration().getEnd()));

        holder.tv_users.setText(getFormatedAttendiesFromList(model.getAttendees()));
        if (position == 0 || !date.equalsIgnoreCase(dateLast)) {
            holder.tvborder.setVisibility(VISIBLE);
            holder.txtDateTime.setVisibility(VISIBLE);
        } else {
            holder.tvborder.setVisibility(GONE);
            holder.txtDateTime.setVisibility(GONE);
        }
        dateLast=date;
        holder.sideBar.setBackgroundColor(Color.parseColor(model.getColor()));
        //  holder.layoutDetails.setBackgroundColor((Color.parseColor(model.getColor()) & 0x00ffffff) | (26 << 24));
        holder.layoutDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (BotResponse.TEMPLATE_TYPE_CAL_EVENTS.equalsIgnoreCase(type)) {
                    try {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (AppPermissionsHelper.hasPermission(mContext, Manifest.permission.READ_CALENDAR)) {
                                launchNativeView(model.getTitle(), (long) model.getDuration().getStart());
                            } else {
                                gModel = model;
                                AppPermissionsHelper.requestForPermission((Activity) mContext, Manifest.permission.READ_CALENDAR, CAL_PERMISSION_REQUEST);
                            }
                        } else {
                            launchNativeView(model.getTitle(), (long) model.getDuration().getStart());
                        }

                    } catch (Exception e) {
                        launchWebView(model.getHtmlLink());
                    }

                } else if (isEnabled) {

                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put("meetingId", model.getEventId());
                    String message = "Cancel \"" + model.getTitle() + "\" " + getDateinDayFormat((long) model.getDuration().getStart()) + ", " + getTimeInAmPm((long) model.getDuration().getStart()) + " - " + getTimeInAmPm((long) model.getDuration().getEnd());
                    composeFooterInterface.sendWithSomeDelay(message, gson.toJson(hashMap), 0);
                }
            }
        });
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return eventList != null ? (!isExpanded && eventList.size() > 3 ? 3 : eventList.size()) : 0;
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

    }

    @Override
    public void setExpanded(boolean isExpanded) {
        this.isExpanded = isExpanded;
    }

    @Override
    public void setVerticalListViewActionHelper(VerticalListViewActionHelper verticalListViewActionHelper) {
        this.verticalListViewActionHelper = verticalListViewActionHelper;

    }

    public interface EventSelectionListener {
        void onEventSelected(String url);
    }

    public void setMoreSelectionListener(EventSelectionListener eventSelectionListener) {
        this.eventSelectionListener = eventSelectionListener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView rowIndex;
        TextView txtDateTime;
        LinearLayout layoutDetails,lin_cal_loc;
        public View sideBar;
        public TextView txtTitle;
        public TextView txtPlace;
        public TextView tv_time;
        public TextView tvborder, tv_users;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtDateTime = (TextView) itemView.findViewById(R.id.txtDateAndTime);
            layoutDetails = (LinearLayout) itemView.findViewById(R.id.layout_deails);
            sideBar = itemView.findViewById(R.id.sideBar);

            txtTitle = (TextView) itemView.findViewById(R.id.txtTitle);
            tv_time = (TextView) itemView.findViewById(R.id.tv_time);
            txtPlace = (TextView) itemView.findViewById(R.id.txtPlace);
            tvborder = (TextView) itemView.findViewById(R.id.tvborder);
            tv_users = (TextView) itemView.findViewById(R.id.tv_users);
            lin_cal_loc = (LinearLayout) itemView.findViewById(R.id.lin_cal_loc);

        }
    }


    private CalEventsTemplateModel gModel;
    private final int CAL_PERMISSION_REQUEST = 3221;


    private void launchNativeView(String title, long beginTime) throws Exception {
        int id = listSelectedCalendars(title, beginTime);
        if (id <= 0) throw new Exception("Invalid event id");

        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("content://com.android.calendar/events/" + String.valueOf(id)));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_SINGLE_TOP
                    | Intent.FLAG_ACTIVITY_CLEAR_TOP
                    | Intent.FLAG_ACTIVITY_NO_HISTORY
                    | Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
            intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, beginTime);
            mContext.startActivity(intent);
        }catch (Exception e){
            throw new Exception("Invalid event id");
        }
    }

    private void launchWebView(String htmlLink) {
        Intent intent = new Intent(mContext, GenericWebViewActivity.class);
        intent.putExtra("url", htmlLink);
        intent.putExtra("header", mContext.getResources().getString(R.string.app_name));
        mContext.startActivity(intent);
    }

    private static final int EVENT_INDEX_ID = 0;
    private static final int EVENT_INDEX_START = 1;
    private static final int EVENT_INDEX_END = 2;
    private static final int EVENT_INDEX_DURATION = 3;

    private static final String[] EVENT_PROJECTION = new String[]{
            CalendarContract.Events._ID,      // 0
            CalendarContract.Events.DTSTART,  // 1
            CalendarContract.Events.DTEND,    // 2
            CalendarContract.Events.DURATION, // 3
    };

    private String getFormatedAttendiesFromList(List<CalEventsTemplateModel.Attendee> userDetailModels) {
        String users = "";
        if (userDetailModels != null && userDetailModels.size() > 0) {
            if (userDetailModels.size() == 1) {

                return userDetailModels.get(0).getName() != null ? userDetailModels.get(0).getName() : userDetailModels.get(0).getEmail();
            } else if (userDetailModels.size() == 2) {

                return String.format("%1$s and %2$s",
                        userDetailModels.get(0).getName() != null ? userDetailModels.get(0).getName() : userDetailModels.get(0).getEmail(),
                        userDetailModels.get(1).getName() != null ? userDetailModels.get(1).getName() : userDetailModels.get(1).getEmail());
            } else if (userDetailModels.size() == 3) {

                return String.format("%1$s , %2$s and %3$s",
                        userDetailModels.get(0).getName() != null ? userDetailModels.get(0).getName() : userDetailModels.get(0).getEmail(),
                        userDetailModels.get(1).getName() != null ? userDetailModels.get(1).getName() : userDetailModels.get(1).getEmail(),
                        userDetailModels.get(2).getName() != null ? userDetailModels.get(2).getName() : userDetailModels.get(2).getEmail());
            } else {
                int remaining = userDetailModels.size() - 3;
                return String.format("%1$s , %2$s , %3$s and %4$d others",
                        userDetailModels.get(0).getName() != null ? userDetailModels.get(0).getName() : userDetailModels.get(0).getEmail(),
                        userDetailModels.get(1).getName() != null ? userDetailModels.get(1).getName() : userDetailModels.get(1).getEmail(),
                        userDetailModels.get(2).getName() != null ? userDetailModels.get(2).getName() : userDetailModels.get(2).getEmail(), remaining);
            }
        }


        return users;

    }
    /*private void openCalendar(Uri uri) {
        String[] eidParts = extractEidAndEmail(uri);
        *//*if (uri != null) {
            String[] eidParts = extractEidAndEmail(uri);
            if (debug) Log.d(TAG, "eidParts=" + eidParts);
            String eid = uri.getQueryParameter("eid");
            if (eid != null) {
                ContentResolver cr = mContext.getContentResolver();
                final String selection = CalendarContract.Events._SYNC_ID + " LIKE \"%" + eidParts[0]
                        + "\" AND " + CalendarContract.Calendars.OWNER_ACCOUNT + " LIKE \"" + eidParts[1] + "\"";

                if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                Cursor eventCursor = cr.query(CalendarContract.Events.CONTENT_URI, EVENT_PROJECTION,
                        null, null, null);
                if(eventCursor != null){
                    Log.d("IKIDO",""+eventCursor.getCount());
                }
            }


        }*//*
        Log.d(TAG, "eidParts=" + eidParts[0]);
        CalendarService.readCalendar(mContext,218,0);
    }*/

    private int listSelectedCalendars(String eventtitle, long beginTime) {


        Uri eventUri;
        if (android.os.Build.VERSION.SDK_INT <= 7) {
            // the old way

            eventUri = Uri.parse("content://calendar/events");
        } else {
            // the new way

            eventUri = Uri.parse("content://com.android.calendar/events");
        }

        int result = 0;
        ContentResolver contentResolver = mContext.getContentResolver();

        /*// Create a cursor and read from the calendar (for Android API below 4.0)
        final Cursor cursor = contentResolver.query(Uri.parse("content://com.android.calendar/calendars"),
                (new String[] { "_id", "displayName", "selected" }), null, null, null);*/

     /*   Cursor lcursor = contentResolver.query(eventUri,
                new String[]{ "calendar_id", "title", "description", "dtstart", "dtend", "eventLocation" },
                null, null, null);

*/
        // Create a set containing all of the calendar IDs available on the phone
//        HashSet<String> calendarIds = CalendarService.getCalenderIds(lcursor);

//        for(String id:calendarIds) {
        String projection[] = {"_id", "title", CalendarContract.Events.DTSTART, CalendarContract.Events.DTEND};
        Cursor cursor = mContext.getContentResolver().query(eventUri, null, null,
                null, null);

        if (cursor.moveToFirst()) {

            String calName;
            String calID;
            String startTime;
            //    String endTime;

            int nameCol = cursor.getColumnIndex(projection[1]);
            int idCol = cursor.getColumnIndex(projection[0]);
            int startTimeCol = cursor.getColumnIndex(projection[2]);
            //     int endTimeCol = cursor.getColumnIndex(projection[3]);
            do {
                calName = cursor.getString(nameCol);
                calID = cursor.getString(idCol);
                startTime = cursor.getString(startTimeCol);
                //   endTime = cursor.getString(endTimeCol);

                if (calName != null && calName.equals(eventtitle)) {
                    Log.d("HI","Hello");
//                        boolean val = (Long.parseLong(startTime) == sTime && Long.parseLong(endTime) == eTime);
                    return Integer.parseInt(calID);
                } else if (Long.parseLong(startTime) == beginTime) {
                    result = Integer.parseInt(calID);
                }

            } while (cursor.moveToNext());
            cursor.close();
        }
//        }

        return result;

    }

}
