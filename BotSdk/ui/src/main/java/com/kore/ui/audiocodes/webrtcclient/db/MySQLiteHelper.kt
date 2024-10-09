package com.kore.ui.audiocodes.webrtcclient.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.kore.ui.audiocodes.webrtcclient.structure.CallEntry
import java.util.LinkedList

class MySQLiteHelper(context: Context?) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase) {
        val CREATE_TASK_TABLE = "CREATE TABLE " + TABLE_NAME + " ( " +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "" + KEY_DISPLAY_NAME + " TEXT, " +
                "" + KEY_PHONE_NUMBER + " TEXT, " +
                "" + KEY_START_TIME + " LONG, " +
                "" + KEY_DURATION + " LONG, " +
                "" + KEY_TYPE + " INTEGER)"
        db.execSQL(CREATE_TASK_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME)
        onCreate(db)
    }

    fun deleteTable() {
        val db = this.writableDatabase
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME)
        onCreate(db)
    }

    //Add new missed call_activity
    fun addEntry(callEntry: CallEntry) {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(KEY_DISPLAY_NAME, callEntry.contactName)
        values.put(KEY_PHONE_NUMBER, callEntry.contactNumber)
        values.put(KEY_START_TIME, callEntry.startTime)
        values.put(KEY_DURATION, callEntry.duration)
        values.put(KEY_TYPE, callEntry.typeAsInt)
        db.insert(TABLE_NAME, null, values)
        db.close()
    }

    val allEntries: List<CallEntry>
        //Get all calls
        get() = getEntries(-1)

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
    //Get all missed calls
    fun getEntries(numberOfEntries: Int): List<CallEntry> {
        val entries: MutableList<CallEntry> = LinkedList()
        var query = "SELECT  * FROM " + TABLE_NAME + " ORDER BY 1 DESC"
        if (numberOfEntries > 0) {
            query += " LIMIT $numberOfEntries"
        }
        val db = this.writableDatabase
        val cursor = db.rawQuery(query, null)
        var callEntry: CallEntry? = null
        if (cursor.moveToFirst()) {
            do {
                callEntry = CallEntry()
                callEntry.id = cursor.getInt(0)
                callEntry.contactName = cursor.getString(1)
                callEntry.contactNumber = cursor.getString(2)
                callEntry.startTime = cursor.getLong(3)
                callEntry.duration = cursor.getLong(4)
                callEntry.setType(cursor.getInt(5))
                entries.add(callEntry)
            } while (cursor.moveToNext())
        }
        return entries
    }

    fun deleteEntry(callEntry: CallEntry) {
        val db = this.writableDatabase
        db.delete(TABLE_NAME, KEY_ID + " = ?", arrayOf(callEntry.id.toString()))
        db.close()
    }

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "WebRTCDB"
        private const val TABLE_NAME = "Recent"
        private const val KEY_ID = "id"
        private const val KEY_DISPLAY_NAME = "display_name"
        private const val KEY_PHONE_NUMBER = "phone_number"
        private const val KEY_START_TIME = "start_time"
        private const val KEY_DURATION = "duration"
        private const val KEY_TYPE = "type"
    }
}
