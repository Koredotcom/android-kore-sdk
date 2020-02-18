package kore.botssdk.net;

/**
 * Created by Ramachandra on 30-May-16.
 * Copyright (c) 2014 Kore Inc. All rights reserved.
 */

/**
 * This class is for defining properties
 */
public class SDKConfiguration {
    /**
     * bot init text  and related settings
     */

    private static boolean TRIGGER_INIT_MESSAGE = false;
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

        public static String JWT_SERVER_URL = "https://demo.kore.net/users/sts";

    }

    //Server related configurations
    public static class Server {

        public static void setKoreBotServerUrl(String koreBotServerUrl) {
            KORE_BOT_SERVER_URL = koreBotServerUrl;
        }

        public static void setServerUrl(String serverUrl) {
            SERVER_URL = serverUrl;
        }

        public static String KORE_BOT_SERVER_URL = "https://bots.kore.ai";//https://qa-bots.kore.ai";
        public static final String SPEECH_SERVER_BASE_URL = "wss://speech.kore.ai/stream/kore/decode";
        public static final String TTS_WS_URL = "wss://speech.kore.ai/tts/ws";
        public static final boolean IS_ANONYMOUS_USER = true;
        public static String SERVER_URL = "https://demo.kore.net/users/sts";
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

        public static String client_id = "cs-96c4747a-bb79-58b0-9dca-0dcf6c6148cf";//"cs-dc0f84ac-4751-5293-b254-6a0a382ab08c";//"cs-a269ad0a-45ec-5b41-9950-18571e42a6a4";//"cs-5649368e-68bb-555a-8803-935f223db585";
        public static String client_secret = "qc4c+FOpEo88m27BgECpcS/oC/CKDWa8u70ll0qr4MM=";//"MiFzNLLWTQZddj1HOmdM4iyePhQ+gED4jdUg88Ujh1Y=";//"kmZ7ck9wRxSVV2dNNwi2P3UZI3qacJgu7JL9AmZapS4=";//"AHSubkG09DRdcz9xlzxUXfrxyRx9V0Yhd+6SnXtjYe4=";
        public static String identity = "anilkumar.routhu@kore.com";
        public static String bot_name = "Android test";//"NewTemplates";
        public static String bot_id = "st-2e4c9eaf-070c-5b86-8020-add76f37e3a2";//"st-05303785-9992-526c-a83c-be3252fd478e";//"st-caecd28f-64ed-5224-a612-7a3d3a870aed";//"st-cc32974e-c7a2-52d1-83bf-c3dc2b2a9db3";

    }

    public static class BubbleColors {
        public static String rightBubbleSelected = "#6168e7";

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

        public static String rightBubbleUnSelected = "#6168e7";
        public static String leftBubbleSelected = "#D3D3D3";
        public static String leftBubbleUnSelected = "#f8f9f8";
        public static String leftBubbleTextColor = "#444444";
        public static String rightBubbleTextColor = "#FFFFFF";
        public static String whiteColor = "#FFFFFF";
        public static String leftBubbleBorderColor = "#eeeef2";
        public static String rightLinkColor = "#FFFFFF";
        public static String leftLinkColor = "#6168e7";

        public static String getTypingStatusDotsColor() {
            return typingStatusDotsColor;
        }

        public static void setTypingStatusDotsColor(String typingStatusDotsColor) {
            BubbleColors.typingStatusDotsColor = typingStatusDotsColor;
        }

        public static String typingStatusDotsColor = "#6168e7";

        public static String getQuickReplyFill() {
            return quickReplyFill;
        }

        public static void setQuickReplyFill(String quickReplyFill) {
            BubbleColors.quickReplyFill = quickReplyFill;
        }

        public static String quickReplyFill = "#FFFFFF";

        public static void setQuickReplyColor(String quickReplyColor) {
            BubbleColors.quickReplyColor = quickReplyColor;
        }

        public static String quickReplyColor = "#6168e7";

        public static String getQuickReplyFontColor() {
            return quickReplyFontColor;
        }

        public static void setQuickReplyFontColor(String quickReplyFontColor) {
            BubbleColors.quickReplyFontColor = quickReplyFontColor;
        }

        public static String quickReplyFontColor = "#000000";

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
