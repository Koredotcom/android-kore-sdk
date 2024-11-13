package kore.botssdk.audiocodes.webrtcclient.General;


import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

public class BasePrefs {

    private static final String TAG = "BasePrefs";

    public static final String PREFS_NAME = "PREFS_WEBRTC";

    // basic functions
    // int
    public static void putInt(Context context, String key, int value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editorSP = sharedPreferences.edit();
        key = key.toLowerCase();
        editorSP.putInt(key, value);
        editorSP.commit();
    }

    public static int getInt(Context context, String key) {
        return getInt(context, key, -1);
    }

    public static int getInt(Context context, String key, int defValue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        key = key.toLowerCase();
        int value = sharedPreferences.getInt(key, defValue);
        return value;
    }

    // long
    public static void putLong(Context context, String key, long value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editorSP = sharedPreferences.edit();
        key = key.toLowerCase();
        editorSP.putLong(key, value);
        editorSP.commit();
    }

    public static long getLong(Context context, String key) {
        return getLong(context, key, -1);
    }

    public static long getLong(Context context, String key, long defValue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        key = key.toLowerCase();
        long value = sharedPreferences.getLong(key, defValue);
        return value;
    }

    // string
    public static void putString(Context context, String key, String value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editorSP = sharedPreferences.edit();
        key = key.toLowerCase();
        editorSP.putString(key, value);
        editorSP.commit();
    }

    public static String getString(Context context, String key) {
        return getString(context, key, null);
    }

    public static String getString(Context context, String key, String defValue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        key = key.toLowerCase();
        String value = sharedPreferences.getString(key, defValue);
        return value;
    }

    // boolean
    public static void putBoolean(Context context, String key, boolean value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editorSP = sharedPreferences.edit();
        key = key.toLowerCase();
        Log.d(TAG, "putBoolean: " + key + " vlaue: " + value);
        editorSP.putBoolean(key, value);
        editorSP.commit();
    }

    public static boolean getBoolean(Context context, String key) {
        return getBoolean(context, key, false);
    }

    public static boolean getBoolean(Context context, String key, boolean defValue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        key = key.toLowerCase();
        boolean value = sharedPreferences.getBoolean(key, defValue);
        Log.d(TAG, "getBoolean: " + key + " vlaue: " + value + " defValue: " + defValue);
        return value;
    }

    // boolean
    public static void putClass(Context context, String key, Object vlaue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editorSP = sharedPreferences.edit();
        key = key.toLowerCase();
        Gson gson = new Gson();
        String json = gson.toJson(vlaue);
        Log.d(TAG, "putClass: " + key + " vlaue: " + json);

        editorSP.putString(key, json);
        editorSP.commit();
    }

    public static Object getClass(Context context, String key, Class cls) {
        return getClass(context, key, cls, null);
    }

    public static Object getClass(Context context, String key, Class cls, Object defValue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        key = key.toLowerCase();
        Gson gson = new Gson();
        String json = sharedPreferences.getString(key, "");
        if (json == null || json.equals("")) {
            return defValue;
        }
        Object value = gson.fromJson(json, cls);
        Log.d(TAG, "getBoolean: " + key + " vlaue: " + value + " defValue: " + defValue);
        return value;

    }

}
