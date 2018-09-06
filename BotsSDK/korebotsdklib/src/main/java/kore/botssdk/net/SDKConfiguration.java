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
    public static  boolean TRIGGER_INIT_MESSAGE = false;
    public static  String INIT_MESSAGE = "Welpro";
    public static final String BOT_ICON_URL = "";
    //JWTServer related configurations
    public static class JWTServer{
        public static void setJwtServerUrl(String jwtServerUrl) {
            JWT_SERVER_URL = jwtServerUrl;
        }

        public static  String JWT_SERVER_URL = "https://demo.kore.net";
    }

    //Server related configurations
    public static class Server{

        public static void setKoreBotServerUrl(String koreBotServerUrl) {
            KORE_BOT_SERVER_URL = koreBotServerUrl;
        }

        public static void setServerUrl(String serverUrl) {
            SERVER_URL = serverUrl;
        }

        public static  String KORE_BOT_SERVER_URL = "https://qa-bots.kore.ai";//https://qa-bots.kore.ai";
        public static final String SPEECH_SERVER_BASE_URL = "wss://speech.kore.ai/stream/kore/decode";
        public static final String TTS_WS_URL = "wss://speech.kore.ai/tts/ws";
        public static final boolean IS_ANONYMOUS_USER = false;
        public static   String SERVER_URL ="https://demo.kore.net";
    }

    public static class Client {
        public static final String client_id = "cs-5649368e-68bb-555a-8803-935f223db585";
        public static final String client_secret = "AHSubkG09DRdcz9xlzxUXfrxyRx9V0Yhd+6SnXtjYe4=";
        public static final String identity = "reshma@testadmin3.xyz";
        public static final String bot_name = "Twitter_Auth";
        public static final String bot_id = "st-cc32974e-c7a2-52d1-83bf-c3dc2b2a9db3";
    }


    public static class BubbleColors {
        public static final String rightBubbleSelected ="#6168e7";
        public static final String rightBubbleUnSelected="#6168e7";
        public static final String leftBubbleSelected="#D3D3D3";
        public static final String leftBubbleUnSelected="#f8f9f8";
        public static final String leftBubbleTextColor="#444444";
        public static final String rightBubbleTextColor="#FFFFFF";
        public static final String whiteColor="#FFFFFF";
        public static final String leftBubbleBorderColor = "#eeeef2";
        public static final String rightLinkColor="#FFFFFF";
        public static final String leftLinkColor = "#6168e7";

    }


}
