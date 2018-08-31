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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import kore.botssdk.R;
import kore.botssdk.activity.GenericWebViewActivity;
import kore.botssdk.models.CalEventsTemplateModel;
import kore.botssdk.utils.AppPermissionsHelper;
import kore.botssdk.utils.DateUtils;
import kore.botssdk.utils.KaFontUtils;

/**
 * Created by Ramachandra Pradeep on 02-Aug-18.
 */

public class CalendarEventsAdapter extends BaseAdapter {
    public ArrayList<CalEventsTemplateModel> getEventList() {
        return eventList;
    }

    public void setEventList(ArrayList<CalEventsTemplateModel> eventList) {
        if(eventList != null) {
            this.eventList.clear();
            this.eventList.addAll(eventList);
        }
    }

    ArrayList<CalEventsTemplateModel> eventList = new ArrayList<>();
    private LayoutInflater inflater = null;
    private int EVENTS_LIST_LIMIT = 3;
    private static String title = "SHOW MORE";
    private EventSelectionListener eventSelectionListener;
    private Context mContext;

    public CalendarEventsAdapter(Context mContext) {
        this.mContext = mContext;
        inflater = LayoutInflater.from(mContext);
//        EVENTS_LIST_LIMIT = 3;
//        title = "SHOW MORE";
    }

    /*private boolean showMore() {
        if (eventList != null && !eventList.isEmpty()) {
            return (eventList.size() > EVENTS_LIST_LIMIT) ? true : false;
        }
        return false;
    }*/

    @Override
    public int getCount() {
        int val;
        if(eventList == null || eventList.isEmpty()) val = 0;
        else if(eventList.size() <= 3) val =  eventList.size();
        else val =  (EVENTS_LIST_LIMIT + 1);
        return val;
    }

    @Override
    public CalEventsTemplateModel getItem(int position) {
        if(position < eventList.size())
            return eventList.get(position);
        else return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

//        CalEventsTemplateModel event = getItem(position);
//        if (event != null) {

            if (position == EVENTS_LIST_LIMIT) {
                convertView = getShowMoreView(convertView, parent);
            } else {
                convertView = getOptionsView(convertView, position);
            }

//        }

        return convertView;
    }

    public interface EventSelectionListener {
        void onEventSelected(String url);
    }

    public void setMoreSelectionListener(EventSelectionListener eventSelectionListener) {
        this.eventSelectionListener = eventSelectionListener;
    }

    public static class ViewHolder {
        public TextView rowIndex;
        public TextView txtDateTime;
        public LinearLayout layoutDetails;
        public View sideBar;
        public TextView txtTitle;
        public TextView txtPlace;
    }

