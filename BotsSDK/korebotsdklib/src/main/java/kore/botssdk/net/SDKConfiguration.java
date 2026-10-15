package kore.botssdk.net;

/*
 * Copyright (c) 2014 Kore Inc. All rights reserved.
 */

import android.annotation.SuppressLint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;

import java.util.HashMap;
import java.util.Locale;

import kore.botssdk.models.BrandingModel;
import kore.botssdk.models.NotificationModel;
import kore.botssdk.websocket.BotStatusListener;

/**
 * This class is for defining properties
 */
@SuppressLint({"UnknownNullness", "HardcodedPassword"})
public class SDKConfiguration {

    public static final String APP_REQ_COLOR = "#3942f6"; // KORA COLOR "#3942f6" // BMC COLOR 2f91e5
    /** Show timestamps on bot and user messages; change at runtime with SDKConfig.setIsTimeStampsRequired(...). */
    private static boolean TIME_STAMPS_REQUIRED = true;
    /** Enable the SDK font styling used by its custom text views. */
    private static final boolean APPLY_FONT_STYLE = true;
    protected static HashMap<String, View> hsh = new HashMap<>();
    protected static HashMap<String, Class<?>> hshViewHolders = new HashMap<>();
    /** Language tag applied by the sample app before opening chat; change it to localize the SDK. */
    public static String appLanguage = "en";
    /**
     * Initial message date format; {S} inserts the day suffix, such as st, nd, rd or th.
     * Use SDKConfig.setBubbleDateFormat(...) to change the active format at runtime.
     */
    public static final String DEFAULT_BUBBLE_DATE_FORMAT = "MMM dd'{S}' 'at' h:mm a";
    private static Locale LOCALE_DEVICE;

    public static boolean isApplyFontStyle() {
        return APPLY_FONT_STYLE;
    }

    private static Typeface regular;
    private static Typeface bold;
    private static Typeface semiBold;

    public static void setFontFamily(Typeface regularTf,
                                     Typeface semiBoldTf,
                                     Typeface boldTf) {
        regular = regularTf;
        semiBold = semiBoldTf;
        bold = boldTf;
    }

    public static Typeface getRegular() { return regular; }

    public static Typeface getSemiBold() { return semiBold; }

    public static Typeface getBold() { return bold; }

    //JWTServer related configurations
    @SuppressLint({"HardcodedPassword", "emptyPassword"})
    public static class JWTServer {
        public static void setJwtServerUrl(String jwtServerUrl) {
            JWT_SERVER_URL = jwtServerUrl;
        }
        /** Token-generation endpoint used when jwt_token is empty. */
        static String JWT_SERVER_URL = "PLEASE_ENTER_JWT_SERVER_URL";
        /** Existing JWT for bot authentication; an empty value requests SDK token generation using the client credentials. */
        static String jwt_token = "PLEASE_ENTER_JWT_TOKEN";

        public static void setJwt_token(String jwt_token) {
            JWTServer.jwt_token = jwt_token;
        }

        public static String getJwt_token() {
            return jwt_token;
        }
    }

    //Server related configurations
    @SuppressLint("HardcodedPassword")
    public static class Server {
        public static void setServerUrl(String serverUrl) {
            SERVER_URL = serverUrl;
        }
        /** Runtime push-notification registration details, populated after authentication. */
        public static NotificationModel notificationModel;
        /** WebSocket endpoint used by the text-to-speech service. */
        public static final String TTS_WS_URL = "wss://speech.kore.ai/tts/ws";
        /** Base URL of the Kore.ai bot platform used for authentication and bot APIs. */
        public static String SERVER_URL = "PLEASE_ENTER_SERVER_URL";
        /** Base URL used to retrieve the bot chat appearance and branding. */
        public static String Branding_SERVER_URL = "PLEASE_ENTER_BRANDING_SERVER_URL";
        /** Push device token/identifier; leave empty when push registration is not required. */
        public static String notificationDeviceId = "";
        /** Extra WebSocket URL parameters; the current q1/q2/q3 values preserve the sample configuration. */
        public static HashMap<String, Object> queryParams = createQueryParams();
        /** Bot-specific data sent during connection, and on each message when update_custom_data_to_user_message is true. */
        public static RestResponse.BotCustomData customData = createCustomData();
        /** Host callback for connection, message, session and deep-link events; register it from the Activity. */
        public static BotStatusListener botStatusListener;

        private static HashMap<String, Object> createQueryParams() {
            HashMap<String, Object> params = new HashMap<>();
            // Sample boolean, numeric and string query parameters appended to the WebSocket URL.
            params.put("q1", true);
            params.put("q2", 4);
            params.put("q3", "connect");
            return params;
        }

        private static RestResponse.BotCustomData createCustomData() {
            RestResponse.BotCustomData data = new RestResponse.BotCustomData();
            // Preserve the sample token custom-data entry; replace/add keys for the bot's needs.
            data.put("jwt_token", JWTServer.getJwt_token());
            return data;
        }

