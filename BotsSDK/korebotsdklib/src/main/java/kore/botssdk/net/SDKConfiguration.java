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
        public static final String KORE_BOT_SERVER_URL = "https://pilot-bots.kore.com";//"https://qabots.kore.com";
        public static final boolean IS_ANONYMOUS_USER = true;
    }

    public static class Client {
        public static final String demo_client_id = "cs-18b8fb30-2c19-5110-949c-a462a8cfb2ac";
        public static final String demo_user_id = "u-73364365-f98d-571d-8e8e-022186cde3bc";
        public static final String demo_auth_token = "mO6emCF8p-rOpDR-cJbQCKg9yTFLqxF_nHEe_d6ZcyBtDnDf3DYpPs9RG3qnqZlS";

        public static final String chatBotName = "National Bank of Oman";
        public static final String taskBotId = "st-edaa5cf8-9d4a-5af3-b86d-421fe53b07ca";
    }

}
