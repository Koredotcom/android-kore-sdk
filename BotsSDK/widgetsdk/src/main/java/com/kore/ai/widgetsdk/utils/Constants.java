package com.kore.ai.widgetsdk.utils;

/**
 * Created by Pradeep Mahato on 08-Jun-16.
 * Copyright (c) 2014 Kore Inc. All rights reserved.
 */
public class Constants {

    //Skill switch name;
    public static  String  SKILL_SELECTION="";
    public static final String SKILL_HOME="Kora";
    public static final String SKILL_UTTERANCE="Ask Kora ";

//    public static final String PUSH_NOTIF_OS_TYPE_ANDROID = "android";
//    public static final String PUSH_NOTIF_DEVICE_ID = "deviceId";
//    public static final String PUSH_NOTIF_OS_TYPE = "osType";

    public static String USER_ID = "userId";
    public static String ACCESS_TOKEN = "accessToken";
    public static String KEY_ASSERTION = "assertion";
    public static String BOT_INFO = "botInfo";

//    public static String UNSECURE_WEBSOCKET_PREFIX = "ws://";
//    public static String SECURE_WEBSOCKET_PREFIX = "wss://";

    public static boolean ENABLE_SDK = false;

    public static String HTTP_AGENT = null;

    public static String getUserAgent(){
        if(HTTP_AGENT == null){
            HTTP_AGENT = System.getProperty("http.agent");
        }
        return HTTP_AGENT;
    }
    public static final String delim = " \n\r\t,;";




}
