package kore.botssdk.audiocodes.webrtcclient.Adapters;


import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import kore.botssdk.R;
import kore.botssdk.audiocodes.webrtcclient.General.ImageUtils;
import kore.botssdk.audiocodes.webrtcclient.Permissions.PermissionManager;
import kore.botssdk.audiocodes.webrtcclient.Permissions.PermissionManagerType;
import kore.botssdk.audiocodes.webrtcclient.Structure.ContactListObject;

public class ContactListAdapter extends BaseAdapter {

    private final String TAG = "ContactListAdapter";

    private List<ContactListObject> contactListObjectList;
    protected ListOnItemClickListener onCreateItemClickListener;
    protected ListOnItemClickListener callButtonClickListener;

    private Context context;

    private class ViewHolder
    {
        TextView name;
        TextView number;
        ImageView image;
        View buttonClickView;
        //View lineClickView;
    }
    public ContactListAdapter(Context context) {
        this.context = context;
    }

    public ContactListAdapter(Context context, List<ContactListObject> listItem) {
        this.context = context;
        contactListObjectList = listItem;
    }

    public List<ContactListObject> getList() {
        return contactListObjectList;
    }

    public void setList(List<ContactListObject> list) {
        this.contactListObjectList = list;
    }

    @Override
    public int getCount() {
        return contactListObjectList.size();
    }

    @Override
    public Object getItem(int position) {
        return contactListObjectList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //if (convertView==null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.contact_list_row, null, false);
            ViewHolder tempHolder = new ViewHolder();
            tempHolder.name = (TextView) convertView.findViewById(R.id.contact_list_row_editText_name);
            tempHolder.number = (TextView) convertView.findViewById(R.id.contact_list_row_editText_phonenumber);
            tempHolder.image = (ImageView) convertView.findViewById(R.id.contact_list_row_imageview_contact);
            tempHolder.buttonClickView = (View) convertView.findViewById(R.id.contact_list_row_layout_video);
            //tempHolder.lineClickView = (View) convertView.findViewById(R.id.contact_list_row_layout_contact_info);


            convertView.setTag(tempHolder);
        //}
        ViewHolder holder = (ViewHolder) convertView.getTag();
        ContactListObject contactListObject = contactListObjectList.get(position);

        holder.name.setText(contactListObject.getName());
        holder.number.setText(contactListObject.getNumber());


        if (contactListObject.getPhotoThumbnailURI() != null) {
            Bitmap photoBitmap = ImageUtils.getContactBitmapFromURI(context, Uri.parse(contactListObject.getPhotoThumbnailURI()));
            if (photoBitmap != null) {

                Bitmap roundPhotoBitmap = ImageUtils.getCroppedRoundBitmap(photoBitmap, holder.image.getDrawable().getIntrinsicHeight());
                //Bitmap photoBitmap = BitmapFactory.decodeByteArray(photo, 0, photo.length);
                holder.image.setImageBitmap(roundPhotoBitmap);
            }
        }

        holder.buttonClickView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (callButtonClickListener != null) {
                    callButtonClickListener.onItemClick(null, view, position, 0);
                }
            }
        });

        convertView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                boolean contactPermission = PermissionManager.getInstance().checkPermission(PermissionManagerType.CONTACTS);
                if (contactPermission && onCreateItemClickListener != null) {
                    onCreateItemClickListener.onItemClick(null, view, position, 0);
                } else {
                    Log.e(TAG, "Contact permission disabled or null");
                }
            }
        });
        return convertView;
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