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
    public static class Config {

        /*
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
         */
        public static final String demo_client_id = "cs-a3d69bdb-996e-5551-b78e-996deb62039f";// Copy this value from Bot Builder SDK Settings ex. cs-5250bdc9-6bfe-5ece-92c9-ab54aa2d4285
        public static final String clientSecret = "1Jm6IHQt8PfunGjgtV444wfEff3YnwtYd1tjmZElXIY=";// Copy this value from Bot Builder SDK Settings ex. Wibn3ULagYyq0J10LCndswYycHGLuIWbwHvTRSfLwhs=
        public static String identity = "demo@example.com";// This should represent the subject for JWT token. This can be an email or phone number, in case of known user, and in case of anonymous user, this can be a randomly generated unique id.

        public static final String chatBotName = "BBCConsu";// Copy this value from Bot Builder -> Channels -> Web/Mobile Config  ex. "Demo Bot"
        public static final String botId = "st-a03b7d94-8823-568f-b3aa-a0bf80a91e16"; // Copy this value from Bot Builder -> Channels -> Web/Mobile Config  ex. st-acecd91f-b009-5f3f-9c15-7249186d827d

        public static final String KORE_BOT_SERVER_URL = "https://pilot-bots.kore.com/";// Replace it with your on-premise server URL, if required
        public static boolean IS_ANONYMOUS_USER = false; // If not anonymous, you should send same identity (such as email or phone number) while making a connection, so as to remember the user state across sessions.
        //For speech to text
        public static final String SPEECH_SERVER_BASE_URL = "wss://speech.kore.ai/stream/kore/decode";

        public static final String JWT_SERVER_URL = "http://50.19.64.173:4000";

    }

}
