package kore.botssdk.korevg.webrtcclient.General;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import kore.botssdk.bot.BotClient;
import kore.botssdk.models.BotInfoModel;
import kore.botssdk.net.SDKConfiguration;
import kore.botssdk.utils.BundleConstants;

/** Holds the RTM and SIP configuration for the branded outbound click-to-call flow. */
public final class ClickToCallManager {
    private static final String TAG = "ClickToCall";
    private static BotClient botClient;
    private static boolean enabledByBranding;
    private static boolean sbcRequestSent;
    private static boolean socketOpen;
    private static JSONObject serverConfig;
    private static String sessionId = "";
    private static String lastLoggedReadiness = "";

    private ClickToCallManager() { }

    public static synchronized void configure(BotClient client, boolean enabled) {
        // The chat screen and RTM connection manager own separate BotClient instances.
        // Once the socket is open, keep the connection manager's instance because it
        // contains the task-bot identity needed for the SIP target.
        if (!socketOpen || botClient == null) {
            botClient = client;
        } else if (client != null && botClient != client) {
            Log.i(TAG, "configure retained active RTM BotClient");
        }
        enabledByBranding = enabled;
        if (!enabled) {
            sbcRequestSent = false;
            serverConfig = null;
        }
        requestSbcDetailsIfReady();
        logReadinessState();
    }

    public static synchronized void onSocketOpened(BotClient client) {
        if (client != null && botClient != client) {
            sessionId = "";
            serverConfig = null;
            sbcRequestSent = false;
            botClient = client;
        }
        socketOpen = true;
        requestSbcDetailsIfReady();
        logReadinessState();
    }

    public static synchronized void onSocketClosed() {
        socketOpen = false;
        sbcRequestSent = false;
        serverConfig = null;
        sessionId = "";
        logReadinessState();
    }

    private static void requestSbcDetailsIfReady() {
        String flowId = SDKConfiguration.Client.clickToCallFlowId;
        if (!enabledByBranding || !socketOpen || sbcRequestSent || TextUtils.isEmpty(flowId)
                || botClient == null) return;

        sbcRequestSent = true;
        Log.i(TAG, "request=sbc_details_request");
        botClient.sendClickToCallEvent("sbc_details_request", "", false);
    }

    /** Updates session/SBC state from raw RTM messages. Returns true for private control messages. */
    public static synchronized boolean onSocketMessage(String payload) {
        if (TextUtils.isEmpty(payload)) return false;
        try {
            JSONObject root = new JSONObject(payload);
            JSONObject data = root.optJSONObject("messageData");
            if (data == null) data = root.optJSONObject("data");
            if (data == null) data = root.optJSONObject("message");
            if (data == null) data = root;

            boolean eventConfigUpdated = updateServerConfigFromCallEvent(root);
            if (!eventConfigUpdated && data != root) {
                updateServerConfigFromCallEvent(data);
            }

            String type = data.optString("type", root.optString("type", ""));
            if ("Session_Start".equalsIgnoreCase(type)) {
                sessionId = data.optString("sessionId", "");
                Log.i(TAG, "rtm=Session_Start sessionAvailable=" + !TextUtils.isEmpty(sessionId));
                logReadinessState();
            } else if ("Bot_Active".equalsIgnoreCase(type)) {
                JSONObject recentSession = data.optJSONObject("recentSessionInfo");
                if (recentSession != null && recentSession.optBoolean("isActive")) {
                    String activeSessionId = recentSession.optString("sessionId", "");
                    if (!TextUtils.isEmpty(activeSessionId)) {
                        sessionId = activeSessionId;
                        Log.i(TAG, "rtm=Bot_Active sessionAvailable=true");
                        logReadinessState();
                    }
                }
            } else if ("sbc_details_response".equalsIgnoreCase(type)) {
                JSONObject response = data.optJSONObject("responseData");
                if (response == null) response = root.optJSONObject("responseData");
                if (response != null) {
                    serverConfig = new JSONObject(response.toString());
                    if (serverConfig.optJSONArray("iceServers") == null) {
                        serverConfig.put("iceServers", new JSONArray());
                    }
                }
                int addressCount = serverConfig == null || serverConfig.optJSONArray("addresses") == null
                        ? 0 : serverConfig.optJSONArray("addresses").length();
                Log.i(TAG, "rtm=sbc_details_response received=" + (response != null)
                        + " domainAvailable=" + (serverConfig != null && !TextUtils.isEmpty(serverConfig.optString("domain")))
                        + " addressCount=" + addressCount);
                logReadinessState();
                return true;
            }
        } catch (JSONException exception) {
            Log.w(TAG, "Could not inspect RTM click-to-call metadata", exception);
        }
        return false;
    }

