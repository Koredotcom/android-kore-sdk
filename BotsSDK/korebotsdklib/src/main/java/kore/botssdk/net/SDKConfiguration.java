package kore.botssdk.net;

/**
 * Created by Ramachandra on 30-May-16.
 * Copyright (c) 2014 Kore Inc. All rights reserved.
 */

import java.util.UUID;

/**
 * This class is for defining properties
 */
public class SDKConfiguration {
    /*public static class Config {

        *//*
        public static final String demo_client_id = "<client-id>";// Copy this value from Bot Builder SDK Settings ex. cs-5250bdc9-6bfe-5ece-92c9-ab54aa2d4285
        public static final String clientSecret = "<client-secret>";// Copy this value from Bot Builder SDK Settings ex. Wibn3ULagYyq0J10LCndswYycHGLuIWbwHvTRSfLwhs=
        public static final String identity = "user@example.com";// This should represent the subject for JWT token. This can be an email or phone number, in case of known user, and in case of anonymous user, this can be a randomly generated unique id.

        public static final String chatBotName = "<bot-name>";// Copy this value from Bot Builder -> Channels -> Web/Mobile Config  ex. "Demo Bot"
        public static final String botId = "<bot-id>"; // Copy this value from Bot Builder -> Channels -> Web/Mobile Config  ex. st-acecd91f-b009-5f3f-9c15-7249186d827d

        public static final String KORE_BOT_SERVER_URL = "https://bots.kore.com/";// Replace it with your on-premise server URL, if required
        public static final boolean IS_ANONYMOUS_USER = true; // If not anonymous, you should send same identity (such as email or phone number) while making a connection, so as to remember the user state across sessions.
        //For speech to text server URL
        public static final String SPEECH_SERVER_BASE_URL = "wss://speech.kore.ai/speechcntxt/ws/speech";

        // Specify the Server URL for JWT token generation. This token is used to authorize the SDK client.
        // Refer to documentation on how to setup the JWT server for token generation
        public static final String JWT_SERVER_URL = "<jwt-token-server-url>";//ex. https://jwt-token-server.example.com/
         *//*
        public static final String demo_client_id = "cs-53775718-1743-5b26-8e16-52d33171a253";//"cs-a3d69bdb-996e-5551-b78e-996deb62039f";// Copy this value from Bot Builder SDK Settings ex. cs-5250bdc9-6bfe-5ece-92c9-ab54aa2d4285
        public static final String clientSecret = "723ItR1V1iXcWuSsS5aRBaN16sKzR4QkwbH2MSmTQcI=";//"1Jm6IHQt8PfunGjgtV444wfEff3YnwtYd1tjmZElXIY=";// Copy this value from Bot Builder SDK Settings ex. Wibn3ULagYyq0J10LCndswYycHGLuIWbwHvTRSfLwhs=
        public static String identity = "abc@gmail.com";//"demo@example.com";// This should represent the subject for JWT token. This can be an email or phone number, in case of known user, and in case of anonymous user, this can be a randomly generated unique id.

        public static final String chatBotName = "ConsumerBot";//"BBCConsu";// Copy this value from Bot Builder -> Channels -> Web/Mobile Config  ex. "Demo Bot"
        public static final String botId = "st-c4e66389-5b6c-5270-8186-e904a8a22357";//"st-a03b7d94-8823-568f-b3aa-a0bf80a91e16"; // Copy this value from Bot Builder -> Channels -> Web/Mobile Config  ex. st-acecd91f-b009-5f3f-9c15-7249186d827d

        public static final String KORE_BOT_SERVER_URL = "http://docker.korebot.com/";//"https://pilot-bots.kore.com/";// Replace it with your on-premise server URL, if required
        public static boolean IS_ANONYMOUS_USER = true; // If not anonymous, you should send same identity (such as email or phone number) while making a connection, so as to remember the user state across sessions.
        //For speech to text
        public static final String SPEECH_SERVER_BASE_URL = "wss://speech.kore.ai/speechcntxt/verizon";//"wss://speech.kore.ai/stream/kore/decode";

        public static final String JWT_SERVER_URL = "http://docker.korebot.com/";//"http://50.19.64.173:4000";

    }*/

    //JWTServer related configurations
    public static class JWTServer{
        public static final String JWT_SERVER_URL = "http://50.19.64.173:4000";
    }

    //Server related configurations
    public static class Server{
        public static final String KORE_BOT_SERVER_URL = "https://qa1-bots.kore.com";//"https://qabots.kore.com";
        public static final String SPEECH_SERVER_BASE_URL = "wss://speech.kore.ai/stream/kore/decode";//"wss://speech.kore.ai/speechcntxt/verizon";
        public static final boolean IS_ANONYMOUS_USER = false;
    }

    public static class Client {
        public static final String client_id = "cs-31d07db4-c7b0-5460-8193-bf68aeea81a3";//"cs-2f6084da-33d0-5b0f-9c66-5b0bbef514f2";
        public static final String client_secret = "jPT8HDU4YfSa+oBoAoTtflvXm+TZP9FjWbcNYMb0d88=";//"T2+PyTibG6f29DcYrhhkoVpD3VSgH/zizJlK8+yMglg=";
        public static final String identity = "riz@testadmin3.xyz";


        public static final String bot_name = "Shopbot_2";
        public static final String bot_id = "st-75bdb67c-894d-520e-a98b-2927a485f604";
    }

}
