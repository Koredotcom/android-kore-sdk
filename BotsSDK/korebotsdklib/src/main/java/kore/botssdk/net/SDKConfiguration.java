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
        public static final String KORE_BOT_SERVER_URL = "https://qa1-bots.kore.com";//https://pilot-bots.kore.com/";//https://bots.kore.com";//"https://qabots.kore.com";
        public static final boolean IS_ANONYMOUS_USER = false;
        //For speech to text
        public static final String SERVER_IP = "demo.kore.com";
    }



    public static class Client {

        public static final String demo_client_id = "cs-21340ce3-b7c6-5544-bc04-84f26188febf";//"cs-649b8bb3-b1c4-5807-b998-d26c8f80b9be";//"cs-784b3387-3ca3-55d9-a577-f3e5e6fd5284";
        public static final String demo_user_id = "u-29fffa9b-32c4-59c2-9ebd-58a543b6a58d";
        public static final String demo_auth_token = "a5U-caQyO35tFeWjBJWZmKf1KLbRvPKiganLjxA0erQzdpLSKtGnK8m6zIuqimeH";//"Bdol8LYrwWfW_Vq72tdcVqrTDEOfzXJ0TRXvltzqos9kog51SVlq323uZG-a9-gk";//"U6_IoMcLJh3he8tJg-G6_FEY1XvCMAPT_VB-s9AzblQmfzDMAo6HTfC0c17iLSi-";

        public static final String chatBotName = "AsanaRiz";//"Asana";// "Teamlease Virtual Assistant";
        public static final String taskBotId = "st-63e6d4f3-03bd-5180-b26f-e6b40b3baf82";//"st-fedfb1a7-f5b9-5604-9fa8-204875d653b5";//"st-66eae596-55d3-5487-bc0b-72d452466079";//"st-b6c56d62-2e89-5a43-bee6-5538982c48cf";


//        public static final String demo_client_id = "cs-b8f91198-1971-5f1e-82e2-1132ae170b0d";//"cs-784b3387-3ca3-55d9-a577-f3e5e6fd5284";
//        public static final String demo_user_id = "u-29fffa9b-32c4-59c2-9ebd-58a543b6a58d";
//        public static final String demo_auth_token = "U6_IoMcLJh3he8tJg-G6_FEY1XvCMAPT_VB-s9AzblQmfzDMAo6HTfC0c17iLSi-";
//
//        public static final String chatBotName = "TeamLeasePoC";
//        public static final String taskBotId = "st-eba29e21-f51e-51c1-9049-c8bc001c6686";//"st-b6c56d62-2e89-5a43-bee6-5538982c48cf";
    }

}
