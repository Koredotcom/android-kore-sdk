package kore.botssdk.audiocodes.webrtcclient.Adapters;


import static kore.botssdk.audiocodes.webrtcclient.db.NativeDBManager.QueryType.BY_PHONE_AND_SIP;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import kore.botssdk.R;
import kore.botssdk.audiocodes.webrtcclient.General.ImageUtils;
import kore.botssdk.audiocodes.webrtcclient.Permissions.PermissionManager;
import kore.botssdk.audiocodes.webrtcclient.Permissions.PermissionManagerType;
import kore.botssdk.audiocodes.webrtcclient.Structure.CallEntry;
import kore.botssdk.audiocodes.webrtcclient.Structure.ContactListObject;
import kore.botssdk.audiocodes.webrtcclient.db.NativeDBManager;
import kore.botssdk.audiocodes.webrtcclient.db.NativeDBObject;


public class RecentListAdapter extends ArrayAdapter<CallEntry> {

    private final String TAG = "RecentListAdapter";

    private List<CallEntry> callEntryList;
    protected ListOnItemClickListener onCreateItemClickListener;
    protected ListOnItemClickListener callButtonClickListener;

    private Context context;

    private class ViewHolder {
        TextView displayName;
        TextView phoneNumber;
        ImageView callType;
        TextView startDate;
        TextView duration;
        ImageView contactImage;
        View buttonClickView;
        //View lineClickView;
    }

    public RecentListAdapter(Activity context, List<CallEntry> callEntryList) {

        super(context, R.layout.recents_list_row, callEntryList);

        this.context = context;
        this.callEntryList = callEntryList;
    }

    public List<CallEntry> getCallEntryList() {
        return callEntryList;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        //if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.recents_list_row, null, false);
            ViewHolder tempHolder = new ViewHolder();

            TextView displayName;
            TextView phoneNumber;
            TextView callType;
            TextView startDate;
            TextView duration;
            ImageView contactImage;
            View buttonClickView;

            tempHolder.displayName = (TextView) convertView.findViewById(R.id.recent_list_row_editText_name);
            tempHolder.phoneNumber = (TextView) convertView.findViewById(R.id.recent_list_row_editText_phonenumber);
            tempHolder.callType = (ImageView) convertView.findViewById(R.id.recent_list_row_imageview_call_type);
            tempHolder.startDate = (TextView) convertView.findViewById(R.id.recent_list_row_editText_call_time);
            tempHolder.duration = (TextView) convertView.findViewById(R.id.recent_list_row_editText_duration);
            tempHolder.contactImage = (ImageView) convertView.findViewById(R.id.recent_list_row_imageview_contact);
            tempHolder.buttonClickView = (View) convertView.findViewById(R.id.recent_list_row_layout_video);


            convertView.setTag(tempHolder);
        //}

        ViewHolder holder = (ViewHolder) convertView.getTag();
        CallEntry callEntry = callEntryList.get(position);

        holder.displayName.setText(callEntry.getContactName());
        holder.phoneNumber.setText(callEntry.getContactNumber());

        int callTypeid = R.mipmap.recent_missed;
        switch (callEntry.getCallType()) {
            case INCOMING:
                callTypeid = R.mipmap.recent_incoming;
                break;
            case OUTGOING:
                callTypeid = R.mipmap.recent_outgoing;
                break;
            case MISSED:
                callTypeid = R.mipmap.recent_missed;
                break;

        }
        holder.callType.setImageResource(callTypeid);


        holder.startDate.setText(buildCallDate(callEntry.getStartTime()));

        holder.duration.setText(buildCallDuration(callEntry.getDuration()));

        //List<NativeContactObject> nativeContactObjectList = NativeContactUtils.getContactListByPhoneNumber(callEntry.getContactNumber());
        List<NativeDBObject> nativeDBObjectList = NativeDBManager.getContactList(BY_PHONE_AND_SIP, callEntry.getContactNumber());


        if(nativeDBObjectList!=null && nativeDBObjectList.size()>0)
        {
            NativeDBObject nativeDBObject = nativeDBObjectList.get(0);
            ContactListObject contactListObject =new ContactListObject(nativeDBObject.getDisplayName(), callEntry.getContactNumber(), nativeDBObject.getPhotoURI(), nativeDBObject.getPhotoThumbnailURI());
                                               //new ContactListObject(nativeDBObject.getDisplayName(),nativeDBPhones.getPhoneNumber(),nativeDBObject.getPhotoURI(),nativeDBObject.getPhotoThumbnailURI()));


            if (contactListObject.getPhotoThumbnailURI() != null) {
                Bitmap photoBitmap = ImageUtils.getContactBitmapFromURI(context, Uri.parse(contactListObject.getPhotoThumbnailURI()));
                if (photoBitmap != null) {

                    Bitmap roundPhotoBitmap = ImageUtils.getCroppedRoundBitmap(photoBitmap, holder.contactImage.getDrawable().getIntrinsicHeight());
                    //Bitmap photoBitmap = BitmapFactory.decodeByteArray(photo, 0, photo.length);
                    holder.contactImage.setImageBitmap(roundPhotoBitmap);
                }
            }
            if (contactListObject.getName() != null) {
                holder.displayName.setText(contactListObject.getName());
            }

        }

        //holder.contactImage.setImageResource(R.drawable.default_contact_list_picture);

        holder.buttonClickView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                ((ViewGroup)view.getParent()).setSelected(true);
                if (callButtonClickListener != null) {
                    callButtonClickListener.onItemClick(null, view, position, 0);
                }

                //((ViewGroup)view.getParent()).dispatchKeyEvent(new KeyEvent(KeyEvent.KEYCODE_F7, ));
            }
        });

        convertView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                ((ViewGroup)view.getParent()).setSelected(true);
                boolean contactPermission = PermissionManager.getInstance().checkPermission(PermissionManagerType.CONTACTS);
                if (contactPermission && onCreateItemClickListener != null) {
                    onCreateItemClickListener.onItemClick(null, view, position, 0);
                } else {
                    Log.e(TAG, "Contact permission disabled or null");
                }
            }
        });

