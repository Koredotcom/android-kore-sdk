package com.kore.ai.widgetsdk.models;

import com.kore.ai.widgetsdk.utils.StringUtils;
import com.kore.ai.widgetsdk.utils.Utils;

/**
 * Created by Ramachandra Pradeep on 01-Apr-19.
 */

public class TaskAssignee {
    public String getlN() {
        return lN;
    }

    public void setlN(String lN) {
        this.lN = lN;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFN() {
        return fN;
    }
    public String getNameInFirstNameFormat(){
//        if(!Utils.isNullOrEmpty(fN) && !Utils.isNullOrEmpty(lN)){
//
//            return StringUtils.capitalize( fN+ " "+ lN.charAt(0));
//        }else if(!Utils.isNullOrEmpty(fN)){
//            return StringUtils.capitalize(fN);
//        }else{
            return lN;
//        }
    }
    public void setFN(String fN) {
        this.fN = fN;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public long getcOn() {
        return cOn;
    }

    public void setcOn(long cOn) {
        this.cOn = cOn;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public long getlMod() {
        return lMod;
    }

    public void setlMod(long lMod) {
        this.lMod = lMod;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getOrg() {
        return org;
    }

    public void setOrg(String org) {
        this.org = org;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    private String lN;
    private String label;
    private String id;
    private String name;
    private String fN;
    private String _id;

    private long cOn;
    private String role;
    private long lMod;
    private String color;
    private String org;
    private boolean verified;

    private String emailId;
    private String userId;
    private String orgId;
    private String status;

}
