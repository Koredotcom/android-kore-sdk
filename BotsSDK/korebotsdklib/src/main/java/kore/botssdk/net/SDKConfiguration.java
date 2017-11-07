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
        public static final String KORE_BOT_SERVER_URL = "https://pilot-bots.kore.ai";//https://qa-bots.kore.ai";
        public static final String SPEECH_SERVER_BASE_URL = "wss://speech.kore.ai/stream/kore/decode";
        public static final boolean IS_ANONYMOUS_USER = true;
    }

    public static class Client {
        public static final String client_id = "cs-49b31906-d106-55ab-a278-b658a37ed08f";//"cs-5649368e-68bb-555a-8803-935f223db585";
        public static final String client_secret = "2kJJH2DgKDv3ZP6XCDmVkNC6xKepPoeJ26ix1Ly7lqk=";//"AHSubkG09DRdcz9xlzxUXfrxyRx9V0Yhd+6SnXtjYe4=";
        public static final String identity = "udayabhaskar.cc@kore.com";

        public static final String bot_name = " Future Group Uday";//"Twitter_Auth";
        public static final String bot_id = "st-dc1ae300-4e90-5e98-b073-ccfccf65b41b";//"st-cc32974e-c7a2-52d1-83bf-c3dc2b2a9db3";
    }

}