        public static void setBrandingUrl(String url) {
            Branding_SERVER_URL = url;
        }

        public static void setQueryParams(HashMap<String, Object> queryParams) {
            Server.queryParams = queryParams;
        }

        public static void setCustomData(RestResponse.BotCustomData customData) {
            Server.customData = customData;
        }

        public static void setNotificationDeviceId(String notificationDeviceId) {
            Server.notificationDeviceId = notificationDeviceId;
        }

        public static void setBotStatusListener(BotStatusListener botStatusListener) {
            Server.botStatusListener = botStatusListener;
        }

        public static void setNotificationModel(NotificationModel notificationModel) {
            Server.notificationModel = notificationModel;
        }

        public static NotificationModel getNotificationModel() {
            return notificationModel;
        }

        public static BotStatusListener getBotStatusListener() {
            return botStatusListener;
        }
    }

    @SuppressLint("HardcodedPassword")
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
        /** Web/Mobile Client channel app ID, required when the SDK generates a JWT. */
        public static String client_id = "PLEASE_ENTER_CLIENT_ID";
        /** Web/Mobile Client channel app secret, required when the SDK generates a JWT. */
        public static String client_secret = "PLEASE_ENTER_CLIENT_SECRET";
        /** End-user identity used to authenticate and associate the chat session. */
        public static String identity = "PLEASE_ENTER_IDENTITY";
        /** Bot display name used by the sample chat and connection. */
        public static String bot_name = "Kore.ai Bot";
        /** Identifier of the bot to connect to; required for bot requests. */
        public static String bot_id = "PLEASE_ENTER_BOT_ID";
        /** Optional ConnectionMode query value; empty uses the normal SDK connection behavior. */
        public static String connection_mode = "";
        /** With a nonempty connection_mode, reuse it during reconnects; false sends Reconnect instead. */
        public static boolean connection_mode_on_reconnect = false;
        /** Reload history after the network resumes to recover missed messages. */
        public static boolean history_on_network_resume = true;
        /** Acknowledge incoming bot messages to the server when supported by the connection. */
        public static boolean enable_ack_delivery = false;

