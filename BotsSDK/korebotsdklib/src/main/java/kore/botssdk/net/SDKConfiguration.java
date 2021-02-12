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
//    private static String INIT_MESSAGE = "Welpro";
    public static final String BOT_ICON_URL = "";

    private static boolean TIME_STAMPS_REQUIRED = true;
    private static boolean APPLY_FONT_STYLE = true;



    public static boolean isTriggerInitMessage() {
        return TRIGGER_INIT_MESSAGE;
    }

    public static void setTriggerInitMessage(boolean triggerInitMessage) {
        TRIGGER_INIT_MESSAGE = triggerInitMessage;
    }

//    public static String getInitMessage() {
//        return INIT_MESSAGE;
//    }
//
//    public static void setInitMessage(String initMessage) {
//        INIT_MESSAGE = initMessage;
//    }


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


//        static  String JWT_SERVER_URL = "https://demo.kore.net";
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

//        static  String KORE_BOT_SERVER_URL = "https://bots.kore.ai";//https://qa-bots.kore.ai";
        public static final String SPEECH_SERVER_BASE_URL = "wss://speech.kore.ai/stream/kore/decode";
        public static final String TTS_WS_URL = "wss://speech.kore.ai/tts/ws";
        public static final boolean IS_ANONYMOUS_USER = true;
//        public static   String SERVER_URL = "https://bots.kore.ai"; // "https://demo.kore.net";
        public static String SERVER_IP = "qa.kore.ai";
        public static String SERVER_PORT = "443";
        public static Boolean IS_PRESENCE_SSL = true;
//        public static String TOKEN_SERVER_URL ="https://demodpd.kore.ai";
        public static String TOKEN_SERVER_URL ="https://staging-bankassist.korebots.com";

//        static  String KORE_BOT_SERVER_URL = "https://bankingassistant-qa.kore.ai";//https://qa-bots.kore.ai";
//        public static   String SERVER_URL = "https://bankingassistant-qa.kore.ai"; // "https://demo.kore.net";

        //Staging
//        static  String KORE_BOT_SERVER_URL = "https://bankingassistant-stg.kore.ai";//https://qa-bots.kore.ai";
//        public static   String SERVER_URL = "https://bankingassistant-stg.kore.ai"; // "https://demo.kore.net";

        //Production
//        static  String KORE_BOT_SERVER_URL = "https://bankassist.kore.ai";//https://qa-bots.kore.ai";
//        public static   String SERVER_URL = "https://bankassist.kore.ai";

        //Production 2
        static  String KORE_BOT_SERVER_URL = "https://bankassist.kore.ai/workbench/";//https://qa-bots.kore.ai";
        public static   String SERVER_URL = "https://bankassist.kore.ai/workbench/";
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

//        public static String client_id = "cs-3dd6a21c-70ef-5d52-b742-7b9dcf6a6a89";//"cs-96c4747a-bb79-58b0-9dca-0dcf6c6148cf";//"cs-dc0f84ac-4751-5293-b254-6a0a382ab08c";//"cs-a269ad0a-45ec-5b41-9950-18571e42a6a4";//"cs-5649368e-68bb-555a-8803-935f223db585";
//        public static String client_secret = "Zf/sPAMUgD4Hl/y6nUELodw6DJu9cuzaytcAyqL5gO8=";//"qc4c+FOpEo88m27BgECpcS/oC/CKDWa8u70ll0qr4MM=";//"MiFzNLLWTQZddj1HOmdM4iyePhQ+gED4jdUg88Ujh1Y=";//"kmZ7ck9wRxSVV2dNNwi2P3UZI3qacJgu7JL9AmZapS4=";//"AHSubkG09DRdcz9xlzxUXfrxyRx9V0Yhd+6SnXtjYe4=";
//        public static String identity = "anilkumar.routhu@kore.com";
//        public static String bot_name = "CanCan";//"Android test";//"NewTemplates";
//        public static String bot_id = "st-05303785-9992-526c-a83c-be3252fd478e";//"st-2e4c9eaf-070c-5b86-8020-add76f37e3a2";//"st-05303785-9992-526c-a83c-be3252fd478e";//"st-caecd28f-64ed-5224-a612-7a3d3a870aed";//"st-cc32974e-c7a2-52d1-83bf-c3dc2b2a9db3";
        public static String tenant_id = "601cd30102a93848fa1b2a2c"; // "601d04d76a7c774a391fafab"; //"5ff46e176b35062143cce27d"; // "5f818b297b86a9659e1075f2";// "mlz-b2c-sandbox"; // "5f9274c15b6a927ae14dce42";
        public static String uniqueuserId = "ffdcuser2_mlz-b2c-sandbox";
        public static boolean enablePanel = false;

        //Non Finastra
//        public static String client_id = "cs-a376418f-5b9e-5a33-9020-37a986cb043f";//"cs-96c4747a-bb79-58b0-9dca-0dcf6c6148cf";//"cs-dc0f84ac-4751-5293-b254-6a0a382ab08c";//"cs-a269ad0a-45ec-5b41-9950-18571e42a6a4";//"cs-5649368e-68bb-555a-8803-935f223db585";
//        public static String client_secret = "TCbLY18QhY4z6mxyLUPzjE6xgapiIkmm2b3rygmdAvA=";//"qc4c+FOpEo88m27BgECpcS/oC/CKDWa8u70ll0qr4MM=";//"MiFzNLLWTQZddj1HOmdM4iyePhQ+gED4jdUg88Ujh1Y=";//"kmZ7ck9wRxSVV2dNNwi2P3UZI3qacJgu7JL9AmZapS4=";//"AHSubkG09DRdcz9xlzxUXfrxyRx9V0Yhd+6SnXtjYe4=";
//        public static String identity = "sudheer.jampana@kore.com";
//        public static String bot_name = "Banking Assist";//"Android test";//"NewTemplates";
//        public static String bot_id = "st-0d759d37-c8e8-57f8-a340-aadbb8aea91d";

        //Non Fintra Staging
