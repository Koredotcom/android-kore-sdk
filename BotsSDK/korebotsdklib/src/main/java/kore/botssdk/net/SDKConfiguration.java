package kore.botssdk.net;

/**
 * Created by Ramachandra on 30-May-16.
 * Copyright (c) 2014 Kore Inc. All rights reserved.
 */

/**
 * This class is for defining properties
 */
public class SDKConfiguration {
    //JWTServer related configurations
    public static class JWTServer{
        public static final String JWT_SERVER_URL = "http://demo.kore.net:3000";
    }

    //Server related configurations
    public static class Server{
        public static final String KORE_BOT_SERVER_URL = "https://qa-bots.kore.ai";
        public static final String SPEECH_SERVER_BASE_URL = "wss://speech.kore.ai/stream/kore/decode";
        public static final boolean IS_ANONYMOUS_USER = false;
    }

    public static class Client {
        public static final String client_id = "cs-5649368e-68bb-555a-8803-935f223db585";
        public static final String client_secret = "AHSubkG09DRdcz9xlzxUXfrxyRx9V0Yhd+6SnXtjYe4=";
        public static final String identity = "reshma@testadmin3.xyz";

        public static final String bot_name = "Twitter_Auth";
        public static final String bot_id = "st-cc32974e-c7a2-52d1-83bf-c3dc2b2a9db3";
    }

}
