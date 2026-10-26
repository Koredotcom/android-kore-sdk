/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.SharedPreferences
 *  android.content.SharedPreferences$Editor
 */
package com.korevg.mv.webrtcsdk.useragent;

import android.content.Context;
import android.content.SharedPreferences;

public class Storage {
    private static final String STORAGE_FILE = "ACStorage";
    public static final String PN_REGISTRATION_TOKEN = "PN_REGISTRATION_TOKEN";
    public static final String PN_PROJECT_ID = "PN_PROJECT_ID";
    public static final String PN_PROVIDER = "PN_PROVIDER";

    public static void saveString(Context context, String key, String value) {
        SharedPreferences pref = context.getSharedPreferences(STORAGE_FILE, 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static String loadString(Context context, String key) {
        return Storage.loadString(context, key, null);
    }

    public static String loadString(Context context, String key, String defaultValue) {
        SharedPreferences pref = context.getSharedPreferences(STORAGE_FILE, 0);
        return pref.getString(key, defaultValue);
    }
}

