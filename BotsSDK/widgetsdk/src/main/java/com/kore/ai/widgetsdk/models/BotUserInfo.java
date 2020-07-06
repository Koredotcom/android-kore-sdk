package com.kore.ai.widgetsdk.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ramachandra Pradeep on 6/1/2016.
 * Copyright (c) 2014 Kore Inc. All rights reserved.
 */
public class BotUserInfo {
    private String userId;
    private String enrollType;
    private List<Object> roles = new ArrayList<Object>();
    private PwdChangeRequire pwdChangeRequire;
    private Boolean makeConsentPolicyAccept;
    private Boolean makeBTConsentPolicyAccept;
    private String sourceType;
    private String img;
    private String orgID;
    private String emailId;
    private String profImage;
    private String profColour;
    private Boolean isFirstTimeLogin;

    /**
     * @return
     * The enrollType
     */
    public String getEnrollType() {
        return enrollType;
    }

    /**
     * @param enrollType
     * The enrollType
     */
    public void setEnrollType(String enrollType) {
        this.enrollType = enrollType;
    }

    /**
     * @return
     * The roles
     */
    public List<Object> getRoles() {
        return roles;
    }

    /**
     * @param roles
     * The roles
     */
    public void setRoles(List<Object> roles) {
        this.roles = roles;
    }

    /**
     * @return
     * The pwdChangeRequire
     */
    public PwdChangeRequire getPwdChangeRequire() {
        return pwdChangeRequire;
    }

    /**
     * @param pwdChangeRequire
     * The pwdChangeRequire
     */
    public void setPwdChangeRequire(PwdChangeRequire pwdChangeRequire) {
        this.pwdChangeRequire = pwdChangeRequire;
    }

    /**
     * @return
     * The makeConsentPolicyAccept
     */
    public Boolean getMakeConsentPolicyAccept() {
        return makeConsentPolicyAccept;
    }

    /**
     * @param makeConsentPolicyAccept
     * The makeConsentPolicyAccept
     */
    public void setMakeConsentPolicyAccept(Boolean makeConsentPolicyAccept) {
        this.makeConsentPolicyAccept = makeConsentPolicyAccept;
    }

    /**
     * @return
     * The makeBTConsentPolicyAccept
     */
    public Boolean getMakeBTConsentPolicyAccept() {
        return makeBTConsentPolicyAccept;
    }

    /**
     * @param makeBTConsentPolicyAccept
     * The makeBTConsentPolicyAccept
     */
    public void setMakeBTConsentPolicyAccept(Boolean makeBTConsentPolicyAccept) {
        this.makeBTConsentPolicyAccept = makeBTConsentPolicyAccept;
    }

    /**
     * @return
     * The sourceType
     */
    public String getSourceType() {
        return sourceType;
    }

    /**
     * @param sourceType
     * The sourceType
     */
    public void setSourceType(String sourceType) {
        this.sourceType = sourceType;
    }

    /**
     * @return
     * The img
     */
    public String getImg() {
        return img;
    }

    /**
     * @param img
     * The img
     */
    public void setImg(String img) {
        this.img = img;
    }

    /**
     * @return
     * The orgID
     */
    public String getOrgID() {
        return orgID;
    }

    /**
     * @param orgID
     * The orgID
     */
    public void setOrgID(String orgID) {
        this.orgID = orgID;
    }

    /**
     * @return
     * The emailId
     */
    public String getEmailId() {
        return emailId;
    }

    /**
     * @param emailId
     * The emailId
     */
    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    /**
     * @return
     * The profImage
     */
    public String getProfImage() {
        return profImage;
    }

    /**
     * @param profImage
     * The profImage
     */
    public void setProfImage(String profImage) {
        this.profImage = profImage;
    }

    /**
     * @return
     * The profColour
     */
    public String getProfColour() {
        return profColour;
    }

    /**
     * @param profColour
     * The profColour
     */
    public void setProfColour(String profColour) {
        this.profColour = profColour;
    }

    /**
     * @return
     * The isFirstTimeLogin
     */
    public Boolean getIsFirstTimeLogin() {
        return isFirstTimeLogin;
    }

    /**
     *
     * @param isFirstTimeLogin
     * The isFirstTimeLogin
     */
    public void setIsFirstTimeLogin(Boolean isFirstTimeLogin) {
        this.isFirstTimeLogin = isFirstTimeLogin;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
