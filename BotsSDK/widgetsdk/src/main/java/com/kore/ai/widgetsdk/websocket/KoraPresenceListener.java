package com.kore.ai.widgetsdk.websocket;

import com.google.gson.Gson;
import com.kore.ai.widgetsdk.events.KaAuthenticationErrorEvents;
import com.kore.ai.widgetsdk.events.KaMessengerUpdate;
import com.kore.ai.widgetsdk.events.KaVoiceEvent;
import com.kore.ai.widgetsdk.events.KoreEventCenter;
import com.kore.ai.widgetsdk.events.NewAnnouncementEvent;
import com.kore.ai.widgetsdk.events.PanelUpdateEvent;
import com.kore.ai.widgetsdk.net.KaRestResponse;
import com.kore.ai.widgetsdk.utils.BellNotificationUtils;
import com.kore.ai.widgetsdk.utils.NotificationUtils;
import com.kore.ai.widgetsdk.utils.StringUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Ramachandra Pradeep on 11-Dec-18.
 */

public class KoraPresenceListener implements PresenceConnectionListener {

    public static KoraPresenceListener mInstance;

    private KoraPresenceListener() {
    }

    private Gson gson = new Gson();

    @Override
    public void onConnected(HashMap<String, String> hsh) {
//        KoreLogger.debugLog(KoreLogger.LOG_TAG, "1111111111 "+hsh.toString());
        String statusName = hsh.get("name");
        String args = hsh.get("args");
//        String notification = hsh.get("notification");
        if (statusName != null) {
            if (statusName.equalsIgnoreCase("notification")) {
                // invoking service for getting latest notification and updating mention notification panel badge
                try {
                    JSONObject root = new JSONObject(args);
                    JSONObject customdata = root.getJSONObject("customdata");

                    if (customdata == null) {
                        return;
                    }

                    if (customdata.has("nStats")) {
                        BellNotificationUtils.setStats(gson.fromJson(customdata.get("nStats").toString(), KaRestResponse.Stats.class));

                    }/* else if (customdata.has("event_type")) {

                    }*/

                    if (customdata.has("t")) {
                        if (customdata.get("t").equals(NotificationUtils.SHORT_TYPE_ANNOUNCEMENT)) {
                            String kId = customdata.getString("knowledgeId");
                            NotificationUtils.addAnnouncementToList(kId);
                            KoreEventCenter.post(new NewAnnouncementEvent(kId, true));
                        } else if (customdata.get("t").equals(NotificationUtils.SHORT_TYPE_KORA_LOGOUT)) {
                            if (customdata.get("region") != null && customdata.get("region").equals("PERMISSIONS_CHANGED"))
                                KoreEventCenter.post(new KaAuthenticationErrorEvents.OnEntAdminSettingsChanged("PERMISSIONS_CHANGED", 61));
                        } else if (customdata.get("t").equals(NotificationUtils.SHORT_TYPE_VOICE_DATA)) {
                                KoreEventCenter.post(new KaVoiceEvent(root.toString()));

                        }else if(customdata.get("t").equals(NotificationUtils.SHORT_TYPE_KORE_MESSENGER_UPDATE)){

                            KaMessengerUpdate msgUpdate = new KaMessengerUpdate();
                            JSONArray panelStatsArr = customdata.getJSONArray("panelStats");

                            for(int i=0;i<panelStatsArr.length();i++){// y n
                                JSONObject jObj =  panelStatsArr.getJSONObject(i);
                                if(jObj.has("panelId") && jObj.getString("panelId").equals(StringUtils.kora_team)){
                                    msgUpdate.setTeamUpdate(jObj.getBoolean("hasUpdates"));
                                    msgUpdate.setTeamId("teamId");
                                }else{
                                    msgUpdate.setMsgUpdate(jObj.getBoolean("hasUpdates"));
                                }
                                msgUpdate.setUri(jObj.getString("uri"));
                            }

                            KoreEventCenter.post(msgUpdate);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else if (statusName.equalsIgnoreCase("live")) {
//                KoreLogger.debugLog(LOG_TAG, args);
                try {
                JSONObject root = new JSONObject(args);
                String eID = root.optString("eId");
                String entity=root.optString("entity");//profile
                String action=root.optString("action");//update
                if(!StringUtils.isNullOrEmpty(eID)&&!StringUtils.isNullOrEmpty(eID)&&!StringUtils.isNullOrEmpty(action)&&entity.equalsIgnoreCase("profile")&&action.equalsIgnoreCase("update"))
                {
                    //update profile ha
//                    BellNotificationUtils.getProfileInfo();
//                    KoreEventCenter.post(new KaProfileImageEvent(true));

                }else if(!StringUtils.isNullOrEmpty(entity)&&!StringUtils.isNullOrEmpty(action)
                        &&entity.equalsIgnoreCase("panels")&&action.equalsIgnoreCase("update"))
                {
                    KoreEventCenter.post(new PanelUpdateEvent());
                }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }

    }

    @Override
    public void onDisconnected(HashMap<String, String> hsh) {

    }

    public static synchronized KoraPresenceListener getInstance() {

        if (mInstance == null) {
            mInstance = new KoraPresenceListener();
        }

        return mInstance;
    }
}
