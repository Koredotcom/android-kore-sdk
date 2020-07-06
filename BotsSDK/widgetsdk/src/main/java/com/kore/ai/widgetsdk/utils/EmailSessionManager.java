package com.kore.ai.widgetsdk.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class EmailSessionManager {

    SharedPreferences pref;

    // Editor for Shared preferences
    SharedPreferences.Editor editor;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;
    public static final String Email = "email";

    public static final String LAST_FEEDBACK_VISIBLE_TIME = "feedbackVisibleTime";

    // Sharedpref file name
    private static final String PREF_NAME = "in.org.koraassistance.email";


    public EmailSessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        if (pref != null) {
            editor = pref.edit();
        }

    }


    public void saveEmail(String email) {


        editor.putString(Email, email);
        editor.commit();


    }

    public void saveTime(long time)
    {
        editor.putLong(LAST_FEEDBACK_VISIBLE_TIME, time);
        editor.commit();
    }

    public long getLastFeedbackVisibleTime() {

        return pref.getLong(LAST_FEEDBACK_VISIBLE_TIME, 0);
    }


    public void clearSharedPreferences() {
        editor.clear().commit();
    }

    public String getEmail() {
        return pref.getString(Email, "");
    }
}
