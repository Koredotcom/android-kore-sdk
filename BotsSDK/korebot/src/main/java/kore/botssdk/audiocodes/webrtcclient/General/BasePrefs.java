package kore.botssdk.audiocodes.webrtcclient.General;


import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import kore.botssdk.application.BotApplication;

public class BasePrefs {

    private static final String TAG = "BasePrefs";

    public static final String PREFS_NAME =  "PREFS_WEBRTC";

    private static SharedPreferences sharedPreferences = BotApplication.getGlobalContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    private static SharedPreferences.Editor editorSP = sharedPreferences.edit();

    // basic functions
    // int
    public static void putInt(String key, int value)
    {
        key = key.toLowerCase();
        editorSP.putInt(key, value);
        editorSP.commit();
    }

    public static int getInt(String key)
    {
        return getInt(key, -1);
    }

    public static int getInt(String key, int defValue)
    {
        key = key.toLowerCase();
        int value = sharedPreferences.getInt(key, defValue);
        return value;
    }

    // long
    public static void putLong(String key, long value)
    {
        key = key.toLowerCase();
        editorSP.putLong(key, value);
        editorSP.commit();
    }

    public static long getLong(String key)
    {
        return getLong(key, -1);
    }

    public static long getLong(String key, long defValue)
    {
        key = key.toLowerCase();
        long value = sharedPreferences.getLong(key, defValue);
        return value;
    }

    // string
    public static void putString(String key, String value)
    {
        key = key.toLowerCase();
        editorSP.putString(key, value);
        editorSP.commit();
    }

    public static String getString(String key)
    {
        return getString(key, null);
    }



    public static String getString(String key, String defValue)
    {
        key = key.toLowerCase();
        String value = sharedPreferences.getString(key, defValue);
        return value;
    }

    // boolean
    public static void putBoolean(String key, boolean value)
    {
        key = key.toLowerCase();
        Log.d(TAG, "putBoolean: "+key+" vlaue: "+value);
        editorSP.putBoolean(key, value);
        editorSP.commit();
    }

    public static boolean getBoolean(String key)
    {
        return getBoolean(key, false);
    }

    public static boolean getBoolean(String key, boolean defValue)
    {
        key = key.toLowerCase();
        boolean value = sharedPreferences.getBoolean(key, defValue);
        Log.d(TAG, "getBoolean: "+key+" vlaue: "+value+" defValue: "+defValue);
        return value;
    }

    // boolean
    public static void putClass(String key, Object vlaue)
    {
        key = key.toLowerCase();
        Gson gson = new Gson();
        String json = gson.toJson(vlaue);
        Log.d(TAG, "putClass: "+key+" vlaue: "+json);


        editorSP.putString(key, json);
        editorSP.commit();
    }

    public static Object getClass(String key, Class cls)
    {
        return getClass(key, cls,null);
    }

    public static Object getClass(String key, Class cls, Object defValue)
    {
        key = key.toLowerCase();
        Gson gson = new Gson();
        String json = sharedPreferences.getString(key, "");
        if (json==null || json.equals(""))
        {
            return defValue;
        }
        Object value = gson.fromJson(json, cls);
        Log.d(TAG, "getBoolean: "+key+" vlaue: "+value+" defValue: "+defValue);
        return value;

    }

}
