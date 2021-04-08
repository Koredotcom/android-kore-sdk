package kore.botssdk.net;

/**
 * Created by Ramachandra on 30-May-16.
 * Copyright (c) 2014 Kore Inc. All rights reserved.
 */

import kore.botssdk.utils.StringUtils;

/**
 * This class is for defining properties
 */
public class SDKConfiguration {

    public static String APP_REQ_COLOR = "#3942f6"; // KORA COLOR "#3942f6" // BMC COLOR 2f91e5
    /**
     * bot init text  and related settings
     */

    private static boolean TRIGGER_INIT_MESSAGE = true;
    private static String INIT_MESSAGE = "Welpro";
    public static final String BOT_ICON_URL = "";

    private static boolean TIME_STAMPS_REQUIRED = true;
    private static boolean APPLY_FONT_STYLE = true;



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

    public static void setApplyFontStyle(boolean applyFontStyle) {
        APPLY_FONT_STYLE = applyFontStyle;
    }

    public static FONT_TYPES getFontType() {
        return fontType;
    }

    public static void setFontType(FONT_TYPES fontType) {
        SDKConfiguration.fontType = fontType;
    }


    //JWTServer related configurations
    public static class JWTServer {
        public static void setJwtServerUrl(String jwtServerUrl) {
            JWT_SERVER_URL = jwtServerUrl;
        }

        static  String JWT_SERVER_URL = "https://mk2r2rmj21.execute-api.us-east-1.amazonaws.com/dev";
    }

    //Server related configurations
    public static class Server {

        public static void setKoreBotServerUrl(String koreBotServerUrl) {
            KORE_BOT_SERVER_URL = koreBotServerUrl;
        }

        public static void setServerUrl(String serverUrl) {
            SERVER_URL = serverUrl;
        }

        public static void setServerIp(String serverIp){
            SERVER_IP = serverIp;
        }

        public static void setServerPort(String serverPort){
            SERVER_PORT = serverPort;
        }

        public static void setIsPresenceSsl(boolean isPresenceSsl){
            IS_PRESENCE_SSL = isPresenceSsl;
        }

        public static final String SPEECH_SERVER_BASE_URL = "wss://speech.kore.ai/stream/kore/decode";
        public static final String TTS_WS_URL = "wss://speech.kore.ai/tts/ws";
        public static final boolean IS_ANONYMOUS_USER = true;
        public static String SERVER_IP = "qa.kore.ai";
        public static String SERVER_PORT = "443";
        public static Boolean IS_PRESENCE_SSL = true;
        public static String TOKEN_SERVER_URL ="https://staging-bankassist.korebots.com";
        public static String Branding_SERVER_URL ="https://wb.korebots.com/";

        //Mashreq
        static  String KORE_BOT_SERVER_URL = "https://wb-bots.korebots.com/api/";//https://qa-bots.kore.ai";
        public static   String SERVER_URL = "https://wb-bots.korebots.com/api/";

        //JwtGrant
        public static String JWT_SERVER_URL = "https://demodpd.kore.ai/api/";
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

        public static String tenant_id = "6040d03a7db76200b8e16a97";
        public static String uniqueuserId = "ffdcuser2_mlz-b2c-sandbox";
        public static boolean enablePanel = false;

        //Mashreq
        public static String client_id = "";
        public static String client_secret = "";
        public static String identity = "";
        public static String bot_name = "";
        public static String bot_id = "";
        public static String authorization = "";
        public static String xauth = "";
    }

    public static class BubbleColors {
        public static  String rightBubbleSelected = APP_REQ_COLOR;

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


        public static  String rightBubbleUnSelected=APP_REQ_COLOR;
        public static  String leftBubbleSelected="#D3D3D3";
        public static  String leftBubbleUnSelected="#f8f9f8";
        public static  String leftBubbleTextColor="#404051";
        public static  String rightBubbleTextColor="#161628";//"#757587";
        public static  String whiteColor="#FFFFFF";
        public static  String leftBubbleBorderColor = "#eeeef2";
        public static  String rightLinkColor=APP_REQ_COLOR;
        public static  String leftLinkColor = APP_REQ_COLOR;
        public static final boolean BubbleUI = false;
        public static final boolean showIcon = false;

        public static int getIcon() {
            return icon;
        }

        public static void setIcon(int icon) {
            BubbleColors.icon = icon;
        }

        private static int icon = -1;


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

        public static  String quickReplyColor = "#EEEEF0";
        public static  String quickReplyTextColor = "#000000";

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

    private static FONT_TYPES fontType = FONT_TYPES.ROBOTO;

}
