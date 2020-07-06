package com.kore.ai.widgetsdk.applicationcontrol;

/**
 * Created by Ramachandra Pradeep Challa
 * Kore.ai
 */

public class ACMEngine {

    public static final byte MODE_ONLY_CHAT = 2;//0+2+0
//    public static final byte MODE_ALL = 6;//1+2+3
//    public static final byte MODE_LEFT_CHAT = 3;//1+2
//    public static final byte MODE_CHAT_RIGHT = 5;//2+3


//    public static byte LEFT_INDEX;
//    public static byte MIDDLE_INDEX;
//    public static byte RIGHT_INDEX;
//    public static byte PAGER_COUNT;
//    public static byte PAGER_MODE;

  /*  private byte ENABLE_MOBILE_APP = 0;
    private byte ENABLE_SYSTEM_TRAY = 0;
    private byte ENABLE_BROWSER_PLUGIN = 0;
    private byte ENABLE_MEETINGS = 0;
    private byte ENABLE_TASK = 0;
    private byte ENABLE_KNOWLEDGE = 0;
    private byte ENABLE_ANNOUNCEMENT = 0;
    private byte ENABLE_EMAIL = 0;
    private byte ENABLE_DRIVE = 0;
    private byte ENABLE_SKILL = 0;*/

    public static ApplicationControl appControl;

    public static void processACMList(ACMModel model){

        if(model == null){
//            createViewPagerConstants(MODE_ONLY_CHAT);
            appControl = new ApplicationControl();
            appControl.resetWithPositives();
            return;
        }
        ACMEngine.appControl = model.getApplicationControl();
//        ApplicationControl appControl = model.getApplicationControl();

//        boolean leftPage = (appControl.getENABLE_MEETINGS() == 1 || appControl.getENABLE_TASK() == 1 || appControl.getENABLE_DRIVE() == 1);
//        boolean rightPage = (appControl.getENABLE_KNOWLEDGE() == 1 || appControl.getENABLE_ANNOUNCEMENT() == 1);
//        byte currentState;
//        if(leftPage && rightPage)currentState = MODE_ALL;
//        else if(leftPage)currentState = MODE_LEFT_CHAT;
//        else if(rightPage)currentState = MODE_CHAT_RIGHT;
//        else currentState = MODE_ONLY_CHAT;
//        createViewPagerConstants(MODE_ONLY_CHAT);

    }

   /* private static void createViewPagerConstants(byte currentState){
        PAGER_MODE = currentState;
        switch (currentState){
            case MODE_ONLY_CHAT:
                LEFT_INDEX = -1;
                RIGHT_INDEX = -1;
                MIDDLE_INDEX = 0;
                PAGER_COUNT = 1;
                break;
            *//*case MODE_ALL:
                LEFT_INDEX = 0;
                RIGHT_INDEX = 2;
                MIDDLE_INDEX = 1;
                PAGER_COUNT = 3;
                break;
            case MODE_LEFT_CHAT:
                LEFT_INDEX = 0;
                RIGHT_INDEX = -1;
                MIDDLE_INDEX = 1;
                PAGER_COUNT = 2;
                break;
            case MODE_CHAT_RIGHT:
                LEFT_INDEX = -1;
                RIGHT_INDEX = 1;
                MIDDLE_INDEX = 0;
                PAGER_COUNT = 2;
                break;*//*

        }
    }*/

}