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
    public static final boolean TRIGGER_INIT_MESSAGE = true;
    public static final String INIT_MESSAGE = "Welpro";
    public static final String BOT_ICON_URL = "";
    //JWTServer related configurations
    public static class JWTServer{
        public static final String JWT_SERVER_URL = "http://demo.kore.net:3000";
    }

    //Server related configurations
    public static class Server{
        public static final String KORE_BOT_SERVER_URL = "https://pilot-bots.kore.ai";//https://qa-bots.kore.ai";
        public static final String SPEECH_SERVER_BASE_URL = "wss://speech.kore.ai/stream/kore/decode";
        public static final String TTS_WS_URL = "wss://speech.kore.ai/tts/ws";
        public static final boolean IS_ANONYMOUS_USER = true;
    }

    public static class Client {
        public static final String client_id = "cs-e1ce2209-a1b6-5969-b852-2d7d4312aed2";//"cs-5649368e-68bb-555a-8803-935f223db585";
        public static final String client_secret = "fpQnMB1YFYlAZCFHZMJ16NhwxNoe1YW8um9NyEeDJ0Q=";//"AHSubkG09DRdcz9xlzxUXfrxyRx9V0Yhd+6SnXtjYe4=";
        public static final String identity = "udayabhaskar.cc@kore.com";

        public static final String bot_name = "FG Order Details";//"Twitter_Auth";
        public static final String bot_id = "st-6417d1b9-c9b7-5995-a0fe-7c6347611fde";//"st-cc32974e-c7a2-52d1-83bf-c3dc2b2a9db3";

    }


}
