package com.kore.ai.widgetsdk.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.kore.ai.widgetsdk.applicationcontrol.ACMModel;

import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;


public class SharedPreferenceUtils {
    private static SharedPreferenceUtils sharedPreferenceUtils;
    private static Context context;
    private static SharedPreferences sharedPreferences;
    private String IS_PERSONAL_ANNOUNCEMENT="IS_PERSONAL_ANNOUNCEMENT";
    public static final String SHARED_PREFERENCE_NAME = "kora_shared_preferences";
    public static final String APP_CONTROLS_EDITOR_KEY = "APP_CONTROLS_EDITOR_KEY";
    public static final String KORA_APP_UPGRADE_SHARED_PREFERENCES = "com.kore.ai.koreassistant.upgrade.shared.preferences";



    public static SharedPreferenceUtils getInstance(Context context) {
        if (sharedPreferenceUtils == null || context == null) {
            sharedPreferenceUtils = new SharedPreferenceUtils(context);
        }
        return sharedPreferenceUtils;
    }

    private SharedPreferenceUtils(Context context) {
        context = context;
       sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCE_NAME, MODE_PRIVATE);
    }

    public String getKeyValue(String Key, String defaultValue) {
        return sharedPreferences.getString(Key, defaultValue);
    }

    public boolean getKeyValue(String Key, boolean defaultValue) {
        return sharedPreferences.getBoolean(Key, defaultValue);
    }
    public int getKeyValue(String Key, int defaultValue) {
        return sharedPreferences.getInt(Key, defaultValue);
    }

    public void putKeyValue(String Key, String defaultValue) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Key, defaultValue);
        editor.apply();
    }

    public void putKeyValue(String Key, Integer defaultValue) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(Key, defaultValue);
        editor.apply();
    }

    public static void saveAppControlList(ACMModel list, String userId){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(list);
        editor.putString(APP_CONTROLS_EDITOR_KEY+userId,json);
        editor.commit();
    }

    public static ACMModel getAppControlList( String userId){
        String jsonStr = sharedPreferences.getString(APP_CONTROLS_EDITOR_KEY+userId, null);
        if(jsonStr != null){
            Gson gson = new Gson();
            return gson.fromJson(jsonStr,ACMModel.class);
        }
        return null;
    }



    public void putAnnouncmentKey(boolean isPersonalAnnoucement)
    {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(IS_PERSONAL_ANNOUNCEMENT, isPersonalAnnoucement);
        editor.apply();
    }

    public boolean getIsPersonalAnnoucment() {
        return sharedPreferences.getBoolean(IS_PERSONAL_ANNOUNCEMENT, false);
    }

    public void putKeyValues(HashMap<String, Object> keyValueMap) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        for (Map.Entry<String, Object> entry : keyValueMap.entrySet()) {
            if(entry.getValue() instanceof String) {
                editor.putString(entry.getKey(), (String) entry.getValue());
            }else if(entry.getValue() instanceof Integer){
                editor.putInt(entry.getKey(), (Integer) entry.getValue());
            }else{
                editor.putBoolean(entry.getKey(), (Boolean) entry.getValue());
            }

        }
        editor.apply();
    }

    public void putKeyValue(String Key, boolean defaultValue) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(Key, defaultValue);
        editor.apply();
    }

    public void clear(){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

}