//        public static String client_id = "cs-94fdf33f-9637-5c12-9c23-0f49244a81c3";//"cs-96c4747a-bb79-58b0-9dca-0dcf6c6148cf";//"cs-dc0f84ac-4751-5293-b254-6a0a382ab08c";//"cs-a269ad0a-45ec-5b41-9950-18571e42a6a4";//"cs-5649368e-68bb-555a-8803-935f223db585";
//        public static String client_secret = "0s9e2wCuomUnQdWegAjXJQgWJlF/YH5SxqyN0hfKX5g=";//"qc4c+FOpEo88m27BgECpcS/oC/CKDWa8u70ll0qr4MM=";//"MiFzNLLWTQZddj1HOmdM4iyePhQ+gED4jdUg88Ujh1Y=";//"kmZ7ck9wRxSVV2dNNwi2P3UZI3qacJgu7JL9AmZapS4=";//"AHSubkG09DRdcz9xlzxUXfrxyRx9V0Yhd+6SnXtjYe4=";
//        public static String identity = "sudheer.jampana@kore.com";
//        public static String bot_name = "Banking Assist";//"Android test";//"NewTemplates";
//        public static String bot_id = "st-b846e2bc-9bae-514e-9d20-6b54f938e993";

        //Production Env
//        public static String client_id = "cs-cde002cd-0e1f-5606-8085-3ff144d64bd4";//"cs-96c4747a-bb79-58b0-9dca-0dcf6c6148cf";//"cs-dc0f84ac-4751-5293-b254-6a0a382ab08c";//"cs-a269ad0a-45ec-5b41-9950-18571e42a6a4";//"cs-5649368e-68bb-555a-8803-935f223db585";
//        public static String client_secret = "vupD3nKQnxm420pXSSAW8xzoB6ufbKtakU3m5C06G5A=";//"qc4c+FOpEo88m27BgECpcS/oC/CKDWa8u70ll0qr4MM=";//"MiFzNLLWTQZddj1HOmdM4iyePhQ+gED4jdUg88Ujh1Y=";//"kmZ7ck9wRxSVV2dNNwi2P3UZI3qacJgu7JL9AmZapS4=";//"AHSubkG09DRdcz9xlzxUXfrxyRx9V0Yhd+6SnXtjYe4=";
//        public static String identity = "Sudheer.Jampana@kore.com";//"Prodaccout2@abc.com";
//        public static String bot_name = "Bank Assist Dev";//"Android test";//"NewTemplates";
//        public static String bot_id = "st-7fb7a8d6-7fd5-51a1-9099-fa1cca07f900";

//        public static String client_id = "cs-273f48cd-bdcf-578b-a0c3-da7e3c7656c9";//"cs-96c4747a-bb79-58b0-9dca-0dcf6c6148cf";//"cs-dc0f84ac-4751-5293-b254-6a0a382ab08c";//"cs-a269ad0a-45ec-5b41-9950-18571e42a6a4";//"cs-5649368e-68bb-555a-8803-935f223db585";
//        public static String client_secret = "yq/1iOs0CYjqRkd02gCC6lPZF3xzIEJVHcROZLUsLlY=";//"qc4c+FOpEo88m27BgECpcS/oC/CKDWa8u70ll0qr4MM=";//"MiFzNLLWTQZddj1HOmdM4iyePhQ+gED4jdUg88Ujh1Y=";//"kmZ7ck9wRxSVV2dNNwi2P3UZI3qacJgu7JL9AmZapS4=";//"AHSubkG09DRdcz9xlzxUXfrxyRx9V0Yhd+6SnXtjYe4=";
//        public static String identity = "Sudheer.Jampana@kore.com";//"Prodaccout2@abc.com";
//        public static String bot_name = "Bank Assist Dev";//"Android test";//"NewTemplates";
//        public static String bot_id = "st-f0b6c048-c54f-5108-a6b6-353e738b04ab";

        //Sudheer Uppunuthula
        public static String client_id = "cs-19161101-00f1-585e-81ad-49f297e5b7a6";//"cs-96c4747a-bb79-58b0-9dca-0dcf6c6148cf";//"cs-dc0f84ac-4751-5293-b254-6a0a382ab08c";//"cs-a269ad0a-45ec-5b41-9950-18571e42a6a4";//"cs-5649368e-68bb-555a-8803-935f223db585";
        public static String client_secret = "pW2V8EbM7pTpHTBLZZjBQw5OC7r6tP0nbyQWN9i+2r0=";//"qc4c+FOpEo88m27BgECpcS/oC/CKDWa8u70ll0qr4MM=";//"MiFzNLLWTQZddj1HOmdM4iyePhQ+gED4jdUg88Ujh1Y=";//"kmZ7ck9wRxSVV2dNNwi2P3UZI3qacJgu7JL9AmZapS4=";//"AHSubkG09DRdcz9xlzxUXfrxyRx9V0Yhd+6SnXtjYe4=";
        public static String identity = "Sudheer.Jampana@kore.com";//"Prodaccout2@abc.com";
        public static String bot_name = "Bank Assist Dev";//"Android test";//"NewTemplates";
        public static String bot_id = "st-2a29a672-c194-5471-b458-0b11b073296f";

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
