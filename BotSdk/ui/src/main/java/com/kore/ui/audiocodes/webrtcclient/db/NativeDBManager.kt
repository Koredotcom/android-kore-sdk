package com.kore.ui.audiocodes.webrtcclient.db

import android.annotation.SuppressLint
import android.content.Context
import android.provider.ContactsContract
import android.provider.ContactsContract.CommonDataKinds.Phone
import android.provider.ContactsContract.CommonDataKinds.SipAddress
import com.kore.ui.audiocodes.webrtcclient.permissions.PermissionManager
import com.kore.ui.audiocodes.webrtcclient.permissions.PermissionManagerType

object NativeDBManager {
    private const val TAG = "NativeDBManager"

    fun getContactList(context: Context, searchType: QueryType?, value: String?): List<NativeDBObject?>? {
        val contactPermission = PermissionManager.instance.checkPermission(context, PermissionManagerType.CONTACTS)
        if (!contactPermission) {
            return null
        }
        val nativeDBObjectHashMap: MutableMap<Long, NativeDBObject?> =
            HashMap()
        val projection = arrayOf(
            ContactsContract.Data.CONTACT_ID,
            ContactsContract.Data.DISPLAY_NAME,
            ContactsContract.Data.MIMETYPE,
            ContactsContract.Data.DATA1,
            ContactsContract.Data.DATA2,
            ContactsContract.Data.PHOTO_URI,
            ContactsContract.Data.PHOTO_THUMBNAIL_URI
        )
        var selection = ContactsContract.Data.MIMETYPE + " IN ('" +
                Phone.CONTENT_ITEM_TYPE + "', '" +  //CommonDataKinds.Email.CONTENT_ITEM_TYPE + "', '" +
                SipAddress.CONTENT_ITEM_TYPE + "')"
        when (searchType) {
            QueryType.ALL -> {}
            QueryType.BY_NAME -> selection += " AND " + ContactsContract.Contacts.DISPLAY_NAME + " = '" + value + "'"
            QueryType.BY_PHONE -> selection += " AND " + Phone.NUMBER + " = '" + value + "'"
            QueryType.BY_SIP -> selection += " AND " + SipAddress.SIP_ADDRESS + " = '" + value + "'"
            QueryType.BY_PHONE_AND_SIP -> selection += " AND " + SipAddress.SIP_ADDRESS + " = '" + value + "' OR " + Phone.NUMBER + " = '" + value + "'"
            else -> {}
        }
        val cr = context.contentResolver
        val cur = cr.query(ContactsContract.Data.CONTENT_URI, projection, selection, null, null)
        while (cur != null && cur.moveToNext()) {
            @SuppressLint("Range") val id = cur.getLong(cur.getColumnIndex(ContactsContract.Data.CONTACT_ID))
            @SuppressLint("Range") val name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
            @SuppressLint("Range") val mime = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.Data.MIMETYPE))
            @SuppressLint("Range") val data = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.Data.DATA1))
            @SuppressLint("Range") val dataType = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.Data.DATA2))
            @SuppressLint("Range") val photo = cur.getString(cur.getColumnIndex(ContactsContract.Data.PHOTO_URI))
            @SuppressLint("Range") val thumb = cur.getString(cur.getColumnIndex(ContactsContract.Data.PHOTO_THUMBNAIL_URI))
            var nativeDBObject: NativeDBObject?
            var nativeDBPhones: MutableList<NativeDBPhones?>?
            if (nativeDBObjectHashMap.containsKey(id)) {
                //contact exist, get it and add data
                nativeDBObject = nativeDBObjectHashMap[id]
                nativeDBPhones = nativeDBObject!!.phones
            } else {

                //Create new contact
                nativeDBPhones = ArrayList()
                nativeDBObject = NativeDBObject()
                nativeDBObject.displayName = name
                nativeDBObject.photoURI = photo
                nativeDBObject.photoThumbnailURI = thumb
            }
            when (mime) {
                Phone.CONTENT_ITEM_TYPE -> nativeDBPhones?.add(NativeDBPhones(data))
                SipAddress.CONTENT_ITEM_TYPE -> {
                    //contact.setSip(data);
                    nativeDBPhones?.add(NativeDBPhones(data))
                }
            }
            nativeDBObject.phones = nativeDBPhones
            nativeDBObjectHashMap[id] = nativeDBObject
        }
        return ArrayList(nativeDBObjectHashMap.values)
    } //    public static List<NativeDBObject> getContactList() {

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
    enum class QueryType {
        ALL,
        BY_NAME,
        BY_PHONE,
        BY_SIP,
        BY_PHONE_AND_SIP
    }
}
