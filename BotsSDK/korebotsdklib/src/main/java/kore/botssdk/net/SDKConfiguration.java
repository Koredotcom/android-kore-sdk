package kore.botssdk.net;

/**
 * Created by Ramachandra on 30-May-16.
 * Copyright (c) 2014 Kore Inc. All rights reserved.
 */

/**
 * This class is for defining properties
 */
public class SDKConfiguration {

    public static String APP_REQ_COLOR = "#CCCCCC"; // KORA COLOR "#3942f6" // BMC COLOR 2f91e5
    /**
     * bot init text  and related settings
     */

    private static boolean TRIGGER_INIT_MESSAGE = false;
    private static String INIT_MESSAGE = "Welpro";
    public static String BOT_ICON_URL = "";

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


        //CanCan
//        static  String JWT_SERVER_URL = "https://demo.kore.net";
        //Citi Client
//            static  String JWT_SERVER_URL = "https://demo.kore.net/users/sts";
        //In line template
        static  String JWT_SERVER_URL = "https://demo.kore.net/users/sts";
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

//        public static  String KORE_BOT_SERVER_URL = "https://bots.kore.ai";//https://qa-bots.kore.ai";//"https://bots.kore.ai";
//        public static   String SERVER_URL ="https://bots.kore.ai";
        public static   String TOKEN_SERVER_URL ="https://demodpd.kore.ai";

//        static  String KORE_BOT_SERVER_URL = "https://bankingassistant-stg.kore.ai";//"https://bots.kore.ai";
//        public static String SERVER_URL ="https://bankingassistant-stg.kore.ai";

        //QA
//        static String KORE_BOT_SERVER_URL = "https://bankingassistant-qa.kore.ai";//https://qa-bots.kore.ai";
//        public static String SERVER_URL ="https://bankingassistant-qa.kore.ai";


        //        static  String KORE_BOT_SERVER_URL = "https://pilot-bots.kore.com/";//"https://bots.kore.ai";
//        public static String SERVER_URL ="https://pilot-bots.kore.com/";
        public static String BANKING_SERVER_URL ="https://demo.kore.ai/";