//        holder.buttonClickView.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View view) {
//                if (callButtonClickListener != null) {
//                    callButtonClickListener.onItemClick(null, view, position, 0);
//                }
//                return false;
//            }
//        });

        return convertView;

    }



    private String buildCallDate(long callDuration)
    {
        String resCallDate;
        if(DateUtils.isToday(callDuration))
        {
            resCallDate=context.getString(R.string.gen_today);
        }
        else {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yy");
            resCallDate = simpleDateFormat.format(new Date(callDuration));
        }
        return resCallDate;
    }


    private String buildCallDuration(long callDuration)
    {
        StringBuilder callDurationBuilder = new StringBuilder();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(callDuration));
        Date callDurationDate = new Date(callDuration);
        int hoursInt = calendar.get(Calendar.HOUR_OF_DAY)-2;
        int minutesInt = calendar.get(Calendar.MINUTE);
        int secondsInt = calendar.get(Calendar.SECOND);
        if(hoursInt-2>0)
        {
            callDurationBuilder.append(" "+String.valueOf(hoursInt-2)+context.getString(R.string.gen_hours_short));
        }
        if(minutesInt>0)
        {
            callDurationBuilder.append(" "+String.valueOf(minutesInt)+context.getString(R.string.gen_minutes_short));
        }
        if(secondsInt>0)
        {
            callDurationBuilder.append(" "+String.valueOf(secondsInt)+context.getString(R.string.gen_seconds_short));
        }

        return callDurationBuilder.toString();
    }

    public interface ListOnItemClickListener {
        public void onItemClick(AdapterView<?> arg0, View v, int position, long id);
    }

    public interface CallButtonClickListener {
        public void onItemClick(AdapterView<?> arg0, View v, int position, long id);
    }

    public void setonCreateItemClickListener(ListOnItemClickListener listener) {
        onCreateItemClickListener = listener;
    }

    public void setCallButtonClickListener(ListOnItemClickListener listener) {
        callButtonClickListener = listener;
    }

}