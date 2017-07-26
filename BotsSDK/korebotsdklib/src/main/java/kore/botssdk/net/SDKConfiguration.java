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
    public static class JWTServer {
        public static final String JWT_SERVER_URL = "http://demo.kore.net:3000";
    }

    public static class SpeechServer {
        public static final String SPEECH_SERVER_SOCKET_URL = "https://qa-speech.kore.ai";
    }

    //Server related configurations
    public static class Server {
        public static final String KORE_BOT_SERVER_URL = "https://qa-bots.kore.ai";
        public static final boolean IS_ANONYMOUS_USER = false;
    }

    public static class Client {
        public static final String client_id = "cs-aa8ed318-e95c-5955-9680-04cd322e48c6";
        public static final String client_secret = "2o4+bp9/QwjGVMuCwTBpp/tTCvi5ZdRKcecTPOo4T+Y=";
        public static final String identity = "admin@testadmin3.xyz";

        public static final String bot_name = "MyBotM2Enterprise_Mahato";
        public static final String bot_id = "st-c0fb100e-2983-5d0d-ba0e-5c030f678e32";
    }
}
