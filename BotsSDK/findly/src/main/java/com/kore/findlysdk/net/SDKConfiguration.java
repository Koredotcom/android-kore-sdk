package com.kore.findlysdk.net;

/**
 * Created by Ramachandra on 30-May-16.
 * Copyright (c) 2014 Kore Inc. All rights reserved.
 */

/**
 * This class is for defining properties
 */
public class SDKConfiguration {

    public static final String SDIX = "";
    public static final String TOKEN = "";
    public static String APP_REQ_COLOR = "#3942f6"; // KORA COLOR "#3942f6" // BMC COLOR 2f91e5
    /**
     * bot init text  and related settings
     */
    private static  boolean TRIGGER_INIT_MESSAGE = false;

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

    private static  String INIT_MESSAGE = "";
    public static final String BOT_ICON_URL = "";

    public static boolean isApplyFontStyle() {
        return APPLY_FONT_STYLE;
    }

    public static void setApplyFontStyle(boolean applyFontStyle) {
        APPLY_FONT_STYLE = applyFontStyle;
    }

    //JWTServer related configurations
    public static class JWTServer{
        public static void setJwtServerUrl(String jwtServerUrl) {
            JWT_SERVER_URL = jwtServerUrl;
        }

//        static  String JWT_SERVER_URL = "https://demo.kore.net";
        static  String JWT_SERVER_URL = "https://mk2r2rmj21.execute-api.us-east-1.amazonaws.com/";
    }

    //Server related configurations
    public static class Server{

        public static void setKoreBotServerUrl(String koreBotServerUrl) {
            KORE_BOT_SERVER_URL = koreBotServerUrl;
        }

        public static void setServerUrl(String serverUrl) {
            SERVER_URL = serverUrl;
        }

        public static void setWidgetsServerUrl(String widgetServerUrl) {
            WIDGETS_SERVER_URL = widgetServerUrl;
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

//        static  String KORE_BOT_SERVER_URL = "https://qa1-bots.kore.ai";//"https://bots.kore.ai";
//        public static String SERVER_URL ="https://qa1-bots.kore.ai";
//        public static String WIDGETS_SERVER_URL = "https://qa1-bots.kore.ai";

        //Staging
//        static  String KORE_BOT_SERVER_URL = "https://staging-bots.korebots.com";//"https://bots.kore.ai";
//        public static String SERVER_URL ="https://staging-bots.korebots.com";
//        public static String WIDGETS_SERVER_URL = "https://staging-bots.korebots.com";

        //Pilot
//        static  String KORE_BOT_SERVER_URL = "https://pilot-bots.kore.com/";//"https://bots.kore.ai";
//        public static String SERVER_URL ="https://pilot-bots.kore.com/";
        public static String WIDGETS_SERVER_URL = "https://pilot-bots.kore.com/";

        //Findly
        static  String KORE_BOT_SERVER_URL = "https://app.findly.ai/";//"https://bots.kore.ai";
        public static String SERVER_URL ="https://app.findly.ai/";

        public static String SERVER_IP = "qa.kore.ai";
        public static String SERVER_PORT = "443";
        public static Boolean IS_PRESENCE_SSL = true;
        public static final String SPEECH_SERVER_BASE_URL = "wss://speech.kore.ai/stream/kore/decode";
        public static final String TTS_WS_URL = "wss://speech.kore.ai/tts/ws";
        public static final boolean IS_ANONYMOUS_USER = true;
    }

    public static class Client {
//        public static  String client_id = "cs-3c71459e-5f83-52ae-b27f-3e49602c322a";

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

//        public static  String client_secret = "O2HzK1jnGSj/1L47vhxkca/4+mK7LEAJ6E2S4WEjp8g=";
//        public static  String identity = "sri.harsha@kore.com";
//        public static  String bot_name = "Widget Sdk";
//        public static  String bot_id = "st-c490c315-160e-5df6-a17a-c9eeeb3de19b";

//        public static  String client_id = "cs-d9e501a4-633e-59e0-8660-ceb32cec9a4c";
//        public static  String client_secret = "3ez7zBmwlt9F5V5Nh5oi8Y7oDdjxCzYZ78DFqgX/Kvo=";
//        public static  String identity = "sri.harsha@kore.com";
//        public static  String bot_name = "SDK_WIDGET_MAIN";
//        public static  String bot_id = "st-5baee3ee-0254-5f9b-8f13-f06da3a0fc5c";

        //Staging Bot
//        public static String client_id = "cs-1dae92c7-47b9-51c4-8dcd-d81f711283a0";//"cs-96c4747a-bb79-58b0-9dca-0dcf6c6148cf";//"cs-dc0f84ac-4751-5293-b254-6a0a382ab08c";//"cs-a269ad0a-45ec-5b41-9950-18571e42a6a4";//"cs-5649368e-68bb-555a-8803-935f223db585";
//        public static String client_secret = "2VJj2L713bmGxETvP6AyvLx8AVfBHEbmDRpZ8y5wFfo=";//"qc4c+FOpEo88m27BgECpcS/oC/CKDWa8u70ll0qr4MM=";//"MiFzNLLWTQZddj1HOmdM4iyePhQ+gED4jdUg88Ujh1Y=";//"kmZ7ck9wRxSVV2dNNwi2P3UZI3qacJgu7JL9AmZapS4=";//"AHSubkG09DRdcz9xlzxUXfrxyRx9V0Yhd+6SnXtjYe4=";
//        public static String identity = "subrahmanyam.donepudi@kore.com";
//        public static String bot_name = "Banking Solution IVR Demo";//"Android test";//"NewTemplates";
//        public static String bot_id = "st-c6ea2967-8e9e-510d-9f30-4841f9f8d8f1";

        //Pilot
        public static String client_id = "cs-acf51d04-7530-5acb-b843-f23031114308";//"cs-96c4747a-bb79-58b0-9dca-0dcf6c6148cf";//"cs-dc0f84ac-4751-5293-b254-6a0a382ab08c";//"cs-a269ad0a-45ec-5b41-9950-18571e42a6a4";//"cs-5649368e-68bb-555a-8803-935f223db585";
        public static String client_secret = "XbudDUnUM61RSOENcPz9d5s13j4hGGA7DSRDGe+yZws=";//"qc4c+FOpEo88m27BgECpcS/oC/CKDWa8u70ll0qr4MM=";//"MiFzNLLWTQZddj1HOmdM4iyePhQ+gED4jdUg88Ujh1Y=";//"kmZ7ck9wRxSVV2dNNwi2P3UZI3qacJgu7JL9AmZapS4=";//"AHSubkG09DRdcz9xlzxUXfrxyRx9V0Yhd+6SnXtjYe4=";
        public static String identity = "subrahmanyam.donepudi@kore.com";
        public static String bot_name = "Banking Solution IVR Demo";//"Android test";//"NewTemplates";
        public static String bot_id = "st-13202efd-b6fe-593c-882f-78234643eed3";
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

    private static boolean TIME_STAMPS_REQUIRED = true;
    private static boolean APPLY_FONT_STYLE = true;


}
