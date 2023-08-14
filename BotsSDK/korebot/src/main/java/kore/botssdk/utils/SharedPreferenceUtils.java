package kore.botssdk.utils;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kore.botssdk.applicationcontrol.ACMModel;
import kore.botssdk.models.limits.Announcement;
import kore.botssdk.models.limits.Attachment;
import kore.botssdk.models.limits.Knowledge;
import kore.botssdk.models.limits.KnwCollections;
import kore.botssdk.models.limits.LimitAccount;
import kore.botssdk.models.limits.Teams;
import kore.botssdk.models.limits.UsageLimit;


public class SharedPreferenceUtils {
    private static SharedPreferenceUtils sharedPreferenceUtils;
    private static Context context;
    private static SharedPreferences sharedPreferences;
    private final String IS_PERSONAL_ANNOUNCEMENT="IS_PERSONAL_ANNOUNCEMENT";
    public static final String SHARED_PREFERENCE_NAME = "kora_shared_preferences";
    public static final String APP_CONTROLS_EDITOR_KEY = "APP_CONTROLS_EDITOR_KEY";
    public static final String KORA_APP_UPGRADE_SHARED_PREFERENCES = "com.kore.ai.koreassistant.upgrade.shared.preferences";
    public static final String ATTACHMENT_KEY = "at";
    public static final String KNOWLEDGE_KEY = "kn";
    public static final String ANNOUNCEMENT_KEY = "an";
    public static final String TEAMS_KEY = "te";
    public static final String LIMIT_ACCOUNT_KEY = "lak";
    public static final String KNW_COLLECTIONS_ACCOUNT_KEY = "knwc";


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
    public static void setLimitAccount(LimitAccount limitAccount, String key) {
        saveObjectToSharedPref(limitAccount, LIMIT_ACCOUNT_KEY+key);
    }

    public static LimitAccount getLimitAccount(String key) {
       return getObjectToSharedPref(LimitAccount.class, LIMIT_ACCOUNT_KEY+key);
    }

    public static void setUsageLimit(List<UsageLimit>  usageLimits, String key) {
        if(usageLimits==null || usageLimits.size()==0) {
            setLimitAttachment(null, ATTACHMENT_KEY+key);
            saveLimitObjectToSharedPref(null, KNOWLEDGE_KEY+key);
            saveLimitObjectToSharedPref(null, ANNOUNCEMENT_KEY+key);
            saveLimitObjectToSharedPref(null, TEAMS_KEY+key);
            saveLimitObjectToSharedPref(null, KNW_COLLECTIONS_ACCOUNT_KEY+key);

            return;
        }
        for(UsageLimit usageLimit: usageLimits) {
            switch (usageLimit.getType()) {
                case "attachment":
                    setLimitAttachment(usageLimit, ATTACHMENT_KEY+key);
                    break;
                case "articles":
                    saveLimitObjectToSharedPref(usageLimit, KNOWLEDGE_KEY+key);
                    break;
                case "announcements":
                    saveLimitObjectToSharedPref(usageLimit, ANNOUNCEMENT_KEY+key);
                    break;
                case "teams":
                    saveLimitObjectToSharedPref(usageLimit, TEAMS_KEY+key);
                    break;
                case "knwCollections":
                    saveLimitObjectToSharedPref(usageLimit, KNW_COLLECTIONS_ACCOUNT_KEY+key);
                    break;

            }
        }




    }

    private static void setLimitAttachment(UsageLimit attachment, String key) {
        if(TextUtils.isEmpty(key) || sharedPreferences==null ) {
            return;
        }
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if(attachment==null) {
            editor.putInt(key,-1);
            editor.commit();
            return;
        }
        editor.putInt(key,attachment.getSize());
        editor.commit();
    }

    public Attachment getAttachmentPref(String key) {
        if(TextUtils.isEmpty(key) || sharedPreferences==null) {
            return null;
        }
        int size = sharedPreferences.getInt(ATTACHMENT_KEY+key, -1);

        Attachment attachment= new Attachment();
        attachment.setSize(size);

        return attachment;

    }
    public Knowledge getKnowledgePref(String key) {
        return getLimitObjectToSharedPref(new Knowledge(), KNOWLEDGE_KEY+key);
    }
    public Announcement getAnnouncementPref(String key) {
        return getLimitObjectToSharedPref(new Announcement(), ANNOUNCEMENT_KEY+key);
    }
    public Teams getTeamsPref(String key) {
        return getLimitObjectToSharedPref(new Teams(), TEAMS_KEY+key);
    }
    public KnwCollections getKnwCollectionsPref(String key) {
        return getLimitObjectToSharedPref(new KnwCollections(), KNW_COLLECTIONS_ACCOUNT_KEY+key);
    }

    private <T extends UsageLimit> T getLimitObjectToSharedPref(UsageLimit obj, String key){
        if(TextUtils.isEmpty(key) || obj==null || sharedPreferences==null) {
            return null;
        }
        int limit = sharedPreferences.getInt("l"+key, -1);
        int used = sharedPreferences.getInt("u"+key, -1);

        obj.setLimit(limit);
        obj.setUsed(used);

        return (T) obj;
    }

    public static <T> T getObjectToSharedPref(Class<T> objClass, String key){
        if(TextUtils.isEmpty(key) || objClass==null || sharedPreferences==null) {
            return null;
        }
        String jsonStr = sharedPreferences.getString(key, null);
        if(jsonStr != null){
            return new Gson().fromJson(jsonStr,objClass);
        }
        return null;
    }

    private static  void saveLimitObjectToSharedPref(UsageLimit limit, String key){
        if(TextUtils.isEmpty(key) || sharedPreferences==null ) {
            return;
        }
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if(limit==null) {
            editor.putInt("l"+key,-1);
            editor.putInt("u"+key,-1);
            editor.commit();
            return;
        }
        editor.putInt("l"+key,limit.getLimit());
        editor.putInt("u"+key,limit.getUsed());
        editor.commit();
    }

    public static <T> void saveObjectToSharedPref(T object, String key){
        if(TextUtils.isEmpty(key) || sharedPreferences==null) {
            return;
        }
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if(object==null) {
            editor.putString(key,null);
            editor.commit();
            return;
        }

        Gson gson = new Gson();
        String json = gson.toJson(object);
        editor.putString(key,json);
        editor.commit();
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