        /** Use HTTP webhook messaging when true, or the WebSocket transport when false. */
        public static boolean isWebHook = false;
    }

    @SuppressLint("HardcodedPassword")
    public static class BubbleColors {
        /** Apply localBranding values over remote branding when true; false uses the server branding. */
        public static boolean enableLocalBranding = false;
        /** Local appearance values copied from the sample; used only when enableLocalBranding is true. */
        public static BrandingModel localBranding = createLocalBranding();
        /** Default accent/border fallback used by supported option views. */
        public static String rightBubbleUnSelected = APP_REQ_COLOR;
        /** Bot-bubble selection/highlight color, which branding can override. */
        public static String leftBubbleSelected = "#D3D3D3";
        /** Shared white color value for SDK styling. */
        public static String whiteColor = "#FFFFFF";
        /** Default hyperlink color in user messages. */
        public static String rightLinkColor = APP_REQ_COLOR;
        /** Default hyperlink color in bot messages. */
        public static String leftLinkColor = APP_REQ_COLOR;
        /** Placeholder text in the chat composer, which branding can override. */
        public static String footer_hint_text = "Type your message...";
        /** Show the bot avatar beside responses; showIconTop controls its placement. */
        public static boolean showIcon = true;

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
        /** Optional local drawable resource for the bot avatar; -1 means no resource override. */
        private static int icon = -1;
        /** Optional URL for the bot avatar image. */
        private static String icon_url = "";
        /** Optional agent avatar drawable supplied by the host app. */
        private static Drawable agentAvatar;
        /** Optional image URL for the agent avatar. */
        private static String agent_url = "";
        /** Quick-reply button background, which branding can override. */
        public static String quickReplyColor = "#EEEEF0";
        /** Quick-reply label color, which branding can override. */
        public static String quickReplyTextColor = "#000000";
        /** Quick-reply button border color, which branding can override. */
        public static String quickBorderColor = "#000000";

        private static BrandingModel createLocalBranding() {
            BrandingModel brandingModel = new BrandingModel();
            // Bot message bubble background.
            brandingModel.setBotchatBgColor("#F3F5F8");
            // Bot message text.
            brandingModel.setBotchatTextColor("#202124");
            // Active button background.
            brandingModel.setButtonActiveBgColor("#0D6EFD");
            // Active button label.
            brandingModel.setButtonActiveTextColor("#ffffff");
            // Inactive button background.
            brandingModel.setButtonInactiveBgColor("#0659d2");
            // Inactive button label.
            brandingModel.setButtonInactiveTextColor("#ffffff");
            // User message bubble background.
            brandingModel.setUserchatBgColor("#0D6EFD");
            // User message text.
            brandingModel.setUserchatTextColor("#ffffff");
            // Chat header background.
            brandingModel.setWidgetHeaderColor("#0D6EFD");
            // Chat composer background.
            brandingModel.setWidgetFooterColor("#FFFFFF");
            // Chat composer border.
            brandingModel.setWidgetFooterBorderColor("#E4E5E7");
            // Composer placeholder color.
            brandingModel.setWidgetFooterHintColor("#262626");
            // Composer placeholder shown before the user types.
            brandingModel.setWidgetFooterHintText("Type your message...");
            // Chat widget text.
            brandingModel.setWidgetTextColor("#ffffff");
            // Chat widget border.
            brandingModel.setWidgetBorderColor("#E4E5E7");
            // Template button border.
            brandingModel.setButtonBorderColor("#0D6EFD");
            // Chat conversation background.
            brandingModel.setWidgetBodyColor("#ffffff");
            // Display name used when local branding is enabled.
            brandingModel.setBotName("Bot Name");
            // Message bubble shape, such as square.
            brandingModel.setChatBubbleStyle("square");
            return brandingModel;
        }

        public static void setAgentAvatar(Drawable agent_Avatar, String agentUrl) {
            agentAvatar = agent_Avatar;
            agent_url = agentUrl;
        }

        public static Drawable getAgentAvatar() {
            return agentAvatar;
        }

        public static String getAgent_url() {
            return agent_url;
        }
    }

    /** Chat behavior defaults. Branding responses may override the supported branding/history options. */
    @SuppressLint("HardcodedPassword")
    public static class OverrideKoreConfig {
        /** Convert supported emoji shortcuts in bot text to emoji characters. */
        public static boolean isEmojiShortcutEnable = false;
        /** Stored typing-indicator timeout in milliseconds; populated by branding, not read by the current renderer. */
        public static int typing_indicator_timeout = 10000;
        /** Allow the SDK to retrieve previous chat messages. */
        public static boolean history_enable = true;
        /** Number of recent messages requested per history batch. */
        public static int history_batch_size = 10;
        /** Fetch older history when the conversation is scrolled upward. */
        public static boolean paginated_scroll_enable = true;
        /** Number of older messages requested per scrolling history batch. */
        public static int paginated_scroll_batch_size = 10;
        /** Stored history loading label; populated by branding, not read by the current renderer. */
        public static String paginated_scroll_loading_label = "Loading old messages";
        /** Place the bot avatar at the top of its response; false places it at the bottom. */
        public static boolean showIconTop = false;
        /** Show the attachment control so users can attach supported files. */
        public static boolean showAttachment = true;
        /** Show the speech-to-text microphone control in the composer. */
        public static boolean showASRMicroPhone = true;
        /** Show the speaker control for reading bot messages aloud. */
        public static boolean showTextToSpeech = true;
        /** Show the hamburger/menu control in compatible footer implementations, such as the sample custom footer. */
        public static boolean showHamburgerMenu = false;
        /** Request history on the initial connection when history_enable is also true. */
        public static boolean history_initial_call = false;
        /** Hide the host action bar in the SDK chat Activity. */
        public static boolean disable_action_bar = true;
        /** Suppress the alert displayed after reconnection attempts reach their limit. */
        public static boolean disable_alert_on_max_reconnection = true;
        /** Include Server.customData with every outgoing user message. */
        public static boolean update_custom_data_to_user_message = true;
        /** Post a local notification for a new bot message while the chat is in the background. */
        public static boolean showLocalNotification = false;
        /** Let the SDK retry a failed WebSocket connection; false leaves retries to the host app. */
        public static boolean reconnectionBySDK = false;
        /** Send URL clicks to BotStatusListener.onDeepLinkClicked instead of opening the SDK web view. */
        public static boolean sendAllDeepLink = false;
        /**
         * Allow button templates to submit even when their message is not the last chat item.
         * Defaults to false. After a payload/title submission, all buttons in that template
         * are always disabled, regardless of this flag. URL navigation alone does not consume it.
         */
        public static boolean enableButtonClicksOnAnyPosition = false;
        /** Let the SDK manage push registration/unregistration when a notification device ID is set. */
        public static boolean default_notifications = true;
        /** Show the Answered by AI label on supported bot responses. */
        public static boolean showAnsweredByAI = true;
        /** Show the SDK chat header. */
        public static boolean showHeader = true;
        /** Show the minimize control in the SDK chat header. */
        public static boolean showHeaderMinimize = true;
        /** Match the Android status bar color to the chat header background. */
        public static boolean updateStatusBarColor = true;
    }

    public static boolean isTimeStampsRequired() {
        return TIME_STAMPS_REQUIRED;
    }

    public static void setTimeStampsRequired(boolean timeStampsRequired) {
        TIME_STAMPS_REQUIRED = timeStampsRequired;
    }

    public static Locale getDeviceLocale()
    {
        return LOCALE_DEVICE;
    }

    public static void setDeviceLocale(Locale locale)
    {
        LOCALE_DEVICE = locale;
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
