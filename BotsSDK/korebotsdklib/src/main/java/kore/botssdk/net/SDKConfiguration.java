package kore.botssdk.net;

/**
 * Created by Ramachandra on 30-May-16.
 * Copyright (c) 2014 Kore Inc. All rights reserved.
 */

/**
 * This class is for defining properties
 */
public class SDKConfiguration {
    public static class Server{
        public static final String KORE_BOT_SERVER_URL = "https://qa1-bots.kore.com";//https://qabots.kore.com";//";//https://pilot-bots.kore.com/";//";//"https://bots.kore.com";
        public static final boolean IS_ANONYMOUS_USER = false;
        //For speech to text
        public static final String SERVER_IP = "demo.kore.com";
    }



    public static class Client {

        public static final String demo_client_id = "cs-649b8bb3-b1c4-5807-b998-d26c8f80b9be";//"cs-21340ce3-b7c6-5544-bc04-84f26188febf";////"cs-784b3387-3ca3-55d9-a577-f3e5e6fd5284";
        public static final String demo_user_id = "u-379b7210-9dc0-5f02-8ab8-92a2986c51de";//koreLoginResponse.getUserInfo().getUserId();
        public static final String demo_auth_token = "dQOq8_DOo_y5dwY7uau0woSaYTyFzAa8v3z0TAY6KLocC9HI4PcCCxlmKfjmKxPb";
//        public static final String demo_user_id = "u-29fffa9b-32c4-59c2-9ebd-58a543b6a58d";
//        public static final String demo_auth_token = "ikIidZi8ivr9LeFbI-GQrDvYXtFJ7U1Fg-lRCigX9y7pcw_KjsGSSMtXkayihUbM";

    }

}