    /** Uses the server details already present in the regular agent WebRTC event when available. */
    private static boolean updateServerConfigFromCallEvent(JSONObject event) throws JSONException {
        JSONObject message = event.optJSONObject("message");
        if (message == null || !BundleConstants.CALL_AGENT_WEBRTC.equalsIgnoreCase(message.optString("type"))) {
            return false;
        }

        String domain = message.optString("domain", "");
        JSONArray addresses = message.optJSONArray("addresses");
        if (TextUtils.isEmpty(domain) || addresses == null || addresses.length() == 0) {
            Log.i(TAG, "rtm=call_agent_webrtc_event configAvailable=false");
            return false;
        }

        JSONObject config = new JSONObject();
        config.put("domain", domain);
        config.put("addresses", addresses);
        JSONArray iceServers = message.optJSONArray("iceServers");
        config.put("iceServers", iceServers == null ? new JSONArray() : iceServers);
        serverConfig = config;
        Log.i(TAG, "rtm=call_agent_webrtc_event configAvailable=true addressCount=" + addresses.length());
        logReadinessState();
        return true;
    }

    public static synchronized boolean isReady() {
        return "ready".equals(getReadinessBlocker());
    }

    /** Returns a non-sensitive reason when the branded call button is waiting. */
    public static synchronized String getReadinessBlocker() {
        if (!enabledByBranding) return "branding_disabled";
        if (!socketOpen) return "rtm_socket_closed";
        if (TextUtils.isEmpty(SDKConfiguration.Client.clickToCallFlowId)) return "flow_id_missing";
        if (TextUtils.isEmpty(sessionId)) return "session_id_missing";
        if (serverConfig == null) return "sbc_response_missing";
        if (botClient == null) return "bot_client_missing";

        BotInfoModel botInfo = botClient.getBotInfoModel();
        if (botInfo == null || TextUtils.isEmpty(botInfo.taskBotId)) return "task_bot_id_missing";
        if (TextUtils.isEmpty(botClient.getUserId())) return "user_id_missing";
        if (TextUtils.isEmpty(serverConfig.optString("domain"))) return "sbc_domain_missing";
        JSONArray addresses = serverConfig.optJSONArray("addresses");
        if (addresses == null || addresses.length() == 0) return "sbc_addresses_missing";
        return "ready";
    }

    private static void logReadinessState() {
        String readiness = getReadinessBlocker();
        if (!readiness.equals(lastLoggedReadiness)) {
            lastLoggedReadiness = readiness;
            Log.i(TAG, "readiness=" + readiness);
        }
    }

    public static synchronized JSONObject makeCallConfig(Context context) throws JSONException {
        if (!isReady()) throw new IllegalStateException("Click-to-call configuration is not ready yet");

        BotInfoModel botInfo = botClient.getBotInfoModel();
        String userId = botClient.getUserId();
        JSONArray headers = new JSONArray();
        headers.put("X-CALLFLOW-ID: " + SDKConfiguration.Client.clickToCallFlowId);
        headers.put("X-CALLFLOW-STATE: published");
        headers.put("X-RTM-SESSION-ID: " + sessionId);
        headers.put("X-RTM-USER-ID: " + userId);
        headers.put("X-RTM-URL: android-app://" + context.getPackageName());

        JSONObject config = new JSONObject(serverConfig.toString());
        config.put("sipIdentity", userId);
        config.put("target", botInfo.taskBotId);
        config.put("callType", "audio");
        config.put("agentName", "Voice Agent");
        config.put("headers", headers);
        config.put("isClickToCall", true);
        return config;
    }

    public static synchronized void sendStartEvent() {
        if (botClient != null) botClient.sendClickToCallEvent("close_agent_chat", "click_to_call", true);
    }
}
