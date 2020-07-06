package com.kore.ai.widgetsdk.applicationcontrol;

/**
 * Created by Ramachandra Pradeep on 6/24/2016.
 */

public class ApplicationControl {

    public byte getENABLE_MOBILE_APP() {
        return ENABLE_MOBILE_APP;
    }

    public void setENABLE_MOBILE_APP(byte ENABLE_MOBILE_APP) {
        this.ENABLE_MOBILE_APP = ENABLE_MOBILE_APP;
    }

    public byte getENABLE_SYSTEM_TRAY() {
        return ENABLE_SYSTEM_TRAY;
    }

    public void setENABLE_SYSTEM_TRAY(byte ENABLE_SYSTEM_TRAY) {
        this.ENABLE_SYSTEM_TRAY = ENABLE_SYSTEM_TRAY;
    }

    public byte getENABLE_BROWSER_PLUGIN() {
        return ENABLE_BROWSER_PLUGIN;
    }

    public void setENABLE_BROWSER_PLUGIN(byte ENABLE_BROWSER_PLUGIN) {
        this.ENABLE_BROWSER_PLUGIN = ENABLE_BROWSER_PLUGIN;
    }

    public byte getENABLE_MEETINGS() {
        return ENABLE_MEETINGS;
    }

    public void setENABLE_MEETINGS(byte ENABLE_MEETINGS) {
        this.ENABLE_MEETINGS = ENABLE_MEETINGS;
    }

    public byte getENABLE_TASK() {
        return ENABLE_TASK;
    }

    public void setENABLE_TASK(byte ENABLE_TASK) {
        this.ENABLE_TASK = ENABLE_TASK;
    }

    public byte getENABLE_KNOWLEDGE() {
        return ENABLE_KNOWLEDGE;
    }

    public void setENABLE_KNOWLEDGE(byte ENABLE_KNOWLEDGE) {
        this.ENABLE_KNOWLEDGE = ENABLE_KNOWLEDGE;
    }

    public byte getENABLE_ANNOUNCEMENT() {
        return ENABLE_ANNOUNCEMENT;
    }

    public void setENABLE_ANNOUNCEMENT(byte ENABLE_ANNOUNCEMENT) {
        this.ENABLE_ANNOUNCEMENT = ENABLE_ANNOUNCEMENT;
    }

    public byte getENABLE_EMAIL() {
        return ENABLE_EMAIL;
    }

    public void setENABLE_EMAIL(byte ENABLE_EMAIL) {
        this.ENABLE_EMAIL = ENABLE_EMAIL;
    }

    public byte getENABLE_DRIVE() {
        return ENABLE_DRIVE;
    }

    public void setENABLE_DRIVE(byte ENABLE_DRIVE) {
        this.ENABLE_DRIVE = ENABLE_DRIVE;
    }

    public byte getENABLE_SKILL() {
        return ENABLE_SKILL;
    }

    public void setENABLE_SKILL(byte ENABLE_SKILL) {
        this.ENABLE_SKILL = ENABLE_SKILL;
    }

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

