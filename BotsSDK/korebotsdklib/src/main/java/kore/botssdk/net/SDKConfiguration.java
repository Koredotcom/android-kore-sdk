package kore.botssdk.net;

/*
 * Copyright (c) 2014 Kore Inc. All rights reserved.
 */

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.View;

import java.util.HashMap;

/**
 * This class is for defining properties
 */
@SuppressLint("UnknownNullness")
public class SDKConfiguration {

    public static final String APP_REQ_COLOR = "#3942f6"; // KORA COLOR "#3942f6" // BMC COLOR 2f91e5
    /**
     * bot init text  and related settings
     */
    private static boolean TRIGGER_INIT_MESSAGE = false;
    private static String INIT_MESSAGE = "Welpro";
    private static boolean TIME_STAMPS_REQUIRED = true;
    private static final boolean APPLY_FONT_STYLE = true;
    protected static HashMap<String, View> hsh = new HashMap<>();

    public static boolean isTriggerInitMessage() {
        return TRIGGER_INIT_MESSAGE;
    }

    public static void setTriggerInitMessage(boolean triggerInitMessage) {
        TRIGGER_INIT_MESSAGE = triggerInitMessage;
    }

    public static String getInitMessage() {
        return INIT_MESSAGE;
    }

    public static void setInitMessage(String initMessage) {
        INIT_MESSAGE = initMessage;
    }


    public static boolean isApplyFontStyle() {
        return APPLY_FONT_STYLE;
    }

    //JWTServer related configurations
    public static class JWTServer {
        static String JWT_SERVER_URL = "https://mk2r2rmj21.execute-api.us-east-1.amazonaws.com/dev/";
    }

    //Server related configurations
    public static class Server {
        public static void setKoreBotServerUrl(String koreBotServerUrl) {
            KORE_BOT_SERVER_URL = koreBotServerUrl;
        }

        public static void setServerUrl(String serverUrl) {
            SERVER_URL = serverUrl;
        }

        static String KORE_BOT_SERVER_URL = "https://bots.kore.ai/";//https://qa-bots.kore.ai";
        public static final String TTS_WS_URL = "wss://speech.kore.ai/tts/ws";
        public static final boolean IS_ANONYMOUS_USER = false;
        public static String SERVER_URL = "https://bots.kore.ai/"; // "https://demo.kore.net";
        public static final String TOKEN_SERVER_URL = "https://demodpd.kore.ai/";
        public static final String Branding_SERVER_URL = "https://bots.kore.ai/";
        public static final String koreAPIUrl = "https://sit-bots.kore.ai/";
        public static HashMap<String, Object> queryParams = new HashMap<>();

        public static RestResponse.BotCustomData customData = new RestResponse.BotCustomData();

        public static void setQueryParams(HashMap<String, Object> queryParams) {
            Server.queryParams = queryParams;
        }

        public static void setCustomData(RestResponse.BotCustomData customData) {
            Server.customData = customData;
        }
    }

    public static class Client {
        public static final String client_id = "cs-1e845b00-81ad-5757-a1e7-d0f6fea227e9";
        public static final String client_secret = "5OcBSQtH/k6Q/S6A3bseYfOee02YjjLLTNoT1qZDBso=";
        public static String identity = "email@kore.com";
        public static final String bot_name = "SDKBot";
        public static final String bot_id = "st-b9889c46-218c-58f7-838f-73ae9203488c";
        public static final boolean enable_ack_delivery = false;
        public static final boolean isWebHook = false;
        public static final String webHook_client_id = "cs-ab324147-4c82-5eb5-b73e-42cf8d8340f8";
        public static final String webHook_client_secret = "kD9HrB5CPeneebDZFXpRmUxamx55NfVsx0t4nVr78v8=";
        public static final String webHook_identity = "sudheer.jampana@kore.com";
        public static final String webHook_bot_id = "st-fd0f5024-2318-56fe-8354-555e1786133e";

        //Weebhook
        // for webhook based communication use following option
        public static String webhookURL = "https://qa1-bots.kore.ai/chatbot/v2/webhook/st-ea1b128f-7895-581a-8c87-bbfe3b9f1ff1";
        public static int apiVersion = 2;
    }

    public static class BubbleColors {
        public static String rightBubbleSelected = APP_REQ_COLOR;

        public static void setRightBubbleSelected(String rightBubbleSelected) {
            BubbleColors.rightBubbleSelected = rightBubbleSelected;
        }

        public static void setRightBubbleUnSelected(String rightBubbleUnSelected) {
            BubbleColors.rightBubbleUnSelected = rightBubbleUnSelected;
        }

        public static void setLeftBubbleSelected(String leftBubbleSelected) {
            BubbleColors.leftBubbleSelected = leftBubbleSelected;
        }

        public static void setLeftBubbleUnSelected(String leftBubbleUnSelected) {
            BubbleColors.leftBubbleUnSelected = leftBubbleUnSelected;
        }

        public static void setLeftBubbleTextColor(String leftBubbleTextColor) {
            BubbleColors.leftBubbleTextColor = leftBubbleTextColor;
        }

        public static void setRightBubbleTextColor(String rightBubbleTextColor) {
            BubbleColors.rightBubbleTextColor = rightBubbleTextColor;
        }

        public static void setWhiteColor(String whiteColor) {
            BubbleColors.whiteColor = whiteColor;
        }

        public static void setLeftBubbleBorderColor(String leftBubbleBorderColor) {
            BubbleColors.leftBubbleBorderColor = leftBubbleBorderColor;
        }

        public static void setRightLinkColor(String rightLinkColor) {
            BubbleColors.rightLinkColor = rightLinkColor;
        }

        public static void setLeftLinkColor(String leftLinkColor) {
            BubbleColors.leftLinkColor = leftLinkColor;
        }


        public static String rightBubbleUnSelected = APP_REQ_COLOR;
        public static String leftBubbleSelected = "#D3D3D3";
        public static String leftBubbleUnSelected = "#f8f9f8";
        public static String leftBubbleTextColor = "#404051";
        public static String rightBubbleTextColor = "#161628";//"#757587";
        public static String whiteColor = "#FFFFFF";
        public static String leftBubbleBorderColor = "#eeeef2";
        public static String rightLinkColor = APP_REQ_COLOR;
        public static String leftLinkColor = APP_REQ_COLOR;
        public static final boolean BubbleUI = false;
        public static final boolean showIcon = false;

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

        public static String getProfileColor() {
            return profileColor;
        }

        public static void setProfileColor(String profileColor) {
            BubbleColors.profileColor = profileColor;
        }

        static String profileColor = APP_REQ_COLOR;

        public static void setQuickReplyColor(String quickReplyColor) {
            BubbleColors.quickReplyColor = quickReplyColor;
        }

        public static String quickReplyColor = "#EEEEF0";
        public static String quickReplyTextColor = "#000000";
        public static String quickBorderColor = "#000000";

    }

    public static boolean isTimeStampsRequired() {
        return TIME_STAMPS_REQUIRED;
    }

    public static void setTimeStampsRequired(boolean timeStampsRequired) {
        TIME_STAMPS_REQUIRED = timeStampsRequired;
    }

    /**
     * don't use relative it is licenced version
     */
    public enum FONT_TYPES {
        ROBOTO, RELATIVE
    }

    private static final FONT_TYPES fontType = FONT_TYPES.ROBOTO;

    public static void setCustomTemplateView(String templateName, View templateView) {
        hsh.put(templateName, templateView);
        Log.e("HashMap Count", hsh.size() + "");
    }

    public static HashMap<String, View> getCustomTemplateView() {
        return hsh;
    }

}