    private CalEventsTemplateModel gModel;
    private final int CAL_PERMISSION_REQUEST = 3221;
    private View getOptionsView(View convertView, int position) {
        View vi = convertView;
        ViewHolder holder;
        if (convertView == null || convertView.getTag() == null) {
            vi = inflater.inflate(R.layout.calendar_event_list_item, null);
            holder = new ViewHolder();
            holder.rowIndex = (TextView) vi.findViewById(R.id.btnRowIndex);
            holder.txtDateTime = (TextView) vi.findViewById(R.id.txtDateAndTime);
            holder.layoutDetails = (LinearLayout) vi.findViewById(R.id.layout_deails);
            holder.sideBar = vi.findViewById(R.id.sideBar);
            holder.txtTitle = (TextView) vi.findViewById(R.id.txtTitle);
            holder.txtPlace = (TextView) vi.findViewById(R.id.txtPlace);
            KaFontUtils.applyCustomFont(mContext,vi);
            vi.setTag(holder);
        } else
            holder = (ViewHolder) vi.getTag();

        if (eventList == null || eventList.size() <= 0) {
            holder.txtTitle.setText("No Data");
        } else {
            final CalEventsTemplateModel model = (CalEventsTemplateModel) eventList.get(position);
            holder.rowIndex.setText("" + (position + 1));
            holder.txtDateTime.setText(DateUtils.calendar_list_format.format(model.getDuration().getStart()) +" - "+DateUtils.calendar_list_format_2.format(model.getDuration().getEnd()));
            holder.txtTitle.setText(model.getTitle());
            holder.txtPlace.setText(model.getWhere());
            holder.sideBar.setBackgroundColor(Color.parseColor(model.getColor()));
            holder.layoutDetails.setBackgroundColor((Color.parseColor(model.getColor()) & 0x00ffffff) | (26 << 24));
            holder.layoutDetails.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try{
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (AppPermissionsHelper.hasPermission(mContext, Manifest.permission.READ_CALENDAR)) {
                                launchNativeView(model.getTitle(), (long) model.getDuration().getStart());
                            } else {
                                gModel = model;
                                AppPermissionsHelper.requestForPermission((Activity) mContext, Manifest.permission.READ_CALENDAR, CAL_PERMISSION_REQUEST);
                            }
                        }else{
                            launchNativeView(model.getTitle(), (long) model.getDuration().getStart());
                        }

                    }catch (Exception e){
                        launchWebView(model.getHtmlLink());
                    }
//                    int eventId = getEventId(model.getTitle(),(long)model.getDuration().getStart(),(long)model.getDuration().getEnd());
                    /*Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("content://com.android.calendar/events/" + String.valueOf(id)));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                            | Intent.FLAG_ACTIVITY_SINGLE_TOP
                            | Intent.FLAG_ACTIVITY_CLEAR_TOP
                            | Intent.FLAG_ACTIVITY_NO_HISTORY
                            | Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                    intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, model.getDuration().getStart());
                    intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, model.getDuration().getEnd());
                    mContext.startActivity(intent);*/

                }
            });
        }
        return vi;
    }


    private void launchNativeView(String title, long beginTime) throws Exception{
        int id = listSelectedCalendars(title);
        if(id <= 0) throw new Exception("Invalid event id");
       /* Uri uri = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, id);
        Intent intent = new Intent(Intent.ACTION_VIEW).setData(uri);*/

        /*Calendar mCalendar = new GregorianCalendar();
        TimeZone mTimeZone = mCalendar.getTimeZone();
        int mGMTOffset = mTimeZone.getRawOffset();*/

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("content://com.android.calendar/events/" + String.valueOf(id)));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_SINGLE_TOP
                | Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_NO_HISTORY
                | Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, beginTime);
        mContext.startActivity(intent);
    }

    private void launchWebView(String htmlLink){
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

    private int listSelectedCalendars(String eventtitle) {


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

        Cursor lcursor = contentResolver.query(Uri.parse("content://com.android.calendar/events"),
                new String[]{ "calendar_id", "title", "description", "dtstart", "dtend", "eventLocation" },
                null, null, null);


        // Create a set containing all of the calendar IDs available on the phone
//        HashSet<String> calendarIds = CalendarService.getCalenderIds(lcursor);

//        for(String id:calendarIds) {
            String projection[] = {"_id", "title", CalendarContract.Events.DTSTART, CalendarContract.Events.DTEND};
            Cursor cursor = mContext.getContentResolver().query(eventUri, null, null,
                    null, null);

            if (cursor.moveToFirst()) {

                String calName;
                String calID;
//                String startTime;
//                String endTime;

                int nameCol = cursor.getColumnIndex(projection[1]);
                int idCol = cursor.getColumnIndex(projection[0]);
//                int startTimeCol = cursor.getColumnIndex(projection[2]);
//                int endTimeCol = cursor.getColumnIndex(projection[3]);
                do {
                    calName = cursor.getString(nameCol);
                    calID = cursor.getString(idCol);
//                    startTime = cursor.getString(startTimeCol);
//                    endTime = cursor.getString(endTimeCol);

                    if (calName != null && calName.equalsIgnoreCase(eventtitle)) {
//                        boolean val = (Long.parseLong(startTime) == sTime && Long.parseLong(endTime) == eTime);
                        return result = Integer.parseInt(calID);
                    }

                } while (cursor.moveToNext());
                cursor.close();
            }
//        }

        return result;

    }

    private int getEventId(String title, long startTime, long endTime){
        final String[] INSTANCE_PROJECTION = new String[] {
                CalendarContract.Instances.EVENT_ID,      // 0
                CalendarContract.Instances.BEGIN,         // 1
                CalendarContract.Instances.TITLE          // 2
        };

// The indices for the projection array above.
        final int PROJECTION_ID_INDEX = 0;
        final int PROJECTION_BEGIN_INDEX = 1;
        final int PROJECTION_TITLE_INDEX = 2;

// Specify the date range you want to search for recurring
// event instances

        Calendar mCalendar = new GregorianCalendar();
        TimeZone mTimeZone = mCalendar.getTimeZone();
        int mGMTOffset = mTimeZone.getRawOffset();

        int eventId = 0;
        long startMillis = startTime+mGMTOffset;
        long endMillis = endTime+mGMTOffset;

        Cursor cur = null;
        ContentResolver cr = mContext.getContentResolver();

// The ID of the recurring event whose instances you are searching
// for in the Instances table
        String selection = CalendarContract.Instances.EVENT_ID + " = ?";
        String[] selectionArgs = new String[] {"207"};

// Construct the query with the desired date range.
        Uri.Builder builder = CalendarContract.Instances.CONTENT_URI.buildUpon();
        ContentUris.appendId(builder, startMillis);
        ContentUris.appendId(builder, endMillis);

// Submit the query
        cur =  cr.query(builder.build(),
                INSTANCE_PROJECTION,
                selection,
                selectionArgs,
                null);

        while (cur.moveToNext()) {
//            String title = null;
            long eventID = 0;
            long beginVal = 0;

            // Get the field values
            eventID = cur.getLong(PROJECTION_ID_INDEX);
            beginVal = cur.getLong(PROJECTION_BEGIN_INDEX);
            title = cur.getString(PROJECTION_TITLE_INDEX);

        }
        return eventId;
    }


    private View getShowMoreView(View convertView, ViewGroup parent) {

        convertView = inflater.inflate(R.layout.cal_event_show_more, parent, false);
        LinearLayout container = (LinearLayout) convertView.findViewById(R.id.event_options_more);
        final TextView moreTextView = (TextView) convertView.findViewById(R.id.events_more_txt_view);
        moreTextView.setText(title);
        KaFontUtils.applyCustomFont(mContext,convertView);

        /*container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (moreSelectionListener != null)
                    moreSelectionListener.onMoreSelected();
            }
        });*/
        moreTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(eventList != null) {
                    if (moreTextView.getText().equals("SHOW MORE")) {
                        if (eventList.size() > EVENTS_LIST_LIMIT) {
                            if (eventList.size() - EVENTS_LIST_LIMIT > 3)
                                EVENTS_LIST_LIMIT += 3;

                            else if (EVENTS_LIST_LIMIT < 12) {
                                EVENTS_LIST_LIMIT = EVENTS_LIST_LIMIT + (eventList.size() - EVENTS_LIST_LIMIT);
                                title = "SHOW LESS";
                            }
                            if(EVENTS_LIST_LIMIT >= 12)
                                title = "SHOW LESS";
                            notifyDataSetChanged();
                        }
                    }else if(moreTextView.getText().equals("SHOW LESS")){
                        EVENTS_LIST_LIMIT = 3;
                        title = "SHOW MORE";
                        notifyDataSetChanged();
                    }
                }
            }
        });
        convertView.setTag(null);

        return convertView;
    }

    private boolean debug = true;
    /**
     * Extracts the ID and calendar email from the eid parameter of a URI.
     *
     * The URI contains an "eid" parameter, which is comprised of an ID, followed
     * by a space, followed by the calendar email address. The domain is sometimes
     * shortened. See the switch statement. This is Base64-encoded before being
     * added to the URI.
     *
     * @param uri incoming request
     * @return the decoded event ID and calendar email
     */

    private String[] extractEidAndEmail(Uri uri) {
        try {
            String eidParam = uri.getQueryParameter("eid");
            if (eidParam == null) {
                return null;
            }

            byte[] decodedBytes = Base64.decode(eidParam, Base64.DEFAULT);
//            if (debug) Log.d(TAG, "decoded eid=" + new String(decodedBytes) );

            for (int spacePosn = 0; spacePosn < decodedBytes.length; spacePosn++) {
                if (decodedBytes[spacePosn] == ' ') {
                    int emailLen = decodedBytes.length - spacePosn - 1;
                    if (spacePosn == 0 || emailLen < 3) {
                        break;
                    }

                    String domain = null;
                    if (decodedBytes[decodedBytes.length - 2] == '@') {
                        // Drop the special one character domain
                        emailLen--;

                        switch(decodedBytes[decodedBytes.length - 1]) {
                            case 'm':
                                domain = "gmail.com";
                                break;
                            case 'g':
                                domain = "group.calendar.google.com";
                                break;
                            case 'h':
                                domain = "holiday.calendar.google.com";
                                break;
                            case 'i':
                                domain = "import.calendar.google.com";
                                break;
                            case 'v':
                                domain = "group.v.calendar.google.com";
                                break;
                            default:
                                Log.wtf("LOG", "Unexpected one letter domain: "
                                        + decodedBytes[decodedBytes.length - 1]);
                                // Add sql wild card char to handle new cases
                                // that we don't know about.
                                domain = "%";
                                break;
                        }
                    }

                    String eid = new String(decodedBytes, 0, spacePosn);
                    String email = new String(decodedBytes, spacePosn + 1, emailLen);
//                    if (debug) Log.d(TAG, "eid=   " + eid );
//                    if (debug) Log.d(TAG, "email= " + email );
//                    if (debug) Log.d(TAG, "domain=" + domain );
                    if (domain != null) {
                        email += domain;
                    }

                    return new String[] { eid, email };
                }
            }
        } catch (RuntimeException e) {
//            Log.w(TAG, "Punting malformed URI " + uri);
        }
        return null;
    }
}
