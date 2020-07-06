package com.kore.ai.widgetsdk.models;
import com.google.gson.annotations.SerializedName;

/**
 * Copyright (c) 2014 Kore Inc. All rights reserved.
 */
public class UserInfo {

    public static final String SSO_FACEBOOK = "facebook";
    public static final String SSO_GOOGLE = "google";

    @SerializedName("id")
    private String userId;

    @SerializedName("fName")
    private String firstName;

    @SerializedName("lName")
    private String lastName;

    private String emailId;
    private String phoneNo;
    private String orgID;
    private String img;
    private boolean isDefault;
    private boolean isLoggedOff;
    private long lastSynced;
    private String identities;
    private long muteTime;
    private String ssoType;

    @SerializedName("makeConsentPolicyAccept")
    private boolean makeConsentPolicyAccept;
    private String userAppState;
    private String userAppStateTeamId;
    private String orgLogoUrl;
    private String enrollType;
    private int accountBadgeCount;

    public void setIdentities(String ident) {
        this.identities = ident;
    }

    public String getIdentities() {
        return this.identities;
    }

    public String getUserId() {
        return userId;
    }

    public String getFirstName() {
        return firstName == null ? "" : firstName;
    }

    public String getLastName() {
        return lastName == null ? "" : lastName;
    }

    public String getEmailId() {
        if (emailId != null) { //Return emailId in case of loggedIn using emailId
            return emailId;
        } else {//Return phoneNo in case of loggedIn using phoneNumber
            return phoneNo;
        }
    }

    public String getPhoneNumber() {
        if (phoneNo != null) { //Return Phone number
            return phoneNo;
        } else {
            return null;
        }
    }


    public String getOrgID() {
        return orgID;
    }

    public String getUserImage() {
        return (img == null || img.equals("no-avatar")) ? "no-avatar" : img;
    }

    public String getImg() {
        return img;
    }

    /* used in edit profile */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /****/

    public void setImg(String img) {
        this.img = img;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean isDefault) {
        this.isDefault = isDefault;
    }

    public boolean isLoggedOff() {
        return isLoggedOff;
    }

    public void setIsLoggedOff(boolean isLoggedOff) {
        this.isLoggedOff = isLoggedOff;
    }

    public long getLastSynced() {
        return lastSynced;
    }

    public void setLastSynced(long lastSynced) {
        this.lastSynced = lastSynced;
    }

    /**
     * @return the muteTime
     */
    public long getMuteTime() {
        return muteTime;
    }

    /**
     * @param muteTime the muteTime to set
     */
    public void setMuteTime(long muteTime) {
        this.muteTime = muteTime;
    }

    public void processForPersistance() {
        if (emailId == null) {
//            emailId = phoneNo;
        }

        if (firstName == null) firstName = "";
        if (lastName == null) lastName = "";
    }

    public String getSsoType() {
        return ssoType;
    }

    public void setSsoType(String ssoType) {
        this.ssoType = ssoType;
    }

    public boolean isMakeConsentPolicyAccept() {
        return makeConsentPolicyAccept;
    }

    public void setMakeConsentPolicyAccept(boolean makeConsentPolicyAccept) {
        this.makeConsentPolicyAccept = makeConsentPolicyAccept;
    }

    public String getUserAppState() {
        return userAppState;
    }

    public void setUserAppState(String userAppState) {
        this.userAppState = userAppState;
    }

    public String getUserAppStateTeamId() {
        return userAppStateTeamId;
    }

    public void setUserAppStateTeamId(String userAppStateTeamId) {
        this.userAppStateTeamId = userAppStateTeamId;
    }

    public String getOrgLogoUrl() {
        return orgLogoUrl;
    }

    public void setOrgLogoUrl(String orgLogoUrl) {
        this.orgLogoUrl = orgLogoUrl;
    }

    public String getEnrollType() {
        return enrollType;
    }

    public int getAccountBadgeCount() {
        return accountBadgeCount;
    }

    public void setAccountBadgeCount(int accountBadgeCount) {
        this.accountBadgeCount = accountBadgeCount;
    }

    public String getDisplayName() {
        return getFirstName() + " " + getLastName();
    }
}
