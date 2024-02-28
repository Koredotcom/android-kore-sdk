package kore.botssdk.audiocodes.webrtcclient.db;


import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.SipAddress;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kore.botssdk.application.BotApplication;
import kore.botssdk.audiocodes.webrtcclient.Permissions.PermissionManager;
import kore.botssdk.audiocodes.webrtcclient.Permissions.PermissionManagerType;

public class NativeDBManager {

    private static final String TAG = "NativeDBManager";

    public enum QueryType {
        ALL,
        BY_NAME,
        BY_PHONE,
        BY_SIP,
        BY_PHONE_AND_SIP;
    }

    public static List<NativeDBObject> getContactList() {
        return getContactList(QueryType.ALL, null);
    }

    public static List<NativeDBObject>  getContactList(QueryType searchType, String value) {

        boolean contactPermission = PermissionManager.getInstance().checkPermission(PermissionManagerType.CONTACTS);
        if (!contactPermission) {
            return null;
        }
        Map<Long, NativeDBObject> nativeDBObjectHashMap = new HashMap<>();

        String[] projection = {Data.CONTACT_ID, Data.DISPLAY_NAME, Data.MIMETYPE, Data.DATA1, Data.DATA2, Data.PHOTO_URI, Data.PHOTO_THUMBNAIL_URI};

        String selection = Data.MIMETYPE + " IN ('" +
                Phone.CONTENT_ITEM_TYPE + "', '" +
                //CommonDataKinds.Email.CONTENT_ITEM_TYPE + "', '" +
                SipAddress.CONTENT_ITEM_TYPE + "')";


        switch (searchType) {
            case ALL:
                break;
            case BY_NAME:
                selection += " AND " + Contacts.DISPLAY_NAME + " = '" + value + "'";
                break;
            case BY_PHONE:
                selection += " AND " + Phone.NUMBER + " = '" + value + "'";
                break;
            case BY_SIP:
                selection += " AND " + SipAddress.SIP_ADDRESS + " = '" + value + "'";
                break;
            case BY_PHONE_AND_SIP:
                selection += " AND " + SipAddress.SIP_ADDRESS + " = '" + value + "' OR " + Phone.NUMBER + " = '" + value + "'";
                break;

        }

        ContentResolver cr = BotApplication.getGlobalContext().getContentResolver();
        Cursor cur = cr.query(Data.CONTENT_URI, projection, selection, null, null);

        while (cur != null && cur.moveToNext()) {
            @SuppressLint("Range") long id = cur.getLong(cur.getColumnIndex(Data.CONTACT_ID));
            @SuppressLint("Range") String name = cur.getString(cur.getColumnIndex(Contacts.DISPLAY_NAME));
            @SuppressLint("Range") String mime = cur.getString(cur.getColumnIndex(Contacts.Data.MIMETYPE));
            @SuppressLint("Range") String data = cur.getString(cur.getColumnIndex(Contacts.Data.DATA1));
            @SuppressLint("Range") String dataType = cur.getString(cur.getColumnIndex(Contacts.Data.DATA2));
            @SuppressLint("Range") String photo = cur.getString(cur.getColumnIndex(Data.PHOTO_URI));
            @SuppressLint("Range") String thumb = cur.getString(cur.getColumnIndex(Data.PHOTO_THUMBNAIL_URI));

            NativeDBObject nativeDBObject;
            List<NativeDBPhones> nativeDBPhones;

            if (nativeDBObjectHashMap.containsKey(id)) {
                //contact exist, get it and add data
                nativeDBObject = nativeDBObjectHashMap.get(id);
                nativeDBPhones = nativeDBObject.getPhones();
            } else {

                //Create new contact
                nativeDBPhones = new ArrayList<>();
                nativeDBObject = new NativeDBObject();
                nativeDBObject.setDisplayName(name);
                nativeDBObject.setPhotoURI(photo);
                nativeDBObject.setPhotoThumbnailURI(thumb);
            }

            switch (mime) {
                case Phone.CONTENT_ITEM_TYPE:
                    nativeDBPhones.add(new NativeDBPhones(data, dataType));
                    break;
                /*case Email.CONTENT_ITEM_TYPE:
                    break;*/
                case SipAddress.CONTENT_ITEM_TYPE:
                    //contact.setSip(data);
                    nativeDBPhones.add(new NativeDBPhones(data, dataType));
                    break;
            }
            nativeDBObject.setPhones(nativeDBPhones);
            nativeDBObjectHashMap.put(id, nativeDBObject);

        }
        List<NativeDBObject> nativeDBObjectList = new ArrayList<NativeDBObject>(nativeDBObjectHashMap.values());
        return nativeDBObjectList;
    }


//    public static List<NativeDBObject> getContactList() {
//        return getContactList(QueryType.ALL, null);
//    }
//
//    public static List<NativeDBObject> getContactList(QueryType searchType, String value) {
//
//
//
//        long start = System.currentTimeMillis();
//        List<NativeDBObject> nativeDBObjectList = new ArrayList<>();
//
//        ContentResolver cr = MainApp.getGlobalContext().getContentResolver();
//
//        Cursor ContactCursor = null;
//        switch (searchType) {
//            case ALL:
//                ContactCursor = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
//                break;
//            case BY_NAME:
//                ContactCursor = cr.query(ContactsContract.Contacts.CONTENT_URI, null, ContactsContract.Contacts.DISPLAY_NAME + "='" + value + "'", null, null);
//                break;
//            case BY_PHONE:
//                ContactCursor = cr.query(Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(value)), null, null, null, null);
//                break;
//        }
//
//        if ((ContactCursor != null ? ContactCursor.getCount() : 0) > 0) {
//            while (ContactCursor != null && ContactCursor.moveToNext()) {
//
//                NativeDBObject nativeDBObject = new NativeDBObject();
//
//                String id = ContactCursor.getString(ContactCursor.getColumnIndex(ContactsContract.Contacts._ID));
//                String name = ContactCursor.getString(ContactCursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
//                String photoURI = ContactCursor.getString(ContactCursor.getColumnIndex(ContactsContract.Contacts.PHOTO_URI));
//                String photoThumbURI = ContactCursor.getString(ContactCursor.getColumnIndex(ContactsContract.Contacts.PHOTO_THUMBNAIL_URI));
//
//
//                //if (ContactCursor.getInt(ContactCursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0)
//                //{
//
//                nativeDBObject.setId(id);
//                nativeDBObject.setDisplayName(name);
//                nativeDBObject.setPhotoURI(photoURI);
//                nativeDBObject.setPhotoThumbnailURI(photoThumbURI);
//
//
//                Cursor pCur0 = cr.query(ContactsContract.Data.CONTENT_URI,
//                        null,
//                        ContactsContract.Data.CONTACT_ID + " = ?",
//                        new String[]{id}, null);
//
//                List<NativeDBPhones> nativeDBPhones = new ArrayList<>();
//                while (pCur0.moveToNext()) {
//
//                    String mimeType = pCur0.getString(pCur0.getColumnIndex(ContactsContract.Data.MIMETYPE));
//
//                    switch (mimeType) {
//
//                        case CommonDataKinds.Phone.CONTENT_ITEM_TYPE:
//                            String phone = pCur0.getString(pCur0.getColumnIndex(CommonDataKinds.Phone.DATA));
//                            int phoneType = pCur0.getInt(pCur0.getColumnIndex(CommonDataKinds.Phone.TYPE));
//                            nativeDBPhones.add(new NativeDBPhones(phone, phoneType));
//                            break;
//
//
//                        case CommonDataKinds.Email.CONTENT_ITEM_TYPE:
//                            //String email = pCur0.getString(pCur0.getColumnIndex(CommonDataKinds.Phone.DATA));
//                            break;
//
//                        case CommonDataKinds.SipAddress.CONTENT_ITEM_TYPE:
//                            //String sip = pCur0.getString(pCur0.getColumnIndex(CommonDataKinds.SipAddress.DATA));
//                            //nativeDBObject.setSip(sip);
//                            String sipPhone = pCur0.getString(pCur0.getColumnIndex(CommonDataKinds.Phone.DATA));
//                            int sipPhoneType = -5;
//                            nativeDBPhones.add(new NativeDBPhones(sipPhone, sipPhoneType));
//                            break;
//                    }
//                }
//
//                nativeDBObject.setPhones(nativeDBPhones);
//                pCur0.close();
//
//                //if (nativeDBObject.getSip() != null || contact.getPhones().size() != 0)
//                if (nativeDBObject.getPhones().size() != 0) {
//                    nativeDBObjectList.add(nativeDBObject);
//                }
//                //}
//            }
//        }
//        if (ContactCursor != null) {
//            ContactCursor.close();
//        }
//
//        long end = System.currentTimeMillis();
//        Log.d("DTAG", "Time took: " + (end - start) + "ms");
//
//        if(nativeDBObjectList.size()==0) {
//            return null;
//        }
//
//        return nativeDBObjectList;
//
//    }

}
