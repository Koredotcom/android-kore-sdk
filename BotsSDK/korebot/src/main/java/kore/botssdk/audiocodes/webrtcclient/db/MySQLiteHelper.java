package kore.botssdk.audiocodes.webrtcclient.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.LinkedList;
import java.util.List;

import kore.botssdk.audiocodes.webrtcclient.Structure.CallEntry;

public class MySQLiteHelper extends SQLiteOpenHelper {



    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "WebRTCDB";
    private static final String TABLE_NAME = "Recent";

    private static final String KEY_ID = "id";
    private static final String KEY_DISPLAY_NAME = "display_name";
    private static final String KEY_PHONE_NUMBER = "phone_number";
    private static final String KEY_START_TIME = "start_time";
    private static final String KEY_DURATION = "duration";
    private static final String KEY_TYPE = "type";



    public MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_TASK_TABLE = "CREATE TABLE " + TABLE_NAME + " ( " +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ""+KEY_DISPLAY_NAME+" TEXT, " +
                ""+KEY_PHONE_NUMBER+" TEXT, " +
                ""+KEY_START_TIME+" LONG, " +
                ""+KEY_DURATION+" LONG, " +
                ""+KEY_TYPE+" INTEGER)";

            db.execSQL(CREATE_TASK_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        this.onCreate(db);
    }

    public void deleteTable()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " +TABLE_NAME);
        this.onCreate(db);
    }

    //Add new missed call_activity
    public void addEntry(CallEntry callEntry){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_DISPLAY_NAME, callEntry.getContactName());
        values.put(KEY_PHONE_NUMBER, callEntry.getContactNumber());
        values.put(KEY_START_TIME, callEntry.getStartTime());
        values.put(KEY_DURATION, callEntry.getDuration());
        values.put(KEY_TYPE, callEntry.getTypeAsInt());

        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    //Get all calls
    public List<CallEntry> getAllEntries () {
        return getEntries(-1);
//        List<CallEntry> entries = new LinkedList<>();
//        String query = "SELECT  * FROM " + TABLE_NAME+" ORDER BY 1 DESC";
//        SQLiteDatabase db = this.getWritableDatabase();
//        Cursor cursor = db.rawQuery(query, null);
//
//        CallEntry callEntry = null;
//        if (cursor.moveToFirst()) {
//            do {
//                callEntry = new CallEntry();
//                callEntry.setId(cursor.getInt(0));
//                callEntry.setContactName(cursor.getString(1));
//                callEntry.setContactNumber(cursor.getString(2));
//                callEntry.setStartTime(cursor.getLong(3));
//                callEntry.setDuration(cursor.getLong(4));
//                callEntry.setType(cursor.getInt(5));
//
//                entries.add(callEntry);
//            } while (cursor.moveToNext());
//        }
//        return entries;
    }

    //Get all missed calls
    public List<CallEntry> getEntries (int numberOfEntries) {

        List<CallEntry> entries = new LinkedList<>();
        String query = "SELECT  * FROM " + TABLE_NAME+" ORDER BY 1 DESC";
        if(numberOfEntries>0) {
            query+=" LIMIT "+numberOfEntries;
        }
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        CallEntry callEntry = null;
        if (cursor.moveToFirst()) {
            do {
                callEntry = new CallEntry();
                callEntry.setId(cursor.getInt(0));
                callEntry.setContactName(cursor.getString(1));
                callEntry.setContactNumber(cursor.getString(2));
                callEntry.setStartTime(cursor.getLong(3));
                callEntry.setDuration(cursor.getLong(4));
                callEntry.setType(cursor.getInt(5));

                entries.add(callEntry);
            } while (cursor.moveToNext());
        }
        return entries;
    }

    public void deleteEntry(CallEntry callEntry)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, KEY_ID + " = ?", new String[]{String.valueOf(callEntry.getId())});
        db.close();

    }
}