        //Findly
        static  String KORE_BOT_SERVER_URL = "https://pilot.findly.ai";//"https://bots.kore.ai";
        public static String SERVER_URL ="https://pilot.findly.ai";


    }

    public static class Client {

        public static void setClient_id(String client_id) {
            Client.client_id = client_id;
        }

        public static void setClient_secret(String client_secret) {
            Client.client_secret = client_secret;
        }

        public static void setIdentity(String identity) {
            Client.identity = Math.random()+"";
        }

        public static void setBot_name(String bot_name) {
            Client.bot_name = bot_name;
        }

        public static void setBot_id(String bot_id) {
            Client.bot_id = bot_id;
        }

        public static void setTenant_id(String tenant_id) {
            Client.tenant_id = tenant_id;
        }

        public static String getTenant_id() {
            return tenant_id;
        }

        //        public static String client_id = "cs-3dd6a21c-70ef-5d52-b742-7b9dcf6a6a89";//"cs-96c4747a-bb79-58b0-9dca-0dcf6c6148cf";//"cs-dc0f84ac-4751-5293-b254-6a0a382ab08c";//"cs-a269ad0a-45ec-5b41-9950-18571e42a6a4";//"cs-5649368e-68bb-555a-8803-935f223db585";
//        public static String client_secret = "Zf/sPAMUgD4Hl/y6nUELodw6DJu9cuzaytcAyqL5gO8=";//"qc4c+FOpEo88m27BgECpcS/oC/CKDWa8u70ll0qr4MM=";//"MiFzNLLWTQZddj1HOmdM4iyePhQ+gED4jdUg88Ujh1Y=";//"kmZ7ck9wRxSVV2dNNwi2P3UZI3qacJgu7JL9AmZapS4=";//"AHSubkG09DRdcz9xlzxUXfrxyRx9V0Yhd+6SnXtjYe4=";
//        public static String identity = "anilkumar.routhu@kore.com";
//        public static String bot_name = "CanCan";//"Android test";//"NewTemplates";
//        public static String bot_id = "st-05303785-9992-526c-a83c-be3252fd478e";//"st-2e4c9eaf-070c-5b86-8020-add76f37e3a2";//"st-05303785-9992-526c-a83c-be3252fd478e";//"st-caecd28f-64ed-5224-a612-7a3d3a870aed";//"st-cc32974e-c7a2-52d1-83bf-c3dc2b2a9db3";

        //Work Bench QA
//        public static String client_id = "cs-e28ed93e-0723-5e22-b8b4-27f8f66de93d";//"cs-96c4747a-bb79-58b0-9dca-0dcf6c6148cf";//"cs-dc0f84ac-4751-5293-b254-6a0a382ab08c";//"cs-a269ad0a-45ec-5b41-9950-18571e42a6a4";//"cs-5649368e-68bb-555a-8803-935f223db585";
//        public static String client_secret = "ezWdID0H6fu8rJUKLXjKRyMTeIFXmswm0Q/d+dwFkc0=";//"qc4c+FOpEo88m27BgECpcS/oC/CKDWa8u70ll0qr4MM=";//"MiFzNLLWTQZddj1HOmdM4iyePhQ+gED4jdUg88Ujh1Y=";//"kmZ7ck9wRxSVV2dNNwi2P3UZI3qacJgu7JL9AmZapS4=";//"AHSubkG09DRdcz9xlzxUXfrxyRx9V0Yhd+6SnXtjYe4=";
//        public static String identity = "ushasri.gundupinnu@kore.com";
//        public static String bot_name = "QA_Env_WB";//"Android test";//"NewTemplates";
//        public static String bot_id = "st-13bd4aa0-8980-5226-984f-271b6468c848";

        //Staging Bot
//        public static String client_id = "cs-9c811447-60b2-5a25-b4f5-e03c0493f262";//"cs-96c4747a-bb79-58b0-9dca-0dcf6c6148cf";//"cs-dc0f84ac-4751-5293-b254-6a0a382ab08c";//"cs-a269ad0a-45ec-5b41-9950-18571e42a6a4";//"cs-5649368e-68bb-555a-8803-935f223db585";
//        public static String client_secret = "jTTTEgdvJ1t1Wo+9yVPSMRpM+LrWs3V3f30WubbubIw=";//"qc4c+FOpEo88m27BgECpcS/oC/CKDWa8u70ll0qr4MM=";//"MiFzNLLWTQZddj1HOmdM4iyePhQ+gED4jdUg88Ujh1Y=";//"kmZ7ck9wRxSVV2dNNwi2P3UZI3qacJgu7JL9AmZapS4=";//"AHSubkG09DRdcz9xlzxUXfrxyRx9V0Yhd+6SnXtjYe4=";
//        public static String identity = "ushasri.gundupinnu@kore.com";
//        public static String bot_name = "Banking Staging ENV Bot";//"Android test";//"NewTemplates";
//        public static String bot_id = "st-a9413ab9-f1e0-5c26-a23d-2f2fa19ad856";

        public static String tenant_id = "5f33b99cff636e7ae858df1c";

        //        IKEA Coworker
//        public static String client_id = "cs-5cd31fe5-cc44-5ab1-8b9c-104637d393fe";//"cs-96c4747a-bb79-58b0-9dca-0dcf6c6148cf";//"cs-dc0f84ac-4751-5293-b254-6a0a382ab08c";//"cs-a269ad0a-45ec-5b41-9950-18571e42a6a4";//"cs-5649368e-68bb-555a-8803-935f223db585";
//        public static String client_secret = "LB2BtYsg4zWjZ6fprvRGhDG9ShX4TrkMNajiATImyIk=";//"qc4c+FOpEo88m27BgECpcS/oC/CKDWa8u70ll0qr4MM=";//"MiFzNLLWTQZddj1HOmdM4iyePhQ+gED4jdUg88Ujh1Y=";//"kmZ7ck9wRxSVV2dNNwi2P3UZI3qacJgu7JL9AmZapS4=";//"AHSubkG09DRdcz9xlzxUXfrxyRx9V0Yhd+6SnXtjYe4=";
//        public static String identity = "henrikjansen72@gmail.com";
//        public static String bot_name = "IKEA Coworker";//"Android test";//"NewTemplates";
//        public static String bot_id = "st-3e4fb572-3e9b-57ae-abd2-13fa1799f947";

        //        IKEA Manager
//        public static String client_id = "cs-49695009-17b1-557d-821a-87ae2516fbf7";//"cs-96c4747a-bb79-58b0-9dca-0dcf6c6148cf";//"cs-dc0f84ac-4751-5293-b254-6a0a382ab08c";//"cs-a269ad0a-45ec-5b41-9950-18571e42a6a4";//"cs-5649368e-68bb-555a-8803-935f223db585";
//        public static String client_secret = "tIcDPU9UlUEgSXcxt491mhj5uVP91Bv4IKLAKZhc/5A=";//"qc4c+FOpEo88m27BgECpcS/oC/CKDWa8u70ll0qr4MM=";//"MiFzNLLWTQZddj1HOmdM4iyePhQ+gED4jdUg88Ujh1Y=";//"kmZ7ck9wRxSVV2dNNwi2P3UZI3qacJgu7JL9AmZapS4=";//"AHSubkG09DRdcz9xlzxUXfrxyRx9V0Yhd+6SnXtjYe4=";
//        public static String identity = "henrikjansen72@gmail.com";
//        public static String bot_name = "IKEA Manager";//"Android test";//"NewTemplates";
//        public static String bot_id = "st-6180af24-7567-5c04-8782-53002fd6e810";


        //        IKEA Buddy
//        public static String client_id = "cs-dfc84d2c-cd14-5b2a-a73a-baf2c210f275";//"cs-96c4747a-bb79-58b0-9dca-0dcf6c6148cf";//"cs-dc0f84ac-4751-5293-b254-6a0a382ab08c";//"cs-a269ad0a-45ec-5b41-9950-18571e42a6a4";//"cs-5649368e-68bb-555a-8803-935f223db585";
//        public static String client_secret = "lxeZGHfmsxFjRCo8e0LH0aUe0efK52UFg4vMvQnc//o=";//"qc4c+FOpEo88m27BgECpcS/oC/CKDWa8u70ll0qr4MM=";//"MiFzNLLWTQZddj1HOmdM4iyePhQ+gED4jdUg88Ujh1Y=";//"kmZ7ck9wRxSVV2dNNwi2P3UZI3qacJgu7JL9AmZapS4=";//"AHSubkG09DRdcz9xlzxUXfrxyRx9V0Yhd+6SnXtjYe4=";
//        public static String identity = "henrikjansen72@gmail.com";
//        public static String bot_name = "IKEA Buddy";//"Android test";//"NewTemplates";
//        public static String bot_id = "st-35cbecc0-2ace-550d-8865-84afd8ba5794";

        //Findly Pilot
        public static String client_id = "cs-99c4522b-d1ff-5cdf-b826-86459d8c835e";//"cs-96c4747a-bb79-58b0-9dca-0dcf6c6148cf";//"cs-dc0f84ac-4751-5293-b254-6a0a382ab08c";//"cs-a269ad0a-45ec-5b41-9950-18571e42a6a4";//"cs-5649368e-68bb-555a-8803-935f223db585";
        public static String client_secret = "dTwkNeAtYB2zs7ZBlRrVLi76jQgwP4j9YBYInqkIUpw=";//"qc4c+FOpEo88m27BgECpcS/oC/CKDWa8u70ll0qr4MM=";//"MiFzNLLWTQZddj1HOmdM4iyePhQ+gED4jdUg88Ujh1Y=";//"kmZ7ck9wRxSVV2dNNwi2P3UZI3qacJgu7JL9AmZapS4=";//"AHSubkG09DRdcz9xlzxUXfrxyRx9V0Yhd+6SnXtjYe4=";
        public static String identity = "raj.peda@kore.com";
        public static String bot_name = "testSDK1";//"Android test";//"NewTemplates";
        public static String bot_id = "st-afc6cf90-4d91-5fa5-aaf3-d7a65f372ae8";
    }

    public static class BubbleColors {
        public static  String rightBubbleSelected = "#0078cd";

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
        public static  String leftBubbleSelected="#ffffff";
        public static  String leftBubbleUnSelected="#f8f9f8";
        public static  String leftBubbleTextColor="#404051";
        public static  String rightBubbleTextColor="#FFFFFF";//"#757587";
        public static  String whiteColor="#FFFFFF";
        public static  String leftBubbleBorderColor = "#eeeef2";
        public static  String rightLinkColor=APP_REQ_COLOR;
        public static  String leftLinkColor = APP_REQ_COLOR;
        public static final boolean BubbleUI = false;
        public static final boolean showIcon = true;
        private static String botIconColor = APP_REQ_COLOR;

        public static String getBotIconColor() {
            return botIconColor;
        }

        public static void setBotIconColor(String botIconColor) {
            BubbleColors.botIconColor = botIconColor;
        }

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
