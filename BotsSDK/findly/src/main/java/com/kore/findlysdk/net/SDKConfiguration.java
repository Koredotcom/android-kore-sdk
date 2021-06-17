package com.kore.findlysdk.net;

/**
 * Created by Ramachandra on 30-May-16.
 * Copyright (c) 2014 Kore Inc. All rights reserved.
 */

/**
 * This class is for defining properties
 */
public class SDKConfiguration {

    private static String SDIX = "sidx-4c33c7cf-9561-58f2-b547-4745ce12b513"; // "sidx-2c9905a1-73ab-5c78-87fb-1b40f2f3439f"; // "sidx-4c33c7cf-9561-58f2-b547-4745ce12b513"; // "sidx-8fb06646-1d82-56bb-9901-e27fc84611e0"; // "sidx-b3350cec-b831-55e9-925a-3e8a39d67f8c"; // "sidx-666bd120-a112-5d47-b3b2-62cf88505bbf"; // "sidx-965c04cf-97cd-5192-a5ff-165178091863";// Pfizer "sidx-d9006b59-6c8c-5a78-bcbd-00e3e0ceb9aa"; // CVS "sidx-6fff8b04-f206-565c-bb02-fb13ae366fd3"; // Future "sidx-a0d5b74c-ef8d-51df-8cf0-d32617d3e66e";
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

    public static String getSDIX() {
        return SDIX;
    }

    public static void setSDIX(String sdix)
    {
        SDIX = sdix;
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

        public static String WIDGETS_SERVER_URL = "https://pilot-bots.kore.com/";
        public static String BOT_SERVER_URL = "https://dev.findly.ai/";

        //Findly
//        static  String KORE_BOT_SERVER_URL = "https://qa.findly.ai";//"https://bots.kore.ai";
//        public static String SERVER_URL ="https://qa.findly.ai";

        //Dev SearchAssist
        static  String KORE_BOT_SERVER_URL = "https://dev.findly.ai/searchassistapi/searchsdk/stream/";//"https://bots.kore.ai";
        public static String SERVER_URL ="https://dev.findly.ai/searchassistapi/searchsdk/stream/";

        //App SearchAssist
//        static  String KORE_BOT_SERVER_URL = "https://app.findly.ai/searchassistapi/businessapp/searchsdk/stream/";//"https://bots.kore.ai";
//        public static String SERVER_URL ="https://app.findly.ai/searchassistapi/businessapp/searchsdk/stream/";

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


        //Future Bank
//        public static String client_id = "cs-f8ab4184-1db2-5638-be23-81316dfd4169";//"cs-96c4747a-bb79-58b0-9dca-0dcf6c6148cf";//"cs-dc0f84ac-4751-5293-b254-6a0a382ab08c";//"cs-a269ad0a-45ec-5b41-9950-18571e42a6a4";//"cs-5649368e-68bb-555a-8803-935f223db585";
//        public static String client_secret = "csHEMy8x8aN48IJLNqekdBi0phYmsbHqA33y3AzxrAM=";//"qc4c+FOpEo88m27BgECpcS/oC/CKDWa8u70ll0qr4MM=";//"MiFzNLLWTQZddj1HOmdM4iyePhQ+gED4jdUg88Ujh1Y=";//"kmZ7ck9wRxSVV2dNNwi2P3UZI3qacJgu7JL9AmZapS4=";//"AHSubkG09DRdcz9xlzxUXfrxyRx9V0Yhd+6SnXtjYe4=";
//        public static String identity = "raj.peda@kore.com";
//        public static String bot_name = "Future Bank App";//"Android test";//"NewTemplates";
//        public static String bot_id = "st-f83a36b2-d239-5495-b86a-2ec863a4c723";

        //App Credentials
//        public static String client_id = "cs-c111b3d0-ffc2-5446-b025-649f8706296b";//"cs-96c4747a-bb79-58b0-9dca-0dcf6c6148cf";//"cs-dc0f84ac-4751-5293-b254-6a0a382ab08c";//"cs-a269ad0a-45ec-5b41-9950-18571e42a6a4";//"cs-5649368e-68bb-555a-8803-935f223db585";
//        public static String client_secret = "E0M9pGZJ/BSPBzK3gUMvuSvtg0dvk2+8I9lsOGxHW/s=";//"qc4c+FOpEo88m27BgECpcS/oC/CKDWa8u70ll0qr4MM=";//"MiFzNLLWTQZddj1HOmdM4iyePhQ+gED4jdUg88Ujh1Y=";//"kmZ7ck9wRxSVV2dNNwi2P3UZI3qacJgu7JL9AmZapS4=";//"AHSubkG09DRdcz9xlzxUXfrxyRx9V0Yhd+6SnXtjYe4=";
//        public static String identity = "raj.peda@kore.com";
//        public static String bot_name = "June 3";//"Android test";//"NewTemplates";
//        public static String bot_id = "st-21f814e3-78c1-557d-abc7-216573823c1c";

        //Dev Credentials
        public static String client_id = "cs-85d24fec-86e3-5b3e-8fb7-0e296f09fa21";//"cs-96c4747a-bb79-58b0-9dca-0dcf6c6148cf";//"cs-dc0f84ac-4751-5293-b254-6a0a382ab08c";//"cs-a269ad0a-45ec-5b41-9950-18571e42a6a4";//"cs-5649368e-68bb-555a-8803-935f223db585";
        public static String client_secret = "KWf79rvhmDEPjA+G0Z+EKJasibER0oT4/pzvucFAWSQ=";//"qc4c+FOpEo88m27BgECpcS/oC/CKDWa8u70ll0qr4MM=";//"MiFzNLLWTQZddj1HOmdM4iyePhQ+gED4jdUg88Ujh1Y=";//"kmZ7ck9wRxSVV2dNNwi2P3UZI3qacJgu7JL9AmZapS4=";//"AHSubkG09DRdcz9xlzxUXfrxyRx9V0Yhd+6SnXtjYe4=";
        public static String identity = "raj.peda@kore.com";
        public static String bot_name = "31May";//"Android test";//"NewTemplates";
        public static String bot_id = "st-12466d46-dc91-5855-a301-b6ecd7a95d82";

        //Dev Credentials
//        public static String client_id = "cs-cd5db130-8296-55ba-8307-7a87d4ec8fff";//"cs-96c4747a-bb79-58b0-9dca-0dcf6c6148cf";//"cs-dc0f84ac-4751-5293-b254-6a0a382ab08c";//"cs-a269ad0a-45ec-5b41-9950-18571e42a6a4";//"cs-5649368e-68bb-555a-8803-935f223db585";
//        public static String client_secret = "ozk5oqlrWerwpZ6odW9wkKr4WHBebTYDYIqP7LBqP4s=";//"qc4c+FOpEo88m27BgECpcS/oC/CKDWa8u70ll0qr4MM=";//"MiFzNLLWTQZddj1HOmdM4iyePhQ+gED4jdUg88Ujh1Y=";//"kmZ7ck9wRxSVV2dNNwi2P3UZI3qacJgu7JL9AmZapS4=";//"AHSubkG09DRdcz9xlzxUXfrxyRx9V0Yhd+6SnXtjYe4=";
//        public static String identity = "raj.peda@kore.com";
//        public static String bot_name = "May 20";//"Android test";//"NewTemplates";
//        public static String bot_id = "st-ba8e817a-0bcd-5855-bd91-eb2be2fdec30";

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
        public static final boolean filterTopIcon = true;

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
