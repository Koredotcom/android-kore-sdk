package com.kore.ai.widgetsdk.applicationcontrol;

import java.util.HashMap;

/**
 * Created by Ramachandra Pradeep on 7/8/2016.
 */
public class OrgSettings {
    private int DISABLE_RECEIVE_ATTACHMENT;

    private int ONLY_PRIVATE_TEAM;
    private HashMap<String,Object> DOMAIN_CONFIG;

    public int getDISABLE_RECEIVE_ATTACHMENT() {
        return DISABLE_RECEIVE_ATTACHMENT;
    }

    public void setDISABLE_RECEIVE_ATTACHMENT(int DISABLE_RECEIVE_ATTACHMENT) {
        this.DISABLE_RECEIVE_ATTACHMENT = DISABLE_RECEIVE_ATTACHMENT;
    }

    public int getONLY_PRIVATE_TEAM() {
        return ONLY_PRIVATE_TEAM;
    }

    public void setONLY_PRIVATE_TEAM(int ONLY_PRIVATE_TEAM) {
        this.ONLY_PRIVATE_TEAM = ONLY_PRIVATE_TEAM;
    }


    public HashMap<String, Object> getDOMAIN_CONFIG() {
        return DOMAIN_CONFIG;
    }

    public void setDOMAIN_CONFIG(HashMap<String, Object> DOMAIN_CONFIG) {
        this.DOMAIN_CONFIG = DOMAIN_CONFIG;
    }
}
