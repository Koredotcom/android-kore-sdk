package com.kore.ai.widgetsdk.applicationcontrol;

public class ApplicationControl {

    private byte ENABLE_MOBILE_APP = 0;
    private byte ENABLE_SYSTEM_TRAY = 0;
    private byte ENABLE_BROWSER_PLUGIN = 0;
    private byte ENABLE_MEETINGS = 0;
    private byte ENABLE_TASK = 0;
    private byte ENABLE_KNOWLEDGE = 0;
    private byte ENABLE_ANNOUNCEMENT = 0;
    private byte ENABLE_EMAIL = 0;
    private byte ENABLE_DRIVE = 0;
    private byte ENABLE_SKILL = 0;

    public void resetWithPositives(){
        ENABLE_MOBILE_APP = 1;
        ENABLE_SYSTEM_TRAY = 1;
        ENABLE_BROWSER_PLUGIN = 1;
        ENABLE_MEETINGS = 1;
        ENABLE_TASK = 1;
        ENABLE_KNOWLEDGE = 1;
        ENABLE_ANNOUNCEMENT = 1;
        ENABLE_EMAIL = 1;
        ENABLE_DRIVE = 1;
        ENABLE_SKILL = 1;
    }

}

