package kore.botssdk.net;

/*
 * Copyright (c) 2014 Kore Inc. All rights reserved.
 */

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.View;

import java.util.HashMap;

import kore.botssdk.models.BrandingModel;

/**
 * This class is for defining properties
 */
@SuppressLint({"UnknownNullness", "HardcodedPassword"})
public class SDKConfiguration {

    public static final String APP_REQ_COLOR = "#3942f6"; // KORA COLOR "#3942f6" // BMC COLOR 2f91e5
    /**
     * bot init text  and related settings
     */
    private static boolean TIME_STAMPS_REQUIRED = true;
    private static final boolean APPLY_FONT_STYLE = true;
    protected static HashMap<String, View> hsh = new HashMap<>();
    protected static HashMap<String, Class<?>> hshViewHolders = new HashMap<>();

    public static boolean isApplyFontStyle() {
        return APPLY_FONT_STYLE;
    }

    //JWTServer related configurations
    public static class JWTServer {
        public static void setJwtServerUrl(String jwtServerUrl) {
            JWT_SERVER_URL = jwtServerUrl;
        }

        static String JWT_SERVER_URL = "PLEASE_ENTER_JWT_SERVER_URL";

        static String jwt_token = "PLEASE_ENTER_JWT_TOKEN";

        public static void setJwt_token(String jwt_token) {
            JWTServer.jwt_token = jwt_token;
        }

        public static String getJwt_token() {
            return jwt_token;
        }
    }

    //Server related configurations
    public static class Server {
        public static void setServerUrl(String serverUrl) {
            SERVER_URL = serverUrl;
        }

        public static final String TTS_WS_URL = "wss://speech.kore.ai/tts/ws";
        public static String SERVER_URL = "PLEASE_ENTER_SERVER_URL";
        public static String Branding_SERVER_URL = "PLEASE_ENTER_BRANDING_SERVER_URL";
        public static HashMap<String, Object> queryParams = new HashMap<>();

        public static RestResponse.BotCustomData customData = new RestResponse.BotCustomData();

        public static void setBrandingUrl(String url) {
            Branding_SERVER_URL = url;
        }

        public static void setQueryParams(HashMap<String, Object> queryParams) {
            Server.queryParams = queryParams;
        }

        public static void setCustomData(RestResponse.BotCustomData customData) {
            Server.customData = customData;
        }
    }

    public static class Client {

        public static void setClient_id(String client_id) {
            Client.client_id = client_id;
        }

        public static void setClient_secret(String client_secret) {
            Client.client_secret = client_secret;
        }

        public static void setIdentity(String identity) {
            Client.identity = identity;
        }

        public static void setBot_name(String bot_name) {
            Client.bot_name = bot_name;
        }

        public static void setBot_id(String bot_id) {
            Client.bot_id = bot_id;
        }

        public static String client_id = "PLEASE_ENTER_CLIENT_ID";
        public static String client_secret = "PLEASE_ENTER_CLIENT_SECRET";
        public static String identity = "PLEASE_ENTER_IDENTITY";
        public static String bot_name = "Kore.ai Bot";
        public static String bot_id = "PLEASE_ENTER_BOT_ID";
        public static String connection_mode = "";
        public static boolean connection_mode_on_reconnect = false;
        public static boolean history_on_network_resume = true;
        public static boolean enable_ack_delivery = false;

        //Webhook
        public static boolean isWebHook = false;
    }

    public static class BubbleColors {
        public static boolean enableLocalBranding = false;
        public static BrandingModel localBranding;
        public static String rightBubbleUnSelected = APP_REQ_COLOR;
        public static String leftBubbleSelected = "#D3D3D3";
        public static String whiteColor = "#FFFFFF";
        public static String rightLinkColor = APP_REQ_COLOR;
        public static String leftLinkColor = APP_REQ_COLOR;
        public static String footer_hint_text = "Type your message...";
        public static boolean showIcon = false;

        public static int getIcon() {
            return icon;
        }

        public static void setIcon(int icon) {
            BubbleColors.icon = icon;
        }

        public static String getIcon_url() {
            return icon_url;
        }

        public static void setIcon_url(String icon_url) {
            BubbleColors.icon_url = icon_url;
        }

        private static int icon = -1;
        private static String icon_url = "";

        public static String quickReplyColor = "#EEEEF0";
        public static String quickReplyTextColor = "#000000";
        public static String quickBorderColor = "#000000";
    }

    public static class OverrideKoreConfig {
        public static boolean isEmojiShortcutEnable = true;
        public static int typing_indicator_timeout = 10000;
        public static boolean history_enable = true;
        public static int history_batch_size = 10;
        public static boolean paginated_scroll_enable = true;
        public static int paginated_scroll_batch_size = 10;
        public static String paginated_scroll_loading_label = "Loading old messages";
        public static boolean showIconTop = true;
        public static boolean showAttachment = true;
        public static boolean showASRMicroPhone = true;
        public static boolean showTextToSpeech = true;
        public static boolean showHamburgerMenu = false;
        public static boolean history_initial_call = false;
        public static boolean disable_action_bar = true;
    }

    public static boolean isTimeStampsRequired() {
        return TIME_STAMPS_REQUIRED;
    }

    public static void setTimeStampsRequired(boolean timeStampsRequired) {
        TIME_STAMPS_REQUIRED = timeStampsRequired;
    }

    public static void setCustomTemplateView(String templateName, View templateView) {
        hsh.put(templateName, templateView);
        Log.e("HashMap Count", hsh.size() + "");
    }

    public static HashMap<String, View> getCustomTemplateView() {
        return hsh;
    }

    public static void setCustomTemplateViewHolder(String templateName, Class<?> viewHolder) {
        hshViewHolders.put(templateName, viewHolder);
        Log.e("HashMap Count", hshViewHolders.size() + "");
    }

    public static Class<?> getCustomTemplateViewHolder(String templateType) {
        return hshViewHolders.get(templateType);
    }

}
