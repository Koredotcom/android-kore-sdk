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
        public static final String KORE_BOT_SERVER_URL = "https://bots.kore.ai";//https://qa-bots.kore.ai";
        public static final String SPEECH_SERVER_BASE_URL = "wss://speech.kore.ai/stream/kore/decode";
        public static final String TTS_WS_URL = "wss://speech.kore.ai/tts/ws";
        public static final boolean IS_ANONYMOUS_USER = true;
    }

    public static class Client {
        public static final String client_id = "cs-712d8f22-a03b-5907-8bd0-ae40a0b97759";//"cs-5649368e-68bb-555a-8803-935f223db585";
        public static final String client_secret = "tMblJdGpwetkumvwHezsgYDmZxVG1GbUzLoVxxWmPIs=";//"AHSubkG09DRdcz9xlzxUXfrxyRx9V0Yhd+6SnXtjYe4=";
        public static final String identity = "anil.gara@kore.com";

        public static final String bot_name = "FG Order Demo Bot";//"Twitter_Aurwth";
        public static final String bot_id = "st-aaab7ca3-c880-5086-9ed8-b77f6aa22689";//"st-cc32974e-c7a2-52d1-83bf-c3dc2b2a9db3";

    }


}
